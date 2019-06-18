package com.leihwelt.android.iii;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leihwelt.android.iii.profile.section.OverviewSection.CutRectTransformation;
import com.leihwelt.diii.api.BattleNetResources;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Hero;
import com.squareup.picasso.Picasso;

public class AvatarHeroesListAdapter extends BaseAdapter {

	private List<Hero> heroes = new ArrayList<Hero>();

	@Override
	public int getCount() {
		return heroes.size();
	}
	

	public void addHeroes(List<Hero> newHeroes) {

		for (Hero h : newHeroes) {
			if (!contains(heroes, h)) {
				this.heroes.add(h);
			}
		}
		this.notifyDataSetChanged();
	}

	private boolean contains(List<Hero> heroes2, Hero h) {

		for (Hero h2 : heroes2) {
			if (h2.id == h.id)
				return true;
		}
		return false;
	}

	@Override
	public Object getItem(int position) {
		return heroes.get(position);
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
			convertView = li.inflate(R.layout.character_item_avatar, parent, false);
			viewHolder = new ItemViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}

		Hero hero = (Hero) this.getItem(position);

		viewHolder.name.setText(hero.name);
		viewHolder.subline.setText(hero.getDetailLine());
		
		
		String heroPortraitsUri = BattleNetResources.HERO_PORTRAITS_BIG;

		Rect rect = BattleNetResources.INSTANCE.getRectForHeroAvatar(hero.d3class, hero.gender);
		CutRectTransformation transform = new CutRectTransformation(rect.left, rect.top, rect.right, rect.bottom);

		Picasso.with(context).load(heroPortraitsUri).transform(transform).into(viewHolder.image);
		
		
		return convertView;
	}

	private class ItemViewHolder {

		public TextView name;
		public TextView subline;
		public ImageView image;

		public ItemViewHolder(View base) {
			this.name = (TextView) base.findViewById(R.id.text);
			this.subline = (TextView) base.findViewById(R.id.text2);
			this.image = (ImageView)base.findViewById(R.id.avatar_image);
		}

	}

	public List<Hero> getHeroes() {
		return this.heroes;
	}

	public void removeHero(Hero hero) {

		for (Hero h : heroes) {
			if (h.id == hero.id) {
				heroes.remove(h);
				this.notifyDataSetChanged();
				return;
			}
		}

	}

}
