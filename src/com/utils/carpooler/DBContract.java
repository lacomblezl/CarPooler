package com.utils.carpooler;

import android.provider.BaseColumns;

/**
 * Contrat definissant l'architecture de la database.
 * Par convention, tous les noms de champ sont compris dans un tableau avec leur type.
 * @author Loic Lacomblez
 *
 */
public abstract class DBContract
{
	
	/**
	 * Table definissant les triplets journey_id, date, roadname
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class JourneyTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Journey";
		public static final String[] JOURNEY_ID = {"\"journey_id\"", " INTEGER PRIMARY KEY"};
		public static final String[] DATE = {"\"date\"", " TEXT NOT NULL"};
		public static final String[] ROADNAME = {"\"roadname\"", " TEXT NOT NULL REFERENCES "+RoadTable.TABLE_NAME};
	}
	
	/**
	 * Table associant a chaque journey_id la/les personnes concernees
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class JourneyContactTable implements BaseColumns
	{
		public static final String TABLE_NAME = "JourneyxContact";
		public static final String[] JOURNEY_ID = {"\"journey_id\"", " INTEGER NOT NULL REFERENCES "+JourneyTable.TABLE_NAME};
		public static final String[] CONTACT_ID = {"\"contact_id\"", " INTEGER NOT NULL REFERENCES " + ContactTable.TABLE_NAME};
	}
	
	/**
	 * Table representant les itineraires possibles
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class RoadTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Road";
		public static final String[] ROADNAME = {"\"roadname\"", " TEXT PRIMARY KEY"};
		public static final String[] LENGTH = {"\"length\"", " REAL"};
		public static final String[] DURATION = {"\"duration\"", " TEXT"};
		public static final String[] PRICE = {"\"price\"", " REAL NOT NULL"};
	}
	
	/**
	 * Table representant les personnes transportees lors du co-voiturage
	 * @author Loic Lacomblez
	 * L'id est en 'autoincrement' : cela veut dire qu'un contact nouvellement cree aura toujours
	 * un id plus grand que les autres contacts... Cela permet de le retrouver dans la database.
	 *
	 */
	public static abstract class ContactTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Contact";
		public static final String[] CONTACT_ID = {"\"contact_id\"", " INTEGER PRIMARY KEY"};
		public static final String[] NAME = {"\"name\"", " TEXT NOT NULL"};
		public static final String[] SURNAME = {"\"firstname\"", " TEXT NOT NULL"};
		public static final String[] BILL = {"\"bill\"", " REAL NOT NULL"};
		public static final String[] INFO_ID = {"\"info_id\"", " INTEGER REFERENCES " + InfoTable.TABLE_NAME};
	}
	
	/**
	 * Unused so far...
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class InfoTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Info";
		public static final String[] INFO_ID = {"\"info_id\"", " INTEGER PRIMARY KEY"};
	}

	/* Some interesting Strings...
	 * ATTENTION : Un String de creation par table, sinon ca plante !!
	 */
	public static final String CREATE_JOURNEY_TABLE = "CREATE TABLE " + JourneyTable.TABLE_NAME + " (" +
			Utilities.concatList(JourneyTable.JOURNEY_ID) + " ," +
			Utilities.concatList(JourneyTable.DATE) + " ," +
			Utilities.concatList(JourneyTable.ROADNAME) 
			+ ")";
	
	public static final String CREATE_JOURNEY_CONTACT_TABLE = "CREATE TABLE " +
			JourneyContactTable.TABLE_NAME + " (" +
			Utilities.concatList(JourneyContactTable.JOURNEY_ID) + " ," +
			Utilities.concatList(JourneyContactTable.CONTACT_ID)
			+ ")";
			
	
	public static final String CREATE_ROAD_TABLE = "CREATE TABLE " + RoadTable.TABLE_NAME + " (" +
			Utilities.concatList(RoadTable.ROADNAME) + " ," +
			Utilities.concatList(RoadTable.LENGTH) + " ," +
			Utilities.concatList(RoadTable.DURATION) + " ," +
			Utilities.concatList(RoadTable.PRICE)
			+ ")";
	
	public static final String CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactTable.TABLE_NAME + " (" +
			Utilities.concatList(ContactTable.CONTACT_ID) + ", " +
			Utilities.concatList(ContactTable.NAME) + ", " +
			Utilities.concatList(ContactTable.SURNAME) + ", " +
			Utilities.concatList(ContactTable.BILL) + ", " +
			Utilities.concatList(ContactTable.INFO_ID)
			+ ")";
	
	public static final String CREATE_INFO_TABLE = "CREATE TABLE " + InfoTable.TABLE_NAME + " (" +
			Utilities.concatList(InfoTable.INFO_ID) + ")";
}
