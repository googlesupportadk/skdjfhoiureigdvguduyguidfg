package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

import java.io.File;

public class ComposeGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] COMPOSABLE_TYPES = {
            "Screen", "Component", "View", "Widget", "Element",
            "Layout", "Container", "Card", "ListItem", "Dialog",
            "BottomSheet", "TopBar", "BottomBar", "FloatingButton", "SnackBar"
    };

    private static final String[] MODIFIER_TYPES = {
            "padding", "margin", "size", "width", "height", "fillMaxWidth", "fillMaxHeight",
            "background", "border", "shadow", "alpha", "rotation", "scale",
            "offset", "clickable", "draggable", "swipeable", "clip"
    };

    public ComposeGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Compose组件");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        // 边界防护：生成数量限定在合理范围
        int generateCount = RandomUtils.between(5, 15);
        for (int i = 0; i < generateCount; i++) {
            // 生成有意义的Composable名称（结合组件类型）
            String composableType = COMPOSABLE_TYPES[RandomUtils.between(0, COMPOSABLE_TYPES.length - 1)];
            String className = RandomUtils.generateClassName(composableType);
            generateComposableClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateComposableClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("ui.compose"));

        // 核心Compose导入
        sb.append(generateImportStatement("androidx.compose.foundation.layout.Column"));
        sb.append(generateImportStatement("androidx.compose.foundation.layout.Row"));
        sb.append(generateImportStatement("androidx.compose.foundation.layout.padding"));
        sb.append(generateImportStatement("androidx.compose.foundation.layout.fillMaxWidth")); // 补充缺失的fillMaxWidth导入
        sb.append(generateImportStatement("androidx.compose.material3.MaterialTheme"));
        sb.append(generateImportStatement("androidx.compose.material3.Text"));
        sb.append(generateImportStatement("androidx.compose.runtime.Composable"));
        sb.append(generateImportStatement("androidx.compose.runtime.getValue")); // State相关必备导入
        sb.append(generateImportStatement("androidx.compose.runtime.setValue"));
        sb.append(generateImportStatement("androidx.compose.ui.Modifier"));
        sb.append(generateImportStatement("androidx.compose.ui.unit.dp"));
        sb.append(generateImportStatement("androidx.lifecycle.viewmodel.compose.viewModel")); // 补充viewModel()方法导入

        // 异步状态适配导入
        if (asyncHandler != null) {
            if (asyncHandler.contains("livedata")) {
                sb.append(generateImportStatement("androidx.compose.runtime.livedata.observeAsState"));
                sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
            } else if (asyncHandler.contains("stateflow")) {
                sb.append(generateImportStatement("androidx.compose.runtime.collectAsState"));
                sb.append(generateImportStatement("kotlinx.coroutines.flow.StateFlow"));
            }
        }

        // 业务相关导入
        sb.append(generateImportStatement(packageName + ".viewmodel.*"));
        sb.append("\n"); // 规范空行

        // 生成Composable函数（修复语法和转义问题）
        String viewModelClassName = RandomUtils.generateClassName("ViewModel");
        sb.append("@Composable\n");
        sb.append("fun ").append(className).append("(\n");
        sb.append("    viewModel: ").append(viewModelClassName).append(" = viewModel(),\n");
        sb.append("    modifier: Modifier = Modifier\n");
        sb.append(") {\n");

        // 适配不同异步状态获取方式
        if (asyncHandler != null) {
            if (asyncHandler.contains("livedata")) {
                sb.append("    val uiState by viewModel.uiState.observeAsState(initial = null)\n");
            } else if (asyncHandler.contains("stateflow")) {
                sb.append("    val uiState by viewModel.uiState.collectAsState()\n");
            } else {
                // 默认StateFlow
                sb.append("    val uiState by viewModel.uiState.collectAsState()\n");
            }
        } else {
            sb.append("    val uiState by viewModel.uiState.collectAsState()\n");
        }

        sb.append("\n");

        // 生成Column布局（修复Modifier链式调用）
        sb.append("    Column(\n");
        sb.append("        modifier = modifier\n");
        sb.append("            .padding(16.dp)\n");
        sb.append("            .fillMaxWidth()\n");
        sb.append("    ) {\n");

        // 生成Text组件（修复字符串转义错误）
        String randomText = RandomUtils.generateWord(RandomUtils.between(3, 8));
        sb.append("        Text(\n");
        sb.append("            text = \"").append(randomText).append("\",\n"); // 正确的字符串转义
        sb.append("            style = MaterialTheme.typography.bodyLarge\n"); // 补充规范的样式设置
        sb.append("        )\n");

        sb.append("    }\n");
        sb.append("}\n");

        // 生成文件（Compose函数生成.kt文件，修复文件类型问题）
        generateKotlinFile(className, sb.toString(), "ui.compose");
    }

    /**
     * 适配Compose的Kotlin文件生成（覆盖基类的Java文件生成）
     */
    protected void generateKotlinFile(String className, String content, String subPackage) throws Exception {
        String packagePath = packageName.replace(".", "/");
        String fullPath = projectRoot + "/app/src/main/java/" + packagePath + "/" + subPackage;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Compose组件生成.kt文件（而非.java）
        String filePath = fullPath + "/" + className + ".kt";
        writeFile(filePath, content);
    }

    /**
     * 兼容基类的writeFile方法（若基类已删除则需保留）
     */
    protected void writeFile(String filePath, String content) throws Exception {
        com.doow.rubbish.generator.FileUtils.writeFile(filePath, content);
    }
}