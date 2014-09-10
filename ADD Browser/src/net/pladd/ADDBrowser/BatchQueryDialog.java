package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class BatchQueryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2937888841036174027L;
	private final BatchQueryDialog thisDialog;
	private final JPanel contentPanel = new JPanel();

	protected boolean OKpressed = false;

	protected JCheckBox  chkPostingDate;
	protected JTextField postingDate;
	
	protected JCheckBox  chkBatchNumber;
	protected JTextField batchNumber;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BatchQueryDialog dialog = new BatchQueryDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BatchQueryDialog() {
		thisDialog = this;
		setTitle("Batch Query");
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		setBounds(100, 100, 450, 150);
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
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			chkPostingDate = new JCheckBox("Posting Date");
			chkPostingDate.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					postingDate.setEnabled(chkPostingDate.isSelected());
				}
			});
			contentPanel.add(chkPostingDate, "2, 2, left, default");
		}
		{
			postingDate = new JTextField();
			postingDate.setEnabled(false);
			contentPanel.add(postingDate, "4, 2, fill, default");
			postingDate.setColumns(10);
		}
		{
			chkBatchNumber = new JCheckBox("Batch Number");
			chkBatchNumber.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					batchNumber.setEnabled(chkBatchNumber.isSelected());
				}
			});
			contentPanel.add(chkBatchNumber, "2, 4, left, default");
		}
		{
			batchNumber = new JTextField();
			batchNumber.setEnabled(false);
			contentPanel.add(batchNumber, "4, 4, fill, default");
			batchNumber.setColumns(10);
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
}
