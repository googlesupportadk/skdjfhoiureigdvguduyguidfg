package com.doow.rubbish.generator;

import com.doow.rubbish.generator.code.*;
import com.doow.rubbish.generator.resource.*;

import com.doow.rubbish.generator.module.*;


public class MainEntry {

    private String projectRoot;
    private String packageName;
    private ConfigLoader configLoader;
    private ConfigValidator configValidator;
    private ProgressTracker progressTracker;
    private Logger logger;
    private ErrorHandler errorHandler;
    private GenerationStats generationStats;

    public MainEntry(String projectRoot, String packageName) {
        this.projectRoot = projectRoot;
        this.packageName = packageName;


        this.configLoader = new ConfigLoader();
        this.configValidator = new ConfigValidator();
        this.progressTracker = ProgressTracker.getInstance();
        this.logger = Logger.getInstance();
        this.errorHandler = ErrorHandler.getInstance();
        this.generationStats = GenerationStats.getInstance();
        

        createProjectStructure();
    }
    

    private void createProjectStructure() {
        java.io.File appDir = new java.io.File(projectRoot + "/app");
        java.io.File srcDir = new java.io.File(projectRoot + "/app/src");
        java.io.File mainDir = new java.io.File(projectRoot + "/app/src/main");
        java.io.File javaDir = new java.io.File(projectRoot + "/app/src/main/java");
        java.io.File resDir = new java.io.File(projectRoot + "/app/src/main/res");
        
        if (!appDir.exists()) appDir.mkdirs();
        if (!srcDir.exists()) srcDir.mkdirs();
        if (!mainDir.exists()) mainDir.mkdirs();
        if (!javaDir.exists()) javaDir.mkdirs();
        if (!resDir.exists()) resDir.mkdirs();
    }


    public void generate() throws Exception {
        try {

            logger.initLogFile(projectRoot);
            logger.phaseStart("初始化");


            logger.info("加载配置文件");
            configLoader.load();
            configLoader.applyToGeneratorConfig();
            configLoader.printConfig();


            logger.info("验证配置");
            ConfigValidator.ValidationResult result = configValidator.validate(projectRoot, packageName);
            result.print();

            if (!result.isValid()) {
                errorHandler.handleError("配置验证", "配置验证失败", null);
                return;
            }

            logger.phaseComplete("初始化");


            progressTracker.setTotalSteps(95);
            progressTracker.reset();


            ResourceTracker.clearAll();
            ClassTracker.clearAll();


            generateResources();


            generateCoreClasses();

            // 第三阶段：生成UI组件
            generateUIComponents();

            // 第四阶段：生成系统组件
            generateSystemComponents();

            // 第五阶段：生成工具类
            generateUtils();

            // 第六阶段：生成选中的模块
            generateSelectedModules();


            generateConfigs();


            progressTracker.complete();
            generationStats.printStats();

            logger.phaseComplete("生成完成");


            printCompletion();

        } catch (Exception e) {
            errorHandler.handleError("主生成流程", "生成过程中发生错误", e);
            throw e;
        } finally {
            logger.close();
        }
    }

    private void generateResources() throws Exception {
        logger.phaseStart("资源文件生成");
        progressTracker.startPhase("资源文件生成", 4);

        try {
            logger.info("生成Values资源");
            ValuesGenerator valuesGenerator = new ValuesGenerator(projectRoot, packageName);
            valuesGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Drawable资源");
            DrawableGenerator drawableGenerator = new DrawableGenerator(projectRoot, packageName);
            drawableGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Layout资源");
            LayoutGenerator layoutGenerator = new LayoutGenerator(projectRoot, packageName);
            layoutGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Raw资源");
            RawGenerator rawGenerator = new RawGenerator(projectRoot, packageName);
            rawGenerator.generateAll();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("资源文件生成");
        } catch (Exception e) {
            errorHandler.handleError("资源文件生成", "生成资源文件时发生错误", e);
            throw e;
        }
    }

