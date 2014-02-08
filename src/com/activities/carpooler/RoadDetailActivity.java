package com.activities.carpooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.driver.carpooler.CarPooler;
import com.model.carpooler.Road;

public class RoadDetailActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_road_detail);
		
		/* Recupere l'id passe par RoadListActivity lors de l'appel
		 * Si l'id recupere est -1; alors on cree un nouvel itineraire 
		 */
		Intent intent = getIntent();
		int id = intent.getExtras().getInt(RoadListActivity.ROAD_ID);
		
		if(id==-1)
		{
			/* bouton rendu visible */
			Button createButton = (Button) findViewById(R.id.create_button);
			createButton.setVisibility(View.VISIBLE);
			//TODO listener sur le bouton : creer un nouveau trajet
			
			/* champ rendus editables */
			activateFields();
		}
		else
		{
			/* Remplissage des champs si necessaire, ceux-ci sont verrouilles */
			fillDetails(id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.road_detail, menu);
		return true;
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
	
	/**
	 * Passe la propriete 'focusable' des champs a 'true'
	 */
	private void activateFields()
	{
		View fields[] = {findViewById(R.id.name_field),
					findViewById(R.id.length_field),
					findViewById(R.id.duration_field),
					findViewById(R.id.price_field)};
	
		for(View view:fields)
			view.setFocusable(true);
	}

}
