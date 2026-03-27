package com.doow.rubbish.generator.resource;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class AnimationResourceGenerator extends BaseResourceGenerator {

    protected VariationManager variationManager;

    private static final String[] ANIMATION_TYPES = {
        "fade", "slide", "scale", "rotate", "translate"
    };

    public AnimationResourceGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成动画资源文件");

        String uiStyle = variationManager.getVariation("ui_style");

        generateAnimations(uiStyle);
    }

    private void generateAnimations(String uiStyle) throws Exception {
        generateFadeAnimations(uiStyle);
        generateSlideAnimations(uiStyle);
        generateScaleAnimations(uiStyle);
        generateRotateAnimations(uiStyle);
        generateTranslateAnimations(uiStyle);
        generateRandomAnimations(uiStyle);
    }

    private void generateFadeAnimations(String uiStyle) throws Exception {
        StringBuilder fadeIn = new StringBuilder();
        fadeIn.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        fadeIn.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        fadeIn.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        fadeIn.append("    <alpha\n");
        fadeIn.append("        android:fromAlpha="" + "0.0"" + ""\n");
        fadeIn.append("        android:toAlpha="" + "1.0"" + " />\n");
        fadeIn.append("</set>");
        generateXmlFile("fade_in", fadeIn.toString(), "anim");

        StringBuilder fadeOut = new StringBuilder();
        fadeOut.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        fadeOut.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        fadeOut.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        fadeOut.append("    <alpha\n");
        fadeOut.append("        android:fromAlpha="" + "1.0"" + ""\n");
        fadeOut.append("        android:toAlpha="" + "0.0"" + " />\n");
        fadeOut.append("</set>");
        generateXmlFile("fade_out", fadeOut.toString(), "anim");
    }

    private void generateSlideAnimations(String uiStyle) throws Exception {
        StringBuilder slideIn = new StringBuilder();
        slideIn.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        slideIn.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        slideIn.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        slideIn.append("    <translate\n");
        slideIn.append("        android:fromXDelta="" + "-100%"" + ""\n");
        slideIn.append("        android:toXDelta="" + "0%"" + " />\n");
        slideIn.append("</set>");
        generateXmlFile("slide_in", slideIn.toString(), "anim");

        StringBuilder slideOut = new StringBuilder();
        slideOut.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        slideOut.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        slideOut.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        slideOut.append("    <translate\n");
        slideOut.append("        android:fromXDelta="" + "0%"" + ""\n");
        slideOut.append("        android:toXDelta="" + "-100%"" + " />\n");
        slideOut.append("</set>");
        generateXmlFile("slide_out", slideOut.toString(), "anim");
    }

    private void generateScaleAnimations(String uiStyle) throws Exception {
        StringBuilder scaleIn = new StringBuilder();
        scaleIn.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        scaleIn.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        scaleIn.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        scaleIn.append("    <scale\n");
        scaleIn.append("        android:fromXScale="" + "0.5"" + ""\n");
        scaleIn.append("        android:fromYScale="" + "0.5"" + ""\n");
        scaleIn.append("        android:toXScale="" + "1.0"" + ""\n");
        scaleIn.append("        android:toYScale="" + "1.0"" + " />\n");
        scaleIn.append("</set>");
        generateXmlFile("scale_in", scaleIn.toString(), "anim");

        StringBuilder scaleOut = new StringBuilder();
        scaleOut.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        scaleOut.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        scaleOut.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        scaleOut.append("    <scale\n");
        scaleOut.append("        android:fromXScale="" + "1.0"" + ""\n");
        scaleOut.append("        android:fromYScale="" + "1.0"" + ""\n");
        scaleOut.append("        android:toXScale="" + "0.5"" + ""\n");
        scaleOut.append("        android:toYScale="" + "0.5"" + " />\n");
        scaleOut.append("</set>");
        generateXmlFile("scale_out", scaleOut.toString(), "anim");
    }

    private void generateRotateAnimations(String uiStyle) throws Exception {
        StringBuilder rotateIn = new StringBuilder();
        rotateIn.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        rotateIn.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        rotateIn.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        rotateIn.append("    <rotate\n");
        rotateIn.append("        android:fromDegrees="" + "0"" + ""\n");
        rotateIn.append("        android:toDegrees="" + "360"" + " />\n");
        rotateIn.append("</set>");
        generateXmlFile("rotate_in", rotateIn.toString(), "anim");

        StringBuilder rotateOut = new StringBuilder();
        rotateOut.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        rotateOut.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        rotateOut.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        rotateOut.append("    <rotate\n");
        rotateOut.append("        android:fromDegrees="" + "360"" + ""\n");
        rotateOut.append("        android:toDegrees="" + "0"" + " />\n");
        rotateOut.append("</set>");
        generateXmlFile("rotate_out", rotateOut.toString(), "anim");
    }

    private void generateTranslateAnimations(String uiStyle) throws Exception {
        StringBuilder translateIn = new StringBuilder();
        translateIn.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        translateIn.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        translateIn.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        translateIn.append("    <translate\n");
        translateIn.append("        android:fromYDelta="" + "-100%"" + ""\n");
        translateIn.append("        android:toYDelta="" + "0%"" + " />\n");
        translateIn.append("</set>");
        generateXmlFile("translate_in", translateIn.toString(), "anim");

        StringBuilder translateOut = new StringBuilder();
        translateOut.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
        translateOut.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
        translateOut.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
        translateOut.append("    <translate\n");
        translateOut.append("        android:fromYDelta="" + "0%"" + ""\n");
        translateOut.append("        android:toYDelta="" + "-100%"" + " />\n");
        translateOut.append("</set>");
        generateXmlFile("translate_out", translateOut.toString(), "anim");
    }

    private void generateRandomAnimations(String uiStyle) throws Exception {
        int randomCount = RandomUtils.between(3, 5);
        for (int i = 0; i < randomCount; i++) {
            String animationType = ANIMATION_TYPES[RandomUtils.between(0, ANIMATION_TYPES.length - 1)];
            String name = "random_" + animationType + "_" + i;
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version="" + "1.0"" + " encoding="" + "utf-8"" + "?>\n");
            sb.append("<set xmlns:android="" + "http://schemas.android.com/apk/res/android"" + ""\n");
            sb.append("    android:duration="" + getAnimationDuration(uiStyle) + """ + "">\n");
            sb.append("    <" + animationType + "\n");
            sb.append("        android:fromXDelta="" + (RandomUtils.nextBoolean() ? "-50%" : "50%") + """ + "\n");
            sb.append("        android:fromYDelta="" + (RandomUtils.nextBoolean() ? "-50%" : "50%") + """ + " />\n");
            sb.append("    </" + animationType + ">\n");
            sb.append("</set>");
            generateXmlFile(name, sb.toString(), "anim");
        }
    }

    private String getAnimationDuration(String uiStyle) {
        if (uiStyle.contains("fast")) {
            return "200";
        } else if (uiStyle.contains("slow")) {
            return "800";
        } else {
            return "300";
        }
    }
}
