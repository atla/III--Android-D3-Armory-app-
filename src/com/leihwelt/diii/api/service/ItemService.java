package com.leihwelt.diii.api.service;

import java.io.IOException;
import java.util.Map;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leihwelt.diii.api.model.DetailedItem;

public class ItemService extends BaseService {

	protected final static String ITEM_BASE = "data/";
	private String server;

	public ItemService(String server) {
		this.server = server;
	}

	@SuppressWarnings("unchecked")
	public DetailedItem getDetailsForItem(String id) {

		try {

			String base = BASE_URI.replace("##", server);
			String profileUri = base + API_BASE + ITEM_BASE + id;
			String content = this.getContentForUri(profileUri);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> data = (Map<String, Object>) mapper.readValue(content, Map.class);

			return new DetailedItem(data);
		} catch (JsonParseException e) {
			Log.d("ItemService", "error parsing data " + e.toString());
		} catch (JsonMappingException e) {
			Log.d("ItemService", "error parsing data " + e.toString());
		} catch (IOException e) {
			Log.d("ItemService", "error parsing data " + e.toString());
		} catch (NullPointerException e) {
			Log.d("ItemService", "error parsing data " + e.getMessage());
			System.out.println (e.getStackTrace());
		}

		return null;
	}

	public byte[] getIcon(String icon) {

		String iconUri = this.getIconUri(icon);

		return this.getDataForUri(iconUri);
	}

	public String getIconUri(String icon) {
		return ITEM_ICON_BASE + icon + ICON_EXT;
	}
	
	public String getSkillUri (String icon){
		return ITEM_SKILL_BASE + icon + ICON_EXT;
	}
	
	public String getRuneUri (String icon){
		return RUNE_SKILL_BASE + icon + ICON_EXT;
	}

}
