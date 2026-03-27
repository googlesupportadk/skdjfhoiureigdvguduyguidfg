
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * 增强版本地安全生成器
 * 满足以下要求：
 * 1. 确保代码功能的随机性和多样性灵活性，避免冗余代码和垃圾代码
 * 2. 在生成的项目中字段都会被使用
 * 3. 在生成的项目中方法都会被调用
 * 4. 不会产生未使用的代码
 * 5. 确保在生成的项目中代码是完整可运行的，且生成器最后生成的java代码也是可运行的
 * 6. 生成的项目中所有的变量名、方法名、类名、传参名、字符串、前缀随机、全部随机生成，防止明显语法BUG
 * 7. 每个类中只能出现0-5个Log和System.out其他都要使用在实际功能中或者在其他类或功能中使用
 * 8. 确保生成的项目中代码和其他生成器功能可以互相关联实现新功能
 * 确保功能无三方依赖，仅安卓系统库
 */
public class LocalSecurityGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 加密算法类型
    private static final String[] ENCRYPTION_ALGORITHMS = {
            "AES", "DES", "DESede", "Blowfish", "RC2", "ARCFOUR"
    };

    // 哈希算法类型
    private static final String[] HASH_ALGORITHMS = {
            "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"
    };

    // 密钥生成算法类型
    private static final String[] KEY_ALGORITHMS = {
            "AES", "DES", "HmacSHA1", "HmacSHA256", "HmacSHA512"
    };

    // 编码类型
    private static final String[] ENCODING_TYPES = {
            "UTF-8", "ISO-8859-1", "US-ASCII", "UTF-16", "UTF-16BE", "UTF-16LE"
    };

    // Base64变体
    private static final String[] BASE64_VARIANTS = {
            "Base64", "Base64.getUrlEncoder()", "Base64.getMimeEncoder()"
    };

    // 功能特性标志
    private boolean includeKeyGeneration;
    private String tagVar;
    private boolean includeKeyValidation;
    private boolean includeKeyStorage;
    private boolean includeEncryptionChain;
    private boolean includeHashChain;
    private boolean includeDataTransformation;
    private boolean includeSecurityUtils;
    private boolean includeCipherPool;

    public LocalSecurityGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatures();
    }

    /**
     * 随机初始化功能特性
     */
    private void initializeFeatures() {
        this.includeKeyGeneration = RandomUtils.randomBoolean();
        this.includeKeyValidation = RandomUtils.randomBoolean();
        this.includeKeyStorage = RandomUtils.randomBoolean();
        this.includeEncryptionChain = RandomUtils.randomBoolean();
        this.includeHashChain = RandomUtils.randomBoolean();
        this.includeDataTransformation = RandomUtils.randomBoolean();
        this.includeSecurityUtils = RandomUtils.randomBoolean();
        this.includeCipherPool = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地安全相关代码（增强版）");

        int classCount = RandomUtils.between(3, 8);
        for (int i = 0; i < classCount; i++) {
            // 每次生成前重新随机初始化功能特性
            initializeFeatures();

            String className = RandomUtils.generateClassName("Security");
            String algorithmType = RandomUtils.randomChoice(ENCRYPTION_ALGORITHMS);
            generateSecurityClass(className, algorithmType);
        }
    }

    private void generateSecurityClass(String className, String algorithmType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("security"));

        // 添加必要的导入语句
        generateImports(sb);

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成字段
        generateFields(sb, className, algorithmType);

        // 生成构造方法
        generateConstructors(sb, className);

        // 生成方法
        generateMethods(sb, className, algorithmType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "security");
    }

    /**
     * 生成导入语句
     */
    private void generateImports(StringBuilder sb) {
        sb.append(generateImportStatement("javax.crypto.Cipher"));
        sb.append(generateImportStatement("javax.crypto.spec.SecretKeySpec"));
        sb.append(generateImportStatement("javax.crypto.spec.IvParameterSpec"));
        sb.append(generateImportStatement("javax.crypto.KeyGenerator"));
        sb.append(generateImportStatement("javax.crypto.Mac"));
        sb.append(generateImportStatement("javax.crypto.SecretKey"));
        sb.append(generateImportStatement("javax.crypto.spec.PBEKeySpec"));
        sb.append(generateImportStatement("javax.crypto.SecretKeyFactory"));
        sb.append(generateImportStatement("java.security.Key"));
        sb.append(generateImportStatement("java.security.MessageDigest"));
        sb.append(generateImportStatement("java.security.SecureRandom"));
        sb.append(generateImportStatement("java.security.spec.KeySpec"));
        sb.append(generateImportStatement("java.util.Base64"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");
    }

    /**
     * 生成字段
     */
    private void generateFields(StringBuilder sb, String className, String algorithmType) {
        // 生成TAG常量
        tagVar = generateRandomVarName("tag");
        sb.append("    private static final String ").append(tagVar).append(" = \"").append(className).append("\";\n");

        // 生成算法类型常量
        String algoVar = generateRandomVarName("algorithm");
        sb.append("    private static final String ").append(algoVar).append(" = \"").append(algorithmType).append("\";\n");

        // 生成编码常量
        String encodingVar = generateRandomVarName("encoding");
        String encoding = RandomUtils.randomChoice(ENCODING_TYPES);
        sb.append("    private static final String ").append(encodingVar).append(" = \"").append(encoding).append("\";\n");

        // 根据功能特性生成其他字段
        if (includeKeyStorage) {
            String keyStoreVar = generateRandomVarName("keyStore");
            sb.append("    private Map<String, byte[]> ").append(keyStoreVar).append(" = new HashMap<>();\n");
        }

        if (includeCipherPool) {
            String cipherPoolVar = generateRandomVarName("cipherPool");
            sb.append("    private Map<String, Cipher> ").append(cipherPoolVar).append(" = new HashMap<>();\n");
        }

        if (includeDataTransformation) {
            String transformVar = generateRandomVarName("transform");
            sb.append("    private String ").append(transformVar).append(" = \"").append(generateRandomTransform()).append("\";\n");
        }

        sb.append("\n");
    }

    /**
     * 生成构造方法
     */
    private void generateConstructors(StringBuilder sb, String className) {
        // 无参构造方法
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        this(\"").append(generateRandomKey()).append("\");\n");
        sb.append("    }\n\n");

        // 带密钥的构造方法
        String keyParam = generateRandomParamName("key");
        sb.append("    public ").append(className).append("(String ").append(keyParam).append(") {\n");
        sb.append("        if (").append(keyParam).append(" != null && !").append(keyParam).append(".isEmpty()) {\n");
        sb.append("            initializeKey(").append(keyParam).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成方法
     */
    private void generateMethods(StringBuilder sb, String className, String algorithmType) {
        // 生成加密方法
        generateEncryptMethod(sb, className, algorithmType);

        // 生成解密方法
        generateDecryptMethod(sb, className, algorithmType);

        // 生成哈希方法
        if (includeHashChain) {
            generateHashMethod(sb, className);
            generateHashChainMethod(sb, className);
        }

        // 生成密钥生成方法
        if (includeKeyGeneration) {
            generateKeyGenerationMethod(sb, className);
            generateSecureRandomMethod(sb, className);
        }

        // 生成密钥验证方法
        if (includeKeyValidation) {
            generateKeyValidationMethod(sb, className);
        }

        // 生成密钥存储方法
        if (includeKeyStorage) {
            generateKeyStorageMethods(sb, className);
        }

        // 生成加密链方法
        if (includeEncryptionChain) {
            generateEncryptionChainMethod(sb, className);
        }

        // 生成数据转换方法
        if (includeDataTransformation) {
            generateDataTransformMethod(sb, className);
        }

        // 生成安全工具方法
        if (includeSecurityUtils) {
            generateSecurityUtilsMethods(sb, className);
        }

        // 生成密钥池方法
        if (includeCipherPool) {
            generateCipherPoolMethods(sb, className);
        }
    }

    /**
     * 生成加密方法
     */
    private void generateEncryptMethod(StringBuilder sb, String className, String algorithmType) {
        String methodName = generateRandomMethodName("encrypt");
        String dataParam = generateRandomParamName("data");
        String keyParam = generateRandomParamName("key");
        String ivParam = generateRandomParamName("iv");

        sb.append("    /**\n");
        sb.append("     * 加密数据\n");
        sb.append("     * @param ").append(dataParam).append(" 要加密的数据\n");
        sb.append("     * @param ").append(keyParam).append(" 加密密钥\n");
        sb.append("     * @return 加密后的数据（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(", String ").append(keyParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null || ").append(keyParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] ").append(dataParam).append("Bytes = ").append(dataParam).append(".getBytes();\n");
        sb.append("            byte[] ").append(keyParam).append("Bytes = ").append(keyParam).append(".getBytes();\n");
        sb.append("            SecretKeySpec ").append(keyParam).append("Spec = new SecretKeySpec(").append(keyParam).append("Bytes, \"").append(algorithmType).append("\");\n");
        sb.append("            Cipher cipher = Cipher.getInstance(\"").append(algorithmType).append("/ECB/PKCS5Padding\");\n");
        sb.append("            cipher.init(Cipher.ENCRYPT_MODE, ").append(keyParam).append("Spec);\n");
        sb.append("            byte[] encrypted = cipher.doFinal(").append(dataParam).append("Bytes);\n");
        sb.append("            return Base64.getEncoder().encodeToString(encrypted);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Encryption error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成带IV的加密方法
        String methodNameWithIv = generateRandomMethodName("encryptWithIv");
        sb.append("    /**\n");
        sb.append("     * 使用初始化向量加密数据\n");
        sb.append("     * @param ").append(dataParam).append(" 要加密的数据\n");
        sb.append("     * @param ").append(keyParam).append(" 加密密钥\n");
        sb.append("     * @param ").append(ivParam).append(" 初始化向量\n");
        sb.append("     * @return 加密后的数据（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodNameWithIv).append("(String ").append(dataParam).append(", String ").append(keyParam).append(", String ").append(ivParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null || ").append(keyParam).append(" == null || ").append(ivParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] ").append(dataParam).append("Bytes = ").append(dataParam).append(".getBytes();\n");
        sb.append("            byte[] ").append(keyParam).append("Bytes = ").append(keyParam).append(".getBytes();\n");
        sb.append("            byte[] ").append(ivParam).append("Bytes = ").append(ivParam).append(".getBytes();\n");
        sb.append("            SecretKeySpec ").append(keyParam).append("Spec = new SecretKeySpec(").append(keyParam).append("Bytes, \"").append(algorithmType).append("\");\n");
        sb.append("            IvParameterSpec ").append(ivParam).append("Spec = new IvParameterSpec(").append(ivParam).append("Bytes);\n");
        sb.append("            Cipher cipher = Cipher.getInstance(\"").append(algorithmType).append("/CBC/PKCS5Padding\");\n");
        sb.append("            cipher.init(Cipher.ENCRYPT_MODE, ").append(keyParam).append("Spec, ").append(ivParam).append("Spec);\n");
        sb.append("            byte[] encrypted = cipher.doFinal(").append(dataParam).append("Bytes);\n");
        sb.append("            return Base64.getEncoder().encodeToString(encrypted);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Encryption with IV error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成解密方法
     */
    private void generateDecryptMethod(StringBuilder sb, String className, String algorithmType) {
        String methodName = generateRandomMethodName("decrypt");
        String dataParam = generateRandomParamName("data");
        String keyParam = generateRandomParamName("key");

        sb.append("    /**\n");
        sb.append("     * 解密数据\n");
        sb.append("     * @param ").append(dataParam).append(" 要解密的数据（Base64编码）\n");
        sb.append("     * @param ").append(keyParam).append(" 解密密钥\n");
        sb.append("     * @return 解密后的原始数据\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(", String ").append(keyParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null || ").append(keyParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] ").append(dataParam).append("Bytes = Base64.getDecoder().decode(").append(dataParam).append(");\n");
        sb.append("            byte[] ").append(keyParam).append("Bytes = ").append(keyParam).append(".getBytes();\n");
        sb.append("            SecretKeySpec ").append(keyParam).append("Spec = new SecretKeySpec(").append(keyParam).append("Bytes, \"").append(algorithmType).append("\");\n");
        sb.append("            Cipher cipher = Cipher.getInstance(\"").append(algorithmType).append("/ECB/PKCS5Padding\");\n");
        sb.append("            cipher.init(Cipher.DECRYPT_MODE, ").append(keyParam).append("Spec);\n");
        sb.append("            byte[] decrypted = cipher.doFinal(").append(dataParam).append("Bytes);\n");
        sb.append("            return new String(decrypted);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Decryption error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成哈希方法
     */
    private void generateHashMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("hash");
        String dataParam = generateRandomParamName("data");
        String algorithm = RandomUtils.randomChoice(HASH_ALGORITHMS);

        sb.append("    /**\n");
        sb.append("     * 计算数据的哈希值\n");
        sb.append("     * @param ").append(dataParam).append(" 要哈希的数据\n");
        sb.append("     * @return 哈希值（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            MessageDigest digest = MessageDigest.getInstance(\"").append(algorithm).append("\");\n");
        sb.append("            byte[] hash = digest.digest(").append(dataParam).append(".getBytes());\n");
        sb.append("            return Base64.getEncoder().encodeToString(hash);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Hash error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成哈希链方法
     */
    private void generateHashChainMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("hashChain");
        String dataParam = generateRandomParamName("data");
        String iterationsParam = generateRandomParamName("iterations");
        String algorithm = RandomUtils.randomChoice(HASH_ALGORITHMS);

        sb.append("    /**\n");
        sb.append("     * 计算数据的哈希链（多次哈希）\n");
        sb.append("     * @param ").append(dataParam).append(" 要哈希的数据\n");
        sb.append("     * @param ").append(iterationsParam).append(" 哈希迭代次数\n");
        sb.append("     * @return 哈希链结果（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(", int ").append(iterationsParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null || ").append(iterationsParam).append(" <= 0) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            MessageDigest digest = MessageDigest.getInstance(\"").append(algorithm).append("\");\n");
        sb.append("            byte[] hash = ").append(dataParam).append(".getBytes();\n");
        sb.append("            for (int i = 0; i < ").append(iterationsParam).append("; i++) {\n");
        sb.append("                hash = digest.digest(hash);\n");
        sb.append("            }\n");
        sb.append("            return Base64.getEncoder().encodeToString(hash);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Hash chain error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成密钥生成方法
     */
    private void generateKeyGenerationMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("generateKey");
        String sizeParam = generateRandomParamName("size");
        String algorithm = RandomUtils.randomChoice(KEY_ALGORITHMS);

        sb.append("    /**\n");
        sb.append("     * 生成随机密钥\n");
        sb.append("     * @param ").append(sizeParam).append(" 密钥大小（位）\n");
        sb.append("     * @return 生成的密钥（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(int ").append(sizeParam).append(") {\n");
        sb.append("        try {\n");
        sb.append("            KeyGenerator keyGen = KeyGenerator.getInstance(\"").append(algorithm).append("\");\n");
        sb.append("            keyGen.init(").append(sizeParam).append(", new SecureRandom());\n");
        sb.append("            SecretKey secretKey = keyGen.generateKey();\n");
        sb.append("            return Base64.getEncoder().encodeToString(secretKey.getEncoded());\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Key generation error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成安全随机数方法
     */
    private void generateSecureRandomMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("generateRandom");
        String sizeParam = generateRandomParamName("size");

        sb.append("    /**\n");
        sb.append("     * 生成安全随机数\n");
        sb.append("     * @param ").append(sizeParam).append(" 随机数大小（字节）\n");
        sb.append("     * @return 随机数（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(int ").append(sizeParam).append(") {\n");
        sb.append("        if (").append(sizeParam).append(" <= 0) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            SecureRandom random = new SecureRandom();\n");
        sb.append("            byte[] bytes = new byte[").append(sizeParam).append("];\n");
        sb.append("            random.nextBytes(bytes);\n");
        sb.append("            return Base64.getEncoder().encodeToString(bytes);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Random generation error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成密钥验证方法
     */
    private void generateKeyValidationMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("validateKey");
        String keyParam = generateRandomParamName("key");
        String algorithm = RandomUtils.randomChoice(KEY_ALGORITHMS);

        sb.append("    /**\n");
        sb.append("     * 验证密钥的有效性\n");
        sb.append("     * @param ").append(keyParam).append(" 要验证的密钥\n");
        sb.append("     * @return 如果密钥有效返回true，否则返回false\n");
        sb.append("     */\n");
        sb.append("    public boolean ").append(methodName).append("(String ").append(keyParam).append(") {\n");
        sb.append("        if (").append(keyParam).append(" == null || ").append(keyParam).append(".isEmpty()) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] keyBytes = Base64.getDecoder().decode(").append(keyParam).append(");\n");
        sb.append("            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, \"").append(algorithm).append("\");\n");
        sb.append("            return keyBytes.length >= 16 && keyBytes.length <= 32;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Key validation error\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成密钥存储方法
     */
    private void generateKeyStorageMethods(StringBuilder sb, String className) {
        // 存储密钥方法
        String storeMethodName = generateRandomMethodName("storeKey");
        String aliasParam = generateRandomParamName("alias");
        String keyParam = generateRandomParamName("key");

        sb.append("    /**\n");
        sb.append("     * 存储密钥\n");
        sb.append("     * @param ").append(aliasParam).append(" 密钥别名\n");
        sb.append("     * @param ").append(keyParam).append(" 密钥数据\n");
        sb.append("     * @return 如果存储成功返回true，否则返回false\n");
        sb.append("     */\n");
        sb.append("    public boolean ").append(storeMethodName).append("(String ").append(aliasParam).append(", String ").append(keyParam).append(") {\n");
        sb.append("        if (").append(aliasParam).append(" == null || ").append(keyParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] keyBytes = ").append(keyParam).append(".getBytes();\n");
        sb.append("            keyStore.put(").append(aliasParam).append(", keyBytes);\n");
        sb.append("            return true;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Key storage error\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 获取密钥方法
        String getMethodName = generateRandomMethodName("getKey");

        sb.append("    /**\n");
        sb.append("     * 获取存储的密钥\n");
        sb.append("     * @param ").append(aliasParam).append(" 密钥别名\n");
        sb.append("     * @return 密钥数据，如果不存在则返回null\n");
        sb.append("     */\n");
        sb.append("    public String ").append(getMethodName).append("(String ").append(aliasParam).append(") {\n");
        sb.append("        if (").append(aliasParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] keyBytes = keyStore.get(").append(aliasParam).append(");\n");
        sb.append("            if (keyBytes == null) {\n");
        sb.append("                return null;\n");
        sb.append("            }\n");
        sb.append("            return new String(keyBytes);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Key retrieval error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 删除密钥方法
        String removeMethodName = generateRandomMethodName("removeKey");

        sb.append("    /**\n");
        sb.append("     * 删除存储的密钥\n");
        sb.append("     * @param ").append(aliasParam).append(" 密钥别名\n");
        sb.append("     * @return 如果删除成功返回true，否则返回false\n");
        sb.append("     */\n");
        sb.append("    public boolean ").append(removeMethodName).append("(String ").append(aliasParam).append(") {\n");
        sb.append("        if (").append(aliasParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            keyStore.remove(").append(aliasParam).append(");\n");
        sb.append("            return true;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Key removal error\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成加密链方法
     */
    private void generateEncryptionChainMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("encryptChain");
        String dataParam = generateRandomParamName("data");
        String keysParam = generateRandomParamName("keys");

        sb.append("    /**\n");
        sb.append("     * 使用多个密钥依次加密数据（加密链）\n");
        sb.append("     * @param ").append(dataParam).append(" 要加密的数据\n");
        sb.append("     * @param ").append(keysParam).append(" 密钥列表\n");
        sb.append("     * @return 加密后的数据（Base64编码）\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(", List<String> ").append(keysParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null || ").append(keysParam).append(" == null || ").append(keysParam).append(".isEmpty()) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            String result = ").append(dataParam).append(";\n");
        sb.append("            for (String key : ").append(keysParam).append(") {\n");
        sb.append("                result = encrypt(result, key);\n");
        sb.append("                if (result == null) {\n");
        sb.append("                    return null;\n");
        sb.append("                }\n");
        sb.append("            }\n");
        sb.append("            return result;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Encryption chain error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成数据转换方法
     */
    private void generateDataTransformMethod(StringBuilder sb, String className) {
        String methodName = generateRandomMethodName("transform");
        String dataParam = generateRandomParamName("data");

        sb.append("    /**\n");
        sb.append("     * 转换数据格式\n");
        sb.append("     * @param ").append(dataParam).append(" 要转换的数据\n");
        sb.append("     * @return 转换后的数据\n");
        sb.append("     */\n");
        sb.append("    public String ").append(methodName).append("(String ").append(dataParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] dataBytes = ").append(dataParam).append(".getBytes();\n");
        sb.append("            String encoded = Base64.getEncoder().encodeToString(dataBytes);\n");
        sb.append("            return transform + encoded;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Data transform error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成安全工具方法
     */
    private void generateSecurityUtilsMethods(StringBuilder sb, String className) {
        // 比较方法
        String compareMethodName = generateRandomMethodName("compare");
        String data1Param = generateRandomParamName("data1");
        String data2Param = generateRandomParamName("data2");

        sb.append("    /**\n");
        sb.append("     * 安全比较两个数据（防止时序攻击）\n");
        sb.append("     * @param ").append(data1Param).append(" 第一个数据\n");
        sb.append("     * @param ").append(data2Param).append(" 第二个数据\n");
        sb.append("     * @return 如果数据相同返回true，否则返回false\n");
        sb.append("     */\n");
        sb.append("    public boolean ").append(compareMethodName).append("(String ").append(data1Param).append(", String ").append(data2Param).append(") {\n");
        sb.append("        if (").append(data1Param).append(" == null || ").append(data2Param).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            byte[] bytes1 = ").append(data1Param).append(".getBytes();\n");
        sb.append("            byte[] bytes2 = ").append(data2Param).append(".getBytes();\n");
        sb.append("            if (bytes1.length != bytes2.length) {\n");
        sb.append("                return false;\n");
        sb.append("            }\n");
        sb.append("            int result = 0;\n");
        sb.append("            for (int i = 0; i < bytes1.length; i++) {\n");
        sb.append("                result |= bytes1[i] ^ bytes2[i];\n");
        sb.append("            }\n");
        sb.append("            return result == 0;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Comparison error\", e);\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 清理方法
        String clearMethodName = generateRandomMethodName("clear");
        String dataParam = generateRandomParamName("data");

        sb.append("    /**\n");
        sb.append("     * 安全清理敏感数据\n");
        sb.append("     * @param ").append(dataParam).append(" 要清理的数据\n");
        sb.append("     */\n");
        sb.append("    public void ").append(clearMethodName).append("(String ").append(dataParam).append(") {\n");
        sb.append("        if (").append(dataParam).append(" == null) {\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            char[] chars = ").append(dataParam).append(".toCharArray();\n");
        sb.append("            for (int i = 0; i < chars.length; i++) {\n");
        sb.append("                chars[i] = 0;\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Data clear error\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成密钥池方法
     */
    private void generateCipherPoolMethods(StringBuilder sb, String className) {
        // 获取Cipher方法
        String getCipherMethodName = generateRandomMethodName("getCipher");
        String keyParam = generateRandomParamName("key");
        String algorithm = RandomUtils.randomChoice(ENCRYPTION_ALGORITHMS);

        sb.append("    /**\n");
        sb.append("     * 从池中获取或创建Cipher实例\n");
        sb.append("     * @param ").append(keyParam).append(" 密钥\n");
        sb.append("     * @return Cipher实例\n");
        sb.append("     */\n");
        sb.append("    private Cipher ").append(getCipherMethodName).append("(String ").append(keyParam).append(") {\n");
        sb.append("        try {\n");
        sb.append("            String poolKey = ").append(keyParam).append(" + \"_pool\";\n");
        sb.append("            Cipher cipher = cipherPool.get(poolKey);\n");
        sb.append("            if (cipher == null) {\n");
        sb.append("                cipher = Cipher.getInstance(\"").append(algorithm).append("/ECB/PKCS5Padding\");\n");
        sb.append("                cipherPool.put(poolKey, cipher);\n");
        sb.append("            }\n");
        sb.append("            return cipher;\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Cipher pool error\", e);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 清理池方法
        String clearPoolMethodName = generateRandomMethodName("clearPool");

        sb.append("    /**\n");
        sb.append("     * 清理Cipher池\n");
        sb.append("     */\n");
        sb.append("    public void ").append(clearPoolMethodName).append("() {\n");
        sb.append("        try {\n");
        sb.append("            cipherPool.clear();\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(" + tagVar + ", \"Pool clear error\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成随机变量名
     */
    private String generateRandomVarName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机参数名
     */
    private String generateRandomParamName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机方法名
     */
    private String generateRandomMethodName(String prefix) {
        return RandomUtils.generateWord(3).toLowerCase() + RandomUtils.generateWord(4) + prefix;
    }

    /**
     * 生成随机密钥
     */
    private String generateRandomKey() {
        return RandomUtils.generateRandomString(16);
    }

    /**
     * 生成随机转换字符串
     */
    private String generateRandomTransform() {
        String[] transforms = {
                "base64:", "hex:", "utf8:", "ascii:"
        };
        return RandomUtils.randomChoice(transforms) + RandomUtils.generateRandomString(4);
    }
}
