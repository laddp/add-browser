package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TableDatesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054193811259838210L;
	private final JPanel contentPanel = new JPanel();
	
	private TableDatesDialog thisDialog;

	/**
	 * Create the dialog.
	 * @param counts 
	 */
	public TableDatesDialog(Map<String, Map<String, Object>> values) {
		thisDialog = this;
		NumberFormat nf = NumberFormat.getInstance();
		
		setTitle("Table Counts and Dates");
		setBounds(100, 100, 450, 233);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblCount = new JLabel("Count");
			GridBagConstraints gbc_lblCount = new GridBagConstraints();
			gbc_lblCount.anchor = GridBagConstraints.EAST;
			gbc_lblCount.insets = new Insets(0, 0, 5, 5);
			gbc_lblCount.gridx = 1;
			gbc_lblCount.gridy = 0;
			contentPanel.add(lblCount, gbc_lblCount);
		}
		{
			JLabel lblEarliestMaint = new JLabel("Earliest Maint");
			GridBagConstraints gbc_lblEarliestMaint = new GridBagConstraints();
			gbc_lblEarliestMaint.insets = new Insets(0, 0, 5, 5);
			gbc_lblEarliestMaint.gridx = 2;
			gbc_lblEarliestMaint.gridy = 0;
			contentPanel.add(lblEarliestMaint, gbc_lblEarliestMaint);
		}
		{
			JLabel lblLastMaint = new JLabel("Last Maint");
			GridBagConstraints gbc_lblLastMaint = new GridBagConstraints();
			gbc_lblLastMaint.insets = new Insets(0, 0, 5, 0);
			gbc_lblLastMaint.gridx = 3;
			gbc_lblLastMaint.gridy = 0;
			contentPanel.add(lblLastMaint, gbc_lblLastMaint);
		}
		{
			JLabel lblAccounts = new JLabel("Accounts");
			GridBagConstraints gbc_lblAccounts = new GridBagConstraints();
			gbc_lblAccounts.anchor = GridBagConstraints.EAST;
			gbc_lblAccounts.insets = new Insets(0, 0, 5, 5);
			gbc_lblAccounts.gridx = 0;
			gbc_lblAccounts.gridy = 1;
			contentPanel.add(lblAccounts, gbc_lblAccounts);
		}
		{
			JLabel acctCount = new JLabel(nf.format(values.get("counts").get("ACCOUNTS")));
			GridBagConstraints gbc_acctCount = new GridBagConstraints();
			gbc_acctCount.anchor = GridBagConstraints.EAST;
			gbc_acctCount.insets = new Insets(0, 0, 5, 5);
			gbc_acctCount.gridx = 1;
			gbc_acctCount.gridy = 1;
			contentPanel.add(acctCount, gbc_acctCount);
		}
		{
			JLabel acctEarly = new JLabel(values.get("early").get("ACCOUNTS").toString());
			GridBagConstraints gbc_acctEarly = new GridBagConstraints();
			gbc_acctEarly.insets = new Insets(0, 0, 5, 5);
			gbc_acctEarly.gridx = 2;
			gbc_acctEarly.gridy = 1;
			contentPanel.add(acctEarly, gbc_acctEarly);
		}
		{
			JLabel acctLast = new JLabel(values.get("last").get("ACCOUNTS").toString());
			GridBagConstraints gbc_acctLast = new GridBagConstraints();
			gbc_acctLast.insets = new Insets(0, 0, 5, 0);
			gbc_acctLast.gridx = 3;
			gbc_acctLast.gridy = 1;
			contentPanel.add(acctLast, gbc_acctLast);
		}
		{
			JLabel lblTanks = new JLabel("Tanks");
			GridBagConstraints gbc_lblTanks = new GridBagConstraints();
			gbc_lblTanks.anchor = GridBagConstraints.EAST;
			gbc_lblTanks.insets = new Insets(0, 0, 5, 5);
			gbc_lblTanks.gridx = 0;
			gbc_lblTanks.gridy = 2;
			contentPanel.add(lblTanks, gbc_lblTanks);
		}
		{
			JLabel tankCount = new JLabel(nf.format(values.get("counts").get("TANKS")));
			GridBagConstraints gbc_tankCount = new GridBagConstraints();
			gbc_tankCount.anchor = GridBagConstraints.EAST;
			gbc_tankCount.insets = new Insets(0, 0, 5, 5);
			gbc_tankCount.gridx = 1;
			gbc_tankCount.gridy = 2;
			contentPanel.add(tankCount, gbc_tankCount);
		}
		{
			JLabel tankEarly = new JLabel(values.get("early").get("TANKS").toString());
			GridBagConstraints gbc_tankEarly = new GridBagConstraints();
			gbc_tankEarly.insets = new Insets(0, 0, 5, 5);
			gbc_tankEarly.gridx = 2;
			gbc_tankEarly.gridy = 2;
			contentPanel.add(tankEarly, gbc_tankEarly);
		}
		{
			JLabel tankLast = new JLabel(values.get("last").get("TANKS").toString());
			GridBagConstraints gbc_tankLast = new GridBagConstraints();
			gbc_tankLast.insets = new Insets(0, 0, 5, 0);
			gbc_tankLast.gridx = 3;
			gbc_tankLast.gridy = 2;
			contentPanel.add(tankLast, gbc_tankLast);
		}
		{
			JLabel lblService = new JLabel("Service");
			GridBagConstraints gbc_lblService = new GridBagConstraints();
			gbc_lblService.anchor = GridBagConstraints.EAST;
			gbc_lblService.insets = new Insets(0, 0, 5, 5);
			gbc_lblService.gridx = 0;
			gbc_lblService.gridy = 3;
			contentPanel.add(lblService, gbc_lblService);
		}
		{
			JLabel svcCount = new JLabel(nf.format(values.get("counts").get("SERVICE")));
			GridBagConstraints gbc_svcCount = new GridBagConstraints();
			gbc_svcCount.anchor = GridBagConstraints.EAST;
			gbc_svcCount.insets = new Insets(0, 0, 5, 5);
			gbc_svcCount.gridx = 1;
			gbc_svcCount.gridy = 3;
			contentPanel.add(svcCount, gbc_svcCount);
		}
		{
			JLabel svcEarly = new JLabel(values.get("early").get("SERVICE").toString());
			GridBagConstraints gbc_svcEarly = new GridBagConstraints();
			gbc_svcEarly.insets = new Insets(0, 0, 5, 5);
			gbc_svcEarly.gridx = 2;
			gbc_svcEarly.gridy = 3;
			contentPanel.add(svcEarly, gbc_svcEarly);
		}
		{
			JLabel svcLast = new JLabel(values.get("last").get("SERVICE").toString());
			GridBagConstraints gbc_svcLast = new GridBagConstraints();
			gbc_svcLast.insets = new Insets(0, 0, 5, 0);
			gbc_svcLast.gridx = 3;
			gbc_svcLast.gridy = 3;
			contentPanel.add(svcLast, gbc_svcLast);
		}
		{
			JLabel lblContacts = new JLabel("Contacts");
			GridBagConstraints gbc_lblContacts = new GridBagConstraints();
			gbc_lblContacts.anchor = GridBagConstraints.EAST;
			gbc_lblContacts.insets = new Insets(0, 0, 5, 5);
			gbc_lblContacts.gridx = 0;
			gbc_lblContacts.gridy = 4;
			contentPanel.add(lblContacts, gbc_lblContacts);
		}
		{
			JLabel contactCount = new JLabel(nf.format(values.get("counts").get("CONTACT_INFO_HDR")));
			GridBagConstraints gbc_contactCount = new GridBagConstraints();
			gbc_contactCount.anchor = GridBagConstraints.EAST;
			gbc_contactCount.insets = new Insets(0, 0, 5, 5);
			gbc_contactCount.gridx = 1;
			gbc_contactCount.gridy = 4;
			contentPanel.add(contactCount, gbc_contactCount);
		}
		{
			JLabel contactEarly = new JLabel(values.get("early").get("CONTACT_INFO_HDR").toString());
			GridBagConstraints gbc_contactEarly = new GridBagConstraints();
			gbc_contactEarly.insets = new Insets(0, 0, 5, 5);
			gbc_contactEarly.gridx = 2;
			gbc_contactEarly.gridy = 4;
			contentPanel.add(contactEarly, gbc_contactEarly);
		}
		{
			JLabel contactLast = new JLabel(values.get("last").get("CONTACT_INFO_HDR").toString());
			GridBagConstraints gbc_contactLast = new GridBagConstraints();
			gbc_contactLast.insets = new Insets(0, 0, 5, 0);
			gbc_contactLast.gridx = 3;
			gbc_contactLast.gridy = 4;
			contentPanel.add(contactLast, gbc_contactLast);
		}
		{
			JLabel lblLogs = new JLabel("Logs");
			GridBagConstraints gbc_lblLogs = new GridBagConstraints();
			gbc_lblLogs.anchor = GridBagConstraints.EAST;
			gbc_lblLogs.insets = new Insets(0, 0, 5, 5);
			gbc_lblLogs.gridx = 0;
			gbc_lblLogs.gridy = 5;
			contentPanel.add(lblLogs, gbc_lblLogs);
		}
		{
			JLabel logCount = new JLabel(nf.format(values.get("counts").get("LOG_HEADER")));
			GridBagConstraints gbc_logCount = new GridBagConstraints();
			gbc_logCount.anchor = GridBagConstraints.EAST;
			gbc_logCount.insets = new Insets(0, 0, 5, 5);
			gbc_logCount.gridx = 1;
			gbc_logCount.gridy = 5;
			contentPanel.add(logCount, gbc_logCount);
		}
		{
			JLabel logEarly = new JLabel(values.get("early").get("LOG_HEADER").toString());
			GridBagConstraints gbc_logEarly = new GridBagConstraints();
			gbc_logEarly.insets = new Insets(0, 0, 5, 5);
			gbc_logEarly.gridx = 2;
			gbc_logEarly.gridy = 5;
			contentPanel.add(logEarly, gbc_logEarly);
		}
		{
			JLabel logLast = new JLabel(values.get("last").get("LOG_HEADER").toString());
			GridBagConstraints gbc_logLast = new GridBagConstraints();
			gbc_logLast.insets = new Insets(0, 0, 5, 0);
			gbc_logLast.gridx = 3;
			gbc_logLast.gridy = 5;
			contentPanel.add(logLast, gbc_logLast);
		}
		{
			JLabel lblDocuments = new JLabel("Documents");
			GridBagConstraints gbc_lblDocuments = new GridBagConstraints();
			gbc_lblDocuments.anchor = GridBagConstraints.EAST;
			gbc_lblDocuments.insets = new Insets(0, 0, 5, 5);
			gbc_lblDocuments.gridx = 0;
			gbc_lblDocuments.gridy = 6;
			contentPanel.add(lblDocuments, gbc_lblDocuments);
		}
		{
			JLabel docCount = new JLabel(nf.format(values.get("counts").get("DOC_HEADER")));
			GridBagConstraints gbc_docCount = new GridBagConstraints();
			gbc_docCount.anchor = GridBagConstraints.EAST;
			gbc_docCount.insets = new Insets(0, 0, 5, 5);
			gbc_docCount.gridx = 1;
			gbc_docCount.gridy = 6;
			contentPanel.add(docCount, gbc_docCount);
		}
		{
			JLabel docEarly = new JLabel(values.get("early").get("DOC_HEADER").toString());
			GridBagConstraints gbc_logEarly = new GridBagConstraints();
			gbc_logEarly.insets = new Insets(0, 0, 5, 5);
			gbc_logEarly.gridx = 2;
			gbc_logEarly.gridy = 6;
			contentPanel.add(docEarly, gbc_logEarly);
		}
		{
			JLabel docLast = new JLabel(values.get("last").get("LOG_HEADER").toString());
			GridBagConstraints gbc_logLast = new GridBagConstraints();
			gbc_logLast.insets = new Insets(0, 0, 5, 0);
			gbc_logLast.gridx = 3;
			gbc_logLast.gridy = 6;
			contentPanel.add(docLast, gbc_logLast);
		}
		{
			JLabel lblTransactions = new JLabel("Transactions");
			GridBagConstraints gbc_lblTransactions = new GridBagConstraints();
			gbc_lblTransactions.anchor = GridBagConstraints.EAST;
			gbc_lblTransactions.insets = new Insets(0, 0, 0, 5);
			gbc_lblTransactions.gridx = 0;
			gbc_lblTransactions.gridy = 7;
			contentPanel.add(lblTransactions, gbc_lblTransactions);
		}
		{
			JLabel transCount = new JLabel(nf.format(values.get("counts").get("TRANS_MAIN")));
			GridBagConstraints gbc_transCount = new GridBagConstraints();
			gbc_transCount.anchor = GridBagConstraints.EAST;
			gbc_transCount.insets = new Insets(0, 0, 0, 5);
			gbc_transCount.gridx = 1;
			gbc_transCount.gridy = 7;
			contentPanel.add(transCount, gbc_transCount);
		}
		{
			JLabel transEarly = new JLabel(values.get("early").get("TRANS_MAIN").toString());
			GridBagConstraints gbc_transEarly = new GridBagConstraints();
			gbc_transEarly.insets = new Insets(0, 0, 0, 5);
			gbc_transEarly.gridx = 2;
			gbc_transEarly.gridy = 7;
			contentPanel.add(transEarly, gbc_transEarly);
		}
		{
			JLabel transLast = new JLabel(values.get("last").get("TRANS_MAIN").toString());
			GridBagConstraints gbc_transLast = new GridBagConstraints();
			gbc_transLast.gridx = 3;
			gbc_transLast.gridy = 7;
			contentPanel.add(transLast, gbc_transLast);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisDialog.dispose();
					}
				});
				okButton.setMnemonic('O');
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
