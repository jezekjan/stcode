package cz.zcu.stcode.coder;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class STCodeTimerTest {
	@Test
	public void testConstructor() throws ParseException{
		Date d = new Date();
		STCodeTime stt1 = new STCodeTime(d);
		STCodeTime stt2 = new STCodeTime(stt1.getPeriodStart(), stt1.getMinutes());		
		Assert.assertEquals(stt1.getDate().getTime(), stt2.getDate().getTime());				
	}

}
