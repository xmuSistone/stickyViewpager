package com.example.appdetail_optimization;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

@SuppressLint("ValidFragment")
public class AppDetailFragment extends Fragment {
	private ViewPager viewpager;
	private FragmentManager fragmentManager;
	private LinearLayout stickyView, navLayout;
	private DetailFragment1 detailFragment1;
	private DetailFragment2 detailFragment2;
	private TextView placeTv, maskTv, titleLocTv;
	private int notifyBarHeight = 0;
	private StickyScrollCallBack scrollListener;
	private PagerSlidingTabStrip tabLayout;
	private View backBtn, bottomLayout;

	public static int STICKY_HEIGHT1; // height1是代表从顶部到tab的距离
	public static int STICKY_HEIGHT2; // height2是代表从顶部到viewpager的距离

	public AppDetailFragment(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.main_detail, container, false);
		initView(rootView);
		initBarHeight();

		return rootView;
	}

	private void initView(View view) {
		viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		maskTv = (TextView) view.findViewById(R.id.mask_tv);
		placeTv = (TextView) view.findViewById(R.id.place_tv);
		navLayout = (LinearLayout) view.findViewById(R.id.nav_layout);
		titleLocTv = (TextView) view.findViewById(R.id.title_location);
		tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		backBtn = view.findViewById(R.id.back_btn);
		bottomLayout = view.findViewById(R.id.bottom_layout);
		stickyView = (LinearLayout) view.findViewById(R.id.sticky_view);
		stickyView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		placeTv.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		STICKY_HEIGHT2 = stickyView.getMeasuredHeight();
		STICKY_HEIGHT1 = stickyView.getChildAt(0).getMeasuredHeight();

		Log.i("LeiTest", "height1=" + STICKY_HEIGHT1 + " height2="
				+ STICKY_HEIGHT2);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		// 这是一个回调 滚动条滑动的时候，会调用onScrollChanged函数
		scrollListener = new StickyScrollCallBack() {

			@Override
			public void onScrollChanged(int scrollY) {
				processStickyTranslateY(scrollY);
			}

			@Override
			public int getCurrentViewpagerItem() {
				return viewpager.getCurrentItem();
			}
		};

		detailFragment1 = new DetailFragment1();
		detailFragment2 = new DetailFragment2();

		detailFragment1.setScrollCallBack(scrollListener);
		detailFragment2.setScrollCallBack(scrollListener);

		ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(
				fragmentManager);
		adapter.addFragment(detailFragment1);
		adapter.addFragment(detailFragment2);
		viewpager.setOffscreenPageLimit(3);
		viewpager.setAdapter(adapter);
		
		ViewPagerStateListener viewpagerStateListener = new ViewPagerStateListener() {
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == 1) {
					downSelect = viewpager.getCurrentItem();
					detailFragment1.invalidScroll();
					detailFragment2.invalidScroll();

					if (downSelect == 0) {
						int tempH1 = detailFragment1.getStickyHeight();
						int stickyH2 = 0;
						if (tempH1 > STICKY_HEIGHT1 / 2) {
							stickyH2 = STICKY_HEIGHT1;
						}
						
						detailFragment2.setStickyH(stickyH2);
					} else {
						int tempH2 = detailFragment2.getStickyHeight();
						int stickyH1 = 0;
						if (tempH2 > STICKY_HEIGHT1 / 2) {
							stickyH1 = STICKY_HEIGHT1;
						}
						detailFragment1.setStickyH(stickyH1);
					}

				}
			}
		};
		
		tabLayout.setViewPagerStateListener(viewpagerStateListener);
		
		tabLayout.setViewPager(viewpager);
	}

	private void initBarHeight() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getActivity().getResources()
					.getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		notifyBarHeight = statusBarHeight;
	}

	private int downSelect = 0;
	private int lastProcessStickyTranslateY = 0;
	private int navBottomPos, locTvTopPosX, locTvTopPosY, titleHeight;

	@SuppressLint("NewApi")
	private void processStickyTranslateY(int translateY) {
		if (translateY == Integer.MIN_VALUE
				|| translateY == lastProcessStickyTranslateY) {
			return;
		}

		if (translateY < -STICKY_HEIGHT1 + 10) {
			bottomLayout.setVisibility(View.VISIBLE);
		}
		else {
			bottomLayout.setVisibility(View.GONE);
		}
		
		lastProcessStickyTranslateY = translateY;

		stickyView.setTranslationY(translateY);
		int[] location = new int[2];
		placeTv.getLocationInWindow(location);

		if (navBottomPos == 0 || locTvTopPosY == 0) {
			locTvTopPosX = titleLocTv.getLeft();
			locTvTopPosY = titleLocTv.getTop();
			navBottomPos = navLayout.getBottom();
			titleHeight = titleLocTv.getMeasuredHeight();
		}

		int locationX = location[0];

		int locationY = location[1] - PixValue.dip.valueOf(50)
				+ notifyBarHeight;
		if (locationY < locTvTopPosY) {
			locationY = locTvTopPosY;
		}

		if (locationY < navBottomPos - titleHeight) {
			locationX = locationX
					- ((navBottomPos - titleHeight - locationY) * 2);
			if (locationX < locTvTopPosX) {
				locationX = locTvTopPosX;
			}
		}

		ViewHelper.setX(maskTv, locationX);
		ViewHelper.setY(maskTv, locationY);
	}

	public interface StickyScrollCallBack {
		public void onScrollChanged(int scrollY);
		public int getCurrentViewpagerItem();
	}
	
	public interface ViewPagerStateListener {
		public void onPageScrollStateChanged(int state);
	}

}
