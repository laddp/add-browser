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

import net.pladd.ADDBrowser.E3types.PostingCode;

public class TransactionQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425113802994773769L;
	private TransactionQueryDialog thisDialog;
	private final JPanel contentPanel = new JPanel();
	
	protected JTextField startDate;
	protected JTextField endDate;
	protected JTextField accountNum;
	protected JCheckBox chckbxStartDate;
	protected JCheckBox chckbxPostingCodes;
	protected JList<PostingCode> postingCodes;
	protected JCheckBox chckbxAccountNumber;
	protected JCheckBox chckbxEndDate;
	protected boolean OKpressed;
	protected JCheckBox chckbxReferenceNum;
	protected JTextField referenceNumber;

	/**
	 * Create the dialog.
	 */
	public TransactionQueryDialog(Vector<PostingCode> postCodes) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		thisDialog = this;
		setTitle("Transaction Query");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{105, 307, 0};
			gbl_contentPanel.rowHeights = new int[]{23, 23, 23, 91, 23, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
			contentPanel.setLayout(gbl_contentPanel);
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
				chckbxStartDate = new JCheckBox("Start Date");
				chckbxStartDate.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						startDate.setEnabled(chckbxStartDate.isSelected());
					}
				});
				GridBagConstraints gbc_chckbxStartDate = new GridBagConstraints();
				gbc_chckbxStartDate.anchor = GridBagConstraints.NORTH;
				gbc_chckbxStartDate.fill = GridBagConstraints.HORIZONTAL;
				gbc_chckbxStartDate.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxStartDate.gridx = 0;
				gbc_chckbxStartDate.gridy = 1;
				contentPanel.add(chckbxStartDate, gbc_chckbxStartDate);
			}
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
			{
				chckbxPostingCodes = new JCheckBox("Posting Codes");
				chckbxPostingCodes.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						postingCodes.setEnabled(chckbxPostingCodes.isSelected());
					}
				});
				{
					chckbxEndDate = new JCheckBox("End Date");
					chckbxEndDate.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							endDate.setEnabled(chckbxEndDate.isSelected());
						}
					});
					GridBagConstraints gbc_chckbxEndDate = new GridBagConstraints();
					gbc_chckbxEndDate.anchor = GridBagConstraints.NORTH;
					gbc_chckbxEndDate.fill = GridBagConstraints.HORIZONTAL;
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
				GridBagConstraints gbc_chckbxPostingCodes = new GridBagConstraints();
				gbc_chckbxPostingCodes.fill = GridBagConstraints.HORIZONTAL;
				gbc_chckbxPostingCodes.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxPostingCodes.gridx = 0;
				gbc_chckbxPostingCodes.gridy = 3;
				contentPanel.add(chckbxPostingCodes, gbc_chckbxPostingCodes);
			}
			{
				JScrollPane postingCodeScrollPane = new JScrollPane();
				GridBagConstraints gbc_postingCodeScrollPane = new GridBagConstraints();
				gbc_postingCodeScrollPane.anchor = GridBagConstraints.WEST;
				gbc_postingCodeScrollPane.fill = GridBagConstraints.BOTH;
				gbc_postingCodeScrollPane.insets = new Insets(0, 0, 5, 0);
				gbc_postingCodeScrollPane.gridx = 1;
				gbc_postingCodeScrollPane.gridy = 3;
				contentPanel.add(postingCodeScrollPane, gbc_postingCodeScrollPane);
				{
					postingCodes = new JList<PostingCode>(postCodes);
					postingCodeScrollPane.setViewportView(postingCodes);
					postingCodes.setEnabled(false);
				}
			}
			{
				chckbxReferenceNum = new JCheckBox("Reference Num");
				chckbxReferenceNum.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						referenceNumber.setEnabled(chckbxReferenceNum.isSelected());
					}
				});
				GridBagConstraints gbc_chckbxReferenceNum = new GridBagConstraints();
				gbc_chckbxReferenceNum.anchor = GridBagConstraints.NORTH;
				gbc_chckbxReferenceNum.fill = GridBagConstraints.HORIZONTAL;
				gbc_chckbxReferenceNum.insets = new Insets(0, 0, 0, 5);
				gbc_chckbxReferenceNum.gridx = 0;
				gbc_chckbxReferenceNum.gridy = 4;
				contentPanel.add(chckbxReferenceNum, gbc_chckbxReferenceNum);
			}
			{
				referenceNumber = new JTextField();
				referenceNumber.setEnabled(false);
				GridBagConstraints gbc_referenceNumber = new GridBagConstraints();
				gbc_referenceNumber.fill = GridBagConstraints.HORIZONTAL;
				gbc_referenceNumber.gridx = 1;
				gbc_referenceNumber.gridy = 4;
				contentPanel.add(referenceNumber, gbc_referenceNumber);
				referenceNumber.setColumns(10);
			}
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

	public void newPostingCodes(Vector<PostingCode> newCodes)
	{
		postingCodes.setListData(newCodes);
	}
}
