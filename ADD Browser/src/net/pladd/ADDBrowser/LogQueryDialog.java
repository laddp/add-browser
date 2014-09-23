package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.pladd.ADDBrowser.E3types.LogGroup;
import net.pladd.ADDBrowser.E3types.PostingCode;

public class LogQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425113802994773769L;
	private LogQueryDialog thisDialog;
	private final JPanel contentPanel = new JPanel();
	protected boolean OKpressed;
	
	protected JCheckBox chckbxAccountNumber;
	protected JTextField accountNum;
	protected JCheckBox chckbxStartDate;
	protected JTextField startDate;
	protected JCheckBox chckbxEndDate;
	protected JTextField endDate;
	protected JCheckBox chckbxLogCategory;
	protected JList<LogGroup> logGroups;
	protected JCheckBox chckbxLogType;
	protected JList<LogGroup> logType;
	private JCheckBox chckbxLogMessage;
	private JTextField logMessage;


	/**
	 * Create the dialog.
	 */
	public LogQueryDialog(Vector<PostingCode> postCodes) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		thisDialog = this;
		setTitle("Log Query");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{105, 307, 0};
			gbl_contentPanel.rowHeights = new int[]{23, 23, 23, 57, 57, 0, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
			contentPanel.setLayout(gbl_contentPanel);
		}
		chckbxAccountNumber = new JCheckBox("Account Number");
		chckbxAccountNumber.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				accountNum.setEnabled(chckbxAccountNumber.isSelected());
			}
		});
		GridBagConstraints gbc_chckbxAccountNumber = new GridBagConstraints();
		gbc_chckbxAccountNumber.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxAccountNumber.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAccountNumber.gridx = 0;
		gbc_chckbxAccountNumber.gridy = 0;
		contentPanel.add(chckbxAccountNumber, gbc_chckbxAccountNumber);
		{
			accountNum = new JTextField();
			accountNum.setEnabled(false);
			GridBagConstraints gbc_accountNum = new GridBagConstraints();
			gbc_accountNum.fill = GridBagConstraints.HORIZONTAL;
			gbc_accountNum.insets = new Insets(0, 0, 5, 0);
			gbc_accountNum.gridx = 1;
			gbc_accountNum.gridy = 0;
			contentPanel.add(accountNum, gbc_accountNum);
			accountNum.setColumns(10);
		}
		{
			chckbxLogCategory = new JCheckBox("Log Category");
			chckbxLogCategory.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					logGroups.setEnabled(chckbxLogCategory.isSelected());
				}
			});
			{
				chckbxStartDate = new JCheckBox("Start Date");
				chckbxStartDate.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						startDate.setEnabled(chckbxStartDate.isSelected());
					}
				});
				GridBagConstraints gbc_chckbxStartDate = new GridBagConstraints();
				gbc_chckbxStartDate.anchor = GridBagConstraints.NORTHWEST;
				gbc_chckbxStartDate.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxStartDate.gridx = 0;
				gbc_chckbxStartDate.gridy = 1;
				contentPanel.add(chckbxStartDate, gbc_chckbxStartDate);
			}
			{
				chckbxEndDate = new JCheckBox("End Date");
				chckbxEndDate.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						endDate.setEnabled(chckbxEndDate.isSelected());
					}
				});
				{
					startDate = new JTextField();
					startDate.setEnabled(false);
					GridBagConstraints gbc_startDate = new GridBagConstraints();
					gbc_startDate.fill = GridBagConstraints.HORIZONTAL;
					gbc_startDate.insets = new Insets(0, 0, 5, 0);
					gbc_startDate.gridx = 1;
					gbc_startDate.gridy = 1;
					contentPanel.add(startDate, gbc_startDate);
					startDate.setColumns(10);
				}
				GridBagConstraints gbc_chckbxEndDate = new GridBagConstraints();
				gbc_chckbxEndDate.anchor = GridBagConstraints.NORTHWEST;
				gbc_chckbxEndDate.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxEndDate.gridx = 0;
				gbc_chckbxEndDate.gridy = 2;
				contentPanel.add(chckbxEndDate, gbc_chckbxEndDate);
			}
			{
				endDate = new JTextField();
				endDate.setEnabled(false);
				GridBagConstraints gbc_endDate = new GridBagConstraints();
				gbc_endDate.fill = GridBagConstraints.HORIZONTAL;
				gbc_endDate.insets = new Insets(0, 0, 5, 0);
				gbc_endDate.gridx = 1;
				gbc_endDate.gridy = 2;
				contentPanel.add(endDate, gbc_endDate);
				endDate.setColumns(10);
			}
			GridBagConstraints gbc_chckbxLogCategory = new GridBagConstraints();
			gbc_chckbxLogCategory.anchor = GridBagConstraints.WEST;
			gbc_chckbxLogCategory.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxLogCategory.gridx = 0;
			gbc_chckbxLogCategory.gridy = 3;
			contentPanel.add(chckbxLogCategory, gbc_chckbxLogCategory);
		}
		{
			JScrollPane groupScrollPane = new JScrollPane();
			GridBagConstraints gbc_groupScrollPane = new GridBagConstraints();
			gbc_groupScrollPane.anchor = GridBagConstraints.WEST;
			gbc_groupScrollPane.fill = GridBagConstraints.BOTH;
			gbc_groupScrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_groupScrollPane.gridx = 1;
			gbc_groupScrollPane.gridy = 3;
			contentPanel.add(groupScrollPane, gbc_groupScrollPane);
			{
				logGroups = new JList<LogGroup>();
				groupScrollPane.setViewportView(logGroups);
				logGroups.setEnabled(false);
			}
		}
		{
			chckbxLogType = new JCheckBox("Log Type");
			chckbxLogType.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					logType.setEnabled(chckbxLogType.isSelected());
				}
			});
			GridBagConstraints gbc_chckbxLogType = new GridBagConstraints();
			gbc_chckbxLogType.anchor = GridBagConstraints.WEST;
			gbc_chckbxLogType.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxLogType.gridx = 0;
			gbc_chckbxLogType.gridy = 4;
			contentPanel.add(chckbxLogType, gbc_chckbxLogType);
		}
		{
			JScrollPane logTypeScrollPane = new JScrollPane();
			GridBagConstraints gbc_logTypeScrollPane = new GridBagConstraints();
			gbc_logTypeScrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_logTypeScrollPane.anchor = GridBagConstraints.WEST;
			gbc_logTypeScrollPane.fill = GridBagConstraints.BOTH;
			gbc_logTypeScrollPane.gridx = 1;
			gbc_logTypeScrollPane.gridy = 4;
			contentPanel.add(logTypeScrollPane, gbc_logTypeScrollPane);
			{
				logType = new JList<LogGroup>();
				logTypeScrollPane.setViewportView(logType);
				logType.setEnabled(false);
			}
		}
		{
			chckbxLogMessage = new JCheckBox("Log Message");
			GridBagConstraints gbc_chckbxLogMessage = new GridBagConstraints();
			gbc_chckbxLogMessage.anchor = GridBagConstraints.WEST;
			gbc_chckbxLogMessage.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxLogMessage.gridx = 0;
			gbc_chckbxLogMessage.gridy = 5;
			contentPanel.add(chckbxLogMessage, gbc_chckbxLogMessage);
		}
		{
			logMessage = new JTextField();
			GridBagConstraints gbc_logMessage = new GridBagConstraints();
			gbc_logMessage.fill = GridBagConstraints.HORIZONTAL;
			gbc_logMessage.gridx = 1;
			gbc_logMessage.gridy = 5;
			contentPanel.add(logMessage, gbc_logMessage);
			logMessage.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisDialog.setVisible(false);
						OKpressed = true;
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisDialog.setVisible(false);
						OKpressed = false;
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	public void newPostingCodes(Vector<LogGroup> newCodes)
	{
		logGroups.setListData(newCodes);
	}

	public void newCats(Vector<LogGroup> logCategories) {
		// TODO Auto-generated method stub
		
	}

	public void newTypes(Vector<LogGroup> logTypes) {
		// TODO Auto-generated method stub
		
	}
}
