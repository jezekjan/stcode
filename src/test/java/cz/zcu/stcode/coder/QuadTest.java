package cz.zcu.stcode.coder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class QuadTest {

	@Test
	public void testConstructor() {
		for (int i= 0 ; i< 1000; i++){
			double lat = 50 +i/500;
			double lon = 14 + i/500;
			double time = i;
			Cube q1 =new Cube(lat, lon, time);
			String s = q1.getCode();
			
			Cube q2 = new Cube(s);						
			Assert.assertEquals(q1.getKeyBinVal(), q2.getKeyBinVal() );

		}
	}
	
	@Test
	public void testParent() {
		double s =0.010986328;
		Cube q =new Cube(s, 14, 50, 6544);
		Cube should =new Cube(s*2, 14, 50, 6544);
		
		Cube is =  q.genParentCube();		
		
		Assert.assertEquals(should, is);
		
	}
	
	@Test
	public void testChild() {	
		Cube q1 =new Cube(0.043945312, 14, 50, 6544);		
		for (Cube c : q1.genChildCubes()){			
			Assert.assertEquals(c.genParentCube().getCode(), q1.getCode());	
		}			
	}
	
	@Test
	public void testLevel() {	
		Cube q1 =new Cube(0.011, 14, 50, 6544);		
		System.out.println(q1.getLatError());		
	}
	
	@Test
	public void testNext() {
		double lat = 50.00; //latitude
		double lon = 14.00; //longitude
		double time = 336610; // seconds 
		Cube q1 = new Cube(lat, lon, time);
		
	
		Cube q2 = new Cube(lat, lon + q1.getLonError(), time);			
		Assert.assertEquals(q1.genNextLonKey(1), q2.getCode());
		
		q2 = new Cube(lat + q1.getLatError(), lon, time);			
		Assert.assertEquals(q1.genNextLatKey(1), q2.getCode());
		
		//q2 = new Cube(lat, lon, time + q1.getTimeError());			
		//Assert.assertEquals(q1.genNextTimeKey(1), q2.getCode());
		
	}
	
	@Test
	public void test(){
				
		Cube q1 =new Cube( 0.02197265625,   13.99658203125, 49.9658203125, 6543.6328125 );
		Cube q2 =new Cube(0.010986328125, -38.001708984375, -65.01708984375, 3271.81640625);		
		
		Assert.assertFalse(q1.equals(q2)); 
	}
	
	@Test
	public void demo() throws ParseException{
		double lat = 50.12;  //latitude
		double lon = 14.3;   //longitude
		String date = "2001-07-04 12:08:56.23";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = df.parse(date);
		
	   /**
		* Date is split to the minutes within two weeks 
		* and start of this two weeks period from first Monday in 1970 
		*/		
	  	STCodeTime sttime = new STCodeTime(d);
	  	
	  	/**
	  	 * Construct Cube object from triplet of lat, lon and minutes
	  	 */
		Cube q1 = new Cube(0.1,lat, lon, 164160);
		
		
		/** Print out STCode String */
		System.out.println(q1.getCode());
	
		
		/** Construct the Cube from the STCode String */
		Cube q2 = new Cube(q1.getCode());
		
		System.out.println(q1.getTime());
		System.out.println(q1.getLatError());
		System.out.println(q1.getLonError());
		System.out.println(q1.getTimeError());
		
		
	}
}
