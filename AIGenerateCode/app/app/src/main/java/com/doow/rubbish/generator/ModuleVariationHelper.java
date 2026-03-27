package com.doow.rubbish.generator;

import java.util.*;


public class ModuleVariationHelper {

    private static ModuleVariationHelper instance;
    private Random random;


    private static final String[] CLASS_PREFIXES = {
        "Animation", "Effect", "Transition", "Motion", "Transform",
        "Movement", "Action", "Operation", "Process", "Handler"
    };


    private static final String[] METHOD_PREFIXES = {
        "execute", "perform", "apply", "run", "start", "begin",
        "trigger", "launch", "initiate", "activate", "enable"
    };


    private static final String[] VARIABLE_PREFIXES = {
        "anim", "effect", "transition", "motion", "transform",
        "movement", "action", "operation", "process", "handler"
    };


    private static final String[] BOOLEAN_NAMES = {
        "isEnabled", "isActive", "isRunning", "isPaused", "isCompleted",
        "isReady", "isValid", "isAvailable", "isLoaded", "isInitialized"
    };


    private static final String[] INT_NAMES = {
        "duration", "delay", "offset", "count", "index", "position",
        "size", "length", "width", "height", "scale"
    };


    private static final String[] FLOAT_NAMES = {
        "alpha", "scaleX", "scaleY", "rotation", "translationX", "translationY",
        "pivotX", "pivotY", "fromValue", "toValue", "progress"
    };

    // 字符串值名称数组
    private static final String[] STRING_NAMES = {
        "tag", "name", "label", "title", "message", "description",
        "content", "text", "value", "key", "identifier"
    };

    // 对象值名称数组
    private static final String[] OBJECT_NAMES = {
        "context", "view", "listener", "callback", "handler", "manager",
        "controller", "adapter", "provider", "source", "target"
    };

    // 集合值名称数组
    private static final String[] COLLECTION_NAMES = {
        "items", "elements", "components", "parts", "sections",
        "fragments", "layers", "stages", "phases", "steps"
    };

    // 日志消息模板
    private static final String[] LOG_MESSAGES = {
        "Operation started", "Process initiated", "Action triggered",
        "Task executing", "Handler activated", "Effect applied",
        "Transition started", "Animation launched", "Motion begun",
        "Transform initialized"
    };

    // 错误消息模板
    private static final String[] ERROR_MESSAGES = {
        "Operation failed", "Process error", "Action interrupted",
        "Task failed", "Handler error", "Effect error",
        "Transition failed", "Animation error", "Motion error",
        "Transform error"
    };

    // 条件判断模板
    private static final String[] CONDITION_TEMPLATES = {
        "if (%s != null)",
        "if (%s != null && %s.isValid())",
        "if (%s.isEnabled())",
        "if (%s.isReady())",
        "if (%s.isAvailable())",
        "if (%s != null && %s.size() > 0)"
    };

    // 返回语句模板
    private static final String[] RETURN_TEMPLATES = {
        "return %s;",
        "return %s != null ? %s : defaultValue;",
        "return %s.isValid() ? %s : null;",
        "return %s.isReady() ? %s : null;"
    };

    // 循环模板
    private static final String[] LOOP_TEMPLATES = {
        "for (int i = 0; i < %s.size(); i++)",
        "for (%s item : %s)",
        "while (%s.hasNext())",
        "do { } while (%s.hasNext())"
    };

    // 常用数值范围
    private static final int[] DURATION_VALUES = {
        100, 150, 200, 250, 300, 350, 400, 450, 500, 600, 800, 1000
    };

    private static final int[] DELAY_VALUES = {
        0, 50, 100, 150, 200, 300, 500
    };

    private static final float[] SCALE_VALUES = {
        0.5f, 0.8f, 1.0f, 1.2f, 1.5f, 2.0f
    };

    private static final float[] ALPHA_VALUES = {
        0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f
    };

    private static final float[] ROTATION_VALUES = {
        0f, 45f, 90f, 135f, 180f, 270f, 360f
    };

    private ModuleVariationHelper() {
        random = new Random();
    }

    public static synchronized ModuleVariationHelper getInstance() {
        if (instance == null) {
            instance = new ModuleVariationHelper();
        }
        return instance;
    }

    /**
     * 设置随机种子
     */
    public void setSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * 生成随机的类名
     */
    public String generateClassName(String suffix) {
        String prefix = CLASS_PREFIXES[random.nextInt(CLASS_PREFIXES.length)];
        String middle = EnhancedRandomUtils.generateWord(EnhancedRandomUtils.between(2, 4));
        return prefix + middle + suffix;
    }

    /**
     * 生成随机的方法名
     */
    public String generateMethodName(String suffix) {
        String prefix = METHOD_PREFIXES[random.nextInt(METHOD_PREFIXES.length)];
        String middle = EnhancedRandomUtils.generateWord(EnhancedRandomUtils.between(2, 4));
        return prefix + Character.toUpperCase(middle.charAt(0)) + middle.substring(1) + suffix;
    }

    /**
     * 生成随机的变量名
     */
    public String generateVariableName(String type) {
        String prefix = VARIABLE_PREFIXES[random.nextInt(VARIABLE_PREFIXES.length)];
        String middle = EnhancedRandomUtils.generateWord(EnhancedRandomUtils.between(2, 4));
        return prefix + middle + type.toLowerCase();
    }

