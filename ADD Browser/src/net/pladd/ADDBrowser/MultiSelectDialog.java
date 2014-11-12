package net.pladd.ADDBrowser;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class MultiSelectDialog<SelectionType> extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6540920711431882841L;
	private final JPanel contentPanel = new JPanel();

	public boolean okSelected;
	private JList<SelectionType> list;
	
	/**
	 * Create the dialog.
	 */
	public MultiSelectDialog(Window parent, String title,
			SelectionType[] selectFrom)
	{
		super(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{1.0};
		gbl_contentPanel.rowWeights = new double[]{1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			contentPanel.add(scrollPane, gbc_scrollPane);
			list = new JList<SelectionType>();
			list.setVisibleRowCount(12);
			list.setListData(selectFrom);
			scrollPane.setViewportView(list);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okSelected = true;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okSelected = false;
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		pack();
	}
	
	public List<SelectionType> doSelect()
	{
		okSelected = false;
		setVisible(true);
		
		if (okSelected == false)
			return null;
		
		if (list.getSelectedValue() == null)
			return null;
		
		return list.getSelectedValuesList();
	}
}
