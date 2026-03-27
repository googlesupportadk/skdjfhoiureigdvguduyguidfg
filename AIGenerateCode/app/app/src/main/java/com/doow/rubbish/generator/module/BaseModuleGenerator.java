package com.doow.rubbish.generator.module;

/**
 * 模块生成器基类
 * 所有模块生成器的基类，提供基础功能
 */
public abstract class BaseModuleGenerator extends BaseCodeGenerator {

    public BaseModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
    }

    /**
     * 生成模块包名
     * @param moduleName 模块名称
     * @return 模块包名
     */
    protected String getModulePackage(String moduleName) {
        return packageName + ".module." + moduleName.toLowerCase();
    }

    /**
     * 生成模块类名
     * @param moduleName 模块名称
     * @return 模块类名
     */
    protected String getModuleClassName(String moduleName) {
        return moduleName + "Module";
    }
}
