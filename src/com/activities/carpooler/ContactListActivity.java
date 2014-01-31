package com.activities.carpooler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ContactListActivity extends Activity
{
	// Liste de strings "nom prenom" des Contacts
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_list);
		
		/* Mise a jour du titre */
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(getText(R.string.contacts));
		
		//TODO remplissage de la liste et listener dessus !
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

}
