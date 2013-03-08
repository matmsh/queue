package net.sf.queue.mm1

import akka.actor.ActorSystem
import akka.actor.Props
import akka.kernel.Bootable

/**
 * Does a M/M/1 simulation. 
 * It is assumed that messages arrive at the queue following the Poisson process with rate lambda.
 * This implies that the interval between arrivals follows the exponential distribution with mean 1/lambda.
 * It is assumed that only one server is serving the messages one at a time. The time to service 
 * one message follows the exponential distribution with mean 1/mu.  
 * 
 * 
 */
class MM1Simulation(lambda:Double, mu:Double, noOfMsg:Int) extends Bootable {
   val system = ActorSystem("MM1Simulation")
    val controller = system.actorOf(Props(new Controller(lambda,mu,noOfMsg)), Controller.CONTROLLER)
  
  
  def startup() {
  }

  def beginSimulation(){
     controller ! StartMsgGeneration
  }
   
  def shutdown() {
    system.shutdown()
  }
}


object MM1Simulation {
  
  def main(args: Array[String]): Unit = {
      // lambda should be smaller than  mu, so that the server, on average, serves 
      // quicker than messages arriving in the queue 
      val lambda = 30.0;
      val mu = 40.0;
      val noOfMsgs =1000;
      val app = new MM1Simulation(lambda, mu, noOfMsgs)
      app.beginSimulation;
    
  }

}