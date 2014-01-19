package com.utils.carpooler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Une classe permettant de gerer la creation et l'instanciation de la database.
 * Suit les recommandations de l'API Android
 * @author Loic Lacomblez
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "History.db";
    
    public DBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBContract.CREATE_DB);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		
	}

}
