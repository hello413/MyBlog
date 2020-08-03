package com.itpainter.dao.impl;

import com.itpainter.dao.VisitorDao;
import com.itpainter.domain.*;
import com.itpainter.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class VisitorDaoImpl implements VisitorDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Blog> blogAsList(String search, int start, int pageSize,String type,String tag) {
        String sql = "select distinct tb.* from t_blog tb,t_blog_tags tbt where tbt.blogs_id = tb.id and tb.published = 1";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        if(type != null && type.length() > 0 && !type.equals("null")){
            sb.append(" and tb.type_id = ? ");
            params.add(type);
        }
        if(tag != null && tag.length() > 0 && !tag.equals("null")){
            sb.append(" and tbt.tags_id = ? ");
            params.add(tag);
        }
        if(search != null && search.length() > 0 && !search.equals("null")){
            sb.append(" and tb.title like ? ");
            params.add("%"+search+"%");
        }
        sb.append(" order by tb.update_time desc limit ?, ?");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        List<Blog> list = null;
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class),params.toArray());
        } catch (Exception e) {
            throw new RuntimeException("查询博客列表失败");
        }
        return list;
    }
    @Override
    public int countAsList(String search, String type,String tag) {
        String sql = "select COUNT(*) from (select distinct tb.* from t_blog tb,t_blog_tags tbt where tbt.blogs_id = tb.id and tb.published = 1";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        if(type != null && type.length() > 0 && !type.equals("null")){
            sb.append(" and tb.type_id = ? ");
            params.add(type);
        }
        if(tag != null && tag.length() > 0 && !tag.equals("null")){
            sb.append(" and tbt.tags_id = ? ");
            params.add(tag);
        }
        if(search != null && search.length() > 0 && !search.equals("null")){
            sb.append(" and tb.title like ? ");
            params.add("%"+search+"%");
        }
        sb.append(") temp;");
        sql = sb.toString();
        Integer i = 0;
        try {
            i = template.queryForObject(sql, Integer.class,params.toArray());
        } catch (Exception e) {
            throw new RuntimeException("查询博客列表数量失败");
        }
        return i;
    }

    @Override
    public List<Blog> TimeAsBlog() {
        String sql = "select * from t_blog where published = 1 order by update_time desc";
        List<Blog> list = null;
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class));
        } catch (Exception e) {
            throw new RuntimeException("查询博客时间时分类列表失败");
        }
        return list;
    }

    @Override
    public Blog BlogById(int id) {
        String sql ="select * from t_blog where id = ? order by id";
        Blog blog = null;
        try {
            blog = template.queryForObject(sql,new BeanPropertyRowMapper<Blog>(Blog.class),id);
        }catch (Exception e){
            throw new RuntimeException("获取博客文章信息失败");
        }
        return blog;
    }

    @Override
    public List<Comment> comment(String id) {
        String sql ="select c.* from t_comment c,t_blog b where b.published = 1 and b.id = c.blog_id and c.blog_id = ?";
        List<Comment> list = null;
        try {
            list = template.query(sql,new BeanPropertyRowMapper<Comment>(Comment.class),id);
        }catch (Exception e){
            throw new RuntimeException("获取博客文章评论信息失败");
        }
        return list;
    }

    @Override
    public User isAdmin(String username, String password) {
        String sql = "select * from t_user where username = ? and password = ? and type = 0";
        User user =null;
        try {
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String findTypeNameById(Integer id) {
        String sql = "select name from t_type where id = ?";
        String i = null;
        try {
            i = template.queryForObject(sql, String.class,id);
        } catch (Exception e) {
            throw new RuntimeException("查询分类名字失败");
        }
        return i;
    }

    @Override
    public String findTagNameById(String id) {
        String sql = "select name from t_tag where id = ?";
        String i = null;
        try {
            i = template.queryForObject(sql, String.class,id);
        } catch (Exception e) {
            throw new RuntimeException("查询标签名字失败");
        }
        return i;
    }

    @Override
    public User findById(int user_id) {
        String sql ="select * from t_user where id = ?";
        User user = null;
        try {
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),user_id);
        }catch (Exception e){
            throw new RuntimeException("获取博主信息失败");
        }
        return user;
    }

    @Override
    public List<Type> typeAsList() {
        String sql = "select distinct tt.* from t_type tt join t_blog tb on tb.type_id = tt.id where tb.published = 1;";
        List<Type> list = null;
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Type>(Type.class));
        } catch (Exception e) {
            throw new RuntimeException("查询博客分类失败");
        }
        return list;
    }

    @Override
    public List<Blog> findByTypeId(Long id) {
        String sql = "select * from t_blog where published = 1 and type_id = ?";
        List<Blog> list = null;
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class),id);
        } catch (Exception e) {
            throw new RuntimeException("查询博客分类列表失败");
        }
        return list;
    }

    @Override
    public List<Tag> tagAsList() {
        String sql = "select tt.* from t_tag tt";
        List<Tag> list = null;
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Tag>(Tag.class));
        } catch (Exception e) {
            throw new RuntimeException("查询博客标签失败");
        }
        return list;
    }

    @Override
    public List<Blog> findByTagId(Long id) {
        String sql = "select tbt.blogs_id as str from t_blog_tags tbt,t_blog tb where tb.published = 1 and tbt.blogs_id = tb.id and tbt.tags_id = ?";
        List<Blog> list = new ArrayList<>();
        try {
            List<OneString> list1 = template.query(sql,new BeanPropertyRowMapper<OneString>(OneString.class),id);
            for (OneString i:list1) {
                Blog blog = findByBlogId((long) i.getStr());
                list.add(blog);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询博客标签列表失败");
        }
        return list;
    }
    public Blog findByBlogId(Long id) {
        String sql ="select * from t_blog where published = 1 and id = ?";
        Blog blog = null;
        try {
            blog = template.queryForObject(sql,new BeanPropertyRowMapper<Blog>(Blog.class),id);
        }catch (Exception e){
            throw new RuntimeException("获取博客文章信息失败");
        }
        return blog;
    }
}
