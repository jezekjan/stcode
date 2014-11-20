package cz.zcu.stcode.kml;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import cz.zcu.stcode.coder.Cube;

public class KMLgenerator {


	public static void main(String args[]) throws IOException{
		Cube c = new Cube("u8XUhjL9X");
		System.out.println(c.getBinLat());
		System.out.println(c.getBinLon());
		//System.out.println(c.genNextLatKey(1));
		System.out.println(c.getTime(2012));		
		
		
	}
	
	public static void window(){
		
		double latmin = 50;
		double latmax = 50.1;
		double lonmin = 14;	
		double lonmax = 14.1;
		Calendar min = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		min.set(2013, 7, 22, 18, 10, 5);
		Calendar max = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		max.set(2013, 7, 24, 18, 10, 5);
		
		
		double lat_size = latmax-latmin;
		double lon_size = lonmax-lonmin;
		double time_size = max.getTimeInMillis()/1000/60 - min.getTimeInMillis()/1000/60;
		

		int minl = Cube.getMinLevel(lat_size,  lon_size, time_size)-1;
		int maxl = Cube.getMaxLevel(lat_size,  lon_size, time_size);
		
		Cube c = new Cube(minl, latmin, lonmin,min.getTime());
		System.out.println("center:"+c.getMetadata());
		Cube cmax = new Cube(minl, latmax, lonmax,max.getTime());
		System.out.println("center:"+cmax.getMetadata());
		
		
		
	}
	
	public static void getKML() throws IOException{
		
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		datef.setTimeZone(TimeZone.getTimeZone("GMT"));
	/*
	 * 	SimpleDateFormat datef = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		datef.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar c =  Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(2013, 0, 1, 0, 0,0);		
		
		System.out.println(datef.format(c.getTime()));*/
		Set<String> set = new HashSet<String>();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(2013, 7, 22, 18, 10, 5);		
		Date d = cal.getTime();
		//for (int i = 2; i<24; i=i+2){
			
			Cube c = new Cube(18, 50, 14, d);
			System.out.println("time"+datef.format(c.getTime(2013))+" ");
			System.out.println("bin lat  "+c.getBinLat()+" "+c.getLat()+"  +-"+c.getLatError()/2);
			System.out.println("bin lon  "+c.getBinLon()+" "+c.getLon()+" +-"+c.getLonError()/2) ;
			System.out.println("bin time "+c.getBinTime()+" "+c.getTime()+"  +-"+c.getTimeError()/2);
			
			
			System.out.print(c.getCode()+"  ");			
			set.add(c.getCode());
		//}
		
		
		PrintWriter writer = new PrintWriter("tmp/tmp_kml.kml", "UTF-8");
		KMLGenerator w = new KMLGenerator(writer);//new PrintWriter(System.out));		
		
		w.writeQuads(set);
		writer.close();
		
	}
	

	
}
