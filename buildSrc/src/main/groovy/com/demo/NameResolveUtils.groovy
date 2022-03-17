package com.demo

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy

class NameResolveUtils {

    private String tablePrefix
    private NamingStrategy namingStrategy

    NameResolveUtils(String tablePrefix, NamingStrategy namingStrategy) {
        this.tablePrefix = tablePrefix
        this.namingStrategy = namingStrategy
    }

    String resolveName(String tableName) {
        return NamingStrategy.capitalFirst(processName(tableName, namingStrategy, tablePrefix))
    }

    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        String propertyName;
        if (ArrayUtils.isNotEmpty(prefix)) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

}
