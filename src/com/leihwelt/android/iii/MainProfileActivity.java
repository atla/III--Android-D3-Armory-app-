package com.leihwelt.android.iii;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.leihwelt.android.iii.events.FirstCharactersAddedEvent;
import com.leihwelt.android.iii.events.HeroSelectedEvent;
import com.leihwelt.android.iii.events.HeroesLoadedEvent;
import com.leihwelt.android.iii.events.JumpedToItemsSectionEvent;
import com.leihwelt.android.iii.events.LoadSpecificHeroEvent;
import com.leihwelt.android.iii.events.ShowCharacterImportEvent;
import com.leihwelt.android.iii.events.ShowDetailedItemSection;
import com.leihwelt.android.iii.events.ShowHero;
import com.leihwelt.android.iii.events.ShowInstructionsEvent;
import com.leihwelt.diii.api.model.DetailedHero;
import com.leihwelt.diii.api.model.Hero;
import com.leihwelt.diii.api.service.CharacterService;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class MainProfileActivity extends FragmentActivity {

	private static final String TAG = MainProfileActivity.class.getCanonicalName();
	private static final int ITEMS_SECTION_ID = 2;
	private ProfileSectionsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private PagerSlidingTabStrip tabs;

	private DrawerLayout drawer;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private View loadingView;
	private View introductionFragment;
	private View selectHeroText;
	private View addCharactersFragment;
	private MenuItem removeCharMenuItem;
	private boolean jumpedToItemsSection = false;

	private static boolean loading = false;
	private static DetailedHero selectedHero = null;
	private static Hero lastSelectedhero = null;

	private class FetchHeroTask extends AsyncTask<Void, Void, DetailedHero> {

		private Hero hero;
		private String profile;

		public FetchHeroTask(Hero hero) {
			this.profile = hero.bnetAccount;
			this.hero = hero;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (pagerAdapter != null) {
				pagerAdapter.fadeForRemove();
			}

			showLoading();
		}

		@Override
		protected DetailedHero doInBackground(Void... params) {

			if (!loading) {
				loading = true;

				// load data
				CharacterService service = new CharacterService(hero.server, profile);
				DetailedHero detailedHero = service.getDetailsForHero(hero.id);

				return detailedHero;

			}

			return null;
		}

		@Override
		protected void onPostExecute(DetailedHero detailedHero) {
			super.onPostExecute(detailedHero);

			loading = false;

			if (detailedHero != null) {
				BUS.INSTANCE.post(new ShowHero(detailedHero));

			}
		}
	}

	private class FetchSpecificHeroTask extends AsyncTask<Void, Void, DetailedHero> {

		private int heroId;
		private String profile;
		private String server;

		public FetchSpecificHeroTask(String server, String profile, int heroId) {
			this.server = server;
			this.heroId = heroId;
			this.profile = profile;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (pagerAdapter != null) {
				pagerAdapter.fadeForRemove();
			}

			showLoading();
		}

		@Override
		protected DetailedHero doInBackground(Void... params) {

			if (!loading) {
				loading = true;

				try {
					// load data
					CharacterService service = new CharacterService(server, profile);

					DetailedHero detailedHero = service.getDetailsForHero(heroId);

					return detailedHero;
				} catch (NullPointerException e) {
					Log.d("FetchSpecificHeroTask", "could not fetch hero");
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(DetailedHero detailedHero) {
			super.onPostExecute(detailedHero);

			loading = false;

			if (detailedHero != null) {
				BUS.INSTANCE.post(new ShowHero(detailedHero));

			}
		}
	}

	@Subscribe
	public void onLoadSpecificHero(LoadSpecificHeroEvent event) {

		this.showLoading();

		(new FetchSpecificHeroTask(event.server, event.profile, event.heroId)).execute();
	}

	public void showSelectHeroText() {

		if (this.pagerAdapter != null) {
			pagerAdapter.fadeForInsert();
		}

		this.selectHeroText.setAlpha(0.0f);
		this.selectHeroText.setVisibility(View.VISIBLE);
		this.selectHeroText.animate().alpha(1.0f).setDuration(3000);
	}

	@Subscribe
	public void onShowDetailedHero(final ShowHero showHeroEvent) {

		Log.e(TAG, "ON SHOW HERO " + showHeroEvent.detailedHero.name);

		hideIntroductionFragment();
		hideSelectHeroText();

		this.selectedHero = showHeroEvent.detailedHero;
		int heroColor = this.getResources().getColor(getHeroColor(selectedHero.d3class));
		changeColor(heroColor);

		this.viewPager.postDelayed(new Runnable() {

			@Override
			public void run() {
				pagerAdapter = new ProfileSectionsPagerAdapter(selectedHero, MainProfileActivity.this, getSupportFragmentManager());

				viewPager.removeAllViews();
				viewPager.setAdapter(pagerAdapter);
				pagerAdapter.notifyDataSetChanged();
				pagerAdapter.fadeForInsert();
				tabs.setViewPager(viewPager);

				hideLoading();
			}
		}, 100);

	}

	private void hideSelectHeroText() {
		this.selectHeroText.setVisibility(View.GONE);
	}

	private void hideIntroductionFragment() {
		if (this.introductionFragment.getVisibility() == View.VISIBLE) {
			this.introductionFragment.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					introductionFragment.setVisibility(View.GONE);
					introductionFragment.animate().setListener(null);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					onAnimationEnd(animation);
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_profile);

		// remove background from main window to reduce GPU overdraw behind our
		// background
		getWindow().setBackgroundDrawable(null);

		this.introductionFragment = this.findViewById(R.id.introduction_section);
		this.introductionFragment.setVisibility(View.GONE);
		this.selectHeroText = this.findViewById(R.id.select_hero_text);
		this.selectHeroText.setVisibility(View.GONE);

		this.addCharactersFragment = this.findViewById(R.id.add_characters_fragment);
		this.addCharactersFragment.setVisibility(View.GONE);

		// Set up the ViewPager with the sections adapter.
		this.drawer = (DrawerLayout) findViewById(R.id.drawer);
		this.loadingView = findViewById(R.id.loading);
		drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mTitle = mDrawerTitle = getTitle();

		mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		this.viewPager = (ViewPager) findViewById(R.id.pager);
		this.viewPager.setOffscreenPageLimit(6);

		LayoutInflater inflater = this.getLayoutInflater();
		View actionbarPager = inflater.inflate(R.layout.actionbar_pager, null);

		this.tabs = (PagerSlidingTabStrip) actionbarPager.findViewById(R.id.pager_title_strip);
		this.tabs.setTextColor(0xFFDEDEDE);

		ActionBar actionBar = this.getActionBar();

		if (actionBar != null) {
			LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(actionbarPager, layout);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		else if (item.getItemId() == R.id.action_add_chars) {
			showAddCharactersDialog();
		} else if (item.getItemId() == R.id.action_about) {
			showAbout();
		} else if (item.getItemId() == R.id.remove_char) {
			removeCurrentChar();
		}

		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	private void removeCurrentChar() {
		(new ShowDeleteHeroDialog(lastSelectedhero)).show(getSupportFragmentManager(), "REMOVE_DIALOG");
	}

	private void showAbout() {
		Intent i = new Intent(this, About.class);
		this.startActivity(i);
	}

	@Subscribe
	public void onFirstCharactersAdded(FirstCharactersAddedEvent event) {
		this.hideIntroductionFragment();

		this.showSelectHeroText();
		this.drawer.openDrawer(Gravity.LEFT);
	}

	@Subscribe
	public void onShowCharacterImport(ShowCharacterImportEvent event) {
		showAddCharactersDialog();
	}

	private void showAddCharactersDialog() {

		this.drawer.closeDrawers();

		hideIntroductionFragment();

		if (this.pagerAdapter != null) {
			this.pagerAdapter.fadeForRemove();
		}

		fadeInView(this.addCharactersFragment);

	}

	private void fadeInView(View v) {

		if (v.getVisibility() == View.VISIBLE)
			return;

		v.setVisibility(View.VISIBLE);
		v.setAlpha(0.0f);
		v.animate().alpha(1.0f).setListener(null);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void loadHeroes() {
		showLoading();
	}

	private void showLoading() {
		this.hideIntroductionFragment();

		this.fadeInView(this.loadingView);
	}

	private void hideLoading() {

		fadeOutView(this.loadingView);

	}

	private void fadeOutView(final View v) {

		if (v.getVisibility() != View.VISIBLE)
			return;

		v.animate().alpha(0.0f).setListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				v.setVisibility(View.GONE);
				v.animate().setListener(null);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				this.onAnimationEnd(animation);
			}
		});
	}

	@Subscribe
	public void onHeroSelected(HeroSelectedEvent heroSelected) {

		Log.e(TAG, "ON HERO SELECTED " + heroSelected.hero.name);

		if (lastSelectedhero != null && lastSelectedhero.id == heroSelected.hero.id)
			return;

		hideAddCharacter();

		lastSelectedhero = heroSelected.hero;

		if (removeCharMenuItem != null) {
			removeCharMenuItem.setVisible(true);
		}

		int heroColor = this.getResources().getColor(getHeroColor(heroSelected.hero.d3class));

		changeColor(heroColor);

		Log.d(TAG, "Hero Selected");

		this.drawer.closeDrawers();

		(new FetchHeroTask(heroSelected.hero)).execute();
	}

	private void hideAddCharacter() {
		this.fadeOutView(this.addCharactersFragment);
	}

	@Subscribe
	public void onHeroesLoaded(HeroesLoadedEvent event) {
		this.fadeOutView(this.addCharactersFragment);
	}

	@Override
	public void onBackPressed() {

		if (this.addCharactersFragment.getVisibility() == View.VISIBLE) {

			this.hideAddCharacter();
			if (this.pagerAdapter != null) {
				this.pagerAdapter.fadeForInsert();
			} else {
				this.onShowInstructions(new ShowInstructionsEvent());
			}

		} else {

			if (this.jumpedToItemsSection) {
				this.viewPager.setCurrentItem(ITEMS_SECTION_ID, false);
				this.jumpedToItemsSection = false;
			} else {
				super.onBackPressed();
			}
		}

	}

	private int getHeroColor(String d3class) {

		if (d3class.equalsIgnoreCase("barbarian"))
			return R.color.hero_barbarian;
		if (d3class.equalsIgnoreCase("monk"))
			return R.color.hero_monk;
		if (d3class.equalsIgnoreCase("demon-hunter"))
			return R.color.hero_demon_hunter;
		if (d3class.equalsIgnoreCase("witch-doctor"))
			return R.color.hero_witch_doctor;
		if (d3class.equalsIgnoreCase("wizard"))
			return R.color.hero_wizard;
		if (d3class.equalsIgnoreCase("crusader"))
			return R.color.hero_crusader;

		return R.color.hero_unknown;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		this.getMenuInflater().inflate(R.menu.main_profile, menu);

		this.removeCharMenuItem = menu.findItem(R.id.remove_char);
		this.removeCharMenuItem.setVisible(false);

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		BUS.INSTANCE.get().register(this);

		if (selectedHero != null) {

			int heroColor = this.getResources().getColor(getHeroColor(selectedHero.d3class));
			changeColor(heroColor);

			viewPager = (ViewPager) this.findViewById(R.id.pager);
			pagerAdapter = new ProfileSectionsPagerAdapter(selectedHero, MainProfileActivity.this, getSupportFragmentManager());
			viewPager.setAdapter(pagerAdapter);
			pagerAdapter.notifyDataSetChanged();
			tabs.setViewPager(viewPager);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		BUS.INSTANCE.get().unregister(this);
	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);
	}

	@Subscribe
	public void onJumpedToItemsSection(JumpedToItemsSectionEvent event) {
		this.jumpedToItemsSection = true;
	}

	@Subscribe
	public void onShowInstructions(ShowInstructionsEvent event) {

		if (this.pagerAdapter == null) {
			this.hideLoading();
			this.introductionFragment.setVisibility(View.VISIBLE);
			this.introductionFragment.setAlpha(0.0f);
			this.introductionFragment.animate().alpha(1.0f).setDuration(1500);
		}

	}

	@Subscribe
	public void onShowDetailedItemSection(ShowDetailedItemSection event) {
		int pos = this.pagerAdapter.getItemDetailSection(event.type);

		this.viewPager.setCurrentItem(pos);
	}

}
