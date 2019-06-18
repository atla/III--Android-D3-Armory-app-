package com.leihwelt.android.iii.events;

import java.util.List;

import com.leihwelt.diii.api.model.Hero;

public class HeroesFoundEvent {

	public List<Hero> heroes;
	public String profile;

	public HeroesFoundEvent(String profile, List<Hero> heroes) {

		this.profile = profile;
		this.heroes = heroes;
	}

}
