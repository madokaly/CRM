package com.madoka.crm.workbench.dao;

import com.madoka.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByCid(String clueId);

    List<Activity> getActivityListByName(Map<String, String> map);

    List<Activity> getAllActivityListByName(String aname);
}
