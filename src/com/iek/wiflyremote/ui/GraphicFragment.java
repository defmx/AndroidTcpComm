package com.iek.wiflyremote.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;

public class GraphicFragment extends Fragment {
	private ImageView drawigImg;
	private final int MARGIN = 25;
	private final int STEP = 5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.graph_fragment, container, false);
		drawigImg = (ImageView) v.findViewById(R.id.imgGraphic);
		int w = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		int h = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawigImg.setImageBitmap(bitmap);

		// List<Object[]> list = M.m().getLocaldb()
		// .select("statistics", " type=0");
		List<Object[]> list = new ArrayList<Object[]>();
		list.add(new Object[] { 0, 0, 10 });
		list.add(new Object[] { 0, 0, 20 });
		list.add(new Object[] { 0, 0, 30 });
		list.add(new Object[] { 0, 0, 40 });
		list.add(new Object[] { 0, 0, 50 });
		list.add(new Object[] { 0, 0, 10 });
		list.add(new Object[] { 0, 0, 20 });
		list.add(new Object[] { 0, 0, 30 });
		list.add(new Object[] { 0, 0, 40 });
		list.add(new Object[] { 0, 0, 50 });
		list.add(new Object[] { 0, 0, 10 });
		list.add(new Object[] { 0, 0, 20 });
		list.add(new Object[] { 0, 0, 30 });
		list.add(new Object[] { 0, 0, 40 });
		list.add(new Object[] { 0, 0, 50 });
		list.add(new Object[] { 0, 0, 10 });
		list.add(new Object[] { 0, 0, 20 });
		list.add(new Object[] { 0, 0, 30 });
		list.add(new Object[] { 0, 0, 40 });
		list.add(new Object[] { 0, 0, 50 });

		int xlim = w - MARGIN;
		int ylim = h - MARGIN;
		paint(canvas, MARGIN - 10, xlim, ylim, ylim, Color.BLACK, 1);
		paint(canvas, MARGIN, MARGIN, MARGIN, ylim + 10, Color.BLACK, 1);
		int step = 0;
		for (int i = 0; i < list.size(); i += 2) {
			int d0 = Integer.parseInt(list.get(i)[2].toString());
			int d1 = Integer.parseInt(list.get(i + 1)[2].toString());
			step += STEP;
			paint(canvas, MARGIN, MARGIN + step, ylim - d0, ylim - d1, Color.BLUE,
					2);
		}

		return v;
	}

	private void paint(Canvas canvas, int x0, int x1, int y0, int y1,
			int color, int width) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(width);
		canvas.drawLine(x0, y0, x1, y1, paint);
	}
}
