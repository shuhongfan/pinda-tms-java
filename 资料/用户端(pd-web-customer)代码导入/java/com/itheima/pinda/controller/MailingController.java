package com.itheima.pinda.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itheima.pinda.DTO.*;
import com.itheima.pinda.DTO.angency.AgencyScopeDto;
import com.itheima.pinda.DTO.user.CourierScopeDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.context.RequestContext;
import com.itheima.pinda.common.utils.DateUtils;
import com.itheima.pinda.common.utils.EntCoordSyncJob;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.AddressBook;
import com.itheima.pinda.entity.Member;
import com.itheima.pinda.enums.OrderPaymentStatus;
import com.itheima.pinda.enums.OrderStatus;
import com.itheima.pinda.enums.OrderType;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskAssignedStatus;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskStatus;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskType;
import com.itheima.pinda.feign.*;
import com.itheima.pinda.feign.agency.AgencyScopeFeign;
import com.itheima.pinda.feign.user.CourierScopeFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import com.itheima.pinda.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * ??????  ???????????????
 * </p>
 *
 * @author diesel
 * @since 2020-3-30
 */
@Log4j2
@Api(tags = "????????????")
@RestController
@RequestMapping("mailing")
public class MailingController {
    private final AgencyScopeFeign agencyScopeFeign;
    private final OrgApi orgApi;
    private final TransportTaskFeign transportTaskFeign;
    private final TransportOrderFeign transportOrderFeign;
    private final UserApi userApi;
    private final AreaApi areaApi;
    private final PickupDispatchTaskFeign pickupDispatchTaskFeign;
    private final OrderFeign orderFeign;
    private final CargoFeign cargoFeign;
    private final IMemberService memberService;
    private final AddressBookFeign addressBookFeign;
    private final CourierScopeFeign courierScopeFeign;

    public MailingController(AgencyScopeFeign agencyScopeFeign, OrgApi orgApi, TransportTaskFeign transportTaskFeign, TransportOrderFeign transportOrderFeign, UserApi userApi, AreaApi areaApi, PickupDispatchTaskFeign pickupDispatchTaskFeign, OrderFeign orderFeign, CargoFeign cargoFeign, IMemberService memberService, AddressBookFeign addressBookFeign, CourierScopeFeign courierScopeFeign) {
        this.agencyScopeFeign = agencyScopeFeign;
        this.orgApi = orgApi;
        this.transportTaskFeign = transportTaskFeign;
        this.transportOrderFeign = transportOrderFeign;
        this.userApi = userApi;
        this.areaApi = areaApi;
        this.pickupDispatchTaskFeign = pickupDispatchTaskFeign;
        this.orderFeign = orderFeign;
        this.cargoFeign = cargoFeign;
        this.memberService = memberService;
        this.addressBookFeign = addressBookFeign;
        this.courierScopeFeign = courierScopeFeign;
    }

    private OrderDTO buildOrderAndPrice(MailingSaveDTO entity) {
        // ????????????????????????
        AddressBook sendAddress = addressBookFeign.detail(entity.getSendAddress());
        AddressBook receiptAddress = addressBookFeign.detail(entity.getReceiptAddress());
        log.info("sendAddress:{},{} receiptAddress:{},{}", entity.getSendAddress(), sendAddress, entity.getReceiptAddress(), receiptAddress);
        OrderDTO orderAddDto = new OrderDTO();
        orderAddDto.setSenderName(sendAddress.getName());
        orderAddDto.setSenderPhone(sendAddress.getPhoneNumber());
        orderAddDto.setSenderProvinceId(sendAddress.getProvinceId().toString());
        orderAddDto.setSenderCityId(sendAddress.getCityId().toString());
        orderAddDto.setSenderCountyId(sendAddress.getCountyId().toString());
        orderAddDto.setSenderAddress(sendAddress.getAddress());
        orderAddDto.setSenderAddressId(entity.getSendAddress());

        orderAddDto.setReceiverName(receiptAddress.getName());
        orderAddDto.setReceiverPhone(receiptAddress.getPhoneNumber());
        orderAddDto.setReceiverProvinceId(receiptAddress.getProvinceId().toString());
        orderAddDto.setReceiverCityId(receiptAddress.getCityId().toString());
        orderAddDto.setReceiverCountyId(receiptAddress.getCountyId().toString());
        orderAddDto.setReceiverAddress(receiptAddress.getAddress());
        orderAddDto.setReceiverAddressId(entity.getReceiptAddress());

        orderAddDto.setPaymentMethod(entity.getPayMethod());
        orderAddDto.setPaymentStatus(1); // ???????????????

        orderAddDto.setOrderType(orderAddDto.getReceiverCityId().equals(orderAddDto.getSenderCityId()) ? OrderType.INCITY.getCode() : OrderType.OUTCITY.getCode());
        orderAddDto.setPickupType(entity.getPickupType());
        Map map = orderFeign.getOrderMsg(orderAddDto);
        orderAddDto.setAmount(new BigDecimal(map.getOrDefault("amount", "23").toString()));
        //??????????????????????????????
//        orderAddDto.setAmount(new BigDecimal("23"));
        return orderAddDto;
    }

    private OrderCargoDto buildOrderCargo(MailingSaveDTO entity) {
        OrderCargoDto cargoDto = new OrderCargoDto();
        cargoDto.setName(entity.getGoodsName());
        cargoDto.setGoodsTypeId(entity.getGoodsType());
        cargoDto.setWeight(new BigDecimal(entity.getGoodsWeight()));
        cargoDto.setQuantity(1);
        cargoDto.setTotalWeight(cargoDto.getWeight().multiply(new BigDecimal(cargoDto.getQuantity())));
        return cargoDto;
    }

