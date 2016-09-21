/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

/**
 *
 * @author ninina31
 */
public class Clock extends Thread {
  
  private Cpu cpu0;
  private Cpu cpu1;
  private Disk hardDrive;
  private int timeTick;
  private Simulator s;
  private BitacoraPanel bitacora;

  public Clock(Cpu cpu0, Cpu cpu1, Disk hardDrive, int timeTick, Simulator s) {
    this.cpu0 = cpu0;
    this.cpu1 = cpu1;
    this.hardDrive = hardDrive;
    this.timeTick = timeTick;
    this.s = s;
  }

  /**
   * Notifies the simulator, disk and CPUs, the end of clock tick.
   */
  @Override
  public void run() {
   int i = 0;
   while(true) {
      System.out.println("Tick: "+i);
      this.cpu0.Notify();
      this.cpu1.Notify();
      this.hardDrive.Notify();
      this.s.Notify();
      try {
        Clock.sleep(this.timeTick*1000);
      } catch (InterruptedException ex) {
        System.out.println("the clock execution has finished");
        return;
      }
      i++;
   } 
  }

  public BitacoraPanel getBitacora() {
    return bitacora;
  }

  public void setBitacora(BitacoraPanel bitacora) {
    this.bitacora = bitacora;
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

  public Disk getHardDrive() {
    return hardDrive;
  }

  public void setHardDrive(Disk hardDrive) {
    this.hardDrive = hardDrive;
  }

  public Simulator getS() {
    return s;
  }

  public void setS(Simulator s) {
    this.s = s;
  }

  public int getTimeTick() {
    return timeTick;
  }

  public void setTimeTick(int timeTick) {
    this.timeTick = timeTick;
  }
  
}