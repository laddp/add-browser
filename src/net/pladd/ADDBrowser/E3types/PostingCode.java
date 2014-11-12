/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

/**
 * @author Patrick
 *
 */
public class PostingCode {

	public final int    postingCode;
	public final String description;
	
	public static int	 max;
	public static int	 maxDebit;
	public static String invalLabel;
	
	public PostingCode(int code, String desc)
	{
		postingCode = code;
		description = desc;
	}
	
	public String toString()
	{
		return "" + postingCode + ": " + description;
	}
}
