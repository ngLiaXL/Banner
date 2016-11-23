package com.ldroid.kwei.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HoriziontalBannerView hsv = (HoriziontalBannerView) findViewById(R.id.h_banner);
        hsv.init() ;

        ArrayList data = new ArrayList() ;
        data.add(R.drawable.banner_1) ;
        data.add(R.drawable.banner_2) ;
        data.add(R.drawable.banner_3) ;

        hsv.setBannerData(data);

        hsv.startFlipping();

    }
}
