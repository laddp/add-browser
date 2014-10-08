/**
 * 
 */
package net.pladd.ADDBrowser;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import net.pladd.ADDBrowser.E3types.Account;
import net.pladd.ADDBrowser.E3types.Category;
import net.pladd.ADDBrowser.E3types.Division;
import net.pladd.ADDBrowser.E3types.DocType;
import net.pladd.ADDBrowser.E3types.LogCategory;
import net.pladd.ADDBrowser.E3types.LogType;
import net.pladd.ADDBrowser.E3types.PostingCode;
import net.pladd.ADDBrowser.E3types.Type;

import com.sybase.jdbcx.SybDriver;

/**
 * @author Patrick
 *
 */
public class ADDBrowser {
	
	protected static MainWindow mainWindow;
	protected static Connection dataSource  = null;
	protected static String     tablePrefix = "";
	
	protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	protected static DateFormat tm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected static ContactTable contactDetail = null;
	protected static TankTable    tankDetail    = null;

	protected static LogTable     logDetail     = null;
	protected static DocTable     docDetail     = null;
	protected static BatchTable   batchDetail   = null;
	protected static BatchTable   transDetail   = null;
	
	protected static Map<Integer, PostingCode> postingCodes  = new TreeMap<Integer, PostingCode>();
	public    static Map<Integer, Division>    divisions     = new TreeMap<Integer, Division>();
	public    static Map<Integer, Category>    categories    = new TreeMap<Integer, Category>();
	public    static Map<String,  Type>        types         = new TreeMap<String, Type>();
	
	public    static Map<String,  LogCategory> logCategories = new TreeMap<String,  LogCategory>();
	public    static Map<Integer, LogType>     logTypes      = new TreeMap<Integer, LogType>();
	
	public    static Map<Integer, DocType>     docTypes      = new TreeMap<Integer, DocType>();
	
	public    static Map<Integer, String>      contactTypes  = new TreeMap<Integer, String>();
	public    static Map<Integer, String>      contactDescrs = new TreeMap<Integer, String>();

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
					mainWindow.doConnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void doConnect(String dbType, String serverName, String serverPort,
			String databaseName, String userName, String password,
			int maxDebit, int maxPost, String invalPost, boolean typesUniform) throws Exception
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
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			Connection newConnection = DriverManager.getConnection(conStr);

			if (dataSource != null)
				dataSource.close();
			dataSource = newConnection;
			
