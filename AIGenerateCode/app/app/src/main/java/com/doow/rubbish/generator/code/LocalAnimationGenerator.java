package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class LocalAnimationGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] ANIMATION_TYPES = {
            "fade", "slide", "scale", "rotate", "translate",
            "flip", "blink", "zoom", "bounce", "shake",
            "wave", "pulse", "spin", "flip_x", "flip_y", "rotate_3d"
    };

    private static final String[] DURATION_VALUES = {
            "100", "200", "300", "400", "500", "600", "800", "1000",
            "1500", "2000", "2500", "3000", "4000", "5000", "6000", "8000"
    };

    private static final String[] INTERPOLATOR_VALUES = {
            "accelerate", "decelerate", "accelerate_decelerate", "anticipate", "overshoot",
            "anticipate_overshoot", "bounce", "cycle", "linear", "path",
            "fast_out_linear_in", "fast_out_slow_in", "linear_out_slow_in"
    };

    public LocalAnimationGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成动画类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 15); i++) {
            String className = RandomUtils.generateClassName("Animation");
            generateAnimationClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateAnimationClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("animation"));

        sb.append(generateImportStatement("android.animation.Animator"));
        sb.append(generateImportStatement("android.animation.AnimatorListenerAdapter"));
        sb.append(generateImportStatement("android.animation.AnimatorSet"));
        sb.append(generateImportStatement("android.animation.ObjectAnimator"));
        sb.append(generateImportStatement("android.animation.ValueAnimator"));
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
        sb.append(generateImportStatement("android.animation.TimeInterpolator"));
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
        boolean useObjectAnimator = RandomUtils.randomBoolean();
        boolean useMultipleViews = RandomUtils.randomBoolean();
        boolean useAnimationQueue = RandomUtils.randomBoolean();
        boolean useRepeat = RandomUtils.randomBoolean();
        boolean useReverse = RandomUtils.randomBoolean();
        boolean useDelay = RandomUtils.randomBoolean();
        boolean useCustomInterpolator = RandomUtils.randomBoolean();
        boolean useAnimationState = RandomUtils.randomBoolean();
        boolean useAnimationEvents = RandomUtils.randomBoolean();

        sb.append("public class ").append(className).append(" {\n\n");

        String duration = DURATION_VALUES[RandomUtils.between(0, DURATION_VALUES.length - 1)];
        String interpolator = INTERPOLATOR_VALUES[RandomUtils.between(0, INTERPOLATOR_VALUES.length - 1)];
        String animationType = ANIMATION_TYPES[RandomUtils.between(0, ANIMATION_TYPES.length - 1)];

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final long DURATION = ").append(duration).append("L;\n");
        sb.append("    private static final String INTERPOLATOR = \"").append(interpolator).append("\";\n");
        sb.append("    private static final String ANIMATION_TYPE = \"").append(animationType).append("\";\n");

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
        sb.append("    private ValueAnimator animator;\n");
        sb.append("    private AnimatorListener listener;\n");

        // AnimatorSet字段
        if (useAnimatorSet) {
            sb.append("    private AnimatorSet animatorSet;\n");
            sb.append("    private List<Animator> animators = new ArrayList<>();\n");
        }

        // ObjectAnimator字段
        if (useObjectAnimator) {
            sb.append("    private ObjectAnimator objectAnimator;\n");
            sb.append("    private String propertyName;\n");
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
            sb.append("    private TimeInterpolator customInterpolator;\n");
        }

        // 动画状态字段
        if (useAnimationState) {
            sb.append("    private boolean isAnimating = false;\n");
            sb.append("    private boolean isPaused = false;\n");
            sb.append("    private float animationProgress = 0f;\n");
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
            sb.append("    }\n");
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

        if (useObjectAnimator) {
            sb.append("        setupObjectAnimator();\n");
        }

        if (useCustomInterpolator) {
            sb.append("        setupCustomInterpolator();\n");
        }

        if (useAnimationState) {
            sb.append("        resetAnimationState();\n");
        }

        sb.append("    }\n\n");

        // 设置ValueAnimator
        sb.append("    private void setupAnimator() {\n");
        sb.append("        animator = ValueAnimator.ofFloat(0f, 1f);\n");
        sb.append("        animator.setDuration(DURATION);\n");
        sb.append("        animator.setInterpolator(createInterpolator(INTERPOLATOR));\n");

        if (useDelay) {
            sb.append("        animator.setStartDelay(START_DELAY);\n");
        }

        if (useRepeat) {
            sb.append("        animator.setRepeatCount(REPEAT_COUNT);\n");
            sb.append("        animator.setRepeatMode(REPEAT_MODE);\n");
        }

        sb.append("        animator.addUpdateListener(animation -> {\n");
        sb.append("            float value = (float) animation.getAnimatedValue();\n");

        if (useAnimationState) {
            sb.append("            animationProgress = value;\n");
        }

        sb.append("            onAnimationUpdate(value);\n");
        sb.append("        });\n");
        sb.append("        animator.addListener(new AnimatorListenerAdapter() {\n");
        sb.append("            @Override\n");
        sb.append("            public void onAnimationStart(Animator animation) {\n");

        if (useAnimationState) {
            sb.append("                isAnimating = true;\n");
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

        // 设置AnimatorSet
        if (useAnimatorSet) {
            sb.append("    private void setupAnimatorSet() {\n");
            sb.append("        animatorSet = new AnimatorSet();\n");
            sb.append("        animatorSet.playTogether(animator);\n");
            sb.append("    }\n\n");

            sb.append("    public void addAnimator(Animator anim) {\n");
            sb.append("        animators.add(anim);\n");
            sb.append("        animatorSet.playTogether(anim);\n");
            sb.append("    }\n\n");

            sb.append("    public void removeAnimator(Animator anim) {\n");
            sb.append("        animators.remove(anim);\n");
            sb.append("        animatorSet.playTogether(animators.toArray(new Animator[0]));\n");
            sb.append("    }\n\n");
        }

        // 设置ObjectAnimator
        if (useObjectAnimator) {
            sb.append("    private void setupObjectAnimator() {\n");
            sb.append("        propertyName = getRandomProperty();\n");
            sb.append("        objectAnimator = ObjectAnimator.ofFloat(targetView, propertyName, 0f, 1f);\n");
            sb.append("        objectAnimator.setDuration(DURATION);\n");
            sb.append("        objectAnimator.setInterpolator(createInterpolator(INTERPOLATOR));\n");
            sb.append("    }\n\n");

            sb.append("    private String getRandomProperty() {\n");
            sb.append("        String[] properties = {\"alpha\", \"scaleX\", \"scaleY\", \"rotation\", \"rotationX\", \"rotationY\", \"translationX\", \"translationY\"};\n");
            sb.append("        return properties[RandomUtils.between(0, properties.length - 1)];\n");
            sb.append("    }\n\n");

            sb.append("    public void setPropertyName(String name) {\n");
            sb.append("        this.propertyName = name;\n");
            sb.append("        objectAnimator.setPropertyName(name);\n");
            sb.append("    }\n\n");
        }

        // 设置自定义插值器
        if (useCustomInterpolator) {
            sb.append("    private void setupCustomInterpolator() {\n");
            sb.append("        customInterpolator = createCustomInterpolator();\n");
            sb.append("    }\n\n");

            sb.append("    private TimeInterpolator createCustomInterpolator() {\n");
            sb.append("        return input -> {\n");
            sb.append("            float output = input;\n");
            sb.append("            switch (INTERPOLATOR) {\n");
            sb.append("                case \"bounce\":\n");
            sb.append("                    output = new BounceInterpolator().getInterpolation(input);\n");
            sb.append("                    break;\n");
            sb.append("                case \"cycle\":\n");
            sb.append("                    output = new CycleInterpolator(2f).getInterpolation(input);\n");
            sb.append("                    break;\n");
            sb.append("                case \"anticipate_overshoot\":\n");
            sb.append("                    output = new AnticipateOvershootInterpolator().getInterpolation(input);\n");
            sb.append("                    break;\n");
            sb.append("                default:\n");
            sb.append("                    output = new AccelerateDecelerateInterpolator().getInterpolation(input);\n");
            sb.append("                    break;\n");
            sb.append("            }\n");
            sb.append("            return output;\n");
            sb.append("        };\n");
            sb.append("    }\n\n");
        }

        // 创建插值器
        sb.append("    private TimeInterpolator createInterpolator(String type) {\n");
        sb.append("        switch (type) {\n");
        sb.append("            case \"accelerate\":\n");
        sb.append("                return new AccelerateInterpolator();\n");
        sb.append("            case \"decelerate\":\n");
        sb.append("                return new DecelerateInterpolator();\n");
        sb.append("            case \"accelerate_decelerate\":\n");
        sb.append("                return new AccelerateDecelerateInterpolator();\n");
        sb.append("            case \"anticipate\":\n");
        sb.append("                return new AnticipateInterpolator();\n");
        sb.append("            case \"overshoot\":\n");
        sb.append("                return new OvershootInterpolator();\n");
        sb.append("            case \"bounce\":\n");
        sb.append("                return new BounceInterpolator();\n");
        sb.append("            case \"cycle\":\n");
        sb.append("                return new CycleInterpolator(2f);\n");
        sb.append("            case \"anticipate_overshoot\":\n");
        sb.append("                return new AnticipateOvershootInterpolator();\n");
        sb.append("            case \"linear\":\n");
        sb.append("            default:\n");
        sb.append("                return new LinearInterpolator();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 重置动画状态
        if (useAnimationState) {
            sb.append("    private void resetAnimationState() {\n");
            sb.append("        isAnimating = false;\n");
            sb.append("        isPaused = false;\n");
            sb.append("        animationProgress = 0f;\n");
            sb.append("    }\n\n");

            sb.append("    public boolean isAnimating() {\n");
            sb.append("        return isAnimating;\n");
            sb.append("    }\n\n");

            sb.append("    public boolean isPaused() {\n");
            sb.append("        return isPaused;\n");
            sb.append("    }\n\n");

            sb.append("    public float getAnimationProgress() {\n");
            sb.append("        return animationProgress;\n");
            sb.append("    }\n\n");
        }

        // 动画队列处理
        if (useAnimationQueue) {
            sb.append("    public void queueAnimation(Runnable animation) {\n");
            sb.append("        animationQueue.add(animation);\n");
            sb.append("        if (!isQueueProcessing) {\n");
            sb.append("            processNextAnimation();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    private void processNextAnimation() {\n");
            sb.append("        if (animationQueue.isEmpty()) {\n");
            sb.append("            isQueueProcessing = false;\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        isQueueProcessing = true;\n");
            sb.append("        Runnable nextAnimation = animationQueue.remove(0);\n");
            sb.append("        nextAnimation.run();\n");
            sb.append("    }\n\n");

            sb.append("    public void clearQueue() {\n");
            sb.append("        animationQueue.clear();\n");
            sb.append("        isQueueProcessing = false;\n");
            sb.append("    }\n\n");
        }

        // 启动动画
        sb.append("    public void start() {\n");
        sb.append("        if (animator != null && !animator.isRunning()) {\n");

        if (useAnimationState) {
            sb.append("            isAnimating = true;\n");
        }

        if (useAnimatorSet) {
            sb.append("            animatorSet.start();\n");
        }

        sb.append("            animator.start();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 取消动画
        sb.append("    public void cancel() {\n");
        sb.append("        if (animator != null && animator.isRunning()) {\n");

        if (useAnimationState) {
            sb.append("            isAnimating = false;\n");
        }

        if (useAnimatorSet) {
            sb.append("            animatorSet.cancel();\n");
        }

        sb.append("            animator.cancel();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 暂停和恢复动画
        if (useAnimationState) {
            sb.append("    public void pause() {\n");
            sb.append("        if (isAnimating && !isPaused) {\n");
            sb.append("            isPaused = true;\n");
            sb.append("            animator.pause();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    public void resume() {\n");
            sb.append("        if (isAnimating && isPaused) {\n");
            sb.append("            isPaused = false;\n");
            sb.append("            animator.resume();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 反转动画
        if (useReverse) {
            sb.append("    public void reverse() {\n");
            sb.append("        if (animator != null) {\n");
            sb.append("            animator.reverse();\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 设置监听器
        sb.append("    public void setListener(AnimatorListener listener) {\n");
        sb.append("        this.listener = listener;\n");
        sb.append("    }\n\n");

        // 设置事件监听器
        if (useAnimationEvents) {
            sb.append("    public void setEventListener(AnimationEventListener listener) {\n");
            sb.append("        this.eventListener = listener;\n");
            sb.append("    }\n\n");
        }

        // 动画更新方法
        sb.append("    protected void onAnimationUpdate(float value) {\n");
        sb.append("        if (targetView != null) {\n");
        sb.append("            applyAnimation(value);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // 应用动画
        sb.append("    private void applyAnimation(float value) {\n");
        sb.append("        switch (ANIMATION_TYPE) {\n");
        sb.append("            case \"fade\":\n");
        sb.append("                targetView.setAlpha(value);\n");
        sb.append("                break;\n");
        sb.append("            case \"scale\":\n");
        sb.append("                targetView.setScaleX(value);\n");
        sb.append("                targetView.setScaleY(value);\n");
        sb.append("                break;\n");
        sb.append("            case \"rotate\":\n");
        sb.append("                targetView.setRotation(value * 360f);\n");
        sb.append("                break;\n");
        sb.append("            case \"translate\":\n");
        sb.append("                targetView.setTranslationX(value * 100f);\n");
        sb.append("                break;\n");
        sb.append("            case \"slide\":\n");
        sb.append("                targetView.setTranslationY(value * 100f);\n");
        sb.append("                break;\n");
        sb.append("            case \"flip\":\n");
        sb.append("                targetView.setRotationY(value * 180f);\n");
        sb.append("                break;\n");
        sb.append("            case \"zoom\":\n");
        sb.append("                targetView.setScaleX(value * 2f);\n");
        sb.append("                targetView.setScaleY(value * 2f);\n");
        sb.append("                break;\n");
        sb.append("            case \"blink\":\n");
        sb.append("                targetView.setAlpha(value > 0.5f ? 1f : 0f);\n");
        sb.append("                break;\n");
        sb.append("            case \"shake\":\n");
        sb.append("                targetView.setTranslationX((float) Math.sin(value * Math.PI * 4) * 20f);\n");
        sb.append("                break;\n");
        sb.append("            case \"wave\":\n");
        sb.append("                targetView.setTranslationY((float) Math.sin(value * Math.PI * 2) * 30f);\n");
        sb.append("                break;\n");
        sb.append("            case \"pulse\":\n");
        sb.append("                float scale = 1f + (float) Math.sin(value * Math.PI) * 0.2f;\n");
        sb.append("                targetView.setScaleX(scale);\n");
        sb.append("                targetView.setScaleY(scale);\n");
        sb.append("                break;\n");
        sb.append("            case \"spin\":\n");
        sb.append("                targetView.setRotation(value * 720f);\n");
        sb.append("                break;\n");
        sb.append("            case \"flip_x\":\n");
        sb.append("                targetView.setRotationX(value * 180f);\n");
        sb.append("                break;\n");
        sb.append("            case \"flip_y\":\n");
        sb.append("                targetView.setRotationY(value * 180f);\n");
        sb.append("                break;\n");
        sb.append("            case \"rotate_3d\":\n");
        sb.append("                targetView.setRotationX(value * 360f);\n");
        sb.append("                targetView.setRotationY(value * 360f);\n");
        sb.append("                break;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // Getter和Setter方法
        sb.append("    public View getTargetView() {\n");
        sb.append("        return targetView;\n");
        sb.append("    }\n\n");

        sb.append("    public void setTargetView(View view) {\n");
        sb.append("        this.targetView = view;\n");

        if (useObjectAnimator) {
            sb.append("        objectAnimator.setTarget(view);\n");
        }

        sb.append("    }\n\n");

        sb.append("    public long getDuration() {\n");
        sb.append("        return DURATION;\n");
        sb.append("    }\n\n");

        sb.append("    public String getAnimationType() {\n");
        sb.append("        return ANIMATION_TYPE;\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "animation");
    }
}
