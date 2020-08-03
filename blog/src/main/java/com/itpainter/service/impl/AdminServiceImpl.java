package com.itpainter.service.impl;

import com.itpainter.dao.AdminDao;
import com.itpainter.dao.impl.AdminDaoImpl;
import com.itpainter.domain.Blog;
import com.itpainter.domain.PageBean;
import com.itpainter.domain.User;
import com.itpainter.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private AdminDao dao = new AdminDaoImpl();

    @Override
    public User isAdmin(String username, String password) {
        return dao.isAdmin(username,password);
    }

    @Override
    public PageBean<Blog> blogAsList(String title, int currentPage, int pageSize, String type) {
        PageBean<Blog> page = new PageBean<>();
        int start = (currentPage - 1) * pageSize;
        List<Blog> list = dao.blogAsList(title, start, pageSize, type);
        page.setPageSize(pageSize);
        int totalCount = dao.countAsList(title, type);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        return page;
    }

    @Override
    public boolean insertBlog(Long id, Blog blog) {
        return dao.insertBlog(id,blog);
    }

    @Override
    public boolean updateBlog(Blog blog) {
        return dao.updateBlog(blog);
    }

    @Override
    public boolean deleteBlog(Long id) {
        return dao.deleteBlog(id);
    }
}
