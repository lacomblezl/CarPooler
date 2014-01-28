package com.utils.carpooler;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.model.carpooler.Contact;
import com.model.carpooler.Journey;
import com.model.carpooler.Road;

/**
 * Classe fournissant les outils d'interfacage avec la database (insert, delete, update)
 * @author Loic Lacomblez
 *
 */
public class Database
{
	private SQLiteDatabase mainDatabase;	// base de donnee utilisee
	
	/**
	 * Constructeur de la classe Database.
	 * @param context
	 */ 
	public Database(Context context)
	{
		/* Instanciation de l'openHelper */
		DBOpenHelper helper = new DBOpenHelper(context);
		
		/* Load de la database */
		//TODO passer en asynchrone pour les perfs
		mainDatabase = helper.getWritableDatabase();
		
		//TODO DEBUG PRINT
		Log.i("info", "Database Loading Suceeded");
	}
	
	/**
	 * Construit la liste des contacts contenus dans la database
	 * @pre _
	 * @post La liste des contacts encodes dans la base de donnee est retournee
	 */
	public ArrayList<Contact> loadContacts()
	{
		//TODO gestion des infos
		
		/* Requete SQL */
		Cursor cursor = mainDatabase.rawQuery("SELECT " + DBContract.ContactTable.NAME[0] + ", " +
				DBContract.ContactTable.SURNAME[0] + ", " + DBContract.ContactTable.BILL[0] + " FROM "
				+ DBContract.ContactTable.TABLE_NAME, null);
		
		ArrayList<Contact> result = new ArrayList<Contact>();
		
		/* Parcour du resultat */
		if(cursor.moveToFirst())
		{
				while(!cursor.isAfterLast())
				{
					/* Instancie le nouveau contact */
					Contact tmp = new Contact(cursor.getString(0), cursor.getString(1));
					tmp.addToBill(cursor.getDouble(2));	
					result.add(tmp);
					cursor.moveToNext();
				}
		}
		cursor.close();
		return result;
	}
	
	/**
	 * Insere un objet dans la database a l'endroit adequat.
	 */
	public void insert(Object item)
	{
		// TODO ajouter les contacts du trajet dans la table JourneyxContact !
		if(item instanceof Journey)
		{
			Journey tmp = (Journey) item;
			
			/* Insertion dans la table Journey */
			ContentValues toAdd = new ContentValues();
			toAdd.put(DBContract.JourneyTable.DATE[0], tmp.getDate().toString());
			toAdd.put(DBContract.JourneyTable.ROADNAME[0], tmp.getRoad().getName());

			long id = mainDatabase.insert(DBContract.JourneyTable.TABLE_NAME, null, toAdd);
			if(id==-1)
				Log.e("error", "Error while inserting the Journey" + id + "in database");
			
			/* Insertion dans la table JourenyxContacts */
			toAdd.clear();
			long retval;
			for(Contact contact:tmp.getContact())
			{
				toAdd.put(DBContract.JourneyContactTable.JOURNEY_ID[0], id);
				toAdd.put(DBContract.JourneyContactTable.CONTACT_ID[0], contact.getId());
				retval = mainDatabase.insert(DBContract.JourneyContactTable.TABLE_NAME, null, toAdd);
				if(retval==-1)
					Log.e("error", "Error while inserting the Journey" + id + "in database");
				toAdd.clear();
			}
			
			return;
		}
		String command = insertCommand(item);
		if(command == null)
			return;
		else
			mainDatabase.execSQL(command);
	}
	
