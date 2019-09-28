package genetic_algorithm_for_timetables;
import java.util.Random;

public class SearchAlgorithm {

  public double acceptanceProbability(double oldCost, double newCost, double t) {
    if(newCost < oldCost)
      return  1;
    return Math.exp((oldCost - newCost) / t);
  }

  public double rand(){
    Random r = new Random();
    return r.nextInt(1000) / 1000.0;
  }
  
  public Schedule geneticAlgorithm(SchedulingProblem problem, long deadline){
       Schedule solution = problem.getEmptySchedule();
        GeneticSearch gen = new GeneticSearch(problem, deadline);
        solution = gen.getSchedule();
        return solution;   
  }

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
  public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    for (int i = 0; i < problem.courses.size(); i++) {
      Course c = problem.courses.get(i);
      boolean scheduled = false;

      for (int j = 0; j < c.timeSlotValues.length; j++) {
        if (scheduled) break;

        if (c.timeSlotValues[j] > 0) {
          for (int k = 0; k < problem.rooms.size(); k++) {

            if (solution.schedule[k][j] < 0) {
              solution.schedule[k][j] = i;
              scheduled = true;
              break;
            }

          }

        }
      }
    }

    return solution;
  }


}
