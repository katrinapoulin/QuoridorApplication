package ca.mcgill.ecse223.quoridor.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.border.LineBorder;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.*;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.PawnSM;
import ca.mcgill.ecse223.quoridor.controller.TOWall.Direction;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial")
public class BoardView extends JFrame {

	private JPanel contentPane;
	public static JPanel panel_3;

	// board game control buttons
	public static JButton prevButton;
	public static JButton nextButton;
	public static JButton addWallButton;
	public static JButton jumpStartVar;
	public static JButton jumpEndVar;
	public static JButton cancelAddWallBtn;
	public static JButton rotateWallButton;
	public static JButton replayButton;
	public static int hitcount = 0;

	public static GridBagLayout gbl_panel_3;
	private JLabel minutesRemainingLabel;
	private JLabel secondsRemainingLabel;
	private static JLabel usernameLabel;
	private static JLabel remainingWallsLabel;
	private static JLabel notices;
	private JFrame load = new LoadMenu(this);
	private static JPanel wallCandidate;
	private static Integer whitePawnRow = 1;
	private static Integer whitePawnCol = 1;
	private static Integer blackPawnRow = 1;
	private static Integer blackPawnCol = 1;
	private static boolean addingWall = false;
	private static boolean isHorizontal = true;
	private static boolean addButtonActivated = false;
	private static boolean validCandidate = true;
	private static String gameStatus = null;
	private static Component match;
	private static Component matchBlack;
	private static ArrayList<ActionPoint> points = new ArrayList<ActionPoint>();
	static ArrayList<TOWall> walls = new ArrayList<TOWall>();
	static ArrayList<int[]> gameStates = new ArrayList<int[]>();
	public static JPanel replayPanel = new JPanel();
	public static GridBagLayout gbl = new GridBagLayout();

	public static int newWallRow;
	public static int newWallCol;
	public static int newWallDir;
	public static int replayIndex = 0;

	public static boolean inReplayMode = false;

	public static TOWall wallMoveCandidate = null;
	public static PawnBehavior pawnBehavior = null;

	public static String currentPlayerUserName = "<username>";

	private static Integer remainingWalls = -1;

	@SuppressWarnings("deprecation")
	private static Time remainingTime = new Time(0, 9 ,9);
	
	public static boolean tilesEnabled = false;


	static Timer timer = new Timer(0, null);
	static int min;
	static int sec;
	private static JButton resignGameButton;




	/**
	 * Create the frame.
	 */
	public BoardView() {
		load.setVisible(false);
		refreshData();
		initComponents();

	}

	public BoardView(PawnBehavior pawnBehavior) {
		load.setVisible(false);
		refreshData();
		initComponents();
		BoardView.pawnBehavior = pawnBehavior;
		System.out.println("In here");

	}


