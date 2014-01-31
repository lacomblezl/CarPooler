package com.graphics.carpooler;


/** Objet permettant d'encapsuler une entree d'une liste possedant une icone
 * et un champ textuel
 * @author Loic Lacomblez
 *
 */
public class LogoList
{
	private int icon;
	private String text;

	/**
	 * Constructeur de la classe
	 * @param icon
	 * @param text
	 */
	public LogoList(int icon, String text)
	{
		this.icon = icon;
		this.text = text;
	}

	/* Getters - les setters ne sont pas autorises */
	public int getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}
}