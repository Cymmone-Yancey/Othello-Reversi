package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.ColorUtil;
import edu.hamptonu.csc191.reversi.Location;
import edu.hamptonu.csc191.reversi.Player;

import java.time.Instant;
import java.util.Scanner;

/**
 * An agent that plays reversi by reading moves from the console.
 */
public class ConsolePlayer implements Player {
  private Color color;
  private GameState gameState;
  private Scanner keyboard;

  public ConsolePlayer(Color color) {
    this.color = color;
    gameState = new GameState();
    keyboard = new Scanner(System.in);
  }

  /**
   * Prompts the user for the location they wish to move to, and once the user
   * provides a valid location, returns it.
   *
   * @param deadline point in time by which the move must be provided.
   * @return the chosen move
   */
  @Override
  public Location nextMove(Instant deadline) {
    // Implementation steps:
    //
    //  - Prompt the user for the move using keyboard.nextLine().
    //  - Convert the move to a Location by way of new Location().  Make
    //    sure you handle the possibility that the user's input was invalid!
    //    Something like:
    //
    //      System.out.print("Enter move for color " + color + ": ");
    //      String move = keyboard.nextLine();
    //      Location loc;
    //      try {
    //        loc = new Location(move);
    //      } catch (IllegalArgumentException e) {
    //        console.println("Bad move " + move + ": " + e.getMessage());
    //      }
    Location myMove = null;
    do {
      System.out.print("Enter desired location (e.g. \"A4\"): ");
      String input = keyboard.nextLine();
      try {
        myMove = new Location(input);
      } catch (IllegalArgumentException e) {
        System.out.println("Bad move " + input + ": " + e.getMessage());
      }
    } while (myMove==null||!gameState.isLegalMove(myMove, this.color));
    //  - Check that the move was valid by calling gameState.isLegalMove.
    //  - If the move is invalid, prompt for another move.  If it's valid,
    //    record it in the game state by calling
    //      gameState.recordMove(loc, color)
    //    and then return it.
    gameState.recordMove(myMove, this.color);
    return myMove; // TODO: Implement me!
  }

  /**
   * The game driver calls this to let us know where our opponent moved.
   *
   * @param move our opponent's move.
   */
  @Override
  public void observeOpponentMove(Location move) {
    // Record the move in our game state so we know what the board looks like.
    // This is important so that later when we get a move from a human, we can
    // check whether it's valid.
    gameState.recordMove(move, ColorUtil.oppositeColor(color));
  }

  /**
   * Tell game drivers that we interact with a human, so that it doesn't
   * strictly enforce deadlines against us.
   */
  @Override
  public boolean isHuman() {
    return true;
  }
}
