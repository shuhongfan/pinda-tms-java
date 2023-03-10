package com.itheima.pinda.vo.base.userCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "消息信息")
public class MessageVo implements Serializable {
    private static final long serialVersionUID = 6933377966443529574L;
    @ApiModelProperty(value = "消息id")
    private String id;
    @ApiModelProperty(value = "消息标题")
    private String title;
    @ApiModelProperty(value = "消息正文")
    private String content;
    @ApiModelProperty(value = "消息创建时间 格式：yyyy-MM-dd HH:mm:ss")
    private String createTime;
    @ApiModelProperty(value = "消息类型：notice为通知,bulletin为公告")
    private String messageType;
    @ApiModelProperty(value = "消息状态：0是已读，1是未读")
    private Integer status;
}
