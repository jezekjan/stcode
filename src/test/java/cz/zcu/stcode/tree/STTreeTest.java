package cz.zcu.stcode.tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Test;

import cz.zcu.stcode.coder.Cube;

/**
 * Not a test... just demo
 * @author jezekjan
 *
 */
public class STTreeTest {
	@Test
	public void exampleTree() throws IOException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		DecimalFormat nf = new DecimalFormat(".00000", new DecimalFormatSymbols(new Locale("en_US")));
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Random r = new Random(100);
		double lat = 50.0;
		double lon= 14.0;
		int m = 10 ;	
		List<Cube> cs = new LinkedList<Cube>();	
		STTree tree = new STTree();
		String sql="";
		
		for(int i=0; i < 10; i++ ){
			cal.set(2013, 7, 22, 18, m, 0);		
			Date d1 = cal.getTime();
			Cube c1 = new Cube(16, lat, lon, d1);	
			tree.insertCode(c1.getCode());
			System.out.println(nf.format(lat)+ " & "+nf.format(lon)+" & "+ df.format(d1) +" & " +c1.getCode() );

			sql = sql+"insert into stcodes(the_geom, time_stamp, stc) " +
					"values(st_geomfromtext('POINT("+lon+" "+lat+")',4326),('"+df.format(d1)+"')::timestamp, '"+c1.getCode()+"' ); \n";
			
			lat = lat+ i/100 + r.nextDouble()/100;
			lon = lon+ i/200 - r.nextDouble()/100;
			int mm = (int )Math.round(r.nextDouble()*5.0);
			m = m+i + mm  ;
		
		}
		
		String t = tree.getLaTex(tree.getTree().first());
		System.out.println(sql);
		System.out.println(t);
		
	}

	//@Test
	public void testTree() throws IOException{
		NumberFormat fl = new DecimalFormat("#.0000");
		NumberFormat ft = new DecimalFormat("#.000");
	    FileWriter fpoint = new FileWriter("/home/jezejan/tmp/test3/b.txt");
	    FileWriter fcode  = new FileWriter("/home/jezejan/tmp/test3/c.txt");
		BufferedWriter wb = new BufferedWriter(fpoint);
		BufferedWriter wc = new BufferedWriter(fcode);		
		
		STTree tree = new STTree();
		for (int i= 0 ; i< 50; i++){
			Random r = new Random();
			double lat = 50 +i/50 + r.nextDouble()/1000;
			double lon = 14 + i/50 + r.nextDouble()/1000;
			double time = i*10 ;
			Cube q1 =new Cube(lat, lon, time);
			System.out.println(q1.getCode());
			String s = q1.getCode();
			String d = fl.format(lat) +" "+fl.format(lon)+" "+ft.format(time) ;
			wb.write(d);
			tree.insertCode(s);

		}
		 
		  wb.close();
		
		
		String t = tree.getLaTex(tree.getTree().first());
		System.out.println(t);
		//wc.write(tree.s);
		wc.close();
	}

}
