package com.activities.carpooler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;

public class ContactDetailsActivity extends DetailsActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		
		/* liste des champs a rendre desactivables */
		EditText[] rows = {(EditText) findViewById(R.id.name_field),
					(EditText) findViewById(R.id.first_name_field)};
		setEditRows(rows);
		
		/* Recupere l'id passe par ContactListActivity lors de l'appel
		 * Si l'id recupere est -1; alors on cree un nouvel itineraire 
		 */
		Intent intent = getIntent();
		int id = intent.getExtras().getInt(ContactListActivity.CONTACT_EXTRA);
		
		/* Si l'id vaut -1, alors il s'agit d'un nouveau trajet destine a etre cree */
		if(id==-1)
		{
			/* bouton rendu visible 
			Button createButton = (Button) findViewById(R.id.create_button);
			createButton.setVisibility(View.VISIBLE); */
			//TODO listener sur le bouton : creer un nouveau trajet
			
			/* champ rendus editables */
			setFields(true);
		}
		else
		{
			/* Remplissage des champs si necessaire, ceux-ci sont verrouilles */
			fillInfos(id);
		}
	}
	
	//TODO Update du contact en overridant onPause
	
	/**
	 * Rempli les champs avec les informations du 'i-eme' contact de CarPooler.contactList 
	 */
	private void fillInfos(int i)
	{
		Contact tmp = CarPooler.contactList.get(i);
		
		EditText field = (EditText) findViewById(R.id.name_field);
		field.setText(tmp.getName());
		
		field = (EditText) findViewById(R.id.first_name_field);
		field.setText(tmp.getFirstName());
	}

}
