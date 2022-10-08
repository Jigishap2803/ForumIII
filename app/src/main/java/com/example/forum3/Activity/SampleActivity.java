package com.example.forum3.Activity;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.example.forum3.Fragment.DashboardFragment;
import com.example.forum3.Menu.DrawerAdapter;
import com.example.forum3.Menu.DrawerItem;
import com.example.forum3.Menu.SimpleItem;
import com.example.forum3.Menu.SpaceItem;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
import com.example.forum3.Utils.SlidingRootNavBuilder;
import com.example.forum3.Utils.SlidingRootNavLayout;

import java.util.Arrays;

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_CART = 2;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private ImageView notification_iv;

    private SlidingRootNavLayout slidingRootNav;
    private PreferenceManager preferenceManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sample);
        preferenceManager = new PreferenceManager(this);
        notification_iv = (ImageView) findViewById(R.id.notification_iv);

        notification_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SampleActivity.this, NotificationListActivity.class);
                startActivity(intent);
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter( Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_CART),
                new SpaceItem(48)));
        adapter.setListener(this::onItemSelected);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    @Override
    public void onItemSelected(int position) {
        Fragment selectedScreen = DashboardFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);

        ImageView cancel_icon = findViewById(R.id.cancel_icon);
        cancel_icon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                slidingRootNav.closeMenu();
            }
        } );

        if(position==0){
            Fragment homeScreen = DashboardFragment.createFor(screenTitles[position]);
            showFragment(homeScreen);
            slidingRootNav.closeMenu();
        }else if(position==1){
            Intent intent = new Intent(SampleActivity.this,ProfileActivity.class);
            startActivity(intent);
            slidingRootNav.closeMenu();
        }else if(position==2){
            Intent intent = new Intent(getApplicationContext(), ChooseLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            preferenceManager.putPreferenceValues( Preference_Constant.IS_LOGGED_IN,"0");
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onBackPressed () {
        exit_pop_dialog();
    }

    private void exit_pop_dialog(){
        Dialog dialog = new Dialog( SampleActivity.this);
        dialog.setContentView(R.layout.exit_pop_up_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        LottieAnimationView lav_success = (LottieAnimationView) dialog.findViewById(R.id.lav_success);
        TextView cancel_tv = (TextView)dialog.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView)dialog.findViewById(R.id.confirm_tv);
        cancel_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                dialog.dismiss();
            }
        } );

        confirm_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
                dialog.dismiss();
            }
        } );
        lav_success.playAnimation();
        dialog.show();
    }

    }
