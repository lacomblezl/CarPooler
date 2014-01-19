package com.utils.carpooler;

import android.provider.BaseColumns;
import android.util.Log;

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
		public static final String[] JOURNEY_ID = {JourneyTable._ID, " INTEGER PRIMARY KEY"};
		public static final String[] DATE = {"date", " TEXT NOT NULL"};
		public static final String[] ROADNAME = {"roadname", " TEXT NOT NULL REFERENCES "+RoadTable.TABLE_NAME};
	}
	
	/**
	 * Table associant a chaque journey_id la/les personnes concernees
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class JourneyContactTable implements BaseColumns
	{
		public static final String TABLE_NAME = "JourneyxContact";
		public static final String[] JOURNEY_ID = {JourneyTable._ID, " INTEGER NOT NULL REFERENCES "+JourneyTable.TABLE_NAME};
		public static final String[] CONTACT_ID = {"contactid", " INTEGER NOT NULL REFERENCES " + ContactTable.TABLE_NAME};
	}
	
	/**
	 * Table representant les itineraires possibles
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class RoadTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Road";
		public static final String[] ROADNAME = {"roadname", " TEXT PRIMARY KEY"};
		public static final String[] LENGTH = {"length", " REAL"};
		public static final String[] DURATION = {"duration", " TEXT"};
		public static final String[] PRICE = {"price", " REAL NOT NULL"};
	}
	
	public static abstract class ContactTable implements BaseColumns
	{
		public static final String TABLE_NAME = "Contact";
		public static final String[] CONTACT_ID = {ContactTable._ID, " INTEGER PRIMARY KEY"};
		public static final String[] NAME = {"name", " TEXT NOT NULL"};
		public static final String[] SURNAME = {"surname", " TEXT NOT NULL"};
		public static final String[] BILL = {"bill", " REAL NOT NULL"};
		public static final String[] INFO_ID = {"info_id", " INTEGER REFERENCES " + ContactInfoTable.TABLE_NAME};
	}
	
	/**
	 * Unused so far...
	 * @author Loic Lacomblez
	 *
	 */
	public static abstract class ContactInfoTable implements BaseColumns
	{
		public static final String TABLE_NAME = "ContactxInfo";
	}

	/* Some interesting Strings... */
	public static final String CREATE_DB = "CREATE TABLE " + ContactTable.TABLE_NAME + " (" +
			ContactTable.CONTACT_ID[0] + ContactTable.CONTACT_ID[1] + ", " + ContactTable.NAME[0] +
			ContactTable.NAME[1] + ", " + ContactTable.SURNAME[0] + ContactTable.SURNAME[1] + ", " +
			ContactTable.BILL[0] + ContactTable.BILL[1] + ", " + ContactTable.INFO_ID[0] +
			ContactTable.INFO_ID[1] + ")";
}
