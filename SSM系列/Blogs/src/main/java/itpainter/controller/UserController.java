package itpainter.controller;

import itpainter.exception.ClientException;
import itpainter.exception.SystemException;
import itpainter.model.Blog;
import itpainter.model.PageBean;
import itpainter.model.PageSearch;
import itpainter.model.User;
import itpainter.service.BlogService;
import itpainter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
//@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;//注意一定要在类型上加上@Service，注册到容器中

    @Autowired
    private BlogService blogService;

    @PostMapping("/login")
    @ResponseBody
    public Object login(User user, HttpServletRequest req) {
        //如果用户名密码校验失败，在service中抛异常，这里exist一定不为null
        User exist = userService.login(user);
        HttpSession session = req.getSession();
        session.setAttribute("user", exist);
        return null;
    }

    /**
     * 注销功能：get api/user/logout
     */
    @GetMapping("/logout")
    public Object logout(HttpSession session) {
        if (session != null)
            session.removeAttribute("user");
        return "/login.html";
    }

    @PostMapping("/blogAsList")
    @ResponseBody
    public Object blogAsList(PageSearch pageSearch, HttpServletRequest req) {
        //search:查询标签；currentPageStr:当前页码；pageSizeStr：每页显示条数
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (pageSearch.getSearch() == null || pageSearch.getSearch().equals("null")) pageSearch.setSearch("");
            System.out.println(pageSearch.toString());
            PageBean<Blog> page = blogService.blogAsUserList(pageSearch);
            return page;
        } else {
            throw new ClientException("400000", "你还没登录");
        }
    }

    @PostMapping("/adminuser")
    @ResponseBody
    public Object adminuser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");

            if (user != null) {
                return user;
            } else {
                throw new ClientException("400000", "你还没登录");
            }
        } else {
            throw new ClientException("400000", "你还没登录");
        }
    }

    @PostMapping("/insertBlog")
    @ResponseBody
    public Object insertBlog(HttpServletRequest request, Blog blog) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        System.out.println(blog);
        if (blog.getTypeId() != null && blog.getTagIds() != null && blog.getTagIds().length() > 0
                && blog.getTitle() != null && blog.getTitle().length() > 0 &&
                blog.getFlag() != null && blog.getFlag().length() > 0 &&
                blog.getContent() != null && blog.getContent().length() > 0 &&
                blog.getFirstPicture() != null && blog.getFirstPicture().length() > 0) {
        } else {
            throw new ClientException("400001", "信息不全");
        }
        if (user != null) {
            return blogService.insertBlog(user.getId(), blog);
        } else {
            throw new ClientException("400000", "你还没登录");
        }
    }

    @PostMapping("/updateBlog")
    @ResponseBody
    public Object updateBlog(HttpServletRequest request, Blog blog) {
        System.out.println(blog.toString());
        if (blog.getTypeId() != null && blog.getTagIds() != null && blog.getTagIds().length() > 0
                && blog.getTitle() != null && blog.getTitle().length() > 0 &&
                blog.getFlag() != null && blog.getFlag().length() > 0 &&
                blog.getContent() != null && blog.getContent().length() > 0 &&
                blog.getFirstPicture() != null && blog.getFirstPicture().length() > 0) {
        } else {
            throw new ClientException("400001", "信息不全");
        }
        if (blog.getId() == null) {
            throw new SystemException("300001", "博客id未导入");
        }
        return blogService.updateBlog(blog);
    }

    @GetMapping("/deleteBlog")
    public Object deleteBlog(int blog_id) {
        blogService.deleteBlog(blog_id);
        return "/admin/blogs.html";
    }

//    @PostMapping("/register")
//    public Object register(User user,
//                           //上传的头像：1.保存在本地文件夹（web服务器需要加载到）2.url存放在注册用户的head字段
//                           @RequestPart(value = "headFile", required = false) MultipartFile headFile){
//        //没有做服务器请求数据的校验，实现方式：1.手动校验2.使用validation框架校验（很多注解）
//        //TODO
//        userService.register(user, headFile);
//        return null;
//    }
//

}
