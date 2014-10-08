package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import net.pladd.ADDBrowser.E3types.Account;
import net.pladd.ADDBrowser.E3types.Category;
import net.pladd.ADDBrowser.E3types.Division;
import net.pladd.ADDBrowser.E3types.DocType;
import net.pladd.ADDBrowser.E3types.LogCategory;
import net.pladd.ADDBrowser.E3types.LogType;
import net.pladd.ADDBrowser.E3types.PostingCode;
import net.pladd.ADDBrowser.E3types.Type;

public class MainWindow {
	// Top level structure
	protected JFrame frmAddDataBrowser;
	protected JTabbedPane tabbedPane;

	private JMenuItem mntmExport;
	protected static final String VersionStr = "v1.7";
	protected JTable logTable;
	protected JSplitPane documentsTab;
	protected JTable batchTable;
	protected JTable transTable;
	
	protected static final int ACCT_TAB_INDEX  = 0;
	protected static final int LOG_TAB_INDEX   = 1;
	protected static final int DOC_TAB_INDEX   = 2;
	protected static final int BATCH_TAB_INDEX = 3;
	protected static final int TRANS_TAB_INDEX = 4;
	
	protected static final int BILLING_TAB_INDEX = 0;
	protected static final int CONTACT_TAB_INDEX = 1;
	protected static final int TANK_TAB_INDEX    = 2;
	protected static final int SERVICE_TAB_INDEX = 3;

	// Dialogs
	private AboutDialog            aboutDialog = null;
	private ConnectDialog          connectDialog = null;
	private JFileChooser           tsvChooser = null;
	private JFileChooser           txtChooser = null;
	private LogQueryDialog         logQueryDlg = null;
	private DocQueryDialog         docQueryDialog = null;
	private BatchQueryDialog       batchQueryDialog = null;
	private TransactionQueryDialog transactionQueryDialog = null; 

	// Toolbar buttons
	private JButton btnExport;
	private JButton btnAccounts;
	private JButton btnLogs;
	private JButton btnDocuments;
	private JButton btnBatches;
	private JButton btnTransactions;

	// Account tab buttons
	protected JButton btnAcctSearch;
	protected JButton btnAcctClear;
	protected JButton btnAcctLogs;
	protected JButton btnAcctDocuments;
	protected JButton btnAcctTransactions;
	
	// Account tab fields
	protected JTextField accountNumber;
	protected JTextField name;
	protected JTextField title;
	protected JTextField firstName;
	protected JTextField middleInitial;
	protected JTextField lastName;
	protected JTextField nameSuffix;
	protected JTextField street1;
	protected JTextField street2;
	protected JTextField city;
	protected JTextField type;
	protected JTextField category;
	protected JTextField sortCode;
	protected JTextField telephone;
	protected JTextField email;
	protected JTextField division;
	protected JTextField state;
	protected JTextField zipCode;
	protected JTextField balance;
	private JButton btnDivision;
	private JButton btnCategory;
	private JButton btnType;

	protected JTable contactInfoTable;
	protected JTable tankInfoTable;
	protected JTable svcInfoTable;

	private Set<JTextField> accountQueryFields = new HashSet<JTextField>();

	private JPanel logsTab;
	private JTable logDetailsTable;
	protected JTextArea logNotes;
	protected JTextArea logResolveNotes;

	protected JTable docListTable;
	protected JTextArea docContent;
	private JButton btnExportDocContent;

