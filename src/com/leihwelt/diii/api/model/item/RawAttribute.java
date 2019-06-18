package com.leihwelt.diii.api.model.item;

import java.util.Map;

public class RawAttribute {

	public RawAttribute(String name, Map<String, Object> data) {

		if (data != null) {
			this.name = name;
			this.min = (Double) data.get("min");
			this.max = (Double) data.get("max");
		}

	}

	public String name;
	public double min;
	public double max;
}
