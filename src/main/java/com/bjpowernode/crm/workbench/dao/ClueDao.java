package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue clue);

    List<Clue> pageList(Map<String, Integer> map);
}
