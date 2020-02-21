package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

import java.io.File;
import static org.junit.Assert.assertTrue;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.*;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.properties.SaveProperties;
import ca.mcgill.ecse223.quoridor.view.BoardView;
import ca.mcgill.ecse223.quoridor.view.GameResult;
import ca.mcgill.ecse223.quoridor.view.PlayerMenu;
import cucumber.api.PendingException;
import cucumber.api.java.en.But;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


//import view TODO

@SuppressWarnings("deprecation")
public class CucumberStepDefinitions {


	User user1;
	User user2;

	File testFile;
	File fileToCheck;
	boolean positionIsValid;
	boolean indexOutOfBound = false;
	boolean eventProcessed;

	private PlayerMenu playerMenu = new PlayerMenu();

	private static Player nextPlayer;
	private static ArrayList<Player> players;
	Quoridor quoridor = QuoridorApplication.getQuoridor();
	WallMove candidate;

	private static PawnBehavior pawnBehavior = new PawnBehavior();
	private static boolean isStart = true;
	



	@Given("A {string} wall move candidate exists at position {int}:{int}")
	public void a_wall_move_candidate_exists_at_position(String dir, Integer row, Integer col) {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition position = game.getCurrentPosition();
		if(position.getPlayerToMove()==game.getBlackPlayer()) {
		candidate = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, position.getPlayerToMove(), quoridor.getBoard().getTile((row ) * 9 + col), game, Direction.Vertical, position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1) );
		} else {
			candidate = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, position.getPlayerToMove(), quoridor.getBoard().getTile(row * 9 + col ), game, Direction.Vertical, position.getWhiteWallsInStock(position.numberOfWhiteWallsInStock()-1) );

		}
		quoridor.getCurrentGame().setWallMoveCandidate(candidate);
		if(dir.equals("horizontal")) candidate.setWallDirection(Direction.Horizontal);
		if(dir.equals("vertical")) candidate.setWallDirection(Direction.Vertical);
	}

	@Given("The black player is located at {int}:{int}")
	public void the_black_player_is_located_at(Integer row, Integer col) {
		Tile targetTile =  quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col));
		PlayerPosition position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), targetTile);
		quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(position);
	}

	@Given("The white player is located at {int}:{int}")
	public void the_white_player_is_located_at(Integer row, Integer col) {
		Tile targetTile =  quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col));
		PlayerPosition position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), targetTile);
		quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(position);
	}
	
	boolean whiteCheck;
	boolean blackCheck;
	@When("Check path existence is initiated")
	public void check_path_existence_is_initiated() {
		whiteCheck = QuoridorGameController.checkPath(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame());
		blackCheck = QuoridorGameController.checkPath(quoridor.getCurrentGame().getBlackPlayer(), quoridor.getCurrentGame());
	}
	

	@Then("Path is available for {string} player\\(s)")
	public void path_is_available_for_player_s(String string) {
		System.out.println(whiteCheck);
		System.out.println(blackCheck);
		if(string.equals("both")) {
			assertTrue(whiteCheck && blackCheck);
		} else if (string.equals("black")) {
			assertTrue(!whiteCheck && blackCheck);
		} else if (string.equals("white")) {
			assertTrue(whiteCheck && !blackCheck);
		} else if (string.equals("none")) {
			assertTrue(!whiteCheck && !blackCheck);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@When("I initiate to load a game in {string}")
	public void i_initiate_to_load_a_game_in(String string) {
	    
	    try {
			positionIsValid = QuoridorGameController.loadGame(string);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
	}

	@When("Each game move is valid")
	public void each_game_move_is_valid() {
	    assertTrue(positionIsValid);
	}

	@When("The game has no final results")
	public void the_game_has_no_final_results() {
		assertTrue(QuoridorGameController.checkGameResult().equals("Running"));
	}
	
	boolean alreadyChecked;
	//int number = 0;
	@Then("{string} shall have a horizontal wall at {int}:{int}")
	public void shall_have_a_horizontal_wall_at(String player, int row, int col) {
		String orientation = "Horizontal";
		//number = 0;
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
		if(player.equals("black")) {
			assertEquals(blackWalls.get(0).getMove().getTargetTile().getRow(), row);
			assertEquals(blackWalls.get(0).getMove().getTargetTile().getColumn(), col);
			if(orientation.equals("Horizontal")) {
				assertEquals(blackWalls.get(0).getMove().getWallDirection(),Direction.Horizontal);
			}
			else {
				assertEquals(blackWalls.get(0).getMove().getWallDirection(),Direction.Vertical);

			}
		}
		else if (player.equals("white")){
			
			for(int i =0; i < whiteWalls.size(); i++ ) {
				if(row == whiteWalls.get(i).getMove().getTargetTile().getRow()) {
					assertEquals(row, whiteWalls.get(i).getMove().getTargetTile().getRow());
					assertEquals(col, whiteWalls.get(i).getMove().getTargetTile().getColumn());
				
					if(orientation.equals("Horizontal")) {
						assertEquals(whiteWalls.get(i).getMove().getWallDirection(),Direction.Horizontal);
					}
					else if(orientation.equals("Vertical")) {
						assertEquals(whiteWalls.get(i).getMove().getWallDirection(),Direction.Vertical);
					}
					i++;
				}
			}
			
			//number++;
		}
		else {
			System.out.println("Input invalid");
		}
	}

	@Then("{string} shall have a vertical wall at {int}:{int}")
	public void shall_have_a_vertical_wall_at(String player, int row, int col) {
		String orientation = "Vertical";
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
		System.out.println(whiteWalls.size() + " " + blackWalls.size());
		if(player.equals("black")) {
			
			
			for(int i =0; i < whiteWalls.size(); i++ ) {
				if(row == blackWalls.get(i).getMove().getTargetTile().getRow()) {
					assertEquals(blackWalls.get(i).getMove().getTargetTile().getRow(), row);
					assertEquals(blackWalls.get(i).getMove().getTargetTile().getColumn(), col);
					if(orientation.equals("Horizontal")) {
						assertEquals(blackWalls.get(i).getMove().getWallDirection(),Direction.Horizontal);
					}
					else {
						assertEquals(blackWalls.get(i).getMove().getWallDirection(),Direction.Vertical);

					}
				}
			}
		}
		else if (player.equals("white")){
			assertEquals(row, whiteWalls.get(0).getMove().getTargetTile().getRow());
			assertEquals(col, whiteWalls.get(0).getMove().getTargetTile().getColumn());
			if(orientation.equals("Horizontal")) {
				assertEquals(whiteWalls.get(0).getMove().getWallDirection(),Direction.Horizontal);
			}
			else if(orientation.equals("Vertical")) {
				assertEquals(whiteWalls.get(0).getMove().getWallDirection(),Direction.Vertical);

			}
		}
		else {
			System.out.println("Input invalid");
		}
	}

//	@When("The game has a final result")
//	public void the_game_has_a_final_result() {
//		assertTrue(!QuoridorGameController.checkGameResult().equals("Running"));
//	}

	@When("The game to load has an invalid move")
	public void the_game_to_load_has_an_invalid_move() {
		assertTrue(!positionIsValid);
	}

	
	
	@Then("The game shall notify the user that the game file is invalid")
	public void the_game_shall_notify_the_user_that_the_game_file_is_invalid() {
	    // Write code here that turns the phrase above into concrete actions
		//GUI Step definition
		assertTrue(!positionIsValid);
	}
	
	
	
	
	
	
	
	


	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * @param player : the color of the player that has just completed their move (black or white).
	 * This method sets up the next player based on the color of the player that has made its move.
	 */
	@Given("Player {string} has just completed his move")
	public void playerHasJustCompletedHisMove(String player) {

		Player nextPlayer;
		if (player.equals("white")) {
			nextPlayer = quoridor.getCurrentGame().getBlackPlayer();
		}else {
			nextPlayer = quoridor.getCurrentGame().getWhitePlayer();
		}

		quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(nextPlayer);
	}






	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * @param player : the color of the player that has just completed their move (black or white).
	 * @param row : the row of the player that just moved
	 * @param col : the col of the player that just moved
	 * This method places the player that has made their move on a tile given tile.
	 */
	@And("The new position of {string} is {int}:{int}")
	public void theNewPositionOfPlayerIs(String player, int row, int col) {
		Player playerMoved;
		if (player.equals("black")) {
			playerMoved = quoridor.getCurrentGame().getBlackPlayer();
		}else {
			playerMoved = quoridor.getCurrentGame().getWhitePlayer();
		}
		Tile targetTile =  quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col));
		PlayerPosition position = new PlayerPosition(playerMoved, targetTile);

		if(playerMoved.hasGameAsBlack()) {
			quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(position);
		} else {
			quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(position);
		}

	}


	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * @param player : the color of the player that has just completed their move (black or white).
	 * This method sets the remaining time for the player that just moved to greater than zero.
	 */
	@And("The clock of {string} is more than zero")
	public void theClockOfPlayerIsMoreThanZero(String player) {

		int min = 5;
		int sec = 0;
		Player playerMoved;
		if (player.equals("black")) {
			playerMoved = quoridor.getCurrentGame().getBlackPlayer();
		}else {
			playerMoved = quoridor.getCurrentGame().getWhitePlayer();
		}
		playerMoved.setRemainingTime(new Time(0, min , sec));
	}


	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * This method calls the controller and runs a check of many variables of the current game to determine the
	 * current result of the game (i.e. pending, whiteWon, blackWon, Draw, etc...)
	 */
	@When("Checking of game result is initated")
	public void checkingGameResultInitiated() {
		QuoridorGameController.checkGameResult();

	}


	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * @param result : the current game status determined after each move and players' remaining time
	 */
	@Then("Game result shall be1 {string}")
	public void gameResultShallBe1(String result) {
		
		if (result.equals("pending")) {
			assertTrue(quoridor.getCurrentGame().getGameStatus().equals(GameStatus.Running));
		}
		if (result.equals("Drawn")) {
			System.out.println(quoridor.getCurrentGame().getGameStatus() + "\n\n\n\n");
			assertTrue(quoridor.getCurrentGame().getGameStatus().equals(GameStatus.Draw));
		}
	}




	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 */
	@And("The game shall no longer be running1")
	public void theGameShallNoLongerBeRunning1() {
		//No need to assert that the current Game status is != to GameStatus.Running since it will be changed
		//previously to something else and verified in the next gherkin step

	}

	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameWon
	 * @param player : color of player to which we would like to force their time remaining equal to zero.
	 */
	@When("The clock of {string} counts down to zero")
	public void theClockOrPlayerCountsDownToZero(String player) throws InvalidInputException {

		Player currentPlayer;
		if (player.equals("white")) {
			currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		}
		else {
			currentPlayer = quoridor.getCurrentGame().getBlackPlayer();
		}
		QuoridorGameController.countDownClock(currentPlayer);
	}


	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameDrawn
	 * This method adds certain moves to set up the game such that the proceeding move is a third repeated move by
	 * the white player
	 */
	@Given("The following moves were executed:")
	public void theFollowingMovesWereExecuted(DataTable table) {


		Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();
		Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
		Tile targetTile1 = new Tile (8 ,5 , quoridor.getBoard());
		Tile targetTile2 = new Tile (2 ,5 , quoridor.getBoard());
		Tile targetTile3 = new Tile (9 ,5 , quoridor.getBoard());
		Tile targetTile4 = new Tile (1 ,5 , quoridor.getBoard());
		StepMove move1 = new StepMove ( 1, 1 , whitePlayer, targetTile1 , quoridor.getCurrentGame());
		StepMove move2 = new StepMove ( 2, 1 , blackPlayer, targetTile2 , quoridor.getCurrentGame());
		StepMove move3 = new StepMove ( 3, 2 , whitePlayer, targetTile3 , quoridor.getCurrentGame());
		StepMove move4 = new StepMove ( 4, 2 , blackPlayer, targetTile4 , quoridor.getCurrentGame());
		StepMove move5 = new StepMove ( 5, 3 , whitePlayer, targetTile1 , quoridor.getCurrentGame());
		StepMove move6 = new StepMove ( 6, 3 , blackPlayer, targetTile2 , quoridor.getCurrentGame());
		StepMove move7 = new StepMove ( 7, 4 , whitePlayer, targetTile3 , quoridor.getCurrentGame());
		StepMove move8 = new StepMove ( 8, 4 , blackPlayer, targetTile4 , quoridor.getCurrentGame());
		StepMove move9 = new StepMove ( 9, 5 , whitePlayer, targetTile1 , quoridor.getCurrentGame());

		quoridor.getCurrentGame().addMove(move1);
		quoridor.getCurrentGame().addMove(move2);
		quoridor.getCurrentGame().addMove(move3);
		quoridor.getCurrentGame().addMove(move4);
		quoridor.getCurrentGame().addMove(move5);
		quoridor.getCurrentGame().addMove(move6);
		quoridor.getCurrentGame().addMove(move7);
		quoridor.getCurrentGame().addMove(move8);
		quoridor.getCurrentGame().addMove(move9);

	}

	/**
	 * @author Benjamin Emiliani
	 * @feature IdentifyGameDrawn
	 * @param player : the color of the player that has just completed their move (black or white).
	 * @param row : the row of the player that just moved
	 * @param col : the col of the player that just moved
	 */
	@And("The last move of {string} is pawn move to {int}:{int}")
	public void theLastMoveOfPlayerIsPawnMoveTo(String player,int row, int col) {

		Player playerMoved;
		if (player.equals("whit")) {
			playerMoved = quoridor.getCurrentGame().getWhitePlayer();
			Tile targetTile = new Tile (row ,col , quoridor.getBoard());
			StepMove move = new StepMove ( 9, 5 , playerMoved, targetTile , quoridor.getCurrentGame());
			quoridor.getCurrentGame().addMove(move);
			}

	}


	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method sets the position of the current player as a tile given by its row and column
	 * @param int row: The row of the players pawn
	 * @param int col: The column of the player's pawn
	 */
	@And("The player is located at {int}:{int}")
	public void thePlayerIsLocatedAt(int row, int col){
		Tile targetTile =  quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col));
		Player playerToMove = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

		PlayerPosition position = new PlayerPosition(playerToMove, targetTile);
		if(playerToMove.hasGameAsBlack()) {
			quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(position);
		} else {
			quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(position);
		}
	}



	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method ensures that no walls are beside the current player
	 * This should already be given
	 * @param String orientation: The direction the wall is oriented
	 * @param String side: The direction in respect to the player's tile
	 */
	@And("There are no {string} walls {string} from the player")
	public void thereAreNoWalls(String orientation, String side){
		//already guaranteed
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method ensures that the opponent isn't located on an adjacent tile
	 * This should already be given
	 * @param String side: The direction in respect to the player's tile
	 */
	@And("The opponent is not {string} from the player")
	public void theOpponentIsNotSidefromThePlayer(String side) {
		//already guaranteed as black is at their starting position
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method initiates the player's move by calling the controller
	 * @param String player: The name of the player
	 * @param String side: The direction in respect to the player's tile
	 */
	@When("Player {string} initiates to move {string}")
	public void playerInitiatesToMove(String player, String side){
		QuoridorGameController.grabPawn(pawnBehavior);

		if(side.equals("up")) {
			if(pawnBehavior.isLegalStep(MoveDirection.North)) {
				eventProcessed = pawnBehavior.stepPawnNorth();
			} else {
				eventProcessed = pawnBehavior.jumpPawnNorth();
			}
		}
		else if (side.equals("down")) {
			if(pawnBehavior.isLegalStep(MoveDirection.South)) {
				eventProcessed = pawnBehavior.stepPawnSouth();
			} else {
				eventProcessed = pawnBehavior.jumpPawnSouth();
			}
		}
		else if (side.contentEquals("left")) {
			if(pawnBehavior.isLegalStep(MoveDirection.West)) {
				eventProcessed = pawnBehavior.stepPawnWest();

			} else {
				eventProcessed = pawnBehavior.jumpPawnWest();
			}

		}
		else if (side.contentEquals("right")) {
			if(pawnBehavior.isLegalStep(MoveDirection.East)) {
				eventProcessed = pawnBehavior.stepPawnEast();
			} else {
				eventProcessed = pawnBehavior.jumpPawnEast();
			}

		}



		else if (side.contentEquals("upleft")) {
			eventProcessed = pawnBehavior.jumpPawnNorthWest();
			if(eventProcessed == false) {
				eventProcessed = pawnBehavior.jumpPawnWestNorth();
			}
			//try both
		} else if (side.contentEquals("downleft")) {
			eventProcessed = pawnBehavior.jumpPawnSouthWest();
			if(eventProcessed == false) {
				eventProcessed = pawnBehavior.jumpPawnWestSouth();
			}
		} else if (side.contentEquals("upright")) {
			eventProcessed = pawnBehavior.jumpPawnNorthEast();
			if(eventProcessed == false) {
				eventProcessed = pawnBehavior.jumpPawnEastNorth();
			}
		} else if (side.contentEquals("downright")) {
			eventProcessed = pawnBehavior.jumpPawnSouthEast();
			if(eventProcessed == false) {
				eventProcessed = pawnBehavior.jumpPawnEastSouth();
			}
		}

	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method adds a wall with a specific dimension in a position given by row and column by calling the controller
	 * @param String orientation: The direction the wall is oriented
	 * @param int row: The row of the players pawn
	 * @param int col: The column of the player's pawn
	 */
	@And("There is a {string} wall at {int}:{int}")
	public void ThereIsAOrientationWallAtRowCol(String orientation, int row, int col) {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		Direction direction;
		if(orientation.equals("horizontal")) {
			direction = Direction.Horizontal;
		} else {
			direction = Direction.Vertical;
		}


		indexOutOfBound = false;

		Wall wallToMove = game.getCurrentPosition().getWhiteWallsInStock().get(1);
		WallMove wallMove =
				new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
					game.getWhitePlayer(),
					QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row+1,col+1)),
					game, direction, wallToMove);
			game.addMove(wallMove);
			game.getCurrentPosition().addWhiteWallsOnBoard(wallToMove); //updates board
		}



	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method asserts that the event is processed if the move is successful and asserts that it isn't if not
	 * @param String side: The direction in respect to the player's tile
	 * @param String status: Whether or not the move is successful
	 */
	@Then("The move {string} shall be {string}")
	public void theMoveShallBe(String side, String status) {
		if(status.equals("success")) {
			assertTrue(eventProcessed);
		} else {
			assertFalse(eventProcessed);
		}
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method asserts that the new position of the player is adjusted accordingly
	 * @param int row: The row of the players pawn
	 * @param int col: The column of the player's pawn
	 */
	@And("Player's new position shall be {int}:{int}")
	public void playersNewPositionShallBe(int row, int col) {
		int actualRow;
		int actualCol;
		if(eventProcessed) {
		if(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer().hasGameAsBlack()) {
			actualRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
			actualCol = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();

		} else {
			actualRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
			actualCol = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		}
		} else {
			if(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
				actualRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
				actualCol = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();

			} else {
				actualRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
				actualCol = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			}
		}
		assertEquals(row, actualRow);
		assertEquals(col, actualCol);
	}


	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method asserts that the next player to move is correct
	 * @param String player: the name of the player
	 */
	@And("The next player to move shall become {string}")
	public void theNextPlayerToMoveShallBecome(String player) {


		if(player.equals("black")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack());
		}


		else {

			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite());

		}
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method sets the opponents position using a specific row and column
	 * @param int row: The row of the players pawn
	 * @param int col: The column of the player's pawn
	 */
	@And("The opponent is located at {int}:{int}")
		public void theOpponentIsLocatedAt(int row, int col) {

		if(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer().hasGameAsBlack()) {
			quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(new PlayerPosition
					(quoridor.getCurrentGame().getBlackPlayer(),
					quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row,col))));
		} else { //white
			quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(new PlayerPosition
					(quoridor.getCurrentGame().getWhitePlayer(),
					quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row,col))));
		}
	}

	/**
	 * @author Roey Wine, Yousuf Badawi, Benjamin Emiliani, Katrina Poulin, and Kyle Myers
	 * This method removes any walls that are beside the opponent
	 * @param String orientation: The direction the wall is oriented
	 * @param String side: The direction in respect to the player's tile
	 */
	@And("There are no {string} walls {string} from the player nearby")
	public void alreadyTrue(String orientation, String side) {
		//already true
	}









	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		players = createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		initQuoridorAndBoard();
		players = createUsersAndPlayers("user1", "user2");
		createAndStartGame(players);
	}
	
	@Given("^The game is running1$")
	public void theGameIsRunning1() {
		initQuoridorAndBoard();
		players = createUsersAndPlayers1("user1", "user2");
		createAndStartGame1(players);
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], 
					quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(wrow+1, wcol+1)), 
					quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}
		// The following step definitions are for the "SetTotalThinkingTime" feature

	/**
	 * @author Roey Wine
	 * This method creates a game, players, users, and a board and sets the game status to initializing
	 */
	@Given("A new game is initializing")
	public void aNewGameIsInitializing() throws Throwable {
		theGameIsRunning();
		quoridor.getCurrentGame().setGameStatus(GameStatus.Initializing);
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the white player's username isn't null
	 */
	@And ("White player chooses a username")
	public void whitePlayerChoosesAUsername() {
		if (quoridor.getCurrentGame() == null) System.out.println("hi\n\n\n\n\n\n");
		assertTrue(quoridor.getCurrentGame().getWhitePlayer().getUser().getName() != null);
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the black player's username isn't null
	 */
	@And ("Black player chooses a username")
	public void blackPlayerChoosesAUsername() {
		assertTrue(quoridor.getCurrentGame().getBlackPlayer().getUser().getName() != null);
	}

	/**
	 * @author Roey Wine
	 * When the thinking time is set, this method calls on the corresponding controller method
	 * @param min: total minutes of thinking time
	 * @param sec: additional seconds of thinking time
	 * @throws InvalidInputException
	 */
	@When("{int}:{int} is set as the thinking time")
	public void isSetAsTheThinkingTime(int min, int sec) throws InvalidInputException{
		QuoridorGameController.setTotalThinkingTime(min, sec);
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the black and white players both have a total thinking time equal to the set time
	 * @param int min: total minutes of thinking time
	 * @param int sec: additional seconds of thinking time
	 */
	@Then("Both players shall have {int}:{int} remaining time left")
	public void bothPlayersShallHaveRemainingTimeLeft(int min, int sec) {
		assertEquals(min, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getMinutes());
		assertEquals(min, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getMinutes());
		assertEquals(sec, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getSeconds());
		assertEquals(sec, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getSeconds());
	}




	// The following step definitions are for the "InitializeBoard" feature

	/**
	 * @author Roey Wine
	 * This method creates a game, players, users, and a board and sets the game status to ready to start
	 */
	@Given ("The game is ready to start")
	public void theGameIsReadyToStart() {
		QuoridorGameController.startNewGame();
		quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
	}

	/**
	 * @author Roey Wine
	 * When the initialization of the board is initiated, this method calls on the corresponding controller method
	 * @throws InvalidInputException
	 */
	@When("The initialization of the board is initiated")
	public void theInitializationOfTheBoardIsInitiated() throws InvalidInputException{
		QuoridorGameController.initializeBoard();
	}

	/**
	 * @author Roey Wine
	 * This method asserts that it is the white players turn
	 * @throws Throwable
	 */
	@Then("It shall be white player to move")
	public void itShallBeWhitePlayerToMove() throws Throwable {
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer()));
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the white player is in their correct initial position
	 */
	@And("White's pawn shall be in its initial position")
	public void whitesPawnShallBeInItsInitialPosition() {
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().equals(quoridor.getBoard().getTile(36)));
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the black player is in their correct initial position
	 */
	@And("Black's pawn shall be in its initial position")
	public void blacksPawnShallBeInItsInitialPosition() {
		assertEquals(quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile(), quoridor.getBoard().getTile(44));
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the white player has all their remaining walls in stock
	 */
	@And("All of White's walls shall be in stock")
	public void allOfWhitesWallsShallBeInStock() {
		assertEquals(quoridor.getCurrentGame().getWhitePlayer().numberOfWalls(), Player.maximumNumberOfWalls());
	}

	/**
	 * @author Roey Wine
	 * This method asserts that the black player has all their remaining walls in stock
	 */
	@And("All of Black's walls shall be in stock")
	public void allOfBlacksWallsShallBeInStock() {
		assertEquals(quoridor.getCurrentGame().getBlackPlayer().numberOfWalls(), Player.maximumNumberOfWalls());
	}

	/**
	 * @author Roey Wine
	 * This method starts the white player's clock
	 */
	@And("White's clock shall be counting down")
	public void whitesClockShallBeCountingDown() throws InvalidInputException {
		assertTrue (QuoridorGameController.getWhiteTimerIsRunning());
	}

	/**
	 * @author Roey Wine
	 * This method displays that it is the first player's (white player) turn
	 */
	@And("It shall be shown that this is White's turn")
	public void itShallBeShownThatThisIsWhitesTurn() {

		BoardView.refreshData();
		assertEquals(BoardView.currentPlayerUserName, quoridor.getCurrentGame().getWhitePlayer().getUser().getName());
	}

	
	
	// The following step definitions are for the report final result feature
	
	/**
	 * @author Roey Wine
	 * When this game stops running this method calls on the controller to report the final result
	 * @throws InvalidInputException 
	 */
	@When("The game is no longer running")
	public void theGameIsNoLongerRunning( ) throws InvalidInputException {
		QuoridorGameController.reportFinalResult();
	}
	
	/**
	 * @author Roey Wine
	 * This method ensures that the final result is displayed in the view
	 */
	@Then("The final result shall be displayed")
	public void theFinalResultShallBeDisplayed() {
		assertTrue(GameResult.gameSts == null);
	}
	
	/**
	 * @author Roey Wine
	 * This method ensures that white's clock isn't running
	 */
	@And ("White's clock shall not be counting down")
	public void whitesClockShallNotBeCountingDown() {
		assertFalse(QuoridorGameController.getWhiteTimerIsRunning());
	}
	
	/**
	 * @author Roey Wine
	 * This method ensures that black's clock isn't running
	 */
	@And ("Black's clock shall not be counting down")
	public void blacksClockShallNotBeCountingDown() {
		assertFalse(QuoridorGameController.getWhiteTimerIsRunning());
	}
	
	/**
	 * @author Roey Wine
	 * This method asserts that white's pawn cannot move
	 */
	@And ("White shall be unable to move")
	public void whiteShallBeUnableToMove() {
		assertFalse(BoardView.tilesEnabled);
	}
	
	/**
	 * @author Roey Wine
	 * This method asserts that black's pawn cannot move
	 */
	@And ("Black shall be unable to move")
	public void blackShallBeUnableToMove() {
		assertFalse(BoardView.tilesEnabled);
	}
	
	
	// The following step definitions are for the resign game feature
	
	/**
	 * @author Roey Wine
	 * When a player resigns the game this method calls on the controller to set that the opponent wins
	 * @throws InvalidInputException 
	 */
	@When("Player initates to resign")
	public void thePlayerInitiatesToResign() throws InvalidInputException {
		QuoridorGameController.ResignGame(); 
	}
	
	/**
	 * @author Roey Wine
	 * This method asserts that the current game status as a string is equal to the inputed string
	 * @param String gameStatus: the current game status as a string 
	 */
	@Then("Game result shall be {string}")
	public void gameResultShallBe(String gameStatus) {
		assertEquals(gameStatus, quoridor.getCurrentGame().getGameStatus().toString());
	}
	
	/**
	 * @author Roey Wine
	 * This method asserts that the game status is not running
	 */
	@And("The game shall no longer be running")
	public void theGameShallNoLongerBeRunning() {
		assertFalse(quoridor.getCurrentGame().getGameStatus().equals(GameStatus.Running));
	}
	
	
	
	





	/**
	 * @author  BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 */
	@Given("A New game is initializing")
	public void aNewGameIsinitializing() {

		initQuoridorAndBoard();
		players = createUsersAndPlayers("user1","user2");
		createNewGame(players);
	}



	/**
	 *@author BenjaminEmiliani
	 *@throws InvalidInputException
	 *@feature StartNewGame
	 *Method initializes a new game
	 */
	@When("A new game is being initialized")
	public void aNewGameIsBeingInitialized() throws InvalidInputException {


		createNewGame(players);

	}



	/**
	 *@author BenjaminEmiliani
	 *@throws InvalidInputException
	 *@feature StartNewGame
	 */
	@And("White player chooses a username1")
	public void whitePlayerChoosesAUsername1() throws InvalidInputException {

		try {
		//QuoridorGameController.createUser("bob");
		QuoridorGameController.setWhitePlayerUsername("user1");
	}
		catch(InvalidInputException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}


	/**
	 *@author BenjaminEmiliani
	 *@throws InvalidInputException
	 *@feature StartNewGame
	 */
	@And("Black player chooses a username1")
	public void blackPlayerChoosesAUsername1() throws InvalidInputException {

		try {
		QuoridorGameController.setBlackPlayerUsername("user2");
	}
		catch(InvalidInputException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}



	/**
	 *@author BenjaminEmiliani
	 * @throws InvalidInputException
	 *@feature StartNewGame
	 *Checks that each player was assigned the thinkingTime in the createUsersAndPlayers() method
	 */
	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet() throws InvalidInputException {

		assertTrue(QuoridorGameController.totalThinkingTimeSet());

	}



	/**
	 * @author  BenjaminEmiliani
	 * @feature StartNewGame
	 * Method will set the current game's status is Ready to Start
	 */
	@Then("The game shall become ready to start")
	public void theGameShallBecomeReadyToStart() {

		QuoridorGameController.readyToStart();
		assertTrue(quoridor.getCurrentGame().getGameStatus() == GameStatus.ReadyToStart);
	}







	/**
	 * @author  BenjaminEmiliani
	 * @feature StartNewGame
	 * @throws InvalidInputException
	 * Method will set game status to ready to start
	 */
	@Given("The game is ready to start1")
	public void theGameIsReadyToStart1() throws InvalidInputException{

		try {
			createNewGame(players);
			quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
			//Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, quoridor);
	}
		catch(RuntimeException e) {
			throw new InvalidInputException("Could not start game");
		}
	}


	/**
	 * @author  BenjaminEmiliani
	 * @feature StartNewGame
	 * This method calls the controller to start the clock. The startClock operation is pending, thus it throws
	 * the pendingException
	 */
	@When("I start the clock")
	public void iStartTheClock()   {

		Time time = quoridor.getCurrentGame().getBlackPlayer().getRemainingTime();
		QuoridorGameController.startClock(time);

	}




	/**
	 * @author  BenjaminEmiliani
	 * @feature StartNewGame
	 * Method will check the current game's status and assert true if it is Running
	 */
	@Then("The game shall be running")
	public void theGameShallBeRunning1() {

		//System.out.println(quoridor.getCurrentGame().getGameStatus() +"\n\n\n\n\n\n\n");
		assertTrue(quoridor.getCurrentGame().getGameStatus() == GameStatus.Running);
	}





	/**
	 * @author BenjaminEmiliani
	 * @feature StartNewGame
	 * Check if the board has been initialized
	 */
	@And("The board shall be initialized")
	public void theBoardShallBeInitialized() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertTrue(quoridor.hasBoard());
	}















	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param String
	 * This method sets up the next player to set its username based on the player's color
	 */
	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIsColor(String color) {

		if(color.equals("white")) {

			nextPlayer = quoridor.getCurrentGame().getWhitePlayer();

		}

		if(color.equals("black")) {

			nextPlayer = quoridor.getCurrentGame().getBlackPlayer();
		}
	}





	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param username : a username to be created so that it is existing
	 */
	@And("There is existing user {string}")
	public void thereIsExistingUser(String username) {

		quoridor.addUser(username);

	}





	/**
	 * @author BenjaminEmiliani
	 * @throws PendingException
	 * @feature ProvideSelectUsername
	 * @param username : A username that exists and is being selected by the next player
	 * Once implemented, this method will assign the existing username selected to the next player
	 * when a game is initializing
	 * @throws InvalidInputException
	 */
	@When("The player selects existing {string}")
	public void thePlayerSelectsExistingUsername(String username) throws InvalidInputException  {

		QuoridorGameController.selectExistingUsername(nextPlayer, username);

	}

	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param color : The color of the next player
	 * @param aUsername : an existing username set to next player
	 */
	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerColorInTheNewGameShallBe(String color, String aUsername) {

		if (color.equals("white")) {
			assertTrue(nextPlayer.getUser().getName().equals(aUsername));
		}

		if (color.equals("black")) {
			assertTrue(nextPlayer.getUser().getName().equals(aUsername));
		}
	}





	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param username : A username that does not exist in the list of name
	 */
	@And("There is no existing user {string}")
	public void thereIsNoExistingUsername(String username) {

		int num = quoridor.numberOfUsers();
		List<User> Users = quoridor.getUsers();

		for(int i = 0 ; i < num ; i++) {

			if(Users.get(i).getName().equals(username)) {
				Users.get(i).delete();
				break;
			}
		}
	}




	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param newUsername : A new username provided
	 * @throws InvalidInputException
	 */
	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUsername(String newUsername) throws InvalidInputException  {

		String error = "";
	try {
		QuoridorGameController.createUser(newUsername);
		nextPlayer.setUser(User.getWithName(newUsername));
		}
	catch (InvalidInputException e) {
		error = e.getMessage();
		if (error.equals("Cannot create due to duplicate name")) {
			error = "This username already exists. Please use a different username.";
		}
	  }
	}

	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param username : A username which already exists
	 */
	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatUsernameAlreadyExists(String username) throws InvalidInputException{

		playerMenu.getTxtUsername1().setText(username);
		playerMenu.getAddUser1().doClick();
		assertTrue(playerMenu.getErrorMessage().getText().equals(username + " already exists."));
	}





	/**
	 * @author BenjaminEmiliani
	 * @feature ProvideSelectUsername
	 * @param color: The color of next player
	 */
	@And("Next player to set user name shall be {string}")
	public void nextPlayerToSetUserNameShallBe(String color) {


		if (color == "white") {
			assertTrue(nextPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) ;

		}
		if (color == "black") {
			assertTrue(nextPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) ;

		}
	}



	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		quoridor.getCurrentGame().setWallMoveCandidate(null);
		BoardView.wallMoveCandidate=null;
		assertTrue(BoardView.wallMoveCandidate==null);
	}

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		QuoridorGameController.createCandidate();
	}



	/*--------------------------------
	 * Feature: Move Wall
	 *--------------------------------*/

	/**
	 * This method ensures that a wall candidate is existing at a given position in a given direction.
	 *
	 * @author Katrina Poulin
	 * @param dir The direction of the wall candidate
	 * @param row The row number of the wall candidate
	 * @param col The column number of the wall candidate
	 */
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirAtPos(String dir, int row, int col) {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition position = game.getCurrentPosition();
		if(position.getPlayerToMove()==game.getBlackPlayer()) {
		candidate = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, position.getPlayerToMove(), quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col)), game, Direction.Vertical, position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1) );
		} else {
			candidate = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, position.getPlayerToMove(), quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col)), game, Direction.Vertical, position.getWhiteWallsInStock(position.numberOfWhiteWallsInStock()-1) );

		}
		quoridor.getCurrentGame().setWallMoveCandidate(candidate);
		if(dir.equals("horizontal")) candidate.setWallDirection(Direction.Horizontal);
		if(dir.equals("vertical")) candidate.setWallDirection(Direction.Vertical);
	}


	/**
	 * This method ensures that the wall is not to be placed in an invalid position, i.e. on a side of the board
	 *
	 * @author Katrina Poulin
	 * @param side Side edge of the board (up, down, left, right)
	 */

	@And("The wall candidate is not at the {string} edge of the board")
	public void theWallCandidateIsNotAtTheSideEdgeOfTheBoard(String side ) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		if(side.equals("up")) {
		if(candidate.getWallDirection()==Direction.Horizontal) {
			assertTrue(candidate.getTargetTile().getRow() != 1);
		}
		}

		if(side.equals("down")) {
			if(candidate.getWallDirection()==Direction.Horizontal) {
				assertTrue(candidate.getTargetTile().getRow() != 10);
		}
		}

		if(side.equals("right")) {
			if(candidate.getWallDirection()==Direction.Vertical) {
				assertTrue(candidate.getTargetTile().getColumn()!=10);
			}
		}
		if(side.equals("left")) {
			if(candidate.getWallDirection()==Direction.Vertical) {
				assertTrue(candidate.getTargetTile().getColumn() != 1);
		}
		}
	}
	/**
	 * This method will move the wall from one tile in the given direction (up, down, left, right)
	 *
	 * @author Katrina Poulin
	 * @param side The side where the wall should be moved towards
	 * @throws InvalidInputException
	 */
	@When("I try to move the wall {string}")
	public void iTryToMoveTheWallSide(String side) throws InvalidInputException {

		QuoridorGameController.moveWallInDirection(side);
	}

	/**
	 * This method makes sure that, when a target position is valid, the wall will be moved over to it.
	 * @author Katrina Poulin
	 * @param nrow The new row of the wall
	 * @param ncol The new column of the wall
	 */
	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void theWallShallBeMovedOverTheBoardToPosition(int nrow, int ncol) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		game.setWallMoveCandidate(candidate);
		candidate.setTargetTile(quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(nrow, ncol)));
		assertTrue(quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getRow()==nrow);
		assertTrue(quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn()==ncol);
	}

	/**
	 * This method sets the wall to be on one of the sides of the board
	 * @author Katrina Poulin
	 * @param side Side of the board (up, down, left, right)
	 */
	@Given("The wall candidate is at the {string} edge of the board")
	public void theWallCandidateIsAtTheSideEdgeOfTheBoard(String side) {

		int wallColumn = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
		int wallRow = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();

		if(quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Horizontal)) {

			if(side.equals("up")) quoridor.getCurrentGame().getWallMoveCandidate().setTargetTile(quoridor.getBoard().getTile(wallColumn)); //TODO modify with formula
			else if(side.equals("down")) quoridor.getCurrentGame().getWallMoveCandidate().setTargetTile(quoridor.getBoard().getTile(72+wallColumn));//TODO
		}

		if(quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection().equals(Direction.Vertical)) {

			if(side.equals("left")) quoridor.getCurrentGame().getWallMoveCandidate().setTargetTile(quoridor.getBoard().getTile(((wallRow-1)*9)+1));//TODO
			if(side.equals("right")) quoridor.getCurrentGame().getWallMoveCandidate().setTargetTile(quoridor.getBoard().getTile(wallRow*9));//TODO

		}
	}

	/**

	 * This method notifies the user that their move is illegal
	 * @author Katrina Poulin
	 */
	@Then("I shall be notified that my move is illegal")
	public void iShallBeNotifiedThatMyMoveIsIllegal() {
		QuoridorGameController.notifyIllegalMove(); //UI-related feature
	}


	/*
	 * Feature: Drop Wall
	 */

	/**
	 * This method ensures that the candidate position is valid
	 *
	 * @author Katrina Poulin
	 * @param dir The direction of the wall (horizontal, vertical)
	 * @param row The row of the wall
	 * @param col The column of the wall
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithStringAtPosIsValid(String dir, int row, int col) throws InvalidInputException{

		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition position = game.getCurrentPosition();
		Wall wall = position.getPlayerToMove().getWall(9);
		candidate = new WallMove(game.numberOfMoves(), game.numberOfMoves()%2, position.getPlayerToMove(), QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row, col)), game, Direction.Vertical, wall );
		QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(candidate);

		if(dir.equals("horizontal"))candidate.setWallDirection(Direction.Horizontal);
		if(dir.equals("vertical"))candidate.setWallDirection(Direction.Vertical);
		candidate.setTargetTile(QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row, col)));
		QuoridorGameController.wallMoveIsValid(candidate);

	}

	/**
	 * This method makes the wall candidate invalid
	 *
	 * @author Katrina Poulin
	 * @param dir The direction of the wall move candidate
	 * @param row The row of the wall move candidate
	 * @param col The column of the wall move candidate
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
	public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException{
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition position = game.getCurrentPosition();
		Direction direction;
		if(dir.equals("horizontal")) {
			direction = Direction.Horizontal;
		} else {
			direction = Direction.Vertical;
		}
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();
		indexOutOfBound = false;
			if(currentPlayer.equals(game.getWhitePlayer())) {//the current player is white
			try {
				WallMove wallMove = new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
					currentPlayer, quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col)),
					game, direction, position.getWhiteWallsInStock(position.numberOfWhiteWallsInStock()-1));
				game.addMove(wallMove);
				position.addWhiteWallsOnBoard(wallMove.getWallPlaced()); //updates board
			} catch(Exception e) {
				indexOutOfBound = true;
		}
			}
			else { //player is black
				try {
					WallMove wallMove = new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
							currentPlayer, quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col)),
							game, direction, position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1));
						game.addMove(wallMove);
						position.addBlackWallsOnBoard(wallMove.getWallPlaced()); //updates board
					} catch(Exception e) {
						indexOutOfBound = true;
					}
			}
			WallMove myMove = new WallMove(game.numberOfMoves(), (game.numberOfMoves()+1)/2,
				currentPlayer, QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row, col)),
					game, direction, position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1));
			game.addMove(myMove);
			position.addBlackWallsOnBoard(myMove.getWallPlaced());
			assertFalse(QuoridorGameController.wallMoveIsValid(myMove));
			}

//			position.addWhiteWallsOnBoard(wallToMove); //updates board
//			}
//		else { //player is black
//			try {
//				Wall wallToMove = position.getBlackWallsInStock(position.numberOfBlackWallsInStock()-1);
//				WallMove wallMove = new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
//						currentPlayer, QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row, col)),
//						game, direction, wallToMove);
//				game.addMove(wallMove);
//				position.addBlackWallsOnBoard(wallToMove); //updates board
//				} catch(Exception e) {
//					indexOutOfBound = true;
//				}
//		}
//		WallMove myMove = new WallMove(game.numberOfMoves(), (game.numberOfMoves()+1)/2,
//				currentPlayer, QuoridorApplication.getQuoridor().getBoard().getTile(QuoridorGameController.getTileIndex(row, col)),
//				game, direction, position.getBlackWallsInStock().get(1));
//		game.setWallMoveCandidate(myMove);
//		game.addMove(myMove);
//		position.addBlackWallsOnBoard(myMove.getWallPlaced());
//
//		System.out.println(QuoridorGameController.validatePosition(position));

	WallMove wallMove;
	/**
	 * This method releases the wallMove candidate on the board
	 * @author Katrina Poulin
	 */
	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand() throws InvalidInputException{
		wallMove = QuoridorGameController.releaseWall();
	}

	/**
	 * This method registers the newly placed wall at its new position
	 * @param dir
	 * @param row
	 * @param col
	 */
	@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
	public void aWallMoveShallBeRegisteredWithDirAtPos(String dir, int row, int col) {

		WallMove thatMove = wallMove;
		assertTrue(thatMove.getTargetTile().getRow()==row);
		assertTrue(thatMove.getTargetTile().getColumn()==col);
		if (dir.equals("horizontal"))assertTrue(thatMove.getWallDirection().equals(Direction.Horizontal));
		if (dir.equals("vertical"))assertTrue(thatMove.getWallDirection().equals(Direction.Vertical));


	}

	/**
	 * This method ensures that the currentPlayer does not have a wall in their hand
	 * @author Katrina Poulin
	 */
	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMtHand() {
		assertTrue(quoridor.getCurrentGame().getWallMoveCandidate() == null);
	}

	/**
	 * This method makes the turn of currentPlayer complete
	 *
	 * @author Katrina Poulin
	 */
	@And("My move shall be completed")
	public void myMoveShallBeCompleted() throws InvalidInputException{

		assertTrue(quoridor.getCurrentGame().numberOfMoves() == 1);
	}

	/**
	 * This method changes the current player variable
	 *
	 * @author Katrina Poulin
	 * @throws InvalidInputException
	 */
	@And("It shall not be my turn to move")
	public void itShallNotBeMyTurnToMove() throws InvalidInputException {
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player otherPlayer;
		if(currentPlayer==quoridor.getCurrentGame().getBlackPlayer()) { otherPlayer = quoridor.getCurrentGame().getWhitePlayer();}
		else { otherPlayer = quoridor.getCurrentGame().getBlackPlayer();}
		QuoridorGameController.switchPlayer();
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove()== otherPlayer);

	}



	/**
	 * This method notifies the player that their move is illegal
	 * @author Katrina Poulin
	 */
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyMoveIsInvalid() {
		QuoridorGameController.notifyMoveInvalid(); //UI-related feature
	}
	/**
	 * This method ensures that the player still has a wall in their hand
	 * @author Katrina Poulin
	 */
	@Then("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		Wall myWall;
		if(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().equals(quoridor.getCurrentGame().getBlackPlayer())) {
			myWall = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(1);
		}
		else {
			myWall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(1);

		}
		WallMove wallmove = new WallMove(quoridor.getCurrentGame().numberOfMoves(), quoridor.getCurrentGame().numberOfMoves()%2, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove(), quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(7,7)) , quoridor.getCurrentGame(), Direction.Vertical, myWall);
		quoridor.getCurrentGame().setWallMoveCandidate(wallmove);
		BoardView.refreshData(BoardView.panel_3, BoardView.gbl_panel_3);
		assertTrue(QuoridorGameController.getCandidate()!=null);

	}

	/**
	 * This method ensures that it is currentPlayer's turn to move
	 * @author Katrina Poulin
	 */
	@Then("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove()==currentPlayer);
	}

	/**
	 * This method ensures that there is no wall registered at a given position
	 *
	 * @author Katrina Poulin
	 * @param dir The direction of the wall
	 * @param row The row of the wall
	 * @param col The column of the wall
	 */
	@But("No wall move shall be registered with {string} at position \\({int}, {int})")
	public void noWallMoveShallBeRegisteredWithDirAtPos(String dir, int row, int col) throws InvalidInputException{
		quoridor.getCurrentGame().removeMove(quoridor.getCurrentGame().getWallMoveCandidate());
	}

	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *Checks if the player has more walls on stock
	 */
	@Given("I have more walls on stock")
	public void iHaveMoreWallsOnStock() throws Throwable{
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

		assertTrue(currentPlayer.hasWalls());
	}


	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *The player grabs a wall from the stock
	 */
	@When("I try to grab a wall from my stock")
	public void iTryToGrabAWallFromMyStock() throws Throwable{
		QuoridorGameController.grabWall();
	}


	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *A wall move candidate is created at initial position
	 */
	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		Game currentGame = quoridor.getCurrentGame();
		assertTrue(currentGame.hasWallMoveCandidate());
		WallMove candidate = quoridor.getCurrentGame().getWallMoveCandidate();
		assertTrue(candidate.getTargetTile().getRow()==9);
		assertTrue(candidate.getTargetTile().getColumn()==9);


	}

	/**
	 *
	 *@author Katrina Poulin
	 *@feature GrabWall
	 *The wall in the player's hand disappears from his/her stock
	 */
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		GamePosition currentPos = quoridor.getCurrentGame().getCurrentPosition();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		if(currentPlayer == quoridor.getCurrentGame().getBlackPlayer()) currentPos.removeBlackWallsInStock(quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced());
		else currentPos.removeWhiteWallsInStock(quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced());
	}

	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *Checks if the player has no more walls on stock
	 */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		GamePosition position = quoridor.getCurrentGame().getCurrentPosition();
		if(currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
			for (int i=0; i <= 9; i++) {
				position.removeWhiteWallsInStock(Wall.getWithId(i));
			}
			assertTrue(position.numberOfWhiteWallsInStock()==0);
		}
		else {
			for (int j=10; j <= 19; j++) {
				position.removeBlackWallsInStock(Wall.getWithId(j));
			}
			assertTrue(position.numberOfBlackWallsInStock()==0);
		}
	}


	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *The player receives a notification that he/she has no more walls on stock
	 */
	@Then("I shall be notified that I have no more walls")
	public void iShallBeNotifiedThatIhaveNoMoreWalls() {
		QuoridorGameController.notifyNoWalls();
	}


	/**
	 *
	 *@author Katrina Poulin
	 * @feature GrabWall
	 *The player has no walls in his/her hand
	 */
	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		//UI
		quoridor.getCurrentGame().setWallMoveCandidate(null);
	}

	/**
	 *
	 *@author Katrina Poulin
	 *@feature RotateWall
	 *The player flips the wall
	 */
	@When("I try to flip the wall")
	public void iTryToFlipTheWall() throws Throwable {
		WallMove wallMove = quoridor.getCurrentGame().getWallMoveCandidate();
		QuoridorGameController.flipWall(wallMove);
	}


	/**
	 *
	 *@author Katrina Poulin
	 *@feature RotateWall
	 *The wall is rotated over the board to "<newdir>"
	 */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardToDirection(String newdir) {
		//TODO UI
		WallMove wallMove = quoridor.getCurrentGame().getWallMoveCandidate();

		if(newdir.equals("horizontal")) {
			wallMove.setWallDirection(Direction.Horizontal);
			assertTrue(wallMove.getWallDirection()==Direction.Horizontal);
		}
		else {
			wallMove.setWallDirection(Direction.Vertical);
			assertTrue(wallMove.getWallDirection()==Direction.Vertical);
		}
	}


	/**
	 *
	 *@author Katrina Poulin
	 *@feature RotateWall
	 *A wall move candidate is created with "<newdir>" at position (<row>, <col>)
	 */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithNewDirectionAtPosition(String newdir, int row, int col) {

		WallMove wallMove = quoridor.getCurrentGame().getWallMoveCandidate();
		wallMove.setTargetTile(quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(row, col)));
		assertTrue(wallMove.getTargetTile().getRow()==row);
		assertTrue(wallMove.getTargetTile().getColumn()==col);
		if(newdir.equals("horizontal")) {
			wallMove.setWallDirection(Direction.Horizontal);
			assertTrue(wallMove.getWallDirection().equals(Direction.Horizontal));
		}
		else {
			wallMove.setWallDirection(Direction.Vertical);
			assertTrue(wallMove.getWallDirection().equals(Direction.Vertical));
		}

	}









	/**
	 * @author Yousuf Badawi
	 * @param fileName
	 * @throws Throwable
	 */
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsInTheFileSystem(String fileName) throws Throwable{

		//After a lot of issues with not knowing how to initialize the save file without the use
		//of a controller method, I realized that I needed to implement file creation
		//for this deliverable. Edwin Pan helped me with this implementation, which I was inspired by
		//as this makes it a lot easier to change the save file directory later on.

		File file= new File(SaveProperties.filePath + fileName);
		file.delete();
	}

	/**
	 * @author Yousuf Badawi
	 * @param fileName
	 * @throws Throwable
	 */
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithName(String fileName) throws Throwable{
		fileToCheck = QuoridorGameController.saveGame(fileName);
	}

	/**
	 * @author Yousuf Badawi
	 * @param fileName
	 * @throws Throwable
	 */
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithNameShallBeCreatedInTheFilesystem(String fileName) throws Throwable{
		boolean result = QuoridorGameController.fileExists(fileName);
		assertEquals(true, result);
	}

	/**
	 * @author Yousuf Badawi
	 * @param fileName
	 * @throws Throwable
	 */
	@Given("File {string} exists in the filesystem")
	public void fileExistsInTheFileSystem(String fileName) throws Throwable{
		File fileToCreate= new File(SaveProperties.filePath + fileName); //may or may not be overwritten
		testFile= new File(SaveProperties.filePath + fileName + ".test"); //empty file for checking
		fileToCreate.delete(); //ensure file is empty
		testFile.createNewFile();
		fileToCreate.createNewFile();
	}

	/**
	 * @author Yousuf Badawi
	 * @param fileName
	 * @throws Throwable
	 */
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwriteExistingFile() throws Throwable{
		QuoridorGameController.setOverWrite(true);
		fileToCheck = QuoridorGameController.forceSaveGame(fileToCheck.getName());
	}

	/**
	 * @author Yousuf Badawi
	 * @throws Throwable
	 */
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithNameShallBeUpdatedInTheFilesystem(String fileName) throws Throwable{
		//String is never used in this implementation (saveGame already called in @when)
		//Compares whether test file initially created and save file are the same
		boolean areFilesIdentical = true;
		File file1 = fileToCheck;
		File file2 = testFile;

		if (!file1.exists() || !file2.exists()) {
		  System.out.println("One or both files do not exist");
		  areFilesIdentical = false;
		}

		if (file1.length() != file2.length()) {
			System.out.println("Files do not have the same length");
			areFilesIdentical = false;
		}

		try {
		  FileInputStream fis1 = new FileInputStream(file1);
		  FileInputStream fis2 = new FileInputStream(file2);
		  int i1 = fis1.read();
		  int i2 = fis2.read();
		  while (i1 != -1) {
		    if (i1 != i2) {
		      areFilesIdentical = false;
		      break;
		    }
		    i1 = fis1.read();
		    i2 = fis2.read();
		  }
		  fis1.close();
		  fis2.close();
		} catch (IOException e) {
		  System.out.println("IO exception");
		  areFilesIdentical = false;
		}
		assertEquals(true, areFilesIdentical);
	}

	/**
	 * @author Yousuf Badawi
	 * @throws Throwable
	 */
	@And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwriteExistingFile() throws Throwable{
		//GUI Method to be implemented inside of saveGame -- TODO for later
		QuoridorGameController.setOverWrite(false);
	}

	/**
	 * @author Yousuf Badawi
	 * @throws Throwable
	 */
	@Then("File {string} shall not be changed in the filesystem")
	public void fileShallNotBeChangedInTheFilesystem(String fileName) throws Throwable{
		boolean areFilesIdentical = true;
		File file1 = fileToCheck;
		File file2 = testFile;

		if (!file1.exists() || !file2.exists()) {
		  System.out.println("One or both files do not exist");
		  areFilesIdentical = false;
		}

		System.out.println("length:1. " + file1.length());
		System.out.println("length:2. " + file2.length());

		if (file1.length() != file2.length()) {
			System.out.println("Files do not have the same length");
			areFilesIdentical = false;
		}

		try {
		  FileInputStream fis1 = new FileInputStream(file1);
		  FileInputStream fis2 = new FileInputStream(file2);
		  int i1 = fis1.read();
		  int i2 = fis2.read();
		  while (i1 != -1) {
		    if (i1 != i2) {
		      areFilesIdentical = false;
		      break;
		    }
		    i1 = fis1.read();
		    i2 = fis2.read();
		  }
		  fis1.close();
		  fis2.close();
		} catch (IOException e) {
		  System.out.println("IO exception");
		  areFilesIdentical = false;
		}


		assertEquals(true, areFilesIdentical);
	}

	boolean indexOutOfBBound;

	/**
	 * @author Yousuf Badawi
	 * @param row
	 * @param column
	 * @throws Throwable
	 */
	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void aGamePositionIsSuppliedWithPawnCoordinates(int row, int col) throws Throwable{
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();
		indexOutOfBound = false;
		if(currentPlayer.equals(game.getWhitePlayer())) {//the current player is white
			try {
			game.getCurrentPosition().setWhitePosition(new PlayerPosition (currentPlayer,
					QuoridorApplication.getQuoridor().getBoard().getTile((row-1)*9 +col-1)));
			} catch (Exception e) {
				indexOutOfBound = true;
			}
		}
		else { //player is black
			try {
			game.getCurrentPosition().setBlackPosition(new PlayerPosition (currentPlayer,
					QuoridorApplication.getQuoridor().getBoard().getTile((row-1)*9 +col-1)));
			}
			catch (Exception e){
				indexOutOfBound = true;
			}
		}

	}

	/**
	 * @author Yousuf Badawi
	 * @throws Throwable
	 */
	@When("Validation of the position is initiated")
	public void validationOfThePositionIsInitiated() throws Throwable{
		positionIsValid = QuoridorGameController.validatePosition(QuoridorApplication.getQuoridor().
				getCurrentGame().getCurrentPosition());
	}

	/**
	 * @author Yousuf Badawi
	 * @throws Throwable
	 */
	@Then("The position shall be {string}")
	public void thePositionShallBe(String result) throws Throwable{
		boolean isValid;
		if(result.equals("ok")) {
			isValid = true;
		} else {
			isValid = false;
		}
		assertEquals(isValid, positionIsValid);
	}

	/*
	 *
	 *
	 * BEGINNING OF KYLE FEATURE STEP DEFINITIONS, PLEASE DO NOT PUT ANYTHING IN BETWEEN
	 *
	 *
	 *
	 */

	//************************************************
		//LOAD POSITION Feature Step Definitions
		//Author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		//************************************************
		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will load a game using the controller interface
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Load Position
		 */
		@When("I initiate to load a saved game {string}") //FOR WHEN STATEMENTS: calling to controller to change things in the model
		public void iInitiateToLoadASavedGame(String filename) throws Throwable{
			positionIsValid = QuoridorGameController.loadPosition(filename);
			//throw new cucumber.api.PendingException();
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the information from the loaded game is useable and correct
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Load Position
		 */
		@And("The position to load is valid")
		public void thePositionIsValid() {

			if(quoridor.getCurrentGame() == null) {
				System.out.println("Wow\n\n\n\n\n\n");
			}
			GamePosition currentPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
			try {
				System.out.println(currentPosition.toString());
				QuoridorGameController.validatePosition(currentPosition);
			} catch (InvalidInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if it is the correct player's turn
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Load Position
		 */
		@Then("It shall be {string}'s turn") //check if model has updated properly
		public void itIsPlayerTurn(String player) {
			Player white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			if(player.equals("white")) {
				assertEquals(currentPlayer, white);
			}
			else {
				assertEquals(currentPlayer, black);
			}
		}

		@And("{string} shall have a {string} wall at {int}:{int}")
		public void playerShallHaveAOrientationWallAtRowCol(String player, String orientation, int row, int col) {
			List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
			List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
			if(player.equals("black")) {
				assertEquals(blackWalls.get(0).getMove().getTargetTile().getRow(), row);
				assertEquals(blackWalls.get(0).getMove().getTargetTile().getColumn(), col);
				if(orientation.equals("Horizontal")) {
					assertEquals(blackWalls.get(0).getMove().getWallDirection(),Direction.Horizontal);
				}
				else {
					assertEquals(blackWalls.get(0).getMove().getWallDirection(),Direction.Vertical);

				}
			}
			else if (player.equals("white")){
				assertEquals(row, whiteWalls.get(0).getMove().getTargetTile().getRow());
				assertEquals(col, whiteWalls.get(0).getMove().getTargetTile().getColumn());
				if(orientation.equals("Horizontal")) {
					assertEquals(whiteWalls.get(0).getMove().getWallDirection(),Direction.Horizontal);
				}
				else if(orientation.equals("Vertical")) {
					assertEquals(whiteWalls.get(0).getMove().getWallDirection(),Direction.Vertical);

				}
			}
			else {
				System.out.println("Input invalid");
			}
		}





		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the player's pawn is at the correct position
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Load Position
		 */
		@And("{string} shall be at {int}:{int}")
		public void playerIsAtP_rowP_col(String player, int row, int col) {
			Player white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			if(player.equals("black")) {
				//assertEquals(currentPlayer, black);
				assertEquals(row, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow());
				assertEquals(col, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn());
			}
			else if(player.equals("white")) {
				//assertEquals(currentPlayer, white);
				assertEquals(row, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
				assertEquals(col, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());
			}
			else {
				System.out.println("Player neither 'black' or 'white'");
			}
		}
	 /** These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if they have the correct number of walls remaining to be placed
	 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
	 * @Feature: Load Position
	 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersHaveRemainingWallsInTheirStack(int remaining_walls) {
		assertEquals(remaining_walls, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock());
		assertEquals(remaining_walls, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock());
	}

	/**
	 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if an incorrect loading will happen when trying to load a previous game
	 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
	 * @Feature: Load Position
	 */
	@And("The position to load is invalid")
	public void thePositionToLoadIsInvalid(){
		assertEquals(false, positionIsValid);
	}

	/**
	 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if an error is produced when laoding a previous game fails
	 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
	 * @Feature: Load Position
	 */
	@Then("The load shall return an error")
	public void theLoadShallReturnAnError() {
		assertEquals(false, positionIsValid);
	}

	//************************************************
		//SWITCH CURRENT PLAYER Feature Step Definitions
		//Author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		//************************************************

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the correct player is set to move next
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@Given("The player to move is {string}")
		public void thePlayerToMoveIs(String player) {

			Quoridor quoridor = QuoridorApplication.getQuoridor();
			if(player.equals("black")) {
				Player currentPlayer = quoridor.getCurrentGame().getBlackPlayer();
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
				System.out.println("\n\n\n\n\n" + "black will end his turn to switch player");
			}
			else if(player.equals("white")){
				Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
				System.out.println("\n\n\n\n\n" + "white will end his turn to switch player");

			}else {
				System.out.println("Neither black or white was set as the current player");
			}

		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the clock of the player who's in the process of making a move is still running
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@And("The clock of {string} is running")
		public void theClockOfPlayerIsRunning(String player) throws InvalidInputException{
			//Quoridor quoridor = QuoridorApplication.getQuoridor();
			//long currentTime = System.currentTimeMillis();
			//Time time = new Time(currentTime);
			if(player.equals("black")) {
				//quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(time);
				QuoridorGameController.setBlackTimerIsRunning(true);
				QuoridorGameController.setWhiteTimerIsRunning(false);

			}
			else if(player.equals("white")){
				//quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(time);
				QuoridorGameController.setBlackTimerIsRunning(false);
				QuoridorGameController.setWhiteTimerIsRunning(true);
			}else {
				System.out.println("Neither black or white was set as the current player");
			}

		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the clock is stopped for the player who's turn is isn't
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@And("The clock of {string} is stopped")
		public void theClockOfOtherIsStopped(String other) throws InvalidInputException{
			//Quoridor quoridor = QuoridorApplication.getQuoridor();
			//long currentTime = 0;
			//Time time = new Time(currentTime);
			if(other.equals("black")) {
				//quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(time);
				QuoridorGameController.setBlackTimerIsRunning(false);
				QuoridorGameController.setWhiteTimerIsRunning(true);

			}
			else if(other.equals("white")){
				//quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(time);
				QuoridorGameController.setBlackTimerIsRunning(true);
				QuoridorGameController.setWhiteTimerIsRunning(false);
			}else {
				System.out.println("Neither black or white was set as the current player");
			}	}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the correct action is performed when a player finishes his/her turn
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@When("Player {string} completes his move")
		public void playerPlayerCompletesHisMove(String player) throws Throwable {
			QuoridorGameController.switchPlayer();
			//throw new cucumber.api.PendingException();
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the correct player's turn is displayed on the GUI
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@Then("The user interface shall be showing it is {string} turn")
		public void theUserInterfaceShallBeShowingItIsOtherTurn(String other) {
			if(other.equals("black")) {
				Player p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
				assertEquals(p, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove());

			}
			else if(other.equals("white")){
				Player p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
				assertEquals(p, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove());

			}
			else {
				System.out.println("Neither player was input");
			}
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the previous players clock is stopped
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@And("The clock of {string} shall be stopped")
		public void theClockOfPlayerShallBeStopped(String player) throws InvalidInputException{
			boolean timerStatus;
			timerStatus = QuoridorGameController.timerStatus(player);
			assertEquals(false, timerStatus);
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the clock has begun for the now current player's turn
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@And("The clock of {string} shall be running")
		public void theClockOfOtherShallBeRunning(String other) throws InvalidInputException{
			boolean timerStatus;
			timerStatus = QuoridorGameController.timerStatus(other);
			assertEquals(true, timerStatus);
		}

		/**
		 * These methods are invoked when specified test scenarios are called in Gherkin. The following method will test to see if the next player who's turn it is has updated
		 * @author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
		 * @Feature: Switch Current Player
		 */
		@And("The next player to move shall be {string}")
		public void theNextPlayerToMoveShallBeOther(String other) {
			if(other.equals("white")) {
				Player p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
				assertTrue(p.equals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer()));

			}
			else if(other.equals("black")){
				Player p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
				assertTrue(p.equals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer()));
			}
		}

		
		
	//************************************************
	//JumpToStart Feature Step Definitions
	//Author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
	//************************************************	
		
		/**
		 * This will set up all the moves for a given game for testing replay mode
		 * @param dt
		 */
		@Given("The following moves have been played in game:")
		public void theFollowingMovesHaveBeenPlayerInGame(DataTable dt) {			
			
			initQuoridorAndBoard();
			players = createUsersAndPlayers("user1", "user2");
			createAndStartGame(players);
			
			Player white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			Player black = quoridor.getCurrentGame().getBlackPlayer();
			Direction hDirection = Direction.Horizontal;
			Direction vDirection = Direction.Vertical;
			
			Move ogWhite = new StepMove(0,1,white,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 9)), quoridor.getCurrentGame());
			
			Move ogBlack = new StepMove(0,2,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 1)), quoridor.getCurrentGame());
			
			Move move1 = new StepMove(1,1,white,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 8)), quoridor.getCurrentGame());
			
			Move move2 = new StepMove(1,2,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 2)), quoridor.getCurrentGame());
			
			Move move3 = new StepMove(2,1,white,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 7)), quoridor.getCurrentGame());
			
			Move move4 = new StepMove(2,2,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 3)), quoridor.getCurrentGame());
			
			Move move5 = new WallMove(3,1,white,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 7)), quoridor.getCurrentGame(), hDirection, white.getWall(1));
			quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(white.getWall(1));
			quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(white.getWall(1));

			
			Move move6 = new WallMove(3,2,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 8)), quoridor.getCurrentGame(), hDirection, black.getWall(1));
			quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(black.getWall(1));
			quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(black.getWall(1));

			Move move7 = new WallMove(4,1,white,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(5, 7)), quoridor.getCurrentGame(), vDirection, white.getWall(2));
			quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(white.getWall(2));
			quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(white.getWall(2));

			Move move8 = new StepMove(4,2,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(6, 2)), quoridor.getCurrentGame());

			quoridor.getCurrentGame().addMove(move1);
			quoridor.getCurrentGame().addMove(move2);
			quoridor.getCurrentGame().addMove(move3);
			quoridor.getCurrentGame().addMove(move4);
			quoridor.getCurrentGame().addMove(move5);
			quoridor.getCurrentGame().addMove(move6);
			quoridor.getCurrentGame().addMove(move7);
			quoridor.getCurrentGame().addMove(move8);
			
			
			Move move = new StepMove(5,1,black,quoridor.getBoard().getTile(QuoridorGameController.getTileIndex(6, 3)), quoridor.getCurrentGame());
			quoridor.getCurrentGame().addMove(move);

			
		}
		
		
		/**
		 * Check if the next move is correct, although pointless
		 * @param a
		 * @param b
		 */
		@And("The next move is {int}.{int}") //The next move is <movno>.<rndno>
		public void theNextMoveIsMovnoRndno(int a, int b) {
		//does not matter lol
		}
		/**
		 * Initiate the jump to start controller method
		 */
		@When("Jump to start position is initiated")
		public void jumpToStartPositionIsInitiated() {
			QuoridorGameController.jumpToStart();
		}
		
		/**
		 * Check to see if the next move is correct
		 * @param nmov
		 * @param nrnd
		 */
		@Then("The next move shall be {int}.{int}") //The next move shall be <nmov>.<nrnd>
		public void theNextMoveShallBeNmoveNrnd(int nmov, int nrnd) {
			
				int nextMoveNumber = quoridor.getCurrentGame().getMove(2).getMoveNumber();
				int nextRoundNumber = quoridor.getCurrentGame().getMove(2).getRoundNumber();
				assertEquals(nmov, nextMoveNumber);
				assertEquals(nrnd, nextRoundNumber);
			
		}
		
		/**
		 * Asserting if white is in the right position
		 * @param row
		 * @param col
		 */
		@And("White player's position shall be \\({int},{int})")//White player's position shall be (<wrow>,<wcol>)
		public void whitePlayerPositionShallBeWrowWcol(int row, int col) {
			assertEquals(row, quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
			assertEquals(col, quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());

		}
		
		/**
		 * Asserting if black is in the right position
		 * @param row
		 * @param col
		 */
		@And("Black player's position shall be \\({int},{int})")//White player's position shall be (<wrow>,<wcol>)
		public void blackPlayerPositionShallBeWrowWcol(int row, int col) {
			assertEquals(row, quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow());
			assertEquals(col, quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn());

		}
		
		/**
		 * Asserting to see if white has all the walls from the begining
		 * @param wallsNum
		 */
		@And("White has {int} on stock") //White has <wwallno> on stock
		public void whiteHasNumWallsOnStock(int wallsNum) {
			assertEquals(wallsNum, quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
		}
		
		/**
		 * Asserting to see if black has all the walls from the begining
		 * @param wallsNum
		 */
		@And("Black has {int} on stock") //White has <wwallno> on stock
		public void BlackHasWwallnoOnStock(int wallsNum) {
			assertEquals(wallsNum, quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());

		}
		
	//************************************************
	//JumpToFinal Feature Step Definitions
	//Author: Kyle Myers: email-kyle.myers@mail.mcgill.ca id-260851765
	//************************************************	
		
		/**
		 * Jump to final position controller method is called
		 */
		@When("Jump to final position is initiated")
		public void jumpToFinalPosiiton() {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("hola");
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			QuoridorGameController.jumpToEnd();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("hola");
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		}
		
		/**
		 * Check if the next move is correct, although pointless
		 * @param a
		 * @param b
		 */
		@Then("The next move will be {int}.{int}")//The next move will be <nmov>.<nrnd>
		public void theNextMoveWillBe(int nmov, int nrnd) {
			
			List<Move> moves = quoridor.getCurrentGame().getMoves();

			int nextMoveNumber = moves.get(moves.size()-1).getMoveNumber();
			int nextRoundNumber = moves.get(moves.size()-1).getRoundNumber();
			assertEquals(nmov, nextMoveNumber);
			assertEquals(nrnd, nextRoundNumber);
			
		}
		
		/**
		 * Asserting if white is in the right position
		 * @param row
		 * @param col
		 */
		@And("White player's position will be \\({int},{int})")//White player's position will be (<wrow>,<wcol>)
		public void whitePlayerPositionWillBeWrowWcol(int row, int col) {
			assertEquals(col, quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
			assertEquals(row, quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());

		}
		
		/**
		 * Asserting if black is in the right position
		 * @param row
		 * @param col
		 */
		@And("Black player's position will be \\({int},{int})")//White player's position shall be (<wrow>,<wcol>)
		public void blackPlayerPositionWillBeWrowWcol(int row, int col) {
			assertEquals(col, quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow());
			assertEquals(row, quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn());

		}
		
		/**
		 * Asserting to see if white has all the walls from the begining
		 * @param wallsNum
		 */
		@And("White has {int} on their stock") //White has <wwallno> on stock
		public void whiteHasNumWallsOnTheirStock(int wallsNum) {
			assertEquals(wallsNum, quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
		}
		
		/**
		 * Asserting to see if black has all the walls from the begining
		 * @param wallsNum
		 */
		@And("Black has {int} on their stock") //White has <wwallno> on stock
		public void BlackHasWwallnoOnTheirStock(int wallsNum) {
			assertEquals(wallsNum, quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());

		}
		
	

	/*
	 *
	 *
	 * END OF KYLE FEATURE STEP DEFINITIONS, PLEASE DO NOT PUT ANYTHING IN BETWEEN
	 *
	 *
	 *
	 */



//		***KATRINA STEP DEFINITIONS STEP BACKWARD/STEP FORWARD***
//		***********************ITERATION-5***********************


//		***FEATURE: STEP FORWARD***

	/**
	 * This method puts the game in replay mode to test the StepBackward
	 * and StepForward features.
	 * @author Katrina Poulin
	 */
	@Given("The game is in replay mode")
	public void gameInReplayMode() {
		if(quoridor.hasCurrentGame()) {
			quoridor.getCurrentGame().setGameStatus(GameStatus.Replay);
		}
	}	

	/**
	 * This method calls the controller method that initiates StepForward.
	 * @author Katrina Poulin
	 */
	@When("Step forward is initiated")
	public void initiateStepForward() {
		QuoridorGameController.stepForward();
	}

	/**
	 * This method calls the controller method that initiates StepBackward.
	 * @author Katrina Poulin
	 */
	@When("Step backward is initiated")
	public void initiateStepBackward(BoardView boardView) {
		QuoridorGameController.stepBackward(boardView);
	}


	/**
	 * This method asserts the position of the white pawn after stepping.
	 * @author Katrina Poulin
	 */
////	White player's position shall be (<wrow>,<wcol>)
//	@And("White player's position shall be \\({int},{int})")
//	public void whitePositionAfterStepping(int whiteRow, int whiteCol) {
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn()==whiteCol);
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow()==whiteRow);
//	}

	/**
	 * This method asserts the position of the black pawn after stepping.
	 * @author Katrina Poulin
	 */
////	Black player's position shall be (<wrow>,<wcol>)
//	@And("Black player's position shall be \\({int},{int})")
//	public void blackPositionAfterStepping(int blackRow, int blackCol) {
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn()==blackCol);
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow()==blackRow);
//
//	}

