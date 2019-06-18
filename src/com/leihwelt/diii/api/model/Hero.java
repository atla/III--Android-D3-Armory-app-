package com.leihwelt.diii.api.model;

import java.io.Serializable;
import java.util.Map;

public class Hero implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3208869348934198175L;

	public Hero(String bnetAccount, String server, Map<String, Object> data) {

		this.name = (String) data.get("name");
		this.id = (Integer) data.get("id");
		this.level = (Integer) data.get("level");
		this.hardcore = (Boolean) data.get("hardcore");
		this.gender = (Integer) data.get("gender");
		this.lastUpdated = (Integer) data.get("last-updated");
		this.dead = (Boolean) data.get("dead");
		this.d3class = (String) data.get("class");
		this.paragonLevel = (Integer) data.get("paragonLevel");
		this.bnetAccount = bnetAccount;
		this.server = server;
	}

	public String toString() {
		return this.name + "(" + this.level + ", " + this.paragonLevel + ")";
	}

	public String name;
	public int id;
	public int level;
	public int paragonLevel;
	public String d3class;
	public boolean hardcore;
	public int gender;
	public int lastUpdated;
	public boolean dead;
	public String bnetAccount;
	public String server;

	public CharSequence getDetailLine() {
		return "lvl " + level + " " + d3class + (hardcore ? "(HC)" : "") + " - " + bnetAccount + " [" + server + "]"; 
	}
}
