# Banner
Android ViewPager实现的简单图片轮播


  <com.ldroid.kwei.banner.HoriziontalBannerView
        android:id="@+id/h_banner"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal" />
      
      
       HoriziontalBannerView hsv = (HoriziontalBannerView) findViewById(R.id.h_banner);
        hsv.init() ;

        ArrayList data = new ArrayList() ;
        data.add(R.drawable.banner_1) ;
        data.add(R.drawable.banner_2) ;
        data.add(R.drawable.banner_3) ;

        hsv.setBannerData(data);

        hsv.startFlipping();
