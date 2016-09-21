/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author ninina31
 */
public class Disk extends Thread {
  
  private LinkedList<Process> q;
  private int accessTime; // Tiempo que va a durar la entrada/salida
  private int currentProcessTime; // Tiempo que lleva el proceso actual haciendo entrada/salida
  private Cpu rescheduler;
  private int idleTime;
  private BitacoraPanel bitacora;

  public Disk(int accessTime) {
    this.accessTime = accessTime;
    this.currentProcessTime = 0;
    this.idleTime = 0;
    this.q = new LinkedList<Process> ();
  }
  
  /**
   * Run the disk.
   */
  @Override
  public void run(){
    this.timerInterrupt();
  }
  
  /**
   * Add a process to the disk queue.
   * 
   * @param p process to add
   */
  public synchronized void addProcess(Process p) {
    q.add(p);
    if(q.size() == 1)
      this.bitacora.colaPD.setText(DiskQueue_toString(false));
    else 
      this.bitacora.colaPD.setText(DiskQueue_toString(true));
  }
  
  /**
   * Performs the disk activities for the current process.
   */
  public synchronized void timerInterrupt() {
    Iterator<Process> it;
    
    while(true) {
      try {
        wait();
      } catch(InterruptedException ie) {
        System.out.println("the disk execution has finished");
        return;
      }
      this.bitacora.colaPD.setText(DiskQueue_toString(true));
      
      
      //Add to every process on the waiting queue a tick waiting, except for 
      //the first process, which is the one in execution
      it = this.q.iterator();
      
      //Ommit first element
      if(it.hasNext()) {
        this.bitacora.pDisk.setText("(" + Integer.toString(it.next().getPid())
                + ", " + (this.accessTime - this.currentProcessTime - 1) + ")");
        while(it.hasNext())
          it.next().addWaitDisk(1);
      
        ++ this.currentProcessTime;
      
        if(this.currentProcessTime == this.accessTime) {
          rescheduler.rescheduleIdle(q.poll());
          this.bitacora.colaPD.setText(DiskQueue_toString(false));
          this.bitacora.pDisk.setText(Integer.toString(0));
          this.currentProcessTime = 0;
        }
      } else {
        //No process on the waiting queue, disk is idle, wait for next tick
        ++ this.idleTime;
      }
    }
  }
  
  /**
   * Recalculates the quantum of disk processes.
   */
  public synchronized void recalculateQuantum() {
    Iterator<Process> it = q.iterator();
    Process p;
    
    while(it.hasNext()) {
      p = it.next();
      p.setQuantum(p.getQuantum()/2 + p.getPriority());
    }
    this.bitacora.colaPD.setText(DiskQueue_toString(false));
  }
  
  /**
   * Notify this disk thread.
   */
  public synchronized void Notify () {
    this.notify();
  }
  
  /**
   * Generate the string representation of the disk queue
   * 
   * @return the disk queue as a string
   */
  private String DiskQueue_toString (boolean b) {
  
    String DiskQueue_string = "[ ";
    Iterator<Process> it;
    Process p;
    it = this.q.iterator();
    if(b && it.hasNext())
      it.next();
    while(it.hasNext()){
      p = it.next();
      DiskQueue_string += "(" + p.getPid() + "," + p.getQuantum() + "), ";
    }
    DiskQueue_string = DiskQueue_string + "]";
    return DiskQueue_string;
  }
    
  /**
   * Recalculate idle time for CPU
   */
  public void idleTimeDisk() {
    this.idleTime++;
  }

  // Getters and Setters
  
  public int getAccessTime() {
    return accessTime;
  }

  public void setAccessTime(int accessTime) {
    this.accessTime = accessTime;
  }

  public BitacoraPanel getBitacoraPanel() {
    return bitacora;
  }

  public void setBitacoraPanel(BitacoraPanel bitacora) {
    this.bitacora = bitacora;
  }

  public int getCurrentProcessTime() {
    return currentProcessTime;
  }

  public void setCurrentProcessTime(int currentProcessTime) {
    this.currentProcessTime = currentProcessTime;
  }

  public synchronized LinkedList<Process> getQ() {
    return q;
  }

  public synchronized void setQ(LinkedList<Process> q) {
    this.q = q;
  }

  public Cpu getRescheduler() {
    return rescheduler;
  }

  public void setRescheduler(Cpu rescheduler) {
    this.rescheduler = rescheduler;
  }

  public int getIdleTime() {
    return idleTime;
  }

  public void setIdleTime(int idleTime) {
    this.idleTime = idleTime;
  }
 
}