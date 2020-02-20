package edu.hamptonu.csc191.reversi;

/**
 * Represents the state of a square on the board -- either white, black, or
 * unoccupied.
 *
 * Enums function mostly like `int`s or other primitive types.  Example usage:
 *
 *   Color c = Color.WHITE;
 *   if (c == Color.WHITE) { ... }  // true, so this `if` executes
 *   if (c == Color.BLACK) { ... }  // false, so this doesn't execute
 *   String s = "The color is: " + c;  // Convert Color to a String, easy peasy.
 */
public enum Color {
  WHITE,
  BLACK,
  NONE,
}
