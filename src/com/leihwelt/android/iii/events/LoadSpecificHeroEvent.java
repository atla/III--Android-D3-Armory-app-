package com.leihwelt.android.iii.events;

public class LoadSpecificHeroEvent {

	public String profile;
	public int heroId;
	public String server;

	public LoadSpecificHeroEvent (String server, String profile, int heroId){
		this.server = server;
		this.profile = profile;
		this.heroId = heroId;
		
	}
	
}
