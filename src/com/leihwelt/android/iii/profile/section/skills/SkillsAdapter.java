package com.leihwelt.android.iii.profile.section.skills;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leihwelt.android.iii.R;
import com.leihwelt.android.iii.profile.section.OverviewSection.CutRectTransformation;
import com.leihwelt.diii.api.BattleNetResources;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Skill;
import com.leihwelt.diii.api.service.ItemService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SkillsAdapter extends BaseAdapter {

	private List<Skill> items = new ArrayList<Skill>();
	private ItemService itemService;
	private DetailedHero hero;

	public void setItems(DetailedHero hero) {

		this.hero = hero;
		this.itemService = new ItemService(hero.server);

		this.items.addAll(this.hero.activeSkills);
		this.items.addAll(this.hero.passiveSkills);

		this.notifyDataSetChanged();
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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		Object obj = getItem(position);
		Skill skill = (Skill) obj;

		return skill.showExpanded ? 1 : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ItemViewHolder viewHolder = null;
		Context context = parent.getContext();

		Skill item = (Skill) this.getItem(position);

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (item.showExpanded) {
				convertView = li.inflate(R.layout.skill_list_item_expanded, parent, false);
			} else {
				convertView = li.inflate(R.layout.skill_list_item, parent, false);
			}

			viewHolder = new ItemViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}

		String iconUri = itemService.getSkillUri(item.icon);

		Picasso.with(context).load(iconUri).into(viewHolder.icon);

		viewHolder.itemName.setText(item.name);

		if (item.passive) {
			viewHolder.itemDescription.setText("Passive Skill");
		} else {

			if (item.rune != null) {
				viewHolder.itemDescription.setText(item.rune.name);
			} else {
				viewHolder.itemDescription.setText("");
			}

		}

		if (item.showExpanded && viewHolder.expandedText != null) {
			viewHolder.expandedText.setText(item.description);

			
			
			if (item.rune != null) {
				viewHolder.runeWrapper.setVisibility(View.VISIBLE);
				
				Rect r = BattleNetResources.INSTANCE.getRectForRune(item.rune.type);

				Picasso.with(context).load(BattleNetResources.INSTANCE.RUNES)
						.transform(new CutRectTransformation(r.left, r.top, r.right, r.bottom)).into(viewHolder.runeIcon);

				viewHolder.runeItemName.setText(item.rune.name);
				viewHolder.runeItemDescription.setText(item.rune.simpleDescription);
				viewHolder.runeExpandedText.setText(item.rune.description);
			}
			else {
				viewHolder.runeWrapper.setVisibility(View.GONE);
			}

		}

		return convertView;
	}

	public static class CutRectTransformation implements Transformation {

		private int x;
		private int y;
		private int width;
		private int height;

		public CutRectTransformation(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public Bitmap transform(Bitmap source) {

			Bitmap result = Bitmap.createBitmap(source, x, y, width, height);

			if (result != source) {
				source.recycle();
			}

			return result;
		}

		@Override
		public String key() {
			return "cutout()";
		}
	}

	private class ItemViewHolder {

		public TextView itemName;
		public TextView itemDescription;
		public TextView expandedText = null;
		public ImageView icon;

		public TextView runeItemName;
		public TextView runeItemDescription;
		public TextView runeExpandedText;
		public ImageView runeIcon;
		public View runeWrapper;

		public ItemViewHolder(View base) {
			this.itemName = (TextView) base.findViewById(R.id.item_name);
			this.itemDescription = (TextView) base.findViewById(R.id.item_description);
			this.icon = (ImageView) base.findViewById(R.id.icon);

			View v = base.findViewById(R.id.item_text_expanded);
			if (v != null) {
				this.expandedText = (TextView) v;
			}

			if (this.expandedText != null) {
				this.runeWrapper = base.findViewById(R.id.rune_wrapper);
				this.runeIcon = (ImageView) base.findViewById(R.id.rune_icon);
				this.runeItemName = (TextView) base.findViewById(R.id.rune_item_name);
				this.runeItemDescription = (TextView) base.findViewById(R.id.rune_item_description);
				this.runeExpandedText = (TextView) base.findViewById(R.id.rune_item_text_expanded);
			}

		}

	}

}
