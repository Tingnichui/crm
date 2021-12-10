package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    Boolean save(Clue clue);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    Boolean unbund(String id);

    Boolean bund(String cid, String[] aid);

    Boolean convert(String clueId, Tran t, String createBy);
}
