package com.leihwelt.android.iii.profile.section.skills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.leihwelt.android.iii.R;
import com.leihwelt.android.iii.profile.section.Section;
import com.leihwelt.diii.api.model.Skill;

public class SkillsSection extends Section implements OnItemClickListener {

	private View view;
	private ListView list;
	private SkillsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.equipped_items_section_fragment, null);

		this.list = (ListView) this.view.findViewById(R.id.list);
		this.adapter = new SkillsAdapter();
		this.list.setAdapter(adapter);
		this.list.setDrawSelectorOnTop(true);

		this.list.setOnItemClickListener(this);
		if (this.hero != null) {
			updateAdapter();
		}

		return this.view;
	}

	private void updateAdapter() {
		this.adapter.setItems(this.hero);
	}

	@Override
	public String getDescription() {
		return "Skills";
	}

	@Override
	protected void onHeroChanged() {

		if (this.adapter != null) {
			this.updateAdapter();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Skill entry = (Skill) this.adapter.getItem(pos);

		entry.showExpanded = !entry.showExpanded;
		adapter.notifyDataSetChanged();
	}
}
