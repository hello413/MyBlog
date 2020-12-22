package itpainter.model;

import itpainter.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Comment extends BaseEntity {
    private Integer id;
    private String avatar;
    private String content;
    private Date createTime;
    private String email;
    private Integer blogId;
    private Integer parentCommentId;
    private Integer commentId;
    private String nickname;

    private List<Comment> replyComments = new ArrayList<>();

    private boolean adminComment;
}
