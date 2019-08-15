package com.sport.nuba.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport.nuba.ListView.InnerItem.GetInnerItem;
import com.sport.nuba.ListView.InnerItem.ViewHolder;
import com.sport.nuba.R;
import com.sport.nuba.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LinksettingList extends BaseAdapter implements View.OnClickListener {

    private String TAG= "LinksettingList";
    private GetInnerItem getInnerItem;
    private List<String> userLink;
    private List<View> saveView;
    private List<ViewHolder> saveHolder;

    public LinksettingList(Context context, List<String> userLink, GetInnerItem getInnerItem) {
        this.userLink = userLink;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.getInnerItem = getInnerItem;
        saveView = new ArrayList<>();
        saveHolder = new ArrayList<>();
        saveView.clear();
        saveHolder.clear();
        for(int i = 0; i < userLink.size(); i++){
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.linksetlist, null);
            ViewHolder viewHolder = new ViewHolder();
            saveView.add(view);
            saveHolder.add(viewHolder);
        }
    }

    @Override
    public int getCount() {
        return userLink.size();
    }

    @Override
    public Object getItem(int position) {
        return userLink.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view;

        view = saveView.get(position);
        viewHolder = saveHolder.get(position);

        try {
            JSONObject jsonObject = new JSONObject(userLink.get(position));
            String company = jsonObject.get("link_from_code").toString();
            String account = jsonObject.get("link_from_user").toString();

            LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
            LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            TextView textView3 = view.findViewById(R.id.textView3);

            if(position == userLink.size() - 1){
                linearLayout1.setBackgroundResource(R.drawable.linksettingstyle_left);
                linearLayout3.setBackgroundResource(R.drawable.liststyle_right);
            }
            textView1.setText(company);
            textView2.setText(account);
            if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                textView3.setText("Cancel");
            }else if(Value.language_flag == 1){
                textView3.setText("取消綁定");
            }else if(Value.language_flag == 2){
                textView3.setText("取消绑定");
            }
            textView3.setTextColor(Color.BLUE);
            viewHolder.textView = textView3;
            viewHolder.textView.setOnClickListener(this);
            viewHolder.textView.setTag(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        getInnerItem.clickItem(view);
    }
}
