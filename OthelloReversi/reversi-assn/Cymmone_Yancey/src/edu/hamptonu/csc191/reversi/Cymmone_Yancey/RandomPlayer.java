package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.ColorUtil;
import edu.hamptonu.csc191.reversi.Location;
import edu.hamptonu.csc191.reversi.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;

// This lets us refer to plain "BOARD_SIZE" when we mean Location.BOARD_SIZE.

/**
 * A player that makes random (legal!) moves.
 */
public class RandomPlayer implements Player {
  private Color color;
  private GameState gameState;
  private Random rand;

  public RandomPlayer(Color color) {
    this.color = color;
    gameState = new GameState();
    rand = new Random();
  }

  @Override
  public Location nextMove(Instant deadline) {
    // Suggested implementation strategy:
    //
    // - Create an empty list to hold all legal moves.
    List<Location> legalMoves = new ArrayList<>();
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Location loc = new Location(row, col);
        if (gameState.isLegalMove(loc, this.color)){
          legalMoves.add(loc);
        }
      }
    }
    // - Choose a move at random from the list with something like
    //   legalMoves.get(rand.nextInt(legalMoves.size()))
    Location move = legalMoves.get(this.rand.nextInt(legalMoves.size()));
    // - Call gameState.recordMove to update this player's game board with our
    //   choice of move.
    gameState.recordMove(move, this.color);
    // - Return the move we chose.
    return move;
    // TODO: Fill this in!
  }

  @Override
  public void observeOpponentMove(Location move) {
    gameState.recordMove(move, ColorUtil.oppositeColor(color));
  }

  @Override
  public boolean isHuman() {
    return false;
  }
}
