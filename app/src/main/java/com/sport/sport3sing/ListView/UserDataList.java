package com.sport.sport3sing.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sport.sport3sing.R;
import com.sport.sport3sing.Support.Value;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserDataList extends BaseAdapter {

    private List<String> user_record;
    private LayoutInflater inflater;

    @SuppressLint("InflateParams")
    public UserDataList(Context context, List<String> user_record) {
        this.user_record = user_record;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return user_record.size();
    }

    @Override
    public Object getItem(int position) {
        return user_record.get(position);
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
            view = inflater.inflate(R.layout.userdata, null);
        }

        TextView date = view.findViewById(R.id.textView1);
        TextView chart_code = view.findViewById(R.id.textView2);
        TextView remarks = view.findViewById(R.id.textView3);
        TextView gain = view.findViewById(R.id.textView4);
        TextView lose = view.findViewById(R.id.textView5);
        TextView balance = view.findViewById(R.id.textView6);
        LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
        LinearLayout linearLayout5 = view.findViewById(R.id.linearLayout5);
        LinearLayout linearLayout6 = view.findViewById(R.id.linearLayout6);

        if (position != user_record.size() - 1) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(user_record.get(position));
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode_en").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }else if(Value.language_flag == 1){
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }else if(Value.language_flag == 2){
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }
                String gain_value = jsonObject.get("record_amount_forex").toString();
                String balance_value = jsonObject.get("record_forex_total").toString();
                if (gain_value.contains("-")) {
                    gain.setText("");
                    lose.setText(gain_value);
                    lose.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    lose.setText("");
                    gain.setTextColor(Color.BLUE);
                }
                balance.setText(balance_value);
                if (balance_value.contains("-")) {
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setTextColor(Color.BLUE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame);
            linearLayout2.setBackgroundResource(R.drawable.datalist_frame);
            linearLayout3.setBackgroundResource(R.drawable.datalist_frame);
            linearLayout4.setBackgroundResource(R.drawable.datalist_frame);
            linearLayout5.setBackgroundResource(R.drawable.datalist_frame);
            linearLayout6.setBackgroundResource(R.drawable.datalist_frame);
        } else {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(user_record.get(user_record.size() - 1));
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    remarks.setText("Total");
                }else if(Value.language_flag == 1){
                    remarks.setText("總數");
                }else if(Value.language_flag == 2){
                    remarks.setText("总数");
                }
                String gain_value = jsonObject.get("surplus").toString();
                String lose_value = jsonObject.get("loss").toString();
                String balance_value = jsonObject.get("surplus_loss").toString();
                if (gain_value.contains("-")) {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.BLUE);
                }
                if (lose_value.contains("-")) {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.RED);
                } else {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.BLUE);
                }
                if (balance_value.contains("-")) {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.BLUE);
                }

                date.setText("");
                chart_code.setText("");
                linearLayout1.setBackgroundResource(R.drawable.datalist_total_first);
                linearLayout2.setBackgroundResource(R.drawable.datalist_total_buttom);
                linearLayout3.setBackgroundResource(R.drawable.datalist_frame);
                linearLayout4.setBackgroundResource(R.drawable.datalist_frame);
                linearLayout5.setBackgroundResource(R.drawable.datalist_frame);
                linearLayout6.setBackgroundResource(R.drawable.datalist_frame);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
/*
        else {
            TextView total = view.findViewById(R.id.textView1);
            TextView gain = view.findViewById(R.id.textView2);
            TextView lose = view.findViewById(R.id.textView3);
            TextView balance = view.findViewById(R.id.textView4);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(user_record.get(user_record.size() - 1));
                String gain_value = jsonObject.get("surplus").toString();
                String lose_value = jsonObject.get("loss").toString();
                String balance_value = jsonObject.get("surplus_loss").toString();
                total.setText("Total");
                if (gain_value.contains("-")) {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.BLUE);
                }
                if (lose_value.contains("-")) {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.RED);
                } else {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.BLUE);
                }
                if (balance_value.contains("-")) {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.BLUE);
                }

                total.setBackgroundResource(R.drawable.datalist_start_frame);
                gain.setBackgroundResource(R.drawable.datalist_frame);
                lose.setBackgroundResource(R.drawable.datalist_frame);
                balance.setBackgroundResource(R.drawable.datalist_frame);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        return view;
    }
}
