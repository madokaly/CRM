package com.madoka.crm.utils.web.listener;

import com.madoka.crm.settings.domain.DicValue;
import com.madoka.crm.settings.service.DicService;
import com.madoka.crm.settings.service.impl.DicServiceImpl;
import com.madoka.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;


public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("监听器启动");
        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService( new DicServiceImpl() );
        Map<String, List<DicValue>> map = ds.getAll();
        Set<String> set = map.keySet();
        for (String s : set) {
            application.setAttribute(s,map.get(s));
        }
        System.out.println("监听器结束");


        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        Map<String,String> pMap = new HashMap<>();
        while(e.hasMoreElements()) {
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }
}
