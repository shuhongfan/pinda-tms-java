package com.itheima.pinda.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.entity.Member;
import com.itheima.pinda.mapper.MemberMapper;
import com.itheima.pinda.service.IMemberService;
import org.springframework.stereotype.Service;

/**
 * 用户服务类实现
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

}