package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;

public class QuoridorMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public QuoridorMenu() {

		initComponents();
	}
		
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Quoridor Game");
		setBounds(200, 200, 535, 361);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	
		
		JLabel lblQuoridor = new JLabel("Quoridor");
		lblQuoridor.setVerticalAlignment(SwingConstants.TOP);
		lblQuoridor.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuoridor.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
	
		JButton btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.setBackground(Color.WHITE);
	
		JButton btnLoadGame = new JButton("Load Game");
	
	
	
	
	
	
	
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(99)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnLoadGame, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
								.addComponent(btnStartNewGame, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
						.addGap(114))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
						.addGap(115)
							.addComponent(lblQuoridor, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(130, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(25)
						.addComponent(lblQuoridor)
						.addGap(55)
						.addComponent(btnStartNewGame, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(btnLoadGame, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(23, Short.MAX_VALUE))
				);
		contentPane.setLayout(gl_contentPane);

	


		//Listeners
				btnStartNewGame.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
					
						QuoridorGameController.startNewGame();
						dispose();
						new PlayerMenu().setVisible(true);
					}
				});
			
				btnLoadGame.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						dispose();
						new LoadMenu().setVisible(true);
						
					}
					
				});

				pack();
				this.setLocationRelativeTo(null);
	} 
}
