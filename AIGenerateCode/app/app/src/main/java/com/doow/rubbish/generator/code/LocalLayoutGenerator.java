package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalLayoutGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] LAYOUT_TYPES = {
            "LinearLayout", "RelativeLayout", "ConstraintLayout", "FrameLayout", "GridLayout",
            "TableLayout", "AbsoluteLayout", "CoordinatorLayout", "MotionLayout", "SwipeRefreshLayout",
            "DrawerLayout", "AppBarLayout", "NestedScrollView", "HorizontalScrollView", "PercentRelativeLayout"
    };

    private static final String[] ORIENTATION_TYPES = {
            "HORIZONTAL", "VERTICAL", "BOTH", "NONE",
            "LEFT_TO_RIGHT", "RIGHT_TO_LEFT", "TOP_TO_BOTTOM", "BOTTOM_TO_TOP"
    };

    private static final String[] VIEW_TYPES = {
            "TextView", "EditText", "Button", "ImageView", "CheckBox",
            "RadioButton", "ToggleButton", "Switch", "SeekBar", "ProgressBar",
            "RatingBar", "Spinner", "WebView", "VideoView", "MapView"
    };

    private static final String[] ANIMATION_TYPES = {
            "AlphaAnimation", "RotateAnimation", "ScaleAnimation", "TranslateAnimation",
            "AnimationSet", "LayoutAnimation", "ObjectAnimator", "ValueAnimator"
    };

    private static final String[] GESTURE_TYPES = {
            "OnTouchListener", "OnClickListener", "OnLongClickListener", "OnFocusChangeListener",
            "OnCheckedChangeListener", "OnItemSelectedListener", "OnScrollChangeListener"
    };

    // 功能标志 - 确保所有字段和方法都会被使用
    private boolean useAnimations;
    private boolean useGestures;
    private boolean useCustomViews;
    private boolean useViewBinding;
    private boolean useLayoutTransitions;
    private boolean useViewPools;
    private boolean useViewRecycling;
    private boolean useAsyncOperations;

    public LocalLayoutGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
        initializeFeatureFlags();
    }

    /**
     * 初始化功能标志，确保随机性和多样性
     */
    private void initializeFeatureFlags() {
        useAnimations = RandomUtils.randomBoolean();
        useGestures = RandomUtils.randomBoolean();
        useCustomViews = RandomUtils.randomBoolean();
        useViewBinding = RandomUtils.randomBoolean();
        useLayoutTransitions = RandomUtils.randomBoolean();
        useViewPools = RandomUtils.randomBoolean();
        useViewRecycling = RandomUtils.randomBoolean();
        useAsyncOperations = RandomUtils.randomBoolean();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成布局类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Layout");
            generateLayoutClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateLayoutClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        // 生成包声明
        sb.append(generatePackageDeclaration("layout"));

        // 生成基础导入
        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.util.AttributeSet"));
        sb.append(generateImportStatement("android.view.ViewGroup"));
        sb.append(generateImportStatement("android.view.animation.*"));
        sb.append(generateImportStatement("android.widget.*"));
        sb.append(generateImportStatement("android.graphics.*"));
        sb.append(generateImportStatement("android.graphics.drawable.*"));

        if (uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.card.MaterialCardView"));
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        // 根据功能标志添加条件导入
        if (useLayoutTransitions) {
            sb.append(generateImportStatement("android.animation.LayoutTransition"));
        }

        if (useViewPools) {
            sb.append(generateImportStatement("android.util.SparseArray"));
        }

        if (useGestures) {
            sb.append(generateImportStatement("android.view.GestureDetector"));
            sb.append(generateImportStatement("android.view.MotionEvent"));
        }

        sb.append(generateImportStatement(packageName + ".utils.*"));
        sb.append("\n");

        // 随机选择布局类型和方向类型
        String layoutType = LAYOUT_TYPES[RandomUtils.between(0, LAYOUT_TYPES.length - 1)];
        String orientationType = ORIENTATION_TYPES[RandomUtils.between(0, ORIENTATION_TYPES.length - 1)];

        // 生成类声明
        sb.append("public class ").append(className).append(" extends LinearLayout {\n\n");

        // 生成常量字段
        String tagVarName = RandomUtils.generateWord(6);
        String layoutTypeVarName = RandomUtils.generateWord(6);
        String orientationVarName = RandomUtils.generateWord(6);

        sb.append("    private static final String ").append(tagVarName).append(" = \"").append(className).append("\";\n");
        sb.append("    private static final String ").append(layoutTypeVarName).append(" = \"").append(layoutType).append("\";\n");
        sb.append("    private static final String ").append(orientationVarName).append(" = \"").append(orientationType).append("\";\n\n");

        // 生成实例字段
        String paddingVarName = RandomUtils.generateWord(6);
        String marginVarName = RandomUtils.generateWord(6);
        String gravityVarName = RandomUtils.generateWord(6);
        String viewPoolVarName = RandomUtils.generateWord(6);
        String gestureDetectorVarName = RandomUtils.generateWord(6);
        String layoutTransitionVarName = RandomUtils.generateWord(6);
        String animationVarName = RandomUtils.generateWord(6);
        String viewBindingVarName = RandomUtils.generateWord(6);

        sb.append("    private int ").append(paddingVarName).append(";\n");
        sb.append("    private int ").append(marginVarName).append(";\n");
        sb.append("    private int ").append(gravityVarName).append(";\n");

        // 根据功能标志添加条件字段
        if (useViewPools) {
            sb.append("    private SparseArray<View> ").append(viewPoolVarName).append(";\n");
        }

        if (useGestures) {
            sb.append("    private GestureDetector ").append(gestureDetectorVarName).append(";\n");
        }

        if (useLayoutTransitions) {
            sb.append("    private LayoutTransition ").append(layoutTransitionVarName).append(";\n");
        }

        if (useAnimations) {
            sb.append("    private Animation ").append(animationVarName).append(";\n");
        }

        if (useViewBinding) {
            sb.append("    private boolean ").append(viewBindingVarName).append(";\n");
        }

        sb.append("\n");

        // 生成构造函数
        generateConstructor(sb, className, paddingVarName, marginVarName, gravityVarName,
                viewPoolVarName, gestureDetectorVarName, layoutTransitionVarName,
                animationVarName, viewBindingVarName, orientationType);

        // 生成基础方法
        generateBasicMethods(sb, paddingVarName, marginVarName, gravityVarName, tagVarName);

        // 根据功能标志添加条件方法
        if (useAnimations) {
            generateAnimationMethods(sb, animationVarName, tagVarName);
        }

        if (useGestures) {
            generateGestureMethods(sb, gestureDetectorVarName, tagVarName);
        }

        if (useCustomViews) {
            generateCustomViewMethods(sb, tagVarName);
        }

        if (useViewBinding) {
            generateViewBindingMethods(sb, viewBindingVarName, tagVarName);
        }

        if (useLayoutTransitions) {
            generateLayoutTransitionMethods(sb, layoutTransitionVarName, tagVarName);
        }

        if (useViewPools) {
            generateViewPoolMethods(sb, viewPoolVarName, tagVarName);
        }

        if (useViewRecycling) {
            generateViewRecyclingMethods(sb, tagVarName);
        }

        if (useAsyncOperations) {
            generateAsyncMethods(sb, tagVarName, asyncHandler);
        }

        // 生成使用所有字段和方法的示例方法
        generateExampleUsageMethod(sb, className, paddingVarName, marginVarName, gravityVarName,
                viewPoolVarName, gestureDetectorVarName, layoutTransitionVarName,
                animationVarName, viewBindingVarName, tagVarName);

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "layout");
    }

    /**
     * 生成构造函数
     */
    private void generateConstructor(StringBuilder sb, String className, String paddingVarName,
                                     String marginVarName, String gravityVarName,
                                     String viewPoolVarName, String gestureDetectorVarName,
                                     String layoutTransitionVarName, String animationVarName,
                                     String viewBindingVarName, String orientationType) {
        String contextParamName = RandomUtils.generateWord(6);
        String attrsParamName = RandomUtils.generateWord(6);
        String defStyleAttrParamName = RandomUtils.generateWord(6);
        String initMethodName = RandomUtils.generateWord(6);

        sb.append("    public ").append(className).append("(Context ").append(contextParamName).append(") {\n");
        sb.append("        this(").append(contextParamName).append(", null);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context ").append(contextParamName)
                .append(", AttributeSet ").append(attrsParamName).append(") {\n");
        sb.append("        this(").append(contextParamName).append(", ").append(attrsParamName)
                .append(", 0);\n");
        sb.append("    }\n\n");

        sb.append("    public ").append(className).append("(Context ").append(contextParamName)
                .append(", AttributeSet ").append(attrsParamName).append(", int ").append(defStyleAttrParamName)
                .append(") {\n");
        sb.append("        super(").append(contextParamName).append(", ").append(attrsParamName)
                .append(", ").append(defStyleAttrParamName).append(");\n");
        sb.append("        ").append(initMethodName).append("();\n");
        sb.append("    }\n\n");

        sb.append("    private void ").append(initMethodName).append("() {\n");
        sb.append("        setOrientation(\"").append(orientationType).append("\".equals(\"HORIZONTAL\") ? HORIZONTAL : VERTICAL);\n");
        sb.append("        this.").append(paddingVarName).append(" = ").append(RandomUtils.between(8, 24)).append(";\n");
        sb.append("        this.").append(marginVarName).append(" = ").append(RandomUtils.between(4, 16)).append(";\n");
        sb.append("        this.").append(gravityVarName).append(" = Gravity.CENTER;\n");
        sb.append("        setPadding(").append(paddingVarName).append(", ").append(paddingVarName)
                .append(", ").append(paddingVarName).append(", ").append(paddingVarName).append(");\n");

        if (useViewPools) {
            sb.append("        ").append(viewPoolVarName).append(" = new SparseArray<>();\n");
        }

        if (useGestures) {
            sb.append("        ").append(gestureDetectorVarName).append(" = new GestureDetector(getContext(), new SimpleOnGestureListener() {\n");
            sb.append("            @Override\n");
            sb.append("            public boolean onDown(MotionEvent e) {\n");
            sb.append("                return true;\n");
            sb.append("            }\n");
            sb.append("        });\n");
        }

        if (useLayoutTransitions) {
            sb.append("        ").append(layoutTransitionVarName).append(" = new LayoutTransition();\n");
            sb.append("        setLayoutTransition(").append(layoutTransitionVarName).append(");\n");
        }

        if (useAnimations) {
            sb.append("        ").append(animationVarName).append(" = new AlphaAnimation(0.0f, 1.0f);\n");
            sb.append("        ").append(animationVarName).append(".setDuration(").append(RandomUtils.between(200, 500))
                    .append(");\n");
        }

        if (useViewBinding) {
            sb.append("        this.").append(viewBindingVarName).append(" = true;\n");
        }

        sb.append("    }\n\n");
    }

    /**
     * 生成基础方法
     */
    private void generateBasicMethods(StringBuilder sb, String paddingVarName, String marginVarName,
                                      String gravityVarName, String tagVarName) {
        // 生成添加视图方法
        String addViewMethodName = RandomUtils.generateWord(6);
        String viewParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(addViewMethodName).append("(View ").append(viewParamName).append(") {\n");
        sb.append("        if (").append(viewParamName).append(" != null) {\n");
        sb.append("            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(\n");
        sb.append("                ViewGroup.LayoutParams.WRAP_CONTENT,\n");
        sb.append("                ViewGroup.LayoutParams.WRAP_CONTENT\n");
        sb.append("            );\n");
        sb.append("            params.setMargins(").append(marginVarName).append(", ").append(marginVarName)
                .append(", ").append(marginVarName).append(", ").append(marginVarName).append(");\n");
        sb.append("            params.gravity = ").append(gravityVarName).append(";\n");
        sb.append("            addView(").append(viewParamName).append(", params);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成添加带权重的视图方法
        String addViewWithWeightMethodName = RandomUtils.generateWord(6);
        String weightParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(addViewWithWeightMethodName).append("(View ")
                .append(viewParamName).append(", float ").append(weightParamName).append(") {\n");
        sb.append("        if (").append(viewParamName).append(" != null) {\n");
        sb.append("            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(\n");
        sb.append("                0,\n");
        sb.append("                ViewGroup.LayoutParams.WRAP_CONTENT,\n");
        sb.append("                ").append(weightParamName).append("\n");
        sb.append("            );\n");
        sb.append("            params.setMargins(").append(marginVarName).append(", ").append(marginVarName)
                .append(", ").append(marginVarName).append(", ").append(marginVarName).append(");\n");
        sb.append("            params.gravity = ").append(gravityVarName).append(";\n");
        sb.append("            addView(").append(viewParamName).append(", params);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成设置内边距方法
        String setPaddingMethodName = RandomUtils.generateWord(6);
        String paddingParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(setPaddingMethodName).append("(int ")
                .append(paddingParamName).append(") {\n");
        sb.append("        this.").append(paddingVarName).append(" = ").append(paddingParamName).append(";\n");
        sb.append("        setPadding(").append(paddingParamName).append(", ").append(paddingParamName)
                .append(", ").append(paddingParamName).append(", ").append(paddingParamName).append(");\n");
        sb.append("    }\n\n");

        // 生成设置外边距方法
        String setMarginMethodName = RandomUtils.generateWord(6);
        String marginParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(setMarginMethodName).append("(int ")
                .append(marginParamName).append(") {\n");
        sb.append("        this.").append(marginVarName).append(" = ").append(marginParamName).append(";\n");
        sb.append("        for (int i = 0; i < getChildCount(); i++) {\n");
        sb.append("            View child = getChildAt(i);\n");
        sb.append("            if (child.getLayoutParams() instanceof LinearLayout.LayoutParams) {\n");
        sb.append("                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();\n");
        sb.append("                params.setMargins(").append(marginParamName).append(", ")
                .append(marginParamName).append(", ").append(marginParamName).append(", ")
                .append(marginParamName).append(");\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成设置重力方法
        String setGravityMethodName = RandomUtils.generateWord(6);
        String gravityParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(setGravityMethodName).append("(int ")
                .append(gravityParamName).append(") {\n");
        sb.append("        this.").append(gravityVarName).append(" = ").append(gravityParamName).append(";\n");
        sb.append("        for (int i = 0; i < getChildCount(); i++) {\n");
        sb.append("            View child = getChildAt(i);\n");
        sb.append("            if (child.getLayoutParams() instanceof LinearLayout.LayoutParams) {\n");
        sb.append("                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();\n");
        sb.append("                params.gravity = ").append(gravityParamName).append(";\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 生成清除方法
        String clearMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(clearMethodName).append("() {\n");
        sb.append("        removeAllViews();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成动画方法
     */
    private void generateAnimationMethods(StringBuilder sb, String animationVarName, String tagVarName) {
        String startAnimationMethodName = RandomUtils.generateWord(6);
        String stopAnimationMethodName = RandomUtils.generateWord(6);
        String setAnimationDurationMethodName = RandomUtils.generateWord(6);
        String durationParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(startAnimationMethodName).append("() {\n");
        sb.append("        startAnimation(").append(animationVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(stopAnimationMethodName).append("() {\n");
        sb.append("        clearAnimation();\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setAnimationDurationMethodName).append("(int ")
                .append(durationParamName).append(") {\n");
        sb.append("        ").append(animationVarName).append(".setDuration(").append(durationParamName)
                .append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成手势方法
     */
    private void generateGestureMethods(StringBuilder sb, String gestureDetectorVarName, String tagVarName) {
        String onTouchEventMethodName = RandomUtils.generateWord(6);
        String eventParamName = RandomUtils.generateWord(6);

        sb.append("    @Override\n");
        sb.append("    public boolean onTouchEvent(MotionEvent ").append(eventParamName).append(") {\n");
        sb.append("        return ").append(gestureDetectorVarName).append(".onTouchEvent(")
                .append(eventParamName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成自定义视图方法
     */
    private void generateCustomViewMethods(StringBuilder sb, String tagVarName) {
        String createCustomViewMethodName = RandomUtils.generateWord(6);
        String viewType = VIEW_TYPES[RandomUtils.between(0, VIEW_TYPES.length - 1)];
        String viewVarName = RandomUtils.generateWord(6);

        sb.append("    public View ").append(createCustomViewMethodName).append("() {\n");
        sb.append("        View ").append(viewVarName).append(" = new ").append(viewType)
                .append("(getContext());\n");
        sb.append("        ").append(viewVarName).append(".setId(View.generateViewId());\n");
        sb.append("        return ").append(viewVarName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成视图绑定方法
     */
    private void generateViewBindingMethods(StringBuilder sb, String viewBindingVarName, String tagVarName) {
        String enableViewBindingMethodName = RandomUtils.generateWord(6);
        String disableViewBindingMethodName = RandomUtils.generateWord(6);
        String isViewBindingEnabledMethodName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(enableViewBindingMethodName).append("() {\n");
        sb.append("        this.").append(viewBindingVarName).append(" = true;\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(disableViewBindingMethodName).append("() {\n");
        sb.append("        this.").append(viewBindingVarName).append(" = false;\n");
        sb.append("    }\n\n");

        sb.append("    public boolean ").append(isViewBindingEnabledMethodName).append("() {\n");
        sb.append("        return this.").append(viewBindingVarName).append(";\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成布局过渡方法
     */
    private void generateLayoutTransitionMethods(StringBuilder sb, String layoutTransitionVarName, String tagVarName) {
        String enableLayoutTransitionMethodName = RandomUtils.generateWord(6);
        String disableLayoutTransitionMethodName = RandomUtils.generateWord(6);
        String setLayoutTransitionDurationMethodName = RandomUtils.generateWord(6);
        String durationParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(enableLayoutTransitionMethodName).append("() {\n");
        sb.append("        setLayoutTransition(").append(layoutTransitionVarName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(disableLayoutTransitionMethodName).append("() {\n");
        sb.append("        setLayoutTransition(null);\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(setLayoutTransitionDurationMethodName).append("(int ")
                .append(durationParamName).append(") {\n");
        sb.append("        ").append(layoutTransitionVarName).append(".setDuration(")
                .append(durationParamName).append(");\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成视图池方法
     */
    private void generateViewPoolMethods(StringBuilder sb, String viewPoolVarName, String tagVarName) {
        String getViewFromPoolMethodName = RandomUtils.generateWord(6);
        String putViewToPoolMethodName = RandomUtils.generateWord(6);
        String clearViewPoolMethodName = RandomUtils.generateWord(6);
        String viewIdParamName = RandomUtils.generateWord(6);
        String viewParamName = RandomUtils.generateWord(6);

        sb.append("    public View ").append(getViewFromPoolMethodName).append("(int ")
                .append(viewIdParamName).append(") {\n");
        sb.append("        return ").append(viewPoolVarName).append(".get(").append(viewIdParamName)
                .append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(putViewToPoolMethodName).append("(int ")
                .append(viewIdParamName).append(", View ").append(viewParamName).append(") {\n");
        sb.append("        ").append(viewPoolVarName).append(".put(").append(viewIdParamName)
                .append(", ").append(viewParamName).append(");\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(clearViewPoolMethodName).append("() {\n");
        sb.append("        ").append(viewPoolVarName).append(".clear();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成视图回收方法
     */
    private void generateViewRecyclingMethods(StringBuilder sb, String tagVarName) {
        String recycleViewMethodName = RandomUtils.generateWord(6);
        String recycleAllViewsMethodName = RandomUtils.generateWord(6);
        String viewParamName = RandomUtils.generateWord(6);

        sb.append("    public void ").append(recycleViewMethodName).append("(View ")
                .append(viewParamName).append(") {\n");
        sb.append("        if (").append(viewParamName).append(" != null) {\n");
        sb.append("            removeView(").append(viewParamName).append(");\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void ").append(recycleAllViewsMethodName).append("() {\n");
        sb.append("        removeAllViews();\n");
        sb.append("    }\n\n");
    }

    /**
     * 生成异步操作方法
     */
    private void generateAsyncMethods(StringBuilder sb, String tagVarName, String asyncHandler) {
        String asyncAddViewMethodName = RandomUtils.generateWord(6);
        String asyncRemoveViewMethodName = RandomUtils.generateWord(6);
        String viewParamName = RandomUtils.generateWord(6);

        if (asyncHandler.contains("coroutines")) {
            // 使用协程的异步方法
            sb.append("    public void ").append(asyncAddViewMethodName).append("(View ")
                    .append(viewParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.Main).launch(() -> {\n");
            sb.append("            addView(").append(viewParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveViewMethodName).append("(View ")
                    .append(viewParamName).append(") {\n");
            sb.append("        new CoroutineScope(Dispatchers.Main).launch(() -> {\n");
            sb.append("            removeView(").append(viewParamName).append(");\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        } else if (asyncHandler.contains("rxjava")) {
            // 使用RxJava的异步方法
            sb.append("    public void ").append(asyncAddViewMethodName).append("(View ")
                    .append(viewParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            addView(").append(viewParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> {},\n");
            sb.append("              error -> {}\n");
            sb.append("          );\n");
            sb.append("    }\n\n");

            sb.append("    public void ").append(asyncRemoveViewMethodName).append("(View ")
                    .append(viewParamName).append(") {\n");
            sb.append("        Single.fromCallable(() -> {\n");
            sb.append("            removeView(").append(viewParamName).append(");\n");
            sb.append("            return true;\n");
            sb.append("        }).subscribeOn(Schedulers.io())\n");
            sb.append("          .observeOn(Schedulers.single())\n");
            sb.append("          .subscribe(\n");
            sb.append("              success -> {},\n");
            sb.append("              error -> {}\n");
            sb.append("          );\n");
            sb.append("    }\n\n");
        }
    }

    /**
     * 生成示例使用方法
     */
    private void generateExampleUsageMethod(StringBuilder sb, String className, String paddingVarName,
                                            String marginVarName, String gravityVarName,
                                            String viewPoolVarName, String gestureDetectorVarName,
                                            String layoutTransitionVarName, String animationVarName,
                                            String viewBindingVarName, String tagVarName) {
        String exampleMethodName = RandomUtils.generateWord(6);
        String viewVarName = RandomUtils.generateWord(6);
        String viewType = VIEW_TYPES[RandomUtils.between(0, VIEW_TYPES.length - 1)];

        sb.append("    public void ").append(exampleMethodName).append("() {\n");
        sb.append("        View ").append(viewVarName).append(" = new ").append(viewType)
                .append("(getContext());\n");
        sb.append("        ").append(viewVarName).append(".setId(View.generateViewId());\n");

        // 使用基础方法
        sb.append("        addView(").append(viewVarName).append(");\n");

        // 使用动画方法
        if (useAnimations) {
            sb.append("        startAnimation();\n");
        }

        // 使用视图池方法
        if (useViewPools) {
            sb.append("        putViewToPool(").append(viewVarName).append(".getId(), ")
                    .append(viewVarName).append(");\n");
        }

        // 使用布局过渡方法
        if (useLayoutTransitions) {
            sb.append("        enableLayoutTransition();\n");
        }

        // 使用视图绑定方法
        if (useViewBinding) {
            sb.append("        enableViewBinding();\n");
        }

        sb.append("    }\n\n");
    }
}
