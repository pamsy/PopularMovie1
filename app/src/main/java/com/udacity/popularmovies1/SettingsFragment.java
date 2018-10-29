package com.udacity.popularmovies1;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

//The class SettingsFragment extends PreferenceFragmentCompact
public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    //In this Method we will add the preferences file using the addPrefenencesFromRessources
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //Add movies prefences, defined  in the  XML file in sort_movies.xml
        addPreferencesFromResource(R.xml.sort_movies);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen  preferenceScreen = getPreferenceScreen();
        //eventhought we know that there will be only one  preference  but it can change in part two
        // so i use a variable to count the number of Preferences
        int count = preferenceScreen.getPreferenceCount();

        for (int i=0; i<count; i++){
            Preference pref =preferenceScreen.getPreference(i);
            String value = sharedPreferences.getString(pref.getKey(), "");
            setPreferenceSummary(pref, value);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    /**Update the summary  of the prefenrences**/
    private void setPreferenceSummary(Preference preference, String value){
        if (preference instanceof ListPreference){
            //For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference)preference;
            int prefSort = listPreference.findIndexOfValue(value);
            if (prefSort >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefSort]);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Toast.makeText(getContext(), "You Change your preferences", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
