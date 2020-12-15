package com.offcn.user.service;

import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private TMemberMapper memberMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TMemberAddressMapper addressMapper;

    @Override
    public void addUser(TMember member) {
        // 2、手机号未被注册，设置相关参数，保存注册信息
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(member.getUserpswd());
        //设置密码
        member.setUserpswd(encode);
        member.setUsername(member.getLoginacct());
        //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        member.setAuthstatus("0");
        //用户类型: 0 - 个人， 1 - 企业
        member.setUsertype("0");
        //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        member.setAccttype("2");
        System.out.println("插入数据:"+member.getLoginacct());
        memberMapper.insert(member);
    }

    @Override
    public TMember findByNameAndPwd(String logincact, String pwd) {

        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(logincact);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<TMember> tMembers = memberMapper.selectByExample(example);
        if (tMembers==null||tMembers.size()==0){
            return null;
        }
        if (encoder.matches(pwd,tMembers.get(0).getUserpswd())){
            return tMembers.get(0);
        }
        return null;
    }

    @Override
    public TMember findByUserId(String userId) {
        return memberMapper.selectByPrimaryKey(Integer.parseInt(userId));

    }

    @Override
    public List<TMemberAddress> addressList(String token) {

        TMemberAddressExample example = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(Integer.parseInt(redisTemplate.opsForValue().get(token)));
        return addressMapper.selectByExample(example);
    }
}
