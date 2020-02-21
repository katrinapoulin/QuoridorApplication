package ca.mcgill.ecse223.quoridor.controller;


import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Destination;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.JumpMove;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.StepMove;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.properties.SaveProperties;
import ca.mcgill.ecse223.quoridor.view.BoardView;
import ca.mcgill.ecse223.quoridor.view.GameResult;
import ca.mcgill.ecse223.quoridor.view.PlayerMenu;
import ca.mcgill.ecse223.quoridor.properties.SaveProperties;
import javax.swing.text.Position;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;

public class QuoridorGameController {

	public static Quoridor quoridor = QuoridorApplication.getQuoridor();
	private static int thinkingTime;
	static Destination aDestination;
	public static boolean wasRunning;

	@SuppressWarnings("deprecation")
	static Time defaultTime = new Time(0, 10, 0);
	private static Timer timer;
	private static int timeAsInt;
	static String gameStatus = null;


	public static PawnBehavior pawnBehavior = new PawnBehavior(); 
	public static Integer currReplayIndex = null;

	private static boolean isResigned = false; 


	static List <Move> replayMoves;
	static List <Move> gameMoves;



	 /**
	  * @author BenjaminEmiliani
	  * @param aUsername : username to be created
	 * @return User : the user created
	  * @throws InvalidInputException
	  */
	public static User createUser(String aUsername) throws InvalidInputException{

		try {
			User user = quoridor.addUser(aUsername);
			return user;
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(aUsername + " already exists.");
		}
	}

