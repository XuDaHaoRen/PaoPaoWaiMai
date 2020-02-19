package com.xbl.com.xbl.vo;

import com.xbl.parse.JsonRespondParse;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.ResponseParser;


/**
 * Created by HG on 2016/11/8.
 *
 */
@HttpResponse(parser = JsonRespondParse.class)
public class DayGoodsVo  {
    private String classify_name;
    private int classify_id;

    public int getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(int classify_id) {
        this.classify_id = classify_id;
    }

    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }

    @Override
    public String toString() {
        return classify_name;
    }
}
