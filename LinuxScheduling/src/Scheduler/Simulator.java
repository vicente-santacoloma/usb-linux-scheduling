/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author ninina31
 */
public class Simulator extends Thread {
  
  private Cpu cpu0;
  private Cpu cpu1;
  private Disk hardDrive;
  private PriorityQueue<Process> readyQueue;
  private PriorityQueue<Process> finishedQueue;
  private Clock ticker;
  private Process process[];
  private int tickCounter;
  private int nextProcess;
  private BitacoraPanel bitacora;

  public Simulator(Cpu cpu0, Cpu cpu1, Disk hardDrive, Process process[]) {
    this.cpu0 = cpu0;
    this.cpu1 = cpu1;
    this.hardDrive = hardDrive;
    this.tickCounter = 0;
    this.nextProcess = 0;
    this.process = process;
    this.readyQueue = new PriorityQueue<Process> (process.length+1,
                                              new ProcessComparator());
    this.finishedQueue = new PriorityQueue<Process> (process.length+1,
                                              new FinishedComparator());
  }
  
  /**
   * Run the simulator.
   */
  @Override
  public void run() {
    timerInterrupt();
  }
  
  /**
   * Add a process to the ready queue.
   * 
   * @param p process to add
   */
  public synchronized void addProcess(Process p) {
    this.readyQueue.add(p);
    this.bitacora.colaPL.setText(readyQueue_toString());
  }
  
  /**
   * Add a process to the finished queue.
   * 
   * @param p process to add
   */
  public synchronized void addFinishedProcess(Process p) {
    this.finishedQueue.add(p);
  }
  
  /**
   * Perform the simulator activities.
   */
  public synchronized void timerInterrupt() {
    Iterator<Process> it;
    while(true) {
      try {
        wait();
      } catch(InterruptedException ie) {
        // Missing exception
      }
      
      bitacora.timeQ.setText(Integer.toString(tickCounter));
      // Review all processes to be added to the ready queue when they arrive
      for(int i = this.nextProcess; i < process.length; i++) {
        if(process[i].getTimeIn() == tickCounter) {
          addProcess(process[i]);
          bitacora.colaPE.setText(processIn_toString(i));
          this.nextProcess = i+1;
        }
      }
      bitacora.colaPL.setText(readyQueue_toString());
      //Check if the execution of all processes has ended
      if(this.readyQueue.isEmpty() 
         && this.hardDrive.getQ().isEmpty() 
         && (this.finishedQueue.size() == process.length)) {
        //Interrupt the execution of all threads
        this.ticker.interrupt();
        this.hardDrive.interrupt();
        this.cpu0.interrupt();
        this.cpu1.interrupt();

        //Calculate stadistics
        Stadistics st = new Stadistics(finishedQueue, hardDrive.getIdleTime(), cpu0.getIdleTime(),
        cpu1.getIdleTime());
        this.bitacora.setVisible(false);

        final OutPanel outPanel = new OutPanel();

        java.awt.EventQueue.invokeLater(new Runnable() {
          @Override
          public void run() {
            outPanel.setVisible(true);
          }
        });
        st.writeProcessLog();
        st.generateAverageProcessTime();
        outPanel.jLabel4.setText(Integer.toString(st.getIdleCPU0Time()));
        outPanel.jLabel5.setText(Integer.toString(st.getIdleCPU1Time()));
        outPanel.jLabel6.setText(Integer.toString(st.getIdleDiskTime()));
        outPanel.jLabel8.setText(Integer.toString(st.getAverageWProcessTime()));

        //Show the outPanel for stadistics

        return;
      }
      //Iterate over each process on the queue to add a tick on waiting state
      it = this.readyQueue.iterator();
      while(it.hasNext()) {
        it.next().addWaitCPU(1);
      }
      
      tickCounter++;
    }
  }
  
  /**
   * Generate the string representation of the ready queue
   * 
   * @return the ready queue as a string
   */
  private String readyQueue_toString () {
  
    String readyQueue_string = "[ ";
    Iterator<Process> it;
    Process p;
    it = this.readyQueue.iterator();
    while(it.hasNext()) {
      p = it.next();
      readyQueue_string += "(" + p.getPid() + "," + p.getQuantum() + "), ";
    }
    readyQueue_string = readyQueue_string + "]";
    return readyQueue_string;
  }
  