	protected JLabel transTotals;
	protected JLabel batchTotals;
	private JTabbedPane accountInfoTabPane;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmAddDataBrowser = new JFrame();
		frmAddDataBrowser.setSize(new Dimension(800, 600));
		frmAddDataBrowser.setTitle("ADD Data Browser " + VersionStr);
		frmAddDataBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmAddDataBrowser.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);
		
		JMenuItem mntmConnect = new JMenuItem("Connect...");
		mntmConnect.setMnemonic(KeyEvent.VK_O);
		mntmConnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmConnect);
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doConnect();
			}
		});
		
		mntmExport = new JMenuItem("Export...");
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExport();
			}
		});
		mntmExport.setEnabled(false);
		mntmExport.setMnemonic(KeyEvent.VK_E);
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmExport);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setMnemonic(KeyEvent.VK_X);
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doExit();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (aboutDialog == null)
					aboutDialog = new AboutDialog();
				aboutDialog.setVisible(true);
			}
		});
		mntmAbout.setMnemonic(KeyEvent.VK_A);
		mnHelp.add(mntmAbout);
		
		JToolBar toolBar = new JToolBar();
		frmAddDataBrowser.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnOpen = new JButton("");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doConnect();
			}
		});
		btnOpen.setToolTipText("Open");
		btnOpen.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		toolBar.add(btnOpen);
		
		btnExport = new JButton("");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doExport();
			}
		});
		btnExport.setEnabled(false);
		btnExport.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnExport.setToolTipText("Export");
		toolBar.add(btnExport);

		toolBar.addSeparator();
		
		btnAccounts = new JButton("Accounts");
		btnAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(ACCT_TAB_INDEX);
				doClearAcct();
			}
		});
		btnAccounts.setEnabled(false);
		btnAccounts.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		btnAccounts.setToolTipText("Search Accounts");
		toolBar.add(btnAccounts);
		
		btnLogs = new JButton("Logs");
		btnLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogSearch();
			}
		});
		btnLogs.setEnabled(false);
		btnLogs.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnLogs);
		
		btnBatches = new JButton("Batches");
		btnBatches.setEnabled(false);
		btnBatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doBatchQuery();
			}
		});
		
		btnDocuments = new JButton("Documents");
		btnDocuments.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				doDocSearch();
			}
		});
		btnDocuments.setEnabled(false);
		btnDocuments.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnDocuments);
		btnBatches.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnBatches);
		
		btnTransactions = new JButton("Transactions");
		btnTransactions.setEnabled(false);
		btnTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doTransactionQuery();
			}
		});
		btnTransactions.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnTransactions);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent change) {
				setExportButtonState();
			}
		});
		frmAddDataBrowser.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel accountsTab = new JPanel();
		tabbedPane.addTab("Accounts", null, accountsTab, null);
		accountsTab.setLayout(new BorderLayout(0, 0));
		
		accountInfoTabPane = new JTabbedPane(JTabbedPane.TOP);
		accountInfoTabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent change) {
				setExportButtonState();
			}
		});
		accountsTab.add(accountInfoTabPane);
		
		JPanel billingInfoTab = new JPanel();
		accountInfoTabPane.addTab("Billing Info", null, billingInfoTab, null);
		accountInfoTabPane.setEnabledAt(0, true);
		GridBagLayout gbl_billingInfoTab = new GridBagLayout();
		gbl_billingInfoTab.columnWidths = new int[]{0, 0, 0};
		gbl_billingInfoTab.rowHeights = new int[]{15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_billingInfoTab.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_billingInfoTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		billingInfoTab.setLayout(gbl_billingInfoTab);
		
		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonPanel.gridwidth = 2;
		gbc_buttonPanel.insets = new Insets(0, 0, 5, 0);
		gbc_buttonPanel.gridx = 0;
		gbc_buttonPanel.gridy = 0;
		billingInfoTab.add(buttonPanel, gbc_buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnAcctSearch = new JButton("Search");
		btnAcctSearch.setMnemonic('S');
		btnAcctSearch.setEnabled(false);
		btnAcctSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAcctSearch();
			}
		});
		buttonPanel.add(btnAcctSearch);
		
		btnAcctClear = new JButton("Clear");
		btnAcctClear.setMnemonic('C');
		btnAcctClear.setEnabled(false);
		btnAcctClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doClearAcct();
			}
		});
		buttonPanel.add(btnAcctClear);
		
		btnAcctLogs = new JButton("Logs");
		btnAcctLogs.setMnemonic('o');
		btnAcctLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ADDBrowser.doLogSearch(accountNumber.getText(), null, null, null, null, null);
			}
		});
		btnAcctLogs.setEnabled(false);
		buttonPanel.add(btnAcctLogs);
		
		btnAcctDocuments = new JButton("Documents");
		btnAcctDocuments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				ADDBrowser.doDocSearch(accountNumber.getText(), null, null, null, null);
			}
		});
		btnAcctDocuments.setMnemonic('m');
		btnAcctDocuments.setEnabled(false);
		buttonPanel.add(btnAcctDocuments);
		
		btnAcctTransactions = new JButton("Transactions");
		btnAcctTransactions.setMnemonic('r');
		btnAcctTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ADDBrowser.doTransactionQuery(null, null, accountNumber.getText(), null, null);
			}
		});
		btnAcctTransactions.setEnabled(false);
		buttonPanel.add(btnAcctTransactions);
		
		JLabel lblAccountNumber = new JLabel("Account Number");
		GridBagConstraints gbc_lblAccountNumber = new GridBagConstraints();
		gbc_lblAccountNumber.anchor = GridBagConstraints.EAST;
		gbc_lblAccountNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccountNumber.gridx = 0;
		gbc_lblAccountNumber.gridy = 1;
		billingInfoTab.add(lblAccountNumber, gbc_lblAccountNumber);
		
		JPanel accountInfoPanel = new JPanel();
		GridBagConstraints gbc_accountInfoPanel = new GridBagConstraints();
		gbc_accountInfoPanel.fill = GridBagConstraints.BOTH;
		gbc_accountInfoPanel.insets = new Insets(0, 0, 5, 0);
		gbc_accountInfoPanel.gridx = 1;
		gbc_accountInfoPanel.gridy = 1;
		billingInfoTab.add(accountInfoPanel, gbc_accountInfoPanel);
		GridBagLayout gbl_accountInfoPanel = new GridBagLayout();
		gbl_accountInfoPanel.columnWidths = new int[]{86, 48, 54, 0};
		gbl_accountInfoPanel.rowHeights = new int[]{20, 0};
		gbl_accountInfoPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_accountInfoPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		accountInfoPanel.setLayout(gbl_accountInfoPanel);
		
		accountNumber = new JTextField();
		accountNumber.setName("full_account");
		GridBagConstraints gbc_accountNumber = new GridBagConstraints();
		gbc_accountNumber.anchor = GridBagConstraints.NORTHWEST;
		gbc_accountNumber.insets = new Insets(0, 0, 0, 5);
		gbc_accountNumber.gridx = 0;
		gbc_accountNumber.gridy = 0;
		accountInfoPanel.add(accountNumber, gbc_accountNumber);
		accountNumber.setColumns(10);
		
		JLabel lblSortCode = new JLabel("Sort Code");
		GridBagConstraints gbc_lblSortCode = new GridBagConstraints();
		gbc_lblSortCode.anchor = GridBagConstraints.WEST;
		gbc_lblSortCode.insets = new Insets(0, 0, 0, 5);
		gbc_lblSortCode.gridx = 1;
		gbc_lblSortCode.gridy = 0;
		accountInfoPanel.add(lblSortCode, gbc_lblSortCode);
		
		sortCode = new JTextField();
		sortCode.setName("sort_code");
		GridBagConstraints gbc_sortCode = new GridBagConstraints();
		gbc_sortCode.anchor = GridBagConstraints.NORTHWEST;
		gbc_sortCode.gridx = 2;
		gbc_sortCode.gridy = 0;
		accountInfoPanel.add(sortCode, gbc_sortCode);
		sortCode.setColumns(6);
		

		accountQueryFields.add(accountNumber);
		accountQueryFields.add(sortCode);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		billingInfoTab.add(lblName, gbc_lblName);
		
		name = new JTextField();
		GridBagConstraints gbc_name = new GridBagConstraints();
		gbc_name.anchor = GridBagConstraints.WEST;
		gbc_name.insets = new Insets(0, 0, 5, 0);
		gbc_name.gridx = 1;
		gbc_name.gridy = 2;
		billingInfoTab.add(name, gbc_name);
		name.setName("name");
		name.setColumns(40);
		accountQueryFields.add(name);
		
		JLabel lblTitleFn = new JLabel("Title / First Name / MI");
		GridBagConstraints gbc_lblTitleFn = new GridBagConstraints();
		gbc_lblTitleFn.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitleFn.gridx = 0;
		gbc_lblTitleFn.gridy = 3;
		billingInfoTab.add(lblTitleFn, gbc_lblTitleFn);
		
		JPanel nameComponentPanel1 = new JPanel();
		GridBagConstraints gbc_nameComponentPanel1 = new GridBagConstraints();
		gbc_nameComponentPanel1.fill = GridBagConstraints.BOTH;
		gbc_nameComponentPanel1.insets = new Insets(0, 0, 5, 0);
		gbc_nameComponentPanel1.gridx = 1;
		gbc_nameComponentPanel1.gridy = 3;
		billingInfoTab.add(nameComponentPanel1, gbc_nameComponentPanel1);
		GridBagLayout gbl_nameComponentPanel1 = new GridBagLayout();
		gbl_nameComponentPanel1.columnWidths = new int[]{0, 86, 0, 0, 0, 0};
		gbl_nameComponentPanel1.rowHeights = new int[]{0, 0};
		gbl_nameComponentPanel1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_nameComponentPanel1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		nameComponentPanel1.setLayout(gbl_nameComponentPanel1);
		
		title = new JTextField();
		title.setName("title");
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.insets = new Insets(0, 0, 0, 5);
		gbc_title.anchor = GridBagConstraints.NORTHWEST;
		gbc_title.gridx = 0;
		gbc_title.gridy = 0;
		nameComponentPanel1.add(title, gbc_title);
		title.setColumns(4);
		
		firstName = new JTextField();
		firstName.setName("first_name");
		GridBagConstraints gbc_firstName = new GridBagConstraints();
		gbc_firstName.insets = new Insets(0, 0, 0, 5);
		gbc_firstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstName.gridx = 1;
		gbc_firstName.gridy = 0;
		nameComponentPanel1.add(firstName, gbc_firstName);
		firstName.setColumns(40);
		
		middleInitial = new JTextField();
		middleInitial.setName("middle_initial");
		middleInitial.setColumns(1);
		GridBagConstraints gbc_middleInitial = new GridBagConstraints();
		gbc_middleInitial.insets = new Insets(0, 0, 0, 5);
		gbc_middleInitial.fill = GridBagConstraints.HORIZONTAL;
		gbc_middleInitial.gridx = 2;
		gbc_middleInitial.gridy = 0;
		nameComponentPanel1.add(middleInitial, gbc_middleInitial);
		accountQueryFields.add(title);
		accountQueryFields.add(firstName);
		accountQueryFields.add(middleInitial);

		JLabel lblLastName = new JLabel("Last Name / Suffix");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 4;
		billingInfoTab.add(lblLastName, gbc_lblLastName);
		
		JPanel nameComponentPanel2 = new JPanel();
		GridBagConstraints gbc_nameComponentPanel2 = new GridBagConstraints();
		gbc_nameComponentPanel2.anchor = GridBagConstraints.WEST;
		gbc_nameComponentPanel2.fill = GridBagConstraints.VERTICAL;
		gbc_nameComponentPanel2.insets = new Insets(0, 0, 5, 0);
		gbc_nameComponentPanel2.gridx = 1;
		gbc_nameComponentPanel2.gridy = 4;
		billingInfoTab.add(nameComponentPanel2, gbc_nameComponentPanel2);
		GridBagLayout gbl_nameComponentPanel2 = new GridBagLayout();
		gbl_nameComponentPanel2.columnWidths = new int[]{0, 0, 0, 0};
		gbl_nameComponentPanel2.rowHeights = new int[]{0, 0};
		gbl_nameComponentPanel2.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_nameComponentPanel2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		nameComponentPanel2.setLayout(gbl_nameComponentPanel2);
		
		lastName = new JTextField();
		lastName.setName("last_name");
		lastName.setColumns(40);
		GridBagConstraints gbc_lastName = new GridBagConstraints();
		gbc_lastName.insets = new Insets(0, 0, 0, 5);
		gbc_lastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastName.gridy = 0;
		gbc_lastName.gridx = 1;
		nameComponentPanel2.add(lastName, gbc_lastName);

		nameSuffix = new JTextField();
		GridBagConstraints gbc_nameSuffix = new GridBagConstraints();
		gbc_nameSuffix.gridy = 0;
		gbc_nameSuffix.gridx = 2;
		nameComponentPanel2.add(nameSuffix, gbc_nameSuffix);
		nameSuffix.setName("name_suffix");
		nameSuffix.setColumns(3);
		accountQueryFields.add(nameSuffix);
		accountQueryFields.add(lastName);
		accountQueryFields.add(nameSuffix);
		
		JLabel lblStreet1 = new JLabel("Street 1");
		GridBagConstraints gbc_lblStreet1 = new GridBagConstraints();
		gbc_lblStreet1.anchor = GridBagConstraints.EAST;
		gbc_lblStreet1.insets = new Insets(0, 0, 5, 5);
		gbc_lblStreet1.gridx = 0;
		gbc_lblStreet1.gridy = 5;
		billingInfoTab.add(lblStreet1, gbc_lblStreet1);
		
		street1 = new JTextField();
		GridBagConstraints gbc_street1 = new GridBagConstraints();
		gbc_street1.anchor = GridBagConstraints.WEST;
		gbc_street1.insets = new Insets(0, 0, 5, 0);
		gbc_street1.gridx = 1;
		gbc_street1.gridy = 5;
		billingInfoTab.add(street1, gbc_street1);
		street1.setName("street1");
		street1.setColumns(40);
		accountQueryFields.add(street1);
		
		JLabel lblStreet2 = new JLabel("Street 2");
		GridBagConstraints gbc_lblStreet2 = new GridBagConstraints();
		gbc_lblStreet2.anchor = GridBagConstraints.EAST;
		gbc_lblStreet2.insets = new Insets(0, 0, 5, 5);
		gbc_lblStreet2.gridx = 0;
		gbc_lblStreet2.gridy = 6;
		billingInfoTab.add(lblStreet2, gbc_lblStreet2);
		
		street2 = new JTextField();
		GridBagConstraints gbc_street2 = new GridBagConstraints();
		gbc_street2.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		gbc_street2.insets = new Insets(0, 0, 5, 0);
		gbc_street2.gridx = 1;
		gbc_street2.gridy = 6;
		billingInfoTab.add(street2, gbc_street2);
		street2.setName("street2");
		street2.setColumns(40);
		accountQueryFields.add(street2);
		
		JLabel lblCityStateZip = new JLabel("City / State / Zip Code");
		GridBagConstraints gbc_lblCityStateZip = new GridBagConstraints();
		gbc_lblCityStateZip.anchor = GridBagConstraints.EAST;
		gbc_lblCityStateZip.insets = new Insets(0, 0, 5, 5);
		gbc_lblCityStateZip.gridx = 0;
		gbc_lblCityStateZip.gridy = 7;
		billingInfoTab.add(lblCityStateZip, gbc_lblCityStateZip);
		
		JPanel cityStateZipPanel = new JPanel();
		GridBagConstraints gbc_cityStateZipPanel = new GridBagConstraints();
		gbc_cityStateZipPanel.fill = GridBagConstraints.BOTH;
		gbc_cityStateZipPanel.insets = new Insets(0, 0, 5, 0);
		gbc_cityStateZipPanel.gridx = 1;
		gbc_cityStateZipPanel.gridy = 7;
		billingInfoTab.add(cityStateZipPanel, gbc_cityStateZipPanel);
		GridBagLayout gbl_cityStateZipPanel = new GridBagLayout();
		gbl_cityStateZipPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_cityStateZipPanel.rowHeights = new int[]{0, 0};
		gbl_cityStateZipPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_cityStateZipPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		cityStateZipPanel.setLayout(gbl_cityStateZipPanel);
		
		city = new JTextField();
		city.setName("city");
		GridBagConstraints gbc_city = new GridBagConstraints();
		gbc_city.insets = new Insets(0, 0, 0, 5);
		gbc_city.anchor = GridBagConstraints.WEST;
		gbc_city.gridx = 0;
		gbc_city.gridy = 0;
		cityStateZipPanel.add(city, gbc_city);
		city.setColumns(38);
		
		state = new JTextField();
		state.setName("state");
		state.setColumns(4);
		GridBagConstraints gbc_state = new GridBagConstraints();
		gbc_state.anchor = GridBagConstraints.WEST;
		gbc_state.insets = new Insets(0, 0, 0, 5);
		gbc_state.gridx = 1;
		gbc_state.gridy = 0;
		cityStateZipPanel.add(state, gbc_state);
		
		zipCode = new JTextField();
		zipCode.setName("postal_code");
		zipCode.setColumns(10);
		GridBagConstraints gbc_zipCode = new GridBagConstraints();
		gbc_zipCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_zipCode.gridx = 2;
		gbc_zipCode.gridy = 0;
		cityStateZipPanel.add(zipCode, gbc_zipCode);
		accountQueryFields.add(city);
		accountQueryFields.add(state);
		accountQueryFields.add(zipCode);
		
		JLabel lblTelephone = new JLabel("Primary Phone");
		GridBagConstraints gbc_lblTelephone = new GridBagConstraints();
		gbc_lblTelephone.anchor = GridBagConstraints.EAST;
		gbc_lblTelephone.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelephone.gridx = 0;
		gbc_lblTelephone.gridy = 8;
		billingInfoTab.add(lblTelephone, gbc_lblTelephone);
		
		telephone = new JTextField();
		GridBagConstraints gbc_telephone = new GridBagConstraints();
		gbc_telephone.anchor = GridBagConstraints.WEST;
		gbc_telephone.insets = new Insets(0, 0, 5, 0);
		gbc_telephone.gridx = 1;
		gbc_telephone.gridy = 8;
		billingInfoTab.add(telephone, gbc_telephone);
		telephone.setColumns(10);
		telephone.setName("telephone");
		accountQueryFields.add(telephone);
		
		JLabel lblEmail = new JLabel("Primary Email");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 9;
		billingInfoTab.add(lblEmail, gbc_lblEmail);
		
		email = new JTextField();
		GridBagConstraints gbc_email = new GridBagConstraints();
		gbc_email.anchor = GridBagConstraints.WEST;
		gbc_email.insets = new Insets(0, 0, 5, 0);
		gbc_email.gridx = 1;
		gbc_email.gridy = 9;
		billingInfoTab.add(email, gbc_email);
		email.setColumns(40);
		email.setName("email");
		accountQueryFields.add(email);
		
		JLabel lblType = new JLabel("Type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 10;
		billingInfoTab.add(lblType, gbc_lblType);
		
		JPanel typePanel = new JPanel();
		GridBagConstraints gbc_typePanel = new GridBagConstraints();
		gbc_typePanel.fill = GridBagConstraints.BOTH;
		gbc_typePanel.insets = new Insets(0, 0, 5, 0);
		gbc_typePanel.gridx = 1;
		gbc_typePanel.gridy = 10;
		billingInfoTab.add(typePanel, gbc_typePanel);
		GridBagLayout gbl_typePanel = new GridBagLayout();
		gbl_typePanel.columnWidths = new int[]{0, 0, 0};
		gbl_typePanel.rowHeights = new int[]{0, 0};
		gbl_typePanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_typePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		typePanel.setLayout(gbl_typePanel);
		
		type = new JTextField();
		type.setName("type");
		type.setEditable(false);
		GridBagConstraints gbc_type = new GridBagConstraints();
		gbc_type.insets = new Insets(0, 0, 0, 5);
		gbc_type.anchor = GridBagConstraints.WEST;
		gbc_type.gridx = 0;
		gbc_type.gridy = 0;
		typePanel.add(type, gbc_type);
		type.setColumns(46);
		accountQueryFields.add(type);
		
		btnType = new JButton("...");
		btnType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTypeButton();
			}
		});
		btnType.setEnabled(false);
		GridBagConstraints gbc_typeButton = new GridBagConstraints();
		gbc_typeButton.gridx = 1;
		gbc_typeButton.gridy = 0;
		typePanel.add(btnType, gbc_typeButton);
		
		JLabel lblCategory = new JLabel("Category");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.anchor = GridBagConstraints.EAST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 11;
		billingInfoTab.add(lblCategory, gbc_lblCategory);
		
		JPanel categoryPanel = new JPanel();
		GridBagConstraints gbc_categoryPanel = new GridBagConstraints();
		gbc_categoryPanel.fill = GridBagConstraints.BOTH;
		gbc_categoryPanel.insets = new Insets(0, 0, 5, 0);
		gbc_categoryPanel.gridx = 1;
		gbc_categoryPanel.gridy = 11;
		billingInfoTab.add(categoryPanel, gbc_categoryPanel);
		GridBagLayout gbl_categoryPanel = new GridBagLayout();
		gbl_categoryPanel.columnWidths = new int[]{0, 0, 0};
		gbl_categoryPanel.rowHeights = new int[]{0, 0};
		gbl_categoryPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_categoryPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		categoryPanel.setLayout(gbl_categoryPanel);
		
		category = new JTextField();
		category.setName("category");
		category.setEditable(false);
		GridBagConstraints gbc_category = new GridBagConstraints();
		gbc_category.insets = new Insets(0, 0, 0, 5);
		gbc_category.anchor = GridBagConstraints.WEST;
		gbc_category.gridx = 0;
		gbc_category.gridy = 0;
		categoryPanel.add(category, gbc_category);
		category.setColumns(46);
		
		btnCategory = new JButton("...");
		btnCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCategoryButton();
			}
		});
		btnCategory.setEnabled(false);
		GridBagConstraints gbc_categoryButton = new GridBagConstraints();
		gbc_categoryButton.gridx = 1;
		gbc_categoryButton.gridy = 0;
		categoryPanel.add(btnCategory, gbc_categoryButton);
		accountQueryFields.add(category);
		
		JLabel lblDivison = new JLabel("Divison");
		GridBagConstraints gbc_lblDivison = new GridBagConstraints();
		gbc_lblDivison.anchor = GridBagConstraints.EAST;
		gbc_lblDivison.insets = new Insets(0, 0, 5, 5);
		gbc_lblDivison.gridx = 0;
		gbc_lblDivison.gridy = 12;
		billingInfoTab.add(lblDivison, gbc_lblDivison);
		
		JPanel divisionPanel = new JPanel();
		GridBagConstraints gbc_divisionPanel = new GridBagConstraints();
		gbc_divisionPanel.fill = GridBagConstraints.BOTH;
		gbc_divisionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_divisionPanel.gridx = 1;
		gbc_divisionPanel.gridy = 12;
		billingInfoTab.add(divisionPanel, gbc_divisionPanel);
		GridBagLayout gbl_divisionPanel = new GridBagLayout();
		gbl_divisionPanel.columnWidths = new int[]{0, 0, 0};
		gbl_divisionPanel.rowHeights = new int[]{0, 0};
		gbl_divisionPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_divisionPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		divisionPanel.setLayout(gbl_divisionPanel);
		
		division = new JTextField();
		division.setName("division");
		division.setEditable(false);
		GridBagConstraints gbc_division = new GridBagConstraints();
		gbc_division.insets = new Insets(0, 0, 0, 5);
		gbc_division.anchor = GridBagConstraints.WEST;
		gbc_division.gridx = 0;
		gbc_division.gridy = 0;
		divisionPanel.add(division, gbc_division);
		division.setColumns(47);
		
		btnDivision = new JButton("...");
		btnDivision.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doDivisionButton();
			}
		});
		btnDivision.setEnabled(false);
		GridBagConstraints gbc_divisionButton = new GridBagConstraints();
		gbc_divisionButton.gridx = 1;
		gbc_divisionButton.gridy = 0;
		divisionPanel.add(btnDivision, gbc_divisionButton);
		accountQueryFields.add(division);
		
		JLabel lblBalance = new JLabel("Balance $");
		GridBagConstraints gbc_lblBalance = new GridBagConstraints();
		gbc_lblBalance.anchor = GridBagConstraints.EAST;
		gbc_lblBalance.insets = new Insets(0, 0, 0, 5);
		gbc_lblBalance.gridx = 0;
		gbc_lblBalance.gridy = 13;
		billingInfoTab.add(lblBalance, gbc_lblBalance);
		
		balance = new JTextField();
		GridBagConstraints gbc_balance = new GridBagConstraints();
		gbc_balance.anchor = GridBagConstraints.SOUTHWEST;
		gbc_balance.gridx = 1;
		gbc_balance.gridy = 13;
		billingInfoTab.add(balance, gbc_balance);
		balance.setName("balance");
		balance.setEditable(false);
		balance.setColumns(20);
		accountQueryFields.add(balance);
		
		JPanel contactInfoTab = new JPanel();
		accountInfoTabPane.addTab("Contact Info", null, contactInfoTab, null);
		accountInfoTabPane.setEnabledAt(1, true);
		GridBagLayout gbl_contactInfoTab = new GridBagLayout();
		gbl_contactInfoTab.columnWidths = new int[]{0, 0};
		gbl_contactInfoTab.rowHeights = new int[]{0, 0};
		gbl_contactInfoTab.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contactInfoTab.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contactInfoTab.setLayout(gbl_contactInfoTab);
		
		JScrollPane contactScrollPane = new JScrollPane();
		GridBagConstraints gbc_contactScrollPane = new GridBagConstraints();
		gbc_contactScrollPane.fill = GridBagConstraints.BOTH;
		gbc_contactScrollPane.gridx = 0;
		gbc_contactScrollPane.gridy = 0;
		contactInfoTab.add(contactScrollPane, gbc_contactScrollPane);
		
		contactInfoTable = new JTable();
		contactScrollPane.setViewportView(contactInfoTable);
		
		JPanel tankInfoTab = new JPanel();
		accountInfoTabPane.addTab("Tank Info", null, tankInfoTab, null);
		accountInfoTabPane.setEnabledAt(2, true);
		tankInfoTab.setLayout(new BorderLayout(0, 0));
		
		JSplitPane tankSplit = new JSplitPane();
		tankSplit.setResizeWeight(0.5);
		tankSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tankInfoTab.add(tankSplit);
		
		JScrollPane tankInfoScroll = new JScrollPane();
		tankSplit.setLeftComponent(tankInfoScroll);

		tankInfoTable = new JTable();
		tankInfoScroll.setViewportView(tankInfoTable);

		JPanel tankDetailPanel = new JPanel();
		tankSplit.setRightComponent(tankDetailPanel);
		
		JPanel serviceInfoTab = new JPanel();
		accountInfoTabPane.addTab("Service Info", null, serviceInfoTab, null);
		serviceInfoTab.setLayout(new BorderLayout(0, 0));
		
		JSplitPane serviceSplit = new JSplitPane();
		serviceSplit.setResizeWeight(0.5);
		serviceSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		serviceInfoTab.add(serviceSplit);
		
		JScrollPane serviceInfoScroll = new JScrollPane();
		serviceSplit.setLeftComponent(serviceInfoScroll);

		svcInfoTable = new JTable();
		serviceInfoScroll.setViewportView(svcInfoTable);
		
		JPanel serviceDetailPanel = new JPanel();
		serviceSplit.setRightComponent(serviceDetailPanel);

		
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_A);
		
		logsTab = new JPanel();
		tabbedPane.addTab("Logs", null, logsTab, null);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_L);
		
		GridBagLayout gbl_logsTab = new GridBagLayout();
		gbl_logsTab.columnWidths = new int[]{779, 0};
		gbl_logsTab.rowHeights = new int[] {200, 200, 0};
		gbl_logsTab.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_logsTab.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		logsTab.setLayout(gbl_logsTab);
		
		JScrollPane logTableScrollPane = new JScrollPane();
		GridBagConstraints gbc_logTableScrollPane = new GridBagConstraints();
		gbc_logTableScrollPane.fill = GridBagConstraints.BOTH;
		gbc_logTableScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_logTableScrollPane.gridx = 0;
		gbc_logTableScrollPane.gridy = 0;
		logsTab.add(logTableScrollPane, gbc_logTableScrollPane);
		
		logTable = new JTable();
		logTableScrollPane.setViewportView(logTable);
		logTable.setFillsViewportHeight(true);
		logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		logTable.setRowSelectionAllowed(true);
		logTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		logTable.getSelectionModel().addListSelectionListener(new LogListener());
		
		JSplitPane logSplitPane = new JSplitPane();
		logSplitPane.setResizeWeight(0.3);
		GridBagConstraints gbc_logSplitPane = new GridBagConstraints();
		gbc_logSplitPane.anchor = GridBagConstraints.SOUTH;
		gbc_logSplitPane.fill = GridBagConstraints.BOTH;
		gbc_logSplitPane.gridx = 0;
		gbc_logSplitPane.gridy = 1;
		logsTab.add(logSplitPane, gbc_logSplitPane);
		
		logDetailsTable = new JTable();
		logDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		logDetailsTable.setFillsViewportHeight(true);
	
		JScrollPane logDetailsScrollPane = new JScrollPane();
		logDetailsScrollPane.setViewportView(logDetailsTable);
		logDetailsTable.setFillsViewportHeight(true);

		logSplitPane.setRightComponent(logDetailsScrollPane);
		
		JSplitPane logVertSplitPane = new JSplitPane();
		logVertSplitPane.setResizeWeight(0.5);
		logVertSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		logSplitPane.setLeftComponent(logVertSplitPane);
		
		
		logNotes = new JTextArea();
		logNotes.setWrapStyleWord(true);
		logNotes.setLineWrap(true);
		logNotes.setEditable(false);

		JScrollPane notesScrollPane = new JScrollPane(logNotes);
		logVertSplitPane.setLeftComponent(notesScrollPane);
		
		logResolveNotes = new JTextArea();
		logResolveNotes.setWrapStyleWord(true);
		logResolveNotes.setLineWrap(true);
		logResolveNotes.setEditable(false);

		JScrollPane resolveScrollPane = new JScrollPane(logResolveNotes);
		logVertSplitPane.setRightComponent(resolveScrollPane);

		documentsTab = new JSplitPane();
		documentsTab.setResizeWeight(0.5);
		documentsTab.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Documents", null, documentsTab, null);
		
		JScrollPane docListPane = new JScrollPane();
		documentsTab.setLeftComponent(docListPane);
		
		docListTable = new JTable();
		docListTable.getSelectionModel().addListSelectionListener(new DocListener());
		docListPane.setViewportView(docListTable);
		JPanel docBottomHalf = new JPanel();
		documentsTab.setRightComponent(docBottomHalf);
		GridBagLayout gbl_docBottomHalf = new GridBagLayout();
		gbl_docBottomHalf.columnWidths = new int[]{385, 0};
		gbl_docBottomHalf.rowHeights = new int[]{24, 0, 0};
		gbl_docBottomHalf.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_docBottomHalf.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		docBottomHalf.setLayout(gbl_docBottomHalf);
		
		JScrollPane docContentPane = new JScrollPane();
		
		GridBagConstraints gbc_docContentPane = new GridBagConstraints();
		gbc_docContentPane.insets = new Insets(0, 0, 5, 0);
		gbc_docContentPane.fill = GridBagConstraints.BOTH;
		gbc_docContentPane.gridx = 0;
		gbc_docContentPane.gridy = 0;
		docBottomHalf.add(docContentPane, gbc_docContentPane);
		
		docContent = new JTextArea();
		docContent.setFont(new Font("Monospaced", Font.PLAIN, 11));
		docContent.setEditable(false);
		DefaultCaret caret = (DefaultCaret) docContent.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		docContentPane.setViewportView(docContent);
		
		btnExportDocContent = new JButton("Export...");
		btnExportDocContent.setMnemonic('E');
		btnExportDocContent.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doExportDocContent();
			}
		});
		GridBagConstraints gbc_btnExportDocContent = new GridBagConstraints();
		gbc_btnExportDocContent.gridx = 0;
		gbc_btnExportDocContent.gridy = 1;
		docBottomHalf.add(btnExportDocContent, gbc_btnExportDocContent);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_D);
		
		batchTable = new JTable();
		batchTable.setRowSelectionAllowed(false);
		batchTable.setFillsViewportHeight(true);
		batchTable.addMouseListener(new TransactionMouseAdapter());

		JScrollPane batchPane = new JScrollPane(batchTable);

		JPanel batchSummaryTab = new JPanel();
		batchSummaryTab.setLayout(new BorderLayout(0, 0));
		
		JPanel batchTotalsPanel = new JPanel();
		batchSummaryTab.add(batchTotalsPanel, BorderLayout.NORTH);
		
		batchTotals = new JLabel(" ");
		batchTotalsPanel.add(batchTotals);
		batchSummaryTab.add(batchPane);
		
		tabbedPane.addTab("Batches", batchSummaryTab);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_B);

		JPanel transSummaryTab = new JPanel();
		transSummaryTab.setLayout(new BorderLayout(0, 0));
		
		tabbedPane.addTab("Transactions", null, transSummaryTab, null);
		
		transTable = new JTable();
		transTable.setRowSelectionAllowed(false);
		transTable.setFillsViewportHeight(true);
		transTable.addMouseListener(new TransactionMouseAdapter());

		JPanel transTotalsPane = new JPanel();
		transSummaryTab.add(transTotalsPane, BorderLayout.NORTH);

		transTotals = new JLabel(" ");
		transTotalsPane.add(transTotals);

		JScrollPane transPane = new JScrollPane(transTable);
		transSummaryTab.add(transPane);
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_T);
	}

	private class TransactionMouseAdapter extends MouseAdapter 
	{
		public void mousePressed(MouseEvent evt)
		{
			if (evt.getClickCount() == 2)
			{
				JTable table = (JTable)evt.getSource();
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (col == 3)
				{
					ADDBrowser.doBatchQuery(
						ADDBrowser.df.format(table.getValueAt(row, 1)), 
						table.getValueAt(row, 3).toString());
					tabbedPane.setSelectedIndex(BATCH_TAB_INDEX);
				}
				if (col == 4)
				{
					Map<String, String> acctQuery = new HashMap<String, String>();
					acctQuery.put("full_account", table.getValueAt(row, 4).toString());
					ADDBrowser.doAcctSearch(acctQuery);
					tabbedPane.setSelectedIndex(ACCT_TAB_INDEX);
				}
			}
		}
	}

	public JTable getSelectedTable()
	{
		switch (tabbedPane.getSelectedIndex())
		{
		case ACCT_TAB_INDEX:
			if (accountInfoTabPane != null)
				switch (accountInfoTabPane.getSelectedIndex())
				{
				case CONTACT_TAB_INDEX: return contactInfoTable;
				case TANK_TAB_INDEX   : return tankInfoTable;
				}
			break;
		case LOG_TAB_INDEX:   return logTable;
		case DOC_TAB_INDEX:   return docListTable;
		case BATCH_TAB_INDEX: return batchTable;
		case TRANS_TAB_INDEX: return transTable;
		}
		return null;
	}

	public void enableQueries(boolean b)
		{
			btnAccounts.setEnabled(b);
			btnLogs.setEnabled(b);
			btnDocuments.setEnabled(b);
			btnBatches.setEnabled(b);
			btnTransactions.setEnabled(b);
	
			btnAcctSearch.setEnabled(b);
			btnAcctClear.setEnabled(b);
			btnAcctLogs.setEnabled(b);
			btnAcctDocuments.setEnabled(b);
			btnAcctTransactions.setEnabled(b);
			
			btnDivision.setEnabled(b);
			btnType.setEnabled(b);
			btnCategory.setEnabled(b);
			
			for (JTextField fld : accountQueryFields)
			{
				fld.setEnabled(b);
			}
		}

	void doConnect()
	{
		if (connectDialog == null)
			connectDialog = new ConnectDialog();
		connectDialog.OKpressed = false;
		connectDialog.setVisible(true);
		if (connectDialog.OKpressed)
		{
			try {
				ADDBrowser.doConnect((String)(connectDialog.jdbcDriver.getSelectedItem()),
						connectDialog.serverName.getText(),
						connectDialog.serverPort.getText(),
						connectDialog.databaseName.getText(),
						connectDialog.userName.getText(),
						new String(connectDialog.password.getPassword()),
						Integer.parseInt(connectDialog.maxDebitPostingCode.getText()),
						Integer.parseInt(connectDialog.maxPostingCode.getText()),
						connectDialog.invalidPClabel.getText(),
						connectDialog.chckbxTypesUniform.isSelected());
				enableQueries(true);
			}
			catch (Exception e)
			{
				enableQueries(false);
			}
			doClearAcct();
		}
		connectDialog.setVisible(false);
	}

	public void newCodes(Map<Integer, PostingCode> postingCodes, Map<String, LogCategory> logCategories, Map<Integer, LogType> logTypes, Map<Integer, DocType> docTypes)
	{
		if (logQueryDlg != null)
		{
			logQueryDlg.newCategories(logCategories.values().toArray(new LogCategory[1]));
			logQueryDlg.newTypes(logTypes.values().toArray(new LogType[1]));
		}
		if (docQueryDialog != null)
			docQueryDialog.newTypes(docTypes.values().toArray(new DocType[1]));
		if (transactionQueryDialog != null)
			transactionQueryDialog.newPostingCodes(postingCodes.keySet());
	}

	private void doExport()
	{
		if (tsvChooser == null)
		{
			tsvChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Tab Separated Values", "tsv");
			tsvChooser.setFileFilter(filter);
		}
		
		int retval = tsvChooser.showSaveDialog(frmAddDataBrowser);
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				JTable selected = getSelectedTable();
				if (selected == null)
					return;
				File f = tsvChooser.getSelectedFile();
				if (!f.getName().endsWith(".tsv"))
					f = new File(f.getAbsolutePath()+".tsv");
				FileWriter out = new FileWriter(f);
				if (selected == batchTable || selected == transTable)
				{
					BatchTable tbl = (BatchTable)selected.getModel();
					tbl.doExport(out);
				}
				else if (selected == logTable)
				{
					LogTable tbl = (LogTable)selected.getModel();
					tbl.doExport(out);
				}
				else if (selected == contactInfoTable)
				{
					ContactTable tbl = (ContactTable)selected.getModel();
					tbl.doExport(out);
				}
				else if (selected == tankInfoTable)
				{
					TankTable tbl = (TankTable)selected.getModel();
					tbl.doExport(out);
				} 
			} 
			catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frmAddDataBrowser, "File write failed:" + e, "File write failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	protected void doExportDocContent()
	{
		if (txtChooser == null)
		{
			txtChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
			txtChooser.setFileFilter(filter);
		}

		if (docListTable != null && docListTable.getSelectedRowCount() == 1)
		{
			int row = docListTable.getSelectedRow();
			row = docListTable.convertRowIndexToModel(row);
			DocTable dt = (DocTable)docListTable.getModel();
			String fn = "" + dt.fullAccounts.get(row);
			if (dt.tankNums.get(row) != 0)
				fn += "-" + dt.tankNums.get(row);
			fn += " - " + ADDBrowser.df.format(dt.dates.get(row)) + " " + dt.docTypes.get(row);
			txtChooser.setSelectedFile(new File(fn));
		}

		int retval = txtChooser.showSaveDialog(frmAddDataBrowser);
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File f = tsvChooser.getSelectedFile();
				if (!f.getName().endsWith(".txt"))
					f = new File(f.getAbsolutePath()+".txt");
				FileWriter out = new FileWriter(f);
				out.write(docContent.getText());
				out.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frmAddDataBrowser, "File write failed:" + e, "File write failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void setExportButtonState()
	{
		JTable selected = getSelectedTable();
		if (selected == null)
			btnExport.setEnabled(false);
		else if (selected.getModel().getRowCount() > 0)
		{
			btnExport.setEnabled(true);
			mntmExport.setEnabled(true);
		}
		else
		{
			btnExport.setEnabled(false);
			mntmExport.setEnabled(false);
		}
	}

	protected void doAcctSearch()
	{
		Map<String, String> acctQuery = new HashMap<String, String>();
		
		for (JTextField fld : accountQueryFields)
		{
			String val  = fld.getText();
			if (val != null && val.length() != 0)
			{
				acctQuery.put(fld.getName(), val);
			}
		}
		
		if (acctQuery.size() == 0)
		{
			JOptionPane.showMessageDialog(frmAddDataBrowser, "No query specified", "Account Query error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ADDBrowser.doAcctSearch(acctQuery);
	}

	public void accountResults(List<Account> accounts)
	{
		Account toDisplay = null;
		if (accounts.size() == 1)
			toDisplay = (Account)accounts.toArray()[0];
		else
		{
			toDisplay = (Account)JOptionPane.showInputDialog(this.frmAddDataBrowser, "Select an account", "Multiple Accounts Matched",
					JOptionPane.PLAIN_MESSAGE, null, accounts.toArray(), null);
		}
	
		if (toDisplay == null)
			return;
	
		accountNumber.setText(toDisplay.full_account);
		sortCode.setText(toDisplay.sort_code);
		name.setText(toDisplay.name);
		title.setText(toDisplay.title);
		firstName.setText(toDisplay.first_name);
		middleInitial.setText(toDisplay.middle_initial);
		lastName.setText(toDisplay.last_name);
		nameSuffix.setText(toDisplay.name_suffix);
		street1.setText(toDisplay.street1);
		street2.setText(toDisplay.street2);
		city.setText(toDisplay.city);
		state.setText(toDisplay.state);
		zipCode.setText(toDisplay.postal_code);
		telephone.setText(ADDBrowser.getAcctPrimary(toDisplay, 1));
		email.setText(ADDBrowser.getAcctPrimary(toDisplay, 3));
		division.setText(toDisplay.division.toString());
		category.setText(toDisplay.category.toString());
		type.setText(toDisplay.type.toString());
		NumberFormat nf2 = NumberFormat.getNumberInstance();
		nf2.setMaximumFractionDigits(2);
		nf2.setMinimumFractionDigits(2);
		balance.setText(nf2.format(toDisplay.balance));
		
		ADDBrowser.getAcctContactInfo(toDisplay);
		ADDBrowser.getTankInfo(toDisplay);
		ADDBrowser.getSvcInfo(toDisplay);
	}

	protected void doClearAcct()
	{
		for (JTextField field : accountQueryFields)
		{
			field.setText("");
		}
	}

	protected void doDivisionButton()
	{
		MultiSelectDialog<Division> dlg = 
				new MultiSelectDialog<Division>((Window)frmAddDataBrowser, "Select a division", 
						ADDBrowser.divisions.values().toArray(new Division[64]));
		List<Division> selectedDivisions = dlg.doSelect();
		if (selectedDivisions == null)
			return;
		
		String value = null;
		for (Division div : selectedDivisions)
		{
			if (value == null)
				value = "";
			else
				value += ", ";
			value += "" + div.division;
		}
		
		division.setText(value);
	}

	protected void doCategoryButton()
	{
		MultiSelectDialog<Category> dlg = 
				new MultiSelectDialog<Category>((Window)frmAddDataBrowser, "Select a category",
						ADDBrowser.categories.values().toArray(new Category[64]));
		List<Category> selectedCategories = dlg.doSelect();
		if (selectedCategories == null)
			return;

		String value = null;
		for (Category cat : selectedCategories)
		{
			if (value == null)
				value = "";
			else
				value += ", ";
			value += "" + cat.category;
		}
		
		category.setText(value);
	}

	protected void doTypeButton()
	{
		MultiSelectDialog<Type> dlg = 
				new MultiSelectDialog<Type>((Window)frmAddDataBrowser, "Select a type",
						ADDBrowser.types.values().toArray(new Type[64*64]));
		List<Type> selectedTypes = dlg.doSelect();
		if (selectedTypes == null)
			return;

		if (Type.typesUniform == false)
		{
			int div = -1;
			for (Type type : selectedTypes)
			{
				if (div == -1)
					div = type.division;
				else if (div != type.division)
				{
					JOptionPane.showMessageDialog(frmAddDataBrowser, "Sorry, can't select types from different divisions unless types are uniform."
							+ "  See the checkbox on the Connect Dialog",
							"Type selection error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			division.setText("" + div);
		}

		String value = null;
		for (Type type : selectedTypes)
		{
			if (value == null)
				value = "";
			else
				value += ", ";
			value += "" + type.type;
		}
		
		type.setText(value);
	}

	
	protected void doLogSearch()
	{
		if (logQueryDlg == null)
			logQueryDlg = new LogQueryDialog(ADDBrowser.logCategories.values(), ADDBrowser.logTypes.values());
		logQueryDlg.setVisible(true);
		if (logQueryDlg.OKpressed == true)
		{
			ADDBrowser.doLogSearch(
					logQueryDlg.accountNum.isEnabled()    ? logQueryDlg.accountNum.getText() : null,
					logQueryDlg.startDate.isEnabled()     ? logQueryDlg.startDate.getText()  : null,
					logQueryDlg.endDate.isEnabled()       ? logQueryDlg.endDate.getText()    : null,
					logQueryDlg.logCategories.isEnabled() ? logQueryDlg.logCategories.getSelectedValuesList() : null,
					logQueryDlg.logTypes.isEnabled()      ? logQueryDlg.logTypes.getSelectedValuesList() : null,
					logQueryDlg.logMessage.isEnabled()    ? logQueryDlg.logMessage.getText() : null);
		}
	}

	private class LogListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			int r = logTable.getSelectedRowCount();
			if (r == 0)
			{
				logResolveNotes.setText("");
			}
			else if (r == 1)
			{
				int row = logTable.getSelectedRow();
				row = logTable.convertRowIndexToModel(row);
				LogTable lt = (LogTable)logTable.getModel();
				logNotes.setText(lt.notes.get(row));
				String foo = lt.resolvedNotes.get(row);
				logResolveNotes.setText(foo);
				ADDBrowser.doLogDetail(lt.acctNums.get(row), lt.log_hdr_ids.get(row), lt.log_tmpl_hdr_ids.get(row));
			}
			else
			{
				// whoa! should only be single select
				logResolveNotes.setText("Multiple logs selected...");
			}
		}
	}

	private class DocListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			int r = docListTable.getSelectedRowCount();
			if (r == 0)
			{
				docContent.setText("");
			}
			else if (r == 1)
			{
				int row = docListTable.getSelectedRow();
				row = docListTable.convertRowIndexToModel(row);
				DocTable dt = (DocTable)docListTable.getModel();
				ADDBrowser.doDocContents(dt.docIDs.get(row));
			}
			else
			{
				// whoa! should only be single select
				docContent.setText("Multiple logs selected...");
			}
		}
	}

	
	public void newLogDetail(ResultSet results) throws SQLException
	{
		LogDetailTable dt = new LogDetailTable();
		dt.newResults(results);
		logDetailsTable.setModel(dt);
	}

	protected void doDocSearch()
	{
		if (docQueryDialog == null)
			docQueryDialog = new DocQueryDialog(ADDBrowser.docTypes.values());
		
		docQueryDialog.OKpressed = false;
		docQueryDialog.setVisible(true);
		if (docQueryDialog.OKpressed)
		{
			String acctNum   = null;
			String refNum    = null;
			String startDate = null;
			String endDate   = null;
			String types     = null;
			
			if (docQueryDialog.chckbxAccountNumber.isSelected())
				acctNum = docQueryDialog.accountNum.getText();
			if (docQueryDialog.chckbxReferenceNumber.isSelected())
				refNum = docQueryDialog.referenceNumber.getText();
			if (docQueryDialog.chckbxStartDate.isSelected())
				startDate = docQueryDialog.startDate.getText();
			if (docQueryDialog.chckbxEndDate.isSelected())
				endDate = docQueryDialog.endDate.getText();
			if (docQueryDialog.chckbxDocType.isSelected())
			{
				for (DocType dt : docQueryDialog.docTypes.getSelectedValuesList())
				{
					if (types == null)
						types = "" + dt.typeID;
					else
						types += ", " + dt.typeID;
				}
			}
			
			if (acctNum == null && refNum == null &&
				startDate == null && endDate == null && types == null)
			{
				JOptionPane.showMessageDialog(frmAddDataBrowser, "No query specified", "Document Query error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ADDBrowser.doDocSearch(acctNum, refNum, startDate, endDate, types);
		}
	}

	protected void doBatchQuery()
	{
		if (batchQueryDialog == null)
			batchQueryDialog = new BatchQueryDialog();
		batchQueryDialog.OKpressed = false;
		batchQueryDialog.setVisible(true);
		if (batchQueryDialog.OKpressed)
		{
			String postingDate = null;
			String batchNumber = null;
			
			if (batchQueryDialog.chkPostingDate.isSelected())
				postingDate = batchQueryDialog.postingDate.getText();
			
			if (batchQueryDialog.chkBatchNumber.isSelected())
				batchNumber = batchQueryDialog.batchNumber.getText();
			
			if (postingDate == null && batchNumber == null)
			{
				JOptionPane.showMessageDialog(frmAddDataBrowser, "No query specified", "Batch Query error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ADDBrowser.doBatchQuery(postingDate, batchNumber);
		}
	}

	
	protected void doTransactionQuery()
	{
		if (transactionQueryDialog == null)
			transactionQueryDialog = new TransactionQueryDialog(ADDBrowser.postingCodes);
		transactionQueryDialog.OKpressed = false;
		transactionQueryDialog.setVisible(true);
		if (transactionQueryDialog.OKpressed)
		{
			String startDate = null;
			String endDate   = null;
			String acctNum   = null;
			String postCodes = null;
			String refNum    = null;
			
			if (transactionQueryDialog.chckbxStartDate.isSelected())
				startDate = transactionQueryDialog.startDate.getText();

			if (transactionQueryDialog.chckbxEndDate.isSelected())
				endDate = transactionQueryDialog.endDate.getText();
			
			if (transactionQueryDialog.chckbxAccountNumber.isSelected())
				acctNum = transactionQueryDialog.accountNum.getText();

			if (transactionQueryDialog.chckbxPostingCodes.isSelected())
			{
				for (PostingCode pc : transactionQueryDialog.postingCodes.getSelectedValuesList())
				{
					if (postCodes == null)
						postCodes = "" + pc.postingCode;
					else
						postCodes += ", " + pc.postingCode;
				}
			}
			
			if (transactionQueryDialog.chckbxReferenceNum.isSelected())
				refNum = transactionQueryDialog.referenceNumber.getText();
			
			if (startDate == null && endDate == null && acctNum == null && postCodes == null && refNum == null)
			{
				JOptionPane.showMessageDialog(frmAddDataBrowser, "No query specified", "Transaction Query error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ADDBrowser.doTransactionQuery(startDate, endDate, acctNum, postCodes, refNum);
		}
	}

	
	private void doExit()
	{
		System.exit(0);
	}
}
