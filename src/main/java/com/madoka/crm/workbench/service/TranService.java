package com.madoka.crm.workbench.service;

import com.madoka.crm.workbench.domain.Tran;
import com.madoka.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> showHistoryListByTranId(String id);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