    /**
   * Generate the string representation of the processes that are waiting to enter.
   * 
   * @return the processes that are waiting to enter as a string
   */
  private String processIn_toString (int index) {
  
    String processIn_string = "[ ";

    for(int i = index+1;i < this.process.length;i++){
      
      processIn_string += this.process[i].toStringWait() + " , ";
    }
    processIn_string = processIn_string + "]";
    return processIn_string;
  }
  
  /**
   * Notify this simulator thread.
   */
  public synchronized void Notify () {
    this.notify();
  }
  
  /**
   * Calculate the dynamic priority of a process.
   *  
   * @param p processes that are going to calculate their dynamic priority
   * @return the dynamic priority of the process
   */
  public static int goodness(Process p) {
    if(p == null)
      return -1000;
    
    if(p.getIsRealTime()){
      return 1000 + p.getPriority();
    }else if(p.getQuantum() == 0){
      return 0;
    }
    return p.getQuantum() + p.getPriority();
  }
  
  /**
   * Returns the best process ready queue.
   * 
   * @return the best process ready queue
   */
  public synchronized Process bestProcess() {
    Process p = readyQueue.poll();
    bitacora.colaPL.setText(readyQueue_toString());
    return p;
  }
  
  /**
   * Recalculates the quantum of all processes.
   * 
   * Recalculates the quantum processes of the simulator ready queue and calls
   * to recalculate the disk processes to start the new epoch.
   */
  public synchronized void recalculateQuantum() {
    Iterator<Process> it = readyQueue.iterator();
    Process p;
    Object []temp;
    
    while(it.hasNext()) {
      p = it.next();
      p.setQuantum(p.getQuantum()/2 + p.getPriority());
    }
    
    temp = this.readyQueue.toArray();
    this.readyQueue = new PriorityQueue<Process> (process.length+1,
                                                    new ProcessComparator()); 
    for(Object pr : temp){
      p = (Process)pr;
      this.readyQueue.add(p);
    }
    
    this.hardDrive.recalculateQuantum();
  }

  // Getters and Setters
  
  public BitacoraPanel getBitacoraPanel() {
    return bitacora;
  }

  public void setBitacoraPanel(BitacoraPanel bitacora) {
    this.bitacora = bitacora;
    this.bitacora.colaPE.setText(processIn_toString(-1));
  }

  public Cpu getCpu0() {
    return cpu0;
  }

  public void setCpu0(Cpu cpu0) {
    this.cpu0 = cpu0;
  }

  public Cpu getCpu1() {
    return cpu1;
  }

  public void setCpu1(Cpu cpu1) {
    this.cpu1 = cpu1;
  }

  public PriorityQueue<Process> getFinishedQueue() {
    return this.finishedQueue;
  }

  public void setFinishedQueue(PriorityQueue<Process> finishedQueue) {
    this.finishedQueue = finishedQueue;
  }

  public Disk getHardDrive() {
    return hardDrive;
  }

  public void setHardDrive(Disk hardDrive) {
    this.hardDrive = hardDrive;
  }

  public int getNextProcess() {
    return nextProcess;
  }

  public void setNextProcess(int nextProcess) {
    this.nextProcess = nextProcess;
  }

  public Process[] getProcess() {
    return process;
  }

  public void setProcess(Process[] process) {
    this.process = process;
  }

  public PriorityQueue<Process> getReadyQueue() {
    return readyQueue;
  }

  public void setReadyQueue(PriorityQueue<Process> readyQueue) {
    this.readyQueue = readyQueue;
  }

  public int getTickCounter() {
    return tickCounter;
  }

  public void setTickCounter(int tickCounter) {
    this.tickCounter = tickCounter;
  }

  public Clock getTicker() {
    return ticker;
  }

  public void setTicker(Clock ticker) {
    this.ticker = ticker;
  }
    
}
