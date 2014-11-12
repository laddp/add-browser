/**
 * 
 */
package net.pladd.ADDBrowser;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * @author Patrick
 *
 */
public class Util
{
	/*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    static void initColumnSizes(JTable table, Object [] longValues) {
        TableModel model = table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < model.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                                 null, model.getColumnName(i),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            TableCellRenderer r = column.getCellRenderer();
            if (r == null)
            	r = table.getDefaultRenderer(model.getColumnClass(i));
            comp = r.getTableCellRendererComponent(table, longValues[i], false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
}
