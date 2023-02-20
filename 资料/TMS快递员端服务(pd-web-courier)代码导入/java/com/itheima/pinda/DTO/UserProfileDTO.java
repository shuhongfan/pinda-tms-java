package com.itheima.pinda.DTO;

import com.itheima.pinda.authority.entity.common.Area;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UserProfileDTO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 用户头
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String manager;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("作业范围")
    private List<Area> areas;
}
