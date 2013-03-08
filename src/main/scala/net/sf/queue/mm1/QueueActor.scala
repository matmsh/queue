package net.sf.queue.mm1

import akka.actor.Actor
import akka.actor.ActorLogging
import scala.collection.mutable.Queue
import akka.actor.ActorRef

/**
 * In testing, dummy serverActor, dummy controller are used.
 */
class QueueActor(serverActor:ActorRef, controller:ActorRef) extends Actor with ActorLogging {

  val queue = new Queue[Message];

  var serverStatus: ServerStatus = Available;
  
  var msgGeneratorStopped = false;

  def receive = {

    case msg @ Message(_, _, 0, 0) => {
      log.info("Arrived in queue : msg=" + msg);
      queue.enqueue(msg);
      sendMsgToServer
    }

    // Message from server.
    case Available => {
      serverStatus = Available;
      if (queue.isEmpty) {       
        if (msgGeneratorStopped) {
           log.info("queue is empty : msgGeneratorStopped =" + msgGeneratorStopped)
          controller ! SimulationCompleted
        }
      } else {
        sendMsgToServer;
      }
    }

    // Message from Msg generator.
    case FinishMsgGeneration => {
      log.info("Msg generation stopped");
      msgGeneratorStopped = true;
    }
  }

  private def sendMsgToServer() {
    if (serverStatus == Available) {
      serverActor ! queue.dequeue
      serverStatus = Busy
    }
  }

}