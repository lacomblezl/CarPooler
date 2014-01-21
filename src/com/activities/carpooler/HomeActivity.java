package com.activities.carpooler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;

public class HomeActivity extends Activity
{
	private TextView textField;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_list);
	
		textField = (TextView) findViewById(R.id.edit_text);
		
		/* Instanciation de la database */
		CarPooler.openDB(getApplicationContext());
		
		/* Parametrage du bouton */
		Button button = (Button) findViewById(R.id.test_button);
	    button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	Contact newContact = new Contact(textField.getText().toString(), "Jean");
	         		CarPooler.appDatabase.insert(newContact);
	         		CarPooler.contactList.add(newContact);
	         		fillList((ListView) findViewById(R.id.my_listview));
	             }
	         });
	    
	    /* Requete SQL et Remplissage de la liste */
	    CarPooler.contactList = CarPooler.appDatabase.loadContacts();
	    fillList((ListView) findViewById(R.id.my_listview));  
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
		 ArrayList<Map<String,String>> data = new ArrayList<Map<String,String>>();
		    Iterator<Contact> it = CarPooler.contactList.iterator();
		    Contact tmp;
		    while(it.hasNext())
		    {
		    	tmp = it.next();
		    	HashMap<String, String> map = new HashMap<String,String>(3);
		    	map.put("name", tmp.getName());
		    	map.put("firstname", tmp.getFirstName());
		    	data.add(map);
		    }
		    
		    String[] from = {"name"};
		    int[] to = {R.id.text_row};
		    SimpleAdapter listAdapter = new SimpleAdapter(getApplicationContext(), data,
		    		R.layout.list_item, from, to);
		    list.setAdapter(listAdapter);
	}

}
