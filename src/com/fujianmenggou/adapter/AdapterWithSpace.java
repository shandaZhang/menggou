package com.fujianmenggou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.fujianmenggou.R;

/**
 * Created by Du on 2014/12/29.
 */
public class AdapterWithSpace extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> mList;
    private Context context;

    public AdapterWithSpace(Context context,ArrayList<HashMap<String, Object>> mList){
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            if(position!=0 && position%2==0){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_simlpe_list3,null);
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.item_simlpe_list,null);
            }
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource((Integer) getItem(position).get("image"));
        viewHolder.textView.setText((String)getItem(position).get("name"));

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