	/**
	 * Genere la commande d'insertion pour l'objet item
	 */
	private String insertCommand(Object item)
	{
		if(item instanceof Road)
		{
			Road tmp = (Road) item;
			return "INSERT INTO " + DBContract.RoadTable.TABLE_NAME + " (" +
				DBContract.RoadTable.ROADNAME[0] + " ," +
				DBContract.RoadTable.LENGTH[0] + " ," +
				DBContract.RoadTable.DURATION[0] + " ," +
				DBContract.RoadTable.PRICE[0] + ") VALUES (" +
				"\"" + tmp.getName() + "\"" + " ," + "\"" + tmp.getLength() + "\"" + " ," +
				"\"" + tmp.getDuration() + "\"" + " ," + "\"" + tmp.getPrice() + "\")";
 		}
		else if(item instanceof Contact)
		{
			Contact tmp = (Contact) item;
			return "INSERT INTO " + DBContract.ContactTable.TABLE_NAME + " (" +
					DBContract.ContactTable.NAME[0] + " ," +
					DBContract.ContactTable.SURNAME[0] + " ," +
					DBContract.ContactTable.BILL[0] + ") VALUES (" +
					"\"" + tmp.getName() + "\"" + " ," + "\"" + tmp.getFirstName() + "\"" +
					" ," + "\"" + tmp.getbill() + "\"" + ")";
		}
		else
			return null;
	}
		
//	/**
//	 * Cree un ArrayList de type <E> avec l'objet cursor specifie
//	 * @pre les Strings presents dans le cursor respectent l'ordre de definition du
//	 * 		constructeur de l'objet.
//	 */
//	private <E> ArrayList<E> generateList(Cursor cursor, Class<E> test)
//	{
//		ArrayList<E> result = new ArrayList<E>();
//		
//		if(cursor.moveToFirst())
//		{
//			while(!cursor.isAfterLast())
//			{
//				Object tmp = tes
//				result.add(E);
//			}
//		}
//		cursor.close();
//		return result;
//	}
	
}
	
