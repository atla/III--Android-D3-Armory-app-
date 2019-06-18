package com.leihwelt.android.iii;

import java.util.List;

import com.leihwelt.android.iii.events.FirstCharactersAddedEvent;
import com.leihwelt.android.iii.events.HeroSelectedEvent;
import com.leihwelt.android.iii.events.HeroesLoadedEvent;
import com.leihwelt.android.iii.events.RemoveCharacterEvent;
import com.leihwelt.android.iii.events.ShowHero;
import com.leihwelt.android.iii.events.ShowInstructionsEvent;
import com.leihwelt.android.util.LocalPersistence;
import com.leihwelt.diii.api.model.Hero;
import com.squareup.otto.Subscribe;

import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;
import de.timroes.android.listview.EnhancedListView.SwipeDirection;
import de.timroes.android.listview.EnhancedListView.UndoStyle;
import de.timroes.android.listview.EnhancedListView.Undoable;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class HeroSelectionFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {// ,
	// OnDismissCallback
	// {

	private ListView list;
	private HeroesListAdapter adapter;
	private static boolean firstResume = true;

	public static class StoreHeroesTask extends AsyncTask<Void, Void, Void> {

		private List<Hero> heroes;
		private Context context;

		public StoreHeroesTask(Context ctx, List<Hero> heroes) {
			this.heroes = heroes;
			this.context = ctx;
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			LocalPersistence.witeObjectToFile(context, heroes, "heroes");

			return null;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.character_list_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.adapter = new HeroesListAdapter();
		this.list = (ListView) view.findViewById(R.id.list);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		this.list.setOnItemLongClickListener(this);
		this.list.setLongClickable(true);
		// this.list.setDismissCallback(this);
		// this.list.enableSwipeToDismiss();
		//

		List<Hero> loadedHeroes = (List<Hero>) LocalPersistence.readObjectFromFile(this.getActivity(), "heroes");

		if (loadedHeroes != null) {
			this.adapter.addHeroes(loadedHeroes);
		} else {
			BUS.INSTANCE.post(new ShowInstructionsEvent());
		}

	}

	@SuppressWarnings("unchecked")
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

	@Override
	public void onResume() {
		super.onResume();

//		if (firstResume) {
			this.selectLastSelectedHero();

			if (this.adapter.getHeroes().size() == 0) {
				BUS.INSTANCE.post(new ShowInstructionsEvent());
			}

//			firstResume = false;
//		}

	}

	@Subscribe
	public void onRemoveCharacter(RemoveCharacterEvent event) {

		this.adapter.removeHero(event.hero);
		storeHeroes();
		if (this.adapter.getHeroes().size() == 0) {
			BUS.INSTANCE.post(new ShowInstructionsEvent());
		}

	}

	@Subscribe
	public void onHeroesLoaded(HeroesLoadedEvent event) {

		if (this.adapter.getHeroes().size() == 0 && event.heroes.size() > 0) {
			BUS.INSTANCE.post(new FirstCharactersAddedEvent());
		}

		this.adapter.addHeroes(event.heroes);

		storeHeroes();

		selectLastSelectedHero();
	}

	private void storeHeroes() {
		(new StoreHeroesTask(this.getActivity(), this.adapter.getHeroes())).execute();
	}

	private void selectLastSelectedHero() {
		int heroId = Preferences.INSTANCE.getLastSelectedHero(getActivity());

		if (heroId > 0) {

			Hero hero = findHeroById(heroId);

			if (hero != null) {
				BUS.INSTANCE.post(new HeroSelectedEvent(hero));
			}

		}
	}

	private Hero findHeroById(int heroId) {

		for (Hero hero : this.adapter.getHeroes()) {
			if (hero.id == heroId) {
				return hero;
			}
		}

		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Hero hero = (Hero) this.adapter.getItem(position);
		Preferences.INSTANCE.setLastSelectedHero(hero.id, getActivity());
		BUS.INSTANCE.post(new HeroSelectedEvent(hero));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		Hero hero = (Hero) this.adapter.getItem(position);
		(new ShowDeleteHeroDialog(hero)).show(this.getActivity().getSupportFragmentManager(), "REMOVE_DIALOG");

		return false;
	}

	// @Override
	// public Undoable onDismiss(EnhancedListView arg0, int arg1) {
	//
	// // Store the item for later undo
	// /*
	// * final Person item = (Person) mAdapter.getItem(position); // Remove
	// * the item from the adapter mAdapter.remove(position);
	// */
	// // return an Undoable
	// return new EnhancedListView.Undoable() {
	// @Override
	// public void undo() {
	// // mAdapter.insert(position, item);
	// }
	//
	// @Override
	// public String getTitle() {
	// return "FOO";
	// // return "Deleted '" + item.getFullName() . "'"; // Plz, use
	// // the resource system :)
	// }
	//
	// @Override
	// public void discard() {
	// // MyDatabasUtils.delete(item);
	// }
	// };
	//
	// }
}
