/**
 * 
 */
package net.pladd.ADDBrowser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * @author Patrick
 *
 */
public class LogDetailTable extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1106954480681647595L;

	protected static List<String> header =
			Arrays.asList("Row", "Label", "Value", "Changed to");
	
	protected static final int COL_ROWNUM  = 0;
	protected static final int COL_LABEL   = 1;
	protected static final int COL_NEW_VAL = 2;
	protected static final int COL_OLD_VAL = 3;

	protected int rowCount;
	
	private Vector<Integer> rownums;
	private Vector<String>  labels;
	private Vector<String>  oldvals;
	private Vector<String>  newvals;

	public LogDetailTable()
	{
		rowCount = 0;

		rownums = new Vector<Integer>();
		labels  = new Vector<String>();
		oldvals = new Vector<String>();
		newvals = new Vector<String>();
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
		case COL_ROWNUM:  return Integer.class;
		case COL_LABEL:   return String.class;
		case COL_OLD_VAL: return String.class;
		case COL_NEW_VAL: return String.class;
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
		case COL_ROWNUM:  return rownums.get(row);
		case COL_LABEL:   return labels.get(row);
		case COL_OLD_VAL: return oldvals.get(row);
		case COL_NEW_VAL: return newvals.get(row);
		default: return null;
		}
	}

	public void newResults(ResultSet results) throws SQLException 
	{
		rowCount = 0;

		rownums.clear();
		labels .clear();
		oldvals.clear();
		newvals.clear();
		
		while (results.next())
		{
			rowCount++;
			{
				Integer i = new Integer(results.getInt(COL_ROWNUM+1));
				rownums.add(i);
			}
			{
				labels .add(results.getString(COL_LABEL+1));
				oldvals.add(results.getString(COL_OLD_VAL+1));
				newvals.add(results.getString(COL_NEW_VAL+1));
			}
		}
		
		fireTableDataChanged();
	}
}
