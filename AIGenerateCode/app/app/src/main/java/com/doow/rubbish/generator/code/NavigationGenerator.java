package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class NavigationGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public NavigationGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Navigation组件");

        // 空值防护：给导航类型设置默认值
        String navigationType = variationManager.getVariation("navigation");
        navigationType = navigationType == null ? "jetpack" : navigationType;

        // 边界防护：确保生成数量在合理范围（3-8）
        int generateCount = RandomUtils.between(3, 8);
        for (int i = 0; i < generateCount; i++) {
            String className = RandomUtils.generateClassName("Navigation");
            generateNavigationClass(className, navigationType);
        }
    }

    private void generateNavigationClass(String className, String navigationType) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("navigation"));

        // 导入核心依赖
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.net.Uri"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("androidx.navigation.NavController"));
        sb.append(generateImportStatement("androidx.navigation.NavDirections"));
        sb.append(generateImportStatement("androidx.navigation.NavGraph"));
        sb.append(generateImportStatement("androidx.navigation.fragment.NavHostFragment"));
        sb.append(generateImportStatement("android.os.Bundle"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.Stack"));
        // 补充线程安全集合导入
        sb.append(generateImportStatement("java.util.Collections"));
        sb.append(generateImportStatement(packageName + ".R"));
        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append("\n"); // 规范空行，替换多余的空白拼接

        // 类定义（修复缩进）
        sb.append("public class ").append(className).append(" {\n\n");

        // 常量定义（修复字符串转义）
        sb.append("    private static final String TAG = \"").append(className).append("\";\n\n");

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useArgs = RandomUtils.randomBoolean();
        boolean useStack = RandomUtils.randomBoolean();
        boolean useHistory = RandomUtils.randomBoolean();
        boolean useDeepLink = RandomUtils.randomBoolean();
        boolean useSafeNavigate = RandomUtils.randomBoolean();

        // 根据标志变量生成字段（线程安全处理）
        if (useArgs) {
            sb.append("    private static Map<String, Object> navigationArgs;\n");
            sb.append("    private static volatile boolean argsInitialized = false;\n\n");
        }

        if (useStack) {
            sb.append("    private static Stack<String> navigationStack = Collections.synchronizedStack(new Stack<>());\n\n");
        }

        if (useHistory) {
            sb.append("    private static List<String> navigationHistory = Collections.synchronizedList(new ArrayList<>());\n\n");
        }

        // 导航常量（修复字符串转义，改为资源ID规范）
        String homeAction = RandomUtils.generateWord(4);
        String detailAction = RandomUtils.generateWord(4);
        String settingsAction = RandomUtils.generateWord(4);
        sb.append("    public static final int ACTION_HOME = R.id.action_global_home;\n");
        sb.append("    public static final int ACTION_DETAIL = R.id.action_global_detail;\n");
        sb.append("    public static final int ACTION_SETTINGS = R.id.action_global_settings;\n\n");

        // DeepLink常量（补充示例）
        if (useDeepLink) {
            sb.append("    public static final String DEEP_LINK_HOME = \"app://").append(packageName).append("/home\";\n");
            sb.append("    public static final String DEEP_LINK_DETAIL = \"app://").append(packageName).append("/detail\";\n\n");
        }

        // 私有构造方法（单例规范）
        sb.append("    private ").append(className).append("() {\n");
        sb.append("        throw new AssertionError(\"No instance for you!\");\n");
        sb.append("    }\n\n");

        // 初始化方法（双重检查锁，线程安全）
        if (useArgs) {
            sb.append("    private static void initializeArgs() {\n");
            sb.append("        if (!argsInitialized) {\n");
            sb.append("            synchronized (").append(className).append(".class) {\n");
            sb.append("                if (!argsInitialized) {\n");
            sb.append("                    navigationArgs = Collections.synchronizedMap(new HashMap<>());\n");
            sb.append("                    argsInitialized = true;\n");
            sb.append("                }\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 基本导航方法（修复NavController API使用）
        sb.append("    public static void navigateToHome(NavController controller) {\n");
        // 空值防护
        sb.append("        if (controller == null) {\n");
        sb.append("            Log.w(TAG, \"NavController is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useStack) {
            sb.append("        navigationStack.push(\"HOME\");\n");
        }
        if (useHistory) {
            sb.append("        navigationHistory.add(\"HOME\");\n");
        }
        // 修复：使用资源ID导航，而非字符串
        sb.append("        controller.navigate(ACTION_HOME);\n");
        sb.append("    }\n\n");

        sb.append("    public static void navigateToDetail(NavController controller, String id) {\n");
        sb.append("        if (controller == null || id == null) {\n");
        sb.append("            Log.w(TAG, \"NavController or id is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        // 构建带参数的Bundle
        sb.append("        Bundle bundle = new Bundle();\n");
        sb.append("        bundle.putString(\"detail_id\", id);\n");
        sb.append("        String route = \"DETAIL_\" + id;\n");
        if (useStack) {
            sb.append("        navigationStack.push(route);\n");
        }
        if (useHistory) {
            sb.append("        navigationHistory.add(route);\n");
        }
        // 修复：带参数导航
        sb.append("        controller.navigate(ACTION_DETAIL, bundle);\n");
        sb.append("    }\n\n");

        sb.append("    public static void navigateToSettings(NavController controller) {\n");
        sb.append("        if (controller == null) {\n");
        sb.append("            Log.w(TAG, \"NavController is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useStack) {
            sb.append("        navigationStack.push(\"SETTINGS\");\n");
        }
        if (useHistory) {
            sb.append("        navigationHistory.add(\"SETTINGS\");\n");
        }
        sb.append("        controller.navigate(ACTION_SETTINGS);\n");
        sb.append("    }\n\n");

        sb.append("    public static void navigateUp(NavController controller) {\n");
        sb.append("        if (controller == null) {\n");
        sb.append("            Log.w(TAG, \"NavController is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useStack) {
            sb.append("        if (!navigationStack.isEmpty()) {\n");
            sb.append("            navigationStack.pop();\n");
            sb.append("        }\n");
        }
        sb.append("        controller.navigateUp();\n");
        sb.append("    }\n\n");

        sb.append("    public static void navigateToRoot(NavController controller) {\n");
        sb.append("        if (controller == null) {\n");
        sb.append("            Log.w(TAG, \"NavController is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        if (useStack) {
            sb.append("        navigationStack.clear();\n");
        }
        if (useHistory) {
            sb.append("        navigationHistory.clear();\n");
        }
        // 修复：资源ID引用规范
        sb.append("        controller.popBackStack(R.id.nav_graph, true);\n");
        sb.append("    }\n\n");

        // 根据标志变量生成配套方法
        if (useArgs) {
            generateArgsMethods(sb, className);
        }

        if (useStack) {
            generateStackMethods(sb);
        }

        if (useHistory) {
            generateHistoryMethods(sb);
        }

        if (useDeepLink) {
            generateDeepLinkMethods(sb);
        }

        if (useSafeNavigate) {
            generateSafeNavigateMethods(sb);
        }

        // 闭合类
        sb.append("}\n");

        // 生成Java文件
        generateJavaFile(className, sb.toString(), "navigation");
    }

    private void generateArgsMethods(StringBuilder sb, String className) {
        sb.append("    public static void navigateWithArgs(NavController controller, String key, Object value) {\n");
        sb.append("        if (controller == null || key == null) {\n");
        sb.append("            Log.w(TAG, \"NavController or key is null, skip navigation\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        initializeArgs();\n");
        sb.append("        navigationArgs.put(key, value);\n");
        sb.append("        Bundle bundle = new Bundle();\n");
        // 空值防护
        sb.append("        bundle.putString(key, value != null ? value.toString() : \"\");\n");
        sb.append("        controller.navigate(ACTION_HOME, bundle);\n");
        sb.append("    }\n\n");

        sb.append("    public static Object getArg(String key) {\n");
        sb.append("        if (key == null) {\n");
        sb.append("            Log.w(TAG, \"Key is null\");\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        initializeArgs();\n");
        sb.append("        return navigationArgs.get(key);\n");
        sb.append("    }\n\n");

        sb.append("    public static void clearArgs() {\n");
        sb.append("        initializeArgs();\n");
        sb.append("        navigationArgs.clear();\n");
        sb.append("    }\n\n");
    }

    private void generateStackMethods(StringBuilder sb) {
        sb.append("    public static boolean canGoBack() {\n");
        // 空值防护
        sb.append("        return navigationStack != null && navigationStack.size() > 1;\n");
        sb.append("    }\n\n");

        sb.append("    public static String getPreviousDestination() {\n");
        sb.append("        if (navigationStack == null || navigationStack.size() <= 1) {\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("        return navigationStack.get(navigationStack.size() - 2);\n");
        sb.append("    }\n\n");

        sb.append("    public static void clearStack() {\n");
        sb.append("        if (navigationStack != null) {\n");
        sb.append("            navigationStack.clear();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateHistoryMethods(StringBuilder sb) {
        sb.append("    public static void addToHistory(String destination) {\n");
        sb.append("        if (destination == null) {\n");
        sb.append("            Log.w(TAG, \"Destination is null\");\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        navigationHistory.add(destination);\n");
        sb.append("    }\n\n");

        sb.append("    public static List<String> getHistory() {\n");
        sb.append("        return new ArrayList<>(navigationHistory);\n");
        sb.append("    }\n\n");

        sb.append("    public static void clearHistory() {\n");
        sb.append("        if (navigationHistory != null) {\n");
        sb.append("            navigationHistory.clear();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateDeepLinkMethods(StringBuilder sb) {
        sb.append("    public static void navigateDeepLink(NavController controller, String deepLink) {\n");
        sb.append("        try {\n");
        sb.append("            if (controller == null || deepLink == null) {\n");
        sb.append("                Log.w(TAG, \"NavController or deepLink is null, skip navigation\");\n");
        sb.append("                return;\n");
        sb.append("            }\n");
        sb.append("            controller.navigate(Uri.parse(deepLink));\n");
        // 修复Log转义
        sb.append("            Log.d(TAG, \"Deep link navigation successful: \" + deepLink);\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Deep link navigation failed\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateSafeNavigateMethods(StringBuilder sb) {
        sb.append("    public static void navigateSafely(NavController controller, int destination) {\n");
        sb.append("        try {\n");
        sb.append("            if (controller == null) {\n");
        sb.append("                Log.w(TAG, \"NavController is null, skip navigation\");\n");
        sb.append("                return;\n");
        sb.append("            }\n");
        sb.append("            if (controller.getCurrentDestination() != null) {\n");
        sb.append("                controller.navigate(destination);\n");
        // 修复Log转义
        sb.append("                Log.d(TAG, \"Navigation successful: destinationId=\" + destination);\n");
        sb.append("            }\n");
        sb.append("        } catch (Exception e) {\n");
        sb.append("            Log.e(TAG, \"Navigation failed\", e);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }
}