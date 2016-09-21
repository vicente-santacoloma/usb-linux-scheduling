package Scheduler;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author lavz24
 */
public class Parser {

  private static String getTagValue(String sTag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
    Node nValue = (Node) nlList.item(0);

    return nValue.getNodeValue();

  }

  public static Process[] readProcces(String fileDirection, int numProcess) {
    
    Process[] processList = null;
    
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new File(fileDirection));
      doc.getDocumentElement().normalize();

      //
      
      NodeList listaProcesos = doc.getElementsByTagName("process");

      
      Process processItem;
      int pid = 0, priority = 0, timeIn = 0;
      boolean isRealTime = false;
      int[] cpuCycle = null;

      //REvisar numProcess == tamano de la lsita
      int numberProcess = 0;
      
      if (numProcess <= listaProcesos.getLength()) {
        numberProcess = numProcess;
      }
      else
      {
        numberProcess = listaProcesos.getLength();
      }
      
      processList = new Process[numberProcess];
      
      for (int i = 0; i < numberProcess; i++) {


        Node process = listaProcesos.item(i);

        if (process.getNodeType() == Node.ELEMENT_NODE) {

          Element elemento = (Element) process;

          pid = Integer.parseInt(getTagValue("pid", elemento));

          int bool = Integer.parseInt(getTagValue("isRealTime", elemento));
          if (bool == 0) {
            isRealTime = false;
          } else if (bool == 1) {
            isRealTime = true;
          } else {
          }

          priority = Integer.parseInt(getTagValue("priority", elemento));
          timeIn = Integer.parseInt(getTagValue("timeIn", elemento));
          
           NodeList nlList = elemento.getElementsByTagName("cycle");
           cpuCycle = new int[nlList.getLength()];
           for(int j = 0; j < nlList.getLength();j++)
           {
             Node nValue = (Node) nlList.item(j).getChildNodes().item(0);
             cpuCycle[j] =  Integer.parseInt(nValue.getNodeValue());
             
           }
           
          processItem = new Process(pid, isRealTime, priority, cpuCycle, timeIn);
       
          processList[i] = processItem;
        }
      }
      //

    } catch (Exception e) {
      e.printStackTrace();
    }

    return processList;
  }
}