    /**
     * 生成随机的布尔值名称
     */
    public String generateBooleanName() {
        return BOOLEAN_NAMES[random.nextInt(BOOLEAN_NAMES.length)];
    }

    /**
     * 生成随机的整数值名称
     */
    public String generateIntName() {
        return INT_NAMES[random.nextInt(INT_NAMES.length)];
    }

    /**
     * 生成随机的浮点值名称
     */
    public String generateFloatName() {
        return FLOAT_NAMES[random.nextInt(FLOAT_NAMES.length)];
    }

    /**
     * 生成随机的字符串值名称
     */
    public String generateStringName() {
        return STRING_NAMES[random.nextInt(STRING_NAMES.length)];
    }

    /**
     * 生成随机的对象值名称
     */
    public String generateObjectName() {
        return OBJECT_NAMES[random.nextInt(OBJECT_NAMES.length)];
    }

    /**
     * 生成随机的集合值名称
     */
    public String generateCollectionName() {
        return COLLECTION_NAMES[random.nextInt(COLLECTION_NAMES.length)];
    }

    /**
     * 生成随机的日志消息
     */
    public String generateLogMessage(String operation) {
        String template = LOG_MESSAGES[random.nextInt(LOG_MESSAGES.length)];
        return template + ": " + operation;
    }

    /**
     * 生成随机的错误消息
     */
    public String generateErrorMessage(String operation) {
        String template = ERROR_MESSAGES[random.nextInt(ERROR_MESSAGES.length)];
        return template + ": " + operation;
    }

    /**
     * 生成随机的条件判断
     */
    public String generateCondition(String variableName) {
        String template = CONDITION_TEMPLATES[random.nextInt(CONDITION_TEMPLATES.length)];
        return String.format(template, variableName, variableName);
    }

    /**
     * 生成随机的返回语句
     */
    public String generateReturnStatement(String variableName) {
        String template = RETURN_TEMPLATES[random.nextInt(RETURN_TEMPLATES.length)];
        return String.format(template, variableName, variableName);
    }

    /**
     * 生成随机的循环语句
     */
    public String generateLoopStatement(String collectionName, String itemName) {
        String template = LOOP_TEMPLATES[random.nextInt(LOOP_TEMPLATES.length)];
        if (template.contains("%s %s")) {
            return String.format(template, itemName, collectionName);
        }
        return String.format(template, collectionName);
    }

    /**
     * 生成随机的时长值
     */
    public int generateDuration() {
        return DURATION_VALUES[random.nextInt(DURATION_VALUES.length)];
    }

    /**
     * 生成随机的延迟值
     */
    public int generateDelay() {
        return DELAY_VALUES[random.nextInt(DELAY_VALUES.length)];
    }

    /**
     * 生成随机的缩放值
     */
    public float generateScale() {
        return SCALE_VALUES[random.nextInt(SCALE_VALUES.length)];
    }

    /**
     * 生成随机的透明度值
     */
    public float generateAlpha() {
        return ALPHA_VALUES[random.nextInt(ALPHA_VALUES.length)];
    }

    /**
     * 生成随机的旋转值
     */
    public float generateRotation() {
        return ROTATION_VALUES[random.nextInt(ROTATION_VALUES.length)];
    }

    /**
     * 生成随机的数值范围
     */
    public int[] generateIntRange(int min, int max) {
        int count = random.nextInt(5) + 2;
        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            values[i] = random.nextInt(max - min + 1) + min;
        }
        return values;
    }

    /**
     * 生成随机的浮点数值范围
     */
    public float[] generateFloatRange(float min, float max) {
        int count = random.nextInt(5) + 2;
        float[] values = new float[count];
        for (int i = 0; i < count; i++) {
            values[i] = min + random.nextFloat() * (max - min);
        }
        return values;
    }

    /**
     * 生成随机的字符串数组
     */
    public String[] generateStringArray(int count) {
        String[] array = new String[count];
        for (int i = 0; i < count; i++) {
            array[i] = EnhancedRandomUtils.generateWord(random.nextInt(8) + 4);
        }
        return array;
    }

    /**
     * 生成随机的代码注释
     */
    public String generateComment(String description) {
        String[] commentTemplates = {
            "// " + description,
            "// " + description + " - " + EnhancedRandomUtils.generateWord(random.nextInt(6) + 3),
            "// " + description + " (v" + (random.nextInt(10) + 1) + ")",
            "// " + description + " - " + LOG_MESSAGES[random.nextInt(LOG_MESSAGES.length)],
            "// " + description + ": " + ERROR_MESSAGES[random.nextInt(ERROR_MESSAGES.length)]
        };
        return commentTemplates[random.nextInt(commentTemplates.length)];
    }

    /**
     * 生成随机的TODO注释
     */
    public String generateTodoComment(String task) {
        return "// TODO: " + task + " - " + EnhancedRandomUtils.generateWord(random.nextInt(6) + 3);
    }

    /**
     * 生成随机的FIXME注释
     */
    public String generateFixmeComment(String issue) {
        return "// FIXME: " + issue + " - " + EnhancedRandomUtils.generateWord(random.nextInt(6) + 3);
    }
}
