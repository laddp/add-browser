/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class Division {
	public final int    division;
	public final String description;

	/**
	 * @param division
	 * @param description
	 */
	public Division(int Division, String description)
	{
		this.division = Division;
		this.description = description;
	}
	
	@Override
	public String toString() 
	{
		return "" + division + ": " + description;
	}
}
