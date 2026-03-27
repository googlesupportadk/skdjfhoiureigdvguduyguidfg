package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class AnimationModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 动画类型
    private static final String[] ANIMATION_TYPES = {
            "property", "value", "object", "layout", "transition"
    };

    // 动画方法名前缀
    private static final String[] METHOD_PREFIXES = {
            "animate", "play", "start", "run", "execute", "perform", "apply"
    };

    // 动画方法名后缀
    private static final String[] METHOD_SUFFIXES = {
            "Fade", "Scale", "Rotate", "Translate", "Slide", "Move", "Transform"
    };

    // 动画变量名前缀
    private static final String[] VARIABLE_PREFIXES = {
            "anim", "animation", "transition", "effect", "motion", "movement"
    };

    // 动画时长值（毫秒）
    private static final int[] DURATION_VALUES = {
            100, 150, 200, 250, 300, 350, 400, 450, 500, 600
    };

    // 动画插值器
    private static final String[] INTERPOLATORS = {
            "AccelerateDecelerateInterpolator",
            "AccelerateInterpolator",
            "DecelerateInterpolator",
            "AnticipateInterpolator",
            "OvershootInterpolator",
            "AnticipateOvershootInterpolator",
            "BounceInterpolator"
    };

    public AnimationModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成动画模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成动画模块
        generateAnimationModule(uiStyle, asyncHandler);
    }

    private void generateAnimationModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成动画管理器
        generateAnimationManager(uiStyle, asyncHandler);

        // 生成动画工具类
        generateAnimationUtils(uiStyle, asyncHandler);

        // 生成动画监听器
        generateAnimationListener(uiStyle, asyncHandler);
    }

    private void generateAnimationManager(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Animation");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        // 使用随机值
        int defaultDuration = DURATION_VALUES[EnhancedRandomUtils.between(0, DURATION_VALUES.length - 1)];
        int shortDuration = DURATION_VALUES[EnhancedRandomUtils.between(0, 3)];
        int longDuration = DURATION_VALUES[EnhancedRandomUtils.between(6, DURATION_VALUES.length - 1)];

        sb.append("package ").append(packageName).append(".animation;\n");

        // 导入
        sb.append("import android.animation.Animator;\n");
        sb.append("import android.animation.AnimatorListenerAdapter;\n");
        sb.append("import android.animation.ValueAnimator;\n");
        sb.append("import android.content.Context;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import android.view.View;\n");
        sb.append("import android.view.animation.AlphaAnimation;\n");
        sb.append("import android.view.animation.Animation;\n");
        sb.append("import android.view.animation.RotateAnimation;\n");
        sb.append("import android.view.animation.ScaleAnimation;\n");
        sb.append("import android.view.animation.TranslateAnimation;\n");
        sb.append("import android.view.animation.AnimationUtils;\n");
        sb.append("import android.view.animation.AccelerateDecelerateInterpolator;\n");
        sb.append("import android.view.animation.AccelerateInterpolator;\n");
        sb.append("import android.view.animation.DecelerateInterpolator;\n");
        sb.append("import android.view.animation.AnticipateInterpolator;\n");
        sb.append("import android.view.animation.OvershootInterpolator;\n");
        sb.append("import android.view.animation.AnticipateOvershootInterpolator;\n");
        sb.append("import android.view.animation.BounceInterpolator;\n");

        // 根据异步处理方式添加导入
        if (asyncHandler.contains("coroutines")) {
            sb.append("import kotlinx.coroutines.CoroutineScope;\n");
            sb.append("import kotlinx.coroutines.Dispatchers;\n");
        } else if (asyncHandler.contains("rxjava")) {
            sb.append("import io.reactivex.rxjava3.core.Single;\n");
            sb.append("import io.reactivex.rxjava3.schedulers.Schedulers;\n");
        }

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量 - 使用随机值
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final long DEFAULT_DURATION = ").append(defaultDuration).append(";\n");
        sb.append("    private static final long SHORT_DURATION = ").append(shortDuration).append(";\n");
        sb.append("    private static final long LONG_DURATION = ").append(longDuration).append(";\n");
        sb.append("\n");

        // 单例
        sb.append("    private static volatile ").append(className).append(" ").append(instanceName).append(";\n");
        sb.append("    private final Context ").append(contextName).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    private ").append(className).append("(Context ").append(contextName).append(") {\n");
        sb.append("        this.").append(contextName).append(" = ").append(contextName).append(".getApplicationContext();\n");
        sb.append("    }\n");
        sb.append("\n");

        // 获取实例方法
        sb.append("    public static ").append(className).append(" getInstance(Context ").append(contextName).append(") {\n");
        sb.append("        if (").append(instanceName).append(" == null) {\n");
        sb.append("            synchronized (").append(className).append(".class) {\n");
        sb.append("                if (").append(instanceName).append(" == null) {\n");
        sb.append("                    ").append(instanceName).append(" = new ").append(className).append("(").append(contextName).append(");\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(instanceName).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        // 生成随机动画方法
        generateAnimationMethods(sb, className, tagName, contextName, defaultDuration, shortDuration, longDuration);

        sb.append("}\n");

        // 写入文件
        String packagePath = getModulePackage("animation");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateAnimationMethods(StringBuilder sb, String className, String tagName,
                                          String contextName, int defaultDuration,
                                          int shortDuration, int longDuration) {
        // 生成淡入淡出方法
        generateFadeMethods(sb, className, tagName, defaultDuration, shortDuration, longDuration);

        // 生成缩放方法
        generateScaleMethods(sb, className, tagName, defaultDuration, shortDuration, longDuration);

        // 生成旋转方法
        generateRotateMethods(sb, className, tagName, defaultDuration, shortDuration, longDuration);

        // 生成平移方法
        generateTranslateMethods(sb, className, tagName, defaultDuration, shortDuration, longDuration);

        // 生成滑动方法
        generateSlideMethods(sb, className, tagName, defaultDuration, shortDuration, longDuration);
    }

    private void generateFadeMethods(StringBuilder sb, String className, String tagName,
                                     int defaultDuration, int shortDuration, int longDuration) {
        String methodName = EnhancedRandomUtils.generateMethodName("Fade");
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String fromAlphaName = EnhancedRandomUtils.generateVariableName("FromAlpha");
        String toAlphaName = EnhancedRandomUtils.generateVariableName("ToAlpha");

        // 淡入方法
        sb.append("    public void ").append(methodName).append("In(").append(viewName).append(") {\n");
        sb.append("        ").append(methodName).append("In(").append(viewName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("In(").append(viewName).append(", long ").append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        AlphaAnimation ").append(animName).append(" = new AlphaAnimation(0.0f, 1.0f);\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Fade in animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 淡出方法
        sb.append("    public void ").append(methodName).append("Out(").append(viewName).append(") {\n");
        sb.append("        ").append(methodName).append("Out(").append(viewName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("Out(").append(viewName).append(", long ").append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        AlphaAnimation ").append(animName).append(" = new AlphaAnimation(1.0f, 0.0f);\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Fade out animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateScaleMethods(StringBuilder sb, String className, String tagName,
                                       int defaultDuration, int shortDuration, int longDuration) {
        String methodName = EnhancedRandomUtils.generateMethodName("Scale");
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String fromScaleName = EnhancedRandomUtils.generateVariableName("FromScale");
        String toScaleName = EnhancedRandomUtils.generateVariableName("ToScale");
        String pivotXName = EnhancedRandomUtils.generateVariableName("PivotX");
        String pivotYName = EnhancedRandomUtils.generateVariableName("PivotY");

        float pivotValue = EnhancedRandomUtils.between(0, 10) / 10.0f;

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ").append(fromScaleName)
                .append(", float ").append(toScaleName).append(") {\n");
        sb.append("        ").append(methodName).append("(").append(viewName).append(", ").append(fromScaleName)
                .append(", ").append(toScaleName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ")
                .append(fromScaleName).append(", float ").append(toScaleName).append(", long ").append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ScaleAnimation ").append(animName).append(" = new ScaleAnimation(")
                .append(fromScaleName).append(", ").append(toScaleName).append(", \n");
        sb.append("                ").append(fromScaleName).append(", ").append(toScaleName).append(",\n");
        sb.append("                Animation.RELATIVE_TO_SELF, ").append(pivotValue).append("f,\n");
        sb.append("                Animation.RELATIVE_TO_SELF, ").append(pivotValue).append("f);\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Scale animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateRotateMethods(StringBuilder sb, String className, String tagName,
                                        int defaultDuration, int shortDuration, int longDuration) {
        String methodName = EnhancedRandomUtils.generateMethodName("Rotate");
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String fromDegreesName = EnhancedRandomUtils.generateVariableName("FromDegrees");
        String toDegreesName = EnhancedRandomUtils.generateVariableName("ToDegrees");
        String pivotXName = EnhancedRandomUtils.generateVariableName("PivotX");
        String pivotYName = EnhancedRandomUtils.generateVariableName("PivotY");

        float pivotValue = EnhancedRandomUtils.between(0, 10) / 10.0f;

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ")
                .append(fromDegreesName).append(", float ").append(toDegreesName).append(") {\n");
        sb.append("        ").append(methodName).append("(").append(viewName).append(", ").append(fromDegreesName)
                .append(", ").append(toDegreesName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ")
                .append(fromDegreesName).append(", float ").append(toDegreesName).append(", long ").append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        RotateAnimation ").append(animName).append(" = new RotateAnimation(")
                .append(fromDegreesName).append(", ").append(toDegreesName).append(",\n");
        sb.append("                Animation.RELATIVE_TO_SELF, ").append(pivotValue).append("f,\n");
        sb.append("                Animation.RELATIVE_TO_SELF, ").append(pivotValue).append("f);\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Rotate animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateTranslateMethods(StringBuilder sb, String className, String tagName,
                                           int defaultDuration, int shortDuration, int longDuration) {
        String methodName = EnhancedRandomUtils.generateMethodName("Translate");
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String fromXName = EnhancedRandomUtils.generateVariableName("FromX");
        String toXName = EnhancedRandomUtils.generateVariableName("ToX");
        String fromYName = EnhancedRandomUtils.generateVariableName("FromY");
        String toYName = EnhancedRandomUtils.generateVariableName("ToY");

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ")
                .append(fromXName).append(", float ").append(toXName).append(", float ")
                .append(fromYName).append(", float ").append(toYName).append(") {\n");
        sb.append("        ").append(methodName).append("(").append(viewName).append(", ").append(fromXName)
                .append(", ").append(toXName).append(", ").append(fromYName).append(", ")
                .append(toYName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("(").append(viewName).append(", float ")
                .append(fromXName).append(", float ").append(toXName).append(", float ")
                .append(fromYName).append(", float ").append(toYName).append(", long ")
                .append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        TranslateAnimation ").append(animName).append(" = new TranslateAnimation(")
                .append(fromXName).append(", ").append(toXName).append(", ")
                .append(fromYName).append(", ").append(toYName).append(");\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Translate animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateSlideMethods(StringBuilder sb, String className, String tagName,
                                      int defaultDuration, int shortDuration, int longDuration) {
        String methodName = EnhancedRandomUtils.generateMethodName("Slide");
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String directionName = EnhancedRandomUtils.generateVariableName("Direction");

        // 随机选择滑动方向
        String[] directions = {"LEFT", "RIGHT", "UP", "DOWN"};
        String direction = directions[EnhancedRandomUtils.between(0, directions.length - 1)];

        sb.append("    public void ").append(methodName).append("In(").append(viewName).append(") {\n");
        sb.append("        ").append(methodName).append("In(").append(viewName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("In(").append(viewName).append(", long ")
                .append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        Animation ").append(animName).append(" = AnimationUtils.loadAnimation(")
                .append(viewName).append(".getContext(), android.R.anim.slide_in_").append(direction.toLowerCase()).append(");\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Slide in animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("Out(").append(viewName).append(") {\n");
        sb.append("        ").append(methodName).append("Out(").append(viewName).append(", DEFAULT_DURATION);\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("    public void ").append(methodName).append("Out(").append(viewName).append(", long ")
                .append(durationName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        Animation ").append(animName).append(" = AnimationUtils.loadAnimation(")
                .append(viewName).append(".getContext(), android.R.anim.slide_out_").append(direction.toLowerCase()).append(");\n");
        sb.append("        ").append(animName).append(".setDuration(").append(durationName).append(");\n");
        sb.append("        ").append(animName).append(".setFillAfter(true);\n");
        sb.append("        ").append(viewName).append(".startAnimation(").append(animName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Slide out animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateAnimationUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        sb.append("package ").append(packageName).append(".animation;\n");
        sb.append("\n");
        sb.append("import android.view.View;\n");
        sb.append("import android.view.animation.Animation;\n");
        sb.append("import android.view.animation.AnimationSet;\n");
        sb.append("import android.view.animation.LayoutAnimationController;\n");
        sb.append("import android.util.Log;\n");
        sb.append("\n");

        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 生成随机工具方法
        generateUtilityMethods(sb, className, tagName);

        sb.append("}\n");

        String packagePath = getModulePackage("animation");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateUtilityMethods(StringBuilder sb, String className, String tagName) {
        String viewName = EnhancedRandomUtils.generateVariableName("View");
        String animName = EnhancedRandomUtils.generateVariableName("Animation");
        String durationName = EnhancedRandomUtils.generateVariableName("Duration");
        String delayName = EnhancedRandomUtils.generateVariableName("Delay");

        // 取消动画方法
        String cancelMethodName = EnhancedRandomUtils.generateMethodName("Cancel");
        sb.append("    public static void ").append(cancelMethodName).append("(").append(viewName).append(") {\n");
        sb.append("        if (").append(viewName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(viewName).append(".clearAnimation();\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation cancelled\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 延迟动画方法
        String delayMethodName = EnhancedRandomUtils.generateMethodName("Delay");
        sb.append("    public static void ").append(delayMethodName).append("(")
                .append(animName).append(", long ").append(delayName).append(") {\n");
        sb.append("        if (").append(animName).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        ").append(animName).append(".setStartOffset(").append(delayName).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation delayed by \" + ").append(delayName).append(" + \"ms\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // 组合动画方法
        String combineMethodName = EnhancedRandomUtils.generateMethodName("Combine");
        String anim1Name = EnhancedRandomUtils.generateVariableName("Animation1");
        String anim2Name = EnhancedRandomUtils.generateVariableName("Animation2");
        sb.append("    public static Animation ").append(combineMethodName).append("(")
                .append(animName).append(", ").append(animName).append(") {\n");
        sb.append("        AnimationSet ").append(animName).append("Set = new AnimationSet(true);\n");
        sb.append("        ").append(animName).append("Set.addAnimation(").append(anim1Name).append(");\n");
        sb.append("        ").append(animName).append("Set.addAnimation(").append(anim2Name).append(");\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animations combined\");\n");
        sb.append("        return ").append(animName).append("Set;\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void generateAnimationListener(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();
        String className = EnhancedRandomUtils.generateClassName("Listener");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        sb.append("package ").append(packageName).append(".animation;\n");
        sb.append("\n");
        sb.append("import android.animation.Animator;\n");
        sb.append("import android.animation.AnimatorListenerAdapter;\n");
        sb.append("import android.util.Log;\n");
        sb.append("\n");

        sb.append("public class ").append(className).append(" extends AnimatorListenerAdapter {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("\n");

        // 生成随机监听器方法
        generateListenerMethods(sb, className, tagName);

        sb.append("}\n");

        String packagePath = getModulePackage("animation");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateListenerMethods(StringBuilder sb, String className, String tagName) {
        String animatorName = EnhancedRandomUtils.generateVariableName("Animator");

        // onAnimationStart
        sb.append("    @Override\n");
        sb.append("    public void onAnimationStart(").append(animatorName).append(" animation) {\n");
        sb.append("        super.onAnimationStart(animation);\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation started\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // onAnimationEnd
        sb.append("    @Override\n");
        sb.append("    public void onAnimationEnd(").append(animatorName).append(" animation) {\n");
        sb.append("        super.onAnimationEnd(animation);\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation ended\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // onAnimationCancel
        sb.append("    @Override\n");
        sb.append("    public void onAnimationCancel(").append(animatorName).append(" animation) {\n");
        sb.append("        super.onAnimationCancel(animation);\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation cancelled\");\n");
        sb.append("    }\n");
        sb.append("\n");

        // onAnimationRepeat
        sb.append("    @Override\n");
        sb.append("    public void onAnimationRepeat(").append(animatorName).append(" animation) {\n");
        sb.append("        super.onAnimationRepeat(animation);\n");
        sb.append("        Log.d(").append(tagName).append(", \"Animation repeated\");\n");
        sb.append("    }\n");
        sb.append("\n");
    }

    private void writeFile(String path, String content) throws Exception {
        java.io.File file = new java.io.File(path);
        java.io.File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        java.io.FileWriter writer = new java.io.FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
