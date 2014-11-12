package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		{
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{93, 319, 0};
			gbl_contentPanel.rowHeights = new int[]{23, 23, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			contentPanel.setLayout(gbl_contentPanel);
			chkPostingDate = new JCheckBox("Posting Date");
			chkPostingDate.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					postingDate.setEnabled(chkPostingDate.isSelected());
				}
			});
			GridBagConstraints gbc_chkPostingDate = new GridBagConstraints();
			gbc_chkPostingDate.anchor = GridBagConstraints.NORTHWEST;
			gbc_chkPostingDate.insets = new Insets(0, 0, 5, 5);
			gbc_chkPostingDate.gridx = 0;
			gbc_chkPostingDate.gridy = 0;
			contentPanel.add(chkPostingDate, gbc_chkPostingDate);
			{
				postingDate = new JTextField();
				postingDate.setEnabled(false);
				GridBagConstraints gbc_postingDate = new GridBagConstraints();
				gbc_postingDate.fill = GridBagConstraints.HORIZONTAL;
				gbc_postingDate.insets = new Insets(0, 0, 5, 0);
				gbc_postingDate.gridx = 1;
				gbc_postingDate.gridy = 0;
				contentPanel.add(postingDate, gbc_postingDate);
				postingDate.setColumns(10);
			}
			{
				chkBatchNumber = new JCheckBox("Batch Number");
				chkBatchNumber.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						batchNumber.setEnabled(chkBatchNumber.isSelected());
					}
				});
				GridBagConstraints gbc_chkBatchNumber = new GridBagConstraints();
				gbc_chkBatchNumber.anchor = GridBagConstraints.NORTHWEST;
				gbc_chkBatchNumber.insets = new Insets(0, 0, 0, 5);
				gbc_chkBatchNumber.gridx = 0;
				gbc_chkBatchNumber.gridy = 1;
				contentPanel.add(chkBatchNumber, gbc_chkBatchNumber);
			}
			{
				batchNumber = new JTextField();
				batchNumber.setEnabled(false);
				GridBagConstraints gbc_batchNumber = new GridBagConstraints();
				gbc_batchNumber.fill = GridBagConstraints.HORIZONTAL;
				gbc_batchNumber.gridx = 1;
				gbc_batchNumber.gridy = 1;
				contentPanel.add(batchNumber, gbc_batchNumber);
				batchNumber.setColumns(10);
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
}