	public void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1228, 626);
		setBounds(100, 100, 1228, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		notices = new JLabel("");

		notices.setBounds(930, 542, 242, 36);

		contentPane.add(notices);
		notices.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		notices.setHorizontalAlignment(SwingConstants.CENTER);
		notices.setForeground(Color.RED);

		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		menuPanel.setBackground(Color.LIGHT_GRAY);
		menuPanel.setForeground(SystemColor.window);
		menuPanel.setBounds(0, 0, 318, 394);
		contentPane.add(menuPanel);

		JLabel quoridorMenuLabel = new JLabel("Quoridor Menu:");
		quoridorMenuLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 38));
		quoridorMenuLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton saveGameButton = new JButton("Save Game");
		saveGameButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));

		JButton loadGameButton = new JButton("Load Game");
		loadGameButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));

		JButton quitGameButton = new JButton("Quit Game");
		quitGameButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));

		GroupLayout gl_menuPanel = new GroupLayout(menuPanel);
		gl_menuPanel.setHorizontalGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_menuPanel
				.createSequentialGroup()
				.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_menuPanel.createSequentialGroup().addGap(56)
								.addGroup(gl_menuPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(quitGameButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(saveGameButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(loadGameButton, GroupLayout.PREFERRED_SIZE, 199,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(
								gl_menuPanel.createSequentialGroup().addContainerGap().addComponent(quoridorMenuLabel)))
				.addContainerGap(26, Short.MAX_VALUE)));
		gl_menuPanel.setVerticalGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_menuPanel.createSequentialGroup().addContainerGap().addComponent(quoridorMenuLabel)
						.addGap(24).addComponent(saveGameButton).addGap(63).addComponent(loadGameButton).addGap(58)
						.addComponent(quitGameButton).addGap(57)));
		menuPanel.setLayout(gl_menuPanel);

		JPanel timePanel = new JPanel();
		timePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		timePanel.setBackground(Color.LIGHT_GRAY);
		timePanel.setBounds(0, 394, 318, 184);
		contentPane.add(timePanel);

		JLabel remainingTimeLabel = new JLabel("Remaining Time:");
		remainingTimeLabel.setForeground(Color.BLACK);
		remainingTimeLabel.setBackground(Color.WHITE);
		remainingTimeLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));
		remainingTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		minutesRemainingLabel = new JLabel("00");
		secondsRemainingLabel = new JLabel("00");

		if(remainingTime.getMinutes() < 10) {
		minutesRemainingLabel = new JLabel("0"+remainingTime.getMinutes());
		}
		else {
		minutesRemainingLabel = new JLabel(""+remainingTime.getMinutes());
		}
		min = Integer.parseInt(minutesRemainingLabel.getText());

		minutesRemainingLabel.setFont(new Font("Dialog", Font.PLAIN, 32));

		secondsRemainingLabel = new JLabel("00");
		if (remainingTime.getSeconds() < 10) {
			secondsRemainingLabel = new JLabel("0" + remainingTime.getSeconds());

		} else {
			secondsRemainingLabel = new JLabel("" + remainingTime.getSeconds());
		}
		sec = Integer.parseInt(secondsRemainingLabel.getText());

		secondsRemainingLabel.setFont(new Font("Dialog", Font.PLAIN, 32));

		JLabel label = new JLabel(":");
		label.setFont(new Font("Dialog", Font.PLAIN, 36));
		
		resignGameButton = new JButton ("Resign Game");
		resignGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new ResignConfirmation().setVisible(true);
			}
		});
		resignGameButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 12));
		GroupLayout gl_timePanel = new GroupLayout(timePanel);
		gl_timePanel.setHorizontalGroup(
			gl_timePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_timePanel.createSequentialGroup()
					.addContainerGap(48, Short.MAX_VALUE)
					.addComponent(remainingTimeLabel)
					.addGap(42))
				.addGroup(Alignment.LEADING, gl_timePanel.createSequentialGroup()
					.addGap(84)
					.addGroup(gl_timePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(resignGameButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_timePanel.createSequentialGroup()
							.addComponent(minutesRemainingLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(secondsRemainingLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(83, Short.MAX_VALUE))
		);
		gl_timePanel.setVerticalGroup(
			gl_timePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_timePanel.createSequentialGroup()
					.addGap(28)
					.addComponent(remainingTimeLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_timePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(secondsRemainingLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addComponent(minutesRemainingLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(resignGameButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		timePanel.setLayout(gl_timePanel);

		/*
		 * for (Wall wall : gpos.getBlackWallsInStock(){ if(wall.getid == size()) do
		 * things
		 *
		 */

		JPanel playerTurnPanel = new JPanel();
		playerTurnPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		playerTurnPanel.setBackground(Color.LIGHT_GRAY);
		playerTurnPanel.setBounds(895, 0, 305, 164);
		contentPane.add(playerTurnPanel);

		JLabel itsLabel = new JLabel("It's");
		itsLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 24));

		usernameLabel = new JLabel(currentPlayerUserName); // will be updated in refresh

		usernameLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 24));

		JLabel turnLabel = new JLabel("'s turn");
		turnLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 24));
		GroupLayout gl_playerTurnPanel = new GroupLayout(playerTurnPanel);
		gl_playerTurnPanel.setHorizontalGroup(gl_playerTurnPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_playerTurnPanel.createSequentialGroup().addGap(25).addComponent(itsLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(usernameLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(turnLabel)
						.addContainerGap(27, Short.MAX_VALUE)));
		gl_playerTurnPanel.setVerticalGroup(gl_playerTurnPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_playerTurnPanel.createSequentialGroup().addGap(65)
						.addGroup(gl_playerTurnPanel.createParallelGroup(Alignment.BASELINE).addComponent(itsLabel)
								.addComponent(usernameLabel).addComponent(turnLabel))
						.addContainerGap(67, Short.MAX_VALUE)));
		playerTurnPanel.setLayout(gl_playerTurnPanel);

		JPanel remainingWallsPanel = new JPanel();
		remainingWallsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		remainingWallsPanel.setBackground(Color.LIGHT_GRAY);
		remainingWallsPanel.setBounds(895, 163, 305, 104);
		contentPane.add(remainingWallsPanel);

		JLabel remainingWallsTextLabel = new JLabel("Remaining Walls:");
		remainingWallsTextLabel.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 24));

		remainingWallsLabel = new JLabel("" + remainingWalls);
		remainingWallsLabel.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 24));
		GroupLayout gl_remainingWallsPanel = new GroupLayout(remainingWallsPanel);
		gl_remainingWallsPanel.setHorizontalGroup(gl_remainingWallsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_remainingWallsPanel.createSequentialGroup().addGap(32)
						.addComponent(remainingWallsTextLabel).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(remainingWallsLabel).addGap(33)));
		gl_remainingWallsPanel.setVerticalGroup(gl_remainingWallsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_remainingWallsPanel.createSequentialGroup().addGap(34)
						.addGroup(gl_remainingWallsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(remainingWallsTextLabel).addComponent(remainingWallsLabel))
						.addContainerGap(38, Short.MAX_VALUE)));
		remainingWallsPanel.setLayout(gl_remainingWallsPanel);

		panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.control);
		panel_3.setPreferredSize(new Dimension(560, 560));
		panel_3.setBounds(327, 6, 560, 560);
		contentPane.add(panel_3);
		gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0 };
		gbl_panel_3.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0 };
		gbl_panel_3.columnWidths = new int[] { 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50 };
		gbl_panel_3.rowHeights = new int[] { 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50, 10, 50 };
		// gbl_panel_3.columnWidths = new int[] {50, 30, 50, 30, 50, 30, 50, 30, 50, 30,
		// 50, 30, 50, 30, 50, 30, 50};
		// gbl_panel_3.rowHeights = new int[] {50, 30, 50, 30, 50, 30, 50, 30, 50, 30,
		// 50, 30, 50, 30, 50, 30, 50};
		//

		rotateWallButton = new JButton("Rotate Wall");
		rotateWallButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				isHorizontal = !isHorizontal;
				refreshData(panel_3, gbl_panel_3);
			}
		});
		rotateWallButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 24));
		rotateWallButton.setBounds(895, 372, 305, 48);
		contentPane.add(rotateWallButton);

		cancelAddWallBtn = new JButton("Cancel");
		cancelAddWallBtn.setBounds(895, 338, 305, 36);
		contentPane.add(cancelAddWallBtn);

		cancelAddWallBtn.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		cancelAddWallBtn.setEnabled(false);

		cancelAddWallBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshData(panel_3, gbl_panel_3);
				QuoridorGameController.cancelCandidate();
				refreshData(panel_3, gbl_panel_3);
				addingWall = false;
			}

		});

		addWallButton = new JButton("Add wall to board");
		addWallButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				QuoridorGameController.dropPawn();
				refreshData(panel_3, gbl_panel_3);
				addingWall = !addingWall;

				if (remainingWalls > 0) {
					if (addingWall) {
						if (wallMoveCandidate == null) {
							QuoridorGameController.createCandidate();

							refreshData(panel_3, gbl_panel_3);
						} else {
							QuoridorGameController.getCandidate();

							refreshData(panel_3, gbl_panel_3);
						}
					}
				} else {
					notices.setText("You have no walls remaining.");
					addingWall = false;
				}
			}

		});
		addWallButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 24));
		addWallButton.setBounds(895, 283, 305, 56);
		contentPane.add(addWallButton);
		

		final GridBagConstraints gc = new GridBagConstraints();
		gc.gridheight = 9;
		gc.gridwidth = 9;
		gc.fill = GridBagConstraints.BOTH;
		gbl_panel_3.setConstraints(panel_3, gc);
		panel_3.setLayout(gbl_panel_3);

		initQboard(panel_3, gbl_panel_3, true);

		GridBagLayout layout = gbl_panel_3;

		WindowListener taskStarterWindowListener = new WindowListener() {
			public void windowOpened(WindowEvent e) {

				refreshData(panel_3, gbl_panel_3);

				timer = new Timer(1000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						refreshClock();
						
						

						if (sec == 0) {
							sec = 60;
							min--;
						}

						if(!inReplayMode) {
							sec--;

							}
						
						remainingTime.setMinutes(min);
						remainingTime.setSeconds(sec);

						if (min < 10)
							minutesRemainingLabel.setText("0" + min);
						else
							minutesRemainingLabel.setText("" + min);

						if (sec < 10)
							secondsRemainingLabel.setText("0" + sec);
						else
							secondsRemainingLabel.setText("" + sec);
					}
				});
				timer.start();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		};

		// Here is where the magic happens! We make (a listener within) the frame start
		// listening to the frame's own events!
		this.addWindowListener(taskStarterWindowListener);

		replayButton = new JButton("Replay Mode: Off");

		replayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				hitcount++;
				inReplayMode = !inReplayMode;
				refreshData(panel_3, gbl_panel_3);
				replayModeButtons(inReplayMode);
				if (inReplayMode == true) {
					replayButton.setText("ReplayMode: On");
					replayModeIn();

					notices.setVisible(true);
					notices.setText("GAME IN REPLAY MODE");
				} else {
					if(QuoridorGameController.wasRunning) {
					replayButton.setText("ReplayMode: Off");
					QuoridorGameController.exitReplayMode();
					notices.setText("");
					replayModeOut();
					}
				}
				refreshData(panel_3, gbl_panel_3);

			}
		});
		replayButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 24));
		replayButton.setBounds(895, 417, 305, 48);
		contentPane.add(replayButton);

		nextButton = new JButton("Next Move");
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				nextMove();
			}
		});
		nextButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		nextButton.setBounds(1046, 464, 154, 48);
		nextButton.setEnabled(false);
		contentPane.add(nextButton);

		prevButton = new JButton("Previous Move");
		prevButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				prevMove();
			}
		});
		prevButton.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		prevButton.setBounds(895, 464, 154, 48);
		prevButton.setEnabled(false);
		contentPane.add(prevButton);
		
		//Jump to start things
		jumpStartVar = new JButton("JumpStart");
		jumpStartVar.setBounds(895, 513, 154, 29);
		contentPane.add(jumpStartVar);
		jumpStartVar.setEnabled(false);
		
		/**
		 * This method will iterate through all the moves to bring back to the first one
		 * Author: Kyle
		 */
		jumpStartVar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				int moveNum = quoridor.getCurrentGame().getMoves().size();
				for(int i=0; i<moveNum; i++) {
					prevMove();
				}
			}
		});
		
		jumpEndVar = new JButton("JumpEnd");
		jumpEndVar.setBounds(1046, 513, 154, 29);
		contentPane.add(jumpEndVar);
		jumpEndVar.setEnabled(false);

		/**
		 * This method will iterate through all the moves to bring back to the first one
		 * Author: Kyle
		 */
		jumpEndVar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				int moveNum = quoridor.getCurrentGame().getMoves().size();
				for(int i=0; i<moveNum; i++) {
					nextMove();
				}
			}
		});
		int x;
		int y;

		for (TOWall wall : walls) { // for each wall object
			Component matchBlack = null;
			if (wall.getDirection() == TOWall.Direction.Horizontal) {// it's horizontal

				for (int i = 0; i < 3; i++) {
					x = (wall.getCol() * 2) - 4 + i;
					y = (wall.getRow() * 2) - 3;
					for (Component comp : panel_3.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == x && gbc.gridy == y) {
							matchBlack = comp;
							// System.out.println(gbc.gridx + "" + gbc.gridy);
							break;
						}
					}

					matchBlack.setBackground(Color.BLUE);
				}

			} else {

				for (int i = 0; i < 3; i++) {
					x = (wall.getCol() * 2) - 3;
					y = (wall.getRow() * 2) - 4 + i;
					for (Component comp : panel_3.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == x && gbc.gridy == y) {
							matchBlack = comp;
							// System.out.println(gbc.gridx + "" + gbc.gridy);
							break;
						}
					}
					matchBlack.setBackground(Color.BLUE);
				}
			}

		}

		loadGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				load.setVisible(true);

			}
		});
		quitGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new QuitConfirmation().setVisible(true);
			}
		});
		saveGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new SaveConfirmation().setVisible(true);
			}
		});
		
		
		colorWalls(panel_3,gbl_panel_3);
		
		
	}

	private void listenersTile(JPanel tile, GridBagConstraints gbc_tile, JPanel panel, GridBagLayout layout) {
		addingWall = false;
		tile.addMouseListener(new MouseAdapter() {

			 	@Override
	        	public void mouseClicked(MouseEvent e) {
//				 	if(
			 		tilesEnabled = true;
			 		if(tile.getBackground() == Color.white ) {
			 			
	 	        		//ArrayList<ActionPoint> points = QuoridorGameController.getPossibleMoves((whitePawnCol-1)*2, (whitePawnRow-1)*2);
	 	        		if(QuoridorGameController.pawnBehavior.getPawnSM() == PawnSM.MyMove && QuoridorGameController.getPlayerToMove().hasGameAsWhite()) {
	 	        			QuoridorGameController.grabPawn();
	 	   
	 	        		} else {
	 						QuoridorGameController.dropPawn();
	 					}   
	 	        		refreshData(panel, layout);
	
			 		} 
			 		else if (tile.getBackground() == Color.BLACK) {
			 			if(QuoridorGameController.pawnBehavior.getPawnSM() == PawnSM.MyMove && QuoridorGameController.getPlayerToMove().hasGameAsBlack()) {
	 	        			QuoridorGameController.grabPawn();
	 	   
	 	        		} else {
	 						QuoridorGameController.dropPawn();
	 					}   
	 	        		refreshData(panel, layout);

			 		}
			 			else {
			 		}
			 		for(ActionPoint point : points) {
			 			refreshData(panel, layout);
			 			
			 			if(gbc_tile.gridx == point.getPoint().getX() && gbc_tile.gridy == point.getPoint().getY()) {
			        		
			 				boolean eventProcessed = false;
			 				
			 				
			 				if(point.getAction().equals("up")) {
			 					if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.North)) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.stepPawnNorth();
			 					} else {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnNorth();
			 					}
			 				} 
			 				else if (point.getAction().equals("down")) {
			 					if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.South)) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.stepPawnSouth();
			 					} else {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnSouth();
			 					}
			 				} 
			 				else if (point.getAction().contentEquals("left")) {
			 					if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.West)) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.stepPawnWest();

			 					} else {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnWest();
			 					}
			 					
			 				} 
			 				else if (point.getAction().contentEquals("right")) {
			 					if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.East)) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.stepPawnEast();
			 					} else {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnEast();
			 					}
			 					
			 				} 
			 				
			 				
			 				
			 				else if (point.getAction().contentEquals("upleft")) {
			 					eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnNorthWest();
			 					if(eventProcessed == false) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnWestNorth();
			 					}
			 					//try both
			 				} else if (point.getAction().contentEquals("downleft")) {
			 					eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnSouthWest();
			 					if(eventProcessed == false) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnWestSouth();
			 					}
			 				} else if (point.getAction().contentEquals("upright")) {
			 					eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnNorthEast();
			 					if(eventProcessed == false) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnEastNorth();
			 					}
			 				} else if (point.getAction().contentEquals("downright")) {
			 					eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnSouthEast();
			 					if(eventProcessed == false) {
			 						eventProcessed = QuoridorGameController.pawnBehavior.jumpPawnEastSouth();
			 					}
			 				} 
	
			 				//refreshData(panel, layout);
			 				 				
				 		}
			 				
			 		}
			 		refreshData(panel, layout);	
			 	}
			 	
	        	
		 });
	}
	
	private void listeners(JPanel wallM, GridBagConstraints gbc_wallM, JPanel panel, GridBagLayout layout) {
		wallM.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (remainingWalls > 0) {
					// System.out.println("pressed");
					QuoridorGameController.updateCandidate(wallMoveCandidate, (gbc_wallM.gridy / 2) + 2,
							(gbc_wallM.gridx / 2) + 2, isHorizontal);
					addWall(gbc_wallM, panel, layout);

				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (remainingWalls > 0) {
					if (addingWall) {
						if (wallMoveCandidate == null) {
							QuoridorGameController.createCandidate();
						}
						activeWall(gbc_wallM, panel, layout);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (remainingWalls > 0) {
					if (addingWall) {
						inActiveWall(gbc_wallM, panel, layout);
					}
				}
			}
		});

	}

	public void activeWall(GridBagConstraints constraint, JPanel panel, GridBagLayout layout) {
		refreshData(panel, layout);
		validCandidate = true;
		int actualRow = 9;
		int actualCol = 9;

		Component match = null;
		validCandidate = QuoridorGameController.wallMoveValid();
		// System.out.println("valid candidate: " + validCandidate);

		if (isHorizontal) {
			for (int i = 0; i < 3; i++) {
				int x = constraint.gridx - 1 + i;
				int y = constraint.gridy;
				// int x = (constraint.gridx*2)-4+i;
				// int y = (constraint.gridy*2)-3;
				for (Component comp : panel.getComponents()) {
					GridBagConstraints gbc = layout.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						// System.out.println("entering green");
						match = comp;
						if (i == 1) {
							actualRow = (gbc.gridy / 2) + 2;
							actualCol = (gbc.gridx / 2) + 2;
							// System.out.println((actualCol) + "<-column | row ->" + (actualRow));

						}
						if (match.getBackground() != Color.BLUE) {
							match.setBackground(Color.green);

						}
					}
				}

			}
		}

		else {
			for (int i = 0; i < 3; i++) {
				int x = constraint.gridx;
				int y = constraint.gridy - 1 + i;
				// int x = (constraint.gridx*2)-4+i;
				// int y = (constraint.gridy*2)-3;
				for (Component comp : panel.getComponents()) {
					GridBagConstraints gbc = layout.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						// System.out.println("entering green");
						match = comp;
						if (i == 1) {
							// System.out.println((gbc.gridx/2)+2 + "<-column | row ->" + (gbc.gridy)/2+2);
						}
						if (match.getBackground() != Color.BLUE) {
							match.setBackground(Color.green);
						}
					}
				}

			}

		}

		if (match != null) {
			System.out.println(constraint.gridx);
			QuoridorGameController.updateCandidate(wallMoveCandidate, actualRow, actualCol, isHorizontal);
			System.out.println(actualRow + "row" + actualCol + "col");
		}

		refreshData(panel, layout);
	}

	public void inActiveWall(GridBagConstraints constraint, JPanel panel, GridBagLayout layout) {
		Component match = null;
		refreshData(panel, layout);
		validCandidate = true;
		int actualRow = 9;
		int actualCol = 9;
		if (isHorizontal) {
			for (int i = 0; i < 3; i++) {
				int x = constraint.gridx - 1 + i;
				int y = constraint.gridy;
				// int x = (constraint.gridx*2)-4+i;
				// int y = (constraint.gridy*2)-3;
				for (Component comp : panel.getComponents()) {
					GridBagConstraints gbc = layout.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						// System.out.println("entering green");
						match = comp;
						// System.out.println(gbc.gridx + "" + gbc.gridy);
						if (i == 1) {
							actualRow = (gbc.gridy / 2) + 2;
							actualCol = (gbc.gridx / 2) + 2;
							QuoridorGameController.updateCandidate(wallMoveCandidate, actualRow, actualCol,
									isHorizontal);

						}
						if (match.getBackground() != Color.BLUE) {
							match.setBackground(new Color(240, 240, 240));
						}
					}
				}
			}

		} else {

			for (int i = 0; i < 3; i++) {
				int x = constraint.gridx;
				int y = constraint.gridy - 1 + i;
				// int x = (constraint.gridx*2)-4+i;
				// int y = (constraint.gridy*2)-3;
				for (Component comp : panel.getComponents()) {
					GridBagConstraints gbc = layout.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						// System.out.println("entering green");
						match = comp;
						// System.out.println(gbc.gridx + "" + gbc.gridy);
						if (match.getBackground() != Color.BLUE) {
							match.setBackground(new Color(240, 240, 240));
						}
					}
				}

			}
		}
		validCandidate = true;
	}
	// REFRESH METHOD
	static boolean loadGame = false;
	public static void refreshData(JPanel panel, GridBagLayout layout) {
		refreshData();
		resetColors(panel, layout);
		

		// add all the other listeners
		int x = (whitePawnCol - 1) * 2;
		int y = (whitePawnRow - 1) * 2;

		for (Component comp : panel.getComponents()) {
			GridBagConstraints gbc = layout.getConstraints(comp);

			if (gbc.gridx == x && gbc.gridy == y) {
				comp.setBackground(Color.white);
				// System.out.println(gbc.gridx + "" + gbc.gridy);
				break;
			}
		}

		int xBlack = (blackPawnCol - 1) * 2;
		int yBlack = (blackPawnRow - 1) * 2;

		for (Component comp : panel.getComponents()) {
			GridBagConstraints gbc = layout.getConstraints(comp);

			if (gbc.gridx == xBlack && gbc.gridy == yBlack) {
				comp.setBackground(Color.BLACK);
				break;
			}
		}
		
		
		
		

		if (!inReplayMode) {
			if (QuoridorGameController.pawnBehavior.getPawnSM() == PawnSM.PawnInHand) {
				// QuoridorGameController.grabPawn();

				for (ActionPoint point : points) {
					for (Component comp : panel.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == point.getPoint().x && gbc.gridy == point.getPoint().y) {
							comp.setBackground(Color.yellow);

						}
					}
				}

			} else {
				for (ActionPoint point : points) {
					for (Component comp : panel.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == point.getPoint().x && gbc.gridy == point.getPoint().y) {
							comp.setBackground(Color.GRAY);
							// QuoridorGameController.dropPawn();
						}
					}

				}
			}
		}
	}


	public static void refreshClock() {

		gameStatus = QuoridorGameController.checkGameResult();
		if((gameStatus.equals("WhiteWon") || gameStatus.equals("BlackWon")) || gameStatus.equals("Drawn"))  {
			timer.stop();
			new GameResult().setVisible(true);
		}
	}



	public static void refreshData() {

		gameStatus = QuoridorGameController.checkGameResult();
		if(gameStatus.equals("WhiteWon") || gameStatus.equals("BlackWon")) {
			timer.stop();
			new GameResult().setVisible(true);
		}

		TOPlayer whitePlayer = QuoridorGameController.getWhitePlayer();
		TOPlayer blackPlayer = QuoridorGameController.getBlackPlayer();
		// System.out.println(QuoidorGameController.getPlayerToMove().toString());

		whitePawnRow = whitePlayer.getRow();
		whitePawnCol = whitePlayer.getCol();
		blackPawnRow = blackPlayer.getRow();
		blackPawnCol = blackPlayer.getCol();


		wallMoveCandidate = QuoridorGameController.getCandidate();
		if (wallMoveCandidate != null) {
			QuoridorGameController.updateCandidate(wallMoveCandidate, wallMoveCandidate.getRow(),
					wallMoveCandidate.getCol(), isHorizontal);
			// System.out.println(wallMoveCandidate.getRow());
		}

		// System.out.println(QuoridorGameController.getPlayerToMove().getUser().toString());
		if (QuoridorGameController.getPlayerToMove().hasGameAsWhite()) {
			// System.out.println("white");
			currentPlayerUserName = whitePlayer.getUser().toString();
			remainingWalls = whitePlayer.getRemainingWalls();
			remainingTime = whitePlayer.getRemainingTime();
			if(pawnBehavior == null) {
				//System.out.println("Wow");
				points = QuoridorGameController.getPossibleMoves((whitePawnCol-1)*2, (whitePawnRow-1)*2);
			}
			else {
				points = QuoridorGameController.getPossibleMoves(pawnBehavior, (whitePawnCol-1)*2, (whitePawnRow-1)*2);
			}
			min = remainingTime.getMinutes();
			sec = remainingTime.getSeconds();


		} else {
			// System.out.println("black");
			currentPlayerUserName = blackPlayer.getUser().toString();
			remainingWalls = blackPlayer.getRemainingWalls();
			remainingTime = blackPlayer.getRemainingTime();
			points = QuoridorGameController.getPossibleMoves((blackPawnCol-1)*2, (blackPawnRow-1)*2);
			min = remainingTime.getMinutes();
			sec = remainingTime.getSeconds();
			if(currentPlayerUserName.contentEquals("skynet")) {
				int dir = QuoridorGameController.optimalPath(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(), QuoridorApplication.getQuoridor().getCurrentGame());
				if(dir == 0) {
					QuoridorGameController.stepPawn(MoveDirection.East);
				} else if (dir == 1) {
					QuoridorGameController.stepPawn(MoveDirection.West);
				} else if (dir == 2) {
					QuoridorGameController.stepPawn(MoveDirection.North);
				} else if (dir == 3) {
					QuoridorGameController.stepPawn(MoveDirection.South);
				} else if (dir == -1){
					if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.East)) {
						QuoridorGameController.stepPawn(MoveDirection.North);
					} else if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.North)) {
						QuoridorGameController.stepPawn(MoveDirection.North);
					} else if(QuoridorGameController.pawnBehavior.isLegalStep(MoveDirection.South)) {
						QuoridorGameController.stepPawn(MoveDirection.South);
					} else {
						QuoridorGameController.stepPawn(MoveDirection.West);
					}
				}
				
				refreshData();
				
			}
		}
		// System.out.println(currentPlayerUserName);
		if (usernameLabel != null) {
			setUsername(currentPlayerUserName);
			setNumberOfWalls(remainingWalls);
			if (!inReplayMode) {
				notices.setText("");
			}
			if (!inReplayMode) {
				addWallButton.setEnabled(!addingWall);
				cancelAddWallBtn.setEnabled(addingWall);
				replayButton.setEnabled(!addingWall);
				rotateWallButton.setEnabled(addingWall);
			}

		}


		// points = QuoridorGameController.getPossibleMoves((blackPawnCol-1)*2,
		// (blackPawnRow-1)*2);
	}

	public static void setUsername(String s) {
		usernameLabel.setText(s);
	}

	public static void setNumberOfWalls(Integer n) {
		remainingWallsLabel.setText("" + n);
	}

	public static void resetColors(JPanel panel, GridBagLayout layout) {
		// if (!inReplayMode) {
		for (Component comp : panel.getComponents()) {
			GridBagConstraints gbc = layout.getConstraints(comp);

			if (comp.getBackground() == Color.white || comp.getBackground() == Color.BLACK
					|| comp.getBackground() == Color.yellow) {
				comp.setBackground(Color.GRAY);
			}
		}
	}

	public static void colorWalls(JPanel panel, GridBagLayout layout) {
		//Component comp = null;
		for (Component comp : panel_3.getComponents()) {
		if (comp.getBackground() == Color.BLUE) {
			comp.setBackground(new Color (240, 240, 240));
		}}
		for(TOWall wall : QuoridorGameController.getWalls()) {
		boolean isHorizontal = (wall.getDirection().equals(Direction.Horizontal));
		int row = wall.getRow();
		int col = wall.getCol();
		if (isHorizontal) {
			for (int i = 0; i < 3; i++) {
				int x = 2*(col-2)+ i;
				int y = 2*(row-2)+1;
				// System.out.println("entering horizontal for loop");
				for (Component comp : panel.getComponents()) {
					GridBagConstraints gbc = layout.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						comp.setBackground(Color.BLUE);
						// System.out.println(gbc.gridx + "" + gbc.gridy);
						loadGame = true;
						break;
					}
				}
			}
			}
		else {
				for (int i = 0; i < 3; i++) {
					int x = 2*(col-2);
					int y = 2*(row-2)-1+i;
					// System.out.println("entering horizontal for loop");
					for (Component comp : panel.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == x && gbc.gridy == y) {
							comp.setBackground(Color.BLUE);
							loadGame =true;
							// System.out.println(gbc.gridx + "" + gbc.gridy);
							break;
						}
					}
				}
		}}
	}
	public void addWall(GridBagConstraints constraint, JPanel panel, GridBagLayout layout) {
		refreshData(panel, layout);
		validCandidate = true;
		int actualRow = 0;
		int actualCol = 0;
		Component match = null;
		validCandidate = QuoridorGameController.wallMoveValid();

		if (validCandidate) {
			// QuoridorGameController.updateCandidate(wallMoveCandidate, currentX,
			// currentY);
			if (isHorizontal) {
				newWallDir = 1;
				for (int i = 0; i < 3; i++) {
					int x = constraint.gridx - 1 + i;
					int y = constraint.gridy;
					// System.out.println("entering horizontal for loop");
					for (Component comp : panel.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == x && gbc.gridy == y) {
							match = comp;
							if (i == 1) {
								actualRow = (gbc.gridy / 2) + 2;
								actualCol = (gbc.gridx / 2) + 2;
								QuoridorGameController.updateCandidate(wallMoveCandidate, actualRow, actualCol,
										isHorizontal);

							}
							// System.out.println(gbc.gridx + "" + gbc.gridy);
							break;
						}
					}

					if (match != null)
						match.setBackground(Color.BLUE);

				}

			} else {
				newWallDir = 2;
				for (int i = 0; i < 3; i++) {
					// System.out.println("entering vertical for loop");
					int x = (constraint.gridx);
					int y = (constraint.gridy) - 1 + i;
					for (Component comp : panel.getComponents()) {
						GridBagConstraints gbc = layout.getConstraints(comp);

						if (gbc.gridx == x && gbc.gridy == y) {
							match = comp;
							if (i == 1) {
								actualRow = (gbc.gridy / 2) + 2;
								actualCol = (gbc.gridx / 2) + 2;
								QuoridorGameController.updateCandidate(wallMoveCandidate, actualRow, actualCol,
										isHorizontal);

							}
							// System.out.println(gbc.gridx + "" + gbc.gridy);
							break;
						}
					}

					match.setBackground(Color.BLUE);
				}
			}
			newWallRow = actualRow;
			newWallCol = actualCol;
			QuoridorGameController.dropCandidate();
			wallMoveCandidate = null;
			addingWall = false;
			refreshData(panel, layout);

		} else {
			System.out.println("Invalid position");
			notices.setText("Invalid Position");
		}
	}

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	/**
	 * This method removes a given wall on the board.
	 *
	 * @author Katrina Poulin
	 * @param wall Wall Transfer Object to be removed
	 */
	public static void removeWall(TOWall wall) {
		int x = (wall.getCol() - 1) * 2 - 1;
		int y = (wall.getRow() - 1) * 2 - 1;
		TOWall wallToRemove = null;

		for (TOWall myWall : walls) {
			if (myWall.equals(wall)) {
				wallToRemove = myWall;
			}
		}

		for (int i = 0; i < walls.size(); i++) {
			if (walls.get(i).equals(wallToRemove))
				walls.remove(i);
		}
		for (Component comp : panel_3.getComponents()) {
			GridBagConstraints gbc = gbl_panel_3.getConstraints(comp);
			if (gbc.gridx == x && gbc.gridy == y) {
				match = comp;
				comp.setBackground(new Color(240, 240, 240));
			}
			if (wall.getDirection().equals(Direction.Vertical)) {
				if (gbc.gridx == x && gbc.gridy == y - 1) {
					match = comp;
					comp.setBackground(new Color(240, 240, 240));
				}
				if (gbc.gridx == x && gbc.gridy == y + 1) {
					match = comp;
					comp.setBackground(new Color(240, 240, 240));
				}
			} else {
				if (gbc.gridx == x - 1 && gbc.gridy == y) {
					match = comp;
					comp.setBackground(new Color(240, 240, 240));
				}
				if (gbc.gridx == x + 1 && gbc.gridy == y) {
					match = comp;
					comp.setBackground(new Color(240, 240, 240));
				}

			}
		}
	}

	void initQboard(JPanel panel_3, GridBagLayout gbl_panel_3, boolean replay) {
		JPanel tile00 = new JPanel();
		tile00.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile00 = new GridBagConstraints();
		gbc_tile00.insets = new Insets(0, 0, 0, 0);
		gbc_tile00.fill = GridBagConstraints.BOTH;
		gbc_tile00.gridx = 0;
		gbc_tile00.gridy = 0;
		panel_3.add(tile00, gbc_tile00);

		JPanel wallV00 = new JPanel();
		GridBagConstraints gbc_wallV00 = new GridBagConstraints();
		gbc_wallV00.insets = new Insets(0, 0, 0, 0);
		gbc_wallV00.fill = GridBagConstraints.BOTH;
		gbc_wallV00.gridx = 1;
		gbc_wallV00.gridy = 0;
		panel_3.add(wallV00, gbc_wallV00);

		JPanel tile01 = new JPanel();

		tile01.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile01 = new GridBagConstraints();
		gbc_tile01.insets = new Insets(0, 0, 0, 0);
		gbc_tile01.fill = GridBagConstraints.BOTH;
		gbc_tile01.gridx = 2;
		gbc_tile01.gridy = 0;
		panel_3.add(tile01, gbc_tile01);
		listenersTile(tile01, gbc_tile01, panel_3, gbl_panel_3);

		JPanel wallV01 = new JPanel();
		GridBagConstraints gbc_wallV01 = new GridBagConstraints();
		gbc_wallV01.insets = new Insets(0, 0, 0, 0);
		gbc_wallV01.fill = GridBagConstraints.BOTH;
		gbc_wallV01.gridx = 3;
		gbc_wallV01.gridy = 0;
		panel_3.add(wallV01, gbc_wallV01);

		JPanel tile02 = new JPanel();

		tile02.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile02 = new GridBagConstraints();
		gbc_tile02.insets = new Insets(0, 0, 0, 0);
		gbc_tile02.fill = GridBagConstraints.BOTH;
		gbc_tile02.gridx = 4;
		gbc_tile02.gridy = 0;
		panel_3.add(tile02, gbc_tile02);

		listenersTile(tile02, gbc_tile02, panel_3, gbl_panel_3);

		JPanel wallV02 = new JPanel();
		GridBagConstraints gbc_wallV02 = new GridBagConstraints();
		gbc_wallV02.insets = new Insets(0, 0, 0, 0);
		gbc_wallV02.fill = GridBagConstraints.BOTH;
		gbc_wallV02.gridx = 5;
		gbc_wallV02.gridy = 0;
		panel_3.add(wallV02, gbc_wallV02);

		JPanel tile03 = new JPanel();

		tile03.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile03 = new GridBagConstraints();
		gbc_tile03.insets = new Insets(0, 0, 0, 0);
		gbc_tile03.fill = GridBagConstraints.BOTH;
		gbc_tile03.gridx = 6;
		gbc_tile03.gridy = 0;
		panel_3.add(tile03, gbc_tile03);

		listenersTile(tile03, gbc_tile03, panel_3, gbl_panel_3);

		JPanel wallV03 = new JPanel();
		GridBagConstraints gbc_wallV03 = new GridBagConstraints();
		gbc_wallV03.insets = new Insets(0, 0, 0, 0);
		gbc_wallV03.fill = GridBagConstraints.BOTH;
		gbc_wallV03.gridx = 7;
		gbc_wallV03.gridy = 0;
		panel_3.add(wallV03, gbc_wallV03);

		JPanel tile04 = new JPanel();

		tile04.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile04 = new GridBagConstraints();
		gbc_tile04.insets = new Insets(0, 0, 0, 0);
		gbc_tile04.fill = GridBagConstraints.BOTH;
		gbc_tile04.gridx = 8;
		gbc_tile04.gridy = 0;
		panel_3.add(tile04, gbc_tile04);

		listenersTile(tile04, gbc_tile04, panel_3, gbl_panel_3);

		JPanel wallV04 = new JPanel();
		GridBagConstraints gbc_wallV04 = new GridBagConstraints();
		gbc_wallV04.insets = new Insets(0, 0, 0, 0);
		gbc_wallV04.fill = GridBagConstraints.BOTH;
		gbc_wallV04.gridx = 9;
		gbc_wallV04.gridy = 0;
		panel_3.add(wallV04, gbc_wallV04);

		JPanel tile05 = new JPanel();

		tile05.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile05 = new GridBagConstraints();
		gbc_tile05.insets = new Insets(0, 0, 0, 0);
		gbc_tile05.fill = GridBagConstraints.BOTH;
		gbc_tile05.gridx = 10;
		gbc_tile05.gridy = 0;
		panel_3.add(tile05, gbc_tile05);

		listenersTile(tile05, gbc_tile05, panel_3, gbl_panel_3);

		JPanel wallV05 = new JPanel();
		wallV05.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
			}
		});
		GridBagConstraints gbc_wallV05 = new GridBagConstraints();
		gbc_wallV05.insets = new Insets(0, 0, 0, 0);
		gbc_wallV05.fill = GridBagConstraints.BOTH;
		gbc_wallV05.gridx = 11;
		gbc_wallV05.gridy = 0;
		panel_3.add(wallV05, gbc_wallV05);

		JPanel tile06 = new JPanel();
		tile06.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile06 = new GridBagConstraints();
		gbc_tile06.insets = new Insets(0, 0, 0, 0);
		gbc_tile06.fill = GridBagConstraints.BOTH;
		gbc_tile06.gridx = 12;
		gbc_tile06.gridy = 0;
		panel_3.add(tile06, gbc_tile06);

		listenersTile(tile06, gbc_tile06, panel_3, gbl_panel_3);

		JPanel wallV06 = new JPanel();
		GridBagConstraints gbc_wallV06 = new GridBagConstraints();
		gbc_wallV06.insets = new Insets(0, 0, 0, 0);
		gbc_wallV06.fill = GridBagConstraints.BOTH;
		gbc_wallV06.gridx = 13;
		gbc_wallV06.gridy = 0;
		panel_3.add(wallV06, gbc_wallV06);

		JPanel tile07 = new JPanel();

		tile07.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile07 = new GridBagConstraints();
		gbc_tile07.insets = new Insets(0, 0, 0, 0);
		gbc_tile07.fill = GridBagConstraints.BOTH;
		gbc_tile07.gridx = 14;
		gbc_tile07.gridy = 0;
		panel_3.add(tile07, gbc_tile07);

		listenersTile(tile07, gbc_tile07, panel_3, gbl_panel_3);

		JPanel wallV07 = new JPanel();
		GridBagConstraints gbc_wallV07 = new GridBagConstraints();
		gbc_wallV07.insets = new Insets(0, 0, 0, 0);
		gbc_wallV07.fill = GridBagConstraints.BOTH;
		gbc_wallV07.gridx = 15;
		gbc_wallV07.gridy = 0;
		panel_3.add(wallV07, gbc_wallV07);

		JPanel tile08 = new JPanel();

		tile08.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile08 = new GridBagConstraints();
		gbc_tile08.insets = new Insets(0, 0, 0, 0);
		gbc_tile08.fill = GridBagConstraints.BOTH;
		gbc_tile08.gridx = 16;
		gbc_tile08.gridy = 0;
		panel_3.add(tile08, gbc_tile08);

		listenersTile(tile08, gbc_tile08, panel_3, gbl_panel_3);

		JPanel wallH00 = new JPanel();
		GridBagConstraints gbc_wallH00 = new GridBagConstraints();
		gbc_wallH00.insets = new Insets(0, 0, 0, 0);
		gbc_wallH00.fill = GridBagConstraints.BOTH;
		gbc_wallH00.gridx = 0;
		gbc_wallH00.gridy = 1;
		panel_3.add(wallH00, gbc_wallH00);

		JPanel wallM10 = new JPanel();
		GridBagConstraints gbc_wallM10 = new GridBagConstraints();
		gbc_wallM10.insets = new Insets(0, 0, 0, 0);
		gbc_wallM10.fill = GridBagConstraints.BOTH;
		gbc_wallM10.gridx = 1;
		gbc_wallM10.gridy = 1;
		panel_3.add(wallM10, gbc_wallM10);

		listeners(wallM10, gbc_wallM10, panel_3, gbl_panel_3);

		JPanel wallH01 = new JPanel();
		GridBagConstraints gbc_wallH01 = new GridBagConstraints();
		gbc_wallH01.insets = new Insets(0, 0, 0, 0);
		gbc_wallH01.fill = GridBagConstraints.BOTH;
		gbc_wallH01.gridx = 2;
		gbc_wallH01.gridy = 1;
		panel_3.add(wallH01, gbc_wallH01);

		JPanel wallM11 = new JPanel();

		GridBagConstraints gbc_wallM11 = new GridBagConstraints();
		gbc_wallM11.insets = new Insets(0, 0, 0, 0);
		gbc_wallM11.fill = GridBagConstraints.BOTH;
		gbc_wallM11.gridx = 3;
		gbc_wallM11.gridy = 1;
		panel_3.add(wallM11, gbc_wallM11);

		listeners(wallM11, gbc_wallM11, panel_3, gbl_panel_3);

		JPanel wallH02 = new JPanel();
		GridBagConstraints gbc_wallH02 = new GridBagConstraints();
		gbc_wallH02.insets = new Insets(0, 0, 0, 0);
		gbc_wallH02.fill = GridBagConstraints.BOTH;
		gbc_wallH02.gridx = 4;
		gbc_wallH02.gridy = 1;
		panel_3.add(wallH02, gbc_wallH02);

		JPanel wallM12 = new JPanel();
		GridBagConstraints gbc_wallM12 = new GridBagConstraints();
		gbc_wallM12.insets = new Insets(0, 0, 0, 0);
		gbc_wallM12.fill = GridBagConstraints.BOTH;
		gbc_wallM12.gridx = 5;
		gbc_wallM12.gridy = 1;
		panel_3.add(wallM12, gbc_wallM12);

		listeners(wallM12, gbc_wallM12, panel_3, gbl_panel_3);

		JPanel wallH03 = new JPanel();
		GridBagConstraints gbc_wallH03 = new GridBagConstraints();
		gbc_wallH03.insets = new Insets(0, 0, 0, 0);
		gbc_wallH03.fill = GridBagConstraints.BOTH;
		gbc_wallH03.gridx = 6;
		gbc_wallH03.gridy = 1;
		panel_3.add(wallH03, gbc_wallH03);

		JPanel wallM13 = new JPanel();

		GridBagConstraints gbc_wallM13 = new GridBagConstraints();
		gbc_wallM13.insets = new Insets(0, 0, 0, 0);
		gbc_wallM13.fill = GridBagConstraints.BOTH;
		gbc_wallM13.gridx = 7;
		gbc_wallM13.gridy = 1;
		panel_3.add(wallM13, gbc_wallM13);

		listeners(wallM13, gbc_wallM13, panel_3, gbl_panel_3);

		JPanel wallH04 = new JPanel();
		GridBagConstraints gbc_wallH04 = new GridBagConstraints();
		gbc_wallH04.insets = new Insets(0, 0, 0, 0);
		gbc_wallH04.fill = GridBagConstraints.BOTH;
		gbc_wallH04.gridx = 8;
		gbc_wallH04.gridy = 1;
		panel_3.add(wallH04, gbc_wallH04);

		JPanel wallM14 = new JPanel();

		GridBagConstraints gbc_wallM14 = new GridBagConstraints();
		gbc_wallM14.insets = new Insets(0, 0, 0, 0);
		gbc_wallM14.fill = GridBagConstraints.BOTH;
		gbc_wallM14.gridx = 9;
		gbc_wallM14.gridy = 1;
		panel_3.add(wallM14, gbc_wallM14);

		listeners(wallM14, gbc_wallM14, panel_3, gbl_panel_3);

		JPanel wallH05 = new JPanel();
		GridBagConstraints gbc_wallH05 = new GridBagConstraints();
		gbc_wallH05.insets = new Insets(0, 0, 0, 0);
		gbc_wallH05.fill = GridBagConstraints.BOTH;
		gbc_wallH05.gridx = 10;
		gbc_wallH05.gridy = 1;
		panel_3.add(wallH05, gbc_wallH05);

		JPanel wallM15 = new JPanel();

		GridBagConstraints gbc_wallM15 = new GridBagConstraints();
		gbc_wallM15.insets = new Insets(0, 0, 0, 0);
		gbc_wallM15.fill = GridBagConstraints.BOTH;
		gbc_wallM15.gridx = 11;
		gbc_wallM15.gridy = 1;
		panel_3.add(wallM15, gbc_wallM15);

		listeners(wallM15, gbc_wallM15, panel_3, gbl_panel_3);

		JPanel wallH06 = new JPanel();
		GridBagConstraints gbc_wallH06 = new GridBagConstraints();
		gbc_wallH06.insets = new Insets(0, 0, 0, 0);
		gbc_wallH06.fill = GridBagConstraints.BOTH;
		gbc_wallH06.gridx = 12;
		gbc_wallH06.gridy = 1;
		panel_3.add(wallH06, gbc_wallH06);

		JPanel wallM16 = new JPanel();

		GridBagConstraints gbc_wallM16 = new GridBagConstraints();
		gbc_wallM16.insets = new Insets(0, 0, 0, 0);
		gbc_wallM16.fill = GridBagConstraints.BOTH;
		gbc_wallM16.gridx = 13;
		gbc_wallM16.gridy = 1;
		panel_3.add(wallM16, gbc_wallM16);

		listeners(wallM16, gbc_wallM16, panel_3, gbl_panel_3);

		JPanel wallH07 = new JPanel();
		GridBagConstraints gbc_wallH07 = new GridBagConstraints();
		gbc_wallH07.insets = new Insets(0, 0, 0, 0);
		gbc_wallH07.fill = GridBagConstraints.BOTH;
		gbc_wallH07.gridx = 14;
		gbc_wallH07.gridy = 1;
		panel_3.add(wallH07, gbc_wallH07);

		JPanel wallM17 = new JPanel();

		GridBagConstraints gbc_wallM17 = new GridBagConstraints();
		gbc_wallM17.insets = new Insets(0, 0, 0, 0);
		gbc_wallM17.fill = GridBagConstraints.BOTH;
		gbc_wallM17.gridx = 15;
		gbc_wallM17.gridy = 1;
		panel_3.add(wallM17, gbc_wallM17);

		listeners(wallM17, gbc_wallM17, panel_3, gbl_panel_3);

		JPanel wallH08 = new JPanel();
		GridBagConstraints gbc_wallH08 = new GridBagConstraints();
		gbc_wallH08.insets = new Insets(0, 0, 0, 0);
		gbc_wallH08.fill = GridBagConstraints.BOTH;
		gbc_wallH08.gridx = 16;
		gbc_wallH08.gridy = 1;
		panel_3.add(wallH08, gbc_wallH08);

		JPanel tile09 = new JPanel();

		tile09.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile09 = new GridBagConstraints();
		gbc_tile09.insets = new Insets(0, 0, 0, 0);
		gbc_tile09.fill = GridBagConstraints.BOTH;
		gbc_tile09.gridx = 0;
		gbc_tile09.gridy = 2;
		panel_3.add(tile09, gbc_tile09);

		listenersTile(tile09, gbc_tile09, panel_3, gbl_panel_3);

		JPanel wallV09 = new JPanel();
		GridBagConstraints gbc_wallV09 = new GridBagConstraints();
		gbc_wallV09.insets = new Insets(0, 0, 0, 0);
		gbc_wallV09.fill = GridBagConstraints.BOTH;
		gbc_wallV09.gridx = 1;
		gbc_wallV09.gridy = 2;
		panel_3.add(wallV09, gbc_wallV09);

		JPanel tile10 = new JPanel();

		tile10.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile10 = new GridBagConstraints();
		gbc_tile10.insets = new Insets(0, 0, 0, 0);
		gbc_tile10.fill = GridBagConstraints.BOTH;
		gbc_tile10.gridx = 2;
		gbc_tile10.gridy = 2;
		panel_3.add(tile10, gbc_tile10);

		listenersTile(tile10, gbc_tile10, panel_3, gbl_panel_3);

		JPanel wallV10 = new JPanel();
		GridBagConstraints gbc_wallV10 = new GridBagConstraints();
		gbc_wallV10.insets = new Insets(0, 0, 0, 0);
		gbc_wallV10.fill = GridBagConstraints.BOTH;
		gbc_wallV10.gridx = 3;
		gbc_wallV10.gridy = 2;
		panel_3.add(wallV10, gbc_wallV10);

		JPanel tile11 = new JPanel();

		tile11.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile11 = new GridBagConstraints();
		gbc_tile11.insets = new Insets(0, 0, 0, 0);
		gbc_tile11.fill = GridBagConstraints.BOTH;
		gbc_tile11.gridx = 4;
		gbc_tile11.gridy = 2;
		panel_3.add(tile11, gbc_tile11);

		listenersTile(tile11, gbc_tile11, panel_3, gbl_panel_3);

		JPanel wallV11 = new JPanel();
		GridBagConstraints gbc_wallV11 = new GridBagConstraints();
		gbc_wallV11.insets = new Insets(0, 0, 0, 0);
		gbc_wallV11.fill = GridBagConstraints.BOTH;
		gbc_wallV11.gridx = 5;
		gbc_wallV11.gridy = 2;
		panel_3.add(wallV11, gbc_wallV11);

		JPanel tile12 = new JPanel();

		tile12.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile12 = new GridBagConstraints();
		gbc_tile12.insets = new Insets(0, 0, 0, 0);
		gbc_tile12.fill = GridBagConstraints.BOTH;
		gbc_tile12.gridx = 6;
		gbc_tile12.gridy = 2;
		panel_3.add(tile12, gbc_tile12);

		listenersTile(tile12, gbc_tile12, panel_3, gbl_panel_3);

		JPanel wallV12 = new JPanel();
		GridBagConstraints gbc_wallV12 = new GridBagConstraints();
		gbc_wallV12.insets = new Insets(0, 0, 0, 0);
		gbc_wallV12.fill = GridBagConstraints.BOTH;
		gbc_wallV12.gridx = 7;
		gbc_wallV12.gridy = 2;
		panel_3.add(wallV12, gbc_wallV12);

		JPanel tile13 = new JPanel();

		tile13.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile13 = new GridBagConstraints();
		gbc_tile13.insets = new Insets(0, 0, 0, 0);
		gbc_tile13.fill = GridBagConstraints.BOTH;
		gbc_tile13.gridx = 8;
		gbc_tile13.gridy = 2;
		panel_3.add(tile13, gbc_tile13);

		listenersTile(tile13, gbc_tile13, panel_3, gbl_panel_3);

		JPanel wallV13 = new JPanel();
		GridBagConstraints gbc_wallV13 = new GridBagConstraints();
		gbc_wallV13.insets = new Insets(0, 0, 0, 0);
		gbc_wallV13.fill = GridBagConstraints.BOTH;
		gbc_wallV13.gridx = 9;
		gbc_wallV13.gridy = 2;
		panel_3.add(wallV13, gbc_wallV13);

		JPanel tile14 = new JPanel();

		tile14.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile14 = new GridBagConstraints();
		gbc_tile14.insets = new Insets(0, 0, 0, 0);
		gbc_tile14.fill = GridBagConstraints.BOTH;
		gbc_tile14.gridx = 10;
		gbc_tile14.gridy = 2;
		panel_3.add(tile14, gbc_tile14);

		JPanel wallV14 = new JPanel();
		GridBagConstraints gbc_wallV14 = new GridBagConstraints();
		gbc_wallV14.insets = new Insets(0, 0, 0, 0);
		gbc_wallV14.fill = GridBagConstraints.BOTH;
		gbc_wallV14.gridx = 11;
		gbc_wallV14.gridy = 2;
		panel_3.add(wallV14, gbc_wallV14);

		JPanel tile15 = new JPanel();

		tile15.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile15 = new GridBagConstraints();
		gbc_tile15.insets = new Insets(0, 0, 0, 0);
		gbc_tile15.fill = GridBagConstraints.BOTH;
		gbc_tile15.gridx = 12;
		gbc_tile15.gridy = 2;
		panel_3.add(tile15, gbc_tile15);

		JPanel wallV15 = new JPanel();
		GridBagConstraints gbc_wallV15 = new GridBagConstraints();
		gbc_wallV15.insets = new Insets(0, 0, 0, 0);
		gbc_wallV15.fill = GridBagConstraints.BOTH;
		gbc_wallV15.gridx = 13;
		gbc_wallV15.gridy = 2;
		panel_3.add(wallV15, gbc_wallV15);

		JPanel tile16 = new JPanel();

		tile16.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile16 = new GridBagConstraints();
		gbc_tile16.insets = new Insets(0, 0, 0, 0);
		gbc_tile16.fill = GridBagConstraints.BOTH;
		gbc_tile16.gridx = 14;
		gbc_tile16.gridy = 2;
		panel_3.add(tile16, gbc_tile16);

		JPanel wallV16 = new JPanel();
		GridBagConstraints gbc_wallV16 = new GridBagConstraints();
		gbc_wallV16.insets = new Insets(0, 0, 0, 0);
		gbc_wallV16.fill = GridBagConstraints.BOTH;
		gbc_wallV16.gridx = 15;
		gbc_wallV16.gridy = 2;
		panel_3.add(wallV16, gbc_wallV16);

		JPanel tile17 = new JPanel();

		tile17.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile17 = new GridBagConstraints();
		gbc_tile17.insets = new Insets(0, 0, 0, 0);
		gbc_tile17.fill = GridBagConstraints.BOTH;
		gbc_tile17.gridx = 16;
		gbc_tile17.gridy = 2;
		panel_3.add(tile17, gbc_tile17);

		JPanel wallH09 = new JPanel();
		GridBagConstraints gbc_wallH09 = new GridBagConstraints();
		gbc_wallH09.insets = new Insets(0, 0, 0, 0);
		gbc_wallH09.fill = GridBagConstraints.BOTH;
		gbc_wallH09.gridx = 0;
		gbc_wallH09.gridy = 3;
		panel_3.add(wallH09, gbc_wallH09);

		JPanel wallM19 = new JPanel();

		GridBagConstraints gbc_wallM19 = new GridBagConstraints();
		gbc_wallM19.insets = new Insets(0, 0, 0, 0);
		gbc_wallM19.fill = GridBagConstraints.BOTH;
		gbc_wallM19.gridx = 1;
		gbc_wallM19.gridy = 3;
		panel_3.add(wallM19, gbc_wallM19);

		listeners(wallM19, gbc_wallM19, panel_3, gbl_panel_3);

		JPanel wallH10 = new JPanel();
		GridBagConstraints gbc_wallH10 = new GridBagConstraints();
		gbc_wallH10.insets = new Insets(0, 0, 0, 0);
		gbc_wallH10.fill = GridBagConstraints.BOTH;
		gbc_wallH10.gridx = 2;
		gbc_wallH10.gridy = 3;
		panel_3.add(wallH10, gbc_wallH10);

		JPanel wallM20 = new JPanel();

		GridBagConstraints gbc_wallM20 = new GridBagConstraints();
		gbc_wallM20.insets = new Insets(0, 0, 0, 0);
		gbc_wallM20.fill = GridBagConstraints.BOTH;
		gbc_wallM20.gridx = 3;
		gbc_wallM20.gridy = 3;
		panel_3.add(wallM20, gbc_wallM20);

		listeners(wallM20, gbc_wallM20, panel_3, gbl_panel_3);

		JPanel wallH11 = new JPanel();
		GridBagConstraints gbc_wallH11 = new GridBagConstraints();
		gbc_wallH11.insets = new Insets(0, 0, 0, 0);
		gbc_wallH11.fill = GridBagConstraints.BOTH;
		gbc_wallH11.gridx = 4;
		gbc_wallH11.gridy = 3;
		panel_3.add(wallH11, gbc_wallH11);

		JPanel wallM21 = new JPanel();

		GridBagConstraints gbc_wallM21 = new GridBagConstraints();
		gbc_wallM21.insets = new Insets(0, 0, 0, 0);
		gbc_wallM21.fill = GridBagConstraints.BOTH;
		gbc_wallM21.gridx = 5;
		gbc_wallM21.gridy = 3;
		panel_3.add(wallM21, gbc_wallM21);

		listeners(wallM21, gbc_wallM21, panel_3, gbl_panel_3);

		JPanel wallH12 = new JPanel();
		GridBagConstraints gbc_wallH12 = new GridBagConstraints();
		gbc_wallH12.insets = new Insets(0, 0, 0, 0);
		gbc_wallH12.fill = GridBagConstraints.BOTH;
		gbc_wallH12.gridx = 6;
		gbc_wallH12.gridy = 3;
		panel_3.add(wallH12, gbc_wallH12);

		JPanel wallM22 = new JPanel();

		GridBagConstraints gbc_wallM22 = new GridBagConstraints();
		gbc_wallM22.insets = new Insets(0, 0, 0, 0);
		gbc_wallM22.fill = GridBagConstraints.BOTH;
		gbc_wallM22.gridx = 7;
		gbc_wallM22.gridy = 3;
		panel_3.add(wallM22, gbc_wallM22);

		listeners(wallM22, gbc_wallM22, panel_3, gbl_panel_3);

		JPanel wallH13 = new JPanel();
		GridBagConstraints gbc_wallH13 = new GridBagConstraints();
		gbc_wallH13.insets = new Insets(0, 0, 0, 0);
		gbc_wallH13.fill = GridBagConstraints.BOTH;
		gbc_wallH13.gridx = 8;
		gbc_wallH13.gridy = 3;
		panel_3.add(wallH13, gbc_wallH13);

		JPanel wallM23 = new JPanel();

		GridBagConstraints gbc_wallM23 = new GridBagConstraints();
		gbc_wallM23.insets = new Insets(0, 0, 0, 0);
		gbc_wallM23.fill = GridBagConstraints.BOTH;
		gbc_wallM23.gridx = 9;
		gbc_wallM23.gridy = 3;
		panel_3.add(wallM23, gbc_wallM23);

		listeners(wallM23, gbc_wallM23, panel_3, gbl_panel_3);

		JPanel wallH14 = new JPanel();
		GridBagConstraints gbc_wallH14 = new GridBagConstraints();
		gbc_wallH14.insets = new Insets(0, 0, 0, 0);
		gbc_wallH14.fill = GridBagConstraints.BOTH;
		gbc_wallH14.gridx = 10;
		gbc_wallH14.gridy = 3;
		panel_3.add(wallH14, gbc_wallH14);

		JPanel wallM24 = new JPanel();

		GridBagConstraints gbc_wallM24 = new GridBagConstraints();
		gbc_wallM24.insets = new Insets(0, 0, 0, 0);
		gbc_wallM24.fill = GridBagConstraints.BOTH;
		gbc_wallM24.gridx = 11;
		gbc_wallM24.gridy = 3;
		panel_3.add(wallM24, gbc_wallM24);

		listeners(wallM24, gbc_wallM24, panel_3, gbl_panel_3);

		JPanel wallH15 = new JPanel();
		GridBagConstraints gbc_wallH15 = new GridBagConstraints();
		gbc_wallH15.insets = new Insets(0, 0, 0, 0);
		gbc_wallH15.fill = GridBagConstraints.BOTH;
		gbc_wallH15.gridx = 12;
		gbc_wallH15.gridy = 3;
		panel_3.add(wallH15, gbc_wallH15);

		JPanel wallM25 = new JPanel();

		GridBagConstraints gbc_wallM25 = new GridBagConstraints();
		gbc_wallM25.insets = new Insets(0, 0, 0, 0);
		gbc_wallM25.fill = GridBagConstraints.BOTH;
		gbc_wallM25.gridx = 13;
		gbc_wallM25.gridy = 3;
		panel_3.add(wallM25, gbc_wallM25);

		listeners(wallM25, gbc_wallM25, panel_3, gbl_panel_3);

		JPanel wallH16 = new JPanel();
		GridBagConstraints gbc_wallH16 = new GridBagConstraints();
		gbc_wallH16.insets = new Insets(0, 0, 0, 0);
		gbc_wallH16.fill = GridBagConstraints.BOTH;
		gbc_wallH16.gridx = 14;
		gbc_wallH16.gridy = 3;
		panel_3.add(wallH16, gbc_wallH16);

		JPanel wallM26 = new JPanel();

		GridBagConstraints gbc_wallM26 = new GridBagConstraints();
		gbc_wallM26.insets = new Insets(0, 0, 0, 0);
		gbc_wallM26.fill = GridBagConstraints.BOTH;
		gbc_wallM26.gridx = 15;
		gbc_wallM26.gridy = 3;
		panel_3.add(wallM26, gbc_wallM26);

		listeners(wallM26, gbc_wallM26, panel_3, gbl_panel_3);

		JPanel wallH17 = new JPanel();
		GridBagConstraints gbc_wallH17 = new GridBagConstraints();
		gbc_wallH17.insets = new Insets(0, 0, 0, 0);
		gbc_wallH17.fill = GridBagConstraints.BOTH;
		gbc_wallH17.gridx = 16;
		gbc_wallH17.gridy = 3;
		panel_3.add(wallH17, gbc_wallH17);

		JPanel tile18 = new JPanel();

		tile18.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile18 = new GridBagConstraints();
		gbc_tile18.insets = new Insets(0, 0, 0, 0);
		gbc_tile18.fill = GridBagConstraints.BOTH;
		gbc_tile18.gridx = 0;
		gbc_tile18.gridy = 4;
		panel_3.add(tile18, gbc_tile18);

		JPanel wallV18 = new JPanel();
		GridBagConstraints gbc_wallV18 = new GridBagConstraints();
		gbc_wallV18.insets = new Insets(0, 0, 0, 0);
		gbc_wallV18.fill = GridBagConstraints.BOTH;
		gbc_wallV18.gridx = 1;
		gbc_wallV18.gridy = 4;
		panel_3.add(wallV18, gbc_wallV18);

		JPanel tile19 = new JPanel();

		tile19.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile19 = new GridBagConstraints();
		gbc_tile19.insets = new Insets(0, 0, 0, 0);
		gbc_tile19.fill = GridBagConstraints.BOTH;
		gbc_tile19.gridx = 2;
		gbc_tile19.gridy = 4;
		panel_3.add(tile19, gbc_tile19);

		JPanel wallV19 = new JPanel();
		GridBagConstraints gbc_wallV19 = new GridBagConstraints();
		gbc_wallV19.insets = new Insets(0, 0, 0, 0);
		gbc_wallV19.fill = GridBagConstraints.BOTH;
		gbc_wallV19.gridx = 3;
		gbc_wallV19.gridy = 4;
		panel_3.add(wallV19, gbc_wallV19);

		JPanel tile20 = new JPanel();

		tile20.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile20 = new GridBagConstraints();
		gbc_tile20.insets = new Insets(0, 0, 0, 0);
		gbc_tile20.fill = GridBagConstraints.BOTH;
		gbc_tile20.gridx = 4;
		gbc_tile20.gridy = 4;
		panel_3.add(tile20, gbc_tile20);

		JPanel wallV20 = new JPanel();
		GridBagConstraints gbc_wallV20 = new GridBagConstraints();
		gbc_wallV20.insets = new Insets(0, 0, 0, 0);
		gbc_wallV20.fill = GridBagConstraints.BOTH;
		gbc_wallV20.gridx = 5;
		gbc_wallV20.gridy = 4;
		panel_3.add(wallV20, gbc_wallV20);

		JPanel tile21 = new JPanel();

		tile21.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile21 = new GridBagConstraints();
		gbc_tile21.insets = new Insets(0, 0, 0, 0);
		gbc_tile21.fill = GridBagConstraints.BOTH;
		gbc_tile21.gridx = 6;
		gbc_tile21.gridy = 4;
		panel_3.add(tile21, gbc_tile21);

		JPanel wallV21 = new JPanel();
		GridBagConstraints gbc_wallV21 = new GridBagConstraints();
		gbc_wallV21.insets = new Insets(0, 0, 0, 0);
		gbc_wallV21.fill = GridBagConstraints.BOTH;
		gbc_wallV21.gridx = 7;
		gbc_wallV21.gridy = 4;
		panel_3.add(wallV21, gbc_wallV21);

		JPanel tile22 = new JPanel();

		tile22.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile22 = new GridBagConstraints();
		gbc_tile22.insets = new Insets(0, 0, 0, 0);
		gbc_tile22.fill = GridBagConstraints.BOTH;
		gbc_tile22.gridx = 8;
		gbc_tile22.gridy = 4;
		panel_3.add(tile22, gbc_tile22);

		JPanel wallV22 = new JPanel();
		GridBagConstraints gbc_wallV22 = new GridBagConstraints();
		gbc_wallV22.insets = new Insets(0, 0, 0, 0);
		gbc_wallV22.fill = GridBagConstraints.BOTH;
		gbc_wallV22.gridx = 9;
		gbc_wallV22.gridy = 4;
		panel_3.add(wallV22, gbc_wallV22);

		JPanel tile23 = new JPanel();

		tile23.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile23 = new GridBagConstraints();
		gbc_tile23.insets = new Insets(0, 0, 0, 0);
		gbc_tile23.fill = GridBagConstraints.BOTH;
		gbc_tile23.gridx = 10;
		gbc_tile23.gridy = 4;
		panel_3.add(tile23, gbc_tile23);

		JPanel wallV23 = new JPanel();
		GridBagConstraints gbc_wallV23 = new GridBagConstraints();
		gbc_wallV23.insets = new Insets(0, 0, 0, 0);
		gbc_wallV23.fill = GridBagConstraints.BOTH;
		gbc_wallV23.gridx = 11;
		gbc_wallV23.gridy = 4;
		panel_3.add(wallV23, gbc_wallV23);

		JPanel tile24 = new JPanel();

		tile24.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile24 = new GridBagConstraints();
		gbc_tile24.insets = new Insets(0, 0, 0, 0);
		gbc_tile24.fill = GridBagConstraints.BOTH;
		gbc_tile24.gridx = 12;
		gbc_tile24.gridy = 4;
		panel_3.add(tile24, gbc_tile24);

		JPanel wallV24 = new JPanel();
		GridBagConstraints gbc_wallV24 = new GridBagConstraints();
		gbc_wallV24.insets = new Insets(0, 0, 0, 0);
		gbc_wallV24.fill = GridBagConstraints.BOTH;
		gbc_wallV24.gridx = 13;
		gbc_wallV24.gridy = 4;
		panel_3.add(wallV24, gbc_wallV24);

		JPanel tile25 = new JPanel();

		tile25.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile25 = new GridBagConstraints();
		gbc_tile25.insets = new Insets(0, 0, 0, 0);
		gbc_tile25.fill = GridBagConstraints.BOTH;
		gbc_tile25.gridx = 14;
		gbc_tile25.gridy = 4;
		panel_3.add(tile25, gbc_tile25);

		JPanel wallV25 = new JPanel();
		GridBagConstraints gbc_wallV25 = new GridBagConstraints();
		gbc_wallV25.insets = new Insets(0, 0, 0, 0);
		gbc_wallV25.fill = GridBagConstraints.BOTH;
		gbc_wallV25.gridx = 15;
		gbc_wallV25.gridy = 4;
		panel_3.add(wallV25, gbc_wallV25);

		JPanel tile26 = new JPanel();

		tile26.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile26 = new GridBagConstraints();
		gbc_tile26.insets = new Insets(0, 0, 0, 0);
		gbc_tile26.fill = GridBagConstraints.BOTH;
		gbc_tile26.gridx = 16;
		gbc_tile26.gridy = 4;
		panel_3.add(tile26, gbc_tile26);

		JPanel wallH18 = new JPanel();
		GridBagConstraints gbc_wallH18 = new GridBagConstraints();
		gbc_wallH18.insets = new Insets(0, 0, 0, 0);
		gbc_wallH18.fill = GridBagConstraints.BOTH;
		gbc_wallH18.gridx = 0;
		gbc_wallH18.gridy = 5;
		panel_3.add(wallH18, gbc_wallH18);

		JPanel wallM28 = new JPanel();

		GridBagConstraints gbc_wallM28 = new GridBagConstraints();
		gbc_wallM28.insets = new Insets(0, 0, 0, 0);
		gbc_wallM28.fill = GridBagConstraints.BOTH;
		gbc_wallM28.gridx = 1;
		gbc_wallM28.gridy = 5;
		panel_3.add(wallM28, gbc_wallM28);

		listeners(wallM28, gbc_wallM28, panel_3, gbl_panel_3);

		JPanel wallH19 = new JPanel();
		GridBagConstraints gbc_wallH19 = new GridBagConstraints();
		gbc_wallH19.insets = new Insets(0, 0, 0, 0);
		gbc_wallH19.fill = GridBagConstraints.BOTH;
		gbc_wallH19.gridx = 2;
		gbc_wallH19.gridy = 5;
		panel_3.add(wallH19, gbc_wallH19);

		JPanel wallM29 = new JPanel();

		GridBagConstraints gbc_wallM29 = new GridBagConstraints();
		gbc_wallM29.insets = new Insets(0, 0, 0, 0);
		gbc_wallM29.fill = GridBagConstraints.BOTH;
		gbc_wallM29.gridx = 3;
		gbc_wallM29.gridy = 5;
		panel_3.add(wallM29, gbc_wallM29);

		listeners(wallM29, gbc_wallM29, panel_3, gbl_panel_3);

		JPanel wallH20 = new JPanel();
		GridBagConstraints gbc_wallH20 = new GridBagConstraints();
		gbc_wallH20.insets = new Insets(0, 0, 0, 0);
		gbc_wallH20.fill = GridBagConstraints.BOTH;
		gbc_wallH20.gridx = 4;
		gbc_wallH20.gridy = 5;
		panel_3.add(wallH20, gbc_wallH20);

		JPanel wallM30 = new JPanel();
		GridBagConstraints gbc_wallM30 = new GridBagConstraints();
		gbc_wallM30.insets = new Insets(0, 0, 0, 0);
		gbc_wallM30.fill = GridBagConstraints.BOTH;
		gbc_wallM30.gridx = 5;
		gbc_wallM30.gridy = 5;
		panel_3.add(wallM30, gbc_wallM30);
		listeners(wallM30, gbc_wallM30, panel_3, gbl_panel_3);

		JPanel wallH21 = new JPanel();
		GridBagConstraints gbc_wallH21 = new GridBagConstraints();
		gbc_wallH21.insets = new Insets(0, 0, 0, 0);
		gbc_wallH21.fill = GridBagConstraints.BOTH;
		gbc_wallH21.gridx = 6;
		gbc_wallH21.gridy = 5;
		panel_3.add(wallH21, gbc_wallH21);

		JPanel wallM31 = new JPanel();

		GridBagConstraints gbc_wallM31 = new GridBagConstraints();
		gbc_wallM31.insets = new Insets(0, 0, 0, 0);
		gbc_wallM31.fill = GridBagConstraints.BOTH;
		gbc_wallM31.gridx = 7;
		gbc_wallM31.gridy = 5;
		panel_3.add(wallM31, gbc_wallM31);

		listeners(wallM31, gbc_wallM31, panel_3, gbl_panel_3);

		JPanel wallH22 = new JPanel();
		GridBagConstraints gbc_wallH22 = new GridBagConstraints();
		gbc_wallH22.insets = new Insets(0, 0, 0, 0);
		gbc_wallH22.fill = GridBagConstraints.BOTH;
		gbc_wallH22.gridx = 8;
		gbc_wallH22.gridy = 5;
		panel_3.add(wallH22, gbc_wallH22);

		JPanel wallM32 = new JPanel();
		GridBagConstraints gbc_wallM32 = new GridBagConstraints();
		gbc_wallM32.insets = new Insets(0, 0, 0, 0);
		gbc_wallM32.fill = GridBagConstraints.BOTH;
		gbc_wallM32.gridx = 9;
		gbc_wallM32.gridy = 5;
		panel_3.add(wallM32, gbc_wallM32);
		listeners(wallM32, gbc_wallM32, panel_3, gbl_panel_3);

		JPanel wallH23 = new JPanel();
		GridBagConstraints gbc_wallH23 = new GridBagConstraints();
		gbc_wallH23.insets = new Insets(0, 0, 0, 0);
		gbc_wallH23.fill = GridBagConstraints.BOTH;
		gbc_wallH23.gridx = 10;
		gbc_wallH23.gridy = 5;
		panel_3.add(wallH23, gbc_wallH23);

		JPanel wallM33 = new JPanel();
		GridBagConstraints gbc_wallM33 = new GridBagConstraints();
		gbc_wallM33.insets = new Insets(0, 0, 0, 0);
		gbc_wallM33.fill = GridBagConstraints.BOTH;
		gbc_wallM33.gridx = 11;
		gbc_wallM33.gridy = 5;
		panel_3.add(wallM33, gbc_wallM33);
		listeners(wallM33, gbc_wallM33, panel_3, gbl_panel_3);

		JPanel wallH24 = new JPanel();
		GridBagConstraints gbc_wallH24 = new GridBagConstraints();
		gbc_wallH24.insets = new Insets(0, 0, 0, 0);
		gbc_wallH24.fill = GridBagConstraints.BOTH;
		gbc_wallH24.gridx = 12;
		gbc_wallH24.gridy = 5;
		panel_3.add(wallH24, gbc_wallH24);

		JPanel wallM34 = new JPanel();

		GridBagConstraints gbc_wallM34 = new GridBagConstraints();
		gbc_wallM34.insets = new Insets(0, 0, 0, 0);
		gbc_wallM34.fill = GridBagConstraints.BOTH;
		gbc_wallM34.gridx = 13;
		gbc_wallM34.gridy = 5;
		panel_3.add(wallM34, gbc_wallM34);
		listeners(wallM34, gbc_wallM34, panel_3, gbl_panel_3);

		JPanel wallH25 = new JPanel();
		GridBagConstraints gbc_wallH25 = new GridBagConstraints();
		gbc_wallH25.insets = new Insets(0, 0, 0, 0);
		gbc_wallH25.fill = GridBagConstraints.BOTH;
		gbc_wallH25.gridx = 14;
		gbc_wallH25.gridy = 5;
		panel_3.add(wallH25, gbc_wallH25);

		JPanel wallM35 = new JPanel();

		GridBagConstraints gbc_wallM35 = new GridBagConstraints();
		gbc_wallM35.insets = new Insets(0, 0, 0, 0);
		gbc_wallM35.fill = GridBagConstraints.BOTH;
		gbc_wallM35.gridx = 15;
		gbc_wallM35.gridy = 5;
		panel_3.add(wallM35, gbc_wallM35);
		listeners(wallM35, gbc_wallM35, panel_3, gbl_panel_3);

		JPanel wallH26 = new JPanel();
		GridBagConstraints gbc_wallH26 = new GridBagConstraints();
		gbc_wallH26.insets = new Insets(0, 0, 0, 0);
		gbc_wallH26.fill = GridBagConstraints.BOTH;
		gbc_wallH26.gridx = 16;
		gbc_wallH26.gridy = 5;
		panel_3.add(wallH26, gbc_wallH26);

		JPanel tile27 = new JPanel();

		tile27.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile27 = new GridBagConstraints();
		gbc_tile27.insets = new Insets(0, 0, 0, 0);
		gbc_tile27.fill = GridBagConstraints.BOTH;
		gbc_tile27.gridx = 0;
		gbc_tile27.gridy = 6;
		panel_3.add(tile27, gbc_tile27);

		JPanel wallV27 = new JPanel();
		GridBagConstraints gbc_wallV27 = new GridBagConstraints();
		gbc_wallV27.insets = new Insets(0, 0, 0, 0);
		gbc_wallV27.fill = GridBagConstraints.BOTH;
		gbc_wallV27.gridx = 1;
		gbc_wallV27.gridy = 6;
		panel_3.add(wallV27, gbc_wallV27);

		JPanel tile28 = new JPanel();

		tile28.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile28 = new GridBagConstraints();
		gbc_tile28.insets = new Insets(0, 0, 0, 0);
		gbc_tile28.fill = GridBagConstraints.BOTH;
		gbc_tile28.gridx = 2;
		gbc_tile28.gridy = 6;
		panel_3.add(tile28, gbc_tile28);

		JPanel wallV28 = new JPanel();
		GridBagConstraints gbc_wallV28 = new GridBagConstraints();
		gbc_wallV28.insets = new Insets(0, 0, 0, 0);
		gbc_wallV28.fill = GridBagConstraints.BOTH;
		gbc_wallV28.gridx = 3;
		gbc_wallV28.gridy = 6;
		panel_3.add(wallV28, gbc_wallV28);

		JPanel tile29 = new JPanel();

		tile29.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile29 = new GridBagConstraints();
		gbc_tile29.insets = new Insets(0, 0, 0, 0);
		gbc_tile29.fill = GridBagConstraints.BOTH;
		gbc_tile29.gridx = 4;
		gbc_tile29.gridy = 6;
		panel_3.add(tile29, gbc_tile29);

		JPanel wallV29 = new JPanel();
		GridBagConstraints gbc_wallV29 = new GridBagConstraints();
		gbc_wallV29.insets = new Insets(0, 0, 0, 0);
		gbc_wallV29.fill = GridBagConstraints.BOTH;
		gbc_wallV29.gridx = 5;
		gbc_wallV29.gridy = 6;
		panel_3.add(wallV29, gbc_wallV29);

		JPanel tile30 = new JPanel();

		tile30.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile30 = new GridBagConstraints();
		gbc_tile30.insets = new Insets(0, 0, 0, 0);
		gbc_tile30.fill = GridBagConstraints.BOTH;
		gbc_tile30.gridx = 6;
		gbc_tile30.gridy = 6;
		panel_3.add(tile30, gbc_tile30);

		JPanel wallV30 = new JPanel();
		GridBagConstraints gbc_wallV30 = new GridBagConstraints();
		gbc_wallV30.insets = new Insets(0, 0, 0, 0);
		gbc_wallV30.fill = GridBagConstraints.BOTH;
		gbc_wallV30.gridx = 7;
		gbc_wallV30.gridy = 6;
		panel_3.add(wallV30, gbc_wallV30);

		JPanel tile31 = new JPanel();

		tile31.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile31 = new GridBagConstraints();
		gbc_tile31.insets = new Insets(0, 0, 0, 0);
		gbc_tile31.fill = GridBagConstraints.BOTH;
		gbc_tile31.gridx = 8;
		gbc_tile31.gridy = 6;
		panel_3.add(tile31, gbc_tile31);

		JPanel wallV31 = new JPanel();
		GridBagConstraints gbc_wallV31 = new GridBagConstraints();
		gbc_wallV31.insets = new Insets(0, 0, 0, 0);
		gbc_wallV31.fill = GridBagConstraints.BOTH;
		gbc_wallV31.gridx = 9;
		gbc_wallV31.gridy = 6;
		panel_3.add(wallV31, gbc_wallV31);

		JPanel tile32 = new JPanel();

		tile32.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile32 = new GridBagConstraints();
		gbc_tile32.insets = new Insets(0, 0, 0, 0);
		gbc_tile32.fill = GridBagConstraints.BOTH;
		gbc_tile32.gridx = 10;
		gbc_tile32.gridy = 6;
		panel_3.add(tile32, gbc_tile32);

		JPanel wallV32 = new JPanel();
		GridBagConstraints gbc_wallV32 = new GridBagConstraints();
		gbc_wallV32.insets = new Insets(0, 0, 0, 0);
		gbc_wallV32.fill = GridBagConstraints.BOTH;
		gbc_wallV32.gridx = 11;
		gbc_wallV32.gridy = 6;
		panel_3.add(wallV32, gbc_wallV32);

		JPanel tile33 = new JPanel();

		tile33.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile33 = new GridBagConstraints();
		gbc_tile33.insets = new Insets(0, 0, 0, 0);
		gbc_tile33.fill = GridBagConstraints.BOTH;
		gbc_tile33.gridx = 12;
		gbc_tile33.gridy = 6;
		panel_3.add(tile33, gbc_tile33);

		JPanel wallV33 = new JPanel();
		GridBagConstraints gbc_wallV33 = new GridBagConstraints();
		gbc_wallV33.insets = new Insets(0, 0, 0, 0);
		gbc_wallV33.fill = GridBagConstraints.BOTH;
		gbc_wallV33.gridx = 13;
		gbc_wallV33.gridy = 6;
		panel_3.add(wallV33, gbc_wallV33);

		JPanel tile34 = new JPanel();

		tile34.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile34 = new GridBagConstraints();
		gbc_tile34.insets = new Insets(0, 0, 0, 0);
		gbc_tile34.fill = GridBagConstraints.BOTH;
		gbc_tile34.gridx = 14;
		gbc_tile34.gridy = 6;
		panel_3.add(tile34, gbc_tile34);

		JPanel wallV34 = new JPanel();
		GridBagConstraints gbc_wallV34 = new GridBagConstraints();
		gbc_wallV34.insets = new Insets(0, 0, 0, 0);
		gbc_wallV34.fill = GridBagConstraints.BOTH;
		gbc_wallV34.gridx = 15;
		gbc_wallV34.gridy = 6;
		panel_3.add(wallV34, gbc_wallV34);

		JPanel tile35 = new JPanel();

		tile35.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile35 = new GridBagConstraints();
		gbc_tile35.insets = new Insets(0, 0, 0, 0);
		gbc_tile35.fill = GridBagConstraints.BOTH;
		gbc_tile35.gridx = 16;
		gbc_tile35.gridy = 6;
		panel_3.add(tile35, gbc_tile35);

		JPanel wallH27 = new JPanel();
		GridBagConstraints gbc_wallH27 = new GridBagConstraints();
		gbc_wallH27.insets = new Insets(0, 0, 0, 0);
		gbc_wallH27.fill = GridBagConstraints.BOTH;
		gbc_wallH27.gridx = 0;
		gbc_wallH27.gridy = 7;
		panel_3.add(wallH27, gbc_wallH27);

		JPanel wallM37 = new JPanel();

		GridBagConstraints gbc_wallM37 = new GridBagConstraints();
		gbc_wallM37.insets = new Insets(0, 0, 0, 0);
		gbc_wallM37.fill = GridBagConstraints.BOTH;
		gbc_wallM37.gridx = 1;
		gbc_wallM37.gridy = 7;
		panel_3.add(wallM37, gbc_wallM37);
		listeners(wallM37, gbc_wallM37, panel_3, gbl_panel_3);

		JPanel wallH28 = new JPanel();
		GridBagConstraints gbc_wallH28 = new GridBagConstraints();
		gbc_wallH28.insets = new Insets(0, 0, 0, 0);
		gbc_wallH28.fill = GridBagConstraints.BOTH;
		gbc_wallH28.gridx = 2;
		gbc_wallH28.gridy = 7;
		panel_3.add(wallH28, gbc_wallH28);

		JPanel wallM38 = new JPanel();

		GridBagConstraints gbc_wallM38 = new GridBagConstraints();
		gbc_wallM38.insets = new Insets(0, 0, 0, 0);
		gbc_wallM38.fill = GridBagConstraints.BOTH;
		gbc_wallM38.gridx = 3;
		gbc_wallM38.gridy = 7;
		panel_3.add(wallM38, gbc_wallM38);
		listeners(wallM38, gbc_wallM38, panel_3, gbl_panel_3);

		JPanel wallH29 = new JPanel();
		GridBagConstraints gbc_wallH29 = new GridBagConstraints();
		gbc_wallH29.insets = new Insets(0, 0, 0, 0);
		gbc_wallH29.fill = GridBagConstraints.BOTH;
		gbc_wallH29.gridx = 4;
		gbc_wallH29.gridy = 7;
		panel_3.add(wallH29, gbc_wallH29);

		JPanel wallM39 = new JPanel();

		GridBagConstraints gbc_wallM39 = new GridBagConstraints();
		gbc_wallM39.insets = new Insets(0, 0, 0, 0);
		gbc_wallM39.fill = GridBagConstraints.BOTH;
		gbc_wallM39.gridx = 5;
		gbc_wallM39.gridy = 7;
		panel_3.add(wallM39, gbc_wallM39);
		listeners(wallM39, gbc_wallM39, panel_3, gbl_panel_3);

		JPanel wallH30 = new JPanel();
		GridBagConstraints gbc_wallH30 = new GridBagConstraints();
		gbc_wallH30.insets = new Insets(0, 0, 0, 0);
		gbc_wallH30.fill = GridBagConstraints.BOTH;
		gbc_wallH30.gridx = 6;
		gbc_wallH30.gridy = 7;
		panel_3.add(wallH30, gbc_wallH30);

		JPanel wallM40 = new JPanel();

		GridBagConstraints gbc_wallM40 = new GridBagConstraints();
		gbc_wallM40.insets = new Insets(0, 0, 0, 0);
		gbc_wallM40.fill = GridBagConstraints.BOTH;
		gbc_wallM40.gridx = 7;
		gbc_wallM40.gridy = 7;
		panel_3.add(wallM40, gbc_wallM40);
		listeners(wallM40, gbc_wallM40, panel_3, gbl_panel_3);

		JPanel wallH31 = new JPanel();
		GridBagConstraints gbc_wallH31 = new GridBagConstraints();
		gbc_wallH31.insets = new Insets(0, 0, 0, 0);
		gbc_wallH31.fill = GridBagConstraints.BOTH;
		gbc_wallH31.gridx = 8;
		gbc_wallH31.gridy = 7;
		panel_3.add(wallH31, gbc_wallH31);

		JPanel wallM41 = new JPanel();

		GridBagConstraints gbc_wallM41 = new GridBagConstraints();
		gbc_wallM41.insets = new Insets(0, 0, 0, 0);
		gbc_wallM41.fill = GridBagConstraints.BOTH;
		gbc_wallM41.gridx = 9;
		gbc_wallM41.gridy = 7;
		panel_3.add(wallM41, gbc_wallM41);
		listeners(wallM41, gbc_wallM41, panel_3, gbl_panel_3);

		JPanel wallH32 = new JPanel();
		GridBagConstraints gbc_wallH32 = new GridBagConstraints();
		gbc_wallH32.insets = new Insets(0, 0, 0, 0);
		gbc_wallH32.fill = GridBagConstraints.BOTH;
		gbc_wallH32.gridx = 10;
		gbc_wallH32.gridy = 7;
		panel_3.add(wallH32, gbc_wallH32);

		JPanel wallM42 = new JPanel();

		GridBagConstraints gbc_wallM42 = new GridBagConstraints();
		gbc_wallM42.insets = new Insets(0, 0, 0, 0);
		gbc_wallM42.fill = GridBagConstraints.BOTH;
		gbc_wallM42.gridx = 11;
		gbc_wallM42.gridy = 7;
		panel_3.add(wallM42, gbc_wallM42);
		listeners(wallM42, gbc_wallM42, panel_3, gbl_panel_3);

		JPanel wallH33 = new JPanel();
		GridBagConstraints gbc_wallH33 = new GridBagConstraints();
		gbc_wallH33.insets = new Insets(0, 0, 0, 0);
		gbc_wallH33.fill = GridBagConstraints.BOTH;
		gbc_wallH33.gridx = 12;
		gbc_wallH33.gridy = 7;
		panel_3.add(wallH33, gbc_wallH33);

		JPanel wallM43 = new JPanel();

		GridBagConstraints gbc_wallM43 = new GridBagConstraints();
		gbc_wallM43.insets = new Insets(0, 0, 0, 0);
		gbc_wallM43.fill = GridBagConstraints.BOTH;
		gbc_wallM43.gridx = 13;
		gbc_wallM43.gridy = 7;
		panel_3.add(wallM43, gbc_wallM43);
		listeners(wallM43, gbc_wallM43, panel_3, gbl_panel_3);

		JPanel wallH34 = new JPanel();
		GridBagConstraints gbc_wallH34 = new GridBagConstraints();
		gbc_wallH34.insets = new Insets(0, 0, 0, 0);
		gbc_wallH34.fill = GridBagConstraints.BOTH;
		gbc_wallH34.gridx = 14;
		gbc_wallH34.gridy = 7;
		panel_3.add(wallH34, gbc_wallH34);

		JPanel wallM44 = new JPanel();

		GridBagConstraints gbc_wallM44 = new GridBagConstraints();
		gbc_wallM44.insets = new Insets(0, 0, 0, 0);
		gbc_wallM44.fill = GridBagConstraints.BOTH;
		gbc_wallM44.gridx = 15;
		gbc_wallM44.gridy = 7;
		panel_3.add(wallM44, gbc_wallM44);
		listeners(wallM44, gbc_wallM44, panel_3, gbl_panel_3);

		JPanel wallH35 = new JPanel();
		GridBagConstraints gbc_wallH35 = new GridBagConstraints();
		gbc_wallH35.insets = new Insets(0, 0, 0, 0);
		gbc_wallH35.fill = GridBagConstraints.BOTH;
		gbc_wallH35.gridx = 16;
		gbc_wallH35.gridy = 7;
		panel_3.add(wallH35, gbc_wallH35);

		JPanel tile36 = new JPanel();

		tile36.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile36 = new GridBagConstraints();
		gbc_tile36.insets = new Insets(0, 0, 0, 0);
		gbc_tile36.fill = GridBagConstraints.BOTH;
		gbc_tile36.gridx = 0;
		gbc_tile36.gridy = 8;
		panel_3.add(tile36, gbc_tile36);

		JPanel wallV36 = new JPanel();
		GridBagConstraints gbc_wallV36 = new GridBagConstraints();
		gbc_wallV36.insets = new Insets(0, 0, 0, 0);
		gbc_wallV36.fill = GridBagConstraints.BOTH;
		gbc_wallV36.gridx = 1;
		gbc_wallV36.gridy = 8;
		panel_3.add(wallV36, gbc_wallV36);

		JPanel tile37 = new JPanel();

		tile37.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile37 = new GridBagConstraints();
		gbc_tile37.insets = new Insets(0, 0, 0, 0);
		gbc_tile37.fill = GridBagConstraints.BOTH;
		gbc_tile37.gridx = 2;
		gbc_tile37.gridy = 8;
		panel_3.add(tile37, gbc_tile37);

		JPanel wallV37 = new JPanel();
		GridBagConstraints gbc_wallV37 = new GridBagConstraints();
		gbc_wallV37.insets = new Insets(0, 0, 0, 0);
		gbc_wallV37.fill = GridBagConstraints.BOTH;
		gbc_wallV37.gridx = 3;
		gbc_wallV37.gridy = 8;
		panel_3.add(wallV37, gbc_wallV37);

		JPanel tile38 = new JPanel();

		tile38.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile38 = new GridBagConstraints();
		gbc_tile38.insets = new Insets(0, 0, 0, 0);
		gbc_tile38.fill = GridBagConstraints.BOTH;
		gbc_tile38.gridx = 4;
		gbc_tile38.gridy = 8;
		panel_3.add(tile38, gbc_tile38);

		JPanel wallV38 = new JPanel();
		GridBagConstraints gbc_wallV38 = new GridBagConstraints();
		gbc_wallV38.insets = new Insets(0, 0, 0, 0);
		gbc_wallV38.fill = GridBagConstraints.BOTH;
		gbc_wallV38.gridx = 5;
		gbc_wallV38.gridy = 8;
		panel_3.add(wallV38, gbc_wallV38);

		JPanel tile39 = new JPanel();

		tile39.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile39 = new GridBagConstraints();
		gbc_tile39.insets = new Insets(0, 0, 0, 0);
		gbc_tile39.fill = GridBagConstraints.BOTH;
		gbc_tile39.gridx = 6;
		gbc_tile39.gridy = 8;
		panel_3.add(tile39, gbc_tile39);

		JPanel wallV39 = new JPanel();
		GridBagConstraints gbc_wallV39 = new GridBagConstraints();
		gbc_wallV39.insets = new Insets(0, 0, 0, 0);
		gbc_wallV39.fill = GridBagConstraints.BOTH;
		gbc_wallV39.gridx = 7;
		gbc_wallV39.gridy = 8;
		panel_3.add(wallV39, gbc_wallV39);

		JPanel tile40 = new JPanel();

		tile40.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile40 = new GridBagConstraints();
		gbc_tile40.insets = new Insets(0, 0, 0, 0);
		gbc_tile40.fill = GridBagConstraints.BOTH;
		gbc_tile40.gridx = 8;
		gbc_tile40.gridy = 8;
		panel_3.add(tile40, gbc_tile40);

		JPanel wallV40 = new JPanel();
		GridBagConstraints gbc_wallV40 = new GridBagConstraints();
		gbc_wallV40.insets = new Insets(0, 0, 0, 0);
		gbc_wallV40.fill = GridBagConstraints.BOTH;
		gbc_wallV40.gridx = 9;
		gbc_wallV40.gridy = 8;
		panel_3.add(wallV40, gbc_wallV40);

		JPanel tile41 = new JPanel();

		tile41.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile41 = new GridBagConstraints();
		gbc_tile41.insets = new Insets(0, 0, 0, 0);
		gbc_tile41.fill = GridBagConstraints.BOTH;
		gbc_tile41.gridx = 10;
		gbc_tile41.gridy = 8;
		panel_3.add(tile41, gbc_tile41);

		JPanel wallV41 = new JPanel();
		GridBagConstraints gbc_wallV41 = new GridBagConstraints();
		gbc_wallV41.insets = new Insets(0, 0, 0, 0);
		gbc_wallV41.fill = GridBagConstraints.BOTH;
		gbc_wallV41.gridx = 11;
		gbc_wallV41.gridy = 8;
		panel_3.add(wallV41, gbc_wallV41);

		JPanel tile42 = new JPanel();

		tile42.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile42 = new GridBagConstraints();
		gbc_tile42.insets = new Insets(0, 0, 0, 0);
		gbc_tile42.fill = GridBagConstraints.BOTH;
		gbc_tile42.gridx = 12;
		gbc_tile42.gridy = 8;
		panel_3.add(tile42, gbc_tile42);

		JPanel wallV42 = new JPanel();
		GridBagConstraints gbc_wallV42 = new GridBagConstraints();
		gbc_wallV42.insets = new Insets(0, 0, 0, 0);
		gbc_wallV42.fill = GridBagConstraints.BOTH;
		gbc_wallV42.gridx = 13;
		gbc_wallV42.gridy = 8;
		panel_3.add(wallV42, gbc_wallV42);

		JPanel tile43 = new JPanel();

		tile43.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile43 = new GridBagConstraints();
		gbc_tile43.insets = new Insets(0, 0, 0, 0);
		gbc_tile43.fill = GridBagConstraints.BOTH;
		gbc_tile43.gridx = 14;
		gbc_tile43.gridy = 8;
		panel_3.add(tile43, gbc_tile43);

		JPanel wallV43 = new JPanel();
		GridBagConstraints gbc_wallV43 = new GridBagConstraints();
		gbc_wallV43.insets = new Insets(0, 0, 0, 0);
		gbc_wallV43.fill = GridBagConstraints.BOTH;
		gbc_wallV43.gridx = 15;
		gbc_wallV43.gridy = 8;
		panel_3.add(wallV43, gbc_wallV43);

		JPanel tile44 = new JPanel();

		tile44.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile44 = new GridBagConstraints();
		gbc_tile44.insets = new Insets(0, 0, 0, 0);
		gbc_tile44.fill = GridBagConstraints.BOTH;
		gbc_tile44.gridx = 16;
		gbc_tile44.gridy = 8;
		panel_3.add(tile44, gbc_tile44);

		JPanel wallH36 = new JPanel();
		GridBagConstraints gbc_wallH36 = new GridBagConstraints();
		gbc_wallH36.insets = new Insets(0, 0, 0, 0);
		gbc_wallH36.fill = GridBagConstraints.BOTH;
		gbc_wallH36.gridx = 0;
		gbc_wallH36.gridy = 9;
		panel_3.add(wallH36, gbc_wallH36);

		JPanel wallM46 = new JPanel();

		GridBagConstraints gbc_wallM46 = new GridBagConstraints();
		gbc_wallM46.insets = new Insets(0, 0, 0, 0);
		gbc_wallM46.fill = GridBagConstraints.BOTH;
		gbc_wallM46.gridx = 1;
		gbc_wallM46.gridy = 9;
		panel_3.add(wallM46, gbc_wallM46);
		listeners(wallM46, gbc_wallM46, panel_3, gbl_panel_3);

		JPanel wallH37 = new JPanel();
		GridBagConstraints gbc_wallH37 = new GridBagConstraints();
		gbc_wallH37.insets = new Insets(0, 0, 0, 0);
		gbc_wallH37.fill = GridBagConstraints.BOTH;
		gbc_wallH37.gridx = 2;
		gbc_wallH37.gridy = 9;
		panel_3.add(wallH37, gbc_wallH37);

		JPanel wallM47 = new JPanel();

		GridBagConstraints gbc_wallM47 = new GridBagConstraints();
		gbc_wallM47.insets = new Insets(0, 0, 0, 0);
		gbc_wallM47.fill = GridBagConstraints.BOTH;
		gbc_wallM47.gridx = 3;
		gbc_wallM47.gridy = 9;
		panel_3.add(wallM47, gbc_wallM47);
		listeners(wallM47, gbc_wallM47, panel_3, gbl_panel_3);

		JPanel wallH38 = new JPanel();
		GridBagConstraints gbc_wallH38 = new GridBagConstraints();
		gbc_wallH38.insets = new Insets(0, 0, 0, 0);
		gbc_wallH38.fill = GridBagConstraints.BOTH;
		gbc_wallH38.gridx = 4;
		gbc_wallH38.gridy = 9;
		panel_3.add(wallH38, gbc_wallH38);

		JPanel wallM48 = new JPanel();

		GridBagConstraints gbc_wallM48 = new GridBagConstraints();
		gbc_wallM48.insets = new Insets(0, 0, 0, 0);
		gbc_wallM48.fill = GridBagConstraints.BOTH;
		gbc_wallM48.gridx = 5;
		gbc_wallM48.gridy = 9;
		panel_3.add(wallM48, gbc_wallM48);
		listeners(wallM48, gbc_wallM48, panel_3, gbl_panel_3);

		JPanel wallH39 = new JPanel();
		GridBagConstraints gbc_wallH39 = new GridBagConstraints();
		gbc_wallH39.insets = new Insets(0, 0, 0, 0);
		gbc_wallH39.fill = GridBagConstraints.BOTH;
		gbc_wallH39.gridx = 6;
		gbc_wallH39.gridy = 9;
		panel_3.add(wallH39, gbc_wallH39);

		JPanel wallM49 = new JPanel();

		GridBagConstraints gbc_wallM49 = new GridBagConstraints();
		gbc_wallM49.insets = new Insets(0, 0, 0, 0);
		gbc_wallM49.fill = GridBagConstraints.BOTH;
		gbc_wallM49.gridx = 7;
		gbc_wallM49.gridy = 9;
		panel_3.add(wallM49, gbc_wallM49);
		listeners(wallM49, gbc_wallM49, panel_3, gbl_panel_3);

		JPanel wallH40 = new JPanel();
		GridBagConstraints gbc_wallH40 = new GridBagConstraints();
		gbc_wallH40.insets = new Insets(0, 0, 0, 0);
		gbc_wallH40.fill = GridBagConstraints.BOTH;
		gbc_wallH40.gridx = 8;
		gbc_wallH40.gridy = 9;
		panel_3.add(wallH40, gbc_wallH40);

		JPanel wallM50 = new JPanel();

		GridBagConstraints gbc_wallM50 = new GridBagConstraints();
		gbc_wallM50.insets = new Insets(0, 0, 0, 0);
		gbc_wallM50.fill = GridBagConstraints.BOTH;
		gbc_wallM50.gridx = 9;
		gbc_wallM50.gridy = 9;
		panel_3.add(wallM50, gbc_wallM50);
		listeners(wallM50, gbc_wallM50, panel_3, gbl_panel_3);

		JPanel wallH41 = new JPanel();
		GridBagConstraints gbc_wallH41 = new GridBagConstraints();
		gbc_wallH41.insets = new Insets(0, 0, 0, 0);
		gbc_wallH41.fill = GridBagConstraints.BOTH;
		gbc_wallH41.gridx = 10;
		gbc_wallH41.gridy = 9;
		panel_3.add(wallH41, gbc_wallH41);

		JPanel wallM51 = new JPanel();

		GridBagConstraints gbc_wallM51 = new GridBagConstraints();
		gbc_wallM51.insets = new Insets(0, 0, 0, 0);
		gbc_wallM51.fill = GridBagConstraints.BOTH;
		gbc_wallM51.gridx = 11;
		gbc_wallM51.gridy = 9;
		panel_3.add(wallM51, gbc_wallM51);

		listeners(wallM51, gbc_wallM51, panel_3, gbl_panel_3);

		JPanel wallH42 = new JPanel();
		GridBagConstraints gbc_wallH42 = new GridBagConstraints();
		gbc_wallH42.insets = new Insets(0, 0, 0, 0);
		gbc_wallH42.fill = GridBagConstraints.BOTH;
		gbc_wallH42.gridx = 12;
		gbc_wallH42.gridy = 9;
		panel_3.add(wallH42, gbc_wallH42);

		JPanel wallM52 = new JPanel();

		GridBagConstraints gbc_wallM52 = new GridBagConstraints();
		gbc_wallM52.insets = new Insets(0, 0, 0, 0);
		gbc_wallM52.fill = GridBagConstraints.BOTH;
		gbc_wallM52.gridx = 13;
		gbc_wallM52.gridy = 9;
		panel_3.add(wallM52, gbc_wallM52);
		listeners(wallM52, gbc_wallM52, panel_3, gbl_panel_3);

		JPanel wallH43 = new JPanel();
		GridBagConstraints gbc_wallH43 = new GridBagConstraints();
		gbc_wallH43.insets = new Insets(0, 0, 0, 0);
		gbc_wallH43.fill = GridBagConstraints.BOTH;
		gbc_wallH43.gridx = 14;
		gbc_wallH43.gridy = 9;
		panel_3.add(wallH43, gbc_wallH43);

		JPanel wallM53 = new JPanel();

		GridBagConstraints gbc_wallM53 = new GridBagConstraints();
		gbc_wallM53.insets = new Insets(0, 0, 0, 0);
		gbc_wallM53.fill = GridBagConstraints.BOTH;
		gbc_wallM53.gridx = 15;
		gbc_wallM53.gridy = 9;
		panel_3.add(wallM53, gbc_wallM53);
		listeners(wallM53, gbc_wallM53, panel_3, gbl_panel_3);

		JPanel wallH44 = new JPanel();
		GridBagConstraints gbc_wallH44 = new GridBagConstraints();
		gbc_wallH44.insets = new Insets(0, 0, 0, 0);
		gbc_wallH44.fill = GridBagConstraints.BOTH;
		gbc_wallH44.gridx = 16;
		gbc_wallH44.gridy = 9;
		panel_3.add(wallH44, gbc_wallH44);

		JPanel tile45 = new JPanel();

		tile45.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile45 = new GridBagConstraints();
		gbc_tile45.insets = new Insets(0, 0, 0, 0);
		gbc_tile45.fill = GridBagConstraints.BOTH;
		gbc_tile45.gridx = 0;
		gbc_tile45.gridy = 10;
		panel_3.add(tile45, gbc_tile45);

		JPanel wallV45 = new JPanel();
		GridBagConstraints gbc_wallV45 = new GridBagConstraints();
		gbc_wallV45.insets = new Insets(0, 0, 0, 0);
		gbc_wallV45.fill = GridBagConstraints.BOTH;
		gbc_wallV45.gridx = 1;
		gbc_wallV45.gridy = 10;
		panel_3.add(wallV45, gbc_wallV45);

		JPanel tile46 = new JPanel();

		tile46.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile46 = new GridBagConstraints();
		gbc_tile46.insets = new Insets(0, 0, 0, 0);
		gbc_tile46.fill = GridBagConstraints.BOTH;
		gbc_tile46.gridx = 2;
		gbc_tile46.gridy = 10;
		panel_3.add(tile46, gbc_tile46);

		JPanel wallV46 = new JPanel();
		GridBagConstraints gbc_wallV46 = new GridBagConstraints();
		gbc_wallV46.insets = new Insets(0, 0, 0, 0);
		gbc_wallV46.fill = GridBagConstraints.BOTH;
		gbc_wallV46.gridx = 3;
		gbc_wallV46.gridy = 10;
		panel_3.add(wallV46, gbc_wallV46);

		JPanel tile47 = new JPanel();

		tile47.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile47 = new GridBagConstraints();
		gbc_tile47.insets = new Insets(0, 0, 0, 0);
		gbc_tile47.fill = GridBagConstraints.BOTH;
		gbc_tile47.gridx = 4;
		gbc_tile47.gridy = 10;
		panel_3.add(tile47, gbc_tile47);

		JPanel wallV47 = new JPanel();
		GridBagConstraints gbc_wallV47 = new GridBagConstraints();
		gbc_wallV47.insets = new Insets(0, 0, 0, 0);
		gbc_wallV47.fill = GridBagConstraints.BOTH;
		gbc_wallV47.gridx = 5;
		gbc_wallV47.gridy = 10;
		panel_3.add(wallV47, gbc_wallV47);

		JPanel tile48 = new JPanel();

		tile48.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile48 = new GridBagConstraints();
		gbc_tile48.insets = new Insets(0, 0, 0, 0);
		gbc_tile48.fill = GridBagConstraints.BOTH;
		gbc_tile48.gridx = 6;
		gbc_tile48.gridy = 10;
		panel_3.add(tile48, gbc_tile48);

		JPanel wallV48 = new JPanel();
		GridBagConstraints gbc_wallV48 = new GridBagConstraints();
		gbc_wallV48.insets = new Insets(0, 0, 0, 0);
		gbc_wallV48.fill = GridBagConstraints.BOTH;
		gbc_wallV48.gridx = 7;
		gbc_wallV48.gridy = 10;
		panel_3.add(wallV48, gbc_wallV48);

		JPanel tile49 = new JPanel();

		tile49.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile49 = new GridBagConstraints();
		gbc_tile49.insets = new Insets(0, 0, 0, 0);
		gbc_tile49.fill = GridBagConstraints.BOTH;
		gbc_tile49.gridx = 8;
		gbc_tile49.gridy = 10;
		panel_3.add(tile49, gbc_tile49);

		JPanel wallV49 = new JPanel();
		GridBagConstraints gbc_wallV49 = new GridBagConstraints();
		gbc_wallV49.insets = new Insets(0, 0, 0, 0);
		gbc_wallV49.fill = GridBagConstraints.BOTH;
		gbc_wallV49.gridx = 9;
		gbc_wallV49.gridy = 10;
		panel_3.add(wallV49, gbc_wallV49);

		JPanel tile50 = new JPanel();

		tile50.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile50 = new GridBagConstraints();
		gbc_tile50.insets = new Insets(0, 0, 0, 0);
		gbc_tile50.fill = GridBagConstraints.BOTH;
		gbc_tile50.gridx = 10;
		gbc_tile50.gridy = 10;
		panel_3.add(tile50, gbc_tile50);

		JPanel wallV50 = new JPanel();
		GridBagConstraints gbc_wallV50 = new GridBagConstraints();
		gbc_wallV50.insets = new Insets(0, 0, 0, 0);
		gbc_wallV50.fill = GridBagConstraints.BOTH;
		gbc_wallV50.gridx = 11;
		gbc_wallV50.gridy = 10;
		panel_3.add(wallV50, gbc_wallV50);

		JPanel tile51 = new JPanel();

		tile51.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile51 = new GridBagConstraints();
		gbc_tile51.insets = new Insets(0, 0, 0, 0);
		gbc_tile51.fill = GridBagConstraints.BOTH;
		gbc_tile51.gridx = 12;
		gbc_tile51.gridy = 10;
		panel_3.add(tile51, gbc_tile51);

		JPanel wallV51 = new JPanel();
		GridBagConstraints gbc_wallV51 = new GridBagConstraints();
		gbc_wallV51.insets = new Insets(0, 0, 0, 0);
		gbc_wallV51.fill = GridBagConstraints.BOTH;
		gbc_wallV51.gridx = 13;
		gbc_wallV51.gridy = 10;
		panel_3.add(wallV51, gbc_wallV51);

		JPanel tile52 = new JPanel();

		tile52.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile52 = new GridBagConstraints();
		gbc_tile52.insets = new Insets(0, 0, 0, 0);
		gbc_tile52.fill = GridBagConstraints.BOTH;
		gbc_tile52.gridx = 14;
		gbc_tile52.gridy = 10;
		panel_3.add(tile52, gbc_tile52);

		JPanel wallV52 = new JPanel();
		GridBagConstraints gbc_wallV52 = new GridBagConstraints();
		gbc_wallV52.insets = new Insets(0, 0, 0, 0);
		gbc_wallV52.fill = GridBagConstraints.BOTH;
		gbc_wallV52.gridx = 15;
		gbc_wallV52.gridy = 10;
		panel_3.add(wallV52, gbc_wallV52);

		JPanel tile53 = new JPanel();

		tile53.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile53 = new GridBagConstraints();
		gbc_tile53.insets = new Insets(0, 0, 0, 0);
		gbc_tile53.fill = GridBagConstraints.BOTH;
		gbc_tile53.gridx = 16;
		gbc_tile53.gridy = 10;
		panel_3.add(tile53, gbc_tile53);

		JPanel wallH45 = new JPanel();
		GridBagConstraints gbc_wallH45 = new GridBagConstraints();
		gbc_wallH45.insets = new Insets(0, 0, 0, 0);
		gbc_wallH45.fill = GridBagConstraints.BOTH;
		gbc_wallH45.gridx = 0;
		gbc_wallH45.gridy = 11;
		panel_3.add(wallH45, gbc_wallH45);

		JPanel wallM55 = new JPanel();

		GridBagConstraints gbc_wallM55 = new GridBagConstraints();
		gbc_wallM55.insets = new Insets(0, 0, 0, 0);
		gbc_wallM55.fill = GridBagConstraints.BOTH;
		gbc_wallM55.gridx = 1;
		gbc_wallM55.gridy = 11;
		panel_3.add(wallM55, gbc_wallM55);
		listeners(wallM55, gbc_wallM55, panel_3, gbl_panel_3);

		JPanel wallH46 = new JPanel();
		GridBagConstraints gbc_wallH46 = new GridBagConstraints();
		gbc_wallH46.insets = new Insets(0, 0, 0, 0);
		gbc_wallH46.fill = GridBagConstraints.BOTH;
		gbc_wallH46.gridx = 2;
		gbc_wallH46.gridy = 11;
		panel_3.add(wallH46, gbc_wallH46);

		JPanel wallM56 = new JPanel();

		GridBagConstraints gbc_wallM56 = new GridBagConstraints();
		gbc_wallM56.insets = new Insets(0, 0, 0, 0);
		gbc_wallM56.fill = GridBagConstraints.BOTH;
		gbc_wallM56.gridx = 3;
		gbc_wallM56.gridy = 11;
		panel_3.add(wallM56, gbc_wallM56);

		listeners(wallM56, gbc_wallM56, panel_3, gbl_panel_3);

		JPanel wallH47 = new JPanel();
		GridBagConstraints gbc_wallH47 = new GridBagConstraints();
		gbc_wallH47.insets = new Insets(0, 0, 0, 0);
		gbc_wallH47.fill = GridBagConstraints.BOTH;
		gbc_wallH47.gridx = 4;
		gbc_wallH47.gridy = 11;
		panel_3.add(wallH47, gbc_wallH47);

		JPanel wallM57 = new JPanel();

		GridBagConstraints gbc_wallM57 = new GridBagConstraints();
		gbc_wallM57.insets = new Insets(0, 0, 0, 0);
		gbc_wallM57.fill = GridBagConstraints.BOTH;
		gbc_wallM57.gridx = 5;
		gbc_wallM57.gridy = 11;
		panel_3.add(wallM57, gbc_wallM57);
		listeners(wallM57, gbc_wallM57, panel_3, gbl_panel_3);

		JPanel wallH48 = new JPanel();
		GridBagConstraints gbc_wallH48 = new GridBagConstraints();
		gbc_wallH48.insets = new Insets(0, 0, 0, 0);
		gbc_wallH48.fill = GridBagConstraints.BOTH;
		gbc_wallH48.gridx = 6;
		gbc_wallH48.gridy = 11;
		panel_3.add(wallH48, gbc_wallH48);

		JPanel wallM58 = new JPanel();

		GridBagConstraints gbc_wallM58 = new GridBagConstraints();
		gbc_wallM58.insets = new Insets(0, 0, 0, 0);
		gbc_wallM58.fill = GridBagConstraints.BOTH;
		gbc_wallM58.gridx = 7;
		gbc_wallM58.gridy = 11;
		panel_3.add(wallM58, gbc_wallM58);
		listeners(wallM58, gbc_wallM58, panel_3, gbl_panel_3);

		JPanel wallH49 = new JPanel();
		GridBagConstraints gbc_wallH49 = new GridBagConstraints();
		gbc_wallH49.insets = new Insets(0, 0, 0, 0);
		gbc_wallH49.fill = GridBagConstraints.BOTH;
		gbc_wallH49.gridx = 8;
		gbc_wallH49.gridy = 11;
		panel_3.add(wallH49, gbc_wallH49);

		JPanel wallM59 = new JPanel();

		GridBagConstraints gbc_wallM59 = new GridBagConstraints();
		gbc_wallM59.insets = new Insets(0, 0, 0, 0);
		gbc_wallM59.fill = GridBagConstraints.BOTH;
		gbc_wallM59.gridx = 9;
		gbc_wallM59.gridy = 11;
		panel_3.add(wallM59, gbc_wallM59);

		listeners(wallM59, gbc_wallM59, panel_3, gbl_panel_3);

		JPanel wallH50 = new JPanel();
		GridBagConstraints gbc_wallH50 = new GridBagConstraints();
		gbc_wallH50.insets = new Insets(0, 0, 0, 0);
		gbc_wallH50.fill = GridBagConstraints.BOTH;
		gbc_wallH50.gridx = 10;
		gbc_wallH50.gridy = 11;
		panel_3.add(wallH50, gbc_wallH50);

		JPanel wallM60 = new JPanel();

		GridBagConstraints gbc_wallM60 = new GridBagConstraints();
		gbc_wallM60.insets = new Insets(0, 0, 0, 0);
		gbc_wallM60.fill = GridBagConstraints.BOTH;
		gbc_wallM60.gridx = 11;
		gbc_wallM60.gridy = 11;
		panel_3.add(wallM60, gbc_wallM60);
		listeners(wallM60, gbc_wallM60, panel_3, gbl_panel_3);

		JPanel wallH51 = new JPanel();
		GridBagConstraints gbc_wallH51 = new GridBagConstraints();
		gbc_wallH51.insets = new Insets(0, 0, 0, 0);
		gbc_wallH51.fill = GridBagConstraints.BOTH;
		gbc_wallH51.gridx = 12;
		gbc_wallH51.gridy = 11;
		panel_3.add(wallH51, gbc_wallH51);

		JPanel wallM61 = new JPanel();

		GridBagConstraints gbc_wallM61 = new GridBagConstraints();
		gbc_wallM61.insets = new Insets(0, 0, 0, 0);
		gbc_wallM61.fill = GridBagConstraints.BOTH;
		gbc_wallM61.gridx = 13;
		gbc_wallM61.gridy = 11;
		panel_3.add(wallM61, gbc_wallM61);
		listeners(wallM61, gbc_wallM61, panel_3, gbl_panel_3);

		JPanel wallH52 = new JPanel();
		GridBagConstraints gbc_wallH52 = new GridBagConstraints();
		gbc_wallH52.insets = new Insets(0, 0, 0, 0);
		gbc_wallH52.fill = GridBagConstraints.BOTH;
		gbc_wallH52.gridx = 14;
		gbc_wallH52.gridy = 11;
		panel_3.add(wallH52, gbc_wallH52);

		JPanel wallM62 = new JPanel();

		GridBagConstraints gbc_wallM62 = new GridBagConstraints();
		gbc_wallM62.insets = new Insets(0, 0, 0, 0);
		gbc_wallM62.fill = GridBagConstraints.BOTH;
		gbc_wallM62.gridx = 15;
		gbc_wallM62.gridy = 11;
		panel_3.add(wallM62, gbc_wallM62);
		listeners(wallM62, gbc_wallM62, panel_3, gbl_panel_3);

		JPanel wallH53 = new JPanel();
		GridBagConstraints gbc_wallH53 = new GridBagConstraints();
		gbc_wallH53.insets = new Insets(0, 0, 0, 0);
		gbc_wallH53.fill = GridBagConstraints.BOTH;
		gbc_wallH53.gridx = 16;
		gbc_wallH53.gridy = 11;
		panel_3.add(wallH53, gbc_wallH53);

		JPanel tile54 = new JPanel();

		tile54.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile54 = new GridBagConstraints();
		gbc_tile54.insets = new Insets(0, 0, 0, 0);
		gbc_tile54.fill = GridBagConstraints.BOTH;
		gbc_tile54.gridx = 0;
		gbc_tile54.gridy = 12;
		panel_3.add(tile54, gbc_tile54);

		JPanel wallV54 = new JPanel();
		GridBagConstraints gbc_wallV54 = new GridBagConstraints();
		gbc_wallV54.insets = new Insets(0, 0, 0, 0);
		gbc_wallV54.fill = GridBagConstraints.BOTH;
		gbc_wallV54.gridx = 1;
		gbc_wallV54.gridy = 12;
		panel_3.add(wallV54, gbc_wallV54);

		JPanel tile55 = new JPanel();

		tile55.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile55 = new GridBagConstraints();
		gbc_tile55.insets = new Insets(0, 0, 0, 0);
		gbc_tile55.fill = GridBagConstraints.BOTH;
		gbc_tile55.gridx = 2;
		gbc_tile55.gridy = 12;
		panel_3.add(tile55, gbc_tile55);

		JPanel wallV55 = new JPanel();
		GridBagConstraints gbc_wallV55 = new GridBagConstraints();
		gbc_wallV55.insets = new Insets(0, 0, 0, 0);
		gbc_wallV55.fill = GridBagConstraints.BOTH;
		gbc_wallV55.gridx = 3;
		gbc_wallV55.gridy = 12;
		panel_3.add(wallV55, gbc_wallV55);

		JPanel tile56 = new JPanel();

		tile56.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile56 = new GridBagConstraints();
		gbc_tile56.insets = new Insets(0, 0, 0, 0);
		gbc_tile56.fill = GridBagConstraints.BOTH;
		gbc_tile56.gridx = 4;
		gbc_tile56.gridy = 12;
		panel_3.add(tile56, gbc_tile56);

		JPanel wallV56 = new JPanel();
		GridBagConstraints gbc_wallV56 = new GridBagConstraints();
		gbc_wallV56.insets = new Insets(0, 0, 0, 0);
		gbc_wallV56.fill = GridBagConstraints.BOTH;
		gbc_wallV56.gridx = 5;
		gbc_wallV56.gridy = 12;
		panel_3.add(wallV56, gbc_wallV56);

		JPanel tile57 = new JPanel();

		tile57.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile57 = new GridBagConstraints();
		gbc_tile57.insets = new Insets(0, 0, 0, 0);
		gbc_tile57.fill = GridBagConstraints.BOTH;
		gbc_tile57.gridx = 6;
		gbc_tile57.gridy = 12;
		panel_3.add(tile57, gbc_tile57);

		JPanel wallV57 = new JPanel();
		GridBagConstraints gbc_wallV57 = new GridBagConstraints();
		gbc_wallV57.insets = new Insets(0, 0, 0, 0);
		gbc_wallV57.fill = GridBagConstraints.BOTH;
		gbc_wallV57.gridx = 7;
		gbc_wallV57.gridy = 12;
		panel_3.add(wallV57, gbc_wallV57);

		JPanel tile58 = new JPanel();

		tile58.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile58 = new GridBagConstraints();
		gbc_tile58.insets = new Insets(0, 0, 0, 0);
		gbc_tile58.fill = GridBagConstraints.BOTH;
		gbc_tile58.gridx = 8;
		gbc_tile58.gridy = 12;
		panel_3.add(tile58, gbc_tile58);

		JPanel wallV58 = new JPanel();
		GridBagConstraints gbc_wallV58 = new GridBagConstraints();
		gbc_wallV58.insets = new Insets(0, 0, 0, 0);
		gbc_wallV58.fill = GridBagConstraints.BOTH;
		gbc_wallV58.gridx = 9;
		gbc_wallV58.gridy = 12;
		panel_3.add(wallV58, gbc_wallV58);

		JPanel tile59 = new JPanel();

		tile59.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile59 = new GridBagConstraints();
		gbc_tile59.insets = new Insets(0, 0, 0, 0);
		gbc_tile59.fill = GridBagConstraints.BOTH;
		gbc_tile59.gridx = 10;
		gbc_tile59.gridy = 12;
		panel_3.add(tile59, gbc_tile59);

		JPanel wallV59 = new JPanel();
		GridBagConstraints gbc_wallV59 = new GridBagConstraints();
		gbc_wallV59.insets = new Insets(0, 0, 0, 0);
		gbc_wallV59.fill = GridBagConstraints.BOTH;
		gbc_wallV59.gridx = 11;
		gbc_wallV59.gridy = 12;
		panel_3.add(wallV59, gbc_wallV59);

		JPanel tile60 = new JPanel();

		tile60.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile60 = new GridBagConstraints();
		gbc_tile60.insets = new Insets(0, 0, 0, 0);
		gbc_tile60.fill = GridBagConstraints.BOTH;
		gbc_tile60.gridx = 12;
		gbc_tile60.gridy = 12;
		panel_3.add(tile60, gbc_tile60);

		JPanel wallV60 = new JPanel();
		GridBagConstraints gbc_wallV60 = new GridBagConstraints();
		gbc_wallV60.insets = new Insets(0, 0, 0, 0);
		gbc_wallV60.fill = GridBagConstraints.BOTH;
		gbc_wallV60.gridx = 13;
		gbc_wallV60.gridy = 12;
		panel_3.add(wallV60, gbc_wallV60);

		JPanel tile61 = new JPanel();

		tile61.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile61 = new GridBagConstraints();
		gbc_tile61.insets = new Insets(0, 0, 0, 0);
		gbc_tile61.fill = GridBagConstraints.BOTH;
		gbc_tile61.gridx = 14;
		gbc_tile61.gridy = 12;
		panel_3.add(tile61, gbc_tile61);

		JPanel wallV61 = new JPanel();
		GridBagConstraints gbc_wallV61 = new GridBagConstraints();
		gbc_wallV61.insets = new Insets(0, 0, 0, 0);
		gbc_wallV61.fill = GridBagConstraints.BOTH;
		gbc_wallV61.gridx = 15;
		gbc_wallV61.gridy = 12;
		panel_3.add(wallV61, gbc_wallV61);

		JPanel tile62 = new JPanel();

		tile62.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile62 = new GridBagConstraints();
		gbc_tile62.insets = new Insets(0, 0, 0, 0);
		gbc_tile62.fill = GridBagConstraints.BOTH;
		gbc_tile62.gridx = 16;
		gbc_tile62.gridy = 12;
		panel_3.add(tile62, gbc_tile62);

		JPanel wallH54 = new JPanel();
		GridBagConstraints gbc_wallH54 = new GridBagConstraints();
		gbc_wallH54.insets = new Insets(0, 0, 0, 0);
		gbc_wallH54.fill = GridBagConstraints.BOTH;
		gbc_wallH54.gridx = 0;
		gbc_wallH54.gridy = 13;
		panel_3.add(wallH54, gbc_wallH54);

		JPanel wallM64 = new JPanel();

		GridBagConstraints gbc_wallM64 = new GridBagConstraints();
		gbc_wallM64.insets = new Insets(0, 0, 0, 0);
		gbc_wallM64.fill = GridBagConstraints.BOTH;
		gbc_wallM64.gridx = 1;
		gbc_wallM64.gridy = 13;
		panel_3.add(wallM64, gbc_wallM64);
		listeners(wallM64, gbc_wallM64, panel_3, gbl_panel_3);

		JPanel wallH55 = new JPanel();
		GridBagConstraints gbc_wallH55 = new GridBagConstraints();
		gbc_wallH55.insets = new Insets(0, 0, 0, 0);
		gbc_wallH55.fill = GridBagConstraints.BOTH;
		gbc_wallH55.gridx = 2;
		gbc_wallH55.gridy = 13;
		panel_3.add(wallH55, gbc_wallH55);

		JPanel wallM65 = new JPanel();

		GridBagConstraints gbc_wallM65 = new GridBagConstraints();
		gbc_wallM65.insets = new Insets(0, 0, 0, 0);
		gbc_wallM65.fill = GridBagConstraints.BOTH;
		gbc_wallM65.gridx = 3;
		gbc_wallM65.gridy = 13;
		panel_3.add(wallM65, gbc_wallM65);
		listeners(wallM65, gbc_wallM65, panel_3, gbl_panel_3);

		JPanel wallH56 = new JPanel();
		GridBagConstraints gbc_wallH56 = new GridBagConstraints();
		gbc_wallH56.insets = new Insets(0, 0, 0, 0);
		gbc_wallH56.fill = GridBagConstraints.BOTH;
		gbc_wallH56.gridx = 4;
		gbc_wallH56.gridy = 13;
		panel_3.add(wallH56, gbc_wallH56);

		JPanel wallM66 = new JPanel();

		GridBagConstraints gbc_wallM66 = new GridBagConstraints();
		gbc_wallM66.insets = new Insets(0, 0, 0, 0);
		gbc_wallM66.fill = GridBagConstraints.BOTH;
		gbc_wallM66.gridx = 5;
		gbc_wallM66.gridy = 13;
		panel_3.add(wallM66, gbc_wallM66);
		listeners(wallM66, gbc_wallM66, panel_3, gbl_panel_3);

		JPanel wallH57 = new JPanel();
		GridBagConstraints gbc_wallH57 = new GridBagConstraints();
		gbc_wallH57.insets = new Insets(0, 0, 0, 0);
		gbc_wallH57.fill = GridBagConstraints.BOTH;
		gbc_wallH57.gridx = 6;
		gbc_wallH57.gridy = 13;
		panel_3.add(wallH57, gbc_wallH57);

		JPanel wallM67 = new JPanel();

		GridBagConstraints gbc_wallM67 = new GridBagConstraints();
		gbc_wallM67.insets = new Insets(0, 0, 0, 0);
		gbc_wallM67.fill = GridBagConstraints.BOTH;
		gbc_wallM67.gridx = 7;
		gbc_wallM67.gridy = 13;
		panel_3.add(wallM67, gbc_wallM67);
		listeners(wallM67, gbc_wallM67, panel_3, gbl_panel_3);

		JPanel wallH58 = new JPanel();
		GridBagConstraints gbc_wallH58 = new GridBagConstraints();
		gbc_wallH58.insets = new Insets(0, 0, 0, 0);
		gbc_wallH58.fill = GridBagConstraints.BOTH;
		gbc_wallH58.gridx = 8;
		gbc_wallH58.gridy = 13;
		panel_3.add(wallH58, gbc_wallH58);

		JPanel wallM68 = new JPanel();

		GridBagConstraints gbc_wallM68 = new GridBagConstraints();
		gbc_wallM68.insets = new Insets(0, 0, 0, 0);
		gbc_wallM68.fill = GridBagConstraints.BOTH;
		gbc_wallM68.gridx = 9;
		gbc_wallM68.gridy = 13;
		panel_3.add(wallM68, gbc_wallM68);
		listeners(wallM68, gbc_wallM68, panel_3, gbl_panel_3);

		JPanel wallH59 = new JPanel();
		GridBagConstraints gbc_wallH59 = new GridBagConstraints();
		gbc_wallH59.insets = new Insets(0, 0, 0, 0);
		gbc_wallH59.fill = GridBagConstraints.BOTH;
		gbc_wallH59.gridx = 10;
		gbc_wallH59.gridy = 13;
		panel_3.add(wallH59, gbc_wallH59);

		JPanel wallM69 = new JPanel();

		GridBagConstraints gbc_wallM69 = new GridBagConstraints();
		gbc_wallM69.insets = new Insets(0, 0, 0, 0);
		gbc_wallM69.fill = GridBagConstraints.BOTH;
		gbc_wallM69.gridx = 11;
		gbc_wallM69.gridy = 13;
		panel_3.add(wallM69, gbc_wallM69);
		listeners(wallM69, gbc_wallM69, panel_3, gbl_panel_3);

		JPanel wallH60 = new JPanel();
		GridBagConstraints gbc_wallH60 = new GridBagConstraints();
		gbc_wallH60.insets = new Insets(0, 0, 0, 0);
		gbc_wallH60.fill = GridBagConstraints.BOTH;
		gbc_wallH60.gridx = 12;
		gbc_wallH60.gridy = 13;
		panel_3.add(wallH60, gbc_wallH60);

		JPanel wallM70 = new JPanel();

		GridBagConstraints gbc_wallM70 = new GridBagConstraints();
		gbc_wallM70.insets = new Insets(0, 0, 0, 0);
		gbc_wallM70.fill = GridBagConstraints.BOTH;
		gbc_wallM70.gridx = 13;
		gbc_wallM70.gridy = 13;
		panel_3.add(wallM70, gbc_wallM70);
		listeners(wallM70, gbc_wallM70, panel_3, gbl_panel_3);

		JPanel wallH61 = new JPanel();
		GridBagConstraints gbc_wallH61 = new GridBagConstraints();
		gbc_wallH61.insets = new Insets(0, 0, 0, 0);
		gbc_wallH61.fill = GridBagConstraints.BOTH;
		gbc_wallH61.gridx = 14;
		gbc_wallH61.gridy = 13;
		panel_3.add(wallH61, gbc_wallH61);

		JPanel wallM71 = new JPanel();

		GridBagConstraints gbc_wallM71 = new GridBagConstraints();
		gbc_wallM71.insets = new Insets(0, 0, 0, 0);
		gbc_wallM71.fill = GridBagConstraints.BOTH;
		gbc_wallM71.gridx = 15;
		gbc_wallM71.gridy = 13;
		panel_3.add(wallM71, gbc_wallM71);
		listeners(wallM71, gbc_wallM71, panel_3, gbl_panel_3);

		JPanel wallH62 = new JPanel();
		GridBagConstraints gbc_wallH62 = new GridBagConstraints();
		gbc_wallH62.insets = new Insets(0, 0, 0, 0);
		gbc_wallH62.fill = GridBagConstraints.BOTH;
		gbc_wallH62.gridx = 16;
		gbc_wallH62.gridy = 13;
		panel_3.add(wallH62, gbc_wallH62);

		JPanel tile63 = new JPanel();

		tile63.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile63 = new GridBagConstraints();
		gbc_tile63.insets = new Insets(0, 0, 0, 0);
		gbc_tile63.fill = GridBagConstraints.BOTH;
		gbc_tile63.gridx = 0;
		gbc_tile63.gridy = 14;
		panel_3.add(tile63, gbc_tile63);

		JPanel wallV63 = new JPanel();
		GridBagConstraints gbc_wallV63 = new GridBagConstraints();
		gbc_wallV63.insets = new Insets(0, 0, 0, 0);
		gbc_wallV63.fill = GridBagConstraints.BOTH;
		gbc_wallV63.gridx = 1;
		gbc_wallV63.gridy = 14;
		panel_3.add(wallV63, gbc_wallV63);

		JPanel tile64 = new JPanel();

		tile64.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile64 = new GridBagConstraints();
		gbc_tile64.insets = new Insets(0, 0, 0, 0);
		gbc_tile64.fill = GridBagConstraints.BOTH;
		gbc_tile64.gridx = 2;
		gbc_tile64.gridy = 14;
		panel_3.add(tile64, gbc_tile64);

		JPanel wallV64 = new JPanel();
		GridBagConstraints gbc_wallV64 = new GridBagConstraints();
		gbc_wallV64.insets = new Insets(0, 0, 0, 0);
		gbc_wallV64.fill = GridBagConstraints.BOTH;
		gbc_wallV64.gridx = 3;
		gbc_wallV64.gridy = 14;
		panel_3.add(wallV64, gbc_wallV64);

		JPanel tile65 = new JPanel();

		tile65.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile65 = new GridBagConstraints();
		gbc_tile65.insets = new Insets(0, 0, 0, 0);
		gbc_tile65.fill = GridBagConstraints.BOTH;
		gbc_tile65.gridx = 4;
		gbc_tile65.gridy = 14;
		panel_3.add(tile65, gbc_tile65);

		JPanel wallV65 = new JPanel();
		GridBagConstraints gbc_wallV65 = new GridBagConstraints();
		gbc_wallV65.insets = new Insets(0, 0, 0, 0);
		gbc_wallV65.fill = GridBagConstraints.BOTH;
		gbc_wallV65.gridx = 5;
		gbc_wallV65.gridy = 14;
		panel_3.add(wallV65, gbc_wallV65);

		JPanel tile66 = new JPanel();

		tile66.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile66 = new GridBagConstraints();
		gbc_tile66.insets = new Insets(0, 0, 0, 0);
		gbc_tile66.fill = GridBagConstraints.BOTH;
		gbc_tile66.gridx = 6;
		gbc_tile66.gridy = 14;
		panel_3.add(tile66, gbc_tile66);

		JPanel wallV66 = new JPanel();
		GridBagConstraints gbc_wallV66 = new GridBagConstraints();
		gbc_wallV66.insets = new Insets(0, 0, 0, 0);
		gbc_wallV66.fill = GridBagConstraints.BOTH;
		gbc_wallV66.gridx = 7;
		gbc_wallV66.gridy = 14;
		panel_3.add(wallV66, gbc_wallV66);

		JPanel tile67 = new JPanel();

		tile67.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile67 = new GridBagConstraints();
		gbc_tile67.insets = new Insets(0, 0, 0, 0);
		gbc_tile67.fill = GridBagConstraints.BOTH;
		gbc_tile67.gridx = 8;
		gbc_tile67.gridy = 14;
		panel_3.add(tile67, gbc_tile67);

		JPanel wallV67 = new JPanel();
		GridBagConstraints gbc_wallV67 = new GridBagConstraints();
		gbc_wallV67.insets = new Insets(0, 0, 0, 0);
		gbc_wallV67.fill = GridBagConstraints.BOTH;
		gbc_wallV67.gridx = 9;
		gbc_wallV67.gridy = 14;
		panel_3.add(wallV67, gbc_wallV67);

		JPanel tile68 = new JPanel();

		tile68.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile68 = new GridBagConstraints();
		gbc_tile68.insets = new Insets(0, 0, 0, 0);
		gbc_tile68.fill = GridBagConstraints.BOTH;
		gbc_tile68.gridx = 10;
		gbc_tile68.gridy = 14;
		panel_3.add(tile68, gbc_tile68);

		JPanel wallV68 = new JPanel();
		GridBagConstraints gbc_wallV68 = new GridBagConstraints();
		gbc_wallV68.insets = new Insets(0, 0, 0, 0);
		gbc_wallV68.fill = GridBagConstraints.BOTH;
		gbc_wallV68.gridx = 11;
		gbc_wallV68.gridy = 14;
		panel_3.add(wallV68, gbc_wallV68);

		JPanel tile69 = new JPanel();

		tile69.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile69 = new GridBagConstraints();
		gbc_tile69.insets = new Insets(0, 0, 0, 0);
		gbc_tile69.fill = GridBagConstraints.BOTH;
		gbc_tile69.gridx = 12;
		gbc_tile69.gridy = 14;
		panel_3.add(tile69, gbc_tile69);

		JPanel wallV69 = new JPanel();
		GridBagConstraints gbc_wallV69 = new GridBagConstraints();
		gbc_wallV69.insets = new Insets(0, 0, 0, 0);
		gbc_wallV69.fill = GridBagConstraints.BOTH;
		gbc_wallV69.gridx = 13;
		gbc_wallV69.gridy = 14;
		panel_3.add(wallV69, gbc_wallV69);

		JPanel tile70 = new JPanel();

		tile70.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile70 = new GridBagConstraints();
		gbc_tile70.insets = new Insets(0, 0, 0, 0);
		gbc_tile70.fill = GridBagConstraints.BOTH;
		gbc_tile70.gridx = 14;
		gbc_tile70.gridy = 14;
		panel_3.add(tile70, gbc_tile70);

		JPanel wallV70 = new JPanel();
		GridBagConstraints gbc_wallV70 = new GridBagConstraints();
		gbc_wallV70.insets = new Insets(0, 0, 0, 0);
		gbc_wallV70.fill = GridBagConstraints.BOTH;
		gbc_wallV70.gridx = 15;
		gbc_wallV70.gridy = 14;
		panel_3.add(wallV70, gbc_wallV70);

		JPanel tile71 = new JPanel();

		tile71.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile71 = new GridBagConstraints();
		gbc_tile71.insets = new Insets(0, 0, 0, 0);
		gbc_tile71.fill = GridBagConstraints.BOTH;
		gbc_tile71.gridx = 16;
		gbc_tile71.gridy = 14;
		panel_3.add(tile71, gbc_tile71);

		JPanel wallH63 = new JPanel();
		GridBagConstraints gbc_wallH63 = new GridBagConstraints();
		gbc_wallH63.insets = new Insets(0, 0, 0, 0);
		gbc_wallH63.fill = GridBagConstraints.BOTH;
		gbc_wallH63.gridx = 0;
		gbc_wallH63.gridy = 15;
		panel_3.add(wallH63, gbc_wallH63);

		JPanel wallM73 = new JPanel();

		GridBagConstraints gbc_wallM73 = new GridBagConstraints();
		gbc_wallM73.insets = new Insets(0, 0, 0, 0);
		gbc_wallM73.fill = GridBagConstraints.BOTH;
		gbc_wallM73.gridx = 1;
		gbc_wallM73.gridy = 15;
		panel_3.add(wallM73, gbc_wallM73);
		listeners(wallM73, gbc_wallM73, panel_3, gbl_panel_3);

		JPanel wallH64 = new JPanel();
		wallH64.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
			}
		});
		GridBagConstraints gbc_wallH64 = new GridBagConstraints();
		gbc_wallH64.insets = new Insets(0, 0, 0, 0);
		gbc_wallH64.fill = GridBagConstraints.BOTH;
		gbc_wallH64.gridx = 2;
		gbc_wallH64.gridy = 15;
		panel_3.add(wallH64, gbc_wallH64);

		JPanel wallM74 = new JPanel();

		GridBagConstraints gbc_wallM74 = new GridBagConstraints();
		gbc_wallM74.insets = new Insets(0, 0, 0, 0);
		gbc_wallM74.fill = GridBagConstraints.BOTH;
		gbc_wallM74.gridx = 3;
		gbc_wallM74.gridy = 15;
		panel_3.add(wallM74, gbc_wallM74);
		listeners(wallM74, gbc_wallM74, panel_3, gbl_panel_3);

		JPanel wallH65 = new JPanel();
		GridBagConstraints gbc_wallH65 = new GridBagConstraints();
		gbc_wallH65.insets = new Insets(0, 0, 0, 0);
		gbc_wallH65.fill = GridBagConstraints.BOTH;
		gbc_wallH65.gridx = 4;
		gbc_wallH65.gridy = 15;
		panel_3.add(wallH65, gbc_wallH65);

		JPanel wallM75 = new JPanel();

		GridBagConstraints gbc_wallM75 = new GridBagConstraints();
		gbc_wallM75.insets = new Insets(0, 0, 0, 0);
		gbc_wallM75.fill = GridBagConstraints.BOTH;
		gbc_wallM75.gridx = 5;
		gbc_wallM75.gridy = 15;
		panel_3.add(wallM75, gbc_wallM75);
		listeners(wallM75, gbc_wallM75, panel_3, gbl_panel_3);

		JPanel wallH66 = new JPanel();
		GridBagConstraints gbc_wallH66 = new GridBagConstraints();
		gbc_wallH66.insets = new Insets(0, 0, 0, 0);
		gbc_wallH66.fill = GridBagConstraints.BOTH;
		gbc_wallH66.gridx = 6;
		gbc_wallH66.gridy = 15;
		panel_3.add(wallH66, gbc_wallH66);

		JPanel wallM76 = new JPanel();

		GridBagConstraints gbc_wallM76 = new GridBagConstraints();
		gbc_wallM76.insets = new Insets(0, 0, 0, 0);
		gbc_wallM76.fill = GridBagConstraints.BOTH;
		gbc_wallM76.gridx = 7;
		gbc_wallM76.gridy = 15;
		panel_3.add(wallM76, gbc_wallM76);
		listeners(wallM76, gbc_wallM76, panel_3, gbl_panel_3);

		JPanel wallH67 = new JPanel();
		GridBagConstraints gbc_wallH67 = new GridBagConstraints();
		gbc_wallH67.insets = new Insets(0, 0, 0, 0);
		gbc_wallH67.fill = GridBagConstraints.BOTH;
		gbc_wallH67.gridx = 8;
		gbc_wallH67.gridy = 15;
		panel_3.add(wallH67, gbc_wallH67);

		JPanel wallM77 = new JPanel();

		GridBagConstraints gbc_wallM77 = new GridBagConstraints();
		gbc_wallM77.insets = new Insets(0, 0, 0, 0);
		gbc_wallM77.fill = GridBagConstraints.BOTH;
		gbc_wallM77.gridx = 9;
		gbc_wallM77.gridy = 15;
		panel_3.add(wallM77, gbc_wallM77);
		listeners(wallM77, gbc_wallM77, panel_3, gbl_panel_3);

		JPanel wallH68 = new JPanel();
		GridBagConstraints gbc_wallH68 = new GridBagConstraints();
		gbc_wallH68.insets = new Insets(0, 0, 0, 0);
		gbc_wallH68.fill = GridBagConstraints.BOTH;
		gbc_wallH68.gridx = 10;
		gbc_wallH68.gridy = 15;
		panel_3.add(wallH68, gbc_wallH68);

		JPanel wallM78 = new JPanel();

		GridBagConstraints gbc_wallM78 = new GridBagConstraints();
		gbc_wallM78.insets = new Insets(0, 0, 0, 0);
		gbc_wallM78.fill = GridBagConstraints.BOTH;
		gbc_wallM78.gridx = 11;
		gbc_wallM78.gridy = 15;
		panel_3.add(wallM78, gbc_wallM78);
		listeners(wallM78, gbc_wallM78, panel_3, gbl_panel_3);

		JPanel wallH69 = new JPanel();
		GridBagConstraints gbc_wallH69 = new GridBagConstraints();
		gbc_wallH69.insets = new Insets(0, 0, 0, 0);
		gbc_wallH69.fill = GridBagConstraints.BOTH;
		gbc_wallH69.gridx = 12;
		gbc_wallH69.gridy = 15;
		panel_3.add(wallH69, gbc_wallH69);

		JPanel wallM79 = new JPanel();

		GridBagConstraints gbc_wallM79 = new GridBagConstraints();
		// wallM79.setBackground(Color.CYAN);
		gbc_wallM79.insets = new Insets(0, 0, 0, 0);
		gbc_wallM79.fill = GridBagConstraints.BOTH;
		gbc_wallM79.gridx = 13;
		gbc_wallM79.gridy = 15;
		panel_3.add(wallM79, gbc_wallM79);
		listeners(wallM79, gbc_wallM79, panel_3, gbl_panel_3);

		JPanel wallH70 = new JPanel();
		GridBagConstraints gbc_wallH70 = new GridBagConstraints();
		gbc_wallH70.insets = new Insets(0, 0, 0, 0);
		gbc_wallH70.fill = GridBagConstraints.BOTH;
		gbc_wallH70.gridx = 14;
		gbc_wallH70.gridy = 15;
		panel_3.add(wallH70, gbc_wallH70);

		JPanel wallM80 = new JPanel();
		GridBagConstraints gbc_wallM80 = new GridBagConstraints();
		gbc_wallM80.insets = new Insets(0, 0, 0, 0);
		gbc_wallM80.fill = GridBagConstraints.BOTH;
		gbc_wallM80.gridx = 15;
		gbc_wallM80.gridy = 15;
		panel_3.add(wallM80, gbc_wallM80);
		listeners(wallM80, gbc_wallM80, panel_3, gbl_panel_3);

		JPanel wallH71 = new JPanel();
		GridBagConstraints gbc_wallH71 = new GridBagConstraints();
		gbc_wallH71.insets = new Insets(0, 0, 0, 0);
		gbc_wallH71.fill = GridBagConstraints.BOTH;
		gbc_wallH71.gridx = 16;
		gbc_wallH71.gridy = 15;
		panel_3.add(wallH71, gbc_wallH71);

		JPanel tile72 = new JPanel();

		tile72.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile72 = new GridBagConstraints();
		gbc_tile72.insets = new Insets(0, 0, 0, 0);
		gbc_tile72.fill = GridBagConstraints.BOTH;
		gbc_tile72.gridx = 0;
		gbc_tile72.gridy = 16;
		panel_3.add(tile72, gbc_tile72);

		JPanel wallV72 = new JPanel();
		GridBagConstraints gbc_wallV72 = new GridBagConstraints();
		gbc_wallV72.insets = new Insets(0, 0, 0, 0);
		gbc_wallV72.fill = GridBagConstraints.BOTH;
		gbc_wallV72.gridx = 1;
		gbc_wallV72.gridy = 16;
		panel_3.add(wallV72, gbc_wallV72);

		JPanel tile73 = new JPanel();

		tile73.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile73 = new GridBagConstraints();
		gbc_tile73.insets = new Insets(0, 0, 0, 0);
		gbc_tile73.fill = GridBagConstraints.BOTH;
		gbc_tile73.gridx = 2;
		gbc_tile73.gridy = 16;
		panel_3.add(tile73, gbc_tile73);

		JPanel wallV73 = new JPanel();
		GridBagConstraints gbc_wallV73 = new GridBagConstraints();
		gbc_wallV73.insets = new Insets(0, 0, 0, 0);
		gbc_wallV73.fill = GridBagConstraints.BOTH;
		gbc_wallV73.gridx = 3;
		gbc_wallV73.gridy = 16;
		panel_3.add(wallV73, gbc_wallV73);

		JPanel tile74 = new JPanel();

		tile74.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile74 = new GridBagConstraints();
		gbc_tile74.insets = new Insets(0, 0, 0, 0);
		gbc_tile74.fill = GridBagConstraints.BOTH;
		gbc_tile74.gridx = 4;
		gbc_tile74.gridy = 16;
		panel_3.add(tile74, gbc_tile74);

		JPanel wallV74 = new JPanel();
		GridBagConstraints gbc_wallV74 = new GridBagConstraints();
		gbc_wallV74.insets = new Insets(0, 0, 0, 0);
		gbc_wallV74.fill = GridBagConstraints.BOTH;
		gbc_wallV74.gridx = 5;
		gbc_wallV74.gridy = 16;
		panel_3.add(wallV74, gbc_wallV74);

		JPanel tile75 = new JPanel();

		tile75.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile75 = new GridBagConstraints();
		gbc_tile75.insets = new Insets(0, 0, 0, 0);
		gbc_tile75.fill = GridBagConstraints.BOTH;
		gbc_tile75.gridx = 6;
		gbc_tile75.gridy = 16;
		panel_3.add(tile75, gbc_tile75);

		JPanel wallV75 = new JPanel();
		GridBagConstraints gbc_wallV75 = new GridBagConstraints();
		gbc_wallV75.insets = new Insets(0, 0, 0, 0);
		gbc_wallV75.fill = GridBagConstraints.BOTH;
		gbc_wallV75.gridx = 7;
		gbc_wallV75.gridy = 16;
		panel_3.add(wallV75, gbc_wallV75);

		JPanel tile76 = new JPanel();

		tile76.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile76 = new GridBagConstraints();
		gbc_tile76.insets = new Insets(0, 0, 0, 0);
		gbc_tile76.fill = GridBagConstraints.BOTH;
		gbc_tile76.gridx = 8;
		gbc_tile76.gridy = 16;
		panel_3.add(tile76, gbc_tile76);

		JPanel wallV76 = new JPanel();
		GridBagConstraints gbc_wallV76 = new GridBagConstraints();
		gbc_wallV76.insets = new Insets(0, 0, 0, 0);
		gbc_wallV76.fill = GridBagConstraints.BOTH;
		gbc_wallV76.gridx = 9;
		gbc_wallV76.gridy = 16;
		panel_3.add(wallV76, gbc_wallV76);

		JPanel tile77 = new JPanel();

		tile77.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile77 = new GridBagConstraints();
		gbc_tile77.insets = new Insets(0, 0, 0, 0);
		gbc_tile77.fill = GridBagConstraints.BOTH;
		gbc_tile77.gridx = 10;
		gbc_tile77.gridy = 16;
		panel_3.add(tile77, gbc_tile77);

		JPanel wallV77 = new JPanel();
		GridBagConstraints gbc_wallV77 = new GridBagConstraints();
		gbc_wallV77.insets = new Insets(0, 0, 0, 0);
		gbc_wallV77.fill = GridBagConstraints.BOTH;
		gbc_wallV77.gridx = 11;
		gbc_wallV77.gridy = 16;
		panel_3.add(wallV77, gbc_wallV77);

		JPanel tile78 = new JPanel();

		tile78.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile78 = new GridBagConstraints();
		gbc_tile78.insets = new Insets(0, 0, 0, 0);
		gbc_tile78.fill = GridBagConstraints.BOTH;
		gbc_tile78.gridx = 12;
		gbc_tile78.gridy = 16;
		panel_3.add(tile78, gbc_tile78);

		JPanel wallV78 = new JPanel();
		GridBagConstraints gbc_wallV78 = new GridBagConstraints();
		gbc_wallV78.insets = new Insets(0, 0, 0, 0);
		gbc_wallV78.fill = GridBagConstraints.BOTH;
		gbc_wallV78.gridx = 13;
		gbc_wallV78.gridy = 16;
		panel_3.add(wallV78, gbc_wallV78);

		JPanel tile79 = new JPanel();

		tile79.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile79 = new GridBagConstraints();
		gbc_tile79.insets = new Insets(0, 0, 0, 0);
		gbc_tile79.fill = GridBagConstraints.BOTH;
		gbc_tile79.gridx = 14;
		gbc_tile79.gridy = 16;
		panel_3.add(tile79, gbc_tile79);

		JPanel wallV79 = new JPanel();
		GridBagConstraints gbc_wallV79 = new GridBagConstraints();
		gbc_wallV79.insets = new Insets(0, 0, 0, 0);
		gbc_wallV79.fill = GridBagConstraints.BOTH;
		gbc_wallV79.gridx = 15;
		gbc_wallV79.gridy = 16;
		panel_3.add(wallV79, gbc_wallV79);

		JPanel tile80 = new JPanel();

		tile80.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_tile80 = new GridBagConstraints();
		gbc_tile80.fill = GridBagConstraints.BOTH;
		gbc_tile80.gridx = 16;
		gbc_tile80.gridy = 16;
		panel_3.add(tile80, gbc_tile80);

		if (!inReplayMode) {

			listenersTile(tile14, gbc_tile14, panel_3, gbl_panel_3);
			listenersTile(tile15, gbc_tile15, panel_3, gbl_panel_3);
			listenersTile(tile16, gbc_tile16, panel_3, gbl_panel_3);
			listenersTile(tile17, gbc_tile17, panel_3, gbl_panel_3);
			listenersTile(tile18, gbc_tile18, panel_3, gbl_panel_3);
			listenersTile(tile19, gbc_tile19, panel_3, gbl_panel_3);
			listenersTile(tile20, gbc_tile20, panel_3, gbl_panel_3);
			listenersTile(tile21, gbc_tile21, panel_3, gbl_panel_3);
			listenersTile(tile22, gbc_tile22, panel_3, gbl_panel_3);
			listenersTile(tile23, gbc_tile23, panel_3, gbl_panel_3);
			listenersTile(tile24, gbc_tile24, panel_3, gbl_panel_3);
			listenersTile(tile25, gbc_tile25, panel_3, gbl_panel_3);
			listenersTile(tile26, gbc_tile26, panel_3, gbl_panel_3);
			listenersTile(tile27, gbc_tile27, panel_3, gbl_panel_3);
			listenersTile(tile28, gbc_tile28, panel_3, gbl_panel_3);
			listenersTile(tile29, gbc_tile29, panel_3, gbl_panel_3);
			listenersTile(tile30, gbc_tile30, panel_3, gbl_panel_3);
			listenersTile(tile31, gbc_tile31, panel_3, gbl_panel_3);
			listenersTile(tile32, gbc_tile32, panel_3, gbl_panel_3);
			listenersTile(tile33, gbc_tile33, panel_3, gbl_panel_3);
			listenersTile(tile34, gbc_tile34, panel_3, gbl_panel_3);
			listenersTile(tile35, gbc_tile35, panel_3, gbl_panel_3);
			listenersTile(tile36, gbc_tile36, panel_3, gbl_panel_3);
			listenersTile(tile37, gbc_tile37, panel_3, gbl_panel_3);
			listenersTile(tile38, gbc_tile38, panel_3, gbl_panel_3);
			listenersTile(tile39, gbc_tile39, panel_3, gbl_panel_3);
			listenersTile(tile40, gbc_tile40, panel_3, gbl_panel_3);
			listenersTile(tile41, gbc_tile41, panel_3, gbl_panel_3);
			listenersTile(tile42, gbc_tile42, panel_3, gbl_panel_3);
			listenersTile(tile43, gbc_tile43, panel_3, gbl_panel_3);
			listenersTile(tile44, gbc_tile44, panel_3, gbl_panel_3);
			listenersTile(tile45, gbc_tile45, panel_3, gbl_panel_3);
			listenersTile(tile46, gbc_tile46, panel_3, gbl_panel_3);
			listenersTile(tile47, gbc_tile47, panel_3, gbl_panel_3);
			listenersTile(tile48, gbc_tile48, panel_3, gbl_panel_3);
			listenersTile(tile49, gbc_tile49, panel_3, gbl_panel_3);
			listenersTile(tile50, gbc_tile50, panel_3, gbl_panel_3);
			listenersTile(tile51, gbc_tile51, panel_3, gbl_panel_3);
			listenersTile(tile52, gbc_tile52, panel_3, gbl_panel_3);
			listenersTile(tile53, gbc_tile53, panel_3, gbl_panel_3);
			listenersTile(tile54, gbc_tile54, panel_3, gbl_panel_3);
			listenersTile(tile55, gbc_tile55, panel_3, gbl_panel_3);
			listenersTile(tile56, gbc_tile56, panel_3, gbl_panel_3);
			listenersTile(tile57, gbc_tile57, panel_3, gbl_panel_3);
			listenersTile(tile58, gbc_tile58, panel_3, gbl_panel_3);
			listenersTile(tile59, gbc_tile59, panel_3, gbl_panel_3);
			listenersTile(tile60, gbc_tile60, panel_3, gbl_panel_3);
			listenersTile(tile61, gbc_tile61, panel_3, gbl_panel_3);
			listenersTile(tile62, gbc_tile62, panel_3, gbl_panel_3);
			listenersTile(tile63, gbc_tile63, panel_3, gbl_panel_3);
			listenersTile(tile64, gbc_tile64, panel_3, gbl_panel_3);
			listenersTile(tile65, gbc_tile65, panel_3, gbl_panel_3);
			listenersTile(tile66, gbc_tile66, panel_3, gbl_panel_3);
			listenersTile(tile67, gbc_tile67, panel_3, gbl_panel_3);
			listenersTile(tile68, gbc_tile68, panel_3, gbl_panel_3);
			listenersTile(tile69, gbc_tile69, panel_3, gbl_panel_3);
			listenersTile(tile70, gbc_tile70, panel_3, gbl_panel_3);
			listenersTile(tile71, gbc_tile71, panel_3, gbl_panel_3);
			listenersTile(tile72, gbc_tile72, panel_3, gbl_panel_3);
			listenersTile(tile73, gbc_tile73, panel_3, gbl_panel_3);
			listenersTile(tile74, gbc_tile74, panel_3, gbl_panel_3);
			listenersTile(tile75, gbc_tile75, panel_3, gbl_panel_3);
			listenersTile(tile76, gbc_tile76, panel_3, gbl_panel_3);
			listenersTile(tile77, gbc_tile77, panel_3, gbl_panel_3);
			listenersTile(tile78, gbc_tile78, panel_3, gbl_panel_3);
			listenersTile(tile79, gbc_tile79, panel_3, gbl_panel_3);
			listenersTile(tile80, gbc_tile80, panel_3, gbl_panel_3);
		}
	}

	public void replayModeIn() {
		QuoridorGameController.goToReplayMode();

	}

	public void replayModeOut() {
		QuoridorGameController.exitReplayMode();

	}

	public void prevMove() {
		QuoridorGameController.stepBackward();
		refreshWalls();
		refreshData(panel_3, gbl_panel_3);
	}
	public void nextMove() {
		QuoridorGameController.stepForward();
		refreshWalls();
		refreshData(panel_3, gbl_panel_3);
	}
	public void refreshWalls(){
		walls.clear();
		int wallsSize = walls.size();
		for (TOWall wall : QuoridorGameController.getWalls()) {
			walls.add(wall);
			colorWalls(panel_3, gbl_panel_3);
		}
		wallsSize = walls.size();
	}

	/**
	 * Toggles the access to the buttons depending if the game is in replay mode or
	 * not.
	 *
	 * @author Katrina Poulin
	 * @param inReplay Game in replay mode
	 */
	public static void replayModeButtons(boolean inReplay) {
		addWallButton.setEnabled(!inReplay);
		rotateWallButton.setEnabled(!inReplay);
		prevButton.setEnabled(inReplay);
		nextButton.setEnabled(inReplay);
		jumpStartVar.setEnabled(inReplay);
		jumpEndVar.setEnabled(inReplay);
	}

	public static void setPNButtons() {
//		nextButton.setEnabled(QuoridorGameController.currReplayIndex<QuoridorGameController.getMovesNo());
//		prevButton.setEnabled(QuoridorGameController.currReplayIndex>0);
	}

	public static void updateGameState() {

		TOPlayer whitePlayer = QuoridorGameController.getWhitePlayer();
		TOPlayer blackPlayer = QuoridorGameController.getBlackPlayer();
		// System.out.println(QuoidorGameController.getPlayerToMove().toString());

		whitePawnRow = whitePlayer.getRow();
		whitePawnCol = whitePlayer.getCol();
		blackPawnRow = blackPlayer.getRow();
		blackPawnCol = blackPlayer.getCol();

		int[] state = { whitePawnRow, whitePawnCol, blackPawnRow, blackPawnCol, newWallRow, newWallCol, newWallDir };
		if ((gameStates.size() == 0) || !isInStates(state, gameStates.get(gameStates.size() - 1))) {
			gameStates.add(state);
		}

		newWallRow = 0;
		newWallCol = 0;
		newWallDir = 0;

	}

	/**
	 * When in Replay Mode, jumps to the next move in the list.
	 *
	 * @author Katrina Poulin
	 */
