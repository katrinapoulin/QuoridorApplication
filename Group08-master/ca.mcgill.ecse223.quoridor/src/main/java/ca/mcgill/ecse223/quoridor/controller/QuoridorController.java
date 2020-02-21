package ca.mcgill.ecse223.quoridor.controller;

import java.sql.Time;

import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public interface QuoridorController {
	

	
	
	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @param game : game to initialize
	 * This method will initialize a new Game once fully implemented
	 */
	public void initializeGame() throws InvalidInputException;
		
	
	/**
<<<<<<< HEAD
=======
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @param username : username to set to white player
	 */
	//public void setWhitePlayerUsername(String username) throws InvalidInputException;


	/**
>>>>>>> c67e67cce2ee78a6f2604b2b6fd3de3125dab096
	 * This method moves the wall in the player's hand in a given direction
	 * 
	 * @author Katrina Poulin
	 * @param side The side towards which the player wishes to move the wall
	 */
	
	public boolean grabWall() throws InvalidInputException;
	
	public boolean flipWall(WallMove myWallMove) throws InvalidInputException;
	
	public void notifyNoWalls();

	public void notifyIllegalMove();
	
	public void notifyMoveInvalid();
	
	public boolean moveWallInDirection(String side) throws InvalidInputException;
	/**
	 * This method takes a wallMove and turns it into a permanent wall on the board (i.e. position of the wall is settled)
	 * 
	 * @author Katrina Poulin
	 * @param myWallMove The current wall move of the current player
	 * @return boolean Wall has been released
	 */
	public boolean releaseWall(WallMove myWallMove) throws InvalidInputException;
	
	/**
	 * This method checks if a specific wall space on the board is occupied
	 * 
	 * @author Katrina Poulin
	 * @param dir The direction of the wall space
	 * @param row The row of the wall space
	 * @param col The column of the wall space
	 * @return true if the space is occupied; false if the space is free
	 */
	public boolean wallMoveIsValid(WallMove myMove) throws InvalidInputException;
	
	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @feature StartNewGame
	 * @param username : The username to set to black player
	 */
	public void setBlackPlayerUsername(String username) throws InvalidInputException ;

	


	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @Feature StartNewGame
	 * @param time : the total thinking time set by user
	 * This method will start the clock of a running game once fully implemented
	 */
	public void startClock(Time time) throws InvalidInputException;

	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @Feature ProvideSelectUserName
	 * @param username : Existing username that will 
	 * This method will allow a player to select an existing username and
	 * assign it to that player for the current game
	 */
	public void selectExistingUsername(String username) throws InvalidInputException;
	/**
	 * @author BenjaminEmiliani
	 * @throws InvalidInputException
	 * @Feature ProvideSelectUserName
	 * This method will allow a player to enter a new user name and assign it to that player for the current game
	 */
	

	/**
	 * @author BenjaminEmiliani
	 * @throws UnsupportedOperationException
	 * @Feature ProvideSelectUserName
	 * This method will allow a player to enter a new user name and assign it to that player for the current game
	 */
	public void provideNewUsername(String username) throws InvalidInputException;
			
	public boolean validatePosition(GamePosition gamePosition) throws InvalidInputException;





	


	
}



