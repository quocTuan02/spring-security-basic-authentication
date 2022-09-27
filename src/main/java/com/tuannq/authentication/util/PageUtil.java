package com.tuannq.authentication.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageUtil {
    private PageRequest pageRequest;

    public PageUtil(Integer page, Integer limit, String order, String direction) {
        if (order == null || order.equals("")) {
            this.pageRequest = PageRequest.of(pageNumber(page), limit);
            return;
        }
        if (direction == null || direction.equals("")) {
            this.pageRequest = PageRequest.of(pageNumber(page), limit, Sort.by(order).descending());
            return;
        }
        switch (direction.toUpperCase()) {
            case "ASC":
                this.pageRequest = PageRequest.of(pageNumber(page), limit, Sort.by(order).ascending());
                break;
            case "DESC":
                this.pageRequest = PageRequest.of(pageNumber(page), limit, Sort.by(order).descending());
                break;
        }
    }

    public PageUtil(Integer page, Integer limit, String order) {
        this.pageRequest = PageRequest.of(pageNumber(page), limit, Sort.by(order).descending());
    }

    public PageUtil(Integer page, Integer limit) {
        this.pageRequest = PageRequest.of(pageNumber(page), limit);
    }

    public Pageable getPageRequest() {
        return pageRequest;
    }


    private Integer pageNumber(Integer page) {
        return Optional.of(page).map(p -> {
            if (p > 0) return p - 1;
            else return 0;
        }).orElse(0);
    }
}
