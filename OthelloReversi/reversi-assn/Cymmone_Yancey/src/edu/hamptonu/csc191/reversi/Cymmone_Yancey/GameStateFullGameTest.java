package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Plays a few full games and checks the final game state.
 *
 * All of these games come from http://www.samsoft.org.uk/reversi/.
 *
 * These are good tests for catching bugs, but they're not easy to debug.
 * They're more like "integration tests" than "unit tests".
 */
public class GameStateFullGameTest {
  /**
   * Plays out the list of moves and returns the resulting game state.
   */
  private GameState playGame(List<String> moves) {
    GameState g = new GameState();
    for (int i = 0; i < moves.size(); i++) {
      assertFalse(g.gameOver());

      Location loc = new Location(moves.get(i));
      Color toMove = g.toMove();
      assertTrue(g.isLegalMove(loc, toMove));
      g.recordMove(loc, toMove);

      // Uncommenting these two lines can be helpful for debugging.
      // System.out.println(moves.get(i));
      // System.out.println(g.toString());
    }
    return g;
  }

  /**
   * Runs the "classic health" opening and check that the board looks correct.
   * There's no significance to this opening; I just picked it at random from
   * the list.
   */
  @Test
  void classicHealthOpening() {
    GameState g = playGame(List.of("c4", "c3", "d3", "c5", "b4", "d2", "c2"));
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - B W - - - -\n"
                     + "3 - - B B - - - -\n"
                     + "4 - B B W B - - -\n"
                     + "5 - - W W W - - -\n"
                     + "6 - - - - - - - -\n"
                     + "7 - - - - - - - -\n"
                     + "8 - - - - - - - -\n"
                     + "WHITE to move."
                     + "",
                 g.toString());
  }

  /**
   * The given sequence of moves is a draw; see
   * "Interesting Games" -> "Perfect Draw #1".
   */
  @Test
  void playPerfectDraw() {
    GameState g = playGame(
        List.of("c4", "e3", "f6", "e6", "f5", "c5", "f4", "g6", "f7", "g5",
                "d6", "d3", "f3", "c3", "h4", "h5", "g4", "h3", "e2", "f2",
                "d2", "d1", "g3", "c1", "b6", "c2", "b4", "e8", "f1", "e7",
                "b3", "e1", "b1", "c7", "g1", "c6", "d8", "b5", "a6", "g2",
                "d7", "b7", "f8", "a3", "a5", "a4", "a2", "g7", "h1", "h2",
                "g8", "h7", "h8", "h6", "a8", "a7", "b8", "c8", "a1", "b2"));
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 W B B B B B B B\n"
                     + "2 W W W W W W B W\n"
                     + "3 W B B W W B B W\n"
                     + "4 W B B B W W B W\n"
                     + "5 W B W B B W W W\n"
                     + "6 W B W B B B W W\n"
                     + "7 W B W W W W B W\n"
                     + "8 B B W B B B B B\n"
                     + "Game over; DRAW.",
                 g.toString());
    assertTrue(g.gameOver());
    assertEquals(Color.NONE, g.winner());
  }

  /**
   * The given sequence of moves ends with no white pieces remaining, a
   * "wipeout".  See "Interesting Games" -> "Blackmur 64-0 MacGuire".
   *
   * This game has at least one case where black moves multiple times in a row
   * (because white has no legal moves).
   */
  @Test
  void playWipeout() {
    GameState g = playGame(
        List.of("c4", "e3", "f6", "e6", "f5", "c5", "f4", "g6", "f7", "e8",
                "h6", "c3", "b4", "f3", "g5", "d6", "g4", "g3", "b5", "d3",
                "f8", "h4", "d8", "b3", "h3", "a4", "h5", "a6", "a5", "b6",
                "e7", "h7", "h8", "d7", "c8", "g8", "g7", "b8", "a8", "a7",
                "c6", "b7", "c7", "h2", "g2", "f2", "e2", "a3"));
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - B B B B\n"
                     + "3 B B B B B B B B\n"
                     + "4 B B B B B B B B\n"
                     + "5 B B B B B B B B\n"
                     + "6 B B B B B B B B\n"
                     + "7 B B B B B B B B\n"
                     + "8 B B B B B B B B\n"
                     + "Game over; BLACK wins.",
                 g.toString());
    assertTrue(g.gameOver());
    assertEquals(Color.BLACK, g.winner());
  }

  /**
   * From "Interesting Games" -> "MacGuire 23-41 Pridmore".
   */
  @Test
  void playMacGuirePridmore() {
    GameState g = playGame(
        List.of("c4", "e3", "f6", "e6", "f5", "c5", "f4", "g6", "f7", "d6",
                "e7", "f3", "g5", "g4", "g3", "d7", "h5", "c6", "h4", "d3",
                "b5", "h6", "c2", "d2", "c8", "b6", "b4", "c3", "f2", "h3",
                "b3", "c7", "e1", "a6", "a5", "f1", "d8", "d1", "e2", "g2",
                "a7", "c1", "g1", "a4", "a3", "h1", "h2", "b2", "g7", "h8",
                "g8", "h7", "a1", "a2", "b1", "f8", "b7", "a8", "e8", "b8"));
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 B B W W W W W W\n"
                     + "2 W B B W B B W B\n"
                     + "3 W B B B B W B W\n"
                     + "4 W B W B W W B W\n"
                     + "5 W B B W B W B W\n"
                     + "6 W B W W B W W W\n"
                     + "7 W W W B B W W W\n"
                     + "8 W W W W W W W W\n"
                     + "Game over; WHITE wins.",
                 g.toString());
  }

  @Test
  void playVecchiMacGuire() {
    GameState g = playGame(
        List.of("f5", "d6", "c3", "d3", "c4", "f4", "c5", "b3", "d2", "e3",
                "c2", "c1", "e1", "f6", "f3", "e6", "c6", "e2", "b4", "a4",
                "a3", "a2", "g4", "g5", "f1", "h4", "d7", "f2", "g1", "b2",
                "e7", "d1", "a1", "b1", "a5", "h1", "g6", "f7", "h5", "e8",
                "h3", "h6", "h7", "g3", "d8", "c8", "g2", "h2", "b5", "c7",
                "b6", "h8", "g7", "g8", "b7", "a7", "a8", "b8", "a6", "f8"));
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 B W W W W W W W\n"
                     + "2 B B W B B W B W\n"
                     + "3 B W W B W B W W\n"
                     + "4 B B W W B W W W\n"
                     + "5 B B W W W B W W\n"
                     + "6 B B B B W B W W\n"
                     + "7 B W W W B B W W\n"
                     + "8 B B B B B B W W\n"
                     + "Game over; WHITE wins.",
                 g.toString());
  }
}
