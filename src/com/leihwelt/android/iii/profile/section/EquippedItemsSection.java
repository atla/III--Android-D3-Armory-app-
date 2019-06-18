package com.leihwelt.android.iii.profile.section;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.leihwelt.android.iii.BUS;
import com.leihwelt.android.iii.R;
import com.leihwelt.android.iii.events.JumpedToItemsSectionEvent;
import com.leihwelt.android.iii.events.ShowDetailedItemSection;
import com.leihwelt.android.iii.profile.section.EquippedItemsAdapter.ItemEntry;
import com.leihwelt.diii.api.service.ItemService;

public class EquippedItemsSection extends Section implements OnItemSelectedListener, OnItemClickListener {

	private View view;
	private ListView list;
	private EquippedItemsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.equipped_items_section_fragment, null);

		this.list = (ListView) this.view.findViewById(R.id.list);
		this.adapter = new EquippedItemsAdapter();
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
		return "Items";
	}

	@Override
	protected void onHeroChanged() {

		if (this.adapter != null) {
			this.updateAdapter();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		ItemEntry entry = (ItemEntry) this.adapter.getItem(pos);

		BUS.INSTANCE.post(new ShowDetailedItemSection(entry.type));

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		ItemEntry entry = (ItemEntry) this.adapter.getItem(pos);

		BUS.INSTANCE.post(new ShowDetailedItemSection(entry.type));
		BUS.INSTANCE.post(new JumpedToItemsSectionEvent());

	}
}
