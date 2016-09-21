/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;
import java.util.Comparator;

/**
 *
 * @author vicente
 */
public class ProcessComparator implements Comparator<Process> {

  /**
   * Compares two processes according to their priority.
   * 
   * @param p1 first  process to compare
   * @param p2 second process to compare
   * @return 1 if p1 priority is greater than p2 priority, -1 if p1 priority is
   *           less than p2 priority, and 0 if p1 priority is equal to p2 
   *           priority
   */
  @Override
  public int compare(Process p1, Process p2) {
      
    if(Simulator.goodness(p1) < Simulator.goodness(p2))
      return 1;
    else if (Simulator.goodness(p1) > Simulator.goodness(p2))
      return -1;
    return 0;
    
  }

}
