package com.example.appdetail_optimization;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments = new ArrayList<Fragment>();

	public ViewPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

	public List<Fragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}

	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 0) {
			return "¼ò½é";
		}
		else {
			return "ÆÀÂÛ";
		}
	}
}
