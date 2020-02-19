package com.xbl.com.xbl.vo;

import com.xbl.parse.JsonRespondParse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by HG on 2016/11/10.
 */
@HttpResponse(parser = JsonRespondParse.class)
public class StoreVo {
    private String end_time_str;
    private String start_time_str;
    private String shop_address;
    private String shop_name;
    private String small_pic;
    private int shop_id;

    public String getEnd_time_str() {
        return end_time_str;
    }

    public void setEnd_time_str(String end_time_str) {
        this.end_time_str = end_time_str;
    }

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getStart_time_str() {
        return start_time_str;
    }

    public void setStart_time_str(String start_time_str) {
        this.start_time_str = start_time_str;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public String toString() {
        return shop_name+shop_id;
    }
}
