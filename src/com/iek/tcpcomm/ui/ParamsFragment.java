package com.iek.tcpcomm.ui;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.BoardResponse;
import com.iek.tcpcomm.stat.M;

public class ParamsFragment extends Fragment {
	private TextView mtextFactor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.params_fragment, container, false);
		final EditText editFactor = (EditText) v
				.findViewById(R.id.wheelDiamEdit);
		mtextFactor = (TextView) v.findViewById(R.id.wheelDiamText);

		editFactor.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == 6) {
					M.m().sendMessage(null, "F=" + editFactor.getText());
					M.m().sendMessage(new Observer() {

						@Override
						public void update(Observable observable, Object data) {
							if (data instanceof BoardResponse) {
								BoardResponse br = (BoardResponse) data;
								final String s = br.getMessage();
								if (s != null && !s.equals("")) {
									getActivity().runOnUiThread(new Runnable() {

										@Override
										public void run() {
											mtextFactor.setText(s);
											Toast.makeText(getActivity(),
													R.string.ok,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}
					}, "f");
				}
				return false;
			}
		});

		return v;
	}
}
