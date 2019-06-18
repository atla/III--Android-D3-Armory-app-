package com.leihwelt.android.iii.profile.section;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.leihwelt.ExpandableHeightGridView;
import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.service.ItemService;

public class CopyOfAllStatsSection extends Section {

	private View view;
	private ExpandableHeightGridView list;
	private AllStatsSectionAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.all_stats_section_fragment, null);

		this.list = (ExpandableHeightGridView) this.view.findViewById(R.id.list);
		this.list.setExpanded(true);
		this.adapter = new AllStatsSectionAdapter();
		this.list.setAdapter(adapter);

		// adapt columns
		this.list.post(new Runnable() {

			@Override
			public void run() {

				Point size = new Point();
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				display.getSize(size);
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);

				int width = (int) (size.x / metrics.density);

				//list.setNumColumns(width > 500 ? 2 : 1);
				list.setNumColumns(1);
			}
		});

		if (this.hero != null) {
			updateAdapter();
		}
		


		return this.view;
	}

	private void updateAdapter() {
		this.adapter.setStats(this.hero.stats);
	
	}

	@Override
	public String getDescription() {
		return "All Stats";
	}

	@Override
	protected void onHeroChanged() {

		if (this.adapter != null) {
			this.updateAdapter();
		}
	}
}
