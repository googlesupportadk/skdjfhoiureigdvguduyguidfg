package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class DialogGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] DIALOG_TYPES = {
            "alert", "confirm", "custom", "progress", "input",
            "list", "single_choice", "multi_choice", "date_picker", "time_picker",
            "color_picker", "bottom_sheet", "side_sheet", "full_screen", "floating"
    };

    private static final String[] BUTTON_TYPES = {
            "positive", "negative", "neutral", "cancel", "ok",
            "yes", "no", "retry", "ignore", "dismiss",
            "close", "save", "delete", "share", "copy"
    };

    private static final String[] ANIMATION_TYPES = {
            "fade", "slide", "scale", "rotate", "flip", "bounce"
    };

    private static final String[] THEME_TYPES = {
            "light", "dark", "day_night", "transparent", "colored"
    };

    public DialogGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成对话框类");

        String uiStyle = variationManager.getVariation("ui_style");

        for (int i = 0; i < RandomUtils.between(3, 10); i++) {
            String className = RandomUtils.generateClassName("Dialog");
            generateDialogClass(className, uiStyle);
        }
    }

    private void generateDialogClass(String className, String uiStyle) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("ui"));

        // 基础导入
        sb.append(generateImportStatement("android.app.Dialog"));
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.os.Bundle"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.Window"));
        sb.append(generateImportStatement("android.widget.Button"));
        sb.append(generateImportStatement("android.widget.TextView"));
        sb.append(generateImportStatement("android.widget.EditText"));
        sb.append(generateImportStatement("android.widget.ProgressBar"));
        sb.append(generateImportStatement("android.widget.ImageView"));
        sb.append(generateImportStatement("android.graphics.drawable.Drawable"));
        sb.append(generateImportStatement("android.view.animation.Animation"));
        sb.append(generateImportStatement("android.view.animation.AnimationUtils"));
        sb.append(generateImportStatement("android.graphics.Color"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));
        sb.append(generateImportStatement("java.util.List"));
        sb.append(generateImportStatement("java.util.ArrayList"));

        if (uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.dialog.MaterialAlertDialogBuilder"));
            sb.append(generateImportStatement("com.google.android.material.button.MaterialButton"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append(generateImportStatement(packageName + ".R"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useInput = RandomUtils.randomBoolean();
        boolean useProgress = RandomUtils.randomBoolean();
        boolean useIcon = RandomUtils.randomBoolean();
        boolean useAnimation = RandomUtils.randomBoolean();
        boolean useClickListener = RandomUtils.randomBoolean();
        boolean useDataStorage = RandomUtils.randomBoolean();
        boolean useTheme = RandomUtils.randomBoolean();
        boolean useValidation = RandomUtils.randomBoolean();
        boolean useAutoDismiss = RandomUtils.randomBoolean();
        boolean useTimeout = RandomUtils.randomBoolean();
        boolean useList = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" extends Dialog {\n\n");

        String dialogType = DIALOG_TYPES[RandomUtils.between(0, DIALOG_TYPES.length - 1)];
        String buttonType = BUTTON_TYPES[RandomUtils.between(0, BUTTON_TYPES.length - 1)];
        String animationType = useAnimation ? ANIMATION_TYPES[RandomUtils.between(0, ANIMATION_TYPES.length - 1)] : "";
        String themeType = useTheme ? THEME_TYPES[RandomUtils.between(0, THEME_TYPES.length - 1)] : "";

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String DIALOG_TYPE = \"").append(dialogType).append("\";\n");
        sb.append("    private static final String BUTTON_TYPE = \"").append(buttonType).append("\";\n");

        if (useAnimation) {
            sb.append("    private static final String ANIMATION_TYPE = \"").append(animationType).append("\";\n");
        }

        if (useTheme) {
            sb.append("    private static final String THEME_TYPE = \"").append(themeType).append("\";\n");
        }

        if (useTimeout) {
            sb.append("    private static final long DEFAULT_TIMEOUT_MS = 5000;\n");
        }

        sb.append("\n");

        // 基础UI字段
        sb.append("    private TextView titleText;\n");
        sb.append("    private TextView messageText;\n");
        sb.append("    private Button ").append(buttonType).append("Button;\n");

        // 功能字段
        if (useInput) {
            sb.append("    private EditText inputField;\n");
        }

        if (useProgress) {
            sb.append("    private ProgressBar progressBar;\n");
        }

        if (useIcon) {
            sb.append("    private ImageView dialogIcon;\n");
        }

        if (useAnimation) {
            sb.append("    private Animation enterAnimation;\n");
            sb.append("    private Animation exitAnimation;\n");
        }

        if (useClickListener) {
            sb.append("    private OnDialogClickListener clickListener;\n");
        }

        if (useDataStorage) {
            sb.append("    private Map<String, Object> dialogData;\n");
        }

        if (useList) {
            sb.append("    private android.widget.ListView listView;\n");
            sb.append("    private List<String> listItems;\n");
        }

        if (useValidation) {
            sb.append("    private java.util.function.Predicate<String> inputValidator;\n");
        }

        if (useTimeout) {
            sb.append("    private Runnable timeoutRunnable;\n");
        }

        sb.append("\n");

        // 构造函数
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        super(context);\n");
        sb.append("        init();\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context context, int themeResId) {\n");
        sb.append("        super(context, themeResId);\n");
        sb.append("        init();\n");
        sb.append("    }\n\n");

        // 初始化方法 - 使用所有生成的字段
        sb.append("    private void init() {\n");
        sb.append("        Log.d(TAG, \"Initializing dialog: \" + DIALOG_TYPE);\n");

        if (useTheme) {
            sb.append("        setupTheme();\n");
        }

        sb.append("        setContentView(R.layout.dialog_layout);\n");
        sb.append("        setupWindow();\n");
        sb.append("        setupViews();\n");
        sb.append("        setupListeners();\n");

        if (useDataStorage) {
            sb.append("        setupDataStorage();\n");
        }

        if (useValidation) {
            sb.append("        setupValidation();\n");
        }

        sb.append("    }\n\n");

        // 主题设置
        if (useTheme) {
            sb.append("    private void setupTheme() {\n");
            sb.append("        switch (THEME_TYPE) {\n");
            sb.append("            case \"light\":\n");
            sb.append("                getWindow().setBackgroundDrawableResource(android.R.color.white);\n");
            sb.append("                break;\n");
            sb.append("            case \"dark\":\n");
            sb.append("                getWindow().setBackgroundDrawableResource(android.R.color.black);\n");
            sb.append("                break;\n");
            sb.append("            case \"transparent\":\n");
            sb.append("                getWindow().setBackgroundDrawableResource(android.R.color.transparent);\n");
            sb.append("                break;\n");
            sb.append("            default:\n");
            sb.append("                getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);\n");
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 窗口设置
        sb.append("    private void setupWindow() {\n");
        sb.append("        Window window = getWindow();\n");
        sb.append("        if (window != null) {\n");
        sb.append("            window.setLayout(\n");
        sb.append("                (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85),\n");
        sb.append("                android.view.ViewGroup.LayoutParams.WRAP_CONTENT\n");
        sb.append("            );\n");

        if (useAnimation) {
            sb.append("            setupAnimations();\n");
        }

        sb.append("        }\n");
        sb.append("    }\n\n");

        // 动画设置
        if (useAnimation) {
            sb.append("    private void setupAnimations() {\n");
            sb.append("        switch (ANIMATION_TYPE) {\n");
            sb.append("            case \"fade\":\n");
            sb.append("                enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);\n");
            sb.append("                exitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);\n");
            sb.append("                break;\n");
            sb.append("            case \"slide\":\n");
            sb.append("                enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);\n");
            sb.append("                exitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);\n");
            sb.append("                break;\n");
            sb.append("            case \"scale\":\n");
            sb.append("                enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in);\n");
            sb.append("                exitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);\n");
            sb.append("                break;\n");
            sb.append("            default:\n");
            sb.append("                enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);\n");
            sb.append("                exitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);\n");
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 视图设置
        sb.append("    private void setupViews() {\n");
        sb.append("        titleText = findViewById(R.id.dialog_title);\n");
        sb.append("        messageText = findViewById(R.id.dialog_message);\n");
        sb.append("        ").append(buttonType).append("Button = findViewById(R.id.dialog_").append(buttonType).append("_button);\n");

        if (useInput) {
            sb.append("        inputField = findViewById(R.id.dialog_input);\n");
        }

        if (useProgress) {
            sb.append("        progressBar = findViewById(R.id.dialog_progress);\n");
        }

        if (useIcon) {
            sb.append("        dialogIcon = findViewById(R.id.dialog_icon);\n");
        }

        if (useList) {
            sb.append("        listView = findViewById(R.id.dialog_list);\n");
            sb.append("        listItems = new ArrayList<>();\n");
        }

        sb.append("    }\n\n");

        // 监听器设置
        sb.append("    private void setupListeners() {\n");
        sb.append("        ").append(buttonType).append("Button.setOnClickListener(v -> {\n");

        if (useClickListener) {
            sb.append("            if (clickListener != null) {\n");
            sb.append("                clickListener.onDialogClick(").append(buttonType).append("Button);\n");
            sb.append("            }\n");
        }

        if (useAutoDismiss) {
            sb.append("            dismiss();\n");
        }

        sb.append("        });\n");

        if (useTimeout) {
            sb.append("        setupTimeout();\n");
        }

        sb.append("    }\n\n");

        // 数据存储设置
        if (useDataStorage) {
            sb.append("    private void setupDataStorage() {\n");
            sb.append("        dialogData = new HashMap<>();\n");
            sb.append("    }\n\n");
        }

        // 验证设置
        if (useValidation) {
            sb.append("    private void setupValidation() {\n");
            sb.append("        inputValidator = input -> {\n");
            sb.append("            boolean isValid = input != null && !input.trim().isEmpty();\n");
            sb.append("            if (!isValid) {\n");
            sb.append("                inputField.setError(\"Input cannot be empty\");\n");
            sb.append("            }\n");
            sb.append("            return isValid;\n");
            sb.append("        };\n");
            sb.append("    }\n\n");
        }

        // 超时设置
        if (useTimeout) {
            sb.append("    private void setupTimeout() {\n");
            sb.append("        timeoutRunnable = () -> {\n");
            sb.append("            Log.d(TAG, \"Dialog timeout reached\");\n");
            sb.append("            dismiss();\n");
            sb.append("        };\n");
            sb.append("        ").append(buttonType).append("Button.postDelayed(timeoutRunnable, DEFAULT_TIMEOUT_MS);\n");
            sb.append("    }\n\n");
        }

        // 公共方法
        sb.append("    public void setTitle(String title) {\n");
        sb.append("        if (titleText != null) {\n");
        sb.append("            titleText.setText(title);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void setMessage(String message) {\n");
        sb.append("        if (messageText != null) {\n");
        sb.append("            messageText.setText(message);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 输入相关方法
        if (useInput) {
            sb.append("    public void setInputHint(String hint) {\n");
            sb.append("        if (inputField != null) {\n");
            sb.append("            inputField.setHint(hint);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public String getInputText() {\n");
            sb.append("        if (inputField != null) {\n");
            sb.append("            return inputField.getText().toString();\n");
            sb.append("        }\n");
            sb.append("        return \"\";\n");
            sb.append("    }\n\n");

            if (useValidation) {
                sb.append("    public boolean validateInput() {\n");
                sb.append("        if (inputField != null && inputValidator != null) {\n");
                sb.append("            return inputValidator.test(getInputText());\n");
                sb.append("        }\n");
                sb.append("        return true;\n");
                sb.append("    }\n\n");
            }
        }

        // 进度相关方法
        if (useProgress) {
            sb.append("    public void setProgress(int progress) {\n");
            sb.append("        if (progressBar != null) {\n");
            sb.append("            progressBar.setProgress(progress);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void setIndeterminate(boolean indeterminate) {\n");
            sb.append("        if (progressBar != null) {\n");
            sb.append("            progressBar.setIndeterminate(indeterminate);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void showProgress() {\n");
            sb.append("        if (progressBar != null) {\n");
            sb.append("            progressBar.setVisibility(View.VISIBLE);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void hideProgress() {\n");
            sb.append("        if (progressBar != null) {\n");
            sb.append("            progressBar.setVisibility(View.GONE);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 图标相关方法
        if (useIcon) {
            sb.append("    public void setIcon(Drawable icon) {\n");
            sb.append("        if (dialogIcon != null) {\n");
            sb.append("            dialogIcon.setImageDrawable(icon);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void setIcon(int resId) {\n");
            sb.append("        if (dialogIcon != null) {\n");
            sb.append("            dialogIcon.setImageResource(resId);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 动画相关方法
        if (useAnimation) {
            sb.append("    public void playEnterAnimation() {\n");
            sb.append("        if (enterAnimation != null) {\n");
            sb.append("            getWindow().getDecorView().startAnimation(enterAnimation);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void playExitAnimation() {\n");
            sb.append("        if (exitAnimation != null) {\n");
            sb.append("            getWindow().getDecorView().startAnimation(exitAnimation);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 点击监听器相关方法
        if (useClickListener) {
            sb.append("    public void setOnDialogClickListener(OnDialogClickListener listener) {\n");
            sb.append("        this.clickListener = listener;\n");
            sb.append("    }\n\n");

            sb.append("    public interface OnDialogClickListener {\n");
            sb.append("        void onDialogClick(Button button);\n");
            sb.append("        void onDialogCancel();\n");
            sb.append("    }\n\n");
        }

        // 数据存储相关方法
        if (useDataStorage) {
            sb.append("    public void putData(String key, Object value) {\n");
            sb.append("        if (dialogData != null) {\n");
            sb.append("            dialogData.put(key, value);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public Object getData(String key) {\n");
            sb.append("        if (dialogData != null) {\n");
            sb.append("            return dialogData.get(key);\n");
            sb.append("        }\n");
            sb.append("        return null;\n");
            sb.append("    }\n\n");

            sb.append("    public void clearData() {\n");
            sb.append("        if (dialogData != null) {\n");
            sb.append("            dialogData.clear();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 列表相关方法
        if (useList) {
            sb.append("    public void setListItems(List<String> items) {\n");
            sb.append("        if (listItems != null && listView != null) {\n");
            sb.append("            listItems.clear();\n");
            sb.append("            listItems.addAll(items);\n");
            sb.append("            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(\n");
            sb.append("                getContext(),\n");
            sb.append("                android.R.layout.simple_list_item_1,\n");
            sb.append("                listItems\n");
            sb.append("            );\n");
            sb.append("            listView.setAdapter(adapter);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void addListItem(String item) {\n");
            sb.append("        if (listItems != null) {\n");
            sb.append("            listItems.add(item);\n");
            sb.append("            setListItems(listItems);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public List<String> getListItems() {\n");
            sb.append("        return listItems != null ? new ArrayList<>(listItems) : new ArrayList<>();\n");
            sb.append("    }\n\n");
        }

        // 显示和取消方法
        sb.append("    @Override\n");
        sb.append("    public void show() {\n");
        sb.append("        super.show();\n");

        if (useAnimation) {
            sb.append("        playEnterAnimation();\n");
        }

        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void cancel() {\n");

        if (useClickListener) {
            sb.append("        if (clickListener != null) {\n");
            sb.append("            clickListener.onDialogCancel();\n");
            sb.append("        }\n");
        }

        if (useTimeout) {
            sb.append("        if (timeoutRunnable != null) {\n");
            sb.append("            ").append(buttonType).append("Button.removeCallbacks(timeoutRunnable);\n");
            sb.append("        }\n");
        }

        sb.append("        super.cancel();\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void dismiss() {\n");

        if (useAnimation) {
            sb.append("        playExitAnimation();\n");
        }

        if (useTimeout) {
            sb.append("        if (timeoutRunnable != null) {\n");
            sb.append("            ").append(buttonType).append("Button.removeCallbacks(timeoutRunnable);\n");
            sb.append("        }\n");
        }

        sb.append("        super.dismiss();\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "ui");
    }
}
