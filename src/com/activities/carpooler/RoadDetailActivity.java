package com.activities.carpooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Road;

public class RoadDetailActivity extends DetailsActivity
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_road_detail);
		
		EditText[] rows = {(EditText) findViewById(R.id.name_field),
				(EditText) findViewById(R.id.length_field),
				(EditText) findViewById(R.id.duration_field),
				(EditText) findViewById(R.id.price_field)};
		
		/* Specifie les champs a verrouiller ou non */
		setEditRows(rows);
		
		/* Recupere l'id passe par RoadListActivity lors de l'appel
		 * Si l'id recupere est -1; alors on cree un nouvel itineraire 
		 */
		Intent intent = getIntent();
		int id = intent.getExtras().getInt(RoadListActivity.ROAD_ID);
		
		/* Si l'id vaut -1, alors il s'agit d'un nouveau trajet destine a etre cree */
		if(id==-1)
		{
			/* bouton rendu visible */
			Button createButton = (Button) findViewById(R.id.create_button);
			createButton.setVisibility(View.VISIBLE);
			//TODO listener sur le bouton : creer un nouveau trajet
			
			/* champ rendus editables */
			setFields(true);
		}
		else
		{
			/* Remplissage des champs si necessaire, ceux-ci sont verrouilles */
			fillDetails(id);
		}
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.road_detail, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case R.id.menu_edit:
			setFields(true);
			Toast toast = Toast.makeText(this, "menu_edit pressed (" + isOnEdit +")", Toast.LENGTH_SHORT);
			toast.show();
			break;
		}
		
		return false;
	}
	*/
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		//TODO mettre a jour le journey accrodingly (arrayList + database) !
	}
	
	/**
	 * Rempli les details du trajet selectionne, sur base de l'id passe lors de l'appel
	 */
	private void fillDetails(int id)
	{
		Road current = CarPooler.roadList.get(id);
		EditText currentField = (EditText) findViewById(R.id.name_field);
		currentField.setText(current.getName());
		
		double tmp;
		
		currentField = (EditText) findViewById(R.id.length_field);
		tmp = current.getLength();
		if(tmp == -1.0)
			currentField.setText(R.string.unknown);
		else
			currentField.setText(Double.toString(tmp));
		
		currentField = (EditText) findViewById(R.id.duration_field);
		tmp = current.getDuration();
		if(tmp == -1.0)
			currentField.setText(R.string.unknown);
		else
		currentField.setText(Double.toString(tmp));
		
		currentField = (EditText) findViewById(R.id.price_field);
		tmp = current.getPrice();
		if(tmp == -1.0)
			currentField.setText(R.string.unknown);
		else
		currentField.setText(Double.toString(tmp));
	}
	/*
	
	 * Passe la propriete 'focusable' des champs a 'true'
	 
	private void setFields(boolean bool)
	{
		isOnEdit = bool;
		
		EditText fields[] = {(EditText) findViewById(R.id.name_field),
				(EditText) findViewById(R.id.length_field),
				(EditText) findViewById(R.id.duration_field),
				(EditText) findViewById(R.id.price_field)};
		
		for(EditText view:fields)
		{
			if(isOnEdit)
				view.setFocusableInTouchMode(isOnEdit);
			else
				view.setFocusable(isOnEdit);
		}
	}
	*/

}
