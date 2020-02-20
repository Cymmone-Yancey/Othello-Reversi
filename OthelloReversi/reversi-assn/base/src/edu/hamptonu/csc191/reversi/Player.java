package edu.hamptonu.csc191.reversi;

import java.time.Instant;

/**
 * Represents an agent that can play reversi.
 *
 * Classes which implement this interface may prompt the user for the next move,
 * or they may somehow compute the next move.
 *
 * Don't change this interface, as it will be used by Players that you didn't
 * write, and they all need to have the same thing here.
 */
public interface Player {
  /**
   * Queries the Player for its next move.
   *
   * Players are encouraged to log debugging info with System.out.println.
   *
   * @param deadline point in time by which the move must be provided,
   *     otherwise the Player forfeits the game.
   */
  Location nextMove(Instant deadline);

  /**
   * Informs the Player of a move made by its opponent.  The player can use this
   * opportunity to update its state.
   */
  void observeOpponentMove(Location move);

  /**
   * Is this player actually just asking a human for moves?  If so, game drivers
   * shouldn't enforce deadlines against it.
   */
  boolean isHuman();
}
