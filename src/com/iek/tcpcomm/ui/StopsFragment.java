package com.iek.tcpcomm.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.CatRow;
import com.iek.tcpcomm.stat.M;

public class StopsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.stops_fragment, container, false);
		final EditText e1 = (EditText) v.findViewById(R.id.addStopCauseEdit);
		final Spinner t1 = (Spinner) v
				.findViewById(R.id.existingReasonsSpinner);
		e1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == 6) {
					ContentValues cv = new ContentValues();
					cv.put("name", e1.getText().toString());
					M.m().getLocaldb().insOrUpd("cstopreasons", cv, "");
					retrieveReasons(t1);
					Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT)
							.show();
					;
					return true;
				}
				return false;
			}
		});
		retrieveReasons(t1);
		t1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		retrieveReasons(t1);
		return v;
	}

	private void retrieveReasons(Spinner spinner) {
		List<CatRow> s = M.m().getLocaldb()
				.selectCat("cstopreasons");
		List<String> lreasons = new ArrayList<String>();
		for (int i = 0; i < s.size(); i++) {
			lreasons.add(s.get(i).getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, lreasons);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
