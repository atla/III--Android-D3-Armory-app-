package com.leihwelt.diii.api.model.item;

import java.util.Map;

public class ItemType {

	public ItemType(Map<String, Object> data) {

		this.twoHanded = (Boolean) data.get("twoHanded");
		this.id = (String) data.get("id");

	}

	public boolean twoHanded;
	public String id;

}
