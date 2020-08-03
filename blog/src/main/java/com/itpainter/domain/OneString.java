package com.itpainter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OneString<T> {
    private T str;

    public OneString() {
    }
}
