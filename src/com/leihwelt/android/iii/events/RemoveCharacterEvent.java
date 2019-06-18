package com.leihwelt.android.iii.events;

import com.leihwelt.diii.api.model.Hero;

public class RemoveCharacterEvent {

	public Hero hero;

	public RemoveCharacterEvent(Hero hero) {
		this.hero = hero;
	}

}
