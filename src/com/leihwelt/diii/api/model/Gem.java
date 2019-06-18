package com.leihwelt.diii.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class Gem extends DetailedItem {

	@SuppressWarnings("unchecked")
	public Gem(Map<String, Object> data, Map<String,Object> attribs) {
		super(data);
	
		List<Object> stats = (List<Object>)attribs.get("primary");		
		this.primaryAttributes = new ArrayList<ItemStat>();
		
		for (Object obj : stats){
				this.primaryAttributes.add(new ItemStat ((Map<String,String>)obj));			
		}	
	}

}
