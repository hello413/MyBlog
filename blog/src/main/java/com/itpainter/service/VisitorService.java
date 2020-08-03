package com.itpainter.service;

import com.itpainter.domain.*;

import java.util.List;
import java.util.Map;

public interface VisitorService {
    PageBean<Blog> blogAsList(String search, int currentPage, int pageSize,String type,String tag);

    User findById(int user_id);

    List<Type> typeAsList();

    List<Blog> findByTypeId(Long id);

    List<Tag> tagAsList();

    List<Blog> findByTagId(Long id);

    PageBean<Blog> typeAsLists(String search, int currentPage, int pageSize, String type);

    Map<Integer,List<Blog>> TimeAsBlog();

    Blog Blog(String id);

    Map<Comment,List<Comment>> comment(String id);

    User isAdmin(String username, String password);
}
