package roy.ha;

import java.awt.List;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calculations {

	//Take csv files calculates attributes and (a decidir) either returns array on each calculation or creates a CSV with all the results (preferred)
	//So from class start execute void generateCSV 
	
	private ArrayList<String> labels;
	private ArrayList<String[]> sensor;
	
	private static final int samplesNumber=300;//(Window length 3sec (300 samples)
	private static final double windowOverlap=0.8;
	private static final double inverseOverlap=0.2;
	
	
	public Calculations(ArrayList<String> labels, ArrayList<String[]> sensor) {
		this.labels=labels;
		this.sensor=sensor;
	}
	
	public void generateCsv() throws Exception{
		//Generates a CSV file with the windows. Which includes the attributes:label, and calculated parameters
		
		for(int i=0;i<3;i++) {
			calc(i);
			//x y z on different csv? and merge them for final common features? search csv merger.
		}
		
		
		
		labelsAvg();
		
	}
	
	public void calc(int column) throws Exception{
		double xSummatory=0;
		double sumaCuadratica=0;
		double zcrSum=0;
		double max=0;
		double min=0;
		HashMap<Integer, String> hmcolumn = new HashMap<Integer, String>();
		hmcolumn.put(0, "x");
		hmcolumn.put(1, "y");
		hmcolumn.put(2, "z");
		PrintWriter writer = new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\features"+hmcolumn.get(column)+".csv");
		
		
		for(int i=0; i<sensor.size()-samplesNumber;i+=(int)(samplesNumber*inverseOverlap)) {
			for(int j=0;j<samplesNumber;j++) {
				//arraylist(i+j)-->outputArrayList
				
				//Mean 
				double valuex=Double.parseDouble(sensor.get(i+j)[column]);
                xSummatory+=valuex;
                sumaCuadratica+=Math.pow(valuex, 2);
                if(valuex>max) {
                	max=valuex;
                }
                if(valuex<min) {
                	min=valuex;
                }
                
                if(j!=0) {
                	//Zero crossing rate
                	zcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[column]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[column])));
                	
                	
                }
                
                
			}
			
			//Mean
			double xMean=xSummatory/samplesNumber;
		
			
			//Standard deviation
			double[] sumArray=summatory(xMean,i,column);
			double sd=Math.sqrt(sumArray[1]/sumArray[0]);
			
			//Skewness
			double sk=(sumArray[2]/sumArray[0])/(Math.pow(sd, 3));
			
			
			//Zero crossing rate
			double zcr=zcrSum/(2*(samplesNumber-1));
			
			
			//Mean crossing rate
			double mcr=sumArray[3]/(2*(samplesNumber-1));
			
			
			//RMS
			double rms=Math.sqrt(sumaCuadratica/samplesNumber);
			
			
			//Energy
			double energy=Math.sqrt(sumArray[1]);
			
			
			//Range
			double range=max-min;
			
			
			//Median
			
			
			//Write line
			writer.println(xMean+","+sd+","+sk+","+zcr+","+mcr+","+rms+","+energy+","+range);
			//Put 0 variables
			xSummatory=0;
			sumaCuadratica=0;
			zcrSum=0;
			max=0;
			min=0;
			
		}
		
		writer.close();
	}
	

	
	public double[] summatory(double mean, int i, int column) {
		double[] result=new double[4];
		double summatory=0;
		double summatory2=0;
		int counter=0;
		double mcrSum=0;
		double energy=0;
		
		for(int j=0;j<samplesNumber;j++) {
			double valuex=Double.parseDouble(sensor.get(i+j)[column]);
            summatory+=Math.pow((valuex-mean),2);
            summatory2+=Math.pow((valuex-mean),3);
            
            //Mean crossing rate
            if(j!=0) {
            	mcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[column])-mean)-Math.signum(Double.parseDouble(sensor.get(i+j-1)[column])-mean));
            }
        	
            counter++;
		}
		result[0]=counter;
		result[1]=summatory;
		result[2]=summatory2;
		result[3]=mcrSum;
		return result;
	}
	
	public void labelsAvg() {
		
	}
	
	public void xyzFeatures() {
		
	}
	
	public void setSensor(ArrayList<String[]> sensor) {
		this.sensor=sensor;
	}
	
	
	
}
