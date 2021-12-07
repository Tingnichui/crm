package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);
        if (null == user){
            throw new LoginException("账号密码错误！");
        }
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();

        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效！");
        }
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已冻结！");
        }


        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址受到限制！");
        }

        return user;
    }

    public List<User> getUserlist() {
        List<User> userList = userDao.getUserlist();
        return userList;
    }
}
