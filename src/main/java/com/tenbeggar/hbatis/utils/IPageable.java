package com.tenbeggar.hbatis.utils;

import com.tenbeggar.hbatis.wrapper.Pageable;

public class IPageable implements Pageable {

    private Object startId;
    private Long pageSize;

    public IPageable() {
    }

    public IPageable(Object startId, Long pageSize) {
        this.startId = startId;
        this.pageSize = pageSize;
    }

    @Override
    public Object getStartId() {
        return startId;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    public void setStartId(Object startId) {
        this.startId = startId;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public static IPageable.IPageableBuilder builder() {
        return new IPageable.IPageableBuilder();
    }

    public static class IPageableBuilder {
        private Object startId;
        private Long pageSize;

        IPageableBuilder() {
        }

        public IPageable.IPageableBuilder startId(final Object startId) {
            this.startId = startId;
            return this;
        }

        public IPageable.IPageableBuilder pageSize(final Long pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public IPageable build() {
            return new IPageable(this.startId, this.pageSize);
        }
    }
}
