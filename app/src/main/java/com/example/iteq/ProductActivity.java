package com.example.iteq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private String myId;
    private String myName;
    private String myPost;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et7;
    private EditText et8;
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private TextView tv1;
    private TextView tv2;
    private String id;
    private String log;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面跳转动画，必须在setContentView之前
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Explode().setDuration(600));
        setContentView(R.layout.activity_product);
        myId = getIntent().getStringExtra("number");
        myName = getIntent().getStringExtra("name");
        myPost = getIntent().getStringExtra("post");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(myId);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et7 = findViewById(R.id.et7);
        et8 = findViewById(R.id.et8);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        init(myId);
    }

    private void init(String number) {
        BmobQuery<Table> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("odd_numbers", number);
        bmobQuery.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    for (Table item : list
                            ) {
                        et1.setText(item.getProduct_number());
                        et2.setText(item.getCopper_containing());
                        et3.setText(item.getProduction_quantity());
                        et4.setText(item.getCustomer_name());
                        et5.setText(item.getCustomer_code());
                        tv1.setText(item.getLog());
                        tv2.setText(item.getInfo());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(myName) || TextUtils.isEmpty(myPost)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        } else {
            switch (view.getId()) {
//                case R.id.fab:
//                    Toast.makeText(this, "暂不提供，该功能", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.bt1:
                    updataTable(id);
                    break;
                case R.id.bt2:
                    updataLog(id, log);
                    break;
                case R.id.bt3:
                    updataInfo(id, info);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updata(myId);
    }

    private void updata(String number) {
        id = "";
        log = "";
        info = "";
        BmobQuery<Table> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("odd_numbers", number);
        bmobQuery.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    for (Table item : list
                            ) {
                        id = item.getObjectId();
                        log = item.getLog();
                        info = item.getInfo();

                    }
                } else {
                    Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updataTable(String id) {
        Table table = new Table();
        table.setProduct_number(et1.getText().toString());
        table.setCopper_containing(et2.getText().toString());
        table.setProduction_quantity(et3.getText().toString());
        table.setCustomer_name(et4.getText().toString());
        table.setCustomer_code(et5.getText().toString());
        table.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(ProductActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                                    intent.putExtra("name", myName);
                                    intent.putExtra("number", myId);
                                    intent.putExtra("post", myPost);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updataLog(String id, String log) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String userinfo = "时间:" + time + "\n" + "岗位:" + myPost
                + "\t\t" + "姓名:" + myName + "\n" + "备注:";
        Table table = new Table();
        table.setLog(log + userinfo + et7.getText().toString() + "\n");
        table.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(ProductActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                                    intent.putExtra("name", myName);
                                    intent.putExtra("number", myId);
                                    intent.putExtra("post", myPost);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updataInfo(String id, String info) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String userinfo = "时间:" + time + "\n" + "岗位:" + myPost
                + "\t\t" + "姓名:" + myName + "\n" + "客户端异常反馈:";
        Table table = new Table();
        table.setInfo(info + userinfo + et8.getText().toString() + "\n");
        table.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(ProductActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                                    intent.putExtra("name", myName);
                                    intent.putExtra("number", myId);
                                    intent.putExtra("post", myPost);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
