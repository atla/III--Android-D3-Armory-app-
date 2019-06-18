package com.leihwelt.diii.api.model;

import java.util.Map;

public class ItemStat {

	public String text;
	public String type;
	public String color;
	
	public ItemStat (Map<String,String> stat){
		
		this.text = stat.get("text");
		this.type = stat.get("affixType");
		this.color = stat.get("color");
		
		
	}
	
}
