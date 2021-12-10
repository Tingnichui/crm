package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(String id);
}
