package com.leihwelt.android.iii;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.leihwelt.android.iii.events.RemoveCharacterEvent;
import com.leihwelt.diii.api.model.Hero;

@SuppressLint("ValidFragment")
public class ShowDeleteHeroDialog extends DialogFragment {

	private Hero hero;

	
	public ShowDeleteHeroDialog(Hero hero) {
		this.hero = hero;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Do you want to remove " + hero.name + " from the list of characters?")
				.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						BUS.INSTANCE.post(new RemoveCharacterEvent(hero));

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		return builder.create();
	}
}