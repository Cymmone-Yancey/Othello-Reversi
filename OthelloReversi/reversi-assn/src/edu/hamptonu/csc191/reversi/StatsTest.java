package edu.hamptonu.csc191.reversi;

import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.hamptonu.csc191.reversi.Stats.InvIncompleteRegularizedBeta;
import static org.apache.commons.math3.special.Beta.regularizedBeta;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsTest {

  @Test
  void invIncompleteRegularizedBeta() {
    // Try some basically random parameters; no real rhyme or reason.
    for (double i : List.of(0.1, 0.001, 0.99, 0.5, 0.3, 0.12345)) {
      for (double alpha : List.of(10.0, 100.0, 99.5, 1000.0, 10000.0)) {
        for (double beta : List.of(1.0, 300.0, 1.23456, 1000 * Math.PI)) {
          // Assert within 0.0002 because our code notionally checks that they're
          // within 0.0001.
          assertEquals(
              i,
              regularizedBeta(InvIncompleteRegularizedBeta(i, alpha, beta),
                              alpha, beta),
              0.0002);
        }
      }
    }
  }
}