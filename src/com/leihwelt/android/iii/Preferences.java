package com.leihwelt.android.iii;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public enum Preferences {
	INSTANCE;

	private static final String LAST_SELECTED_HERO = "LAST_SELECTED_HERO";
	private static final String PREFS = "III_PREFS";

	private Editor edit(final Context ctx) {
		return prefs(ctx).edit();
	}

	private SharedPreferences prefs(final Context ctx) {
		return ctx.getSharedPreferences(PREFS, 0);
	}

	// SETTINGS

	public void setLastSelectedHero(int heroId, final Context ctx) {
		edit(ctx).putInt(LAST_SELECTED_HERO, heroId).commit();
	}

	public int getLastSelectedHero(final Context ctx) {
		return prefs(ctx).getInt(LAST_SELECTED_HERO, -1);
	}
}
