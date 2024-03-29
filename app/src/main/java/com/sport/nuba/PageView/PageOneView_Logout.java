package com.sport.nuba.PageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.sport.nuba.R;
import com.sport.nuba.Support.InternetImage;
import com.sport.nuba.Support.Value;

public class PageOneView_Logout extends PageView {

    private Bitmap preview_bitmap;
    private ImageView imageView;
    private Handler handler = new Handler();
    private InternetImage internetImage = new InternetImage();

    public PageOneView_Logout(Context context) {
        super(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.pageone, null);
        imageView = view.findViewById(R.id.imageView);
        Runnable getimage = () -> {
            String imageUri = "";
            if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                imageUri = "https://dl.kz168168.com/img/android-slider01_en.png";
            }else if(Value.language_flag == 1){
                imageUri = "https://dl.kz168168.com/img/android-slider01_tw.png";
            }else if(Value.language_flag == 2){
                imageUri = "https://dl.kz168168.com/img/android-slider01_cn.png";
            }
            preview_bitmap = internetImage.fetchImage(imageUri);
            handler.post(() -> imageView.setImageBitmap(preview_bitmap));
        };
        new Thread(getimage).start();
        //MakeBitmap makeBitmap = new MakeBitmap();
        /*imageView.setImageBitmap(makeBitmap.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.slider01), 45f));*/
        imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        addView(view);
    }
}
