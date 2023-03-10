/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.CacheLineUseEntity;

/**
 * 缓冲使用表
 *
 * @author
 */
public interface ICacheLineUseService extends IService<CacheLineUseEntity> {

    CacheLineUseEntity getByOrderClassifyId(String id);
}
