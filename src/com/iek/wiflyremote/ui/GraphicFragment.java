package com.iek.wiflyremote.ui;

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

public class GraphicFragment extends Fragment {
	private ImageView drawigImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.graph_fragment, container, false);
		drawigImg = (ImageView) v.findViewById(R.id.imgGraphic);
		Bitmap bitmap = Bitmap.createBitmap((int) getActivity()
				.getWindowManager().getDefaultDisplay().getWidth(),
				(int) getActivity().getWindowManager().getDefaultDisplay()
						.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawigImg.setImageBitmap(bitmap);

		paint(canvas, 5, 150, 100, 210, Color.BLUE, 7);
		paint(canvas, 5, 150, 200, 310, Color.BLACK, 7);
		paint(canvas, 5, 150, 300, 410, Color.RED, 7);
		paint(canvas, 5, 150, 400, 510, Color.MAGENTA, 7);

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
