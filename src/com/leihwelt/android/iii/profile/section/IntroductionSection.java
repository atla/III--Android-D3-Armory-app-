package com.leihwelt.android.iii.profile.section;

import com.leihwelt.android.iii.BUS;
import com.leihwelt.android.iii.R;
import com.leihwelt.android.iii.events.LoadSpecificHeroEvent;
import com.leihwelt.android.iii.events.ShowCharacterImportEvent;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class IntroductionSection extends Section {

	private View view;
	private View mainLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.introduction_section, null);

		return view;
	}

	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.mainLayout = this.view.findViewById(R.id.main_layout);

		View dh1 = view.findViewById(R.id.dh_ladygoose);
		dh1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new LoadSpecificHeroEvent("eu", "goosemaster-2222", 17024142));
			}
		});

		View m1 = view.findViewById(R.id.monk_lenaki);
		m1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new LoadSpecificHeroEvent("eu", "raistlin33-2955", 15029859));
			}
		});

		View b1 = view.findViewById(R.id.barb_fighter);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new LoadSpecificHeroEvent("us", "stanley3998-3642", 33426163));
			}
		});

		View w1 = view.findViewById(R.id.wizard_extension);
		w1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new LoadSpecificHeroEvent("eu", "frontier-2614", 29539475));
			}
		});

		View wd1 = view.findViewById(R.id.witch_doctor_lolz);
		wd1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new LoadSpecificHeroEvent("us", "lolz-6844", 32618201));
			}
		});

		View importButton = view.findViewById(R.id.import_characters_button);
		importButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BUS.INSTANCE.post(new ShowCharacterImportEvent());
			}
		});

		view.post(new Runnable() {

			@Override
			public void run() {
				Point size = new Point();
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				display.getSize(size);

				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);

				int width = (int) (size.x / metrics.density);

				if (width < 620) {
					LayoutParams params = view.getLayoutParams();
					params.width = params.MATCH_PARENT;
					view.setLayoutParams(params);

					LayoutParams paramss = mainLayout.getLayoutParams();
					paramss.width = params.MATCH_PARENT;
					mainLayout.setLayoutParams(paramss);
				}

			}
		});

	}

	@Override
	public String getDescription() {
		return "Introduction";
	}

	@Override
	protected void onHeroChanged() {

	}

}
