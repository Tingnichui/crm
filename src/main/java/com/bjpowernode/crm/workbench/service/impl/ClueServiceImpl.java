package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    public Boolean save(Clue clue) {
        Boolean flag = true;
        int count = clueDao.save(clue);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getListByCondition(map);

        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    public Clue detail(String id) {
        Clue clue = clueDao.getById(id);
        return clue;
    }

    public Boolean unbund(String id) {
        Boolean flag = true;
        int count = clueDao.unbund(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public Boolean bund(String cid, String[] aid) {
        Boolean flag = true;
        for (String id:aid){
             ClueActivityRelation car = new ClueActivityRelation();
             car.setId(UUIDUtil.getUUID());
             car.setClueId(cid);
             car.setActivityId(id);
             int count = clueActivityRelationDao.bund(car);
             if (count != 1){
                 flag = false;
             }
        }
        return flag;
    }

    public Boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        Boolean flag = true;
        Clue clue = clueDao.getByIdInit(clueId);

        String company = clue.getCompany();
        Customer customer = customerDao.getCusTomerByName(company);
        if (null == customer) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getPhone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        int count2 = contactsDao.save(contacts);
        if (count2 != 1){
            flag = false;
        }

        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark cr : clueRemarkList){
            String noteContent = cr.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }

        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1){
                flag = false;
            }
        }

        if (t != null){
            t.setOwner(clue.getOwner());
            t.setCustomerId(customer.getId());
            t.setSource(clue.getSource());
            t.setContactsId(contacts.getId());
            t.setDescription(clue.getDescription());
            t.setContactSummary(clue.getContactSummary());
            t.setNextContactTime(clue.getNextContactTime());
            int count6 = tranDao.save(t);
            if (count6 != 1){
                flag=false;
            }

            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(t.getId());
            tranHistory.setStage(t.getStage());
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7 != 1){
                flag = false;
            }
        }
/*
        for (ClueRemark cr : clueRemarkList){
            int count8 = clueRemarkDao.delete(cr.getId());
            if (count8 != 1){
                flag =false;
            }
        }

        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation.getId());
            if (count9 != 1){
                flag = false;
            }
        }
        int count10 = clueDao.delete(clueId);
        if (count10 != 1){
            flag = false;
        }*/
        return flag;
    }

}
