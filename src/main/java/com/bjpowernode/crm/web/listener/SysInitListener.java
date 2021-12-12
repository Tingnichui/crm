package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("创建上下文域对象");
        System.out.println("服务器开始进行数字字典缓存");
        ServletContext application = event.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器完成数字字典缓存");

        ResourceBundle rs = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rs.getKeys();
        Map<String,String> pMap = new HashMap<String, String>();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rs.getString(key);
            pMap.put(key,value);

        }
        application.setAttribute("pMap",pMap);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("销毁上下文域对象");

    }
}
