package com.itpainter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Comment {
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    private Date createTime;
    private int blog_id;
    private List<Comment> replyComments = new ArrayList<>();
    private Long parentCommentId;

    private boolean adminComment;

    public Comment() {
    }
}
