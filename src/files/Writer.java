package files;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class Writer {
	
	private static final double R_P= 0.9; //Estimated radius of a car
	private static final double MIN_DISTANCE= 2;
	private static final double LANE_CENTER= 1.75;
		
    public Writer(double length, int lanes, int particlesCant, String type) {

		try {
            File file = new File("./resources/" + type + ".txt");
            FileWriter myWriter = new FileWriter("./resources/" + type + ".txt");
            try {
            	if (type.compareTo("static") == 0) {
					this.staticFile(length, lanes, particlesCant, myWriter);
				} else {
					this.dynamicFile(length, lanes, particlesCant, myWriter);
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
    
	private void staticFile(double length, int lanes, int particlesCant, FileWriter myWriter) throws IOException {
		myWriter.write("" + (particlesCant * lanes)+ "\n"); //N cars
		myWriter.write("" + length + "\n"); //A
		myWriter.write("" + lanes + "\n"); //A
		myWriter.write("" + R_P + "\n");
	}

	private void dynamicFile(double length, int lanes, Integer particlesCant, FileWriter myWriter) throws IOException {
		
		ArrayList<Point2D> particles= new ArrayList<>();
		
		//first line initial time
		myWriter.write("0\n");
		
		//for each lane, insert particles
		for (int i = 0; i < lanes; i++) {
			//write particlesCant particles not overlapped
			while (particles.size() < particlesCant) {
				double x= (Math.random() * (length - R_P) + R_P);
				double y= LANE_CENTER + i * 2 * LANE_CENTER;
				
				Point2D auxPoint = new Point2D.Float();
				auxPoint.setLocation(x, y);
				
				boolean particleOverlapped= false;
				
				for (Iterator particle = particles.iterator(); particle.hasNext();) {
					Point2D point2d = (Point2D) particle.next();
					if (point2d.distance(auxPoint) < (MIN_DISTANCE)) {
						particleOverlapped= true;
						break;
					}
				}
				
				if (!particleOverlapped) {
					particles.add(auxPoint);
					
					myWriter.write("" + String.format("%.2f",x) + "\t" + y + "\t" + R_P + "\n"); //x y not overlapped				
				}
			}
			particles.clear();
		}
	}
	
}