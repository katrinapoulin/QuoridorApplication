package ca.mcgill.ecse223.quoridor.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.Time;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

public class TimeMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String error;
	private JLabel errorMessage;
	private JComboBox selectMinutesComboBox;
	private JComboBox selectSecondsComboBox;

	/**
	 * Create the frame.
	 */
	public TimeMenu() {

		initComponents();
		refreshData();
	}

	// Creates the contentPane
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton backButton = new JButton("< Back");
		backButton.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("Select Total Thinking Time:");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// Creates selection of 0-59 for minutes and seconds
		selectMinutesComboBox = new JComboBox ();
		for (int i = 0 ; i < 60 ; i ++) {
			if ( i < 10) selectMinutesComboBox.addItem("0" + i);
			else selectMinutesComboBox.addItem(""+i);
		}

		selectSecondsComboBox = new JComboBox();
		for (int i = 0 ; i < 60 ; i ++) {
			if ( i < 10) selectSecondsComboBox.addItem("0" + i);
			else selectSecondsComboBox.addItem(""+i);
		}

		JLabel selectMinutesLabel = new JLabel("Select Minutes:");

		JLabel selectSecondsLabel = new JLabel("Select Seconds:");

		// Layout control
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.RED);
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(backButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
						.addComponent(nextButton))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(87)
										.addComponent(selectMinutesLabel, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(75)
										.addComponent(selectMinutesComboBox, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(46)
										.addComponent(selectSecondsLabel, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(38)
										.addComponent(selectSecondsComboBox, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(79, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(errorMessage)
						.addContainerGap(434, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(nextButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(backButton))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(selectMinutesLabel)
								.addComponent(selectSecondsLabel))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(selectMinutesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(selectSecondsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(28)
						.addComponent(errorMessage)
						.addGap(25))
				);
		contentPane.setLayout(gl_contentPane);

		// Back button returns to PlayerMenu screen
		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				dispose();
				new PlayerMenu().setVisible(true);
			}
		});

		// Back button goes to BoardView and sets the inputed total thinking time using the controller
		nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				
				//boardView.getBtnStart().doClick();
				int min = Integer.parseInt((String)selectMinutesComboBox.getSelectedItem());
				int sec = Integer.parseInt((String)selectSecondsComboBox.getSelectedItem());

				try {
					QuoridorGameController.setTotalThinkingTime(min, sec);
					
					dispose();
					new BoardView().setVisible(true);
					
					//boardView.getBtnStart().doClick();

				} catch (InvalidInputException e) {
					error = e.getMessage();

				}
				refreshData();


			}
		});

		pack();
		this.setLocationRelativeTo(null);
	}

	// refreshes the error message (total time can't be 0)
	private void refreshData() {
		errorMessage.setText(error);
	}
	
}
