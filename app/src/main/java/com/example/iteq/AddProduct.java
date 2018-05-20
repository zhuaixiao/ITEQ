package com.example.iteq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {
    private EditText product_number;
    private EditText odd_numbers;
    private EditText copper_containing;
    private EditText production_quantity;
    private EditText customer_name;
    private EditText customer_code;
    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        product_number = findViewById(R.id.editText8);
        odd_numbers = findViewById(R.id.editText9);
        copper_containing = findViewById(R.id.editText10);
        production_quantity = findViewById(R.id.editText11);
        customer_name = findViewById(R.id.editText12);
        customer_code = findViewById(R.id.editText13);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String s1 = product_number.getText().toString();
        String s2 = odd_numbers.getText().toString();
        String s3 = copper_containing.getText().toString();
        String s4 = production_quantity.getText().toString();
        String s5 = customer_name.getText().toString();
        String s6 = customer_code.getText().toString();
        String name=getIntent().getStringExtra("name");
        String post=getIntent().getStringExtra("post");

        switch (view.getId()) {
            case R.id.button:
                if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2) ||TextUtils.isEmpty(s4) ||
                        TextUtils.isEmpty(s5) ||TextUtils.isEmpty(s6)) {
                    Toast.makeText(this, "请填写完整，“含铜”若无,可不填", Toast.LENGTH_SHORT).show();
                } else if (!s2.contains("-")) {
                    Toast.makeText(this, "“定制单号”格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    //获取当前时间
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date=new Date(System.currentTimeMillis());
                    String time=simpleDateFormat.format(date);
                    Table table = new Table(s1, s2, s3, s4, s5, s6, "{制表--" +"时间:"+time+",岗位:"+post+","+"姓名:"+name+"}", "");
                    table.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(AddProduct.this, "创建工单成功", Toast.LENGTH_SHORT).show();
                                product_number.setText("");
                                odd_numbers.setText("");
                                copper_containing.setText("");
                                production_quantity.setText("");
                                customer_name.setText("");
                                customer_code.setText("");
                            } else if (e.getMessage().contains("duplicate")) {
                                Toast.makeText(AddProduct.this, "已存在此工单", Toast.LENGTH_SHORT).show();
                                product_number.setText("");
                                odd_numbers.setText("");
                                copper_containing.setText("");
                                production_quantity.setText("");
                                customer_name.setText("");
                                customer_code.setText("");
                            } else {
                                Toast.makeText(AddProduct.this, "创建工单失败,再提交一次" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.button2:
                Toast.makeText(this, "暂不支持，扫码", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}