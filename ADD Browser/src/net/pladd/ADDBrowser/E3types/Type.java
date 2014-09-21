/**
 * 
 */
package net.pladd.ADDBrowser.E3types;


/**
 * @author Patrick
 *
 */
public class Type {
	public final int    type;
	public final int    division;
	public final String description;
	
	public static boolean typesUniform;

	/**
	 * @param type
	 * @param description
	 */
	public Type(int division, int type, String description)
	{
		this.type = type;
		this.division = division;
		this.description = description;
	}
	
	@Override
	public String toString() 
	{
		if (typesUniform)
			return "" + type + ": " + description;
		else
			return "" + division + "-" + type + ": " + description;
	}
}
