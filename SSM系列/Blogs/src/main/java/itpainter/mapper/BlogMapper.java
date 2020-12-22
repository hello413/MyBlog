package itpainter.mapper;

import itpainter.base.BaseMapper;
import itpainter.model.Blog;
import itpainter.model.PageSearch;
import itpainter.model.User;

import java.util.List;

public interface BlogMapper extends BaseMapper<Blog> {
    int deleteByPrimaryKey(Integer id);

    int insert(Blog record);

    Blog selecByPrimaryKey(Integer id);

    List<Blog> selectAll();

    int updateByPrimaryKey(Blog record);

    List<Blog> blogAsList(PageSearch pageSearch);

    int countAsList(PageSearch pageSearch);

    List<Blog> findByTypeId(int id);

    List<Integer> findByTagId(int id);

    List<Blog> TimeAsBlog();

    Blog BlogById(int id);

    List<Blog> blogAsUserList(PageSearch pageSearch);

    int countAsUserList(PageSearch pageSearch);

    int selecByTitle(String title);

    void foreignStart();

    void foreignEnd();
}