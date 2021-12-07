package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    public Boolean save(Clue clue) {
        Boolean flag = true;
        int count = clueDao.save(clue);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public List<Clue> pageList(Map<String, Integer> map) {
        List<Clue> dataList = clueDao.pageList(map);
        return dataList;
    }
}
