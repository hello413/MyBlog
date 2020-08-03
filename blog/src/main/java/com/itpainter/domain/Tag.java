package com.itpainter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Tag {
    private Long id;
    private String name;
    private List<Blog> blogs = new ArrayList<>();

    public Tag() {
    }
}
