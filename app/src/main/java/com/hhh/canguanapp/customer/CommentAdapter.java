package com.hhh.canguanapp.customer;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhh.canguanapp.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends BaseAdapter {
    Context context;
    List<Map<String, String>> list;

    public CommentAdapter() {
    }

    public CommentAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(list != null && list.size()>0){
            String username = list.get(i).get("username");
            String comment = list.get(i).get("comment");
            String str = "<font color = '#6495ED'>"+username+"</font>"+"<font color = '#000000'>"+"  :  "+comment+"</font>";
            if(str != null && str.length()>0){
                viewHolder.tv_comment.setText(Html.fromHtml(str));
            }
        }
        return view;
    }



    class ViewHolder{
        TextView tv_comment;
        public ViewHolder(View view){
            tv_comment = view.findViewById(R.id.tv_comment);
        }
    }
}
