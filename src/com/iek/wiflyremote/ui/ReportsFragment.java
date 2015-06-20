package com.iek.wiflyremote.ui;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.iek.wiflyremote.R;

public class ReportsFragment extends Fragment {

	private String mText;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.reportmenuitems,
				android.R.layout.simple_spinner_item);
		actionBar.setListNavigationCallbacks(adapter,
				new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						switch (itemPosition) {
						case 0:
							
							break;
						}

						return true;
					}
				});
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mText = getTag();
	}
}
