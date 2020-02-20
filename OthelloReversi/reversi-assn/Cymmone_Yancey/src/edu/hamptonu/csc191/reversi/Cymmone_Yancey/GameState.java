package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

// This lets us refer to plain "BOARD_SIZE" when we mean Location.BOARD_SIZE.
import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.ColorUtil;
import edu.hamptonu.csc191.reversi.Location;

/**
 * Represents the state of a reversi game: The pieces on the board plus whose
 * turn it is.
 *
 * You can change anything in here that you like.  For instance, if you want the
 * board to be a 1D array instead of a 2D array, go for it.  If you change
 * public methods, you'll need to update tests.  Before making any really big
 * changes, maybe check with your instructor, they may be able to keep you from
 * going astray.
 */
public class GameState {
  /**
   * Represents the game board in [row][col] order (i.e. [y][x]).
   *
   * Each square is in one of three states.
   *  - It's empty (Color.NONE), OR
   *  - it contains a white token (Color.WHITE), OR
   *  - it contains a black token (Color.BLACK).
   *
   * Because this is a private member, you could change this to a different
   * representation if you like.
   */
  private Color[][] board;

  /**
   * Checks whether a player can legally place a piece in the given location.
   *
   * A move is legal if it's on a space that's empty and it flips at least one
   * of our opponent's pieces.
   *
   * Ignores whose turn it is.  (This is important so that you can use it in
   * hasLegalMove, which you might use in determining whose move it is.  If you
   * don't know whose move it is then you can't check it here...)
   *
   * @param loc the location under question
   * @param c the color of the piece to be played.  Must be Color.WHITE or
   *     Color.BLACK.
   * @return whether playing at that location would be legal.
   * @throws IllegalArgumentException if p is Color.NONE.
   */
  public boolean isLegalMove(Location loc, Color c) {
    Color spc  = board[loc.row][loc.col];
    if(c == Color.NONE){
      throw new IllegalArgumentException("Player color cannot be NONE");
    } else if(board[loc.row][loc.col] == Color.NONE){
      boolean northW, north, northE, east, west, southW, south, southE;
      northW = wouldFlip(loc, c, -1, -1);
      north = wouldFlip(loc, c, -1, 0);
      northE = wouldFlip(loc, c, -1, 1);
      east = wouldFlip(loc, c, 0, -1);
      west = wouldFlip(loc, c, 0, 1);
      southW = wouldFlip(loc, c, 1, -1);
      south = wouldFlip(loc, c, 1, 0);
      southE = wouldFlip(loc, c, 1, 1);

      if (northW || north || northE || east || west || southW || south || southE) {
        return true;
      }else{
        return false;
      }
    }else{
      return false;
    }// TODO: You fill this in!
  }

  /**
   * The player whose move it will be next.  When the game is over, this is
   * Color.NONE.
   */
  private Color toMove;

