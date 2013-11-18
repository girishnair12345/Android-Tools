package com.girish.android.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
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
import android.util.Log;

public class CustomSharedPreference implements SharedPreferences,Editor{

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private Context mContext;
	private boolean isEncyrpted;
	private String keyForEncryption="customPrefs";
	private static String LOG_TAG = "Android Tools";
	
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
	
	public void enableEncryption(String keyForEncryption){
		if(keyForEncryption == null)
			throw new RuntimeException("Key For Encryption is null");
		this.keyForEncryption = keyForEncryption;
		isEncyrpted = true;
	}
	
	public void disableEncryption(){
		isEncyrpted = false;
	}
	
	public Object getObject(String key, Object defValue){
		key = put(key);
		if(key == null)
			return null;
		String mString = mSharedPreferences.getString(key, null);
		if(mString == null)
			return defValue;
		Object obj = null;
		mString = get(mString);
		try {
			obj = fromString(mString);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Exception " + e.getMessage());
			e.printStackTrace();
		} 
		return obj;
	}
	
	public Editor putObject(String key, Serializable value){
		key = put(key);
		if(key == null)
			return mEditor;
		
		String mString = null;
		try {
			mString = toString(value);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOException " + e.getMessage());
			e.printStackTrace();
		}
		if(mString == null)
			return mEditor;
		mString = put(mString);
		return mEditor.putString(key,mString);
	}
	
	
	private String put(String plainText){
		if(isEncyrpted){
			try {
				return SimpleCrypto.encrypt(keyForEncryption, plainText);
			} catch (Exception e) {
				Log.e(LOG_TAG, "Exception " + e.getMessage());
				e.printStackTrace();
				return null;
				
			}
		}
		return plainText;
	}
	
	private String get(String encryptedText){
		if(isEncyrpted){
			try {
				return SimpleCrypto.decrypt(keyForEncryption, encryptedText);
			} catch (Exception e) {
				Log.e(LOG_TAG, "Exception " + e.getMessage());
				e.printStackTrace();
				return null;
				
			}
		}
		return encryptedText;
	}

    private static Object fromString(String s) throws IOException,ClassNotFoundException {
        byte [] data = Base64Coder.decode( s );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    private static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return new String( Base64Coder.encode( baos.toByteArray() ) );
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
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = put(""+value);
			return mEditor.putString(key, tmp);
		}
		return mEditor.putBoolean(key, value);
	}

	
	public Editor putFloat(String key, float value) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = put(""+value);
			return mEditor.putString(key, tmp);
		}
		return mEditor.putFloat(key, value);
	}

	
	public Editor putInt(String key, int value) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = put(""+value);
			return mEditor.putString(key, tmp);
		}
		return mEditor.putInt(key, value);
	}

	
	public Editor putLong(String key, long value) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = put(""+value);
			return mEditor.putString(key, tmp);
		}
		return mEditor.putLong(key, value);
	}

	
	public Editor putString(String key, String value) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = put(""+value);
			return mEditor.putString(key, tmp);
		}
		return mEditor.putString(key, value);
	}

	@SuppressLint("NewApi")
	public Editor putStringSet(String key, Set<String> value) {
		if(isEncyrpted){
			key = put(key);
			for (String s : value) {
				value.remove(s);
				s = put(s);
				value.add(s);
			}
			return mEditor.putStringSet(key, value);
		}
		
		return mEditor.putStringSet(key, value);
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
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = mSharedPreferences.getString(key, null);
			if(tmp == null)
				return defValue;
			tmp = get(tmp);
			return Boolean.parseBoolean(tmp);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}

	
	public float getFloat(String key, float defValue) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = mSharedPreferences.getString(key, null);
			if(tmp == null)
				return defValue;
			tmp = get(tmp);
			return Float.parseFloat(tmp);
		}
		return mSharedPreferences.getFloat(key, defValue);
	}

	
	public int getInt(String key, int defValue) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = mSharedPreferences.getString(key, null);
			if(tmp == null)
				return defValue;
			tmp = get(tmp);
			return Integer.parseInt(tmp);
		}
		return mSharedPreferences.getInt(key, defValue);
	}

	
	public long getLong(String key, long defValue) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = mSharedPreferences.getString(key, null);
			if(tmp == null)
				return defValue;
			tmp = get(tmp);
			return Long.parseLong(tmp);
		}
		return mSharedPreferences.getLong(key, defValue);
	}

	
	public String getString(String key, String defValue) {
		if(isEncyrpted){
			String tmp;
			key = put(key);
			tmp = mSharedPreferences.getString(key, null);
			if(tmp == null)
				return defValue;
			tmp = get(tmp);
			return tmp;
		}
		return mSharedPreferences.getString(key, defValue);
	}

	
	@SuppressLint("NewApi")
	public Set<String> getStringSet(String key, Set<String> value) {
		if(isEncyrpted){
			key = put(key) ;
			Set<String> tmp = mSharedPreferences.getStringSet(key, null);
			if(tmp == null)
				return value;
			for (String s : tmp) {
				value.remove(s);
				s = get(s);
				value.add(s);
			}
			return tmp;
		}
		return mSharedPreferences.getStringSet(key, value);
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
