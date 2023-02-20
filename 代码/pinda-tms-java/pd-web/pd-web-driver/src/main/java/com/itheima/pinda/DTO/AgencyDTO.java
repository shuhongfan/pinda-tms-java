package com.itheima.pinda.DTO;

import com.itheima.pinda.vo.AgencyVo;
import lombok.Data;

@Data
public class AgencyDTO {

    /**
     * 转运中心名称
     */
    private String transport;

    /**
     * 地址
     */
    private String address;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String contactsNumber;


    private Position position;
    public AgencyDTO(AgencyVo agencyVo) {
        this.address = (agencyVo.getProvince() != null ? agencyVo.getProvince().getName() : "") +
                (agencyVo.getCity() != null ? agencyVo.getCity().getName() : "") +
                (agencyVo.getCounty() != null ? agencyVo.getCounty().getName() : "") +
                (agencyVo.getAddress() != null ? agencyVo.getAddress() : "");
        this.transport = agencyVo.getName();
        this.contactsNumber = agencyVo.getContractNumber();
        this.contacts = agencyVo.getManager() != null ? agencyVo.getManager().getName() : "";
        this.position = Position.builder().latitude(agencyVo.getLatitude()).longitude(agencyVo.getLongitude()).build();
    }

}
