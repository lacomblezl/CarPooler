package com.model.carpooler;

import java.util.ArrayList;
import java.util.Date;

import com.utils.carpooler.EmptyContactListException;

/** Classe modelisant un trajet, c'est a dire un triplet ([Contact]-Road-Date).
 * Permet de dresser un historique des trajets.
 * @author loic
 *
 */
public class Journey
{
	/* variables d'instance */
	private ArrayList<Contact> contacts;
	private Road road;
	private Date date;
	
	/**
	 * Constructeur de la Classe Journey, initialise un trajet sur la route road
	 * a la date date, sans contact associe. 
	 */
	public Journey(ArrayList<Contact> contacts, Road road, Date date) throws EmptyContactListException
	{
		if(contacts.isEmpty())
		{
			throw new EmptyContactListException("");
		}
		this.contacts = contacts;
		this.road = road;
		this.date = date;
	}
	
	/**
	 * Ajoute un contact a ce trajet
	 * @pre _
	 * @post contact a ete ajoute a this.contact
	 */
	public void addContact(Contact contact)
	{
		this.contacts.add(contact);
	}
	
	/**
	 * Retire un contact a ce trajet
	 * @pre contact se trouve dans this.contacts
	 * @post contact a ete retire de this.contacts
	 */
	public void removeContact(Contact contact)
	{
		this.contacts.remove(contact);
	}
	
	/**
	 * Retourne la liste des contacts associes a ce trajet
	 * @pre _
	 * @post la liste des contacts est retournee, ces derniers sont tries selon
	 * l'ordre dans lequel ils ont ete ajoutes.
	 */
	public ArrayList<Contact> getContact()
	{
		return contacts;
	}

	/**
	 * @return the road
	 */
	public Road getRoad()
	{
		return road;
	}

	/**
	 * @param road the road to set
	 */
	public void setRoad(Road road)
	{
		this.road = road;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
	
}
