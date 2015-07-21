package com.iek.wiflyremote.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;

public class GraphicFragment extends Fragment {
	private LineChart chart;
	private int w;
	private int h;
	private View v;
	private int showMode = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.graph_fragment, container, false);
		chart = (LineChart) v.findViewById(R.id.chart);

		w = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		h = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		setHasOptionsMenu(true);

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.graphfragment, menu);
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
		case R.id.mnuvar:
			varMenuClick();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		generateChart();
	}

	private void generateChart() {
		generateChart(0, 0);
	}

	private void generateChart(long timeInMillis, long timeInMillis2) {
		LineData data = new LineData();
		String where = " utime between %s and %s";
		if (timeInMillis == 0 || timeInMillis2 == 0) {
			where = "";
		} else {
			where = String.format(where, "" + timeInMillis, "" + timeInMillis2);
		}
		List<Object[]> tbl = M.m().getLocaldb().select("statistics", where, 100);
		for (int i = 0; i < tbl.size(); i++) {
			data.addXValue("");
		}
		ArrayList<LineDataSet> ds = getDataSet(tbl);
		for (LineDataSet d : ds) {
			data.addDataSet(d);
		}
		data.setDrawValues(false);
		chart.setDrawGridBackground(false);
		chart.setDrawMarkerViews(false);
		chart.setDescription("Estadísticas");
		chart.animateXY(2000, 2000);
		chart.setData(data);
		chart.invalidate();
	}

	private ArrayList<LineDataSet> getDataSet(List<Object[]> tbl) {
		ArrayList<LineDataSet> dataSets = null;

		ArrayList<Entry> valueSet1 = new ArrayList<Entry>();
		ArrayList<Entry> valueSet2 = new ArrayList<Entry>();
		ArrayList<Entry> valueSet3 = new ArrayList<Entry>();
		ArrayList<Entry> valueSet4 = new ArrayList<Entry>();
		for (int i = 0; i < tbl.size(); i++) {
			int xscale = i;
			if (showMode == 0 || showMode == 1) {
				valueSet3.add(new BarEntry(
						Float.parseFloat("" + tbl.get(i)[2]), xscale));
			}
			if (showMode == 0 || showMode == 2) {
				valueSet2.add(new BarEntry(
						Float.parseFloat("" + tbl.get(i)[3]), xscale));
			}
			if (showMode == 0 || showMode == 3) {
				valueSet1.add(new BarEntry(
						Float.parseFloat("" + tbl.get(i)[4]), xscale));
			}
			if (showMode == 0 || showMode == 4) {
				valueSet4.add(new BarEntry(
						Float.parseFloat("" + tbl.get(i)[5]), xscale));
			}
		}

		LineDataSet lineDataSet1 = new LineDataSet(valueSet1, "Tiempo muerto");
		lineDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet2 = new LineDataSet(valueSet2, "Vel promedio");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet3 = new LineDataSet(valueSet3, "Vel");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet4 = new LineDataSet(valueSet4, "Piezas");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

		dataSets = new ArrayList<LineDataSet>();
		dataSets.add(lineDataSet1);
		dataSets.add(lineDataSet2);
		dataSets.add(lineDataSet3);
		dataSets.add(lineDataSet4);
		return dataSets;
	}

	private void shareMenuClick() {
		OutputStream fOut = null;
		File imagePath = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ File.separator + "KinexWiMote" + File.separator);
		if (!imagePath.exists()) {
			imagePath.mkdir();
		}
		File file = new File(imagePath, "GE_" + System.currentTimeMillis()
				+ ".jpg");
		try {
			file.createNewFile();
			fOut = new FileOutputStream(file);
			chart.getChartBitmap().compress(Bitmap.CompressFormat.JPEG, 100,
					fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, file);
		shareIntent.setType("image/jpeg");
		startActivity(Intent.createChooser(shareIntent,
				getResources().getText(R.string.share)));
	}

	private void varMenuClick() {
		AlertDialog.Builder screenDialog = new AlertDialog.Builder(
				getActivity());
		screenDialog.setTitle("Mostrar");
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.graph_variables_menu, null);
		RadioButton rb1 = (RadioButton) v.findViewById(R.id.mnuv);
		RadioButton rb2 = (RadioButton) v.findViewById(R.id.mnud);
		RadioButton rb3 = (RadioButton) v.findViewById(R.id.mnudt);
		RadioButton rb5 = (RadioButton) v.findViewById(R.id.mnuvm);
		OnCheckedChangeListener chkListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					switch (buttonView.getId()) {
					case R.id.mnudt:
						showMode = 3;
						break;
					case R.id.mnuv:
						showMode = 1;
						break;
					case R.id.mnuvm:
						showMode = 2;
						break;
					case R.id.mnud:
						showMode = 4;
						break;
					}
				}
				generateChart();
			}
		};
		rb1.setOnCheckedChangeListener(chkListener);
		rb2.setOnCheckedChangeListener(chkListener);
		rb3.setOnCheckedChangeListener(chkListener);
		rb5.setOnCheckedChangeListener(chkListener);

		screenDialog.setView(v);
		screenDialog.create();
		screenDialog.show();
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
				generateChart(c1.getTimeInMillis(), c2.getTimeInMillis());
			}
		});
		screenDialog.setView(v);
		screenDialog.create();
		screenDialog.show();
	}
}
