package com.bjpowernode.crm.workbench.web.webcontroller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

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
        System.out.println("进入市场活动控制器");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }
    }
//    更新/修改
    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");
//        获取参数
        String id  = request.getParameter("id");
        String name  = request.getParameter("name");
        String owner  = request.getParameter("owner");
        String startDate  = request.getParameter("startDate");
        String endDate  = request.getParameter("endDate");
        String cost  = request.getParameter("cost");
        String description  = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
//        封装对象
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
//        链接数据库修改
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }
//    修改页
    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入修改页");

        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }
//    删除页
    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除操作");
        String[] ids = request.getParameterValues("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }
//    分页
    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询市场活动信息");
        String pageNo = request.getParameter("pageNo");
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        int skipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> vo = activityService.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }
//    创建
    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加市场活动信息");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
//        封装成对象
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
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.save(activity);
        PrintJson.printJsonFlag(response,flag);

    }
//    获取userList
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户信息列表");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserlist();

        PrintJson.printJsonObj(response,userList);
    }

}
