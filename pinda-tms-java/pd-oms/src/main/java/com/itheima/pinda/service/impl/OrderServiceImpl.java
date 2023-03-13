package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.DTO.OrderDTO;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.common.utils.BaiduMapUtils;
import com.itheima.pinda.common.utils.CustomIdGenerator;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.entity.fact.AddressCheckResult;
import com.itheima.pinda.entity.fact.AddressRule;
import com.itheima.pinda.enums.OrderPaymentStatus;
import com.itheima.pinda.enums.OrderPickupType;
import com.itheima.pinda.enums.OrderStatus;
import com.itheima.pinda.mapper.OrderMapper;
import com.itheima.pinda.service.IOrderService;
import org.apache.commons.lang.StringUtils;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Autowired
    private KieContainer kieContainer;

    @Override
    public Order saveOrder(Order order) {
        order.setId(idGenerator.nextId(order) + "");
        order.setCreateTime(LocalDateTime.now());
        order.setPaymentStatus(OrderPaymentStatus.UNPAID.getStatus());
        if (OrderPickupType.NO_PICKUP.getCode() == order.getPickupType()) {
            order.setStatus(OrderStatus.OUTLETS_SINCE_SENT.getCode());
        } else {
            order.setStatus(OrderStatus.PENDING.getCode());
        }
        save(order);
        return order;
    }

    @Override
    public IPage<Order> findByPage(Integer page, Integer pageSize, Order order) {
        Page<Order> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(order.getId())) {
            lambdaQueryWrapper.like(Order::getId, order.getId());
        }
        if (order.getStatus() != null) {
            lambdaQueryWrapper.eq(Order::getStatus, order.getStatus());
        }
        if (order.getPaymentStatus() != null) {
            lambdaQueryWrapper.eq(Order::getPaymentStatus, order.getPaymentStatus());
        }
        //发件人信息
        if (StringUtils.isNotEmpty(order.getSenderName())) {
            lambdaQueryWrapper.like(Order::getSenderName, order.getSenderName());
        }
        if (StringUtils.isNotEmpty(order.getSenderPhone())) {
            lambdaQueryWrapper.like(Order::getSenderPhone, order.getSenderPhone());
        }
        if (StringUtils.isNotEmpty(order.getSenderProvinceId())) {
            lambdaQueryWrapper.eq(Order::getSenderProvinceId, order.getSenderProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCityId())) {
            lambdaQueryWrapper.eq(Order::getSenderCityId, order.getSenderCityId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCountyId())) {
            lambdaQueryWrapper.eq(Order::getSenderCountyId, order.getSenderCountyId());
        }
        //收件人信息
        if (StringUtils.isNotEmpty(order.getReceiverName())) {
            lambdaQueryWrapper.like(Order::getReceiverName, order.getReceiverName());
        }
        if (StringUtils.isNotEmpty(order.getReceiverPhone())) {
            lambdaQueryWrapper.like(Order::getReceiverPhone, order.getReceiverPhone());
        }
        if (StringUtils.isNotEmpty(order.getReceiverProvinceId())) {
            lambdaQueryWrapper.eq(Order::getReceiverProvinceId, order.getReceiverProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCityId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCityId, order.getReceiverCityId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCountyId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCountyId, order.getReceiverCountyId());
        }
        lambdaQueryWrapper.orderBy(true, false, Order::getId);
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<Order> findAll(List<String> ids) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(Order::getId, ids);
        }
        lambdaQueryWrapper.orderBy(true, false, Order::getId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO) {

        Integer page = orderSearchDTO.getPage();
        Integer pageSize = orderSearchDTO.getPageSize();

        IPage<Order> ipage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Order> orderQueryWrapper = new LambdaQueryWrapper<>();
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getId()), Order::getId, orderSearchDTO.getId());
        orderQueryWrapper.like(StringUtils.isNotEmpty(orderSearchDTO.getKeyword()), Order::getId, orderSearchDTO.getKeyword());
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getMemberId()), Order::getMemberId, orderSearchDTO.getMemberId());
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getReceiverPhone()), Order::getReceiverPhone, orderSearchDTO.getReceiverPhone());
        orderQueryWrapper.orderByDesc(Order::getCreateTime);
        return page(ipage, orderQueryWrapper);
    }


    /**
     * 计算订单价格
     * @param orderDTO
     * @return
     */
    public Map calculateAmount(OrderDTO orderDTO) {
        orderDTO = getDistance(orderDTO);

        if("sender error msg".equals(orderDTO.getSenderAddress()) || "receiver error msg".equals(orderDTO.getReceiverAddress())){
            //地址解析失败，直接返回
            Map map = new HashMap();
            map.put("amount","0");
            map.put("errorMsg","无法计算订单距离和订单价格，请输入真实地址");
            map.put("orderDto",orderDTO);
            return map;
        }

//        KieSession kieSession = kieContainer.newKieSession();
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

//        设置Fact对象
        AddressRule addressRule = new AddressRule();
        addressRule.setTotalWeight(orderDTO.getOrderCargoDto().getTotalWeight().doubleValue());
        addressRule.setDistance(orderDTO.getDistance().doubleValue());

//        将对象加入到工作内存
        kieSession.insert(addressRule);

        AddressCheckResult addressCheckResult = new AddressCheckResult();
        kieSession.insert(addressCheckResult);

        int result = kieSession.fireAllRules();
        System.out.println("触发了" + result + "条规则");
        kieSession.destroy();

        if (addressCheckResult.isPostCodeResult()) {
            System.out.println("规则匹配成功,订单价格为:" + addressCheckResult.getResult());
            orderDTO.setAmount(new BigDecimal(addressCheckResult.getResult()));

            HashMap<String, Object> map = new HashMap<>();
            map.put("orderDto",orderDTO);
            map.put("amount",addressCheckResult.getResult());
            return map;
        }
        return null;
    }

    /**
     * 调用百度地图服务接口，根据寄件人地址和收件人地址计算订单距离
     * @param orderDTO
     * @return
     */
    public OrderDTO getDistance(OrderDTO orderDTO){
//        调用百度地图服务接口获取寄件人地址对应的坐标经纬度
        String begin = BaiduMapUtils.getCoordinate(orderDTO.getSenderAddress());
        if (begin == null) {
            orderDTO.setSenderAddress("sender error msg");
            return orderDTO;
        }

//        调用百度地图服务接口获取收件人地址对应的坐标经纬度
        String end = BaiduMapUtils.getCoordinate(orderDTO.getReceiverAddress());
        if (end == null) {
            orderDTO.setReceiverAddress("receiver error msg");
            return orderDTO;
        }

        Double distance = BaiduMapUtils.getDistance(begin, end);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.format(distance / 1000);

        orderDTO.setDistance(new BigDecimal(distance));

        return orderDTO;
    }
}
