package edu.hamptonu.csc191.reversi;

/**
 * Simple utility functions for dealing with Players.
 */
public class ColorUtil {
  /**
   * "Inverts" a color -- given white, returns black, and given black, returns
   * white.
   *
   * The fact that this is `static` means that you call it directly on the
   * ColorUtil class, e.g. `ColorUtil.oppositeColor(Color.BLACK)`.
   * Static methods probably show up as italicized in your IntelliJ.
   *
   * @param c the Color value to invert.
   * @return the "opposite" of c -- Color.WHITE if it's Color.BLACK, and
   *     Color.BLACK if it's
   *   Color.WHITE.
   * @throws IllegalArgumentException if c is Color.NONE.
   */
  public static Color oppositeColor(Color c) {
    // TODO: You implement this.
    switch (c){
      case BLACK: return Color.WHITE;
      case WHITE: return Color.BLACK;
    // Hint: To raise an error, do something like
      default: throw new IllegalArgumentException("Color argument cannot be NONE.");
    }
    //   throw new IllegalArgumentException("Color argument cannot be NONE.");
  }
}
