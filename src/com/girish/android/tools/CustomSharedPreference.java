package com.girish.android.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera.ShutterCallback;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

public class CustomSharedPreference implements SharedPreferences,Editor{

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private Context mContext;
	private boolean isEncyrpted;
	
	public CustomSharedPreference(Context context){
		mContext = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditor = mSharedPreferences.edit();
		isEncyrpted = false;
	}
	
	public CustomSharedPreference(String fileName, int mode){
		mSharedPreferences = mContext.getSharedPreferences(fileName, mode);
		mEditor = mSharedPreferences.edit();
		isEncyrpted = false;
		
	}
	
	public Object getObject(String key, Object defValue){
		key = put(key);
		if(key == null)
			return null;
		String mString = mSharedPreferences.getString(key, null);
		if(mString == null)
			return defValue;
		Object obj = stringToObject(get(mString));
		return obj;
	}
	
	public boolean putObject(String key, Object value){
		key = put(key);
		if(key == null)
			return false;
		String mString = put(objectToString(value));
		mEditor.putString(key, mString);
		return mEditor.commit();
	}
	
	
	private String put(String plainText){
		return plainText;
	}
	
	private String get(String encryptedText){
		return encryptedText;
	}

    private String objectToString(Object obj) {
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(
              new Base64OutputStream(baos, Base64.NO_PADDING
                  | Base64.NO_WRAP));
          oos.writeObject(obj);
          oos.close();
          return baos.toString("UTF-8");
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }

    private Object stringToObject(String str) {
        try {
          return new ObjectInputStream(new Base64InputStream(
              new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                  | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

    // SharedPreferences.Editor Method Calls	
	public void apply() {
		mEditor.apply();
	}

	
	public Editor clear() {
		return mEditor.clear();
	}

	
	public boolean commit() {
		return mEditor.commit();
	}

	
	public Editor putBoolean(String key, boolean value) {
		return mEditor.putBoolean(key, value);
	}

	
	public Editor putFloat(String key, float value) {
		return mEditor.putFloat(key, value);
	}

	
	public Editor putInt(String key, int value) {
		return mEditor.putInt(key, value);
	}

	
	public Editor putLong(String key, long value) {
		return mEditor.putLong(key, value);
	}

	
	public Editor putString(String key, String value) {
		return mEditor.putString(key, value);
	}

	@SuppressLint("NewApi")
	public Editor putStringSet(String arg0, Set<String> arg1) {
		return mEditor.putStringSet(arg0, arg1);
	}

	
	public Editor remove(String key) {
		return mEditor.remove(key);
	}

	// SharedPreferences method calls
	public boolean contains(String key) {
		return mSharedPreferences.contains(key);
	}

	
	public Editor edit() {
		return mSharedPreferences.edit();
	}

	
	public Map<String, ?> getAll() {
		return mSharedPreferences.getAll();
	}

	
	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	
	public float getFloat(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	
	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	
	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	
	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	
	@SuppressLint("NewApi")
	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		return mSharedPreferences.getStringSet(arg0, arg1);
	}

	
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
