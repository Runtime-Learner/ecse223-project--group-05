/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 15 "../Model.ump"
// line 29 "../Model.ump"
// line 66 "../Model.ump"
public class Pawn extends BoardItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private String lastPosition;

  //Pawn Associations
  private User player;
  private Game game;
  private List<Wall> walls;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(String aCurrentPosition, String aLastPosition, User aPlayer, Game aGame)
  {
    super(aCurrentPosition);
    lastPosition = aLastPosition;
    boolean didAddPlayer = setPlayer(aPlayer);
    if (!didAddPlayer)
    {
      throw new RuntimeException("Unable to create pawn due to player");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    walls = new ArrayList<Wall>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLastPosition(String aLastPosition)
  {
    boolean wasSet = false;
    lastPosition = aLastPosition;
    wasSet = true;
    return wasSet;
  }

  public String getLastPosition()
  {
    return lastPosition;
  }
  /* Code from template association_GetOne */
  public User getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public Wall getWall(int index)
  {
    Wall aWall = walls.get(index);
    return aWall;
  }

  public List<Wall> getWalls()
  {
    List<Wall> newWalls = Collections.unmodifiableList(walls);
    return newWalls;
  }

  public int numberOfWalls()
  {
    int number = walls.size();
    return number;
  }

  public boolean hasWalls()
  {
    boolean has = walls.size() > 0;
    return has;
  }

  public int indexOfWall(Wall aWall)
  {
    int index = walls.indexOf(aWall);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPlayer(User aPlayer)
  {
    boolean wasSet = false;
    if (aPlayer == null)
    {
      return wasSet;
    }

    User existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePawn(this);
    }
    player.addPawn(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to pawn
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (4)
    if (aGame.numberOfPawn() >= Game.maximumNumberOfPawn())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePawn(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPawn(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfWallsValid()
  {
    boolean isValid = numberOfWalls() >= minimumNumberOfWalls() && numberOfWalls() <= maximumNumberOfWalls();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWalls()
  {
    return 5;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWalls()
  {
    return 10;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Wall addWall(String aCurrentPosition, boolean aIsAvailable)
  {
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return null;
    }
    else
    {
      return new Wall(aCurrentPosition, aIsAvailable, this);
    }
  }

  public boolean addWall(Wall aWall)
  {
    boolean wasAdded = false;
    if (walls.contains(aWall)) { return false; }
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return wasAdded;
    }

    Pawn existingPawn = aWall.getPawn();
    boolean isNewPawn = existingPawn != null && !this.equals(existingPawn);

    if (isNewPawn && existingPawn.numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasAdded;
    }

    if (isNewPawn)
    {
      aWall.setPawn(this);
    }
    else
    {
      walls.add(aWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWall(Wall aWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aWall, as it must always have a pawn
    if (this.equals(aWall.getPawn()))
    {
      return wasRemoved;
    }

    //pawn already at minimum (5)
    if (numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasRemoved;
    }
    walls.remove(aWall);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWallAt(Wall aWall, int index)
  {  
    boolean wasAdded = false;
    if(addWall(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWallAt(Wall aWall, int index)
  {
    boolean wasAdded = false;
    if(walls.contains(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWallAt(aWall, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    User placeholderPlayer = player;
    this.player = null;
    if(placeholderPlayer != null)
    {
      placeholderPlayer.removePawn(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePawn(this);
    }
    for(int i=walls.size(); i > 0; i--)
    {
      Wall aWall = walls.get(i - 1);
      aWall.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "lastPosition" + ":" + getLastPosition()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}