	 /**
	  * @author BenjaminEmiliani
	  */
	public static void startNewGame() {

		initQuoridorAndBoard();
		createNewGame(createPlayers("user1", "user2"));
	}


	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * This method will initialize a new Game once fully implemented
	 */
	public static void initializeGame() throws InvalidInputException {

		try {
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);

	}
		catch(RuntimeException e) {
			throw new InvalidInputException("Could not initiate new game");
		}
	}


	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @param username : the username to set to white player
	 */
	public static void setWhitePlayerUsername(String username) throws InvalidInputException {


		User user1 = User.getWithName(username); //guaranteed to exist

		try {
			Player player1 = quoridor.getCurrentGame().getWhitePlayer();
			player1.setUser(user1);
			System.out.println("White player name: "  + username);

		}
		catch (RuntimeException e) {
			throw new InvalidInputException("Could not set white player's username");
		}
	}


	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @param username : username to set to black player
	 */
	public static void setBlackPlayerUsername(String username) throws InvalidInputException{


		User user2 = User.getWithName(username);
		try {
			Player player1 = quoridor.getCurrentGame().getBlackPlayer();
			player1.setUser(user2);
			System.out.println("Black player name: "  +username);

		}
		catch (RuntimeException e) {
			throw new InvalidInputException("Could not set black player's username");
		}
	}



	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @return boolean
	 *
	 */
	public static boolean totalThinkingTimeSet() throws InvalidInputException {

		try {
		 return (quoridor.getCurrentGame().getBlackPlayer().getRemainingTime() != null);
		}
		catch(RuntimeException e) {
			throw new InvalidInputException("Total thinking time was not set");
		}
	}


	/**
	 * @author BenjaminEmiliani
	 * @Feature StartNewGame
	 * This method will set game ready to start if white and black player have a username and a thinking time
	 */
	public static void readyToStart() {

		String a = quoridor.getCurrentGame().getBlackPlayer().getUser().getName();
		String b = quoridor.getCurrentGame().getBlackPlayer().getUser().getName();
		Time   c = quoridor.getCurrentGame().getBlackPlayer().getRemainingTime();

		if(a != null && b != null && c != null) {
			quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
		}
	}



	public static Time decreaseTime(Time time) {

		int seconds = time.getSeconds();
		seconds -= 1;
		time.setSeconds(seconds);


		return time;

	}

	/**
	 * @author BenjaminEmiliani
	 * @throws UnsupportedOperationException
	 * @Feature StartNewGame
	 * This method will start the clock of a running game once fully implemented
	 */
	public static void startClock(Time time)  {

		time = quoridor.getCurrentGame().getBlackPlayer().getRemainingTime();


		System.out.println(time.toString());
		quoridor.getCurrentGame().setGameStatus(GameStatus.Running);

	}



	/**
	 * @author BenjaminEmiliani
	 * @Feature ProvideSelectUserName
	 * @throws InvalidInputException
	 * @param username : existing username
	 * @param player : next player to set username
	 *
	 * This method will allow a player to select an existing username and
	 * assign it to that player for the current game
	 */
	public static void selectExistingUsername(Player player, String username) throws InvalidInputException {

		User userAdded = User.getWithName(username);
		player.setUser(userAdded);

		try {
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * @author BenjaminEmiliani
	 * @param username : existing username
	 *  @return User : user found
	 * This method will find a user inside the quoridor with its respective name
	 */
	public static User findExistingUsername(String username)  {

		User existingUsername = null;
		for (User User : QuoridorApplication.getQuoridor().getUsers()) {
			if (User.getName() == username) {
				existingUsername = User;
				break;
			}
		}
		return existingUsername;

	}





	/**
	 * @author BenjaminEmiliani
	 * @return List of user transfer objects used to display in the view.
	 */
	public static List<TOUser> getUsers() {
		ArrayList<TOUser> users = new ArrayList<TOUser>();
		for (User user : QuoridorApplication.getQuoridor().getUsers()) {
			TOUser toUser = new TOUser(user.getName());
			users.add(toUser);
		}
		return users;
	}

	private static void initQuoridorAndBoard() {

		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}


	private static ArrayList<Player> createPlayers(String userName1, String userName2) {

		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);


	Player player1 = new Player(defaultTime, user1, 9, Direction.Horizontal);
	Player player2 = new Player(defaultTime, user2, 1, Direction.Horizontal);

	Player[] players = { player1, player2 };

	// Create all walls. Walls with lower ID belong to player1,
	// while the second half belongs to player 2
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 10; j++) {
			Wall wall = new Wall(i * 10 + j +1 +differentIdGenerator()*100, players[i]);
		System.out.println(wall.toString());
		}
	}
	ArrayList<Player> playersList = new ArrayList<Player>();
	playersList.add(player1);
	playersList.add(player2);

	return playersList;
   }

	private static ArrayList<Player> createLoadedPlayers(String userName1, String userName2) {

		User user1 = quoridor.addUser(userName1 + differentIdGenerator());
		User user2 = quoridor.addUser(userName2 + differentIdGenerator());


	Player player1 = new Player(defaultTime, user1, 9, Direction.Horizontal);
	Player player2 = new Player(defaultTime, user2, 1, Direction.Horizontal);

	Player[] players = { player1, player2 };

	// Create all walls. Walls with lower ID belong to player1,
	// while the second half belongs to player 2
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 10; j++) {
			new Wall(i * 10 + j+ differentIdGenerator()*100, players[i]);
		}
	}
	ArrayList<Player> playersList = new ArrayList<Player>();
	playersList.add(player1);
	playersList.add(player2);

	return playersList;
   }

	
	
	private static ArrayList<Player> createLoadedPlayersVertical(String userName1, String userName2) {

	User user1 = quoridor.addUser(userName1 + differentIdGenerator());
	User user2 = quoridor.addUser(userName2 + differentIdGenerator());

		
	Player player1 = new Player(defaultTime, user1, 9, Direction.Vertical);
	Player player2 = new Player(defaultTime, user2, 1, Direction.Vertical);

	Player[] players = { player1, player2 };

	// Create all walls. Walls with lower ID belong to player1,
	// while the second half belongs to player 2
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 10; j++) {	
			new Wall(i * 10 + j+ differentIdGenerator()*100, players[i]);
		}
	}
	
	ArrayList<Player> playersList = new ArrayList<Player>();
	playersList.add(player1);
	playersList.add(player2);
	
	return playersList;
   }
	

	private static void createNewGame(ArrayList<Player> players) {

		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);

		game.setWhitePlayer(players.get(0));
		//players.get(0).setGameAsWhite(game);
		whiteTimerIsRunning = true;
		game.setBlackPlayer(players.get(1));
		//players.get(1).setGameAsBlack(game);

		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);
		System.out.println("Player to move: " +gamePosition.getPlayerToMove().getUser().getName());
		// Add the walls as in stock for the players
		for (Wall wall : players.get(0).getWalls()) {
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (Wall wall : players.get(1).getWalls()) {
			gamePosition.addBlackWallsInStock(wall);
		}
		game.setCurrentPosition(gamePosition);
		pawnBehavior.setCurrentGame(game);
	}


	/**
	 * @author Roey Wine
	 * This method initializes the board and adds all the tiles (9x9)
	 * @throws InvalidInputException
	 */
	public static void initializeBoard() throws InvalidInputException {

		try {
			if (quoridor.getBoard() == null) { // If the board doesn't already exist
				Board board = new Board(quoridor); // Creates new board
				// Creating tiles by rows, i.e., the column index changes with every tile
				// creation
				for (int i = 1; i <= 9; i++) { // for each row
					for (int j = 1; j <= 9; j++) { // for each column
						board.addTile(i,j); // adds tile for each position
					}
				}
			}
		} catch (RuntimeException e){

			throw new InvalidInputException(e.getMessage());
		}

	}



	/**
	 * @author Roey Wine
	 * This method sets the total thinking time based on the inputed minutes and seconds.
	 * It also checks to make sure that minutes and seconds are greater than zero.
	 * @param min Minutes of total thinking time
	 * @param sec Additional seconds of thinking time
	 * @throws InvalidInputException
	 */
	@SuppressWarnings("deprecation")
	public static void setTotalThinkingTime(int min, int sec) throws InvalidInputException {

		try {
			if (min == 0 && sec == 0) { // Checks that user inputs time larger than 1 second
				throw new InvalidInputException("The total thinking time must be greater than 0 minutes, 0 seconds");
			}

			// Set the remaining time for both players as the input
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(0,min, sec));
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(0,min, sec));

		} catch (RuntimeException e){

			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * @author Roey Wine
	 * This method opens a window that displays the final results if either player wins or draws
	 * @throws InvalidInputException
	 */
	public static void reportFinalResult() throws InvalidInputException {
		try {
			
			// This stuff should have worked better
			
//			checkGameResult();
//			if (!gameStatus.equals("Running")) {
//				new GameResult().setVisible(true);
//			}
		} catch (RuntimeException e){
			
			throw new InvalidInputException(e.getMessage());
		}
	}
	
	
	/**
	 * @author Roey Wine
	 * This method sets the opponent as the winner
	 * @throws InvalidInputException
	 */
	public static String ResignGame() throws InvalidInputException {
		try {
			// If it is white players turn (and he resigns) black wins
			if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.BlackWon);
				gameStatus = "BlackWon";
			}
			// If it is black players turn (and he resigns) white wins
			else {
				quoridor.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
				gameStatus = "WhiteWon";
			}
			isResigned = true;
			
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		return gameStatus;
	}
	
	/**	
	 * @author Benjamin Emiliani
	 * This method sets the remaining thinking time based on the inputed minutes and seconds.
	 * It also checks to make sure that minutes and seconds are greater than zero.
	 * @param min Minutes of total thinking time
	 * @param sec Additional seconds of thinking time
	 * @throws InvalidInputException
	 */
	@SuppressWarnings("deprecation")
	public static void setRemainingThinkingTime(String player ,int min, int sec)  {



			// Set the remaining time for both players as the input
			if(player.equals("white")) {
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(0,min, sec));
			}
			if(player.equals("black"))
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(0,min, sec));


	}

	/*
	 *
	 *
	 * BEGINNING OF KYLE CONTROLLER METHOD! DONT PUT ANYTHING IN BETWEEN PLEASE!!!!!!!
	 *
	 *
	 *
	 */

	/*
	 * Start of SwitchCurrentPlayer Feature Controller Methods
	 * Author: Kyle
	 */
	static boolean blackTimerIsRunning = false;
	static boolean whiteTimerIsRunning = false;
	static long totalThinkingTime = 30;

	public static boolean getBlackTimerIsRunning() {
		return blackTimerIsRunning;
	}

	public static boolean getWhiteTimerIsRunning() {
		return whiteTimerIsRunning;
	}

	public static void setBlackTimerIsRunning(boolean isRunning) {
		blackTimerIsRunning = isRunning;
	}

	public static void setWhiteTimerIsRunning(boolean isRunning) {
		whiteTimerIsRunning = isRunning;
	}

	public static void switchPlayer() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player black = quoridor.getCurrentGame().getBlackPlayer();
		Player white = quoridor.getCurrentGame().getWhitePlayer();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		if(currentPlayer.equals(black)) {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(white);
			quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().setNextPlayer(black);
			blackTimerIsRunning = false;
			whiteTimerIsRunning = true;
		}
		else if(currentPlayer.equals(white)){
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(black);
			quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().setNextPlayer(white);
			blackTimerIsRunning = true;
			whiteTimerIsRunning = false;
		}
	}

	
	public static void switchPlayer(Game game) {
		
		Player black = game.getBlackPlayer();
		Player white = game.getWhitePlayer();
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();
		if(currentPlayer.equals(black)) {
			game.getCurrentPosition().setPlayerToMove(white);
			//game.getCurrentPosition().getPlayerToMove().setNextPlayer(black);
			blackTimerIsRunning = false;
			whiteTimerIsRunning = true;
			System.out.println("In here!!!1" + game.getCurrentPosition().getPlayerToMove().hasGameAsBlack());
		}
		else if(currentPlayer.equals(white)){
			System.out.println("In here!!!2" + game.getCurrentPosition().getPlayerToMove().hasGameAsBlack());
			game.getCurrentPosition().setPlayerToMove(black);
			//game.getCurrentPosition().getPlayerToMove().setNextPlayer(white);
			blackTimerIsRunning = true;
			whiteTimerIsRunning = false;
		}
		//System.out.println("In here!!!" + game.getCurrentPosition().getPlayerToMove().hasGameAsBlack());
	}
	


	public static void startClock(String player) {

	}

	public static void stopClock(String player) {
	}

	public static boolean timerStatus(String playerToPlay) {
		boolean isRunning = true;
		if(playerToPlay.equals("black")) {
			isRunning = blackTimerIsRunning;
		}
		else if(playerToPlay.equals("white")){
			isRunning = whiteTimerIsRunning;
		}
		return isRunning;
	}

	//FOR TIMER DECREMENT
	public void refreshTimer() {
		Runnable timeDecrement = new Runnable() {
			public void run() {
				Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
				Time remainingTime = currentPlayer.getRemainingTime();
				long remTime = remainingTime.getTime();
				currentPlayer.setRemainingTime(new Time(remTime-1));
				if(remTime<=0) {
					switchPlayer();
				}
				//set display time
			}
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(timeDecrement, 0, 1, TimeUnit.SECONDS);
	}
	/*
	 * End of SwitchCurrentPlayer Feature Implementation
	 */
	
	public static Move getMoveEnd() {
		List<Move> moves = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();
		int size = moves.size();
		return moves.get(size-3);
	}
	
	public static void jumpToStart() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		List<Tile> allTiles = quoridor.getBoard().getTiles();
		Tile whiteBegining = quoridor.getBoard().getTile(1);
		Tile blackBegining = quoridor.getBoard().getTile(1);
		for(Tile tempTile: allTiles) {
			if (tempTile.getRow() == 9 && tempTile.getColumn() == 5) {
				whiteBegining = tempTile;
			}
			if(tempTile.getRow() == 1 && tempTile.getColumn() == 5) {
				blackBegining = tempTile;
			}
		}
		
		quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(whiteBegining);
		quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(blackBegining);
		
	
		List<Wall> whiteWallsOnBoard= quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
		List<Wall> blackWallsOnBoard= quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		
		
		
		for(Wall walls:whiteWallsOnBoard) {
			System.out.println("\n\n\n\n\n\n"+ walls.getId());
			quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(walls);
		}
		for(Wall walls:blackWallsOnBoard) {
			quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(walls);
		}
	}
	
	public static void jumpToEnd() {
		List<Move> moves = quoridor.getCurrentGame().getMoves();
		Move lastMoveBlack = moves.get(moves.size()-1);
		Tile lastTileBlack = lastMoveBlack.getTargetTile();
		quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(lastTileBlack);
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("hereeee");
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		Move lastMoveWhite = null;
		
		for(Move move:moves) {
			if(move.getPlayer().equals(quoridor.getCurrentGame().getWhitePlayer())) {
				lastMoveWhite = move;
				System.out.println("\n\n\n\n\n\n\n\n\n");
				if(move.getPlayer().equals(quoridor.getCurrentGame().getWhitePlayer())){	System.out.println("White: ");}
				if(move.getPlayer().equals(quoridor.getCurrentGame().getBlackPlayer())){	System.out.println("Black: ");}
				System.out.println("Row: "+move.getTargetTile().getRow());
				System.out.println("Col: "+move.getTargetTile().getColumn());
				System.out.println("\n\n\n\n\n\n\n\n\n");

			}
		}
		
		Tile lastTileWhite = lastMoveWhite.getTargetTile();
		quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(lastTileWhite);
		
		
		
		
	}
	

	/*
	 * Beginning of LoadPosition Feature Controller Methods
	 */


	/*
	 *
	 *
	 * END OF KYLE CONTROLLER METHOD! DONT PUT ANYTHING IN BETWEEN PLEASE!!!!!!!
	 *
	 *
	 *
	 */




	public static boolean grabWall() throws InvalidInputException {
		/*
		 * 1. Get a wall from the stock
		 * 2. Create WallMove with this wall
		 * 3. Set this wallMove as my wallMoveCandidate
		 * 4. Remove wall from stock
		 */

		boolean hasGrabbed = false;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		game.setMoveMode(MoveMode.WallMove);
		Board board = quoridor.getBoard();
		GamePosition position = game.getCurrentPosition();
		Player currentPlayer = position.getPlayerToMove();
		Wall wallGrabbed = null;
		WallMove wallMove;

		try {

		if (currentPlayer == game.getBlackPlayer()) {
			//1. Get a wall from the stock
			wallGrabbed = position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1);
			if(wallGrabbed==null) {
				wallGrabbed = position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-2);
			}

			//2. Create WallMove with this wall
			wallMove = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, currentPlayer, board.getTile(getTileIndex(8,9)) , game, Direction.Vertical, wallGrabbed);
			//3. Set this wallMove as my wallMoveCandidate
			game.setWallMoveCandidate(wallMove);
			//4. Remove wall from stock
			position.removeBlackWallsInStock(wallGrabbed);
			hasGrabbed = true;
		}
		else if (currentPlayer == game.getWhitePlayer()) {
			//1. Get a wall from the stock
			System.out.println("white walls" + position.numberOfWhiteWallsInStock());
			System.out.println(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().toString());
			wallGrabbed = position.getWhiteWallsInStock(position.numberOfWhiteWallsInStock()-1);
			if(wallGrabbed==null) {
				wallGrabbed = position.getWhiteWallsInStock(position.numberOfWhiteWallsInStock()-2);
			}
			//System.out.println(wallGrabbed);
			//2. Create WallMove with this wall
			wallMove = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, currentPlayer, board.getTile(getTileIndex(9,9)) , game, Direction.Vertical, wallGrabbed);
			//3. Set this wallMove as my wallMoveCandidate
			game.setWallMoveCandidate(wallMove);
			//4. Remove wall from stock
			position.removeWhiteWallsInStock(wallGrabbed);
			hasGrabbed = true;
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("You have no more walls in stock");
		}

		return hasGrabbed;
	}

	public static boolean flipWall(WallMove myWallMove) throws InvalidInputException {
		/*
		 * 1. Make sure surroundings are clear
		 * 2. Change the direction of the wallMove object
		 * 3. Check for errors
		 */

		boolean hasFlipped = false;

		if(myWallMove.getWallDirection().equals(Direction.Horizontal)) {
			myWallMove.setWallDirection(Direction.Vertical);
			hasFlipped = validatePosition(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition());
		}
		if(myWallMove.getWallDirection().equals(Direction.Vertical)) {
			myWallMove.setWallDirection(Direction.Horizontal);
			hasFlipped = validatePosition(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition());

	}
		return hasFlipped;
	}


	public static boolean moveWallInDirection(String side) throws InvalidInputException { //Katrina
		/*
		 * 1. Change targetTile to either the one left, right, up, down
		 * 2. While move is illegal, display message, but still move there
		 * 3. When move is legal, remove message
		 */

		boolean legalMove = true;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		Board board = quoridor.getBoard();
		WallMove myWallMove = game.getWallMoveCandidate();

		Tile oldTargetTile = myWallMove.getTargetTile();
		int oldRow = oldTargetTile.getRow();
		int oldCol = oldTargetTile.getColumn();
		Tile newTargetTile;

		if (side.equals("left")) {
			if(oldCol <= 2) {
				System.out.print("The wall is already at the left-most position");
				legalMove = false;
			}
			else {
				newTargetTile = board.getTile(getTileIndex(oldRow, oldCol-1)); //the tile on the right should be the one with index oldIndex+1
				myWallMove.setTargetTile(newTargetTile);
				legalMove = true;
			}
		}


		else if (side.equals("right")) {
			if(oldCol >= 9) {
				System.out.print("The wall is already at the right-most position");
				legalMove = false;
			}
			else {
				newTargetTile = board.getTile(getTileIndex(oldRow, oldCol+1)); //the tile on the right should be the one with index oldIndex+1
				myWallMove.setTargetTile(newTargetTile);
				legalMove = true;
			}
		}
		else if (side.equals("up")) {
			if(oldRow <= 2) {
				System.out.print("The wall is already at the up-most position");
				legalMove = false;
			}
			else {
				newTargetTile = board.getTile(getTileIndex(oldRow-1, oldCol)); //the tile on the right should be the one with index oldIndex+1
				myWallMove.setTargetTile(newTargetTile);
				legalMove = true;
			}
		}

		else if (side.equals("down")) {
			if(oldRow >= 9) {
				System.out.print("The wall is already at the down-most position");
				legalMove = false;
			}
			else {
				newTargetTile = board.getTile(getTileIndex(oldRow+1, oldCol)); //the tile on the right should be the one with index oldIndex+1
				myWallMove.setTargetTile(newTargetTile);
				legalMove = true;
			}
		}
		return legalMove;

	}

	public static int getTileIndex (int row, int col) {
		int index = ((row - 1) * 9)+ col - 1;
		return index;
	}

	public static boolean wallMoveIsValid(WallMove myMove) throws InvalidInputException {

		/*
		 * 1. Create hypothetical position in which the wall is placed
		 * 2. Validate this position
		 */
		boolean isValid = false;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition dummyPos = game.getCurrentPosition();
		if(dummyPos.getPlayerToMove().equals(game.getWhitePlayer())) {

			dummyPos.addWhiteWallsOnBoard(myMove.getWallPlaced());
		}
		else {
			dummyPos.addBlackWallsOnBoard(myMove.getWallPlaced());
		}

		if(game.getWhitePlayer() == null || game.getBlackPlayer() == null) {
			isValid = validatePosition(dummyPos);
		} else {
			isValid = validatePosition(dummyPos) && checkPath(game.getWhitePlayer(), game) && checkPath(game.getBlackPlayer(), game);
		}
		
		

		return isValid;
	}

	public static WallMove releaseWall() throws InvalidInputException { //Katrina
		boolean hasReleased = false;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition position = game.getCurrentPosition();
		Player player = game.getWallMoveCandidate().getPlayer();
		WallMove currentCan = game.getWallMoveCandidate();

//		if(game.numberOfPositions()==1) {
//			System.err.println("I AM 0 \n\n\n\n\n");
//			changePosition();
//		}


			if (player == game.getBlackPlayer()) {
				position.addBlackWallsOnBoardAt(game.getWallMoveCandidate().getWallPlaced(), position.numberOfBlackWallsOnBoard());
				game.setWallMoveCandidate(null);
				hasReleased = true;
		}
			else if (player == game.getWhitePlayer()) {
				position.addWhiteWallsOnBoardAt(game.getWallMoveCandidate().getWallPlaced(), position.numberOfWhiteWallsOnBoard());
				game.setWallMoveCandidate(null);
				hasReleased = true;

			}

		if (hasReleased == false) {
			return null;
		} else {
			game.addMove(currentCan);
		}
		if(hasReleased==true) {
			changePosition();
			switchPlayer();
			BoardView.cancelAddWallBtn.setEnabled(false);
		}
		return currentCan;
	}





	static boolean overWrite = false;

	public static boolean getOverWrite() {
		return overWrite;
	}

	public static void setOverWrite(Boolean input) {
		overWrite = input;
	}


	public static boolean fileExists(String fileName) throws InvalidInputException {

		File tempFile = new File(SaveProperties.filePath + fileName);

		boolean exists = tempFile.exists();

		return exists;
	}


	public static File savePosition(String fileName) throws InvalidInputException { //ASSUMES FILE MAY OR MAY NOT EXIST
		

		if(fileExists(fileName)) {
			System.out.println("WOW");
			return new File(SaveProperties.filePath + fileName); //return existing file
		}
		else {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition gamePosition = game.getCurrentPosition();
		Player firstPlayer = gamePosition.getPlayerToMove();
		//Player secondPlayer = firstPlayer.getNextPlayer();
		File file = new File(SaveProperties.filePath + fileName);
		String data = "";


		//FIRST PLAYER
		if(firstPlayer.hasGameAsWhite()) { //firstPlayer is white
			data = "W: ";
			Tile whiteTile = gamePosition.getWhitePosition().getTile();
			Character whiteColumn = (char) (whiteTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer whiteRow = whiteTile.getRow();
			data += whiteColumn.toString() + whiteRow.toString() + " "; //"W: e9 "
			List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard();
			

			for(Wall wall : whiteWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}
			data += "\n";

			//BLACK INFORMATION
			data += "B: ";
			Tile blackTile = gamePosition.getBlackPosition().getTile();
			Character blackColumn = (char) (blackTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer blackRow = blackTile.getRow();
			data += blackColumn.toString() + blackRow.toString() + " "; //"W: e9 "
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard();


			for(Wall wall : blackWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}

		}

		else {
			//BLACK INFORMATION
			data = "B: ";
			Tile blackTile = gamePosition.getBlackPosition().getTile();
			Character blackColumn = (char) (blackTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer blackRow = blackTile.getRow();
			data += blackColumn.toString() + blackRow.toString() + " "; //"W: e9 "
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard();


			for(Wall wall : blackWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";

			}
			data += "\n";

			//WHITE INFORMATION
			data += "W: ";
			Tile whiteTile = gamePosition.getWhitePosition().getTile();
			Character whiteColumn = (char) (whiteTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer whiteRow = whiteTile.getRow();
			data += whiteColumn.toString() + whiteRow.toString() + " "; //"W: e9 "
			List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard();


			for(Wall wall : whiteWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}
		}

		System.out.println(data);


	    Path path = Paths.get(file.getPath());
	    byte[] strToBytes = data.getBytes();

	    try {
			Files.write(path, strToBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return file;
		}
	}

	
		public static File forceSavePosition(String fileName) throws InvalidInputException { //ASSUMES FILE MAY OR MAY NOT EXIST
		
		
		

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition gamePosition = game.getCurrentPosition();
		Player firstPlayer = gamePosition.getPlayerToMove();
		//Player secondPlayer = firstPlayer.getNextPlayer();
		File file = new File(SaveProperties.filePath + fileName);
		String data = "";


		//FIRST PLAYER
		if(firstPlayer.hasGameAsWhite()) { //firstPlayer is white
			data = "W: ";
			Tile whiteTile = gamePosition.getWhitePosition().getTile();
			Character whiteColumn = (char) (whiteTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer whiteRow = whiteTile.getRow();
			data += whiteColumn.toString() + whiteRow.toString() + " "; //"W: e9 "
			List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard();


			for(Wall wall : whiteWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}
			data += "\n";

			//BLACK INFORMATION
			data += "B: ";
			Tile blackTile = gamePosition.getBlackPosition().getTile();
			Character blackColumn = (char) (blackTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer blackRow = blackTile.getRow();
			data += blackColumn.toString() + blackRow.toString() + " "; //"W: e9 "
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard();


			for(Wall wall : blackWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}

		}

		else {
			//BLACK INFORMATION
			data = "B: ";
			Tile blackTile = gamePosition.getBlackPosition().getTile();
			Character blackColumn = (char) (blackTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer blackRow = blackTile.getRow();
			data += blackColumn.toString() + blackRow.toString() + " "; //"W: e9 "
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard();


			for(Wall wall : blackWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {
					wallOrientation = 'v';
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";

			}
			data += "\n";

			//WHITE INFORMATION
			data += "W: ";
			Tile whiteTile = gamePosition.getWhitePosition().getTile();
			Character whiteColumn = (char) (whiteTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
			Integer whiteRow = whiteTile.getRow();
			data += whiteColumn.toString() + whiteRow.toString() + " "; //"W: e9 "
			List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard();


			for(Wall wall : whiteWalls) {
				Tile wallTile = wall.getMove().getTargetTile();
				Character wallColumn = (char) (wallTile.getColumn() + 'a' - 1); //1-9 ==> 'a'-'i'
				Integer wallRow = wallTile.getRow();
				Character wallOrientation;
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					wallOrientation = 'h';
				}
				else {

					wallOrientation = 'v'; 
				}
				data += wallColumn.toString() + wallRow.toString() + wallOrientation.toString() + " ";
			}
		}
		
		System.out.println(data);
		
		 
	    Path path = Paths.get(file.getPath());
	    byte[] strToBytes = data.getBytes();
	 
	    try {
			Files.write(path, strToBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return file;
		
	}
		
		
		
		public static File saveGame(String fileName) throws InvalidInputException { //ASSUMES FILE MAY OR MAY NOT EXIST
			 
			if(fileExists(fileName)) {
				System.out.println("WOW");
				return new File(SaveProperties.filePath + fileName); //return existing file
			} 
			else {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Game game = quoridor.getCurrentGame();
			GamePosition gamePosition = game.getCurrentPosition();
			Player firstPlayer = gamePosition.getPlayerToMove();
			//Player secondPlayer = firstPlayer.getNextPlayer();
			File file = new File(SaveProperties.filePath + fileName);
			String data = "";
			List<Move> moves = game.getMoves();
			Integer moveCounter = 1;
			for(int i = 0; i < moves.size(); i++) {
				if(i%2 == 0) {
					data += "\n"+moveCounter.toString()+". "; 			
					moveCounter++;
				}
				Character col = (char) (moves.get(i).getTargetTile().getColumn() + 'a' -1); 
				//System.out.println(moves.get(i).getTargetTile().getRow());
				Character row = (char) (moves.get(i).getTargetTile().getRow() + '0');
				data += col.toString()+row.toString();
				if(moves.get(i) instanceof WallMove) {
					Character wallOrientation;
					WallMove move = (WallMove) moves.get(i);
					if(move.getWallDirection() == Direction.Horizontal) {
						wallOrientation = 'h';
					}
					else {
						wallOrientation = 'v'; 
					}
					data += wallOrientation.toString();
				}
				data += " "; 
			}
			
			System.out.println(data);
			
			 
		    Path path = Paths.get(file.getPath());
		    byte[] strToBytes = data.getBytes();
		 
		    try {
				Files.write(path, strToBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return file;
			}
		}
		public static File forceSaveGame(String fileName) throws InvalidInputException { //ASSUMES FILE MAY OR MAY NOT EXIST
			 
			
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Game game = quoridor.getCurrentGame();
			GamePosition gamePosition = game.getCurrentPosition();
			Player firstPlayer = gamePosition.getPlayerToMove();
			//Player secondPlayer = firstPlayer.getNextPlayer();
			File file = new File(SaveProperties.filePath + fileName);
			String data = "";
			List<Move> moves = game.getMoves();
			Integer moveCounter = 1;
			for(int i = 0; i < moves.size(); i++) {
				if(i%2 == 0) {
					data += "\n"+moveCounter.toString()+". "; 			
					moveCounter++;
				}
				Character col = (char) (moves.get(i).getTargetTile().getColumn() + 'a' -1); 
				Character row = (char) (moves.get(i).getTargetTile().getRow() + '0');
				data += col.toString()+row.toString();
				if(moves.get(i) instanceof WallMove) {
					Character wallOrientation;
					WallMove move = (WallMove) moves.get(i);
					if(move.getWallDirection() == Direction.Horizontal) {
						wallOrientation = 'h';
					}
					else {
						wallOrientation = 'v'; 
					}
					data += wallOrientation.toString();
				}
				data += " "; 
			}
			
			System.out.println(data);
			
			 
		    Path path = Paths.get(file.getPath());
		    byte[] strToBytes = data.getBytes();
		 
		    try {
				Files.write(path, strToBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return file;
			
		}
		
		
		
	    public static boolean checkPath(Player player, Game game) {
	    	
	    	
	    	int totalEdges = 500;
	    	int wallNumber = game.getCurrentPosition().getBlackWallsOnBoard().size()+
	    			game.getCurrentPosition().getWhiteWallsOnBoard().size() +1;
	    	int remainingEdges = totalEdges-4*wallNumber;
	    	GamePosition gamePosition = game.getCurrentPosition();
	    	
	    	List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard(); //get all the white walls
			ArrayList<WallMove> walls = new ArrayList<WallMove>();
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard(); //add all the black walls

			for(Wall wall : whiteWalls) {
				walls.add(wall.getMove());
				//System.out.println(getTileIndex(wall.getMove().getTargetTile().getRow(),wall.getMove().getTargetTile().getColumn()));
			}
			for(Wall wall : blackWalls) {
				walls.add(wall.getMove());
			}
			if(quoridor.getCurrentGame().getWallMoveCandidate() != null) {
				walls.add(quoridor.getCurrentGame().getWallMoveCandidate());
				
			}
			
			
			
			
	    	Graph g =  new Graph(100, 300);
	    	ArrayList<Pair> tentativeList = new ArrayList<Pair>(); //will hold all the edges I am not adding
	    	
	    	for(WallMove wall : walls) {
	    		
	    		int index  = getTileIndex(wall.getTargetTile().getRow(),
	    				wall.getTargetTile().getColumn());
	    		
    			if(wall.getWallDirection() == Direction.Horizontal) {
    			     tentativeList.add(new Pair(index, index-9));
    			     tentativeList.add(new Pair(index-9, index));
    			     tentativeList.add(new Pair(index-1, index-10));
    			     tentativeList.add(new Pair(index-10, index-1));
    			} else {
    				 tentativeList.add(new Pair(index-1, index));
	   			     tentativeList.add(new Pair(index, index-1));
	   			     tentativeList.add(new Pair(index-9, index-10));
	   			     tentativeList.add(new Pair(index-10, index-9));
    			}
    		}
	    	
	    	System.out.println("end");
	    	int j = 0;
	    	for(int i = 0; i < quoridor.getBoard().getTiles().size(); i++) {
	    		
	    		boolean check  = false;
	    		//right to Left
	    		for(Pair pair: tentativeList) {
	    			if(pair.src == i && pair.dest == i+1 && i%9 != 8) {
	    				//System.out.println(pair.src + " " + pair.dest);
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i%9 !=8) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i+1; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		
	    		check = false;
	    		//Left to right
	    		for(Pair pair: tentativeList) {
	    			if(pair.src == i && pair.dest == i-1 && i%9 != 0 ) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i%9 != 0) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i-1; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		
	    		check = false; 
	    		for(Pair pair: tentativeList) {//up down
	    			if(pair.src == i && pair.dest == i+9 && i<72) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i < 72) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i+9; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		check = false;
	    		for(Pair pair: tentativeList) {//down up
	    			if(pair.src == i && pair.dest == i-9 && i>9) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		
	    		if(check == false && i > 9) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i-9; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		   		
	    	}
	    	
	    	System.out.println(j +  ": Number of edges");
	    	System.out.println(remainingEdges);
	    	//NOW THAT THE GRAPH HAS ALL THE EDGES
	    	PlayerPosition position;
	    	
	    	if(player.hasGameAsWhite()) {
	    		position = gamePosition.getWhitePosition();
	    	} else {
	    		position = gamePosition.getBlackPosition();
	    	}
	    	int index = getTileIndex(position.getTile().getRow(), position.getTile().getColumn());
	    	System.out.println(index);
	    	
	    	int[] bellmanFord = g.BellmanFord(g, index);
	    	
	    	int[] distancesToTarget = new int[9];
	    	for(int i = 0; i < 9; i++) {
	    		if(player.getDestination().getDirection() == Direction.Horizontal) {
	    			distancesToTarget[i] = bellmanFord[player.getDestination().getTargetNumber()-1+9*i];
	    			//System.out.println(player.getDestination().getTargetNumber()-1+9*i);
	    		} else {
	    			distancesToTarget[i] = bellmanFord[(player.getDestination().getTargetNumber()-1)*9+i];
	    			System.out.println((player.getDestination().getTargetNumber()-1)*9+i);
	    		}   		
	    	}
	    	boolean check;
	    	for(int i = 0; i < 9; i++) {
	    		System.out.println(i + "'s distance: " + distancesToTarget[i]);
	    	}
	    	for(int i = 0; i < 9; i++) {
	    		System.out.println(i + "'s distance: " + distancesToTarget[i]);
	    		if(!(distancesToTarget[i] == Integer.MAX_VALUE)) {
	    			System.out.println(distancesToTarget[i]);
	    			return true;
	    		}
	    	}
	    	return false;
	    	
	    }
	    
	    
	    public static int optimalPath(Player player, Game game) {
	    	
	    	
	    	int totalEdges = 500;
	    	int wallNumber = game.getCurrentPosition().getBlackWallsOnBoard().size()+
	    			game.getCurrentPosition().getWhiteWallsOnBoard().size() +1;
	    	int remainingEdges = totalEdges-4*wallNumber;
	    	GamePosition gamePosition = game.getCurrentPosition();
	    	
	    	List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard(); //get all the white walls
			ArrayList<WallMove> walls = new ArrayList<WallMove>();
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard(); //add all the black walls

			for(Wall wall : whiteWalls) {
				walls.add(wall.getMove());
				//System.out.println(getTileIndex(wall.getMove().getTargetTile().getRow(),wall.getMove().getTargetTile().getColumn()));
			}
			for(Wall wall : blackWalls) {
				walls.add(wall.getMove());
			}
			if(quoridor.getCurrentGame().getWallMoveCandidate() != null) {
				walls.add(quoridor.getCurrentGame().getWallMoveCandidate());
				
			}
			
			
			
			
	    	Graph g =  new Graph(100, 300);
	    	ArrayList<Pair> tentativeList = new ArrayList<Pair>(); //will hold all the edges I am not adding
	    	
	    	for(WallMove wall : walls) {
	    		
	    		int index  = getTileIndex(wall.getTargetTile().getRow(),
	    				wall.getTargetTile().getColumn());
	    		
    			if(wall.getWallDirection() == Direction.Horizontal) {
    			     tentativeList.add(new Pair(index, index-9));
    			     tentativeList.add(new Pair(index-9, index));
    			     tentativeList.add(new Pair(index-1, index-10));
    			     tentativeList.add(new Pair(index-10, index-1));
    			} else {
    				 tentativeList.add(new Pair(index-1, index));
	   			     tentativeList.add(new Pair(index, index-1));
	   			     tentativeList.add(new Pair(index-9, index-10));
	   			     tentativeList.add(new Pair(index-10, index-9));
    			}
    		}
	    	
	    	System.out.println("end");
	    	int j = 0;
	    	for(int i = 0; i < quoridor.getBoard().getTiles().size(); i++) {
	    		
	    		boolean check  = false;
	    		//right to Left
	    		for(Pair pair: tentativeList) {
	    			if(pair.src == i && pair.dest == i+1 && i%9 != 8) {
	    				//System.out.println(pair.src + " " + pair.dest);
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i%9 !=8) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i+1; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		
	    		check = false;
	    		//Left to right
	    		for(Pair pair: tentativeList) {
	    			if(pair.src == i && pair.dest == i-1 && i%9 != 0 ) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i%9 != 0) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i-1; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		
	    		check = false; 
	    		for(Pair pair: tentativeList) {//up down
	    			if(pair.src == i && pair.dest == i+9 && i<72) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		if(check == false && i < 72) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i+9; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		check = false;
	    		for(Pair pair: tentativeList) {//down up
	    			if(pair.src == i && pair.dest == i-9 && i>9) {
	    				check = true;
	    				break;
	    			}
	    		}
	    		
	    		if(check == false && i > 9) {
	    			g.edge[j].src = i; 
   			     	g.edge[j].dest = i-9; 
   			     	g.edge[j].weight = 1;
   			     	j++;
	    		}
	    		   		
	    	}
	    	
	    	System.out.println(j +  ": Number of edges");
	    	System.out.println(remainingEdges);
	    	//NOW THAT THE GRAPH HAS ALL THE EDGES
	    	PlayerPosition position;
	    	
	    	if(player.hasGameAsWhite()) {
	    		position = gamePosition.getWhitePosition();
	    	} else {
	    		position = gamePosition.getBlackPosition();
	    	}
	    	int index = getTileIndex(position.getTile().getRow(), position.getTile().getColumn());
	    	System.out.println(index);
	    	int[] bellmanFordEast = new int[g.V], bellmanFordWest = new int[g.V], bellmanFordNorth  = new int[g.V], bellmanFordSouth = new int[g.V];
	    	Arrays.fill(bellmanFordEast, Integer.MAX_VALUE);
	    	Arrays.fill(bellmanFordWest, Integer.MAX_VALUE);
	    	Arrays.fill(bellmanFordNorth, Integer.MAX_VALUE);
	    	Arrays.fill(bellmanFordSouth, Integer.MAX_VALUE);
	    	if(!(index%9 == 8) && pawnBehavior.isLegalStep(MoveDirection.East)) {
	    		bellmanFordEast = g.BellmanFord(g, index+1);
	    	}
	    	if(!(index%9 == 0) && pawnBehavior.isLegalStep(MoveDirection.West)) {
	    		bellmanFordWest = g.BellmanFord(g, index-1);
	    	}
	    	if(!(index < 9) && pawnBehavior.isLegalStep(MoveDirection.North)) {
	    		bellmanFordNorth = g.BellmanFord(g, index-9);
	    	}
	    	if(!(index > 72) && pawnBehavior.isLegalStep(MoveDirection.South)) {
	    		bellmanFordSouth = g.BellmanFord(g, index+9);
	    	}
	    	
	    	int[][] distancesToTarget = new int[9][4];//east west north south
	    	for(int i = 0; i < 9; i++) {
	    		if(player.getDestination().getDirection() == Direction.Horizontal) {
	    			distancesToTarget[i][0] = bellmanFordEast[player.getDestination().getTargetNumber()-1+9*i];
	    			distancesToTarget[i][1] = bellmanFordWest[player.getDestination().getTargetNumber()-1+9*i];
	    			distancesToTarget[i][2] = bellmanFordNorth[player.getDestination().getTargetNumber()-1+9*i];
	    			distancesToTarget[i][3] = bellmanFordSouth[player.getDestination().getTargetNumber()-1+9*i];
	    			//System.out.println(player.getDestination().getTargetNumber()-1+9*i);
	    		} else {
	    			distancesToTarget[i][0] = bellmanFordEast[(player.getDestination().getTargetNumber()-1)*9+i];
	    			distancesToTarget[i][1] = bellmanFordWest[(player.getDestination().getTargetNumber()-1)*9+i];
	    			distancesToTarget[i][2] = bellmanFordNorth[(player.getDestination().getTargetNumber()-1)*9+i];
	    			distancesToTarget[i][3] = bellmanFordSouth[(player.getDestination().getTargetNumber()-1)*9+i];
	    		}   		
	    	}
	    	
	    	int[] minima  = new int[4];
	    	for(int i = 0; i <4; i++) {
	    		minima[i] = distancesToTarget[0][i];
	    	}
	    	for(int i = 1; i < 9; i++) {
	    		for(j = 0; j < 4; j++) {
	    			if(distancesToTarget[i][j] < minima[j]) {
	    				minima[j] = distancesToTarget[i][j];
	    			}
	    		}
	    	}
	    	int minimum = minima[0];
	    	int direction = 0;
	    	
	    	for(int i = 1; i <4; i++) {
	    		System.out.println(minima[i]);
	    		if(minima[i] < minimum) {
	    			minimum = minima[i];
	    			direction = i;
	    		}
	    	}
	    	
	    	if(minimum == Integer.MAX_VALUE) {
	    		return -1;
	    	} 
	    	
	    	return direction;
	    	
	    }
		

	
		
		public static boolean loadGame(String fileName) throws InvalidInputException {
			
			int aDifferentId = differentIdGenerator();
			
			
			String EoL = System.getProperty("line.separator");
			
			List<String> lines;
			try {
				lines = Files.readAllLines(Paths.get(fileName),
				        Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to parse\n");
				return false;
			}

			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
			    sb.append(line).append(EoL);
			}
			String content = sb.toString();

			StringTokenizer st = new StringTokenizer(content);
			
			
			
			ArrayList<Player> players = createLoadedPlayersVertical("BillyFromLoad", "BobFromLoad");
			
			Quoridor quoridorBro = new Quoridor();
			Game game = new Game(GameStatus.Running,MoveMode.PlayerMove, quoridorBro);//quoridor cannot have multiple games
																				//there could be a game in progress when loading
			
			Board board = quoridor.getBoard();
			ArrayList<String> whiteWalls = new ArrayList<String>();
			ArrayList<String> blackWalls = new ArrayList<String>();
			game.setWhitePlayer(players.get(0));
			game.setBlackPlayer(players.get(1));
			//Player playerToMove = players.get(0);
			int counter = 0;
			
			ArrayList<String> tokens = new ArrayList<String>();
			int j = 0;
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken());
				System.out.println(tokens.get(j) + " " + tokens.get(j).length());
				j++;
			}
			
			Tile whiteTile = board.getTile(getTileIndex(1,5)); 
					
			Tile blackTile = board.getTile(getTileIndex(9,5));
			PlayerPosition aNewWhitePosition = new PlayerPosition(players.get(0), whiteTile);
			PlayerPosition aNewBlackPosition = new PlayerPosition(players.get(1), blackTile);
			
			GamePosition gamePosition = new GamePosition(aDifferentId, aNewWhitePosition, aNewBlackPosition, game.getWhitePlayer() , game);//white starts
			
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getWhitePlayer().getWalls()) {
					gamePosition.addWhiteWallsInStock(wall);
				}
			}
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getBlackPlayer().getWalls()) {
					gamePosition.addBlackWallsInStock(wall);
				}
			}
			
			boolean isValid = validatePosition(gamePosition);
			pawnBehavior.setCurrentGame(game);
			game.setCurrentPosition(gamePosition);
			//game.set
			//Initialize pawn location to E1, E9
			
			int roundCounter = 1;
			int moveCounter = 1;
			for(int i = 0; i < tokens.size(); i++) {
				if(tokens.get(i).contentEquals(roundCounter + ".")) {
					roundCounter++;
				}
				else if(tokens.get(i).length()==2) {//pawnMove
					int col = tokens.get(i).charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
					int row = tokens.get(i).charAt(1) - '0'; //'1' - '0' = 1
					int rowBefore, colBefore;
					 if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						 rowBefore = game.getCurrentPosition().getWhitePosition().getTile().getRow();
						 colBefore = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
						
					 } else {
						 rowBefore = game.getCurrentPosition().getBlackPosition().getTile().getRow();
						 colBefore = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
					 }
					 
					MoveDirection MoveDirection1 = null; 
					MoveDirection MoveDirection2 = null;
					
					
					if(col - colBefore == 1) {
						MoveDirection1 = MoveDirection.East;
					} else if (col - colBefore == -1){
						MoveDirection1 = MoveDirection.West;
					}
					if(row - rowBefore == -1) {
						if(MoveDirection1 == null) {
							MoveDirection1 = MoveDirection.North; 
						} else {
							MoveDirection2 = MoveDirection.North;
						}
					} else if (row - rowBefore == 1){
						if(MoveDirection1 == null) {
							MoveDirection1 = MoveDirection.South; 
						} else {
							MoveDirection2 = MoveDirection.South;
						}
						
					}
					
					boolean done = false;
					//covers both step pawn and jumping in directionse
					if(MoveDirection1 == null && MoveDirection2 == null) {//separation by 2 or nothing 
						System.out.println("Black " + game.getCurrentPosition().getPlayerToMove().hasGameAsBlack());
						System.out.println(row + " :row, " + col + " :col.");
						System.out.println(rowBefore + " :rowBefore, " + colBefore + " :colBefore.");
						if(row-rowBefore == -2) {
							MoveDirection1 = MoveDirection.North;
						} else if (row-rowBefore == 2) {
							MoveDirection1 = MoveDirection.South; 
						} else if(col-colBefore == 2) {
							MoveDirection1 = MoveDirection.East; 
						} else if(col-colBefore == -2) {
							MoveDirection1 = MoveDirection.West; 
						} else {
							System.out.println("Illegal jump pawn move.");
							return false;
						}
						if(pawnBehavior.isLegalJump(MoveDirection1)) {
							jumpPawn(game, MoveDirection1);
							System.out.println("jumping");
							done = true;
						} else {
							System.out.println("Illegal pawn move.");
							return false;
						}
					}
					
					
					if(MoveDirection2 == null) {
						if(pawnBehavior.isLegalStep(MoveDirection1) && !done) {
							System.out.println(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite());
							stepPawn(game, MoveDirection1);
						} else {
							if(!done) {
								System.out.println(MoveDirection1);
								System.out.println(row + " :row, " + col + " :col.");
								System.out.println(rowBefore + " :rowBefore, " + colBefore + " :colBefore.");
								System.out.println("Illegal pawn step.");
								return false;
							}
						}
					} else {
						if(pawnBehavior.isLegalJump(MoveDirection1, MoveDirection2) || 
								pawnBehavior.isLegalJump(MoveDirection2, MoveDirection1)) {
							jumpPawn(game, MoveDirection1,MoveDirection2);
							//jumpPawn(game, MoveDirection2,MoveDirection1);
						} else {
							System.out.println("Illegal jump step");
							return false;
						}
					}
					//switchPlayer(game);
					
				}
				else if(tokens.get(i).length() == 3) {
					//it's a wall
					int col = tokens.get(i).charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
					int row = tokens.get(i).charAt(1) - '0'; //'1' - '0' = 1
					Direction direction;
					if (tokens.get(i).charAt(2) == 'h') {
						direction = Direction.Horizontal;
					} else {
						direction = Direction.Vertical;
					}
					Wall wallToMove;
					if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						wallToMove = gamePosition.getWhiteWallsInStock(gamePosition.numberOfWhiteWallsInStock() - 1);
					} else {
						wallToMove = gamePosition.getBlackWallsInStock(gamePosition.numberOfWhiteWallsInStock() - 1);
					}
					
					Tile tile = null;
					try {
						tile = new Tile(row, col, quoridor.getBoard());
						} catch (RuntimeException e) {
							System.out.println("User position invalid\n");
							System.out.println(tokens.get(i) + "\n");
							System.out.println("Row = " + row + "\nCol = " + col);
							e.printStackTrace();
							return false;
						} 
					WallMove wallMove = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, 
							quoridorBro.getCurrentGame().getCurrentPosition().getPlayerToMove(), 
							tile, game, direction, wallToMove);
					game.addMove(wallMove);
					
					if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						gamePosition.removeWhiteWallsInStock(wallToMove);
						gamePosition.addWhiteWallsOnBoard(wallToMove);
					} else {
						gamePosition.removeBlackWallsInStock(wallToMove);
						gamePosition.addBlackWallsOnBoard(wallToMove);
					}
					
					try {
						isValid = validatePosition(gamePosition);
					} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
						System.out.println("Validate failed due to exception\n");
						e.printStackTrace();
						return false;
					}
					if(!isValid) {
						System.out.println("Position Invalid\n");
						return false;
					}
					switchPlayer(game);
				} 
				else {
					System.out.println("Invalid file format");
					return false;
				}
				//check if position is valid
				if(!validatePosition(gamePosition)) {
					System.out.println("Position Invalid\n");
					return false;
				}
				
				//check game status
				if(!checkGameResult(game).contentEquals("Running")) {
					//goToReplayMode();
					quoridor.setCurrentGame(game);
					game.setCurrentPosition(gamePosition);
					return true;
				}
				
			}
			
			//I have added everything and its all valid: I can begin to load the game
			if(!validatePosition(gamePosition)) {
				System.out.println("Position Invalid\n");
				return false;
			}
			
			quoridor.setCurrentGame(game);
			game.setCurrentPosition(gamePosition);
			gamePosition.setPlayerToMove(gamePosition.getPlayerToMove());
			return true;
		}
		
		
		public static boolean loadGameHorizontal(String fileName) throws InvalidInputException {
			
			int aDifferentId = differentIdGenerator();
			
			
			String EoL = System.getProperty("line.separator");
			
			List<String> lines;
			try {
				lines = Files.readAllLines(Paths.get(SaveProperties.filePath + fileName),
				        Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to parse\n");
				return false;
			}

			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
			    sb.append(line).append(EoL);
			}
			String content = sb.toString();

			StringTokenizer st = new StringTokenizer(content);
			
			
			
			ArrayList<Player> players = createLoadedPlayers("BillyFromLoad", "BobFromLoad");
			
			Quoridor quoridorBro = new Quoridor();
			Game game = new Game(GameStatus.Running,MoveMode.PlayerMove, quoridorBro);//quoridor cannot have multiple games
																				//there could be a game in progress when loading
			if(quoridor.getBoard() == null) {
			
				initQuoridorAndBoard();
			}
			
			Board board = quoridor.getBoard();
			ArrayList<String> whiteWalls = new ArrayList<String>();
			ArrayList<String> blackWalls = new ArrayList<String>();
			game.setWhitePlayer(players.get(0));
			game.setBlackPlayer(players.get(1));
			//Player playerToMove = players.get(0);
			int counter = 0;
			
			ArrayList<String> tokens = new ArrayList<String>();
			int j = 0;
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken());
				System.out.println(tokens.get(j) + " " + tokens.get(j).length());
				j++;
			}
			
			Tile whiteTile = board.getTile(getTileIndex(5,1));
			Tile blackTile = board.getTile(getTileIndex(5,9));
			PlayerPosition aNewWhitePosition = new PlayerPosition(players.get(0), whiteTile);
			PlayerPosition aNewBlackPosition = new PlayerPosition(players.get(1), blackTile);
			
			GamePosition gamePosition = new GamePosition(differentIdGenerator(), aNewWhitePosition, aNewBlackPosition, game.getWhitePlayer() , game);//white starts
			
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getWhitePlayer().getWalls()) {
					gamePosition.addWhiteWallsInStock(wall);
				}
			}
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getBlackPlayer().getWalls()) {
					gamePosition.addBlackWallsInStock(wall);
				}
			}
			
			boolean isValid = validatePosition(gamePosition);
			pawnBehavior.setCurrentGame(game);
			game.setCurrentPosition(gamePosition);
			//game.set
			//Initialize pawn location to E1, E9
			
			int roundCounter = 1;
			int moveCounter = 1;
			for(int i = 0; i < tokens.size(); i++) {
				if(tokens.get(i).contentEquals(roundCounter + ".")) {
					roundCounter++;
				}
				else if(tokens.get(i).length()==2) {//pawnMove
					int col = tokens.get(i).charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
					int row = tokens.get(i).charAt(1) - '0'; //'1' - '0' = 1
					int rowBefore, colBefore;
					 if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						 rowBefore = game.getCurrentPosition().getWhitePosition().getTile().getRow();
						 colBefore = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
						
					 } else {
						 rowBefore = game.getCurrentPosition().getBlackPosition().getTile().getRow();
						 colBefore = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
					 }
					 
					MoveDirection MoveDirection1 = null; 
					MoveDirection MoveDirection2 = null;
					
					
					if(col - colBefore == 1) {
						MoveDirection1 = MoveDirection.East;
					} else if (col - colBefore == -1){
						MoveDirection1 = MoveDirection.West;
					}
					if(row - rowBefore == -1) {
						if(MoveDirection1 == null) {
							MoveDirection1 = MoveDirection.North; 
						} else {
							MoveDirection2 = MoveDirection.North;
						}
					} else if (row - rowBefore == 1){
						if(MoveDirection1 == null) {
							MoveDirection1 = MoveDirection.South; 
						} else {
							MoveDirection2 = MoveDirection.South;
						}
						
					}
					
					boolean done =false;
					//covers both step pawn and jumping in directionse
					if(MoveDirection1 == null && MoveDirection2 == null) {//separation by 2 or nothing 
						System.out.println("Black " + game.getCurrentPosition().getPlayerToMove().hasGameAsBlack());
						System.out.println(row + " :row, " + col + " :col.");
						System.out.println(rowBefore + " :rowBefore, " + colBefore + " :colBefore.");
						if(row-rowBefore == -2) {
							MoveDirection1 = MoveDirection.North;
						} else if (row-rowBefore == 2) {
							MoveDirection1 = MoveDirection.South; 
						} else if(col-colBefore == 2) {
							MoveDirection1 = MoveDirection.East; 
						} else if(col-colBefore == -2) {
							MoveDirection1 = MoveDirection.West; 
						} else {
							System.out.println("Illegal jump pawn move.");
							return false;
						}
						if(pawnBehavior.isLegalJump(MoveDirection1)) {
							jumpPawn(game, MoveDirection1);
							System.out.println("Jumping");
							done = true;
						} else {
							System.out.println("Illegal pawn move.");
							return false;
						}
					}
					
					
					if(MoveDirection2 == null) {
						if(pawnBehavior.isLegalStep(MoveDirection1) && !done) {//
							System.out.println(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite());
							stepPawn(game, MoveDirection1);
						} else {//neither were legal
							if(!done) {
								System.out.println(MoveDirection1);
								System.out.println(row + " :row, " + col + " :col.");
								System.out.println(rowBefore + " :rowBefore, " + colBefore + " :colBefore.");
								System.out.println("Illegal pawn step.");
								return false;
							}
						}
					} else {
						if(pawnBehavior.isLegalJump(MoveDirection1, MoveDirection2) || 
								pawnBehavior.isLegalJump(MoveDirection2, MoveDirection1)) {
							jumpPawn(game, MoveDirection1,MoveDirection2);
							//jumpPawn(game, MoveDirection2,MoveDirection1);
						} else {
							System.out.println("Illegal jump step");
							return false;
						}
					}
					//switchPlayer(game);
					
				}
				else if(tokens.get(i).length() == 3) {
					//it's a wall
					int col = tokens.get(i).charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
					int row = tokens.get(i).charAt(1) - '0'; //'1' - '0' = 1
					Direction direction;
					if (tokens.get(i).charAt(2) == 'h') {
						direction = Direction.Horizontal;
					} else {
						direction = Direction.Vertical;
					}
					Wall wallToMove;
					if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						wallToMove = gamePosition.getWhiteWallsInStock(gamePosition.numberOfWhiteWallsInStock() - 1);
					} else {
						wallToMove = gamePosition.getBlackWallsInStock(gamePosition.numberOfWhiteWallsInStock() - 1);
					}
					
					Tile tile = null;
					try {
						tile = new Tile(row, col, quoridor.getBoard());
						} catch (RuntimeException e) {
							System.out.println("User position invalid\n");
							System.out.println(tokens.get(i) + "\n");
							System.out.println("Row = " + row + "\nCol = " + col);
							e.printStackTrace();
							return false;
						} 
					WallMove wallMove = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, 
							quoridorBro.getCurrentGame().getCurrentPosition().getPlayerToMove(), 
							tile, game, direction, wallToMove);
					game.addMove(wallMove);
					
					if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
						gamePosition.removeWhiteWallsInStock(wallToMove);
						gamePosition.addWhiteWallsOnBoard(wallToMove);
					} else {
						gamePosition.removeBlackWallsInStock(wallToMove);
						gamePosition.addBlackWallsOnBoard(wallToMove);
					}
					
					try {
						isValid = validatePosition(gamePosition);
					} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
						System.out.println("Validate failed due to exception\n");
						e.printStackTrace();
						return false;
					}
					if(!isValid) {
						System.out.println("Position Invalid\n");
						return false;
					}
					switchPlayer(game);
				} 
				else {
					System.out.println("Invalid file format");
					return false;
				}
				//check if position is valid
				if(!validatePosition(gamePosition)) {
					System.out.println("Position Invalid\n");
					return false;
				}
				
				//check game status
				if(!checkGameResult(game).contentEquals("Running")) {
					//goToReplayMode();
					quoridor.setCurrentGame(game);
					game.setCurrentPosition(gamePosition);
					return true;
				}
				
			}
			
			//I have added everything and its all valid: I can begin to load the game
			if(!validatePosition(gamePosition)) {
				System.out.println("Position Invalid\n");
				return false;
			}
			
			quoridor.setCurrentGame(game);
			game.setCurrentPosition(gamePosition);
			gamePosition.setPlayerToMove(gamePosition.getPlayerToMove());
			return true;
		}
		
		
		

		public static void notifyNoWalls() {

		}

		public static void notifyIllegalMove() {

		}

		public static void notifyMoveInvalid() {

		}

		private static int id = 0;

		public static int differentIdGenerator() {
			id += 1;
			return id;
		}

		public static ArrayList<String> getFileNames() {
			ArrayList<String> fileNames  = new ArrayList<String>();


			File[] files = new File(SaveProperties.filePath).listFiles();
			//If this pathname does not denote a directory, then listFiles() returns null.

			for (File file : files) {
			    if (file.isFile()) {
			        fileNames.add(file.getName());
			    }
			}
			return fileNames;
		}


		public static boolean loadPosition(String fileName) throws InvalidInputException {

			int aDifferentId = differentIdGenerator();


			String EoL = System.getProperty("line.separator");

			List<String> lines;
			try {
				lines = Files.readAllLines(Paths.get(fileName),
				        Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to parse\n");
				return false;
			}

			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
			    sb.append(line).append(EoL);
			}
			String content = sb.toString();

			StringTokenizer st = new StringTokenizer(content);

			Player playerToMove = null;
			PlayerPosition whitePosition = null;
			PlayerPosition blackPosition = null;


			ArrayList<Player> players = createLoadedPlayers("WhiteFromLoad", "BlackFromLoad");

			Quoridor quoridorBro = new Quoridor();
			Game game = new Game(GameStatus.Running,MoveMode.PlayerMove, quoridorBro);//quoridor cannot have multiple games
																				//there could be a game in progress when loading

			ArrayList<String> whiteWalls = new ArrayList<String>();
			ArrayList<String> blackWalls = new ArrayList<String>();
			game.setWhitePlayer(players.get(0));
			game.setBlackPlayer(players.get(1));
			int counter = 0;

			ArrayList<String> tokens = new ArrayList<String>();
			int j = 0;
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken());
				System.out.println(tokens.get(j) + " " + tokens.get(j).length());
				j++;
			}
			for(int i = 0; i < tokens.size(); i++) {
				if(i==0) {//first token
					if(tokens.get(i).equals("W:")) { //player is white
						counter++;
						playerToMove = game.getWhitePlayer();
						i++;//want to check pawn
						String tileString = tokens.get(i); //pawn position
						int row = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int col = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridor.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						whitePosition = new PlayerPosition(game.getWhitePlayer(), tile);
						i++;
						while(i < tokens.size()  && tokens.get(i).length() == 3) {
							whiteWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					} else { //player is black
						counter+=2;
						playerToMove = game.getBlackPlayer();
						i++;
						String tileString = tokens.get(i);
						int row = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int col = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridor.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						blackPosition = new PlayerPosition(game.getBlackPlayer(), tile);
						i++;
						while(i < tokens.size()  && tokens.get(i).length() == 3) {
							blackWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					}

				} else { //"W" or "B" second
					if(counter == 1) {//next player black
						String tileString = tokens.get(i);
						int row = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int col = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridor.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						blackPosition = new PlayerPosition(game.getBlackPlayer(), tile);
						i++;
						while(i < tokens.size() && tokens.get(i).length() == 3) {
							blackWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					} else { //next player white
						String tileString = tokens.get(i);
						if(tileString.length()!=2) {
							throw new InvalidInputException("Failed to parse" + tileString);
				
						}
						int row = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int col = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridor.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						whitePosition = new PlayerPosition(game.getWhitePlayer(), tile);
						i++;
						while(i < tokens.size() && tokens.get(i).length() == 3) {
							whiteWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					}
				}


			}



			GamePosition gamePosition = new GamePosition(aDifferentId, whitePosition, blackPosition, playerToMove, game);

			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getWhitePlayer().getWalls()) {
					gamePosition.addWhiteWallsInStock(wall);
				}
			}
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getBlackPlayer().getWalls()) {
					gamePosition.addBlackWallsInStock(wall);
				}
			}

			boolean isValid = validatePosition(gamePosition);
			if(!isValid) {
				System.out.println("Pawn overlap");
				return false;
			}

			int moveCounter = 0;
			for(String wall: whiteWalls) {
				int col = wall.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
				int row = wall.charAt(1) - '0'; //'1' - '0' = 1
				Direction direction;
				if (wall.charAt(2) == 'h') {
					direction = Direction.Horizontal;
				} else {
					direction = Direction.Vertical;
				}
				Wall wallToMove = gamePosition.getWhiteWallsInStock(moveCounter);
				moveCounter++;
				//move number doesnt really matter
				Tile tile = null;
				try {
					tile = new Tile(row, col, quoridor.getBoard());
					} catch (RuntimeException e) {
						System.out.println("User position invalid\n");
						System.out.println(wall + "\n");
						System.out.println("Row = " + row + "\nCol = " + col);
						e.printStackTrace();
						return false;
					}
				WallMove wallMove = new WallMove(moveCounter, (moveCounter)/2,
						quoridorBro.getCurrentGame().getWhitePlayer(),
						tile, game, direction, wallToMove);
				game.addMove(wallMove);
				gamePosition.removeWhiteWallsInStock(wallToMove);
				gamePosition.addWhiteWallsOnBoard(wallToMove); //updates board
				try {
					isValid = validatePosition(gamePosition);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					System.out.println("Validate failed due to exception\n");
					e.printStackTrace();
					return false;
				}
				if(!isValid) {
					System.out.println("Position Invalid\n");
					return false;
				}
			}

			moveCounter = 0;
			for(String wall: blackWalls) {
				int col = wall.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
				int row = wall.charAt(1) - '0'; //'1' - '0' = 1
				Direction direction;
				if (wall.charAt(2) == 'h') {
					direction = Direction.Horizontal;
				} else {
					direction = Direction.Vertical;
				}
				Wall wallToMove = gamePosition.getBlackWallsInStock(moveCounter);
				moveCounter++;
				//move number doesnt really matter
				Tile tile = null;
				try {
					tile = new Tile(row, col, quoridor.getBoard());
					} catch (RuntimeException e) {
						System.out.println("User position invalid\n");
						System.out.println(wall + "\n");
						System.out.println("Row = " + row + "\nCol = " + col);
						e.printStackTrace();
						return false;
					}
				WallMove wallMove = new WallMove(moveCounter, (moveCounter)/2,
						quoridorBro.getCurrentGame().getBlackPlayer(),
						tile, game, direction, wallToMove);
				game.addMove(wallMove);
				gamePosition.removeBlackWallsInStock(wallToMove);
				gamePosition.addBlackWallsOnBoard(wallToMove); //updates board
				try {

					isValid = validatePosition(gamePosition);
				} catch (InvalidInputException e) {
					System.out.println("Validate failed due to exception\n");
					e.printStackTrace();
					return false;
				}

				if(!isValid) {
					System.out.println("Position Invalid\n");
					return false;
				}

			}

			//I have added everything and its all valid: I can begin to load the game
			if(!validatePosition(gamePosition)) {
				System.out.println("Position Invalid\n");
				return false;
			}
			quoridor.setCurrentGame(game);
			game.setCurrentPosition(gamePosition);
			gamePosition.setPlayerToMove(playerToMove);
			return true;
		}

		public static boolean loadPositionFromPath(String fileName) throws InvalidInputException {

			int aDifferentId = differentIdGenerator();


			String EoL = System.getProperty("line.separator");

			List<String> lines;
			try {
				lines = Files.readAllLines(Paths.get(SaveProperties.filePath + fileName),
				        Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to parse\n");
				return false;
			}

			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
			    sb.append(line).append(EoL);
			}
			String content = sb.toString();

			StringTokenizer st = new StringTokenizer(content);

			Player playerToMove = null;
			PlayerPosition whitePosition = null;
			PlayerPosition blackPosition = null;


			ArrayList<Player> players = createLoadedPlayers("WhiteFromLoad", "BlackFromLoad");

			Quoridor quoridorBro = new Quoridor();
			Game game = new Game(GameStatus.Running,MoveMode.PlayerMove, quoridorBro);//quoridor cannot have multiple games
																				//there could be a game in progress when loading
			Board board = new Board(quoridorBro);

			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}

			ArrayList<String> whiteWalls = new ArrayList<String>();
			ArrayList<String> blackWalls = new ArrayList<String>();
			game.setWhitePlayer(players.get(0));
			game.setBlackPlayer(players.get(1));
			int counter = 0;

			ArrayList<String> tokens = new ArrayList<String>();
			int j = 0;
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken());
				System.out.println(tokens.get(j) + " " + tokens.get(j).length());
				j++;
			}
			for(int i = 0; i < tokens.size(); i++) {
				if(i==0) {//first token
					if(tokens.get(i).equals("W:")) { //player is white
						counter++;
						playerToMove = game.getWhitePlayer();
						i++;//want to check pawn
						String tileString = tokens.get(i); //pawn position
						int col = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int row = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridorBro.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						whitePosition = new PlayerPosition(game.getWhitePlayer(), tile);
						i++;
						while(i < tokens.size()  && tokens.get(i).length() == 3) {
							whiteWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					} else { //player is black
						counter+=2;
						playerToMove = game.getBlackPlayer();
						i++;
						String tileString = tokens.get(i);
						int col = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int row = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridorBro.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						blackPosition = new PlayerPosition(game.getBlackPlayer(), tile);
						i++;
						while(i < tokens.size()  && tokens.get(i).length() == 3) {
							blackWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					}

				} else { //"W" or "B" second
					if(counter == 1) {//next player black
						String tileString = tokens.get(i);
						int col = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int row = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridorBro.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						blackPosition = new PlayerPosition(game.getBlackPlayer(), tile);
						i++;
						while(i < tokens.size() && tokens.get(i).length() == 3) {
							blackWalls.add(tokens.get(i));
							System.out.println("Adding wall: " + tokens.get(i));
							i++;
						}
					} else { //next player white
						String tileString = tokens.get(i);
						if(tileString.length()!=2) {
							throw new InvalidInputException("Failed to parse" + tileString);
						}
						int col = tileString.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
						int row = tileString.charAt(1) - '0'; //'1' - '0' = 1
						Tile tile = null;
						try {
							tile = new Tile(row, col, quoridorBro.getBoard());
							} catch (RuntimeException e) {
								System.out.println("User position invalid\n");
								System.out.println(tileString + "\n");
								System.out.println("Row = " + row + "\nCol = " + col);
								e.printStackTrace();
								return false;
							}
						whitePosition = new PlayerPosition(game.getWhitePlayer(), tile);
						i++;
						while(i < tokens.size() && tokens.get(i).length() == 3) {
							whiteWalls.add(tokens.get(i));
							System.out.println("Adding wall to queue: " + tokens.get(i));
							i++;
						}
					}
				}


			}



			GamePosition gamePosition = new GamePosition(aDifferentId, whitePosition, blackPosition, playerToMove, game);

			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getWhitePlayer().getWalls()) {
					gamePosition.addWhiteWallsInStock(wall);
				}
			}
			for (int z = 0; z < 10; z++) {
				for(Wall wall : game.getBlackPlayer().getWalls()) {
					gamePosition.addBlackWallsInStock(wall);
				}
			}

			boolean isValid = validatePosition(gamePosition);
			if(!isValid) {
				System.out.println("Pawn overlap");
				return false;
			}

			int moveCounter = 0;
			for(String wall: whiteWalls) {
				int col = wall.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
				int row = wall.charAt(1) - '0'; //'1' - '0' = 1
				Direction direction;
				if (wall.charAt(2) == 'h') {
					direction = Direction.Horizontal;
				} else {
					direction = Direction.Vertical;
				}
				Wall wallToMove = gamePosition.getWhiteWallsInStock(moveCounter);
				moveCounter++;
				//move number doesnt really matter
				Tile tile = null;
				try {
					tile = new Tile(row, col, quoridorBro.getBoard());
					} catch (RuntimeException e) {
						System.out.println("User position invalid\n");
						System.out.println(wall + "\n");
						System.out.println("Row = " + row + "\nCol = " + col);
						e.printStackTrace();
						return false;
					}
				WallMove wallMove = new WallMove(moveCounter, (moveCounter)/2,
						quoridorBro.getCurrentGame().getWhitePlayer(),
						tile, game, direction, wallToMove);
				game.addMove(wallMove);
				gamePosition.removeWhiteWallsInStock(wallToMove);
				gamePosition.addWhiteWallsOnBoard(wallToMove); //updates board
				try {
					isValid = validatePosition(gamePosition);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					System.out.println("Validate failed due to exception\n");
					e.printStackTrace();
					return false;
				}
				if(!isValid) {
					System.out.println("Position Invalid\n");
					return false;
				}
			}

			moveCounter = 0;
			for(String wall: blackWalls) {
				int col = wall.charAt(0) - 'a' + 1;//'a' - 'a' + 1 = 1
				int row = wall.charAt(1) - '0'; //'1' - '0' = 1
				Direction direction;
				if (wall.charAt(2) == 'h') {
					direction = Direction.Horizontal;
				} else {
					direction = Direction.Vertical;
				}
				Wall wallToMove = gamePosition.getBlackWallsInStock(moveCounter);
				moveCounter++;
				//move number doesnt really matter
				Tile tile = null;
				try {
					tile = new Tile(row, col, quoridorBro.getBoard());
					} catch (RuntimeException e) {
						System.out.println("User position invalid\n");
						System.out.println(wall + "\n");
						System.out.println("Row = " + row + "\nCol = " + col);
						e.printStackTrace();
						return false;
					}
				WallMove wallMove = new WallMove(moveCounter, (moveCounter)/2,
						quoridorBro.getCurrentGame().getBlackPlayer(),
						tile, game, direction, wallToMove);
				game.addMove(wallMove);
				gamePosition.removeBlackWallsInStock(wallToMove);
				gamePosition.addBlackWallsOnBoard(wallToMove); //updates board
				try {

					isValid = validatePosition(gamePosition);
				} catch (InvalidInputException e) {
					System.out.println("Validate failed due to exception\n");
					e.printStackTrace();
					return false;
				}

				if(!isValid) {
					System.out.println("Position Invalid\n");
					return false;
				}

			}

			//I have added everything and its all valid: I can begin to load the game
			if(!validatePosition(gamePosition)) {
				System.out.println("Position Invalid\n");
				return false;
			}

			quoridor.delete();
			quoridor = QuoridorApplication.getQuoridor();
			gamePosition.setPlayerToMove(playerToMove);
			QuoridorApplication.setQuoridor(quoridorBro);
			game.setCurrentPosition(gamePosition);