    private void generateCoreClasses() throws Exception {
        logger.phaseStart("核心类生成");
        progressTracker.startPhase("核心类生成", 4);

        try {
            logger.info("生成Model类");
            ModelGenerator modelGenerator = new ModelGenerator(projectRoot, packageName);
            modelGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Repository类");
            RepositoryGenerator repositoryGenerator = new RepositoryGenerator(projectRoot, packageName);
            repositoryGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成ViewModel类");
            ViewModelGenerator viewModelGenerator = new ViewModelGenerator(projectRoot, packageName);
            viewModelGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成LiveData类");
            LiveDataGenerator liveDataGenerator = new LiveDataGenerator(projectRoot, packageName);
            liveDataGenerator.generateAll();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("核心类生成");
        } catch (Exception e) {
            errorHandler.handleError("核心类生成", "生成核心类时发生错误", e);
            throw e;
        }
    }

    private void generateUIComponents() throws Exception {
        logger.phaseStart("UI组件生成");
        progressTracker.startPhase("UI组件生成", 5);

        try {
            logger.info("生成Adapter类");
            AdapterGenerator adapterGenerator = new AdapterGenerator(projectRoot, packageName);
            adapterGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Fragment类");
            FragmentGenerator fragmentGenerator = new FragmentGenerator(projectRoot, packageName);
            fragmentGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Activity类");
            ActivityGenerator activityGenerator = new ActivityGenerator(projectRoot, packageName);
            activityGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Dialog类");
            DialogGenerator dialogGenerator = new DialogGenerator(projectRoot, packageName);
            dialogGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成CustomView类");
            CustomViewGenerator customViewGenerator = new CustomViewGenerator(projectRoot, packageName);
            customViewGenerator.generateAll();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("UI组件生成");
        } catch (Exception e) {
            errorHandler.handleError("UI组件生成", "生成UI组件时发生错误", e);
            throw e;
        }
    }

    private void generateSystemComponents() throws Exception {
        logger.phaseStart("系统组件生成");
        progressTracker.startPhase("系统组件生成", 4);

        try {
            logger.info("生成WorkManager类");
            WorkManagerGenerator workManagerGenerator = new WorkManagerGenerator(projectRoot, packageName);
            workManagerGenerator.generateAll();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("系统组件生成");
        } catch (Exception e) {
            errorHandler.handleError("系统组件生成", "生成系统组件时发生错误", e);
            throw e;
        }
    }

