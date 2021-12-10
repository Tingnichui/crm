package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getListByCondition(Map<String, Object> map);

    Clue getById(String id);

    int unbund(String id);

    Clue getByIdInit(String clueId);

    int delete(String clueId);
}
