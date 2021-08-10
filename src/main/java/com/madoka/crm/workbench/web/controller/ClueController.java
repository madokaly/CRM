package com.madoka.crm.workbench.web.controller;

import com.madoka.crm.settings.domain.DicValue;
import com.madoka.crm.settings.domain.User;
import com.madoka.crm.settings.service.UserService;
import com.madoka.crm.settings.service.impl.UserServiceImpl;
import com.madoka.crm.utils.DateTimeUtil;
import com.madoka.crm.utils.PrintJson;
import com.madoka.crm.utils.ServiceFactory;
import com.madoka.crm.utils.UUIDUtil;
import com.madoka.crm.workbench.domain.Activity;
import com.madoka.crm.workbench.domain.Clue;
import com.madoka.crm.workbench.domain.Tran;
import com.madoka.crm.workbench.service.ActivityService;
import com.madoka.crm.workbench.service.ClueService;
import com.madoka.crm.workbench.service.impl.ActivityServiceImpl;
import com.madoka.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if( "/workbench/clue/getUserList.do".equals(path) ) {
            getUserList(request,response);
        }else if( "/workbench/clue/save.do".equals(path) ) {
            save(request,response);
        }else if( "/workbench/clue/detail.do".equals(path) ) {
            detail(request,response);
        }else if( "/workbench/clue/getActivityListByCid.do".equals(path) ) {
            getActivityListByCid(request,response);
        }else if( "/workbench/clue/unbund.do".equals(path) ) {
            unbund(request,response);
        }else if( "/workbench/clue/getActivityListByName.do".equals(path) ) {
            getActivityListByName(request,response);
        }else if( "/workbench/clue/bund.do".equals(path) ) {
            bund(request,response);
        }else if( "/workbench/clue/getAllActivityListByName.do".equals(path) ) {
            getAllActivityListByName(request,response);
        }else if( "/workbench/clue/convert.do".equals(path) ) {
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        Tran tran = null;
        String createBy = ( (User)request.getSession().getAttribute("user")).getName();
        if("a".equals(flag)) {
            tran = new Tran();
            String id = UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();
            tran.setId(id);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateTime(createTime);
            tran.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,tran,createBy);
        if(flag1) {
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getAllActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getAllActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        String[] aIds = request.getParameterValues("activityId");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(clueId,aIds);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");
        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService( new ClueServiceImpl() );
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByCid(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByCid(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService( new ClueServiceImpl() );
        Clue clue = cs.detail(id);
        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs = (ClueService) ServiceFactory.getService( new ClueServiceImpl() );
        String id = UUIDUtil.getUUID();
        String owner =request.getParameter("owner");
        String fullname =request.getParameter("fullname");
        String appellation =request.getParameter("appellation");
        String company =request.getParameter("company");
        String job =request.getParameter("job");
        String email =request.getParameter("email");
        String phone =request.getParameter("phone");
        String website =request.getParameter("website");
        String mphone =request.getParameter("mphone");
        String state =request.getParameter("state");
        String source =request.getParameter("source");
        String description =request.getParameter("description");
        String nextContactTime =request.getParameter("nextContactTime");
        String contactSummary =request.getParameter("contactSummary");
        String address =request.getParameter("address");
        String createBy = ( (User)request.getSession().getAttribute("user") ).getName();
        String createTime = DateTimeUtil.getSysTime();
        Clue clue = new Clue();
        clue.setId(id);
        clue.setOwner(owner);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setDescription(description);
        clue.setNextContactTime(nextContactTime);
        clue.setContactSummary(contactSummary);
        clue.setAddress(address);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService( new UserServiceImpl() );
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
