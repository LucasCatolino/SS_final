package files;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class Writer {
	
	private static final double R_P= 0.3; //Estimated radius of a human
	private static final double MIN_DISTANCE= 1;
	private static final int DEGREES= 360;
		
    public Writer(double R, int N, String type) {

		try {
            File file = new File("./resources/" + type + ".txt");
            FileWriter myWriter = new FileWriter("./resources/" + type + ".txt");
            try {
            	if (type.compareTo("static") == 0) {
					this.staticFile(R, N, myWriter);
				} else {
					this.dynamicFile(R, N, myWriter);
				}
			} catch (Exception e) {
				System.err.println("IOException");
			}
            myWriter.close();
            System.out.println("Successfully wrote to the file ./resources/" + type + ".txt");
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
    }
    
	private void staticFile(double r, int n, FileWriter myWriter) throws IOException {
		myWriter.write("" + n + "\n"); //N humans
		myWriter.write("" + r + "\n"); //L
		myWriter.write("" + R_P + "\n");
	}

	private void dynamicFile(double r, int n, FileWriter myWriter) throws IOException {
		Point2D center = new Point2D.Double(r, r);
		ArrayList<Point2D> particles= new ArrayList<>();
		particles.add(center);
		double limitInf= MIN_DISTANCE + 2 * R_P;
		double limitSup= r - R_P;
		
		//first line initial time
		myWriter.write("0\n");
		
		//first particle centered and still
		myWriter.write("" + r + "\t" + r + "\n");
		
		//write n particles not overlapped
		while (particles.size() <= n) {
			double distance= (Math.random() * (limitSup - limitInf) + limitInf);
			double degrees= Math.random() * DEGREES;
			
			double x= distance * Math.cos(Math.toRadians(degrees)) + r;
			double y= distance * Math.sin(Math.toRadians(degrees)) + r;
			
			Point2D auxPoint = new Point2D.Float();
			auxPoint.setLocation(x, y);
			
			boolean particleOverlapped= false;

			for (Iterator particle = particles.iterator(); particle.hasNext();) {
				Point2D point2d = (Point2D) particle.next();
				if (point2d.distance(auxPoint) < (R_P + R_P)) {
					particleOverlapped= true;
					break;
				}
			}

			if (!particleOverlapped) {
				particles.add(auxPoint);
				
				myWriter.write("" + x + "\t" + y + "\n"); //x y not overlapped				
			}
		}
	}
	
}