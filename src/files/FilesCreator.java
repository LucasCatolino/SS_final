package files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused") //Warnings because Writer is not used, it only creates files
public class FilesCreator {
	
	private static final double DEFAULT_A= 500;
	private static final int DEFAULT_N= 5;
	private static final int DEFAULT_N_PART= 25;
	private static final double DEFAULT_AGGRESIVE= 0.25;
	
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

    	System.out.println("Insert A (> 0, default 500m)");
		BufferedReader readerA= new BufferedReader(new InputStreamReader(System.in));
		double AInput= 0;
		try {
			AInput = Double.parseDouble(readerA.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		double A= (AInput > 0) ? AInput : DEFAULT_A;
		
		System.out.println("Insert n (> 0, default 5)");
		BufferedReader readerN= new BufferedReader(new InputStreamReader(System.in));
		int NInput= 0;
		try {
			NInput = Integer.parseInt(readerN.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		int n= (NInput > 0) ? NInput : DEFAULT_N;
		
		System.out.println("Insert N (> 0, default 25)");
		BufferedReader readerNPart= new BufferedReader(new InputStreamReader(System.in));
		int NPartInput= 0;
		try {
			NPartInput = Integer.parseInt(readerNPart.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		int N= (NPartInput > 0) ? NPartInput : DEFAULT_N_PART;
		
		System.out.println("Insert percentage of agressive drivers (> 0, default %25)");
		BufferedReader readerAggresive= new BufferedReader(new InputStreamReader(System.in));
		double aggresiveInput= 0;
		try {
			aggresiveInput = Double.parseDouble(readerAggresive.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		double aggressiveProb= (aggresiveInput > 0) ? (double) aggresiveInput/100 : DEFAULT_AGGRESIVE;

    	System.out.println("A: " + A + " n: " + n + " N: " + N);
    	
    	Writer writerStatic = new Writer(A, n, N, aggressiveProb, "static");
		Writer writerDynamic = new Writer(A, n, N, aggressiveProb, "dynamic");
    }

}
