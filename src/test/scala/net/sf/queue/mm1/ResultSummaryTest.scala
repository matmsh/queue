package net.sf.queue.mm1

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.junit.Assert._;
import scala.collection.mutable.ListBuffer

class ResultSummaryTest extends AssertionsForJUnit {
  @Test
  def test {
    val msg0 = Message(0, 1, 4, 10)
    val msg1 = Message(1, 4, 7, 13)
    val msg2 = Message(2, 10, 12, 15)
    val msg3 = Message(3, 15, 17, 20)
    val msg4 = Message(4, 20, 23, 25)

    val msgs = ListBuffer(msg0, msg1, msg2, msg3, msg4)
    val deltaT = 5;
    val summary = ResultSummary(5, msgs)
    val tol = 0.001

    assertEquals(1.458, summary.averageMsgInSystem, tol)
    assertEquals(1.041, summary.averageMsgInQueue, tol)
    assertEquals(0.4167, summary.averageMsgInServer, tol)

    assertEquals(0.0066, summary.averageTimeInSystem, tol)
    assertEquals(0.0026, summary.averageTimeInQueue, tol)
    assertEquals(0.0046, summary.averageTimeInServer, tol)
  }
  
  
  
  
}