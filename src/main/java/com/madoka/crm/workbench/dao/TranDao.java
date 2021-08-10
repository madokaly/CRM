package com.madoka.crm.workbench.dao;

import com.madoka.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String id);

    int update(Tran tran);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
