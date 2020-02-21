package ca.mcgill.ecse223.quoridor.view;

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

public class ResignConfirmation extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ResignConfirmation() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel resignConfirmationLabel = new JLabel("Are you sure you want to resign the game?");
		resignConfirmationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		resignConfirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton yesButton = new JButton("Yes");
		
		JButton noButton = new JButton("No");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(resignConfirmationLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(122, Short.MAX_VALUE)
					.addComponent(yesButton)
					.addGap(51)
					.addComponent(noButton)
					.addGap(117))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(47)
					.addComponent(resignConfirmationLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(yesButton)
						.addComponent(noButton))
					.addContainerGap(100, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			
				dispose();
				try {
					QuoridorGameController.ResignGame();
					new GameResult().setVisible(true); 
				} catch (InvalidInputException e) {
					e.printStackTrace();
				}
			}
		});
		
		noButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			
				dispose();
			}
		});
		this.setLocationRelativeTo(null);
	}

}
