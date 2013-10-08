package com.novelbio.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * java 关闭进程
 * @author geidianli
 *
 */
public class KillProcessTest {
	public static void main(String[] args) {
		KillProcessTest.SoftStatus("chrome");
	}
	public static boolean SoftStatus(String command){
		  boolean re=false;
		  try{
		   command="ps -ef";
		   Process child = Runtime.getRuntime().exec(command);
		   BufferedReader ins = new BufferedReader(new InputStreamReader(child.getInputStream()));
		         String c=null;
		         while ((c = ins.readLine()) != null) {
		          //System.out.println("PID:"+c.substring(10,14));
		          if(c.indexOf(command)>0) System.out.println(c);
		         }
		         ins.close();
		  
		   Process child1 = Runtime.getRuntime().exec(command);
		   BufferedReader ins1 = new BufferedReader(new InputStreamReader(child1.getInputStream()));
		         String c1=null;
		         while ((c1 = ins1.readLine()) != null) {
		          System.out.println("PID:"+c1);
		          if(c1.indexOf(command)>0) System.out.println(c1);
		         }
		         ins1.close();
		  }catch(Exception e){
		   System.out.println(e.toString());
		  }
		  
		  System.out.println("SoftStatus() check "+command+" over");
		  return re;
		}


}