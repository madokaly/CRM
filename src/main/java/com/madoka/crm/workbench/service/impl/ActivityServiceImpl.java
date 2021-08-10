package com.madoka.crm.workbench.service.impl;

import com.madoka.crm.settings.dao.UserDao;
import com.madoka.crm.settings.domain.User;
import com.madoka.crm.utils.SqlSessionUtil;
import com.madoka.crm.workbench.dao.ActivityDao;
import com.madoka.crm.workbench.dao.ActivityRemarkDao;
import com.madoka.crm.workbench.domain.Activity;
import com.madoka.crm.workbench.domain.ActivityRemark;
import com.madoka.crm.workbench.service.ActivityService;
import com.madoka.crm.workbench.vo.paginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public paginationVO<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> aList = activityDao.getActivityListByCondition(map);
        paginationVO<Activity> vo = new paginationVO<>();
        vo.setTotal(total);
        vo.setDataList(aList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        int count = activityRemarkDao.getCountByAids(ids);

        int delCount = activityRemarkDao.deleteByAids(ids);

        if(count != delCount) {
            flag = false;
        }
        int delCounts = activityDao.delete(ids);
        if(delCounts != ids.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> uList = userDao.getUserList();

        Activity a = activityDao.getById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String id) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(id);
        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if(count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if(count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if(count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByName(Map<String, String> map) {
        List<Activity> aList = activityDao.getActivityListByName(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByCid(String clueId) {
        List<Activity> aList = activityDao.getActivityListByCid(clueId);
        return aList;
    }

    @Override
    public List<Activity> getAllActivityListByName(String aname) {
        List<Activity> aList = activityDao.getAllActivityListByName(aname);
        return aList;
    }
}
