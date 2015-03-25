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

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.M;

public class RythmFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.prodrythm_fragment, container, false);
		EditText edit1 = (EditText) v.findViewById(R.id.avgspeedEdit);
		EditText edit2 = (EditText) v.findViewById(R.id.deadtimeEdit);
		EditText edit3 = (EditText) v.findViewById(R.id.instspeedEdit);
		EditText edit4 = (EditText) v.findViewById(R.id.linmetEdit);
		edit1 = new EditText(getActivity());
		edit1.setImeActionLabel(getString(R.string.send),
				KeyEvent.KEYCODE_ENTER);
		edit1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					M.m().sendMessage(new Observer() {

						@Override
						public void update(Observable observable, Object data) {

						}
					}, "M" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit2 = new EditText(getActivity());
		edit2.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit2.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					M.m().sendMessage(new Observer() {

						@Override
						public void update(Observable observable, Object data) {
							// TODO Auto-generated method stub

						}
					}, "V" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit3 = new EditText(getActivity());
		edit3.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit3.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					M.m().sendMessage(new Observer() {

						@Override
						public void update(Observable observable, Object data) {
							// TODO Auto-generated method stub

						}
					}, "A" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit4 = new EditText(getActivity());
		edit4.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit4.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					M.m().sendMessage(new Observer() {

						@Override
						public void update(Observable observable, Object data) {
							// TODO Auto-generated method stub

						}
					}, "D" + v.getText());
					return true;
				}
				return false;
			}
		});
		return v;
	}
}
