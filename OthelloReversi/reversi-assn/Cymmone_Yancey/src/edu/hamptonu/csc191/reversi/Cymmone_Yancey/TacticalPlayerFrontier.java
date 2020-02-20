package edu.hamptonu.csc191.reversi.Cymmone_Yancey;
//MINIMIZE OPPONENT LEGAL MOVES
//MINIMIZE FRONTIER
//MAXIMIZE OPPONENT FRONTIER
//PLAY STABLE DISCS
import edu.hamptonu.csc191.reversi.Color;
import edu.hamptonu.csc191.reversi.ColorUtil;
import edu.hamptonu.csc191.reversi.Location;
import edu.hamptonu.csc191.reversi.Player;

import java.time.Instant;
import java.util.*;

import static edu.hamptonu.csc191.reversi.Location.BOARD_SIZE;

// This lets us refer to plain "BOARD_SIZE" when we mean Location.BOARD_SIZE.

/**
 * A reversi player which greedily picks the move which will maximize the number
 * of tokens showing in its color
 *
 * When two moves have the same score, chooses randomly between them.
 */
public class TacticalPlayerFrontier implements Player {
  private Color color;
  private GameState gameState;
  private Random rand;

  public TacticalPlayerFrontier(Color color) {
    this.color = color;
    gameState = new GameState();
    rand = new Random();
  }

  private boolean isCornerMove(Location loc){
    Location A1 = new Location(0,0);
    Location A8 = new Location(0,7);
    Location H1 = new Location(7,0);
    Location H8 = new Location(7,7);
    return loc.equals(A1) || loc.equals(A8) || loc.equals(H1) || loc.equals(H8);
  }

  private boolean isEdgeMove(Location loc){
    Location A3 = new Location(0,2);
    Location A4 = new Location(0,3);
    Location A5 = new Location(0,4);
    Location A6 = new Location(0,5);
    Location H3 = new Location(7,2);
    Location H4 = new Location(7,3);
    Location H5 = new Location(7,4);
    Location H6 = new Location(7,5);
    Location C1 = new Location(2,0);
    Location D1 = new Location(3,0);
    Location E1 = new Location(4,0);
    Location F1 = new Location(5,0);
    Location C8 = new Location(2,7);
    Location D8 = new Location(3,7);
    Location E8 = new Location(4,7);
    Location F8 = new Location(5,7);
    return loc.equals(A3) || loc.equals(A4)|| loc.equals(A5)|| loc.equals(A6)
        || loc.equals(H3)|| loc.equals(H4)|| loc.equals(H5)|| loc.equals(H6)
        || loc.equals(C1) || loc.equals(D1)|| loc.equals(E1)|| loc.equals(F1)
        || loc.equals(C8)|| loc.equals(D8) || loc.equals(E8)|| loc.equals(F8);
  }

  private boolean isCriticalMove(Location loc){
    Location C3 = new Location(2,2);
    Location C6 = new Location(2,5);
    Location F3 = new Location(5,2);
    Location F6 = new Location(5,5);
    return loc.equals(C3) || loc.equals(C6) || loc.equals(F3) || loc.equals(F6);
  }

  private boolean isLMove(Location loc){
    Location A2 = new Location(0,1);
    Location A7 = new Location(0,6);
    Location B1 = new Location(1,0);
    Location B2 = new Location(1,1);
    Location B7 = new Location(1,6);
    Location B8 = new Location(1,7);
    Location G1 = new Location(6,0);
    Location G2 = new Location(6,1);
    Location G7 = new Location(6,6);
    Location G8 = new Location(6,7);
    Location H2 = new Location(7,1);
    Location H7 = new Location(7,6);
    return loc.equals(A2) || loc.equals(A7)|| loc.equals(B1)|| loc.equals(B2)
        || loc.equals(B7)|| loc.equals(B8)|| loc.equals(G1)|| loc.equals(G2)
        || loc.equals(G7) || loc.equals(G8)|| loc.equals(H2)|| loc.equals(H7);
  }

