package com.leihwelt.android.iii.search;

import java.util.List;

import com.leihwelt.android.iii.BUS;
import com.leihwelt.android.iii.R;
import com.leihwelt.android.iii.ShowDeleteHeroDialog;
import com.leihwelt.android.iii.R.id;
import com.leihwelt.android.iii.R.layout;
import com.leihwelt.android.iii.events.HeroesFoundEvent;
import com.leihwelt.android.iii.events.HeroesLoadedEvent;
import com.leihwelt.diii.api.model.Hero;
import com.leihwelt.diii.api.service.CharacterService;
import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

public class AddCharactersDialogFragment extends Fragment implements OnItemClickListener {

	private View view;
	private ListView list;
	private EditText playerNameField;
	private ImageButton findButton;
	public boolean loading = false;
	private HeroSearchResultListAdapter adapter;
	private Button addSelected;
	private View mainLayout;
	private Spinner serverSelector;

	private class FetchHeroesTask extends AsyncTask<Void, Void, List<Hero>> {

		private String profile;
		private String server;

		public FetchHeroesTask(String server, String profile) {
			this.profile = profile;
			this.server = server;
		}

		@Override
		protected List<Hero> doInBackground(Void... params) {

			if (!loading) {
				loading = true;

				try{
					// load data
					CharacterService service = new CharacterService(server, profile);
					return service.getHeroes();	
				}
				catch (NullPointerException e) {
					Log.d("FetchHeroesTask", "Caught null pointer.");
				}				
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			findButton.setEnabled(false);
		}

		@Override
		protected void onPostExecute(List<Hero> heroes) {
			super.onPostExecute(heroes);

			loading = false;

			if (heroes != null) {

				BUS.INSTANCE.post(new HeroesFoundEvent(profile, heroes));

			}

			findButton.setEnabled(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.add_chars_dialog_fragment, null);
		this.list = (ListView) this.view.findViewById(R.id.character_add_list);
		this.adapter = new HeroSearchResultListAdapter();
		this.list.setAdapter(this.adapter);
		this.list.setOnItemClickListener(this);

		this.playerNameField = (EditText) this.view.findViewById(R.id.playerNameField);
		this.findButton = (ImageButton) this.view.findViewById(R.id.findButton);
		this.serverSelector = (Spinner) this.view.findViewById(R.id.server);
		this.mainLayout = this.view.findViewById(R.id.main_layout);

		this.addSelected = (Button) this.view.findViewById(R.id.addSelected);
		this.addSelected.setEnabled(false);

		this.addSelected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addAllSelectedHeroes();
			}
		});

		this.findButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loookupCharacter();
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

		return this.view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		BUS.INSTANCE.get().register(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		BUS.INSTANCE.get().unregister(this);
	}

	@Subscribe
	public void onHeroesFoundEvent(HeroesFoundEvent event) {

		this.addSelected.setEnabled(true);

		if (this.adapter != null) {
			this.adapter.addHeroes(event.heroes);
		}

	}

	public void addAllSelectedHeroes() {

		List<Hero> selectedHeroes = this.adapter.getSelectedHeroes();
		BUS.INSTANCE.post(new HeroesLoadedEvent(null, selectedHeroes));

	}

	protected void loookupCharacter() {

		String profile = playerNameField.getText().toString();
		String server = (String) serverSelector.getSelectedItem();

		if (profile.contains("#")) {
			(new FetchHeroesTask(server, profile)).execute();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Hero hero = (Hero) adapter.getItem(pos);

		adapter.toggle(hero);
		adapter.notifyDataSetChanged();
	}


}
