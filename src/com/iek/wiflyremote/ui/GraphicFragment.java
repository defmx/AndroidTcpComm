package com.iek.wiflyremote.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private Canvas canvas;
	private int w;
	private int h;
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.graph_fragment, container, false);
		drawigImg = (ImageView) v.findViewById(R.id.imgGraphic);

		w = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		h = (int) getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		drawigImg.setImageBitmap(bitmap);

		// List<Object[]> list = M.m().getLocaldb().select("statistics",
		// " type=0");

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (getActivity() == null) {
						return;
					}
					final List<Object[]> list = getValues();
					final int xlim = w - MARGIN;
					final int ylim = h - MARGIN;
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							paint(canvas, MARGIN - 10, ylim, xlim, ylim,
									Color.BLACK, 1);
							paint(canvas, MARGIN, MARGIN, MARGIN, ylim + 10,
									Color.BLACK, 1);
							for (int i = 0; i < list.size(); i++) {
								if (i + 1 < list.size()) {
									int x0 = i;
									int y0 = Integer.parseInt(list.get(i)[1]
											.toString());
									int x1 = i + 1;
									int y1 = Integer.parseInt(list.get(i + 1)[1]
											.toString());
									paint(canvas, MARGIN + x0, ylim - y0,
											MARGIN + x1, ylim - y1, Color.BLUE,
											1);
								}
							}
							v.invalidate();
						}
					});
					try {
						Thread.sleep(300);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								canvas.drawColor(Color.WHITE);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private List<Object[]> getValues() {
		List<Object[]> list = new ArrayList<Object[]>();
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			list.add(new Object[] { 0, r.nextInt(400) });
		}

		return list;
	}

	private void paint(Canvas canvas, int x0, int y0, int x1, int y1,
			int color, int width) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(width);
		canvas.drawLine(x0, y0, x1, y1, paint);
	}
}
