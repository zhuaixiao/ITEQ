package com.example.iteq;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private TextView worknumber;
    private TextView username;
    private TextView mPost;
    private CircleImageView imageView;
    private NavigationView navView;
    private ProductAdapter adapter;
    static final int REQUEST_CODE_SCAN = 2;
    private List<Product> productList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private String number;
    private static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadBingPic();
        initProduct();
        //获取侧滑菜单上控件id
        View headerView = navView.getHeaderView(0);
        worknumber = headerView.findViewById(R.id.work_number);
        username = headerView.findViewById(R.id.username);
        mPost = headerView.findViewById(R.id.mpost);
        imageView = headerView.findViewById(R.id.icon_image);
        //设置人员详情
        worknumber.setText("ID：" + getIntent().getStringExtra("id"));
        username.setText(getIntent().getStringExtra("name"));
        mPost.setText("岗位：" + getIntent().getStringExtra("post"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //设置Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(productList, getIntent().getStringExtra("name"),
                getIntent().getStringExtra("post"));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initProduct();
    }

    private void init() {
        //Dialog弹窗软件自动更新
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        //初始化Bmob
        Bmob.initialize(this, "bae1a67e9200774d8d2baa7aad589dd0");
        //设置toolbar为标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化侧滑菜单
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        //设置下拉刷新
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBingPic();
                initProduct();
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
        //初始化悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "创建工单", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("post", getIntent().getStringExtra("post"));
                startActivity(intent);
            }
        });
        //定义侧滑菜单中的选项
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.introduction:
//                        Toast.makeText(MainActivity.this, "公司简介", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, About.class);
                        intent.putExtra("i", "u");
                        startActivity(intent);
                        break;
                    case R.id.about:
//                        Toast.makeText(MainActivity.this, "软件介绍", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, About.class);
                        intent1.putExtra("i", "i");
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
        List<String> permissionList = new ArrayList<>();
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    /**
     * 请求权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults
                            ) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "请同意此权限！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            initProduct();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 添加menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        //在Toolbar中添加搜索框
        MenuItem item = menu.findItem(R.id.input);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(true);
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHint("516-852452");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestNumber(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.scanning:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                Toast.makeText(this, "扫描二维码", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                requestNumber(content);
            }
        }
    }

    //加载图片
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }

    //加载工单数据
    private void initProduct() {
        SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(this);
        final String pic = perf.getString("bing_pic", "");
        final BmobQuery<Table> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    productList.clear();
                    for (Table item : list
                            ) {
                        productList.add(new Product(item.getOdd_numbers(), pic));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestNumber(String number) {
        SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(this);
        final String pic = perf.getString("bing_pic", "");
        BmobQuery<Table> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("odd_numbers", number);
        bmobQuery.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    productList.clear();
                    for (Table item : list
                            ) {
                        productList.add(new Product(item.getOdd_numbers(), pic));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "查询失败！请检查是否已连接网络，否则不存在此工单",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //双击退出程序
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次   退出程序", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            this.finish();
        }
    }
}
