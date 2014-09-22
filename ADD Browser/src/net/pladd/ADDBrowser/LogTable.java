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
public class LogTable extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187630712246701080L;
	
	
	protected static List<String> header =
			Arrays.asList("Full Account", "Location Type", "Location #",
					"Create Date", "Created by", 
					"Resolved", "Resolved Date", "Resolved by", "Last Modified", "Last Modified by",  
					"Log template", "Notes", "Resolved Note");
	
	protected static final int COL_FULL_ACCT     = 0;
	protected static final int COL_LOC_TYPE      = 1;
	protected static final int COL_LOC_NUM       = 2;
	protected static final int COL_CREATE_DT     = 3;
	protected static final int COL_CREATE_USER   = 4;
	protected static final int COL_RESOLVED      = 5;
	protected static final int COL_RESOVLED_DT   = 6;
	protected static final int COL_RESOVLED_USER = 7;
	protected static final int COL_MAINT_DT      = 8;
	protected static final int COL_MAINT_USER    = 9;
	protected static final int COL_TEMPLATE      = 10;
	protected static final int COL_NOTES         = 11; // there are 4 notes columns
	protected static final int COL_RESOLVED_NOTE = 12; // there are 2 notes columns
	protected static final int COL_CREATE_TM     = 17;
	protected static final int COL_ACCT_NUM      = 18;
	protected static final int COL_LOG_HDR_ID    = 19;
	protected static final int COL_LOG_TMPL_HDR  = 20;

	protected int rowCount;
	
	private Vector<Integer>  fullAccounts;
	private Vector<String>   locTypes;
	private Vector<Integer>  locNums;
	private Vector<Date>     createDates;
	private Vector<String>   createUsers;
	private Vector<Boolean>  resolveds;
	private Vector<Calendar> resolvedDates;
	private Vector<String>   resolvedUsers;
	private Vector<Date>     maintDates;
	private Vector<String>   maintUsers;
	private Vector<String>   templates;
	protected Vector<String> notes;
	protected Vector<String> resolvedNotes;

	protected Vector<Integer> acctNums;
	protected Vector<Integer> log_hdr_ids;
	protected Vector<Integer> log_tmpl_hdr_ids;

	public LogTable()
	{
		rowCount = 0;
		fullAccounts  = new Vector<Integer>();
		locTypes      = new Vector<String>();
		locNums       = new Vector<Integer>();
		createDates   = new Vector<Date>();
		createUsers   = new Vector<String>();
		resolveds     = new Vector<Boolean>();
		resolvedDates = new Vector<Calendar>();
		resolvedUsers = new Vector<String>();
		maintDates    = new Vector<Date>();
		maintUsers    = new Vector<String>();
		templates     = new Vector<String>();
		notes         = new Vector<String>();
		resolvedNotes = new Vector<String>();
		
		acctNums         = new Vector<Integer>();
		log_hdr_ids      = new Vector<Integer>();
		log_tmpl_hdr_ids = new Vector<Integer>();
		
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
		case COL_LOC_TYPE      : return String.class;
		case COL_LOC_NUM       : return Integer.class;
		case COL_CREATE_DT     : return Date.class;
		case COL_CREATE_USER   : return String.class;
		case COL_RESOLVED      : return Boolean.class;
		case COL_RESOVLED_DT   : return String.class;
		case COL_RESOVLED_USER : return String.class;
		case COL_MAINT_DT      : return Date.class;
		case COL_MAINT_USER    : return String.class;
		case COL_TEMPLATE      : return String.class;
		case COL_NOTES         : return String.class;
		case COL_RESOLVED_NOTE : return String.class;
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
		case COL_LOC_TYPE      : return locTypes.get(row);
		case COL_LOC_NUM       : return locNums.get(row);
		case COL_CREATE_DT     : return createDates.get(row);
		case COL_CREATE_USER   : return createUsers.get(row);
		case COL_RESOLVED      : return resolveds.get(row);
		case COL_RESOVLED_USER : return resolvedUsers.get(row);
		case COL_MAINT_DT      : return maintDates.get(row);
		case COL_MAINT_USER    : return maintUsers.get(row);
		case COL_TEMPLATE      : return templates.get(row);
		case COL_NOTES         : return notes.get(row).replace('\n', '/').replace('\r', '/');
		case COL_RESOLVED_NOTE : return resolvedNotes.get(row).replace('\n', '/').replace('\r', '/');
		case COL_RESOVLED_DT  :
			Calendar d = resolvedDates.get(row);
			if (d == null)
				return "";
			else
				return ADDBrowser.tm.format(d.getTime());
		default: return null;
		}
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		rowCount = 0;

		fullAccounts .clear();
		locTypes     .clear();
		locNums      .clear();
		createDates  .clear();
		createUsers  .clear();
		resolveds    .clear();
		resolvedDates.clear();
		resolvedUsers.clear();
		maintDates   .clear();
		maintUsers   .clear();
		templates    .clear();
		notes        .clear();
		resolvedNotes.clear();
		acctNums     .clear();
		log_hdr_ids  .clear();
		log_tmpl_hdr_ids.clear();
		
		while (results.next())
		{
			rowCount++;
			
			int acctNum;
			int hdr_id;
			int hdr_tmpl;
			
			{
				Integer i = new Integer(results.getInt(COL_FULL_ACCT+1));
				fullAccounts.add(i);
			}
			{
				locTypes.add(results.getString(COL_LOC_TYPE+1));
			}
			{
				Integer i = new Integer(results.getInt(COL_LOC_NUM+1));
				locNums.add(i);
			}
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(results.getDate(COL_CREATE_DT+1));
				int tm = results.getInt(COL_CREATE_TM+1);
				cal.set(Calendar.SECOND,      0);
				cal.set(Calendar.MINUTE,      tm % 100);
				tm = tm / 100;
				cal.set(Calendar.HOUR_OF_DAY, tm % 100);
				createDates.add(cal.getTime());
			}
			{
				createUsers.add(results.getString(COL_CREATE_USER+1));
			}
			{
				Boolean b = results.getBoolean(COL_RESOLVED+1);
				if (b == null)
					resolveds.add(Boolean.FALSE);
				else
					resolveds.add(b);
			}
			{
				Date d = results.getDate(COL_RESOVLED_DT+1);
				if (d == null)
				{
					resolvedDates.add(null);
				}
				else
				{
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE,      0);
					cal.set(Calendar.SECOND,      0);
					resolvedDates.add(cal);
				}
			}
			{
				String u = results.getString(COL_RESOVLED_USER+1);
				if (u == null)
					resolvedUsers.add("");
				else
					resolvedUsers.add(u);
			}
			{
				Timestamp ts = results.getTimestamp(COL_MAINT_DT+1);
				maintDates.add(new Date(ts.getTime()));
			}
			{
				maintUsers.add(results.getString(COL_MAINT_USER+1));
			}
			{
				templates.add(results.getString(COL_TEMPLATE+1));
			}
			{
				acctNum = results.getInt(COL_ACCT_NUM+1);
				acctNums.add(new Integer(acctNum));
				
				hdr_id = results.getInt(COL_LOG_HDR_ID+1);
				log_hdr_ids.add(new Integer(hdr_id));
				
				hdr_tmpl = results.getInt(COL_LOG_TMPL_HDR+1);
				log_tmpl_hdr_ids.add(new Integer(hdr_tmpl));
			}
			{
				String note = "";
				String rc;
				
				rc = results.getString(COL_NOTES+1);
				if (rc != null) note += rc;
				rc = results.getString(COL_NOTES+2);
				if (rc != null) note += rc;
				rc = results.getString(COL_NOTES+3);
				if (rc != null) note += rc;
				rc = results.getString(COL_NOTES+4);
				if (rc != null) note += rc;
				
				String extended = ADDBrowser.fullLogNote(acctNum, hdr_id, hdr_tmpl, 0);
				
				notes.add(note + extended);
			}
			{
				String note = "";
				String rc;

				rc = results.getString(COL_RESOLVED_NOTE+3+1);
				if (rc != null) note += rc;
				rc = results.getString(COL_RESOLVED_NOTE+3+2);
				if (rc != null) note += rc;

				String extended = ADDBrowser.fullLogNote(acctNum, hdr_id, hdr_tmpl, 1);

				resolvedNotes.add(note + extended);
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

		table.getColumnModel().getColumn(COL_CREATE_DT)  .setCellRenderer(new FormatRenderer(ADDBrowser.tm));
		table.getColumnModel().getColumn(COL_RESOVLED_DT).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(COL_MAINT_DT)   .setCellRenderer(new FormatRenderer(ADDBrowser.tm));
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
			out.write(locTypes.get(row)+"\t");
			out.write(locNums.get(row)+"\t");
			out.write(ADDBrowser.tm.format(createDates.get(row))+"\t");
			out.write(createUsers.get(row)+"\t");
			out.write(resolveds.get(row)+"\t");
			Calendar c = resolvedDates.get(row);
			if (c == null)
				out.write("\t");
			else
				out.write(ADDBrowser.df.format(c.getTime())+"\t");
			out.write(resolvedUsers.get(row)+"\t");
			out.write(ADDBrowser.tm.format(maintDates.get(row))+"\t");
			out.write(maintUsers.get(row)+"\t");
			out.write(templates.get(row)+"\t");
			out.write(notes.get(row).replace('\n', '/').replace('\r', '/')+"\t");
			out.write(resolvedNotes.get(row).replace('\n', '/').replace('\r', '/')+"\t");
			out.write("\n");
		}
		out.close();
	}
}
