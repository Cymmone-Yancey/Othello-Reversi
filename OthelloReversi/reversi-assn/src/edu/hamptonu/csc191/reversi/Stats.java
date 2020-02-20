package edu.hamptonu.csc191.reversi;

import static org.apache.commons.math3.special.Beta.regularizedBeta;

/**
 * Statistical utility functions.
 */
public class Stats {
  /**
   * Computes the inverse of the regularized incomplete beta function [1] with
   * respect to x.
   *
   * [1] https://en.wikipedia.org/wiki/Beta_function#Incomplete_beta_function
   *
   * @return the value x such that I(x; alpha, beta) == i.
   */
  static double InvIncompleteRegularizedBeta(double i, double alpha,
                                             double beta) {
    // I can't find a library that computes this directly, so I'll just do it
    // via binary search, which works because this function is monotonic.
    final double ABS_ERR = 0.0001;
    double min = 0;
    double max = 1;
    while (true) {
      double guess = (max + min) / 2;
      double val = regularizedBeta(guess, alpha, beta);
      if (Math.abs(val - i) <= ABS_ERR) {
        return guess;
      }
      if (val < i) {
        min = guess;
      } else {
        max = guess;
      }
    }
  }
}
