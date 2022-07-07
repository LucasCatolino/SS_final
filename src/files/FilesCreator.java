package files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused") //Warnings because Writer is not used, it only creates files
public class FilesCreator {
	
	private static final double DEFAULT_R= 11;
	private static final int DEFAULT_Nh= 10;
	
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

    	System.out.println("Insert R (> 0, default 11m)");
		BufferedReader readerR= new BufferedReader(new InputStreamReader(System.in));
		double RInput= 0;
		try {
			RInput = Double.parseDouble(readerR.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		double R= (RInput > 0) ? RInput : DEFAULT_R;
		
		System.out.println("Insert Nh (> 0, default 10)");
		BufferedReader readerNh= new BufferedReader(new InputStreamReader(System.in));
		int NhInput= 0;
		try {
			NhInput = Integer.parseInt(readerNh.readLine());			
		} catch (Exception e) {
			//do nothing
		}
		int Nh= (NhInput > 0) ? NhInput : DEFAULT_Nh;

    	System.out.println("R: " + R + " N: " + Nh);
    	
    	Writer writerStatic = new Writer(R, Nh, "static");
		Writer writerDynamic = new Writer(R, Nh, "dynamic");
    }

}
