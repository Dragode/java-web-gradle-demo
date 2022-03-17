package com.demo.util;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: omotest
 * @BelongsPackage: com.nd.mdm.core.util
 * @Author: Administrator
 * @CreateTime: 2019-10-10 16:49
 * @Description: 分页帮助类
 */
@Slf4j
@UtilityClass
public class PageUtils {
    public static final String DEFAULT_DIRECTION_DELIMITER = ",";
    private static final int DEFAULT_MAX_PAGE_SIZE = 500;

    /**
     * 获取Spring的Pageable对象
     *
     * @param offset      起始位置
     * @param limit       结束位置
     * @param sortQuery   排序字符串，例如“crTime desc,crId asc”
     * @param toCamelCase 是否要将传入属性转成camelCase，默认false
     * @return mybatis plus的page对象
     */
    public Pageable getPageable(int offset, int limit, String sortQuery, boolean toCamelCase) {
        if (limit >= 1) {
            if (limit > DEFAULT_MAX_PAGE_SIZE) {
                log.info("page size {} exceed max size, changed to default max size{}", limit, DEFAULT_MAX_PAGE_SIZE);
                limit = DEFAULT_MAX_PAGE_SIZE;
            }
        } else {
            log.info("page size {} must not be less than one, changed to one", limit);
            limit = 1;
        }
        if (offset < 0) {
            log.info("page offset {} less than zero changed to zero", offset);
            offset = 0;
        }

        PageRequest pageRequest;
        int page = offset / limit;
        if (StringUtils.isBlank(sortQuery)) {
            pageRequest = PageRequest.of(page, limit);
        } else {
            Sort sort = parseSort(sortQuery, DEFAULT_DIRECTION_DELIMITER, toCamelCase);
            pageRequest = PageRequest.of(page, limit, sort);
        }
        return pageRequest;
    }

    /**
     * @param source
     * @param directionDelimiter
     * @param toCamelCase        是否要将传入属性转成camelCase，默认false
     * @return
     */
    public static Sort parseSort(final String source, final String directionDelimiter, boolean toCamelCase) {
        log.debug("Parsing page sort matrix..." + source);
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        String[] ordersStrings = source.split(directionDelimiter);

        for (String orderParam : ordersStrings) {
            String[] parts = orderParam.split(" ");

            Sort.Direction direction = parts.length <= 1 ? null : Sort.Direction.fromString(parts[parts.length - 1]);
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1 && direction != null) {
                    continue;
                }
                String property = parts[i];
                if (StringUtils.isNotBlank(property)) {
                    if (toCamelCase) {
                        orderList.add(new Sort.Order(direction, StrUtil.toCamelCase(property)));
                    } else {
                        orderList.add(new Sort.Order(direction, property));
                    }

                }
            }
        }
        return orderList.isEmpty() ? null : Sort.by(orderList);
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 获取mybatis plus的page对象
     *
     * @param offset 起始位置
     * @param limit  结束位置
     * @param sort   排序字符串，例如“crTime desc,crId asc”
     * @return mybatis plus的page对象
     */
    public Page getPage(long offset, long limit, String sort) {
        Page page = new Page(offset, limit);
        page.setOrders(PageUtils.parseSortString(sort));
        return page;
    }

    /**
     * 转换“crTime desc,crId asc”这样的排序字段到mybatis plus用的 List<OrderItem>
     *
     * @param sort “crTime desc,crId asc”这样的排序字段
     * @return
     */
    public List<OrderItem> parseSortString(String sort) {
        List<OrderItem> orderItemList = new ArrayList<>();
        String[] split = sort.split(",");
        for (String str : split) {
            OrderItem orderItem = new OrderItem();
            String[] order = str.split(" ");
            orderItem.setColumn(humpToLine(order[0]));
            if ("desc".equals(order[1].toLowerCase())) {
                orderItem.setAsc(false);
            }
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /**
     * 驼峰转下划线
     *
     * @param humStr
     * @return
     */
    public String humpToLine(String humStr) {
        Matcher humpMatcher = humpPattern.matcher(humStr);
        StringBuffer sb = new StringBuffer();
        while (humpMatcher.find()) {
            humpMatcher.appendReplacement(sb, "_" + humpMatcher.group(0).toLowerCase());
        }
        humpMatcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 将Spring的分页转换成mybatis plus的分页
     *
     * @param pageable spring的分页
     * @return mybatis plus的分页
     */
    public Page pageableToPage(Pageable pageable) {
        Page page = new OffsetPage(pageable.getPageNumber() + 1, pageable.getPageSize(),pageable.getOffset());
        if (pageable.getSort() != null) {
            List<OrderItem> orderItemList = new ArrayList<>();
            Iterator<Sort.Order> it;
            for (it = pageable.getSort().iterator(); it.hasNext(); ) {
                Sort.Order order = it.next();
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(order.getProperty());
                orderItem.setAsc(order.getDirection().isAscending());
                orderItemList.add(orderItem);
            }
            if (CollectionUtils.isNotEmpty(orderItemList)) {
                page.setOrders(orderItemList);
            }
        }
        return page;
    }
}
