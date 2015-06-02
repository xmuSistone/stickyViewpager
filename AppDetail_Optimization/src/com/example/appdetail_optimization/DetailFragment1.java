package com.example.appdetail_optimization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.appdetail_optimization.AppDetailFragment.StickyScrollCallBack;

public class DetailFragment1 extends Fragment {
	private StickyScrollView scrollView;
	private TextView descriptionTv;
	private StickyScrollCallBack scrollListener;
	private HorizontalListView mHlvCustomList;

	private CustomData[] mCustomData = new CustomData[] {
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray") };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_detail1, null);
		initView(view);
		return view;
	}

	/**
	 * ³õÊ¼»¯View
	 */
	private void initView(View view) {
		scrollView = (StickyScrollView) view.findViewById(R.id.scrollview);
		View placeHolder = view.findViewById(R.id.placeHolder);
		descriptionTv = (TextView) view.findViewById(R.id.description);
		descriptionTv.setText(getDescripTxt());

		LayoutParams lp = (LayoutParams) placeHolder.getLayoutParams();
		lp.height = AppDetailFragment.STICKY_HEIGHT2;
		scrollView.setScrollCallBack(scrollListener);

		mHlvCustomList = (HorizontalListView) view
				.findViewById(R.id.hlvCustomList);
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
				mCustomData);
		mHlvCustomList.setAdapter(adapter);
	}

	private CharSequence getDescripTxt() {
		String content = "";
		try {
			InputStream is = getActivity().getAssets().open("description.txt");
			if (is != null) {
				DataInputStream dIs = new DataInputStream(is);
				int length = dIs.available();
				byte[] buffer = new byte[length];
				dIs.read(buffer);
				content = EncodingUtils.getString(buffer, "UTF-8");
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public void setScrollCallBack(StickyScrollCallBack scrollListener) {
		this.scrollListener = scrollListener;
	}

	public int getStickyHeight() {
		int scrollHeight = scrollView.getScrollY();
		if (scrollHeight > AppDetailFragment.STICKY_HEIGHT1) {
			return AppDetailFragment.STICKY_HEIGHT1;
		}
		return scrollHeight;
	}

	public void invalidScroll() {
		scrollView.invalidScroll();
	}

	public void setStickyH(int stickyH) {
		if (Math.abs(stickyH - getStickyHeight()) < 10) {
			return;
		}

		scrollView.scrollTo(0, stickyH);
	}
}
