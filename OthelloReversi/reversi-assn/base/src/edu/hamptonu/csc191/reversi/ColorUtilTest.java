package edu.hamptonu.csc191.reversi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ColorUtil class.
 */
class ColorUtilTest {
  @Test
  void oppositeColorValid() {
    Assertions.assertEquals(Color.WHITE, ColorUtil.oppositeColor(Color.BLACK));
    Assertions.assertEquals(Color.BLACK, ColorUtil.oppositeColor(Color.WHITE));
  }

  @Test
  void oppositeColorInvalid() {
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> { ColorUtil.oppositeColor(Color.NONE); });
  }
}