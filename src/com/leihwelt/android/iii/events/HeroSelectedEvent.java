package com.leihwelt.android.iii.events;

import com.leihwelt.diii.api.model.Hero;

public class HeroSelectedEvent {

	public Hero hero;

	public HeroSelectedEvent (Hero hero){
		this.hero = hero;
	}
	
}
