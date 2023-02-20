package com.itheima.pinda.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author diesel
 * @since 2020-3-30
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Member {

    private String id;
    /**
     * 认证id
     */
    private String authId;
    /**
     * 身份证号
     */
    private String idCardNo;
    /**
     * 身份证号是否认证 1认证
     */
    private Integer idCardNoVerify;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 姓名
     */
    private String name;
}
