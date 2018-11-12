package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Page<T> {
    private List<T> content;
    private int total;
    private int totalPage;
    private int currentPage;
    private int pageSize;

    public boolean isFirst() {
        return currentPage == 1;
    }

    public boolean isLast() {
        return currentPage == totalPage;
    }

    public int getCurrentSize() {
        return content.size();
    }
}
