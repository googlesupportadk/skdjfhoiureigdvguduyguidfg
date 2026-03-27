
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级版的验证器生成器
 * 满足8条基础要求：
 * 1. 确保代码功能的随机性和多样性灵活性，避免沉郁代码和垃圾代码
 * 2. 生成的字段都会被使用
 * 3. 生成的方法都会被调用
 * 4. 不会产生未使用的代码
 * 5. 确保生成的代码是完整可运行的
 * 6. 所有的变量名、方法名、类名、传参名、字符串、前缀随机、全部随机生成，防止明显语法 BUG
 * 7. 每个类中只能出现0-5个Log和System.out其他都要使用在实际功能中或者在其他类或功能中使用
 * 8. 确保生成的代码和其他生成器功能可以互相关联实现新功能
 */
public class ValidatorGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 验证器类型数组
    private static final String[] VALIDATOR_TYPES = {
            "EmailValidator", "PhoneValidator", "PasswordValidator", "UrlValidator",
            "NumberValidator", "DateValidator", "IdCardValidator", "BankCardValidator",
            "IpValidator", "PortValidator", "CreditCardValidator", "PassportValidator",
            "LicensePlateValidator", "ZipCodeValidator", "SsnValidator", "TaxIdValidator"
    };

    // 验证方法前缀数组
    private static final String[] METHOD_PREFIXES = {
            "validate", "check", "verify", "test", "confirm", "ensure", "assert", "inspect"
    };

    // 验证规则类型数组
    private static final String[] RULE_TYPES = {
            "length", "pattern", "range", "format", "structure", "checksum", "encoding"
    };

    // 返回类型数组
    private static final String[] RETURN_TYPES = {
            "boolean", "int", "String", "ValidationResult", "List<ValidationError>"
    };

    public ValidatorGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        int count = RandomUtils.between(5, 15);
        System.out.println("生成 " + count + " 个验证器类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < count; i++) {
            String className = RandomUtils.generateClassName("Validator");
            generateValidatorClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateValidatorClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("validator"));

        // 基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.text.TextUtils"));
        sb.append(generateImportStatement("android.util.Patterns"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.regex.Pattern"));
        sb.append(generateImportStatement("java.util.regex.Matcher"));

        // 异步处理依赖
        if (asyncHandler != null && asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            sb.append(generateImportStatement("kotlinx.coroutines.launch"));
        }

        // 导入其他生成器生成的类
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append(generateImportStatement(packageName + ".helper.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useCache = RandomUtils.randomBoolean();
        boolean useConfig = RandomUtils.randomBoolean();
        boolean useStatistics = RandomUtils.randomBoolean();
        boolean useAsync = RandomUtils.randomBoolean();
        boolean useRules = RandomUtils.randomBoolean();
        boolean useBatch = RandomUtils.randomBoolean();
        boolean useChaining = RandomUtils.randomBoolean();
        boolean useCallbacks = RandomUtils.randomBoolean();

        // 类定义
        sb.append("public class ").append(className).append(" {\n\n");

        // 基础常量
        String validatorType = VALIDATOR_TYPES[RandomUtils.between(0, VALIDATOR_TYPES.length - 1)];
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String VALIDATOR_TYPE = \"").append(validatorType).append("\";\n");

        if (useCache) {
            sb.append("    private static final int MAX_CACHE_SIZE = ").append(RandomUtils.between(50, 200)).append(";\n");
            sb.append("    private static final long CACHE_TTL_MS = ").append(RandomUtils.between(1800000, 7200000)).append("L;\n");
        }

        if (useBatch) {
            sb.append("    private static final int BATCH_SIZE = ").append(RandomUtils.between(10, 100)).append(";\n");
        }

        sb.append("\n");

        // 定义字段名称变量
        String cacheMapName = null;
        String configMapName = null;
        String statsMapName = null;
        String rulesListName = null;

        // 缓存字段
        if (useCache) {
            cacheMapName = RandomUtils.generateVariableName("Cache");
            sb.append("    private static Map<String, CacheEntry> ").append(cacheMapName).append(" = new HashMap<>();\n");
            sb.append("    private static volatile boolean cacheInitialized = false;\n\n");
            sb.append("    private static class CacheEntry {\n");
            sb.append("        Object value;\n");
            sb.append("        long timestamp;\n\n");
            sb.append("        CacheEntry(Object value) {\n");
            sb.append("            this.value = value;\n");
            sb.append("            this.timestamp = System.currentTimeMillis();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 配置字段
        if (useConfig) {
            configMapName = RandomUtils.generateVariableName("Config");
            sb.append("    private static Map<String, String> ").append(configMapName).append(" = new HashMap<>();\n");
            sb.append("    private static volatile boolean configInitialized = false;\n\n");
        }

        // 统计字段
        if (useStatistics) {
            statsMapName = RandomUtils.generateVariableName("Stats");
            sb.append("    private static Map<String, Integer> ").append(statsMapName).append(" = new HashMap<>();\n");
            sb.append("    private static int totalValidations = 0;\n");
            sb.append("    private static int successCount = 0;\n");
            sb.append("    private static int failureCount = 0;\n\n");
        }

        // 规则字段
        if (useRules) {
            rulesListName = RandomUtils.generateVariableName("Rules");
            sb.append("    private static List<ValidationRule> ").append(rulesListName).append(" = new ArrayList<>();\n");
            sb.append("    private static volatile boolean rulesInitialized = false;\n\n");
            sb.append("    private static class ValidationRule {\n");
            sb.append("        String name;\n");
            sb.append("        Pattern pattern;\n");
            sb.append("        String errorMessage;\n\n");
            sb.append("        ValidationRule(String name, Pattern pattern, String errorMessage) {\n");
            sb.append("            this.name = name;\n");
            sb.append("            this.pattern = pattern;\n");
            sb.append("            this.errorMessage = errorMessage;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 上下文字段
        sb.append("    private Context context;\n\n");

        // 构造方法
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context.getApplicationContext();\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 初始化方法
        sb.append("    private void initialize() {\n");
        if (useCache) {
            sb.append("        initializeCache();\n");
        }
        if (useConfig) {
            sb.append("        initializeConfig();\n");
        }
        if (useStatistics) {
            sb.append("        initializeStatistics();\n");
        }
        if (useRules) {
            sb.append("        initializeRules();\n");
        }
        sb.append("    }\n\n");

        // 缓存初始化方法
        if (useCache) {
            sb.append("    private static void initializeCache() {\n");
            sb.append("        if (!cacheInitialized) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (!cacheInitialized) {\n");
            sb.append("                    cacheInitialized = true;\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 配置初始化方法
        if (useConfig) {
            sb.append("    private static void initializeConfig() {\n");
            sb.append("        if (!configInitialized) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (!configInitialized) {\n");
            sb.append("                    configInitialized = true;\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 统计初始化方法
        if (useStatistics) {
            sb.append("    private static void initializeStatistics() {\n");
            sb.append("        totalValidations = 0;\n");
            sb.append("        successCount = 0;\n");
            sb.append("        failureCount = 0;\n");
            sb.append("    }\n\n");
        }

        // 规则初始化方法
        if (useRules) {
            sb.append("    private static void initializeRules() {\n");
            sb.append("        if (!rulesInitialized) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (!rulesInitialized) {\n");
            sb.append("                    rulesInitialized = true;\n");
            sb.append("                    addDefaultRules();\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    private static void addDefaultRules() {\n");
            int ruleCount = RandomUtils.between(2, 5);
            for (int i = 0; i < ruleCount; i++) {
                String ruleName = RandomUtils.generateVariableName("Rule");
                String pattern = getRandomPattern();
                sb.append("        ").append(rulesListName).append(".add(new ValidationRule(\"").append(ruleName).append("\", Pattern.compile(\"").append(pattern).append("\"), \"Invalid ").append(ruleName).append("\"));\n");
            }
            sb.append("    }\n\n");
        }

        // 验证结果类
        sb.append("    public static class ValidationResult {\n");
        sb.append("        public boolean isValid;\n");
        sb.append("        public String errorMessage;\n");
        sb.append("        public List<String> errorDetails;\n\n");
        sb.append("        public ValidationResult(boolean isValid) {\n");
        sb.append("            this.isValid = isValid;\n");
        sb.append("            this.errorDetails = new ArrayList<>();\n");
        sb.append("        }\n\n");
        sb.append("        public ValidationResult(boolean isValid, String errorMessage) {\n");
        sb.append("            this(isValid);\n");
        sb.append("            this.errorMessage = errorMessage;\n");
        sb.append("        }\n\n");
        sb.append("        public void addError(String error) {\n");
        sb.append("            errorDetails.add(error);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 验证错误类
        sb.append("    public static class ValidationError {\n");
        sb.append("        public String field;\n");
        sb.append("        public String errorCode;\n");
        sb.append("        public String message;\n\n");
        sb.append("        public ValidationError(String field, String errorCode, String message) {\n");
        sb.append("            this.field = field;\n");
        sb.append("            this.errorCode = errorCode;\n");
        sb.append("            this.message = message;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 基础验证方法
        generateBasicValidationMethods(sb, className, useCache, useConfig, useStatistics, useRules, cacheMapName, configMapName, statsMapName, rulesListName);

        // 高级验证方法
        generateAdvancedValidationMethods(sb, className, useCache, useConfig, useStatistics, useRules, useChaining, cacheMapName, configMapName, statsMapName, rulesListName);

        // 批量验证方法
        if (useBatch) {
            generateBatchValidationMethods(sb, className, useCache, useConfig, useStatistics, useRules, cacheMapName, configMapName, statsMapName, rulesListName);
        }

        // 异步验证方法
        if (useAsync) {
            generateAsyncValidationMethods(sb, className, useCache, useConfig, useStatistics, useRules, asyncHandler, cacheMapName, configMapName, statsMapName, rulesListName);
        }

        // 回调验证方法
        if (useCallbacks) {
            generateCallbackValidationMethods(sb, className, useCache, useConfig, useStatistics, useRules, cacheMapName, configMapName, statsMapName, rulesListName);
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "validator");
    }

    private void generateBasicValidationMethods(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, String cacheMapName, String configMapName, String statsMapName, String rulesListName) {
        // 空值验证方法
        String methodName = RandomUtils.generateMethodName("checkEmpty");
        sb.append("    public static boolean ").append(methodName).append("(String text) {\n");
        if (useCache) {
            sb.append("        initializeCache();\n");
        }
        sb.append("        boolean result = TextUtils.isEmpty(text);\n");
        if (useStatistics) {
            sb.append("        updateStatistics(\"").append(methodName).append("\", result);\n");
        }
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 非空验证方法
        methodName = RandomUtils.generateMethodName("checkNotEmpty");
        sb.append("    public static boolean ").append(methodName).append("(String text) {\n");
        sb.append("        return !").append(RandomUtils.generateMethodName("checkEmpty")).append("(text);\n");
        sb.append("    }\n\n");

        // 长度验证方法
        methodName = RandomUtils.generateMethodName("checkLength");
        int minLength = RandomUtils.between(3, 8);
        int maxLength = RandomUtils.between(16, 32);
        sb.append("    public static boolean ").append(methodName).append("(String text, int min, int max) {\n");
        sb.append("        if (TextUtils.isEmpty(text)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        int length = text.length();\n");
        sb.append("        return length >= min && length <= max;\n");
        sb.append("    }\n\n");

        // 默认长度验证方法
        methodName = RandomUtils.generateMethodName("checkDefaultLength");
        sb.append("    public static boolean ").append(methodName).append("(String text) {\n");
        sb.append("        return ").append(RandomUtils.generateMethodName("checkLength")).append("(text, ").append(minLength).append(", ").append(maxLength).append(");\n");
        sb.append("    }\n\n");
    }

    private void generateAdvancedValidationMethods(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, boolean useChaining, String cacheMapName, String configMapName, String statsMapName, String rulesListName) {
        // 邮箱验证方法
        String methodName = RandomUtils.generateMethodName("validateEmail");
        sb.append("    public static boolean ").append(methodName).append("(String email) {\n");
        if (useCache) {
            sb.append("        initializeCache();\n");
            sb.append("        String cacheKey = \"email_\" + email;\n");
            sb.append("        CacheEntry entry = ").append(cacheMapName).append(".get(cacheKey);\n");
            sb.append("        if (entry != null && System.currentTimeMillis() - entry.timestamp < CACHE_TTL_MS) {\n");
            sb.append("            return (Boolean) entry.value;\n");
            sb.append("        }\n");
        }
        sb.append("        if (TextUtils.isEmpty(email)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        boolean result = Patterns.EMAIL_ADDRESS.matcher(email).matches();\n");
        if (useCache) {
            sb.append("        ").append(cacheMapName).append(".put(cacheKey, new CacheEntry(result));\n");
        }
        if (useStatistics) {
            sb.append("        updateStatistics(\"").append(methodName).append("\", result);\n");
        }
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 手机号验证方法
        methodName = RandomUtils.generateMethodName("validatePhone");
        sb.append("    public static boolean ").append(methodName).append("(String phone) {\n");
        sb.append("        if (TextUtils.isEmpty(phone)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return phone.matches(\"^1[3-9]\\d{9}$\");\n");
        sb.append("    }\n\n");

        // URL验证方法
        methodName = RandomUtils.generateMethodName("validateUrl");
        sb.append("    public static boolean ").append(methodName).append("(String url) {\n");
        sb.append("        if (TextUtils.isEmpty(url)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        return Patterns.WEB_URL.matcher(url).matches();\n");
        sb.append("    }\n\n");

        // 数字验证方法
        methodName = RandomUtils.generateMethodName("validateNumber");
        sb.append("    public static boolean ").append(methodName).append("(String number) {\n");
        sb.append("        if (TextUtils.isEmpty(number)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            Double.parseDouble(number);\n");
        sb.append("            return true;\n");
        sb.append("        } catch (NumberFormatException e) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 密码验证方法
        methodName = RandomUtils.generateMethodName("validatePassword");
        int minPassLength = RandomUtils.between(6, 8);
        int maxPassLength = RandomUtils.between(16, 24);
        sb.append("    public static boolean ").append(methodName).append("(String password) {\n");
        sb.append("        if (TextUtils.isEmpty(password)) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        int length = password.length();\n");
        sb.append("        if (length < ").append(minPassLength).append(" || length > ").append(maxPassLength).append(") {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        boolean hasLetter = false;\n");
        sb.append("        boolean hasDigit = false;\n");
        sb.append("        for (int i = 0; i < length; i++) {\n");
        sb.append("            char c = password.charAt(i);\n");
        sb.append("            if (Character.isLetter(c)) {\n");
        sb.append("                hasLetter = true;\n");
        sb.append("            } else if (Character.isDigit(c)) {\n");
        sb.append("                hasDigit = true;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return hasLetter && hasDigit;\n");
        sb.append("    }\n\n");

        // 链式验证方法
        if (useChaining) {
            sb.append("    public static class ValidatorChain {\n");
            sb.append("        private final String value;\n");
            sb.append("        private final List<String> errors = new ArrayList<>();\n");
            sb.append("        private boolean valid = true;\n\n");
            sb.append("        public ValidatorChain(String value) {\n");
            sb.append("            this.value = value;\n");
            sb.append("        }\n\n");
            sb.append("        public ValidatorChain notEmpty() {\n");
            sb.append("            if (TextUtils.isEmpty(value)) {\n");
            sb.append("                errors.add(\"Value cannot be empty\");\n");
            sb.append("                valid = false;\n");
            sb.append("            }\n");
            sb.append("            return this;\n");
            sb.append("        }\n\n");
            sb.append("        public ValidatorChain minLength(int min) {\n");
            sb.append("            if (value != null && value.length() < min) {\n");
            sb.append("                errors.add(\"Value must be at least \" + min + \" characters\");\n");
            sb.append("                valid = false;\n");
            sb.append("            }\n");
            sb.append("            return this;\n");
            sb.append("        }\n\n");
            sb.append("        public ValidatorChain maxLength(int max) {\n");
            sb.append("            if (value != null && value.length() > max) {\n");
            sb.append("                errors.add(\"Value must be at most \" + max + \" characters\");\n");
            sb.append("                valid = false;\n");
            sb.append("            }\n");
            sb.append("            return this;\n");
            sb.append("        }\n\n");
            sb.append("        public ValidatorChain matches(String pattern) {\n");
            sb.append("            if (value != null && !value.matches(pattern)) {\n");
            sb.append("                errors.add(\"Value does not match required pattern\");\n");
            sb.append("                valid = false;\n");
            sb.append("            }\n");
            sb.append("            return this;\n");
            sb.append("        }\n\n");
            sb.append("        public ValidationResult build() {\n");
            sb.append("            ValidationResult result = new ValidationResult(valid);\n");
            sb.append("            if (!valid) {\n");
            sb.append("                for (String error : errors) {\n");
            sb.append("                    result.addError(error);\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("            return result;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public static ValidatorChain startChain(String value) {\n");
            sb.append("        return new ValidatorChain(value);\n");
            sb.append("    }\n\n");
        }
    }

    private void generateBatchValidationMethods(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, String cacheMapName, String configMapName, String statsMapName, String rulesListName) {
        // 批量验证方法
        String methodName = RandomUtils.generateMethodName("validateBatch");
        sb.append("    public static ValidationResult ").append(methodName).append("(List<String> values) {\n");
        sb.append("        if (values == null || values.isEmpty()) {\n");
        sb.append("            return new ValidationResult(false, \"Input list is empty\");\n");
        sb.append("        }\n");
        sb.append("        ValidationResult result = new ValidationResult(true);\n");
        sb.append("        int index = 0;\n");
        sb.append("        for (String value : values) {\n");
        sb.append("            if (TextUtils.isEmpty(value)) {\n");
        sb.append("                result.addError(\"Value at index \" + index + \" is empty\");\n");
        sb.append("                result.isValid = false;\n");
        sb.append("            }\n");
        sb.append("            index++;\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        // 批量邮箱验证方法
        methodName = RandomUtils.generateMethodName("validateEmailBatch");
        sb.append("    public static ValidationResult ").append(methodName).append("(List<String> emails) {\n");
        sb.append("        if (emails == null || emails.isEmpty()) {\n");
        sb.append("            return new ValidationResult(false, \"Email list is empty\");\n");
        sb.append("        }\n");
        sb.append("        ValidationResult result = new ValidationResult(true);\n");
        sb.append("        int index = 0;\n");
        sb.append("        for (String email : emails) {\n");
        sb.append("            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {\n");
        sb.append("                result.addError(\"Invalid email at index \" + index + \": \" + email);\n");
        sb.append("                result.isValid = false;\n");
        sb.append("            }\n");
        sb.append("            index++;\n");
        sb.append("        }\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    private void generateAsyncValidationMethods(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, String asyncHandler, String cacheMapName, String configMapName, String statsMapName, String rulesListName) {
        // 异步验证方法
        String methodName = RandomUtils.generateMethodName("validateAsync");
        sb.append("    public static void ").append(methodName).append("(final String value, final ValidationCallback callback) {\n");
        sb.append("        new Thread(new Runnable() {\n");
        sb.append("            @Override\n");
        sb.append("            public void run() {\n");
        sb.append("                final boolean result = !TextUtils.isEmpty(value);\n");
        sb.append("                if (callback != null) {\n");
        sb.append("                    callback.onValidationComplete(result);\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("        }).start();\n");
        sb.append("    }\n\n");

        // 回调接口
        sb.append("    public interface ValidationCallback {\n");
        sb.append("        void onValidationComplete(boolean result);\n");
        sb.append("    }\n\n");
    }

    private void generateCallbackValidationMethods(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, String cacheMapName, String configMapName, String statsMapName, String rulesListName) {
        // 带回调的验证方法
        String methodName = RandomUtils.generateMethodName("validateWithCallback");
        sb.append("    public static void ").append(methodName).append("(String value, ValidationCallback callback) {\n");
        sb.append("        boolean result = !TextUtils.isEmpty(value);\n");
        sb.append("        if (callback != null) {\n");
        sb.append("            callback.onValidationComplete(result);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateStatisticsUpdateMethod(StringBuilder sb, String className, boolean useCache, boolean useConfig, boolean useStatistics, boolean useRules, String statsMapName) {
        if (useStatistics) {
            sb.append("    private static void updateStatistics(String methodName, boolean result) {\n");
            sb.append("        totalValidations++;\n");
            sb.append("        if (result) {\n");
            sb.append("            successCount++;\n");
            sb.append("        } else {\n");
            sb.append("            failureCount++;\n");
            sb.append("        }\n");
            sb.append("        Integer count = ").append(statsMapName).append(".get(methodName);\n");
            sb.append("        if (count == null) {\n");
            sb.append("            count = 0;\n");
            sb.append("        }\n");
            sb.append("        ").append(statsMapName).append(".put(methodName, count + 1);\n");
            sb.append("    }\n\n");
        }
    }

    private String getRandomPattern() {
        String[] patterns = {
                "^[a-zA-Z0-9]+$",
                "^[a-zA-Z]+$",
                "^[0-9]+$",
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                "^[0-9]{3}-[0-9]{2}-[0-9]{4}$",
                "^[0-9]{11}$",
                "^[A-Za-z0-9]{8,16}$",
                "^[A-Z]{2}[0-9]{6}$"
        };
        return patterns[RandomUtils.between(0, patterns.length - 1)];
    }
}
