package com.demo

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.*
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder
import lombok.AccessLevel
import lombok.Data
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors
import org.yaml.snakeyaml.Yaml

import static PathUtils.CODE_ROOT_PATH
import static PathUtils.RESOURCE_ROOT_PATH

@Data
@Accessors(chain = true)
class VoAutoGeneratorBuilder {

    private String parentPackage
    private String dataSourceEnv

    private String entityName = "%sDO";
    private String entityVoName = "%sVO";
    private String entityCreateVoName = "%sCreateReq";
    private String entityUpdateVoName = "%sUpdateReq";
    private String controllerName = "%sController";
    private String serviceName = "%sService";
    private String serviceImplName = "%sServiceImpl";
    private String mapperName = "%sMapper";
    private String xmlName = "%sMapper";

    private String entityPackage = "entity";
    private String entityVoPackage = "controller.vo";
    private String entityCreateVoPackage = "controller.vo";
    private String entityUpdateVoPackage = "controller.vo";
    private String controllerPackage = "controller";
    private String servicePackage = "service";
    private String serviceImplPackage = "service.impl";
    private String mapperPackage = "mapper";
    private String xmlPackage = "mapper.xml"

    /**
     * 配置信息
     */
    protected ConfigBuilder config;
    /**
     * 注入配置
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected InjectionConfig injectionConfig;
    /**
     * 数据源配置
     */
    private DataSourceConfig dataSource;
    /**
     * 数据库表配置
     */
    private StrategyConfig strategy;
    /**
     * 包 相关配置
     */
    private PackageConfig packageInfo;
    /**
     * 模板 相关配置
     */
    private TemplateConfig template;
    /**
     * 全局 相关配置
     */
    private GlobalConfig globalConfig;


    InjectionConfig getCfg() {
        return injectionConfig
    }

    void setCfg(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig
    }

    AutoGenerator build() {
        this.parentPackagePath = CODE_ROOT_PATH + File.separator + parentPackage.replaceAll("\\.", "\\" + File.separator) + File.separator

        if (null == globalConfig) {
            globalConfig = new GlobalConfig()
        }

        globalConfig.controllerName = controllerName
        globalConfig.serviceName = serviceName
        globalConfig.serviceImplName = serviceImplName
        globalConfig.mapperName = mapperName
        globalConfig.xmlName = xmlName
        globalConfig.entityName = entityName

        if (null == packageInfo) {
            packageInfo = new PackageConfig()
        }
        packageInfo.parent = parentPackage
        packageInfo.controller = controllerPackage
        packageInfo.service = servicePackage
        packageInfo.serviceImpl = serviceImplPackage
        packageInfo.mapper = mapperPackage
        packageInfo.entity = entityPackage
        packageInfo.pathInfo = [
                controller_path  : convertPackageToPath(controllerPackage),
                service_path     : convertPackageToPath(servicePackage),
                service_impl_path: convertPackageToPath(serviceImplPackage),
                mapper_path      : convertPackageToPath(mapperPackage),
                xml_path         : RESOURCE_ROOT_PATH + File.separator + xmlPackage.replaceAll("\\.", "\\" + File.separator),
                entity_path      : convertPackageToPath(entityPackage)
        ]

        this.injectionConfig = new VoInjectionConfig(
                parentPackage: this.parentPackage,
                tablePrefix: this.strategy.tablePrefix,
                naming: this.strategy.naming,
                vo: new VoInjectionConfig.Config(
                        name: entityVoName,
                        path: entityVoPackage
                ),
                createVo: new VoInjectionConfig.Config(
                        name: entityCreateVoName,
                        path: entityCreateVoPackage
                ),
                updateVo: new VoInjectionConfig.Config(
                        name: entityUpdateVoName,
                        path: entityUpdateVoPackage
                )
        )

        if (null == dataSource) {
            dataSource = resolveDataConfigFromConfigFile()
        }


        return new AutoGenerator(
                globalConfig: globalConfig,
                dataSource: dataSource,
                packageInfo: packageInfo,
                strategy: strategy,
                template: template,
                cfg: injectionConfig
        )
    }

    private String parentPackagePath

    String convertPackageToPath(String packageName) {
        return parentPackagePath + File.separator + packageName.replaceAll("\\.", "\\" + File.separator)
    }

    DataSourceConfig resolveDataConfigFromConfigFile() {
        String configFileName
        if (null == dataSourceEnv) {
            configFileName = "application.yaml"
        } else {
            configFileName = "application-" + dataSourceEnv + ".yml"
        }
        Map applicationConfig = new Yaml()
                .load(new FileInputStream(new File(RESOURCE_ROOT_PATH, configFileName)))

        //todo resolve from project application.yml
        return new DataSourceConfig(
                dbType: DbType.MYSQL,
                driverName: applicationConfig.datasource.global["driver-class-name"],
                url: applicationConfig.datasource.global.jdbcUrl,
                username: applicationConfig.datasource.global.username,
                password: applicationConfig.datasource.global.password
        )
    }
}
