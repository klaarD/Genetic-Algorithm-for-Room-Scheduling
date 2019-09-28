package Schedule;
import java.util.Random;

/**
 * @author Klara Dvorakova
 * 
 * class impelements genetic search for creating a schedule
 */

public class GeneticSearch {
    
    public static final int POPULATION_SIZE = 20;
    public static final double CROSSOVER_RATE = 0.9;
    public static final double MUTATION_RATE = 0.2;

    SchedulingProblem data; //
    Schedule schedule;
    
    //create initial instance
    GeneticSearch(SchedulingProblem data, long deadline){
        this.data = data;
        Population population;
        population = new Population(data);

        for(int j = 0; j < POPULATION_SIZE; j++){
            Schedule tmp = initSchedule();
            population.addSchedule(tmp);
        } 
        
        for(int i = 0; i < population.schedules.size(); i++){             
                replaceCourses(population.schedules.get(i));
                population.sortByFitness();
        }       
        schedule = population.schedules.get(0);
        
        while(overtime(deadline) == false){
            population = crossoverPopulation(population);
            population = mutatePopulation(population);
            Population tmpPop = new Population(data);
            for(int i = 0; i < population.schedules.size(); i++){
                Schedule s =  deleteDupl(population.schedules.get(i)); // delete courses that are scheduled more than once
                s = scheduleNewCourses(s); // try to schedule courses that are not scheduled
                s = replaceCourses(s); // try to remove courses with 0 in time slot
                tmpPop.addSchedule(s);
            }
            population = tmpPop;
            population = selectTournamentPopulation(population);

            if (data.evaluateSchedule(schedule) < data.evaluateSchedule(population.schedules.get(0))){                
             //   System.out.println("New best: " + data.evaluateSchedule(population.schedules.get(0)) + " " + estimatedTime/60 + ":" + estimatedTime%60);
                schedule = population.schedules.get(0);
            }
        } 
    }
    
    public boolean overtime(long deadline){
        long currentTime = System.currentTimeMillis();
        return (currentTime >= deadline -1000);
    }
    
    /**
     * Creates random schedule
     * @return schedule
     */
    private Schedule initSchedule(){
       Schedule tmp = data.getEmptySchedule();
        for(int i = 0 ; i < data.courses.size(); i++){
            Random r = new Random();
            int x = r.nextInt(data.rooms.size()-1) ;       
            int y = r.nextInt(SchedulingProblem.NUM_TIME_SLOTS-1);
            tmp.schedule[x][y] = i;
        }    
        return tmp;
    }

    /**
     * @return Schedule - best schedule
     */
    public Schedule getSchedule(){
       return schedule;
    }

    /**
     * Create new population - crossover the top 10
     * @param population - current population
     * @return Population - new population
     */
    private Population crossoverPopulation(Population population){ 
        Population pop = population;
        pop.addSchedule(crossover(population.schedules.get(0), population.schedules.get(1)));
        if(CROSSOVER_RATE > Math.random()) pop.addSchedule(crossover(population.schedules.get(1), population.schedules.get(2)));
        if(CROSSOVER_RATE > Math.random()) pop.addSchedule(crossover(population.schedules.get(2), population.schedules.get(3)));        
        if(CROSSOVER_RATE > Math.random()) pop.addSchedule(crossover(population.schedules.get(4), population.schedules.get(0)));
        if(CROSSOVER_RATE > Math.random()) pop.addSchedule(crossover(population.schedules.get(5), population.schedules.get(2)));
        if(CROSSOVER_RATE > Math.random())pop.addSchedule(crossover(population.schedules.get(1), population.schedules.get(4)));      
        if(CROSSOVER_RATE > Math.random()) pop.addSchedule(crossover(population.schedules.get(0), population.schedules.get(7)));        
        if(CROSSOVER_RATE > Math.random() )pop.addSchedule(crossover(population.schedules.get(8), population.schedules.get(1)));       
        if(CROSSOVER_RATE > Math.random() )pop.addSchedule(crossover(population.schedules.get(9), population.schedules.get(0)));
        return pop;
    }
    
    /**
     * Crossover two schedules - crossover depends on random number
     * @param s1 first schedule
     * @param s2 second schedule
     * @return final schedule
     */
    private Schedule crossover(Schedule s1, Schedule s2){
        Schedule s =  data.getEmptySchedule();
        for(int i = 0; i < data.rooms.size(); i++){            
            for(int j = 0; j < SchedulingProblem.NUM_TIME_SLOTS; j++){
                if(Math.random() > 0.5) {
                    s.schedule[i][j] = s1.schedule[i][j];
                }
                else s.schedule[i][j] = s2.schedule[i][j];
            }
        }
        return s;
    }
    
