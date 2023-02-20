package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.entity.DriverJob;
import com.itheima.pinda.entity.TaskTransport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 司机作业单 Mapper 接口
 * </p>
 */
@Mapper
public interface DriverJobMapper extends BaseMapper<DriverJob> {
}
