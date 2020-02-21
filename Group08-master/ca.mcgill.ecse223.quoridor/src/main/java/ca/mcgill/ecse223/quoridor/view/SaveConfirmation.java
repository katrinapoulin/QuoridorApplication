package ca.mcgill.ecse223.quoridor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;

import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SaveConfirmation extends JFrame {

	private JPanel contentPane;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtSavefilename;
	private JLabel errorMsg;
	String error = "";
	String overWrite ="";
	
	boolean forceSave = false;
	private JLabel lblWow;

	/**
	 * Create the frame.
	 */
	public SaveConfirmation() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel guitConfirmationLabel = new JLabel("Please enter a name for your savefile:");
		guitConfirmationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		guitConfirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtSavefilename = new JTextField();
		txtSavefilename.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Boolean fileExists = false;
				
				
				if(forceSave == false) {
					try {
						fileExists = QuoridorGameController.fileExists(txtSavefilename.getText());
					} catch (InvalidInputException e2) {
						error = e2.getMessage();
						errorMsg.setText(error);
						txtSavefilename.setText("");
						e2.printStackTrace();
					}

					if(fileExists){
						overWrite = "The file " + txtSavefilename.getText() + " already exists in the filesystem. Overwrite it?";
						btnSave.setText("Force Save");
						lblWow.setText(overWrite);
						forceSave = true;

					} else {
						try {
							QuoridorGameController.saveGame(txtSavefilename.getText());
							dispose();
						} catch (InvalidInputException e1) {
							error = e1.getMessage();
							errorMsg.setText(error);
							e1.printStackTrace();
						}

					}
				} else { //force save is true
					try {
						QuoridorGameController.forceSaveGame(txtSavefilename.getText());
						dispose();
					} catch (InvalidInputException e1) {
						error = e1.getMessage();
						e1.printStackTrace();
						
					}
					//save was forced
					
				}
			}
		});
		
		errorMsg = new JLabel("");
		errorMsg.setForeground(Color.RED);
		
		lblWow = new JLabel("");
		lblWow.setForeground(new Color(255, 69, 0));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(guitConfirmationLabel, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
					.addGap(34))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(63)
					.addComponent(txtSavefilename, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(btnSave)
					.addContainerGap(131, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(75)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblWow)
						.addComponent(errorMsg))
					.addContainerGap(347, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(35)
					.addComponent(guitConfirmationLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(txtSavefilename, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addComponent(errorMsg)
					.addGap(18)
					.addComponent(lblWow)
					.addContainerGap(52, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		this.setLocationRelativeTo(null);
	}
}
