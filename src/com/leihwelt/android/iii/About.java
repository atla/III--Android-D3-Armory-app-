package com.leihwelt.android.iii;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.about);

		if (this.getActionBar() != null){
			Drawable back = this.getResources().getDrawable(R.drawable.black_drawable);
			this.getActionBar().setBackgroundDrawable(back);	
		}
		
		
	}

	public void openWebURL(String inURL) {
		Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

		startActivity(browse);
	}

	public void rateClicked(View v) {
		openWebURL("https://play.google.com/store/apps/details?id=com.leihwelt.android.write2");

	}

	public void googleClicked(View v) {
		openWebURL("https://plus.google.com/115465922685935745669/posts");
	}

	public void twitterClicked(View v) {
		openWebURL("http://twitter.com/atla_");
	}

	public void showOtto(View v) {
		openWebURL("http://square.github.io/otto/");
	}

	public void showJackson(View v) {
		openWebURL("http://jackson.codehaus.org/");
	}

	public void showPicasso(View v) {
		openWebURL("http://square.github.io/picasso/");
	}

	public void showOkHttp(View v) {
		openWebURL("http://square.github.io/okhttp/");
	}
	
	public void showPagerSlidingTabStrip(View v) {
		openWebURL("https://github.com/astuetz/PagerSlidingTabStrip");
	}
	

}
