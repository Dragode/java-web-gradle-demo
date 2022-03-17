package com.demo


import com.baomidou.mybatisplus.generator.config.FileOutConfig
import com.baomidou.mybatisplus.generator.config.po.TableInfo

class SimpleFileOutConfig extends FileOutConfig {

    private static final String TEMPLE_PATH_PREFIX = "/templates/"
    private static final String TEMPLE_PATH_POSTFIX = ".java.vm"

    String name
    String path

    @Override
    String getTemplatePath() {
        return TEMPLE_PATH_PREFIX + name + TEMPLE_PATH_POSTFIX
    }

    @Override
    String outputFile(TableInfo tableInfo) {
        return path
    }
}
