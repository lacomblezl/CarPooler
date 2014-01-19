package com.driver.carpooler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.utils.carpooler.DBOpenHelper;

/**
 * Classe modelisant une instance de l'application.
 * @author loic
 *
 */
public class CarPooler
{	
	public static final int historySize = 50; // definit le nbre d'entrees retenues
	public static SQLiteDatabase database;
	
	/**
	 * Gere l'instanciation de la database en modifiant la variable database.
	 * @param context
	 */
	public static void openDB(Context context)
	{
		/* Instanciation de l'openHelper */
		DBOpenHelper helper = new DBOpenHelper(context);
		
		/* Load de la database */
		//TODO passer en asynchrone pour les perfs
		database = helper.getWritableDatabase();
	}
	
	
}
