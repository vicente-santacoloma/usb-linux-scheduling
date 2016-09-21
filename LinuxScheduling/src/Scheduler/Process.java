/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

/**
 *
 * @author ninina31
 */
public class Process {
  
  private int pid;
  private boolean isRealTime;
  private int priority;
  private int quantum;
  private String state;
  private int cpu;
  private int lastCpu;
  private int cpuTime;
  private int waitingTimeCPU;
  private int waitingTimeDisk;
  private int cpuCycle[];
  private int actualCycle;
  private int timeIn;

  public Process(int pid, boolean isRealTime, int priority, int cpuCycle[], int timeIn) {
    this.pid = pid;
    this.isRealTime = isRealTime;
    this.priority = priority;
    this.cpuCycle = cpuCycle;
    this.actualCycle = 0;
    this.timeIn = timeIn;
    this.lastCpu = -1;
    this.quantum = 0;
  }
  
  public int timeToExec(){
    if(this.actualCycle == -1){
      return 0;
    }else{
      return this.cpuCycle[this.actualCycle];
    }
  }
  
  /**
   * Consumes a quantum of this process.
   * 
   * @return true if the process ended their quantum or left to perform 
   *         I/O, otherwise false
   */
  public boolean consumeQuantum() {
    -- this.quantum;
    -- this.cpuCycle[this.actualCycle];
    
    if(this.cpuCycle[this.actualCycle] == 0) {
      ++ this.actualCycle;
      if(this.actualCycle == cpuCycle.length)
        this.actualCycle = -1;
      return true;
    }
    return false;
  }
  
  /**
   * Add waiting time to the CPU for this process.
   * 
   * @param t the time to wait
   */
  public void addWaitCPU(int t) {
    this.waitingTimeCPU = this.waitingTimeCPU + t;
  }

  /**
   * Add waiting time to the disk for this process.
   * 
   * @param t the time to wait
   */
  public void addWaitDisk(int t) {
    this.waitingTimeDisk = this.waitingTimeDisk + t;
  }
  
  /**
   * Generate the string representation of the process
   * 
   * @return the process as a string 
   */
  @Override
  public String toString() {
    String process;
    process = "Process: \n pid : "+pid+"\n isRealTime : "+isRealTime;
    process = process + "\n Priority : " +priority+" \n cpuCycles : ";
    
    for(int i = 0; i<cpuCycle.length;i++)
      process = process + cpuCycle[i]+" , ";
    
    process = process + "\n TimeIn : "+timeIn +"\n";
    process = process + "Quantum : " + quantum +"\n";
    return process;
  }
  
  /**
   * Generate the string representation of the pid and timeIn
   * 
   * @return the pid and timeIn as a string 
   */
  public String toStringWait() {  
    String process = "( ";
    process += pid + " : " + timeIn;
    return process + " ) ";
  }
  
  // Getters and Setters
  
  public int getActualCycle() {
    return actualCycle;
  }

  public void setActualCycle(int actualCycle) {
    this.actualCycle = actualCycle;
  }

  public int getCpu() {
    return cpu;
  }

  public void setCpu(int cpu) {
    this.cpu = cpu;
  }

  public int[] getCpuCycle() {
    return cpuCycle;
  }

  public void setCpuCycle(int[] cpuCycle) {
    this.cpuCycle = cpuCycle;
  }

  public int getCpuTime() {
    return cpuTime;
  }

  public void setCpuTime(int cpuTime) {
    this.cpuTime = cpuTime;
  }

  public boolean getIsRealTime() {
    return isRealTime;
  }

  public void setIsRealTime(boolean isRealTime) {
    this.isRealTime = isRealTime;
  }

  public int getLastCpu() {
    return lastCpu;
  }

  public void setLastCpu(int lastCpu) {
    this.lastCpu = lastCpu;
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int getQuantum() {
    return quantum;
  }

  public void setQuantum(int quantum) {
    this.quantum = quantum;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public int getTimeIn() {
    return timeIn;
  }

  public void setTimeIn(int timeIn) {
    this.timeIn = timeIn;
  }

  public int getWaitingTimeCPU() {
    return waitingTimeCPU;
  }

  public void setWaitingTimeCPU(int waitingTimeCPU) {
    this.waitingTimeCPU = waitingTimeCPU;
  }

  public int getWaitingTimeDisk() {
    return waitingTimeDisk;
  }

  public void setWaitingTimeDisk(int waitingTimeDisk) {
    this.waitingTimeDisk = waitingTimeDisk;
  }
     
}