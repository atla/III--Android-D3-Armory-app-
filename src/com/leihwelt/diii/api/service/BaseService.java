package com.leihwelt.diii.api.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public abstract class BaseService {

	protected final static String BASE_URI = "http://##.battle.net";
	protected final static String API_BASE = "/api/d3/";
	protected final static String PROFILE_BASE = "profile/";
	
		protected final static String ITEM_SKILL_BASE = "http://media.blizzard.com/d3/icons/skills/64/";
		protected final static String RUNE_SKILL_BASE = "http://media.blizzard.com/d3/icons/rune/64/";
	protected final static String ITEM_ICON_BASE = "http://media.blizzard.com/d3/icons/items/large/";
	protected final static String ICON_EXT = ".png";
	private static final String TAG = BaseService.class.getCanonicalName();

	private OkHttpClient client;

	public BaseService() {
		this.client = new OkHttpClient();
	}

	protected String getContentForUri(String uri) {
		 System.out.println("getContentForUri: " + uri);

		String content = null;
		InputStream in = null;
		BufferedReader rd = null;

		try {

			HttpURLConnection connection = client.open(new URL(uri));

			in = connection.getInputStream();
			StringBuilder responseContent = new StringBuilder();
			// Get the response
			rd = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = rd.readLine()) != null) {
				responseContent.append(line);
			}

			content = responseContent.toString();

		} catch (IOException e) {

		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
				}
		}

		return content;
	}

	protected byte[] getDataForUri(String uri) {
		// System.out.println("getDataForUri: " + uri);

		byte[] data = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in;

		try {
			HttpURLConnection connection = client.open(new URL(uri));
			in = connection.getInputStream();

			data = readFully(in);

		} catch (IOException e) {
			Log.e(TAG, "Could not read data for uri: " + uri);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
			}
		}

		return data;
	}

	private byte[] readFully(InputStream is) throws IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

}
