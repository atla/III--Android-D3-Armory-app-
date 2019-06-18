package com.leihwelt.android.iii;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.leihwelt.android.iii.profile.section.AllStatsSection;
import com.leihwelt.android.iii.profile.section.DetailedItemSection;
import com.leihwelt.android.iii.profile.section.EquippedItemsSection;
import com.leihwelt.android.iii.profile.section.OverviewSection;
import com.leihwelt.android.iii.profile.section.Section;
import com.leihwelt.android.iii.profile.section.skills.SkillsSection;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Hero;
import com.leihwelt.diii.api.model.Item;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;

public class ProfileSectionsPagerAdapter extends FragmentStatePagerAdapter {

	private List<Section> sections = new ArrayList<Section>();
	private Activity act;
	private DetailedHero hero;

	private Map<String, Section> itemSections = new HashMap<String, Section>();

	public ProfileSectionsPagerAdapter(DetailedHero hero, Activity act, FragmentManager fm) {
		super(fm);

		this.hero = hero;
		this.act = act;

		sections.add(new OverviewSection());
		sections.add(new AllStatsSection());
		sections.add(new EquippedItemsSection());
		sections.add(new SkillsSection());
		

		addItem("Head", "helms", this.hero.head);
		addItem("Shoulders", "shoulders", this.hero.shoulders);
		addItem("Neck", "amulets", this.hero.neck);
		addItem("Torso", "chests", this.hero.torso);
		addItem("Hands", "gloves", this.hero.hands);
		addItem("Wrists", "bracers", this.hero.bracers);
		addItem("Waist", "belts", this.hero.waist);
		addItem("Finger", "rings", this.hero.leftFinger);
		addItem("Finger", "rings", this.hero.rightFinger);

		addItem("Legs", "pants", this.hero.legs);
		addItem("Feet", "boots", this.hero.feet);
		addItem("Main-Hand", "one-hand", this.hero.mainHand);
		addItem("Off-Hand", "holdable", this.hero.offHand);

		for (int i = 0; i < this.getCount(); ++i) {

			Section section = (Section) this.getItem(i);

			if (section != null && section.getView() != null) {
				section.setStartDelay(i * 400);
			}

		}
	}

	public void fadeForRemove() {

		for (int i = 0; i < this.getCount(); ++i) {

			Section section = (Section) this.getItem(i);

			if (section != null && section.getView() != null) {
				section.getView().animate().yBy(2000).setStartDelay(i * 10);
			}

		}

	}

	public void fadeForInsert() {

		for (int i = 0; i < this.getCount(); ++i) {

			Section section = (Section) this.getItem(i);

			if (section != null && section.getView() != null) {
				section.getView().setAlpha(0.0f);
				section.getView().animate().y(0.0f).alpha(1.0f).setStartDelay(i * 200);
			}
		}
	}

	private void addItem(String desc, String upgradeName, Item item) {

		if (item != null && item.name != null) {
			Section section = new DetailedItemSection(desc, upgradeName, item);

			sections.add(section);
			itemSections.put(desc, section);
		}

	}

	@Override
	public Fragment getItem(int pos) {

		Section section = sections.get(pos);
		section.setHero(hero);
		return section;

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return sections.size();
	}

	@Override
	public float getPageWidth(int position) {

		Point size = new Point();
		Display display = this.act.getWindowManager().getDefaultDisplay();
		display.getSize(size);

		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		int width = (int) (size.x / metrics.density);

		if (width > 600) {
			// 425 gives 3 cards on 10" landscape!
			return 425.0f / width;
		}

		return 1.0f;
	};

	@Override
	public CharSequence getPageTitle(int position) {
		Section section = this.sections.get(position);
		return section.getDescription();
	}

	public int getItemDetailSection(String type) {
		Section s = this.itemSections.get(type);

		int i;
		for (i = 0; i < this.getCount(); ++i) {
			if (this.getItem(i) == s) {
				return i;
			}
		}

		return i;

	}
}
