package com.demo

import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.po.TableInfo
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import org.apache.commons.lang3.StringUtils

class VoInjectionConfig extends InjectionConfig {

    private String parentPackage
    private Config vo
    private Config createVo
    private Config updateVo

    private String tablePrefix
    private NamingStrategy naming

    @Override
    void initMap() {
    }

    @Override
    void initTableMap(TableInfo tableInfo) {
        super.initTableMap(tableInfo)

        NameResolveUtils nameResolveUtils = new NameResolveUtils(tablePrefix, naming)
        String entityName = NamingStrategy.capitalFirst(nameResolveUtils.resolveName(tableInfo.getName()))
        PathUtils pathUtils = new PathUtils(this.parentPackage)

        String entityVoName = String.format(vo.name, entityName)
        String entityVoPackage = parentPackage + "." + vo.path
        String entityVoPath = pathUtils.convertPackageToPath(vo.path)

        String createVoName = String.format(createVo.name, entityName)
        String createVoPackage = parentPackage + "." + createVo.path
        String createVoPath = pathUtils.convertPackageToPath(createVo.path)

        String updateVoName = String.format(updateVo.name, entityName)
        String updateVoPackage = parentPackage + "." + updateVo.path
        String updateVoPath = pathUtils.convertPackageToPath(updateVo.path)

        this.map = [
                StringUtils: new StringUtils(),
                "package"  : [
                        parentPackage : parentPackage,
                        entityVO      : entityVoPackage,
                        entityCreateVO: createVoPackage,
                        entityUpdateVO: updateVoPackage
                ],
                "table"    : [
                        origEntityName    : tableInfo.name,
                        entityVoName      : entityVoName,
                        entityCreateVoName: createVoName,
                        entityUpdateVoName: updateVoName
                ]
        ]

        this.fileOutConfigList = [
                new SimpleFileOutConfig(
                        name: "entityVO",
                        path: entityVoPath + File.separator + entityVoName + ".java"
                ),
                new SimpleFileOutConfig(
                        name: "entityCreateVO",
                        path: createVoPath + File.separator + createVoName + ".java"
                ),
                new SimpleFileOutConfig(
                        name: "entityUpdateVO",
                        path: updateVoPath + File.separator + updateVoName + ".java"
                )
        ]
    }

    static class Config {
        private String name
        private String path
    }
}
