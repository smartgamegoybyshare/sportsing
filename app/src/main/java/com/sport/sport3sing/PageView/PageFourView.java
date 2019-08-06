package com.sport.sport3sing.PageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.sport.sport3sing.R;
import com.sport.sport3sing.Support.MakeBitmap;

public class PageFourView extends PageView {

    public PageFourView(Context context) {
        super(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.pageone, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        //new DownloadImageTask(imageView).execute("http://www.jetec.com.tw/W8_Banner1/images/gallery/20181213_WeatherStation_Cloud.png");
        /*Runnable getimage = () -> {
            String imageUri = "http://www.jetec.com.tw/W8_Banner1/images/gallery/20181213_WeatherStation_Cloud.png";
            preview_bitmap = fetchImage(imageUri);
        };
        imageView.setImageBitmap(preview_bitmap);*/

        MakeBitmap makeBitmap = new MakeBitmap();
        imageView.setImageBitmap(makeBitmap.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.slider04), 45f));

        imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        addView(view);
    }
}
