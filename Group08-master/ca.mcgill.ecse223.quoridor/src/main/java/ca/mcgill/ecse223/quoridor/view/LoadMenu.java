package ca.mcgill.ecse223.quoridor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorGameController;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoadMenu extends JFrame {

	private JPanel contentPane;
	private JComboBox<String> comboBox;
	private JFrame board;


	/**
	 * Create the frame.
	 */
	
	
	public LoadMenu() {
		initComponents();
		refreshData();
	}
	
	public LoadMenu(BoardView board) {
		this.board = board;
		initComponents();
		refreshData();
	}

	/**
	 * Create the frame.
	 */
	public void initComponents() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 683, 251);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel guitConfirmationLabel = new JLabel("Select a file to load from (horizontal):");
		guitConfirmationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		guitConfirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		comboBox = new JComboBox<String>();
		
		JButton btnLoadGame = new JButton("Load Game");
		btnLoadGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					QuoridorGameController.loadGameHorizontal(comboBox.getSelectedItem().toString());
					BoardView.refreshData();
					dispose();
					if(board != null) {
						board.setVisible(false);
					}
					new BoardView().setVisible(true);
					
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(guitConfirmationLabel, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
					.addGap(51)
					.addComponent(btnLoadGame)
					.addGap(73))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(guitConfirmationLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLoadGame))
					.addContainerGap(143, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		this.setLocationRelativeTo(null);
	}
	
	void refreshData(){
		comboBox.removeAllItems();
		for(String fileName: QuoridorGameController.getFileNames()) {
			comboBox.addItem(fileName);
		}
	}
}
