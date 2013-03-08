package net.sf.queue.mm1

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.commons.math.distribution.PoissonDistributionImpl
import org.apache.commons.math.distribution.ExponentialDistributionImpl

/**
 * 
 * startQueue : The arrival time (in milliseconds) of message in the queue.
 * startService : The time at which the server started processing the message.
 * endService : The time at which the server has finished processing the message.  
 */
case class Message(val id:Long,var startQueue:Long=0L, var startService:Long=0L, var endService:Long=0L)

/**
 *  In testing, a dummy queueActor is used.
 */
class MessageGenerator(lambda:Double,noOfMsgs:Int, queueActor:ActorRef) extends Actor with ActorLogging {


  val msgGenerator = new MsgGeneratorImp(lambda, noOfMsgs, queueActor);
  
  def receive ={    
    case StartMsgGeneration => log.info("Received start ");                               
                               msgGenerator.start();
  }
  
  
}


class MsgGeneratorImp(lambda:Double,noOfMsgs:Int, queueActor:ActorRef) extends Thread {
    var count =0
    
    // Mean =1 means 1 sec.
    val mean = 1.0/lambda;
    val exp = new ExponentialDistributionImpl(mean);
    
    
	override def run() {
		while (count < noOfMsgs){		 	     
	      val sleepTimeInMills = (Controller.TIME_SCALER * exp.sample()).toLong; 	    
		  Thread.sleep(sleepTimeInMills);		
	      queueActor ! Message(count,System.currentTimeMillis(),0,0)
		  count = count +1
		  if (count % 100 ==0) println("%s out of %s messages generated".format(count, noOfMsgs)) 
		}
	  
		queueActor ! FinishMsgGeneration
		
	}

}