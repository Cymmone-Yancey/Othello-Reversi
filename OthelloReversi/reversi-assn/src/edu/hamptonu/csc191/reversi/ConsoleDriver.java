package edu.hamptonu.csc191.reversi;

import edu.hamptonu.csc191.reversi.jlebar.GameState;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

import static edu.hamptonu.csc191.reversi.Stats.InvIncompleteRegularizedBeta;

/**
 * Coordinates one or more games of reversi between two agents, displaying the
 * progress in the console.
 *
 * Either of the two agents may be a human (ConsolePlayer) or a computer
 * following any number of strategies.
 *
 * You shouldn't need to modify anything in here other than the part at the
 * top of this class, but I've tried to explain everything I'm doing here.
 *
 * (If I were keeping with the CSC 191 idiom, I'd make this class a
 * ConsoleProgram.  Instead I'm doing it the CSC 151 way and writing to the
 * plain terminal because it turns out ConsoleProgram's println is very slow
 * compared to System.out.println, and we want to write a lot of data to the
 * console!)
 */
public class ConsoleDriver {
  /**
   * Time limit per move.  Only applied to non-human Players.
   *
   * TODO: Feel free to change this for your testing.
   */
  private static final Duration TIME_PER_MOVE = Duration.ofSeconds(10);

  /**
   * Number of games to play.
   *
   * TODO: Feel free to change this for your testing.
   */
  private static final int NUM_GAMES = 1000;

  /**
   * These "factory functions" are used to create the two Players we're pitting
   * against each other.
   *
   * Feel free to change the bodies of these two functions to return
   * different Player objects.
   */
  private static final Player newPlayer1(Color color) {
    // TODO: You can change this line!
    //return new edu.hamptonu.csc191.reversi.jlebar.RandomPlayer(color);
    //return new edu.hamptonu.csc191.reversi.jlebar.GreedyPlayer(color);
    //return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.GreedyPlayer(color);
    return new edu.hamptonu.csc191.reversi.jlebar.TacticalPlayer(color);
    //return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.TacticalPlayerBetter(color);
  }
  private static final Player newPlayer2(Color color) {
    // TODO: You can change this line!
    //return new edu.hamptonu.csc191.reversi.jlebar.TacticalPlayer(color);
    //return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.RandomPlayer(color);
   //return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.GreedyPlayer(color);
    //return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.ConsolePlayer(color);
    return new edu.hamptonu.csc191.reversi.Cymmone_Yancey.TacticalPlayerFrontier(color);
  }

  //***********************************************************************
  /////////////////////////////////////////////////////////////////////////
  // Should be no need to modify below this point.  But feel free to read!
  /////////////////////////////////////////////////////////////////////////
  //***********************************************************************

  /* This weird main method is needed because this class is in the package
   * edu.hamptonu.csc191.reversi rather than the default (empty) package.  As
   * far as I can tell this shouldn't be required; I think it's just a bug in
   * the Stanford ConsoleProgram class. */
  public static void main(String[] args) { new ConsoleDriver().run(); }

  /**
   * Thread pool for running calls to Player.nextMove().  This lets us implement
   * the per-move deadline.  You shouldn't need to change this.
   */
  private ExecutorService executor = Executors.newFixedThreadPool(1);

