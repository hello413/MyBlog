package itpainter.mapper;

import itpainter.model.TBlogTags;
import java.util.List;

public interface TBlogTagsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TBlogTags record);

    TBlogTags selectByPrimaryKey(Integer id);

    List<TBlogTags> selectAll();

    int updateByPrimaryKey(TBlogTags record);
}