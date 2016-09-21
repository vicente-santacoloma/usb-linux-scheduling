package Scheduler;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lavz24
 */
public class Main {
  
  public static void sort(Process arr[]) {
    
    int i;
    
    for(i=arr.length; i>1; i--){
      fnSortHeap(arr, i - 1);
    }

  }

  public static void fnSortHeap(Process array[], int arr_ubound) {
    
    int i, o;
    int lChild, rChild, mChild, root;
    Process temp;
    root = (arr_ubound-1)/2;

    for(o = root; o >= 0; o--) {
      for(i=root;i>=0;i--){
        lChild = (2*i)+1;
        rChild = (2*i)+2;
        
        if((lChild <= arr_ubound) && (rChild <= arr_ubound)) {
          if(array[rChild].getTimeIn() >= array[lChild].getTimeIn())
            mChild = rChild;
          else
            mChild = lChild;
        }
        else{
          if(rChild > arr_ubound)
            mChild = lChild;
          else
            mChild = rChild;
        }

        if(array[i].getTimeIn() < array[mChild].getTimeIn()) {
          temp = array[i];
          array[i] = array[mChild];
          array[mChild] = temp;
        }
      }
    }
    
    temp = array[0];
    array[0] = array[arr_ubound];
    array[arr_ubound] = temp;
  }
  
  
  public static void main(String args[]) {
     
    final BitacoraPanel bitacora = new BitacoraPanel();
    
    java.awt.EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        bitacora.setVisible(true);
      }
    });
    
    Process [] processList = Parser.readProcces(args[1],Integer.parseInt(args[2]));    
    
    sort(processList);
        
    Disk disk = new Disk(Integer.parseInt(args[3]));
    Cpu cpu0 = new Cpu(0,disk);
    Cpu cpu1 = new Cpu(1,disk);
    Simulator simulator = new Simulator(cpu0, cpu1, disk, processList);
    Clock clock = new Clock(cpu0,cpu1,disk,Integer.parseInt(args[0]),simulator);
    cpu0.setSimulator(simulator);
    cpu1.setSimulator(simulator);
    disk.setRescheduler(cpu0);
    simulator.setTicker(clock);
    
    disk.setBitacoraPanel(bitacora);
    cpu0.setBitacoraPanel(bitacora);
    cpu1.setBitacoraPanel(bitacora);
    simulator.setBitacoraPanel(bitacora);
        
    // Start all devices
    disk.start();
    cpu0.start();
    cpu1.start();    
    simulator.start();
    clock.start();
    
  }
}
