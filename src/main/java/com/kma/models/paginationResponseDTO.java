package com.kma.models;

import java.util.List;

public class paginationResponseDTO<T> {
    private List<T> content;         // Danh sách dữ liệu
    private Integer totalPages;          // Tổng số trang
    private Integer totalElements;      // Tổng số phần tử
    private boolean isFirst;         // Có phải trang đầu không
    private boolean isLast;          // Có phải trang cuối không
    private Integer currentPage;         // Trang hiện tại
    private Integer pageSize;            // Số lượng phần tử mỗi trang

    public paginationResponseDTO(List<T> content, Integer totalPages, Integer totalElements, boolean isFirst, boolean isLast, Integer currentPage, Integer pageSize) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
