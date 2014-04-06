package com.activities.carpooler;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activite abstraite contenant des champs editables qui peuvent etre activtes ou desactives
 * a l'aide d'un bouton situe dans 'menu'.
 * @author Loic Lacomblez
 *
 */
public abstract class DetailsActivity extends Activity
{
	/* Marque si les champ editables sont actifs ou non */
	private boolean isOnEdit;
	
	/* Liste des champs activables */
	private EditText[] editRows;
	
	/**
	 * @param editRows the editRows to set
	 */
	public void setEditRows(EditText[] editRows)
	{
		this.editRows = editRows;
	}
	
	public EditText[] getEditRows()
	{
		return editRows;
	}

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
	
	@Override
	protected void onPause()
	{
		if(isOnEdit)
		{
			setFields(false);
		}
		super.onPause();
	}
	
	/**
	 * Passe la propriete 'focusable' des champs a la valeur 'bool'
	 */
	public void setFields(boolean bool)
	{
		isOnEdit = bool;
		for(EditText view:editRows)
		{
			if(isOnEdit)
				view.setFocusableInTouchMode(isOnEdit);
			else
				view.setFocusable(isOnEdit);
		}
	}

}
