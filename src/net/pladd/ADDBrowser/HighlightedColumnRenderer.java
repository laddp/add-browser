package net.pladd.ADDBrowser;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class HighlightedColumnRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 7175995921839106792L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		final Color lightYellow = new Color(255,255,175);
		Component l = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		l.setBackground(lightYellow);
		return l;
	}

}