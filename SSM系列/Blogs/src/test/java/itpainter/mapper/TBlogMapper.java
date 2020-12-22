package itpainter.mapper;

import itpainter.model.TBlog;
import java.util.List;

public interface TBlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TBlog record);

    TBlog selectByPrimaryKey(Integer id);

    List<TBlog> selectAll();

    int updateByPrimaryKey(TBlog record);
}