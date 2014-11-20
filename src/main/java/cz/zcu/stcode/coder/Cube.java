package cz.zcu.stcode.coder;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Class that converts triplet of coordinates into STCode
 * @author jezekjan
 *
 */
public class Cube extends STCoder{
	
	private static double getTimeFromDate(Date d){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(d.getYear()+1900, 0, 1, 0, 0, 0);		
		Date tt= cal.getTime();	
		double time = (d.getTime()-cal.getTime().getTime())/1000/60;
		return time;
	}
	/**
	 * Helper variable for generating subcubes
	 */
	private static final String[] subbins = new String[]{"000","001","010","011","100","101","110","111"};
	
	public Cube(double lat, double lon, double time) {
		super(lat, lon, time);
	}		
	
	public Cube(double latsize, double lat, double lon, double time) {
		super(latsize, lat, lon, time);
	}	
	
	public Cube(int level, double lat, double lon, double time) {
		super(level, lat, lon, time);
	}	
	
	public Cube(int level, double lat, double lon, Date d) {
		
		super(level, lat, lon,  getTimeFromDate(d));
	}	

	public Cube(String hash) {
		super(hash);		
	}
	
	public Map<String, Double> genVertices(){
		Map<String, Double> m = new HashMap<String, Double>(8);
		m.put("minlat", corner[0]);
		m.put("minlon", corner[1]);
		m.put("mintime",corner[2]);
		m.put("maxlat", corner[0] + latError);
		m.put("maxlon", corner[1] + lonError);
		m.put("maxtime",corner[2] + timeError);
		
		return m;
	}

	public String genNextLatKey(int ofset){
		int lat = (int)getDec(binLat)+ofset;		
		String lats = Integer.toString(lat, 2);
		String bkey = compositeBinKey(lats, binLon, binTime);
		return base64(bkey);
	}
	
	public String genNextLonKey(int ofset){
		int lon = (int)getDec(binLon)+ofset;		
		String lons = Integer.toString(lon, 2);
		String bkey = compositeBinKey(binLat, lons, binTime);
		return base64(bkey);
	}
	
	public String genNextTimeKey(int ofset){
		int time = (int)getDec(binTime)+ofset;		
		String times = Integer.toString(time, 2);
		String bkey = compositeBinKey(binLat, binLon, times);
		return base64(bkey);
	}
	

	

	
	public String getKMLString(){
		NumberFormat f = new DecimalFormat("#0.00000");
		String bottom = 		
		f.format(corner[1]) + ", " +
		f.format(corner[0]) + ", " +
		f.format(corner[2]*3) + " " +
		f.format(corner[1] +lonError) + ", " +
		f.format(corner[0]) + ", " +
		f.format(corner[2]*3) + " " +
		f.format(corner[1] +lonError) + ", " +
		f.format(corner[0] +latError )+ ", " +
		f.format(corner[2]*3) + " " +
		f.format(corner[1]) + ", " +
		f.format(corner[0] +latError) + ", " +
		f.format(corner[2]*3) + " " ;
				
		
		
		String kml = bottom;
		return kml;
				
	}
	public String[] genChildKeys(){
		String[] c = new String[8];
		int i = 0;
		
		for (String s : subbins){			
			String key = keyBinVal+ s;
			c[i++] = base64(key);
		}	
		
		return c;
	}
	public Cube[] genChildCubes(){
		Cube[] c = new Cube[8];
		int i = 0;
		
		for ( String h : genChildKeys()){
			c[i++] = new Cube(h);
		}		
		return c;
	}
	
	public Cube genParentCube(){
		
		String key = keyBinVal.substring(0, keyBinVal.length() -3);
		return new Cube(base64(key));
	}
		
	
	public double getTimeError() {
		return timeError;
	}

	public double getLatError() {
		return latError;
	}

	public double getLonError() {
		return lonError;
	}

	public BigInteger getKeyDecVal() {
		return keyDecVal;
	}

	public String getKeyBinVal() {
		return keyBinVal;
	}

	public String getBinLat() {
		return binLat;
	}

	public String getBinLon() {
		return binLon;
	}

	public String getBinTime() {
		return binTime;
	}

	public double[] getCenter() {
		double[] c =  
	   {corner[0] + latError / 2,
		corner[1] + lonError / 2, 
		corner[2] + timeError / 2};
		return c;
	}

	public int getLevel() {
		return level;
	}

	public double getLat(){
		return corner[0] + latError / 2;	
	}
	
	public double getLon(){
		return corner[1] + lonError / 2;	
	}
	
	public double getTime(){
		return corner[2] + timeError / 2;
	}
	
	public Date getTime(int year){
		return convertTime(getTime(),year);		
	}
	
	public Date getMinTime(int year){
		return convertTime(corner[2],year);		
	}
	
	public Date getMaxTime(int year){
		return convertTime(corner[2]+timeError,year);	
	}
	
	private Date convertTime(double minutes, int year){
		Calendar c =  Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(year, 0, 1, 0, 0,0);		;		
		long milisec =  (long)(minutes*60*1000);
		long time = c.getTime().getTime()+milisec;
		return new Date(time);
	}

	
	@Override
	public boolean equals(Object q) {
		// TODO Auto-generated method stub
		boolean e = hash.equals(((Cube)q).getCode());
		return e;
	}

	public String getMetadata(){
		return hash+"\n"+
				" minlat: "+ corner[0] + " minlon: "+corner[1]+ " mintime:"+ corner[2] +" \n"+
				" maxlat: "+ (corner[0] + latError) +
				" maxlon"  + (corner[1] + lonError) + 
				" maxtime" + (corner[2] + timeError);
				
		// TODO Auto-generated method stub 

	}
	
	@Override
	public String toString() {
	//	String s = hash+" [ lat: "+ corner[0] + "lon: "+corner[1]+ " time:"+ corner[2] + " size: "+latError + " ]";
		// TODO Auto-generated method stub 
		return getCode();
	}

	

	
}
