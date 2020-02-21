package ca.mcgill.ecse223.quoridor.controller;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.sql.Time;

// line 6 "TOQuoridor.ump"
public class TOPlayer
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOPlayer Attributes
  private String user;
  private Time remainingTime;
  private int remainingWalls;
  private int row;
  private int col;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOPlayer(String aUser, Time aRemainingTime, int aRemainingWalls, int aRow, int aCol)
  {
    user = aUser;
    remainingTime = aRemainingTime;
    remainingWalls = aRemainingWalls;
    row = aRow;
    col = aCol;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUser(String aUser)
  {
    boolean wasSet = false;
    user = aUser;
    wasSet = true;
    return wasSet;
  }

  public boolean setRemainingTime(Time aRemainingTime)
  {
    boolean wasSet = false;
    remainingTime = aRemainingTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setRemainingWalls(int aRemainingWalls)
  {
    boolean wasSet = false;
    remainingWalls = aRemainingWalls;
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

  public String getUser()
  {
    return user;
  }

  public Time getRemainingTime()
  {
    return remainingTime;
  }

  public int getRemainingWalls()
  {
    return remainingWalls;
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
            "user" + ":" + getUser()+ "," +
            "remainingWalls" + ":" + getRemainingWalls()+ "," +
            "row" + ":" + getRow()+ "," +
            "col" + ":" + getCol()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "remainingTime" + "=" + (getRemainingTime() != null ? !getRemainingTime().equals(this)  ? getRemainingTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}