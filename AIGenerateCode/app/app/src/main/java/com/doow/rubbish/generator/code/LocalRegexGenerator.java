
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修复后的本地正则表达式生成器
 * 修复了原LocalRegexGenerator中的变量名不一致、缺少导入语句等问题
 */
public class LocalRegexGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] OPERATION_TYPES = {
            "match", "find", "replace", "split", "replace_all",
            "matches", "find_all", "group", "named_group", "lookahead",
            "lookbehind", "backreference", "quantifier", "anchor", "boundary"
    };

    private static final String[] PATTERN_TYPES = {
            "email", "phone", "url", "date", "time",
            "ip_address", "mac_address", "credit_card", "id_card", "custom",
            "zip_code", "postal_code", "ssn", "tax_id", "passport",
            "license_plate", "vin", "isbn", "ean", "upc", "sku"
    };

    private static final String[] FLAG_TYPES = {
            "case_insensitive", "multiline", "dotall", "unicode",
            "canon_eq", "unix_lines", "literal", "comments", "unicode_case",
            "unix_dots", "posix", "unicode_character_class", "comments", "verbose"
    };

    public LocalRegexGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地正则表达式相关代码（修复版）");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Regex");
            String operationType = RandomUtils.randomChoice(OPERATION_TYPES);
            generateRegexClass(className, operationType);
        }
    }

    private void generateRegexClass(String className, String operationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("regex"));

        // 添加必要的导入语句
        sb.append(generateImportStatement("java.util.regex.Pattern"));
        sb.append(generateImportStatement("java.util.regex.Matcher"));
        sb.append(generateImportStatement("java.util.regex.MatchResult"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 生成类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 添加常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String OPERATION_TYPE = \"").append(operationType).append("\";\n\n");

        // 为每个方法生成一致的变量名
        generateMatchMethod(sb, operationType);
        generateFindMethod(sb, operationType);
        generateReplaceMethod(sb, operationType);
        generateSplitMethod(sb, operationType);
        generateReplaceAllMethod(sb, operationType);
        generateFindAllMethod(sb, operationType);
        generateGroupMethod(sb, operationType);
        generateNamedGroupMethod(sb, operationType);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "regex");
    }

    /**
     * 生成匹配方法
     */
    private void generateMatchMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";

        sb.append("    /**\n");
        sb.append("     * 检查输入字符串是否完全匹配给定的正则表达式\n");
        sb.append("     * @param ").append(inputVar).append(" 要匹配的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @return 如果完全匹配则返回true，否则返回false\n");
        sb.append("     */\n");
        sb.append("    public boolean ").append(operationType).append("Match(String ").append(inputVar).append(", String ").append(patternVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        return ").append(matcherVar).append(".matches();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成查找方法
     */
    private void generateFindMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";

        sb.append("    /**\n");
        sb.append("     * 在输入字符串中查找第一个匹配的子串\n");
        sb.append("     * @param ").append(inputVar).append(" 要搜索的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @return 找到的第一个匹配子串，如果没有找到则返回null\n");
        sb.append("     */\n");
        sb.append("    public String ").append(operationType).append("Find(String ").append(inputVar).append(", String ").append(patternVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        if (").append(matcherVar).append(".find()) {\n");
        sb.append("            return ").append(matcherVar).append(".group();\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成替换方法
     */
    private void generateReplaceMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String replacementVar = "replacement";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";

        sb.append("    /**\n");
        sb.append("     * 替换输入字符串中第一个匹配的子串\n");
        sb.append("     * @param ").append(inputVar).append(" 要处理的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @param ").append(replacementVar).append(" 替换字符串\n");
        sb.append("     * @return 替换后的字符串\n");
        sb.append("     */\n");
        sb.append("    public String ").append(operationType).append("Replace(String ").append(inputVar).append(", String ").append(patternVar).append(", String ").append(replacementVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return ").append(inputVar).append(";\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        if (").append(matcherVar).append(".find()) {\n");
        sb.append("            return ").append(matcherVar).append(".replaceFirst(").append(replacementVar).append(");\n");
        sb.append("        }\n");
        sb.append("        return ").append(inputVar).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成分割方法
     */
    private void generateSplitMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";

        sb.append("    /**\n");
        sb.append("     * 使用正则表达式分割输入字符串\n");
        sb.append("     * @param ").append(inputVar).append(" 要分割的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @return 分割后的字符串数组\n");
        sb.append("     */\n");
        sb.append("    public String[] ").append(operationType).append("Split(String ").append(inputVar).append(", String ").append(patternVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return new String[0];\n");
        sb.append("        }\n");
        sb.append("        return ").append(inputVar).append(".split(").append(patternVar).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成替换所有方法
     */
    private void generateReplaceAllMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String replacementVar = "replacement";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";

        sb.append("    /**\n");
        sb.append("     * 替换输入字符串中所有匹配的子串\n");
        sb.append("     * @param ").append(inputVar).append(" 要处理的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @param ").append(replacementVar).append(" 替换字符串\n");
        sb.append("     * @return 替换后的字符串\n");
        sb.append("     */\n");
        sb.append("    public String ").append(operationType).append("ReplaceAll(String ").append(inputVar).append(", String ").append(patternVar).append(", String ").append(replacementVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return ").append(inputVar).append(";\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        return ").append(matcherVar).append(".replaceAll(").append(replacementVar).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成查找所有方法
     */
    private void generateFindAllMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";
        String resultListVar = "matches";

        sb.append("    /**\n");
        sb.append("     * 查找输入字符串中所有匹配的子串\n");
        sb.append("     * @param ").append(inputVar).append(" 要搜索的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @return 包含所有匹配子串的列表\n");
        sb.append("     */\n");
        sb.append("    public List<String> ").append(operationType).append("FindAll(String ").append(inputVar).append(", String ").append(patternVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return new ArrayList<>();\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        List<String> ").append(resultListVar).append(" = new ArrayList<>();\n");
        sb.append("        while (").append(matcherVar).append(".find()) {\n");
        sb.append("            ").append(resultListVar).append(".add(").append(matcherVar).append(".group());\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultListVar).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成分组方法
     */
    private void generateGroupMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String groupIndexVar = "groupIndex";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";

        sb.append("    /**\n");
        sb.append("     * 获取指定分组的匹配内容\n");
        sb.append("     * @param ").append(inputVar).append(" 要处理的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @param ").append(groupIndexVar).append(" 分组索引\n");
        sb.append("     * @return 指定分组的匹配内容，如果不存在则返回null\n");
        sb.append("     */\n");
        sb.append("    public String ").append(operationType).append("Group(String ").append(inputVar).append(", String ").append(patternVar).append(", int ").append(groupIndexVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        if (").append(matcherVar).append(".find() && ").append(groupIndexVar).append(" >= 0 && ").append(groupIndexVar).append(" <= ").append(matcherVar).append(".groupCount()) {\n");
        sb.append("            return ").append(matcherVar).append(".group(").append(groupIndexVar).append(");\n");
        sb.append("        }\n");
        sb.append("        return null;\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成命名分组方法
     */
    private void generateNamedGroupMethod(StringBuilder sb, String operationType) {
        String inputVar = "input";
        String patternVar = "pattern";
        String patternObjVar = "compiledPattern";
        String matcherVar = "matcher";
        String resultMapVar = "resultMap";

        sb.append("    /**\n");
        sb.append("     * 获取所有命名分组的匹配内容\n");
        sb.append("     * @param ").append(inputVar).append(" 要处理的输入字符串\n");
        sb.append("     * @param ").append(patternVar).append(" 正则表达式模式\n");
        sb.append("     * @return 包含所有分组匹配内容的映射\n");
        sb.append("     */\n");
        sb.append("    public Map<String, String> ").append(operationType).append("NamedGroup(String ").append(inputVar).append(", String ").append(patternVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null || ").append(patternVar).append(" == null) {\n");
        sb.append("            return new HashMap<>();\n");
        sb.append("        }\n");
        sb.append("        Pattern ").append(patternObjVar).append(" = Pattern.compile(").append(patternVar).append(");\n");
        sb.append("        Matcher ").append(matcherVar).append(" = ").append(patternObjVar).append(".matcher(").append(inputVar).append(");\n");
        sb.append("        Map<String, String> ").append(resultMapVar).append(" = new HashMap<>();\n");
        sb.append("        if (").append(matcherVar).append(".matches()) {\n");
        sb.append("            for (int i = 0; i <= ").append(matcherVar).append(".groupCount(); i++) {\n");
        sb.append("                ").append(resultMapVar).append(".put(\"group\" + i, ").append(matcherVar).append(".group(i));\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        return ").append(resultMapVar).append(";\n");
        sb.append("    }\n\n");
    }
}
