package com.girish.samples;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.girish.android.tools.CustomSharedPreference;
import com.girish.android.tools.InfiniteToast;
import com.girish.android.tools.TimeBasedToast;

public class MainActivity extends Activity implements OnClickListener {
	Button btnPrefActivity, btnInfiniteToast, btnTimeToast,
			btnCustomSharedPreference,btnGifImagePlayer;
	EditText edtTime;
	private Context mContext;
	private static String LOG_TAG = "Android Tools";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = getApplicationContext();

		btnPrefActivity = (Button) findViewById(R.id.btnPrefActivity);
		btnPrefActivity.setOnClickListener(this);

		btnInfiniteToast = (Button) findViewById(R.id.btnInfiniteToast);
		btnInfiniteToast.setOnClickListener(this);

		btnTimeToast = (Button) findViewById(R.id.btnTimeToast);
		btnTimeToast.setOnClickListener(this);
		edtTime = (EditText) findViewById(R.id.edtTime);

		btnCustomSharedPreference = (Button) findViewById(R.id.bntCustomSharedPreference);
		btnCustomSharedPreference.setOnClickListener(this);
		
		btnGifImagePlayer = (Button) findViewById(R.id.btnGifImagePlayer);
		btnGifImagePlayer.setOnClickListener(this);
	}

	@Override
	public void onClick(View veiw) {
		switch (veiw.getId()) {
		case R.id.btnPrefActivity:
			startActivity(new Intent(MainActivity.this,
					MyPreferenceActivity.class));
			break;

		case R.id.btnInfiniteToast:
			InfiniteToast inf = new InfiniteToast(mContext, "Toast");
			if (btnInfiniteToast.getText().equals("Infinite Toast")) {
				inf.setText("Infinite Toast");
				inf.show();
				btnInfiniteToast.setText("Stop Toast");
			} else {
				inf.cancel();
				btnInfiniteToast.setText("Infinite Toast");
			}
			break;

		case R.id.btnTimeToast:
			try {
				int time = Integer.parseInt(edtTime.getText().toString());
				TimeBasedToast mTimeBasedToast = new TimeBasedToast(mContext,
						"Time for " + time + " seconds", time);

				if (btnTimeToast.getText().equals("Timed Toast")) {
					mTimeBasedToast.show();
					btnTimeToast.setText("Stop Toast");
				} else {
					mTimeBasedToast.cancel();
					btnTimeToast.setText("Timed Toast");
				}
			} catch (NumberFormatException e) {
				Toast.makeText(mContext, "Time is not valid",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.bntCustomSharedPreference:
//			CustomSharedPreference mPrefs = new CustomSharedPreference(
//					getApplicationContext());
			CustomSharedPreference mPrefs = new CustomSharedPreference(
					"file",Context.MODE_PRIVATE, getApplicationContext());
			Employee employee = new Employee("Girish", 25, true, "Google", 5555);
			mPrefs.enableEncryption("girish");
			//Any Object that you put must implement the Serializable interface
			mPrefs.putObject("Test", employee);
			mPrefs.commit();
			Employee mEmployee = (Employee) mPrefs.getObject("Test", null);
			if (mEmployee != null) {
				Log.d(LOG_TAG, mEmployee.getEmployeDetailsInOrganization());
				Toast.makeText(mContext, "Data Retrived Successfully", Toast.LENGTH_LONG).show();
			} 
			mPrefs.disableEncryption();
			
			mPrefs.enableEncryption("girish");
			mPrefs.putBoolean("bool", true);
			mPrefs.putFloat("float", 1.5f);
			mPrefs.putInt("int", 15);
			mPrefs.putString("string", "my value");
			//Works in API 11 or greater
			Set<String> arg1 = new TreeSet<String>();
			arg1.add("one");
			arg1.add("Two");
			mPrefs.putStringSet("string set", arg1);
			mPrefs.commit();
			
			Log.d(LOG_TAG,"Get Prefs "+ mPrefs.getBoolean("bool", false));
			Log.d(LOG_TAG,"Get Prefs "+ mPrefs.getFloat("float", 0));
			Log.d(LOG_TAG,"Get Prefs "+ mPrefs.getInt("int", 0));
			Log.d(LOG_TAG,"Get Prefs "+ mPrefs.getString("string", null));
			Log.d(LOG_TAG,"Get Prefs"+ mPrefs.getStringSet("string set", null));
			
			break;
			
		case R.id.btnGifImagePlayer:
			startActivity(new Intent(MainActivity.this, GIFPlayer.class));
			break;
			
		}
	}

}

class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	private boolean permanent;
	private Organization organization;

	public Employee(String Name, int Age, boolean permanent) {
		this.name = Name;
		this.age = Age;
		this.permanent = permanent;
	}

	public Employee(String Name, int Age, boolean permanent,
			String OrganizationName, int CompanyID) {
		this.name = Name;
		this.age = Age;
		this.permanent = permanent;
		organization = new Organization(OrganizationName, CompanyID);
	}

	public String getEmployeDetailsInOrganization() {
		return "Employee => Name: " + name + " Age: " + age + " Permanent: "
				+ permanent + "\n" + organization.getOrganizationDetails();
	}
}

class Organization implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Name;
	private int CompanyID;

	public Organization(String OrganizationName, int CompanyID) {
		this.Name = OrganizationName;
		this.CompanyID = CompanyID;
	}

	public String getOrganizationDetails() {
		return "Organization => Name: " + Name + " Company ID " + CompanyID;
	}
}