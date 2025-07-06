package com.jeffjackson.enquiry.model;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> content;
    private Meta meta;

    public PaginatedResponse(Page<T> page) {
        this.content = page.getContent();
        this.meta = new Meta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    // Getters
    public List<T> getContent() { return content; }
    public Meta getMeta() { return meta; }

    public static class Meta {
        private final int currentPage;
        private final int pageSize;
        private final long totalItems;
        private final int totalPages;
        private final boolean hasNext;
        private final boolean hasPrevious;

        public Meta(int currentPage, int pageSize, long totalItems,
                    int totalPages, boolean hasNext, boolean hasPrevious) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
        }

        // Getters
        public int getCurrentPage() { return currentPage; }
        public int getPageSize() { return pageSize; }
        public long getTotalItems() { return totalItems; }
        public int getTotalPages() { return totalPages; }
        public boolean isHasNext() { return hasNext; }
        public boolean isHasPrevious() { return hasPrevious; }
    }
}