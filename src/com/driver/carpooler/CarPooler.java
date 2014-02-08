package com.driver.carpooler;

import java.util.ArrayList;

import android.content.Context;

import com.model.carpooler.Contact;
import com.model.carpooler.Journey;
import com.model.carpooler.Road;
import com.utils.carpooler.Database;

/**
 * Classe modelisant une instance de l'application.
 * @author loic
 *
 */
public class CarPooler
{	
	public static final int historySize = 50; // definit le nbre d'entrees retenues
	public static Database appDatabase;
	public static ArrayList<Contact> contactList = null;
	public static ArrayList<Road> roadList = null;
	public static ArrayList<Journey> history=null;
	
	/**
	 * Gere l'instanciation de la database en modifiant la variable appDatabase.
	 * @param context
	 */
	public static void openDB(Context context)
	{
		appDatabase = new Database(context);
	}
	
	
}
