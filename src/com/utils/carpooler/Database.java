package com.utils.carpooler;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

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
	// Constance definissant la conversion dates <-> String
	static final Locale dateStyle = Locale.FRANCE;
	static final DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, dateStyle);
	
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
		
		/* DEBUG PRINT */
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
		Cursor cursor = mainDatabase.rawQuery("SELECT " + DBContract.ContactTable.CONTACT_ID[0] + ", " +
				DBContract.ContactTable.NAME[0] + ", " + DBContract.ContactTable.SURNAME[0] + ", " + 
				DBContract.ContactTable.BILL[0] + " FROM " + DBContract.ContactTable.TABLE_NAME, null);
		
		ArrayList<Contact> result = new ArrayList<Contact>();
		
		/* Parcour du resultat */
		if(cursor.moveToFirst())
		{
				while(!cursor.isAfterLast())
				{
					/* Instancie le nouveau contact : id-nom-prenom */
					Contact tmp = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
					tmp.addToBill(cursor.getDouble(3));	
					result.add((int) (tmp.getId() - 1), tmp); //TODO mod -1 //TODO verifier qu'il n'y a pas d'id 0 et que tout se passe bien !
					cursor.moveToNext();
				}
		}
		cursor.close();
		return result;
	}
	
	/**
	 * Construit la liste des itineraires contenus dans la database
	 * @pre _
	 * @post La liste des itineraires encodes dans la base de donnee est retournee
	 */
	public ArrayList<Road> loadRoads()
	{
		/* Requete SQL */
		Cursor cursor = mainDatabase.rawQuery("SELECT " + DBContract.RoadTable.ROADNAME[0] + ", " +
				DBContract.RoadTable.LENGTH[0] + ", " + DBContract.RoadTable.DURATION[0] + ", " +
				DBContract.RoadTable.PRICE[0] + " FROM " + DBContract.RoadTable.TABLE_NAME, null);
		
		ArrayList<Road> result = new ArrayList<Road>();
		
		/* Parcour du resultat */
		if(cursor.moveToFirst())
		{
				while(!cursor.isAfterLast())
				{
					/* Instancie la nouvelle route */
					Road tmp = new Road(cursor.getString(0), cursor.getDouble(1), cursor.getDouble(2),
							cursor.getDouble(3));	
					result.add(tmp);
					cursor.moveToNext();
				}
		}
		cursor.close();
		return result;
	}
	
	//TODO LoadHistory
	/**
	 * Cree un ArrayList 
	 * @pre loadContact a deja ete appele pour remplir contactList
	 * @return
	 */
	public ArrayList<Journey> loadHistory(ArrayList<Contact> contactList, ArrayList<Road> roadList)
	{
		/* Requete SQL */
		Cursor cursor = mainDatabase.rawQuery("SELECT " + DBContract.JourneyTable.JOURNEY_ID[0] + ", " +
				DBContract.JourneyTable.DATE[0] + ", " + DBContract.JourneyTable.ROADNAME[0] +
				" FROM " + DBContract.JourneyTable.TABLE_NAME, null);
		
		ArrayList<Journey> result = new ArrayList<Journey>();
		
		/* Contruction du resultat */
		if(cursor.moveToFirst())
		{
			while(!cursor.isAfterLast())
			{
				/* Requete SQL secondaire pour construire les sous-listes de contacts */
				long id = cursor.getLong(0);
				Cursor contacts = mainDatabase.rawQuery("SELECT " +
						DBContract.JourneyContactTable.CONTACT_ID[0] + " FROM " + DBContract.JourneyContactTable.TABLE_NAME +
						" WHERE " + DBContract.JourneyContactTable.JOURNEY_ID[0] + "=" + Long.toString(id), null);
				
				/* Construction de la liste de contact sur base de l'ArrayList contactList */
				ArrayList<Contact> subList = new ArrayList<Contact>();
				if(contacts.moveToFirst())
				{
					while(!contacts.isAfterLast())
					{
						//TODO mod -1
						Log.d("debug", "contact id found in database : " + contacts.getLong(0));
						subList.add(contactList.get((int) (contacts.getLong(0) - 1)));
						contacts.moveToNext();
					}
				}
				contacts.close();
				
				/* Ajout du nouveau Journey a result */
				Journey newJourney;
				Road newRoad = findByName(cursor.getString(2), roadList);
				Date newDate;
				try
				{
					newDate = df.parse(cursor.getString(1));
				}
				catch (ParseException e)
				{
					Log.e("error", "Unvalid date format found in Journey " + cursor.getLong(0));
					return result;
				}
				if(newRoad==null)
				{
					Log.e("error", "Unvalid roadName found in Journey " + cursor.getLong(0));
					return result;
				}
				try
				{
					newJourney = new Journey(subList, newRoad, newDate);
				}
				catch(EmptyContactListException e)
				{
					Log.e("error", "No contacts linked to this Journey (id:" + cursor.getLong(0) + ")");
					return result;
				}
				result.add(newJourney);
				cursor.moveToNext();
			}
		}
		cursor.close();
		//TODO debug print
		Log.d("info","History loaded successfully - " + result.size() + " entries loaded");
		return result;
	}
	
	/**
	 * Methode secondaire - retourne l'objet Road dont le nom est 'toFind' contenu
	 * dans l'ArrayList 'list'
	 */
	Road findByName(String toFind, ArrayList<Road> list)
	{
		Road tmp;
		Iterator<Road> it = list.iterator();
		while(it.hasNext())
		{
			tmp = it.next();
			if(tmp.getName().equals(toFind))
			{
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Insere un objet dans la database a l'endroit adequat.
	 * @pre _
	 * @post l'objet item a ete insere dans la database s'il est compatible, sinon rien
	 * 		n'est fait et une erreur est ecrite dans Log.e
	 * 		 Si l'objet est de type Contact, ce dernier est mis a jour avec son id unique
	 * 		 une fois qu'il a ete insere dans la database. 
	 */
	public void insert(Object item)
	{
		if(item instanceof Journey)
		{
			Journey tmp = (Journey) item;
			
			/* Insertion dans la table Journey */
			ContentValues toAdd = new ContentValues();
			toAdd.put(DBContract.JourneyTable.DATE[0], df.format(tmp.getDate()));
			toAdd.put(DBContract.JourneyTable.ROADNAME[0], tmp.getRoad().getName());

			long id = mainDatabase.insert(DBContract.JourneyTable.TABLE_NAME, null, toAdd);
			if(id==-1)
			{
				Log.e("error", "Error while inserting new Journey in database");
			}
			
			/* Insertion dans la table JourenyxContacts */
			toAdd.clear();
			long retval;
			for(Contact contact:tmp.getContact())
			{
				toAdd.put(DBContract.JourneyContactTable.JOURNEY_ID[0], id);
				toAdd.put(DBContract.JourneyContactTable.CONTACT_ID[0], contact.getId());
				//TODO Debug print
				Log.i("info", "Inserted Journey id : " + id);
				Log.i("info", "Inserted Contact id : " + contact.getId());
				retval = mainDatabase.insert(DBContract.JourneyContactTable.TABLE_NAME, null, toAdd);
				if(retval==-1)
				{
					Log.e("error", "Error while inserting the Journey" + id + "in database");
				}
				toAdd.clear();
			}
			return;
		}
		
		if(item instanceof Contact)
		{
			Contact tmp = (Contact) item;
			
			/* Insertion dans la table Contact */
			ContentValues toAdd = new ContentValues();
			toAdd.put(DBContract.ContactTable.NAME[0], tmp.getName());
			toAdd.put(DBContract.ContactTable.SURNAME[0], tmp.getFirstName());
			toAdd.put(DBContract.ContactTable.BILL[0], tmp.getbill());
			
			long id = mainDatabase.insert(DBContract.ContactTable.TABLE_NAME, null, toAdd);
			if(id==-1)
			{
				Log.e("error", "Error while inserting new Contact in database");
			}
			
			/* Mise a jour du contact */
			tmp.setId(id);
			
			return;
		}
		
		String command = insertCommand(item);
		if(command == null)
		{
			Log.e("error", "Trying to insert non database compliant object !");
			return;
		}
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
				"\"" + tmp.getName() + "\"" + " ," + tmp.getLength() + " ," +
				tmp.getDuration() + " ," + tmp.getPrice() + ")";
 		}
		else if(item instanceof Contact)
		{
			Contact tmp = (Contact) item;
			return "INSERT INTO " + DBContract.ContactTable.TABLE_NAME + " (" +
					DBContract.ContactTable.NAME[0] + " ," +
					DBContract.ContactTable.SURNAME[0] + " ," +
					DBContract.ContactTable.BILL[0] + ") VALUES (" +
					"\"" + tmp.getName() + "\"" + " ," + "\"" + tmp.getFirstName() + "\"" +
					" ," + tmp.getbill() + ")";
		}
		else
			return null;
	}
	
	/**
	 * Met a jour l'entree correspondant a 'object' dans la database.
	 * @pre _
	 * @post la database est mise a jour si necessaire.
	 * @return 'true' si la database a ete modifiee, false sinon.
	 */
	public boolean update(Object object)
	{
		if(object instanceof Road)
		{
			Road tmp = (Road) object;
			
			String cmd = "UPDATE " + DBContract.RoadTable.TABLE_NAME + " SET " +
					DBContract.RoadTable.LENGTH + "=" + tmp.getLength() + ", " +
					DBContract.RoadTable.DURATION + "=" + tmp.getDuration() + ", " +
					DBContract.RoadTable.PRICE + "=" + tmp.getPrice() + " WHERE " +
					DBContract.RoadTable.ROADNAME + "=" + tmp.getName();
			mainDatabase.execSQL(cmd);
			
			return true;
		}
		
		if(object instanceof Contact)
		{
			Contact tmp = (Contact) object;
			
			String cmd = "UPDATE " + DBContract.ContactTable.TABLE_NAME + " SET " +
					DBContract.ContactTable.NAME + "=" + tmp.getName() + ", " +
					DBContract.ContactTable.SURNAME + "=" + tmp.getFirstName() + ", " +
					DBContract.ContactTable.BILL + "=" + tmp.getbill() + " WHERE " +
					DBContract.ContactTable.CONTACT_ID + "=" + tmp.getId();
			mainDatabase.execSQL(cmd);
			
			return true;
		}
		else
		{
			Log.e("Database", "Trying to update database with a non-recognized item");
			return false;
		}
	}
	
	//TODO Les REMOVE ! Et ca va pas etre comique ca !
	/**
	 * Retire de la database l'objet 'object'.
	 * @pre _
	 * @post retire de la database l'objet 'object' s'il s'y trouvait
	 * @return 'true' si 'object' a ete supprime de la database, 'false' en cas d'erreur
	 */
	public boolean delete(Object object)
	{
		if(object instanceof Road)
		{
			Road tmp = (Road) object;
			
			String cmd = "DELETE FROM " + DBContract.RoadTable.TABLE_NAME + " WHERE " +
					DBContract.RoadTable.ROADNAME + "=" + tmp.getName();
			mainDatabase.execSQL(cmd);
			return true;
		}
		
		if(object instanceof Contact)
		{
			Contact tmp = (Contact) object;
			
			String cmd = "DELETE FROM " + DBContract.ContactTable.TABLE_NAME + " WHERE " +
					DBContract.ContactTable.CONTACT_ID + "=" + tmp.getId();
			mainDatabase.execSQL(cmd);
			return true;
		}
		else
		{
			Log.e("Database", "Trying to remove a non-recognized item");
		}
	}
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
