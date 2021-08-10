package com.madoka.crm.workbench.dao;

import com.madoka.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> showHistoryListByTranId(String id);
}
