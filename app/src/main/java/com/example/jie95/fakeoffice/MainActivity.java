package com.example.jie95.fakeoffice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jie95.fakeoffice.FileSelected.FileService;
import com.example.jie95.fakeoffice.FileSelected.Filename;
import com.example.jie95.fakeoffice.Login.LoginUI;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {

    private List<Map<String, ?>> filename;
    private ListView fileList;
    FileService fileService;
    AlertDialog deleteDiaryAlert;
    SimpleAdapter simpleAdapter;
    int id;


    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_setting;
    private RadioButton rb_waste;

    public static final int PAGE_ONE=0;
    public static final int PAGE_TWO=1;
    public static final int PAGE_THREE=2;

    private MyFragmentPagerAdapter mAdapter;
    private ViewPager vpager;
    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Documents:

                    return true;
                case R.id.Personal:

                    return true;
                case R.id.LibraryData:

                    return true;
                case R.id.Recently:

                    return true;
            }
            return false;
        }
    };*/

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                Typeface mTypeface=Typeface.createFromAsset(getAssets(),"lingdong.ttf");
                try{
                    Field field=Typeface.class.getDeclaredField("MONOSPACE");
                    field.setAccessible(true);
                    field.set(null,mTypeface);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
            bindViews();
            rb_channel.setChecked(true);
        }

        private void bindViews() {
            rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
            rb_channel = (RadioButton) findViewById(R.id.rb_Recently);
            rb_setting = (RadioButton) findViewById(R.id.rb_Personal);
            rb_waste = (RadioButton) findViewById(R.id.rb_waste);

            rb_channel.setTypeface(Typeface.createFromAsset(getAssets(),"lingdong.ttf"));
            rb_setting.setTypeface(Typeface.createFromAsset(getAssets(),"lingdong.ttf"));
            rb_waste.setTypeface(Typeface.createFromAsset(getAssets(),"lingdong.ttf"));

            rg_tab_bar.setOnCheckedChangeListener(this);

            vpager = (ViewPager) findViewById(R.id.vpager);
            vpager.setAdapter(mAdapter);
            vpager.setCurrentItem(0);
            vpager.addOnPageChangeListener(this);

        }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_Recently:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_waste:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_Personal:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }
    /*@Override
    protected void onRestart()
    {
        super.onRestart();
        mAdapter.fleshPage();
    }*/
        //重写ViewPager页面切换的处理方法
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
            if (state == 2) {
                switch (vpager.getCurrentItem()) {
                    case PAGE_ONE:
                        rb_channel.setChecked(true);
                        break;
                    case PAGE_TWO:
                        rb_waste.setChecked(true);
                        break;
                    case PAGE_THREE:
                        rb_setting.setChecked(true);
                        break;
                }
            }
        }

    }
