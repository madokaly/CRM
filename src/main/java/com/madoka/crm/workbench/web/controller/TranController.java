package com.madoka.crm.workbench.web.controller;

import com.madoka.crm.settings.domain.User;
import com.madoka.crm.settings.service.UserService;
import com.madoka.crm.settings.service.impl.UserServiceImpl;
import com.madoka.crm.utils.DateTimeUtil;
import com.madoka.crm.utils.PrintJson;
import com.madoka.crm.utils.ServiceFactory;
import com.madoka.crm.utils.UUIDUtil;
import com.madoka.crm.workbench.domain.Tran;
import com.madoka.crm.workbench.domain.TranHistory;
import com.madoka.crm.workbench.service.ClueService;
import com.madoka.crm.workbench.service.CustomerService;
import com.madoka.crm.workbench.service.TranService;
import com.madoka.crm.workbench.service.impl.ClueServiceImpl;
import com.madoka.crm.workbench.service.impl.CustomerServiceImpl;
import com.madoka.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if( "/workbench/transaction/add.do".equals(path) ) {
            add(request,response);
        }else if( "/workbench/transaction/getCustomerName.do".equals(path) ) {
            getCustomerName(request,response);
        }else if( "/workbench/transaction/save.do".equals(path) ) {
            save(request,response);
        }else if( "/workbench/transaction/detail.do".equals(path) ) {
            detail(request,response);
        }else if( "/workbench/transaction/showHistoryListByTranId.do".equals(path) ) {
            showHistoryListByTranId(request,response);
        }else if( "/workbench/transaction/changeStage.do".equals(path) ) {
            changeStage(request,response);
        }else if( "/workbench/transaction/getCharts.do".equals(path) ) {
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> dataMap = ts.getCharts();
        PrintJson.printJsonObj(response,dataMap);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String expectedDate = request.getParameter("expectedDate");
        String money = request.getParameter("money");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Map<String,String> pMap = (Map<String,String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        Tran tran = new Tran();
        tran.setId(tranId);
        tran.setStage(stage);
        tran.setExpectedDate(expectedDate);
        tran.setMoney(money);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        tran.setPossibility(possibility);
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(tran);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",tran);
        PrintJson.printJsonObj(response,map);
    }

    private void showHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> hList = ts.showHistoryListByTranId(id);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        for(TranHistory th : hList) {
            String possibility = pMap.get(th.getStage());
            th.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,hList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = ts.detail(id);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String stage = tran.getStage();
        String possibility = pMap.get(stage);
        request.setAttribute("possibility",possibility);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(tran,customerName);
        if(flag) {
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> nList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,nList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
