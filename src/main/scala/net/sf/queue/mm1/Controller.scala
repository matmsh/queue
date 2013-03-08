package net.sf.queue.mm1

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.agent.Agent
import scala.collection.mutable.ListBuffer


trait ControllerMsg
case object SimulationCompleted extends ControllerMsg

/**
 * Start generation of messages
 */
case object StartMsgGeneration extends ControllerMsg

/**
 * Finish generation of messages
 */
case object FinishMsgGeneration extends ControllerMsg

class Controller(lambda:Double, mu:Double, noOfMsgs:Int) extends Actor with ActorLogging {

  val completedMsgsAgent = Agent(ListBuffer[Message]())(context.system)
  val serverActor = context.actorOf(Props(new ServerActor(mu,completedMsgsAgent)), Controller.SERVER)

  val queueActor = context.actorOf(Props(new QueueActor(serverActor,context.self)), Controller.QUEUE)
  val msgGeneratorActor = context.actorOf(Props(new MessageGenerator(lambda,noOfMsgs,queueActor)), Controller.MSG_GENERATOR)
  
  def receive = {

    case StartMsgGeneration => msgGeneratorActor ! StartMsgGeneration

    case SimulationCompleted =>
      log.info("Simulation completed.");
      // context.stop(self) only stops this actor and its childern.
      // It does not stop all threads. 
      //context.stop(self)
      process()
      context.system.shutdown

  }

  def process() {
     val completedMsgs = completedMsgsAgent.get()
     
    // In mills. 1/mu is the average time the server takes to service a msg.
    val deltaT =  ((1000.0/mu)/10.0).toLong;
    val summary = ResultSummary(deltaT, completedMsgs)
    println(summary)
    
  }

}

object Controller {
  val CONTROLLER = "Controller"
  val QUEUE = "Queue"
  val MSG_GENERATOR = "MsgGenerator"
  val SERVER = "Server"

  // Time in millsecs. 
  // 100 means the time is in 1/10 s   
  val TIME_SCALER = 1000L;

}






