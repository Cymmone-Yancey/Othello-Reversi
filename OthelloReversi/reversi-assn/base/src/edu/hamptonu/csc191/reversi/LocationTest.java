package edu.hamptonu.csc191.reversi;

import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the Location class.
 *
 * Feel free to add additional tests, but you shouldn't need to delete or modify
 * any of the existing code, and you do so at your own peril!
 */
class LocationTest {
  /**
   * Helper that lets us assert the row and col of a Location.
   */
  private void assertLoc(int expectedRow, int expectedCol, Location loc) {
    Assertions.assertEquals(expectedRow, loc.row);
    Assertions.assertEquals(expectedCol, loc.col);
  }

  /**
   * Constructs some valid Locations and checks their row/cols.
   */
  @Test
  void constructorValid() {
    assertLoc(1, 2, new Location(1, 2));
    assertLoc(3, 4, new Location(3, 4));
  }

  /**
   * Constructs some invalid Locations and checks that they throw exceptions as
   * expected.
   */
  @Test
  void constructorInvalid() {
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> { new Location(-1, 0); });
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> { new Location(0, -1); });
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> { new Location(BOARD_SIZE, 0); });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Location(BOARD_SIZE + 10, 0);
    });
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> { new Location(0, BOARD_SIZE); });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Location(0, BOARD_SIZE + 10);
    });
  }

  /**
   * Tests some valid calls to fromString.
   */
  @Test
  void fromStringValid() {
    assertLoc(2, 0, new Location("a3"));
    assertLoc(0, 0, new Location("A1"));
    assertLoc(6, 3, new Location("D7"));
    assertLoc(7, 7, new Location("H8"));
    assertLoc(7, 7, new Location("h8"));
  }

  /**
   * Tests some invalid calls to fromString.
   */
  @Test
  void fromStringInvalid() {
    // Feel free to add testcases to this list if you like!
    List<String> testcases =
        List.of("A9", "I1", "A11", "A0", "1A", "AA1", "A-1", "A 1", "11", "",
                "1", "A", "@1", "@@");
    for (String s : testcases) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        new Location(s);
      }, s + " should be invalid!");
    }
  }

  /**
   * (Normally I don't call @Test methods "testFoo", that's redundant.  But the
   * "toString()" method in Java is special, so I can't call this method
   * toString.  "testToString" it is!)
   */
  @Test
  void testToString() {
    Assertions.assertEquals("A1", new Location(0, 0).toString());
    Assertions.assertEquals("A8", new Location(7, 0).toString());
    Assertions.assertEquals("H8", new Location(7, 7).toString());
    Assertions.assertEquals("D2", new Location(1, 3).toString());
  }

  @Test
  void testEquals() {
    Assertions.assertTrue(new Location(0, 0).equals(new Location(0, 0)));
    Assertions.assertFalse(new Location(1, 1).equals(new Location(1, 0)));
    Assertions.assertFalse(new Location(1, 1).equals(new Location(0, 1)));
  }
}