    /**
     * ????????????
     *
     * @param entity
     * @return
     */
    @ApiOperation("????????????")
    @PostMapping("totalPrice")
    public Result totalPrice(@RequestBody MailingSaveDTO entity) {
        log.info("???????????????{}", entity);
        OrderDTO orderAddDto = buildOrderAndPrice(entity);
        log.info("?????????{}", orderAddDto);
        Result result = Result.ok().put("amount", orderAddDto.getAmount().toString());
        log.info("???????????????{}", result);
        return result;
    }

    /**
     * ??????
     *
     * @param entity
     * @return
     */
    @ApiOperation("??????")
    @PostMapping("")
    public Result save(@RequestBody MailingSaveDTO entity) {
        log.info("?????????{}", entity);
        //??????userid
        String userId = RequestContext.getUserId();

        // ????????????????????????
        OrderDTO orderDTO = buildOrderAndPrice(entity);
        orderDTO.setMemberId(userId);
        log.info("?????????????????????{}", orderDTO);

        // ???????????????????????????????????????????????????????????????
        Result result = getAgencyId(orderDTO);
        if (!result.get("code").toString().equals("0")) {
            return result;
        }
        String agencyId = result.get("agencyId").toString();
        orderDTO.setCurrentAgencyId(agencyId);
        // ??????????????????????????????????????????id??????????????????????????????????????????????????????
        String sendLocation = result.get("location").toString();
        Result resultCourier = getCourierId(sendLocation, orderDTO);

        if (!resultCourier.get("code").toString().equals("0")) {
            return resultCourier;
        }
        String courierId = resultCourier.get("userId").toString();

        //????????????????????????????????????????????????
        Result resultReceive = getReceiveAgencyId(orderDTO);
        if (!resultReceive.get("code").toString().equals("0")) {
            return resultReceive;
        }
        String receiveAgencyId = resultReceive.get("agencyId").toString();
        String receiveAgentLocation = resultReceive.get("location").toString();

        OrderCargoDto cargoDto = buildOrderCargo(entity);
        orderDTO.setOrderCargoDto(cargoDto);
        orderDTO = orderFeign.save(orderDTO);
        //????????????????????????
        OrderLocationDto orderLocationDto = new OrderLocationDto();
        orderLocationDto.setOrderId(orderDTO.getId());
        orderLocationDto.setSendLocation(sendLocation);
        orderLocationDto.setSendAgentId(agencyId);
        orderLocationDto.setReceiveLocation(receiveAgentLocation);
        orderLocationDto.setReceiveAgentId(receiveAgencyId);
        orderFeign.saveOrUpdateLoccation(orderLocationDto);

        if ("send error msg".equals(orderDTO.getSenderAddress())) {
            return Result.error("?????????????????????,???????????????????????????????????????");
        }
        if ("receive error msg".equals(orderDTO.getReceiverAddress())) {
            return Result.error("?????????????????????,???????????????????????????????????????");
        }

        if (orderDTO.getId() != null) {
            cargoDto.setOrderId(orderDTO.getId());
            cargoFeign.save(cargoDto);
        }

        log.info("??????????????????:{},?????????:{}", agencyId, courierId);

        TaskPickupDispatchDTO pickupDispatchTaskDTO = new TaskPickupDispatchDTO();
        pickupDispatchTaskDTO.setOrderId(orderDTO.getId());
        pickupDispatchTaskDTO.setTaskType(PickupDispatchTaskType.PICKUP.getCode());
        pickupDispatchTaskDTO.setStatus(PickupDispatchTaskStatus.PENDING.getCode());
        pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
        pickupDispatchTaskDTO.setCreateTime(LocalDateTime.now());
        pickupDispatchTaskDTO.setAgencyId(agencyId);
        pickupDispatchTaskDTO.setCourierId(courierId);
        pickupDispatchTaskDTO.setAgencyId(agencyId);
        String[] pickupTimes = entity.getPickUpTime().split("-");
        String[] pickupTimeStrs = pickupTimes[1].split(":");
        LocalDateTime pickupDateTime = LocalDateTime.now().withHour(Integer.parseInt(pickupTimeStrs[0])).withMinute(Integer.parseInt(pickupTimeStrs[1])).withSecond(00);
        pickupDispatchTaskDTO.setEstimatedStartTime(LocalDateTime.now());
        pickupDispatchTaskDTO.setEstimatedEndTime(DateUtils.getUTCTime(pickupDateTime));
        if (StringUtils.isNotBlank(courierId)) {
            pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.DISTRIBUTED.getCode());
        } else {
            pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.MANUAL_DISTRIBUTED.getCode());
        }

        pickupDispatchTaskFeign.save(pickupDispatchTaskDTO);
        return Result.ok().put("amount", orderDTO.getAmount());
    }

    private Result getReceiveAgencyId(OrderDTO orderDTO) {
        String address = receiverFullAddress(orderDTO);
        if (StringUtils.isBlank(address)) {
            return Result.error("?????????????????????????????????");
        }
        String location = EntCoordSyncJob.getCoordinate(address);
        log.info("???????????????????????????-->" + address + "--" + location);
        if (StringUtils.isBlank(location)) {
            return Result.error("?????????????????????????????????");
        }
        //????????????????????????????????????????????????
        Map map = EntCoordSyncJob.getLocationByPosition(location);
        if (ObjectUtils.isEmpty(map)) {
            return Result.error("????????????????????????????????????");
        }
        String adcode = map.getOrDefault("adcode", "").toString();
        R<Area> r = areaApi.getByCode(adcode + "000000");
        if (!r.getIsSuccess()) {
            Result.error(r.getMsg());
        }
        Area area = r.getData();
        if (area == null) {
            return Result.error("????????????:" + adcode + "?????????????????????????????????");
        }
        Long areaId = area.getId();
        if (!orderDTO.getReceiverCountyId().equals(String.valueOf(areaId))) {
            return Result.error("?????????????????????id??????????????????????????????id????????????????????????");
        }
        List<AgencyScopeDto> agencyScopes = agencyScopeFeign.findAllAgencyScope(areaId + "", null, null, null);
        if (agencyScopes == null || agencyScopes.size() == 0) {
            return Result.error("?????????????????????????????????????????????????????????");
        }
        Result res = calcuate(location, agencyScopes);
        if (!res.get("code").toString().equals("0")) {
            return res;
        }
        Result result = new Result();
        result.put("agencyId", res.get("agencyId").toString());
        result.put("location", location);
        return result;
    }

    @SneakyThrows
    private String receiverFullAddress(OrderDTO orderDTO) {
        StringBuffer stringBuffer = new StringBuffer();

        Long province = Long.valueOf(orderDTO.getReceiverProvinceId());
        Long city = Long.valueOf(orderDTO.getReceiverCityId());
        Long county = Long.valueOf(orderDTO.getReceiverCountyId());

        Set areaIdSet = new HashSet();
        areaIdSet.add(province);
        areaIdSet.add(city);
        areaIdSet.add(county);

        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, areaIdSet);
        Map<Long, Area> areaMap = areaMapFuture.get();

        stringBuffer.append(areaMap.get(province).getName());
        stringBuffer.append(areaMap.get(city).getName());
        stringBuffer.append(areaMap.get(county).getName());
        stringBuffer.append(orderDTO.getReceiverAddress());

        return stringBuffer.toString();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param orderDTO
     * @return
     */
    private Result getCourierId(String location, OrderDTO orderDTO) {
        List<CourierScopeDto> courierScopeDtoList = courierScopeFeign.findAllCourierScope(orderDTO.getSenderCountyId(), null);
        if (courierScopeDtoList == null || courierScopeDtoList.size() == 0) {
            return Result.error("?????????????????????????????????????????????????????????????????????");
        }

        Result res = calcuateCourier(location, courierScopeDtoList);
        if (!res.get("code").toString().equals("0")) {
            return res;
        }
        Result result = new Result();
        result.put("userId", res.get("userId").toString());
        return result;

    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param location
     * @param courierScopeDtoList
     * @return
     */
    private Result calcuateCourier(String location, List<CourierScopeDto> courierScopeDtoList) {
        log.info("??????????????????????????????????????????:{}  {}", location, JSONArray.toJSONString(courierScopeDtoList));
        try {
            Map courierMap = Maps.newHashMap();
            for (CourierScopeDto courierScopeDto : courierScopeDtoList) {
                List<List<Map>> mutiPoints = courierScopeDto.getMutiPoints();
                for (List<Map> list : mutiPoints) {
                    String[] originArray = location.split(",");
                    boolean flag = EntCoordSyncJob.isInScope(list, Double.parseDouble(originArray[0]), Double.parseDouble(originArray[1]));
                    if (flag) {
                        log.info("????????????????????????????????????:{}  {}", courierScopeDto.getUserId());
                        return Result.ok().put("userId", courierScopeDto.getUserId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("???????????????????????????", e);
            return Result.error(5000, "???????????????????????????");
        }
        return Result.error(5000, "???????????????????????????");
    }

    /**
     * ????????????????????????
     *
     * @param orderDTO
     * @return
     */
    private Result getAgencyId(OrderDTO orderDTO) {
        String address = senderFullAddress(orderDTO);
        if (StringUtils.isBlank(address)) {
            return Result.error("?????????????????????????????????");
        }
        String location = EntCoordSyncJob.getCoordinate(address);
        log.info("???????????????????????????-->" + address + "--" + location);
        if (StringUtils.isBlank(location)) {
            return Result.error("?????????????????????????????????");
        }
        //????????????????????????????????????????????????
        Map map = EntCoordSyncJob.getLocationByPosition(location);
        if (ObjectUtils.isEmpty(map)) {
            return Result.error("????????????????????????????????????");
        }
        String adcode = map.getOrDefault("adcode", "").toString();
        R<Area> r = areaApi.getByCode(adcode + "000000");
        if (!r.getIsSuccess()) {
            Result.error(r.getMsg());
        }
        Area area = r.getData();
        if (area == null) {
            return Result.error("????????????:" + adcode + "?????????????????????????????????");
        }
        Long areaId = area.getId();
        if (!orderDTO.getSenderCountyId().equals(String.valueOf(areaId))) {
            log.info("???????????????id??????????????????????????????id??????,??????????????????{},{}", orderDTO.getSenderCountyId(), areaId);
            return Result.error("???????????????id??????????????????????????????id????????????????????????");
        }
        List<AgencyScopeDto> agencyScopes = agencyScopeFeign.findAllAgencyScope(areaId + "", null, null, null);
        if (agencyScopes == null || agencyScopes.size() == 0) {
            return Result.error("?????????????????????????????????????????????????????????");
        }
        Result res = calcuate(location, agencyScopes);
        if (!res.get("code").toString().equals("0")) {
            return res;
        }
        Result result = new Result();
        result.put("agencyId", res.get("agencyId").toString());
        result.put("location", location);
        return result;
    }

    @SneakyThrows
    private String senderFullAddress(OrderDTO orderDTO) {
        StringBuffer stringBuffer = new StringBuffer();

        Long province = Long.valueOf(orderDTO.getSenderProvinceId());
        Long city = Long.valueOf(orderDTO.getSenderCityId());
        Long county = Long.valueOf(orderDTO.getSenderCountyId());

        Set areaIdSet = new HashSet();
        areaIdSet.add(province);
        areaIdSet.add(city);
        areaIdSet.add(county);

        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, areaIdSet);
        Map<Long, Area> areaMap = areaMapFuture.get();

        stringBuffer.append(areaMap.get(province).getName());
        stringBuffer.append(areaMap.get(city).getName());
        stringBuffer.append(areaMap.get(county).getName());
        stringBuffer.append(orderDTO.getSenderAddress());

        return stringBuffer.toString();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param location
     * @param agencyScopes
     * @return
     */
    private Result calcuate(String location, List<AgencyScopeDto> agencyScopes) {
        log.info("???????????????????????????????????????:{}  {}", location, JSONArray.toJSONString(agencyScopes));
        try {
            for (AgencyScopeDto agencyScopeDto : agencyScopes) {
                List<List<Map>> mutiPoints = agencyScopeDto.getMutiPoints();
                for (List<Map> list : mutiPoints) {
                    String[] originArray = location.split(",");
                    boolean flag = EntCoordSyncJob.isInScope(list, Double.parseDouble(originArray[0]), Double.parseDouble(originArray[1]));
                    if (flag) {
                        log.info("?????????????????????????????????:{}  {}", agencyScopeDto.getAgencyId());
                        return Result.ok().put("agencyId", agencyScopeDto.getAgencyId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("????????????????????????", e);
            return Result.error(5000, "????????????????????????");
        }
        return Result.error(5000, "????????????????????????");
    }


    /**
     * ?????????
     *
     * @param pointMap
     * @return
     */
    private String getPoint(Map pointMap) {
        String lng = pointMap.getOrDefault("lng", "").toString();
        String lat = pointMap.getOrDefault("lat", "").toString();
        return lng + "," + lat;
    }

    /**
     * ?????? ??????
     *
     * @param entity
     * @return
     */
    @ApiOperation("????????????")
    @PutMapping("/{id}")
    public Result update(@PathVariable("id") String id, @RequestBody MailingSaveDTO entity) {
        log.info("???????????? id:{} params???{}", id, entity);
        //??????userid
        String userId = RequestContext.getUserId();

        OrderDTO order = orderFeign.findById(id);
        log.info("????????? id:{} result???{}", id, order);
        String oldSenderCountId = order.getSenderCountyId();
        String oldSendAdress = order.getSenderAddress();
        // ????????????????????????
        OrderDTO orderDTO = buildOrderAndPrice(entity);
        orderDTO.setMemberId(userId);
        orderDTO.setCreateTime(LocalDateTime.now());
        orderDTO = orderFeign.updateById(order.getId(), orderDTO);
        log.info("????????? id:{} params???{}", id, orderDTO);

        if (orderDTO.getId() != null) {
            List<OrderCargoDto> cargoDtos = cargoFeign.findAll(null, orderDTO.getId());
            log.info("????????????????????? id:{} result???{}", id, cargoDtos);
            for (OrderCargoDto item : cargoDtos) {
                OrderCargoDto cargoDto = buildOrderCargo(entity);
                cargoDto.setOrderId(orderDTO.getId());
                OrderCargoDto resultCargoDto = cargoFeign.update(item.getId(), cargoDto);
                log.info("??????????????????????????? id:{} params???{} result:{}", id, cargoDto, resultCargoDto);
            }
        }

        List<AgencyScopeDto> agencyScope = agencyScopeFeign.findAllAgencyScope(orderDTO.getSenderCountyId(), null, null, null);
//        String agencyId = null;
//        String courierId = null;
//        if (!CollectionUtils.isEmpty(agencyScope)) {
//            agencyId = agencyScope.get(0).getAgencyId();
//            //??????id
//            Long stationId = StaticStation.COURIER_ID;
//            R<List<User>> userRs = userApi.list(null, stationId, null, Long.valueOf(agencyId));
//            if (userRs.getData() != null && userRs.getData().size() > 0) {
//                User user = userRs.getData().get(0);
//                courierId = user.getId().toString();
//            }
//        }
        //?????????????????????????????????????????????
        Result result = getAgencyId(orderDTO);
        if (!result.get("code").toString().equals("0")) {
            return result;
        }
        String agencyId = result.get("agencyId").toString();
        // ??????????????????????????????????????????id??????????????????????????????????????????????????????
        Result resultCourier = getCourierId(result.get("location").toString(), orderDTO);

        if (!resultCourier.get("code").toString().equals("0")) {
            return resultCourier;
        }
        String courierId = resultCourier.get("userId").toString();


        String sendLocation = result.get("location").toString();
        //????????????????????????????????????????????????
        Result resultReceive = getReceiveAgencyId(orderDTO);
        if (!resultReceive.get("code").toString().equals("0")) {
            return resultReceive;
        }
        String receiveAgencyId = resultReceive.get("agencyId").toString();
        String receiveAgentLocation = resultReceive.get("location").toString();
        OrderLocationDto orderLocationDto = orderFeign.selectByOrderId(order.getId());
        //??????????????????
        OrderLocationDto orderLocationDtoUpdate = new OrderLocationDto();
        orderLocationDtoUpdate.setId(orderLocationDto.getId());
        orderLocationDtoUpdate.setOrderId(orderDTO.getId());
        orderLocationDtoUpdate.setSendLocation(sendLocation);
        orderLocationDtoUpdate.setSendAgentId(agencyId);
        orderLocationDtoUpdate.setReceiveLocation(receiveAgentLocation);
        orderLocationDtoUpdate.setReceiveAgentId(receiveAgencyId);
        orderFeign.saveOrUpdateLoccation(orderLocationDtoUpdate);

        TaskPickupDispatchDTO taskPickupDispatchDTO = pickupDispatchTaskFeign.findByOrderId(id, PickupDispatchTaskType.PICKUP.getCode());
        log.info("?????????????????? id:{} result:{}", id, taskPickupDispatchDTO);

//        if (!oldSenderCountId.equals(orderDTO.getSenderCountyId())) {
        if (!oldSendAdress.equals(orderDTO.getSenderAddress())) {
            log.info("????????????????????? ??????????????????????????? id:{}", id);
            if (taskPickupDispatchDTO != null && StringUtils.isNotBlank(taskPickupDispatchDTO.getId())) {
                TaskPickupDispatchDTO taskPickupDispatchEditDTO = new TaskPickupDispatchDTO();
                taskPickupDispatchEditDTO.setStatus(PickupDispatchTaskStatus.CANCELLED.getCode());
                taskPickupDispatchEditDTO.setCancelTime(LocalDateTime.now());
                taskPickupDispatchEditDTO.setMark("???????????????????????????");
                pickupDispatchTaskFeign.updateById(taskPickupDispatchDTO.getId(), taskPickupDispatchEditDTO);
            }

            if (!CollectionUtils.isEmpty(agencyScope)) {
                log.info("??????????????????:{},?????????:{}", agencyId, courierId);
                TaskPickupDispatchDTO pickupDispatchTaskDTO = new TaskPickupDispatchDTO();
                pickupDispatchTaskDTO.setOrderId(order.getId());
                pickupDispatchTaskDTO.setTaskType(PickupDispatchTaskType.PICKUP.getCode());
                pickupDispatchTaskDTO.setStatus(PickupDispatchTaskStatus.PENDING.getCode());
                pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
                pickupDispatchTaskDTO.setCreateTime(LocalDateTime.now());
                pickupDispatchTaskDTO.setAgencyId(agencyId);
                pickupDispatchTaskDTO.setCourierId(courierId);
                pickupDispatchTaskDTO.setAgencyId(agencyId);
                String[] pickupTimes = entity.getPickUpTime().split("-");
                String[] pickupTimeStrs = pickupTimes[1].split(":");
                LocalDateTime pickupDateTime = LocalDateTime.now().withHour(Integer.parseInt(pickupTimeStrs[0])).withMinute(Integer.parseInt(pickupTimeStrs[1])).withSecond(00);
                log.info("?????????????????????{}  {}", pickupDateTime, LocalDateTime.now());
                pickupDispatchTaskDTO.setEstimatedStartTime(LocalDateTime.now());
                pickupDispatchTaskDTO.setEstimatedEndTime(DateUtils.getUTCTime(pickupDateTime));
                if (StringUtils.isNotBlank(courierId)) {
                    pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.DISTRIBUTED.getCode());
                } else {
                    pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.MANUAL_DISTRIBUTED.getCode());
                }

                pickupDispatchTaskFeign.save(pickupDispatchTaskDTO);
            }
        } else {
            log.info("???????????????????????????????????????");

            log.info("????????????????????????:{},?????????:{}", agencyId, courierId);

            TaskPickupDispatchDTO pickupDispatchTaskDTO = new TaskPickupDispatchDTO();
            pickupDispatchTaskDTO.setOrderId(order.getId());
            pickupDispatchTaskDTO.setTaskType(PickupDispatchTaskType.PICKUP.getCode());
            pickupDispatchTaskDTO.setStatus(PickupDispatchTaskStatus.PENDING.getCode());
            pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
            pickupDispatchTaskDTO.setCreateTime(LocalDateTime.now());
            pickupDispatchTaskDTO.setAgencyId(agencyId);
            pickupDispatchTaskDTO.setCourierId(courierId);
            pickupDispatchTaskDTO.setAgencyId(agencyId);
            String[] pickupTimes = entity.getPickUpTime().split("-");
            String[] pickupTimeStrs = pickupTimes[1].split(":");
            LocalDateTime pickupDateTime = LocalDateTime.now().withHour(Integer.parseInt(pickupTimeStrs[0])).withMinute(Integer.parseInt(pickupTimeStrs[1])).withSecond(00);
            pickupDispatchTaskDTO.setEstimatedStartTime(LocalDateTime.now());
            pickupDispatchTaskDTO.setEstimatedEndTime(DateUtils.getUTCTime(pickupDateTime));
            if (StringUtils.isNotBlank(courierId)) {
                pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.DISTRIBUTED.getCode());
            } else {
                pickupDispatchTaskDTO.setAssignedStatus(PickupDispatchTaskAssignedStatus.MANUAL_DISTRIBUTED.getCode());
            }

            pickupDispatchTaskFeign.updateById(taskPickupDispatchDTO.getId(), pickupDispatchTaskDTO);
            log.info("???????????????????????????{} , {}", taskPickupDispatchDTO.getId(), pickupDispatchTaskDTO);
        }

        return Result.ok();
    }

    /**
     * ?????? ??????
     *
     * @return
     */
    @ApiOperation("??????")
    @PutMapping("/pay/{id}")
    public Result pay(@PathVariable("id") String id) {
        try {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setPaymentStatus(OrderPaymentStatus.PAID.getStatus());
            orderFeign.updateById(id, orderDTO);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }


    /**
     * ?????? ??????
     *
     * @return
     */
    @ApiOperation("??????")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable("id") String id) {
        log.info("?????????????????????{}", id);
        // ???????????????????????????????????????????????????????????????????????????
        try {
            log.info("???????????????{}", id);
            // ????????????????????????
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setStatus(OrderStatus.CANCELLED.getCode());
            orderFeign.updateById(id, orderDTO);

            //??????????????????
            OrderLocationDto orderLocationDto = new OrderLocationDto();
            orderLocationDto.setOrderId(id);
            orderFeign.deleteOrderLocation(orderLocationDto);

            TaskPickupDispatchDTO taskPickupDispatchDTO = pickupDispatchTaskFeign.findByOrderId(id, PickupDispatchTaskType.PICKUP.getCode());
            if (taskPickupDispatchDTO != null && StringUtils.isNotBlank(taskPickupDispatchDTO.getId())) {
                TaskPickupDispatchDTO taskPickupDispatchEditDTO = new TaskPickupDispatchDTO();
                taskPickupDispatchEditDTO.setStatus(PickupDispatchTaskStatus.CANCELLED.getCode());
                taskPickupDispatchEditDTO.setCancelTime(LocalDateTime.now());
                taskPickupDispatchEditDTO.setMark("????????????");
                pickupDispatchTaskFeign.updateById(taskPickupDispatchDTO.getId(), taskPickupDispatchEditDTO);
            }
            return Result.ok();
        } catch (Exception e) {
            log.error("????????????????????????", e);
        }
        return Result.error();
    }


    @ApiOperation("?????????????????????")
    @GetMapping("count")
    public Result count(MailingQueryDTO dto) {
        //??????userid
        String userId = RequestContext.getUserId();

        OrderSearchDTO orderSearchDto = new OrderSearchDTO();
        orderSearchDto.setPage(1);
        orderSearchDto.setPageSize(1);
        orderSearchDto.setKeyword(dto.getKeyword());
        if (0 == dto.getMailType()) {
            // ?????????
            orderSearchDto.setMemberId(userId);
        } else {
            // ????????? ??????????????????????????????
            Member member = memberService.detail(userId);
            if (member == null || StringUtils.isEmpty(member.getPhone())) {
                return Result.ok().put("count", 0);
            }
            String phone = member.getPhone();
            orderSearchDto.setReceiverPhone(phone);
        }

        PageResponse<OrderDTO> result = orderFeign.pageLikeForCustomer(orderSearchDto);

        return Result.ok().put("count", result.getCounts());
    }

    /**
     * ??????
     *
     * @param dto
     * @return
     */
    @ApiOperation("????????????")
    @SneakyThrows
    @GetMapping("page")
    public Result page(MailingQueryDTO dto) {
        //??????userid
        String userId = RequestContext.getUserId();

        OrderSearchDTO orderSearchDto = new OrderSearchDTO();
        orderSearchDto.setPage(dto.getPage());
        orderSearchDto.setPageSize(dto.getPagesize());
        orderSearchDto.setKeyword(dto.getKeyword());
        if (0 == dto.getMailType()) {
            // ?????????
            orderSearchDto.setMemberId(userId);
        } else {
            // ????????? ??????????????????????????????
            Member member = memberService.detail(userId);
            if (member == null || StringUtils.isEmpty(member.getPhone())) {
                return new Result().ok().put("data", PageResponse.<CustomerOrderDTO>builder().page(dto.getPage()).pagesize(dto.getPagesize()).pages(0L).counts(0L).build());
            }
            String phone = member.getPhone();
            orderSearchDto.setReceiverPhone(phone);
        }
        PageResponse<OrderDTO> result = orderFeign.pageLikeForCustomer(orderSearchDto);
        if (result.getItems().size() > 0) {
            // ????????????
            Set<Long> addressSet = new HashSet<>();
            addressSet.addAll(result.getItems().stream().filter(item -> item.getReceiverProvinceId() != null).map(item -> Long.valueOf(item.getReceiverProvinceId())).collect(Collectors.toSet()));
            addressSet.addAll(result.getItems().stream().filter(item -> item.getReceiverCityId() != null).map(item -> Long.valueOf(item.getReceiverCityId())).collect(Collectors.toSet()));
            addressSet.addAll(result.getItems().stream().filter(item -> item.getReceiverCountyId() != null).map(item -> Long.valueOf(item.getReceiverCountyId())).collect(Collectors.toSet()));
            addressSet.addAll(result.getItems().stream().filter(item -> item.getSenderProvinceId() != null).map(item -> Long.valueOf(item.getSenderProvinceId())).collect(Collectors.toSet()));
            addressSet.addAll(result.getItems().stream().filter(item -> item.getSenderCityId() != null).map(item -> Long.valueOf(item.getSenderCityId())).collect(Collectors.toSet()));
            addressSet.addAll(result.getItems().stream().filter(item -> item.getSenderCountyId() != null).map(item -> Long.valueOf(item.getSenderCountyId())).collect(Collectors.toSet()));
            CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, addressSet);

            // ????????????  ??????
            Set<String> cargoSet = result.getItems().stream().map(item -> item.getId()).collect(Collectors.toSet());
            CompletableFuture<Map<String, OrderCargoDto>> cargoMapFuture = PdCompletableFuture.cargoMapFuture(cargoFeign, cargoSet);

            // ??????
            Set<String> transportOrderDTOSet = result.getItems().stream().map(item -> item.getId()).collect(Collectors.toSet());
            CompletableFuture<Map<String, TransportOrderDTO>> transportOrderMapFuture = PdCompletableFuture.transportOrderMapFuture(transportOrderFeign, transportOrderDTOSet);
            //????????????
            Set<String> iPickupDispatchTaskSet = result.getItems().stream().map(item -> item.getId()).collect(Collectors.toSet());
            // ???????????????1??????????????????2???????????????
            CompletableFuture<Map<String, TaskPickupDispatchDTO>> taskPickupDispatchPullMapFuture = PdCompletableFuture.taskTranSportMapFuture(pickupDispatchTaskFeign, iPickupDispatchTaskSet, PickupDispatchTaskType.PICKUP.getCode());
            CompletableFuture<Map<String, TaskPickupDispatchDTO>> taskPickupDispatchPushMapFuture = PdCompletableFuture.taskTranSportMapFuture(pickupDispatchTaskFeign, iPickupDispatchTaskSet, PickupDispatchTaskType.DISPATCH.getCode());

            // ????????????
            Map<Long, Area> areaMap = areaMapFuture.get();
            Map<String, OrderCargoDto> cargoMap = cargoMapFuture.get();
            Map<String, TransportOrderDTO> transportOrderMap = transportOrderMapFuture.get();
            Map<String, TaskPickupDispatchDTO> taskPickupDispatchPullMap = taskPickupDispatchPullMapFuture.get();
            Map<String, TaskPickupDispatchDTO> taskPickupDispatchPushMap = taskPickupDispatchPushMapFuture.get();

            List<CustomerOrderDTO> newItem = result.getItems().stream().map(item -> {
                CustomerOrderDTO customerOrderDto = new CustomerOrderDTO(item, areaMap, cargoMap, transportOrderMap, taskPickupDispatchPullMap, taskPickupDispatchPushMap);
                String id = customerOrderDto.getId();
                List<RouteDTO> routeList = (List<RouteDTO>) route(id).get("data");
                if (!CollectionUtils.isEmpty(routeList)) {
                    customerOrderDto.setRouteDTO(routeList.get(0));
                } else {
                    customerOrderDto.setRouteDTO(RouteDTO.builder().build());
                }
                return customerOrderDto;
            }).collect(Collectors.toList());

            return new Result().ok().put("data", PageResponse.<CustomerOrderDTO>builder()
                    .page(dto.getPage())
                    .pagesize(dto.getPagesize())
                    .pages(result.getPages())
                    .counts(result.getCounts())
                    .items(newItem)
                    .build());
        } else {
            return new Result().ok().put("data", PageResponse.<CustomerOrderDTO>builder()
                    .page(dto.getPage())
                    .pagesize(dto.getPagesize())
                    .pages(0L)
                    .counts(0L)
                    .items(Lists.newArrayList())
                    .build());
        }

    }

    /**
     * ?????? ??????
     *
     * @param id ??????id
     * @return
     */
    @ApiOperation("??????")
    @SneakyThrows
    @GetMapping("detail")
    public Result detail(String id) {
        CustomerOrderDetailDTO.CustomerOrderDetailDTOBuilder builder = CustomerOrderDetailDTO.builder();
        //??????userid
        String userId = RequestContext.getUserId();
        //????????????
        OrderDTO orderDTO = orderFeign.findById(id);
        builder.id(orderDTO.getId());
        builder.status(orderDTO.getStatus());
        //???????????????
        TaskPickupDispatchDTO queryDTO = new TaskPickupDispatchDTO();
        queryDTO.setOrderId(id);
        List<TaskPickupDispatchDTO> pickupDispatchTasks = pickupDispatchTaskFeign.findAll(queryDTO);
        log.info("????????????id?????????????????????{}", pickupDispatchTasks);
        if (orderDTO.getStatus().equals(OrderStatus.CANCELLED.getCode())) {
            pickupDispatchTasks = pickupDispatchTasks.stream().filter(item -> (PickupDispatchTaskStatus.CANCELLED.getCode().equals(item.getStatus()))).collect(Collectors.toList());
        } else {
            pickupDispatchTasks = pickupDispatchTasks.stream().filter(item -> (!PickupDispatchTaskStatus.CANCELLED.getCode().equals(item.getStatus()))).collect(Collectors.toList());
        }
        Map<Integer, TaskPickupDispatchDTO> taskPickupDispatchDTOMap = pickupDispatchTasks.stream().
                collect(Collectors.toMap(TaskPickupDispatchDTO::getTaskType, taskPickupDispatchDTO -> taskPickupDispatchDTO, (v1, v2) -> v1));
        log.info("?????????????????????{}", taskPickupDispatchDTOMap);
        //????????????????????????
        TaskPickupDispatchDTO taskPickupDispatchPullDTO = taskPickupDispatchDTOMap.get(PickupDispatchTaskType.PICKUP.getCode());//iPickupDispatchTaskClient.findByOrderIdAndTaskType(id, 1);
        //????????????
        TaskPickupDispatchDTO taskPickupDispatchPushDTO = taskPickupDispatchDTOMap.get(PickupDispatchTaskType.DISPATCH.getCode());//iPickupDispatchTaskClient.findByOrderIdAndTaskType(id, 2);

        TaskPickupDispatchDTO taskPickupDispatchCurrentDTO = null;
        if (null != taskPickupDispatchPullDTO) {
            taskPickupDispatchCurrentDTO = taskPickupDispatchPullDTO;
        }
        if (null != taskPickupDispatchPushDTO) {
            taskPickupDispatchCurrentDTO = taskPickupDispatchPushDTO;
        }
        if (null != taskPickupDispatchCurrentDTO) {
            builder.planPickUpTime(taskPickupDispatchCurrentDTO.getEstimatedEndTime());
            builder.actualDispathedTime(taskPickupDispatchCurrentDTO.getActualEndTime());
            builder.cancelTime(taskPickupDispatchCurrentDTO.getCancelTime());

            //?????????id
            String courierId = taskPickupDispatchCurrentDTO.getCourierId();
            if (courierId != null) {
                R<User> userR = userApi.get(Long.valueOf(courierId));
                User user = userR.getData();
                builder.name(user.getName());
                builder.mobile(user.getMobile());
                builder.avatar(user.getAvatar());
            }
        }
        // ?????????
        TransportOrderDTO transportOrderDTO = transportOrderFeign.findByOrderId(orderDTO.getId());
        builder.tranOrderId(transportOrderDTO != null ? transportOrderDTO.getId() : "");
        CustomerOrderDetailDTO data = builder.build();
        log.info("result:{}", data);
        return Result.ok().put("data", data);
    }

    /**
     * ?????? ????????????
     *
     * @param id ??????id
     * @return
     */
    @SneakyThrows
    @ApiOperation("??????")
    @GetMapping("route")
    public Result route(String id) {
        List<RouteDTO> result = new ArrayList<>();
        //????????????
        OrderDTO orderDTO = orderFeign.findById(id);
        //???????????????
        TaskPickupDispatchDTO queryDTO = new TaskPickupDispatchDTO();
        queryDTO.setOrderId(id);

        List<TaskPickupDispatchDTO> pickupDispatchTasks = pickupDispatchTaskFeign.findAll(queryDTO);
        log.info("????????????id?????????????????????{}", pickupDispatchTasks);
        if (orderDTO.getStatus().equals(OrderStatus.CANCELLED.getCode())) {
            pickupDispatchTasks = pickupDispatchTasks.stream().filter(item -> (PickupDispatchTaskStatus.CANCELLED.getCode().equals(item.getStatus()))).collect(Collectors.toList());
        } else {
            pickupDispatchTasks = pickupDispatchTasks.stream().filter(item -> (!PickupDispatchTaskStatus.CANCELLED.getCode().equals(item.getStatus()))).collect(Collectors.toList());
        }
        Map<Integer, TaskPickupDispatchDTO> taskPickupDispatchDTOMap = pickupDispatchTasks.stream().
                collect(Collectors.toMap(TaskPickupDispatchDTO::getTaskType, taskPickupDispatchDTO -> taskPickupDispatchDTO, (v1, v2) -> v1));
        log.info("?????????????????????{}", taskPickupDispatchDTOMap);
        //????????????
        TaskPickupDispatchDTO taskPickupDispatchPullDTO = taskPickupDispatchDTOMap.get(1);
        //????????????
        TaskPickupDispatchDTO taskPickupDispatchPushDTO = taskPickupDispatchDTOMap.get(2);

        RouteDTO.RouteDTOBuilder builder;
        /**
         * ???????????????????????? ???????????????
         */
        builder = RouteDTO.builder();
        // ?????????

        // ?????????  ??????????????????  ????????? ??????????????????
        if (null != taskPickupDispatchPullDTO) {
            if (PickupDispatchTaskStatus.CANCELLED.getCode() == taskPickupDispatchPullDTO.getStatus()) {
                builder.time(taskPickupDispatchPullDTO.getCreateTime());
                builder.status(OrderStatus.CANCELLED.getCode());
                builder.msg("????????????");
            } else {
                if (PickupDispatchTaskStatus.PENDING.getCode() == taskPickupDispatchPullDTO.getStatus()) {
                    builder.time(taskPickupDispatchPullDTO.getEstimatedStartTime());
                    builder.status(OrderStatus.PENDING.getCode());
                    builder.msg("???????????????????????????");
                } else {
                    builder.time(taskPickupDispatchPullDTO.getActualStartTime());
                    builder.status(OrderStatus.PICKED_UP.getCode());
                    builder.msg("???????????????");
                }
            }
        }
        result.add(builder.build());

        /**
         * ????????????????????????
         */
        TransportOrderDTO transportOrderDTO = transportOrderFeign.findByOrderId(orderDTO.getId());
        if (transportOrderDTO == null) {
            Collections.reverse(result);
            return Result.ok().put("data", result);
        }

        List<TaskTransportDTO> transportTaskDTOs = transportTaskFeign.findAllByOrderIdOrTaskId(transportOrderDTO.getId(), null);
        if (transportTaskDTOs != null && transportTaskDTOs.size() > 0) {
            Set<String> agencySet = new HashSet<>();
            agencySet.addAll(transportTaskDTOs.stream().map(item -> item.getStartAgencyId()).collect(Collectors.toSet()));
            agencySet.addAll(transportTaskDTOs.stream().map(item -> item.getEndAgencyId()).collect(Collectors.toSet()));

            CompletableFuture<Map<Long, Org>> orgMapFeture = PdCompletableFuture.agencyMapFuture(orgApi, null, agencySet, null);
            Map<Long, Org> orgMap = orgMapFeture.get();
            for (TaskTransportDTO taskTranSport : transportTaskDTOs) {
                if (null != taskTranSport.getActualPickUpGoodsTime()) {
                    //?????????????????? ????????????????????????
                    builder = RouteDTO.builder();
                    builder.status(OrderStatus.IN_TRANSIT.getCode());
                    builder.time(taskTranSport.getActualPickUpGoodsTime());
                    // ??????????????????
                    Org org = orgMap.get(Long.valueOf(taskTranSport.getStartAgencyId()));
                    builder.msg("????????????" + org.getName() + "????????????????????????????????????");
                    result.add(builder.build());
                }
                if (null != taskTranSport.getActualDepartureTime()) {
                    //??????????????????
                    builder = RouteDTO.builder();
                    builder.status(OrderStatus.IN_TRANSIT.getCode());
                    builder.time(taskTranSport.getActualDepartureTime());
                    builder.msg("???????????????");
                    result.add(builder.build());
                }

                if (null != taskTranSport.getActualArrivalTime()) {
                    //??????????????????
                    builder = RouteDTO.builder();
                    builder.status(OrderStatus.IN_TRANSIT.getCode());
                    builder.time(taskTranSport.getActualArrivalTime());
                    // ??????????????????
                    Org org = orgMap.get(Long.valueOf(taskTranSport.getEndAgencyId()));
                    builder.msg("??????????????????" + org.getName() + "???");
                    result.add(builder.build());
                }
            }
        }


        /**
         * ????????????????????????
         */
        if (null != taskPickupDispatchPushDTO) {
            // ????????? ?????????????????????????????????
            if (taskPickupDispatchPushDTO.getActualStartTime() != null) {
                builder = RouteDTO.builder();
                builder.time(taskPickupDispatchPushDTO.getActualStartTime());
                builder.status(OrderStatus.DISPATCHING.getCode());
                String userId = taskPickupDispatchPushDTO.getCourierId();
                if (StringUtils.isNotEmpty(userId)) {
                    R<User> userR = userApi.get(Long.valueOf(userId));
                    User user = userR.getData();
                    String mobile = user.getMobile();
                    String name = user.getName();
                    builder.msg("????????????" + name + "????????????????????????????????????" + mobile + "???");
                }
                result.add(builder.build());
            }
            // ????????? ??????????????????
            if (taskPickupDispatchPushDTO.getActualEndTime() != null && taskPickupDispatchPushDTO.getSignStatus() != null) {
                builder = RouteDTO.builder();
                builder.time(taskPickupDispatchPushDTO.getActualEndTime());
                builder.status(OrderStatus.RECEIVED.getCode());
                List<OrderCargoDto> cargos = cargoFeign.findAll(null, id);
                Integer quantity = 1;
                if (!CollectionUtils.isEmpty(cargos)) {
                    quantity = cargos.get(0).getQuantity();
                }
                builder.quantity(quantity);
                if (taskPickupDispatchPushDTO.getSignStatus().equals(1)) {
                    // ?????????
                    builder.msg(taskPickupDispatchPushDTO.getMark() != null ? taskPickupDispatchPushDTO.getMark() : "?????????! ");
                } else {
                    //??????
                    builder.msg(taskPickupDispatchPushDTO.getMark() != null ? taskPickupDispatchPushDTO.getMark() : "?????????! ");
                }
                result.add(builder.build());
            }

        }
        Collections.reverse(result);
        result.forEach(item -> {
            log.info("???????????????{}", item);
        });
        return Result.ok().put("data", result);
    }
}
