package com.madoka.crm.workbench.service.impl;

import com.madoka.crm.utils.SqlSessionUtil;
import com.madoka.crm.workbench.dao.CustomerDao;
import com.madoka.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> nList = customerDao.getCustomerName(name);
        return nList;
    }
}
