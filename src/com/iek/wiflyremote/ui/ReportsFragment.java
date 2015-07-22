package com.iek.wiflyremote.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.ui.adapters.MultiViewAdapter;

public class ReportsFragment extends Fragment {

	View v;
	View v1;
	View v2;
	LayoutInflater inflaterG;
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				getActivity(), R.array.reportmenuitems,
//				android.R.layout.simple_spinner_item);
//		actionBar.setListNavigationCallbacks(adapter,
//				new OnNavigationListener() {
//
//					@Override
//					public boolean onNavigationItemSelected(int itemPosition,
//							long itemId) {
//						switch (itemPosition) {
//						case 0:
//
//							break;
//						}
//
//						return true;
//					}
//				});

		Calendar cn1 = Calendar.getInstance();
		cn1.set(Calendar.HOUR_OF_DAY, 0);
		cn1.set(Calendar.MINUTE, 0);
		Calendar cn2 = Calendar.getInstance();
		cn2.set(Calendar.HOUR_OF_DAY, 23);
		cn2.set(Calendar.MINUTE, 59);
		String where = String.format(  " start_time between %s and %s", cn1.getTimeInMillis()/1000, cn2.getTimeInMillis()/1000);
		inflaterG = inflater;
		v = inflater.inflate(R.layout.simple_list_view, null);
		ListView list = (ListView) v.findViewById(R.id.listView);
		List<View> vList = new ArrayList<View>();
		List<Object[]> regList = M.m().getLocaldb().select("stops",where, 200);
		v1 = inflater.inflate(R.layout.stops_row, null);
		TextView hid = (TextView) v1.findViewById(R.id.txStopId);
		TextView hst = (TextView) v1.findViewById(R.id.txStopStart);
		TextView hend = (TextView) v1.findViewById(R.id.txStopEnd);
		TextView hreason = (TextView) v1.findViewById(R.id.txStopReason);
		TextView hlen = (TextView) v1.findViewById(R.id.txStopLength);
		hid.setText("");
		hst.setText( "   Inicio      ");
		hend.setText("              Fin         ");
        hreason.setText("                 Razón    ");
		hlen.setText("    Duración    ");
		vList.add(v1);
		for (Object[] o : regList) {
			v2 = inflater.inflate(R.layout.stops_row, null);
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
				c1.setTimeInMillis(l1*1000);
				c2.setTimeInMillis(l2*1000);
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
				double len_val = (double)(l2 - l1)/60;         
				len.setText(String.format("%.2f min",len_val));
				vList.add(v2);
			} catch (NumberFormatException e) {
				Log.e(":o", e.getMessage());
			}
		}
		MultiViewAdapter adapter2 = new MultiViewAdapter(vList);
		list.setAdapter(adapter2);
		adapter2.notifyDataSetChanged();
		setHasOptionsMenu(true);
		return v;
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.reportfragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnurange:
			rangeMenuClick();
			break;
		case R.id.mnushare:
			shareMenuClick();
			break;
			}

		return super.onOptionsItemSelected(item);
	}
	
	private void shareMenuClick() {
//		OutputStream fOut = null;
//		File imagePath = new File(
//				Environment
//						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//						+ File.separator + "KinexWiMote" + File.separator);
//		if (!imagePath.exists()) {
//			imagePath.mkdir();
//		}
//		File file = new File(imagePath, "GE_" + System.currentTimeMillis()
//				+ ".jpg");
//		try {
//			file.createNewFile();
//			fOut = new FileOutputStream(file);
//			chart.getChartBitmap().compress(Bitmap.CompressFormat.JPEG, 100,
//					fOut);
//			fOut.flush();
//			fOut.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		Intent shareIntent = new Intent();
//		shareIntent.setAction(Intent.ACTION_SEND);
//		shareIntent.putExtra(Intent.EXTRA_STREAM, file);
//		shareIntent.setType("image/jpeg");
//		startActivity(Intent.createChooser(shareIntent,
//				getResources().getText(R.string.share)));
	}

	private void rangeMenuClick() {
		AlertDialog.Builder screenDialog = new AlertDialog.Builder(
				getActivity());
		screenDialog.setTitle("Filtrar");
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.date_range_picker, null);
		final DatePicker dtFrom = (DatePicker) v.findViewById(R.id.datefrom);
		final DatePicker dtTo = (DatePicker) v.findViewById(R.id.dateto);
		Button btn = (Button) v.findViewById(R.id.btnaccept);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.DAY_OF_MONTH, dtFrom.getDayOfMonth());
				c1.set(Calendar.MONTH, dtFrom.getMonth());
				c1.set(Calendar.YEAR, dtFrom.getYear());
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(Calendar.DAY_OF_MONTH, dtTo.getDayOfMonth());
				c2.set(Calendar.MONTH, dtTo.getMonth());
				c2.set(Calendar.YEAR, dtTo.getYear());
				c2.set(Calendar.HOUR_OF_DAY, 23);
				c2.set(Calendar.MINUTE, 59);
				generateReport(c1.getTimeInMillis(), c2.getTimeInMillis());
			}
		});
		screenDialog.setView(v);
		screenDialog.create();
		screenDialog.show();
	}
			


	protected void generateReport(long timeInMillis, long timeInMillis2) {
		
		String where = String.format(  " start_time between %s and %s", timeInMillis/1000, timeInMillis2/1000);
		
		v = inflaterG.inflate(R.layout.simple_list_view, null);
		ListView list = (ListView) v.findViewById(R.id.listView);
		List<View> vList = new ArrayList<View>();
		List<Object[]> regList = M.m().getLocaldb().select("stops",where, 200);
		v1 = inflaterG.inflate(R.layout.stops_row, null);
		TextView hid = (TextView) v1.findViewById(R.id.txStopId);
		TextView hst = (TextView) v1.findViewById(R.id.txStopStart);
		TextView hend = (TextView) v1.findViewById(R.id.txStopEnd);
		TextView hreason = (TextView) v1.findViewById(R.id.txStopReason);
		TextView hlen = (TextView) v1.findViewById(R.id.txStopLength);
		hid.setText("");
		hst.setText( "   Inicio      ");
		hend.setText("              Fin         ");
        hreason.setText("                 Razón    ");
		hlen.setText("    Duración    ");
		vList.add(v1);
		for (Object[] o : regList) {
			v2 = inflaterG.inflate(R.layout.stops_row, null);
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
				c1.setTimeInMillis(l1*1000);
				c2.setTimeInMillis(l2*1000);
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
				double len_val = (double)(l2 - l1)/60;         
				len.setText(String.format("%.2f min",len_val));
				vList.add(v2);
			} catch (NumberFormatException e) {
				Log.e(":o", e.getMessage());
			}
		}
		MultiViewAdapter adapter2 = new MultiViewAdapter(vList);
		list.setAdapter(adapter2);
		adapter2.notifyDataSetChanged();
		
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
