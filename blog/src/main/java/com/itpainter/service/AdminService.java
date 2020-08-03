package com.itpainter.service;

import com.itpainter.domain.Blog;
import com.itpainter.domain.PageBean;
import com.itpainter.domain.User;

public interface AdminService {
    User isAdmin(String username, String password);

    PageBean<Blog> blogAsList(String title, int currentPage, int pageSize, String type);

    boolean insertBlog(Long id, Blog blog);

    boolean updateBlog(Blog blog);

    boolean deleteBlog(Long id);
}
