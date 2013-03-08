package net.sf.queue.mm1

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import junit.framework.Assert
import org.scalatest.junit.JUnitRunner
import akka.testkit.TestProbe


@RunWith(classOf[JUnitRunner])
class QueueActorTest extends TestKit(ActorSystem("TestQueueActor"))
  with ImplicitSender
  with WordSpec
  with MustMatchers {

  
  "Message " should {
    "be sent to server when it available" in {
    

      val dummyServerActor = this.testActor
      val dummyController = this.testActor
      val queueActor = system.actorOf(Props(new QueueActor(dummyServerActor,dummyController)))
      queueActor ! Message(1,0,0,0)

      expectMsg(Message(1,0,0,0))
      
      queueActor ! Available
      queueActor ! Message(2,0,0,0)
      expectMsg(Message(2,0,0,0))
         
    }
  }
  
   
  "No message " should {
    "be sent to server when it is busy" in {
    
      val dummyServerActor = this.testActor
           val dummyController = this.testActor
      val queueActor = system.actorOf(Props(new QueueActor(dummyServerActor,dummyController)))
      queueActor ! Message(1,0,0,0)
      queueActor ! Message(2,0,0,0)
 
      expectMsg(Message(1,0,0,0))
      expectNoMsg
      
    }
  }
  
  
   "A completion  message " should {
    "be sent to the controller " in {
    
      val dummyServerActor = this.testActor
      val dummyController = this.testActor
      val queueActor = system.actorOf(Props(new QueueActor(dummyServerActor,dummyController)))
      val controller = system.actorFor("../../" + Controller.CONTROLLER)
      
      
      queueActor ! Message(2,0,0,0)
      expectMsg(Message(2,0,0,0))
         
      queueActor ! FinishMsgGeneration
      queueActor ! Available
      
     
      expectMsg( SimulationCompleted)
      
      
    }
  }
  

}












