package com.doow.rubbish.generator.module;

import com.doow.rubbish.generator.EnhancedRandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class GestureModuleGenerator extends BaseModuleGenerator {

    protected VariationManager variationManager;

    // 手势类型
    private static final String[] GESTURE_TYPES = {
        "tap", "double_tap", "long_press", "scroll", "fling"
    };

    // 手势方法名前缀
    private static final String[] METHOD_PREFIXES = {
        "handle", "process", "detect", "recognize", "identify", "analyze", "evaluate"
    };

    // 手势方法名后缀
    private static final String[] METHOD_SUFFIXES = {
        "Gesture", "Action", "Event", "Motion", "Movement", "Interaction", "Touch"
    };

    // 手势变量名前缀
    private static final String[] VARIABLE_PREFIXES = {
        "gesture", "motion", "touch", "event", "action", "interaction", "movement"
    };

    // 手势阈值
    private static final int[] SWIPE_THRESHOLDS = {
        50, 75, 100, 125, 150
    };

    // 手势速度阈值
    private static final int[] VELOCITY_THRESHOLDS = {
        50, 75, 100, 125, 150
    };

    // 长按超时时间
    private static final int[] LONG_PRESS_TIMEOUTS = {
        300, 400, 500, 600, 800
    };

    public GestureModuleGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成手势模块");

        // 获取当前UI风格和异步处理方式
        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 生成手势模块
        generateGestureModule(uiStyle, asyncHandler);
    }

    private void generateGestureModule(String uiStyle, String asyncHandler) throws Exception {
        // 生成手势检测器
        generateGestureDetector(uiStyle, asyncHandler);

        // 生成手势工具类
        generateGestureUtils(uiStyle, asyncHandler);

        // 生成手势监听器
        generateGestureListener(uiStyle, asyncHandler);
    }

    private void generateGestureDetector(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("GestureDetector");
        String instanceName = EnhancedRandomUtils.generateVariableName("Instance");
        String contextName = EnhancedRandomUtils.generateObjectName();
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");

        // 使用随机值
        int swipeThreshold = SWIPE_THRESHOLDS[EnhancedRandomUtils.between(0, SWIPE_THRESHOLDS.length - 1)];
        int swipeVelocityThreshold = VELOCITY_THRESHOLDS[EnhancedRandomUtils.between(0, VELOCITY_THRESHOLDS.length - 1)];
        int longPressTimeout = LONG_PRESS_TIMEOUTS[EnhancedRandomUtils.between(0, LONG_PRESS_TIMEOUTS.length - 1)];

        sb.append("package ").append(packageName).append(".gesture;\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.PointF;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import android.view.GestureDetector;\n");
        sb.append("import android.view.MotionEvent;\n");

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
        sb.append("public class ").append(className).append(" extends GestureDetector.SimpleOnGestureListener {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int SWIPE_THRESHOLD = ").append(swipeThreshold).append(";\n");
        sb.append("    private static final int SWIPE_VELOCITY_THRESHOLD = ").append(swipeVelocityThreshold).append(";\n");
        sb.append("    private static final long LONG_PRESS_TIMEOUT = ").append(longPressTimeout).append(";\n");
        sb.append("\n");

        // 回调接口
        sb.append("    public interface ").append(callbackName).append(" {\n");
        sb.append("        void onSingleTap(PointF point);\n");
        sb.append("        void onDoubleTap(PointF point);\n");
        sb.append("        void onLongPress(PointF point);\n");
        sb.append("        void onScroll(PointF startPoint, PointF endPoint, float distanceX, float distanceY);\n");
        sb.append("        void onFling(PointF startPoint, PointF endPoint, float velocityX, float velocityY);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private final ").append(callbackName).append(" ").append(callbackName.toLowerCase()).append(";\n");
        sb.append("    private final GestureDetector gestureDetector;\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context context, ").append(callbackName).append(" ").append(callbackName.toLowerCase()).append(") {\n");
        sb.append("        this.").append(callbackName.toLowerCase()).append(" = ").append(callbackName.toLowerCase()).append(";\n");
        sb.append("        this.gestureDetector = new GestureDetector(context, this);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 触摸事件处理方法
        sb.append("    public boolean onTouchEvent(MotionEvent event) {\n");
        sb.append("        return gestureDetector.onTouchEvent(event);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 单击方法
        sb.append("    @Override\n");
        sb.append("    public boolean onSingleTapUp(MotionEvent e) {\n");
        sb.append("        PointF point = new PointF(e.getX(), e.getY());\n");
        sb.append("        Log.d(").append(tagName).append(", \"Single tap at: \" + point.x + \", \" + point.y);\n");
        sb.append("        if (").append(callbackName.toLowerCase()).append(" != null) {\n");
        sb.append("            ").append(callbackName.toLowerCase()).append(".onSingleTap(point);\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 双击方法
        sb.append("    @Override\n");
        sb.append("    public boolean onDoubleTap(MotionEvent e) {\n");
        sb.append("        PointF point = new PointF(e.getX(), e.getY());\n");
        sb.append("        Log.d(").append(tagName).append(", \"Double tap at: \" + point.x + \", \" + point.y);\n");
        sb.append("        if (").append(callbackName.toLowerCase()).append(" != null) {\n");
        sb.append("            ").append(callbackName.toLowerCase()).append(".onDoubleTap(point);\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 长按方法
        sb.append("    @Override\n");
        sb.append("    public void onLongPress(MotionEvent e) {\n");
        sb.append("        PointF point = new PointF(e.getX(), e.getY());\n");
        sb.append("        Log.d(").append(tagName).append(", \"Long press at: \" + point.x + \", \" + point.y);\n");
        sb.append("        if (").append(callbackName.toLowerCase()).append(" != null) {\n");
        sb.append("            ").append(callbackName.toLowerCase()).append(".onLongPress(point);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 滚动方法
        sb.append("    @Override\n");
        sb.append("    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {\n");
        sb.append("        PointF startPoint = new PointF(e1.getX(), e1.getY());\n");
        sb.append("        PointF endPoint = new PointF(e2.getX(), e2.getY());\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Scroll from (%.2f,%.2f) to (%.2f,%.2f)\",\n");
        sb.append("                startPoint.x, startPoint.y, endPoint.x, endPoint.y));\n");
        sb.append("        if (").append(callbackName.toLowerCase()).append(" != null) {\n");
        sb.append("            ").append(callbackName.toLowerCase()).append(".onScroll(startPoint, endPoint, distanceX, distanceY);\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 滑动方法
        sb.append("    @Override\n");
        sb.append("    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {\n");
        sb.append("        PointF startPoint = new PointF(e1.getX(), e1.getY());\n");
        sb.append("        PointF endPoint = new PointF(e2.getX(), e2.getY());\n");
        sb.append("        Log.d(").append(tagName).append(", String.format(\"Fling from (%.2f,%.2f) to (%.2f,%.2f) with velocity (%.2f,%.2f)\",\n");
        sb.append("                startPoint.x, startPoint.y, endPoint.x, endPoint.y, velocityX, velocityY));\n");
        sb.append("        if (").append(callbackName.toLowerCase()).append(" != null) {\n");
        sb.append("            ").append(callbackName.toLowerCase()).append(".onFling(startPoint, endPoint, velocityX, velocityY);\n");
        sb.append("        }\n");
        sb.append("        return true;\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("gesture");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateGestureUtils(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("Utils");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");

        // 使用随机值
        int swipeThreshold = SWIPE_THRESHOLDS[EnhancedRandomUtils.between(0, SWIPE_THRESHOLDS.length - 1)];
        int swipeVelocityThreshold = VELOCITY_THRESHOLDS[EnhancedRandomUtils.between(0, VELOCITY_THRESHOLDS.length - 1)];

        sb.append("package ").append(packageName).append(".gesture;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.PointF;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import android.view.MotionEvent;\n");

        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int SWIPE_THRESHOLD = ").append(swipeThreshold).append(";\n");
        sb.append("    private static final int SWIPE_VELOCITY_THRESHOLD = ").append(swipeVelocityThreshold).append(";\n");
        sb.append("\n");

        // 私有构造函数
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        // 私有构造函数，防止实例化\n");
        sb.append("    }\n");
        sb.append("\n");

        // 计算两点距离方法
        sb.append("    public static float distance(PointF p1, PointF p2) {\n");
        sb.append("        float dx = p1.x - p2.x;\n");
        sb.append("        float dy = p1.y - p2.y;\n");
        sb.append("        return (float) Math.sqrt(dx * dx + dy * dy);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 计算两点角度方法
        sb.append("    public static float angle(PointF p1, PointF p2) {\n");
        sb.append("        float dx = p2.x - p1.x;\n");
        sb.append("        float dy = p2.y - p1.y;\n");
        sb.append("        return (float) Math.toDegrees(Math.atan2(dy, dx));\n");
        sb.append("    }\n");
        sb.append("\n");

        // 判断滑动方向方法
        sb.append("    public static String getSwipeDirection(float dx, float dy) {\n");
        sb.append("        if (Math.abs(dx) > Math.abs(dy)) {\n");
        sb.append("            return dx > 0 ? \"right\" : \"left\";\n");
        sb.append("        } else {\n");
        sb.append("            return dy > 0 ? \"down\" : \"up\";\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("\n");

        // 判断是否是快速滑动方法
        sb.append("    public static boolean isFling(float velocityX, float velocityY) {\n");
        sb.append("        return Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD ||\n");
        sb.append("               Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD;\n");
        sb.append("    }\n");
        sb.append("\n");

        // 判断是否是长按方法
        sb.append("    public static boolean isLongPress(MotionEvent e1, MotionEvent e2, long duration) {\n");
        sb.append("        float distance = distance(new PointF(e1.getX(), e1.getY()),\n");
        sb.append("                                new PointF(e2.getX(), e2.getY()));\n");
        sb.append("        return distance < SWIPE_THRESHOLD && duration > ").append(longPressTimeout).append(";\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("gesture");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }

    private void generateGestureListener(String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 使用随机类名和变量名
        String className = EnhancedRandomUtils.generateClassName("View");
        String tagName = EnhancedRandomUtils.generateVariableName("Tag");
        String callbackName = EnhancedRandomUtils.generateClassName("Callback");

        // 使用随机值
        int swipeThreshold = SWIPE_THRESHOLDS[EnhancedRandomUtils.between(0, SWIPE_THRESHOLDS.length - 1)];
        int swipeVelocityThreshold = VELOCITY_THRESHOLDS[EnhancedRandomUtils.between(0, VELOCITY_THRESHOLDS.length - 1)];

        sb.append("package ").append(packageName).append(".gesture;\n");
        sb.append("\n");

        // 导入
        sb.append("import android.content.Context;\n");
        sb.append("import android.graphics.PointF;\n");
        sb.append("import android.util.Log;\n");
        sb.append("import android.view.MotionEvent;\n");
        sb.append("import android.view.View;\n");

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
        sb.append("public class ").append(className).append(" extends View {\n");
        sb.append("\n");

        // 常量
        sb.append("    private static final String ").append(tagName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final int SWIPE_THRESHOLD = ").append(swipeThreshold).append(";\n");
        sb.append("    private static final int SWIPE_VELOCITY_THRESHOLD = ").append(swipeVelocityThreshold).append(";\n");
        sb.append("\n");

        // 成员变量
        sb.append("    private PointF startPoint;\n");
        sb.append("    private PointF endPoint;\n");
        sb.append("    private long startTime;\n");
        sb.append("    private final ").append(callbackName).append(" ").append(callbackName.toLowerCase()).append(";\n");
        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        super(context);\n");
        sb.append("        ").append(callbackName.toLowerCase()).append(" = new ").append(callbackName).append("() {\n");
        sb.append("            @Override\n");
        sb.append("            public void onSingleTap(PointF point) {\n");
        sb.append("                Log.d(").append(tagName).append(", \"Single tap: \" + point.x + \", \" + point.y);\n");
        sb.append("                onGesture(\"tap\", point);\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            @Override\n");
        sb.append("            public void onDoubleTap(PointF point) {\n");
        sb.append("                Log.d(").append(tagName).append(", \"Double tap: \" + point.x + \", \" + point.y);\n");
        sb.append("                onGesture(\"double_tap\", point);\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            @Override\n");
        sb.append("            public void onLongPress(PointF point) {\n");
        sb.append("                Log.d(").append(tagName).append(", \"Long press: \" + point.x + \", \" + point.y);\n");
        sb.append("                onGesture(\"long_press\", point);\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            @Override\n");
        sb.append("            public void onScroll(PointF startPoint, PointF endPoint, float distanceX, float distanceY) {\n");
        sb.append("                Log.d(").append(tagName).append(", String.format(\"Scroll: (%.2f,%.2f) to (%.2f,%.2f)\",\n");
        sb.append("                        startPoint.x, startPoint.y, endPoint.x, endPoint.y));\n");
        sb.append("                onGesture(\"scroll\", endPoint);\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            @Override\n");
        sb.append("            public void onFling(PointF startPoint, PointF endPoint, float velocityX, float velocityY) {\n");
        sb.append("                Log.d(").append(tagName).append(", String.format(\"Fling: (%.2f,%.2f) to (%.2f,%.2f) with (%.2f,%.2f)\",\n");
        sb.append("                        startPoint.x, startPoint.y, endPoint.x, endPoint.y, velocityX, velocityY));\n");
        sb.append("                String direction = ").append(className).append("Utils.getSwipeDirection(velocityX, velocityY);\n");
        sb.append("                onGesture(\"fling_\" + direction, endPoint);\n");
        sb.append("            }\n");
        sb.append("        });\n");
        sb.append("    }\n");
        sb.append("\n");

        // 触摸事件处理方法
        sb.append("    @Override\n");
        sb.append("    public boolean onTouchEvent(MotionEvent event) {\n");
        sb.append("        switch (event.getAction()) {\n");
        sb.append("            case MotionEvent.ACTION_DOWN:\n");
        sb.append("                startPoint = new PointF(event.getX(), event.getY());\n");
        sb.append("                startTime = System.currentTimeMillis();\n");
        sb.append("                break;\n");
        sb.append("            case MotionEvent.ACTION_MOVE:\n");
        sb.append("                endPoint = new PointF(event.getX(), event.getY());\n");
        sb.append("                break;\n");
        sb.append("            case MotionEvent.ACTION_UP:\n");
        sb.append("                endPoint = new PointF(event.getX(), event.getY());\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("        return gestureDetector.onTouchEvent(event);\n");
        sb.append("    }\n");
        sb.append("\n");

        // 手势回调方法
        sb.append("    protected void onGesture(String gestureType, PointF point) {\n");
        sb.append("        // 子类可以重写此方法来处理手势\n");
        sb.append("    }\n");
        sb.append("\n");

        sb.append("}\n");

        // 保存文件
        String packagePath = getModulePackage("gesture");
        String classPath = packagePath.replace(".", "/") + "/" + className + ".java";
        writeFile(classPath, sb.toString());
    }
}