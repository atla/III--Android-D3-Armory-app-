package com.leihwelt.diii.api.model;

import java.util.Map;

public class Skill {

	public Skill(Map<String, Object> data, boolean passive) {

		this.passive = passive;

		if (data != null) {
			this.name = (String) data.get("name");
			this.icon = (String) data.get("icon");
			this.categorySlug = (String) data.get("categorySlug");
			this.description = (String) data.get("description");
			this.simpleDescription = (String) data.get("simpleDescription");
			this.skillCalcId = (String) data.get("skillCalcId");
			this.level = (Integer) data.get("level");

			this.showExpanded = false;
		}
	}
	
	public boolean showExpanded;

	public String name;
	public String icon;
	public String categorySlug;
	public String description;
	public String simpleDescription;
	public String skillCalcId;
	
	public Rune rune = null;

	public int level;

	public boolean passive;

}
