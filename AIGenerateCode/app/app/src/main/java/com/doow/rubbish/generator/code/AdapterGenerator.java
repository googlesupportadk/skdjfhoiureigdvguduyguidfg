package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.VariationManager;

public class AdapterGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] VIEW_TYPES = {
            "Item", "ViewHolder", "Row", "Card", "Cell",
            "View", "Element", "Node", "Entry", "Widget",
            "Component", "Block", "Section", "Part", "Piece"
    };

    private static final String[] DATA_TYPES = {
            "String", "Model", "Entity", "Object", "Item",
            "Data", "Content", "Element", "Record", "Entry",
            "Value", "Instance", "Bean", "DTO", "VO"
    };

    public AdapterGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成Adapter类");

        String uiStyle = variationManager.getVariation("ui_style");
        String asyncHandler = variationManager.getVariation("async_handler");

        for (int i = 0; i < RandomUtils.between(5, 12); i++) {
            String className = RandomUtils.generateClassName("Adapter");
            generateAdapterClass(className, uiStyle, asyncHandler);
        }
    }

    private void generateAdapterClass(String className, String uiStyle, String asyncHandler) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("adapter"));

        sb.append(generateImportStatement("android.content.Context"));
        sb.append(generateImportStatement("android.view.LayoutInflater"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.ViewGroup"));
        sb.append(generateImportStatement("android.widget.TextView"));
        sb.append(generateImportStatement("android.util.Log"));

        if (uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.card.MaterialCardView"));
        }

        sb.append(generateImportStatement("androidx.recyclerview.widget.RecyclerView"));
        sb.append(generateImportStatement("androidx.annotation.NonNull"));
        sb.append(generateImportStatement("java.util.ArrayList"));
        sb.append(generateImportStatement("java.util.List"));

        // 使用标志变量确保字段和配套方法一起生成和使用
        boolean useMultiType = RandomUtils.randomBoolean();
        boolean useAnimation = RandomUtils.randomBoolean();
        boolean useDiffUtil = RandomUtils.randomBoolean();
        boolean useHeaderFooter = RandomUtils.randomBoolean();
        boolean useSwipe = RandomUtils.randomBoolean();
        boolean useFilter = RandomUtils.randomBoolean();
        boolean useSelection = RandomUtils.randomBoolean();

        if (useMultiType) {
            sb.append(generateImportStatement("java.util.HashMap"));
            sb.append(generateImportStatement("java.util.Map"));
        }

        if (useAnimation) {
            sb.append(generateImportStatement("android.view.animation.Animation"));
            sb.append(generateImportStatement("android.view.animation.AnimationUtils"));
        }

        if (useDiffUtil) {
            sb.append(generateImportStatement("androidx.recyclerview.widget.DiffUtil"));
        }

        if (asyncHandler.contains("coroutines")) {
            sb.append(generateImportStatement("kotlinx.coroutines.CoroutineScope"));
            sb.append(generateImportStatement("kotlinx.coroutines.Dispatchers"));
        } else if (asyncHandler.contains("rxjava")) {
            sb.append(generateImportStatement("io.reactivex.rxjava3.core.Single"));
            sb.append(generateImportStatement("io.reactivex.rxjava3.schedulers.Schedulers"));
        }

        sb.append(generateImportStatement(packageName + ".model.*"));
        sb.append(generateImportStatement(packageName + ".R"));
        sb.append("\n"); // 修复多余空格，保留换行

        // 修复类定义的换行和格式
        sb.append("public class ").append(className).append(" extends RecyclerView.Adapter<").append(className).append(".ViewHolder> {\n\n");

        String viewType = VIEW_TYPES[RandomUtils.between(0, VIEW_TYPES.length - 1)];
        String dataType = DATA_TYPES[RandomUtils.between(0, DATA_TYPES.length - 1)];

        // 修复常量定义的双引号转义+格式
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String VIEW_TYPE = \"").append(viewType).append("\";\n");
        sb.append("    private static final String DATA_TYPE = \"").append(dataType).append("\";\n\n");

        // 根据标志变量生成字段
        if (useMultiType) {
            sb.append("    private Map<Integer, Integer> viewTypeMap;\n");
            sb.append("    private static final int TYPE_NORMAL = 0;\n");
            sb.append("    private static final int TYPE_SPECIAL = 1;\n\n");
        }

        if (useAnimation) {
            sb.append("    private Animation itemEnterAnimation;\n");
            sb.append("    private Animation itemExitAnimation;\n\n");
        }

        if (useDiffUtil) {
            sb.append("    private boolean useDiffUtil = true;\n\n");
        }

        if (useHeaderFooter) {
            sb.append("    private View headerView;\n");
            sb.append("    private View footerView;\n\n");
        }

        if (useSwipe) {
            sb.append("    private boolean enableSwipe = true;\n\n");
        }

        if (useFilter) {
            sb.append("    private List<").append(dataType).append("> filteredItems;\n");
            sb.append("    private boolean isFiltering = false;\n\n");
        }

        if (useSelection) {
            sb.append("    private java.util.Set<Integer> selectedPositions = new java.util.HashSet<>();\n");
            sb.append("    private boolean isSelectionMode = false;\n\n");
        }

        // 基础字段
        sb.append("    private List<").append(dataType).append("> items = new ArrayList<>();\n");
        sb.append("    private OnItemClickListener listener;\n");
        sb.append("    private Context context;\n\n");

        // 接口定义
        sb.append("    public interface OnItemClickListener {\n");
        sb.append("        void onItemClick(").append(dataType).append(" item, int position);\n");
        sb.append("        void onItemLongClick(").append(dataType).append(" item, int position);\n");
        if (useSelection) {
            sb.append("        void onSelectionChanged(int selectedCount);\n");
        }
        sb.append("    }\n\n");

        // 构造方法
        sb.append("    public ").append(className).append("(Context context) {\n");
        sb.append("        this.context = context;\n\n");

        // 根据标志变量初始化字段
        if (useMultiType) {
            sb.append("        viewTypeMap = new HashMap<>();\n");
        }

        if (useAnimation) {
            sb.append("        itemEnterAnimation = AnimationUtils.loadAnimation(context, R.anim.item_enter);\n");
            sb.append("        itemExitAnimation = AnimationUtils.loadAnimation(context, R.anim.item_exit);\n");
        }

        if (useFilter) {
            sb.append("        filteredItems = new ArrayList<>();\n");
        }

        sb.append("    }\n\n");

        // 基础方法 - setItems
        sb.append("    public void setItems(List<").append(dataType).append("> items) {\n");
        if (useDiffUtil) {
            sb.append("        if (useDiffUtil) {\n");
            sb.append("            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {\n");
            sb.append("                @Override\n");
            sb.append("                public int getOldListSize() {\n");
            sb.append("                    return ").append(className).append(".this.items.size();\n");
            sb.append("                }\n\n");
            sb.append("                @Override\n");
            sb.append("                public int getNewListSize() {\n");
            sb.append("                    return items.size();\n");
            sb.append("                }\n\n");
            sb.append("                @Override\n");
            sb.append("                public boolean areItemsTheSame(int oldPos, int newPos) {\n");
            sb.append("                    return ").append(className).append(".this.items.get(oldPos).equals(items.get(newPos));\n");
            sb.append("                }\n\n");
            sb.append("                @Override\n");
            sb.append("                public boolean areContentsTheSame(int oldPos, int newPos) {\n");
            sb.append("                    return ").append(className).append(".this.items.get(oldPos).equals(items.get(newPos));\n");
            sb.append("                }\n");
            sb.append("            });\n");
            sb.append("            this.items.clear();\n");
            sb.append("            this.items.addAll(items);\n");
            sb.append("            result.dispatchUpdatesTo(this);\n");
            sb.append("        } else {\n");
            sb.append("            this.items.clear();\n");
            sb.append("            this.items.addAll(items);\n");
            sb.append("            notifyDataSetChanged();\n");
            sb.append("        }\n");
        } else {
            sb.append("        this.items.clear();\n");
            sb.append("        this.items.addAll(items);\n");
            sb.append("        notifyDataSetChanged();\n");
        }
        sb.append("    }\n\n");

        // addItem方法
        sb.append("    public void addItem(").append(dataType).append(" item) {\n");
        sb.append("        items.add(item);\n");
        sb.append("        notifyItemInserted(items.size() - 1);\n");
        sb.append("    }\n\n");

        // removeItem方法
        sb.append("    public void removeItem(int position) {\n");
        sb.append("        if (position >= 0 && position < items.size()) {\n");
        sb.append("            items.remove(position);\n");
        sb.append("            notifyItemRemoved(position);\n");
        if (useSelection) {
            sb.append("            updateSelection();\n");
        }
        sb.append("        }\n");
        sb.append("    }\n\n");

        // updateItem方法
        sb.append("    public void updateItem(int position, ").append(dataType).append(" item) {\n");
        sb.append("        if (position >= 0 && position < items.size()) {\n");
        sb.append("            items.set(position, item);\n");
        sb.append("            notifyItemChanged(position);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // clearItems方法
        sb.append("    public void clearItems() {\n");
        sb.append("        items.clear();\n");
        sb.append("        notifyDataSetChanged();\n");
        if (useSelection) {
            sb.append("        selectedPositions.clear();\n");
            sb.append("        updateSelection();\n");
        }
        sb.append("    }\n\n");

        // setOnItemClickListener方法
        sb.append("    public void setOnItemClickListener(OnItemClickListener listener) {\n");
        sb.append("        this.listener = listener;\n");
        sb.append("    }\n\n");

        // 根据标志变量生成配套方法
        if (useMultiType) {
            generateMultiTypeMethods(sb, dataType);
        }

        if (useAnimation) {
            generateAnimationMethods(sb);
        }

        if (useHeaderFooter) {
            generateHeaderFooterMethods(sb);
        }

        if (useSwipe) {
            generateSwipeMethods(sb, dataType);
        }

        if (useFilter) {
            generateFilterMethods(sb, dataType);
        }

        if (useSelection) {
            generateSelectionMethods(sb, dataType);
        }

        // getItemViewType方法
        sb.append("    @Override\n");
        sb.append("    public int getItemViewType(int position) {\n");
        if (useHeaderFooter) {
            sb.append("        if (headerView != null && position == 0) {\n");
            sb.append("            return TYPE_HEADER;\n");
            sb.append("        }\n");
            sb.append("        if (footerView != null && position == getItemCount() - 1) {\n");
            sb.append("            return TYPE_FOOTER;\n");
            sb.append("        }\n");
        }
        if (useMultiType) {
            sb.append("        int adjustedPosition = ").append(useHeaderFooter ? "getAdjustedPosition(position)" : "position").append(";\n");
            sb.append("        if (viewTypeMap != null && viewTypeMap.containsKey(adjustedPosition)) {\n");
            sb.append("            return viewTypeMap.get(adjustedPosition);\n");
            sb.append("        }\n");
        }
        sb.append("        return TYPE_NORMAL;\n");
        sb.append("    }\n\n");

        // onCreateViewHolder方法
        sb.append("    @NonNull\n");
        sb.append("    @Override\n");
        sb.append("    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {\n");
        sb.append("        View view;\n");
        if (useHeaderFooter) {
            sb.append("        if (viewType == TYPE_HEADER && headerView != null) {\n");
            sb.append("            return new ViewHolder(headerView);\n");
            sb.append("        }\n");
            sb.append("        if (viewType == TYPE_FOOTER && footerView != null) {\n");
            sb.append("            return new ViewHolder(footerView);\n");
            sb.append("        }\n");
        }
        sb.append("        view = LayoutInflater.from(parent.getContext())\n");
        sb.append("                .inflate(R.layout.item_").append(viewType.toLowerCase()).append(", parent, false);\n");
        sb.append("        return new ViewHolder(view);\n");
        sb.append("    }\n\n");

        // onBindViewHolder方法
        sb.append("    @Override\n");
        sb.append("    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {\n");
        if (useHeaderFooter) {
            sb.append("        if (headerView != null && position == 0) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append("        if (footerView != null && position == getItemCount() - 1) {\n");
            sb.append("            return;\n");
            sb.append("        }\n");
        }
        sb.append("        int adjustedPosition = ").append(useHeaderFooter ? "getAdjustedPosition(position)" : "position").append(";\n");
        sb.append("        ").append(dataType).append(" item = items.get(adjustedPosition);\n");
        sb.append("        holder.bind(item, adjustedPosition);\n");
        if (useAnimation) {
            sb.append("        applyAnimation(holder.itemView, true);\n");
        }
        if (useSelection) {
            sb.append("        holder.itemView.setSelected(selectedPositions.contains(adjustedPosition));\n");
        }
        sb.append("    }\n\n");

        // getItemCount方法 - 修复三元表达式语法错误
        sb.append("    @Override\n");
        sb.append("    public int getItemCount() {\n");
        sb.append("        int count = ").append(useFilter ? "(isFiltering ? filteredItems.size() : items.size())" : "items.size()").append(";\n");
        if (useHeaderFooter) {
            sb.append("        if (headerView != null) count++;\n");
            sb.append("        if (footerView != null) count++;\n");
        }
        sb.append("        return count;\n");
        sb.append("    }\n\n");

        if (useHeaderFooter) {
            sb.append("    private int getAdjustedPosition(int position) {\n");
            sb.append("        int adjusted = position;\n");
            sb.append("        if (headerView != null && position > 0) {\n");
            sb.append("            adjusted--;\n");
            sb.append("        }\n");
            sb.append("        return adjusted;\n");
            sb.append("    }\n\n");
        }

        // ViewHolder类
        sb.append("    public static class ViewHolder extends RecyclerView.ViewHolder {\n");
        sb.append("        private TextView textView;\n\n");
        sb.append("        public ViewHolder(View itemView) {\n");
        sb.append("            super(itemView);\n");
        sb.append("            textView = itemView.findViewById(R.id.text_view);\n");
        sb.append("        }\n\n");
        sb.append("        public void bind(").append(dataType).append(" item, int position) {\n");
        sb.append("            textView.setText(item.toString());\n");
        sb.append("            itemView.setOnClickListener(v -> {\n");
        sb.append("                if (listener != null) {\n");
        sb.append("                    listener.onItemClick(item, position);\n");
        sb.append("                }\n");
        sb.append("            });\n");
        sb.append("            itemView.setOnLongClickListener(v -> {\n");
        sb.append("                if (listener != null) {\n");
        sb.append("                    listener.onItemLongClick(item, position);\n");
        sb.append("                }\n");
        sb.append("                return true;\n");
        sb.append("            });\n");
        sb.append("        }\n");
        sb.append("    }\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "adapter");
    }

    private void generateMultiTypeMethods(StringBuilder sb, String dataType) {
        sb.append("    public void setItemType(int position, int viewType) {\n");
        sb.append("        if (viewTypeMap != null) {\n");
        sb.append("            viewTypeMap.put(position, viewType);\n");
        sb.append("            notifyItemChanged(position);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void clearItemTypes() {\n");
        sb.append("        if (viewTypeMap != null) {\n");
        sb.append("            viewTypeMap.clear();\n");
        sb.append("            notifyDataSetChanged();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateAnimationMethods(StringBuilder sb) {
        sb.append("    public void applyAnimation(View view, boolean enter) {\n");
        sb.append("        if (enter && itemEnterAnimation != null) {\n");
        sb.append("            view.startAnimation(itemEnterAnimation);\n");
        sb.append("        } else if (!enter && itemExitAnimation != null) {\n");
        sb.append("            view.startAnimation(itemExitAnimation);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void setAnimations(int enterAnim, int exitAnim) {\n");
        sb.append("        itemEnterAnimation = AnimationUtils.loadAnimation(context, enterAnim);\n");
        sb.append("        itemExitAnimation = AnimationUtils.loadAnimation(context, exitAnim);\n");
        sb.append("    }\n\n");
    }

    private void generateHeaderFooterMethods(StringBuilder sb) {
        sb.append("    private static final int TYPE_HEADER = -1;\n");
        sb.append("    private static final int TYPE_FOOTER = -2;\n\n");

        sb.append("    public void setHeader(View header) {\n");
        sb.append("        this.headerView = header;\n");
        sb.append("        notifyItemInserted(0);\n");
        sb.append("    }\n\n");

        sb.append("    public void setFooter(View footer) {\n");
        sb.append("        this.footerView = footer;\n");
        sb.append("        notifyItemInserted(getItemCount());\n");
        sb.append("    }\n\n");

        sb.append("    public void removeHeader() {\n");
        sb.append("        if (headerView != null) {\n");
        sb.append("            notifyItemRemoved(0);\n");
        sb.append("            headerView = null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void removeFooter() {\n");
        sb.append("        if (footerView != null) {\n");
        sb.append("            int position = getItemCount() - 1;\n");
        sb.append("            notifyItemRemoved(position);\n");
        sb.append("            footerView = null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateSwipeMethods(StringBuilder sb, String dataType) {
        sb.append("    public void swipeItem(int position) {\n");
        sb.append("        if (enableSwipe && position >= 0 && position < items.size()) {\n");
        sb.append("            items.remove(position);\n");
        sb.append("            notifyItemRemoved(position);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public void setSwipeEnabled(boolean enabled) {\n");
        sb.append("        this.enableSwipe = enabled;\n");
        sb.append("    }\n\n");
    }

    private void generateFilterMethods(StringBuilder sb, String dataType) {
        sb.append("    public void filterItems(String query) {\n");
        sb.append("        if (query == null || query.isEmpty()) {\n");
        sb.append("            isFiltering = false;\n");
        sb.append("            notifyDataSetChanged();\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append("        isFiltering = true;\n");
        sb.append("        filteredItems.clear();\n");
        sb.append("        for (").append(dataType).append(" item : items) {\n");
        sb.append("            if (item.toString().toLowerCase().contains(query.toLowerCase())) {\n");
        sb.append("                filteredItems.add(item);\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        notifyDataSetChanged();\n");
        sb.append("    }\n\n");

        sb.append("    public void clearFilter() {\n");
        sb.append("        isFiltering = false;\n");
        sb.append("        notifyDataSetChanged();\n");
        sb.append("    }\n\n");
    }

    private void generateSelectionMethods(StringBuilder sb, String dataType) {
        sb.append("    public void toggleSelection(int position) {\n");
        sb.append("        if (selectedPositions.contains(position)) {\n");
        sb.append("            selectedPositions.remove(position);\n");
        sb.append("        } else {\n");
        sb.append("            selectedPositions.add(position);\n");
        sb.append("        }\n");
        sb.append("        notifyItemChanged(position);\n");
        sb.append("        updateSelection();\n");
        sb.append("    }\n\n");

        sb.append("    public void clearSelection() {\n");
        sb.append("        selectedPositions.clear();\n");
        sb.append("        notifyDataSetChanged();\n");
        sb.append("        updateSelection();\n");
        sb.append("    }\n\n");

        sb.append("    public void selectAll() {\n");
        sb.append("        selectedPositions.clear();\n");
        sb.append("        for (int i = 0; i < items.size(); i++) {\n");
        sb.append("            selectedPositions.add(i);\n");
        sb.append("        }\n");
        sb.append("        notifyDataSetChanged();\n");
        sb.append("        updateSelection();\n");
        sb.append("    }\n\n");

        sb.append("    public void setSelectionMode(boolean enabled) {\n");
        sb.append("        this.isSelectionMode = enabled;\n");
        sb.append("        if (!enabled) {\n");
        sb.append("            clearSelection();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private void updateSelection() {\n");
        sb.append("        if (listener != null) {\n");
        sb.append("            listener.onSelectionChanged(selectedPositions.size());\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    public int getSelectedCount() {\n");
        sb.append("        return selectedPositions.size();\n");
        sb.append("    }\n\n");
    }
}