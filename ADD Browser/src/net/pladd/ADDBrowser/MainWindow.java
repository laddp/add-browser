package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

public class MainWindow {

	protected JFrame frmAddDataBrowser;
	protected JTable accountsTable;
	protected JTable logsTable;
	protected JTable batchTable;
	protected JTable transactionsTable;
	
	protected ConnectDialog    connectDialog    = null;
	protected BatchQueryDialog batchQueryDialog = null;
	private JButton btnAccounts;
	private JButton btnLogs;
	private JButton btnBatches;
	private JButton btnTransactions;
	protected JTabbedPane tabbedPane;
	private JButton btnExport;

	JFileChooser chooser = null; 

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddDataBrowser = new JFrame();
		frmAddDataBrowser.setSize(new Dimension(800, 600));
		frmAddDataBrowser.setTitle("ADD Data Browser");
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
		
		JMenu mnSearch = new JMenu("Search");
		mnSearch.setEnabled(false);
		menuBar.add(mnSearch);
		
		JRadioButtonMenuItem rdbtnmntmAccounts = new JRadioButtonMenuItem("Accounts");
		rdbtnmntmAccounts.setEnabled(false);
		mnSearch.add(rdbtnmntmAccounts);
		
		JRadioButtonMenuItem rdbtnmntmLogEntries = new JRadioButtonMenuItem("Log Entries");
		rdbtnmntmLogEntries.setEnabled(false);
		mnSearch.add(rdbtnmntmLogEntries);
		
		JRadioButtonMenuItem rdbtnmntmPostingBatch = new JRadioButtonMenuItem("Posting Batch");
		rdbtnmntmPostingBatch.setEnabled(false);
		mnSearch.add(rdbtnmntmPostingBatch);
		
		JRadioButtonMenuItem rdbtnmntmTransaction = new JRadioButtonMenuItem("Transaction");
		rdbtnmntmTransaction.setEnabled(false);
		mnSearch.add(rdbtnmntmTransaction);
		
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
		btnBatches.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnBatches);
		
		btnTransactions = new JButton("Transactions");
		btnTransactions.setEnabled(false);
		btnTransactions.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		toolBar.add(btnTransactions);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent change) {
				setExportButtonState();
			}
		});
		frmAddDataBrowser.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		accountsTable = new JTable();
		tabbedPane.addTab("Accounts", null, accountsTable, null);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_A);
		
		logsTable = new JTable();
		tabbedPane.addTab("Logs", null, logsTable, null);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_L);
		
		batchTable = new JTable();
		batchTable.setRowSelectionAllowed(false);
		batchTable.setFillsViewportHeight(true);
		
		JScrollPane batchPane = new JScrollPane(batchTable);
		tabbedPane.addTab("Batches", batchPane);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_B);
		
		transactionsTable = new JTable();
		tabbedPane.addTab("Transactions", null, transactionsTable, null);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_T);
	}

	
	protected void doBatchQuery() {
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
			
			ADDBrowser.doBatchQuery(postingDate, batchNumber);
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
					Integer.parseInt(connectDialog.maxDebitPosting.getText()),
					Integer.parseInt(connectDialog.maxPostingCode.getText()));
		}
		connectDialog.setVisible(false);
	}

	private void doExit()
	{
		System.exit(0);
	}

	public void enableQueries(boolean b)
	{
//		btnAccounts.setEnabled(b);
//		btnLogs.setEnabled(b);
		btnBatches.setEnabled(b);
//		btnTransactions.setEnabled(b);
	}

	public JTable getSelectedTable()
	{
		switch (tabbedPane.getSelectedIndex())
		{
		case 0: return accountsTable;
		case 1: return logsTable;
		case 2: return batchTable;
		case 3: return transactionsTable;
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
				TableModel model = selected.getModel();
				FileWriter out = new FileWriter(chooser.getSelectedFile());
				
				for(int i = 0; i < model.getColumnCount(); i++){
					out.write(model.getColumnName(i) + "\t");
				}
				out.write("\n");

				for(int i=0; i< model.getRowCount(); i++) {
					for(int j=0; j < model.getColumnCount(); j++) {
						out.write(model.getValueAt(i,j).toString()+"\t");
					}
					out.write("\n");
				}
				out.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frmAddDataBrowser, "File write failed:" + e, "File write failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
