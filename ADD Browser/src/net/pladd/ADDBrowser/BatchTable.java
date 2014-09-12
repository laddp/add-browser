/**
 * 
 */
package net.pladd.ADDBrowser;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * @author Patrick
 *
 */
public class BatchTable extends AbstractTableModel {
	private static final long serialVersionUID = -4119385912426229448L;

	protected static List<String> header =
			Arrays.asList("Event Date", "Posting Date", "Last Modified", "Batch #", 
					"Full Account", "Name", "Type", 
					"Posting Code", "Posting Code Descr",
					"Net Amount", "Credit Amount", "Debit Amount", "User ID");
	int rowCount;
	
	private Vector<Calendar>   eventDates;
	private Vector<Calendar>   postingDates;
	private Vector<Date>       transDates;
	private Vector<Integer>    batchNums;
	private Vector<Integer>    fullAccounts;
	private Vector<String>     names;
	private Vector<String>     types;
	private Vector<Integer>    postCodes;
	private Vector<String>     postDescrs;
	private Vector<BigDecimal> netAmounts;
	private Vector<String>     userIDs;

	public BatchTable()
	{
		eventDates   = new Vector<Calendar>();
		postingDates = new Vector<Calendar>();
		transDates   = new Vector<Date>();
		batchNums    = new Vector<Integer>();
		fullAccounts = new Vector<Integer>();
		names        = new Vector<String>();
		types        = new Vector<String>();
		postCodes    = new Vector<Integer>();
		postDescrs   = new Vector<String>();
		netAmounts   = new Vector<BigDecimal>();
		userIDs      = new Vector<String>();
	}
	
	@Override
	public int getColumnCount()
	{
		return header.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return header.get(col);		
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		switch (col)
		{
		case 0: return Date.class;
		case 1: return Date.class;
		case 2: return Date.class;
		case 3: return Integer.class;
		case 4: return Integer.class;
		case 5: return String.class;
		case 6: return String.class;
		case 7: return Integer.class;
		case 8: return String.class;
		case 9: return BigDecimal.class;
		case 10: return BigDecimal.class;
		case 11: return BigDecimal.class;
		case 12: return String.class;
		default: return null;
		}
	}
	
	@Override
	public int getRowCount()
	{
		return eventDates.size();
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		BigDecimal amt = netAmounts.get(row);
		int postCode = postCodes.get(row).intValue();
		if (postCode >= 200)
			amt = amt.negate();

		switch (col)
		{
		case 0: return eventDates.get(row).getTime();
		case 1: return postingDates.get(row).getTime();
		case 2: return transDates.get(row);
		case 3: return batchNums.get(row);
		case 4: return fullAccounts.get(row);
		case 5: return names.get(row);
		case 6: return types.get(row);
		case 7: return postCodes.get(row);
		case 8: return postDescrs.get(row);
		case 9: return amt;
		case 10:
			if (amt.compareTo(BigDecimal.ZERO) < 0)
				return amt.negate();
			else
				return BigDecimal.ZERO;
		case 11:
			if (amt.compareTo(BigDecimal.ZERO) > 0)
				return amt;
			else
				return BigDecimal.ZERO;
		case 12: return userIDs.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results) throws SQLException 
	{
		eventDates.clear();
		postingDates.clear();
		batchNums.clear();
		fullAccounts.clear();
		names.clear();
		types.clear();
		postCodes.clear();
		postDescrs.clear();
		netAmounts.clear();
		userIDs.clear();
		transDates.clear();
		
		rowCount = 0;
		while (results.next())
		{
			rowCount++;
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(1));
				eventDates.add(cal);
			}
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(2));
				postingDates.add(cal);
			}
			{
				Integer i = new Integer(results.getInt(3));
				batchNums.add(i);
			}
			{
				Integer i = new Integer(results.getInt(4));
				fullAccounts.add(i);
			}
			{
				names.add(results.getString(5));
			}
			{
				types.add(results.getString(6));
			}
			{
				Integer i = new Integer(results.getInt(7));
				postCodes.add(i);
			}
			{
				postDescrs.add(results.getString(8));
			}
			{
				netAmounts.add(results.getBigDecimal(9));
			}
			{
				userIDs.add(results.getString(10));
			}
			{
				Timestamp ts = results.getTimestamp(11);
				transDates.add(new Date(ts.getTime()));
			}
		}
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");
	}
}
