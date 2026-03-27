package com.doow.rubbish.generator.code;

import com.doow.rubbish.generator.ClassTracker;
import com.doow.rubbish.generator.GeneratorConfig;
import com.doow.rubbish.generator.RandomUtils;
import com.doow.rubbish.generator.ResourceTracker;
import com.doow.rubbish.generator.VariationManager;

public class FragmentGenerator extends BaseCodeGenerator {

    protected VariationManager variationManager;

    private static final String[] VIEW_TYPES = {
            "list", "grid", "detail", "form", "dashboard",
            "profile", "settings", "search", "gallery", "chart"
    };

    private static final String[] DATA_SOURCES = {
            "repository", "database", "network", "cache", "local", "remote"
    };

    private static final String[] ANIMATION_TYPES = {
            "fade", "slide", "scale", "flip", "rotate", "bounce"
    };

    public FragmentGenerator(String projectRoot, String packageName) {
        super(projectRoot, packageName);
        this.variationManager = VariationManager.getInstance();
    }

    @Override
    public void generateAll() throws Exception {
        System.out.println("生成 " + GeneratorConfig.fragmentCount + " 个Fragment类");

        String architecture = variationManager.getVariation("architecture");
        String uiStyle = variationManager.getVariation("ui_style");
        String navigationMode = variationManager.getVariation("navigation_mode");

        for (int i = 0; i < GeneratorConfig.fragmentCount; i++) {
            String className = RandomUtils.generateClassName("Fragment");
            ClassTracker.addFragment(className);
            generateFragment(className, architecture, uiStyle, navigationMode);
        }
    }

    private void generateFragment(String className, String architecture,
                                  String uiStyle, String navigationMode) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(generatePackageDeclaration("ui.fragment"));

        // 基础导入
        sb.append(generateImportStatement("android.os.Bundle"));
        sb.append(generateImportStatement("android.util.Log"));
        sb.append(generateImportStatement("android.view.LayoutInflater"));
        sb.append(generateImportStatement("android.view.View"));
        sb.append(generateImportStatement("android.view.ViewGroup"));
        sb.append(generateImportStatement("android.widget.Button"));
        sb.append(generateImportStatement("android.widget.TextView"));
        sb.append(generateImportStatement("android.widget.ImageView"));
        sb.append(generateImportStatement("android.widget.EditText"));
        sb.append(generateImportStatement("android.widget.ProgressBar"));
        sb.append(generateImportStatement("android.widget.ScrollView"));
        sb.append(generateImportStatement("android.widget.LinearLayout"));
        sb.append(generateImportStatement("android.widget.RelativeLayout"));
        sb.append(generateImportStatement("androidx.fragment.app.Fragment"));
        sb.append(generateImportStatement("androidx.lifecycle.ViewModelProvider"));
        sb.append(generateImportStatement("androidx.lifecycle.Observer"));
        sb.append(generateImportStatement("androidx.recyclerview.widget.RecyclerView"));
        sb.append(generateImportStatement("androidx.swiperefreshlayout.widget.SwipeRefreshLayout"));
        sb.append(generateImportStatement("java.util.HashMap"));
        sb.append(generateImportStatement("java.util.Map"));

        // 架构相关导入
        if (architecture.contains("mvvm")) {
            sb.append(generateImportStatement("androidx.lifecycle.ViewModel"));
            sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
            sb.append(generateImportStatement("androidx.lifecycle.MutableLiveData"));
        } else if (architecture.contains("mvp")) {
            sb.append(generateImportStatement("android.content.Context"));
        } else if (architecture.contains("mvi")) {
            sb.append(generateImportStatement("androidx.lifecycle.ViewModel"));
            sb.append(generateImportStatement("androidx.lifecycle.LiveData"));
        }

