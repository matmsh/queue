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
import akka.agent.Agent
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._ // For the Duration DSL

@RunWith(classOf[JUnitRunner])
class ServerActorTest extends TestKit(ActorSystem("TestServer"))
  with ImplicitSender
  with WordSpec
  with MustMatchers {

  
  "Server " should {
    "process messages at the specified rate" in {
      
      // The process time of each message is exp(mu) with mean 1/mu. 
      val mu = 80;
      val noOfMsgs = 1000;
     
      val completedMsgsAgent = Agent(ListBuffer[Message]())(system)
 
      val dummyQueueActor = this.testActor
      val server = system.actorOf(Props(new ServerActor(mu,completedMsgsAgent)))
      
      (1 to noOfMsgs).foreach( i => server ! Message(i,0,0,0))

      val msgs = receiveN(noOfMsgs, 120.seconds)  // Duration(120, TimeUnit.SECONDS))
      msgs.foreach(msg => msg == Available)
      val completedMsgs = completedMsgsAgent.get()
      
      val processTimes = completedMsgs.map( msg => msg.endService-msg.startService)
      
      // Average process time In secs 
      val processTimeM = processTimes.sum*1.0/(1000.0* noOfMsgs)
      
      val tol = 0.06/mu
      Assert.assertEquals(1.0/mu, processTimeM, tol)
      
      
      
    }
  }

}












