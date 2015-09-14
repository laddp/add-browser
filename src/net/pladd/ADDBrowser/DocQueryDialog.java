package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

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

import net.pladd.ADDBrowser.E3types.DocType;

public class DocQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425113802994773769L;
	private DocQueryDialog thisDialog;
	private final JPanel contentPanel = new JPanel();
	protected boolean OKpressed;
	
	protected JCheckBox chckbxAccountNumber;
	protected JTextField accountNum;
	protected JCheckBox chckbxStartDate;
	protected JTextField startDate;
	protected JCheckBox chckbxEndDate;
	protected JTextField endDate;
	protected JCheckBox chckbxDocType;
	protected JList<DocType> docTypes;
	protected JCheckBox chckbxReferenceNumber;
	protected JTextField referenceNumber;


	/**
	 * Create the dialog.
	 */
	public DocQueryDialog(Collection<DocType> types) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		thisDialog = this;
		setTitle("Document Query");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{105, 307, 0};
			gbl_contentPanel.rowHeights = new int[]{23, 0, 23, 23, 57, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
			chckbxDocType = new JCheckBox("Document Type");
			chckbxDocType.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					docTypes.setEnabled(chckbxDocType.isSelected());
				}
			});
			{
				chckbxStartDate = new JCheckBox("Start Date");
				chckbxStartDate.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						startDate.setEnabled(chckbxStartDate.isSelected());
					}
				});
				{
					chckbxReferenceNumber = new JCheckBox("Reference Number");
					GridBagConstraints gbc_chckbxReferenceNumber = new GridBagConstraints();
					gbc_chckbxReferenceNumber.anchor = GridBagConstraints.NORTHWEST;
					gbc_chckbxReferenceNumber.insets = new Insets(0, 0, 5, 5);
					gbc_chckbxReferenceNumber.gridx = 0;
					gbc_chckbxReferenceNumber.gridy = 1;
					contentPanel.add(chckbxReferenceNumber, gbc_chckbxReferenceNumber);
				}
				{
					referenceNumber = new JTextField();
					GridBagConstraints gbc_referenceNumber = new GridBagConstraints();
					gbc_referenceNumber.fill = GridBagConstraints.HORIZONTAL;
					gbc_referenceNumber.insets = new Insets(0, 0, 5, 0);
					gbc_referenceNumber.gridx = 1;
					gbc_referenceNumber.gridy = 1;
					contentPanel.add(referenceNumber, gbc_referenceNumber);
					referenceNumber.setColumns(10);
				}
				GridBagConstraints gbc_chckbxStartDate = new GridBagConstraints();
				gbc_chckbxStartDate.anchor = GridBagConstraints.NORTHWEST;
				gbc_chckbxStartDate.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxStartDate.gridx = 0;
				gbc_chckbxStartDate.gridy = 2;
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
					gbc_startDate.gridy = 2;
					contentPanel.add(startDate, gbc_startDate);
					startDate.setColumns(10);
				}
				GridBagConstraints gbc_chckbxEndDate = new GridBagConstraints();
				gbc_chckbxEndDate.anchor = GridBagConstraints.NORTHWEST;
				gbc_chckbxEndDate.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxEndDate.gridx = 0;
				gbc_chckbxEndDate.gridy = 3;
				contentPanel.add(chckbxEndDate, gbc_chckbxEndDate);
			}
			{
				endDate = new JTextField();
				endDate.setEnabled(false);
				GridBagConstraints gbc_endDate = new GridBagConstraints();
				gbc_endDate.fill = GridBagConstraints.HORIZONTAL;
				gbc_endDate.insets = new Insets(0, 0, 5, 0);
				gbc_endDate.gridx = 1;
				gbc_endDate.gridy = 3;
				contentPanel.add(endDate, gbc_endDate);
				endDate.setColumns(10);
			}
			GridBagConstraints gbc_chckbxDocType = new GridBagConstraints();
			gbc_chckbxDocType.anchor = GridBagConstraints.WEST;
			gbc_chckbxDocType.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxDocType.gridx = 0;
			gbc_chckbxDocType.gridy = 4;
			contentPanel.add(chckbxDocType, gbc_chckbxDocType);
		}
		{
			JScrollPane typeScrollPane = new JScrollPane();
			GridBagConstraints gbc_typeScrollPane = new GridBagConstraints();
			gbc_typeScrollPane.anchor = GridBagConstraints.WEST;
			gbc_typeScrollPane.fill = GridBagConstraints.BOTH;
			gbc_typeScrollPane.gridx = 1;
			gbc_typeScrollPane.gridy = 4;
			contentPanel.add(typeScrollPane, gbc_typeScrollPane);
			{
				docTypes = new JList<DocType>();
				docTypes.setListData(types.toArray(new DocType[1]));
				typeScrollPane.setViewportView(docTypes);
				docTypes.setEnabled(false);
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

	public void newTypes(DocType[] newTypes)
	{
		docTypes.setListData(newTypes);
	}
}
