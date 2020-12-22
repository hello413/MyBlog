package itpainter.mapper;

import itpainter.base.BaseMapper;
import itpainter.model.BlogTags;
import java.util.List;

public interface BlogTagsMapper extends BaseMapper<BlogTags> {
    int insert(BlogTags record);

    List<BlogTags> selectAll();

    void deleteByBlogId(int id);
}