  public List<Location> maxScores(List<Location> input){
    List<Location> ret = new ArrayList<>();
    List<Integer> scores = new ArrayList<>();
    for(Location move : input){
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
    int max = Integer.MIN_VALUE;
    for(int score : scores){
      max = Math.max(score, max);
    }
    for(int score : scores){
      if(score == max){
        ret.add(input.get(scores.indexOf(score)));
      }
    }
    return ret;
  }
 private boolean isFrontier(Location loc){
   for(int dy = -1; dy <= 1; dy+=1) {
     for(int dx = -1; dx <= 1; dx++) {
       int x = loc.col + dx;
       int y = loc.row + dy;//Location next = new Location(y,x);
       if ((y < 0) || (y > BOARD_SIZE - 1) || (x < 0) || (x > BOARD_SIZE - 1)) { //IN BOUNDS
         return false;
       } else if (gameState.get(new Location(y,x)).equals(Color.NONE)) {
         return true;
       }
     }
   }
   return false;
  }
  private List<Location> minFrontier(Map<Integer, Location> legalMoves){
    List<Location> minFrontScores = new ArrayList<>();
    int min = Integer.MAX_VALUE;
    for(int score : legalMoves.keySet()){
      min = Math.min(score, min);
    }
    for(int score : legalMoves.keySet()){
      if(score == min){
        minFrontScores.add(legalMoves.get(score));
      }
    }
    return minFrontScores;
  }
  private Location bestMove(List<Integer> aScores, List<Location> aLegalMoves){
    Map<Integer, Location> cornerMoves = new HashMap<>();
    Map<Integer, Location> edgeMoves = new HashMap<>();
    Map<Integer, Location> criticalMoves = new HashMap<>();
    Map<Integer, Location> lMoves = new HashMap<>();
    Map<Integer, Location> otherMoves = new HashMap<>();

    //CATEGORIZE LEGAL MOVES WITH SCORES
    for (Location loc : aLegalMoves) {
      int locScore = aScores.get(aLegalMoves.indexOf(loc));
      if(isCornerMove(loc)){
        cornerMoves.put(locScore, loc);
      } else if(isEdgeMove(loc)){
        edgeMoves.put(locScore, loc);
      } else if(isCriticalMove(loc)){
        criticalMoves.put(locScore, loc);
      } else if(isLMove(loc)){
        lMoves.put(locScore, loc);
      } else{
        otherMoves.put(locScore, loc);
      }
    }

    //CHOOSE WHAT MOVE TO MAKE
    if(!cornerMoves.isEmpty()){
      List<Location> best = minFrontier(cornerMoves);
      List<Location> front = maxScores(best);
      return front.get(rand.nextInt(front.size()));
    }else if(!edgeMoves.isEmpty()){
      /*List<Location> best = minFrontier(edgeMoves);
      return best.get(rand.nextInt(best.size()));*/
      List<Location> best = minFrontier(edgeMoves);
      List<Location> front = maxScores(best);
      return front.get(rand.nextInt(front.size()));
    }else if(!criticalMoves.isEmpty()){
      /*List<Location> best = minFrontier(criticalMoves);
      return best.get(rand.nextInt(best.size()));*/
      List<Location> best = minFrontier(criticalMoves);
      List<Location> front = maxScores(best);
      return front.get(rand.nextInt(front.size()));
    } else if(!otherMoves.isEmpty()){
      /*List<Location> best = minFrontier(otherMoves);
      return best.get(rand.nextInt(best.size()));*/
      List<Location> best = minFrontier(otherMoves);
      List<Location> front = maxScores(best);
      return front.get(rand.nextInt(front.size()));
    } else {
      /*List<Location> best = minFrontier(lMoves);
      return best.get(rand.nextInt(best.size()));*/
      List<Location> best = minFrontier(lMoves);
      List<Location> front = maxScores(best);
      return front.get(rand.nextInt(front.size()));
    }
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
    List<Integer> runScores = new ArrayList<>();
    for(Location loc : legalMoves){
      int count = 0;
      GameState obs = gameState.copy();
      obs.recordMove(loc, this.color);
      for(int y = 0; y < BOARD_SIZE; y++){
        for(int x = 0; x< BOARD_SIZE; x++){
          Location piece = new Location(y,x);
          if(isFrontier(piece)){
            count++;
          }
        }
      }
      runScores.add(count);
    }
    Location move = bestMove(runScores, legalMoves);
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
