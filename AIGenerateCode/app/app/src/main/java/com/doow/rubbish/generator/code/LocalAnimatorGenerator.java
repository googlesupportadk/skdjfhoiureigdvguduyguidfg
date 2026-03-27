package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalAnimatorGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] ANIMATOR_TYPES = {
            "ObjectAnimator", "ValueAnimator", "AnimatorSet", "LayoutTransition", "ViewPropertyAnimator",
            "TimeAnimator", "ArgbEvaluator", "FloatEvaluator", "IntEvaluator", "RectEvaluator",
            "PointFEvaluator", "PathInterpolator", "AccelerateInterpolator", "DecelerateInterpolator"
    };

    private static final String[] PROPERTY_TYPES = {
            "alpha", "translationX", "translationY", "scaleX", "scaleY", "rotation",
            "rotationX", "rotationY", "pivotX", "pivotY", "x", "y",
            "width", "height", "scrollX", "scrollY"
    };

    private static final int[] DURATION_VALUES = {
            100, 200, 300, 400, 500, 600, 800, 1000,
            1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000
    };

    public LocalAnimatorGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成动画器类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Animator");
            generateAnimatorClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateAnimatorClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("animation"));

        sb.append(generateImportStatement("android.animation.Animator"));
        sb.append(generateImportStatement("android.animation.AnimatorListenerAdapter"));
        sb.append(generateImportStatement("android.animation.AnimatorSet"));
        sb.append(generateImportStatement("android.animation.Keyframe"));
        sb.append(generateImportStatement("android.animation.ObjectAnimator"));
        sb.append(generateImportStatement("android.animation.PropertyValuesHolder"));
        sb.append(generateImportStatement("android.animation.TypeEvaluator"));
        sb.append(generateImportStatement("android.animation.ValueAnimator"));
        sb.append(generateImportStatement("android.graphics.Path"));
        sb.append(generateImportStatement("android.graphics.RectF"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.ViewGroup"));
        sb.append(generateImportStatement("android.view.animation.AccelerateDecelerateInterpolator"));
        sb.append(generateImportStatement("android.view.animation.AccelerateInterpolator"));
        sb.append(generateImportStatement("android.view.animation.AnticipateInterpolator"));
        sb.append(generateImportStatement("android.view.animation.AnticipateOvershootInterpolator"));
        sb.append(generateImportStatement("android.view.animation.BounceInterpolator"));
        sb.append(generateImportStatement("android.view.animation.CycleInterpolator"));
        sb.append(generateImportStatement("android.view.animation.DecelerateInterpolator"));
        sb.append(generateImportStatement("android.view.animation.LinearInterpolator"));
        sb.append(generateImportStatement("android.view.animation.OvershootInterpolator"));
        sb.append(generateImportStatement("android.view.animation.PathInterpolator"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useAnimatorSet = RandomUtils.randomBoolean();
        boolean usePropertyValuesHolder = RandomUtils.randomBoolean();
        boolean useKeyframes = RandomUtils.randomBoolean();
        boolean useMultipleViews = RandomUtils.randomBoolean();
        boolean useAnimationQueue = RandomUtils.randomBoolean();
        boolean useRepeat = RandomUtils.randomBoolean();
        boolean useReverse = RandomUtils.randomBoolean();
        boolean useDelay = RandomUtils.randomBoolean();
        boolean useCustomInterpolator = RandomUtils.randomBoolean();
        boolean useAnimationState = RandomUtils.randomBoolean();
        boolean useAnimationEvents = RandomUtils.randomBoolean();
        boolean usePathAnimation = RandomUtils.randomBoolean();
        boolean useValueAnimator = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" {\n\n");

        String animatorType = ANIMATOR_TYPES[RandomUtils.between(0, ANIMATOR_TYPES.length - 1)];
        String propertyType = PROPERTY_TYPES[RandomUtils.between(0, PROPERTY_TYPES.length - 1)];
        int duration = DURATION_VALUES[RandomUtils.between(0, DURATION_VALUES.length - 1)];

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String ANIMATOR_TYPE = \"").append(animatorType).append("\";\n");
        sb.append("    private static final String PROPERTY_TYPE = \"").append(propertyType).append("\";\n");
        sb.append("    private static final int DURATION = ").append(duration).append(";\n");

        if (useRepeat) {
            sb.append("    private static final int REPEAT_COUNT = ").append(RandomUtils.between(1, 5)).append(";\n");
            sb.append("    private static final int REPEAT_MODE = ").append(RandomUtils.between(1, 2)).append(";\n");
        }

        if (useDelay) {
            sb.append("    private static final long START_DELAY = ").append(DURATION_VALUES[RandomUtils.between(0, DURATION_VALUES.length / 2)]).append("L;\n");
        }

        sb.append("\n");

        // 基础字段
        sb.append("    private View targetView;\n");
        sb.append("    private Animator animator;\n");
        sb.append("    private AnimatorListener listener;\n");

        // AnimatorSet字段
        if (useAnimatorSet) {
            sb.append("    private AnimatorSet animatorSet;\n");
            sb.append("    private List<Animator> animators = new ArrayList<>();\n");
        }

        // PropertyValuesHolder字段
        if (usePropertyValuesHolder) {
            sb.append("    private PropertyValuesHolder propertyValuesHolder;\n");
            sb.append("    private String[] propertyNames;\n");
        }

        // Keyframes字段
        if (useKeyframes) {
            sb.append("    private Keyframe[] keyframes;\n");
            sb.append("    private int keyframeCount = ").append(RandomUtils.between(3, 7)).append(";\n");
        }

        // 多视图字段
        if (useMultipleViews) {
            sb.append("    private List<View> targetViews = new ArrayList<>();\n");
        }

        // 动画队列字段
        if (useAnimationQueue) {
            sb.append("    private List<Runnable> animationQueue = new ArrayList<>();\n");
            sb.append("    private boolean isQueueProcessing = false;\n");
        }

        // 自定义插值器字段
        if (useCustomInterpolator) {
            sb.append("    private PathInterpolator customInterpolator;\n");
        }

        // 动画状态字段
        if (useAnimationState) {
            sb.append("    private boolean isAnimating = false;\n");
            sb.append("    private boolean isPaused = false;\n");
            sb.append("    private float animationProgress = 0f;\n");
            sb.append("    private long animationStartTime = 0;\n");
        }

        // 动画事件字段
        if (useAnimationEvents) {
            sb.append("    private AnimationEventListener eventListener;\n");
            sb.append("\n");
            sb.append("    public interface AnimationEventListener {\n");
            sb.append("        void onAnimationStart();\n");
            sb.append("        void onAnimationEnd();\n");
            sb.append("        void onAnimationCancel();\n");
            sb.append("        void onAnimationRepeat();\n");
            sb.append("        void onAnimationPause();\n");
            sb.append("        void onAnimationResume();\n");
            sb.append("    }\n");
        }

        // 路径动画字段
        if (usePathAnimation) {
            sb.append("    private Path animationPath;\n");
        }

        sb.append("\n");

        // 构造方法
        sb.append("    public ").append(className).append("(View targetView) {\n");
        sb.append("        this.targetView = targetView;\n");
        sb.append("        initialize();\n");
        sb.append("    }\n\n");

        // 多视图构造方法
        if (useMultipleViews) {
            sb.append("    public ").append(className).append("(List<View> views) {\n");
            sb.append("        this.targetViews.addAll(views);\n");
            sb.append("        this.targetView = views.isEmpty() ? null : views.get(0);\n");
            sb.append("        initialize();\n");
            sb.append("    }\n\n");
        }

        // 初始化方法
        sb.append("    private void initialize() {\n");
        sb.append("        setupAnimator();\n");

        if (useAnimatorSet) {
            sb.append("        setupAnimatorSet();\n");
        }

        if (usePropertyValuesHolder) {
            sb.append("        setupPropertyValuesHolder();\n");
        }

        if (useKeyframes) {
            sb.append("        setupKeyframes();\n");
        }

        if (useCustomInterpolator) {
            sb.append("        setupCustomInterpolator();\n");
        }

        if (usePathAnimation) {
            sb.append("        setupAnimationPath();\n");
        }

        if (useAnimationState) {
            sb.append("        resetAnimationState();\n");
        }

        sb.append("    }\n\n");

        // 设置Animator
        sb.append("    private void setupAnimator() {\n");

        if (useValueAnimator) {
            sb.append("        animator = ValueAnimator.ofFloat(0f, 1f);\n");
        } else {
            sb.append("        animator = ObjectAnimator.ofFloat(targetView, PROPERTY_TYPE, 0f, 1f);\n");
        }

        sb.append("        animator.setDuration(DURATION);\n");
        sb.append("        animator.setInterpolator(createInterpolator());\n");

        if (useDelay) {
            sb.append("        animator.setStartDelay(START_DELAY);\n");
        }

        if (useRepeat) {
            sb.append("        animator.setRepeatCount(REPEAT_COUNT);\n");
            sb.append("        animator.setRepeatMode(REPEAT_MODE);\n");
        }

        sb.append("        setupAnimatorListener();\n");
        sb.append("    }\n\n");

        // 设置AnimatorSet
        if (useAnimatorSet) {
            sb.append("    private void setupAnimatorSet() {\n");
            sb.append("        animatorSet = new AnimatorSet();\n");
            sb.append("        animatorSet.playTogether(animators);\n");
            sb.append("        animatorSet.setDuration(DURATION);\n");
            sb.append("        animatorSet.addListener(new AnimatorListenerAdapter() {\n");
            sb.append("            @Override\n");
            sb.append("            public void onAnimationEnd(Animator animation) {\n");

            if (useAnimationEvents) {
                sb.append("                if (eventListener != null) {\n");
                sb.append("                    eventListener.onAnimationEnd();\n");
                sb.append("                }\n");
            }

            if (useAnimationQueue) {
                sb.append("                processNextAnimation();\n");
            }

            sb.append("            }\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        }

        // 设置PropertyValuesHolder
        if (usePropertyValuesHolder) {
            sb.append("    private void setupPropertyValuesHolder() {\n");
            sb.append("        propertyNames = new String[]{PROPERTY_TYPE, \"alpha\", \"scaleX\", \"scaleY\"};\n");
            sb.append("        float[] values = new float[propertyNames.length];\n");
            sb.append("        for (int i = 0; i < values.length; i++) {\n");
            sb.append("            values[i] = RandomUtils.nextFloat(0f, 1f);\n");
            sb.append("        }\n");
            sb.append("        propertyValuesHolder = PropertyValuesHolder.ofFloat(propertyNames[0], values);\n");
            sb.append("    }\n\n");
        }

        // 设置Keyframes
        if (useKeyframes) {
            sb.append("    private void setupKeyframes() {\n");
            sb.append("        keyframes = new Keyframe[keyframeCount];\n");
            sb.append("        for (int i = 0; i < keyframeCount; i++) {\n");
            sb.append("            float fraction = (float) i / (keyframeCount - 1);\n");
            sb.append("            keyframes[i] = Keyframe.ofFloat(fraction, RandomUtils.nextFloat(0f, 1f));\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 设置自定义插值器
        if (useCustomInterpolator) {
            sb.append("    private void setupCustomInterpolator() {\n");
            sb.append("        float controlX1 = RandomUtils.nextFloat(0f, 1f);\n");
            sb.append("        float controlY1 = RandomUtils.nextFloat(0f, 1f);\n");
            sb.append("        float controlX2 = RandomUtils.nextFloat(0f, 1f);\n");
            sb.append("        float controlY2 = RandomUtils.nextFloat(0f, 1f);\n");
            sb.append("        customInterpolator = new PathInterpolator(controlX1, controlY1, controlX2, controlY2);\n");
            sb.append("    }\n\n");
        }

        // 设置动画路径
        if (usePathAnimation) {
            sb.append("    private void setupAnimationPath() {\n");
            sb.append("        animationPath = new Path();\n");
            sb.append("        animationPath.moveTo(0, 0);\n");
            sb.append("        animationPath.lineTo(100, 100);\n");
            sb.append("        animationPath.quadTo(200, 0, 300, 100);\n");
            sb.append("    }\n\n");
        }

        // 设置Animator监听器
        sb.append("    private void setupAnimatorListener() {\n");
        sb.append("        animator.addListener(new AnimatorListenerAdapter() {\n");
        sb.append("            @Override\n");
        sb.append("            public void onAnimationStart(Animator animation) {\n");

        if (useAnimationState) {
            sb.append("                isAnimating = true;\n");
            sb.append("                animationStartTime = System.currentTimeMillis();\n");
        }

        if (useAnimationEvents) {
            sb.append("                if (eventListener != null) {\n");
            sb.append("                    eventListener.onAnimationStart();\n");
            sb.append("                }\n");
        }

        sb.append("            }\n\n");
        sb.append("            @Override\n");
        sb.append("            public void onAnimationEnd(Animator animation) {\n");

        if (useAnimationState) {
            sb.append("                isAnimating = false;\n");
        }

        sb.append("                if (listener != null) {\n");
        sb.append("                    listener.onAnimationEnd(animation);\n");
        sb.append("                }\n");

        if (useAnimationEvents) {
            sb.append("                if (eventListener != null) {\n");
            sb.append("                    eventListener.onAnimationEnd();\n");
            sb.append("                }\n");
        }

        if (useAnimationQueue) {
            sb.append("                processNextAnimation();\n");
        }

        sb.append("            }\n\n");
        sb.append("            @Override\n");
        sb.append("            public void onAnimationCancel(Animator animation) {\n");

        if (useAnimationState) {
            sb.append("                isAnimating = false;\n");
        }

        if (useAnimationEvents) {
            sb.append("                if (eventListener != null) {\n");
            sb.append("                    eventListener.onAnimationCancel();\n");
            sb.append("                }\n");
        }

        sb.append("            }\n\n");
        sb.append("            @Override\n");
        sb.append("            public void onAnimationRepeat(Animator animation) {\n");

        if (useAnimationEvents) {
            sb.append("                if (eventListener != null) {\n");
            sb.append("                    eventListener.onAnimationRepeat();\n");
            sb.append("                }\n");
        }

        sb.append("            }\n");
        sb.append("        });\n");
        sb.append("    }\n\n");

        // 创建插值器
        sb.append("    private android.view.animation.Interpolator createInterpolator() {\n");

        if (useCustomInterpolator) {
            sb.append("        return customInterpolator;\n");
        }

        sb.append("        String interpolatorType = ANIMATOR_TYPES[RandomUtils.between(0, ANIMATOR_TYPES.length - 1)];\n");
        sb.append("        switch (interpolatorType) {\n");
        sb.append("            case \"AccelerateInterpolator\":\n");
        sb.append("                return new AccelerateInterpolator();\n");
        sb.append("            case \"DecelerateInterpolator\":\n");
        sb.append("                return new DecelerateInterpolator();\n");
        sb.append("            case \"AccelerateDecelerateInterpolator\":\n");
        sb.append("                return new AccelerateDecelerateInterpolator();\n");
        sb.append("            case \"AnticipateInterpolator\":\n");
        sb.append("                return new AnticipateInterpolator();\n");
        sb.append("            case \"OvershootInterpolator\":\n");
        sb.append("                return new OvershootInterpolator();\n");
        sb.append("            case \"AnticipateOvershootInterpolator\":\n");
        sb.append("                return new AnticipateOvershootInterpolator();\n");
        sb.append("            case \"BounceInterpolator\":\n");
        sb.append("                return new BounceInterpolator();\n");
        sb.append("            case \"CycleInterpolator\":\n");
        sb.append("                return new CycleInterpolator(1.0f);\n");
        sb.append("            case \"LinearInterpolator\":\n");
        sb.append("                return new LinearInterpolator();\n");
        sb.append("            default:\n");
        sb.append("                return new AccelerateDecelerateInterpolator();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 重置动画状态
        if (useAnimationState) {
            sb.append("    private void resetAnimationState() {\n");
            sb.append("        isAnimating = false;\n");
            sb.append("        isPaused = false;\n");
            sb.append("        animationProgress = 0f;\n");
            sb.append("        animationStartTime = 0;\n");
            sb.append("    }\n\n");
        }

        // 处理动画队列
        if (useAnimationQueue) {
            sb.append("    private void processNextAnimation() {\n");
            sb.append("        if (!animationQueue.isEmpty()) {\n");
            sb.append("            Runnable nextAnimation = animationQueue.remove(0);\n");
            sb.append("            nextAnimation.run();\n");
            sb.append("        } else {\n");
            sb.append("            isQueueProcessing = false;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 启动方法
        sb.append("    public void start() {\n");

        if (useAnimatorSet) {
            sb.append("        if (animatorSet != null && !animatorSet.isRunning()) {\n");
            sb.append("            animatorSet.start();\n");
            sb.append("        }\n");
        }

        sb.append("        if (animator != null && !animator.isRunning()) {\n");
        sb.append("            animator.start();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 取消方法
        sb.append("    public void cancel() {\n");

        if (useAnimatorSet) {
            sb.append("        if (animatorSet != null && animatorSet.isRunning()) {\n");
            sb.append("            animatorSet.cancel();\n");
            sb.append("        }\n");
        }

        sb.append("        if (animator != null && animator.isRunning()) {\n");
        sb.append("            animator.cancel();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 暂停方法
        if (useAnimationState) {
            sb.append("    public void pause() {\n");
            sb.append("        if (isAnimating && !isPaused) {\n");
            sb.append("            animator.pause();\n");
            sb.append("            isPaused = true;\n");

            if (useAnimationEvents) {
                sb.append("            if (eventListener != null) {\n");
                sb.append("                eventListener.onAnimationPause();\n");
                sb.append("            }\n");
            }

            sb.append("        }\n");
            sb.append("    }\n\n");

            // 恢复方法
            sb.append("    public void resume() {\n");
            sb.append("        if (isAnimating && isPaused) {\n");
            sb.append("            animator.resume();\n");
            sb.append("            isPaused = false;\n");

            if (useAnimationEvents) {
                sb.append("            if (eventListener != null) {\n");
                sb.append("                eventListener.onAnimationResume();\n");
                sb.append("            }\n");
            }

            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 反转方法
        if (useReverse) {
            sb.append("    public void reverse() {\n");
            sb.append("        if (animator != null) {\n");
            sb.append("            animator.reverse();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 设置监听器方法
        sb.append("    public void setListener(AnimatorListener listener) {\n");
        sb.append("        this.listener = listener;\n");
        sb.append("    }\n\n");

        // 设置事件监听器方法
        if (useAnimationEvents) {
            sb.append("    public void setEventListener(AnimationEventListener listener) {\n");
            sb.append("        this.eventListener = listener;\n");
            sb.append("    }\n\n");
        }

        // 设置目标视图方法
        sb.append("    public void setTargetView(View view) {\n");
        sb.append("        this.targetView = view;\n");

        if (useMultipleViews) {
            sb.append("        if (!targetViews.contains(view)) {\n");
            sb.append("            targetViews.add(view);\n");
            sb.append("        }\n");
        }

        sb.append("        if (animator instanceof ObjectAnimator) {\n");
        sb.append("            ((ObjectAnimator) animator).setTarget(view);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 添加动画到队列方法
        if (useAnimationQueue) {
            sb.append("    public void addToQueue(Runnable animation) {\n");
            sb.append("        animationQueue.add(animation);\n");
            sb.append("        if (!isQueueProcessing) {\n");
            sb.append("            isQueueProcessing = true;\n");
            sb.append("            processNextAnimation();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 获取动画状态方法
        if (useAnimationState) {
            sb.append("    public boolean isAnimating() {\n");
            sb.append("        return isAnimating;\n");
            sb.append("    }\n\n");

            sb.append("    public boolean isPaused() {\n");
            sb.append("        return isPaused;\n");
            sb.append("    }\n\n");

            sb.append("    public float getAnimationProgress() {\n");
            sb.append("        return animationProgress;\n");
            sb.append("    }\n\n");

            sb.append("    public long getAnimationStartTime() {\n");
            sb.append("        return animationStartTime;\n");
            sb.append("    }\n\n");
        }

        // 获取动画器方法
        sb.append("    public Animator getAnimator() {\n");
        sb.append("        return animator;\n");
        sb.append("    }\n\n");

        // 获取AnimatorSet方法
        if (useAnimatorSet) {
            sb.append("    public AnimatorSet getAnimatorSet() {\n");
            sb.append("        return animatorSet;\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "animation");
    }
}
