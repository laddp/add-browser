/**
 * 
 */
package net.pladd.ADDBrowser;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
public class ServiceTable extends AbstractTableModel {
	private static final long serialVersionUID = -4119385912426229448L;

	protected static List<String> header =
			Arrays.asList("Service #", "Contract", "Renewal Date", "Status", "Last Service", "Last Cleaning",
					"Service Addr", "Service Instr",
					"Last User", "Last Maint");
	
	private final Object[] longValues = { 
			new Integer("999"), "MMM", new Date(), "MMMMMMMMM",
			new Date(), new Date(),
			"MMMMMMMMMMMMMMMMM", "MMMMMMMMMMMMMMMMM",
			"MMMMMMMMMM", new Date() };
	
	protected static final int COL_SVC_NUM    = 0;
	protected static final int COL_CONTRACT   = 1;
	protected static final int COL_CONTR_DT   = 2;
	protected static final int COL_STATUS     = 3;
	protected static final int COL_LAST_SVC   = 4;
	protected static final int COL_LAST_CLEAN = 5;
	protected static final int COL_ADDRESS    = 6;
	protected static final int COL_INSTRUCT   = 7;
	protected static final int COL_LAST_USER  = 8;
	protected static final int COL_LAST_MAINT = 9;
	protected static final int COL_SVC_SEQ    = 10;

	protected int rowCount;
	
	private Vector<Integer>     svcNums;
	private Vector<String>      contracts;
	private Vector<Date>        contrDates;
	private Vector<String>      statuses;
	private Vector<Date>        svcDates;
	private Vector<Date>        cleanDates;
	private Vector<String>      svcAddrs;
	private Vector<String>      svcInstructs;
	private Vector<String>      maintIDs;
	private Vector<Date>        maintDates;

	private Vector<Integer>     svcSeqs;
	
	public ServiceTable()
	{
		rowCount = 0;
		svcNums      = new Vector<Integer>();
		contracts    = new Vector<String>();
		contrDates   = new Vector<Date>();
		statuses     = new Vector<String>();
		svcDates     = new Vector<Date>();
		cleanDates   = new Vector<Date>();
		svcAddrs     = new Vector<String>();
		svcInstructs = new Vector<String>();
		maintIDs     = new Vector<String>();
		maintDates   = new Vector<Date>();
		
		svcSeqs     = new Vector<Integer>();
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
		case COL_SVC_NUM    : return Integer.class;
		case COL_CONTRACT   : return String.class;
		case COL_CONTR_DT   : return Date.class; 
		case COL_STATUS     : return String.class;
		case COL_LAST_SVC   : return Date.class; 
		case COL_LAST_CLEAN : return Date.class; 
		case COL_ADDRESS    : return String.class;
		case COL_INSTRUCT   : return String.class;
		case COL_LAST_USER  : return String.class;
		case COL_LAST_MAINT : return Date.class;
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
		case COL_SVC_NUM    : return svcNums.get(row);
		case COL_CONTRACT   : return contracts.get(row);
		case COL_CONTR_DT   : return contrDates.get(row);
		case COL_STATUS     : return statuses.get(row);
		case COL_LAST_SVC   : return svcDates.get(row);
		case COL_LAST_CLEAN : return cleanDates.get(row);
		case COL_ADDRESS    : return svcAddrs.get(row);
		case COL_INSTRUCT   : return svcInstructs.get(row);
		case COL_LAST_USER  : return maintIDs.get(row);
		case COL_LAST_MAINT : return maintDates.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		rowCount = 0;

		svcNums.clear();
		contracts.clear();
		contrDates.clear();
		statuses.clear();
		cleanDates.clear();
		svcDates.clear();
		svcAddrs.clear();
		svcInstructs.clear();
		maintIDs.clear();
		maintDates.clear();

		svcSeqs.clear();

		while (results.next())
		{
			rowCount++;
			{
				Integer i = new Integer(results.getInt(COL_SVC_NUM+1));
				svcNums.add(i);
			}
			{
				contracts.add(results.getString(COL_CONTRACT+1)+results.getString(COL_CONTRACT+2));
			}
			{
				contrDates.add(results.getDate(COL_CONTR_DT+2));
			}
			{
				String stat = results.getString(COL_STATUS+2);
				switch (stat)
				{
				case "I": statuses.add("Inact"); break;
				case "T": statuses.add("Term"); break;
				case "A":
				default:  statuses.add("Active"); break;
				}
			}
			{
				svcDates.add(results.getDate(COL_LAST_SVC+2));
			}
			{
				cleanDates.add(results.getDate(COL_LAST_CLEAN+2));
			}
			{
				svcAddrs.add(results.getString(COL_ADDRESS+2));
			}
			{
				svcInstructs.add(results.getString(COL_INSTRUCT+2));
			}
			{
				maintIDs.add(results.getString(COL_LAST_USER+2));
			}
			{
				maintDates.add(results.getTimestamp(COL_LAST_MAINT+2));
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

		table.getColumnModel().getColumn(ServiceTable.COL_CONTR_DT  ).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(ServiceTable.COL_LAST_SVC  ).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(ServiceTable.COL_LAST_CLEAN).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(ServiceTable.COL_LAST_MAINT).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		
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
			out.write(svcNums.get(row)+"\t");
			out.write(contracts.get(row)+"\t");
			out.write(ADDBrowser.tm.format(contrDates.get(row))+"\t");
			out.write(statuses.get(row)+"\t");
			out.write(ADDBrowser.tm.format(svcDates.get(row))+"\t");
			out.write(ADDBrowser.tm.format(cleanDates.get(row))+"\t");
			out.write(svcAddrs.get(row)+"\t");
			out.write(svcInstructs.get(row)+"\t");
			out.write(maintIDs.get(row)+"\t");
			out.write(ADDBrowser.tm.format(maintDates.get(row))+"\t");
			out.write("\n");
		}
		out.close();
	}
}
