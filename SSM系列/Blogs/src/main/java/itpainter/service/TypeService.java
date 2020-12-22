package itpainter.service;

import itpainter.mapper.BlogMapper;
import itpainter.mapper.TypeMapper;
import itpainter.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;

    public List<Type> typeAsList() {
        return typeMapper.typeAsList();
    }
}
