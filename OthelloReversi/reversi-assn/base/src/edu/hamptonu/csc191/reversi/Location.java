package edu.hamptonu.csc191.reversi;

/**
 * Represents a square on a reversi board.
 *
 * Locations are immutable; you can't change `row` or `col` after you've
 * constructed one.  The row and col are guaranteed to be in bounds, i.e.
 * 0 <= row < BOARD_SIZE (and same for col).
 *
 * This class is used by the Player interface, so you shouldn't change the API
 * (i.e. you shouldn't add or remove public members).  If you want a different
 * Location for use in your code, you can do that, just come talk to your
 * instructor and they'll show you how.
 */
public class Location {
  /**
   * Width/height of the board.
   *
   * This constant is public, so other classes can access it via
   * Location.BOARD_SIZE.
   *
   * You should refer to this constant in this class *and elsewhere* instead of
   * using the magic number 8.
   */
  public static final int BOARD_SIZE = 8;

  /** The location's row (starting at 0). */
  public final int row;

  /** The location's column (starting at 0). */
  public final int col;

  /**
   * Creates a new location on the board.
   *
   * This location must be valid, i.e. within the bounds of the board.
   *
   * @param r the location's row (starting at 0).
   * @param c the location's column (starting at 0).
   * @throws IllegalArgumentException if row or col is negative or out of bounds
   *     (according to BOARD_SIZE).
   */
  public Location(int r, int c) {
    // Given to you for free.  :)
    if (r < 0 || r >= BOARD_SIZE) {
      throw new IllegalArgumentException("row is out of bounds.");
    }
    if (c < 0 || c >= BOARD_SIZE) {
      throw new IllegalArgumentException("col is out of bounds.");
    }
    this.row = r;
    this.col = c;
  }

  /**
   * Creates a Location from a human-readable spot on the board, e.g. "B3".
   *
   * Not case-sensitive.
   *
   * Note that the row specified in the string is one-indexed (i.e. "A1" is the
   * upper-left corner), but the `row` member in this class is zero-indexed
   * (i.e. 0 is the first row).
   *
   * Use this as e.g.
   *
   *   String s = readLine();
   *   Location loc = new Location(s);
   *
   * You may assume that BOARD_SIZE <= 9.
   *
   * @throws IllegalArgumentException if the string is unparsable or not a valid
   *     location.
   */
  public Location(String s) {
    // TODO: You fill this in.
    int rowI = 0;
    int colI = 0;
    if((s.length()!=2)||(!((s.charAt(1)>='1')&&(s.charAt(1)<='8')))){
      throw new IllegalArgumentException("Not a valid location entry");
    } else {
      switch(s.charAt(0)){ //GO BACK AND USE TO UPPER
        case 'H':
        case 'h': colI++;
        case 'G':
        case 'g': colI++;
        case 'F':
        case 'f': colI++;
        case 'E':
        case 'e': colI++;
        case 'D':
        case 'd': colI++;
        case 'C':
        case 'c': colI++;
        case 'B':
        case 'b': colI++;
        case 'A':
        case 'a':
                  break;
        default: throw new IllegalArgumentException("Not a valid location");
      }
      rowI = Integer.parseInt(Character.toString(s.charAt(1))) - 1;
    }
    this.row = rowI;
    this.col = colI;
  }

  public String toString() {
    // TODO: You fill this in.
    char c = 'A';
    int dexR = this.row + 1;
    int dexC = this.col;
    switch(dexC){
      case 7: c+=1;
      case 6: c+=1;
      case 5: c+=1;
      case 4: c+=1;
      case 3: c+=1;
      case 2: c+=1;
      case 1: c+=1;
    }
    String loc = c + (Integer.toString(dexR));
    return loc;
  }

  @Override
  public boolean equals(Object other) {
    Location loc = (Location)other;
    return row == loc.row && col == loc.col;
  }
}
