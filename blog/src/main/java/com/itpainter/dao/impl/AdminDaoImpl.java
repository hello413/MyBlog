package com.itpainter.dao.impl;

import com.itpainter.dao.AdminDao;
import com.itpainter.domain.Blog;
import com.itpainter.domain.Type;
import com.itpainter.domain.User;
import com.itpainter.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AdminDaoImpl implements AdminDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private static final String SALT = "itpainter";

    @Override
    public User isAdmin(String username, String password) {
        password = md5(password);
        System.out.println(password);
        String sql = "select * from t_user where type = 1 and username = ? and password = ?";
        User user =null;
        try {
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e) {
            throw new RuntimeException("账号或密码错误");
        }
        return user;
    }

    @Override
    public List<Blog> blogAsList(String title, int start, int pageSize, String type) {
        String sql = "select blog.*,type.name type from t_blog blog,t_type type where blog.type_id=type.id";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        if(type != null && type.length() > 0 && !type.equals("null")){
            sb.append(" and name like ? ");
            params.add("%"+type+"%");
        }
        if(title != null && title.length() > 0 && !title.equals("null")){
            sb.append(" and title like ? ");
            params.add("%"+title+"%");
        }
        sb.append(" order by update_time desc limit ?, ?");
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
    public int countAsList(String title, String type) {
        String sql = "select COUNT(*) from t_blog,t_type where t_blog.type_id=t_type.id";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        if(type != null && type.length() > 0 && !type.equals("null")){
            sb.append(" and name like ? ");
            params.add("%"+type+"%");
        }
        if(title != null && title.length() > 0 && !title.equals("null")){
            sb.append(" and title like ? ");
            params.add("%"+title+"%");
        }
        sql = sb.toString();
        Integer i = 0;
        try {
            i = template.queryForObject(sql, Integer.class,params.toArray());
        } catch (Exception e) {
            throw new RuntimeException("查询博客列表数量失败");
        }
        return i;
    }

    private Long findByTypeName(String name) {
        if(!isNumeric(name)) {
            Type type = null;
            try {
                //1.定义sql
                String sql = "select * from t_type where name = ?";
                //2.执行sql
                type = template.queryForObject(sql, new BeanPropertyRowMapper<Type>(Type.class), name);
            } catch (Exception e) {

            }
            //没找到,则新建
            if (type == null) {
                String sql0 = "INSERT INTO t_type(name) VALUES (?)";
                try {
                    int i = template.update(sql0, name);
                } catch (DataAccessException e) {
                    throw new RuntimeException("添加文章分类失败");
                }
                return findByTypeName(name);
            } else {
                return type.getId();
            }
        }else {
            return Long.parseLong(name);
        }
    }

    private String findByTagIds(String tagIds) {
        String result = "";
        String[] split = tagIds.split(",");
        for (String i:split){
            if (i.length()>0&&!isNumeric(i)) {
                String li = findByTagName(i) + "";
                result += li + ",";
            }else {
                result +=i+",";
            }
        }
        result = result.substring(0,result.length()-1);
        System.out.println(result);
        return result;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private Long findByTagName(String name) {
        Type type = null;
        try {
            //1.定义sql
            String sql = "select * from t_tag where name = ?";
            //2.执行sql
            type = template.queryForObject(sql, new BeanPropertyRowMapper<Type>(Type.class), name);
        } catch (Exception e) {

        }
        //没找到,则新建
        if (type==null){
            String sql0 = "INSERT INTO t_tag(name) VALUES (?)";
            try {
                int i = template.update(sql0, name);
            } catch (DataAccessException e) {
                throw new RuntimeException("添加标签失败");
            }
            return findByTagName(name);
        }else {
            return type.getId();
        }
    }

    @Override
    public boolean insertBlog(Long id, Blog blog) {
        String sql = "INSERT INTO t_blog(content,first_picture,flag,published,title,type_id,user_id,tag_ids) VALUES (?, ?, ?, ?,?, ?, ?, ?)";
        Integer i = null;
        try {
            i = template.update(sql, blog.getContent(), blog.getFirst_picture(), blog.getFlag(), blog.getPublished(), blog.getTitle(), findByTypeName(blog.getType()), id, findByTagIds(blog.getTagIds()));
        } catch (DataAccessException e) {
            throw new RuntimeException("添加文章失败");
        }
        String sql0 = "select id from t_blog where title = ?";
        Integer j = null;
        try {
            j = template.queryForObject(sql0,Integer.class, blog.getTitle());
        } catch (DataAccessException e) {
            throw new RuntimeException("查询文章Id失败");
        }
        System.out.println(j);
        insertingType(j,blog.getTagIds());
        return i!=0;
    }

    private void insertingType(Integer j, String tagIds) {
        String[] split = tagIds.split(",");
        for (String i:split){
            String li;
            if (i.length()>0&&!isNumeric(i)) {
                li = findByTagName(i) + "";
            }else {
                li = i;
            }
            insert(j,li);
        }
    }

    private void insert(Integer j, String li) {
        String sql = "INSERT INTO t_blog_tags VALUES (?, ?)";
        Integer i = null;
        try {
            i = template.update(sql, j,li);
        } catch (DataAccessException e) {
            throw new RuntimeException("添加文章失败");
        }
    }

    @Override
    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE t_blog SET content = ?, first_picture = ?, flag = ?, published = ?, title = ?, update_time = now(), type_id = ?, tag_ids = ? WHERE (id = ?)";
        Integer i = null;
        try {
            i = template.update(sql, blog.getContent(), blog.getFirst_picture(), blog.getFlag(), blog.getPublished(), blog.getTitle(), findByTypeName(blog.getType()), findByTagIds(blog.getTagIds()),blog.getId());
        } catch (DataAccessException e) {
            throw new RuntimeException("修改文章失败");
        }
        return i!=0;
    }

    @Override
    public boolean deleteBlog(Long id) {
        String sql0 = "DELETE FROM t_blog_tags WHERE(blogs_id = ?)";
        template.update(sql0,id);
        String sql1 = "SET FOREIGN_KEY_CHECKS = 0;";
        template.update(sql1);
        String sql = "DELETE FROM t_blog WHERE(id = ?);";
        Integer i = null;
//        try {
            i = template.update(sql, id);
//        } catch (DataAccessException e) {
//            throw new RuntimeException("删除文章失败");
//        }
        String sql2 = "SET FOREIGN_KEY_CHECKS = 1;";
        template.update(sql2);
        System.out.println("i:"+i);
        return i!=0;
    }

    private static String md5(String password){
        return SALT.charAt(5)+ DigestUtils.md5DigestAsHex(password.getBytes())+SALT.substring(2,5);
    }
}
