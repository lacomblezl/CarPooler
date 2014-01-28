package com.activities.carpooler;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;
import com.model.carpooler.Journey;
import com.model.carpooler.Road;

public class HomeActivity extends Activity
{
	//liste des boutons du menu 
	public int[] listText = {R.string.new_journey, R.string.contacts,
			R.string.roads, R.string.history};
	public int[] listIcon = {};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		/* Instanciation de la database */
		CarPooler.openDB(getApplicationContext());
	    
	    /* Requete SQL et Remplissage de la liste */
	    CarPooler.contactList = CarPooler.appDatabase.loadContacts();
	    
	    //TODO test !!
	    Contact tmpCont = new Contact("Paul","Joseph");
	    ArrayList<Contact> contList = new ArrayList<Contact>(2);
	    contList.add(tmpCont);
	    tmpCont = new Contact("Delvaux", "Charles");
	    contList.add(tmpCont);
	    Road tmpRoad = new Road("Grande route");
	    CarPooler.appDatabase.insert(new Journey(contList, tmpRoad, new Date()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
}
