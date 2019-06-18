package com.leihwelt.diii.api.model;

import java.util.Map;

public class Item {

	public Item(Map<String, Object> data) {

		if (data != null) {
			this.name = (String) data.get("name");
			this.icon = (String) data.get("icon");
			this.displayColor = (String) data.get("displayColor");
			this.tooltipParams = (String) data.get("tooltipParams");

		}
	}

	public String name;
	public String icon;
	public String displayColor;
	public String tooltipParams;

}
