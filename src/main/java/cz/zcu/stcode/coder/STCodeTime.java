package cz.zcu.stcode.coder;

import java.text.ParseException;
import java.util.Date;

public class STCodeTime {
	
	private final Date periodStart;
	private final double minutes;
	private final Date date;
	
	public STCodeTime(Date periodStart, double minutes){
		Date d = new Date((long)periodStart.getTime() + (long)(minutes*60000));
		this.date = d;
		this.periodStart = periodStart;
		this.minutes = minutes;
	}
	
	
	public STCodeTime(Date d) throws ParseException{
		/**milliseconds of first Monday in 1970*/
		long start = 345600000;		
		
		/*difference between start and the date*/
		long dif   = (long)d.getTime() - start;
		
		if (dif<0){
			throw new ParseException("Date must be newer then 1970",0);
		}
		/*number of week pairs till first Monday in 1970*/
		long twoWeek = (long)Math.floor(dif / (1209600000));
		
		/*Date of start of the period*/
		long msec =  (long)twoWeek * 1209600000;
		this.periodStart =  new Date((long)(start+msec));				
		this.minutes = ((double)(d.getTime() - periodStart.getTime()))/60000;
		this.date = d;
		
	}	
	public Date getPeriodStart() {
		return periodStart;
	}
	public double getMinutes() {
		return minutes;
	}
	public Date getDate() {
		return date;
	}	

}
