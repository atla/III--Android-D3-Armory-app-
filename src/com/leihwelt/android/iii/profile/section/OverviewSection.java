package com.leihwelt.android.iii.profile.section;

import java.io.IOException;

import com.leihwelt.RoundedAvatarDrawable;
import com.leihwelt.android.iii.R;
import com.leihwelt.diii.api.BattleNetResources;
import com.leihwelt.diii.api.model.DetailedHero;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OverviewSection extends Section {

	private static final String DIABLOPROGRESS_BASE_URI = "http://www.diabloprogress.com/rating.stat_dps_unbuffed/class.";

	public final static String TAG = OverviewSection.class.getCanonicalName();

	private View view;
	private TextView heroName;
	private TextView heroSubline;
	// private TextView heroClass;
	private ImageView avatarIcon;
	private ImageView header;

	private static RoundedAvatarDrawable drawable;

	private TextView stat1;

	private TextView stat2;

	private TextView stat3;

	private TextView stat4;

	private View hardcore;

	private TextView action1Text2;

	private ImageButton action1Button;

	private static boolean AVATAR_DOWNLOAD_ACTIVE = false;

	private class DownloadAvatarTask extends AsyncTask<Void, Void, Bitmap> {

		private DetailedHero hero;
		private Context ctx;

		public DownloadAvatarTask(DetailedHero hero, Context ctx) {

			AVATAR_DOWNLOAD_ACTIVE = true;
			this.hero = hero;
			this.ctx = ctx;
		}

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			try {

				String heroPortraitsUri = BattleNetResources.HERO_PORTRAITS_BIG;

				Rect rect = BattleNetResources.INSTANCE.getRectForHeroAvatar(hero.d3class, hero.gender);
				CutRectTransformation transform = new CutRectTransformation(rect.left, rect.top, rect.right, rect.bottom);

				Bitmap avatar = Picasso.with(ctx).load(heroPortraitsUri).transform(transform).get();

				return avatar;
			} catch (Exception e) {
				Log.e(TAG, "Could not load hero portrait", e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {

			if (result != null) {
				setAvatar(result);
			}

			AVATAR_DOWNLOAD_ACTIVE = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.overview_section_fragment, null);
		this.heroName = (TextView) this.view.findViewById(R.id.hero_name);
		this.heroSubline = (TextView) this.view.findViewById(R.id.hero_subline);
		// this.heroClass = (TextView) this.view.findViewById(R.id.hero_class);
		this.avatarIcon = (ImageView) this.view.findViewById(R.id.avatar_image);
		this.header = (ImageView) this.view.findViewById(R.id.header_background);

		this.hardcore = this.view.findViewById(R.id.hardcore);

		this.stat1 = (TextView) this.view.findViewById(R.id.stat1);
		this.stat2 = (TextView) this.view.findViewById(R.id.stat2);
		this.stat3 = (TextView) this.view.findViewById(R.id.stat3);
		this.stat4 = (TextView) this.view.findViewById(R.id.stat4);

		this.action1Text2 = (TextView) this.view.findViewById(R.id.action1_text2);
		this.action1Button = (ImageButton) this.view.findViewById(R.id.action1_button);
		this.action1Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openItemSuggestions();
			}
		});

		if (this.hero != null) {
			this.onHeroChanged();
		}

		return this.view;

	}

	protected void openItemSuggestions() {

		String base = DIABLOPROGRESS_BASE_URI;
		String d3class = this.hero.d3class;
		openWebURL(base + d3class);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (this.hero != null)
			this.onHeroChanged();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (this.hero != null) {
			// this.drawable = null;

			setToHero();
		}
	}

	@Override
	public String getDescription() {
		return "Overview";
	}

	public void setAvatar(Bitmap avatar) {

		Log.e(TAG, "SET AVATAR CALLED");

		this.drawable = new RoundedAvatarDrawable(avatar);
		// this.avatarIcon.setAlpha(0.0f);
		this.avatarIcon.setImageDrawable(drawable);
		// this.avatarIcon.animate().alpha(1.0f);
	}

	@Override
	protected void onHeroChanged() {

		drawable = null;
		
		setToHero();

	}

	private void setToHero() {
		// check if this fragment is attached
		if (this.getActivity() == null || this.isDetached() || this.view == null)
			return;

		if (!AVATAR_DOWNLOAD_ACTIVE && this.drawable == null) {
			(new DownloadAvatarTask(hero, this.getActivity())).execute();
		}

		this.hardcore.setVisibility(this.hero.hardcore ? View.VISIBLE : View.GONE);

		loadHeader();

		if (this.view != null) {
			this.heroName.setText(this.hero.name);

			String subline = "";

			if (this.hero.paragonLevel == 0)
				subline = String.valueOf(this.hero.level) + " " + this.hero.d3class;
			else
				subline = String.valueOf(this.hero.level) + " (" + this.hero.paragonLevel + ") " + this.hero.d3class;

			this.heroSubline.setText(subline);

			double dmg = (Double) this.hero.stats.get("damage");

			if (dmg > 1000) {
				float dmgShort = (float) (dmg / 1000.0f);
				this.stat1.setText(String.format("%.1fk DPS", dmgShort));
			} else {
				this.stat1.setText(String.format("%.1f DPS", dmg));
			}

			double crit = (Double) this.hero.stats.get("critChance");
			this.stat2.setText(String.format("%.1f", crit * 100) + "% Crit");

			int life = (Integer) this.hero.stats.get("life");

			if (life > 1000) {
				float lifeShort = (float) (life / 1000.0f);
				this.stat3.setText(String.format("%.1fk Life", lifeShort));
			} else {
				this.stat3.setText(life + " Life");
			}

			int armor = (Integer) this.hero.stats.get("armor");
			this.stat4.setText((int) (armor) + " Armor");

			String text = "See the current top " + this.hero.d3class + " characters worldwide.";
			this.action1Text2.setText(text);

		}
	}

	private void loadHeader() {
		// do after UI is drawn
		this.header.post(new Runnable() {

			@Override
			public void run() {

				int width = header.getWidth();
				int height = header.getHeight();
				int imgWidth = 480;
				float scale = (imgWidth * 1.0f) / width;

				String uri = BattleNetResources.INSTANCE.getHeaderImageUri(hero.d3class, hero.gender);
				Picasso.with(getActivity()).load(uri).placeholder(R.drawable.no_image).skipMemoryCache()
						.transform(new CutRectTransformation(0, 30, imgWidth, (int) (height * scale))).into(header);
			}
		});
	}

	public static class CutRectTransformation implements Transformation {

		private int x;
		private int y;
		private int width;
		private int height;

		public CutRectTransformation(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public Bitmap transform(Bitmap source) {

			Bitmap result = Bitmap.createBitmap(source, x, y, width, height);

			if (result != source) {
				source.recycle();
			}

			return result;
		}

		@Override
		public String key() {
			return "cutout()";
		}
	}

}
