package com.girish.android.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
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
	
	public CustomSharedPreference(String fileName, int mode, Context context){
		mContext = context;
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
			throw new RuntimeException("Exception " + e.getMessage());
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
			throw new RuntimeException("IOException " + e.getMessage());
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
				throw new RuntimeException("Exception " + e.getMessage());
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
				throw new RuntimeException("Exception " + e.getMessage());
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
			Set<String> temp = new LinkedHashSet<String>();
			key = put(key);
			for (String s : value) {
				s = put(s);
				temp.add(s);
			}
			return mEditor.putStringSet(key, temp);
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
			Set<String> temp = new LinkedHashSet<String>();
			for (String s : tmp) {
				s = get(s);
				temp.add(s);
			}
			return temp;
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

class Base64Coder {

	private static final String systemLineSeparator = System.getProperty("line.separator");
	private static final char[] map1 = new char[64];
	   static {
	      int i=0;
	      for (char c='A'; c<='Z'; c++) map1[i++] = c;
	      for (char c='a'; c<='z'; c++) map1[i++] = c;
	      for (char c='0'; c<='9'; c++) map1[i++] = c;
	      map1[i++] = '+'; map1[i++] = '/'; }

	private static final byte[] map2 = new byte[128];
	   static {
	      for (int i=0; i<map2.length; i++) map2[i] = -1;
	      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }

	public static String encodeString (String s) {
	   return new String(encode(s.getBytes())); }

	public static String encodeLines (byte[] in) {
	   return encodeLines(in, 0, in.length, 76, systemLineSeparator); }

	public static String encodeLines (byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
	   int blockLen = (lineLen*3) / 4;
	   if (blockLen <= 0) throw new IllegalArgumentException();
	   int lines = (iLen+blockLen-1) / blockLen;
	   int bufLen = ((iLen+2)/3)*4 + lines*lineSeparator.length();
	   StringBuilder buf = new StringBuilder(bufLen);
	   int ip = 0;
	   while (ip < iLen) {
	      int l = Math.min(iLen-ip, blockLen);
	      buf.append (encode(in, iOff+ip, l));
	      buf.append (lineSeparator);
	      ip += l; }
	   return buf.toString(); }

	public static char[] encode (byte[] in) {
	   return encode(in, 0, in.length); }

	public static char[] encode (byte[] in, int iLen) {
	   return encode(in, 0, iLen); }

	public static char[] encode (byte[] in, int iOff, int iLen) {
	   int oDataLen = (iLen*4+2)/3;       
	   int oLen = ((iLen+2)/3)*4;         
	   char[] out = new char[oLen];
	   int ip = iOff;
	   int iEnd = iOff + iLen;
	   int op = 0;
	   while (ip < iEnd) {
	      int i0 = in[ip++] & 0xff;
	      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
	      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
	      int o0 = i0 >>> 2;
	      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
	      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
	      int o3 = i2 & 0x3F;
	      out[op++] = map1[o0];
	      out[op++] = map1[o1];
	      out[op] = op < oDataLen ? map1[o2] : '='; op++;
	      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
	   return out; }

	public static String decodeString (String s) {
	   return new String(decode(s)); }

	public static byte[] decodeLines (String s) {
	   char[] buf = new char[s.length()];
	   int p = 0;
	   for (int ip = 0; ip < s.length(); ip++) {
	      char c = s.charAt(ip);
	      if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
	         buf[p++] = c; }
	   return decode(buf, 0, p); }

	public static byte[] decode (String s) {
	   return decode(s.toCharArray()); }

	public static byte[] decode (char[] in) {
	   return decode(in, 0, in.length); }

	public static byte[] decode (char[] in, int iOff, int iLen) {
	   if (iLen%4 != 0) throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");
	   while (iLen > 0 && in[iOff+iLen-1] == '=') iLen--;
	   int oLen = (iLen*3) / 4;
	   byte[] out = new byte[oLen];
	   int ip = iOff;
	   int iEnd = iOff + iLen;
	   int op = 0;
	   while (ip < iEnd) {
	      int i0 = in[ip++];
	      int i1 = in[ip++];
	      int i2 = ip < iEnd ? in[ip++] : 'A';
	      int i3 = ip < iEnd ? in[ip++] : 'A';
	      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
	         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
	      int b0 = map2[i0];
	      int b1 = map2[i1];
	      int b2 = map2[i2];
	      int b3 = map2[i3];
	      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
	         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
	      int o0 = ( b0       <<2) | (b1>>>4);
	      int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
	      int o2 = ((b2 &   3)<<6) |  b3;
	      out[op++] = (byte)o0;
	      if (op<oLen) out[op++] = (byte)o1;
	      if (op<oLen) out[op++] = (byte)o2; }
	   return out; }

	
	private Base64Coder() {}

	} 

class SimpleCrypto {

	public static String encrypt(String seed, String cleartext)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); 
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

}