    private void generateUtils() throws Exception {
        logger.phaseStart("工具类生成");
        progressTracker.startPhase("工具类生成", 77);

        try {
            // 生成符合离线、安全、隐私要求的本地功能生成器
            
            logger.info("生成Helper类");
            HelperGenerator helperGenerator = new HelperGenerator(projectRoot, packageName);
            helperGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Manager类");
            ManagerGenerator managerGenerator = new ManagerGenerator(projectRoot, packageName);
            managerGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Utils类");
            UtilsGenerator utilsGenerator = new UtilsGenerator(projectRoot, packageName);
            utilsGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成Preference类");
            PreferenceGenerator preferenceGenerator = new PreferenceGenerator(projectRoot, packageName);
            preferenceGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成DataBinding类");
            DataBindingGenerator dataBindingGenerator = new DataBindingGenerator(projectRoot, packageName);
            dataBindingGenerator.generateAll();
            progressTracker.updateProgress(1);

            logger.info("生成ViewModelFactory类");
            ViewModelFactoryGenerator viewModelFactoryGenerator = new ViewModelFactoryGenerator(projectRoot, packageName);
            viewModelFactoryGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地数学相关代码
            logger.info("生成本地数学相关代码");
            LocalMathGenerator localMathGenerator = new LocalMathGenerator(projectRoot, packageName);
            localMathGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地计算相关代码
            logger.info("生成本地计算相关代码");
            LocalCalculatorGenerator localCalculatorGenerator = new LocalCalculatorGenerator(projectRoot, packageName);
            localCalculatorGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地计算功能相关代码
            logger.info("生成本地计算功能相关代码");
            LocalCalculationGenerator localCalculationGenerator = new LocalCalculationGenerator(projectRoot, packageName);
            localCalculationGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地几何相关代码
            logger.info("生成本地几何相关代码");
            LocalGeometryGenerator localGeometryGenerator = new LocalGeometryGenerator(projectRoot, packageName);
            localGeometryGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地代数相关代码
            logger.info("生成本地代数相关代码");
            LocalAlgebraGenerator localAlgebraGenerator = new LocalAlgebraGenerator(projectRoot, packageName);
            localAlgebraGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地三角函数相关代码
            logger.info("生成本地三角函数相关代码");
            LocalTrigonometryGenerator localTrigonometryGenerator = new LocalTrigonometryGenerator(projectRoot, packageName);
            localTrigonometryGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地逻辑相关代码
            logger.info("生成本地逻辑相关代码");
            LocalLogicGenerator localLogicGenerator = new LocalLogicGenerator(projectRoot, packageName);
            localLogicGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地数据结构相关代码
            logger.info("生成本地数据结构相关代码");
            LocalDataStructureGenerator localDataStructureGenerator = new LocalDataStructureGenerator(projectRoot, packageName);
            localDataStructureGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地集合相关代码
            logger.info("生成本地集合相关代码");
            LocalCollectionGenerator localCollectionGenerator = new LocalCollectionGenerator(projectRoot, packageName);
            localCollectionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地映射相关代码
            logger.info("生成本地映射相关代码");
            LocalMapGenerator localMapGenerator = new LocalMapGenerator(projectRoot, packageName);
            localMapGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地列表相关代码
            logger.info("生成本地列表相关代码");
            LocalListGenerator localListGenerator = new LocalListGenerator(projectRoot, packageName);
            localListGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地队列相关代码
            logger.info("生成本地队列相关代码");
            LocalQueueGenerator localQueueGenerator = new LocalQueueGenerator(projectRoot, packageName);
            localQueueGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地栈相关代码
            logger.info("生成本地栈相关代码");
            LocalStackGenerator localStackGenerator = new LocalStackGenerator(projectRoot, packageName);
            localStackGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地字符串相关代码
            logger.info("生成本地字符串相关代码");
            LocalStringGenerator localStringGenerator = new LocalStringGenerator(projectRoot, packageName);
            localStringGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地日期时间相关代码
            logger.info("生成本地日期时间相关代码");
            LocalDateTimeGenerator localDateTimeGenerator = new LocalDateTimeGenerator(projectRoot, packageName);
            localDateTimeGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地数字相关代码
            logger.info("生成本地数字相关代码");
            LocalNumberGenerator localNumberGenerator = new LocalNumberGenerator(projectRoot, packageName);
            localNumberGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地数组相关代码
            logger.info("生成本地数组相关代码");
            LocalArrayGenerator localArrayGenerator = new LocalArrayGenerator(projectRoot, packageName);
            localArrayGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地流相关代码
            logger.info("生成本地流相关代码");
            LocalStreamGenerator localStreamGenerator = new LocalStreamGenerator(projectRoot, packageName);
            localStreamGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地选项相关代码
            logger.info("生成本地选项相关代码");
            LocalOptionalGenerator localOptionalGenerator = new LocalOptionalGenerator(projectRoot, packageName);
            localOptionalGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地比较器相关代码
            logger.info("生成本地比较器相关代码");
            LocalComparatorGenerator localComparatorGenerator = new LocalComparatorGenerator(projectRoot, packageName);
            localComparatorGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地函数相关代码
            logger.info("生成本地函数相关代码");
            LocalFunctionGenerator localFunctionGenerator = new LocalFunctionGenerator(projectRoot, packageName);
            localFunctionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地谓词相关代码
            logger.info("生成本地谓词相关代码");
            LocalPredicateGenerator localPredicateGenerator = new LocalPredicateGenerator(projectRoot, packageName);
            localPredicateGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地消费者相关代码
            logger.info("生成本地消费者相关代码");
            LocalConsumerGenerator localConsumerGenerator = new LocalConsumerGenerator(projectRoot, packageName);
            localConsumerGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地提供者相关代码
            logger.info("生成本地提供者相关代码");
            LocalSupplierGenerator localSupplierGenerator = new LocalSupplierGenerator(projectRoot, packageName);
            localSupplierGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地反射相关代码
            logger.info("生成本地反射相关代码");
            LocalReflectionGenerator localReflectionGenerator = new LocalReflectionGenerator(projectRoot, packageName);
            localReflectionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地注解相关代码
            logger.info("生成本地注解相关代码");
            LocalAnnotationGenerator localAnnotationGenerator = new LocalAnnotationGenerator(projectRoot, packageName);
            localAnnotationGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地泛型相关代码
            logger.info("生成本地泛型相关代码");
            LocalGenericGenerator localGenericGenerator = new LocalGenericGenerator(projectRoot, packageName);
            localGenericGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地异常相关代码
            logger.info("生成本地异常相关代码");
            LocalExceptionGenerator localExceptionGenerator = new LocalExceptionGenerator(projectRoot, packageName);
            localExceptionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地日志相关代码
            logger.info("生成本地日志相关代码");
            LocalLogGenerator localLogGenerator = new LocalLogGenerator(projectRoot, packageName);
            localLogGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地缓存相关代码
            logger.info("生成本地缓存相关代码");
            LocalCacheGenerator localCacheGenerator = new LocalCacheGenerator(projectRoot, packageName);
            localCacheGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地配置相关代码
            logger.info("生成本地配置相关代码");
            LocalConfigGenerator localConfigGenerator = new LocalConfigGenerator(projectRoot, packageName);
            localConfigGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地安全相关代码
            logger.info("生成本地安全相关代码");
            LocalSecurityGenerator localSecurityGenerator = new LocalSecurityGenerator(projectRoot, packageName);
            localSecurityGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地数据库相关代码
            logger.info("生成本地数据库相关代码");
            LocalDatabaseGenerator localDatabaseGenerator = new LocalDatabaseGenerator(projectRoot, packageName);
            localDatabaseGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地JSON相关代码
            logger.info("生成本地JSON相关代码");
            LocalJsonGenerator localJsonGenerator = new LocalJsonGenerator(projectRoot, packageName);
            localJsonGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地XML相关代码
            logger.info("生成本地XML相关代码");
            LocalXmlGenerator localXmlGenerator = new LocalXmlGenerator(projectRoot, packageName);
            localXmlGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地正则表达式相关代码
            logger.info("生成本地正则表达式相关代码");
            LocalRegexGenerator localRegexGenerator = new LocalRegexGenerator(projectRoot, packageName);
            localRegexGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地日期相关代码
            logger.info("生成本地日期相关代码");
            LocalDateGenerator localDateGenerator = new LocalDateGenerator(projectRoot, packageName);
            localDateGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地时间相关代码
            logger.info("生成本地时间相关代码");
            LocalTimeGenerator localTimeGenerator = new LocalTimeGenerator(projectRoot, packageName);
            localTimeGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地编码相关代码
            logger.info("生成本地编码相关代码");
            LocalEncodingGenerator localEncodingGenerator = new LocalEncodingGenerator(projectRoot, packageName);
            localEncodingGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地压缩相关代码
            logger.info("生成本地压缩相关代码");
            LocalCompressionGenerator localCompressionGenerator = new LocalCompressionGenerator(projectRoot, packageName);
            localCompressionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地动画相关代码
            logger.info("生成本地动画相关代码");
            LocalAnimationGenerator localAnimationGenerator = new LocalAnimationGenerator(projectRoot, packageName);
            localAnimationGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地动画器相关代码
            logger.info("生成本地动画器相关代码");
            LocalAnimatorGenerator localAnimatorGenerator = new LocalAnimatorGenerator(projectRoot, packageName);
            localAnimatorGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地图表相关代码
            logger.info("生成本地图表相关代码");
            LocalChartGenerator localChartGenerator = new LocalChartGenerator(projectRoot, packageName);
            localChartGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地时钟相关代码
            logger.info("生成本地时钟相关代码");
            LocalClockGenerator localClockGenerator = new LocalClockGenerator(projectRoot, packageName);
            localClockGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地颜色相关代码
            logger.info("生成本地颜色相关代码");
            LocalColorGenerator localColorGenerator = new LocalColorGenerator(projectRoot, packageName);
            localColorGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地图形相关代码
            logger.info("生成本地图形相关代码");
            LocalGraphicsGenerator localGraphicsGenerator = new LocalGraphicsGenerator(projectRoot, packageName);
            localGraphicsGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地笔记相关代码
            logger.info("生成本地笔记相关代码");
            LocalNoteGenerator localNoteGenerator = new LocalNoteGenerator(projectRoot, packageName);
            localNoteGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地概率相关代码
            logger.info("生成本地概率相关代码");
            LocalProbabilityGenerator localProbabilityGenerator = new LocalProbabilityGenerator(projectRoot, packageName);
            localProbabilityGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地集合Set相关代码
            logger.info("生成本地集合Set相关代码");
            LocalSetGenerator localSetGenerator = new LocalSetGenerator(projectRoot, packageName);
            localSetGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地统计相关代码
            logger.info("生成本地统计相关代码");
            LocalStatisticsGenerator localStatisticsGenerator = new LocalStatisticsGenerator(projectRoot, packageName);
            localStatisticsGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地工具相关代码
            logger.info("生成本地工具相关代码");
            LocalToolGenerator localToolGenerator = new LocalToolGenerator(projectRoot, packageName);
            localToolGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 本地待办事项相关代码
            logger.info("生成本地待办事项相关代码");
            LocalTodoGenerator localTodoGenerator = new LocalTodoGenerator(projectRoot, packageName);
            localTodoGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 动画相关代码
            logger.info("生成动画相关代码");
            AnimationGenerator animationGenerator = new AnimationGenerator(projectRoot, packageName);
            animationGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 缓存相关代码
            logger.info("生成缓存相关代码");
            CacheGenerator cacheGenerator = new CacheGenerator(projectRoot, packageName);
            cacheGenerator.generateAll();
            progressTracker.updateProgress(1);

            // Compose相关代码
            logger.info("生成Compose相关代码");
            ComposeGenerator composeGenerator = new ComposeGenerator(projectRoot, packageName);
            composeGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 协程相关代码
            logger.info("生成协程相关代码");
            CoroutineGenerator coroutineGenerator = new CoroutineGenerator(projectRoot, packageName);
            coroutineGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 协程作用域相关代码
            logger.info("生成协程作用域相关代码");
            CoroutineScopeGenerator coroutineScopeGenerator = new CoroutineScopeGenerator(projectRoot, packageName);
            coroutineScopeGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 数据库相关代码
            logger.info("生成数据库相关代码");
            DatabaseGenerator databaseGenerator = new DatabaseGenerator(projectRoot, packageName);
            databaseGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 依赖注入相关代码
            logger.info("生成依赖注入相关代码");
            DependencyInjectionGenerator dependencyInjectionGenerator = new DependencyInjectionGenerator(projectRoot, packageName);
            dependencyInjectionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 枚举相关代码
            logger.info("生成枚举相关代码");
            EnumGenerator enumGenerator = new EnumGenerator(projectRoot, packageName);
            enumGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 异常相关代码
            logger.info("生成异常相关代码");
            ExceptionGenerator exceptionGenerator = new ExceptionGenerator(projectRoot, packageName);
            exceptionGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 接口相关代码
            logger.info("生成接口相关代码");
            InterfaceGenerator interfaceGenerator = new InterfaceGenerator(projectRoot, packageName);
            interfaceGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 导航相关代码
            logger.info("生成导航相关代码");
            NavigationGenerator navigationGenerator = new NavigationGenerator(projectRoot, packageName);
            navigationGenerator.generateAll();
            progressTracker.updateProgress(1);

            // 验证器相关代码
            logger.info("生成验证器相关代码");
            ValidatorGenerator validatorGenerator = new ValidatorGenerator(projectRoot, packageName);
            validatorGenerator.generateAll();
            progressTracker.updateProgress(1);

            // DataBindingAdapter相关代码
            logger.info("生成DataBindingAdapter相关代码");
            DataBindingAdapterGenerator dataBindingAdapterGenerator = new DataBindingAdapterGenerator(projectRoot, packageName);
            dataBindingAdapterGenerator.generateAll();
            progressTracker.updateProgress(1);

            // DataFlow相关代码
            logger.info("生成DataFlow相关代码");
            DataFlowGenerator dataFlowGenerator = new DataFlowGenerator(projectRoot, packageName);
            dataFlowGenerator.generateAll();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("工具类生成");
        } catch (Exception e) {
            errorHandler.handleError("工具类生成", "生成工具类时发生错误", e);
            throw e;
        }
    }

