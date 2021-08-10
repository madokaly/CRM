package com.madoka.crm.workbench.web.controller;

import com.madoka.crm.settings.domain.User;
import com.madoka.crm.settings.service.UserService;
import com.madoka.crm.settings.service.impl.UserServiceImpl;
import com.madoka.crm.utils.*;
import com.madoka.crm.workbench.domain.Activity;
import com.madoka.crm.workbench.domain.ActivityRemark;
import com.madoka.crm.workbench.service.ActivityService;
import com.madoka.crm.workbench.service.impl.ActivityServiceImpl;
import com.madoka.crm.workbench.vo.paginationVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if( "/workbench/activity/getUserList.do".equals(path) ) {
            getUserList(request,response);
        }else if( "/workbench/activity/save.do".equals(path) ){
            save(request,response);
        }else if( "/workbench/activity/pageList.do".equals(path) ){
            pageList(request,response);
        }else if( "/workbench/activity/delete.do".equals(path) ){
            delete(request,response);
        }else if( "/workbench/activity/getUserListAndActivity.do".equals(path) ){
            getUserListAndActivity(request,response);
        }else if( "/workbench/activity/update.do".equals(path) ){
            update(request,response);
        }else if( "/workbench/activity/detail.do".equals(path) ){
            detail(request,response);
        }else if( "/workbench/activity/getRemarkListByAid.do".equals(path) ){
            getRemarkListByAid(request,response);
        }else if( "/workbench/activity/deleteRemark.do".equals(path) ){
            deleteRemark(request,response);
        }else if( "/workbench/activity/saveRemark.do".equals(path) ){
            saveRemark(request,response);
        }else if( "/workbench/activity/updateRemark.do".equals(path) ){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ( (User)request.getSession().getAttribute("user") ).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setId(id);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        boolean flag = as.updateRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ( (User)request.getSession().getAttribute("user") ).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setId(id);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        boolean flag = as.saveRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        List<ActivityRemark> arList = as.getRemarkListByAid(id);
        PrintJson.printJsonObj(response,arList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        Activity activity = as.detail(id);
        request.setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ( (User)request.getSession().getAttribute("user") ).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        String id = request.getParameter("id");
        Map<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        boolean flag = as.delete( ids );
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl() );
        paginationVO<Activity> vo = as.pageList(map);
        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ( (User)request.getSession().getAttribute("user") ).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
