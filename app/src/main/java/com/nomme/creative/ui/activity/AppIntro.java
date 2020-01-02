package com.nomme.creative.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.nomme.creative.R;
import com.nomme.creative.databinding.ActivityAppIntro2Binding;
import com.nomme.creative.preferences.SharedPrefrence;
import com.nomme.creative.ui.adapter.AppIntroPagerAdapter;
import com.nomme.creative.utils.ProjectUtils;


public class AppIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private AppIntroPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    public SharedPrefrence preference;
    private Context mContext;
    int[] mResources = {R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3};
    private ActivityAppIntro2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(AppIntro.this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_app_intro2);
        mContext = AppIntro.this;
        preference = SharedPrefrence.getInstance(mContext);



        binding.llSignin.setOnClickListener(this);
        binding.llSignup.setOnClickListener(this);


        mAdapter = new AppIntroPagerAdapter(AppIntro.this, mContext, mResources);
        binding.viewpager.setAdapter(mAdapter);
        binding.viewpager.setPageTransformer(true, new StackTransformer());
        binding.viewpager.setCurrentItem(0);
        binding.viewpager.setOnPageChangeListener(this);
        setPageViewIndicator();
    }

    private void setPageViewIndicator() {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    18,
                    18
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    binding.viewpager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            binding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }


        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {

        } else {

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void scrollPage(int position) {
        binding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.closeMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSignin:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
            case R.id.llSignup:
                startActivity(new Intent(mContext, SignUpActivity.class));
                finish();
                break;
        }
    }
}
