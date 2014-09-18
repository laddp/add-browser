/**
 * 
 */
package net.pladd.ADDBrowser;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.camick.FormatRenderer;
import com.camick.NumberRenderer;

/**
 * @author Patrick
 *
 */
public class BatchTable extends AbstractTableModel {
	private static final long serialVersionUID = -4119385912426229448L;

	protected static List<String> header =
			Arrays.asList("Event Date", "Posting Date", "Last Modified", "Batch #", 
					"Full Account", "Name", "Type", 
					"Posting Code", "Posting Code Descr", "Ref Num",
					"Net Amount", "Credit Amount", "Debit Amount", "Units", "Price Per Gal", "User ID");
	protected static final int COL_EVENT_DATE = 0;
	protected static final int COL_POST_DATE  = 1;
	protected static final int COL_TRANS_DATE = 2;
	protected static final int COL_BATCH_NUM  = 3;
	protected static final int COL_FULL_ACCT  = 4;
	protected static final int COL_NAME       = 5;
	protected static final int COL_TYPE       = 6;
	protected static final int COL_POST_CODE  = 7;
	protected static final int COL_POST_DESC  = 8;
	protected static final int COL_REF_NUM    = 9;
	protected static final int COL_AMT_NET    = 10;
	protected static final int COL_AMT_CRED   = 11;
	protected static final int COL_AMT_DEBIT  = 12;
	protected static final int COL_UNITS      = 13;
	protected static final int COL_PPG        = 14;
	protected static final int COL_USERID     = 15;

	protected int rowCount;
	protected BigDecimal totalAmt;
	protected BigDecimal credAmt;
	protected BigDecimal debAmt;
	
	private Vector<Calendar>   eventDates;
	private Vector<Calendar>   postingDates;
	private Vector<Date>       transDates;
	private Vector<Integer>    batchNums;
	private Vector<Integer>    fullAccounts;
	private Vector<String>     names;
	private Vector<String>     types;
	private Vector<Integer>    postCodes;
	private Vector<String>     postDescrs;
	private Vector<String>     refNums;
	private Vector<BigDecimal> netAmounts;
	private Vector<BigDecimal> units;
	private Vector<BigDecimal> ppgs;
	private Vector<String>     userIDs;