        // UI风格相关导入
        if (uiStyle.contains("material")) {
            sb.append(generateImportStatement("com.google.android.material.appbar.MaterialToolbar"));
            sb.append(generateImportStatement("com.google.android.material.button.MaterialButton"));
            sb.append(generateImportStatement("com.google.android.material.textfield.TextInputLayout"));
        } else if (uiStyle.contains("glassmorphism")) {
            sb.append(generateImportStatement("android.graphics.BlurMaskFilter"));
            sb.append(generateImportStatement("android.graphics.PorterDuff"));
        }

        // 导航相关导入
        if (navigationMode.contains("navigation")) {
            sb.append(generateImportStatement("androidx.navigation.fragment.NavHostFragment"));
            sb.append(generateImportStatement("androidx.navigation.NavController"));
        }

        sb.append(generateImportStatement(packageName + ".ui.*"));
        sb.append(generateImportStatement(packageName + ".viewmodel.*"));
        sb.append(generateImportStatement(packageName + ".manager.*"));
        sb.append(generateImportStatement(packageName + ".helper.*"));
        sb.append(generateImportStatement(packageName + ".adapter.*"));
        sb.append(generateImportStatement(packageName + ".R"));
        sb.append("\n");

        // 功能标志 - 确保所有字段和方法都会被使用
        boolean useViewModel = RandomUtils.randomBoolean();
        boolean useManager = RandomUtils.randomBoolean();
        boolean useHelper = RandomUtils.randomBoolean();
        boolean useDataBinding = RandomUtils.randomBoolean();
        boolean useViewBinding = RandomUtils.randomBoolean();
        boolean useArguments = RandomUtils.randomBoolean();
        boolean useLifecycle = RandomUtils.randomBoolean();
        boolean useMenu = RandomUtils.randomBoolean();
        boolean useAnimation = RandomUtils.randomBoolean();
        boolean useStateSave = RandomUtils.randomBoolean();
        boolean useRecyclerView = RandomUtils.randomBoolean();
        boolean useSwipeRefresh = RandomUtils.randomBoolean();
        boolean useToolbar = RandomUtils.randomBoolean();
        boolean useSearch = RandomUtils.randomBoolean();
        boolean useFilter = RandomUtils.randomBoolean();
        boolean useSort = RandomUtils.randomBoolean();

        String viewType = VIEW_TYPES[RandomUtils.between(0, VIEW_TYPES.length - 1)];
        String dataSource = DATA_SOURCES[RandomUtils.between(0, DATA_SOURCES.length - 1)];
        String animationType = useAnimation ? ANIMATION_TYPES[RandomUtils.between(0, ANIMATION_TYPES.length - 1)] : "";

        sb.append("public class ").append(className).append(" extends Fragment {\n\n");

        // 基础常量
        sb.append("    private static final String TAG = \"").append(className).append("\";\n");
        sb.append("    private static final String VIEW_TYPE = \"").append(viewType).append("\";\n");
        sb.append("    private static final String DATA_SOURCE = \"").append(dataSource).append("\";\n");

        if (useAnimation) {
            sb.append("    private static final String ANIMATION_TYPE = \"").append(animationType).append("\";\n");
        }

        if (useStateSave) {
            sb.append("    private static final String KEY_STATE = \"").append(className).append("_STATE\";\n");
        }

        sb.append("\n");

        // ViewModel字段
        if (useViewModel) {
            String viewModelClass = ClassTracker.getRandomViewModel();
            if (viewModelClass != null) {
                sb.append("    private ").append(viewModelClass).append(" viewModel;\n\n");
            }
        }

        // Manager字段
        if (useManager) {
            String managerClass = ClassTracker.getRandomManager();
            if (managerClass != null) {
                sb.append("    private ").append(managerClass).append(" manager;\n\n");
            }
        }

        // Helper字段
        if (useHelper) {
            String helperClass = ClassTracker.getRandomHelper();
            if (helperClass != null) {
                sb.append("    private ").append(helperClass).append(" helper;\n\n");
            }
        }

        // ViewBinding字段
        if (useViewBinding) {
            String layoutName = ResourceTracker.getRandomLayout();
            if (layoutName != null) {
                sb.append("    private ").append(layoutName).append("Binding binding;\n\n");
            }
        }

