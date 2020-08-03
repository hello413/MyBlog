package com.itpainter.dao;

import com.itpainter.domain.Blog;
import com.itpainter.domain.User;

import java.util.List;

public interface AdminDao {
    User isAdmin(String username, String password);

    List<Blog> blogAsList(String title, int start, int pageSize, String type);

    int countAsList(String title, String type);

    boolean insertBlog(Long id, Blog blog);

    boolean updateBlog(Blog blog);

    boolean deleteBlog(Long id);
}
