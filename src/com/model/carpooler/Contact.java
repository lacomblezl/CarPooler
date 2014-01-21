package com.model.carpooler;


/** Cette classe modelise un contact, pour laquelle on maintient des informations et un solde
 * @author Loïc Lacomblez
 */
public class Contact
{
	private int id; // permet d'identifier l'entree de la database liee
	private String name;
	private String firstName;
	private Information info;
	private double bill;
	
	/** Contructeur de la classe Contact
	 * @pre _
	 * @post Un nouvel objet Contact a ete cree, avec comme nom name et comme prenom firstName.
	 * Ses infos sont vides et sa facture est initialisee a zero.
	 */
	public Contact(String name, String firstName)
	{
		id = -1; // valeur sentinelle
		this.name = name;
		this.firstName = firstName;
		info = new Information();
		bill = 0.0;
	}
	
	/** Contructeur alternatif de la classe Contact, permet d'inclure l'id.
	 * @pre _
	 * @post Un nouvel objet Contact a ete cree, avec comme nom name et comme prenom firstName.
	 * Ses infos sont vides et sa facture est initialisee a zero.
	 */
	public Contact(int id, String name, String firstName)
	{
		this.name = name;
		this.firstName = firstName;
		info = new Information();
		bill = 0.0;
	}
	
	/** Incremente this.bill de amount
	 * @pre _
	 * @post this.bill a ete incremente de amount
	 */
	public void addToBill(Double amount)
	{
		bill += amount;
	}
	
	/** Diminue this.bill de amount
	 * @pre _
	 * @post this.bill a ete decremente de amount. Il est possible que this.bill < 0
	 */
	public void pay(Double amount)
	{
		bill -= amount;
	}
	
	/* Getters et Setters */
	/** Getter pour this.Name */
	public String getName()
	{
		return name;
	}
	
	/** Setter pour this.Name */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/** Getter pour this.firstName */
	public String getFirstName()
	{
		return firstName;
	}

	/** Setter pour this.firstName */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/** Getter pour this.info */
	public Information getInfo()
	{
		return info;
	}

	/** Getter pour this.sold */
	public Double getbill()
	{
		return bill;
	}
	
	/** Getter pour this.id */
	public int getId()
	{
		return id;
	}
	
	/** Setter pour this.id */
	public void setId(int value)
	{
		id = value;
	}
}
