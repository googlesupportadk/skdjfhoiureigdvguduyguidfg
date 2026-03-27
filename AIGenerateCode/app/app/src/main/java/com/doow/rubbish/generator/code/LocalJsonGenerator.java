package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalJsonGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] JSON_TYPES = {
            "JsonObject", "JsonArray", "JsonParser", "JsonBuilder", "JsonSerializer",
            "JsonDeserializer", "JsonValidator", "JsonPath", "JsonPointer", "JsonPatch",
            "JsonMerge", "JsonDiff", "JsonSchema", "JsonConverter", "JsonFilter"
    };

    private static final String[] DATA_TYPES = {
            "String", "Integer", "Boolean", "Double", "Object",
            "Long", "Float", "Short", "Byte", "Character",
            "BigDecimal", "BigInteger", "Date", "List", "Map",
            "Set", "Array", "Null", "Number", "JsonElement"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useAsyncOperations;
    private boolean useJsonValidation;
    private boolean useJsonTransformation;
    private boolean useJsonCaching;
    private boolean useJsonEncryption;
    private boolean useJsonCompression;
    private boolean useJsonStreaming;
    private boolean useJsonMerge;

    public LocalJsonGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAsyncOperations = RandomUtils.randomBoolean();
        useJsonValidation = RandomUtils.randomBoolean();
        useJsonTransformation = RandomUtils.randomBoolean();
        useJsonCaching = RandomUtils.randomBoolean();
        useJsonEncryption = RandomUtils.randomBoolean();
        useJsonCompression = RandomUtils.randomBoolean();
        useJsonStreaming = RandomUtils.randomBoolean();
        useJsonMerge = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成JSON类");

        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Json");
            generateJsonClass(className, asyncHandler);
        }
    }

    private void generateJsonClass(String className, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("json"));

        // 生成基础导入
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("org.json.*"));

        // 根据功能标志添加条件导入
        if (useAsyncOperations) {
            if (asyncHandler.contains("coroutines")) {
                sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
                sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
            } else if (asyncHandler.contains("rxjava")) {
                sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
                sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
            }
        }

        if (useJsonValidation) {
            sb.append(generateImportStatement("java.util.regex.Pattern"));
        }

        if (useJsonCaching) {
            sb.append(generateImportStatement("android.util.LruCache"));
        }

        if (useJsonEncryption) {
            sb.append(generateImportStatement("javax.crypto.Cipher"));
            sb.append(generateImportStatement("javax.crypto.spec.SecretKeySpec"));
        }

        if (useJsonCompression) {
            sb.append(generateImportStatement("java.util.zip.Deflater"));
            sb.append(generateImportStatement("java.util.zip.Inflater"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择JSON类型和数据类型
        String jsonType = JSON_TYPES[RandomUtils.between(0, JSON_TYPES.length - 1)];
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String jsonTypeVarName = RandomUtils.generateWord(6);
        String dataTypeVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(jsonTypeVarName).append(" = \"").append(jsonType).append("\";\n");
        sb.append("    private static final String ").append(dataTypeVarName).append(" = \"").append(dataType).append("\";\n\n");

        // 生成实例字段
        String jsonObjectVarName = RandomUtils.generateWord(6);
        String jsonArrayVarName = RandomUtils.generateWord(6);
        String cacheVarName = RandomUtils.generateWord(6);
        String encryptionKeyVarName = RandomUtils.generateWord(6);
        String compressionLevelVarName = RandomUtils.generateWord(6);
        String validatorPatternVarName = RandomUtils.generateWord(6);

        sb.append("    private JSONObject ").append(jsonObjectVarName).append(";\n");
        sb.append("    private JSONArray ").append(jsonArrayVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useJsonCaching) {
            sb.append("    private LruCache<String, String> ").append(cacheVarName).append(";\n");
        }

        if (useJsonEncryption) {
            sb.append("    private byte[] ").append(encryptionKeyVarName).append(";\n");
        }

        if (useJsonCompression) {
            sb.append("    private int ").append(compressionLevelVarName).append(" = ").append(RandomUtils.between(1, 9)).append(";\n");
        }

        if (useJsonValidation) {
            sb.append("    private Pattern ").append(validatorPatternVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        generateConstructor(sb, className, jsonObjectVarName, jsonArrayVarName, cacheVarName,
                encryptionKeyVarName, compressionLevelVarName, validatorPatternVarName);

        // 生成基础方法
        generateBasicMethods(sb, jsonObjectVarName, jsonArrayVarName, tagVarName, dataType);

        // 根据功能标志添加条件方法
        if (useAsyncOperations) {
            generateAsyncMethods(sb, jsonObjectVarName, jsonArrayVarName, tagVarName, asyncHandler);
        }

        if (useJsonValidation) {
            generateValidationMethods(sb, jsonObjectVarName, validatorPatternVarName, tagVarName);
        }

        if (useJsonTransformation) {
            generateTransformationMethods(sb, jsonObjectVarName, jsonArrayVarName, tagVarName);
        }

        if (useJsonCaching) {
            generateCachingMethods(sb, jsonObjectVarName, cacheVarName, tagVarName);
        }

        if (useJsonEncryption) {
            generateEncryptionMethods(sb, jsonObjectVarName, encryptionKeyVarName, tagVarName);
        }

        if (useJsonCompression) {
            generateCompressionMethods(sb, jsonObjectVarName, compressionLevelVarName, tagVarName);
        }

        if (useJsonStreaming) {
            generateStreamingMethods(sb, jsonObjectVarName, tagVarName);
        }

        if (useJsonMerge) {
            generateMergeMethods(sb, jsonObjectVarName, jsonArrayVarName, tagVarName);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, jsonObjectVarName, jsonArrayVarName, tagVarName,
                cacheVarName, encryptionKeyVarName, compressionLevelVarName,
                validatorPatternVarName, dataType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "json");
    }

    /**
     * 生成构造函数
     */
    private void generateConstructor(StringBuilder sb, String className, String jsonObjectVarName,
                                     String jsonArrayVarName, String cacheVarName,
                                     String encryptionKeyVarName, String compressionLevelVarName,
                                     String validatorPatternVarName) {
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        try {\n");
        sb.append("            ").append(jsonObjectVarName).append(" = new JSONObject();\n");
        sb.append("            ").append(jsonArrayVarName).append(" = new JSONArray();\n");

        if (useJsonCaching) {
            sb.append("            ").append(cacheVarName).append(" = new LruCache<>(").append(RandomUtils.between(10, 50)).append(");\n");
        }

        if (useJsonEncryption) {
            sb.append("            ").append(encryptionKeyVarName).append(" = new byte[").append(RandomUtils.between(16, 32)).append("];\n");
            sb.append("            for (int i = 0; i < ").append(encryptionKeyVarName).append(".length; i++) {\n");
            sb.append("                ").append(encryptionKeyVarName).append("[i] = (byte) RandomUtils.between(0, 256);\n");
            sb.append("            }\n");
        }

        if (useJsonValidation) {
            sb.append("            ").append(validatorPatternVarName).append(" = Pattern.compile(\"").append(generateRandomRegexPattern()).append("\");\n");
        }

        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(RandomUtils.generateRandomString()).append(", \"Error initializing ").append(className).append("\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String jsonObjectVarName, String jsonArrayVarName,
                                      String tagVarName, String dataType) {
        // 生成put方法
        String putMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(putMethodName).append("(String ").append(keyParamName).append(", ")
                .append(dataType).append(" ").append(valueParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            ").append(jsonObjectVarName).append(".put(").append(keyParamName).append(", ")
                .append(valueParamName).append(");\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Put: \").append(").append(keyParamName)
                .append(").append(\" = \").append(").append(valueParamName).append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error putting value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成get方法
        String getMethodName = RandomUtils.generateWord(6);

        sb.append("    public ").append(dataType).append(" ").append(getMethodName).append("(String ")
                .append(keyParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName).append(")) {\n");
        sb.append("                return (").append(dataType).append(") ").append(jsonObjectVarName)
                .append(".get(").append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("            return null;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error getting value\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成添加到数组的方法
        String addToArrayMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(addToArrayMethodName).append("(").append(dataType).append(" ")
                .append(valueParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            ").append(jsonArrayVarName).append(".put(").append(valueParamName).append(");\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Added: \").append(").append(valueParamName)
                .append(");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error adding value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成从数组获取的方法
        String getFromArrayMethodName = RandomUtils.generateWord(6);
        String indexParamName = RandomUtils.generateWord(6);

        sb.append("    public ").append(dataType).append(" ").append(getFromArrayMethodName).append("(int ")
                .append(indexParamName).append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(indexParamName).append(" >= 0 && ").append(indexParamName)
                .append(" < ").append(jsonArrayVarName).append(".length()) {\n");
        sb.append("                return (").append(dataType).append(") ").append(jsonArrayVarName)
                .append(".get(").append(indexParamName).append(");\n");
        sb.append("            }\n");
        sb.append("            return null;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error getting value at index\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成toString方法
        String objectToStringMethodName = RandomUtils.generateWord(6);
        String arrayToStringMethodName = RandomUtils.generateWord(6);

        sb.append("    public String ").append(objectToStringMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return ").append(jsonObjectVarName).append(".toString();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error converting to string\", e);\n");
        sb.append("            return \"{}\";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(arrayToStringMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            return ").append(jsonArrayVarName).append(".toString();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error converting array to string\", e);\n");
        sb.append("            return \"[]\";\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成clear方法
        String clearMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            ").append(jsonObjectVarName).append(" = new JSONObject();\n");
        sb.append("            ").append(jsonArrayVarName).append(" = new JSONArray();\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"JSON cleared\");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error clearing JSON\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String jsonObjectVarName, String jsonArrayVarName,
                                      String tagVarName, String asyncHandler) {
        String asyncPutMethodName = RandomUtils.generateWord(6);
        String asyncGetMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncPutMethodName).append("(String ").append(keyParamName)
                    .append(", String ").append(valueParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.IO).launch(() -> {\n");
            sb.append("            try {\n");
            sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
            sb.append("                Log.d(").append(tagVarName).append(", \"Async put: \").append(")
                    .append(keyParamName).append(").append(\" = \").append(").append(valueParamName).append(");\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Error in async put\", e);\n");
            sb.append("            }\n");
            sb.append("        });\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncGetMethodName).append("(String ").append(keyParamName)
                    .append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.IO).launch(() -> {\n");
            sb.append("            try {\n");
            sb.append("                if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                    .append(")) {\n");
            sb.append("                    Object value = ").append(jsonObjectVarName).append(".get(")
                    .append(keyParamName).append(");\n");
            sb.append("                    Log.d(").append(tagVarName).append(", \"Async get: \").append(")
                    .append(keyParamName).append(").append(\" = \").append(value);\n");
            sb.append("                }\n");
            sb.append("            } catch (Exception e) {\n");
            sb.append("                Log.e(").append(tagVarName).append(", \"Error in async get\", e);\n");
            sb.append("            }\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncPutMethodName).append("(String ").append(keyParamName)
                    .append(", String ").append(valueParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> Log.d(").append(tagVarName).append(", \"Async put: \").append(")
                    .append(keyParamName).append(").append(\" = \").append(").append(valueParamName).append("),\n");
            sb.append("              error -> Log.e(").append(tagVarName).append(", \"Error in async put\", error)\n");
            sb.append("          );\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncGetMethodName).append("(String ").append(keyParamName)
                    .append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                    .append(")) {\n");
            sb.append("                return ").append(jsonObjectVarName).append(".get(").append(keyParamName)
                    .append(");\n");
            sb.append("            }\n");
            sb.append("            return null;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              value -> Log.d(").append(tagVarName).append(", \"Async get: \").append(")
                    .append(keyParamName).append(").append(\" = \").append(value),\n");
            sb.append("              error -> Log.e(").append(tagVarName).append(", \"Error in async get\", error)\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成验证方法
     */
    private void generateValidationMethods(StringBuilder sb, String jsonObjectVarName,
                                           String validatorPatternVarName, String tagVarName) {
        String validateMethodName = RandomUtils.generateWord(6);
        String isValidMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public boolean ").append(isValidMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (!").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                return false;\n");
        sb.append("            }\n");
        sb.append("            String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("            return ").append(validatorPatternVarName).append(".matcher(value).matches();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error validating value\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(validateMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(isValidMethodName).append("(").append(keyParamName).append(")) {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Value is valid\");\n");
        sb.append("            } else {\n");
        sb.append("                Log.w(").append(tagVarName).append(", \"Value is invalid\");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error in validation\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成转换方法
     */
    private void generateTransformationMethods(StringBuilder sb, String jsonObjectVarName,
                                               String jsonArrayVarName, String tagVarName) {
        String transformMethodName = RandomUtils.generateWord(6);
        String reverseMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(transformMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                String transformed = new StringBuilder(value).reverse().toString();\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_transformed\", transformed);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Transformed: \").append(")
                .append(keyParamName).append(").append(\" -> \").append(transformed);\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error in transformation\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(reverseMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            JSONArray reversed = new JSONArray();\n");
        sb.append("            for (int i = ").append(jsonArrayVarName).append(".length() - 1; i >= 0; i--) {\n");
        sb.append("                reversed.put(").append(jsonArrayVarName).append(".get(i));\n");
        sb.append("            }\n");
        sb.append("            ").append(jsonArrayVarName).append(" = reversed;\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Array reversed\");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error reversing array\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成缓存方法
     */
    private void generateCachingMethods(StringBuilder sb, String jsonObjectVarName,
                                        String cacheVarName, String tagVarName) {
        String cacheMethodName = RandomUtils.generateWord(6);
        String getCachedMethodName = RandomUtils.generateWord(6);
        String clearCacheMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(cacheMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                ").append(cacheVarName).append(".put(").append(keyParamName)
                .append(", value);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cached: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error caching value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public String ").append(getCachedMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            String cachedValue = ").append(cacheVarName).append(".get(")
                .append(keyParamName).append(");\n");
        sb.append("            if (cachedValue != null) {\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Cache hit: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("                return cachedValue;\n");
        sb.append("            } else if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                ").append(cacheMethodName).append("(").append(keyParamName).append(");\n");
        sb.append("                return value;\n");
        sb.append("            }\n");
        sb.append("            return null;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error getting cached value\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearCacheMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            ").append(cacheVarName).append(".evictAll();\n");
        sb.append("            Log.d(").append(tagVarName).append(", \"Cache cleared\");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error clearing cache\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成加密方法
     */
    private void generateEncryptionMethods(StringBuilder sb, String jsonObjectVarName,
                                           String encryptionKeyVarName, String tagVarName) {
        String encryptMethodName = RandomUtils.generateWord(6);
        String decryptMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(encryptMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                byte[] encrypted = encrypt(value, ").append(encryptionKeyVarName)
                .append(");\n");
        sb.append("                String encryptedBase64 = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_encrypted\", encryptedBase64);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Encrypted: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error encrypting value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(decryptMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            String encryptedKey = ").append(keyParamName).append(" + \"_encrypted\";\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(encryptedKey)) {\n");
        sb.append("                String encryptedBase64 = ").append(jsonObjectVarName)
                .append(".getString(encryptedKey);\n");
        sb.append("                byte[] encrypted = android.util.Base64.decode(encryptedBase64, android.util.Base64.DEFAULT);\n");
        sb.append("                String decrypted = decrypt(encrypted, ").append(encryptionKeyVarName)
                .append(");\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_decrypted\", decrypted);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Decrypted: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error decrypting value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private byte[] encrypt(String value, byte[] key) throws Exception {\n");
        sb.append("        SecretKeySpec keySpec = new SecretKeySpec(key, \"AES\");\n");
        sb.append("        Cipher cipher = Cipher.getInstance(\"AES/ECB/PKCS5Padding\");\n");
        sb.append("        cipher.init(Cipher.ENCRYPT_MODE, keySpec);\n");
        sb.append("        return cipher.doFinal(value.getBytes());\n");
        sb.append("    }\n\n");

        sb.append("    private String decrypt(byte[] encrypted, byte[] key) throws Exception {\n");
        sb.append("        SecretKeySpec keySpec = new SecretKeySpec(key, \"AES\");\n");
        sb.append("        Cipher cipher = Cipher.getInstance(\"AES/ECB/PKCS5Padding\");\n");
        sb.append("        cipher.init(Cipher.DECRYPT_MODE, keySpec);\n");
        sb.append("        byte[] decrypted = cipher.doFinal(encrypted);\n");
        sb.append("        return new String(decrypted);\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成压缩方法
     */
    private void generateCompressionMethods(StringBuilder sb, String jsonObjectVarName,
                                            String compressionLevelVarName, String tagVarName) {
        String compressMethodName = RandomUtils.generateWord(6);
        String decompressMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(compressMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                byte[] compressed = compress(value.getBytes(), ")
                .append(compressionLevelVarName).append(");\n");
        sb.append("                String compressedBase64 = android.util.Base64.encodeToString(compressed, android.util.Base64.DEFAULT);\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_compressed\", compressedBase64);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Compressed: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error compressing value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(decompressMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            String compressedKey = ").append(keyParamName).append(" + \"_compressed\";\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(compressedKey)) {\n");
        sb.append("                String compressedBase64 = ").append(jsonObjectVarName)
                .append(".getString(compressedKey);\n");
        sb.append("                byte[] compressed = android.util.Base64.decode(compressedBase64, android.util.Base64.DEFAULT);\n");
        sb.append("                byte[] decompressed = decompress(compressed);\n");
        sb.append("                String decompressedString = new String(decompressed);\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_decompressed\", decompressedString);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Decompressed: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error decompressing value\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private byte[] compress(byte[] data, int level) throws Exception {\n");
        sb.append("        Deflater deflater = new Deflater(level);\n");
        sb.append("        deflater.setInput(data);\n");
        sb.append("        deflater.finish();\n");
        sb.append("        byte[] buffer = new byte[data.length];\n");
        sb.append("        int compressedSize = deflater.deflate(buffer);\n");
        sb.append("        byte[] result = new byte[compressedSize];\n");
        sb.append("        System.arraycopy(buffer, 0, result, 0, compressedSize);\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");

        sb.append("    private byte[] decompress(byte[] data) throws Exception {\n");
        sb.append("        Inflater inflater = new Inflater();\n");
        sb.append("        inflater.setInput(data);\n");
        sb.append("        byte[] buffer = new byte[data.length * 10];\n");
        sb.append("        int decompressedSize = inflater.inflate(buffer);\n");
        sb.append("        byte[] result = new byte[decompressedSize];\n");
        sb.append("        System.arraycopy(buffer, 0, result, 0, decompressedSize);\n");
        sb.append("        return result;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成流式处理方法
     */
    private void generateStreamingMethods(StringBuilder sb, String jsonObjectVarName, String tagVarName) {
        String streamProcessMethodName = RandomUtils.generateWord(6);
        String streamFilterMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(streamProcessMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                String processed = value.chars()\n");
        sb.append("                    .mapToObj(c -> String.valueOf((char) c))\n");
        sb.append("                    .reduce(\"\", (a, b) -> a + b);\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_processed\", processed);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Stream processed: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error in stream processing\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(streamFilterMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                String value = ").append(jsonObjectVarName).append(".getString(")
                .append(keyParamName).append(");\n");
        sb.append("                String filtered = value.chars()\n");
        sb.append("                    .filter(c -> !Character.isWhitespace(c))\n");
        sb.append("                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)\n");
        sb.append("                    .toString();\n");
        sb.append("                ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(" + \"_filtered\", filtered);\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Stream filtered: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error in stream filtering\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成合并方法
     */
    private void generateMergeMethods(StringBuilder sb, String jsonObjectVarName,
                                      String jsonArrayVarName, String tagVarName) {
        String mergeObjectMethodName = RandomUtils.generateWord(6);
        String mergeArrayMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(mergeObjectMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                JSONObject other = new JSONObject(").append(jsonObjectVarName)
                .append(".getString(").append(keyParamName).append("));\n");
        sb.append("                java.util.Iterator<String> keys = other.keys();\n");
        sb.append("                while (keys.hasNext()) {\n");
        sb.append("                    String key = keys.next();\n");
        sb.append("                    Object value = other.get(key);\n");
        sb.append("                    ").append(jsonObjectVarName).append(".put(key, value);\n");
        sb.append("                }\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Merged object: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error merging object\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(mergeArrayMethodName).append("(String ").append(keyParamName)
                .append(") {\n");
        sb.append("        try {\n");
        sb.append("            if (").append(jsonObjectVarName).append(".has(").append(keyParamName)
                .append(")) {\n");
        sb.append("                JSONArray other = new JSONArray(").append(jsonObjectVarName)
                .append(".getString(").append(keyParamName).append("));\n");
        sb.append("                for (int i = 0; i < other.length(); i++) {\n");
        sb.append("                    ").append(jsonArrayVarName).append(".put(other.get(i));\n");
        sb.append("                }\n");
        sb.append("                Log.d(").append(tagVarName).append(", \"Merged array: \").append(")
                .append(keyParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error merging array\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className, String jsonObjectVarName,
                                            String jsonArrayVarName, String tagVarName,
                                            String cacheVarName, String encryptionKeyVarName,
                                            String compressionLevelVarName, String validatorPatternVarName,
                                            String dataType) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String keyParamName = RandomUtils.generateWord(6);
        String valueParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            // 基本操作\n");
        sb.append("            String ").append(keyParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("            String ").append(valueParamName).append(" = \"").append(RandomUtils.generateWord(6))
                .append("\";\n");
        sb.append("            ").append(jsonObjectVarName).append(".put(").append(keyParamName)
                .append(", ").append(valueParamName).append(");\n");

        if (useJsonValidation) {
            sb.append("            // 验证\n");
            sb.append("            boolean isValid = ").append(validatorPatternVarName).append(".matcher(")
                    .append(valueParamName).append(").matches();\n");
        }

        if (useJsonCaching) {
            sb.append("            // 缓存\n");
            sb.append("            ").append(cacheVarName).append(".put(").append(keyParamName)
                    .append(", ").append(valueParamName).append(");\n");
        }

        if (useJsonEncryption) {
            sb.append("            // 加密\n");
            sb.append("            byte[] encrypted = encrypt(").append(valueParamName).append(", ")
                    .append(encryptionKeyVarName).append(");\n");
        }

        if (useJsonCompression) {
            sb.append("            // 压缩\n");
            sb.append("            byte[] compressed = compress(").append(valueParamName).append(".getBytes(), ")
                    .append(compressionLevelVarName).append(");\n");
        }

        if (useJsonStreaming) {
            sb.append("            // 流式处理\n");
            sb.append("            String processed = ").append(valueParamName).append(".chars()\n");
            sb.append("                .mapToObj(c -> String.valueOf((char) c))\n");
            sb.append("                .reduce(\"\", (a, b) -> a + b);\n");
        }

        if (useJsonMerge) {
            sb.append("            // 合并\n");
            sb.append("            JSONObject other = new JSONObject();\n");
            sb.append("            other.put(\"otherKey\", \"otherValue\");\n");
            sb.append("            java.util.Iterator<String> keys = other.keys();\n");
            sb.append("            while (keys.hasNext()) {\n");
            sb.append("                String key = keys.next();\n");
            sb.append("                ").append(jsonObjectVarName).append(".put(key, other.get(key));\n");
            sb.append("            }\n");
        }

        sb.append("            Log.d(").append(tagVarName).append(", \"Example usage completed\");\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(").append(tagVarName).append(", \"Error in example usage\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成随机正则表达式模式
     */
    private String generateRandomRegexPattern() {
        String[] patterns = {
                "^[a-zA-Z0-9]+$",
                "^\\d+$",
                "^[a-z]+$",
                "^[A-Z]+$",
                "^[a-zA-Z]+$",
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
                "^\\d{4}-\\d{2}-\\d{2}$",
                "^\\d{2}:\\d{2}:\\d{2}$",
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        };
        return patterns[RandomUtils.between(0, patterns.length - 1)];
    }
}