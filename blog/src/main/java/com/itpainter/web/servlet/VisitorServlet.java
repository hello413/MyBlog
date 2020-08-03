package com.itpainter.web.servlet;

import com.itpainter.domain.*;
import com.itpainter.service.VisitorService;
import com.itpainter.service.impl.VisitorServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/visitor/*")
public class VisitorServlet extends AbstractBaseServlet {
    private VisitorService service = new VisitorServiceImpl();

    public User login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username!=null&&password!=null){
            User user = service.isAdmin(username,password);
            if (user!=null){
                request.getSession().setAttribute("user", user);
                return user;
            }else {
                throw new RuntimeException("账号或密码错误");
            }
        }else {
            throw new RuntimeException("信息输入不完整");
        }
    }

    public PageBean<Blog> blogAsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        User visitoruser = (User) session.getAttribute("visitoruser");
//        if (visitoruser == null){
//            visitoruser = service.findByNickname("IT_Painter");
//            request.getSession().setAttribute("visitoruser", visitoruser);
//        }
        String search = request.getParameter("search");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");//每页显示条数,如果不传递，默认每页显示6条记录
        String type = request.getParameter("type");//每页显示条数,如果不传递，默认每页显示6条记录
        String tag = request.getParameter("tag");//每页显示条数,如果不传递，默认每页显示6条记录
        int currentPage = 1;//当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1;
        }
        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 6;
        }
        PageBean<Blog> page = service.blogAsList(search, currentPage, pageSize,type,tag);
        return page;
    }

    public List<Type> typeShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Type> list = service.typeAsList();
        for (int i=0;i<list.size();i++){
            Long id = list.get(i).getId();
            list.get(i).setBlogs(service.findByTypeId(id));
        }
        return list;
    }

    public List<Tag> tagsShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tag> list = service.tagAsList();
        for (int i=0;i<list.size();i++){
            Long id = list.get(i).getId();
            list.get(i).setBlogs(service.findByTagId(id));
        }
        return list;
    }

    public Map<Integer,List<Blog>> TimeAsBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Integer,List<Blog>> page = service.TimeAsBlog();
        return page;
    }

    public Blog Blog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("blog_id");
        Blog page = null;
        if (id.equals("null")){
            throw new RuntimeException("没有指定阅读文章");
        }else {
            page = service.Blog(id);
        }
        return page;
    }

    public List<MyMap> comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("blog_id");
        Map<Comment,List<Comment>> comment = null;
        if (id.equals("null")){
            throw new RuntimeException("没有指定阅读文章");
        }else {
            comment = service.comment(id);
        }
        List<MyMap> list = new ArrayList<>();
        for (Comment c:comment.keySet()){
            MyMap myMap = new MyMap();
            myMap.setKey(c);
            myMap.setValue(comment.get(c));
            list.add(myMap);
        }
        return list;
    }
}
