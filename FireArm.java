/*
   @author Safal Adhikari
   @class IT 219
   @date 03/12/2020
   @files: FireArm.java, background2.csv, lib folder for JFreeChar jar files
   @This program will output a graph.jpg file with a line graph of monthly 
    FBI NICS Firearm Background Check Totals for all the states and territories
    of the United States for the given year between 1999 - 2019.
   
   @File Source: https://github.com/BuzzFeedNews/nics-firearm-background-checks/blob/master/README.md   
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NumberFormatException;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FireArm
{
	static int STARTYEAR = 1999;
	static int ENDYEAR = 2019;
	
   //Source File
	static String FILE_NAME = "background2.csv";
	
	static Scanner keyboard;
	
	public static void main(String[] args) throws FileNotFoundException
	{	
		introduction();
		String state = getState();
      String year = getYear();
		
		getTotal(state, year);
		
	}
   
   //Prints an introductory message for the program.
   public static void introduction()
   {
      System.out.println("This program will create a line graph of FBI NICS Firearm Background Check Totals" +
                        "\nfor all the states and territories of the United States for the given year between 1999 - 2019." + 
                        "\n"+
                        "\nThese statistics represent the number of firearm background checks initiated through the NICS" + 
                        "\nThey do not represent the number of firearms sold Based on varying state laws and purchase scenarios," +
                        "\na one-to-one correlation cannot be made between a firearm background check and a firearm sale.");
   }
	
   //Returns the name of the state from the user	
	public static String getState()
	{
		keyboard = new Scanner(System.in);
		
		System.out.print("Enter your State: ");
		String tempState = keyboard.nextLine();
		
		return tempState;
	}
   
   //Returns the year from the user
   public static String getYear()
   {
      keyboard = new Scanner(System.in);
      
      System.out.print("Enter your Year between 1999 - 2019: ");
      String tempYear = keyboard.nextLine();
      
      //Keeps prompting the user until they have enter the year between 1999 - 2019.
      while (!(Integer.valueOf(tempYear) >= STARTYEAR && Integer.valueOf(tempYear) <= ENDYEAR))
      {
         System.out.print("Try again: ");
         tempYear = keyboard.nextLine();
      }     
      return tempYear;  
   }
	
   //Opens the csv file and creates a line graph for the given year and state
	public static void getTotal(String tempState, String tempYear) throws FileNotFoundException, NumberFormatException
	{
		XYSeries xySeries = new XYSeries("Monthly NICS Background Check Totals");
      
		File myFile = new File(FILE_NAME);
      
		Scanner fileInput = new Scanner(myFile);
		String lineArray[];
      
      boolean found = false;
      
		while(fileInput.hasNextLine())
		{
			String line = fileInput.nextLine();
			lineArray = line.split(",");
				
			if(!(lineArray[0].equals("month")))
			{				
				if(tempState.equalsIgnoreCase(lineArray[1]) && tempYear.equals(lineArray[0].substring(0,4)))
				{           
               xySeries.add(Double.valueOf(lineArray[0].substring(5)), Double.valueOf(lineArray[2]));
               found = true;
				}	
			}					
		}
      
      if(found == true)
      {
         XYSeriesCollection dataset = new XYSeriesCollection();
   		dataset.addSeries(xySeries);
   		
   		JFreeChart chart = ChartFactory.createXYLineChart(
               "Monthly FBI NICS Firearm Background Check Totals for " + tempState.toUpperCase() + " on " + tempYear, 
   				"Months", 
   				"Background Check Totals", 
   				dataset, 
   				PlotOrientation.VERTICAL, 
   				true, 
   				true, 
   				false);
   		
   		try 
   		{
   			ChartUtilities.saveChartAsJPEG(new File("graph.jpeg"), chart, 850, 600);
   		}
   		catch(IOException e)
   		{
   			System.err.println("Error in chart generation " + e);
   		}
         System.out.println("Your graph is ready for you to open on graph.jpeg file."+ 
                            "\nIt should be in the same folder as FireArm.java");   
      }
      
      if(found == false)
      {
         System.out.println("No Data Found");
      }  
	}	
}