    /**
     * Select the top 8 schedules of the population + 2 not that good
     * If there is not enough schedules to select, generate random ones to get to 10 schedules
     * @param pop - current population
     * @return new population
     */
    private Population selectTournamentPopulation(Population pop){ //select 10 best schedules from the population
        while(pop.schedules.size() < 10){
            pop.addSchedule(initSchedule());
        }
        Schedule s1 = pop.schedules.get(pop.schedules.size()-1);
        Schedule s2 = pop.schedules.get(pop.schedules.size()-5);
         pop.schedules.subList(8, pop.schedules.size()).clear();
         pop.addSchedule(s1);
         pop.addSchedule(s2);
        while(pop.schedules.size() < 10){
            pop.addSchedule(initSchedule());
        }
         return pop;
    }
    
    /**
     * Randomly mutate current population
     * Dont mutate the best 3 schedules of the population
     * @param population current population
     * @return new population
     */
    private Population mutatePopulation(Population population){
        for(int i = 3; i < population.schedules.size(); i++){
            if(MUTATION_RATE > Math.random()){
                population.schedules.remove(population.schedules.get(i));
                population.addSchedule(initSchedule());
            }
        }
        return population;
    }
    
    /**
     * if a course is schedule to a timeslot where it has value 0, reschedule its timeslot
     * @param s current schedule
     * @return new schedule
     */
    private Schedule replaceCourses(Schedule s){
        for(int i = 0; i < data.rooms.size();i++){
            for(int j = 0; j < SchedulingProblem.NUM_TIME_SLOTS; j++){
                if(s.schedule[i][j] != -1 && data.courses.get(s.schedule[i][j]).timeSlotValues[j] == 0){
                    int value = 0;
                    int pos = j;
                    for(int k = 0; k < SchedulingProblem.NUM_TIME_SLOTS; k++){
                        if(s.schedule[i][k] == -1 && data.courses.get(s.schedule[i][pos]).timeSlotValues[k] > value){
                            s.schedule[i][k] = s.schedule[i][pos];
                            s.schedule[i][pos] = -1;
                            value = data.courses.get(s.schedule[i][k]).timeSlotValues[k];
                            pos = k;      
                        } 
                    }
                }
            }
        }
        return s;
    }
    
    /**
     * Finds courses that are not scheduled and tries to schedule them
     * @param s current schedule
     * @return new schedule
     */
    private Schedule scheduleNewCourses(Schedule s){
        //find not scheduled courses
        boolean[] scheduled = new boolean[data.courses.size()];
        for(int j = 0; j < data.courses.size(); j++) scheduled[j] = false;
        for(int i = 0; i < data.rooms.size(); i++){
            for (int j = 0; j < SchedulingProblem.NUM_TIME_SLOTS; j++){
                   if(s.schedule[i][j] != -1){
                       scheduled[s.schedule[i][j]] = true;
                   }
            }
        }
        for(int j = 0; j < data.courses.size(); j++){
            boolean added = false;
            if(!scheduled[j]){
                for(int i = 0; i < data.rooms.size(); i++){
                    for(int k = 0; k < SchedulingProblem.NUM_TIME_SLOTS; k++){
                        if(s.schedule[i][k] == -1){
                            s.schedule[i][k] = j;
                            added = true;
                            break;
                        }
                    }
                    if(added == true) break;                   
                }               
            }
        }       
        return s;
    }
    
    /**
     * Removes duplicite courses from a given schedule
     * @param s current schedule
     * @return new schedule
     */
    private Schedule deleteDupl(Schedule s){
        //find courses that are scheduled more than once
        int[] assigned = new int[data.courses.size()];
        for (int i = 0; i < data.rooms.size(); i++) {
            for (int j = 0; j < SchedulingProblem.NUM_TIME_SLOTS; j++) {
                if (s.schedule[i][j] > 0) {
                    assigned[s.schedule[i][j]]++;
                    if (assigned[s.schedule[i][j]] > 1) 
                      s.schedule[i][j] = -1;
                }
             }
        }       
        return s;
    }
       
    private void printPopulation(Population population){
          for(int i = 0; i < population.schedules.size(); i++){
            System.out.println("score: " + data.evaluateSchedule(population.schedules.get(i)));
        }      
    }
    
    private void printSchedule(Schedule s){
        for(int i = 0; i < data.rooms.size(); i++){
                for(int j = 0; j < SchedulingProblem.NUM_TIME_SLOTS; j++){
                    System.out.print(s.schedule[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();     
    }
    
}
