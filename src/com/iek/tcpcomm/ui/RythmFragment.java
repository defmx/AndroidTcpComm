package com.iek.tcpcomm.ui;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.BoardResponse;
import com.iek.tcpcomm.stat.M;

public class RythmFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.prodrythm_fragment, container, false);
		final EditText edit1 = (EditText) v.findViewById(R.id.avgspeedEdit);
		final EditText edit2 = (EditText) v.findViewById(R.id.deadtimeEdit);
		final EditText edit3 = (EditText) v.findViewById(R.id.instspeedEdit);
		final EditText edit4 = (EditText) v.findViewById(R.id.linmetEdit);
		edit1.setImeActionLabel(getString(R.string.send),
				KeyEvent.KEYCODE_ENTER);
		final InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		edit1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					edit1.clearFocus();
					imm.hideSoftInputFromWindow(edit1.getWindowToken(), 0);
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
											Toast.makeText(getActivity(),
													R.string.ok,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}
					}, "M" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit2.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit2.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					edit2.clearFocus();
					imm.hideSoftInputFromWindow(edit2.getWindowToken(), 0);
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
											Toast.makeText(getActivity(),
													R.string.ok,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}
					}, "V" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit3.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit3.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					edit3.clearFocus();
					imm.hideSoftInputFromWindow(edit3.getWindowToken(), 0);
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
											Toast.makeText(getActivity(),
													R.string.ok,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}
					}, "A" + v.getText());
					return true;
				}
				return false;
			}
		});
		edit4.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit4.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					edit4.clearFocus();
					imm.hideSoftInputFromWindow(edit4.getWindowToken(), 0);
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
											Toast.makeText(getActivity(),
													R.string.ok,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}
					}, "D" + v.getText());
					return true;
				}
				return false;
			}
		});
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
