package com.itpainter.service.impl;

import com.itpainter.dao.VisitorDao;
import com.itpainter.dao.impl.VisitorDaoImpl;
import com.itpainter.domain.*;
import com.itpainter.service.VisitorService;

import java.util.*;

public class VisitorServiceImpl implements VisitorService {
    private VisitorDao dao = new VisitorDaoImpl();

    @Override
    public PageBean<Blog> blogAsList(String search, int currentPage, int pageSize, String type, String tag) {
        PageBean<Blog> page = new PageBean<>();
        int start = (currentPage - 1) * pageSize;
        List<Blog> list = dao.blogAsList(search, start, pageSize, type, tag);
        System.out.println("visit:当前页："+currentPage+"开始计数："+start+"查询数："+pageSize+"；标题："+search+"；分类"+type+"；标签："+tag);
        for (int i = 0; i < list.size(); i++) {
            int user_id = list.get(i).getUser_id();
            list.get(i).setUser(dao.findById(user_id));
        }
        page.setPageSize(pageSize);
        int totalCount = dao.countAsList(search, type, tag);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        return page;
    }

    @Override
    public PageBean<Blog> typeAsLists(String search, int currentPage, int pageSize, String type) {
        PageBean<Blog> page = new PageBean<>();
        int start = (currentPage - 1) * pageSize;
        List<Blog> list = dao.blogAsList(search, start, pageSize, type, null);
        for (int i = 0; i < list.size(); i++) {
            int user_id = list.get(i).getUser_id();
            list.get(i).setUser(dao.findById(user_id));
        }
        page.setPageSize(pageSize);
        int totalCount = dao.countAsList(search, type, null);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        return page;
    }

    @Override
    public Map<Integer, List<Blog>> TimeAsBlog() {
        Map<Integer, List<Blog>> map = new HashMap<>();
        List<Blog> blogs = dao.TimeAsBlog();
        for (Blog i : blogs) {
            Calendar c = Calendar.getInstance();
            c.setTime(i.getUpdateTime());
            int year = c.get(Calendar.YEAR);
            List list = new ArrayList();
            if (map.containsKey(year)) {
                list = map.get(year);
                list.add(i);
            } else {
                list.add(i);
            }
            map.put(year, list);
        }
        return map;
    }

    @Override
    public Blog Blog(String id) {
        Blog blog = dao.BlogById(Integer.parseInt(id));
        int user_id = blog.getUser_id();
        blog.setUser(dao.findById(user_id));
        Integer type_id = blog.getType_id();
        blog.setType(dao.findTypeNameById(type_id));
        String tagIds = blog.getTagIds();
        String[] split = tagIds.split(",");
        String result = "";
        for (String i:split){
            if (i.length()>0) {
                result += dao.findTagNameById(i) + ",";
            }
        }
        result = result.substring(0,result.length()-1);
        System.out.println("tag:"+result);
        blog.setTag(result);
        return blog;
    }

    @Override
    public User isAdmin(String username, String password) {
        return dao.isAdmin(username, password);
    }

    @Override
    public Map<Comment, List<Comment>> comment(String id) {
        List<Comment> list = dao.comment(id);
        Map<Comment, List<Comment>> map = new HashMap<>();
        for (Comment c : list) {
            boolean flag = false;
            if (c.getParentCommentId() == null) {
                map.put(c, new ArrayList<Comment>());
                flag = true;
            } else {
                Long pid = c.getParentCommentId();
                for (Comment set : map.keySet()) {
                    if (set.getId() == pid) {
                        List<Comment> list1 = map.get(set);
                        list1.add(c);
                        map.put(set, list1);
                        flag = true;
                        break;
                    } else {
                        List<Comment> list1 = map.get(set);
                        for (Comment i : list1) {
                            if (i.getId() == pid) {
                                list1.add(c);
                                map.put(set, list1);
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!flag) {
                throw new RuntimeException("没找到论评位置或父评论");
            }
        }
        return map;
    }

    @Override
    public User findById(int user_id) {
        return dao.findById(user_id);
    }

    @Override
    public List<Type> typeAsList() {
        return dao.typeAsList();
    }

    @Override
    public List<Blog> findByTypeId(Long id) {
        return dao.findByTypeId(id);
    }

    @Override
    public List<Tag> tagAsList() {
        return dao.tagAsList();
    }

    @Override
    public List<Blog> findByTagId(Long id) {
        return dao.findByTagId(id);
    }

}
