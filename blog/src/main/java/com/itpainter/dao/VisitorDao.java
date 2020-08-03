package com.itpainter.dao;

import com.itpainter.domain.*;

import java.util.List;

public interface VisitorDao {
    List<Blog> blogAsList(String search, int currentPage, int pageSize,String type,String tag);

    User findById(int user_id);

    List<Type> typeAsList();

    List<Blog> findByTypeId(Long id);

    List<Tag> tagAsList();

    List<Blog> findByTagId(Long id);

    int countAsList(String search,String type,String tag);

    List<Blog> TimeAsBlog();

    Blog BlogById(int id);

    List<Comment> comment(String id);

    User isAdmin(String username, String password);

    String findTypeNameById(Integer type_id);

    String findTagNameById(String i);
}
