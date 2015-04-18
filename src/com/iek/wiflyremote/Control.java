package com.iek.wiflyremote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Observer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.ui.NavigationDrawerFragment;
import com.iek.wiflyremote.ui.StatisticsFragment;

public class Control extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static final int MESSAGE_DATA_RECEIVE = 0;

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private Runnable listenThr = new Runnable() {

		@Override
		public void run() {
			InputStream in;
			try {
				in = M.m().getGlobalSocket().getInputStream();
				byte[] buff = new byte[1024];
				int bread = 0;

				while ((bread = in.read(buff)) != -1) {
					final ByteArrayOutputStream ostream = new ByteArrayOutputStream(
							1024);
					ostream.write(buff, 0, bread);
					try {
						final String s = ostream.toString("UTF-8").replace(
								"\n", "");
						Log.i("SVRRESP", s);
						runOnUiThread(new Runnable() {
							public void run() {
								Observer obs = M.m().getBoardRespObserver();
								if (obs != null) {
									obs.update(null, s);
								}

							}
						});
					} catch (UnsupportedEncodingException e) {
						Log.e("SVRRESP", e.getMessage());
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};

	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.control);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	protected void onStart() {
		super.onStart();
		connect();
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						new StatisticsFragment()).commit();
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
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (M.m().getGlobalSocket() == null) {
						M.m().setGlobalSocket(new Socket());
						M.m()
								.getGlobalSocket()
								.connect(
										new InetSocketAddress(M.m().getHost(),
												M.m().getPort()), 3000);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"Connect OK", Toast.LENGTH_SHORT)
										.show();
							}
						});
						new Thread(listenThr).start();
					}
				} catch (Exception e) {
					Log.i("CONNECT",
							e.getMessage() == null ? ":(" : e.getMessage());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Connect Failed", Toast.LENGTH_SHORT)
									.show();
						}

					});
					finish();
				}
			}
		}).start();
	}

}
