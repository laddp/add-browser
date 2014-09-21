/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class Category {
	public final int    category;
	public final String description;

	/**
	 * @param category
	 * @param description
	 */
	public Category(int category, String description)
	{
		this.category = category;
		this.description = description;
	}
	
	@Override
	public String toString() 
	{
		return "" + category + ": " + description;
	}
}
