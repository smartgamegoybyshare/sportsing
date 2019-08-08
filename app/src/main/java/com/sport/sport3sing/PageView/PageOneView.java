package com.sport.sport3sing.PageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.sport.sport3sing.R;
import com.sport.sport3sing.Support.MakeBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

import static java.lang.Thread.sleep;

public class PageOneView extends PageView{

    private Bitmap preview_bitmap;
    //private ImageView imageView;
    //private int flag = 0;
    //private Uri uri = Uri.parse("http://www.jetec.com.tw/#services");

    public PageOneView(Context context) {
        super(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.pageone, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        //new DownloadImageTask(imageView).execute("http://www.jetec.com.tw/W8_Banner1/images/gallery/20181213_WeatherStation_Cloud.png");
       /* Runnable getimage = () -> {
            String imageUri = "https://i.imgur.com/b6aGO6U.png";
            preview_bitmap = fetchImage(imageUri);
            flag = 1;
        };
        new Thread(getimage).start();
        for(;flag == 0;){
            try{
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(preview_bitmap);*/
        //private Uri uri = Uri.parse("");
        //private Bitmap preview_bitmap;
        MakeBitmap makeBitmap = new MakeBitmap();
        imageView.setImageBitmap(makeBitmap.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.slider01), 45f));

        imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        addView(view);
    }

    /*private Bitmap fetchImage(String urlstr ) {  //連接網頁獲取的圖片
        try {
            URL url;
            url = new URL(urlstr);
            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
            c.setDoInput( true );
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
