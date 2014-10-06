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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.camick.FormatRenderer;

/**
 * @author Patrick
 *
 */
public class ContactTable extends AbstractTableModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2995147331400163594L;

	protected static List<String> header =
			Arrays.asList("Type", "Description", "Value", "Active", "Notes", "Maint Date", "User");
	
	private final Object[] longValues = { "MMMMMMMMMMMMMM", "MMMMMMMMM", "MMMMMMMMMMMMMMMMMMM", true, "MMMMMMMMMMMMMMMMMMM", 
			"MMMMMMMMM", new Date() };
	
	protected static final int COL_TYPE       = 0;
	protected static final int COL_DESC       = 1;
	protected static final int COL_VALUE      = 2;
	protected static final int COL_ACTIVE     = 3;
	protected static final int COL_NOTES      = 4;
	protected static final int COL_MAINT_DT   = 5;
	protected static final int COL_MAINT_USER = 6;
	protected static final int COL_CONTACT_ID = 7;
	

	protected int rowCount;
	
	private Vector<String>   types;
	private Vector<String>   descs;
	private Vector<String>   values;
	private Vector<Boolean>  active;
	private Vector<String>   notes;
	private Vector<String>   maintUsers;
	private Vector<Date>     maintDates;

	protected Vector<Integer>  contactIDs;
	
	private Map<Integer, String> contactTypes;

	public ContactTable(Map<Integer, String> contactTypes)
	{
		this.contactTypes = contactTypes;
		
		rowCount = 0;
		types        = new Vector<String>();
		descs        = new Vector<String>();
		values       = new Vector<String>();
		active       = new Vector<Boolean>();
		notes        = new Vector<String>();
		maintDates   = new Vector<Date>();
		maintUsers   = new Vector<String>();

		contactIDs   = new Vector<Integer>();
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
		case COL_TYPE       : return String.class;
		case COL_DESC       : return String.class;
		case COL_VALUE      : return String.class;
		case COL_ACTIVE     : return Boolean.class;
		case COL_NOTES      : return String.class;
		case COL_MAINT_USER : return String.class;
		case COL_MAINT_DT   : return Date.class;
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
		case COL_TYPE       : return types.get(row);
		case COL_DESC       : return descs.get(row);
		case COL_VALUE      : return values.get(row);
		case COL_ACTIVE     : return active.get(row);
		case COL_NOTES      : return notes.get(row);
		case COL_MAINT_USER : return maintUsers.get(row);
		case COL_MAINT_DT   : return maintDates.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		rowCount = 0;

		types        .clear();
		descs        .clear();
		values       .clear();
		active       .clear();
		notes        .clear();
		maintUsers   .clear();
		maintDates   .clear();
		contactIDs   .clear();
		
		while (results.next())
		{
			rowCount++;
			
			{
				int type = results.getInt(COL_TYPE+1);
				String desc = contactTypes.get(type);
				if (desc != null)
					types.add(desc);
				else
					types.add("Unknown");
			}
			{
				descs.add(results.getString(COL_DESC+1));
			}
			{
				values.add(results.getString(COL_VALUE+1));
			}
			{
				String act = results.getString(COL_ACTIVE+1);
				if (act.compareTo("A") == 0)
					active.add(true);
				else
					active.add(false);
			}
			{
				notes.add(results.getString(COL_NOTES+1));
			}
			{
				maintUsers.add(results.getString(COL_MAINT_USER+1));
			}
			{
				Timestamp ts = results.getTimestamp(COL_MAINT_DT+1);
				maintDates.add(new Date(ts.getTime()));
			}
			{
				Integer contID = results.getInt(COL_CONTACT_ID+1);
				contactIDs.add(new Integer(contID));
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

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
			out.write(types.get(row)+"\t");
			out.write(descs.get(row)+"\t");
			out.write(values.get(row)+"\t");
			out.write(active.get(row)+"\t");
			out.write(notes.get(row)+"\t");
			out.write(maintUsers.get(row)+"\t");
			out.write(ADDBrowser.tm.format(maintDates.get(row))+"\t");
			out.write("\n");
		}
		out.close();
	}
}
