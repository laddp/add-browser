package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JList;

public class TransactionQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425113802994773769L;
	private final JPanel contentPanel = new JPanel();
	private JTextField startDate;
	private JTextField endDate;
	private JTextField accountNum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TransactionQueryDialog dialog = new TransactionQueryDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TransactionQueryDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JCheckBox chckbxDateRange = new JCheckBox("Date Range");
			contentPanel.add(chckbxDateRange, "2, 2");
		}
		{
			startDate = new JTextField();
			contentPanel.add(startDate, "4, 2, fill, default");
			startDate.setColumns(10);
		}
		{
			endDate = new JTextField();
			contentPanel.add(endDate, "6, 2, fill, default");
			endDate.setColumns(10);
		}
		{
			JCheckBox chckbxPostingCodes = new JCheckBox("Posting Codes");
			contentPanel.add(chckbxPostingCodes, "2, 4");
		}
		{
			JList list = new JList();
			contentPanel.add(list, "4, 4, fill, fill");
		}
		{
			JCheckBox chckbxAccountNumber = new JCheckBox("Account Number");
			contentPanel.add(chckbxAccountNumber, "2, 6");
		}
		{
			accountNum = new JTextField();
			contentPanel.add(accountNum, "4, 6, fill, default");
			accountNum.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