        // RecyclerView相关字段
        if (useRecyclerView) {
            sb.append("    private RecyclerView recyclerView;\n");
            String adapterClass = ClassTracker.getRandomAdapter();
            if (adapterClass != null) {
                sb.append("    private ").append(adapterClass).append(" adapter;\n");
            }
            sb.append("\n");
        }

        // SwipeRefreshLayout字段
        if (useSwipeRefresh) {
            sb.append("    private SwipeRefreshLayout swipeRefreshLayout;\n\n");
        }

        // Toolbar字段
        if (useToolbar) {
            sb.append("    private MaterialToolbar toolbar;\n\n");
        }

        // 搜索相关字段
        if (useSearch) {
            sb.append("    private EditText searchEditText;\n");
            sb.append("    private String searchQuery = \"\";\n\n");
        }

        // 过滤和排序相关字段
        if (useFilter || useSort) {
            sb.append("    private Map<String, Object> filterMap = new HashMap<>();\n");
            sb.append("    private String sortField = \"\";\n");
            sb.append("    private boolean sortAscending = true;\n\n");
        }

        // 状态保存字段
        if (useStateSave) {
            sb.append("    private Bundle savedState;\n\n");
        }

        // 动画字段
        if (useAnimation) {
            sb.append("    private android.view.animation.Animation enterAnimation;\n");
            sb.append("    private android.view.animation.Animation exitAnimation;\n\n");
        }

        // 菜单相关字段
        if (useMenu) {
            sb.append("    private android.view.MenuItem searchMenuItem;\n\n");
        }

        // 构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("        // Required empty public constructor\n");
        sb.append("    }\n\n");

        // 带参数的构造函数
        if (useArguments) {
            sb.append("    public static ").append(className).append(" newInstance(Bundle args) {\n");
            sb.append("        ").append(className).append(" fragment = new ").append(className).append("();\n");
            sb.append("        fragment.setArguments(args);\n");
            sb.append("        return fragment;\n");
            sb.append("    }\n\n");
        }

        // onCreate方法
        sb.append("    @Override\n");
        sb.append("    public void onCreate(Bundle savedInstanceState) {\n");
        sb.append("        super.onCreate(savedInstanceState);\n");
        sb.append("        Log.d(TAG, \"onCreate\");\n");

        if (useArguments) {
            sb.append("        handleArguments();\n");
        }

        if (useStateSave) {
            sb.append("        restoreState(savedInstanceState);\n");
        }

        if (useMenu) {
            sb.append("        setHasOptionsMenu(true);\n");
        }

        sb.append("    }\n\n");

        // onCreateView方法
        sb.append("    @Override\n");
        sb.append("    public View onCreateView(LayoutInflater inflater, ViewGroup container,\n");
        sb.append("                             Bundle savedInstanceState) {\n");
        sb.append("        Log.d(TAG, \"onCreateView\");\n");

        if (useViewBinding) {
            sb.append("        binding = ").append(ResourceTracker.getRandomLayout() != null ?
                    ResourceTracker.getRandomLayout() : "fragment_layout").append("Binding.inflate(inflater, container, false);\n");
            sb.append("        View rootView = binding.getRoot();\n");
        } else {
            sb.append("        View rootView = inflater.inflate(R.layout.").append(
                    ResourceTracker.getRandomLayout() != null ? ResourceTracker.getRandomLayout() : "fragment_layout").append(", container, false);\n");
        }

        sb.append("        initializeViews(rootView);\n");
        sb.append("        setupViewModel();\n");
        sb.append("        setupListeners();\n");
        sb.append("        loadData();\n");
        sb.append("        return rootView;\n");
        sb.append("    }\n\n");

        // onViewCreated方法
        sb.append("    @Override\n");
        sb.append("    public void onViewCreated(View view, Bundle savedInstanceState) {\n");
        sb.append("        super.onViewCreated(view, savedInstanceState);\n");
        sb.append("        Log.d(TAG, \"onViewCreated\");\n");

