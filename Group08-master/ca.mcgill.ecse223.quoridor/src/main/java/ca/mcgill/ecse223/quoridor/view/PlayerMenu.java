package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;
import ca.mcgill.ecse223.quoridor.controller.TOUser;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PlayerMenu extends JFrame {

	private JPanel contentPane;
	private static JTextField txtUsername1;
	private JComboBox<String> user1ToggleList;
	private JLabel errorMessage;
	private String error;
	
	private JTextField txtUsername2;
	private JComboBox<String> user2ToggleList;
	
	private static List<String> users;
	private JButton addUser1;

	
	public PlayerMenu() {

		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 378, 378);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblPlayer = new JLabel("Player 1");
		lblPlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		
		JLabel lblWhitePlayer = new JLabel("White Player");
		lblWhitePlayer.setForeground(SystemColor.inactiveCaptionBorder);
		lblWhitePlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JLabel lblEnterAUsername = new JLabel("Enter a Username: ");
		
		txtUsername1 = new JTextField();
		txtUsername1.setColumns(10);
		
		JLabel lblOr = new JLabel("or");
		
		JLabel lblSelectExistingUsername = new JLabel("Select Existing Username: ");
		
		user1ToggleList = new JComboBox<String>();
		
		JSeparator separator = new JSeparator();
		
		JLabel lblPlayer_1 = new JLabel("Player 2");
		lblPlayer_1.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		
		JLabel lblBlackPlayer = new JLabel("Black Player");
		lblBlackPlayer.setForeground(SystemColor.inactiveCaption);
		lblBlackPlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JLabel label = new JLabel("Enter a Username: ");
		
		JLabel label_1 = new JLabel("or");
		
		JLabel label_2 = new JLabel("Select Existing Username: ");
		JButton btnNext = new JButton("Next >");
		
		
		txtUsername2 = new JTextField();
		txtUsername2.setColumns(10);
		
		user2ToggleList = new JComboBox<String>();
		
		addUser1 = new JButton("Add Username");
		
		JButton addUser2 = new JButton("Add Username");
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(560)
					.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(167)
					.addComponent(lblOr)
					.addContainerGap(455, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(169)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(453, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(120)
							.addComponent(errorMessage, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPlayer_1, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblPlayer)
												.addComponent(lblWhitePlayer))
											.addGap(17)
											.addComponent(lblEnterAUsername))
										.addComponent(lblSelectExistingUsername))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(user1ToggleList, 0, 170, Short.MAX_VALUE)
										.addComponent(txtUsername1, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(lblBlackPlayer, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
											.addGap(57)
											.addComponent(label, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
									.addGap(18)
									.addComponent(txtUsername2, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
						.addComponent(user2ToggleList, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addComponent(addUser1))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(addUser2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
					.addGap(62))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPlayer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWhitePlayer)
						.addComponent(lblEnterAUsername)
						.addComponent(txtUsername1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(addUser1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblOr)
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectExistingUsername)
						.addComponent(user1ToggleList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPlayer_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBlackPlayer, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(txtUsername2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(addUser2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(user2ToggleList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2))
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(errorMessage)
						.addComponent(btnNext))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		
		
		
		
		//Listeners
		
		addUser1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addUser1ActionPerformed(evt);
			}
		});
		
		addUser2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			addUser2ActionPerformed(evt);
			}
		});
		
		
		btnNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				btnNextActionPerformed(evt);
				
				int a = user1ToggleList.getSelectedIndex();
				int b = user2ToggleList.getSelectedIndex();
				if (a != -1 && b != -1) {
				dispose();
				new TimeMenu().setVisible(true);}
			}
		});
		
		
		
		pack();
		this.setLocationRelativeTo(null);
		
		
		
	}
	
	public void refreshData(){
		
		errorMessage.setText(error);
		if (error == null || error.length() == 0) {
			// populate page with data
		
			txtUsername1.setText("");
			txtUsername2.setText("");
			
			users = new ArrayList<String>();
			user1ToggleList.removeAllItems();
			user2ToggleList.removeAllItems();
			
			for (TOUser user : QuoridorGameController.getUsers()) {
				if(!((user.getName().equals("user1")|| (user.getName().equals("user2"))))){
					users.add(user.getName());
					user1ToggleList.addItem(user.getName());
					user2ToggleList.addItem(user.getName());
				}
			};
			user1ToggleList.setSelectedIndex(-1);
			user2ToggleList.setSelectedIndex(-1);
			
			}
	}
	
	

	private void addUser1ActionPerformed(java.awt.event.ActionEvent evt) {
		
		error = "";
		
		if(txtUsername1.getText().equals("")) {
		error = "Username field can not be empty";
		}
		
	if(error.length() == 0) {
		try {
		QuoridorGameController.createUser(txtUsername1.getText());
	
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
	}	
	refreshData();
		
	}
	
private void addUser2ActionPerformed(java.awt.event.ActionEvent evt) {
		
		error = "";
		
		if(txtUsername2.getText().equals("")) {
		error = "Username field can not be empty";
		}
		else {
		try {
		QuoridorGameController.createUser(txtUsername2.getText());
	
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
	}	
	refreshData();
		
	}
	
	
	
	private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {
		
		error = "";
		int a = user1ToggleList.getSelectedIndex();
		int b = user2ToggleList.getSelectedIndex();
		int c = -1;
		if (a == c || b == c) {
			error= "Please select a Username";
		}
		if (a >= 0 && b >= 0) {
		try{
		QuoridorGameController.setWhitePlayerUsername((String)user1ToggleList.getSelectedItem());
		QuoridorGameController.setBlackPlayerUsername((String)user2ToggleList.getSelectedItem());
		
		
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
		return;
	  }
		
		refreshData();
	}	
		
		
			
	public static JTextField getTxtUsername1() {
		return txtUsername1;
	}
	public  JLabel getErrorMessage() {
		return errorMessage;
	}
	public JButton getAddUser1() {
		return addUser1;
	}
}
