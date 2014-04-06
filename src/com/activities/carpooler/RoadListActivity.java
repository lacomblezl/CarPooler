package com.activities.carpooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Road;

public class RoadListActivity extends Activity
{
	public static final String ROAD_ID = "Road_id";	// champ d'extra pour l'intent

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_list);
		
		/* Mise a jour du titre */
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(getText(R.string.roads));
		
		/* remplissage de la liste et listener dessus ! */
		ListView list = (ListView) findViewById(R.id.main_list);
		fillList(list);
		list.setOnItemClickListener(listListener);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.road_list, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ListView list = (ListView) findViewById(R.id.main_list);
		fillList(list);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case R.id.road_add:
			Intent i = new Intent(this, RoadDetailActivity.class);
			i.putExtra(ROAD_ID, -1);
			startActivity(i);
		}
		return false;
	}
	
	/**
	 * Rempli la liste des itineraires connus
	 */
	private void fillList(ListView list)
	{	
		list.setAdapter(new ArrayAdapter<Road>(this, android.R.layout.simple_list_item_1,
				CarPooler.roadList));
	}
	
	/**
	 * Listener sur les elements de la liste
	 */
	private OnItemClickListener listListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{	
			/* Determine l'id de l'itineraire qui a ete selectionne dans la liste et lance l'activite
			 * en lui passant l'id de la selection.
			 */
			Intent intent = new Intent(view.getContext(), RoadDetailActivity.class);
			intent.putExtra(ROAD_ID, position); //Optional parameters
			startActivity(intent);
		}
	};

}
