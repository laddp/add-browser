package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JScrollPane;

import net.pladd.ADDBrowser.E3types.PostingCode;

public class TransactionQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425113802994773769L;
	private TransactionQueryDialog thisDialog;
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	
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
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		{
			chckbxAccountNumber = new JCheckBox("Account Number");
			chckbxAccountNumber.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					accountNum.setEnabled(chckbxAccountNumber.isSelected());
				}
			});
			contentPanel.add(chckbxAccountNumber, "2, 2");
		}
		{
			accountNum = new JTextField();
			accountNum.setEnabled(false);
			contentPanel.add(accountNum, "4, 2, fill, default");
			accountNum.setColumns(10);
		}
		{
			chckbxStartDate = new JCheckBox("Start Date");
			chckbxStartDate.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					startDate.setEnabled(chckbxStartDate.isSelected());
				}
			});
			contentPanel.add(chckbxStartDate, "2, 4");
		}
		{
			startDate = new JTextField();
			startDate.setEnabled(false);
			contentPanel.add(startDate, "4, 4, fill, default");
			startDate.setColumns(10);
		}
		{
			chckbxEndDate = new JCheckBox("End Date");
			chckbxEndDate.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					endDate.setEnabled(chckbxEndDate.isSelected());
				}
			});
			contentPanel.add(chckbxEndDate, "2, 6");
		}
		{
			endDate = new JTextField();
			endDate.setEnabled(false);
			contentPanel.add(endDate, "4, 6, fill, default");
			endDate.setColumns(10);
		}
		{
			chckbxPostingCodes = new JCheckBox("Posting Codes");
			chckbxPostingCodes.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					postingCodes.setEnabled(chckbxPostingCodes.isSelected());
				}
			});
			contentPanel.add(chckbxPostingCodes, "2, 8");
		}
		{
			scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "4, 8, left, fill");
			{
				postingCodes = new JList<PostingCode>(postCodes);
				scrollPane.setViewportView(postingCodes);
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
			contentPanel.add(chckbxReferenceNum, "2, 10");
		}
		{
			referenceNumber = new JTextField();
			referenceNumber.setEnabled(false);
			contentPanel.add(referenceNumber, "4, 10, fill, default");
			referenceNumber.setColumns(10);
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
		scrollPane.remove(postingCodes);
		postingCodes = new JList<PostingCode>(newCodes);
		postingCodes.setEnabled(chckbxPostingCodes.isSelected());
		scrollPane.setViewportView(postingCodes	);
	}
}
