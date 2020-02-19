package com.xbl.com.xbl.vo;

import com.xbl.parse.JsonRespondParse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by HG on 2016/11/9.
 */
@HttpResponse(parser = JsonRespondParse.class)
public class FlavourVo {
    private String small_pic;
    private String product_name;
    private String category_name;
    private String classify_name;
    private int nowprice;
    private  int product_id;

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }

    public int getNowprice() {
        return nowprice;
    }

    public void setNowprice(int nowprice) {
        this.nowprice = nowprice;
    }

    @Override
    public String toString() {
        return product_name+small_pic;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
