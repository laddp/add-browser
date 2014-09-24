/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class LogCategory
{
	public final int    categoryID;
	public final int    groupID;
	public final String description;

	public LogCategory(int groupID, int categoryID, String description) {
		this.categoryID  = categoryID;
		this.groupID     = groupID;
		this.description = description;
	}

	@Override
	public String toString()
	{
		return description;
	}

}
