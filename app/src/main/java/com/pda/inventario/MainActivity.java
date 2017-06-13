package com.pda.inventario;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.util.LogWriter;
import android.support.v7.app.ActionBarActivity;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.DbOpenHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Context mContext = null;
//		try {
//			mContext = createPackageContext("br.com.pdasolucoes.connectfood",0);
//		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//			Log.e("Not data shared", e.toString());
//		}
//		SharedPreferences mPrefs = mContext.getSharedPreferences("PDA-INVENTARIO", MODE_PRIVATE);

		Intent intent = new Intent(MainActivity.this, SplashActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
}
