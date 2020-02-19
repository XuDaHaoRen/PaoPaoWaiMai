package com.xbl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by HG on 2016/11/8.
 */
public abstract class MyBaseAdapter extends BaseAdapter {
    private Context context;
    private List list;
    private LayoutInflater layoutInflater;

    public MyBaseAdapter(Context context, List list) {
        layoutInflater=LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent) ;

}
