package com.utils.carpooler;

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

}
