package net.sf.queue.mm1

import scala.collection.mutable.ListBuffer

/**
 * An class to summarise  the completed messages.
 * deltaT is in millsecs
 */
class ResultSummary(val deltaT: Long, completedMsgs: ListBuffer[Message]) {

  var averageMsgInSystem: Double = _
  var averageMsgInQueue: Double = _
  var averageMsgInServer: Double = _

  // In seconds 
  var averageTimeInSystem: Double = _
  var averageTimeInQueue: Double = _
  var averageTimeInServer: Double = _

  process();
  
  private def process(): Unit = {
    val noOfMsgs = completedMsgs.size

    averageTimeInSystem = completedMsgs.map(msg => msg.endService - msg.startQueue).sum / (1000.0*noOfMsgs)

    averageTimeInQueue = completedMsgs.map(msg => msg.startService - msg.startQueue).sum / (1000.0*noOfMsgs)

    averageTimeInServer = completedMsgs.map(msg => msg.endService - msg.startService).sum / (1000.0*noOfMsgs)

    val start = completedMsgs.head.startQueue
    val end = completedMsgs.last.endService
    val totalTime = end - start;

    // In mills. 1/mu is the average time the server takes to service a msg.
    //val deltaT =  ((1000.0/mu)/10.0).toLong;

    val timeSteps = (start to end by deltaT);
    val scaleFactor = 1.0 * deltaT / totalTime

    averageMsgInSystem = timeSteps.map(t => completedMsgs.count(msg => msg.startQueue <= t && t <= msg.endService)).sum * scaleFactor

    averageMsgInServer = timeSteps.map(
      t => completedMsgs.count(msg => msg.startService <= t && t <= msg.endService)).sum * scaleFactor

    averageMsgInQueue = timeSteps.map(
      t => completedMsgs.count(msg => msg.startQueue <= t && t <= msg.startService)).sum * scaleFactor

  }

  override def toString(): String = {
    val sb: StringBuilder = new StringBuilder();
    sb.append("Mean no of message in system:" + averageMsgInSystem).append("\n")
    sb.append("Mean no of message in queue:" + averageMsgInQueue).append("\n")
    sb.append("Mean no of message in server:" + averageMsgInServer).append("\n")
    sb.append("Mean time (in sec) spend in system:" + averageTimeInSystem).append("\n")
    sb.append("Mean time (in Sec) spend in queue:" + averageTimeInQueue).append("\n")
    sb.append("Mean time (in sec) spend in server:" + averageTimeInServer).append("\n")

    sb.toString
  }

}

object ResultSummary {

   def apply(deltaT:Long, msgs:ListBuffer[Message]):ResultSummary= new ResultSummary(deltaT, msgs)
  
}









