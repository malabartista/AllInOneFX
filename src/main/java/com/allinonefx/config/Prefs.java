package com.allinonefx.config;

import java.util.prefs.Preferences;

public class Prefs {

    Preferences preferences;

    public Prefs() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    public void setPrefs(String key, String value) {
        preferences.put(key, value);
    }

    public String getPrefs(String key) {
        return preferences.get(key, key);
    }

}
