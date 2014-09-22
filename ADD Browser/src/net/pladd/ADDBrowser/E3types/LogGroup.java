/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class LogGroup
{
	public final int    groupID;
	public final String description;

	/**
	 * @param groupID
	 * @param description
	 */
	public LogGroup(int groupID, String description) {
		this.groupID = groupID;
		this.description = description;
	}
}
