package com.iek.tcpcomm.ui.adapters;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MultipleViewAdapter extends BaseAdapter {
	private List<View> m_viewList;

	public MultipleViewAdapter() {
	}

	@Override
	public int getCount() {
		return m_viewList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return m_viewList.get(position);
	}

	public List<View> getViewList() {
		return m_viewList;
	}

	public void setViewList(List<View> viewList) {
		this.m_viewList = viewList;
	}

}
