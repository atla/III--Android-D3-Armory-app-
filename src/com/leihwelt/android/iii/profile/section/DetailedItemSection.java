package com.leihwelt.android.iii.profile.section;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.DetailedItem;
import com.leihwelt.diii.api.model.Gem;
import com.leihwelt.diii.api.model.Item;
import com.leihwelt.diii.api.model.ItemStat;
import com.leihwelt.diii.api.service.ItemService;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class DetailedItemSection extends Section implements OnClickListener {

	public final static String TAG = DetailedItemSection.class.getCanonicalName();

	private ItemService itemService;

	private View view;
	private TextView itemName;
	private TextView details;
	private ImageView icon;

	private DetailedItem detailedItem = null;
	private Item item = null;

	private String sectionName = "Item";

	private TextView itemNameSubline;

	private TextView itemNameType;

	private TextView dps;

	private TextView description;

	private TextView attributes;
	private TextView attributesSecondary;
	private TextView attributesPassive;
	
	private TextView action1Text2;
	private View line;

	private ImageButton action1Button;

	private String upgradeName;

	private TextView lvl;

	public DetailedItemSection() {

	}

	public DetailedItemSection(String desc, String upgradeName, Item item) {
		this.item = item;
		this.upgradeName = upgradeName;

		this.sectionName = desc;

		if (this.sectionName.length() > 18) {
			this.sectionName = this.sectionName.substring(0, 17) + "...";
		}
	}

	private class FetchItemDetailsTask extends AsyncTask<Void, Void, DetailedItem> {

		private DetailedHero hero;
		private Context ctx;
		private ItemService itemService;
		private Item item;

		public FetchItemDetailsTask(String server, Item item) {
			itemService = new ItemService(server);
			this.item = item;
		}

		@Override
		protected DetailedItem doInBackground(Void... arg0) {
			try {

				DetailedItem detailedItem = itemService.getDetailsForItem(item.tooltipParams);

				return detailedItem;

			} catch (Exception e) {
				Log.e(TAG, "Could not load hero portrait", e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(DetailedItem result) {

			if (result != null) {
				setDetailedItem(result);
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.detailed_item_section_fragment, null);
		this.itemName = (TextView) this.view.findViewById(R.id.item_name);
		this.icon = (ImageView) this.view.findViewById(R.id.icon);

		this.itemNameSubline = (TextView) this.view.findViewById(R.id.item_name_subline);
		this.itemNameType = (TextView) this.view.findViewById(R.id.item_name_type);
		this.dps = (TextView) this.view.findViewById(R.id.dps);
		this.description = (TextView) this.view.findViewById(R.id.description);
		
		this.line = this.view.findViewById(R.id.line);
		
		this.attributes = (TextView) this.view.findViewById(R.id.attributes);
		this.attributesSecondary = (TextView) this.view.findViewById(R.id.attributesSecondary);
		this.attributesPassive = (TextView) this.view.findViewById(R.id.attributesPassive);
		
		this.lvl = (TextView) this.view.findViewById(R.id.lvl);

		this.action1Text2 = (TextView) this.view.findViewById(R.id.action1_text2);
		this.action1Button = (ImageButton) this.view.findViewById(R.id.action1_button);
		this.action1Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openItemSuggestions();
			}
		});

		return this.view;

	}

	protected void openItemSuggestions() {

		String base = "http://diablo.somepage.com/popular/items/";
		String d3class = this.hero.d3class;
		openWebURL(base + d3class + "#" + upgradeName);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		try{
			this.setToItem();
			(new FetchItemDetailsTask(this.hero.server, this.item)).execute();	
		}
		catch (NullPointerException e) {
		}		
	}

	private void setToItem() {
		this.itemName.setText(this.item.name);
		this.itemName.setTextColor(getItemColor(item));
		String iconUri = itemService.getIconUri(item.icon);

		// check if this is a small item or big

		String smallItems = "NeckFingerWaist";

		if (smallItems.contains(this.sectionName)) {

			LayoutParams params = this.icon.getLayoutParams();
			params.height = params.width;
			this.icon.setLayoutParams(params);

			Picasso.with(this.getActivity()).load(iconUri).centerInside().resize(120, 120).into(this.icon);
		} else {
			Picasso.with(this.getActivity()).load(iconUri).centerInside().resize(120, 200).into(this.icon);
		}

	}

	private int getItemColor(Item item2) {
		return this.getColorForName(item2.displayColor);
	}

	private int getColorForName(String name) {

		if (name.equals("orange"))
			return 0xFFff9f00;

		if (name.equals("green"))
			return 0xFF00FF00;

		if (name.equals("yellow"))
			return 0xFFFFFF00;

		if (name.equals("blue"))
			return 0xFF6a63ff;

		return 0xFFEEEEEE;
	}

	public void setDetailedItem(DetailedItem result) {

		this.detailedItem = result;

		setToDetailItem();

	}

	private void setToDetailItem() {

		if (this.detailedItem.flavorText != null) {
			this.description.setText("\"" + this.detailedItem.flavorText + "\"");
		} else {
			this.description.setVisibility(View.GONE);
		}

		String lvlString = String.valueOf(this.detailedItem.requiredLevel);

		this.lvl.setText("ILVL " + lvlString);

		this.itemNameSubline.setText(this.detailedItem.typeName);
		this.itemNameSubline.setTextColor(getItemColor(item));

		this.itemNameType.setText(this.sectionName);

		if (this.detailedItem.armor != null) {
			this.dps.setVisibility(View.VISIBLE);
			int armor = (int) this.detailedItem.armor.max;
			this.dps.setText(Integer.toString(armor) + " Armor");
		} else if (this.detailedItem.dps != null) {
			double dpsValue = this.detailedItem.dps.max;
			this.dps.setVisibility(View.VISIBLE);
			this.dps.setText(String.format("%.1f DPS", dpsValue));
		} else {
			this.dps.setVisibility(View.GONE);			
		}

		StringBuilder builder = new StringBuilder();

		for (ItemStat stat : this.detailedItem.primaryAttributes) {
			builder.append('\u25C8' + " " + stat.text + "\n");
		}
		
		String text = "See what other " + this.hero.d3class + "s use in the " + this.sectionName + " slot.";
		this.action1Text2.setText(text);

		
		String text1 = builder.toString();	
		this.attributes.setText(text1.substring(0,text1.length()-1));
		
		StringBuilder builder2 = new StringBuilder();

		for (ItemStat stat : this.detailedItem.secondaryAttributes) {
			builder2.append('\u25C8' + " " + stat.text + "\n");
		}	

		String text2 = builder2.toString();
		
		if (text2.length() > 1)
			this.attributesSecondary.setText(text2.substring(0,text2.length()-1));		
		
		StringBuilder builder3 = new StringBuilder();

		for (ItemStat stat : this.detailedItem.passiveAttributes) {
			builder3.append(stat.text + "\n");
		}
		
		String text3 = builder3.toString();
		
		if (text3.length() > 1)
			this.attributesPassive.setText(text3.substring(0,text3.length()-1));
		else
			this.attributesPassive.setVisibility(View.GONE);
		
		
		setGems();
	}

	private void setGems() {

		ImageView iv;
		TextView stats;
		TextView type;
		View container;
		Gem gem;

		List<DetailedItem> gems = this.detailedItem.gems;
		
		
		if (gems.size() == 0)
			this.line.setVisibility(View.GONE);

		if (gems != null && gems.size() > 0) {
			gem = (Gem) gems.get(0);

			container = this.view.findViewById(R.id.gem1);
			container.setVisibility(View.VISIBLE);

			String iconUri = itemService.getIconUri(gem.icon);
			iv = (ImageView) this.view.findViewById(R.id.gem1_icon);
			Picasso.with(this.getActivity()).load(iconUri).into(iv);

			stats = (TextView) this.view.findViewById(R.id.gem1_stats);
			stats.setText(gem.primaryAttributes.get(0).text);

			type = (TextView) this.view.findViewById(R.id.gem1_type);
			type.setText(gem.name);
		}

		if (gems != null && gems.size() > 1) {
			gem = (Gem) gems.get(1);

			container = this.view.findViewById(R.id.gem2);
			container.setVisibility(View.VISIBLE);

			String iconUri = itemService.getIconUri(gem.icon);
			iv = (ImageView) this.view.findViewById(R.id.gem2_icon);
			Picasso.with(this.getActivity()).load(iconUri).into(iv);

			stats = (TextView) this.view.findViewById(R.id.gem2_stats);
			stats.setText(gem.primaryAttributes.get(0).text);

			type = (TextView) this.view.findViewById(R.id.gem2_type);
			type.setText(gem.name);
		}

		if (gems != null && gems.size() > 2) {
			gem = (Gem) gems.get(2);

			container = this.view.findViewById(R.id.gem3);
			container.setVisibility(View.VISIBLE);

			String iconUri = itemService.getIconUri(gem.icon);
			iv = (ImageView) this.view.findViewById(R.id.gem3_icon);
			Picasso.with(this.getActivity()).load(iconUri).into(iv);

			stats = (TextView) this.view.findViewById(R.id.gem3_stats);
			stats.setText(gem.primaryAttributes.get(0).text);

			type = (TextView) this.view.findViewById(R.id.gem3_type);
			type.setText(gem.name);
		}

	}

	@Override
	public String getDescription() {
		return sectionName;
	}

	@Override
	protected void onHeroChanged() {
		 itemService = new ItemService(hero.server);
	}

	@Override
	public void onClick(View v) {
	}

}
