package com.madoka.crm.workbench.service;

import com.madoka.crm.settings.domain.User;
import com.madoka.crm.workbench.domain.Clue;
import com.madoka.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {

    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] aIds);

    boolean convert(String clueId, Tran tran, String createBy);
}