//			System.out.println(gamePosition.getWhiteWallsOnBoard().toString());
//			System.out.println(gamePosition.getBlackWallsOnBoard().toString());
			quoridorBro.setCurrentGame(game);
			quoridor = quoridorBro;

			return true;
		}



		public static boolean validatePosition(GamePosition gamePosition) throws InvalidInputException {

			//first check pawns
			Tile whiteTile = gamePosition.getWhitePosition().getTile();
			Tile blackTile = gamePosition.getBlackPosition().getTile();

			if(whiteTile.getRow() > 9 || whiteTile.getColumn() < 1 || whiteTile.getRow() < 1 || whiteTile.getColumn() > 9) {
				System.out.println("White pawn position out of bounds");
				return false;
			}
			if(blackTile.getRow() > 9 || blackTile.getColumn() < 1 || blackTile.getRow() < 1 || blackTile.getColumn() > 9) {
				System.out.println("Black pawn position out of bounds");
				return false;
			}
			if(whiteTile.equals(blackTile) || (whiteTile.getRow() == blackTile.getRow()
					&& whiteTile.getColumn() == blackTile.getColumn())) { //check overlap
				System.out.println("White and Black pawn cannot occupy the same tile");
				return false;
			}

			List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard(); //get all the white walls
			ArrayList<Wall> walls = new ArrayList<Wall>();
			List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard(); //add all the black walls

			for(Wall wall : whiteWalls) {
				walls.add(wall);
			}
			for(Wall wall : blackWalls) {
				walls.add(wall);
			}

			for(Wall wall : walls) {//check if out of bounds
				Tile wallStartingPosition = wall.getMove().getTargetTile();
				Direction wallDirection = wall.getMove().getWallDirection();
				if(wallStartingPosition.getColumn() < 1 || wallStartingPosition.getColumn() > 9 || wallStartingPosition.getRow() < 1
						|| wallStartingPosition.getRow() > 9) {
					System.out.println("Wall is out of bounds");
					return false;
				}

			}




			for(Wall wall1 : walls) {
				Tile wall1StartingPosition = wall1.getMove().getTargetTile();
				Direction wall1Direction = wall1.getMove().getWallDirection();
				int[] wall1Rows = new int[3];
				int[] wall1Columns = new int[3];


				if(wall1Direction == Direction.Horizontal) {//horizontal: i.e. multiple columns
					for(int i = 0; i < wall1Columns.length; i++) {
						wall1Columns[i] = wall1StartingPosition.getColumn() + i ;
						wall1Rows[i]= wall1StartingPosition.getRow() +1;

					}

				} else { //vertical

					for(int i = 0; i < wall1Columns.length; i++) {

						//System.out.println("Vertical wall" + wall1Start)
						wall1Columns[i] = wall1StartingPosition.getColumn()+1;
						wall1Rows[i]= wall1StartingPosition.getRow() +i ;
						//System.out.println(wall1Rows[i]);
					}


				}
				for(Wall wall2: walls) {
					if(!(wall1.equals(wall2))) { //so long as the walls are not equal


						Tile wall2StartingPosition = wall2.getMove().getTargetTile();
						Direction wall2Direction = wall2.getMove().getWallDirection();

						int[] wall2Rows = new int[3];
						int[] wall2Columns = new int[3];

						if(wall2Direction == Direction.Horizontal) {//horizontal: i.e. multiple columns
							for(int i = 0; i < wall2Columns.length; i++) {
								wall2Columns[i] = wall2StartingPosition.getColumn() + i ;
								wall2Rows[i]= wall2StartingPosition.getRow() +1;
							}

						} else {

							for(int i = 0; i < wall2Columns.length; i++) {
								wall2Columns[i] = wall2StartingPosition.getColumn()+1;
								wall2Rows[i]= wall2StartingPosition.getRow() +i;
								//System.out.println(wall2Rows[i]);
							}

						}
						//GamePosition gamePosition2 = gamePosition

						int counter = 0;
						if(wall2Direction.equals(wall1Direction)) {
							for(int i = 0; i < 3; i++) {
								for(int j = 0; j < 3; j++) {
									//if directions are the same, two walls may only share one position
									//same orientation
									if(wall1Rows[i] == wall2Rows[j] && wall1Columns[i] == wall2Columns[j]) {
										counter++;
										if(counter > 1) {
											System.out.println("Two walls of same direction overlap.");
											return false; //they can only share one coordinate if they have the same orientation
										}
									}
								}
							}
						}


						else {
							if(wall1StartingPosition.getRow() == wall2StartingPosition.getRow()  &&
									wall1StartingPosition.getColumn() == wall2StartingPosition.getColumn()) {//middle intersects
								System.out.println("A vertical and a horizontal wall overlap.");
								return false;
							}
						}
					}
				}
			}



			return true;

		}


	public static TOPlayer getWhitePlayer() {

		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();

		//System.out.println(player.getRemainingTime());
		TOPlayer toPlayer = new TOPlayer(player.getUser().getName(),player.getRemainingTime(),
				position.numberOfWhiteWallsInStock(),
				position.getWhitePosition().getTile().getRow(),
				position.getWhitePosition().getTile().getColumn());

		return toPlayer;

	}

	public static Player getPlayerToMove() {
		return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
	}

	public static TOPlayer getBlackPlayer() {

		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		//System.out.println(player.getRemainingTime());
		TOPlayer toPlayer = new TOPlayer(player.getUser().getName(),player.getRemainingTime(),
				position.numberOfBlackWallsInStock(),
				position.getBlackPosition().getTile().getRow(),
				position.getBlackPosition().getTile().getColumn());

		return toPlayer;

	}




	public static List<TOWall> getWalls() {

		ArrayList<TOWall> toWalls = new ArrayList<TOWall>();
		GamePosition gamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();

		List<Wall> whiteWalls = gamePosition.getWhiteWallsOnBoard(); //get all the white walls
		ArrayList<Wall> walls = new ArrayList<Wall>();
		List<Wall> blackWalls = gamePosition.getBlackWallsOnBoard(); //add all the black walls

		for(Wall wall : whiteWalls) {
			walls.add(wall);
		}
		for(Wall wall : blackWalls) {
			walls.add(wall);
		}

		for(Wall wall: walls) {
			TOWall.Direction direction;
			if(wall.getMove().getWallDirection() == Direction.Horizontal) {
				direction = TOWall.Direction.Horizontal;
			} else {
				direction = TOWall.Direction.Vertical;
			}
			TOWall toWall = new TOWall(direction, wall.getId(), wall.getOwner().getUser().getName(),
					wall.getMove().getTargetTile().getRow(),
					wall.getMove().getTargetTile().getColumn());
			toWalls.add(toWall);
		}

		return toWalls;
	}

	public static TOWall getCandidate() {
		if(!(quoridor.getCurrentGame().hasWallMoveCandidate())) return null;
		WallMove move = quoridor.getCurrentGame().getWallMoveCandidate();
		int id = move.getWallPlaced().getId();
		String user = move.getPlayer().getUser().toString();
		int row = move.getTargetTile().getRow();
		int col = move.getTargetTile().getColumn();
		TOWall.Direction defaultDirection = TOWall.Direction.Horizontal;
		return new TOWall(defaultDirection, id, user, row, col);
	}

	public static void createCandidate() {
		try {
			grabWall();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void dropCandidate() {
		try {
			releaseWall();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void rotateCandidate() {
		WallMove candidate = quoridor.getCurrentGame().getWallMoveCandidate();
		try {
			flipWall(candidate);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void cancelCandidate() {
		Player player = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		Wall cancelledWall = quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced();
		if(player.equals(quoridor.getCurrentGame().getWhitePlayer())) {//current player is white
			quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(cancelledWall);
			//System.out.println("\n\n\n\n\n\n\n\n White Wallssssss" + quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock());
		}
		else {//current player is black
			quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(cancelledWall);
			//System.out.println(quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock());

		}
		//quoridor.getCurrentGame().setWallMoveCandidate(null);
		quoridor.getCurrentGame().getWallMoveCandidate().delete();
		BoardView.wallMoveCandidate=null;


	}

	public static void updateCandidate(TOWall viewCandidate, int row, int col, boolean horizontal) {
		try{
			WallMove controllerCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
		controllerCandidate.setTargetTile(quoridor.getBoard().getTile(getTileIndex(row, col)));
		if(horizontal==true) {
			controllerCandidate.setWallDirection(Direction.Horizontal);
		}
		else {
			controllerCandidate.setWallDirection(Direction.Vertical);
		}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	public static boolean wallMoveValid() {
		WallMove wallCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
		boolean isValid = false;
		try {
			isValid = wallMoveIsValid(wallCandidate);

		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		return isValid;
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
<<<<<<< HEAD
	 * This method grabs the pawn for this pawn behavior 
=======
	 * This method grabs the pawn this pawn behavior
>>>>>>> master
	 * Used for testing
	 */
	public static void grabPawn() {
		pawnBehavior.clickPawn();
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method grabs the pawn for the inputed pawn behavior
	 * @param PawnBehavior pb: The pawn behavior
	 */
	public static void grabPawn(PawnBehavior pb) {
		pb.clickPawn();
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method moves the pawn one tile over in an inputed direction
	 * @param MoveDirection dir: The direction the pawn is moving
	 */
	public static void stepPawn(MoveDirection dir) {

		//Move the pawn
		//Update the game position
		//Update the move list
		//create a new step move
		Game game = quoridor.getCurrentGame();
		Player player = game.getCurrentPosition().getPlayerToMove();
		int row, col;

		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
			col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
			col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
		}



		if(dir == MoveDirection.East) {
			col++;
		}
		if(dir == MoveDirection.West) {
			col--;
		}
		if(dir == MoveDirection.North) {
			row--;
		}
		if(dir == MoveDirection.South) {
			row++;
		}

		int index = getTileIndex(row, col);
		Tile targetTile = quoridor.getBoard().getTile(index);
		StepMove stepMove = new StepMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
		PlayerPosition position = new PlayerPosition(player, targetTile);
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			game.getCurrentPosition().setWhitePosition(position);
		} else {
			game.getCurrentPosition().setBlackPosition(position);
		}
		//positions have been updated
		game.addMove(stepMove);
		changePosition();
		switchPlayer();
	}
	
	
	public static void stepPawn(Game game, MoveDirection dir) {
		
		//Move the pawn
		//Update the game position
		//Update the move list
		//create a new step move 
		//Game game = quoridor.getCurrentGame();
		Player player = game.getCurrentPosition().getPlayerToMove();
		int row, col;
		
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
			col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
			col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
		}
		
		
		
		if(dir == MoveDirection.East) {
			col++;
		}
		if(dir == MoveDirection.West) {
			col--;
		}
		if(dir == MoveDirection.North) {
			row--;
		}
		if(dir == MoveDirection.South) {
			row++;
		}
		
		int index = getTileIndex(row, col);
		Tile targetTile = quoridor.getBoard().getTile(index);
		StepMove stepMove = new StepMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
		PlayerPosition position = new PlayerPosition(player, targetTile);
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			game.getCurrentPosition().setWhitePosition(position);
		} else {
			game.getCurrentPosition().setBlackPosition(position);
		}
		//positions have been updated
		game.addMove(stepMove);
		switchPlayer(game);
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method moves the pawn two tile over (jumps over opponent) in an inputed direction
	 * @param MoveDirection dir: The direction the pawn is moving
	 */
	public static void jumpPawn(MoveDirection dir) {
		//Move the pawn
		//Update the game position
		//Update the move list
		//create a new step move
		Game game = quoridor.getCurrentGame();
		Player player = game.getCurrentPosition().getPlayerToMove();
		int row, col;

		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
			col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
			col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
		}



		if(dir == MoveDirection.East) {
			col+=2;
		}
		if(dir == MoveDirection.West) {
			col-=2;
		}
		if(dir == MoveDirection.North) {
			row-=2;
		}
		if(dir == MoveDirection.South) {
			row+=2;
		}

		int index = getTileIndex(row, col);
		Tile targetTile = quoridor.getBoard().getTile(index);
		JumpMove jumpMove = new JumpMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
		PlayerPosition position = new PlayerPosition(player, targetTile);
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			game.getCurrentPosition().setWhitePosition(position);
		} else {
			game.getCurrentPosition().setBlackPosition(position);
		}
		//positions have been updated
		game.addMove(jumpMove);
		changePosition();
		switchPlayer();
	}
	
	
	public static void jumpPawn(Game game, MoveDirection dir) {
		//Move the pawn
		//Update the game position
		//Update the move list
		//create a new step move 
		Player player = game.getCurrentPosition().getPlayerToMove();
		int row, col;
		
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
			col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
			col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
		}
		
		
		
		if(dir == MoveDirection.East) {
			col+=2;
		}
		if(dir == MoveDirection.West) {
			col-=2;
		}
		if(dir == MoveDirection.North) {
			row-=2;
		}
		if(dir == MoveDirection.South) {
			row+=2;
		}
		
		int index = getTileIndex(row, col);
		Tile targetTile = quoridor.getBoard().getTile(index);
		JumpMove jumpMove = new JumpMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
		PlayerPosition position = new PlayerPosition(player, targetTile);
		if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			game.getCurrentPosition().setWhitePosition(position);
		} else {
			game.getCurrentPosition().setBlackPosition(position);
		}
		//positions have been updated
		game.addMove(jumpMove);
		switchPlayer(game);
	}
	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method moves the pawn one tile over in two inputted directions
	 * @param MoveDirection dir: The first direction the pawn is moving
	 * @param MoveDirection dirs: The second direction the pawn is moving
	 */
	public static void jumpPawn(MoveDirection dir, MoveDirection dir2) {
		//Move the pawn
				//Update the game position
				//Update the move list
				//create a new step move
				Game game = quoridor.getCurrentGame();
				Player player = game.getCurrentPosition().getPlayerToMove();
				int row, col;

				if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
					row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
					col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
				} else {
					row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
					col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
				}



				if(dir == MoveDirection.East) {
					col++;
				}
				if(dir == MoveDirection.West) {
					col--;
				}
				if(dir == MoveDirection.North) {
					row--;
				}
				if(dir == MoveDirection.South) {
					row++;
				}

				if(dir2 == MoveDirection.East) {
					col++;
				}
				if(dir2 == MoveDirection.West) {
					col--;
				}
				if(dir2 == MoveDirection.North) {
					row--;
				}
				if(dir2 == MoveDirection.South) {
					row++;
				}

				int index = getTileIndex(row, col);
				Tile targetTile = quoridor.getBoard().getTile(index);
				JumpMove jumpMove = new JumpMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
				PlayerPosition position = new PlayerPosition(player, targetTile);
				if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
					game.getCurrentPosition().setWhitePosition(position);
				} else {
					game.getCurrentPosition().setBlackPosition(position);
				}
				//positions have been updated
				game.addMove(jumpMove);
				changePosition();
				switchPlayer(game);
	}
	public static void jumpPawn(Game game, MoveDirection dir, MoveDirection dir2) {
		//Move the pawn
				//Update the game position
				//Update the move list
				//create a new step move 
				Player player = game.getCurrentPosition().getPlayerToMove();
				int row, col;
				
				if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
					row = game.getCurrentPosition().getWhitePosition().getTile().getRow();
					col = game.getCurrentPosition().getWhitePosition().getTile().getColumn();
				} else {
					row = game.getCurrentPosition().getBlackPosition().getTile().getRow();
					col = game.getCurrentPosition().getBlackPosition().getTile().getColumn();
				}
				
				
				
				if(dir == MoveDirection.East) {
					col++;
				}
				if(dir == MoveDirection.West) {
					col--;
				}
				if(dir == MoveDirection.North) {
					row--;
				}
				if(dir == MoveDirection.South) {
					row++;
				}
				
				if(dir2 == MoveDirection.East) {
					col++;
				}
				if(dir2 == MoveDirection.West) {
					col--;
				}
				if(dir2 == MoveDirection.North) {
					row--;
				}
				if(dir2 == MoveDirection.South) {
					row++;
				}
				
				int index = getTileIndex(row, col);
				Tile targetTile = quoridor.getBoard().getTile(index);
				JumpMove jumpMove = new JumpMove(game.numberOfMoves(), game.numberOfMoves()%2, player, targetTile, game);
				PlayerPosition position = new PlayerPosition(player, targetTile);
				if(game.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
					game.getCurrentPosition().setWhitePosition(position);
				} else {
					game.getCurrentPosition().setBlackPosition(position);
				}
				//positions have been updated
				game.addMove(jumpMove);
				switchPlayer(game);
	}

	public static void dropPawn() {
		pawnBehavior.cancelPawnMove();
	}



	public static ArrayList<ActionPoint> getPossibleMoves(int xPos, int yPos) {

		ArrayList<ActionPoint> points = new ArrayList<ActionPoint>();

		if(pawnBehavior.isLegalStep(MoveDirection.North)){

			Point point= new Point(xPos, yPos - 2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("up");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalStep(MoveDirection.East)){

			Point point = new Point(xPos + 2, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("right");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalStep(MoveDirection.South)){

			Point point = new Point(xPos, yPos + 2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("down");
			points.add(pointN);
		}
		if(pawnBehavior.isLegalStep(MoveDirection.West)){

			Point point = new Point(xPos - 2, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("left");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.North)) {
			Point point = new Point (xPos, yPos - 4);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("up");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.South)) {
			Point point = new Point (xPos, yPos + 4);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("down");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West)) {
			Point point = new Point (xPos-4, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("left");
			points.add(pointN);
		}
		if(pawnBehavior.isLegalJump(MoveDirection.East)) {
			Point point = new Point (xPos+4, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("right");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.East, MoveDirection.North) ||
				pawnBehavior.isLegalJump(MoveDirection.North, MoveDirection.East)) {
			Point point = new Point (xPos+2, yPos-2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("upright");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West, MoveDirection.North) ||
				pawnBehavior.isLegalJump(MoveDirection.North, MoveDirection.West)) {
			Point point = new Point (xPos-2, yPos-2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("upleft");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West, MoveDirection.South) ||
				pawnBehavior.isLegalJump(MoveDirection.South, MoveDirection.West)) {
			Point point = new Point (xPos-2, yPos+2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("downleft");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.East, MoveDirection.South) ||
				pawnBehavior.isLegalJump(MoveDirection.South, MoveDirection.East)) {
			Point point = new Point (xPos+2, yPos+2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("downright");
			points.add(pointN);
		}


		return points;
	}

public static ArrayList<ActionPoint> getPossibleMoves(PawnBehavior pawnBehavior, int xPos, int yPos) {

		ArrayList<ActionPoint> points = new ArrayList<ActionPoint>();

		if(pawnBehavior.isLegalStep(MoveDirection.North)){

			Point point= new Point(xPos, yPos - 2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("up");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalStep(MoveDirection.East)){

			Point point = new Point(xPos + 2, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("right");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalStep(MoveDirection.South)){

			Point point = new Point(xPos, yPos + 2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("down");
			points.add(pointN);
		}
		if(pawnBehavior.isLegalStep(MoveDirection.West)){

			Point point = new Point(xPos - 2, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("left");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.North)) {
			Point point = new Point (xPos, yPos - 4);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("up");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.South)) {
			Point point = new Point (xPos, yPos + 4);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("down");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West)) {
			Point point = new Point (xPos-4, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("left");
			points.add(pointN);
		}
		if(pawnBehavior.isLegalJump(MoveDirection.East)) {
			Point point = new Point (xPos+4, yPos);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("right");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.East, MoveDirection.North) ||
				pawnBehavior.isLegalJump(MoveDirection.North, MoveDirection.East)) {
			Point point = new Point (xPos+2, yPos-2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("upright");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West, MoveDirection.North) ||
				pawnBehavior.isLegalJump(MoveDirection.North, MoveDirection.West)) {
			Point point = new Point (xPos-2, yPos-2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("upleft");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.West, MoveDirection.South) ||
				pawnBehavior.isLegalJump(MoveDirection.South, MoveDirection.West)) {
			Point point = new Point (xPos-2, yPos+2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("downleft");
			points.add(pointN);
		}

		if(pawnBehavior.isLegalJump(MoveDirection.East, MoveDirection.South) ||
				pawnBehavior.isLegalJump(MoveDirection.South, MoveDirection.East)) {
			Point point = new Point (xPos+2, yPos+2);
			ActionPoint pointN = new ActionPoint();
			pointN.setPoint(point);
			pointN.setAction("downright");
			points.add(pointN);
		}


		return points;
	}
	/**
	 * @author Benjamin Emiliani
	 * @return String : gameStatus, this string will be returned to when called by the view acting as a
	 * transfer object for the enumeration GameStatus of the model
	 * This method evaluates certain parameters of the current game in order to assess the current game status
	 * after a move.
	 */
	@SuppressWarnings("deprecation")
	public static String checkGameResult() {



		//String gameStatus = null;
		
		Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();
		Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
		
		Tile tile1 = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
		Tile tile2 = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
		
		
		if(blackPlayer.getRemainingTime() != null && 
				whitePlayer.getRemainingTime() != null && tile1.getColumn() != 9 && tile2.getColumn() != 1 && !isResigned) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.Running);
			
				gameStatus = "Running";
			}	
		
			List<Move> moves = quoridor.getCurrentGame().getMoves();
			if(moves.size() > 8 && moves.size() < 11) {	
			
			if(moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-5).getTargetTile()) && 
					moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-9).getTargetTile()) &&
					moves.get(moves.size() - 3).getTargetTile().equals(moves.get(moves.size() - 7).getTargetTile())) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.Draw);
				gameStatus = "Drawn";
			}
			
		}
			if(moves.size() >= 11) {
				if(moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-5).getTargetTile()) && 
						moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-9).getTargetTile()) &&
						moves.get(moves.size() - 3).getTargetTile().equals(moves.get(moves.size() - 7).getTargetTile()) &&
						moves.get(moves.size() - 3).getTargetTile().equals(moves.get(moves.size() - 11).getTargetTile())) {
					quoridor.getCurrentGame().setGameStatus(GameStatus.Draw);
					gameStatus = "Drawn";
				}
				
		
	}		
		if((blackPlayer.getRemainingTime().getMinutes() == 0 
				&& blackPlayer.getRemainingTime().getSeconds() == 1) || (tile1.getColumn() == 9)) {
			quoridor.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
			gameStatus = "WhiteWon";
		}
		if((whitePlayer.getRemainingTime().getMinutes() == 0 
				&& whitePlayer.getRemainingTime().getSeconds() == 1) || tile2.getColumn() == 1){
			quoridor.getCurrentGame().setGameStatus(GameStatus.BlackWon);
			gameStatus = "BlackWon";

				}
		//System.out.println(gameStatus);
		return gameStatus;
	}
	
	public static String checkGameResult(Game game) {
		
		String gameStatus = null;
		
		Player blackPlayer = game.getBlackPlayer();
		Player whitePlayer = game.getWhitePlayer();
		
		Tile tile1 = game.getCurrentPosition().getWhitePosition().getTile();
		Tile tile2 = game.getCurrentPosition().getBlackPosition().getTile();
		
		
		if(blackPlayer.getRemainingTime() != null && 
				whitePlayer.getRemainingTime() != null && tile1.getColumn() != 9 && tile2.getColumn() != 1) {
				game.setGameStatus(GameStatus.Running);
			
				gameStatus = "Running";
			}	
		
			List<Move> moves = game.getMoves();
			if(moves.size() > 8) {	
			
			if((moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-5).getTargetTile())) && 
					(moves.get(moves.size()-1).getTargetTile().equals(moves.get(moves.size()-9).getTargetTile()))) {
				game.setGameStatus(GameStatus.Draw);
				gameStatus = "Drawn";
			}
		}
			
	// direction = whitePlayer.getDestination().getDirection();
		
		if((blackPlayer.getRemainingTime().getMinutes() == 0 
				&& blackPlayer.getRemainingTime().getSeconds() == 0) || 
				((tile1.getColumn() == whitePlayer.getDestination().getTargetNumber()) 
						&& whitePlayer.getDestination().getDirection().equals(Direction.Horizontal))
				|| ((tile1.getRow() == whitePlayer.getDestination().getTargetNumber()) 
						&& whitePlayer.getDestination().getDirection().equals(Direction.Vertical))) {
			game.setGameStatus(GameStatus.WhiteWon);
			gameStatus = "WhiteWon";
				}
		if((whitePlayer.getRemainingTime().getMinutes() == 0 
				&& whitePlayer.getRemainingTime().getSeconds() == 0) || 
				((tile2.getColumn() == blackPlayer.getDestination().getTargetNumber()) 
						&& blackPlayer.getDestination().getDirection().equals(Direction.Horizontal))
				|| ((tile2.getRow() == blackPlayer.getDestination().getTargetNumber()) 
						&& blackPlayer.getDestination().getDirection().equals(Direction.Vertical))){
			game.setGameStatus(GameStatus.BlackWon);
			gameStatus = "BlackWon";
				}
		
		
		
		
		return gameStatus;
		}
		
		
		
		

		//System.out.println(gameStatus);
		
	


	/**
	 * @author Benjamin Emiliani
	 * @param currentPlayer : a player to force its remaining time to zero
	 * @throws InvalidInputException
	 */
	@SuppressWarnings("deprecation")
	public static void countDownClock(Player currentPlayer) throws InvalidInputException {

		try {
		currentPlayer.setRemainingTime(new Time(0 , 0 , 0));
	}
		catch (RuntimeException e){
			throw new InvalidInputException("Could not set remaining time of player");
		}
	}

	/*
	 * This method puts the game in replay mode.  It should start by stopping the clock.
	 * It will display the move number and the state of the board at this number.
	 * Initial position is the current position (move size-1).
	 * @author Katrina Poulin
	 */
	public static void goToReplayMode() {
		wasRunning = false;
		Game game = quoridor.getCurrentGame();
		if(game.getGameStatus().equals(GameStatus.Running)) wasRunning = true;
		quoridor.getCurrentGame().setGameStatus(GameStatus.Replay);
		currReplayIndex = quoridor.getCurrentGame().numberOfPositions()-1;
		BoardView.nextButton.setEnabled(false);
	}

	/**
	 * This method exits replay mode. Will do the opposite of goToReplayMode();
	 * 1. Get current index of the moves list
	 * 2. from that index to the size of replayMoves, copy moves.
	 * 3. start game (should start clock?)
	 *
	 * @author Katrina Poulin
	 */
	public static void exitReplayMode() {
		// TODO Auto-generated method stub
		Wall wall = null;
		if(wasRunning) quoridor.getCurrentGame().setGameStatus(GameStatus.Running);
		for(int i = currReplayIndex+1; i < quoridor.getCurrentGame().numberOfPositions(); i++) {
			Move move = quoridor.getCurrentGame().getMove(i-1);
			if(move instanceof WallMove) {
				wall = ((WallMove) move).getWallPlaced();
//				((WallMove) move).getWallPlaced().setMove(null);
			}
			move.delete();
			quoridor.getCurrentGame().getPosition(i).delete();
			if(wall!=null) {
			if(wall.getOwner().equals(quoridor.getCurrentGame().getBlackPlayer())) {
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(wall);
			}
			else if (wall.getOwner().equals(quoridor.getCurrentGame().getWhitePlayer())) {
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(wall);
			}
			}
		}
	}

	/**
	 * When in replay mode, goes to the next move in the list.
	 * @author Katrina Poulin
	 */
	public static void stepForward() {
	//TODO
		if(currReplayIndex < quoridor.getCurrentGame().numberOfPositions()) {
			currReplayIndex++;
			BoardView.nextButton.setEnabled(true);
			BoardView.prevButton.setEnabled(true);
		}
		else {
			BoardView.nextButton.setEnabled(false);
			BoardView.prevButton.setEnabled(true);

		}
		quoridor.getCurrentGame().setCurrentPosition(quoridor.getCurrentGame().getPosition(currReplayIndex));
	}

	/**
	 * When in replay mode, goes to the previous move in the list.
	 * @author Katrina Poulin
	 */
	public static void stepBackward() {
		//TODO
		//replayMoves.remove(gameMoves.get(currReplayIndex));
		if(currReplayIndex>0) {
		currReplayIndex--;
		BoardView.prevButton.setEnabled(true);
		BoardView.nextButton.setEnabled(true);
		}
		else {
			BoardView.prevButton.setEnabled(false);
			BoardView.nextButton.setEnabled(true);

		}
		quoridor.getCurrentGame().setCurrentPosition(quoridor.getCurrentGame().getPosition(currReplayIndex));
	}
	public static void stepBackward(BoardView boardView) {
		//TODO
		//replayMoves.remove(gameMoves.get(currReplayIndex));
		if(currReplayIndex>0) {
		currReplayIndex--;
		BoardView.prevButton.setEnabled(true);
		BoardView.nextButton.setEnabled(true);
		}
		else {
			BoardView.prevButton.setEnabled(false);
			BoardView.nextButton.setEnabled(true);

		}
		quoridor.getCurrentGame().setCurrentPosition(quoridor.getCurrentGame().getPosition(currReplayIndex));
	}

	/**
	 * Returns the number of moves in the game (size of the moves list)
	 * @author Katrina Poulin
	 * @return number of moves
	 */
	public static int getMovesNo() {
		return quoridor.getCurrentGame().numberOfMoves();
	}

	public static void changePosition() {
		Game game = quoridor.getCurrentGame();
		GamePosition oldPos = game.getCurrentPosition();
		Player whitePlayer = game.getWhitePlayer();
		Player blackPlayer = game.getBlackPlayer();
		Tile whiteTile = oldPos.getWhitePosition().getTile();
		Tile blackTile = oldPos.getBlackPosition().getTile();
		Player player = game.getCurrentPosition().getPlayerToMove();
		int id = game.numberOfPositions();

		
		GamePosition newPos = new GamePosition(id+differentIdGenerator(), new PlayerPosition(whitePlayer, whiteTile), new PlayerPosition (blackPlayer, blackTile), player, game);

		for(Wall wall : oldPos.getBlackWallsInStock()) {
			newPos.addBlackWallsInStock(wall);
		}
		for(Wall wall : oldPos.getBlackWallsOnBoard()) {
			newPos.addBlackWallsOnBoard(wall);
		}
		for(Wall wall: oldPos.getWhiteWallsInStock()) {
			newPos.addWhiteWallsInStock(wall);
		}
		for(Wall wall : oldPos.getWhiteWallsOnBoard()) {
			newPos.addWhiteWallsOnBoard(wall);
		}
		game.addPosition(newPos);
		game.setCurrentPosition(newPos);

	}

	public static int getPositions() {
		return quoridor.getCurrentGame().numberOfPositions();
	}

}
//	private static void initializeEmptyPos() {
//		Game game = quoridor.getCurrentGame();
//		GamePosition oldPos = game.getCurrentPosition();
//		Player whitePlayer = game.getWhitePlayer();
//		Player blackPlayer = game.getBlackPlayer();
//		Tile whiteTile = oldPos.getWhitePosition().getTile();
//		Tile blackTile = oldPos.getBlackPosition().getTile();
//		Player player = game.getCurrentPosition().getPlayerToMove();
//		int id = game.numberOfPositions();
//		
//		GamePosition newPos = new GamePosition(id, new PlayerPosition(whitePlayer, whiteTile), new PlayerPosition (blackPlayer, blackTile), player, game);
//		for(Wall wall : oldPos.getBlackWallsInStock()) {
//			newPos.addBlackWallsInStock(wall);
//		}
//		for(Wall wall : oldPos.getBlackWallsOnBoard()) {
//			newPos.addBlackWallsOnBoard(wall);
//		}
//		for(Wall wall: oldPos.getWhiteWallsInStock()) {
//			newPos.addWhiteWallsInStock(wall);
//		}
//		for(Wall wall : oldPos.getWhiteWallsOnBoard()) {
//			newPos.addWhiteWallsOnBoard(wall);
//		}
//		game.addPosition(newPos);
//		game.setCurrentPosition(newPos);
//		
//	}
//}





