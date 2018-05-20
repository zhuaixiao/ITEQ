package com.example.iteq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private String number;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private EditText et8;
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private TextView tv1;
    private TextView tv2;
    private String id;
    private String log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面跳转动画，必须在setContentView之前
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(600));
        setContentView(R.layout.activity_product);
        number = getIntent().getStringExtra("number");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(number);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        et7 = findViewById(R.id.et7);
        et8 = findViewById(R.id.et8);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        init(number);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        switch (view.getId()) {
            case R.id.fab:
                et7.setText("{时间:" + time + "," + "岗位:" + getIntent().getStringExtra("post")
                        + "," + "姓名:" + getIntent().getStringExtra("name") + "," + "备注:");
                break;
            case R.id.bt1:
                Toast.makeText(this, "暂不提供，该功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt2:
                updata(number);
                break;
            case R.id.bt3:
                Toast.makeText(this, "暂不提供，该功能", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updata(String number) {
        id = "";
        log = "";
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
                        updataLog(id, log);
                    }
                } else {
                    Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updataLog(String id, String log) {
        Table table = new Table();
        table.setLog(log + et7.getText().toString() + "}");
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
                                    intent.putExtra("name", getIntent().getStringExtra("name"));
                                    intent.putExtra("number", getIntent().getStringExtra("number"));
                                    intent.putExtra("post", getIntent().getStringExtra("post"));
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
