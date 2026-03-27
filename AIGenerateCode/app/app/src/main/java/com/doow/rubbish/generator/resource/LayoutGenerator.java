package com.doow.rubbish.generator.resource;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import java.io.IOException;

public class LayoutGenerator extends BaseResourceGenerator {

    public LayoutGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Layout资源");

        generateActivityLayouts();
        generateFragmentLayouts();
        generateItemLayouts();
        generateDialogLayouts();
    }

    private void generateActivityLayouts() throws IOException {
        int count = RandomUtils.between(10, 20);

        for (int i = 0; i < count; i++) {
            String layoutName = RandomUtils.generateLayoutName("activity");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<androidx.constraintlayout.widget.ConstraintLayout\n");
            sb.append("    xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    xmlns:app="http://schemas.android.com/apk/res-auto"\n");
            sb.append("    xmlns:tools="http://schemas.android.com/tools"\n");
            sb.append("    android:layout_width="match_parent"\n");
            sb.append("    android:layout_height="match_parent"\n");
            sb.append("    tools:context=".").append(RandomUtils.generateClassName("Activity")).append("">\n\n");

            // 添加一些子视图
            int viewCount = RandomUtils.between(3, 8);
            for (int j = 0; j < viewCount; j++) {
                sb.append(generateRandomView(j));
            }

            sb.append("</androidx.constraintlayout.widget.ConstraintLayout>");

            writeFile("layout", layoutName + ".xml", sb.toString());
            ResourceTracker.addResource("layout/" + layoutName + ".xml");
        }
    }

    private void generateFragmentLayouts() throws IOException {
        int count = RandomUtils.between(10, 20);

        for (int i = 0; i < count; i++) {
            String layoutName = RandomUtils.generateLayoutName("fragment");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<FrameLayout\n");
            sb.append("    xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    xmlns:tools="http://schemas.android.com/tools"\n");
            sb.append("    android:layout_width="match_parent"\n");
            sb.append("    android:layout_height="match_parent"\n");
            sb.append("    tools:context=".").append(RandomUtils.generateClassName("Fragment")).append("">\n\n");

            // 添加一些子视图
            int viewCount = RandomUtils.between(2, 5);
            for (int j = 0; j < viewCount; j++) {
                sb.append(generateRandomView(j));
            }

            sb.append("</FrameLayout>");

            writeFile("layout", layoutName + ".xml", sb.toString());
            ResourceTracker.addResource("layout/" + layoutName + ".xml");
        }
    }

    private void generateItemLayouts() throws IOException {
        int count = RandomUtils.between(10, 20);

        for (int i = 0; i < count; i++) {
            String layoutName = RandomUtils.generateLayoutName("item");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<LinearLayout\n");
            sb.append("    xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    android:layout_width="match_parent"\n");
            sb.append("    android:layout_height="wrap_content"\n");
            sb.append("    android:orientation="").append(RandomUtils.generateRandomOrientation()).append(""\n");
            sb.append("    android:padding="").append(RandomUtils.generateRandomDimension()).append("">\n\n");

            // 添加一些子视图
            int viewCount = RandomUtils.between(2, 4);
            for (int j = 0; j < viewCount; j++) {
                sb.append(generateRandomView(j));
            }

            sb.append("</LinearLayout>");

            writeFile("layout", layoutName + ".xml", sb.toString());
            ResourceTracker.addResource("layout/" + layoutName + ".xml");
        }
    }

    private void generateDialogLayouts() throws IOException {
        int count = RandomUtils.between(5, 10);

        for (int i = 0; i < count; i++) {
            String layoutName = RandomUtils.generateLayoutName("dialog");
            StringBuilder sb = new StringBuilder();

            sb.append("<?xml version="1.0" encoding="utf-8"?>\n");
            sb.append("<LinearLayout\n");
            sb.append("    xmlns:android="http://schemas.android.com/apk/res/android"\n");
            sb.append("    android:layout_width="match_parent"\n");
            sb.append("    android:layout_height="wrap_content"\n");
            sb.append("    android:orientation="vertical"\n");
            sb.append("    android:padding="").append(RandomUtils.generateRandomDimension()).append("">\n\n");

            // 添加标题
            sb.append("    <TextView\n");
            sb.append("        android:layout_width="match_parent"\n");
            sb.append("        android:layout_height="wrap_content"\n");
            sb.append("        android:text="").append(RandomUtils.generateRandomString(5, 20)).append(""\n");
            sb.append("        android:textSize="").append(RandomUtils.generateRandomDimension()).append(""\n");
            sb.append("        android:textStyle="bold"\n");
            sb.append("        android:padding="").append(RandomUtils.generateRandomDimension()).append("" />\n\n");

            // 添加内容
            sb.append("    <TextView\n");
            sb.append("        android:layout_width="match_parent"\n");
            sb.append("        android:layout_height="wrap_content"\n");
            sb.append("        android:text="").append(RandomUtils.generateRandomString(20, 100)).append(""\n");
            sb.append("        android:textSize="").append(RandomUtils.generateRandomDimension()).append(""\n");
            sb.append("        android:padding="").append(RandomUtils.generateRandomDimension()).append("" />\n\n");

            // 添加按钮
            sb.append("    <Button\n");
            sb.append("        android:id="@+id/button_ok"\n");
            sb.append("        android:layout_width="match_parent"\n");
            sb.append("        android:layout_height="wrap_content"\n");
            sb.append("        android:text="OK"\n");
            sb.append("        android:layout_marginTop="").append(RandomUtils.generateRandomDimension()).append("" />\n\n");

            sb.append("</LinearLayout>");

            writeFile("layout", layoutName + ".xml", sb.toString());
            ResourceTracker.addResource("layout/" + layoutName + ".xml");
        }
    }

    private String generateRandomView(int index) {
        String viewType = RandomUtils.generateRandomViewType();
        StringBuilder sb = new StringBuilder();

        sb.append("    <").append(viewType).append("\n");
        sb.append("        android:id="@+id/view_").append(index).append(""\n");
        sb.append("        android:layout_width="").append(RandomUtils.generateRandomSize()).append(""\n");
        sb.append("        android:layout_height="").append(RandomUtils.generateRandomSize()).append(""\n");

        if ("TextView".equals(viewType) || "Button".equals(viewType)) {
            sb.append("        android:text="").append(RandomUtils.generateRandomString(5, 20)).append(""\n");
            sb.append("        android:textSize="").append(RandomUtils.generateRandomDimension()).append(""\n");
        }

        sb.append("        android:layout_margin="").append(RandomUtils.generateRandomDimension()).append(""\n");
        sb.append("        android:padding="").append(RandomUtils.generateRandomDimension()).append(""\n");

        if (RandomUtils.randomBoolean()) {
            sb.append("        android:background="@drawable/").append(RandomUtils.generateDrawableName("shape")).append(""\n");
        }

        sb.append("        app:layout_constraintTop_toTopOf="parent"\n");
        sb.append("        app:layout_constraintStart_toStartOf="parent"\n");
        sb.append("        app:layout_constraintEnd_toEndOf="parent"\n");

        if (index > 0) {
            sb.append("        app:layout_constraintTop_toBottomOf="@id/view_").append(index - 1).append(""\n");
        }

        sb.append("        />\n\n");

        return sb.toString();
    }
}
