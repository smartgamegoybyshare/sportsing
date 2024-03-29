package com.sport.nuba.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sport.nuba.Language.LanguageChose;
import com.sport.nuba.Language.LanguageListener;
import com.sport.nuba.Language.SetLanguage;
import com.sport.nuba.ListView.Home.NewsList;
import com.sport.nuba.PageView.PageAdapter;
import com.sport.nuba.PageView.PageFourView_Logout;
import com.sport.nuba.PageView.PageOneView_Logout;
import com.sport.nuba.PageView.PageThreeView_Logout;
import com.sport.nuba.PageView.PageTwoView_Logout;
import com.sport.nuba.PageView.PageView;
import com.sport.nuba.PageView.ViewPagerIndicator;
import com.sport.nuba.Post_Get.Login.ConnectListener;
import com.sport.nuba.Post_Get.Login.Connected;
import com.sport.nuba.Post_Get.Login.GetConnect;
import com.sport.nuba.R;
import com.sport.nuba.SQL.LanguageSQL;
import com.sport.nuba.SQL.LoginSQL;
import com.sport.nuba.Support.InternetImage;
import com.sport.nuba.Support.Loading;
import com.sport.nuba.Support.MarqueeTextView;
import com.sport.nuba.Support.Screen;
import com.sport.nuba.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class MainActivity extends AppCompatActivity implements ConnectListener, LanguageListener {

    private String TAG = "MainActivity";
    private ViewPager viewPager;
    private Screen screen = new Screen(this);
    private List<PageView> pageList;
    private ViewPagerIndicator viewPagerIndicator;
    private DisplayMetrics dm;
    private String company, account, password, imformation;
    private Loading loading = new Loading(this);
    private Connected connected = new Connected(this);
    private GetConnect getConnect = new GetConnect();
    private LoginSQL loginSQL = new LoginSQL(this);
    private LanguageSQL languageSQL = new LanguageSQL(this);
    private MarqueeTextView announcement;
    private EditText editText1, editText2, editText3;
    private CheckBox checkBox;
    private Button login;
    private ListView listView;
    private GifImageView gifImageView1;
    private Bitmap bitmap_title, preview_bitmap;
    private ImageView imageViewtitle;
    private Handler viewpageHandler = new Handler();
    private LanguageChose languageChose = new LanguageChose(this);
    private SetLanguage setLanguage = new SetLanguage();
    private InternetImage internetImage = new InternetImage();
    private Handler checkHandler = new Handler(), titleHandler = new Handler(), announceHandler = new Handler();

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
        getConnect.setListener(this);
        dm = screen.size();
        setLanguage.setListener(this);
        if (languageSQL.getCount() == 0) {
            languageChose.show(setLanguage, languageSQL);
        } else {
            Value.language_flag = languageSQL.getflag();
        }
        //new Thread(test).start();
        showview();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showview() {
        try {
            setContentView(R.layout.homepage);

            imageViewtitle = findViewById(R.id.imageView1);
            ImageView homebuttondown = findViewById(R.id.homebuttondown);   //隱藏的上拉view
            LinearLayout gif_linear = findViewById(R.id.linear_gif);    //廣告欄
            LinearLayout bottom = findViewById(R.id.bottom_button); //隱藏的上拉view的欄框
            LinearLayout object = findViewById(R.id.edit_object);   //上拉view的欄框
            listView = findViewById(R.id.listview);    //公告列表
            editText1 = findViewById(R.id.editText1);   //公司
            editText2 = findViewById(R.id.editText2);  //戶口
            editText3 = findViewById(R.id.editText3);  //密碼
            checkBox = findViewById(R.id.checkBox);    //記住我的登入資訊
            login = findViewById(R.id.login);   //登入按鈕
            announcement = findViewById(R.id.textView1);   //公告字串
            viewPager = findViewById(R.id.pager);   //slider廣告介面
            viewPagerIndicator = findViewById(R.id.indicator);  //slider下的點點
            BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll)); //上拉view

            setLanguage.isSet();

            object.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, (4 * dm.heightPixels) / 10));
            bottom.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels / 10));
            gifImageView1 = findViewById(R.id.imageView4); //廣告圖
            /*
            因上拉頁面nestedscrollview無法使用適配螢幕比
            故以下為與IOS相同之寬高比例計算適配廣告欄高度
            */
            double gif_height = dm.widthPixels / 6.25;
            gif_linear.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, (int) gif_height));
            new Thread(getImage).start();
            /*GifDrawable gifFromPath = new GifDrawable(this.getResources(), R.drawable.adphoto);
            gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifImageView1.setImageDrawable(gifFromPath);*/
            gifImageView1.setOnClickListener(view -> {
                Uri uri = Uri.parse("http://3singsport.win");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });

            String thisversion = getVersionName(MainActivity.this);
            Log.e(TAG, "thisversion = " + thisversion);
            Value.ver = thisversion;

            listView();

            announcement.setOnClickListener(view -> showhowto());

            List<String> dataList = new ArrayList<>();
            dataList.clear();
            dataList = loginSQL.getlist();
            if (dataList.size() != 0) {
                editText1.setText(dataList.get(0));
                editText2.setText(dataList.get(1));
                editText3.setText(dataList.get(2));
                checkBox.setChecked(true);
            }

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

            login.setOnClickListener(v -> {
                company = editText1.getText().toString().trim();
                account = editText2.getText().toString().trim();
                password = editText3.getText().toString().trim();

                if (company.matches("")) {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        Toast toast = Toast.makeText(this, "Sub Account is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 1) {
                        Toast toast = Toast.makeText(this, "子帳號不可為空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 2) {
                        Toast toast = Toast.makeText(this, "子帐号不可为空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else if (account.matches("")) {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        Toast toast = Toast.makeText(this, "User is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 1) {
                        Toast toast = Toast.makeText(this, "戶口不可為空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 2) {
                        Toast toast = Toast.makeText(this, "户口不可为空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else if (password.matches("")) {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        Toast toast = Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 1) {
                        Toast toast = Toast.makeText(this, "密碼不可為空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Value.language_flag == 2) {
                        Toast toast = Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        loading.show("Logining...");
                    } else if (Value.language_flag == 1) {
                        loading.show("登入中...");
                    } else if (Value.language_flag == 2) {
                        loading.show("登陆中...");
                    }
                    connected.setConnect(company, account, password, getConnect);
                }
            });

            behavior.setPeekHeight(dm.heightPixels / 14);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == STATE_EXPANDED) {
                        homebuttondown.setImageDrawable(MainActivity.this.getResources().
                                getDrawable((R.drawable.homebuttondown)));
                        homebuttondown.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    if (newState == STATE_COLLAPSED) {
                        homebuttondown.setImageDrawable(MainActivity.this.getResources().
                                getDrawable((R.drawable.homebutton)));
                        homebuttondown.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            setlistViewAdapter();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Runnable getImage = new Runnable() {
        @Override
        public void run() {
            String imageUri = "https://dl.kz168168.com/img/android-logo01.png";
            String imageUri2 = "https://dl.kz168168.com/img/android-ad01.png";
            bitmap_title = internetImage.fetchImage(imageUri);
            preview_bitmap = internetImage.fetchImage(imageUri2);
            titleHandler.post(() -> {
                imageViewtitle.setImageBitmap(bitmap_title);
                imageViewtitle.setScaleType(ImageView.ScaleType.CENTER_CROP);
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        }
    };

    private Runnable test = this::getVersion;

    private void getVersion() {
        try {
            URL url = new URL("http://3singsport.co/apk/kz-version.json");
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(2000);
            InputStream uin = urlCon.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            boolean more = true;
            String line;
            for (; more; ) {
                line = in.readLine();
                if (line != null) {
                    if (line.contains("version")) {
                        line = "{" + line + "}";
                        String thisversion = getVersionName(MainActivity.this);
                        JSONObject jsonObject = new JSONObject(line);
                        if (thisversion.matches(jsonObject.getString("version"))) {
                            Log.e(TAG, "版本相同");
                        } else {
                            Log.e(TAG, "thisversion = " + thisversion);
                            Log.e(TAG, "line = " + jsonObject.getString("version"));
                            checkHandler.post(() -> {
                                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                                    new AlertDialog.Builder(this)
                                            .setTitle("努霸財富管家")
                                            .setIcon(R.drawable.app_icon_mini)
                                            .setMessage("This app have a new version.\nDo you want to update?")
                                            .setPositiveButton("Yes", (dialog, which) -> {
                                                getNewVersion();
                                                finish();
                                            })
                                            .setNegativeButton("No", (dialog, which) -> {
                                                // TODO Auto-generated method stub
                                            }).show();
                                } else if (Value.language_flag == 1) {
                                    new AlertDialog.Builder(this)
                                            .setTitle("努霸財富管家")
                                            .setIcon(R.drawable.app_icon_mini)
                                            .setMessage("偵測到有新版本\n現在要更新嗎?")
                                            .setPositiveButton("確定", (dialog, which) -> {
                                                getNewVersion();
                                                finish();
                                            })
                                            .setNegativeButton("取消", (dialog, which) -> {
                                                // TODO Auto-generated method stub
                                            }).show();
                                } else if (Value.language_flag == 2) {
                                    new AlertDialog.Builder(this)
                                            .setTitle("努霸财富管家")
                                            .setIcon(R.drawable.app_icon_mini)
                                            .setMessage("侦测到有新版本\n现在要更新吗?")
                                            .setPositiveButton("确定", (dialog, which) -> {
                                                getNewVersion();
                                                finish();
                                            })
                                            .setNegativeButton("取消", (dialog, which) -> {
                                                // TODO Auto-generated method stub
                                            }).show();
                                }
                                checkHandler.removeCallbacksAndMessages(null);
                            });
                        }
                    }
                } else more = false;
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException = " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException = " + e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "JSONException = " + e);
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Runnable announce = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("https://dl.kz168168.com/apk/nuba_default_marquee.json");
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(2000);
                InputStream uin = urlCon.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(uin));
                boolean more = true;
                String line = "";
                for (; more; ) {
                    String getline = in.readLine();
                    Log.e(TAG, "getline = " + getline);
                    if (getline != null) {
                        line = line + getline;
                    } else {
                        break;
                    }
                }
                Log.e(TAG, "line = " + line);
                JSONObject jsonObject = new JSONObject(line);
                Log.e(TAG, "jsonObject = " + jsonObject);
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    imformation = jsonObject.getString("text_en");
                } else if (Value.language_flag == 1) {
                    imformation = jsonObject.getString("text_tw");
                } else if (Value.language_flag == 2) {
                    imformation = jsonObject.getString("text_cn");
                }
                if (imformation.length() < 80) {
                    StringBuilder imformationBuilder = new StringBuilder(imformation);
                    for (int j = imformationBuilder.length(); j < 80; j++) {
                        imformationBuilder.append("  ");
                    }
                    imformation = imformationBuilder.toString();
                }
                announceHandler.post(() -> {
                    announcement.setText(imformation);
                    checkHandler.removeCallbacksAndMessages(null);
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void showhowto() {
        Intent intent = new Intent(this, HowtoActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNewVersion() {
        Uri uri = Uri.parse("https://dl.kz168168.com/apk/kz.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    private void nextPage() {
        Intent intent = new Intent(this, LoginMainActivity.class);
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
        pageList.add(new PageOneView_Logout(this));
        pageList.add(new PageTwoView_Logout(this));
        pageList.add(new PageThreeView_Logout(this));
        pageList.add(new PageFourView_Logout(this));
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

    private void setlistViewAdapter() {
        List<String> news_api = new ArrayList<>();
        news_api.clear();
        news_api.add("");
        NewsList newsList = new NewsList(this, news_api);
        listView.setAdapter(newsList);
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

    @Override
    public void isConnected(JSONObject responseJson) {
        try {
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                loading.dismiss();
                Value.check_user = responseJson;
                if (checkBox.isChecked()) {
                    if (loginSQL.getCount() != 0) {
                        loginSQL.deleteAll();
                        loginSQL.insert(company, account, password);
                    } else {
                        loginSQL.insert(company, account, password);
                    }
                } else {
                    loginSQL.deleteAll();
                }
                Value.login_in = true;
                nextPage();
            } else if (result.matches("error1")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Password Incorrect", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "密碼不正確", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error3")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Sub Account Does Not Exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "子帳號戶口不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "子帐号户口不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error4")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Reconciliation Invalid", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "尚未開放對帳", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "尚未开放对帐", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {    //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
        if (Value.language_flag == 0) {
            editText1.setHint("Sub Account");
            editText2.setHint("User");
            editText3.setHint("Password");
            checkBox.setText("  Remember Me");
            login.setText("Login");
        } else if (Value.language_flag == 1) {
            editText1.setHint("分公司/子帳號");
            editText2.setHint("戶口");
            editText3.setHint("密碼");
            checkBox.setText("  記住我的登入資訊");
            login.setText("登入");
        } else if (Value.language_flag == 2) {
            editText1.setHint("分公司/子帐号");
            editText2.setHint("户口");
            editText3.setHint("密码");
            checkBox.setText("  记住我的登陆资讯");
            login.setText("登陆");
        }
        new Thread(announce).start();
        listView.setAdapter(null);
        setlistViewAdapter();
    }
}
