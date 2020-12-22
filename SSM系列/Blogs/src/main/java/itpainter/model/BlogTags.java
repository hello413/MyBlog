package itpainter.model;

import itpainter.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BlogTags extends BaseEntity {
    private Integer blogsId;
    private Integer tagsId;

    public BlogTags(Integer blogsId, Integer tagsId) {
        this.blogsId = blogsId;
        this.tagsId = tagsId;
    }
}
