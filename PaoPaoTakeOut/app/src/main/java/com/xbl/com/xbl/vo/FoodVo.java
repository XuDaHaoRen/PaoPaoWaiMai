package com.xbl.com.xbl.vo;

import com.xbl.parse.JsonRespondParse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by HG on 2016/11/9.
 */
@HttpResponse(parser = JsonRespondParse.class)
public class FoodVo {
    private String product_name;
    private String small_pic;
    private int nowprice;
    private String product_address;



    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public int getNowprice() {
        return nowprice;
    }

    public void setNowprice(int nowprice) {
        this.nowprice = nowprice;
    }

    @Override
    public String toString() {
        return product_name+"11"+small_pic;

    }

    public String getProduct_address() {
        return product_address;
    }

    public void setProduct_address(String product_address) {
        this.product_address = product_address;
    }
}
