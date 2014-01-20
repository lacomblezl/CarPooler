package com.activities.carpooler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.driver.carpooler.CarPooler;

public class HomeActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_list);
		
		/* Instanciation de la database */
		CarPooler.openDB(getApplicationContext());
		
		/* Parametrage du champ textuel */
		//TODO
		
		/* Parametrage du bouton */
		Button button = (Button) findViewById(R.id.test_button);
	    button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 Toast msg = Toast.makeText(getApplicationContext(), "Button pressed",
	                		 Toast.LENGTH_SHORT);
	                 msg.show();
	             }
	         });
	    
	    /* Requete SQL et Remplissage de la liste */
	    CarPooler.contactList = CarPooler.appDatabase.loadContacts();
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/**
	 * Permet de mettre a jour le contenu d'une liste selon la database  
	 * @param list
	 */
	private void fillList(ListView list)
	{
		
	}

}
