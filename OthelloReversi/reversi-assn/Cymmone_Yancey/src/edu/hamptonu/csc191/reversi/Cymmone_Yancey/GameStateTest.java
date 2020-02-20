package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the GameState class.
 *
 * It's possible that you'll need to add additional tests here, especially if
 * you see your player failing some of the tests in GameStateFullGameTest.
 */
class GameStateTest {
  /**
   * Check that board looks correct immediately after construction.
   */
  @Test
  void initialState() {
    GameState g = new GameState();

    // Check the four squares in the middle.
    int mid = BOARD_SIZE / 2;
    Assertions.assertEquals(Color.WHITE, g.get(new Location(mid - 1, mid - 1)));
    assertEquals(Color.WHITE, g.get(new Location(mid, mid)));
    assertEquals(Color.BLACK, g.get(new Location(mid - 1, mid)));
    assertEquals(Color.BLACK, g.get(new Location(mid, mid - 1)));

    // Check other squares.
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row != mid && row != mid - 1 && col != mid && col != mid - 1) {
          assertEquals(Color.NONE, g.get(new Location(row, col)),
                       String.format("(%d, %d)", row, col));
        }
      }
    }

    // Black moves first, and the game should not be over before it's begun!
    assertEquals(Color.BLACK, g.toMove());
    assertFalse(g.gameOver());

    // Since the game isn't over, it's an error to ask who's won.
    assertThrows(IllegalStateException.class, () -> { g.winner(); });

    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - - - - - - -\n"
                     + "4 - - - W B - - -\n"
                     + "5 - - - B W - - -\n"
                     + "6 - - - - - - - -\n"
                     + "7 - - - - - - - -\n"
                     + "8 - - - - - - - -\n"
                     + "BLACK to move.",
                 g.toString());
  }

  @Test
  void recordMoveInvalidInitialState() {
    GameState g = new GameState();

    // Black moves first, so any move for white initially is illegal.
    assertThrows(IllegalArgumentException.class, () -> {
      g.recordMove(new Location("F4"), Color.WHITE);
    });

    // Can't move to a square that's occupied.
    assertThrows(IllegalArgumentException.class, () -> {
      g.recordMove(new Location("e4"), Color.BLACK);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      g.recordMove(new Location("d4"), Color.BLACK);
    });

    // Can't move to d6 or f4 in the initial state; that doesn't result in
    // capturing any of white's pieces.
    assertThrows(IllegalArgumentException.class, () -> {
      g.recordMove(new Location("d6"), Color.BLACK);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      g.recordMove(new Location("f4"), Color.BLACK);
    });
  }

  /**
   * Send an input through fromString and get it out via toString.
   */
  @Test
  void fromStringToString() {
    GameState g = GameState.fromString(""
                                       + "--------"
                                       + "--------"
                                       + "----W---"
                                       + "---WW---"
                                       + "---BW---"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "BLACK");
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - - - W - - -\n"
                     + "4 - - - W W - - -\n"
                     + "5 - - - B W - - -\n"
                     + "6 - - - - - - - -\n"
                     + "7 - - - - - - - -\n"
                     + "8 - - - - - - - -\n"
                     + "BLACK to move.",
                 g.toString());
  }

  /**
   * Check the winner() method in the three possible cases.
   */
  @Test
  void winnerBlack() {
    GameState g = GameState.fromString(""
                                       + "BBBBBWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "NONE");
    assertTrue(g.gameOver());
    assertEquals(Color.BLACK, g.winner());
  }

  @Test
  void winnerWhite() {
    GameState g = GameState.fromString(""
                                       + "WBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "NONE");
    assertTrue(g.gameOver());
    assertEquals(Color.WHITE, g.winner());
  }

  @Test
  void winnerDraw() {
    GameState g = GameState.fromString(""
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "BBBBWWWW"
                                       + "NONE");
    assertTrue(g.gameOver());
    assertEquals(Color.NONE, g.winner());
  }

  /**
   * Check that we correctly declare the game over even when the board is not
   * completely full.
   */
  @Test
  void winnerBoardNotFull1() {
    // Example taken from https://en.wikipedia.org/wiki/Reversi.
    GameState g = GameState.fromString(""
                                       + "WWWWWWWW"
                                       + "WWWWWWWW"
                                       + "WWWWWWWW"
                                       + "WWWWWWW-"
                                       + "WWWWWW--"
                                       + "WWWWWW-B"
                                       + "WWWWWWW-"
                                       + "WWWWWWWW"
                                       + "NONE");
    assertTrue(g.gameOver());
    assertEquals(Color.WHITE, g.winner());
  }

  @Test
  void winnerBoardNotFull2() {
    // Example taken from https://en.wikipedia.org/wiki/Reversi, but with colors
    // flipped.
    GameState g = GameState.fromString(""
                                       + "----B---"
                                       + "----BB--"
                                       + "BBBBBBBW"
                                       + "--BBBB-W"
                                       + "--BBB--W"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "NONE");
    assertTrue(g.gameOver());
    assertEquals(Color.BLACK, g.winner());
  }

  /**
   * "Easy" testcases for toMove(), where the next player to move is the
   * opposite of the person last to move.
   */
  @Test
  void toMoveSimple() {
    assertEquals(Color.BLACK, GameState
                                  .fromString(""
                                              + "--------"
                                              + "--------"
                                              + "----W---"
                                              + "---WW---"
                                              + "---BW---"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "BLACK")
                                  .toMove());
    assertEquals(Color.WHITE, GameState
                                  .fromString(""
                                              + "--------"
                                              + "--------"
                                              + "----W---"
                                              + "---WW---"
                                              + "---BW---"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "WHITE")
                                  .toMove());
  }

  /**
   * Test toMove() in the case when one player is forced to pass because
   * they don't have any valid moves.
   */
  @Test
  void toMovePlayerPasses() {
    assertEquals(Color.BLACK, GameState
                                  .fromString(""
                                              + "B-------"
                                              + "B-------"
                                              + "B-------"
                                              + "W-------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "BLACK")
                                  .toMove());

    assertEquals(Color.WHITE, GameState
                                  .fromString(""
                                              + "WWB-----"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "--------"
                                              + "WHITE")
                                  .toMove());
  }

  /**
   * It's never legal to set a square to NONE -- you have to set it to WHITE or
   * BLACK.
   */
  @Test
  void isLegalMoveCannotSetSquareToNone() {
    GameState g = new GameState();
    assertThrows(IllegalArgumentException.class, () -> {
      g.isLegalMove(new Location("A1"), Color.NONE);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      g.isLegalMove(new Location("D4"), Color.NONE);
    });
  }

  @Test
  void cannotMoveToOccupiedSpace() {
    GameState g = GameState.fromString(""
                                       + "WBBWB---"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "BLACK");

    // Neither player can "re-move" on top of any of the spaces that already
    // contain tokens.
    for (Color c : List.of(Color.WHITE, Color.BLACK)) {
      assertFalse(g.isLegalMove(new Location("A1"), c));
      assertFalse(g.isLegalMove(new Location("B1"), c));
      assertFalse(g.isLegalMove(new Location("C1"), c));
      assertFalse(g.isLegalMove(new Location("D1"), c));
      assertFalse(g.isLegalMove(new Location("E1"), c));
    }
  }

  /**
   * Gets the legal moves for the given color.  Returned as a set of Strings
   * rather than a set of Locations for ease of testing.
   */
  private Set<String> allLegalMoves(GameState g, Color toMove) {
    Set<String> moves = new HashSet<>();
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Location loc = new Location(row, col);
        if (g.isLegalMove(loc, toMove)) {
          moves.add(loc.toString());
        }
      }
    }
    return moves;
  }

  /**
   * Play out the Murakami Variation and check that our set of legal moves is
   * correct at each point.
   */
  @Test
  void legalMovesMurakamiVariation() {
    GameState g = new GameState();

    assertEquals(Set.of("C4", "D3", "E6", "F5"), allLegalMoves(g, Color.BLACK));
    g.recordMove(new Location("C4"), Color.BLACK);

    assertEquals(Set.of("C3", "C5", "E3"), allLegalMoves(g, Color.WHITE));
    g.recordMove(new Location("E3"), Color.WHITE);

    assertEquals(Set.of("F2", "F3", "F4", "F5", "F6"),
                 allLegalMoves(g, Color.BLACK));
    g.recordMove(new Location("F4"), Color.BLACK);

    assertEquals(Set.of("C3", "C5", "G3", "G5"), allLegalMoves(g, Color.WHITE));
    g.recordMove(new Location("C5"), Color.WHITE);

    assertEquals(Set.of("C6", "D2", "D6", "E2", "E6"),
                 allLegalMoves(g, Color.BLACK));
    g.recordMove(new Location("D6"), Color.BLACK);

    assertEquals(Set.of("B3", "B4", "C3", "D7", "E6", "E7", "F3", "F5", "F6",
                        "G4", "G5"),
                 allLegalMoves(g, Color.WHITE));
    g.recordMove(new Location("F3"), Color.WHITE);

    // Okay, there's more to this opening, but I got bored.
  }

  @Test
  void flipThreeDirections() {
    // Figures 3 and 4 from https://www.fgbradleys.com/rules/Othello.pdf.
    GameState g = GameState.fromString(""
                                       + "--------"
                                       + "--------"
                                       + "--W---W-"
                                       + "--B--B--"
                                       + "--B-B---"
                                       + "--BB----"
                                       + "---BW---"
                                       + "--------"
                                       + "WHITE");
    g.recordMove(new Location("C7"), Color.WHITE);
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - W - - - W -\n"
                     + "4 - - W - - W - -\n"
                     + "5 - - W - W - - -\n"
                     + "6 - - W W - - - -\n"
                     + "7 - - W W W - - -\n"
                     + "8 - - - - - - - -\n"
                     + "Game over; WHITE wins.",
                 g.toString());
  }

  @Test
  void flipDoesNotGoThroughOwnPiece() {
    // Figure 5 from https://www.fgbradleys.com/rules/Othello.pdf.
    GameState g = GameState.fromString(""
                                       + "--------"
                                       + "--------"
                                       + "------B-"
                                       + "------W-"
                                       + "------W-"
                                       + "------B-"
                                       + "------W-"
                                       + "--------"
                                       + "BLACK");
    g.recordMove(new Location("G8"), Color.BLACK);
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - - - - - B -\n"
                     + "4 - - - - - - W -\n"
                     + "5 - - - - - - W -\n"
                     + "6 - - - - - - B -\n"
                     + "7 - - - - - - B -\n"
                     + "8 - - - - - - B -\n"
                     + "WHITE to move.",
                 g.toString());
  }

  @Test
  void flipDoesNotGoThroughEmptySpace() {
    // Modification of figure 5 from
    // https://www.fgbradleys.com/rules/Othello.pdf.
    // Check that we don't flip the white pieces in the G column -- they get
    // ignored because of the empty space between the Ws.
    GameState g = GameState.fromString(""
                                       + "--------"
                                       + "--------"
                                       + "------B-"
                                       + "------W-"
                                       + "------W-"
                                       + "--------"
                                       + "------W-"
                                       + "----BW--"
                                       + "BLACK");
    g.recordMove(new Location("G8"), Color.BLACK);
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - - - - - B -\n"
                     + "4 - - - - - - W -\n"
                     + "5 - - - - - - W -\n"
                     + "6 - - - - - - - -\n"
                     + "7 - - - - - - W -\n"
                     + "8 - - - - B B B -\n"
                     + "WHITE to move.",
                 g.toString());
  }

  @Test
  void flipIsNotRecursive() {
    // Figures 6 and 7 from https://www.fgbradleys.com/rules/Othello.pdf.
    GameState g = GameState.fromString(""
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "--------"
                                       + "---B----"
                                       + "---W----"
                                       + "---W----"
                                       + "--WWB---"
                                       + "BLACK");
    g.recordMove(new Location("B8"), Color.BLACK);
    assertEquals(""
                     + "  A B C D E F G H \n"
                     + "1 - - - - - - - -\n"
                     + "2 - - - - - - - -\n"
                     + "3 - - - - - - - -\n"
                     + "4 - - - - - - - -\n"
                     + "5 - - - B - - - -\n"
                     + "6 - - - W - - - -\n"
                     + "7 - - - W - - - -\n"
                     + "8 - B B B B - - -\n"
                     + "WHITE to move.",
                 g.toString());
  }
}