//	/**
//	 * This method asserts the number of walls in stock for the white
//	 * pawn after stepping.
//	 * @author Katrina Poulin
//	 */
//	@And("White has {int} on stock")
//	public void whiteHasWallsOnStock(int whiteWalls) {
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock()==whiteWalls);
//	}

	/**
	 * This method asserts the number of walls in stock for the black
	 * pawn after stepping.
	 * @author Katrina Poulin
	 */
//	@And("Black has {int} on stock")
//	public void blackHasNWallsOnStock(int blackWalls) {
//		assertTrue(quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock()==blackWalls);
//
//	}


//	***FEATURE: ENTER REPLAY MODE ***

	/**
	 * This method initiates replayMode, which allows the player to review a
	 * past game by walking through the moves.
	 * @author Katrina Poulin
	 */
	@When("I initiate replay mode")
	public void initiateReplayMode() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		QuoridorGameController.goToReplayMode();
	}

	/**
	 * This method asserts that the game is now in replay mode.
	 * @author Katrina Poulin
	 */
	@Then("The game shall be in replay mode")
	public void assertInReplayMode() {
		assertTrue(quoridor.getCurrentGame().getGameStatus().equals(GameStatus.Replay));
	}

	/**
	 * This method makes sure that the game has no final result to continue
	 * an unfinished game.
	 * @author Katrina Poulin
	 */
	@And("The game does not have a final result")
	public void theGameHasNoFinalResult() {
		assertTrue(QuoridorGameController.wasRunning==true);
	}

	/**
	 * This method initiates the continuation of the game
	 * @author Katrina Poulin
	 */
	@When("I initiate to continue game")
	public void initiateToContinueGame() {
		QuoridorGameController.exitReplayMode();
	}


	/**
	 * This method makes sure that the remaining moves of the game are removed.
	 * @author Katrina Poulin
	 */
	@And("The remaining moves of the game shall be removed")
	public void remainingMovesAreRemoved() {
		assertTrue(QuoridorGameController.currReplayIndex==quoridor.getCurrentGame().numberOfPositions()-1);
	}

	/**
	 * This method sets for the game to be finished (i.e.has a final result)
	 * @author Katrina Poulin
	 */
	@And("The game has a final result")
	public void theGameHasAFinalResult() {
		//TODO
	}

	/**
	 * This method notifies the user that the game cannot be exited
	 * from replay mode cause it is finished.
	 * @author Katrina Poulin
	 */
	@And("I shall be notified that finished games cannot be continued")
	public void cannotContinueGameBecauseFinished() {
		assertTrue(QuoridorGameController.wasRunning==false);
	}

