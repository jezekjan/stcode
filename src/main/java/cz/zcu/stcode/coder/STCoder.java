package cz.zcu.stcode.coder;

import java.math.BigInteger;
import java.util.HashMap;

public class STCoder {

	protected final int level;
	protected final double timeError;
	protected final double latError;
	protected final double lonError;
	protected final String hash;
	protected final BigInteger keyDecVal;
	protected final String keyBinVal;
	protected final String binLat;
	protected final String binLon;
	protected final String binTime;
		
	/*
	 * private final int latNorm; private final int lonNorm; private final int
	 * timeNorm;
	 */
	protected final double[] corner;

	final public static char[] digits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', ':', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z' };
	final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
	
	final static int timeInterval = 60*24*366;
	
	static {
		int i = 0;
		for (char c : digits)
			lookup.put(c, i++);
	}

	public STCoder(double latsize, double lat, double lon, double time) {

		this(calcLatLevel(latsize), lat, lon, time);
	}

	public STCoder(double lat, double lon, double time) {

		this(14, lat, lon, time);
	}


	public STCoder(int level, double lat, double lon, double time) {
		this.level = level;
		latError = calcLatSize(level);
		timeError = calcTimeSize(level);
		lonError = calcLonSize(level);

		int lat_norm = (int) Math.floor((lat + 90) / latError);
		int lon_norm = (int) Math.floor((lon + 180) / lonError);
		int time_norm = (int) Math.floor(time / timeError);

		double latc = lat_norm * latError - 90;
		double lonc = lon_norm * lonError - 180;
		double timec = time_norm * timeError;
		corner = new double[] { latc, lonc, timec };

		binLat = getString(lat_norm);
		binLon = getString(lon_norm);
		binTime = getString(time_norm);

		keyBinVal = compositeBinKey(binLat, binLon, binTime);
		keyDecVal = new BigInteger(keyBinVal.toString(), 2);
		hash = base64(keyBinVal);

	}

	public STCoder(String hash) {
		this.hash = hash;

		keyBinVal = base64tobin(hash);
		keyDecVal = new BigInteger(keyBinVal, 2);
		level = keyBinVal.length() / 3;
		timeError = calcTimeSize(level);
		latError = calcLatSize(level);
		lonError = calcLonSize(level);
		String[] bins = decompositeBinKey(keyBinVal);
		binLat = bins[0];
		binLon = bins[1];
		binTime = bins[2];
		corner = binToDec(bins);

	}

	private static String base64tobin(String hh) {
		StringBuilder buffer = new StringBuilder();
		String des = "";
		if (hh.contains("!")) {
			char d = hh.toCharArray()[hh.length() - 1];
			int i = lookup.get(d) + 8;
			des = (Integer.toString(i, 2).substring(1));
			hh = hh.substring(0, hh.length() - 2);
		}

		for (char c : hh.toCharArray()) {
			/* add 64 to keep nulls */
			int i = lookup.get(c) + 64;
			buffer.append(Integer.toString(i, 2).substring(1));
		}
		buffer.append(des);
		return buffer.toString();
	}

	public static int calcLatLevel(double size) {
		double l = Math.floor(Math.log(180 / size) / Math.log(2));
		return (int) l;
	}
	
	public static int calcLonLevel(double size) {
		double l = Math.floor(Math.log(180 / size) / Math.log(2));
		return (int) l;
	}
	
	public static int calcTimeLevel(double size) {
		double l = Math.floor(Math.log(timeInterval / size) / Math.log(2));
		return (int) l;
	}
	
	public static int getMinLevel(double lat, double lon, double time){
		int l_lat = calcLatLevel(lat);
		int l_lon = calcLonLevel(lon);
		int t_level = calcTimeLevel(time);
		return Math.min(l_lat, Math.min(l_lon, t_level));	
	}
	
	public static int getMaxLevel(double lat, double lon, double time){
		int l_lat = calcLatLevel(lat);
		int l_lon = calcLonLevel(lon);
		int t_level = calcTimeLevel(time);
		return Math.max(l_lat, Math.min(l_lon, t_level));	
	}

	private double calcLonSize(int num) {
		return 360 / (Math.pow(2, num));
	}

	private double calcLatSize(int num) {
		return 180 / (Math.pow(2, num));
	}

	private double calcTimeSize(int num) {
		return (timeInterval) / (Math.pow(2, num));
	}

	private String[] decompositeBinKey(String buffer) {

		StringBuilder lons = new StringBuilder();
		StringBuilder lats = new StringBuilder();
		StringBuilder times = new StringBuilder();

		// decvalue = Integer.valueOf(buffer, 2);

		/* lon bits */
		for (int i = 0; i < level * 3; i += 3) {
			lons.append(buffer.charAt(i));
		}
		/* lat bits */
		for (int i = 1; i < level * 3; i += 3) {
			lats.append(buffer.charAt(i));
		}

		/* time bits */
		for (int i = 2; i < level * 3; i += 3) {
			times.append(buffer.charAt(i));
		}

		return new String[] { lats.toString(), lons.toString(),
				times.toString() };
	}

	protected String compositeBinKey(String lat, String lon, String time) {

		StringBuilder buffer = new StringBuilder();		
		for (int i = 0; i < level; i++) {
			buffer.append(lon.charAt(i));
			buffer.append(lat.charAt(i));
			buffer.append(time.charAt(i));
		}

		return buffer.toString();
	}

	private double[] binToDec(String[] vals) {

		double lat = (getDec(vals[0].toString()) * latError - 90);
		double lon = (getDec(vals[1].toString()) * lonError - 180);
		double time = (getDec(vals[2].toString()) * timeError);

		return new double[] { lat, lon, time };

	}

	protected int getDec(String coord) {
		return Integer.valueOf(coord, 2);

	}

	/**
	 * converts to bin and add 0
	 * 
	 * @param coord
	 * @return
	 */
	private String getString(int coord) {
		String scoord = Integer.toString(coord, 2);
		int null_num = level - scoord.length();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < null_num; i++) {
			buffer.append("0");
		}
		return buffer.toString() + scoord;
	}
	
	public static String base64(String bin) {
		StringBuffer buf = new StringBuffer();
		String end = "";

		if (bin.length() % 6 == 3) {
			end = bin.substring(bin.length() - 3, bin.length());
			bin = bin.substring(0, bin.length() - 3);
		}

		for (int i = 0; i < bin.length(); i = i + 6) {
			String part = bin.substring(i, i + 6);
			int l = Integer.valueOf(part.toString(), 2);
			buf.append(digits[l]);
		}
		if (end.length() == 3) {
			int l = Integer.valueOf(end.toString(), 2);
			buf.append('!');
			buf.append(digits[l]);
		}
		return buf.toString();
	}	
	

	protected int getLevel() {
		return level;
	}

	protected double getTimeError() {
		return timeError;
	}

	protected double getLatError() {
		return latError;
	}

	protected double getLonError() {
		return lonError;
	}

	/**
	 * Returns STCode string
	 * @return STCode string
	 */
	public String getCode() {		
		return hash;
	}

	protected BigInteger getKeyDecVal() {
		return keyDecVal;
	}

	protected String getKeyBinVal() {
		return keyBinVal;
	}

	protected String getBinLat() {
		return binLat;
	}

	protected String getBinLon() {
		return binLon;
	}

	protected String getBinTime() {
		return binTime;
	}

	protected double[] getCorner() {
		return corner;
	}	
}
