package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class DependencyInjectionGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] INJECTION_TYPES = {
            "hilt", "dagger", "koin", "manual"
    };

    private static final String[] SCOPE_TYPES = {
            "Singleton", "ViewModelScoped", "ActivityScoped", "FragmentScoped",
            "ApplicationScoped", "ServiceScoped", "RepositoryScoped", "UseCaseScoped"
    };

    private static final String[] DEPENDENCY_TYPES = {
            "Repository", "Manager", "Helper", "Service", "UseCase", "DataSource", "Cache"
    };

    private static final String[] FEATURE_TYPES = {
            "caching", "logging", "validation", "retry", "timeout", "fallback", "circuitBreaker"
    };

    public DependencyInjectionGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成依赖注入类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Module");
            generateInjectionClass(className, asyncHandler);
        }
    }

    private void generateInjectionClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("di"));

        // 基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.concurrent.ConcurrentHashMap"));

        String injectionType = INJECTION_TYPES[RandomUtils.between(0, INJECTION_TYPES.length - 1)];
        String scopeType = SCOPE_TYPES[RandomUtils.between(0, SCOPE_TYPES.length - 1)];

        if (injectionType.contains("hilt")) {
            sb.append(generateImportStatement("dagger.Module"));
            sb.append(generateImportStatement("dagger.Provides"));
            sb.append(generateImportStatement("dagger.Binds"));
            sb.append(generateImportStatement("dagger.hilt.InstallIn"));
            sb.append(generateImportStatement("androidx.hilt.components.ViewModelComponent"));
            sb.append(generateImportStatement("androidx.hilt.components.ActivityComponent"));
            sb.append(generateImportStatement("androidx.hilt.components.ApplicationComponent"));
        } else if (injectionType.contains("dagger")) {
            sb.append(generateImportStatement("dagger.Module"));
            sb.append(generateImportStatement("dagger.Provides"));
            sb.append(generateImportStatement("dagger.Binds"));
            sb.append(generateImportStatement("dagger.Component"));
            sb.append(generateImportStatement("javax.inject.Singleton"));
        } else if (injectionType.contains("koin")) {
            sb.append(generateImportStatement("org.koin.dsl.module"));
            sb.append(generateImportStatement("org.koin.core.module.Module"));
            sb.append(generateImportStatement("org.koin.core.context.startKoin"));
            sb.append(generateImportStatement("org.koin.core.context.stopKoin"));
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        }

        sb.append(generateImportStatement(packageName + ".repository.*"));
        sb.append(generateImportStatement(packageName + ".manager.*"));
        sb.append(generateImportStatement(packageName + ".helper.*"));
        sb.append(generateImportStatement(packageName + ".service.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useCache = RandomUtils.randomBoolean();
        boolean useLogging = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useLazy = RandomUtils.randomBoolean();
        boolean useFactory = RandomUtils.randomBoolean();
        boolean useQualifier = RandomUtils.randomBoolean();
        boolean useProvider = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();

        // 生成类定义
        if (injectionType.contains("hilt")) {
            sb.append("@Module\n");
            if (scopeType.equals("Singleton")) {
                sb.append("@InstallIn(ApplicationComponent.class)\n");
            } else if (scopeType.equals("ActivityScoped")) {
                sb.append("@InstallIn(ActivityComponent.class)\n");
            } else {
                sb.append("@InstallIn(ViewModelComponent.class)\n");
            }
            sb.append("public class ").append(className).append(" {\n\n");
        } else if (injectionType.contains("koin")) {
            sb.append("public class ").append(className).append(" {\n\n");
        } else {
            sb.append("public class ").append(className).append(" {\n\n");
        }

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String INJECTION_TYPE = \"").append(injectionType).append("\";\n");
        sb.append("    private static final String SCOPE_TYPE = \"").append(scopeType).append("\";\n\n");

        // 根据功能标志生成字段
        if (useCache) {
            sb.append("    private final Map<String, Object> cache = new ConcurrentHashMap<>();\n");
            sb.append("    private static final int MAX_CACHE_SIZE = 100;\n");
        }

        if (useLogging) {
            sb.append("    private boolean loggingEnabled = true;\n");
        }

        if (useValidation) {
            sb.append("    private boolean validationEnabled = true;\n");
        }

        if (useLazy) {
            sb.append("    private final Map<Class<?>, Object> lazyInstances = new ConcurrentHashMap<>();\n");
        }

        if (useFactory) {
            sb.append("    private final Map<Class<?>, java.util.function.Supplier<?>> factories = new ConcurrentHashMap<>();\n");
        }

        if (useProvider) {
            sb.append("    private final Map<Class<?>, java.util.function.Supplier<?>> providers = new ConcurrentHashMap<>();\n");
        }

        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 初始化方法 - 使用所有生成的字段
        sb.append("    private void initialize() {\n");
        sb.append("        Log.d(TAG, \"Initializing ").append(className).append(" with \").append(INJECTION_TYPE);\n");

        if (useCache) {
            sb.append("        cache.clear();\n");
        }

        if (useLazy) {
            sb.append("        lazyInstances.clear();\n");
        }

        if (useFactory) {
            sb.append("        factories.clear();\n");
        }

        if (useProvider) {
            sb.append("        providers.clear();\n");
        }

        sb.append("    }\n\n");

        // 单例模式
        sb.append("    private static volatile ").append(className).append(" instance;\n\n");

        sb.append("    public static ").append(className).append(" getInstance() {\n");
        sb.append("        if (instance == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (instance == null) {\n");
        sb.append("                    instance = new ").append(className).append("();\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return instance;\n");
        sb.append("    }\n\n");

        // 缓存相关方法
        if (useCache) {
            sb.append("    private void putCache(String key, Object value) {\n");
            sb.append("        if (cache.size() >= MAX_CACHE_SIZE) {\n");
            sb.append("            cache.clear();\n");
            sb.append("        }\n");
            sb.append("        cache.put(key, value);\n");
            sb.append("    }\n\n");

            sb.append("    private Object getCache(String key) {\n");
            sb.append("        return cache.get(key);\n");
            sb.append("    }\n\n");

            sb.append("    public void clearCache() {\n");
            sb.append("        cache.clear();\n");
            sb.append("    }\n\n");
        }

        // 日志相关方法
        if (useLogging) {
            sb.append("    public void setLoggingEnabled(boolean enabled) {\n");
            sb.append("        this.loggingEnabled = enabled;\n");
            sb.append("    }\n\n");

            sb.append("    private void log(String message) {\n");
            sb.append("        if (loggingEnabled) {\n");
            sb.append("            Log.d(TAG, message);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 验证相关方法
        if (useValidation) {
            sb.append("    public void setValidationEnabled(boolean enabled) {\n");
            sb.append("        this.validationEnabled = enabled;\n");
            sb.append("    }\n\n");

            sb.append("    private boolean validate(Object obj) {\n");
            sb.append("        if (!validationEnabled) {\n");
            sb.append("            return true;\n");
            sb.append("        }\n");
            sb.append("        boolean isValid = obj != null;\n");
            sb.append("        log(\"Validation result: \" + isValid);\n");
            sb.append("        return isValid;\n");
            sb.append("    }\n\n");
        }

        // 懒加载相关方法
        if (useLazy) {
            sb.append("    private <T> T getLazy(Class<T> clazz, java.util.function.Supplier<T> supplier) {\n");
            sb.append("        return clazz.cast(lazyInstances.computeIfAbsent(clazz, k -> supplier.get()));\n");
            sb.append("    }\n\n");

            sb.append("    public void clearLazyInstances() {\n");
            sb.append("        lazyInstances.clear();\n");
            sb.append("    }\n\n");
        }

        // 工厂相关方法
        if (useFactory) {
            sb.append("    public <T> void registerFactory(Class<T> clazz, java.util.function.Supplier<T> factory) {\n");
            sb.append("        factories.put(clazz, factory);\n");
            sb.append("        log(\"Registered factory for: \" + clazz.getSimpleName());\n");
            sb.append("    }\n\n");

            sb.append("    private <T> T createFromFactory(Class<T> clazz) {\n");
            sb.append("        java.util.function.Supplier<?> factory = factories.get(clazz);\n");
            sb.append("        if (factory != null) {\n");
            sb.append("            T instance = clazz.cast(factory.get());\n");
            sb.append("            log(\"Created instance from factory: \" + clazz.getSimpleName());\n");
            sb.append("            return instance;\n");
            sb.append("        }\n");
            sb.append("        return null;\n");
            sb.append("    }\n\n");
        }

        // Provider相关方法
        if (useProvider) {
            sb.append("    public <T> void registerProvider(Class<T> clazz, java.util.function.Supplier<T> provider) {\n");
            sb.append("        providers.put(clazz, provider);\n");
            sb.append("        log(\"Registered provider for: \" + clazz.getSimpleName());\n");
            sb.append("    }\n\n");

            sb.append("    private <T> T getFromProvider(Class<T> clazz) {\n");
            sb.append("        java.util.function.Supplier<?> provider = providers.get(clazz);\n");
            sb.append("        if (provider != null) {\n");
            sb.append("            T instance = clazz.cast(provider.get());\n");
            sb.append("            log(\"Got instance from provider: \" + clazz.getSimpleName());\n");
            sb.append("            return instance;\n");
            sb.append("        }\n");
            sb.append("        return null;\n");
            sb.append("    }\n\n");
        }

        // 依赖提供方法 - 根据injectionType生成不同风格的依赖注入代码
        if (injectionType.contains("hilt")) {
            // 生成Hilt风格的@Provides方法
            for (int i = 0; i < RandomUtils.between(2, 4); i++) {
                String depType = DEPENDENCY_TYPES[RandomUtils.between(0, DEPENDENCY_TYPES.length - 1)];
                String depClassName = RandomUtils.generateClassName(depType);

                sb.append("    @Provides\n");
                if (useQualifier) {
                    sb.append("    @Named(\"").append(depClassName).append("\")\n");
                }
                sb.append("    @").append(scopeType).append("\n");
                sb.append("    public static ").append(depClassName).append(" provide").append(depClassName).append("(\n");

                // 添加依赖参数
                int depCount = RandomUtils.between(0, 2);
                for (int j = 0; j < depCount; j++) {
                    String paramType = DEPENDENCY_TYPES[RandomUtils.between(0, DEPENDENCY_TYPES.length - 1)];
                    String paramClassName = RandomUtils.generateClassName(paramType);
                    sb.append("        ").append(paramClassName).append(" ").append(Character.toLowerCase(paramClassName.charAt(0))).append(paramClassName.substring(1));
                    if (j < depCount - 1) {
                        sb.append(",\n");
                    }
                }
                sb.append("\n    ) {\n");
                sb.append("        ").append(depClassName).append(" instance = new ").append(depClassName).append("(");

                // 添加构造参数
                for (int j = 0; j < depCount; j++) {
                    sb.append(Character.toLowerCase(depClassName.charAt(0))).append(depClassName.substring(1));
                    if (j < depCount - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(");\n");
                sb.append("        Log.d(TAG, \"Providing: \").append(depClassName);\n");
                sb.append("        return instance;\n");
                sb.append("    }\n\n");
            }
        } else if (injectionType.contains("koin")) {
            // 生成Koin风格的模块定义
            sb.append("    public Module createKoinModule() {\n");
            sb.append("        return module {\n");

            for (int i = 0; i < RandomUtils.between(2, 4); i++) {
                String depType = DEPENDENCY_TYPES[RandomUtils.between(0, DEPENDENCY_TYPES.length - 1)];
                String depClassName = RandomUtils.generateClassName(depType);

                sb.append("            single { ").append(depClassName).append("(get()) }\n");
            }

            sb.append("        };\n");
            sb.append("    }\n\n");
        } else {
            // 生成手动依赖注入方法
            for (int i = 0; i < RandomUtils.between(2, 4); i++) {
                String depType = DEPENDENCY_TYPES[RandomUtils.between(0, DEPENDENCY_TYPES.length - 1)];
                String depClassName = RandomUtils.generateClassName(depType);

                sb.append("    public ").append(depClassName).append(" get").append(depClassName).append("() {\n");
                sb.append("        String cacheKey = \"").append(depClassName).append("\";\n");

                if (useCache) {
                    sb.append("        Object cached = getCache(cacheKey);\n");
                    sb.append("        if (cached != null) {\n");
                    sb.append("            return (").append(depClassName).append(") cached;\n");
                    sb.append("        }\n");
                }

                if (useFactory) {
                    sb.append("        ").append(depClassName).append(" instance = createFromFactory(").append(depClassName).append(".class);\n");
                    sb.append("        if (instance != null) {\n");
                    if (useCache) {
                        sb.append("            putCache(cacheKey, instance);\n");
                    }
                    sb.append("            return instance;\n");
                    sb.append("        }\n");
                }

                if (useProvider) {
                    sb.append("        instance = getFromProvider(").append(depClassName).append(".class);\n");
                    sb.append("        if (instance != null) {\n");
                    if (useCache) {
                        sb.append("            putCache(cacheKey, instance);\n");
                    }
                    sb.append("            return instance;\n");
                    sb.append("        }\n");
                }

                sb.append("        ").append(depClassName).append(" newInstance = new ").append(depClassName).append("();\n");
                if (useValidation) {
                    sb.append("        if (!validate(newInstance)) {\n");
                    sb.append("            Log.e(TAG, \"Validation failed for: \").append(depClassName);\n");
                    sb.append("            return null;\n");
                    sb.append("        }\n");
                }
                if (useCache) {
                    sb.append("        putCache(cacheKey, newInstance);\n");
                }
                sb.append("        log(\"Created new instance: \").append(depClassName);\n");
                sb.append("        return newInstance;\n");
                sb.append("    }\n\n");
            }
        }

        // 生命周期方法
        sb.append("    public void initializeModule() {\n");
        sb.append("        log(\"Initializing module: \").append(className);\n");

        if (useFactory) {
            sb.append("        registerFactories();\n");
        }

        if (useProvider) {
            sb.append("        registerProviders();\n");
        }

        sb.append("    }\n\n");

        if (useFactory) {
            sb.append("    private void registerFactories() {\n");
            sb.append("        // 注册工厂方法\n");
            sb.append("        registerFactory(String.class, () -> \"default\");\n");
            sb.append("        registerFactory(Integer.class, () -> 0);\n");
            sb.append("        registerFactory(Boolean.class, () -> false);\n");
            sb.append("    }\n\n");
        }

        if (useProvider) {
            sb.append("    private void registerProviders() {\n");
            sb.append("        // 注册提供者方法\n");
            sb.append("        registerProvider(String.class, () -> \"provided\");\n");
            sb.append("        registerProvider(Integer.class, () -> 1);\n");
            sb.append("        registerProvider(Boolean.class, () -> true);\n");
            sb.append("    }\n\n");
        }

        // 清理方法
        sb.append("    public void cleanup() {\n");
        sb.append("        log(\"Cleaning up module: \").append(className);\n");

        if (useCache) {
            sb.append("        clearCache();\n");
        }

        if (useLazy) {
            sb.append("        clearLazyInstances();\n");
        }

        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "di");
    }
}
