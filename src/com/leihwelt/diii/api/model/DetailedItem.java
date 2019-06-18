package com.leihwelt.diii.api.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.leihwelt.diii.api.model.item.ItemType;
import com.leihwelt.diii.api.model.item.RawAttribute;

public class DetailedItem {

	public ItemType type;

	public String name;
	public String icon;
	public String displayColor;
	public String tooltipParams;

	// detail information
	public int requiredLevel;
	public int itemLevel;
	public int bonusAffixes;
	public int bonusAffixesMax;

	public RawAttribute dps;
	public RawAttribute attacksPerSecond;
	public RawAttribute minDamage;
	public RawAttribute maxDamage;

	public boolean accountBound;

	public String flavorText;
	public String typeName;

	// values
	public RawAttribute armor;

	public List<RawAttribute> rawAttributes = new LinkedList<RawAttribute>();
	public List<DetailedItem> gems = new LinkedList<DetailedItem>();
	//public Map<String,String> attributes;
	
	public List<ItemStat> primaryAttributes;
	public List<ItemStat> secondaryAttributes;
	public List<ItemStat> passiveAttributes;
	
	public List<Object> socketEffects;

	@SuppressWarnings("unchecked")
	public DetailedItem(Map<String, Object> data) {

		if (data != null) {
			
		
			if (data.containsKey("name"))
				this.name = (String) data.get("name");
			
			if (data.containsKey("icon"))
				this.icon = (String) data.get("icon");
			
			if (data.containsKey("displayColor"))
				this.displayColor = (String) data.get("displayColor");
			
			if (data.containsKey("tooltipParams"))
				this.tooltipParams = (String) data.get("tooltipParams");

			if (data.containsKey("requiredLevel"))
				this.requiredLevel = getInt(data, "requiredLevel", 0);
			
			if (data.containsKey("itemLevel"))
				this.itemLevel = getInt(data, "itemLevel", 0);
			
			if (data.containsKey("bonusAffixes"))
				this.bonusAffixes = getInt(data, "bonusAffixes", 0);
			
			if (data.containsKey("bonusAffixesMax"))
				this.bonusAffixesMax = getInt(data, "bonusAffixesMax", 0);

			if (data.containsKey("accountBound"))
				this.accountBound = getBoolean(data, "accountBound", false);
			
			if (data.containsKey("flavorText"))
				this.flavorText = (String) data.get("flavorText");
			
			if (data.containsKey("typeName"))
				this.typeName = (String) data.get("typeName");

		
			if (data.containsKey("attributes")){				
				Map<String,Object> attributes = (Map<String,Object>) data.get("attributes");
	
				this.primaryAttributes = getStats (attributes.get("primary"));
				this.secondaryAttributes = getStats (attributes.get("secondary"));
				this.passiveAttributes = getStats (attributes.get("passive"));
			}
			
			if (data.containsKey("socketEffects"))
				this.socketEffects = (List<Object>) data.get("socketEffects");
			
			extractRawAttributes(data);

			extractGems(data);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ItemStat> getStats(Object object) {
		
		if (object == null)
			return new ArrayList<ItemStat>();
		
		
		List<Object> stats = (List<Object>)object;
		
		List<ItemStat> itemStats = new ArrayList<ItemStat>();
		
		for (Object obj : stats){			
			itemStats.add(new ItemStat ((Map<String, String>) obj));			
		}
		
		return itemStats;		
	}

	private boolean getBoolean(Map<String, Object> data, String key, boolean defaultValue) {

		if (!data.containsKey(key))
			return defaultValue;

		return (Boolean) data.get(key);
	}

	private int getInt(Map<String, Object> data, String key, int defaultValue) {

		if (!data.containsKey(key))
			return defaultValue;

		return (Integer) data.get(key);
	}

	@SuppressWarnings("unchecked")
	private void extractGems(Map<String, Object> data) {

		if (data.containsKey("gems")) {

			List<Object> gemMaps = (List<Object>) data.get("gems");

			for (Object gem : gemMaps) {

				Map<String, Object> gemMap = (Map<String, Object>) gem;

				if (gemMap.containsKey("item") && gemMap.containsKey("attributes"))
					this.gems.add(new Gem((Map<String, Object>) gemMap.get("item"), (Map<String,Object>) gemMap.get("attributes")));

			}
		}

		if (gems.size() > 0)
			System.out.println("Gems " + gems.size());

	}

	@SuppressWarnings("unchecked")
	private void extractRawAttributes(Map<String, Object> data) {

		if (data.containsKey("armor")) {
			this.armor = new RawAttribute("armor", (Map<String, Object>) data.get("armor"));
		}

		if (data.containsKey("dps")) {
			this.dps = new RawAttribute("dps", (Map<String, Object>) data.get("dps"));
		}

		if (data.containsKey("attacksPerSecond")) {
			this.attacksPerSecond = new RawAttribute("attacksPerSecond", (Map<String, Object>) data.get("attacksPerSecond"));
		}

		if (data.containsKey("minDamage")) {
			this.minDamage = new RawAttribute("minDamage", (Map<String, Object>) data.get("minDamage"));
		}

		if (data.containsKey("maxDamage")) {
			this.maxDamage = new RawAttribute("maxDamage", (Map<String, Object>) data.get("maxDamage"));
		}

		if (data.containsKey("type")) {
			this.type = new ItemType((Map<String, Object>) data.get("type"));
		}

		if (data.containsKey("attributesRaw")) {
			Map<String, Object> rawAttribs = (Map<String, Object>) data.get("attributesRaw");

			for (String key : rawAttribs.keySet()) {
				this.rawAttributes.add(new RawAttribute(key, (Map<String, Object>) rawAttribs.get(key)));
			}
		}
	}
}
