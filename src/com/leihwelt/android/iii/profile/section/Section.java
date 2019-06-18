package com.leihwelt.android.iii.profile.section;

import com.leihwelt.diii.api.model.DetailedHero;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class Section extends Fragment {

	private int startDelay = 0;

	public abstract String getDescription();

	protected abstract void onHeroChanged();

	protected Section() {
		this.setRetainInstance(true);
	}

	public DetailedHero getHero() {
		return hero;
	}

	public void setHero(DetailedHero hero) {

		if (this.hero != hero) {
			this.hero = hero;
			this.onHeroChanged();
		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		view.setAlpha(0.0f);

		view.animate().alpha(1.0f).setStartDelay(startDelay);
	}

	public int getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(int startDelay) {
		this.startDelay = startDelay;
	}

	protected DetailedHero hero;

	protected void openWebURL(String inURL) {
		Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

		startActivity(browse);
	}

}
