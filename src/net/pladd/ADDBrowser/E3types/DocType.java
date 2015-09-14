/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class DocType
{
	public final int    typeID;
	public final String description;

	/**
	 * @param typeID
	 * @param description
	 */
	public DocType(int typeID, String description) {
		this.typeID = typeID;
		this.description = description;
	}
	
	public String toString()
	{
		return description;
	}
}
