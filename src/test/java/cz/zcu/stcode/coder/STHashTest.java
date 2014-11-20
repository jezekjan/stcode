package cz.zcu.stcode.coder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class STHashTest {

		
	@Test
	public void example() throws ParseException{
		 double lat  = 50.12;  //latitude
		 double lon  = 14.3;   //longitude
		 String date = "2001-07-04 12:08:56.23"; //time
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date d = df.parse(date);
		        
		 /**  * Date is split to the minutes within two weeks 
		  * and start of this two weeks period from first Monday in 1970 
		  */            
		 STCodeTime sttime = new STCodeTime(d);         
		 Cube q1 = new Cube(lat, lon, (new STCodeTime(d)).getMinutes());
		        
		 /** Print out STCode String */
		 System.out.println(q1.getCode());
		                
		 /** Construct the Cube from the STCode String */
		 Cube q2 = new Cube(q1.getCode());

	}
	@Test
	public void testPilsen(){
		//double ofset = 0.010986328;
		double lat = 49.7252;
		double lon = 13.3510;
		double time = 13568;
		
		for (int i = 2; i<18; i=i+2 ){
			STCoder gh = new STCoder(i, lat, lon, time);					
			printData(gh);
		
		}
		
	}
	
	
	@Test
	public void testChars(){
		for (char c: STCoder.digits){
			STCoder gh = new STCoder(c+"");
			System.out.println(c+" -- "+gh.getKeyBinVal());
		}
		
	}
	@Test
	public void testHash(){		
		double lat = 50;
		double lon = 14;
		double time = 13568;		
	
		for (int i=1; i<22; i++){
			STCoder st1 = new STCoder(i, lat, lon, time);							
			STCoder st2 = new STCoder(st1.getCode());		
			Assert.assertEquals(st1.getCorner()[0], st2.getCorner()[0], 0);
			Assert.assertEquals(st1.getCorner()[1], st2.getCorner()[1], 0);
			Assert.assertEquals(st1.getCorner()[2], st2.getCorner()[2], 0);
			Assert.assertEquals(st1.getLatError(), st2.getLatError(), 0);
			Assert.assertEquals(st1.getLonError(), st2.getLonError(), 0);
			Assert.assertEquals(st1.getTimeError(), st2.getTimeError(), 0);
			System.out.println(st2.getTimeError());
		}
	
		
		
		
	}
	public void printData(STCoder gh){
		System.out.print(gh.getLevel()+"	");
		System.out.print( gh.corner[0]+    "	"+ gh.corner[1]+"	"+gh.corner[2]+"	" );
		System.out.print( gh.getLatError()+"	"+ gh.getLonError()+"	"+gh.getTimeError()+"	" );
		System.out.print("	"+gh.keyBinVal);
		System.out.print("	"+gh.hash);
		System.out.println("");
	}
	/*@Test
	public void getParent(){
		double lat = 50;
		double lon = 14;
		double time = 13568;
		
		STHash en = new STHash(lat, lon, time);
		String pkey = en.getHash().substring(0, en.getHash().length()-1);
		
		STHash de = new STHash(en.genParentKey());
		en = new STHash(de.getResult()[0], de.getResult()[1], de.getResult()[2]);
		Assert.assertEquals(pkey, en.genParentKey());
	}*/	
}
