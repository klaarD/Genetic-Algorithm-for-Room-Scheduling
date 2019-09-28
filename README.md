# Room Scheduling Using Genetic Algorithm

The program creates a schedule that assigns courses to classrooms based on some criteria. 
The goal of the algorithm is to find the best possible schedule as quickly as possible. 
More specifically, we have a set of N rooms, a set of M courses that need to be scheduled, and a set of L buildings. 

Each building has an associated location, given by (x,y) coordinates. 

#### Each room has the following properties:
1)	A building
2)	A maximum capacity

#### Each course has the following properties: 
1)	An enrollment number
2)	A value for being scheduled 
3)	A list of values for each of 10 available time slots
4)	A preferred building

There are 10 possible time slots, and each room can have only one class scheduled in each time slot. In addition, courses can only be scheduled in rooms where the capacity is greater than the enrollment. 

For each course, there is a list of values for each time slot.  A value of 0 corresponds to infeasible (i.e., the course cannot be held at this time), while any other positive value is a bonus given for scheduling the course in that particular time slot. 

Courses also have preferred buildings.  Courses scheduled in another building receive a penalty based on the distance between where the course is actually scheduled and the preferred building. 

A solution is a mapping from rooms and time slots to courses.  That is, each room can be assigned to hold one course in each available time slot. Courses are identified by their indices from 0 to N-1. 

#### The overall value of a schedule is calculated as follows:
1)	NEGATIVE_INFINITY if the schedule is invalid (e.g., courses assigned multiple times to more than one room or time slot).
2)	The sum of the values and time slot bonuses for all courses assigned to valid rooms (rooms with a large enough capacity).
3)	Subtracting the sum of the penalties for scheduling courses away from their preferred building.  



### To run the project from the command line type the following:
 > java -jar "Schedule.jar" 'nBuildings' 'nRooms' 'nCourses' 'time_limit' 'alg_number' 'seed'
  
## Input
#### nBuildings 
   - number of buildings
   - positive integer
#### nRooms 
   - number of rooms
   - positive integer
#### nCourses 
   - number of courses
   - positive integer
#### time_limit 
   - time limit of the algorithm given in seconds
#### alg_number 
   - 0 - naive approach
   - 1 - genetic algorithm
#### seed 
   - random seed
   - long


### Output
- score: maximum score solution found in the given time
    
