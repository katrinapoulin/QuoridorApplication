package ca.mcgill.ecse223.quoridor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class GameResult extends JFrame {

	private JPanel contentPane;
	private static JLabel gameStatusLabel = new JLabel();
	public static String gameSts = null;


	/**
	 * Create the frame.
	 */
	public GameResult() {

		refreshData();
		initComponents();
		refreshData();
	}


	private void refreshData() {
		
		gameSts = QuoridorGameController.checkGameResult();
	

		String gameSts = QuoridorGameController.checkGameResult();

		if(gameSts.equals("Drawn")) {
			gameStatusLabel.setText("The game has ended in a draw.");
		}
		if(gameSts.equals("WhiteWon")) {
			gameStatusLabel.setText("White player has won the game!");
		}
		if(gameSts.equals("BlackWon")) {
			gameStatusLabel.setText("Black player has won the game!");
		}

	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel Header = new JLabel("The game has ended!");
		Header.setFont(new Font("Lucida Grande", Font.PLAIN, 23));

		gameStatusLabel = new JLabel("");
		gameStatusLabel.setForeground(Color.green.darker());
		gameStatusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JButton btnReplay = new JButton("Replay");
		btnReplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BoardView.getReplayButton().doClick();
				dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(97)
							.addComponent(exitButton)
							.addGap(62)
							.addComponent(btnReplay))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(87)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(gameStatusLabel)
								.addComponent(Header))))
					.addContainerGap(115, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(Header)
					.addGap(33)
					.addComponent(gameStatusLabel)
					.addGap(45)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnReplay)
						.addComponent(exitButton))
					.addContainerGap(95, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		this.setLocationRelativeTo(null);
	}
}
