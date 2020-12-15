package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import java.util.List;

public interface UserService {

    public void addUser(TMember member);

    TMember findByNameAndPwd(String logincact, String pwd);

    TMember findByUserId(String userId);

    public List<TMemberAddress> addressList(String token);
}

