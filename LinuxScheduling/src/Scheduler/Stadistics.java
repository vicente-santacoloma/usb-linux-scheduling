/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;
import java.util.*;
import java.io.*;

/**
 *
 * @author ninina31
 */
public class Stadistics {
  
  private PriorityQueue<Process> finishedQueue;
  private int idleDiskTime;
  private int idleCPU0Time;
  private int idleCPU1Time;
  private int averageWProcessTime;

  public Stadistics(PriorityQueue<Process> finishedQueue, int idleDiskTime, int idleCPU0Time, int idleCPU1Time) {
    this.finishedQueue = finishedQueue;
    this.idleDiskTime = idleDiskTime;
    this.idleCPU0Time = idleCPU0Time;
    this.idleCPU1Time = idleCPU1Time;
  }

  public int getAverageWProcessTime() {
    return averageWProcessTime;
  }

  public int getIdleCPU0Time() {
    return idleCPU0Time;
  }

  public int getIdleCPU1Time() {
    return idleCPU1Time;
  }

  public int getIdleDiskTime() {
    return idleDiskTime;
  }
  
  public void generateAverageProcessTime() {
    
    Iterator<Process> it = this.finishedQueue.iterator();
    int temp = 0;
    
    while (it.hasNext()) {
      temp = temp + (it.next()).getWaitingTimeCPU();
    }
   
    this.averageWProcessTime = temp/this.finishedQueue.size();
    
  }
  
  public void writeProcessLog() {
  
    try{
      // Create file 
      FileWriter fstream = new FileWriter("ProcessLog.txt");
      BufferedWriter out = new BufferedWriter(fstream);
      out.write("Process WaitingTime RunTime\n\n");
      Iterator<Process> it = this.finishedQueue.iterator();
      int i, temp;
      Process next = null;
      
      while (it.hasNext()) {
        
        temp = 0;
        next = it.next();
        
        for(i = 0; i < next.getCpuCycle().length; i++){
          temp = temp + next.getCpuCycle()[i];
        }
        
        out.write(next.getWaitingTimeCPU() + "   " + next.getWaitingTimeCPU() 
                + "   " + next.getWaitingTimeCPU() + temp + "\n");
      }
      
      //Close the output stream
      out.close();
      }catch (Exception e){//Catch exception
      System.err.println("Error: " + e.getMessage());
      }
  
  }
  
  
  
}
