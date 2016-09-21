/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;
import java.util.Comparator;

/**
 *
 * @author ninina31
 */
public class FinishedComparator implements Comparator<Process> {
  
  /**
   * Compares two processes according to their priority.
   * 
   * @param p1 first  process to compare
   * @param p2 second process to compare
   * @return 1 if p1 pid is greater than p2 pid, -1 if p1 pid is
   *           less than p2 pid, and 0 if p1 pid is equal to p2 
   *           pid
   */
 @Override
  public int compare(Process p1, Process p2) {
      
    if(p1.getPid() > p2.getPid())
      return 1;
    else if (p1.getPid() < p2.getPid())
      return -1;
    return 0;
    
  }
  
}
