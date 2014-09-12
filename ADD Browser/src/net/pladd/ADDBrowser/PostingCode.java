/**
 * 
 */
package net.pladd.ADDBrowser;

/**
 * @author Patrick
 *
 */
public class PostingCode {

	public final int    postingCode;
	public final String description;
	
	protected static int	max;
	protected static int	maxDebit;
	protected static String invalLabel;
	
	public PostingCode(int code, String desc)
	{
		postingCode = code;
		description = desc;
	}
	
	public String toString()
	{
		return "" + postingCode + " - " + description;
	}
}