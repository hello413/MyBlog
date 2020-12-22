package itpainter.service;

import itpainter.mapper.TagMapper;
import itpainter.mapper.TypeMapper;
import itpainter.model.Tag;
import itpainter.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> tagAsList() {
        return tagMapper.selectAll();
    }
}
