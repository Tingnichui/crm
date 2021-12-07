package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getListByAid(String activityId);

    int deleteRemarkById(String id);

    int save(ActivityRemark ar);

    int updateRemarkById(ActivityRemark activityRemark);
}
