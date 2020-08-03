package com.itpainter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@ToString
@Setter
public class Blog {
    private Long id;
    private String title;
    private String content;
    private String first_picture;
    private String flag;
    private Integer views;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private String published;
    private boolean recommend;
    private Date createTime;
    private Date updateTime;
    private Integer type_id;
    private String type;
    private String tagIds;
    private String tag;
    private List<Tag> tags = new ArrayList<>();
    private Integer user_id;
    private User user;
    private List<Comment> comments = new ArrayList<>();
    private String description;
    public Blog() { }

    public Blog(String title, String content, String first_picture, String flag,String type,String tagIds,String published) {
        this.title = title;
        this.content = content;
        this.first_picture = first_picture;
        this.flag = flag;
        this.type = type;
        this.tagIds = tagIds;
        this.published = published;
    }

    public Blog(Long id, String title, String content, String first_picture, String flag,String type,String tagIds,String published) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.first_picture = first_picture;
        this.flag = flag;
        this.type = type;
        this.tagIds = tagIds;
        this.published = published;
    }

    //1,2,3
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
