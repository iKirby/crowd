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
        if (totalPage > 0) {
            return currentPage == totalPage;
        } else {
            return true;
        }
    }

    public int getCurrentSize() {
        return content.size();
    }

    public void setTotal(int total) {
        this.total = total;
        updatePageInfo();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        updatePageInfo();
    }

    private void updatePageInfo() {
        if (total == 0 || pageSize == 0) {
            return;
        }
        setTotalPage(total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    }

    private void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
