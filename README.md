# F2018_AI_A3_Wumpus_World
Create and solve Wumpus caves with AI

#### Joshua Stephenson-Losey, Keefer Sands, Dylan Lynn

### Interesting choices:

* World class to hold each cave instance for cleaner code
* Agent writes the report each step and flags possible dangers in the report in order to avoid hazzerdous locations
* Agent checks the report for attributes of a space rather than the space itself (after the report is written) This was done for ease of implementation and speed as the report is a clean return of only what we want and referenced in a hash table based on location while the cave is indexed and returns extra information.
* Most movement choices are in the same function called noPlan() that will try to go (in order) north, east, south, west. All movement is made by "turning" which checks the desired direction as a valid move and returns a boolean value tell if the step can be taken.

### First Order Logic Rules:

* When danger is found, step back until it is safe and try another route
* If glitter is found seek out gold in the safest/fastest way possible
* When not enough information, depth first search to explore

### Analysis of results:

### Contributions:

* ##### Joshua Stephenson-Losey:
    * Initial code structure
    * Gold search logic
    * Worked together with entire group for pit and Wumpus avoidance

* ##### Keefer Sands:
    * Code cleanup (remove methods and change code structure that should have been written better
    * Worked with entire group for pit and Wumpus avoidance

* ##### Dylan Lynn:
    * Worked with entire group for pit and Wumpus avoidance
