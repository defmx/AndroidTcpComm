package com.iek.wiflyremote.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
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
		View v = inflater.inflate(R.layout.simple_list_view, null);
		ListView list = (ListView) v.findViewById(R.id.listView);
		List<View> vList = new ArrayList<View>();
		List<Object[]> regList = M.m().getLocaldb().select("stops", "", 0);
		View v1 = inflater.inflate(R.layout.stops_row, null);
		TextView hid = (TextView) v1.findViewById(R.id.txStopId);
		TextView hst = (TextView) v1.findViewById(R.id.txStopStart);
		TextView hend = (TextView) v1.findViewById(R.id.txStopEnd);
		TextView hreason = (TextView) v1.findViewById(R.id.txStopReason);
		TextView hlen = (TextView) v1.findViewById(R.id.txStopLength);
		hid.setText("");
		hst.setText("Inicio");
		hend.setText("Fin");
		hreason.setText("Razón");
		hlen.setText("Duración");
		vList.add(v1);
		for (Object[] o : regList) {
			View v2 = inflater.inflate(R.layout.stops_row, null);
			TextView id = (TextView) v2.findViewById(R.id.txStopId);
			TextView st = (TextView) v2.findViewById(R.id.txStopStart);
			TextView end = (TextView) v2.findViewById(R.id.txStopEnd);
			TextView reason = (TextView) v2.findViewById(R.id.txStopReason);
			TextView len = (TextView) v2.findViewById(R.id.txStopLength);

			reason.setText(o[3] == null ? "(Sin razón)" : o[3] + "");
			try {
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				Long l1 = Long.parseLong("" + o[1]);
				Long l2 = Long.parseLong("" + o[2]);
				c1.setTimeInMillis(l1);
				c2.setTimeInMillis(l2);
				id.setText("" + o[0]);
				st.setText(String.format("%02d/%02d/%02d %02d:%02d",
						c1.get(Calendar.MONTH) + 1,
						c1.get(Calendar.DAY_OF_MONTH), c1.get(Calendar.YEAR),
						c1.get(Calendar.HOUR), c1.get(Calendar.MINUTE),
						c1.get(Calendar.SECOND)));
				end.setText(String.format("%02d/%02d/%02d %02d:%02d",
						c2.get(Calendar.MONTH) + 1,
						c2.get(Calendar.DAY_OF_MONTH), c2.get(Calendar.YEAR),
						c2.get(Calendar.HOUR), c2.get(Calendar.MINUTE),
						c2.get(Calendar.SECOND)));
				len.setText((l2 - l1) / 1000 + " s");
				vList.add(v2);
			} catch (NumberFormatException e) {
				Log.e(":o", e.getMessage());
			}
		}
		MultiViewAdapter adapter2 = new MultiViewAdapter(vList);
		list.setAdapter(adapter2);
		adapter2.notifyDataSetChanged();

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
