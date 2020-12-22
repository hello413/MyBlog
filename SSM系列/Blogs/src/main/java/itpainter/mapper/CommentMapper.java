package itpainter.mapper;

import itpainter.base.BaseMapper;
import itpainter.model.Comment;
import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    Comment selectByPrimaryKey(Integer id);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);

    List<Comment> comment(int id);
}