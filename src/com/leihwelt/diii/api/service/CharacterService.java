package com.leihwelt.diii.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Hero;

public class CharacterService extends BaseService {

	protected String battleTagName;
	protected String battleTagCode;
	private String server;

	@SuppressWarnings("unused")
	private CharacterService() {

	}

	public CharacterService(String server, String battleTagName, String battleTagCode) {

		this.battleTagName = battleTagName;
		this.battleTagCode = battleTagCode;
		this.server = server;

	}

	public CharacterService(String server, String profile) {

		if (profile.contains("#")) {

			String[] data = profile.split("#");

			this.battleTagName = data[0];
			this.battleTagCode = data[1];
		} else if (profile.contains("-")) {

			String[] data = profile.split("-");

			this.battleTagName = data[0];
			this.battleTagCode = data[1];
		}

		this.server = server;
	}

	@SuppressWarnings("unchecked")
	public DetailedHero getDetailsForHero(int heroId) {

		try {
			String serverbase = BASE_URI.replace("##", this.server);
			String profileUri = serverbase + API_BASE + PROFILE_BASE + this.battleTagName + "-" + this.battleTagCode + "/hero/" + heroId;
			String content = this.getContentForUri(profileUri);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> data = (Map<String, Object>) mapper.readValue(content, Map.class);

			return new DetailedHero(this.server, data);
		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Hero> getHeroes() {

		List<Hero> heroes = new ArrayList<Hero>();
		try {

			String bnetAccount = this.battleTagName + "#" + this.battleTagCode;
			String serverbase = BASE_URI.replace("##", this.server);
			String profileUri = serverbase + API_BASE + PROFILE_BASE + this.battleTagName + "-" + this.battleTagCode + "/";
			String content = this.getContentForUri(profileUri);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, ArrayList<Object>> profileMap;
			profileMap = (Map<String, ArrayList<Object>>) mapper.readValue(content, Map.class);

			if (profileMap != null && profileMap.containsKey("heroes")) {
				for (Object heroData : profileMap.get("heroes")) {

					Hero newHero = new Hero(bnetAccount, this.server, (Map<String, Object>) heroData);
					heroes.add(newHero);
				}
			}

		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}

		return heroes;

	}

}