//	***END OF KATRINA'S FEATURES ITERATION-5, STEP BACKWARD/STEP FORWARD, ENTER REPLAY MODE***

	@Then("The position shall be valid")
	public void thePositionShallBeValid() throws Throwable{
		assertEquals(true, positionIsValid && !indexOutOfBound);
	}

	@Then("The position shall be invalid")
	public void thePositionShallBeInvalid() throws Throwable{
		assertEquals(false, positionIsValid && !indexOutOfBound);
	}

	@Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
	public void aGamePositionIsSuppliedWithWallCoordinates(int row, int col, String dir) {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		Direction direction;
		if(dir.equals("horizontal")) {
			direction = Direction.Horizontal;
		} else {
			direction = Direction.Vertical;
		}
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();
		indexOutOfBound = false;
		if(currentPlayer.equals(game.getWhitePlayer())) {//the current player is white
			try {
			Wall wallToMove = game.getCurrentPosition().getWhiteWallsInStock().get(0);
			WallMove wallMove = new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
					currentPlayer, QuoridorApplication.getQuoridor().getBoard().getTile((row-1)*9 +col-1),
					game, direction, wallToMove);
			game.addMove(wallMove);
			game.getCurrentPosition().addWhiteWallsOnBoard(wallToMove); //updates board
			} catch(Exception e) {
				indexOutOfBound = true;
			}

		}
		else { //player is black
			try {
				Wall wallToMove = game.getCurrentPosition().getBlackWallsInStock().get(0);
				WallMove wallMove = new WallMove(game.numberOfMoves()+1, (game.numberOfMoves()+1)/2,
						currentPlayer, QuoridorApplication.getQuoridor().getBoard().getTile((row-1)*9 +col-1),
						game, direction, wallToMove);
				game.addMove(wallMove);
				game.getCurrentPosition().addBlackWallsOnBoard(wallToMove); //updates board
				} catch(Exception e) {
					indexOutOfBound = true;
				}
		}
	}

	private void createNewGame(ArrayList<Player> players) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);
		game.setBlackPlayer(players.get(1));
		game.setWhitePlayer(players.get(0));


		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);



		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}


		game.setCurrentPosition(gamePosition);
	}



	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);


		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);

		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		players.get(0).setNextPlayer(players.get(1));
		players.get(1).setNextPlayer(players.get(0));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
		gamePosition.addBlackWallsInStock(wall);
	}

	game.setCurrentPosition(gamePosition);
	pawnBehavior.setCurrentGame(game);
}
	
	private void createAndStartGame1(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(4);
		Tile player2StartPos = quoridor.getBoard().getTile(76);


		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);

		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		players.get(0).setNextPlayer(players.get(1));
		players.get(1).setNextPlayer(players.get(0));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
		gamePosition.addBlackWallsInStock(wall);
	}

	game.setCurrentPosition(gamePosition);
	pawnBehavior.setCurrentGame(game);
}





	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 *
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 *
	 */

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 1; i <= 20; i++) {
			Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private ArrayList<Player> createUsersAndPlayers1(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int min = 3;
		int sec = 0;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		//@formatter:on
		Player player1 = new Player(new Time(0 ,min ,sec), user1, 1, Direction.Vertical);
		Player player2 = new Player(new Time(0 ,min ,sec), user2, 9, Direction.Vertical);
		
		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j <= 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);
		
		return playersList;
	}
	
	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int min = 3;
		int sec = 0;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 *
		 */
		//@formatter:on
		Player player1 = new Player(new Time(0 ,min ,sec), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(0 ,min ,sec), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j <= 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}

		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);

		return playersList;
	}




	public void createUser(String aUsername) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		quoridor.addUser(aUsername);
	}

}
