package itpainter.mapper;

import itpainter.base.BaseMapper;
import itpainter.model.Tag;
import itpainter.model.Type;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    Tag selectByPrimaryKey(Integer id);

    List<Tag> selectAll();

    int updateByPrimaryKey(Tag record);

    String findTagNameById(String id);

    Type selectByName(String name);

    void insertName(String name);
}