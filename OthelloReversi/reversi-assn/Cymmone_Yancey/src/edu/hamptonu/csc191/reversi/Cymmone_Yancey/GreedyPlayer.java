package edu.hamptonu.csc191.reversi.Cymmone_Yancey;

import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.ColorUtil;
import edu.hamptonu.csc191.reversi.Location;
import edu.hamptonu.csc191.reversi.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;

// This lets us refer to plain "BOARD_SIZE" when we mean Location.BOARD_SIZE.

/**
 * A reversi player which greedily picks the move which will maximize the number
 * of tokens showing in its color
 *
 * When two moves have the same score, chooses randomly between them.
 */
public class GreedyPlayer implements Player {
  private Color color;
  private GameState gameState;
  private Random rand;

  public GreedyPlayer(Color color) {
    this.color = color;
    gameState = new GameState();
    rand = new Random();
  }

  public Location bestMove(List<Location> input){
    Location tRC = new Location(0,0);
    Location tLC = new Location(7,0);
    Location bRC = new Location(0,7);
    Location bLC = new Location(7,7);
    Location s1 = new Location(0,0);
    Location s2 = new Location(0,0);
    Location s3 = new Location(0,0);
    Location s4 = new Location(0,0);

    for(Location loc : input){
      if(loc==tRC||loc==tLC||loc==bRC||loc==bLC){
        return loc;
      }
    }
    for(Location loc : input) {
      if (loc == s1 || loc == s2 || loc == s3 || loc == s4){
        return loc;
      }
    }
    return input.get(rand.nextInt(input.size()));
  }
  @Override
  public Location nextMove(Instant deadline) {
    // TODO: You fill this in!
    //LIST OF LEGAL MOVES
    List<Location> legalMoves = new ArrayList<>();
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Location loc = new Location(row, col);
        if (gameState.isLegalMove(loc, this.color)){
          legalMoves.add(loc);
        }
      }
    }
    //for each legal move calculate score and store
    List<Integer> scores = new ArrayList<>();
    for(Location move : legalMoves){
      GameState obs = this.gameState.copy();
      obs.recordMove(move, this.color);
      //determine score for move
      int score = 0;
      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          Location loc = new Location(row, col);
          if(obs.get(loc) == this.color){
            score++;
          } else {
            score--;
          }
        }
      }
      scores.add(score);
    }

    //find the max of the scores
    List<Location> maxScores = new ArrayList<>();
    int max = Integer.MIN_VALUE;
    for(int score : scores){
      max = Math.max(score, max);
    }

    for (int score : scores){
      if(score == max){
        maxScores.add(legalMoves.get(scores.indexOf(score)));
      }
    }
    Location move = bestMove(maxScores);
    gameState.recordMove(move, this.color);
    return move;
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
