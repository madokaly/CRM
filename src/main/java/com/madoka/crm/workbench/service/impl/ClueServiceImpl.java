package com.madoka.crm.workbench.service.impl;

import com.madoka.crm.settings.dao.UserDao;
import com.madoka.crm.settings.domain.User;
import com.madoka.crm.utils.DateTimeUtil;
import com.madoka.crm.utils.SqlSessionUtil;
import com.madoka.crm.utils.UUIDUtil;
import com.madoka.crm.workbench.dao.*;
import com.madoka.crm.workbench.domain.*;
import com.madoka.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao  = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] aIds) {
        boolean flag = true;
        for(String aid : aIds) {
            ClueActivityRelation car = new ClueActivityRelation();
            String id = UUIDUtil.getUUID();
            car.setId(id);
            car.setClueId(clueId);
            car.setActivityId(aid);
            int count = clueActivityRelationDao.bund(car);
            if(count != 1) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        //1.通过clueId获得线索对象
        Clue clue = clueDao.getById(clueId);
        //2.通过线索对象获取客户信息
        String company = clue.getCompany();
        Customer customer = customerDao.getByName(company);
        if(customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            //保存客户
            int count1 = customerDao.save(customer);
            if(count1 != 1) {
                flag = false;
            }
        }
        //3.通过线索对象获取联系人信息
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setOwner(clue.getOwner());
        con.setSource(clue.getSource());
        con.setCustomerId(customer.getId());
        con.setFullname(clue.getFullname());
        con.setAppellation(clue.getAppellation());
        con.setEmail(clue.getEmail());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setDescription(clue.getDescription());
        con.setContactSummary(clue.getContactSummary());
        con.setNextContactTime(clue.getNextContactTime());
        con.setAddress(clue.getAddress());
        //添加客户
        int count2 = contactsDao.save(con);
        if(count2 != 1) {
            flag = false;
        }
        //4.将线索备注转换到客户备注和联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        if(clueRemarkList != null) {
            for(ClueRemark clueRemark : clueRemarkList) {
                String noteContent = clueRemark.getNoteContent();
                //添加客户备注
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setNoteContent(noteContent);
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag("0");
                int count3 = customerRemarkDao.save(customerRemark);
                if(count3 != 1) {
                    flag = false;
                }

                //添加联系人备注
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setContactsId(con.getId());
                contactsRemark.setNoteContent(noteContent);
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag("0");
                int count4 = contactsRemarkDao.save(contactsRemark);
                if(count4 != 1) {
                    flag = false;
                }
            }
        }
        //5."线索和市场活动"的关系转换到"联系人和市场活动"的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        if (clueActivityRelationList != null) {
            for(ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                String activityId = clueActivityRelation.getActivityId();
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(con.getId());
                int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
                if (count5 != 1) {
                    flag = false;
                }
            }
        }
        //6.如果有创建交易需求，创建交易
        if (tran != null) {
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setCustomerId(customer.getId());
            tran.setContactsId(con.getId());
            int count6 = tranDao.save(tran);
            if (count6 != 1) {
                flag = false;
            }
            //7.如果创建交易，则创建该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            int count7 = tranHistoryDao.save(th);
            if(count7 != 1) {
                flag = false;
            }
        }
        //8.删除线索备注
        if(clueRemarkList != null) {
            for (ClueRemark clueRemark : clueRemarkList) {
                int count8 = clueRemarkDao.delete(clueRemark);
                if(count8 != 1) {
                    flag = false;
                }
            }
        }
        //9.删除线索与市场活动关系
        if (clueActivityRelationList != null) {
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                int count9 = clueActivityRelationDao.delete(clueActivityRelation);
                if(count9 != 1) {
                    flag = false;
                }
            }
        }
        //10.删除线索
        int count10 = clueDao.delete(clueId);
        if(count10 != 1) {
            flag = false;
        }

        return flag;
    }
}
