package com.iek.wiflyremote.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FillFormatter;
import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.stat.Table;

public class GraphicFragment extends Fragment {
	private LineChart chart;
	private final int MARGIN = 25;
	private Canvas canvas;
	private int w;
	private int h;
	private View v;

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
		canvas = new Canvas(bitmap);

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		LineData data = new LineData();
		List<Object[]> tbl = M.m().getLocaldb().select("statistics", "");
		for (int i = 0; i < tbl.size(); i++) {
			data.addXValue("");
		}
		ArrayList<LineDataSet> ds = getDataSet(tbl);
		for (LineDataSet d : ds) {
			data.addDataSet(d);
		}
		chart.setData(data);
		chart.setDescription("Estadísticas");
		chart.animateXY(2000, 2000);
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
			valueSet1.add(new BarEntry(Float.parseFloat("" + tbl.get(i)[4]),
					xscale));

			valueSet2.add(new BarEntry(Float.parseFloat("" + tbl.get(i)[3]),
					xscale));

			valueSet3.add(new BarEntry(Float.parseFloat("" + tbl.get(i)[2]),
					xscale));

			valueSet4.add(new BarEntry(Float.parseFloat("" + tbl.get(i)[5]),
					xscale));
		}

		LineDataSet lineDataSet1 = new LineDataSet(valueSet1, "Vel");
		lineDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet2 = new LineDataSet(valueSet2, "Vel media");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet3 = new LineDataSet(valueSet3, "Tiempo muerto");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
		LineDataSet lineDataSet4 = new LineDataSet(valueSet4, "Distancia");
		lineDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

		dataSets = new ArrayList<LineDataSet>();
		dataSets.add(lineDataSet1);
		dataSets.add(lineDataSet2);
		dataSets.add(lineDataSet3);
		dataSets.add(lineDataSet4);
		return dataSets;
	}

	private ArrayList<String> getXAxisValues() {
		ArrayList<String> xAxis = new ArrayList();
		xAxis.add("JAN");
		xAxis.add("FEB");
		xAxis.add("MAR");
		xAxis.add("APR");
		xAxis.add("MAY");
		xAxis.add("JUN");
		return xAxis;
	}
}
