package com.girish.android.tools;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class MutableCheckBoxListPreference extends ListPreference {

	
	private static String prefName;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private CharSequence[] mEntries;
	private CharSequence[] mEntryValues;
	private boolean[] checkedEntries;
	private boolean[] restorecheckedEntries;
	private Map<Integer, String> mMap;
	
	private static final String SEPERATOR = "SePeRaToR";
	
	public MutableCheckBoxListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		prefName = getKey();
		mMap = new Hashtable<Integer, String>();
		mSharedPreferences = context.getSharedPreferences(prefName,0);
		mEditor = mSharedPreferences.edit();
		int count = mSharedPreferences.getInt("count", 0);
		if(count == 0){
			mEntries = getEntries();
			mEntryValues = getEntryValues();	
			for(int i=0; i<mEntries.length; i++){
				mEditor.putString(""+i, mEntries[i]+SEPERATOR+mEntryValues[i] + SEPERATOR + "false");
			}
			mEditor.putInt("count", mEntries.length);
			mEditor.commit();
		}else{
			String[] temp;
			mEntries = new String[count];
			mEntryValues = new String[count];
			checkedEntries = new boolean[count];
			restorecheckedEntries = new boolean[count];
			for(int i=0; i<count; i++){
				temp = mSharedPreferences.getString(""+i, null).split(SEPERATOR);
				mEntries[i] = temp[0];
				mEntryValues[i] = temp[1];
				restorecheckedEntries[i] = checkedEntries[i] = Boolean.parseBoolean(temp[2]);
			}
			setEntries(mEntries);
			setEntryValues(mEntryValues);
		}
		
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		reloadList();
		
		builder.setMultiChoiceItems(mEntries, checkedEntries , new OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int pos, boolean checked) {
				mMap.put(pos, mEntries[pos] + SEPERATOR + mEntryValues[pos] + SEPERATOR +checked);
			}
		});
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (Map.Entry<Integer, String> entry : mMap.entrySet()) {
				    mEditor.putString(""+entry.getKey(), mEntries[entry.getKey()] + SEPERATOR + mEntryValues[entry.getKey()] + SEPERATOR +checkedEntries[entry.getKey()]);
				}
				mEditor.commit();
				for(int i=0 ; i<checkedEntries.length; i++)
					restorecheckedEntries[i] = checkedEntries[i];
				mMap.clear();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for(int i=0 ; i<checkedEntries.length; i++)
					 checkedEntries[i] = restorecheckedEntries[i];
				mMap.clear();
			}
		});
	}
	
	

	public CharSequence getEntryValue(String Key) {
		for(int i=0; i<mEntries.length; i++){
			if(mEntries[i].equals(Key))
				return mEntryValues[i];
		}
		return null;
	}
	
	public boolean getEntryValueCheckedState(String EntryValue) {
		for(int i=0; i<mEntryValues.length; i++){
			if(mEntryValues[i].equals(EntryValue))
				return checkedEntries[i];
		}
		return false;
	}


	public void setEntriesAndEntryValues(CharSequence[] entries, CharSequence[] entryValues){
		if(entries.length != entryValues.length)
			throw new RuntimeException("Entries and EntryValues lenght do not match");
		mEditor.clear().commit();
		for(int i=0; i<entries.length; i++){
			mEditor.putString(""+i, entries[i]+SEPERATOR+entryValues[i] + SEPERATOR + "false");
		}
		mEditor.putInt("count", entryValues.length);
		mEditor.commit();
	}
	
	public void setEntriesAndEntryValues(CharSequence[] entries, CharSequence[] entryValues,  boolean[] checkedEntries){
		if(entries.length != entryValues.length)
			throw new RuntimeException("Entries and EntryValues lenght do not match");
		if(checkedEntries.length != entryValues.length)
			throw new RuntimeException("checkedEntries do not match length of entries & entryValues");
		mEditor.clear().commit();
		for(int i=0; i<entries.length; i++){
			mEditor.putString(""+i, entries[i]+SEPERATOR+entryValues[i] + SEPERATOR + checkedEntries[i]);
		}
		mEditor.putInt("count", entryValues.length);
		mEditor.commit();
	}

	public void addEntry(String key, String value, boolean checked){
		int count = mSharedPreferences.getInt("count", 0);
		mEditor.putString(""+count, key+SEPERATOR+value + SEPERATOR + checked);
		mEditor.putInt("count", count+1);
		mEditor.commit();
		
		reloadList();
	}

	public void removeEntry(String key) {
		int removeCount = 0;
		ArrayList<String> strtemp = new ArrayList<String>();
		int count = mSharedPreferences.getInt("count", 0);
		String str;
		for(int i=0; i<count; i++){
			str = mSharedPreferences.getString(""+i, null);
			if(!(str.split(SEPERATOR)[0]).equals(key)){
				strtemp.add(str);
			}else{
				removeCount++;
			}
		}
		
		for(int i=0; i<strtemp.size(); i++){
			mEditor.putString(""+i, strtemp.get(i));
		}
		mEditor.putInt("count", count -removeCount);
		mEditor.commit();
	}
	
	public void reloadList(){
		int count = mSharedPreferences.getInt("count", 0);
		String[] temp;
		mEntries = new String[count];
		mEntryValues = new String[count];
		checkedEntries = new boolean[count];
		restorecheckedEntries = new boolean[count];
		for(int i=0; i<count; i++){
			temp = mSharedPreferences.getString(""+i, null).split(SEPERATOR);
			mEntries[i] = temp[0];
			mEntryValues[i] = temp[1];
			restorecheckedEntries[i] = checkedEntries[i] = Boolean.parseBoolean(temp[2]);
		}
		setEntries(mEntries);
		setEntryValues(mEntryValues);
	}
}
