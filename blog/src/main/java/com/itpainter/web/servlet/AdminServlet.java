package com.itpainter.web.servlet;

import com.itpainter.domain.Blog;
import com.itpainter.domain.PageBean;
import com.itpainter.domain.User;
import com.itpainter.service.AdminService;
import com.itpainter.service.impl.AdminServiceImpl;
import com.itpainter.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet("/admini/*")
public class AdminServlet extends AbstractBaseServlet {
    private AdminService service = new AdminServiceImpl();

    /**
     * 登录
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public User login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "---" + password);
        if (username.length() != 0 && password.length() != 0) {
            User user = service.isAdmin(username, password);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                return user;
            } else {
//                throw new RuntimeException("账号或密码错误");
            }
        } else {
            throw new RuntimeException("信息输入不完整");
        }
        return null;
    }

    /**
     * 分页展示数据
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public PageBean<Blog> blogAsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String title = request.getParameter("title");
            String currentPageStr = request.getParameter("currentPage");
            String type = request.getParameter("type");//每页显示条数,如果不传递，默认每页显示6条记录
            System.out.println("admin 当前页：" + currentPageStr + "；标题：" + title + "；分类：" + type);
            int currentPage = 1;//当前页码，如果不传递，则默认为第一页
            if (currentPageStr != null && currentPageStr.length() > 0) {
                currentPage = Integer.parseInt(currentPageStr);
            } else {
                currentPage = 1;
            }
            int pageSize = 6;//每页显示条数，如果不传递，默认每页显示6条记录
            PageBean<Blog> page = service.blogAsList(title, currentPage, pageSize, type);
            return page;
        } else {
            throw new RuntimeException("你还没登录");
        }
    }

    /**
     * 管理员信息
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public User adminuser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        if (user != null) {
            return user;
        } else {
            throw new RuntimeException("你还没登录");
        }
    }

    /**
     * 注销账号
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            response.sendRedirect("/blog/page/login.html");
            return true;
        } else {
            throw new RuntimeException("注销失败");
        }
    }

    /**
     * 新增博客
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean insertBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String first_picture = request.getParameter("first_picture");
        String flag = request.getParameter("flag");
        String type = request.getParameter("type");
        String tagIds = request.getParameter("tagIds");
        String published = request.getParameter("published");
        System.out.println("添加标题："+title);
        if (type != null && type.length() > 0&&tagIds != null && tagIds.length() > 0&&title != null && title.length() > 0&&flag != null && flag.length() > 0&&content != null && content.length() > 0&&first_picture != null && first_picture.length() > 0) {
        } else {
            throw new RuntimeException("信息不全");
        }
        Blog blog = new Blog(title,content,first_picture,flag,type,tagIds,published);
        System.out.println(user);
        if (user != null) {
            return service.insertBlog(user.getId(),blog);
        } else {
            throw new RuntimeException("你还没登录");
        }
    }

    public boolean updateBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String first_picture = request.getParameter("first_picture");
        String flag = request.getParameter("flag");
        String type = request.getParameter("type");
        String tagIds = request.getParameter("tagIds");
        String published = request.getParameter("published");
        String blog_id = request.getParameter("blog_id");
        System.out.println("修改标题："+title);
        if (type != null && type.length() > 0&&tagIds != null && tagIds.length() > 0&&title != null && title.length() > 0&&flag != null && flag.length() > 0&&content != null && content.length() > 0&&first_picture != null && first_picture.length() > 0) {
        } else {
            throw new RuntimeException("信息不全");
        }
        Long id = null;
        if (blog_id != null && blog_id.length() > 0){
            id = Long.parseLong(blog_id);
        } else {
            throw new RuntimeException("博客id未导入");
        }
        Blog blog = new Blog(id,title,content,first_picture,flag,type,tagIds,published);
        return service.updateBlog(blog);
    }
    public boolean deleteBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blog_id = request.getParameter("blog_id");
        Long id = null;
        if (blog_id != null && blog_id.length() > 0){
            id = Long.parseLong(blog_id);
        } else {
            throw new RuntimeException("博客id未导入");
        }
        boolean b = service.deleteBlog(id);
        if (b) {
//            request.getRequestDispatcher("/blog/page/admin/blogs.html").forward(request, response);
            response.sendRedirect("/blog/page/admin/blogs.html");
        }else {
            throw new RuntimeException("删除出错");
        }
        return b;
    }
}