  /**
   * Initializes the board to the default starting position in reversi: Two
   * white and two black pieces in the center, and black to move.
   */
  public GameState() {
    toMove = Color.BLACK; // Black moves first.

    // Set all squares to empty (they're initialized to null).
    board = new Color[BOARD_SIZE][BOARD_SIZE];
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        board[y][x] = Color.NONE;
      }
    }

    // TODO: Fill in to set the middle four squares to the starting position.
    int mid = BOARD_SIZE/2;
    board[mid][mid] = Color.WHITE;
    board[mid-1][mid] = Color.BLACK;
    board[mid-1][mid-1] = Color.WHITE;
    board[mid][mid-1] = Color.BLACK;
  }

  /**
   * Gets the piece at the given location, or Color.NONE if the location is
   * empty.
   */
  public Color get(Location loc) { return board[loc.row][loc.col]; }

  /**
   * Gets the player whose turn it is.  If the game is over, returns Color.NONE.
   */
  public Color toMove() { return toMove; }

  /**
   * Determines whether the game is over.
   *
   * This is equivalent to toMove() == Color.NONE.
   */
  public boolean gameOver() { return toMove == Color.NONE; }

  /**
   * Determines who won the game, or if it's a tie (Color.NONE).
   *
   * The winner is simply the player with the most disks.
   *
   * @return the player who won.
   * @throws IllegalStateException if the game isn't yet over (!gameOver()).
   */
  public Color winner() {
    if (!gameOver()) {
      throw new IllegalStateException(
          "Can't call winner() until gameOver() is true.");
    }
    int black = 0;
    int white = 0;
    for(int r = 0; r < BOARD_SIZE; r++){
      for(int c = 0; c < BOARD_SIZE; c++){
        if(board[r][c] == Color.WHITE){
          white+=1;
        }else if(board[r][c] == Color.BLACK){
          black+=1;
        }
      }
    }
    if(black > white){
      return Color.BLACK;
    } else if(white > black){
      return Color.WHITE;
    } else {
      return Color.NONE; // TODO: Fill this in.
    }
  }

  /**
   * Determines whether playing a token of the given color at the given location
   * would flip one or more pieces in the direction dy dx.
   *
   * dy and dx indicate the direction to search.  For instance, to search in the
   * diagonal up and to the left, set dy = -1, dx = -1.  dy and dx should be
   * either -1, 0, or 1, and they both shouldn't be 0.
   */
  private boolean wouldFlip(Location loc, Color c, int dy, int dx) {
    int y = loc.row + dy;
    int x = loc.col + dx;
    if((y < 0)||(y > BOARD_SIZE-1)||(x < 0)||(x > BOARD_SIZE-1)){ //IN BOUNDS
      return false;
    } else if(board[y][x] != ColorUtil.oppositeColor(c)){
      return false;
    } else {
      for(int rw = y, cl = x; rw>=0 && rw<BOARD_SIZE&&cl>=0 && cl<BOARD_SIZE; rw+=dy, cl+=dx){
        if(board[rw][cl] == c){
          return true;
        } else if(board[rw][cl] == Color.NONE){
          break;
        }
      }
      return false;
    }
    // TODO: Recommend implementing this this private method and using it in
    // e.g. isLegalMove.
  }

  /**
   * Marks that the player playing color `c` placed a token on the given square,
   * and flips tokens on the board as appropriate.
   *
   * Requires that it's currently `c`'s turn to move and that the move is legal,
   * meaning the square is empty and that playing there flips at least one token
   * already on the board.
   *
   * @param loc the place on the board at which we're placing a token
   * @param c the player making the move
   * @throws IllegalArgumentException if c is Color.NONE or this is not a legal
   *     move (e.g. there's a token already on that square, or it's not c's turn
   *     to move).
   */
  public void recordMove(Location loc, Color c) {
    // TODO: Fill this in!
    /*if(c == Color.NONE || !isLegalMove(loc, c) || toMove()!=c){
      throw new IllegalArgumentException("This is not legal");
    }*/
    if(c == Color.NONE){
      throw new IllegalArgumentException("The color cannot be NONE");
    }
    if(!isLegalMove(loc, c)){
      throw new IllegalArgumentException("This move is not legal");
    }
    if(toMove()!=c){
      throw new IllegalArgumentException("It is not your turn");
    }

    board[loc.row][loc.col] = c;
    flip(loc, c, -1, -1);
    flip(loc, c, -1, 0);
    flip(loc, c, -1, 1);
    flip(loc, c, 0, -1);
    flip(loc, c, 0, 1);
    flip(loc, c, 1, -1);
    flip(loc, c, 1, 0);
    flip(loc, c, 1, 1);
    // Don't forget to update whose turn it is at the end of this method!
   if(hasLegalMove(ColorUtil.oppositeColor(toMove))){
      toMove = ColorUtil.oppositeColor(toMove);
    } else if(!hasLegalMove(toMove)){
      toMove = Color.NONE;
      winner();
    }
  }

  /**
   * Determines whether a player has a legal move somewhere on the board.
   */
  private boolean hasLegalMove(Color c) {
    // TODO: Recommend implementing this private function and using it to help
    // you elsewhere, but up to you!  (For instance, at the end of recordMove()
    // you have to figure out who will move next.  This method might help.)
    for(int rw = 0; rw < BOARD_SIZE; rw++){
      for(int cl = 0; cl < BOARD_SIZE; cl++){
        Location loc = new Location(rw, cl);
        if(isLegalMove(loc, c)){
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Pretends that we put a white/black piece at row/col and attempts to flip
   * pieces in the direction of dx dy.
   *
   * See wouldFlip for the meaning of dy and dx.
   */
  private void flip(Location loc, Color c, int dy, int dx) {
    // TODO: Recommend implementing this this private method and using it in
    // e.g. recordMove.
    int y = loc.row + dy;
    int x = loc.col + dx;
    if(wouldFlip(loc, c, dy, dx)){
      for(int rw = y, cl = x; rw>=0 && rw<BOARD_SIZE&&cl>=0 && cl<BOARD_SIZE; rw+=dy, cl+=dx){
        if(board[rw][cl] == ColorUtil.oppositeColor(c)){
          board[rw][cl] = c;
        } else if(board[rw][cl] == c){
          break;
        }
      }
    }
  }

  /**
   * Converts the game state to a string representation.
   */
  @Override
  public String toString() {
    // I'll give this one to you for free.  :)
    StringBuilder sb = new StringBuilder();
    sb.append("  A ");
    for (int i = 1; i < BOARD_SIZE; i++) {
      sb.append((char)('A' + i));
      sb.append(' ');
    }
    sb.append('\n');
    for (int y = 0; y < BOARD_SIZE; y++) {
      sb.append((char)('1' + y));
      sb.append(' ');
      for (int x = 0; x < BOARD_SIZE; x++) {
        switch (board[y][x]) {
          case NONE:
            sb.append("-");
            break;
          case BLACK:
            sb.append("B");
            break;
          case WHITE:
            sb.append("W");
            break;
        }
        if (x != BOARD_SIZE - 1) {
          sb.append(' ');
        }
      }
      sb.append('\n');
    }
    if (!gameOver()) {
      sb.append(toMove + " to move.");
    } else {
      Color winner = winner();
      if (winner == Color.WHITE || winner == Color.BLACK) {
        sb.append("Game over; " + winner + " wins.");
      } else {
        sb.append("Game over; DRAW.");
      }
    }
    return sb.toString();
  }

  /**
   * Creates a deep copy of this GameState.  The new version is identical to the
   * old one, and because it's a deep copy, changes to one don't affect the
   * other.
   *
   * This is useful if e.g. you want to "see what happens" after making a move:
   * Clone the original state and then apply the move to the new state.
   */
  public GameState copy() {
    GameState g = new GameState();
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[0].length; col++) {
        g.board[row][col] = board[row][col];
      }
    }
    g.toMove = toMove;
    return g;
  }

  /**
   * Creates a GameState from a String.
   *
   * The natural thing would be to make the format accepted here match that of
   * toString().  But that's sort of complicated.  Sufficient for our purposes
   * is to accept a simpler format: A string of 64 "W", "B", and "-" optionally
   * separated by whitespace, followed by a Color, indicating whose turn it is.
   *
   * For example:
   *
   *    --------
   *    --------
   *    ----W---
   *    ---WW---
   *    ---BW---
   *    --------
   *    --------
   *    --------
   *    WHITE
   *
   * is a valid spelling of a board and means that it's WHITE's turn.
   *
   * @param s the string representation of the game state
   * @return a GameState built as specified by the String.
   * @throws IllegalArgumentException if the board is not valid per above.
   */
  static GameState fromString(String s) {
    int boardSize = BOARD_SIZE * BOARD_SIZE;
    GameState g = new GameState();

    int i = 0;
    int pos;
    for (pos = 0; pos < s.length(); pos++) {
      char c = s.charAt(pos);
      if (i == boardSize) {
        break; // We've read the whole board.
      }
      if (Character.isWhitespace(c)) {
        continue; // Ignore whitespace.
      }
      switch (Character.toUpperCase(c)) {
        case 'B':
          g.board[i / BOARD_SIZE][i % BOARD_SIZE] = Color.BLACK;
          break;
        case 'W':
          g.board[i / BOARD_SIZE][i % BOARD_SIZE] = Color.WHITE;
          break;
        case '-':
          g.board[i / BOARD_SIZE][i % BOARD_SIZE] = Color.NONE;
          break;
        default:
          throw new IllegalArgumentException("Unexpected character in board: " +
              c);
      }
      i++;
    }

    if (i != boardSize) {
      throw new IllegalArgumentException("Board too small. Expected " +
          boardSize + " but was " + i);
    }

    String remainder = s.substring(pos).strip().toUpperCase();
    // Throws IllegalArgumentException if remainder is not BLACK, WHITE, or
    // NONE.
    g.toMove = Color.valueOf(remainder);

    return g;
  }
}
