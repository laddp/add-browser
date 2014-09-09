/**
 * 
 */
package net.pladd.ADDBrowser;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import com.camick.FormatRenderer;
import com.camick.NumberRenderer;
import com.sybase.jdbcx.SybDriver;

/**
 * @author Patrick
 *
 */
public class ADDBrowser {
	
	protected static MainWindow mainWindow;
	protected static Connection dataSource = null;
	protected static String     tablePrefix = "";

	protected static BatchTable batchDetail = null;

	/**
	 * @param args
	 */
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow = new MainWindow();
					mainWindow .frmAddDataBrowser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void doConnect(String dbType, String serverName, String serverPort,
			String databaseName, String userName, String password)
	{
		String conStr;
		if (dbType.compareTo(ConnectDialog.MYSQL_STRING) == 0)
		{
			conStr = "jdbc:mysql://" + serverName;
			if (serverPort.length() != 0)
				conStr += ":" + serverPort;
			conStr += "/" + 
					databaseName + "?" +
					"user=" + userName + "&" +
					"password=" + password;
			tablePrefix = "";
		}
		else if (dbType.compareTo(ConnectDialog.SYBASE_STRING) == 0)
		{
			try
	        {
	            SybDriver sybDriver = 
	                (SybDriver)Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
	            sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_6);
	            DriverManager.registerDriver(sybDriver);
	        }
	        catch (Exception e)
	        {
	            System.out.println(e);
	        }
			conStr = "jdbc:sybase:Tds:" +
					serverName + ":" + 
					serverPort +"/" + 
					databaseName + "?" +
					"USER=" + userName + "&" +
					"PASSWORD=" + password;
			tablePrefix = "dbo.";
		}
		else
		{
			conStr = "";
			tablePrefix = "";
		}

		try {
			Connection newConnection = DriverManager.getConnection(conStr);

			if (dataSource != null)
				dataSource.close();
			dataSource = newConnection;
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Connection succeeded", "Connection Succeeded", JOptionPane.INFORMATION_MESSAGE);
			mainWindow.enableQueries(true);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Connection failed:" + e, "Connection Failed", JOptionPane.ERROR_MESSAGE);
			mainWindow.enableQueries(false);
			dataSource = null;
			
		}
	}

	public static void doBatchQuery(String postingDate, String batchNum) {
		try {
			Statement stmt = dataSource.createStatement();
			String query =
					"SELECT " + 
							tablePrefix + "TRANS_MAIN.event_date, " +
							tablePrefix + "TRANS_MAIN.posting_date, " +
							tablePrefix + "TRANS_MAIN.batch_num, " +
							tablePrefix + "FULL_ACCOUNT.full_account, " +
							tablePrefix + "ACCOUNTS.name, " +
							tablePrefix + "TRANS_MAIN.posting_code, " +
							tablePrefix + "POST_CODE.long_desc, " +
							tablePrefix + "TRANS_MAIN.net_amount, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_userid, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_dt " +
					"FROM " +
						tablePrefix + "TRANS_MAIN inner join " + tablePrefix + "FULL_ACCOUNT ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
						"inner join " + tablePrefix + "ACCOUNTS ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "ACCOUNTS.account_num "+
						"inner join " + tablePrefix + "POST_CODE ON " +
							tablePrefix + "TRANS_MAIN.posting_code = " + tablePrefix + "POST_CODE.posting_code " +
					"WHERE " +
						tablePrefix + "TRANS_MAIN.posting_date = '" + postingDate + "' " + "and " + 
						tablePrefix + "TRANS_MAIN.batch_num = " + batchNum + " " +
					"ORDER BY " +
						tablePrefix + "TRANS_MAIN.posting_date, " +
						tablePrefix + "TRANS_MAIN.batch_num, " +
						tablePrefix + "TRANS_MAIN.account_num";
			ResultSet results = stmt.executeQuery(query);
			
			if (batchDetail == null)
			{
				batchDetail = new BatchTable();
				mainWindow.batchTable.setAutoCreateRowSorter(true);
				mainWindow.batchTable.setModel(batchDetail);
			}
			batchDetail.newResults(results);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat tm = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			mainWindow.batchTable.getColumnModel().getColumn(0).setCellRenderer(new FormatRenderer(df));
			mainWindow.batchTable.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer(df));
			mainWindow.batchTable.getColumnModel().getColumn(2).setCellRenderer(new FormatRenderer(tm));
			mainWindow.batchTable.getColumnModel().getColumn(8).setCellRenderer(NumberRenderer.getCurrencyRenderer());
			mainWindow.batchTable.getColumnModel().getColumn(9).setCellRenderer(NumberRenderer.getCurrencyRenderer());
			mainWindow.batchTable.getColumnModel().getColumn(10).setCellRenderer(NumberRenderer.getCurrencyRenderer());
			mainWindow.tabbedPane.setSelectedIndex(2);
			mainWindow.setExportButtonState();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
}
