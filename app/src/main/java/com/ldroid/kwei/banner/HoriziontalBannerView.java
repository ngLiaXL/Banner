package com.ldroid.kwei.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ldroid.kwei.banner.pager.HackyViewPager;
import com.ldroid.kwei.banner.pager.PixelUtil;
import com.ldroid.kwei.banner.pager.ViewPager;

import java.util.ArrayList;


public class HoriziontalBannerView extends LinearLayout {

	class BannerAdapter extends PagerAdapter {

		private ArrayList<Integer> urls;

		public BannerAdapter() {
		}

		public void setListData(ArrayList<Integer> data) {
			this.urls = data;
			notifyDataSetChanged();
		}

		public Integer getItem(int position) {
			if (urls == null)
				return null;
			return urls.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		public int getListSize() {
			return urls != null ? urls.size() : 0;
		}

		@Override
		public int getCount() {
			if (urls != null && !urls.isEmpty()) {
				if (urls.size() == 1) {
					return 1;
				}
				return Integer.MAX_VALUE;
			}
			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = LayoutInflater.from(mContext).inflate(
					R.layout.layout_h_banner_item, view, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.niv_main);
			final Integer res = urls.get(position
					% (urls == null || urls.isEmpty() ? 1 : urls.size()));

			imageView.setImageResource(res);

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	private Context mContext;

	private final int FLIP_MSG = 1;
	private boolean mRunning = false;
	private boolean mStarted = false;
	private final long mFlipInterval = 2000;
	private Handler mHandler = new UpdateHandler();

	private BannerAdapter mAdapter;
	private HackyViewPager mViewPager;
	private ImageView[] mIndicatorPoint;
	private int mIndicatorIndex;

	private View mRootView;
	private LinearLayout mPointLayout;

	public HoriziontalBannerView(Context context) {
		super(context);
		this.mContext = context;

	}

	public HoriziontalBannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

	}

	public HoriziontalBannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void init() {
		if (mRootView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			mRootView = layoutInflater.inflate(R.layout.layout_h_banner, null);

			addView(mRootView);
			mPointLayout = (LinearLayout) mRootView.findViewById(R.id.linear);
			mViewPager = (HackyViewPager) mRootView.findViewById(R.id.hvp_pager);
			mAdapter = new BannerAdapter();
			mViewPager.setAdapter(mAdapter);
		}

	}

	public void setBannerData(ArrayList<Integer> bannerList) {
		mAdapter.setListData(bannerList);
		initIndicator();
		initBannerListener();
	}

	private void initIndicator() {
		mPointLayout.removeAllViews();
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(PixelUtil.dp2px(2.5f,mContext), PixelUtil.dp2px(5.5f,mContext),
				PixelUtil.dp2px(2.5f,mContext), PixelUtil.dp2px(10,mContext));

		mIndicatorPoint = new ImageView[mAdapter.getListSize()];
		for (int i = 0; i < mAdapter.getListSize(); i++) {
			mIndicatorPoint[i] = new ImageView(mContext);
			if (i == mIndicatorIndex) {
				mIndicatorPoint[i].setImageResource(R.mipmap.topic_banner_big);
			} else {
				mIndicatorPoint[i].setImageResource(R.mipmap.topic_banner_small);
			}
			mPointLayout.addView(mIndicatorPoint[i], layoutParams);
		}
	}

	private void initBannerListener() {
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mIndicatorIndex = arg0 % mAdapter.getListSize();
				for (int i = 0; i < mAdapter.getListSize(); i++) {
					if (i == mIndicatorIndex) {
						mIndicatorPoint[i].setImageResource(R.mipmap.topic_banner_big);
					} else {
						mIndicatorPoint[i].setImageResource(R.mipmap.topic_banner_small);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

	public void startFlipping() {
		mStarted = true;
		updateRunning(false);
	}

	public void stopFlipping() {
		mStarted = false;
		updateRunning(false);
	}

	private void updateRunning(boolean flipNow) {
		boolean running = mStarted;
		if (running != mRunning) {
			if (running) {
				Message msg = mHandler.obtainMessage(FLIP_MSG);
				mHandler.sendMessageDelayed(msg, mFlipInterval);
			} else {
				mHandler.removeMessages(FLIP_MSG);
			}
			mRunning = running;
		}
	}

	@SuppressLint("HandlerLeak")
	private class UpdateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == FLIP_MSG) {
				if (mRunning) {
					int size = mAdapter.getCount();
					if (size > 0) {
						int i = mViewPager.getCurrentItem();
						mViewPager.setCurrentItem(++i % size, true);
						msg = obtainMessage(FLIP_MSG);
						sendMessageDelayed(msg, mFlipInterval);
					}

				}
			}
		}
	}

}
