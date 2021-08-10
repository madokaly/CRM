package com.madoka.crm.settings.dao;

import com.madoka.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
