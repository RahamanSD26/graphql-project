package com.graphql.learn.pagination;

import com.graphql.learn.exception.response.ResponseStatusDTO;
import com.graphql.learn.util.CommonUtil;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomPage<T> {
    List<T> content;
    CustomPageable pagination;
    ResponseStatusDTO status;
    public CustomPage(T object, ResponseStatusDTO status) throws Exception {
        this.content = new ArrayList<>();
        if(object!=null){
            this.content.add(object);
        }
        this.pagination = null;
        this.status = status;
    }

    public CustomPage(T object, String statusCode) throws Exception {
        this.content = new ArrayList<>();
        if(object!=null){
            this.content.add(object);
        }
        this.pagination = null;
        if(StringUtils.isNotBlank(statusCode)){
            this.status = CommonUtil.getStatusObject(statusCode,null);
        }
    }

    public CustomPage(List<T> list, String statusCode) throws Exception {
        this.content = list;
        this.pagination = null;
        if(StringUtils.isNotBlank(statusCode)){
            this.status = CommonUtil.getStatusObject(statusCode,null);
        }
    }

    public CustomPage(Page<T> page, String statusCode) throws Exception {
        this.content = page.getContent();
        this.pagination = new CustomPageable(page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(), page.getContent().size(), page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast(), page.isEmpty(), page.getSort());
        if(StringUtils.isNotBlank(statusCode)){
            this.status = CommonUtil.getStatusObject(statusCode,null);
        }
    }

    public CustomPage(Pageable pageable, List<T> list) throws Exception {
        long totalCount = list.size();
        int offSet = pageable.getPageNumber() * pageable.getPageSize();
        long totalPages = getTotalPages(totalCount, pageable.getPageSize());
        long maxOffSet = totalCount - pageable.getPageSize();
        boolean isFirst = true;
        boolean isLast = false;
        if (pageable.getPageNumber() > (totalPages - 1)) {
            isFirst = false;
            isLast = true;
            list = new ArrayList<>();
            this.content = list;
            this.pagination = new CustomPageable(pageable.getPageNumber(),
                    pageable.getPageSize(), list.size(), totalCount, totalPages, isFirst, isLast, list.isEmpty(), pageable.getSort());
        } else {
            if (offSet == 0) {
                isFirst = true;
                isLast = false;
            } else if (offSet > 0 && offSet < maxOffSet) {
                isFirst = false;
                isLast = false;
            } else {
                isFirst = false;
                isLast = true;
            }
            list = list.subList(offSet, list.size());
            list = list.stream().limit(pageable.getPageSize()).collect(Collectors.toList());
            this.content = list;
            this.pagination = new CustomPageable(pageable.getPageNumber(),
                    pageable.getPageSize(), list.size(), totalCount, totalPages, isFirst, isLast, list.isEmpty(), pageable.getSort());
        }
    }

    public long getTotalPages(long totalCount, long size) {
        long totalPages = 0L;
        long remainder = totalCount % size;
        long quotient = (totalCount - remainder) / size;
        if (remainder > 0) {
            totalPages = quotient + 1;
        } else {
            totalPages = quotient;
        }
        return totalPages;
    }

    @Data
    class CustomPageable {
        int pageNumber;
        int pageSize;
        long currentPageElements;
        long totalElements;
        long totalPages;
        boolean first;
        boolean last;
        long offSet;
        boolean empty;
        String sorting;

        public CustomPageable(int pageNumber, int pageSize, long currentPageElements, long totalElements, long totalPages, boolean first, boolean last, boolean empty, Sort sort) throws Exception {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.currentPageElements = currentPageElements;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
            this.offSet = pageNumber * pageSize;
            this.empty = empty;
            this.sorting = sort.toString();
            if(this.totalPages>0 && this.pageNumber>=this.totalPages){
                throw new Exception("ERR_ADMIN_0129");
            }
        }
    }
}