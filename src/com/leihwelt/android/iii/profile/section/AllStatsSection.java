package com.leihwelt.android.iii.profile.section;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leihwelt.ExpandableHeightGridView;
import com.leihwelt.android.iii.R;

public class AllStatsSection extends Section {

	private View view;
	private ExpandableHeightGridView list;
	private AllStatsSectionAdapter adapter;
	private TextView statStrength;
	private TextView statDexterity;
	private TextView statIntelligence;
	private TextView statArmor;
	private TextView statVitality;
	private TextView statDamageIncrease;
	private TextView statAttackSpeed;
	private TextView statCritChance;
	private TextView statCritDamage;
	private TextView statDamage;
	private TextView statBlockAmount;
	private TextView statBlockChance;
	private TextView statDamageReduction;
	private TextView statPhysicalResistance;
	private TextView statColdResistance;
	private TextView statFireResistance;
	private TextView statLightningResistance;
	private TextView statPoisonResistance;
	private TextView statArcaneResistance;
	private TextView statThorns;
	private TextView statLife;
	private TextView statLifeSteal;
	private TextView statLifePerKill;
	private TextView statLifePerHit;
	private TextView statGoldFind;
	private TextView statMagicFind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.all_stats_section_fragment, null);

		// base stats
		this.statStrength = (TextView) this.view.findViewById(R.id.stat_strength);
		this.statDexterity = (TextView) this.view.findViewById(R.id.stat_dexterity);
		this.statIntelligence = (TextView) this.view.findViewById(R.id.stat_intelligence);
		this.statArmor = (TextView) this.view.findViewById(R.id.stat_armor);
		this.statVitality = (TextView) this.view.findViewById(R.id.stat_vitality);
		
		// offense
		this.statDamage = (TextView) this.view.findViewById(R.id.stat_damage);
		this.statDamageIncrease = (TextView) this.view.findViewById(R.id.stat_damage_increase);
		this.statAttackSpeed = (TextView) this.view.findViewById(R.id.stat_attack_speed);
		this.statCritChance = (TextView) this.view.findViewById(R.id.stat_crit_chance);
		this.statCritDamage = (TextView) this.view.findViewById(R.id.stat_crit_damage);

		// defense
		this.statBlockAmount = (TextView) this.view.findViewById(R.id.stat_block_amount);
		this.statBlockChance = (TextView) this.view.findViewById(R.id.stat_block_chance);
		this.statDamageReduction = (TextView) this.view.findViewById(R.id.stat_damage_reduction);
		this.statPhysicalResistance = (TextView) this.view.findViewById(R.id.stat_physical_resistance);
		this.statColdResistance = (TextView) this.view.findViewById(R.id.stat_cold_resistance);
		this.statFireResistance = (TextView) this.view.findViewById(R.id.stat_fire_resistance);
		this.statLightningResistance = (TextView) this.view.findViewById(R.id.stat_lightning_resistance);
		this.statPoisonResistance = (TextView) this.view.findViewById(R.id.stat_poison_resistance);
		this.statArcaneResistance = (TextView) this.view.findViewById(R.id.stat_arcane_resistance);
		this.statThorns = (TextView) this.view.findViewById(R.id.stat_thorns);

		// life stats
		this.statLife = (TextView) this.view.findViewById(R.id.stat_life);
		this.statLifeSteal = (TextView) this.view.findViewById(R.id.stat_life_steal);
		this.statLifePerKill = (TextView) this.view.findViewById(R.id.stat_life_per_kill);
		this.statLifePerHit = (TextView) this.view.findViewById(R.id.stat_life_per_hit);

		// adventure stats
		this.statGoldFind = (TextView) this.view.findViewById(R.id.stat_gold_find);
		this.statMagicFind = (TextView) this.view.findViewById(R.id.stat_magic_find);

		if (this.hero != null) {
			onHeroChanged();
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

		if (this.view != null) {
			Map<String, Object> stats = this.hero.stats;

			// attributes
			this.statStrength.setText(String.format("%d", (Integer) stats.get("strength")));

			this.statDexterity.setText(String.format("%d", (Integer) stats.get("dexterity")));
			this.statIntelligence.setText(String.format("%d", (Integer) stats.get("intelligence")));
			this.statArmor.setText(String.format("%d", (Integer) stats.get("armor")));
			this.statVitality.setText(String.format("%d", (Integer) stats.get("vitality")));
			this.statDamage.setText(String.format("%.0f", (Double) stats.get("damage")));

			// offense
			this.statDamageIncrease.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("damageIncrease")));
			this.statAttackSpeed.setText(String.format("%.2f", (Double) stats.get("attackSpeed")));
			this.statCritChance.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("critChance")));
			this.statCritDamage.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("critDamage")));

			// defense

			// offense
			this.statBlockAmount.setText(String.format("%d", (Integer) stats.get("blockAmountMin")));
			this.statBlockChance.setText(String.format("%.2f%%", 100.0f * (Double) stats.get("blockChance")));
			this.statDamageReduction.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("damageReduction")));

			this.statPhysicalResistance.setText(String.format("%d", (Integer) stats.get("physicalResist")));

			this.statColdResistance.setText(String.format("%d", (Integer) stats.get("coldResist")));
			this.statFireResistance.setText(String.format("%d", (Integer) stats.get("fireResist")));
			this.statLightningResistance.setText(String.format("%d", (Integer) stats.get("lightningResist")));

			this.statPoisonResistance.setText(String.format("%d", (Integer) stats.get("poisonResist")));
			this.statArcaneResistance.setText(String.format("%d", (Integer) stats.get("arcaneResist")));
			this.statThorns.setText(String.format("%.1f", (Double) stats.get("thorns")));

			// life
			this.statLife.setText(String.format("%d", (Integer) stats.get("life")));
			this.statLifeSteal.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("lifeSteal")));
			this.statLifePerKill.setText(String.format("%.0f", (Double) stats.get("lifePerKill")));
			this.statLifePerHit.setText(String.format("%.0f", (Double) stats.get("lifeOnHit")));

			// adventure
			this.statGoldFind.setText(String.format("%.1f%%", 100.0f * (Double) stats.get("goldFind")));

			this.statMagicFind.setText(String.format("%.1f%%", 100.0f *  (Double) stats.get("magicFind")));

		}

	}

	private String getDouble(Object object, int factor) {
		double val = (Double) object;
		int casted = (int) (val * factor);

		return String.valueOf(casted);
	}

	private String getInt(Object object) {
		int val = (Integer) object;

		return String.valueOf(val);
	}
}
