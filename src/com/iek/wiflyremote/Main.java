package com.iek.wiflyremote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iek.wiflyremote.stat.M;

public class Main extends Activity {
	EditText etxtIP, etxtPort;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		SharedPreferences settings = getSharedPreferences("IPAddress", 0);

		etxtIP = (EditText) findViewById(R.id.etxtIP);
		etxtIP.setText(settings.getString("IP", "192.168.1.132"));

		etxtPort = (EditText) findViewById(R.id.etxtPort);
		etxtPort.setText(settings.getString("PORT", "1016"));

		Button buttonConnect = (Button) findViewById(R.id.buttonConnect);
		buttonConnect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Control.class);
				intent.putExtra("IP", etxtIP.getText().toString());
				intent.putExtra("PORT", etxtPort.getText().toString());
				M.m().setHost(etxtIP.getText().toString());
				M.m().setPort(Integer.parseInt(etxtPort.getText().toString()));
				startActivity(intent);
			}
		});
	}

	public void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences("IPAddress", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("IP", etxtIP.getText().toString());
		editor.putString("PORT", etxtPort.getText().toString());
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Toast.makeText(this, "Rees com", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Toast.makeText(this, "Cerrando com", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Toast.makeText(this, "Aplicaci�n cerrada por OS",
		// Toast.LENGTH_SHORT).show();
	}
}
