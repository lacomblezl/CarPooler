package com.activities.carpooler;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Activite permettant de selectionner le trajet concerne ainsi que la date
 * @author Loic Lacomblez
 *
 */
public class NewJourneyActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_journey);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_journey, menu);
		return true;
	}

}