        if (useAnimation) {
            sb.append("        setupAnimations();\n");
            sb.append("        playEnterAnimation();\n");
        }

        sb.append("    }\n\n");

        // 初始化视图方法
        sb.append("    private void initializeViews(View rootView) {\n");

        if (useRecyclerView) {
            sb.append("        recyclerView = rootView.findViewById(R.id.recycler_view);\n");
            sb.append("        setupRecyclerView();\n");
        }

        if (useSwipeRefresh) {
            sb.append("        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);\n");
            sb.append("        setupSwipeRefresh();\n");
        }

        if (useToolbar) {
            sb.append("        toolbar = rootView.findViewById(R.id.toolbar);\n");
            sb.append("        setupToolbar();\n");
        }

        if (useSearch) {
            sb.append("        searchEditText = rootView.findViewById(R.id.search_edit_text);\n");
            sb.append("        setupSearch();\n");
        }

        sb.append("    }\n\n");

        // 设置ViewModel方法
        if (useViewModel) {
            sb.append("    private void setupViewModel() {\n");
            sb.append("        viewModel = new ViewModelProvider(this).get(").append(
                    ClassTracker.getRandomViewModel() != null ? ClassTracker.getRandomViewModel() : "ViewModel").append(".class);\n");

            if (architecture.contains("mvvm") || architecture.contains("mvi")) {
                sb.append("        observeData();\n");
            }

            sb.append("    }\n\n");
        }

        // 设置监听器方法
        sb.append("    private void setupListeners() {\n");

        if (useSwipeRefresh) {
            sb.append("        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);\n");
        }

        if (useSearch) {
            sb.append("        searchEditText.addTextChangedListener(new android.text.TextWatcher() {\n");
            sb.append("            @Override\n");
            sb.append("            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}\n\n");
            sb.append("            @Override\n");
            sb.append("            public void onTextChanged(CharSequence s, int start, int before, int count) {\n");
            sb.append("                searchQuery = s.toString();\n");
            sb.append("                performSearch();\n");
            sb.append("            }\n\n");
            sb.append("            @Override\n");
            sb.append("            public void afterTextChanged(android.text.Editable s) {}\n");
            sb.append("        });\n");
        }

        if (useToolbar) {
            sb.append("        toolbar.setNavigationOnClickListener(v -> {\n");
            sb.append("            onNavigationClick();\n");
            sb.append("        });\n");
        }

        if (useFilter) {
            sb.append("        setupFilterListeners();\n");
        }

        if (useSort) {
            sb.append("        setupSortListeners();\n");
        }

        sb.append("    }\n\n");

        // 加载数据方法
        sb.append("    private void loadData() {\n");
        sb.append("        Log.d(TAG, \"Loading data from: \" + DATA_SOURCE);\n");

        if (useViewModel) {
            sb.append("        if (viewModel != null) {\n");
            sb.append("            viewModel.loadData();\n");
            sb.append("        }\n");
        }

