/**
 * 
 */
package net.pladd.ADDBrowser;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.pladd.ADDBrowser.E3types.PostingCode;

import com.camick.FormatRenderer;
import com.camick.NumberRenderer;

/**
 * @author Patrick
 *
 */
public class TankTable extends AbstractTableModel {
	private static final long serialVersionUID = -4119385912426229448L;

	protected static List<String> header =
			Arrays.asList("Tank #", "Status", "Product", "Size", "Base Price", "Deviation", "Stop Code", "Deliv Addr", "Deliv Instr",
					"Last User", "Last Maint");
	
	private final Object[] longValues = { 
			new Integer("999"), "MMMMMMMMM", "MMMMMMMMMMMMMMMMM", new Integer("99999"), new Integer("999"), new BigDecimal("999999.9"),
			new Integer("99"),
			"MMMMMMMMMMMMMMMMM", "MMMMMMMMMMMMMMMMM",
			"MMMMMMMMMM", new Date() };
	
	protected static final int COL_TANK_NUM   = 0;
	protected static final int COL_STATUS     = 1;
	protected static final int COL_PRODUCT    = 2;
	protected static final int COL_SIZE       = 3;
	protected static final int COL_BASE_PRICE = 4;
	protected static final int COL_DEVIATION  = 5;
	protected static final int COL_STOP_CODE  = 6;
	protected static final int COL_ADDRESS    = 7;
	protected static final int COL_INSTRUCT   = 8;
	protected static final int COL_LAST_USER  = 9;
	protected static final int COL_LAST_MAINT = 10;
	protected static final int COL_TANK_SEQ   = 11;

	protected int rowCount;
	
	private Vector<Integer>     tankNums;
	private Vector<PostingCode> products;
	private Vector<Integer>     sizes;
	private Vector<Integer>     basePrices;
	private Vector<BigDecimal>  deviations;
	private Vector<String>      statuses;
	private Vector<Integer>     stopCodes;
	private Vector<String>      delAddrs;
	private Vector<String>      delInstructs;
	private Vector<String>      maintIDs;
	private Vector<Date>        maintDates;

	private Vector<Integer>     tankSeqs;
	
	public TankTable()
	{
		rowCount = 0;
		tankNums     = new Vector<Integer>();
		products     = new Vector<PostingCode>();
		sizes        = new Vector<Integer>();
		basePrices   = new Vector<Integer>();
		deviations   = new Vector<BigDecimal>();
		statuses     = new Vector<String>();
		stopCodes    = new Vector<Integer>();
		delAddrs     = new Vector<String>();
		delInstructs = new Vector<String>();
		maintIDs     = new Vector<String>();
		maintDates   = new Vector<Date>();
		
		tankSeqs     = new Vector<Integer>();
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
		case COL_TANK_NUM   : return Integer.class;
		case COL_PRODUCT    : return String.class;
		case COL_SIZE       : return Integer.class;
		case COL_BASE_PRICE : return Integer.class;
		case COL_DEVIATION  : return BigInteger.class;
		case COL_STATUS     : return String.class;
		case COL_STOP_CODE  : return Integer.class;
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
		case COL_TANK_NUM   : return tankNums.get(row);
		case COL_PRODUCT    : return products.get(row);
		case COL_SIZE       : return sizes.get(row);
		case COL_BASE_PRICE : return basePrices.get(row);
		case COL_DEVIATION  : return deviations.get(row);
		case COL_STATUS     : return statuses.get(row);
		case COL_STOP_CODE  : return stopCodes.get(row);
		case COL_ADDRESS    : return delAddrs.get(row);
		case COL_INSTRUCT   : return delInstructs.get(row);
		case COL_LAST_USER  : return maintIDs.get(row);
		case COL_LAST_MAINT : return maintDates.get(row);
		default: return null;
		}
	}

	public void clear()
	{
		rowCount = 0;

		tankNums.clear();
		products.clear();
		sizes.clear();
		basePrices.clear();
		deviations.clear();
		statuses.clear();
		stopCodes.clear();
		delAddrs.clear();
		delInstructs.clear();
		maintIDs.clear();
		maintDates.clear();

		tankSeqs.clear();
	}

	public void newResults(ResultSet results, JTable table) throws SQLException 
	{
		clear();

		while (results.next())
		{
			rowCount++;
			{
				Integer i = new Integer(results.getInt(COL_TANK_NUM+1));
				tankNums.add(i);
			}
			{
				PostingCode prod = ADDBrowser.postingCodes.get(results.getInt(COL_PRODUCT+1));
				products.add(prod);
			}
			{
				Integer i = new Integer(results.getInt(COL_SIZE+1));
				sizes.add(i);
			}
			{
				Integer i = new Integer(results.getInt(COL_BASE_PRICE+1));
				basePrices.add(i);
			}
			{
				Integer i = new Integer(results.getInt(COL_STOP_CODE+1));
				stopCodes.add(i);
			}
			{
				BigDecimal dev = results.getBigDecimal(COL_DEVIATION+1).movePointLeft(2);
				deviations.add(dev);
			}
			{
				String stat = results.getString(COL_STATUS+1);
				switch (stat)
				{
				case "I": statuses.add("Inact"); break;
				case "T": statuses.add("Term"); break;
				case "A":
				default:  statuses.add("Active"); break;
				}
			}
			{
				delAddrs.add(results.getString(COL_ADDRESS+1));
			}
			{
				delInstructs.add(results.getString(COL_INSTRUCT+1));
			}
			{
				maintIDs.add(results.getString(COL_LAST_USER+1));
			}
			{
				maintDates.add(results.getTimestamp(COL_LAST_MAINT+1));
			}
		}
		
		fireTableDataChanged();

		if (rowCount == 0)
			throw new SQLException("No results");

		table.getColumnModel().getColumn(TankTable.COL_LAST_MAINT).setCellRenderer(new FormatRenderer(ADDBrowser.df));
		table.getColumnModel().getColumn(TankTable.COL_DEVIATION ).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		
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
			out.write(tankNums.get(row)+"\t");
			out.write(products.get(row)+"\t");
			out.write(sizes.get(row)+"\t");
			out.write(basePrices.get(row)+"\t");
			out.write(deviations.get(row)+"\t");
			out.write(statuses.get(row)+"\t");
			out.write(stopCodes.get(row)+"\t");
			out.write(delAddrs.get(row)+"\t");
			out.write(delInstructs.get(row)+"\t");
			out.write(maintIDs.get(row)+"\t");
			out.write(ADDBrowser.tm.format(maintDates.get(row))+"\t");
			out.write("\n");
		}
		out.close();
	}
}
