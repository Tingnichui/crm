package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao =SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public Boolean save(Activity activity) {
        Boolean flag = true;


        int count = activityDao.save(activity);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Activity> pageList(Map<String, Object> map) {
//        获取total
        int total = activityDao.getTotalByCondition(map);
//        获取dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
//        封装
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
//        返回
        return vo;
    }

    public Boolean delete(String[] ids) {
        Boolean flag = true;
        int count1 = activityRemarkDao.getCountByAids(ids);
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2){
            flag = false;
        }
        int count3 = activityDao.delete(ids);
        if (ids.length != count3){
            flag = false;
        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
//        获取userList
        List<User> userList = userDao.getUserlist();
//        获取activity
        Activity activity = activityDao.getById(id);
//        返回map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    public Boolean update(Activity activity) {
        Boolean flag = true;


        int count = activityDao.update(activity);
        if (1 != count){
            flag = false;
        }
        return flag;
    }
}
