package genetic_algorithm_for_timetables;
import java.util.ArrayList;

/**
 * @author Klara Dvorakova
 * 
 * Class for saving shcedules of one population
 */
public class Population {
    ArrayList<Schedule> schedules;
    SchedulingProblem problem;
    
    public Population(SchedulingProblem problem){
        this.problem = problem;
        schedules = new ArrayList<>();
    }
    
    /**
     * Method to sort shcedules in the array based on their score
     */
    void sortByFitness(){
        ArrayList<Schedule> tmpList = new ArrayList<>();
        
        for(int i = 0; i < schedules.size(); i++){
            boolean added = false;
            Schedule s = schedules.get(i);
            for(int j = 0; j < tmpList.size(); j++){
                if(problem.evaluateSchedule(tmpList.get(j)) < problem.evaluateSchedule(s)){
                    tmpList.add(j, s);
                    added = true;
                    break;
                }
                else if(problem.evaluateSchedule(tmpList.get(j)) == problem.evaluateSchedule(s)){
                    added = true;
                    break;              
                }
            }
            if(!added) {
                tmpList.add(s);
            }
        }
        schedules = tmpList;
    }
    
    /**
     * Method to add a schedule to the array based on the schedule score
     * @param schedule - to be added to the array
     */
    public void addSchedule(Schedule schedule){
        boolean added = false;
        for(int i = 0; i < schedules.size(); i++){
            if(problem.evaluateSchedule(schedules.get(i)) < problem.evaluateSchedule(schedule)){
                schedules.add(i, schedule);
                added = true;
                break;
            }
            else if(problem.evaluateSchedule(schedules.get(i)) == problem.evaluateSchedule(schedule)){
                if(Double.NEGATIVE_INFINITY == problem.evaluateSchedule(schedule)) schedules.add(i, schedule);
                added = true;
                break;              
            }
        }
        if(!added) schedules.add(schedule);
    }   
}
