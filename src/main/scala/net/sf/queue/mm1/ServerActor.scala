package net.sf.queue.mm1

import akka.actor.ActorLogging
import akka.actor.Actor
import org.apache.commons.math.distribution.ExponentialDistributionImpl
import akka.actor.ActorRef
import akka.agent.Agent
import scala.collection.mutable.ListBuffer

trait ServerStatus
case object Available extends ServerStatus
case object Busy extends ServerStatus

class ServerActor(mu: Double, msgAgent: Agent[ListBuffer[Message]]) extends Actor with ActorLogging {
  // mean is in seconds
  val mean = 1.0 / mu;
  val exp = new ExponentialDistributionImpl(mean);

  def receive = {
    case msg @ Message(_, _, _, _) => processMsg(sender, msg)

  }

  private def processMsg(sender: ActorRef, msg: Message): Unit = {
    msg.startService = System.currentTimeMillis();
    // Service time in mills
    val serviceTime = Controller.TIME_SCALER * exp.sample();
    Thread.sleep(serviceTime.toLong);

    msg.endService = System.currentTimeMillis();

    // Add completed msg to agent.
    msgAgent.send(_ += msg)

    sender ! Available
    log.info("completed =" + msg)
  }

}