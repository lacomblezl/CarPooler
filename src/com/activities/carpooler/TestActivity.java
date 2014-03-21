package com.activities.carpooler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;
import com.model.carpooler.Journey;
import com.model.carpooler.Road;
import com.utils.carpooler.EmptyContactListException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity
{
	//private TextView textField;
	private EditText contactForm;
	private EditText roadForm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_screen);	
		
		/* Parametrage bouton Add Contact */
		contactForm = (EditText) findViewById(R.id.contact_form);
		Button contactButton = (Button) findViewById(R.id.contact_button);
		contactButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				//TODO a reutiliser ailleurs !
				Contact tmp = new Contact(contactForm.getText().toString(), "Prenom");
				CarPooler.appDatabase.insert(tmp);
				CarPooler.contactList.add(tmp);
				Toast toast = Toast.makeText(v.getContext(), "Insert succeeded (id:" +
						tmp.getId() + ")", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		/* Parametrage bouton Add Road */
		roadForm = (EditText) findViewById(R.id.road_form);
		Button roadButton = (Button) findViewById(R.id.road_button);
		roadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Road tmp = new Road(roadForm.getText().toString(), 69, 69, 69);
				CarPooler.appDatabase.insert(tmp);
				CarPooler.roadList.add(tmp);
				Toast toast = Toast.makeText(v.getContext(), "Insert succeeded", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		/* Parametrage bouton Add Journey */
		Button journeyButton = (Button) findViewById(R.id.journey_button);
		journeyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Toast toast;
				if(CarPooler.contactList.size() < 2) {
					toast = Toast.makeText(v.getContext(),
							"Too few contacts saved !", Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				if(CarPooler.roadList.isEmpty()) {
					toast = Toast.makeText(v.getContext(),
							"Too few roads saved !", Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				Journey tmp;
				try
				{
					tmp = new Journey(new ArrayList<Contact>(CarPooler.contactList.subList(0,2)),
						CarPooler.roadList.get(0), new Date());
				}
				catch(EmptyContactListException e)
				{
					Log.e("error", "Error while saving Journey");
					return;
				}
				CarPooler.appDatabase.insert(tmp);
			}
		});
		
		/* Parametrage du bouton
		Button button = (Button) findViewById(R.id.test_button);
	    button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	Contact newContact = new Contact(textField.getText().toString(), "Jean");
	         		CarPooler.appDatabase.insert(newContact);
	         		CarPooler.contactList.add(newContact);
	         		fillList((ListView) findViewById(R.id.my_listview));
	             }
	         }); */
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
