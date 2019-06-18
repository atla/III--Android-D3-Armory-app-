package com.leihwelt.diii.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.DetailedItem;
import com.leihwelt.diii.api.model.Hero;
import com.leihwelt.diii.api.model.Item;
import com.leihwelt.diii.api.service.CharacterService;
import com.leihwelt.diii.api.service.ItemService;

public class ServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CharacterService service = new CharacterService("eu", "atla", "2554");
		ItemService itemService = new ItemService("eu");

		List<Hero> heroes = service.getHeroes();

		System.out.println(heroes);

		DetailedHero detailedHero = service.getDetailsForHero(heroes.get(1).id);

		System.out.println(detailedHero);

		DetailedItem detailedItem = itemService.getDetailsForItem(detailedHero.mainHand.tooltipParams);

		// byte[] iconData = itemService.getIcon(detailedHero.torso.icon);

		System.out.println(detailedItem);

	}

}
