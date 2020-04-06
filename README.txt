Authors: Mincky Feng, Frank Xu, Zhi Qiao, Dennis Liu, Xucheng Shi 

Last modified: 10/04/2019 

------------------------------------------------------------------------------------------------------------------- 

Iteration 5 – L4E Group 8 

------------------------------------------------------------------------------------------------------------------- 

Team Member Contributions: 

Final: 

  Mincky Feng 

  Dennis Liu  Report 

  Zhi Qiao  ReadMe 

  Xucheng Shi 

  Frank Xu  GUI, Test for both system 

Iteration 3: 

  Mincky Feng Update Scheduler 

  Dennis Liu  Update elevator 

  Zhi Qiao  UML diagrams sketching 

  Xucheng Shi Update Scheduler 

  Frank Xu  Update floor subsystem 

Iteration 2: 

  Mincky Feng Update Elevator subsystem and Scheduler, Test for both system 

  Dennis Liu  Update Elevator subsystem, UML Sequence and class diagram 

  Zhi Qiao  Floor subsystem and test development 

  Xucheng Shi Update Elevator subsystem and Scheduler, Test for both system 

  Frank Xu  State machine diagrams sketching 

 

Iteration 1: 

  Mincky Feng UML class and sequence diagram skectching 

  Dennis Liu  Scheduler and floor subsystems development 

  Zhi Qiao  Elevator raw data acquirement, floor subsystem development 

  Xucheng Shi UML class and sequence diagram skectching 

  Frank Xu  Project management, elevator and scheduler subsystems development 

------------------------------------------------------------------------------------------------------------------- 

The Project Containing:  

  3 subsystems/directories located in elevator/src/ca/carleton/winter2020/sysc3303a/group8/ (elevator, floor, scheduler)  

  there are total of 8 .java source files.  

  instructions for how to open and run files.  

---------------------------------------------------------------------------------------------------------------------
Setup (for eclipse): 

  extract the L4EG8.zip file 

  in Eclipse navigate to File > Open Projects from File System and click ‘Directory’ 

  find the ‘L4EG8/elevator/’ directory and click ‘Finish’ 

  the main files to run are ElevatorSubsystem.java, FloorSubsystem.java and Scheduler.java,  

---------------------------------------------------------------------------------------------------------------------
Running:  

  note: the Scheduler.java and ElevatorSubsystem.java need to be run before FloorSubsystem.java 

  In Eclipse, run the Scheduler.java and ElevatorSubsystem.java files, and then the FloorSubsystem.java file 



  you need to open 3 consoles to show the output of all 3 systems: 

  ElevatorSubsystem prints the floor where the elevator is located, lamp information and door opening and closing actions 

   FloorSubsystem shows which floor lamps have been lit and time-stamped outgoing requests have been sent to the Scheduler 

  Scheduler shows data transfer between all systems 

---------------------------------------------------------------------------------------------------------------------
Elevator Timing Data: 

  (data obtained from Canal Building, record in specs.xlsx) 

  approx. 7s move per floor 

  approx. 4s for door opening and closing 

  approx. 5s stay per floor 

  assuming 50% of requests from the ground floor 

---------------------------------------------------------------------------------------------------------------------
Elevator Algorithm: 

  This system consists of 11 floors and a total of 3 elevators 

  In order to reduce the waiting time, each elevator will place itself on a different floor when it is idle 

  When requesting an elevator floor request, the elevator assigned to pick up / off will be closed 

  The calculated adaptability depends on the current state of the elevator, which is divided into 4 parts 

  The elevator car is going to the destination and the request direction is the same 

  The elevator car is moving towards the destination and the request is in the opposite direction 

  The elevator car has moved away from the requested destination 

  The elevator is idle 


---------------------------------------------------------------------------------------------------------------------
Testing: 

  The junit test is not completely finished 

 
