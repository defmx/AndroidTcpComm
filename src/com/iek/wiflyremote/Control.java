package com.iek.wiflyremote;

import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.stat.Receiver3;
import com.iek.wiflyremote.ui.GoalsFragment;
import com.iek.wiflyremote.ui.GraphicFragment;
import com.iek.wiflyremote.ui.NavigationDrawerFragment;
import com.iek.wiflyremote.ui.PreferenceFragment;
import com.iek.wiflyremote.ui.StatisticsFragment;

public class Control extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static final int MESSAGE_DATA_RECEIVE = 0;

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;
	private ProgressBar mprogBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.control);
		mprogBar = (ProgressBar) findViewById(R.id.progressbar);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	protected void onStart() {
		super.onStart();
		M.m().connect(new Observer() {

			@Override
			public void update(Observable observable, Object data) {
				if (data.equals("ok")) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mprogBar.setVisibility(View.GONE);
							Toast.makeText(getApplicationContext(),
									"Conectado", Toast.LENGTH_SHORT).show();
						}
					});
					

					Intent intent = new Intent(Control.this, Receiver3.class);
					intent.putExtra("id", 1);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							Control.this, 1, intent, Intent.FILL_IN_DATA);
					AlarmManager alarm = (AlarmManager) Control.this
							.getSystemService(Context.ALARM_SERVICE);
					alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, 120000,
							pendingIntent);					
					M.m().sendMessage(null, "Q");
				} else if (data.equals("f")) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Conexión no establecida", Toast.LENGTH_SHORT)
									.show();
						}

					});
					finish();
				}
			}
		});
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment f = null;
		switch (position) {
		case 0:
			f = new StatisticsFragment();
			break;
		case 1:
			f = new GoalsFragment();
			break;
		case 2:
			f = new GraphicFragment();
			break;
		case 3:
			f = new PreferenceFragment();
			break;
		default:
			return;
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, f).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section4);
			break;
		}
		getActionBar().setTitle(mTitle);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
