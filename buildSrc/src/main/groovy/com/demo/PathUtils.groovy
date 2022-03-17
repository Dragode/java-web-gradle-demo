package com.demo;

class PathUtils {

    public static final String CODE_ROOT_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java";
    public static final String RESOURCE_ROOT_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";

    private String parentPackagePath;

    PathUtils(String parentPackage) {
        this.parentPackagePath = CODE_ROOT_PATH + File.separator + parentPackage.replaceAll("\\.", "\\" + File.separator) + File.separator;
    }

    String convertPackageToPath(String packageName) {
        return parentPackagePath + File.separator + packageName.replaceAll("\\.", "\\" + File.separator);
    }
}
