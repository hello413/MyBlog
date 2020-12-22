package itpainter.service;

import itpainter.exception.BusinessException;
import itpainter.mapper.*;
import itpainter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagsMapper blogTagsMapper;


    public PageBean<Blog> blogAsList(PageSearch pageSearch) {
        int currentPage = pageSearch.getCurrentPage();//当前页码，如果不传递，则默认为第一页
        int pageSize = pageSearch.getPageSize();//每页显示条数，如果不传递，默认每页显示6条记录
        PageBean<Blog> page = new PageBean<>();
        int start = (currentPage - 1) * pageSize;
        pageSearch.setStart(start);
        List<Blog> list = blogMapper.blogAsList(pageSearch);
        for (int i = 0; i < list.size(); i++) {
            int user_id = list.get(i).getUserId();
            list.get(i).setUser(userMapper.selectByPrimaryKey(user_id));
        }
        page.setPageSize(pageSize);

        int totalCount = blogMapper.countAsList(pageSearch);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        return page;
    }

    public PageBean<Blog> blogAsUserList(PageSearch pageSearch) {
        int currentPage = pageSearch.getCurrentPage();//当前页码，如果不传递，则默认为第一页
        int pageSize = pageSearch.getPageSize();//每页显示条数，如果不传递，默认每页显示6条记录
        PageBean<Blog> page = new PageBean<>();
        int start = (currentPage - 1) * pageSize;
        pageSearch.setStart(start);
        List<Blog> list = blogMapper.blogAsUserList(pageSearch);
        page.setPageSize(pageSize);
        int totalCount = blogMapper.countAsUserList(pageSearch);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        return page;
    }

    public List<Blog> findByTypeId(int id) {
        return blogMapper.findByTypeId(id);
    }

    public List<Blog> findByTagId(int id) {
        List<Integer> listIds = blogMapper.findByTagId(id);
        List<Blog> list = new ArrayList<>();
        for (Integer i : listIds) {
            Blog blog = blogMapper.selectByPrimaryKey(i);
            list.add(blog);
        }
        return list;
    }

    public Map<Integer, List<Blog>> TimeAsBlog() {
        Map<Integer, List<Blog>> map = new HashMap<>();
        List<Blog> blogs = blogMapper.TimeAsBlog();
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

    public Blog Blog(int id) {
        Blog blog = blogMapper.BlogById(id);
        int user_id = blog.getUserId();
        blog.setUser(userMapper.selectByPrimaryKey(user_id));
        Integer type_id = blog.getTypeId();
        blog.setType(typeMapper.findTypeNameById(id));
        String tagIds = blog.getTagIds();
        String[] split = tagIds.split(",");
        String result = "";
        for (String i : split) {
            if (i.length() > 0) {
                result += tagMapper.findTagNameById(i) + ",";
            }
        }
        result = result.substring(0, result.length() - 1);
        System.out.println("tag:" + result);
        blog.setTag(result);
        return blog;
    }

    @Transactional
    public Object insertBlog(Integer id, Blog blog) {
        blog.setUserId(id);
        int i = 0;
        int blogId = 0;
        System.out.println(blog);
//        try {
            i = blogMapper.insert(blog);
//        } catch (Exception e) {
//            throw new BusinessException("500001","添加文章失败");
//        }
        try {
            blogId = blogMapper.selecByTitle(blog.getTitle());
        } catch (DataAccessException e) {
            throw new BusinessException("500003","查询文章Id失败");
        }
        insertingType(blogId, blog.getTagIds());
        return i != 0;
    }

    private void insertingType(int blogId, String tagIds) {
        String[] split = tagIds.split(",");
        for (String i : split) {
            String li;
            if (i.length() > 0 && !isNumeric(i)) {
                li = findByTagName(i) + "";
            } else {
                li = i;
            }
            insert(blogId, li);
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private int findByTagName(String name) {
        Type type = null;
        try {
            type = tagMapper.selectByName(name);
        } catch (Exception e) {

        }
        //没找到,则新建
        if (type == null) {
            tagMapper.insertName(name);
            return findByTagName(name);
        } else {
            return type.getId();
        }
    }

    private void insert(Integer j, String li) {
        System.out.println(j);
        System.out.println(li);
        int i = Integer.parseInt(li);
        blogTagsMapper.insert(new BlogTags(j, i));
    }


    public Object updateBlog(Blog blog) {
        try {
            blogMapper.updateByPrimaryKey(blog);
        } catch (Exception e) {
            throw new BusinessException("500004","修改文章失败");
        }
        return true;
    }

    public void deleteBlog(int id) {
//        try {
            blogTagsMapper.deleteByBlogId(id);
            blogMapper.foreignStart();
            blogMapper.deleteByPrimaryKey(id);
            blogMapper.foreignEnd();
//        } catch (Exception e) {
//            throw new BusinessException("500002","删除文章出错");
//        }
    }
}
