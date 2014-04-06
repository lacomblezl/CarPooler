package com.utils.carpooler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.activities.carpooler.R;
import com.driver.carpooler.CarPooler;
import com.model.carpooler.Contact;

/**
 * Une collection d'utilitaires pour realiser diverses choses
 * @author Loic Lacomblez
 *
 */
public class Utilities
{
	/**
	 * Concatene les elements d'une liste, sans separation entre les elements
	 * @pre _
	 * @post Un String est retourne, il est compose des elements de 'list' ecrits
	 * dans leur ordre au sein de la liste.
	 */
	public static String concatList(String [] list)
	{
		String result = new String();
		for(String text:list)
			result+=text;
		
		return result;
	}
	
	/**
	 * Methode permettant de remplir un listView (layout list_item) avec les elements
	 * contenus dans elem
	 */
	public static void fillList(ListView list, Context context, ArrayList<String> elem)
	{
		 ArrayList<Map<String,String>> data = new ArrayList<Map<String,String>>();
		    Iterator<String> it = elem.iterator();
		    String tmp;
		    while(it.hasNext())
		    {
		    	tmp = it.next();
		    	HashMap<String, String> map = new HashMap<String,String>(1);
		    	map.put("name", tmp);
		    	data.add(map);
		    }
		    
		    String[] from = {"name"};
		    int[] to = {R.id.text_row};
		    SimpleAdapter listAdapter = new SimpleAdapter(context, data,
		    		R.layout.list_item, from, to);
		    list.setAdapter(listAdapter);
	}

}