    private void generateSelectedModules() throws Exception {
        logger.phaseStart("选中模块生成");

        // 获取选中的模块列表
        VariationManager variationManager = VariationManager.getInstance();
        String[] selectedModules = variationManager.getVariation("features");

        if (selectedModules == null || selectedModules.length == 0) {
            // 如果没有选中模块，使用默认模块
            selectedModules = new String[]{"CacheModuleGenerator", "CalculatorModuleGenerator", "TimerModuleGenerator"};
        }

        progressTracker.startPhase("选中模块生成", selectedModules.length);

        try {
            for (String module : selectedModules) {
                String moduleName = module.replace("ModuleGenerator", "");
                logger.info("生成模块: " + moduleName);

                try {
                    // 根据模块名称创建对应的生成器
                    switch (module) {
                        case "AnimationModuleGenerator":
                            AnimationModuleGenerator animationModule = new AnimationModuleGenerator(projectRoot, packageName);
                            animationModule.generateAll();
                            break;
                        case "CacheModuleGenerator":
                            CacheModuleGenerator cacheModule = new CacheModuleGenerator(projectRoot, packageName);
                            cacheModule.generateAll();
                            break;
                        case "CalculatorModuleGenerator":
                            CalculatorModuleGenerator calculatorModule = new CalculatorModuleGenerator(projectRoot, packageName);
                            calculatorModule.generateAll();
                            break;
                        case "ChartModuleGenerator":
                            ChartModuleGenerator chartModule = new ChartModuleGenerator(projectRoot, packageName);
                            chartModule.generateAll();
                            break;
                        case "ConverterModuleGenerator":
                            ConverterModuleGenerator converterModule = new ConverterModuleGenerator(projectRoot, packageName);
                            converterModule.generateAll();
                            break;
                        case "DatabaseModuleGenerator":
                            DatabaseModuleGenerator databaseModule = new DatabaseModuleGenerator(projectRoot, packageName);
                            databaseModule.generateAll();
                            break;
                        case "FileModuleGenerator":
                            FileModuleGenerator fileModule = new FileModuleGenerator(projectRoot, packageName);
                            fileModule.generateAll();
                            break;
                        case "GestureModuleGenerator":
                            GestureModuleGenerator gestureModule = new GestureModuleGenerator(projectRoot, packageName);
                            gestureModule.generateAll();
                            break;
                        case "GraphicsModuleGenerator":
                            GraphicsModuleGenerator graphicsModule = new GraphicsModuleGenerator(projectRoot, packageName);
                            graphicsModule.generateAll();
                            break;
                        case "LogModuleGenerator":
                            LogModuleGenerator logModule = new LogModuleGenerator(projectRoot, packageName);
                            logModule.generateAll();
                            break;
                        case "NotepadModuleGenerator":
                            NotepadModuleGenerator notepadModule = new NotepadModuleGenerator(projectRoot, packageName);
                            notepadModule.generateAll();
                            break;
                        case "PerformanceModuleGenerator":
                            PerformanceModuleGenerator performanceModule = new PerformanceModuleGenerator(projectRoot, packageName);
                            performanceModule.generateAll();
                            break;
                        case "TimerModuleGenerator":
                            TimerModuleGenerator timerModule = new TimerModuleGenerator(projectRoot, packageName);
                            timerModule.generateAll();
                            break;
                        case "TodoModuleGenerator":
                            TodoModuleGenerator todoModule = new TodoModuleGenerator(projectRoot, packageName);
                            todoModule.generateAll();
                            break;
                        default:
                            logger.warning("未知的模块: " + module);
                            break;
                    }

                    progressTracker.updateProgress(1);
                } catch (Exception e) {
                    logger.error("生成模块 " + module + " 时发生错误: " + e.getMessage());
                    errorHandler.handleError("模块生成", "生成模块 " + module + " 时发生错误", e);
                }
            }

            progressTracker.completePhase();
            logger.phaseComplete("选中模块生成");
        } catch (Exception e) {
            errorHandler.handleError("选中模块生成", "生成选中模块时发生错误", e);
            throw e;
        }
    }