			PostingCode.maxDebit   = maxDebit;
			PostingCode.max        = maxPost;
			PostingCode.invalLabel = invalPost;
			Type.typesUniform      = typesUniform;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Connection failed:" + e, "Connection Failed", JOptionPane.ERROR_MESSAGE);
			dataSource = null;
			throw e;
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}

		try {
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			fetchPostingCodes();
			fetchCategories();
			fetchDivisions();
			fetchTypes();
			fetchLogCategories();
			fetchLogTypes();
			fetchDocTypes();
			fetchContactTypes();
			mainWindow.newCodes(postingCodes, logCategories, logTypes, docTypes);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}

		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT min(posting_date), max(posting_date) " +
							"FROM " + tablePrefix + "TRANS_MAIN");

			if (!results.next())
				throw new SQLException("no results");
			Date min = results.getDate(1);
			Date max = results.getDate(2);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, 
					"Connection succeeded - Min Date " + min + " Max Date " + max, 
					"Connection Succeeded", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	private static void fetchPostingCodes() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT posting_code, long_desc " +
							"FROM " + tablePrefix + "POST_CODE " +
							"WHERE posting_code < " + PostingCode.max +
							" and " + "short_desc <> '" + PostingCode.invalLabel + "'");
	
			postingCodes.clear();
			while (results.next())
			{
				postingCodes.put(results.getInt(1), new PostingCode(results.getInt(1), results.getString(2)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching posting codes:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchDivisions() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT division, name " +
							"FROM " + tablePrefix + "DIVISION_INFO ");
	
			divisions.clear();
			while (results.next())
			{
				divisions.put(results.getInt(1), new Division(results.getInt(1), results.getString(2)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching division info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchTypes() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			String query = "SELECT division, type, name " +
					"FROM " + tablePrefix + "TYPE_INFO ";
			if (Type.typesUniform)
				query += "WHERE division = 1";
			ResultSet results = stmt.executeQuery(query);
	
			types.clear();
			while (results.next())
			{
				String key;
				if (Type.typesUniform)
					key = "" + results.getInt(2);
				else
					key = "" + results.getInt(1) + "-" + results.getInt(2);
				types.put(key, new Type(results.getInt(1), results.getInt(2), results.getString(3)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching type info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchCategories() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT category, description " +
							"FROM " + tablePrefix + "CATEGORY_INFO ");
	
			categories.clear();
			while (results.next())
			{
				categories.put(results.getInt(1), new Category(results.getInt(1), results.getString(2)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching category info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchLogCategories() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT log_group_id, log_category_id, log_category_desc " +
							"FROM " + tablePrefix + "LOG_CATEGORY ");
	
			logCategories.clear();
			while (results.next())
			{
				logCategories.put("" + results.getInt(1) + "-" + results.getInt(2),
						new LogCategory(results.getInt(1), results.getInt(2), results.getString(3)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching log category info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchLogTypes() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT log_template_header_id, log_template_long_desc " +
							"FROM " + tablePrefix + "LOG_TEMPLATE_HEADER ");
	
			logTypes.clear();
			while (results.next())
			{
				logTypes.put(results.getInt(1),
						new LogType(results.getInt(1), results.getString(2)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching log type info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchDocTypes() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT doc_type, description " +
							"FROM " + tablePrefix + "DOC_TYPE_GROUP ");
	
			docTypes.clear();
			while (results.next())
			{
				docTypes.put(results.getInt(1),
						new DocType(results.getInt(1), results.getString(2)));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching doc type info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	private static void fetchContactTypes() throws SQLException
	{
		try {
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT code, description " +
							"FROM " + tablePrefix + "CODE_DESCRIPTION_DETAIL " +
					"WHERE table_id = 148");
	
			contactTypes.clear();
			while (results.next())
			{
				contactTypes.put(results.getInt(1), results.getString(2));
			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching contact type info:" + e, "Setup problem", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		try {
			contactDescrs.clear();
			Statement stmt = dataSource.createStatement();
			ResultSet results = stmt.executeQuery(
					"SELECT contact_type_id, description " +
							"FROM " + tablePrefix + "CONTACT_TYPE ");

			while (results.next())
			{
				contactDescrs.put(results.getInt(1), results.getString(2));
			}
		}
		catch (SQLException e)
		{
			try {
				Statement stmt = dataSource.createStatement();
				ResultSet results = stmt.executeQuery("SELECT contact_type_id " +
						"FROM " + tablePrefix + "CONTACT_INFO_HDR " +
						" group by contact_type_id");
		
				while (results.next())
				{
					int code = results.getInt(1);
					contactDescrs.put(results.getInt(1), "Type: " + code);
				}
			}
			catch (SQLException e1)
			{
				System.out.println(e1);
				JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Error fetching contact type info:" + e1, "Setup problem", JOptionPane.ERROR_MESSAGE);
				throw e;
			}
		}
	}


	public static void doAcctSearch(Map<String, String> acctQuery)
	{
		String queryPrefix =
			"SELECT " + 
				tablePrefix + "FULL_ACCOUNT.full_account, " +
				tablePrefix + "ACCOUNTS.account_num, " +
				tablePrefix + "ACCOUNTS.sort_code, " +
				tablePrefix + "ACCOUNTS.name, " +
				tablePrefix + "ACCOUNTS.title, " +
				tablePrefix + "ACCOUNTS.first_name, " +
				tablePrefix + "ACCOUNTS.middle_initial, " +
				tablePrefix + "ACCOUNTS.last_name, " +
				tablePrefix + "ACCOUNTS.name_suffix, " +
				tablePrefix + "ACCOUNTS.street1, " +
				tablePrefix + "ACCOUNTS.street2, " +
				tablePrefix + "ACCOUNTS.city, " +
				tablePrefix + "ACCOUNTS.state, " +
				tablePrefix + "ACCOUNTS.postal_code, " +
				tablePrefix + "ACCOUNTS.division, " +
				tablePrefix + "ACCOUNTS.type, " +
				tablePrefix + "ACCOUNTS.category, " +
				tablePrefix + "ACCOUNTS.balance " +
			" FROM " +
				tablePrefix + "FULL_ACCOUNT inner join " + tablePrefix + "ACCOUNTS ON " +
					tablePrefix + "ACCOUNTS.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
				"inner join " + tablePrefix + "DIVISION_INFO ON " +
					tablePrefix + "ACCOUNTS.division = " + tablePrefix + " DIVISION_INFO.division " +
				"inner join " + tablePrefix + "TYPE_INFO ON " +
					tablePrefix + "ACCOUNTS.type = " + tablePrefix + " TYPE_INFO.type and " +
					tablePrefix + "ACCOUNTS.division = " + tablePrefix + " TYPE_INFO.division ";
	
		String queryWhere   = null;
		String queryContact = null;
		
		for (Map.Entry<String, String> item : acctQuery.entrySet())
		{
			switch (item.getKey())
			{
			case "full_account":
				if (queryWhere != null)
					queryWhere += " and ";
				else
					queryWhere = " WHERE ";
				queryWhere += tablePrefix + "FULL_ACCOUNT." + item.getKey() + " like '" + item.getValue() + "' ";
				break;
			case "sort_code":
			case "name":
			case "title":
			case "first_name":
			case "middle_initial":
			case "last_name":
			case "name_suffix":
			case "street1":
			case "street2":
			case "city":
			case "state":
			case "postal_code":
				if (queryWhere != null)
					queryWhere += " and ";
				else
					queryWhere = " WHERE ";
				queryWhere += tablePrefix + "ACCOUNTS." + item.getKey() + " like '" + item.getValue() + "' ";
				break;
			case "division":
			case "type":
			case "category":
				if (queryWhere != null)
					queryWhere += " and ";
				else
					queryWhere = " WHERE ";
				queryWhere += tablePrefix + "ACCOUNTS." + item.getKey() + " in (" + item.getValue() + ") ";
				break;
			case "telephone":
				if (queryContact != null)
				{
					JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Can only query email or phone, not both", "Query Failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
				queryContact = " WHERE " + tablePrefix + "CONTACT_INFO_HDR.type = 1 and " +
						tablePrefix + "CONTACT_INFO_HDR.contact_value like '" + item.getValue() + "' ";
				break;
			case "email":
				if (queryContact != null)
				{
					JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Can only query email or phone, not both", "Query Failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
				queryContact = " WHERE " + tablePrefix + "CONTACT_INFO_HDR.type = 3 and " +
						tablePrefix + "CONTACT_INFO_HDR.contact_value like '" + item.getValue() + "' ";
				break;
			default:
				break;
			}
		}
		
		if (queryContact != null && queryWhere != null)
		{
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Email & phone queries can't be combined with other queries", "Query Failed", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String querySuffix = " ORDER BY " + tablePrefix + "ACCOUNTS.account_num";
		
		if (queryContact != null)
		{
			try
			{
				Statement stmt = dataSource.createStatement();
				mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				String queryContactPrefix =
						"SELECT " + 
								tablePrefix + "CONTACT_INFO_HDR.account_num " +
						"FROM " +
							tablePrefix + "CONTACT_INFO_HDR ";

				ResultSet results = stmt.executeQuery(queryContactPrefix + queryContact);

				while (results.next())
				{
					if (queryWhere == null)
						queryWhere = "";
					else
						queryWhere += ", ";
					queryWhere += results.getInt(1);
				}
				if (queryWhere == null)
					throw new SQLException("No results");
				
				queryWhere = " WHERE ACCOUNTS.account_num in (" + queryWhere + ") ";
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
			}
		}
		
		try
		{
			Statement stmt = dataSource.createStatement();
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);

			List<Account> accounts = new LinkedList<Account>();

			while (results.next())
			{
				int division = results.getInt(15);
				int typeNum  = results.getInt(16);
				Type type;
				if (Type.typesUniform)
					type = ADDBrowser.types.get(""+typeNum);
				else
					type = ADDBrowser.types.get(""+division+"-"+typeNum);

				accounts.add(new Account(
						results.getString(1),
						results.getInt(2),
						results.getString(3),
						results.getString(4),
						results.getString(5),
						results.getString(6),
						results.getString(7),
						results.getString(8),
						results.getString(9),
						results.getString(10),
						results.getString(11),
						results.getString(12),
						results.getString(13),
						results.getString(14),
						division,
						type,
						results.getInt(17),
						results.getBigDecimal(18)));
			}

			if (accounts.size() == 0)
				throw new SQLException("No results");
			mainWindow.accountResults(accounts);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}

	}

	public static void getAcctContactInfo(Account acct)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "CONTACT_INFO_HDR.type, " +
							tablePrefix + "CONTACT_INFO_HDR.contact_type_id, " +
							tablePrefix + "CONTACT_INFO_HDR.contact_value, " +
							tablePrefix + "CONTACT_INFO_HDR.row_status, " +
							tablePrefix + "CONTACT_INFO_HDR.notes, " +
							tablePrefix + "CONTACT_INFO_HDR.last_maintenance_dt, " +
							tablePrefix + "CONTACT_INFO_HDR.last_maintenance_userid, " +
							tablePrefix + "CONTACT_INFO_HDR.cust_contact_id " +
					"FROM " +
						tablePrefix + "CONTACT_INFO_HDR ";

			String queryWhere = "WHERE "+
					tablePrefix + "CONTACT_INFO_HDR.account_num = " + acct.account_num + " ";
			
			String querySuffix =
					"ORDER BY " +
						tablePrefix + "CONTACT_INFO_HDR.type";
			
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);

			if (contactDetail == null)
			{
				contactDetail = new ContactTable(contactTypes);
				mainWindow.contactInfoTable.setAutoCreateRowSorter(true);
				mainWindow.contactInfoTable.setModel(contactDetail);
			}

			contactDetail.newResults(results, mainWindow.contactInfoTable);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static String getAcctPrimary(Account acct, int type)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "CONTACT_INFO_HDR.contact_value " +
					"FROM " +
						tablePrefix + "CONTACT_INFO_HDR inner join " + tablePrefix + "CONTACT_INFO_DTL ON " +
							tablePrefix + "CONTACT_INFO_HDR.cust_contact_id = " + tablePrefix + "CONTACT_INFO_DTL.cust_contact_id ";
	
			String queryWhere = "WHERE "+
					tablePrefix + "CONTACT_INFO_HDR.account_num   = " + acct.account_num + " and " +
					tablePrefix + "CONTACT_INFO_DTL.location_type = 0 and " +
					tablePrefix + "CONTACT_INFO_DTL.location_num  = 0 and " +
					tablePrefix + "CONTACT_INFO_DTL.primary_flag  = 'Y' and " +
					tablePrefix + "CONTACT_INFO_HDR.type = " + type;
			
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere);
	
			String rc = "";
			while (results.next())
			{
				if (rc.length() != 0)
					return "Error: multiple primaries!";
				else
					rc += results.getString(1);
			}
			return rc;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
			return "";
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void getTankInfo(Account acct)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "TANKS.tank_num, " +
							tablePrefix + "TANKS.product, " +
							tablePrefix + "TANKS.size, " +
							tablePrefix + "TANKS.base_price_code, " +
							tablePrefix + "TANKS.deviation, " +
							tablePrefix + "TANKS.status, " +
							tablePrefix + "TANKS.delivery_stop, " +
							tablePrefix + "DAD_TEXT.dad_text, " +
							tablePrefix + "DIN_TEXT.din_text, " +
							tablePrefix + "TANKS.last_maintenance_userid, " +
							tablePrefix + "TANKS.last_maintenance_dt, " +
							tablePrefix + "TANKS.tank_seq_number " +
					"FROM " +
						tablePrefix + "TANKS inner join "  + tablePrefix + "DAD_TEXT ON " +
								tablePrefix + "TANKS.tank_seq_number = " + tablePrefix + "DAD_TEXT.dad_text_owner " +
								"inner join " + tablePrefix + "DIN_TEXT ON " +
								tablePrefix + "TANKS.tank_seq_number = " + tablePrefix + "DIN_TEXT.din_text_owner ";
	
			String queryWhere = "WHERE "+
					tablePrefix + "TANKS.account_num   = " + acct.account_num + " ";
			
			String querySuffix = " ORDER BY TANKS.tank_num";
			
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
	
			if (tankDetail == null)
			{
				tankDetail = new TankTable();
				mainWindow.tankInfoTable.setAutoCreateRowSorter(true);
				mainWindow.tankInfoTable.setModel(tankDetail);
			}
			tankDetail.newResults(results, mainWindow.tankInfoTable);
		} 
		catch (SQLException e)
		{
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void doLogSearch(String full_account, String startDate, String endDate, List<LogCategory> catList, List<LogType> typeList, String note)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
						tablePrefix + "FULL_ACCOUNT.full_account, " +
						tablePrefix + "LOG_HEADER.location_type, " +
						tablePrefix + "LOG_HEADER.location, " +
						tablePrefix + "LOG_HEADER.create_dt, " +
						tablePrefix + "LOG_HEADER.create_user_id, " +
						tablePrefix + "LOG_HEADER.resolved, " +
						tablePrefix + "LOG_HEADER.resolved_dt, " +
						tablePrefix + "LOG_HEADER.resolved_user_id, " +
						tablePrefix + "LOG_HEADER.last_maintenance_dt, " +
						tablePrefix + "LOG_HEADER.last_maintenance_userid, " +
						tablePrefix + "LOG_TEMPLATE_HEADER.log_template_long_desc, " +
						tablePrefix + "LOG_HEADER.note1, " +
						tablePrefix + "LOG_HEADER.note2, " +
						tablePrefix + "LOG_HEADER.note3, " +
						tablePrefix + "LOG_HEADER.note4, " +
						tablePrefix + "LOG_HEADER.resolved_note1, " +
						tablePrefix + "LOG_HEADER.resolved_note2, " +
						tablePrefix + "LOG_HEADER.created_time, " +
						tablePrefix + "LOG_HEADER.account_num, " +
						tablePrefix + "LOG_HEADER.log_header_id, " +
						tablePrefix + "LOG_HEADER.log_template_header_id " +
					"FROM " +
						tablePrefix + "LOG_HEADER inner join " + tablePrefix + "FULL_ACCOUNT ON " +
							tablePrefix + "LOG_HEADER.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
						"inner join " + tablePrefix + "LOG_TEMPLATE_HEADER ON " +
							tablePrefix + "LOG_TEMPLATE_HEADER.log_template_header_id = " + tablePrefix + "LOG_HEADER.log_template_header_id ";
			
			String queryWhere = "WHERE ";
			if (full_account != null)
				queryWhere += tablePrefix + "FULL_ACCOUNT.full_account like '" + full_account + "' ";
			if (startDate != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "LOG_HEADER.create_dt >= '" + startDate + "' ";
			}
			if (endDate != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "LOG_HEADER.create_dt <= '" + endDate + "' ";
			}
			if (catList != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += "(";
				boolean firstCat = true;
				for (LogCategory cat : catList)
				{
					if (!firstCat)
						queryWhere += " OR ";
					firstCat = false;
					queryWhere += "(" + tablePrefix + "LOG_TEMPLATE_HEADER.log_category_id = " + cat.categoryID + " and " +
									    tablePrefix + "LOG_TEMPLATE_HEADER.log_group_id    = " + cat.groupID + ")";
				}
				queryWhere += ") ";
			}
			if (typeList != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "LOG_TEMPLATE_HEADER.log_template_header_id in (";
				boolean firstType = true;
				for (LogType type : typeList)
				{
					if (!firstType)
						queryWhere += ", ";
					firstType = false;
					queryWhere += type.typeID;
				}
				queryWhere += ")";
			}
			if (note != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += "(" +
					tablePrefix + "LOG_HEADER.note1 like '" + note + "' OR " +
					tablePrefix + "LOG_HEADER.note2 like '" + note + "' OR " +
					tablePrefix + "LOG_HEADER.note3 like '" + note + "' OR " +
					tablePrefix + "LOG_HEADER.note4 like '" + note + "' OR " +
					tablePrefix + "LOG_HEADER.resolved_note1 like '" + note + "' OR " +
					tablePrefix + "LOG_HEADER.resolved_note1 like '" + note + "') ";
			}
			
			if (queryWhere.length() == 6)
			{
				JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "No query specified", "Query Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String querySuffix = "ORDER BY " + 
					tablePrefix + "LOG_HEADER.account_num, " +
					tablePrefix + "LOG_HEADER.create_dt, " +
					tablePrefix + "LOG_HEADER.created_time";
			
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);

			if (logDetail == null)
			{
				logDetail = new LogTable();
				mainWindow.logTable.setAutoCreateRowSorter(true);
				mainWindow.logTable.setModel(logDetail);
			}
			logDetail.newResults(results, mainWindow.logTable);
			mainWindow.logNotes.setText("");
			mainWindow.logResolveNotes.setText("");
			mainWindow.newLogDetail(null);
			mainWindow.tabbedPane.setSelectedIndex(MainWindow.LOG_TAB_INDEX);
			mainWindow.setExportButtonState();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void doDocSearch(String acctNum, String refNum, 
			String startDate, String endDate, String srchTypes)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "FULL_ACCOUNT.full_account, " +
							tablePrefix + "DOC_HEADER.tank_num, " +
							tablePrefix + "DOC_HEADER.service_num, " +
							tablePrefix + "ACCOUNTS.name, " +
							tablePrefix + "DOC_HEADER.doc_reference_num, " +
							tablePrefix + "DOC_TYPE_GROUP.description, " +
							tablePrefix + "DOC_HEADER.doc_date, " +
							tablePrefix + "DOC_HEADER.doc_time, " +
							tablePrefix + "DOC_HEADER.last_maintenance_dt, " +
							tablePrefix + "DOC_HEADER.last_maintenance_userid, " +
							tablePrefix + "DOC_HEADER.doc_id " +
					"FROM " +
						tablePrefix + "DOC_HEADER inner join " + tablePrefix + "FULL_ACCOUNT ON " +
							tablePrefix + "DOC_HEADER.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
						"inner join " + tablePrefix + "ACCOUNTS ON " +
							tablePrefix + "DOC_HEADER.account_num = " + tablePrefix + "ACCOUNTS.account_num "+
						"inner join " + tablePrefix + "DOC_TYPE_GROUP ON " +
							tablePrefix + "DOC_HEADER.doc_type = " + tablePrefix + "DOC_TYPE_GROUP.doc_type ";

			String queryWhere = "WHERE ";
			if (acctNum != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "FULL_ACCOUNT.full_account like '" + acctNum + "' ";
			}
			if (refNum != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "DOC_HEADER.doc_reference_num = " + refNum + " ";
			}
			if (startDate != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "DOC_HEADER.doc_date >= '" + startDate + "' ";
			}
			if (endDate != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "DOC_HEADER.doc_date <= '" + endDate + "' ";
			}
			if (srchTypes != null)
			{
				if (queryWhere.length() != 6)
					queryWhere += " AND ";
				queryWhere += tablePrefix + "DOC_HEADER.doc_type in (" + srchTypes + ") ";
			}
			
			String querySuffix =
					"ORDER BY " +
						tablePrefix + "DOC_HEADER.account_num, " +
						tablePrefix + "DOC_HEADER.doc_date, " +
						tablePrefix + "DOC_HEADER.doc_time";
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			
			if (docDetail == null)
			{
				docDetail = new DocTable();
				mainWindow.docListTable.setAutoCreateRowSorter(true);
				mainWindow.docListTable.setModel(docDetail);
			}
			docDetail.newResults(results, mainWindow.docListTable);
			mainWindow.tabbedPane.setSelectedIndex(MainWindow.DOC_TAB_INDEX);
			mainWindow.setExportButtonState();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void doDocContents(Integer docID)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix = 
					"SELECT " + 
						"doc_line, doc_line_data " +
					"FROM " + tablePrefix + "DOC_PRINT_DETAIL ";
			String queryWhere = " WHERE "+
					"doc_id = " + docID;
			String querySuffix = " ORDER BY " + "doc_page, doc_line";
			
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			
			mainWindow.docContent.setText("");
			int oldLine = 0;
			while (results.next())
			{
				int newLine = results.getInt(1);
				String padding = "";
				for (int i = newLine - oldLine - 1; i > 0; i--)
					padding += "\n";
				oldLine = newLine;
				if (padding.length() != 0)
					mainWindow.docContent.append(padding);

				mainWindow.docContent.append(results.getString(2) + "\n");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static String fullLogNote(int acct_num, int hdr_id, int tmpl_hdr_id, int noteType)
	{
		String rc = "";
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix = 
					"SELECT " + 
						tablePrefix + "LOG_NOTE.note " +
					"FROM " + tablePrefix + "LOG_HEADER " +
						"inner join  " + tablePrefix + "LOG_NOTE ON " +
							tablePrefix + "LOG_HEADER.account_num = " + tablePrefix + "LOG_NOTE.account_num and " +
							tablePrefix + "LOG_HEADER.log_header_id = " + tablePrefix + "LOG_NOTE.log_header_id ";
			String queryWhere = "WHERE "+
					tablePrefix + "LOG_NOTE.note_type       = " + noteType + " and " +
					tablePrefix + "LOG_HEADER.account_num   = " + acct_num + " and " +
					tablePrefix + "LOG_HEADER.log_header_id = " + hdr_id   + " and " +
					tablePrefix + "LOG_HEADER.log_template_header_id = " + tmpl_hdr_id + " ";
			String querySuffix = "ORDER BY " + tablePrefix + "LOG_NOTE.sequence_no";
			
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			
			while (results.next())
			{
				rc += results.getString(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		return rc;
	}

	
	public static void doLogDetail(int acct_num, int hdr_id, int tmpl_hdr_id)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "LOG_TEMPLATE_DETAIL.sequence_no, " +
							tablePrefix + "LOG_TEMPLATE_DETAIL.label, " +
							tablePrefix + "LOG_DETAIL.value, " +
							tablePrefix + "LOG_DETAIL.value_1 " +
					"FROM " +
						tablePrefix + "LOG_DETAIL inner join " + tablePrefix + "LOG_HEADER ON " +
							tablePrefix + "LOG_HEADER.account_num   = " + tablePrefix + "LOG_DETAIL.account_num and " +
							tablePrefix + "LOG_HEADER.log_header_id = " + tablePrefix + "LOG_DETAIL.log_header_id " + 
						"inner join " + tablePrefix + "LOG_TEMPLATE_DETAIL ON " +
							tablePrefix + "LOG_HEADER.log_template_header_id = " + tablePrefix + "LOG_TEMPLATE_DETAIL.log_template_header_id and "+
							tablePrefix + "LOG_TEMPLATE_DETAIL.sequence_no = " + tablePrefix + "LOG_DETAIL.sequence_no ";

			String queryWhere = "WHERE "+
					tablePrefix + "LOG_HEADER.account_num   = " + acct_num + " and " +
					tablePrefix + "LOG_HEADER.log_header_id = " + hdr_id   + " and " +
					tablePrefix + "LOG_HEADER.log_template_header_id = " + tmpl_hdr_id + " ";
			String querySuffix = "ORDER BY " + tablePrefix + "LOG_DETAIL.sequence_no";
			
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			mainWindow.newLogDetail(results);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void doBatchQuery(String postingDate, String batchNum)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "TRANS_MAIN.event_date, " +
							tablePrefix + "TRANS_MAIN.posting_date, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_dt, " +
							tablePrefix + "TRANS_MAIN.batch_num, " +
							tablePrefix + "FULL_ACCOUNT.full_account, " +
							tablePrefix + "ACCOUNTS.name, " +
							tablePrefix + "TYPE_INFO.name as \"type\", " +
							tablePrefix + "TRANS_MAIN.posting_code, " +
							tablePrefix + "POST_CODE.long_desc, " +
							tablePrefix + "TRANS_MAIN.reference_num, " +
							tablePrefix + "TRANS_MAIN.net_amount, " +
							tablePrefix + "TRANS_MAIN.units, " +
							tablePrefix + "TRANS_MAIN.ppg, " +
							tablePrefix + "TRANS_MAIN.created_by, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_userid, " +
							tablePrefix + "TRANS_MAIN.comment, " +
							tablePrefix + "TRANS_MAIN.invoice_comment " +
					"FROM " +
						tablePrefix + "TRANS_MAIN inner join " + tablePrefix + "FULL_ACCOUNT ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
						"inner join " + tablePrefix + "ACCOUNTS ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "ACCOUNTS.account_num "+
						"inner join " + tablePrefix + "POST_CODE ON " +
							tablePrefix + "TRANS_MAIN.posting_code = " + tablePrefix + "POST_CODE.posting_code " +
						"inner join " + tablePrefix + "TYPE_INFO ON " +
							tablePrefix + "ACCOUNTS.type = " + tablePrefix + " TYPE_INFO.type and " +
							tablePrefix + "ACCOUNTS.division = " + tablePrefix + " TYPE_INFO.division ";

			String queryWhere = "WHERE "+
					tablePrefix + "TRANS_MAIN.posting_code <= " + PostingCode.max + " and " +
					tablePrefix + "TRANS_MAIN.status = 'A' ";
			if (postingDate != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.posting_date = '" + postingDate + "' ";
			if (batchNum != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.batch_num = " + batchNum + " ";
			
			String querySuffix =
					"ORDER BY " +
						tablePrefix + "TRANS_MAIN.posting_date, " +
						tablePrefix + "TRANS_MAIN.batch_num, " +
						tablePrefix + "TRANS_MAIN.account_num";
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			
			if (batchDetail == null)
			{
				batchDetail = new BatchTable();
				mainWindow.batchTable.setAutoCreateRowSorter(true);
				mainWindow.batchTable.setModel(batchDetail);
			}
			mainWindow.batchTotals.setText("");
			batchDetail.newResults(results, mainWindow.batchTable);
			mainWindow.batchTotals.setText(
					"Total: "   + NumberFormat.getCurrencyInstance().format(batchDetail.totalAmt) + " " +
					"Credits: " + NumberFormat.getCurrencyInstance().format(batchDetail.credAmt)  + " " +
					"Debits: "  + NumberFormat.getCurrencyInstance().format(batchDetail.debAmt)   + " " +
					"Count: "    + batchDetail.rowCount
					);
			mainWindow.tabbedPane.setSelectedIndex(MainWindow.BATCH_TAB_INDEX);
			mainWindow.setExportButtonState();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void doTransactionQuery(String startDate, String endDate, String acctNum, String postCodes, String refNum)
	{
		try {
			Statement stmt = dataSource.createStatement();
			String queryPrefix =
					"SELECT " + 
							tablePrefix + "TRANS_MAIN.event_date, " +
							tablePrefix + "TRANS_MAIN.posting_date, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_dt, " +
							tablePrefix + "TRANS_MAIN.batch_num, " +
							tablePrefix + "FULL_ACCOUNT.full_account, " +
							tablePrefix + "ACCOUNTS.name, " +
							tablePrefix + "TYPE_INFO.name as \"type\", " +
							tablePrefix + "TRANS_MAIN.posting_code, " +
							tablePrefix + "POST_CODE.long_desc, " +
							tablePrefix + "TRANS_MAIN.reference_num, " +
							tablePrefix + "TRANS_MAIN.net_amount, " +
							tablePrefix + "TRANS_MAIN.units, " +
							tablePrefix + "TRANS_MAIN.ppg, " +
							tablePrefix + "TRANS_MAIN.created_by, " +
							tablePrefix + "TRANS_MAIN.last_maintenance_userid, " +
							tablePrefix + "TRANS_MAIN.comment, " +
							tablePrefix + "TRANS_MAIN.invoice_comment " +
					"FROM " +
						tablePrefix + "TRANS_MAIN inner join " + tablePrefix + "FULL_ACCOUNT ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "FULL_ACCOUNT.account_num " +
						"inner join " + tablePrefix + "ACCOUNTS ON " +
							tablePrefix + "TRANS_MAIN.account_num = " + tablePrefix + "ACCOUNTS.account_num "+
						"inner join " + tablePrefix + "POST_CODE ON " +
							tablePrefix + "TRANS_MAIN.posting_code = " + tablePrefix + "POST_CODE.posting_code " +
						"inner join " + tablePrefix + "TYPE_INFO ON " +
							tablePrefix + "ACCOUNTS.type = " + tablePrefix + " TYPE_INFO.type and " +
							tablePrefix + "ACCOUNTS.division = " + tablePrefix + " TYPE_INFO.division ";

			String queryWhere = "WHERE "+
					tablePrefix + "TRANS_MAIN.posting_code <= " + PostingCode.max + " and " +
					tablePrefix + "TRANS_MAIN.status = 'A' ";
			if (startDate != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.posting_date >= '" + startDate + "' ";
			if (endDate != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.posting_date <=  '" + endDate + "' ";
			if (acctNum != null)
				queryWhere += " and " + tablePrefix + "FULL_ACCOUNT.full_account = '" + acctNum + "' ";
			if (postCodes != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.posting_code in (" + postCodes + ") ";
			if (refNum != null)
				queryWhere += " and " + tablePrefix + "TRANS_MAIN.reference_num = " + refNum + " ";
			
			String querySuffix =
					"ORDER BY " +
						tablePrefix + "TRANS_MAIN.posting_date, " +
						tablePrefix + "TRANS_MAIN.batch_num, " +
						tablePrefix + "TRANS_MAIN.account_num";

			mainWindow.frmAddDataBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ResultSet results = stmt.executeQuery(queryPrefix + queryWhere + querySuffix);
			
			if (transDetail == null)
			{
				transDetail = new BatchTable();
				mainWindow.transTable.setAutoCreateRowSorter(true);
				mainWindow.transTable.setModel(transDetail);
			}
			mainWindow.transTotals.setText("");
			transDetail.newResults(results, mainWindow.transTable);
			mainWindow.transTotals.setText(
					"Total: "   + NumberFormat.getCurrencyInstance().format(transDetail.totalAmt) + " " +
					"Credits: " + NumberFormat.getCurrencyInstance().format(transDetail.credAmt)  + " " +
					"Debits: "  + NumberFormat.getCurrencyInstance().format(transDetail.debAmt)   + " " +
					"Count: "    + transDetail.rowCount
					);
			mainWindow.tabbedPane.setSelectedIndex(MainWindow.TRANS_TAB_INDEX);
			mainWindow.setExportButtonState();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow.frmAddDataBrowser, "Query failed:" + e, "Query Failed", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			mainWindow.frmAddDataBrowser.setCursor(Cursor.getDefaultCursor());
		}
	}
}
