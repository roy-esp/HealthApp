package roy.ha;

import java.io.File;
import java.util.ArrayList;

public class TestCalc {

	public static void main(String[] args) throws Exception {
		File labels=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S01_labels.csv");
		File sensor=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S01_LB.csv");
		CSVmanager csvmanagersen=new CSVmanager(sensor);
		ArrayList<String[]> arraysensor=csvmanagersen.csvSensorToArray();
		CSVmanager csvmanagerlab=new CSVmanager(labels);
		ArrayList<String> arraylabel=csvmanagerlab.csvLabelToArray();
		
		//Generate CSV files x,y,z separated feature csv files
		Calculations calculations=new Calculations(arraylabel,arraysensor);
		calculations.generateCsv();
		
		
		/*
		//X-Y  XY
		File sensorX=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresx.csv");
		File sensorY=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresy.csv");
		String outPath="C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresxy.csv";
		CSVmanager csvmanagerX=new CSVmanager(sensorX);
		csvmanagerX.merge(sensorY, outPath);
		
		//XY-Z  XYZ
		File sensorXY=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresxy.csv");
		File sensorZ=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresz.csv");
		String outPathXYZ="C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresxyz.csv";
		CSVmanager csvmanagerXY=new CSVmanager(sensorXY);
		csvmanagerXY.merge(sensorZ, outPathXYZ);
		
		//XYZ features
		File filexyz=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\featuresxyz.csv");
		CSVmanager csvmanagerxyz=new CSVmanager(filexyz);
		ArrayList<String[]> arrayxyz=csvmanagerxyz.csvSensorToArray();
		calculations.setSensor(arrayxyz);
		calculations.xyzFeatures();
*/
	}

}