//	public static void nextMove() {
////		int index = QuoridorGameController.currReplayIndex;
//		// int index = QuoridorGameController.currReplayIndex+1;
//		if (gameStates.get(index)[4] == 0) {// pawnMove
//			whitePawnRow = gameStates.get(index)[0];
//			whitePawnCol = gameStates.get(index)[1];
//			blackPawnRow = gameStates.get(index)[2];
//			blackPawnCol = gameStates.get(index)[3];
//			refreshData(replayPanel, gbl);
//
//		} else {// wallMove
//			addWallReplay(gameStates.get(index));
//		}
//	}

	/**
	 * When in Replay Mode, jumps to the previous move in the list.
	 *
	 * @author Katrina Poulin
	 */
//	public static void prevMove() {
////		int index = QuoridorGameController.currReplayIndex + 1;
//		// int index = QuoridorGameController.currReplayIndex+1;
//		if (gameStates.get(index)[4] == 0) {// pawnMove
//			whitePawnRow = gameStates.get(index-1)[0];
//			whitePawnCol = gameStates.get(index-1)[1];
//			blackPawnRow = gameStates.get(index-1)[2];
//			blackPawnCol = gameStates.get(index-1)[3];
//			refreshData(replayPanel, gbl);
//		} else {// wallMove
//			remWallReplay(gameStates.get(index));
//		}
//	}

	private static void addWallReplay(int[] wallState) {
		Component match = null;
		int actualRow = wallState[4];
		int actualCol = wallState[5];
		if (wallState[6] == 1) {// isHorizontal
			for (int i = 0; i < 3; i++) {
				int x = (actualCol - 2) * 2 + i;
				int y = (actualRow - 2) * 2 + 1;

				// System.out.println("entering horizontal for loop");
				for (Component comp : replayPanel.getComponents()) {
					GridBagConstraints gbc = gbl.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						match = comp;
					}

					if (match != null) {
						match.setBackground(Color.BLUE);

					}

				}
			}
		} else {
			for (int i = 0; i < 3; i++) {
				// System.out.println("entering vertical for loop");
				int x = (actualCol - 2) * 2;
				int y = (actualRow - 2) * 2 - 1 + i;
				for (Component comp : replayPanel.getComponents()) {
					GridBagConstraints gbc = gbl.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						match = comp;
					}
				}
				if (match != null) {
					match.setBackground(Color.BLUE);
				}
			}
		}

	}

	public static void remWallReplay(int[] wallState) {
		Component match = null;
		int actualRow = wallState[4];
		int actualCol = wallState[5];
		if (wallState[6] == 1) {// isHorizontal
			for (int i = 0; i < 3; i++) {
				int x = (actualCol - 2) * 2 + i;
				int y = (actualRow - 2) * 2 + 1;

				// System.out.println("entering horizontal for loop");
				for (Component comp : replayPanel.getComponents()) {
					GridBagConstraints gbc = gbl.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						match = comp;
					}

					if (match != null) {
						match.setBackground(new Color(240, 240, 240));

					}

				}
			}
		} else {
			for (int i = 0; i < 3; i++) {
				// System.out.println("entering vertical for loop");
				int x = (actualCol - 2) * 2;
				int y = (actualRow - 2) * 2 - 1 + i;
				for (Component comp : replayPanel.getComponents()) {
					GridBagConstraints gbc = gbl.getConstraints(comp);

					if (gbc.gridx == x && gbc.gridy == y) {
						match = comp;
					}
				}
				if (match != null) {
					match.setBackground(new Color(240, 240, 240));
				}
			}
		}

	}

	public static boolean isInStates(int[] state, int[] lastState) {
		int count = 0;
		for (int j = 0; j < 7; j++) {
			if (state[j] != lastState[j]) {
				count++;
			}
		}
		if ((state[4] == 0 && count == 3) || count == 0)
			return true;
		else
			return false;
	}
	
	public static JButton getResignGameButton() {
		return resignGameButton;
	}
	public static JButton getReplayButton() {
		return replayButton;
	}
}