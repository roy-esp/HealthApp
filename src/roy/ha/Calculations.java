package roy.ha;

import java.util.List;
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
		
		
		calc();
			
		
		
		
		
		labelsAvg();
		
	}
	
	public void calc() throws Exception{
		
		//X
		double xSummatory=0;
		double xSumaCuadratica=0;
		double xZcrSum=0;
		double xMax=0;
		double xMin=0;
		
		//X
		double ySummatory=0;
		double ySumaCuadratica=0;
		double yZcrSum=0;
		double yMax=0;
		double yMin=0;
		
		//X
		double zSummatory=0;
		double zSumaCuadratica=0;
		double zZcrSum=0;
		double zMax=0;
		double zMin=0;
		
		//XYZ
		double xyzSummatory=0;
		double xyzMax=0;
		
		
		//FREQ
		//X
		double xMaxAmpl=0;
		int xIndexMaxAmpl=0;
		//Y
		double yMaxAmpl=0;
		int yIndexMaxAmpl=0;
		//Z
		double zMaxAmpl=0;
		int zIndexMaxAmpl=0;

		
		PrintWriter writer = new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\features.csv");
		
		
		for(int i=0; i<sensor.size()-samplesNumber;i+=(int)(samplesNumber*inverseOverlap)) {
			
			
			
			int n = (int) Math.pow(2,Math.round(Math.log10(samplesNumber)/Math.log10(2)));
			double[][] timearray=new double[3][n];
			for(int k=0; k<n;k++) {
				if(k<sensor.size()) {
					timearray[0][k]=Double.parseDouble(sensor.get(i+k)[0]);
				}
				else {
					timearray[0][k]=0;
				}
				
			}
			
			//GET FREQ(FFT)
			//X
			FreqCalc2 fft = new FreqCalc2(n);
		    double[] im = new double[n];
		    
		    for(int l=0; l<n; l++) {
		         im[l] = 0;
		       }
		    double[] freqarrayX=FreqCalc2.getFFT(fft, timearray[0], im);
		    double[] freqarrayY=FreqCalc2.getFFT(fft, timearray[1], im);
		    double[] freqarrayZ=FreqCalc2.getFFT(fft, timearray[2], im);
		    
		    for(int m=1; m<freqarrayX.length;m++) {
		    	
		    	//MAX AMPLITUDE
		    	if (freqarrayX[m]>xMaxAmpl) {
		    		xMaxAmpl=freqarrayX[m];
		    		xIndexMaxAmpl=m;
		    	}
		    	if (freqarrayY[m]>yMaxAmpl) {
		    		yMaxAmpl=freqarrayY[m];
		    		yIndexMaxAmpl=m;
		    	}
		    	if (freqarrayZ[m]>zMaxAmpl) {
		    		zMaxAmpl=freqarrayZ[m];
		    		zIndexMaxAmpl=m;
		    	}
		    }
			
			
			for(int j=0;j<samplesNumber;j++) {
		
				//Mean 
				
				//X
				double valuex=Double.parseDouble(sensor.get(i+j)[0]);
                xSummatory+=valuex;
                xSumaCuadratica+=Math.pow(valuex, 2);
                
                //Y
                double valuey=Double.parseDouble(sensor.get(i+j)[1]);
                ySummatory+=valuey;
                ySumaCuadratica+=Math.pow(valuey, 2);
                
                //Z
                double valuez=Double.parseDouble(sensor.get(i+j)[2]);
                zSummatory+=valuez;
                zSumaCuadratica+=Math.pow(valuez, 2);
                
                if(valuex>xMax) {
                	xMax=valuex;
                }
                if(valuey>yMax) {
                	yMax=valuey;
                }
                if(valuez>zMax) {
                	zMax=valuez;
                }
                if(valuex<xMin) {
                	xMin=valuex;
                }
                if(valuey<yMin) {
                	yMin=valuey;
                }
                if(valuez<zMin) {
                	zMin=valuez;
                }
                
                if(j!=0) {
                	//Zero crossing rate
                	xZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[0]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[0])));
                	yZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[1]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[1])));
                	zZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[2]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[2])));
                	
                	
                }
                
                
                //XYZ
                double sqrtxyz=Math.sqrt(Math.pow(valuex, 2)+Math.pow(valuey, 2)+Math.pow(valuez, 2));
                xyzSummatory+=sqrtxyz;
                
                if(sqrtxyz>xyzMax) {
                	xyzMax=sqrtxyz;
                }
                
                
                
			}
			
			
			
			
			//Mean
			double xMean=xSummatory/samplesNumber;
			double yMean=ySummatory/samplesNumber;
			double zMean=zSummatory/samplesNumber;
			double xyzMean=xyzSummatory/samplesNumber;
		
			
			//Standard deviation
			double[] xsumArray=summatory(xMean,i,0);
			double xsd=Math.sqrt(xsumArray[1]/xsumArray[0]);
			
			double[] ysumArray=summatory(yMean,i,1);
			double ysd=Math.sqrt(ysumArray[1]/ysumArray[0]);
			
			double[] zsumArray=summatory(zMean,i,2);
			double zsd=Math.sqrt(zsumArray[1]/zsumArray[0]);
			
			double[] xyzsumArray=summatory(xyzMean,i);
			double xyzsd=Math.sqrt(xyzsumArray[1]/xyzsumArray[0]);
			
			//Skewness
			double xsk=(xsumArray[2]/xsumArray[0])/(Math.pow(xsd, 3));
			double ysk=(ysumArray[2]/ysumArray[0])/(Math.pow(ysd, 3));
			double zsk=(zsumArray[2]/zsumArray[0])/(Math.pow(zsd, 3));
			
			
			//Zero crossing rate
			double xzcr=xZcrSum/(2*(samplesNumber-1));
			double yzcr=yZcrSum/(2*(samplesNumber-1));
			double zzcr=zZcrSum/(2*(samplesNumber-1));
			
			
			//Mean crossing rate
			double xmcr=xsumArray[3]/(2*(samplesNumber-1));
			double ymcr=ysumArray[3]/(2*(samplesNumber-1));
			double zmcr=zsumArray[3]/(2*(samplesNumber-1));
			
			
			//RMS
			double xrms=Math.sqrt(xSumaCuadratica/samplesNumber);
			double yrms=Math.sqrt(ySumaCuadratica/samplesNumber);
			double zrms=Math.sqrt(zSumaCuadratica/samplesNumber);
			
			
			//Energy
			double xenergy=Math.sqrt(xsumArray[1]);
			double yenergy=Math.sqrt(ysumArray[1]);
			double zenergy=Math.sqrt(zsumArray[1]);
			
			
			//Range
			double xrange=xMax-xMin;
			double yrange=yMax-yMin;
			double zrange=zMax-zMin;
			
			
			//Median
			
			
			//Interquartile range
			
			//Correlation
			double xycorrelation=correlation(i,xMean, yMean, xsd, ysd,0);
			double xzcorrelation=correlation(i,xMean, zMean, xsd, zsd,1);
			double yzcorrelation=correlation(i,yMean, zMean, ysd, zsd,2);
			
			
			//Freq
			
		
			
			//Write line
			String xFeatures=xMean+","+xsd+","+xsk+","+xzcr+","+xmcr+","+xrms+","+xenergy+","+xrange;
			String yFeatures=yMean+","+ysd+","+ysk+","+yzcr+","+ymcr+","+yrms+","+yenergy+","+yrange;
			String zFeatures=zMean+","+zsd+","+zsk+","+zzcr+","+zmcr+","+zrms+","+zenergy+","+zrange;
			String xyzFeatures=xyzMean+","+xyzsd+","+xyzMax+","+xycorrelation+","+xzcorrelation+","+yzcorrelation;
			String freqFeatures=xMaxAmpl+","+xIndexMaxAmpl+","+yMaxAmpl+","+yIndexMaxAmpl+","+zMaxAmpl+","+zIndexMaxAmpl;
			writer.println(xFeatures+","+yFeatures+","+zFeatures+","+xyzFeatures+","+freqFeatures);
			
			//Put 0 variables
			xSummatory=0;
			xSumaCuadratica=0;
			xZcrSum=0;
			xMax=0;
			xMin=0;
			
			ySummatory=0;
			ySumaCuadratica=0;
			yZcrSum=0;
			yMax=0;
			yMin=0;
			
			zSummatory=0;
			zSumaCuadratica=0;
			zZcrSum=0;
			zMax=0;
			zMin=0;
			
			xyzSummatory=0;
			xyzMax=0;
			
			
			//PUT 0 FREQ VARIABLES
			//X
			xMaxAmpl=0;
			xIndexMaxAmpl=0;
			//Y
			yMaxAmpl=0;
			yIndexMaxAmpl=0;
			//Z
			zMaxAmpl=0;
			zIndexMaxAmpl=0;
			
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
	
	public double[] summatory(double mean, int i) {
		double[] result=new double[4];
		double summatory=0;
		int counter=0;
		
		for(int j=0;j<samplesNumber;j++) {
			double valuex=Double.parseDouble(sensor.get(i+j)[0]);
			double valuey=Double.parseDouble(sensor.get(i+j)[1]);
			double valuez=Double.parseDouble(sensor.get(i+j)[2]);
			
			double sqrtxyz=Math.sqrt(Math.pow(valuex, 2)+Math.pow(valuey, 2)+Math.pow(valuez, 2));
			
            summatory+=Math.pow((sqrtxyz-mean),2);
            counter++;
		}
		result[0]=counter;
		result[1]=summatory;
		return result;
	}
	
	public void labelsAvg() {
		
	}
	
	public void xyzFeatures() {
		
	}
	
	public void setSensor(ArrayList<String[]> sensor) {
		this.sensor=sensor;
	}
	
	public double correlation(int i, double xMean, double yMean, double sdx, double sdy,int pair) {
		double correlation=0;
		double summatory=0;
		int column1=0;
		int column2=0;
		
		switch (pair) {
        case 0:  
        	column1 = 0;
        	column2 = 1;
            break;
        case 1:  
        	column1 = 0;
        	column2 = 2;
            break;
        case 2:  
        	column1 = 1;
        	column2 = 2;
            break;
        default:
        	column1=0;
        	column2=0;
        	break;
		}
		
		for(int j=0;j<samplesNumber;j++) {
			summatory+=((Double.parseDouble(sensor.get(i+j)[column1]))-xMean)*((Double.parseDouble(sensor.get(i+j)[column2]))-yMean);
		}
		
		correlation=summatory/((samplesNumber-1)*sdx*sdy);
		
		return correlation;
	}
	
	
}
