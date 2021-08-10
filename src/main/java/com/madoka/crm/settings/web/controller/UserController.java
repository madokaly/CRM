package com.madoka.crm.settings.web.controller;

import com.madoka.crm.settings.domain.User;
import com.madoka.crm.settings.service.UserService;
import com.madoka.crm.settings.service.impl.UserServiceImpl;
import com.madoka.crm.utils.MD5Util;
import com.madoka.crm.utils.PrintJson;
import com.madoka.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if("/settings/User/login.do".equals(path)) {
            login(request,response);
        }else{

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        String ip = request.getRemoteAddr();
        loginPwd = MD5Util.getMD5(loginPwd);
        UserService us = (UserService) ServiceFactory.getService( new UserServiceImpl() );
        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