  public void run() {
    String player1Name = newPlayer1(Color.BLACK).getClass().getName();
    String player2Name = newPlayer2(Color.BLACK).getClass().getName();
    System.out.println("Welcome to reversi!");
    System.out.println("We'll be playing " + NUM_GAMES +
                       " games, pitting\n - Player 1, " + player1Name +
                       "\nVERSUS\n - Player 2, " + player2Name);

    int player1Wins = 0;
    int player2Wins = 0;
    int draws = 0;
    for (int i = 1; i <= NUM_GAMES; i++) {
      // Create the two players, and choose whether they're controlling white or
      // black.
      //
      // These are ternary expressions, a shorter way of writing some `if`
      // statements. See https://www.baeldung.com/java-ternary-operator.
      Player blackPlayer =
          i % 2 == 0 ? newPlayer1(Color.BLACK) : newPlayer2(Color.BLACK);
      Player whitePlayer =
          i % 2 == 1 ? newPlayer1(Color.WHITE) : newPlayer2(Color.WHITE);

      System.out.println(
          "Starting game " + i + ", with " + blackPlayer.getClass().getName() +
          " playing the black pieces and " + whitePlayer.getClass().getName() +
          " controlling the white pieces.");
      Color winner = playOneGame(blackPlayer, whitePlayer);

      if (winner == Color.NONE) {
        draws++;
        System.out.println("Game ended in a draw!");
      } else if ((winner == Color.BLACK && i % 2 == 0) ||
                 (winner == Color.WHITE && i % 2 == 1)) {
        player1Wins++;
        System.out.println("Player 1 (" + winner + ") won!");
      } else {
        player2Wins++;
        System.out.println("Player 2 (" + winner + ") won!");
      }
      System.out.println((i < NUM_GAMES ? "Current" : "Final") + " score is " +
                         player1Wins + " - " + player2Wins + " - " + draws);
      System.out.println();
    }

    System.out.println("In this tournament of\n - Player 1 " + player1Name +
                       " vs \n - Player 2 " + player2Name);
    if (player1Wins > player2Wins) {
      System.out.println("Player 1 (" + player1Name + ") is the winner!");
    } else if (player2Wins > player1Wins) {
      System.out.println("Player 2 (" + player2Name + ") is the winner!");
    } else {
      System.out.println("The result is a perfect draw!!");
    }

    printStats(player1Wins, player2Wins, draws);
    executor.shutdown();
  }

  private Color playOneGame(Player blackPlayer, Player whitePlayer) {
    GameState gameState = new GameState();

    while (!gameState.gameOver()) {
      System.out.println(gameState.toString());
      System.out.println();

      // Ask the game state whose move it is.
      Color toMove = gameState.toMove();

      // Let nextPlayer refer to the player whose turn it is, and otherPlayer
      // refer to the player who isn't moving at the moment.
      Player nextPlayer = toMove == Color.BLACK ? blackPlayer : whitePlayer;
      Player otherPlayer = toMove == Color.BLACK ? whitePlayer : blackPlayer;

      // Calculate the deadline by which nextPlayer must make its move.  If
      // nextPlayer is a human, it has an arbitrarily long amount of time to
      // move (Instant.MAX).  Otherwise, the clock starts ticking now!
      Instant deadline = nextPlayer.isHuman()
                             ? Instant.MAX
                             : Instant.now().plus(TIME_PER_MOVE);

      // This line is equivalent to nextPlayer.nextMove(deadline), except it
      // returns null if nextPlayer doesn't respond within the deadline or if it
      // throws an exception while running.
      Location move = getMoveWithDeadline(nextPlayer, deadline);

      if (move == null) {
        System.err.println(
            toMove +
            " forfeits by exceeding deadline or raising an exception.");
        return ColorUtil.oppositeColor(toMove);
      }

      // Inform the other player of the move that was just made.
      otherPlayer.observeOpponentMove(move);

      // Record the move in our game state.
      gameState.recordMove(move, toMove);

      System.out.println(toMove + " moves to " + move);
    }

    // Print the game state one final time.  It will indicate who won, so we
    // don't have to worry about it.
    System.out.println(gameState.toString());
    return gameState.winner();
  }

