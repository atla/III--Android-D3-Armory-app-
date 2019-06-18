package com.leihwelt.android.iii.profile.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.model.Item;
import com.leihwelt.diii.api.service.ItemService;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllStatsSectionAdapter extends BaseAdapter {

	private class Stat {

		public String name;
		public Object value;
		public boolean header = false;

		public Stat(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public Stat(String headerName) {
			this.name = headerName;
			this.header = true;
		}

	}
	
	/*
	 * "stats" : {
"life" : 52925,
"damage" : 370588.0,
"attackSpeed" : 2.8327370224834456,
"armor" : 6699,
"strength" : 3187,
"dexterity" : 435,
"vitality" : 1057,
"intelligence" : 380,
"physicalResist" : 387,
"fireResist" : 387,
"coldResist" : 474,
"lightningResist" : 387,
"poisonResist" : 387,
"arcaneResist" : 387,
"critDamage" : 5.79,
"blockChance" : 0.0,
"blockAmountMin" : 0,
"blockAmountMax" : 0,
"damageIncrease" : 31.8700008392334,
"critChance" : 0.6100000143051147,
"damageReduction" : 0.6906899809837341,
"thorns" : 0.0,
"lifeSteal" : 0.027,
"lifePerKill" : 0.0,
"goldFind" : 2.31,
"magicFind" : 2.46,
"primaryResource" : 100,
"secondaryResource" : 0,
"lifeOnHit" : 0.0
},
	 * 
	 */

	private List<Stat> stats = new ArrayList<Stat>();

	public void setStats(Map<String, Object> statMap) {

		List<Stat> newStats = new ArrayList<Stat>();

		// attributes
		newStats.add(new Stat("Attributes"));
		newStats.add(new Stat("Strength", statMap.get("strength")));
		newStats.add(new Stat("Dexterity", statMap.get("dexterity")));
		newStats.add(new Stat("Intelligence", statMap.get("intelligence")));
		newStats.add(new Stat("Armor", statMap.get("armor")));
		newStats.add(new Stat("Vitality", statMap.get("vitality")));

		// offense
		newStats.add(new Stat("Offense"));
		newStats.add(new Stat("Damage Increase", statMap.get("damageIncrease")));
		newStats.add(new Stat("Attack Speed", statMap.get("attackSpeed")));
		newStats.add(new Stat("Critical Hit Chance", statMap.get("critChance")));
		newStats.add(new Stat("Critical Hit Damage", statMap.get("critDamage")));


		// defense
		newStats.add(new Stat("Defense"));
		newStats.add(new Stat("Block Amount", statMap.get("blockAmountMin")));
		newStats.add(new Stat("Block Chance", statMap.get("blockChance")));
		newStats.add(new Stat("Damage Reduction", statMap.get("damageReduction")));
		
		newStats.add(new Stat("Physical Resistance", statMap.get("physicalResist")));
		newStats.add(new Stat("Cold Resistance", statMap.get("coldResist")));
		newStats.add(new Stat("Fire Resistance", statMap.get("fireResist")));
		newStats.add(new Stat("Lightning Resistance", statMap.get("lightningResist")));
		newStats.add(new Stat("Poison Resistance", statMap.get("poisonResist")));
		newStats.add(new Stat("Arcane/Holy Resistance", statMap.get("arcanceResist")));
		
		newStats.add(new Stat("Thorns", statMap.get("physicalResistance")));
		
		// life
		newStats.add(new Stat("Life"));
		newStats.add(new Stat("Maximum Life", statMap.get("life")));
		newStats.add(new Stat("Life Steal", statMap.get("lifeSteal")));
		newStats.add(new Stat("Life per Kill", statMap.get("lifePerKill")));
		newStats.add(new Stat("Life per Hit", statMap.get("lifePerHit")));

		// life
		newStats.add(new Stat("Adventure"));
		newStats.add(new Stat("Gold Find", statMap.get("goldFind")));
		newStats.add(new Stat("Magic Find", statMap.get("magicFind")));

		this.stats = newStats;
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return stats.size();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		Stat s = (Stat) this.getItem(position);
		return s.header ? 1 : 0;
	}

	@Override
	public Object getItem(int position) {
		return stats.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Stat item = (Stat) this.getItem(position);

		ItemViewHolder viewHolder = null;
		Context context = parent.getContext();

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = li.inflate(item.header ? R.layout.stat_grid_header
					: R.layout.stat_grid_item, parent, false);

			viewHolder = new ItemViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(item.name);
		if (!item.header) {
			viewHolder.value.setText("" + item.value);
		}

		return convertView;
	}

	private class ItemViewHolder {

		public TextView name;
		public TextView value;

		public ItemViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.name);

			if (base.findViewById(R.id.value) != null) {
				this.value = (TextView) base.findViewById(R.id.value);
			}
		}

	}

}
