/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse223.quoridor.model.*;

// line 7 "../../../../../PawnStateMachine.ump"
public class PawnBehavior
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehavior State Machines
  public enum PawnSM { MyMove, PawnInHand }
  private PawnSM pawnSM;

  //PawnBehavior Associations
  private Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehavior()
  {
    setPawnSM(PawnSM.MyMove);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public boolean clickPawn()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case MyMove:
        setPawnSM(PawnSM.PawnInHand);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean illegalMove()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        setPawnSM(PawnSM.MyMove);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelPawnMove()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        setPawnSM(PawnSM.MyMove);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean stepPawnEast()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalStep(MoveDirection.East))
        {
        // line 26 "../../../../../PawnStateMachine.ump"
          stepPawn(MoveDirection.East);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnEast()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.East))
        {
        // line 27 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.East);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnEastNorth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.East,MoveDirection.North))
        {
        // line 28 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.East, MoveDirection.North);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnEastSouth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.East,MoveDirection.South))
        {
        // line 29 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.East, MoveDirection.South);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean stepPawnWest()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalStep(MoveDirection.West))
        {
        // line 31 "../../../../../PawnStateMachine.ump"
          stepPawn(MoveDirection.West);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnWest()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.West))
        {
        // line 32 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.West);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnWestNorth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.West,MoveDirection.North))
        {
        // line 33 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.West, MoveDirection.North);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnWestSouth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.West,MoveDirection.South))
        {
        // line 34 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.West, MoveDirection.South);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean stepPawnNorth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalStep(MoveDirection.North))
        {
        // line 37 "../../../../../PawnStateMachine.ump"
          stepPawn(MoveDirection.North);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnNorth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.North))
        {
        // line 38 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.North);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnNorthEast()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.North,MoveDirection.East))
        {
        // line 39 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.North, MoveDirection.East);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnNorthWest()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.North,MoveDirection.West))
        {
        // line 40 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.North, MoveDirection.West);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean stepPawnSouth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalStep(MoveDirection.South))
        {
        // line 43 "../../../../../PawnStateMachine.ump"
          stepPawn(MoveDirection.South);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnSouth()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.South))
        {
        // line 44 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.South);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnSouthEast()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.South,MoveDirection.East))
        {
        // line 45 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.South, MoveDirection.East);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jumpPawnSouthWest()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PawnInHand:
        if (isLegalJump(MoveDirection.South,MoveDirection.West))
        {
        // line 46 "../../../../../PawnStateMachine.ump"
          jumpPawn(MoveDirection.South, MoveDirection.West);
          setPawnSM(PawnSM.MyMove);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }

  public boolean hasCurrentGame()
  {
    boolean has = currentGame != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentGame(Game aNewCurrentGame)
  {
    boolean wasSet = false;
    currentGame = aNewCurrentGame;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPlayer(Player aNewPlayer)
  {
    boolean wasSet = false;
    player = aNewPlayer;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    currentGame = null;
    player = null;
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 56 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnRow(){
    int row;
		if(currentGame.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			row = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
		} else {
			row = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
		}
	    return row;
  }


  /**
   * Returns the current column number of the pawn
   */
  // line 67 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnColumn(){
    int col;
		if(currentGame.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			col = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			col = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
		}
	    return col;
  }

  // line 77 "../../../../../PawnStateMachine.ump"
  public ArrayList<Wall> getWalls(){
    List<Wall> whiteWalls = currentGame.getCurrentPosition().getWhiteWallsOnBoard(); //get all the white walls
		ArrayList<Wall> walls = new ArrayList<Wall>();
		List<Wall> blackWalls = currentGame.getCurrentPosition().getBlackWallsOnBoard(); //add all the black walls

		for(Wall wall : whiteWalls) {
			walls.add(wall);
		}
		
		
		for(Wall wall : blackWalls) {
			walls.add(wall);
		}
		
		return walls;
  }

  // line 96 "../../../../../PawnStateMachine.ump"
   public boolean isLegalStep(MoveDirection dir){
    int rowToCheck = getCurrentPawnRow();
		int colToCheck = getCurrentPawnColumn();
		int tileIndex;
		Tile tile;
		
		
		if(dir == null) {
			return false;
		}
		
		if(dir == MoveDirection.East) {
			colToCheck++;
		}
		if(dir == MoveDirection.West) {
			colToCheck--;
		}
		if(dir == MoveDirection.North) {
			rowToCheck--;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
		}
		
		if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
			return false;
		}
		
		if(currentGame.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			if(currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() == colToCheck && 
					currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() == rowToCheck) {
				return false;
			}
		} else {
			if(currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() == colToCheck && 
					currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() == rowToCheck) {
				return false;
			}	
		}
		
		ArrayList<Wall> walls = getWalls();
		rowToCheck = getCurrentPawnRow();
		colToCheck = getCurrentPawnColumn();
		
		int rowToCheck2 = getCurrentPawnRow();
		int colToCheck2 = getCurrentPawnColumn();
		
		if(dir == MoveDirection.East) {
			colToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		if(dir == MoveDirection.West) {
			rowToCheck2++;
		}
		if(dir == MoveDirection.North) {
			colToCheck2++;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		
		for(Wall wall: walls) {
			if(dir == MoveDirection.North || dir == MoveDirection.South) {
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
				if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
				(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
				return false;
				}		
			} 
			} else { //east or west
				if(wall.getMove().getWallDirection() == Direction.Vertical) {
					if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
					(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
					return false;
					}
				}
			}
			
		}
		return true;
  }


  /**
   * 
   * Returns if it is legal to jump in the given direction
   * line 169 "../../../../../PawnStateMachine.ump"
   */
  // line 186 "../../../../../PawnStateMachine.ump"
   public boolean isLegalJump(MoveDirection dir){
    int rowToCheck = getCurrentPawnRow();
	  int colToCheck = getCurrentPawnColumn();
	  int tileIndex;
	  Tile tile;
	  boolean resume = false;
		
		if(dir == null) {
			return false;
		}
		
		int rowToCheck2 = getCurrentPawnRow();
		int colToCheck2 = getCurrentPawnColumn();
		ArrayList<Wall> walls = getWalls();
		
		
		if(dir == MoveDirection.East) {
			colToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		if(dir == MoveDirection.West) {
			rowToCheck2++;
		}
		if(dir == MoveDirection.North) {
			colToCheck2++;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		
		if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
			return false;
		}
		if(colToCheck2 > 9 || colToCheck2 < 1 || rowToCheck2 < 1 || rowToCheck2>9) { //outofbounds verify with katrina
			return false;
		}
		for(Wall wall: walls) {
			if(dir == MoveDirection.North || dir == MoveDirection.South) {
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
				if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
				(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
				return false;
				}		
			} 
			} else { //east or west
				if(wall.getMove().getWallDirection() == Direction.Vertical) {
					if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
					(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
					return false;
					}
				}
			}
			
		}
		
		
		rowToCheck = getCurrentPawnRow();
	  	colToCheck = getCurrentPawnColumn();
	  	
		if(dir == MoveDirection.East) {
			colToCheck++;
		}
		if(dir == MoveDirection.West) {
			colToCheck--;
		}
		if(dir == MoveDirection.North) {
			rowToCheck--;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
		}
		
		if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
			return false;
		}
		
		if(currentGame.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
			if(currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() == colToCheck && 
					currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() == rowToCheck) {
				resume = true;
			}
		} else {
			if(currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() == colToCheck && 
					currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() == rowToCheck) {
				resume = true;
			}	
		}
		
		if(resume == false) {
			return false;
		}
		
		
		rowToCheck2 = rowToCheck;
		colToCheck2 = colToCheck;
		
		if(dir == MoveDirection.East) {
			colToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		if(dir == MoveDirection.West) {
			rowToCheck2++;
		}
		if(dir == MoveDirection.North) {
			colToCheck2++;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		
		for(Wall wall: walls) {
			if(dir == MoveDirection.North || dir == MoveDirection.South) {
				if(wall.getMove().getWallDirection() == Direction.Horizontal) 	{
				if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
				(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
				return false;
				}		
			} 
			} else { //east or west
				if(wall.getMove().getWallDirection() == Direction.Vertical) {
					if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
					(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
					return false;
					}
				}
			}
			
		}
		
		rowToCheck = getCurrentPawnRow();
		colToCheck = getCurrentPawnColumn();
		
		if(dir == MoveDirection.East) {
			colToCheck+=2;
		}
		if(dir == MoveDirection.West) {
			colToCheck-=2;
		}
		if(dir == MoveDirection.North) {
			rowToCheck-=2;
		}
		if(dir == MoveDirection.South) {
			rowToCheck+=2;
		}
		
		if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
			return false;
		}
		//checking for out of bounds
		
		return true;
  }


  /**
   * 
   * line 242 "../../../../../PawnStateMachine.ump"
   * line 276 "../../../../../PawnStateMachine.ump"
   */
  // line 350 "../../../../../PawnStateMachine.ump"
   public boolean isLegalJump(MoveDirection dir, MoveDirection dir2){
    int rowToCheck = getCurrentPawnRow();
		  int colToCheck = getCurrentPawnColumn();
		  int tileIndex;
		  Tile tile;
		  boolean resume = false;
			
		int rowToCheck2 = getCurrentPawnRow();
		int colToCheck2 = getCurrentPawnColumn();
		ArrayList<Wall> walls = getWalls();
		
		
		if(dir == MoveDirection.East) {
			colToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		if(dir == MoveDirection.West) {
			rowToCheck2++;
		}
		if(dir == MoveDirection.North) {
			colToCheck2++;
		}
		if(dir == MoveDirection.South) {
			rowToCheck++;
			rowToCheck2++;
			colToCheck2++;
		}
		
		if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
			return false;
		}
		if(colToCheck2 > 9 || colToCheck2 < 1 || rowToCheck2 < 1 || rowToCheck2>9) { //outofbounds verify with katrina
			return false;
		}
		for(Wall wall: walls) {
			if(dir == MoveDirection.North || dir == MoveDirection.South) {
				if(wall.getMove().getWallDirection() == Direction.Horizontal) {
				if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
				(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
				return false;
				}		
			} 
			} else { //east or west
				if(wall.getMove().getWallDirection() == Direction.Vertical) {
					if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
					(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
					return false;
					}
				}
			}
			
		}
			
			
			
			
			rowToCheck = getCurrentPawnRow();
		  colToCheck = getCurrentPawnColumn();
			
			
			
			
			if(dir == null) {
				return false;
			}
			
			if(dir == MoveDirection.East) {
				colToCheck++;
			}
			if(dir == MoveDirection.West) {
				colToCheck--;
			}
			if(dir == MoveDirection.North) {
				rowToCheck--;
			}
			if(dir == MoveDirection.South) {
				rowToCheck++;
			}
			
			if(currentGame.getCurrentPosition().getPlayerToMove().hasGameAsWhite() == true) {
				if(currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() == colToCheck && 
						currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() == rowToCheck) {
					resume = true;
				}
			} else {
				if(currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() == colToCheck && 
						currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() == rowToCheck) {
					resume = true;
				}	
			}
			
			if(resume == false) {
			return resume;
			}
			//resume is true
			
			
			
			
			
			
			
			if(isLegalJump(dir)) {//it's legal to just jump in that direction
				return false; //why are you jumping in two directions
			}
			
			// it's illegal to just jump east: factor in second direction
			rowToCheck2 = rowToCheck;
			colToCheck2 = colToCheck;
			
			if(dir2 == MoveDirection.East) {
				colToCheck++;
				rowToCheck2++;
				colToCheck2++;
			}
			if(dir2 == MoveDirection.West) {
				rowToCheck2++;
			}
			if(dir2 == MoveDirection.North) {
				colToCheck2++;
			}
			if(dir2 == MoveDirection.South) {
				rowToCheck++;
				rowToCheck2++;
				colToCheck2++;
			}
			
			for(Wall wall: walls) {
				if(dir2 == MoveDirection.North || dir2 == MoveDirection.South) {
					if(wall.getMove().getWallDirection() == Direction.Horizontal) {
					if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
					(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
					return false;
					}		
				} 
				} else { //east or west
					if(wall.getMove().getWallDirection() == Direction.Vertical) {
						if((wall.getMove().getTargetTile().getRow() == rowToCheck && wall.getMove().getTargetTile().getColumn() == colToCheck) ||
						(wall.getMove().getTargetTile().getRow() == rowToCheck2 && wall.getMove().getTargetTile().getColumn() == colToCheck2)) {
						return false;
						}
					}
				}
				
			}
			
			rowToCheck = getCurrentPawnRow();
			colToCheck = getCurrentPawnColumn();
			
			if(dir == MoveDirection.East) {
				colToCheck++;
			}
			if(dir == MoveDirection.West) {
				colToCheck--;
			}
			if(dir == MoveDirection.North) {
				rowToCheck--;
			}
			if(dir == MoveDirection.South) {
				rowToCheck++;
			}
			
			if(dir2 == MoveDirection.East) {
				colToCheck++;
			}
			if(dir2 == MoveDirection.West) {
				colToCheck--;
			}
			if(dir2 == MoveDirection.North) {
				rowToCheck--;
			}
			if(dir2 == MoveDirection.South) {
				rowToCheck++;
			}
			
			if(colToCheck > 9 || colToCheck < 1 || rowToCheck < 1 || rowToCheck>9) { //outofbounds verify with katrina
				return false;
			}
			
			return true;
  }

  // line 534 "../../../../../PawnStateMachine.ump"
   private void stepPawn(MoveDirection dir){
    QuoridorGameController.stepPawn(dir);
  }

  // line 538 "../../../../../PawnStateMachine.ump"
   private void jumpPawn(MoveDirection dir){
    QuoridorGameController.jumpPawn(dir);
  }

  // line 541 "../../../../../PawnStateMachine.ump"
   private void jumpPawn(MoveDirection dir, MoveDirection dir2){
    QuoridorGameController.jumpPawn(dir, dir2);
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 547 "../../../../../PawnStateMachine.ump"
  public enum MoveDirection 
  {
    East, South, West, North;
  }

  
}