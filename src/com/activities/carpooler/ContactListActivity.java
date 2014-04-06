package com.activities.carpooler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;

public class ContactListActivity extends Activity
{
	public static String CONTACT_EXTRA = "contact_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		/* Remplissage de la liste et application du listener dessus */
		ListView mainList = (ListView) findViewById(R.id.contact_list);
		fillContactList(mainList);
		mainList.setOnItemClickListener(listListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case R.id.contact_add:
			Intent i = new Intent(this, ContactDetailsActivity.class);
			i.putExtra(CONTACT_EXTRA, -1);
			startActivity(i);
		}
		return false;
	}
	
	/**
	 * Permet de remplir la listView 'list' avec les elements de CarPooler.contactList
	 * @pre list != null
	 * @post 'list' est rempli avec les elements de CarPooler.contactList
	 */
	private void fillContactList(ListView list)
	{
		/* Les donnees pour remplir la liste */
		ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
		
		/* Remplissage de data */
		Iterator<Contact> it = CarPooler.contactList.iterator();
		Contact tmp;
		while(it.hasNext())
		{
			tmp = it.next();
			HashMap<String, String> map = new HashMap<String,String>();
			map.put("name", tmp.getFirstName() + " " + tmp.getName());
			map.put("bill", Double.toString(tmp.getbill()) + " €");
			data.add(map);
		}
		
		/* Label des elements a mapper vers la liste */
		String[] label = {"name", "bill"};
		
		/* Id des elements de la liste a remplir */
		int[] pos = {R.id.main_text, R.id.sub_text};
		
		/* Application de l'adapter a la liste */
		SimpleAdapter listAdapter = new SimpleAdapter(this, data,
				R.layout.contact_detail_list, label, pos);
		list.setAdapter(listAdapter);
	}
	
	/*
	 * Listener dont le comportement est de lancer une activite 'contact details',
	 * avec en intent.extras l'id de l'element selectionne 
	 */
	private OnItemClickListener listListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent i = new Intent(view.getContext(), ContactDetailsActivity.class);

			i.putExtra(CONTACT_EXTRA, position);
			startActivity(i);
		}
	};
}
