package genetic_algorithm_for_timetables;

public class Schedule {
  int[][] schedule;

  Schedule(int nRooms, int nTimeSlots) {
    schedule = new int[nRooms][nTimeSlots];
  }  
}