	public BatchTable()
	{
		rowCount = 0;
		eventDates   = new Vector<Calendar>();
		postingDates = new Vector<Calendar>();
		transDates   = new Vector<Date>();
		batchNums    = new Vector<Integer>();
		fullAccounts = new Vector<Integer>();
		names        = new Vector<String>();
		types        = new Vector<String>();
		postCodes    = new Vector<Integer>();
		postDescrs   = new Vector<String>();
		refNums      = new Vector<String>();
		netAmounts   = new Vector<BigDecimal>();
		units        = new Vector<BigDecimal>();
		ppgs         = new Vector<BigDecimal>();
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
		case COL_EVENT_DATE: return Date.class;
		case COL_POST_DATE:  return Date.class;
		case COL_TRANS_DATE: return Date.class;
		case COL_BATCH_NUM:  return Integer.class;
		case COL_FULL_ACCT:  return Integer.class;
		case COL_NAME:       return String.class;
		case COL_TYPE:       return String.class;
		case COL_POST_CODE:  return Integer.class;
		case COL_POST_DESC:  return String.class;
		case COL_REF_NUM:    return String.class;
		case COL_AMT_NET:    return BigDecimal.class;
		case COL_AMT_CRED:   return BigDecimal.class;
		case COL_AMT_DEBIT:  return BigDecimal.class;
		case COL_UNITS:      return BigDecimal.class;
		case COL_PPG:        return BigDecimal.class;
		case COL_USERID:     return String.class;
		default: return null;
		}
	}
	
	@Override
	public int getRowCount()
	{
		return rowCount;
	}

	private BigDecimal amtToNet(BigDecimal amt, int pc)
	{
		if (pc > PostingCode.maxDebit)
			return amt.negate();
		else
			return amt;
	}
	
	private BigDecimal amtToCredit(BigDecimal amt, int pc)
	{
		BigDecimal net = amtToNet(amt, pc);
		if (net.compareTo(BigDecimal.ZERO) < 0)
			return net.negate();
		else
			return BigDecimal.ZERO;
	}
	
	private BigDecimal amtToDebit(BigDecimal amt, int pc)
	{
		BigDecimal net = amtToNet(amt, pc);
		if (net.compareTo(BigDecimal.ZERO) > 0)
			return net;
		else
			return BigDecimal.ZERO;
	}
	
	@Override
	public Object getValueAt(int row, int col)
	{
		switch (col)
		{
		case COL_EVENT_DATE: return eventDates.get(row).getTime();
		case COL_POST_DATE:  return postingDates.get(row).getTime();
		case COL_TRANS_DATE: return transDates.get(row);
		case COL_BATCH_NUM:  return batchNums.get(row);
		case COL_FULL_ACCT:  return fullAccounts.get(row);
		case COL_NAME:       return names.get(row);
		case COL_TYPE:       return types.get(row);
		case COL_POST_CODE:  return postCodes.get(row);
		case COL_POST_DESC:  return postDescrs.get(row);
		case COL_REF_NUM:    return refNums.get(row);
		case COL_AMT_NET:    return amtToNet   (netAmounts.get(row), postCodes.get(row));
		case COL_AMT_CRED:   return amtToCredit(netAmounts.get(row), postCodes.get(row));
		case COL_AMT_DEBIT:  return amtToDebit (netAmounts.get(row), postCodes.get(row));
		case COL_UNITS:      return units.get(row);
		case COL_PPG:        return ppgs.get(row);
		case COL_USERID:     return userIDs.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		rowCount = 0;
		totalAmt = new BigDecimal(0);
		credAmt  = new BigDecimal(0);
		debAmt   = new BigDecimal(0);

		eventDates.clear();
		postingDates.clear();
		transDates.clear();
		batchNums.clear();
		fullAccounts.clear();
		names.clear();
		types.clear();
		postCodes.clear();
		postDescrs.clear();
		refNums.clear();
		netAmounts.clear();
		units.clear();
		ppgs.clear();
		userIDs.clear();
		
		while (results.next())
		{
			rowCount++;
			Integer pc;
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(COL_EVENT_DATE+1));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE,      0);
				cal.set(Calendar.SECOND,      0);
				eventDates.add(cal);
			}
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(COL_POST_DATE+1));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE,      0);
				cal.set(Calendar.SECOND,      0);
				postingDates.add(cal);
			}
			{
				Timestamp ts = results.getTimestamp(COL_TRANS_DATE+1);
				transDates.add(new Date(ts.getTime()));
			}
			{
				Integer i = new Integer(results.getInt(COL_BATCH_NUM+1));
				batchNums.add(i);
			}
			{
				Integer i = new Integer(results.getInt(COL_FULL_ACCT+1));
				fullAccounts.add(i);
			}
			{
				names.add(results.getString(COL_NAME+1));
			}
			{
				types.add(results.getString(COL_TYPE+1));
			}
			{
				pc = new Integer(results.getInt(COL_POST_CODE+1));
				postCodes.add(pc);
			}
			{
				postDescrs.add(results.getString(COL_POST_DESC+1));
			}
			{
				refNums.add(results.getString(COL_REF_NUM+1));
			}
			{
				BigDecimal amt = results.getBigDecimal(COL_AMT_NET+1);
				netAmounts.add(amt);
				totalAmt = totalAmt.add(amtToNet   (amt, pc));
				credAmt  = credAmt .add(amtToCredit(amt, pc));
				debAmt   = debAmt  .add(amtToDebit (amt, pc));
			}
			// NOTE DIFFERENT CONSTANTS FROM HERE ON!!! columns don't match up because of calculated cols for amounts			
			{
				units.add(results.getBigDecimal(COL_AMT_NET+2));
			}
			{
				ppgs.add(results.getBigDecimal(COL_AMT_NET+3).movePointLeft(2));
			}
			{
				userIDs.add(results.getString(COL_AMT_NET+4));
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

		table.getColumnModel().getColumn(BatchTable.COL_EVENT_DATE).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(BatchTable.COL_POST_DATE ).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(BatchTable.COL_TRANS_DATE).setCellRenderer(new FormatRenderer(ADDBrowser.tm));
		table.getColumnModel().getColumn(BatchTable.COL_AMT_NET   ).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		table.getColumnModel().getColumn(BatchTable.COL_AMT_CRED  ).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		table.getColumnModel().getColumn(BatchTable.COL_AMT_DEBIT ).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		
		NumberFormat nf1 = NumberFormat.getNumberInstance();
		nf1.setMaximumFractionDigits(1);
		nf1.setMinimumFractionDigits(1);
		table.getColumnModel().getColumn(BatchTable.COL_UNITS     ).setCellRenderer(new NumberRenderer(nf1));

		NumberFormat nf5 = NumberFormat.getCurrencyInstance();
		nf5.setMinimumFractionDigits(5);
		table.getColumnModel().getColumn(BatchTable.COL_PPG       ).setCellRenderer(new NumberRenderer(nf5));
	}
	
	protected void doExport(FileWriter out) throws IOException
	{
		for(int col = 0; col < this.getColumnCount(); col++)
		{
			out.write(this.getColumnName(col) + "\t");
		}
		out.write("\n");

		for(int row = 0; row < this.getRowCount(); row++)
		{
			out.write(ADDBrowser.tm.format(eventDates.get(row).getTime())+"\t");
			out.write(ADDBrowser.tm.format(postingDates.get(row).getTime())+"\t");
			out.write(ADDBrowser.tm.format(transDates.get(row))+"\t");
			out.write(batchNums.get(row)+"\t");
			out.write(fullAccounts.get(row)+"\t");
			out.write(names.get(row)+"\t");
			out.write(types.get(row)+"\t");
			out.write(postCodes.get(row)+"\t");
			out.write(postDescrs.get(row)+"\t");
			out.write(refNums.get(row)+"\t");
			out.write("$"+amtToNet   (netAmounts.get(row), postCodes.get(row))+"\t");
			out.write("$"+amtToCredit(netAmounts.get(row), postCodes.get(row))+"\t");
			out.write("$"+amtToDebit (netAmounts.get(row), postCodes.get(row))+"\t");
			out.write(units.get(row)+"\t");
			out.write("$"+ppgs.get(row)+"\t");
			out.write(userIDs.get(row)+"\t");
			out.write("\n");
		}
		out.close();
	}
}
