package com.model.carpooler;

/** Classe modelisant une route de covoiturage */
public class Road
{
	private String name;
	private double length;
	private double duration;
	
	/** Constructeur de la classe Road
	 * @pre _
	 * @post Un nouvel objet Road a ete cree, avec this.name==name et length=duration=-1
	 */
	public Road(String name)
	{
		this.name = name;
		length = -1.0;
		duration = -1.0;
	}
	
	/** Constructeur alternatif pour la classe Road
	 * @pre _
	 * @post Un nouvel objet Road a ete cree, avec this.name==name, this.length==length, et
	 * this.duration==duration
	 */
	public Road(String name, double length, double duration)
	{
		this.name = name;
		this.length = length;
		this.duration = duration;
	}
	
	/** Verifie si this a une longueur connue 
	 * @pre _
	 * @post true est retourne si this a une longueur specifiee, false sinon
	 */
	public boolean hasLength()
	{
		return (length != -1.0);
	}
	
	/** Verifie si this a une duree connue 
	 * @pre _
	 * @post true est retourne si this a une duree specifiee, false sinon
	 */
	public boolean hasDuration()
	{
		return (duration != -1.0);
	}
	
	/* Getters et Setters */

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the length
	 */
	public double getLength()
	{
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(double length)
	{
		this.length = length;
	}

	/**
	 * @return the duration
	 */
	public double getDuration()
	{
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(double duration)
	{
		this.duration = duration;
	}
	
	

}