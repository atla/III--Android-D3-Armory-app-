package com.leihwelt.diii.api;

import android.graphics.Rect;
import android.graphics.RectF;

import com.leihwelt.android.iii.profile.section.OverviewSection.CutRectTransformation;

public enum BattleNetResources {
	INSTANCE;

	public static enum HeroPortraits {
		BARBAR_MALE, BARBAR_FEMALE, DEMONHUNTER_MALE, DEMONHUNTER_FEMALE, MONK_MALE, MONK_FEMALE, WITCHDOCTOR_MALE, WITCHDOCTOR_FEMALE, WIZARD_MALE, BWIZARD_FEMALE
	}

	public final static String HERO_PORTRAITS = "http://eu.battle.net/d3/static/images/profile/hero/hero-nav-portraits.jpg";
	public final static String HERO_PORTRAITS_BIG = "http://eu.battle.net/d3/static/images/profile/career-portraits.jpg";

	public final static String RUNES = "http://eu.battle.net/d3/static/images/icons/runes/large.png";

	private final static String BARBARIAN_HEADER = "http://eu.battle.net/d3/static/images/profile/hero/paperdoll/barbarian-male.jpg";

	private final static float HERO_PORTRAIT_WIDTH = 83;
	private final static float HERO_PORTRAIT_HEIGHT = 66;

	public String getHeaderImageUri(String d3class, int gender) {
		String gen = (gender == 0) ? "male" : "female";
		return "http://eu.battle.net/d3/static/images/profile/hero/paperdoll/" + d3class + "-" + gen + ".jpg";
	}

	public Rect getRectForHeroAvatar(String d3class, int gender) {

		int x = gender;
		int y = 0;

		if (d3class.equals("barbarian")) {
			y = 0;
		} else if (d3class.equals("demon-hunter")) {
			y = 1;
		} else if (d3class.equals("monk")) {
			y = 2;
		} else if (d3class.equals("witch-doctor")) {
			y = 3;
		} else if (d3class.equals("wizard")) {
			y = 4;
		} else if (d3class.equals("crusader")) {
			y = 5;
		}

		x *= 168;
		y *= 130;

		return new Rect(x + 24, y, 130, 130);
	}

	public Rect getRectForRune(String type) {

		if (type.equals("a"))
			return new Rect(0, 0, 50, 50);
		if (type.equals("b"))
			return new Rect(50, 0, 50, 50);
		if (type.equals("c"))
			return new Rect(100, 0, 50, 50);
		if (type.equals("d"))
			return new Rect(150, 0, 50, 50);
		if (type.equals("e"))
			return new Rect(200, 0, 50, 50);

		return new Rect(250, 0, 50, 50);

	}

}
