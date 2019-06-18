package com.leihwelt.diii.api.model;

import java.util.Map;

public class Rune {
	
	public Rune(Map<String, Object> data, boolean passive) {

		if (data != null) {
			this.name = (String) data.get("name");
			this.icon = (String) data.get("icon");
			this.description = (String) data.get("description");
			this.type = (String) data.get("type");
			this.simpleDescription = (String) data.get("simpleDescription");
			this.skillCalcId = (String) data.get("skillCalcId");
			this.level = (Integer) data.get("level");
			this.order = (Integer) data.get("order");
		}
	}
	
	public boolean showExpanded;

	public String name;
	public String icon;
	public String type;
	public String description;
	public String simpleDescription;
	public String skillCalcId;

	public int order;
	public int level;
}
