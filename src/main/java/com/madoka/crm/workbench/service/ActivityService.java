package com.madoka.crm.workbench.service;

import com.madoka.crm.workbench.domain.Activity;
import com.madoka.crm.workbench.domain.ActivityRemark;
import com.madoka.crm.workbench.vo.paginationVO;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    paginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByCid(String clueId);

    List<Activity> getActivityListByName(Map<String, String> map);

    List<Activity> getAllActivityListByName(String aname);
}
