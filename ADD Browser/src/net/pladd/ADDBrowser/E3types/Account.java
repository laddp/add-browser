/**
 * 
 */
package net.pladd.ADDBrowser.E3types;

import java.math.BigDecimal;

import net.pladd.ADDBrowser.ADDBrowser;

/**
 * @author Patrick
 *
 */
public class Account
{
	public final String     full_account;
	public final String     sort_code;
	public final String     name;
	public final String     title;
	public final String     first_name;
	public final String     middle_initial;
	public final String     last_name;
	public final String     name_suffix;
	public final String     street1;
	public final String     street2;
	public final String     city;
	public final String     state;
	public final String     postal_code;
	public final Division   division;
	public final Type       type;
	public final Category   category;
	public final BigDecimal balance;
	
	/**
	 * @param full_account
	 * @param sort_code
	 * @param name
	 * @param title
	 * @param first_name
	 * @param middle_initial
	 * @param last_name
	 * @param name_suffix
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param postal_code
	 */
	/**
	 * @param full_account
	 * @param sort_code
	 * @param name
	 * @param title
	 * @param first_name
	 * @param middle_initial
	 * @param last_name
	 * @param name_suffix
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param postal_code
	 * @param division
	 * @param type
	 * @param category
	 */
	public Account(String full_account, String sort_code, String name,
			String title, String first_name, String middle_initial,
			String last_name, String name_suffix, String street1,
			String street2, String city, String state, String postal_code,
			int division, int type, int category, BigDecimal balance) {
		this.full_account = full_account;
		this.sort_code = sort_code;
		this.name = name;
		this.title = title;
		this.first_name = first_name;
		this.middle_initial = middle_initial;
		this.last_name = last_name;
		this.name_suffix = name_suffix;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.state = state;
		this.postal_code = postal_code;
		this.division = ADDBrowser.divisions.get(division);
		this.type = ADDBrowser.types.get("" + division + "-" + type);
		this.category = ADDBrowser.categories.get(category);
		this.balance = balance;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [full_account=" + full_account + ", name=" + name + "]";
	}	
}
