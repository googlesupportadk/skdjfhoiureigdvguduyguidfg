
package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.util.*;

/**
 * 升级版XML代码生成器 - 支持随机功能组合和多样性生成
 */
public class LocalXmlGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    // 功能类型集合
    private static final String[] FEATURE_TYPES = {
            "parser", "builder", "validator", "transformer", "serializer",
            "query", "merger", "splitter", "filter", "sorter"
    };

    // XML操作类型
    private static final String[] OPERATION_TYPES = {
            "parse", "build", "validate", "transform", "query",
            "xpath", "xslt", "namespace", "schema", "dtd",
            "serialize", "deserialize", "merge", "split", "filter",
            "sort", "group", "aggregate", "search", "replace"
    };

    // 元素类型
    private static final String[] ELEMENT_TYPES = {
            "element", "attribute", "text", "comment", "cdata",
            "processing_instruction", "entity", "notation", "doctype", "root",
            "namespace", "prefix", "local_name", "qualified_name", "uri"
    };

    // 数据类型
    private static final String[] DATA_TYPES = {
            "String", "int", "boolean", "long", "double", "float", "List<String>", "Map<String, String>"
    };

    // 返回类型
    private static final String[] RETURN_TYPES = {
            "Document", "Element", "NodeList", "String", "boolean", "int", "long", "List<String>"
    };

    // Android XML相关类
    private static final String[] ANDROID_XML_CLASSES = {
            "org.xmlpull.v1.XmlPullParser",
            "org.xmlpull.v1.XmlPullParserFactory",
            "org.xmlpull.v1.XmlSerializer",
            "android.util.Xml"
    };

    // DOM相关类
    private static final String[] DOM_CLASSES = {
            "org.w3c.dom.Document",
            "org.w3c.dom.Element",
            "org.w3c.dom.Node",
            "org.w3c.dom.NodeList",
            "org.w3c.dom.NamedNodeMap",
            "javax.xml.parsers.DocumentBuilder",
            "javax.xml.parsers.DocumentBuilderFactory",
            "javax.xml.transform.Transformer",
            "javax.xml.transform.TransformerFactory",
            "javax.xml.transform.dom.DOMSource",
            "javax.xml.transform.stream.StreamResult"
    };

    // SAX相关类
    private static final String[] SAX_CLASSES = {
            "org.xml.sax.Attributes",
            "org.xml.sax.InputSource",
            "org.xml.sax.SAXException",
            "org.xml.sax.helpers.DefaultHandler",
            "javax.xml.parsers.SAXParser",
            "javax.xml.parsers.SAXParserFactory"
    };

    public LocalXmlGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成本地XML相关代码 - 升级版");

        // 随机生成5-15个XML处理类
        int classCount = RandomUtils.between(5, 15);
        for (int i = 0; i < classCount; i++) {
            String className = RandomUtils.generateClassName("Xml");
            String featureType = RandomUtils.randomChoice(FEATURE_TYPES);
            generateXmlClass(className, featureType);
        }
    }

    /**
     * 生成XML处理类
     */
    private void generateXmlClass(String className, String featureType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 包声明
        sb.append(generatePackageDeclaration("xml"));

        // 导入语句 - 根据功能类型随机选择需要的类
        Set<String> imports = generateRequiredImports(featureType);
        for (String importClass : imports) {
            sb.append(generateImportStatement(importClass));
        }
        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 类声明
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成类成员变量
        List<String> fieldNames = generateFields(sb, featureType);

        // 生成构造方法
        generateConstructor(sb, className, fieldNames);

        // 生成核心方法
        List<String> methodNames = new ArrayList<>();

        // 根据功能类型生成不同的方法组合
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateParseMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateBuildMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateTransformMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateValidateMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateQueryMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSerializeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateMergeMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateFilterMethod(sb, fieldNames));
        }

        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateSortMethod(sb, fieldNames));
        }

        // 生成辅助方法
        if (RandomUtils.randomBoolean()) {
            methodNames.add(generateHelperMethod(sb, fieldNames));
        }

        // 生成调用方法 - 确保所有方法都被调用
        if (methodNames.size() > 0) {
            generateCallerMethod(sb, className, fieldNames, methodNames);
        }

        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "xml");
    }

    /**
     * 生成所需的导入语句
     */
    private Set<String> generateRequiredImports(String featureType) {
        Set<String> imports = new HashSet<>();

        // 基础导入
        imports.add("java.io.StringReader");
        imports.add("java.io.StringWriter");
        imports.add("java.util.List");
        imports.add("java.util.ArrayList");
        imports.add("java.util.Map");
        imports.add("java.util.HashMap");
        imports.add("java.util.Random");

        // 根据功能类型添加特定导入
        if (featureType.equals("parser") || featureType.equals("validator")) {
            imports.addAll(Arrays.asList(DOM_CLASSES));
            imports.addAll(Arrays.asList(SAX_CLASSES));
        } else if (featureType.equals("builder") || featureType.equals("serializer")) {
            imports.addAll(Arrays.asList(DOM_CLASSES));
            imports.addAll(Arrays.asList(ANDROID_XML_CLASSES));
        } else if (featureType.equals("transformer")) {
            imports.addAll(Arrays.asList(DOM_CLASSES));
        } else if (featureType.equals("query")) {
            imports.addAll(Arrays.asList(DOM_CLASSES));
            imports.add("javax.xml.xpath.XPath");
            imports.add("javax.xml.xpath.XPathConstants");
            imports.add("javax.xml.xpath.XPathFactory");
        } else {
            // 其他功能类型随机选择
            if (RandomUtils.randomBoolean()) {
                imports.addAll(Arrays.asList(DOM_CLASSES));
            }
            if (RandomUtils.randomBoolean()) {
                imports.addAll(Arrays.asList(ANDROID_XML_CLASSES));
            }
        }

        return imports;
    }

    /**
     * 生成类成员变量
     */
    private List<String> generateFields(StringBuilder sb, String featureType) {
        List<String> fieldNames = new ArrayList<>();

        // 生成随机数量的字段
        int fieldCount = RandomUtils.between(3, 8);

        // 常量字段
        String tagName = RandomUtils.generateWord(6);
        sb.append("    private static final String TAG = \"").append(tagName).append("\";\n");
        fieldNames.add("TAG");

        String featureTypeField = RandomUtils.generateWord(6);
        sb.append("    private static final String FEATURE_TYPE = \"").append(featureType).append("\";\n");
        fieldNames.add("FEATURE_TYPE");

        // 随机生成其他字段
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = RandomUtils.randomChoice(DATA_TYPES);
            String fieldName = RandomUtils.generateVariableName(fieldType);

            sb.append("    private ").append(fieldType).append(" ").append(fieldName);

            // 随机初始化
            if (RandomUtils.randomBoolean()) {
                if (fieldType.equals("String")) {
                    sb.append(" = \"").append(RandomUtils.generateRandomString(8)).append("\"");
                } else if (fieldType.equals("int")) {
                    sb.append(" = ").append(RandomUtils.between(0, 100));
                } else if (fieldType.equals("boolean")) {
                    sb.append(" = ").append(RandomUtils.randomBoolean());
                } else if (fieldType.equals("long")) {
                    sb.append(" = ").append(RandomUtils.betweenLong(0, 1000));
                } else if (fieldType.equals("double")) {
                    sb.append(" = ").append(RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("float")) {
                    sb.append(" = ").append((float) RandomUtils.nextDouble(0.0, 100.0));
                } else if (fieldType.equals("List<String>")) {
                    sb.append(" = new ArrayList<>()");
                } else if (fieldType.equals("Map<String, String>")) {
                    sb.append(" = new HashMap<>()");
                }
            }

            sb.append(";\n");
            fieldNames.add(fieldName);
        }

        sb.append("\n");
        return fieldNames;
    }

    /**
     * 生成构造方法
     */
    private void generateConstructor(StringBuilder sb, String className, List<String> fieldNames) {
        sb.append("    public ").append(className).append("() {\n");

        // 随机选择一些字段进行初始化
        int initCount = RandomUtils.between(1, fieldNames.size());
        List<String> shuffledFields = new ArrayList<>(fieldNames);
        Collections.shuffle(shuffledFields);

        for (int i = 0; i < initCount; i++) {
            String fieldName = shuffledFields.get(i);

            // 跳过常量字段
            if (fieldName.equals("TAG") || fieldName.equals("FEATURE_TYPE")) {
                continue;
            }

            // 随机生成初始化代码
            int initType = RandomUtils.between(1, 4);
            switch (initType) {
                case 1:
                    sb.append("        this.").append(fieldName).append(" = ");
                    sb.append(RandomUtils.generateRandomString(6));
                    sb.append(";\n");
                    break;
                case 2:
                    sb.append("        this.").append(fieldName).append(" = ");
                    sb.append(RandomUtils.between(0, 100));
                    sb.append(";\n");
                    break;
                case 3:
                    sb.append("        this.").append(fieldName).append(" = ");
                    sb.append(RandomUtils.randomBoolean());
                    sb.append(";\n");
                    break;
                case 4:
                    sb.append("        this.").append(fieldName).append(" = ");
                    sb.append(RandomUtils.nextDouble(0.0, 100.0));
                    sb.append(";\n");
                    break;
            }
        }

        // 随机添加日志
        if (RandomUtils.randomBoolean() && RandomUtils.randomBoolean()) {
            sb.append("        System.out.println(TAG + \" initialized\");\n");
        }

        sb.append("    }\n\n");
    }

    /**
     * 生成解析方法
     */
    private String generateParseMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("parse");
        String inputParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("Document");
        String factoryVar = RandomUtils.generateVariableName("DocumentBuilderFactory");
        String builderVar = RandomUtils.generateVariableName("DocumentBuilder");

        sb.append("    public Document ").append(methodName).append("(String ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = DocumentBuilderFactory.newInstance();\n");
        sb.append("            ").append(builderVar).append(" = ").append(factoryVar).append(".newDocumentBuilder();\n");
        sb.append("            Document ").append(resultVar).append(" = ").append(builderVar).append(".parse(new InputSource(new StringReader(").append(inputParam).append(")));\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(".getDocumentElement().getNodeName();\n");
            }
        }

        sb.append("            return ").append(resultVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" parse error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成构建方法
     */
    private String generateBuildMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("build");
        String rootElementParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("Document");
        String factoryVar = RandomUtils.generateVariableName("DocumentBuilderFactory");
        String builderVar = RandomUtils.generateVariableName("DocumentBuilder");
        String elementVar = RandomUtils.generateVariableName("Element");

        sb.append("    public Document ").append(methodName).append("(String ").append(rootElementParam).append(") {\n");
        sb.append("        if (").append(rootElementParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = DocumentBuilderFactory.newInstance();\n");
        sb.append("            ").append(builderVar).append(" = ").append(factoryVar).append(".newDocumentBuilder();\n");
        sb.append("            Document ").append(resultVar).append(" = ").append(builderVar).append(".newDocument();\n");
        sb.append("            Element ").append(elementVar).append(" = ").append(resultVar).append(".createElement(").append(rootElementParam).append(");\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(elementVar).append(".setAttribute(\"").append(usedField).append("\", String.valueOf(").append(usedField).append("));\n");
            }
        }

        sb.append("            ").append(resultVar).append(".appendChild(").append(elementVar).append(");\n");
        sb.append("            return ").append(resultVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" build error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成转换方法
     */
    private String generateTransformMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("transform");
        String inputParam = RandomUtils.generateVariableName("Document");
        String resultVar = RandomUtils.generateVariableName("String");
        String factoryVar = RandomUtils.generateVariableName("TransformerFactory");
        String transformerVar = RandomUtils.generateVariableName("Transformer");
        String writerVar = RandomUtils.generateVariableName("StringWriter");

        sb.append("    public String ").append(methodName).append("(Document ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = TransformerFactory.newInstance();\n");
        sb.append("            ").append(transformerVar).append(" = ").append(factoryVar).append(".newTransformer();\n");
        sb.append("            StringWriter ").append(writerVar).append(" = new StringWriter();\n");
        sb.append("            ").append(transformerVar).append(".transform(new DOMSource(").append(inputParam).append("), new StreamResult(").append(writerVar).append("));\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(writerVar).append(".toString();\n");
            }
        }

        sb.append("            return ").append(writerVar).append(".toString();\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" transform error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成验证方法
     */
    private String generateValidateMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("validate");
        String inputParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("boolean");
        String factoryVar = RandomUtils.generateVariableName("DocumentBuilderFactory");
        String builderVar = RandomUtils.generateVariableName("DocumentBuilder");

        sb.append("    public boolean ").append(methodName).append("(String ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = DocumentBuilderFactory.newInstance();\n");
        sb.append("            ").append(builderVar).append(" = ").append(factoryVar).append(".newDocumentBuilder();\n");
        sb.append("            ").append(builderVar).append(".parse(new InputSource(new StringReader(").append(inputParam).append(")));\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = true;\n");
            }
        }

        sb.append("            return true;\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" validate error: \" + e.getMessage());\n");
        }

        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成查询方法
     */
    private String generateQueryMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("query");
        String docParam = RandomUtils.generateVariableName("Document");
        String xpathParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("NodeList");
        String xpathVar = RandomUtils.generateVariableName("XPath");
        String factoryVar = RandomUtils.generateVariableName("XPathFactory");

        sb.append("    public NodeList ").append(methodName).append("(Document ").append(docParam).append(", String ").append(xpathParam).append(") {\n");
        sb.append("        if (").append(docParam).append(" == null || ").append(xpathParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = XPathFactory.newInstance();\n");
        sb.append("            ").append(xpathVar).append(" = ").append(factoryVar).append(".newXPath();\n");
        sb.append("            NodeList ").append(resultVar).append(" = (NodeList) ").append(xpathVar).append(".evaluate(").append(xpathParam).append(", ").append(docParam).append(", XPathConstants.NODESET);\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(".getLength() > 0 ? ").append(resultVar).append(".item(0).getNodeName() : null;\n");
            }
        }

        sb.append("            return ").append(resultVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" query error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成序列化方法
     */
    private String generateSerializeMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("serialize");
        String inputParam = RandomUtils.generateVariableName("Document");
        String resultVar = RandomUtils.generateVariableName("String");
        String factoryVar = RandomUtils.generateVariableName("TransformerFactory");
        String transformerVar = RandomUtils.generateVariableName("Transformer");
        String writerVar = RandomUtils.generateVariableName("StringWriter");

        sb.append("    public String ").append(methodName).append("(Document ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = TransformerFactory.newInstance();\n");
        sb.append("            ").append(transformerVar).append(" = ").append(factoryVar).append(".newTransformer();\n");
        sb.append("            StringWriter ").append(writerVar).append(" = new StringWriter();\n");
        sb.append("            ").append(transformerVar).append(".transform(new DOMSource(").append(inputParam).append("), new StreamResult(").append(writerVar).append("));\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(writerVar).append(".toString();\n");
            }
        }

        sb.append("            return ").append(writerVar).append(".toString();\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" serialize error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成合并方法
     */
    private String generateMergeMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("merge");
        String doc1Param = RandomUtils.generateVariableName("Document");
        String doc2Param = RandomUtils.generateVariableName("Document");
        String resultVar = RandomUtils.generateVariableName("Document");
        String factoryVar = RandomUtils.generateVariableName("DocumentBuilderFactory");
        String builderVar = RandomUtils.generateVariableName("DocumentBuilder");
        String rootVar = RandomUtils.generateVariableName("Element");

        sb.append("    public Document ").append(methodName).append("(Document ").append(doc1Param).append(", Document ").append(doc2Param).append(") {\n");
        sb.append("        if (").append(doc1Param).append(" == null || ").append(doc2Param).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            ").append(factoryVar).append(" = DocumentBuilderFactory.newInstance();\n");
        sb.append("            ").append(builderVar).append(" = ").append(factoryVar).append(".newDocumentBuilder();\n");
        sb.append("            Document ").append(resultVar).append(" = ").append(builderVar).append(".newDocument();\n");
        sb.append("            Element ").append(rootVar).append(" = ").append(resultVar).append(".createElement(\"merged\");\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(rootVar).append(".setAttribute(\"").append(usedField).append("\", \"merged\");\n");
            }
        }

        sb.append("            ").append(resultVar).append(".appendChild(").append(rootVar).append(");\n");
        sb.append("            ").append(rootVar).append(".appendChild(").append(resultVar).append(".importNode(").append(doc1Param).append(".getDocumentElement(), true));\n");
        sb.append("            ").append(rootVar).append(".appendChild(").append(resultVar).append(".importNode(").append(doc2Param).append(".getDocumentElement(), true));\n");
        sb.append("            return ").append(resultVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" merge error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成过滤方法
     */
    private String generateFilterMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("filter");
        String docParam = RandomUtils.generateVariableName("Document");
        String elementNameParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("NodeList");
        String elementsVar = RandomUtils.generateVariableName("NodeList");
        String filteredVar = RandomUtils.generateVariableName("List");

        sb.append("    public List<String> ").append(methodName).append("(Document ").append(docParam).append(", String ").append(elementNameParam).append(") {\n");
        sb.append("        if (").append(docParam).append(" == null || ").append(elementNameParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            NodeList ").append(elementsVar).append(" = ").append(docParam).append(".getElementsByTagName(").append(elementNameParam).append(");\n");
        sb.append("            List<String> ").append(filteredVar).append(" = new ArrayList<>();\n");
        sb.append("            for (int i = 0; i < ").append(elementsVar).append(".getLength(); i++) {\n");
        sb.append("                ").append(filteredVar).append(".add(").append(elementsVar).append(".item(i).getTextContent());\n");
        sb.append("            }\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(filteredVar).append(".size() > 0 ? ").append(filteredVar).append(".get(0) : null;\n");
            }
        }

        sb.append("            return ").append(filteredVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" filter error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成排序方法
     */
    private String generateSortMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("sort");
        String listParam = RandomUtils.generateVariableName("List");
        String resultVar = RandomUtils.generateVariableName("List");

        sb.append("    public List<String> ").append(methodName).append("(List<String> ").append(listParam).append(") {\n");
        sb.append("        if (").append(listParam).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        try {\n");
        sb.append("            List<String> ").append(resultVar).append(" = new ArrayList<>(").append(listParam).append(");\n");
        sb.append("            Collections.sort(").append(resultVar).append(");\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("            ").append(usedField).append(" = ").append(resultVar).append(".size() > 0 ? ").append(resultVar).append(".get(0) : null;\n");
            }
        }

        sb.append("            return ").append(resultVar).append(";\n");
        sb.append("        } catch (Exception e) {\n");

        // 随机添加错误日志
        if (RandomUtils.randomBoolean()) {
            sb.append("            System.out.println(TAG + \" sort error: \" + e.getMessage());\n");
        }

        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成辅助方法
     */
    private String generateHelperMethod(StringBuilder sb, List<String> fieldNames) {
        String methodName = RandomUtils.generateMethodName("helper");
        String inputParam = RandomUtils.generateVariableName("String");
        String resultVar = RandomUtils.generateVariableName("boolean");

        sb.append("    private boolean ").append(methodName).append("(String ").append(inputParam).append(") {\n");
        sb.append("        if (").append(inputParam).append(" == null) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("        boolean ").append(resultVar).append(" = ").append(inputParam).append(".length() > 0;\n");

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(inputParam).append(";\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");

        return methodName;
    }

    /**
     * 生成调用方法 - 确保所有方法都被调用
     */
    private void generateCallerMethod(StringBuilder sb, String className, List<String> fieldNames, List<String> methodNames) {
        String methodName = RandomUtils.generateMethodName("process");
        String inputVar = RandomUtils.generateVariableName("String");
        String docVar = RandomUtils.generateVariableName("Document");
        String resultVar = RandomUtils.generateVariableName("String");

        sb.append("    public String ").append(methodName).append("(String ").append(inputVar).append(") {\n");
        sb.append("        if (").append(inputVar).append(" == null) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");

        // 调用所有方法
        for (String name : methodNames) {
            if (name.contains("parse")) {
                sb.append("        Document ").append(docVar).append(" = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("build")) {
                sb.append("        Document ").append(docVar).append(" = ").append(name).append("(\"root\");\n");
            } else if (name.contains("transform") || name.contains("serialize")) {
                sb.append("        String ").append(resultVar).append(" = ").append(name).append("(").append(docVar).append(");\n");
            } else if (name.contains("validate")) {
                sb.append("        boolean isValid = ").append(name).append("(").append(inputVar).append(");\n");
            } else if (name.contains("query")) {
                sb.append("        NodeList nodes = ").append(name).append("(").append(docVar).append(", \"//*\");\n");
            } else if (name.contains("merge")) {
                sb.append("        Document mergedDoc = ").append(name).append("(").append(docVar).append(", ").append(docVar).append(");\n");
            } else if (name.contains("filter")) {
                sb.append("        List<String> filteredList = ").append(name).append("(").append(docVar).append(", \"item\");\n");
            } else if (name.contains("sort")) {
                sb.append("        List<String> sortedList = ").append(name).append("(filteredList);\n");
            } else if (name.contains("helper")) {
                sb.append("        boolean helperResult = ").append(name).append("(").append(inputVar).append(");\n");
            }
        }

        // 使用一些字段
        if (fieldNames.size() > 2) {
            String usedField = fieldNames.get(RandomUtils.between(0, fieldNames.size() - 1));
            if (!usedField.equals("TAG") && !usedField.equals("FEATURE_TYPE")) {
                sb.append("        ").append(usedField).append(" = ").append(resultVar).append(";\n");
            }
        }

        sb.append("        return ").append(resultVar).append(";\n");
        sb.append("    }\n\n");
    }
}
