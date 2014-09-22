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
	protected JCheckBox chckbxLogGroup;
	protected JList<LogGroup> logGroups;
	protected JCheckBox chckbxLogType;
	protected JList<LogGroup> logType;


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
			chckbxLogGroup = new JCheckBox("Log Group");
			chckbxLogGroup.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					logGroups.setEnabled(chckbxLogGroup.isSelected());
				}
			});
			contentPanel.add(chckbxLogGroup, "2, 8");
		}
		{
			JScrollPane groupScrollPane = new JScrollPane();
			contentPanel.add(groupScrollPane, "4, 8, left, fill");
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
			contentPanel.add(chckbxLogType, "2, 10");
		}
		{
			JScrollPane logTypeScrollPane = new JScrollPane();
			contentPanel.add(logTypeScrollPane, "4, 10, fill, fill");
			{
				logType = new JList<LogGroup>();
				logTypeScrollPane.setViewportView(logType);
				logType.setEnabled(false);
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
