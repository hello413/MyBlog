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
public class User {
    private Long id;
    private String nickname;
    private String email;
    private String avatar;
    private Integer type;
    private Date updateTime;
    private String username;
    public User() {
    }
    public User(String username) {
        this.username = username;
    }
}
