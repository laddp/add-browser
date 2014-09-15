package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow {
	// Top level structure
	protected JFrame frmAddDataBrowser;
	protected JTabbedPane tabbedPane;

	protected JPanel accountsTab;
	protected JTable logsTable;
	protected JTable documentsTable;
	protected JTable batchTable;
	protected JTable transactionsTable;
	
	protected static final int ACCT_TAB_INDEX  = 0;
	protected static final int LOG_TAB_INDEX   = 1;
	protected static final int DOC_TAB_INDEX   = 2;
	protected static final int BATCH_TAB_INDEX = 3;
	protected static final int TRANS_TAB_INDEX = 4;

	// Dialogs
	private ConnectDialog connectDialog = null;
	private TransactionQueryDialog transactionQueryDialog = null; 
	private BatchQueryDialog batchQueryDialog = null;
	private JFileChooser chooser = null;

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
	protected JTextField address1;
	protected JTextField address2;
	protected JTextField city;
	protected JTextField type;
	protected JTextField category;
	protected JTextField sortCode;
	protected JTextField telephone;
	protected JTextField email;
	protected JTextField title;
	protected JTextField firstName;
	protected JTextField middleInitial;
	protected JTextField lastName;
	protected JTextField nameSuffix;
	protected JTextField division;
	private JTextField state;
	private JTextField zipCode;

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
		frmAddDataBrowser.setSize(new Dimension(1200, 600));
		frmAddDataBrowser.setTitle("ADD Data Browser v1.2");
		frmAddDataBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmAddDataBrowser.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
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
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doExit();
			}
		});
		
		JMenuItem mntmExport = new JMenuItem("Export...");
		mntmExport.setMnemonic(KeyEvent.VK_E);
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmExport);
		mntmExit.setMnemonic(KeyEvent.VK_X);
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
		
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
		
		accountsTab = new JPanel();
		tabbedPane.addTab("Accounts", null, accountsTab, null);
		GridBagLayout gbl_accountsTab = new GridBagLayout();
		gbl_accountsTab.columnWidths = new int[]{0, 0, 0};
		gbl_accountsTab.rowHeights = new int[]{15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_accountsTab.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_accountsTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		accountsTab.setLayout(gbl_accountsTab);
		
		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonPanel.gridwidth = 2;
		gbc_buttonPanel.insets = new Insets(0, 0, 5, 0);
		gbc_buttonPanel.gridx = 0;
		gbc_buttonPanel.gridy = 0;
		accountsTab.add(buttonPanel, gbc_buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnAcctSearch = new JButton("Search");
		btnAcctSearch.setEnabled(false);
		btnAcctSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ADDBrowser.doSearch();
			}
		});
		buttonPanel.add(btnAcctSearch);
		
		btnAcctClear = new JButton("Clear");
		btnAcctClear.setEnabled(false);
		btnAcctClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doClearAcct();
			}
		});
		buttonPanel.add(btnAcctClear);
		
		btnAcctTransactions = new JButton("Transactions");
		btnAcctTransactions.setEnabled(false);
		buttonPanel.add(btnAcctTransactions);
		
		btnAcctLogs = new JButton("Logs");
		btnAcctLogs.setEnabled(false);
		buttonPanel.add(btnAcctLogs);
		
		btnAcctDocuments = new JButton("Documents");
		btnAcctDocuments.setEnabled(false);
		buttonPanel.add(btnAcctDocuments);
		
		JLabel lblAccountNumber = new JLabel("Account Number");
		GridBagConstraints gbc_lblAccountNumber = new GridBagConstraints();
		gbc_lblAccountNumber.anchor = GridBagConstraints.EAST;
		gbc_lblAccountNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccountNumber.gridx = 0;
		gbc_lblAccountNumber.gridy = 1;
		accountsTab.add(lblAccountNumber, gbc_lblAccountNumber);
		
		JPanel accountInfoPanel = new JPanel();
		GridBagConstraints gbc_accountInfoPanel = new GridBagConstraints();
		gbc_accountInfoPanel.insets = new Insets(0, 0, 5, 0);
		gbc_accountInfoPanel.fill = GridBagConstraints.BOTH;
		gbc_accountInfoPanel.gridx = 1;
		gbc_accountInfoPanel.gridy = 1;
		accountsTab.add(accountInfoPanel, gbc_accountInfoPanel);
		GridBagLayout gbl_accountInfoPanel = new GridBagLayout();
		gbl_accountInfoPanel.columnWidths = new int[]{86, 48, 54, 0};
		gbl_accountInfoPanel.rowHeights = new int[]{20, 0};
		gbl_accountInfoPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_accountInfoPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		accountInfoPanel.setLayout(gbl_accountInfoPanel);
		
		accountNumber = new JTextField();
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
		GridBagConstraints gbc_sortCode = new GridBagConstraints();
		gbc_sortCode.anchor = GridBagConstraints.NORTHWEST;
		gbc_sortCode.gridx = 2;
		gbc_sortCode.gridy = 0;
		accountInfoPanel.add(sortCode, gbc_sortCode);
		sortCode.setColumns(6);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		accountsTab.add(lblName, gbc_lblName);
		
		name = new JTextField();
		GridBagConstraints gbc_name = new GridBagConstraints();
		gbc_name.anchor = GridBagConstraints.WEST;
		gbc_name.insets = new Insets(0, 0, 5, 0);
		gbc_name.gridx = 1;
		gbc_name.gridy = 2;
		accountsTab.add(name, gbc_name);
		name.setColumns(40);
		
		JPanel nameComponentPanel = new JPanel();
		GridBagConstraints gbc_nameComponentPanel = new GridBagConstraints();
		gbc_nameComponentPanel.anchor = GridBagConstraints.WEST;
		gbc_nameComponentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_nameComponentPanel.fill = GridBagConstraints.BOTH;
		gbc_nameComponentPanel.gridx = 1;
		gbc_nameComponentPanel.gridy = 3;
		accountsTab.add(nameComponentPanel, gbc_nameComponentPanel);
		GridBagLayout gbl_nameComponentPanel = new GridBagLayout();
		gbl_nameComponentPanel.columnWidths = new int[]{0, 86, 0, 0, 0, 0};
		gbl_nameComponentPanel.rowHeights = new int[]{0, 0};
		gbl_nameComponentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_nameComponentPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		nameComponentPanel.setLayout(gbl_nameComponentPanel);
		
		title = new JTextField();
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.insets = new Insets(0, 0, 0, 5);
		gbc_title.anchor = GridBagConstraints.NORTHWEST;
		gbc_title.gridx = 0;
		gbc_title.gridy = 0;
		nameComponentPanel.add(title, gbc_title);
		title.setColumns(4);
		
		firstName = new JTextField();
		GridBagConstraints gbc_firstName = new GridBagConstraints();
		gbc_firstName.insets = new Insets(0, 0, 0, 5);
		gbc_firstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstName.gridx = 1;
		gbc_firstName.gridy = 0;
		nameComponentPanel.add(firstName, gbc_firstName);
		firstName.setColumns(40);
		
		middleInitial = new JTextField();
		middleInitial.setColumns(1);
		GridBagConstraints gbc_middleInitial = new GridBagConstraints();
		gbc_middleInitial.insets = new Insets(0, 0, 0, 5);
		gbc_middleInitial.fill = GridBagConstraints.HORIZONTAL;
		gbc_middleInitial.gridx = 2;
		gbc_middleInitial.gridy = 0;
		nameComponentPanel.add(middleInitial, gbc_middleInitial);
		
		lastName = new JTextField();
		GridBagConstraints gbc_lastName = new GridBagConstraints();
		gbc_lastName.insets = new Insets(0, 0, 0, 5);
		gbc_lastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastName.gridx = 3;
		gbc_lastName.gridy = 0;
		nameComponentPanel.add(lastName, gbc_lastName);
		lastName.setColumns(40);
		
		nameSuffix = new JTextField();
		GridBagConstraints gbc_nameSuffix = new GridBagConstraints();
		gbc_nameSuffix.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameSuffix.gridx = 4;
		gbc_nameSuffix.gridy = 0;
		nameComponentPanel.add(nameSuffix, gbc_nameSuffix);
		nameSuffix.setColumns(3);
		
		JLabel lblAddress = new JLabel("Address 1");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 4;
		accountsTab.add(lblAddress, gbc_lblAddress);
		
		address1 = new JTextField();
		GridBagConstraints gbc_address1 = new GridBagConstraints();
		gbc_address1.anchor = GridBagConstraints.WEST;
		gbc_address1.insets = new Insets(0, 0, 5, 0);
		gbc_address1.gridx = 1;
		gbc_address1.gridy = 4;
		accountsTab.add(address1, gbc_address1);
		address1.setColumns(40);
		
		JLabel lblAddress_1 = new JLabel("Address 2");
		GridBagConstraints gbc_lblAddress_1 = new GridBagConstraints();
		gbc_lblAddress_1.anchor = GridBagConstraints.EAST;
		gbc_lblAddress_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress_1.gridx = 0;
		gbc_lblAddress_1.gridy = 5;
		accountsTab.add(lblAddress_1, gbc_lblAddress_1);
		
		address2 = new JTextField();
		GridBagConstraints gbc_address2 = new GridBagConstraints();
		gbc_address2.insets = new Insets(0, 0, 5, 0);
		gbc_address2.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		gbc_address2.gridx = 1;
		gbc_address2.gridy = 5;
		accountsTab.add(address2, gbc_address2);
		address2.setColumns(40);
		
		JLabel lblCityStateZip = new JLabel("City / State / Zip Code");
		GridBagConstraints gbc_lblCityStateZip = new GridBagConstraints();
		gbc_lblCityStateZip.anchor = GridBagConstraints.EAST;
		gbc_lblCityStateZip.insets = new Insets(0, 0, 5, 5);
		gbc_lblCityStateZip.gridx = 0;
		gbc_lblCityStateZip.gridy = 6;
		accountsTab.add(lblCityStateZip, gbc_lblCityStateZip);
		
		JPanel cityStateZipPanel = new JPanel();
		GridBagConstraints gbc_cityStateZipPanel = new GridBagConstraints();
		gbc_cityStateZipPanel.fill = GridBagConstraints.BOTH;
		gbc_cityStateZipPanel.insets = new Insets(0, 0, 5, 0);
		gbc_cityStateZipPanel.gridx = 1;
		gbc_cityStateZipPanel.gridy = 6;
		accountsTab.add(cityStateZipPanel, gbc_cityStateZipPanel);
		GridBagLayout gbl_cityStateZipPanel = new GridBagLayout();
		gbl_cityStateZipPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_cityStateZipPanel.rowHeights = new int[]{0, 0};
		gbl_cityStateZipPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_cityStateZipPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		cityStateZipPanel.setLayout(gbl_cityStateZipPanel);
		
		city = new JTextField();
		GridBagConstraints gbc_city = new GridBagConstraints();
		gbc_city.insets = new Insets(0, 0, 0, 5);
		gbc_city.anchor = GridBagConstraints.WEST;
		gbc_city.gridx = 0;
		gbc_city.gridy = 0;
		cityStateZipPanel.add(city, gbc_city);
		city.setColumns(38);
		
		state = new JTextField();
		state.setColumns(4);
		GridBagConstraints gbc_state = new GridBagConstraints();
		gbc_state.anchor = GridBagConstraints.WEST;
		gbc_state.insets = new Insets(0, 0, 0, 5);
		gbc_state.gridx = 1;
		gbc_state.gridy = 0;
		cityStateZipPanel.add(state, gbc_state);
		
		zipCode = new JTextField();
		zipCode.setColumns(10);
		GridBagConstraints gbc_zipCode = new GridBagConstraints();
		gbc_zipCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_zipCode.gridx = 2;
		gbc_zipCode.gridy = 0;
		cityStateZipPanel.add(zipCode, gbc_zipCode);
		
		JLabel lblTelephone = new JLabel("Telephone");
		GridBagConstraints gbc_lblTelephone = new GridBagConstraints();
		gbc_lblTelephone.anchor = GridBagConstraints.EAST;
		gbc_lblTelephone.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelephone.gridx = 0;
		gbc_lblTelephone.gridy = 7;
		accountsTab.add(lblTelephone, gbc_lblTelephone);
		
		telephone = new JTextField();
		GridBagConstraints gbc_telephone = new GridBagConstraints();
		gbc_telephone.anchor = GridBagConstraints.WEST;
		gbc_telephone.insets = new Insets(0, 0, 5, 0);
		gbc_telephone.gridx = 1;
		gbc_telephone.gridy = 7;
		accountsTab.add(telephone, gbc_telephone);
		telephone.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 8;
		accountsTab.add(lblEmail, gbc_lblEmail);
		
		email = new JTextField();
		GridBagConstraints gbc_email = new GridBagConstraints();
		gbc_email.anchor = GridBagConstraints.WEST;
		gbc_email.insets = new Insets(0, 0, 5, 0);
		gbc_email.gridx = 1;
		gbc_email.gridy = 8;
		accountsTab.add(email, gbc_email);
		email.setColumns(40);
		
		JLabel lblType = new JLabel("Type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 9;
		accountsTab.add(lblType, gbc_lblType);
		
		type = new JTextField();
		GridBagConstraints gbc_type = new GridBagConstraints();
		gbc_type.anchor = GridBagConstraints.WEST;
		gbc_type.insets = new Insets(0, 0, 5, 0);
		gbc_type.gridx = 1;
		gbc_type.gridy = 9;
		accountsTab.add(type, gbc_type);
		type.setColumns(46);
		
		JLabel lblCategory = new JLabel("Category");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.anchor = GridBagConstraints.EAST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 10;
		accountsTab.add(lblCategory, gbc_lblCategory);
		
		category = new JTextField();
		GridBagConstraints gbc_category = new GridBagConstraints();
		gbc_category.insets = new Insets(0, 0, 5, 0);
		gbc_category.anchor = GridBagConstraints.WEST;
		gbc_category.gridx = 1;
		gbc_category.gridy = 10;
		accountsTab.add(category, gbc_category);
		category.setColumns(46);
		
		JLabel lblDivison = new JLabel("Divison");
		GridBagConstraints gbc_lblDivison = new GridBagConstraints();
		gbc_lblDivison.anchor = GridBagConstraints.EAST;
		gbc_lblDivison.insets = new Insets(0, 0, 0, 5);
		gbc_lblDivison.gridx = 0;
		gbc_lblDivison.gridy = 11;
		accountsTab.add(lblDivison, gbc_lblDivison);
		
		division = new JTextField();
		GridBagConstraints gbc_division = new GridBagConstraints();
		gbc_division.anchor = GridBagConstraints.WEST;
		gbc_division.gridx = 1;
		gbc_division.gridy = 11;
		accountsTab.add(division, gbc_division);
		division.setColumns(47);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_A);
		
		logsTable = new JTable();
		tabbedPane.addTab("Logs", null, logsTable, null);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_L);
		
		documentsTable = new JTable();
		tabbedPane.addTab("Documents", null, documentsTable, null);
		
		batchTable = new JTable();
		batchTable.setRowSelectionAllowed(false);
		batchTable.setFillsViewportHeight(true);
		
		JScrollPane batchPane = new JScrollPane(batchTable);
		tabbedPane.addTab("Batches", batchPane);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_B);
		
		transactionsTable = new JTable();
		transactionsTable.setRowSelectionAllowed(false);
		transactionsTable.setFillsViewportHeight(true);
		transactionsTable.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				if (evt.getClickCount() == 2)
				{
					int row = transactionsTable.rowAtPoint(evt.getPoint());
					int col = transactionsTable.columnAtPoint(evt.getPoint());
					if (col == 3)
						ADDBrowser.doBatchQuery(
							ADDBrowser.df.format(transactionsTable.getValueAt(row, 1)), 
							transactionsTable.getValueAt(row, 3).toString());
				}
			}
		});

		JScrollPane transPane = new JScrollPane(transactionsTable);
		tabbedPane.addTab("Transactions", null, transPane, null);
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_T);
	}

	
	protected void doClearAcct()
	{
		accountNumber.setText("");
		sortCode.setText("");
		name.setText("");
		title.setText("");
		firstName.setText("");
		middleInitial.setText("");
		lastName.setText("");
		nameSuffix.setText("");
		address1.setText("");
		address2.setText("");
		city.setText("");
		state.setText("");
		zipCode.setText("");
		telephone.setText("");
		email.setText("");
		type.setText("");
		category.setText("");
		division.setText("");
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
				JOptionPane.showMessageDialog(frmAddDataBrowser, "No query specified", "Transactoin Query error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ADDBrowser.doTransactionQuery(startDate, endDate, acctNum, postCodes, refNum);
		}
	}

	
	private void doConnect()
	{
		if (connectDialog == null)
			connectDialog = new ConnectDialog();
		connectDialog.OKpressed = false;
		connectDialog.setVisible(true);
		if (connectDialog.OKpressed)
		{
			ADDBrowser.doConnect((String)(connectDialog.jdbcDriver.getSelectedItem()),
					connectDialog.serverName.getText(),
					connectDialog.serverPort.getText(),
					connectDialog.databaseName.getText(),
					connectDialog.userName.getText(),
					new String(connectDialog.password.getPassword()),
					Integer.parseInt(connectDialog.maxDebitPostingCode.getText()),
					Integer.parseInt(connectDialog.maxPostingCode.getText()),
					connectDialog.invalidPClabel.getText());
		}
		connectDialog.setVisible(false);
	}

	private void doExit()
	{
		System.exit(0);
	}

	public void enableQueries(boolean b)
	{
		btnAccounts.setEnabled(b);
//		btnLogs.setEnabled(b);
//		btnDocuments.setEnabled(b);
		btnBatches.setEnabled(b);
		btnTransactions.setEnabled(b);

		btnAcctSearch.setEnabled(b);
		btnAcctClear.setEnabled(b);
		btnAcctLogs.setEnabled(b);
		btnAcctDocuments.setEnabled(b);
		btnAcctTransactions.setEnabled(b);
	}

	public JTable getSelectedTable()
	{
		switch (tabbedPane.getSelectedIndex())
		{
		case LOG_TAB_INDEX:   return logsTable;
		case DOC_TAB_INDEX:   return documentsTable;
		case BATCH_TAB_INDEX: return batchTable;
		case TRANS_TAB_INDEX: return transactionsTable;
		}
		return null;
	}
	
	public void setExportButtonState()
	{
		JTable selected = getSelectedTable();
		if (selected == null)
			btnExport.setEnabled(false);
		else if (selected.getModel().getRowCount() > 0)
			btnExport.setEnabled(true);
		else
			btnExport.setEnabled(false);
	}
	

	private void doExport()
	{
		if (chooser == null)
		{
			chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Tab Separated Values", "tsv");
			chooser.setFileFilter(filter);
		}
		
		int retval = chooser.showSaveDialog(frmAddDataBrowser);
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				JTable selected = getSelectedTable();
				if (selected == null)
					return;
				FileWriter out = new FileWriter(chooser.getSelectedFile());
				if (selected == batchTable || selected == transactionsTable)
				{
					BatchTable tbl = (BatchTable)selected.getModel();
					tbl.doExport(out);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frmAddDataBrowser, "File write failed:" + e, "File write failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void newPostCodes(Vector<PostingCode> postingCodes)
	{
		if (transactionQueryDialog != null)
			transactionQueryDialog.newPostingCodes(postingCodes);
	}
}
