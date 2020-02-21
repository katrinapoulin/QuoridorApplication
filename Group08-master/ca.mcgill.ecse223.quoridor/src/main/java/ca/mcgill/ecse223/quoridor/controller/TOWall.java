package ca.mcgill.ecse223.quoridor.controller;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 15 "TOQuoridor.ump"
public class TOWall
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Direction { Vertical, Horizontal }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOWall Attributes
  private Direction direction;
  private int id;
  private String user;
  private int row;
  private int col;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOWall(Direction aDirection, int aId, String aUser, int aRow, int aCol)
  {
    direction = aDirection;
    id = aId;
    user = aUser;
    row = aRow;
    col = aCol;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDirection(Direction aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setUser(String aUser)
  {
    boolean wasSet = false;
    user = aUser;
    wasSet = true;
    return wasSet;
  }

  public boolean setRow(int aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setCol(int aCol)
  {
    boolean wasSet = false;
    col = aCol;
    wasSet = true;
    return wasSet;
  }

  public Direction getDirection()
  {
    return direction;
  }

  public int getId()
  {
    return id;
  }

  public String getUser()
  {
    return user;
  }

  public int getRow()
  {
    return row;
  }

  public int getCol()
  {
    return col;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "user" + ":" + getUser()+ "," +
            "row" + ":" + getRow()+ "," +
            "col" + ":" + getCol()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "direction" + "=" + (getDirection() != null ? !getDirection().equals(this)  ? getDirection().toString().replaceAll("  ","    ") : "this" : "null");
  }
}