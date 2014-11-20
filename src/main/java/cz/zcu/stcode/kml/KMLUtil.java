package cz.zcu.stcode.kml;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.zcu.stcode.coder.Cube;

public class KMLUtil {
	
	
	private static NumberFormat getF(){
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		otherSymbols.setDecimalSeparator('.');
		NumberFormat formatter = new DecimalFormat("#0.000000",otherSymbols );
		return formatter;
	}
	private static Map<String, String> getKML(Cube q){
		Map<String, String> m = new HashMap<String, String>();
		double[] center = q.getCenter();
		NumberFormat f = getF();
		double zz = (center[2]-2880)*1000;
		String xd = f.format(center[1]);
		String yd = f.format(center[0]);
		String zd = f.format(zz);
		String xu = f.format(center[1] + q.getLonError());
		String yu = f.format(center[0] + q.getLatError());
		String zu = f.format(zz+ q.getTimeError()*1000);
		
		String w1 = 		
		getP(xd, yd, zd) +
		getP(xd, yu, zd) +
		getP(xu, yu, zd) +
		getP(xu, yd, zd) +	
		getP(xd, yd, zd);
		
		String w2 = 		
			getP(xd, yd, zu) +
			getP(xd, yu, zu) +
			getP(xu, yu, zu) +
			getP(xu, yd, zu);			
		
		String w3 = 			
		getP(xd, yd, zd) +
		getP(xd, yd, zu) +
		getP(xu, yd, zu) +
		getP(xu, yd, zd);
		
		String w4 = 			
		getP(xd, yu, zd) +
		getP(xd, yu, zu) +
		getP(xu, yu, zu) +
		getP(xu, yu, zd);
		
		String w5 = 			
		getP(xd, yd, zd) +
		getP(xd, yd, zu) +
		getP(xd, yu, zu) +
		getP(xd, yu, zd);
		
		String w6 = 			
		getP(xu, yd, zd) +
		getP(xu, yd, zu) +
		getP(xu, yu, zu) +
		getP(xu, yu, zd);
				
		m.put("w1", w1);
		m.put("w2", w2);
		m.put("w3", w3);
		m.put("w4", w4);
		m.put("w5", w5);
		m.put("w6", w6);
		
		
		return m;
	}
	protected static Map<String, String> getKML2D(Cube q){
		NumberFormat f = getF();
		Map<String, String> m = new HashMap<String, String>();
		double[] center = q.getCenter();				
		String yd = f.format(center[0]);
		String xd = f.format(center[1]);				
		String yu = f.format(center[0] + q.getLatError());
		String xu = f.format(center[1] + q.getLonError());
		m.put("w1", xd+","+yd+"\n"+xd+","+yu+"\n"+xu+","+yu+"\n"+xu+","+yd+"\n"+xd+","+yd);
		m.put("name", q.getCode());
		m.put("p", xd+", "+yd);
		return m;
	}
	
	private static String getP(String x, String y, String z){
		return x + ", " +y +  ", "+ z + " ";
	}
}
