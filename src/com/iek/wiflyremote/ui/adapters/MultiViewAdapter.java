package com.iek.wiflyremote.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MultiViewAdapter extends BaseAdapter {
	private List<View> mViewList;

	public MultiViewAdapter() {
		this.setViewList(new ArrayList<View>());
	}

	public MultiViewAdapter(List<View> viewList) {
		this.setViewList(viewList);
	}

	@Override
	public int getCount() {
		return mViewList.size();
	}

	@Override
	public Object getItem(int position) {
		return mViewList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mViewList.get(position);
	}

	public List<View> getViewList() {
		return mViewList;
	}

	public void setViewList(List<View> mViewList) {
		this.mViewList = mViewList;
	}

}
