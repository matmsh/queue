This Akka simulation does a M/M/1 Queue simulation.

To start a simulation, run MM1Simmulation.scala. 

Sample out put for  lambda = 30.0, mu = 40.0 : 

Mean no of message in system:2.208138517691774
Mean no of message in queue:1.4944948870320653
Mean no of message in server:0.7430846731621972
Mean time (in sec) spend in system:0.072201
Mean time (in Sec) spend in queue:0.048562
Mean time (in sec) spend in server:0.023639






-------------------------------------------
The following example is from R : 

i_mm1 <- NewInput.MM1(lambda=30, mu=40, n=0)

o_mm1 <- QueueingModel(i_mm1)


The probability (p0, p1, ..., pn) of the n = 0 clients in the system are:
0.25
The traffic intensity is: 0.75
The server use is: 0.75
The mean number of clients in the system is: 3
The mean number of clients in the queue is: 2.25
The mean number of clients in the server is: 0.75
The mean time spend in the system is: 0.1
The mean time spend in the queue is: 0.075
The mean time spend in the server is: 0.025
The mean time spend in the queue when there is queue is: 0.1
The throughput is: 30
> 


