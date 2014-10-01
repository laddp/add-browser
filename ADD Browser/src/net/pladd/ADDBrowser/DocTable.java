/**
 * 
 */
package net.pladd.ADDBrowser;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.camick.FormatRenderer;

/**
 * @author Patrick
 *
 */
public class DocTable extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187630712246701080L;
	
	
	protected static List<String> header =
			Arrays.asList("Full Account", "Tank #", "Svc #", "Name", "Ref #", 
					"Doc Type", "Date", "Last Maint", "Last Modified by");
	
	private final Object[] longValues = { new Integer("9999999"), new Integer("999"), new Integer("999"),
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM", new Integer("999999"),
			"MMMMMMMMMMMMMMMMMMMM", new Date(), new Date(), "MMMMMMMMMMMMMMM" };
	
	protected static final int COL_FULL_ACCT  = 0;
	protected static final int COL_TANK_NUM   = 1;
	protected static final int COL_SVC_NUM    = 2;
	protected static final int COL_NAME       = 3;
	protected static final int COL_REF_NUM    = 4;
	protected static final int COL_DOC_TYPE   = 5;
	protected static final int COL_DATE       = 6; // NOTE date is two columns in SQL - date & time
	protected static final int COL_MAINT_DT   = 7;
	protected static final int COL_MAINT_USER = 8;
	protected static final int COL_DOC_ID     = 9;

	protected int rowCount;
	
	protected Vector<Integer>  fullAccounts;
	protected Vector<Integer>  tankNums;
	private Vector<Integer>  svcNums;
	private Vector<String>   names;
	private Vector<Integer>  refNums;
	protected Vector<String>   docTypes;
	protected Vector<Date>     dates;
	private Vector<Date>     maintDates;
	private Vector<String>   maintUsers;

	protected Vector<Integer>  docIDs;

	public DocTable()
	{
		rowCount = 0;
		fullAccounts = new Vector<Integer>();
		tankNums     = new Vector<Integer>();
		svcNums      = new Vector<Integer>();
		names        = new Vector<String>();
		refNums      = new Vector<Integer>();
		docTypes     = new Vector<String>();
		dates        = new Vector<Date>();
		maintDates   = new Vector<Date>();
		maintUsers   = new Vector<String>();
		
		docIDs      = new Vector<Integer>();
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
		case COL_FULL_ACCT     : return Integer.class;
		case COL_TANK_NUM      : return Integer.class;
		case COL_SVC_NUM       : return Integer.class;
		case COL_NAME          : return String.class;
		case COL_REF_NUM       : return Integer.class;
		case COL_DOC_TYPE      : return String.class;
		case COL_DATE          : return Date.class;
		case COL_MAINT_DT      : return Date.class;
		case COL_MAINT_USER    : return String.class;
		default: return null;
		}
	}
	
	@Override
	public int getRowCount()
	{
		return rowCount;
	}

	
	@Override
	public Object getValueAt(int row, int col)
	{
		switch (col)
		{
		case COL_FULL_ACCT     : return fullAccounts.get(row);
		case COL_TANK_NUM      : return tankNums.get(row);
		case COL_SVC_NUM       : return svcNums.get(row);
		case COL_NAME          : return names.get(row);
		case COL_REF_NUM       : return refNums.get(row);
		case COL_DOC_TYPE      : return docTypes.get(row);
		case COL_DATE          : return dates.get(row);
		case COL_MAINT_DT      : return maintDates.get(row);
		case COL_MAINT_USER    : return maintUsers.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		rowCount = 0;

		fullAccounts .clear();
		tankNums     .clear();
		svcNums      .clear();
		names        .clear();
		refNums      .clear();
		docTypes     .clear();
		dates        .clear();
		maintDates   .clear();
		maintUsers   .clear();
		docIDs     .clear();
		
		while (results.next())
		{
			rowCount++;
			
			{
				Integer i = new Integer(results.getInt(COL_FULL_ACCT+1));
				fullAccounts.add(i);
			}
			{
				Integer i = new Integer(results.getInt(COL_TANK_NUM+1));
				tankNums.add(i);
			}
			{
				Integer i = new Integer(results.getInt(COL_SVC_NUM+1));
				svcNums.add(i);
			}
			{
				names.add(results.getString(COL_NAME+1));
			}
			{
				Integer i = new Integer(results.getInt(COL_REF_NUM+1));
				refNums.add(i);
			}
			{
				docTypes.add(results.getString(COL_DOC_TYPE+1));
			}
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(COL_DATE+1));
				int tm = results.getInt(COL_DATE+2);
				cal.set(Calendar.SECOND,      0);
				cal.set(Calendar.MINUTE,      tm % 100);
				tm = tm / 100;
				cal.set(Calendar.HOUR_OF_DAY, tm % 100);
				dates.add(cal.getTime());
			}
			{
				Timestamp ts = results.getTimestamp(COL_MAINT_DT+2);
				maintDates.add(new Date(ts.getTime()));
			}
			{
				maintUsers.add(results.getString(COL_MAINT_USER+2));
			}
			{
				Integer docID = results.getInt(COL_DOC_ID+2);
				docIDs.add(new Integer(docID));
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

		table.getColumnModel().getColumn(COL_DATE)    .setCellRenderer(new FormatRenderer(ADDBrowser.tm));
		table.getColumnModel().getColumn(COL_MAINT_DT).setCellRenderer(new FormatRenderer(ADDBrowser.tm));
		
		Util.initColumnSizes(table, longValues);
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
			out.write(fullAccounts.get(row)+"\t");
			out.write(tankNums.get(row)+"\t");
			out.write(svcNums.get(row)+"\t");
			out.write(names.get(row)+"\t");
			out.write(refNums.get(row)+"\t");
			out.write(docTypes.get(row)+"\t");
			out.write(ADDBrowser.tm.format(dates.get(row))+"\t");
			out.write(ADDBrowser.tm.format(maintDates.get(row))+"\t");
			out.write(maintUsers.get(row)+"\t");
			out.write("\n");
		}
		out.close();
	}
}
