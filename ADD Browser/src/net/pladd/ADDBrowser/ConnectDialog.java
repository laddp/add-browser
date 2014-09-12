package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;

public class ConnectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final String MYSQL_STRING  = "MYSQL ConnectJ";
	protected static final String SYBASE_STRING = "Sybase JDBC";
	
	private final JPanel connectionPanel = new JPanel();
	protected JTextField serverName;
	protected JTextField serverPort;
	protected JTextField userName;
	protected JPasswordField password;
	protected JTextField databaseName;
	protected JComboBox<String> jdbcDriver;
	
	protected boolean OKpressed = false;
	private final ConnectDialog thisDialog;
	protected JTextField maxPostingCode;
	protected JTextField maxDebitPostingCode;

	/**
	 * Create the dialog.
	 */
	public ConnectDialog() {
		thisDialog = this;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			tabbedPane.addTab("Connection Details", null, connectionPanel, null);
			connectionPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagLayout gbl_connectionPanel = new GridBagLayout();
			gbl_connectionPanel.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_connectionPanel.columnWeights = new double[]{0.0, 1.0};
			gbl_connectionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			connectionPanel.setLayout(gbl_connectionPanel);
			{
				JLabel lblConnectionType = new JLabel("Connection Type:");
				GridBagConstraints gbc_lblConnectionType = new GridBagConstraints();
				gbc_lblConnectionType.anchor = GridBagConstraints.EAST;
				gbc_lblConnectionType.insets = new Insets(0, 0, 5, 5);
				gbc_lblConnectionType.gridx = 0;
				gbc_lblConnectionType.gridy = 0;
				connectionPanel.add(lblConnectionType, gbc_lblConnectionType);
			}
			{
				jdbcDriver = new JComboBox<String>();
				jdbcDriver.setModel(new DefaultComboBoxModel<String>(new String[] {MYSQL_STRING, SYBASE_STRING}));
				GridBagConstraints gbc_jdbcClassComboBox = new GridBagConstraints();
				gbc_jdbcClassComboBox.insets = new Insets(0, 0, 5, 0);
				gbc_jdbcClassComboBox.anchor = GridBagConstraints.NORTHWEST;
				gbc_jdbcClassComboBox.gridx = 1;
				gbc_jdbcClassComboBox.gridy = 0;
				connectionPanel.add(jdbcDriver, gbc_jdbcClassComboBox);
			}
			{
				JLabel lblServerName = new JLabel("Server Name:");
				GridBagConstraints gbc_lblServerName = new GridBagConstraints();
				gbc_lblServerName.anchor = GridBagConstraints.EAST;
				gbc_lblServerName.insets = new Insets(0, 0, 5, 5);
				gbc_lblServerName.gridx = 0;
				gbc_lblServerName.gridy = 1;
				connectionPanel.add(lblServerName, gbc_lblServerName);
			}
			{
				serverName = new JTextField();
				GridBagConstraints gbc_serverName = new GridBagConstraints();
				gbc_serverName.insets = new Insets(0, 0, 5, 0);
				gbc_serverName.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverName.gridx = 1;
				gbc_serverName.gridy = 1;
				connectionPanel.add(serverName, gbc_serverName);
				serverName.setColumns(10);
			}
			{
				JLabel lblPort = new JLabel("Port:");
				GridBagConstraints gbc_lblPort = new GridBagConstraints();
				gbc_lblPort.anchor = GridBagConstraints.EAST;
				gbc_lblPort.insets = new Insets(0, 0, 5, 5);
				gbc_lblPort.gridx = 0;
				gbc_lblPort.gridy = 2;
				connectionPanel.add(lblPort, gbc_lblPort);
			}
			{
				serverPort = new JTextField();
				GridBagConstraints gbc_serverPort = new GridBagConstraints();
				gbc_serverPort.insets = new Insets(0, 0, 5, 0);
				gbc_serverPort.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverPort.gridx = 1;
				gbc_serverPort.gridy = 2;
				connectionPanel.add(serverPort, gbc_serverPort);
				serverPort.setColumns(10);
			}
			{
				JLabel lblUserName = new JLabel("User Name:");
				GridBagConstraints gbc_lblUserName = new GridBagConstraints();
				gbc_lblUserName.anchor = GridBagConstraints.EAST;
				gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
				gbc_lblUserName.gridx = 0;
				gbc_lblUserName.gridy = 3;
				connectionPanel.add(lblUserName, gbc_lblUserName);
			}
			{
				userName = new JTextField();
				GridBagConstraints gbc_userName = new GridBagConstraints();
				gbc_userName.insets = new Insets(0, 0, 5, 0);
				gbc_userName.fill = GridBagConstraints.HORIZONTAL;
				gbc_userName.gridx = 1;
				gbc_userName.gridy = 3;
				connectionPanel.add(userName, gbc_userName);
				userName.setColumns(10);
			}
			{
				JLabel lblPassword = new JLabel("Password:");
				GridBagConstraints gbc_lblPassword = new GridBagConstraints();
				gbc_lblPassword.anchor = GridBagConstraints.EAST;
				gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
				gbc_lblPassword.gridx = 0;
				gbc_lblPassword.gridy = 4;
				connectionPanel.add(lblPassword, gbc_lblPassword);
			}
			{
				password = new JPasswordField();
				GridBagConstraints gbc_password = new GridBagConstraints();
				gbc_password.insets = new Insets(0, 0, 5, 0);
				gbc_password.fill = GridBagConstraints.HORIZONTAL;
				gbc_password.gridx = 1;
				gbc_password.gridy = 4;
				connectionPanel.add(password, gbc_password);
			}
			{
				JLabel lblDatabase = new JLabel("Database:");
				GridBagConstraints gbc_lblDatabase = new GridBagConstraints();
				gbc_lblDatabase.anchor = GridBagConstraints.EAST;
				gbc_lblDatabase.insets = new Insets(0, 0, 5, 5);
				gbc_lblDatabase.gridx = 0;
				gbc_lblDatabase.gridy = 5;
				connectionPanel.add(lblDatabase, gbc_lblDatabase);
			}
			{
				databaseName = new JTextField();
				GridBagConstraints gbc_databaseName = new GridBagConstraints();
				gbc_databaseName.insets = new Insets(0, 0, 5, 0);
				gbc_databaseName.fill = GridBagConstraints.HORIZONTAL;
				gbc_databaseName.gridx = 1;
				gbc_databaseName.gridy = 5;
				connectionPanel.add(databaseName, gbc_databaseName);
				databaseName.setColumns(10);
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Database Settings", null, panel, null);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{0, 0, 0};
				gbl_panel.rowHeights = new int[]{0, 0, 0};
				gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				{
					JLabel label = new JLabel("Max Posting Code");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.insets = new Insets(0, 0, 5, 5);
					gbc_label.anchor = GridBagConstraints.EAST;
					gbc_label.gridx = 0;
					gbc_label.gridy = 0;
					panel.add(label, gbc_label);
				}
				{
					maxPostingCode = new JTextField();
					maxPostingCode.setText("255");
					maxPostingCode.setColumns(5);
					GridBagConstraints gbc_maxPostingCode = new GridBagConstraints();
					gbc_maxPostingCode.insets = new Insets(0, 0, 5, 0);
					gbc_maxPostingCode.fill = GridBagConstraints.HORIZONTAL;
					gbc_maxPostingCode.gridx = 1;
					gbc_maxPostingCode.gridy = 0;
					panel.add(maxPostingCode, gbc_maxPostingCode);
				}
				{
					JLabel label = new JLabel("Max Debit Posting Code");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.anchor = GridBagConstraints.EAST;
					gbc_label.insets = new Insets(0, 0, 0, 5);
					gbc_label.gridx = 0;
					gbc_label.gridy = 1;
					panel.add(label, gbc_label);
				}
				{
					maxDebitPostingCode = new JTextField();
					maxDebitPostingCode.setText("199");
					maxDebitPostingCode.setColumns(5);
					GridBagConstraints gbc_maxDebitPostingCode = new GridBagConstraints();
					gbc_maxDebitPostingCode.fill = GridBagConstraints.HORIZONTAL;
					gbc_maxDebitPostingCode.gridx = 1;
					gbc_maxDebitPostingCode.gridy = 1;
					panel.add(maxDebitPostingCode, gbc_maxDebitPostingCode);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisDialog.setVisible(false);
						OKpressed = true;
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisDialog.setVisible(false);
						OKpressed = false;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
