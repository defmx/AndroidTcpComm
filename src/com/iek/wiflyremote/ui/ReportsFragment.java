package com.iek.wiflyremote.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.ui.adapters.MultiViewAdapter;

public class ReportsFragment extends Fragment {

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
		View v = inflater.inflate(R.layout.simple_list_view, container, false);
		ListView list = (ListView) v.findViewById(R.id.listView);
		List<View> vList = new ArrayList<View>();
		List<Object[]> regList = M.m().getLocaldb().select("stops", "", 0);
		for (Object[] o : regList) {
			View v2 = inflater.inflate(R.layout.stops_row, container,false);
			TextView id = (TextView) v2.findViewById(R.id.txStopId);
			TextView st = (TextView) v2.findViewById(R.id.txStopStart);
			TextView end = (TextView) v2.findViewById(R.id.txStopEnd);
			TextView reason = (TextView) v2.findViewById(R.id.txStopReason);
			TextView len = (TextView) v2.findViewById(R.id.txStopLength);
			id.setText("" + o[0]);
			st.setText("" + o[1]);
			end.setText("" + o[2]);
			reason.setText("" + o[3]);
			len.setText("" + o[4]);
			vList.add(v2);
		}
		list.setAdapter(new MultiViewAdapter());

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
