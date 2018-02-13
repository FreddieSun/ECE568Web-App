import Jama.*;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.csv.*;
import org.apache.commons.math3.linear.*;

public class bayesian {

	public static double[][] readData(String fileName) {
		//ArrayList<Double[]> result=new ArrayList<Double[]>();
		
		
		// read CSV data into array
		double[] xArr=new double[1000];
		double[] tArr=new double[1000];
		Reader in = null;
		
		try {
			in=new FileReader(fileName+".csv");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Iterable<CSVRecord> records=null;
		try {
			records=CSVFormat.EXCEL.parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int index=0;
		for(CSVRecord record:records) {
			xArr[index]=Double.valueOf(record.get(0));
			tArr[index]=Double.valueOf(record.get(1));
			index++;
		}
		;

		double[][] result=new double[2][index];
		System.arraycopy(xArr, 0, result[0], 0, index);
		System.arraycopy(tArr, 0, result[1], 0, index);
		
		
//		System.out.println(Arrays.toString(result[0]));
//		System.out.println(Arrays.toString(result[1]));

		
		return result;
	}
	
	public static double[] predictData(double[] xArr, double[] tArr) {
		// here we have x data. we use the [1,x-1] as the learning data, and predict the x data.
				int index=xArr.length-1;
				
				
				int M=4;
			    double beta = 12;
			    double alpha = 0.1;
				
				double a[][] = new double[M+1][1];
				double b[][]=new double[1][M+1];
				double s[][];
				double lt[][]=new double[M+1][1];
				double predictPrice[][];
				
		        /*---------------initialize the training data---------------*/
				
				// xArr
				
		        /*--------------calculate SUM-φ(xn)-------------------*/
				
				for(int i=0;i<index;i++) {
					for(int j=0;j<M+1;j++) {
						a[j][0]+=Math.pow(xArr[i], j);
					}
				}
				
				Matrix A=new Matrix(a);
				
		        /*-----------------initialize φ(x)T------------------*/
				for(int i=0;i<M+1;i++) {
					b[0][i]=Math.pow(xArr[index], i);
				}
				Matrix B=new Matrix(b);
				
		        /*------------calculate the matrix S-------------*/
				
				Matrix sMat=A.times(B).times(beta);
				s=sMat.getArray();
				
				for(int i=0;i<M+1;i++) {
					for(int j=0;j<M+1;j++) {
						if(i==j) {
							s[i][j]+=alpha;
						}
					}
				}
				
		        /*------------calculate the inversion of matrix S-------------*/
				sMat=sMat.inverse();
				
				for(int i=0;i<index;i++) {
					for(int j=0;j<M+1;j++) {
						lt[j][0]+=Math.pow(xArr[i], j)*tArr[i];
					}
				}
				
				Matrix LT=new Matrix(lt);
				
				Matrix FT=B.times(sMat);
				
				Matrix pPMatrix=FT.times(LT).times(beta);
				predictPrice=pPMatrix.getArray();
				
				double variance=1/beta+B.times(sMat).times(B.transpose()).get(0, 0);
//				double variance=1/beta+B.transpose().times(sMat).times(B).get(0, 0);
				variance=Math.sqrt(variance);
				
				double estimation=predictPrice[0][0];
				double meanError=Math.abs(tArr[index]-estimation);
			    double relativeError=meanError/tArr[index];
			    double[] result= {estimation,variance,meanError,relativeError};
				
			    System.out.println("Actual value is"+String.valueOf(tArr[index]));
			    System.out.println("Estimated Value is: " + estimation);
			    System.out.println("To be more precise, the estimated value is in this range: ["+String.valueOf(estimation-3*variance)+" , "+String.valueOf(estimation+3*variance)+"]");
			    System.out.println("Mean Error is: "+meanError);
			    System.out.println("Relative Error is: "+relativeError);
			    
			    System.out.println();
			    
			   
			    return result;
		//
	}
	
	
	public static void main(String[] args) {
		double meanError=0;
		double relativeError=0;
		
		String[] fileList= {"EA1","EA2","GE","GE2","GM","GM2","google","testdata","yahoo","yahoo2"};
		for(int fileIndex=0;fileIndex<10;fileIndex++) {
			System.out.println("for "+fileList[fileIndex]);
			double[][] rawData=readData(fileList[fileIndex]);
			double[] xArr=rawData[0];
			double[] tArr=rawData[1];
			
			
			
			double[] predictVal=predictData(xArr, tArr);
			
			meanError+=predictVal[2];
			relativeError+=predictVal[3];
			
		}
		

		meanError=meanError/10;
		relativeError=relativeError/10;
		System.out.printf("The overall absolute mean error is: %f\n", meanError);
        System.out.printf("The overall average relative error is: %f\n", relativeError);
		



	}

}
