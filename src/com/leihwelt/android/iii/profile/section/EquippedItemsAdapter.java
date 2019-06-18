package com.leihwelt.android.iii.profile.section;

import java.util.ArrayList;
import java.util.List;

import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Item;
import com.leihwelt.diii.api.service.ItemService;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquippedItemsAdapter extends BaseAdapter {

	public static class ItemEntry {
		String type;
		Item item;

		public ItemEntry(String type, Item item) {
			this.type = type;
			this.item = item;
		}
	}

	private List<ItemEntry> items = new ArrayList<ItemEntry>();
	private ItemService itemService;
	private DetailedHero hero;

	private final String smallItems = "NeckFingerWaist";

	public void setItems(DetailedHero hero) {

		this.hero = hero;
		this.itemService = new ItemService(hero.server);

		addItem("Head", this.hero.head);
		addItem("Shoulders", this.hero.shoulders);
		addItem("Neck", this.hero.neck);
		addItem("Torso", this.hero.torso);
		addItem("Hands", this.hero.hands);
		addItem("Wrists", this.hero.bracers);
		addItem("Waist", this.hero.waist);
		addItem("Finger", this.hero.leftFinger);
		addItem("Finger", this.hero.rightFinger);

		addItem("Legs", this.hero.legs);
		addItem("Feet", this.hero.feet);
		addItem("Main-Hand", this.hero.mainHand);

		addItem("Off-Hand", this.hero.offHand);

		this.notifyDataSetChanged();
	}

	private void addItem(String string, Item item) {

		if (item == null)
			return;

		this.items.add(new ItemEntry(string, item));

	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ItemViewHolder viewHolder = null;
		Context context = parent.getContext();

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.item_list_item, parent, false);
			viewHolder = new ItemViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}

		ItemEntry item = (ItemEntry) this.getItem(position);
		String iconUri = itemService.getIconUri(item.item.icon);

		Picasso.with(context).load(iconUri).centerInside().resize(120, 200).into(viewHolder.icon);

		viewHolder.itemName.setText(item.item.name);
		viewHolder.color.setBackgroundResource(getItemDrawable(item.item));
		viewHolder.color.setText(getItemText(item.item));

		return convertView;
	}

	private CharSequence getItemText(Item item) {

		if (item.displayColor == null)
			return "Normal";

		if (item.displayColor.equalsIgnoreCase("orange"))
			return "Legendary Item";
		if (item.displayColor.equalsIgnoreCase("green"))
			return "Set Item";
		if (item.displayColor.equalsIgnoreCase("yellow"))
			return "Rare Item";
		if (item.displayColor.equalsIgnoreCase("blue"))
			return "Magic Item";
		return "Normal";
	}

	private int getItemDrawable(Item item2) {

		if (item2.displayColor == null)
			return R.drawable.white_bubble;

		if (item2.displayColor.equals("orange"))
			return R.drawable.orange_bubble;

		if (item2.displayColor.equals("green"))
			return R.drawable.green2_bubble;

		if (item2.displayColor.equals("yellow"))
			return R.drawable.yellow_bubble;

		if (item2.displayColor.equals("blue"))
			return R.drawable.blue_bubble;

		return R.drawable.white_bubble;
	}

	private class ItemViewHolder {

		public TextView itemName;
		public TextView itemDescription;
		public ImageView icon;
		private TextView color;

		public ItemViewHolder(View base) {
			this.itemName = (TextView) base.findViewById(R.id.item_name);
			this.itemDescription = (TextView) base.findViewById(R.id.item_description);
			this.icon = (ImageView) base.findViewById(R.id.icon);
			this.color = (TextView) base.findViewById(R.id.color);
		}

	}

}
