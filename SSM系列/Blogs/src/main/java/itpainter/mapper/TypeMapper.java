package itpainter.mapper;

import itpainter.base.BaseMapper;
import itpainter.model.Type;
import java.util.List;

public interface TypeMapper extends BaseMapper<Type> {
    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    Type selectByPrimaryKey(Integer id);

    List<Type> selectAll();

    int updateByPrimaryKey(Type record);

    List<Type> typeAsList();

    String findTypeNameById(int id);
}