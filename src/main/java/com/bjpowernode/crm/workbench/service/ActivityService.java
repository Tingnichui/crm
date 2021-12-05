package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    Boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);
}
