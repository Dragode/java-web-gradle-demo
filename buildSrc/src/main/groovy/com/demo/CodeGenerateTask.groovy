package com.demo

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import com.baomidou.mybatisplus.generator.config.rules.DateType
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

import static PathUtils.CODE_ROOT_PATH

/**
 * gradle code generate task
 * @author dragode
 */
class CodeGenerateTask extends DefaultTask {

    private String parentPackage
    private String author
    private String dataSourceEnv
    private String[] tables

    @Inject
    CodeGenerateTask(String parentPackage) {
        this.parentPackage = parentPackage
    }


    @TaskAction
    def generateCode() {

        resolveParams()

        AutoGenerator autoGenerator = new VoAutoGeneratorBuilder(
                parentPackage: parentPackage,
                dataSourceEnv: dataSourceEnv,
                globalConfig: new GlobalConfig(
                        outputDir: CODE_ROOT_PATH,
                        open: false,
                        author: author,
                        swagger2: true,
                        //baseResultMap: true,
                        //baseColumnList: true,
                        dateType: DateType.ONLY_DATE,
                        idType: IdType.ASSIGN_ID
                ),
                //todo 添加dataSource指定说明
                // 策略配置
                strategy: new StrategyConfig(
                        tablePrefix: "t_",
                        naming: NamingStrategy.underline_to_camel,
                        columnNaming: NamingStrategy.underline_to_camel,
                        include: tables,
                        chainModel: true,
                        entityLombokModel: true,
                        restControllerStyle: true
                )
        ).build().execute()
    }

    def resolveParams() {
        String tablesString
        if (project.hasProperty("userConsole")
                && project.property("userConsole") as Boolean) {
            println "Input table names split by ,"
            tablesString = System.in.newReader().readLine()
        } else {
            tablesString = project.property("tables")
        }
        this.tables = tablesString.split(",")

        this.author = project.property("author")
        if (project.hasProperty("dataSourceEnv")) {
            this.dataSourceEnv = project.property("dataSourceEnv")
        }
    }
}
