package com.demo.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Mybatis-plus分页Page本身实现，只是页数和页大小，会把offset根据页数和每页大小，转成了那页的第一项，所以使用OffsetPage
 *
 * @author Administrator
 */
public class OffsetPage extends Page {

    /**
     * 偏移量
     */
    protected long offset;

    public OffsetPage(long current, long size, long offset) {
        super(current, size);
        this.offset = offset;
    }

    @Override
    public long offset() {
        return offset;
    }
}
