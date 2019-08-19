package com.sport.nuba.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sport.nuba.Language.LanguageListener;
import com.sport.nuba.Language.SetLanguage;
import com.sport.nuba.ListView.Home.NewsList;
import com.sport.nuba.PageView.PageAdapter;
import com.sport.nuba.PageView.PageFourView;
import com.sport.nuba.PageView.PageOneView;
import com.sport.nuba.PageView.PageThreeView;
import com.sport.nuba.PageView.PageTwoView;
import com.sport.nuba.PageView.PageView;
import com.sport.nuba.PageView.ViewPagerIndicator;
import com.sport.nuba.Post_Get.Message.GetPost;
import com.sport.nuba.Post_Get.Message.Post;
import com.sport.nuba.Post_Get.Message.PostListener;
import com.sport.nuba.R;
import com.sport.nuba.SQL.LoginSQL;
import com.sport.nuba.Support.MarqueeTextView;
import com.sport.nuba.Support.Screen;
import com.sport.nuba.Support.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class LoginMainActivity extends AppCompatActivity implements LanguageListener, PostListener {

    private String TAG = "LoginMainActivity";
    private ViewPager viewPager;
    private Screen screen = new Screen(this);
    private List<PageView> pageList;
    private ViewPagerIndicator viewPagerIndicator;
    private DisplayMetrics dm;
    private MarqueeTextView announcement;
    private List<String> news_api;
    private String company, account;
    private LoginSQL loginSQL = new LoginSQL(this);
    private NewsList newsList;
    private Button login, logout;
    private ListView listView;
    private List<String> dataList;
    private GifImageView gifImageView1;
    private Bitmap bitmap_title, preview_bitmap;
    private ImageView imageViewtitle;
    private Handler viewpageHandler = new Handler();
    private SetLanguage setLanguage = new SetLanguage();
    private Post post = new Post(this);
    private GetPost getPost = new GetPost();
    private Handler checkHandler = new Handler(), titleHandler = new Handler(), adHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         //隱藏狀態欄
         getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
         //隱藏底部HOME工具列
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
         **/
        dm = screen.size();
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);
        showview();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showview() {
        setContentView(R.layout.homepage2);

        imageViewtitle = findViewById(R.id.imageView1);
        ImageView homebuttondown = findViewById(R.id.homebuttondown);   //隱藏的上拉view
        LinearLayout gif_linear = findViewById(R.id.linear_gif);    //廣告欄
        LinearLayout bottom = findViewById(R.id.bottom_button); //隱藏的上拉view的欄框
        LinearLayout object = findViewById(R.id.edit_object);   //上拉view的欄框
        listView = findViewById(R.id.listview);    //公告列表
        login = findViewById(R.id.checkin);   //登入按鈕
        logout = findViewById(R.id.logout); //登出按鈕
        announcement = findViewById(R.id.textView1);   //公告字串
        viewPager = findViewById(R.id.pager);   //slider廣告介面
        viewPagerIndicator = findViewById(R.id.indicator);  //slider下的點點
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll)); //上拉view
        getPost.setListener(this);
        setLanguage.setListener(this);
        setLanguage.isSet();

        object.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, (2 * dm.heightPixels) / 10));
        bottom.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels / 10));
        gifImageView1 = findViewById(R.id.imageView4); //廣告圖

        Runnable gettitle = () -> {
            String imageUri = "https://dl.kz168168.com/img/android-logo01.png";
            bitmap_title = fetchImage(imageUri);
            titleHandler.post(() -> {
                imageViewtitle.setImageBitmap(bitmap_title);
                imageViewtitle.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(gettitle).start();
            /*
            因上拉頁面nestedscrollview無法使用適配螢幕比
            故以下為與IOS相同之寬高比例計算適配廣告欄高度
            */
        double gif_height = dm.widthPixels / 6.25;
        gif_linear.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, (int) gif_height));
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/android-ad01.png";
            preview_bitmap = fetchImage(imageUri);
            adHandler.post(() -> {
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(getimage).start();
            /*GifDrawable gifFromPath = new GifDrawable(this.getResources(), R.drawable.adphoto);
            gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifImageView1.setImageDrawable(gifFromPath);*/
        gifImageView1.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        listView();

        announcement.setOnClickListener(view -> showhowto());

        dataList = new ArrayList<>();
        dataList.clear();
        dataList = loginSQL.getlist();

        //homebuttondown.setBackgroundResource(R.drawable.homebutton);  //android:background語法
        homebuttondown.setImageDrawable(this.getResources().getDrawable((R.drawable.homebutton)));  //android:src語法
        homebuttondown.setScaleType(ImageView.ScaleType.CENTER_CROP);   //ScaleType等比例縮放僅適用android:src語法
        homebuttondown.setOnClickListener(view -> {
            if (behavior.getState() == STATE_EXPANDED) {
                behavior.setState(STATE_COLLAPSED);
            } else if (behavior.getState() == STATE_COLLAPSED) {
                behavior.setState(STATE_EXPANDED);
            }
        });

        login.setOnClickListener(v -> nextPage());
        logout.setOnClickListener(v -> homePage());

        behavior.setPeekHeight(dm.heightPixels / 14);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_EXPANDED) {
                    homebuttondown.setImageDrawable(LoginMainActivity.this.getResources().
                            getDrawable((R.drawable.homebuttondown)));
                    homebuttondown.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                if (newState == STATE_COLLAPSED) {
                    homebuttondown.setImageDrawable(LoginMainActivity.this.getResources().
                            getDrawable((R.drawable.homebutton)));
                    homebuttondown.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void homePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Bitmap fetchImage(String urlstr) {  //連接網頁獲取的圖片
        try {
            URL url;
            url = new URL(urlstr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setDoInput(true);
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void nextPage() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void showhowto() {
        Intent intent = new Intent(this, Howto2Activity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    public void autoViewpager() {
        viewpageHandler.postDelayed(() -> {
            int i = viewPager.getCurrentItem();
            i++;
            viewPager.setCurrentItem(i);
            viewpageHandler.removeCallbacksAndMessages(null);
        }, 4000);
    }

    private void listView() {
        pageList = new ArrayList<>();
        pageList.clear();
        pageList.add(new PageOneView(this));
        pageList.add(new PageTwoView(this));
        pageList.add(new PageThreeView(this));
        pageList.add(new PageFourView(this));
        PageAdapter pageAdapter = new PageAdapter(initItemList());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(1);
        autoViewpager();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        viewpageHandler.removeCallbacksAndMessages(null);
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.removeAllViews();
                            viewPager.addView(pageList.get(3));
                            viewPager.addView(pageList.get(4));
                            viewPager.addView(pageList.get(5));
                            viewPager.setCurrentItem(4, false);
                        } else if (viewPager.getCurrentItem() == 5) {
                            viewPager.removeAllViews();
                            viewPager.addView(pageList.get(0));
                            viewPager.addView(pageList.get(1));
                            viewPager.addView(pageList.get(2));
                            viewPager.setCurrentItem(1, false);
                        }
                        autoViewpager();
                        viewPagerIndicator.setSelected(viewPager.getCurrentItem() - 1);
                        break;
                }
            }
        });
    }

    private List<PageView> initItemList() {
        List<PageView> newPageView = new ArrayList<>();
        newPageView.clear();
        newPageView.addAll(pageList);
        viewPagerIndicator.setLength(pageList.size());
        if (newPageView.size() > 1) {
            //第0个位最后一个，向左拉动时，可以实现直接滑动到最后一个，最后一个是第0个，可以实现向右滑动的时直接跳到第0个
            newPageView.add(0, pageList.get(pageList.size() - 1));
            newPageView.add(pageList.get(0));
        }

        pageList = newPageView;

        return newPageView;
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    new AlertDialog.Builder(this)
                            .setTitle("努霸財富管家")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("Do you want to exit?")
                            .setPositiveButton("Yes", (dialog, which) -> finish())
                            .setNegativeButton("No", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 1) {
                    new AlertDialog.Builder(this)
                            .setTitle("努霸財富管家")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("確定要離開?")
                            .setPositiveButton("離開", (dialog, which) -> finish())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 2) {
                    new AlertDialog.Builder(this)
                            .setTitle("努霸财富管家")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("确定要离开?")
                            .setPositiveButton("离开", (dialog, which) -> finish())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                }
            }
            break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        loginSQL.close();
        viewpageHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {    //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
        Log.e(TAG, "setLanguage()");
        if (Value.language_flag == 0) {
            login.setText("Over All");
            logout.setText("Logout");
        } else if (Value.language_flag == 1) {
            login.setText("對帳");
            logout.setText("登出");
        } else if (Value.language_flag == 2) {
            login.setText("对帐");
            logout.setText("登出");
        }
        listView.setAdapter(null);
        post.setConnect(company, account, getPost);
    }

    @Override
    public void getmessage(JSONObject responseJson) {
        try {
            Log.e(TAG, "getmessage = " + responseJson);
            JSONArray jsonArray = new JSONArray(responseJson.get("records").toString());
            news_api = new ArrayList<>();
            news_api.clear();
            news_api.add("");
            Log.e(TAG, "jsonArray = " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                Log.e(TAG, "jsonObject = " + jsonObject);
                if (jsonObject.get("post_category").toString().matches("marquee")) {
                    String announce_text = jsonObject.get("post_name").toString();
                    Log.e(TAG, "announce_text.lenth = " + announce_text.length());
                    if (announce_text.length() < 80) {
                        StringBuilder announce_textBuilder = new StringBuilder(announce_text);
                        for (int j = announce_textBuilder.length(); j < 80; j++) {
                            announce_textBuilder.append("  ");
                        }
                        announce_text = announce_textBuilder.toString();
                    }
                    announcement.setText(announce_text);
                } else {
                    String news = jsonObject.get("post_name").toString();
                    news_api.add(news);
                    Log.e(TAG, "news_api = " + news_api);
                }
            }
            newsList = new NewsList(this, news_api);
            listView.setAdapter(newsList);
            //setlistViewAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
