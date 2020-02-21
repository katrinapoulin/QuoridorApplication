package ca.mcgill.ecse223.quoridor.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.JButton;

public class QuitConfirmation extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public QuitConfirmation() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel guitConfirmationLabel = new JLabel("Are you sure you want to quit the game?");
		guitConfirmationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		guitConfirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel progressWillBeLostLabel = new JLabel("All your progress will be lost.");
		progressWillBeLostLabel.setForeground(Color.RED);
		progressWillBeLostLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton yesButton = new JButton("Yes");
		
		JButton noButton = new JButton("No");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(guitConfirmationLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(122, Short.MAX_VALUE)
					.addComponent(yesButton)
					.addGap(51)
					.addComponent(noButton)
					.addGap(117))
				.addComponent(progressWillBeLostLabel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(47)
					.addComponent(guitConfirmationLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressWillBeLostLabel)
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(yesButton)
						.addComponent(noButton))
					.addContainerGap(100, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			
				System.exit(0);
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