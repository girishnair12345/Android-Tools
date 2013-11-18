This is a set of android tools which I have build when I needed some solutions that weren't available

CheckBoxListPreferences
The default android doesn't have a check box list preference, you can use this class to make a checkBox list preference
You can inflate and view it as a normal List Preference but with availability of Check box's instead of radio buttons

MutableListPreference
The mutable List Preference allows to add and remove elements dynamically from the list Preference
You can add an entry using
	prefMutableListPreference.addEntry("key 1", "value 1");
	prefMutableListPreference.addEntry("key 2", "value 2");
and remove using
	prefMutableListPreference.removeEntry("key 1");
	prefMutableListPreference.removeEntry("key 2");

MutableCheckBoxListPreference
The mutable List Preference allows to add and remove elements dynamically from the Checkbox List Preference 
You can add an entry using
	prefMutableListPreference.addEntry("key 1", "value 1");
	prefMutableListPreference.addEntry("key 2", "value 2");
and remove using
	prefMutableListPreference.removeEntry("key 1");
	prefMutableListPreference.removeEntry("key 2");

Apart from these there are some more methods
//Gives value of the key provided
public CharSequence getEntryValue(String Key);
//States weather the value is checked or not
public boolean getEntryValueCheckedState(String EntryValue);
//Set your custom entries
public void setEntriesAndEntryValues(CharSequence[] entries, CharSequence[] entryValues)
public void setEntriesAndEntryValues(CharSequence[] entries, CharSequence[] entryValues,  boolean[] checkedEntries)

InfiniteToast
This allows you to show an infinitely long running toast
Just create an instance of the Custom Toast
InfiniteToast inf = new InfiniteToast(Context, "Message");
//To show
inf.show();
//To cancel
inf.cancel();
It has normal methods as of Toast to modify it

TimeBasedToast
This is a custom toast with specific duration length
Just create an instance of the Custom Toast
TimeBasedToast mTimeBasedToast = new TimeBasedToast(Context, "Message",timeInSeonds);
//To show
mTimeBasedToast.show();
//To cancel
mTimeBasedToast.cancel();


You can mail me at gnairgithub@gmail.com if you wish to want some improvement or need some functionality that needs to be added on
Thank you
:)