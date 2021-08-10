package com.madoka.crm.settings.service.impl;

import com.madoka.crm.exception.LoginException;
import com.madoka.crm.settings.dao.UserDao;
import com.madoka.crm.settings.domain.User;
import com.madoka.crm.settings.service.UserService;
import com.madoka.crm.utils.DateTimeUtil;
import com.madoka.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);
        if(user == null) {
            throw new LoginException("账号密码错误");
        }

        String sysTime = DateTimeUtil.getSysTime();
        if( sysTime.compareTo( user.getExpireTime() ) > 0) {
            throw new LoginException("账号已失效");
        }

        String lockState = user.getLockState();
        if( "0".equals( lockState ) ) {
            throw new LoginException("账号已锁定");
        }
        String allowIps = user.getAllowIps();
        if( !allowIps.contains( ip ) ) {
            throw new LoginException("ip地址受限");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> uList = userDao.getUserList();
        return uList;
    }
}