        if (useManager) {
            sb.append("        if (manager != null) {\n");
            sb.append("            manager.loadData();\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // RecyclerView设置方法
        if (useRecyclerView) {
            sb.append("    private void setupRecyclerView() {\n");
            sb.append("        recyclerView.setLayoutManager(");

            if (viewType.contains("grid")) {
                sb.append("new androidx.recyclerview.widget.GridLayoutManager(\n");
                sb.append("            getContext(),\n");
                sb.append("            getResources().getInteger(R.integer.grid_span_count)\n");
                sb.append("        ));\n");
            } else {
                sb.append("new androidx.recyclerview.widget.LinearLayoutManager(getContext()));\n");
            }

            sb.append("        \n");
            String adapterClass = ClassTracker.getRandomAdapter();
            if (adapterClass != null) {
                sb.append("        adapter = new ").append(adapterClass).append("();\n");
                sb.append("        recyclerView.setAdapter(adapter);\n");
            }

            sb.append("    }\n\n");
        }

        // SwipeRefresh设置方法
        if (useSwipeRefresh) {
            sb.append("    private void setupSwipeRefresh() {\n");
            sb.append("        swipeRefreshLayout.setColorSchemeResources(\n");
            sb.append("            R.color.colorPrimary,\n");
            sb.append("            R.color.colorAccent,\n");
            sb.append("            R.color.colorPrimaryDark\n");
            sb.append("        );\n");
            sb.append("    }\n\n");
        }

        // Toolbar设置方法
        if (useToolbar) {
            sb.append("    private void setupToolbar() {\n");
            sb.append("        toolbar.setTitle(VIEW_TYPE);\n");

            if (uiStyle.contains("material")) {
                sb.append("        toolbar.setNavigationIcon(R.drawable.ic_back);\n");
            }

            sb.append("    }\n\n");
        }

        // 搜索设置方法
        if (useSearch) {
            sb.append("    private void setupSearch() {\n");
            sb.append("        searchEditText.setHint(R.string.search_hint);\n");
            sb.append("    }\n\n");

            sb.append("    private void performSearch() {\n");
            sb.append("        Log.d(TAG, \"Searching for: \" + searchQuery);\n");

            if (useViewModel) {
                sb.append("        if (viewModel != null) {\n");
                sb.append("            viewModel.search(searchQuery);\n");
                sb.append("        }\n");
            }

            sb.append("    }\n\n");
        }

        // 过滤设置方法
        if (useFilter) {
            sb.append("    private void setupFilterListeners() {\n");
            sb.append("        // Setup filter UI and listeners\n");
            sb.append("    }\n\n");

            sb.append("    private void applyFilter() {\n");
            sb.append("        Log.d(TAG, \"Applying filters\");\n");
            sb.append("        filterMap.forEach((key, value) -> {\n");
            sb.append("            Log.d(TAG, \"Filter - \" + key + \": \" + value);\n");
            sb.append("        });\n");

            if (useViewModel) {
                sb.append("        if (viewModel != null) {\n");
                sb.append("            viewModel.applyFilter(filterMap);\n");
                sb.append("        }\n");
            }

            sb.append("    }\n\n");
        }

        // 排序设置方法
        if (useSort) {
            sb.append("    private void setupSortListeners() {\n");
            sb.append("        // Setup sort UI and listeners\n");
            sb.append("    }\n\n");

            sb.append("    private void applySort() {\n");
            sb.append("        Log.d(TAG, \"Sorting by: \" + sortField + \", Ascending: \" + sortAscending);\n");

            if (useViewModel) {
                sb.append("        if (viewModel != null) {\n");
                sb.append("            viewModel.sort(sortField, sortAscending);\n");
                sb.append("        }\n");
            }

            sb.append("    }\n\n");
        }

        // 观察数据方法
        if (useViewModel && (architecture.contains("mvvm") || architecture.contains("mvi"))) {
            sb.append("    private void observeData() {\n");
            sb.append("        if (viewModel == null) return;\n\n");
            sb.append("        viewModel.getData().observe(getViewLifecycleOwner(), data -> {\n");
            sb.append("            Log.d(TAG, \"Data observed: \" + data);\n");

            if (useRecyclerView) {
                sb.append("            if (adapter != null) {\n");
                sb.append("                adapter.setData(data);\n");
                sb.append("            }\n");
            }

            sb.append("        });\n\n");

            sb.append("        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {\n");
            sb.append("            if (swipeRefreshLayout != null) {\n");
            sb.append("                swipeRefreshLayout.setRefreshing(loading);\n");
            sb.append("            }\n");
            sb.append("        });\n\n");

            sb.append("        viewModel.getError().observe(getViewLifecycleOwner(), error -> {\n");
            sb.append("            if (error != null) {\n");
            sb.append("                handleError(error);\n");
            sb.append("            }\n");
            sb.append("        });\n");
            sb.append("    }\n\n");
        }

        // 刷新方法
        if (useSwipeRefresh) {
            sb.append("    private void onRefresh() {\n");
            sb.append("        Log.d(TAG, \"Refreshing data\");\n");
            sb.append("        loadData();\n");
            sb.append("    }\n\n");
        }

        // 导航点击方法
        if (useToolbar) {
            sb.append("    private void onNavigationClick() {\n");
            sb.append("        Log.d(TAG, \"Navigation clicked\");\n");

            if (navigationMode.contains("navigation")) {
                sb.append("        NavController navController = null;\n");
                sb.append("        try {\n");
                sb.append("            navController = NavHostFragment.findNavController(this);\n");
                sb.append("            navController.navigateUp();\n");
                sb.append("        } catch (Exception e) {\n");
                sb.append("            Log.e(TAG, \"Navigation error\", e);\n");
                sb.append("        }\n");
            } else {
                sb.append("        if (getActivity() != null) {\n");
                sb.append("            getActivity().onBackPressed();\n");
                sb.append("        }\n");
            }

            sb.append("    }\n\n");
        }

        // 处理参数方法
        if (useArguments) {
            sb.append("    private void handleArguments() {\n");
            sb.append("        Bundle args = getArguments();\n");
            sb.append("        if (args != null) {\n");
            sb.append("            Log.d(TAG, \"Handling arguments\");\n");
            sb.append("            // Process arguments as needed\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 状态保存方法
        if (useStateSave) {
            sb.append("    private void restoreState(Bundle savedInstanceState) {\n");
            sb.append("        if (savedInstanceState != null) {\n");
            sb.append("            savedState = savedInstanceState.getBundle(KEY_STATE);\n");
            sb.append("            if (savedState != null) {\n");
            sb.append("                Log.d(TAG, \"State restored\");\n");
            sb.append("                // Restore state as needed\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    @Override\n");
            sb.append("    public void onSaveInstanceState(Bundle outState) {\n");
            sb.append("        super.onSaveInstanceState(outState);\n");
            sb.append("        Log.d(TAG, \"Saving state\");\n");

            if (useSearch) {
                sb.append("        outState.putString(\"search_query\", searchQuery);\n");
            }

            if (useFilter) {
                sb.append("        outState.putSerializable(\"filter_map\", new HashMap<>(filterMap));\n");
            }

            sb.append("    }\n\n");
        }

        // 动画方法
        if (useAnimation) {
            sb.append("    private void setupAnimations() {\n");
            sb.append("        switch (ANIMATION_TYPE) {\n");
            sb.append("            case \"fade\":\n");
            sb.append("                enterAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.fade_in\n");
            sb.append("                );\n");
            sb.append("                exitAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.fade_out\n");
            sb.append("                );\n");
            sb.append("                break;\n");
            sb.append("            case \"slide\":\n");
            sb.append("                enterAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.slide_in_right\n");
            sb.append("                );\n");
            sb.append("                exitAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.slide_out_right\n");
            sb.append("                );\n");
            sb.append("                break;\n");
            sb.append("            case \"scale\":\n");
            sb.append("                enterAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.scale_in\n");
            sb.append("                );\n");
            sb.append("                exitAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.scale_out\n");
            sb.append("                );\n");
            sb.append("                break;\n");
            sb.append("            default:\n");
            sb.append("                enterAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.fade_in\n");
            sb.append("                );\n");
            sb.append("                exitAnimation = android.view.animation.AnimationUtils.loadAnimation(\n");
            sb.append("                    getContext(), R.anim.fade_out\n");
            sb.append("                );\n");
            sb.append("                break;\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    private void playEnterAnimation() {\n");
            sb.append("        if (getView() != null && enterAnimation != null) {\n");
            sb.append("            getView().startAnimation(enterAnimation);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            sb.append("    private void playExitAnimation() {\n");
            sb.append("        if (getView() != null && exitAnimation != null) {\n");
            sb.append("            getView().startAnimation(exitAnimation);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 错误处理方法
        sb.append("    private void handleError(Throwable error) {\n");
        sb.append("        Log.e(TAG, \"Error occurred\", error);\n");

        if (useHelper) {
            sb.append("        if (helper != null) {\n");
            sb.append("            helper.handleError(error);\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // 菜单创建方法
        if (useMenu) {
            sb.append("    @Override\n");
            sb.append("    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {\n");
            sb.append("        super.onCreateOptionsMenu(menu, inflater);\n");
            sb.append("        inflater.inflate(R.menu.fragment_menu, menu);\n");

            if (useSearch) {
                sb.append("        searchMenuItem = menu.findItem(R.id.action_search);\n");
                sb.append("        setupSearchView(searchMenuItem);\n");
            }

            sb.append("    }\n\n");
        }

        // 菜单项选择方法
        if (useMenu) {
            sb.append("    @Override\n");
            sb.append("    public boolean onOptionsItemSelected(android.view.MenuItem item) {\n");
            sb.append("        int id = item.getItemId();\n");
            sb.append("        switch (id) {\n");

            if (useSearch) {
                sb.append("            case R.id.action_search:\n");
                sb.append("                onSearchAction();\n");
                sb.append("                return true;\n");
            }

            sb.append("            case R.id.action_refresh:\n");
            sb.append("                onRefresh();\n");
            sb.append("                return true;\n");
            sb.append("            case R.id.action_filter:\n");

            if (useFilter) {
                sb.append("                applyFilter();\n");
            }

            sb.append("                return true;\n");
            sb.append("            case R.id.action_sort:\n");

            if (useSort) {
                sb.append("                applySort();\n");
            }

            sb.append("                return true;\n");
            sb.append("            default:\n");
            sb.append("                return super.onOptionsItemSelected(item);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 搜索视图设置方法
        if (useMenu && useSearch) {
            sb.append("    private void setupSearchView(android.view.MenuItem searchItem) {\n");
            sb.append("        if (searchItem != null) {\n");
            sb.append("            android.widget.SearchView searchView = (android.widget.SearchView) searchItem.getActionView();\n");
            sb.append("            if (searchView != null) {\n");
            sb.append("                searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {\n");
            sb.append("                    @Override\n");
            sb.append("                    public boolean onQueryTextSubmit(String query) {\n");
            sb.append("                        searchQuery = query;\n");
            sb.append("                        performSearch();\n");
            sb.append("                        return true;\n");
            sb.append("                    }\n\n");
            sb.append("                    @Override\n");
            sb.append("                    public boolean onQueryTextChange(String newText) {\n");
            sb.append("                        searchQuery = newText;\n");
            sb.append("                        performSearch();\n");
            sb.append("                        return true;\n");
            sb.append("                    }\n");
            sb.append("                });\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 搜索操作方法
        if (useMenu && useSearch) {
            sb.append("    private void onSearchAction() {\n");
            sb.append("        if (searchEditText != null) {\n");
            sb.append("            searchEditText.requestFocus();\n");
            sb.append("            android.view.inputmethod.InputMethodManager imm =\n");
            sb.append("                (android.view.inputmethod.InputMethodManager) getContext().\n");
            sb.append("                    getSystemService(Context.INPUT_METHOD_SERVICE);\n");
            sb.append("            if (imm != null) {\n");
            sb.append("                imm.showSoftInput(searchEditText, 0);\n");
            sb.append("            }\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // 销毁视图方法
        sb.append("    @Override\n");
        sb.append("    public void onDestroyView() {\n");
        sb.append("        super.onDestroyView();\n");
        sb.append("        Log.d(TAG, \"onDestroyView\");\n");

        if (useViewBinding) {
            sb.append("        if (binding != null) {\n");
            sb.append("            binding.unbind();\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");

        // 销毁方法
        sb.append("    @Override\n");
        sb.append("    public void onDestroy() {\n");
        sb.append("        super.onDestroy();\n");
        sb.append("        Log.d(TAG, \"onDestroy\");\n");

        if (useViewModel) {
            sb.append("        if (viewModel != null) {\n");
            sb.append("            viewModel.clear();\n");
            sb.append("        }\n");
        }

        sb.append("    }\n");

        sb.append("}\n");

        generateJavaFile(className, sb.toString(), "ui.fragment");
    }
}
