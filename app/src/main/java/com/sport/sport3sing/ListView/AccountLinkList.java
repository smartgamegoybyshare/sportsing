package com.sport.sport3sing.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sport.sport3sing.R;
import com.sport.sport3sing.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class AccountLinkList extends BaseAdapter {

    private List<String> user_Link;
    private LayoutInflater inflater;

    public AccountLinkList(Context context, List<String> user_Link) {
        this.user_Link = user_Link;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return user_Link.size();
    }

    @Override
    public Object getItem(int position) {
        return user_Link.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.linkchose, null);
        }

        try {
            JSONObject jsonObject = new JSONObject(user_Link.get(position));
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            TextView textView3 = view.findViewById(R.id.textView3);
            TextView textView4 = view.findViewById(R.id.textView4);

            String company = jsonObject.get("link_from_code").toString();
            String account = jsonObject.get("link_from_user").toString();
            if(Value.language_flag == 0){   //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                textView1.setText("Sub Account：");
                textView3.setText("User：");
            }else if(Value.language_flag == 1){
                textView1.setText("子帳號：");
                textView3.setText("戶口：");
            }else if(Value.language_flag == 2){
                textView1.setText("子帐号：");
                textView3.setText("户口：");
            }

            textView2.setText(company);
            textView4.setText(account);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
