package com.madoka.crm.settings.service.impl;

import com.madoka.crm.settings.dao.DicTypeDao;
import com.madoka.crm.settings.dao.DicValueDao;
import com.madoka.crm.settings.domain.DicType;
import com.madoka.crm.settings.domain.DicValue;
import com.madoka.crm.settings.service.DicService;
import com.madoka.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        List<DicType> typeList = dicTypeDao.getTypeList();
        for (DicType dicType : typeList) {
            String code = dicType.getCode();
            List<DicValue> valueList = dicValueDao.getListByCode(code);
            map.put(code,valueList);
        }
        return map;
    }
}
