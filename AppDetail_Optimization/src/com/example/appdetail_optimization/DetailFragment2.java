package com.example.appdetail_optimization;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appdetail_optimization.AppDetailFragment.StickyScrollCallBack;

public class DetailFragment2 extends Fragment {
	private StickyListView listview;
	private StickyScrollCallBack scrollListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_detail2, null);
		initView(view);
		return view;
	}

	/**
	 * 初始化View
	 */
	private void initView(View view) {
		listview = (StickyListView) view.findViewById(R.id.listview);
		listview.setScrollCallBack(scrollListener);
		listview.setAdapter(adapter);
	}

	public void setScrollCallBack(StickyScrollCallBack scrollListener) {
		this.scrollListener = scrollListener;
	}

	public int getStickyHeight() {
		int scrollHeight = listview.getFirstViewScrollTop();
		if (scrollHeight > AppDetailFragment.STICKY_HEIGHT1) {
			return AppDetailFragment.STICKY_HEIGHT1;
		}
		return scrollHeight;
	}

	public void invalidScroll() {

	}

	private BaseAdapter adapter = new BaseAdapter() {
		private static final int VIEW_TYPE_EDIT = 0;
		private static final int VIEW_TYPE_TITLE = 1;
		private static final int VIEW_TYPE_CONTENT = 2;
		private static final int VIEW_TYPE_BOTTOM = 3;
		private static final int VIEW_TYPE_PREFIX = 4;

		LayoutInflater inflater;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == inflater) {
				inflater = LayoutInflater.from(getActivity());
			}

			int viewType = getItemViewType(position);
			View resultView = null;
			switch (viewType) {
			case VIEW_TYPE_PREFIX:
				// 这个相当于listview的header 跟stickyView的高度完全一致，但是被stickyView覆盖了
				TextView resultTv;
				if (convertView == null) {
					resultTv = new TextView(getActivity());
				} else {
					resultTv = (TextView) convertView;
				}
				resultTv.setHeight(AppDetailFragment.STICKY_HEIGHT2);
				resultView = resultTv;
				break;

			case VIEW_TYPE_EDIT:
				View editView;
				if (convertView == null) {
					editView = inflater.inflate(R.layout.edit_layout, null);
				} else {
					editView = convertView;
				}
				resultView = editView;
				break;
				
			case VIEW_TYPE_CONTENT:
				View normalView2;
				if (convertView == null) {
					normalView2 = inflater.inflate(R.layout.list_item, null);
				} else {
					normalView2 = convertView;
				}

				TextView tv2 = (TextView) normalView2
						.findViewById(R.id.textview);
				tv2.setText("评论内容 --> 这是ListView，第 " + position + "  项");
				resultView = normalView2;
				break;

			case VIEW_TYPE_TITLE:
				View normalView3;
				if (convertView == null) {
					normalView3 = inflater.inflate(R.layout.listview_title, null);
				} else {
					normalView3 = convertView;
				}
				
				resultView = normalView3;
				break;

			case VIEW_TYPE_BOTTOM:

				TextView resultTv2;
				if (convertView == null) {
					resultTv2 = new TextView(getActivity());
				} else {
					resultTv2 = (TextView) convertView;
				}
				resultTv2.setHeight(PixValue.dip.valueOf(60));
				resultView = resultTv2;
				break;
			default:
				break;
			}

			return resultView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return 50;
		}

		@Override
		public int getViewTypeCount() {
			return 5;
		};

		public int getItemViewType(int position) {
			if (position == 0) {
				return VIEW_TYPE_PREFIX;
			} else if (position == 1) {
				return VIEW_TYPE_EDIT;
			} else if (position == 2) {
				return VIEW_TYPE_TITLE;
			} else if (position == getCount() - 1) {
				return VIEW_TYPE_BOTTOM;
			} else {
				return VIEW_TYPE_CONTENT;
			}
		};
	};

	public void setStickyH(int stickyH) {

		if (Math.abs(stickyH - getStickyHeight()) < 5) {
			return;
		}

		listview.setSelectionFromTop(0, -stickyH);
	}
}