    private void generateConfigs() throws Exception {
        logger.phaseStart("配置文件生成");
        progressTracker.startPhase("配置文件生成", 1);

        try {
            logger.info("生成AndroidManifest.xml");
            ManifestGenerator manifestGenerator = new ManifestGenerator(projectRoot, packageName);
            manifestGenerator.generate();
            progressTracker.updateProgress(1);

            progressTracker.completePhase();
            logger.phaseComplete("配置文件生成");
        } catch (Exception e) {
            errorHandler.handleError("配置文件生成", "生成配置文件时发生错误", e);
            throw e;
        }
    }

    /**
     * 打印完成信息
     */
    private void printCompletion() {
        System.out.println("\n========================================");
        System.out.println("生成完成！");
        System.out.println("========================================");
        System.out.println("项目信息:");
        System.out.println("  项目路径: " + projectRoot);
        System.out.println("  包名: " + packageName);
        System.out.println("  应用名: " + GeneratorConfig.appName);
        System.out.println("----------------------------------------");
        System.out.println("生成统计:");
        System.out.println("  包数量: " + GeneratorConfig.packageCount);
        System.out.println("  Activity总数: " + (GeneratorConfig.packageCount * GeneratorConfig.activityCountPerPackage));
        System.out.println("  Model数量: " + GeneratorConfig.modelCount);
        System.out.println("  ViewModel数量: " + GeneratorConfig.viewModelCount);
        System.out.println("  Repository数量: " + GeneratorConfig.repositoryCount);
        System.out.println("  Adapter数量: " + GeneratorConfig.adapterCount);
        System.out.println("  Fragment数量: " + GeneratorConfig.fragmentCount);
        System.out.println("  Manager数量: " + GeneratorConfig.managerCount);
        System.out.println("  Helper数量: " + GeneratorConfig.helperCount);
        System.out.println("----------------------------------------");
        System.out.println("使用说明:");
        System.out.println("1. 在Android Studio中打开项目");
        System.out.println("2. 等待Gradle同步完成");
        System.out.println("3. 运行应用");
        System.out.println("----------------------------------------");
        System.out.println("日志文件: " + logger.getLogFilePath());
        System.out.println("========================================");
    }
}
