package cz.zcu.stcode.kml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cz.zcu.stcode.coder.Cube;
import cz.zcu.stcode.coder.STCodeTime;
import cz.zcu.stcode.tree.CodeList;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Prototype class for getting kml out of the codes
 * 
 * @author jezekjan
 * 
 */
public class KMLGenerator {

	Writer writer;
	private static String header;

	private static String end = " </Document> </kml>";

	private static Template point = null;
	private static Template box = null;
	private static Template line = null;
	private static Template line_svg = null;

	static SimpleDateFormat format_day = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");
	NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
	NumberFormat formatter = new DecimalFormat("#0.000000");

	public KMLGenerator(Writer writer) {
	
		
		
		try {
			this.writer = writer;

			Configuration cfg = new Configuration();
			File dir = new File(getClass().getResource("template.ftl")
					.getFile().replace("template.ftl", ""));
			cfg.setDirectoryForTemplateLoading(new File(dir.getPath()));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			point = cfg.getTemplate("points.xml");
			box = cfg.getTemplate("box.xml");
			line = cfg.getTemplate("line.xml");
			line_svg = cfg.getTemplate("line.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeQuads(CodeList l, Date d, int max) throws IOException {
		header = read((getClass().getResource("header")));
		// link = read((getClass().getResource("link.xml")));
		String style = read((getClass().getResource("style_box.xml")));
		writer.write(header);
		writer.write(style);
		// List<Quad> res = util.getIndexQuads(unit_id,date);
		for (int i = 1; i <= max; i++) {
			writer.write("<Folder>");
			writer.write("<name>"+(l.level - i)+"</name>");

			for (String c : l.getLevel(l.level - i)) {
				
				Map<String, String> m = KMLUtil.getKML2D(new Cube(c));
				try {
					line.process(m, writer);
				} catch (TemplateException e) {
					throw new IOException(e);
				}
			}
			writer.write("</Folder>");
		}
		writer.write(end);
	}

	public void writeQuadsSvg(Set<String> codes) throws IOException {
		
		
		writer.write("<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>");	
		// List<Quad> res = util.getIndexQuads(unit_id,date);
		for (String c:codes) {						
				Map<String, String> m = KMLUtil.getKML2D(new Cube(c));
				try {
					line_svg.process(m, writer);
				} catch (TemplateException e) {
					throw new IOException(e);
				}
			
		
		}
		writer.write("</svg> ");
	}
	public void writeQuads(Set<String> codes) throws IOException {
		header = read((getClass().getResource("header")));
		// link = read((getClass().getResource("link.xml")));
		String style = read((getClass().getResource("style_box.xml")));
		writer.write(header);
		writer.write(style);
		// List<Quad> res = util.getIndexQuads(unit_id,date);
		for (String c:codes) {			
			writer.write("<name>"+(c.length())+"</name>");
				Map<String, String> m = KMLUtil.getKML2D(new Cube(c));
				try {
					line.process(m, writer);
				} catch (TemplateException e) {
					throw new IOException(e);
				}
			
		
		}
		writer.write(end);
	}

	public void writePoints(Set<String> codes, Date d) throws IOException {
		header = read((getClass().getResource("header")));
		writer.write(header);
		List<Map<String, String>> res = generateMap(codes, d);
		for (Map<String, String> map : res) {
			try {
				point.process(map, writer);
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		writer.write(end);
	}

	private List<Map<String, String>> generateMap(Set<String> codes, Date period) {
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		for (String s : codes) {
			Map<String, String> m = new HashMap<String, String>(3);
			Cube c = new Cube(s);
			m.put("lat", Double.toString(c.getLat()));
			m.put("lon", Double.toString(c.getLon()));
			double er = c.getTimeError() / 2;
			Date dstart = new STCodeTime(period, c.getTime() - er).getDate();
			Date dend = new STCodeTime(period, c.getTime() + er).getDate();
			m.put("begin",
					format_day.format(dstart) + "T"
							+ format_time.format(dstart) + "Z");
			m.put("end",
					format_day.format(dend) + "T" + format_time.format(dend)
							+ "Z");
			list.add(m);
		}
		return list;
	}

	private String read(URL url) {

		String res = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				res = res + inputLine + ("\n");
			}

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

}
