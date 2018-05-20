package com.example.iteq;

import cn.bmob.v3.BmobObject;

public class Table extends BmobObject {
    //产品编号
    private String product_number;
    //定制单号
    private String odd_numbers;
    //含铜
    private String copper_containing;
    //客户名称
    private String customer_name;
    //客户代码
    private String customer_code;
    //生产数量
    private String production_quantity;
    //日志
    private String log;
    //信息
    private String info;

    public Table(){
    }
//用于详情页面修改资料
    public Table(String product_number, String copper_containing, String production_quantity,
                 String customer_name, String customer_code){
        this.product_number = product_number;
        this.copper_containing = copper_containing;
        this.customer_name = customer_name;
        this.customer_code = customer_code;
        this.production_quantity = production_quantity;

    }
    //用于建表
    public Table(String product_number, String odd_Numbers, String copper_containing, String production_quantity,
                 String customer_name, String customer_code, String log, String info) {
        this.product_number = product_number;
        this.odd_numbers = odd_Numbers;
        this.copper_containing = copper_containing;
        this.customer_name = customer_name;
        this.customer_code = customer_code;
        this.production_quantity = production_quantity;
        this.log = log;
        this.info = info;

    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }

    public String getCopper_containing() {
        return copper_containing;
    }

    public void setCopper_containing(String copper_containing) {
        this.copper_containing = copper_containing;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getProduction_quantity() {
        return production_quantity;
    }

    public void setProduction_quantity(String production_quantity) {
        this.production_quantity = production_quantity;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOdd_numbers() {
        return odd_numbers;
    }

    public void setOdd_numbers(String odd_numbers) {
        this.odd_numbers = odd_numbers;
    }
}
