package net.sf.queue.mm1

import junit.framework.TestCase
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.actor.Props
import akka.testkit.TestProbe
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.matchers.MustMatchers
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import com.typesafe.config.ConfigFactory
import org.scalatest.junit.JUnitRunner
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import junit.framework.Assert
import scala.concurrent.duration._ // For the Duration DSL

@RunWith(classOf[JUnitRunner])
class MessageGeneratorTest extends TestKit(ActorSystem("TestMsgGen"))
  with ImplicitSender
  with WordSpec
  with MustMatchers {

  
  /**
   * Test the messages are generated at interval with an exp(lambda) distribution.
   */
  "Msg generator" should {
    "generate the given no of messages and at specified rate" in {
      
      // The interval between messages is exp(lambda) with mean 1/lambda
      val lambda = 100;
      val noOfMsgs = 1000;

      val dummyQueueActor = this.testActor
      val msgGenerator = system.actorOf(Props(new MessageGenerator(lambda, noOfMsgs, dummyQueueActor)))
      msgGenerator ! StartMsgGeneration

      // Expect noOfMsgs to send to dummyQueueActor with 10sec. 
      val msgs = receiveN(noOfMsgs,  120.seconds) //Duration(120, TimeUnit.SECONDS))

      msgs.foreach(msg => assert(msg.isInstanceOf[Message]))

     
      val arrivalTimes =  msgs.map( obj => obj.asInstanceOf[Message].startQueue)
            
      val scaleFactor = 1.0/(1000.0*(msgs.size-1))
     
      // Average intervals between two msgs in seconds. 
      val averageM = (arrivalTimes.tail zip arrivalTimes.init).map( p => p._1 - p._2).sum*scaleFactor
      
      val tol = 0.06/lambda
      Assert.assertEquals(1.0/lambda,averageM, tol)
      
      
    }
  }

}












