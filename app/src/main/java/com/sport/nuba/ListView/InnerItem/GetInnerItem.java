package com.sport.nuba.ListView.InnerItem;

import android.view.View;

public class GetInnerItem {

    private InnerItemOnclickListener innerItemOnclickListener;

    public void setInnerItemOnclickListener(InnerItemOnclickListener mInnerItemOnclickListener){
        innerItemOnclickListener = mInnerItemOnclickListener;
    }

    public void clickItem(View view){
        if(innerItemOnclickListener != null && view != null){
            innerItemOnclickListener.itemOnClick(view);
        }
    }
}
