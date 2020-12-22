package itpainter.model;

import itpainter.base.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Tag extends BaseEntity {
    private Integer id;
    private String name;
    private List<Blog> blogs = new ArrayList<>();

    public Tag() {
    }
}
