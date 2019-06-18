package com.leihwelt.diii.api.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DetailedHero {
	public Map<String, Object> stats;
	public String server;

	@SuppressWarnings("unchecked")
	public DetailedHero(String server, Map<String, Object> data) {

		this.name = (String) data.get("name");
		this.id = (Integer) data.get("id");
		this.level = (Integer) data.get("level");
		this.hardcore = (Boolean) data.get("hardcore");
		this.gender = (Integer) data.get("gender");
		this.lastUpdated = (Integer) data.get("last-updated");
		this.dead = (Boolean) data.get("dead");
		this.d3class = (String) data.get("class");
		this.paragonLevel = (Integer) data.get("paragonLevel");

		getSkills(data);

		getItems(data);

		this.stats = (Map<String, Object>) data.get("stats");
		this.server = server;

	}

	@SuppressWarnings("unchecked")
	private void getSkills(Map<String, Object> data) {
		Map<String, Object> skillsMap = (Map<String, Object>) data.get("skills");

		List<Object> activeSkills = (List<Object>) skillsMap.get("active");
		List<Object> passiveSkills = (List<Object>) skillsMap.get("passive");

		for (Object activeSkill : activeSkills) {
			Map<String, Object> asm = (Map<String, Object>) activeSkill;
			Skill skill = new Skill((Map<String, Object>) asm.get("skill"), false);

			if (asm.containsKey("rune")) {
				Rune rune = new Rune((Map<String, Object>) asm.get("rune"), false);
				skill.rune = rune;
			}

			this.activeSkills.add(skill);
		}

		for (Object passiveSkill : passiveSkills) {
			Map<String, Object> asm = (Map<String, Object>) passiveSkill;
			Skill skill = new Skill((Map<String, Object>) asm.get("skill"), true);
			this.passiveSkills.add(skill);
		}

	}

	@SuppressWarnings("unchecked")
	private void getItems(Map<String, Object> data) {

		Map<String, Object> itemsMap = (Map<String, Object>) data.get("items");

		this.head = createItem(itemsMap.get("head"));
		this.torso = createItem(itemsMap.get("torso"));
		this.feet = createItem(itemsMap.get("feet"));
		this.hands = createItem(itemsMap.get("hands"));
		this.shoulders = createItem(itemsMap.get("shoulders"));
		this.legs = createItem(itemsMap.get("legs"));
		this.bracers = createItem(itemsMap.get("bracers"));
		this.offHand = createItem(itemsMap.get("offHand"));
		this.mainHand = createItem(itemsMap.get("mainHand"));
		this.waist = createItem(itemsMap.get("waist"));
		this.rightFinger = createItem(itemsMap.get("rightFinger"));
		this.leftFinger = createItem(itemsMap.get("leftFinger"));
		this.neck = createItem(itemsMap.get("neck"));

		addItem(this.head);
		addItem(this.torso);
		addItem(this.feet);
		addItem(this.hands);
		addItem(this.shoulders);
		addItem(this.legs);
		addItem(this.bracers);
		addItem(this.offHand);
		addItem(this.mainHand);
		addItem(this.waist);
		addItem(this.rightFinger);
		addItem(this.leftFinger);
		addItem(this.neck);
	}

	private void addItem(Item item) {
		if (item == null)
			return;

		this.items.add(item);
	}

	@SuppressWarnings("unchecked")
	private Item createItem(Object object) {
		Map<String, Object> data = (Map<String, Object>) object;

		if (data == null || data.size() == 0)
			return null;

		return new Item(data);
	}

	public String toString() {
		return this.name + "(" + this.level + ", " + this.paragonLevel + ")";
	}

	public String name;
	public int id;
	public int level;
	public int paragonLevel;
	public String d3class;
	public boolean hardcore;
	public int gender;
	public int lastUpdated;
	public boolean dead;

	// items
	public Item head;
	public Item torso;
	public Item feet;
	public Item hands;
	public Item shoulders;
	public Item legs;
	public Item bracers;
	public Item offHand;
	public Item mainHand;
	public Item waist;
	public Item rightFinger;
	public Item leftFinger;
	public Item neck;

	public List<Skill> activeSkills = new LinkedList<Skill>();
	public List<Skill> passiveSkills = new LinkedList<Skill>();
	public List<Item> items = new LinkedList<Item>();

}
