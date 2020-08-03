package com.itpainter.web.filter;

import com.itpainter.domain.Info;
import com.itpainter.util.JSONUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/admini/*")
public class adminFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //将父接口转为子接口
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //获取请求方法
        String method = req.getMethod();
        //解决post请求中文数据乱码问题
        if(method.equalsIgnoreCase("post")){
            req.setCharacterEncoding("utf-8");
        }
        HttpSession session = req.getSession(false);
        if (session==null){
            String uri = req.getRequestURI();
            System.out.println("uri:"+uri);
            if ("/blog/page/admin/blogs.html".equals(uri)||"/blog/page/admin/blogs-input.html".equals(uri)||"/blog/page/admin/blogs-updaye.html".equals(uri)){
//                String schema = req.getScheme();//http
//                String host = req.getServerName();//域名或者IP
//                int port = req.getServerPort();//端口号
//                String contextPath = req.getContextPath();//项目部署名 /blog
//                String url = schema+"://"+host+":"+port+contextPath+"/page/login.html";
//                res.sendRedirect(url);
                request.getRequestDispatcher("/page/login.html").forward(request, response);
                return;
            } else if (!"/blog/admini/login".equals(uri)){
                req.setCharacterEncoding("UTF-8");
                res.setCharacterEncoding("UTF-8");
                res.setContentType("application/json");
                Info info = new Info();
                info.setErrorMsg("你还没登录，无效操作");
                info.setFlag(false);
                PrintWriter writer = res.getWriter();
                writer.println(JSONUtil.serialize(info));
                writer.flush();
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
