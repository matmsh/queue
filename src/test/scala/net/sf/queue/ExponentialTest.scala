package net.sf.queue

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._;
import org.apache.commons.math.distribution.ExponentialDistributionImpl

class ExponentialTest extends AssertionsForJUnit {

  @Test
  def test {
    // If X is exp(lambda), EX =1/lambda
    val lambda = 4.0;
    val mean =1/lambda;
    val exp = new ExponentialDistributionImpl(mean);
    val n = 100000
    val simulatedMean = (1 to n).map(i => exp.sample()).sum / n

    assertEquals(mean, simulatedMean, 0.002)

  }

}