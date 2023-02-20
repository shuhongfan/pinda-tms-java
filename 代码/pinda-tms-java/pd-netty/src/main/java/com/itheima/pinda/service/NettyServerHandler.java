package com.itheima.pinda.service;

import com.alibaba.fastjson.JSON;
import com.itheima.pinda.entity.LocationEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import java.io.UnsupportedEncodingException;

/**
 * netty 业务处理
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("ServerHandler.channelRead()");
        ByteBuf in = (ByteBuf) msg;
        try {
            //接收报文
            String body = getRequestBody(in);
            log.info("报文内容:{}", body);

            //解析报文
            String message = parseMessage(body);
            if (StringUtils.isBlank(message)) {
                log.info("报文解析失败");
                return;
            }

            //发送至kafka队列
            KafkaSender.send(KafkaSender.MSG_TOPIC, message);

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            //使用完ByteBuf之后，需要主动去释放资源，否则，资源一直在内存中加载，容易造成内存泄漏
            ReferenceCountUtil.release(msg);
        }
        if (null != in) {
            //把当前的写指针 writerIndex 恢复到之前保存的 markedWriterIndex值
            in.resetWriterIndex();
        }
    }

    /**
     * 解析请求内容
     *
     * @param in
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getRequestBody(ByteBuf in) throws UnsupportedEncodingException {
        if (in.readableBytes() <= 0) {
            return null;
        }
        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        return new String(req, "UTF-8");
    }

    /**
     * 解析报文
     * <p>
     * 设备不同报文也不同，本次设备为移动端，直接使用json格式传输
     */
    private String parseMessage(String body) {
        if (StringUtils.isBlank(body)) {
            log.warn("报文为空");
            return null;
        }
        body = body.trim();
        // 其它格式的报文需要解析后放入MessageEntity实体
        LocationEntity message = JSON.parseObject(body, LocationEntity.class);
        if (message == null || StringUtils.isBlank(message.getType()) || StringUtils.isBlank(message.getBusinessId()) || StringUtils.isBlank(message.getLat()) || StringUtils.isBlank(message.getLng()) || StringUtils.isBlank(message.getId())) {
            log.warn("报文内容异常");
            return null;
        }

        String result = JSON.toJSONString(message);
        return result;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 关闭发生异常的连接
        ctx.close();
    }
}