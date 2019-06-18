package com.leihwelt.android.iii.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.model.Hero;

public class HeroSearchResultListAdapter extends BaseAdapter {

	private List<Hero> heroes = new ArrayList<Hero>();
	private SparseBooleanArray hselected = new SparseBooleanArray();

	@Override
	public int getCount() {
		return heroes.size();
	}

	public void addHeroes(List<Hero> newHeroes) {

		this.hselected.clear();
		this.heroes.clear();
		
		for (Hero h : newHeroes) {
			hselected.put(h.id, true);
		}

		this.heroes.addAll(newHeroes);
		this.notifyDataSetChanged();

	}

	public List<Hero> getSelectedHeroes() {

		List<Hero> selectedHeroes = new LinkedList<Hero>();

		for (Hero h : heroes) {
			if (hselected.get(h.id) == true) {
				selectedHeroes.add(h);
			}
		}

		return selectedHeroes;

	}

	public void toggle(Hero hero) {
		boolean selected = hselected.get(hero.id);
		hselected.put(hero.id, !selected);
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
			convertView = li.inflate(R.layout.character_item_light, parent, false);
			viewHolder = new ItemViewHolder(convertView);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}

		final Hero hero = (Hero) this.getItem(position);
		boolean selected = hselected.get(hero.id);

		String label = hero.name + ", level " + hero.level + " " + hero.d3class + (hero.hardcore ? " (HC)" : "");

		viewHolder.text.setText(label);
		viewHolder.check.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
		return convertView;
	}

	private class ItemViewHolder {

		public ImageView check;
		public TextView text;

		public ItemViewHolder(View base) {
			this.check = (ImageView) base.findViewById(R.id.check);
			this.text = (TextView) base.findViewById(R.id.text);
		}

	}

	public List<Hero> getHeroes() {
		return this.heroes;
	}

}
