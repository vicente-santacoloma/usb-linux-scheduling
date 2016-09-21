/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

/**
 *
 * @author ninina31
 */
public class Cpu extends Thread {
  
  private int id;
  private Process actualProcess;
  private Disk disk;
  private Simulator simulator;
  private boolean mode;
  private int idleTime;
  private BitacoraPanel bitacora;

  public Cpu(int id, Disk disk) {
    this.id = id;
    this.disk = disk;
    this.mode = true;
    this.idleTime = 0;
    this.actualProcess = null;
  }

  /**
   * Run the CPU.
   */
  @Override
  public void run() {
    this.interruptByTimer();
  }
    
  /**
   * Performs the CPU activities for the current process.
   */
  public synchronized void interruptByTimer() {
    while(true) {
      try {
        // Here I wait for the interruption from the timer.
        wait();
      } catch(InterruptedException ie) {
        System.out.println("the cpu "+this.id+" execution has finished");
        return;        
      }
   
      // Check wether the process in the CPU has used all of it's quantum or
      // has finished a CPU cycle.
      if((this.actualProcess != null) && (this.actualProcess.getQuantum() > 0)) {
        
        // The process needs to go to I/O, or it has finished execution.
        if(this.actualProcess.consumeQuantum()){
          if(this.actualProcess.getActualCycle() == -1){
            //get rid if the process.
            System.out.println(this.actualProcess.toString()+"\n\n");
            simulator.addFinishedProcess(this.actualProcess);

            this.actualProcess = null;
          } else {
            //Send the process to I/O.
            disk.addProcess(this.actualProcess);
            this.actualProcess = null;
          }
          setCPBitacora(0);
        } 
      } else {
        // This is the code for the schedule function.
        Process prev = this.actualProcess;

        // If the prev is a real time process, then it is immediately added to the 
        // run queue with a new time quantum.
        if((prev != null) && prev.getIsRealTime()) {
          prev.setQuantum(prev.getPriority());
          this.actualProcess = null;
        }
        
        if(prev != null)
          simulator.addProcess(prev);

        // Select the next process to be located in the CPU.
        this.actualProcess = simulator.bestProcess();
        
        // There are no processes ready to be excuted. Wait until next tick.
        if(this.actualProcess == null) {
          setCPBitacora(0);
          ++ this.idleTime;
        } else {
          // Process selected has no goodness, epoch has ended.
          if(Simulator.goodness(this.actualProcess) == 0) {
            System.out.println("Starting a new epoch");
            simulator.addProcess(this.actualProcess);
            this.actualProcess = null;
            setCPBitacora(0);
            simulator.recalculateQuantum();
          }
        }
      }
      
      //If a process was actually selected after the schedule, then set it's
      //last cpu
      if(this.actualProcess != null){
        this.actualProcess.setLastCpu(this.id);
        setCPBitacora(this.actualProcess.getPid());
      }
    }
  }
    
  /**
   * Evaluate whether a process that ended performing I/O can preempt a
   * process that is in the CPU.
   * 
   * @param p the process that ended performing I/O
   */
  public synchronized void rescheduleIdle(Process p) {
    int goodP = Simulator.goodness(p);
    int good0 = Simulator.goodness(simulator.getCpu0().getActualProcess());
    int good1 = Simulator.goodness(simulator.getCpu1().getActualProcess());
    
    //Check if the last cpu the process used is idle
    if(p.getLastCpu() != -1) {
      if((p.getLastCpu() == 0) && (this.simulator.getCpu0().getActualProcess() == null)) {
        simulator.getCpu0().setActualProcess(p);
        return;
      } else if((p.getLastCpu() == 1) && (this.simulator.getCpu1().getActualProcess() == null)) {
        simulator.getCpu1().setActualProcess(p);
        return;
      }
    }
    //Otherwise check if the process can preempt any cpu
    if(((goodP - good0) > (goodP - good1))) {
      if((goodP - good0) > 0){
        simulator.getCpu0().setActualProcess(p);
        return;
      }
    }else{
      if((goodP - good1 > 0)) {
        simulator.getCpu1().setActualProcess(p);
        return;
      }
    }
    
    // The process can't preempt any CPU, so it has to go to the run queue.
    simulator.addProcess(p);
  }
  
  /**
   * Notify this CPU thread.
   */
  public synchronized void Notify () {
    this.notify();
  }
  
  /**
   * Recalculate idle time for CPU
   */
  public void idleTimeCPU() {
    this.idleTime++;
  }

  /**
   * Set the current pid to the corresponding CPU
   * 
   * @param Pid the current pid
   */
  private void setCPBitacora(int Pid) {
    if (this.id == 0)
      bitacora.pCPU0.setText("(" + Integer.toString(Pid) 
              + (this.actualProcess != null ? ", " + this.actualProcess.getQuantum()
                                              + ", " + this.actualProcess.timeToExec() + ")" 
                                            : ")"));
    else if (this.id == 1)
      bitacora.pCPU1.setText("(" + Integer.toString(Pid) 
              + (this.actualProcess != null ? ", " + this.actualProcess.getQuantum() 
                                                   + ", " + this.actualProcess.timeToExec() + ")" 
                                            : ")"));   
  }
  
  // Getters and Setters
  
  /**
   * Gets the actual process in this CPU.
   * 
   * @return the actual process
   */
  public Process getActualProcess() {
    return actualProcess;
  }

  /**
   * Sets a process to this CPU.
   * 
   * @param actualProcess the process to take the CPU
   */
  public synchronized void setActualProcess(Process actualProcess) {
    if(this.actualProcess != null){
      simulator.addProcess(this.actualProcess);
    }
    this.actualProcess = actualProcess;
    setCPBitacora(this.actualProcess.getPid());
  }
  
  public BitacoraPanel getBitacoraPanel() {
    return bitacora;
  }

  public void setBitacoraPanel(BitacoraPanel bitacora) {
    this.bitacora = bitacora;
  }

  public Disk getDisk() {
    return disk;
  }

  public void setDisk(Disk disk) {
    this.disk = disk;
  }

  public int getCPUId() {
    return id;
  }

  public void setCPUId(int id) {
    this.id = id;
  }

  public int getIdleTime() {
    return idleTime;
  }

  public void setIdleTime(int idleTime) {
    this.idleTime = idleTime;
  }

  public boolean isMode() {
    return mode;
  }

  public void setMode(boolean mode) {
    this.mode = mode;
  }

  public Simulator getSimulator() {
    return simulator;
  }

  public void setSimulator(Simulator simulator) {
    this.simulator = simulator;
  }

}
