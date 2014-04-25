package com.activities.carpooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;

public class ContactViewActivity extends Activity
{
	private Contact currentContact;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_view);
		
		/* Recuperation de l'Intent et du Contact associe */
		Intent intent = getIntent();
		int id = intent.getExtras().getInt(ContactListActivity.CONTACT_EXTRA);
		currentContact = CarPooler.contactList.get(id);
		
		/* Remplissage des champs */
		fillInfos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}
	
	/**
	 * Rempli les champs du nom et du solde pour le contact courant
	 */
	private void fillInfos()
	{
		/* Champ du nom */
		TextView txt = (TextView) findViewById(R.id.name_header);
		txt.setText(currentContact.getFirstName() + " " + currentContact.getName());
		
		/* Champ de l'argent */
		fillBill();
	}
	
	/**
	 * Rempli le champ du solde et adapte la couleur en suffisance
	 */
	private void fillBill()
	{
		Double bill = currentContact.getbill();
		
		/* Champ de l'argent */
		TextView txt = (TextView) findViewById(R.id.bill_field);
		txt.setText(Double.toString(bill));
		if(bill >= 15.0)
		{
			txt.setTextColor(android.graphics.Color.RED);
		}
	}
	
	/**
	 * TODO Listener sur les boutons 
	 */
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			Toast toast = Toast.makeText(this, "Down Pressed", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			Toast toast = Toast.makeText(this, "Up Pressed", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);	
	}

}
