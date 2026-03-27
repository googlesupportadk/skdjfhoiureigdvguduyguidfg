package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.ClassTracker;
import com.doow.rubbish.generator.GeneratorConfig;
import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import com.doow.rubbish.generator.VariationManager;

public class ActivityGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    public ActivityGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成 " + (GeneratorConfig.packageCount * GeneratorConfig.activityCountPerPackage) + " 个Activity类");

        for (int i = 0; i < GeneratorConfig.packageCount; i++) {
            String subPackage = "feature" + (i + 1);
            for (int j = 0; j < GeneratorConfig.activityCountPerPackage; j++) {
                String className = RandomUtils.generateClassName("Activity");
                ClassTracker.addActivity(className);
                generateActivity(className, subPackage);
            }
        }
    }

    private void generateActivity(String className, String subPackage) throws Exception {
        StringBuilder sb = new StringBuilder();

        String architecture = variationManager.getVariation("architecture");
        String uiStyle = variationManager.getVariation("ui_style");
        String navigationMode = variationManager.getVariation("navigation_mode");

        sb.append(generatePackageDeclaration(subPackage));

        sb.append(generateImportStatement("android.content.Intent"));
        sb.append(generateImportStatement("android.os.Bundle"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.widget.Button"));
        sb.append(generateImportStatement("android.widget.TextView"));
        sb.append(generateImportStatement("androidx.appcompat.app.AppCompatActivity"));

        if (architecture.contains("mvvm")) {
            sb.append(generateImportStatement("androidx.lifecycle.ViewModel"));
            sb.append(generateImportStatement("androidx.lifecycle.ViewModelProvider"));
        } else if (architecture.contains("mvp")) {
            sb.append(generateImportStatement("android.content.Context"));
        } else if (architecture.contains("mvi")) {
            sb.append(generateImportStatement("androidx.lifecycle.ViewModel"));
            sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
        }

        if (uiStyle.contains("material") || uiStyle.contains("glassmorphism")) {
            sb.append(generateImportStatement("com.google.android.material.appbar.MaterialToolbar"));
        }

        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append(generateImportStatement(packageName + ".viewmodel.*"));
        sb.append(generateImportStatement(packageName + ".manager.*"));
        sb.append(generateImportStatement(packageName + ".helper.*"));
        sb.append("\n");

        sb.append("public class ").append(className).append(" extends AppCompatActivity {\n\n");

        String viewModelClass = ClassTracker.getRandomViewModel();
        if (viewModelClass != null) {
            sb.append("    private ").append(viewModelClass).append(" viewModel;\n\n");
        }

        String managerClass = ClassTracker.getRandomManager();
        if (managerClass != null) {
            sb.append("    private ").append(managerClass).append(" manager;\n\n");
        }

        String helperClass = ClassTracker.getRandomHelper();
        if (helperClass != null) {
            sb.append("    private ").append(helperClass).append(" helper;\n\n");
        }

        sb.append("    @Override\n");
        sb.append("    protected void onCreate(Bundle savedInstanceState) {\n");
        sb.append("        super.onCreate(savedInstanceState);\n");
        sb.append("        setContentView(R.layout.").append(ResourceTracker.getRandomLayout() != null ? ResourceTracker.getRandomLayout() : "activity_layout").append(");\n\n");

        if (managerClass != null) {
            sb.append("        manager = ").append(managerClass).append(".getInstance(this);\n");
        }

        if (viewModelClass != null) {
            sb.append("        viewModel = new ").append(viewModelClass).append("();\n");
        }

        sb.append("        initView();\n");
        sb.append("        initData();\n");
        sb.append("    }\n\n");

        sb.append("    private void initView() {\n");
        sb.append("        TextView textView = findViewById(R.id.text_title);\n");
        sb.append("        textView.setText(\"" + "" + RandomUtils.generateWord(RandomUtils.between(3, 8)) + \"\");\n\n");

        int buttonCount = RandomUtils.between(1, 3);
        for (int i = 0; i < buttonCount; i++) {
            sb.append("        Button button").append(i).append(" = findViewById(R.id.button_").append(i).append(");\n");
            sb.append("        button").append(i).append(".setOnClickListener(v -> {\n");

            String targetActivity = ClassTracker.getRandomActivity();
            if (targetActivity != null && !targetActivity.equals(className)) {
                sb.append("            Intent intent = new Intent(").append(className).append(".this, ").append(targetActivity).append(".class);\n");
                sb.append("            startActivity(intent);\n");
            }

            sb.append("        });\n\n");
        }

        sb.append("    }\n\n");

        sb.append("    private void initData() {\n");
        sb.append("        if (viewModelClass != null) {\n");
        sb.append("            if (viewModel != null) {\n");
        sb.append("                viewModel.loadData();\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    protected void onDestroy() {\n");
        sb.append("        super.onDestroy();\n");
        if (managerClass != null) {
            sb.append("        if (manager != null) {\n");
            sb.append("            manager.clear();\n");
            sb.append("        }\n");
        }
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), subPackage);
    }
}