  /**
   * Gets the Player's next move while enforcing the given deadline.
   *
   * Returns the Player's move, or, if it doesn't respond before the deadline
   * expires, null.  Also returns null if Player.nextMove raised an exception
   * (probably a bug in the Player).
   */
  private Location getMoveWithDeadline(Player p, Instant deadline) {
    // This is a bit complicated.  It's not very important that you understand
    // this!  This whole thing is basically equivalent to
    // `return p.nextMove(deadline)`, but with some additional work to enforce
    // the deadline.
    //
    // The reason enforcing the deadlne is important is, maybe a Player has a
    // bug where it hits an infinite loop -- if so we don't want our whole
    // program to stall.  Instead we'll kill the Player (and indicate that it
    // forfeit the game).
    //
    // The high-level concept we use to accomplish this is "threads".  We run
    // `p.nextMove(deadline)` in a separate thread, and then this thread waits
    // some amount of time for it to complete.  If p.nextMove has not completed
    // by then, we kill the Player and return null, indicating that this Player
    // forfeits the game.

    // Schedule p.nextMove(deadline) to be run on a separate thread.
    // The expression `() -> p.nextMove(deadline)` is a Java lambda expression.
    // See http://tutorials.jenkov.com/java/lambda-expressions.html or
    // https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html.
    Future<Location> moveFuture = executor.submit(() -> p.nextMove(deadline));

    // Calculate how long we're willing to wait for p.nextMove to complete.
    //
    // Sadly Duration.between(Instant.now(), Instant.MAX) overflows and throws
    // an exception, so we need this dumb hack.  :(
    long waitNanos = deadline.equals(Instant.MAX)
                         ? Long.MAX_VALUE
                         : Duration.between(Instant.now(), deadline).toNanos();

    try {
      // Wait for it to complete.  If it completes in time, we return it.  If it
      // doesn't complete in time, this throws a TimeoutException, which we
      // handle below.
      return moveFuture.get(waitNanos, TimeUnit.NANOSECONDS);
    } catch (TimeoutException e) {
      System.err.println("Timed out retrieving move from player!");
      moveFuture.cancel(true);
      return null;
    } catch (InterruptedException | ExecutionException e) {
      // If Player.nextMove() has a bug, it could raise an exception (e.g. a
      // NullPointerException) and crash.  We'll catch this here, log it, and
      // return null to indicate that the player forfeits. If something went
      // wrong, we're going to assume it's the Player's fault.
      System.err.println("Exception thrown during Player.nextMove!");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Do some statistics to determine how confident we are that one player is
   * stronger than the other.
   */
  private void printStats(int player1Wins, int player2Wins, int draws) {
    if (NUM_GAMES < 100) {
      System.out.println(
          "Run at least 100 games to get a statistical summary.");
      return;
    }

    // Compute "how much better" the winner is than the loser.
    //
    // Suppose we could run an infinite number of games.  We'd see that the
    // player which won the tournament won some fraction W of those games.  (Say
    // that ties count as 1/2 of a win.)  We want to estimate W.
    //
    // An easy way to estimate W would be to say that if a player won (say) 2/3
    // of its games, our estimate is 2/3.  This is indeed a good estimate, but
    // it doesn't tell us anything about the random uncertainty.  If we run few
    // trials, we're not very sure about our estimate, whereas if we run many
    // trials, we can be more certain.
    //
    // Our approach is to *conservatively* estimate W.  We want to find the
    // biggest value w such that there's a 99.9% chance that w <= W.  That is, w
    // is our best estimate that we're very confident isn't too big.
    //
    // I chose this estimator because I expect you're going to be running many
    // trials, and I don't want you to succumb to the temptation of falsely
    // believing that you've improved something when you haven't.
    //
    // Computing w is actually pretty simple once you figure out that our
    // problem is related to the problem of determining whether a coin is fair,
    // https://en.wikipedia.org/wiki/Checking_whether_a_coin_is_fair.
    // We see that Pr[W | wins, losses] ~ Beta(wins + 1, losses + 1).  So
    // Pr[w <= W | wins, losses] comes from the CDF of Beta, which is given by
    // the incomplete regularized beta function.  We have
    //
    //   I_w(wins + 1, losses + 1) = 0.001 (for 99.9% certainty),
    //
    // and we solve for w.  There's no nice formula, it's just the inverse of
    // I_w(alpha, beta) wrt w.
    final double CONFIDENCE = 0.001;
    double minStrength = InvIncompleteRegularizedBeta(
        CONFIDENCE, 1 + Math.max(player1Wins, player2Wins) + draws * 0.5,
        1 + Math.min(player1Wins, player2Wins) + draws * 0.5);
    System.out.println(String.format(
        "We compute that if we ran infinitely many games, we're %.1f%% sure the "
            + "winner would win at least %.1f%% of them.",
        100 * (1 - CONFIDENCE), 100 * minStrength));
  }
}
