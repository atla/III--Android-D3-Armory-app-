package com.leihwelt.android.iii.events;

import java.util.List;

import com.leihwelt.diii.api.model.Hero;

public class HeroesLoadedEvent {

	public List<Hero> heroes;
	public String profile;

	public HeroesLoadedEvent(String profile, List<Hero> heroes) {

		this.profile = profile;
		this.heroes = heroes;
	}

}
