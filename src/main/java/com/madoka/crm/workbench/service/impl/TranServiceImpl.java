package com.madoka.crm.workbench.service.impl;

import com.madoka.crm.utils.DateTimeUtil;
import com.madoka.crm.utils.SqlSessionUtil;
import com.madoka.crm.utils.UUIDUtil;
import com.madoka.crm.workbench.dao.CustomerDao;
import com.madoka.crm.workbench.dao.TranDao;
import com.madoka.crm.workbench.dao.TranHistoryDao;
import com.madoka.crm.workbench.domain.Customer;
import com.madoka.crm.workbench.domain.Tran;
import com.madoka.crm.workbench.domain.TranHistory;
import com.madoka.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran tran, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getByName(customerName);
        if(customer == null) {
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setNextContactTime(tran.getNextContactTime());
            int count1 = customerDao.save(customer);
            if(count1 != 1) {
                flag = false;
            }
        }
        tran.setCustomerId(customer.getId());
        int count2 = tranDao.save(tran);
        if(count2 != 1) {
            flag = false;
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setCreateBy(tran.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(tran.getExpectedDate());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> showHistoryListByTranId(String id) {
        List<TranHistory> hList = tranHistoryDao.showHistoryListByTranId(id);
        return hList;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count1 = tranDao.update(tran);
        if(count1 != 1) {
            flag = false;
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(tran.getStage());
        th.setTranId(tran.getId());
        th.setMoney(tran.getMoney());
        th.setPossibility(tran.getPossibility());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(tran.getEditTime());
        int count2 = tranHistoryDao.save(th);
        if(count2 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        int total = tranDao.getTotal();
        List< Map<String,Object> > dataList = tranDao.getCharts();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("total",total);
        dataMap.put("dataList",dataList);
        return dataMap;
    }
}
