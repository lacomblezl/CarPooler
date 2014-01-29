package com.activities.carpooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.driver.carpooler.CarPooler;

public class HomeActivity extends Activity
{
	//liste des boutons du menu 
	public String[] listText = new String[4];
	public int[] listIcon = new int[4];
	
	ListView listMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		/* Instanciation de la database */
		CarPooler.openDB(getApplicationContext());
		
		listText[0]=getString(R.string.new_journey);
		listText[1]=getString(R.string.contacts);
		listText[2]=getString(R.string.roads);
		listText[3]=getString(R.string.history);
	    
		listMenu = (ListView) findViewById(R.id.home_list);
		
		/* remplissage de la liste et listener sur chaque element de la liste */
		listMenu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listText));
		listMenu.setOnItemClickListener(menuListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/**
	 * Listener associe a la liste correspondant au menu d'acceuil
	 * Lance l'activite associee a l'element selectionne
	 */
	private OnItemClickListener menuListener = new OnItemClickListener()
	{	
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{	
			/* Determine l'option qui a ete selectionnee dans la liste et lance l'activite
			 * adaptee.
			 */
			Intent intent;
			switch (position)
			{
				case 0: intent = new Intent(view.getContext(), NewJourneyActivity.class);
						break;
				case 1: intent = new Intent(view.getContext(), ContactListActivity.class);
						break;
				case 2: intent = new Intent(view.getContext(), RoadListActivity.class);
						break;
				case 3: intent = new Intent(view.getContext(), HistoryActivity.class);
						break;
				default: intent = new Intent();
						break;
			}
			startActivity(intent);
		}
	};
}