//	/**
//	 * Cette méthode crée et renvoit la liste des utilisateurs.
//	 * @return listUsers
//	 */
//	public ArrayList<User> createUsers() 
//	{
//		Cursor cursorUsers = applicationDatabase.rawQuery("SELECT \"cliMail\", \"cliNom\", \"cliPrenom\", \"cliTel\" FROM Client", null);
//		ArrayList<User> listUsers = new ArrayList<User>();
//		if(cursorUsers.moveToFirst())
//		{
//			while(!cursorUsers.isAfterLast())
//			{
//				User user = new User(cursorUsers.getString(0), cursorUsers.getString(1), cursorUsers.getString(2), cursorUsers.getString(3));
//				Cursor cursorPref = applicationDatabase.rawQuery("SELECT \"resStyCui\", \"resTarMoy\", \"nomAller\" FROM Preference WHERE \"cliMail\" = \""+user.getMail()+"\"", null);
//				if(cursorPref.moveToFirst())
//				{
//					user.setPreference(new Preference(StringUtility.parseToArrayList(cursorPref.getString(0)), 
//							                          cursorPref.getDouble(1), 
//							                          StringUtility.parseToArrayList(cursorPref.getString(2)))); 
//				}
//				cursorPref.close();
//				listUsers.add(user);
//				cursorUsers.moveToNext();
//			}
//		}
//		cursorUsers.close();
//		return listUsers;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des villes.
//	 * @return listCities
//	 */
//	public ArrayList<City> createCities() 
//	{
//		Cursor cursorCities = applicationDatabase.rawQuery("SELECT \"villeCode\", \"lat\", \"long\" FROM Ville", null);
//		ArrayList<City> listCities = new ArrayList<City>();
//		if(cursorCities.moveToFirst())
//		{
//			while(!cursorCities.isAfterLast())
//			{
//				String restaurantName = cursorCities.getString(0);
//				Cursor cursorRestaurants = applicationDatabase.rawQuery("SELECT \"resNom\" FROM Restaurant WHERE \"villeCode\" = \""+restaurantName+"\"", null);
//				listCities.add(new City(restaurantName, cursorRestaurants.getCount(), cursorCities.getDouble(1), cursorCities.getDouble(2)));
//				cursorCities.moveToNext();
//				cursorRestaurants.close();
//			}
//		}
//		cursorCities.close();
//		return listCities;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des restaurants de la ville passée en paramétre.
//	 * @param city
//	 * @return listRestaurants
//	 */
//	public ArrayList<Restaurant> createRestaurants(String city)
//	{
//		Cursor cursorRestaurants = applicationDatabase.rawQuery("SELECT \"resNom\", \"resTarMoy\", \"horOuv\", \"horFerm\", \"resSite\", \"resNote\", \"resPlaceTot\", \"resTel\" FROM Restaurant WHERE \"villeCode\" = \""+city+"\"", null);
//		ArrayList<Restaurant> listRestaurants = new ArrayList<Restaurant>();
//		if(cursorRestaurants.moveToFirst())
//		{
//			while(!cursorRestaurants.isAfterLast())
//			{
//				String name = cursorRestaurants.getString(0);
//				double longitude = -1;
//				double latitude = -1;
//				String adress = "";
//				String description = "";
//				Cursor cursorAdresse = applicationDatabase.rawQuery("SELECT \"adresse\", \"long\", \"lat\" FROM Adresse WHERE \"resNom\" = \""+name+"\"", null);
//				if(cursorAdresse.moveToFirst())
//				{
//					adress = cursorAdresse.getString(0);
//					longitude = cursorAdresse.getDouble(1);
//					latitude = cursorAdresse.getDouble(2);
//				}
//				cursorAdresse.close();
//				Cursor cursorDescription = applicationDatabase.rawQuery("SELECT \"desCont\" FROM Description WHERE \"nomObj\" = \""+name+"\"", null);
//				if(cursorDescription.moveToFirst())
//				{
//					description = cursorDescription.getString(0);
//				}
//				cursorDescription.close();
//				Restaurant restaurant = new Restaurant(name,
//													   cursorRestaurants.getInt(1),
//													   cursorRestaurants.getString(2),
//													   cursorRestaurants.getString(3),
//							                           cursorRestaurants.getString(4),
//							                           cursorRestaurants.getFloat(5),
//							                           cursorRestaurants.getInt(6),
//							                           cursorRestaurants.getString(7),
//							                           adress,
//							                           longitude,
//							                           latitude,
//							                           description);
//				Cursor cursorStyles = applicationDatabase.rawQuery("SELECT \"resStyCui\" FROM StyleCuisine WHERE \"resNom\" = \""+name+"\"", null);
//				if(cursorStyles.moveToFirst())
//				{
//					while(!cursorStyles.isAfterLast())
//					{
//						restaurant.addStyle(cursorStyles.getString(0));
//						cursorStyles.moveToNext();
//					}
//				}	
//				cursorStyles.close();
//				Cursor cursorPictures = applicationDatabase.rawQuery("SELECT \"photoUrl\" FROM Photo WHERE \"nomObj\" = \""+name+"\"", null);
//				if(cursorPictures.moveToFirst())
//				{
//					while(!cursorPictures.isAfterLast())
//					{
//						restaurant.addPicture(cursorPictures.getString(0));
//						cursorPictures.moveToNext();
//					}
//				}
//				cursorPictures.close();
//				listRestaurants.add(restaurant);
//				cursorRestaurants.moveToNext();
//			}
//		}
//		cursorRestaurants.close();
//		return listRestaurants;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des styles de cuisine.
//	 * @return listSty
//	 */
//	public ArrayList<String> createStyles()
//	{
//		Cursor cursorStyle = applicationDatabase.rawQuery("SELECT DISTINCT \"resStyCui\" FROM StyleCuisine", null);
//		ArrayList<String> listSty = new ArrayList<String>();
//		if(cursorStyle.moveToFirst())
//		{
//			while(!cursorStyle.isAfterLast())
//			{
//				listSty.add(cursorStyle.getString(0));
//				cursorStyle.moveToNext();
//			}
//		}
//		cursorStyle.close();
//		return listSty;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des ingrédients.
//	 * @return listIngredients
//	 */
//	public ArrayList<String> createIngredients()
//	{
//		Cursor cursorIngredients = applicationDatabase.rawQuery("SELECT DISTINCT \"ingreNom\" FROM Ingredient", null);
//		ArrayList<String> listIngredients = new ArrayList<String>();
//		if(cursorIngredients.moveToFirst())
//		{
//			while(!cursorIngredients.isAfterLast())
//			{
//				listIngredients.add(cursorIngredients.getString(0));
//				cursorIngredients.moveToNext();
//			}
//		}
//		cursorIngredients.close();
//		return listIngredients;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des plats du restaurant passée en paramètre.
//	 * @param restaurant
//	 * @return listDishes
//	 */
//	public ArrayList<Dish> createDishes(String restaurant)
//	{
//		Cursor cursorDish = applicationDatabase.rawQuery("SELECT \"platNom\", \"platCat\", \"platPrix\" FROM Plat WHERE \"resNom\" = \""+restaurant+"\"", null);
//		ArrayList<Dish> listDishes = new ArrayList<Dish>();
//		if(cursorDish.moveToFirst())
//		{
//			while(!cursorDish.isAfterLast())
//			{
//				String name = "";
//				Cursor cursorTrad = applicationDatabase.rawQuery("SELECT \"nomFr\" FROM Traduction WHERE \"nomCode\" = \""+cursorDish.getString(0)+"\"", null);
//				if(cursorTrad.moveToFirst())
//				{
//					name = cursorTrad.getString(0);
//				}
//				cursorTrad.close();
//				String description = "";
//				Cursor cursorDescription = applicationDatabase.rawQuery("SELECT \"desCont\" FROM Description WHERE \"nomObj\" = \""+name+"\"", null);
//				if(cursorDescription.moveToFirst())
//				{
//					description = cursorDescription.getString(0);
//				}
//				cursorDescription.close();
//				Dish dish = new Dish(name, cursorDish.getString(1), cursorDish.getDouble(2), description);
//				Cursor cursorPictures = applicationDatabase.rawQuery("SELECT \"photoUrl\" FROM Photo WHERE \"nomObj\" = \""+name+"\"", null);
//				if(cursorPictures.moveToFirst())
//				{
//					while(!cursorPictures.isAfterLast())
//					{
//						dish.addPicture(cursorPictures.getString(0));
//						cursorPictures.moveToNext();
//					}
//				}
//				cursorPictures.close();
//				listDishes.add(dish);
//				cursorDish.moveToNext();
//			}
//		}
//		cursorDish.close();
//		return listDishes;
//	}
//	
//	/**
//	 * Cette méthode crée et renvoit la liste des réservations de l'utilisateur dont le mail est passé en paramétre.
//	 * @param userMail
//	 * @return listReservations
//	 */
//	public ArrayList<Reservation> createReservations(String userMail)
//	{
//		Cursor cursorRes = applicationDatabase.rawQuery("SELECT \"reseObj\", \"resNom\", \"nbrePers\", \"heure\", \"date\", \"cliMail\" FROM Reservation WHERE \"cliMail\" = \""+Guide.currentUser.getMail()+"\"", null);
//		ArrayList<Reservation> listReservations = new ArrayList<Reservation>();
//		if(cursorRes.moveToFirst())
//		{
//			while(!cursorRes.isAfterLast())
//			{
//				Date date = new Date();
//				Date hour = new Date();
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.FRENCH);
//                SimpleDateFormat hf = new SimpleDateFormat("HH:mm",Locale.FRENCH);
//                try {
//                	date = df.parse(cursorRes.getString(4));
//                	hour = hf.parse(cursorRes.getString(3));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Reservation res = new Reservation(Guide.currentUser, cursorRes.getString(0), cursorRes.getString(1), cursorRes.getInt(2), hour, date);
//                Cursor cursorPlats = applicationDatabase.rawQuery("SELECT \"platNom\", \"nbrePlats\" FROM platXreservation WHERE \"reseObj\" = \""+res.getObject()+"\"", null);
//                if(cursorPlats.moveToFirst())
//                {
//            		ArrayList<Dish> listDishes = createDishes(res.getRestaurantName());
//                	while(!cursorPlats.isAfterLast())
//                	{
//                		for(int i = 0; i < listDishes.size(); i++)
//                		{
//                			Dish dish = listDishes.get(i);
//                			if(dish.getName().equals(cursorPlats.getString(0))) 
//                				res.reserveDish(dish, cursorPlats.getInt(1));
//                		}
//                		cursorPlats.moveToNext();
//                	}
//                }
//                cursorPlats.close();
//				listReservations.add(res); 
//				cursorRes.moveToNext();
//			}
//			cursorRes.close();
//		}
//		return listReservations;
//	}
//
//	/**
//	 * Cette méthode insert un objet Insertable dans la base de données.
//	 * @param item
//	 */
//	public void insert(Insertable item)
//	{
//		ArrayList<String> columns = item.getColumnsName();
//		ArrayList<String> values = item.getValues();
//		if((columns != null & values != null) && columns.size() == values.size() & columns.size() > 0)
//			applicationDatabase.execSQL("INSERT INTO "+item.getTable()+" ("+StringUtility.parseToArray(columns)+") VALUES ("+StringUtility.parseToArray(values)+")");
//	}
//	
//	/**
//	 * Cette méthode update un objet Updatable dans la base de données.
//	 * @param item
//	 */
//	public void update(Updatable item)
//	{
//		ArrayList<String> columns = item.getColumnsName();
//		ArrayList<String> values = item.getValues();
//		if((columns != null & values != null) && columns.size() == values.size() & columns.size() > 0)
//		{
//			String sql = "UPDATE "+item.getTable()+" SET ";
//			for(int i = 0; i < columns.size(); i++)
//			{
//				sql = sql+columns.get(i)+" = "+values.get(i);
//				if(i < columns.size() - 1)
//					sql = sql + ", ";
//			}
//			applicationDatabase.execSQL(sql+" "+item.getWhereClause());
//		}				
//	}
//}
