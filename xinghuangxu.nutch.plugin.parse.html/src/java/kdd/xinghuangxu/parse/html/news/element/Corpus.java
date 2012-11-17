package kdd.xinghuangxu.parse.html.news.element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import kdd.xinghuangxu.parse.html.news.LinkDB;
import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 *
 */
public class Corpus extends CompositeElement {

	public static final String DIR_NAME = "DB";

	public static final String CORPUS_FILE_NAME = "corpus_db.xml";

	private Map<String, Document> corpus = null;
	
	private LinkDB links;

	public Corpus(LinkDB links){
		this.links=links;
		corpus = new HashMap<String, Document>();
	}
	
//	public Corpus() {
//		corpus = new HashMap<String, StringBuilder>();
//	}

	// private StringBuilder sb = new StringBuilder();

	public void add(String key, Document value) {
		Document val = corpus.get(key);
		if (val == null) {
			//System.out.println(key);
			corpus.put(key, value);
		}
	}

	public final void Write(BufferedWriter out) throws IOException {
		out.write(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<corpus>");
		StringBuilder[] sbs = corpus.values().toArray(
				new StringBuilder[corpus.size()]);
		for (int i = 0; i < sbs.length; i++) {
			sb.append("<document>");
			sb.append(sbs[i].toString());
			sb.append("</document>");
		}
		sb.append("</corpus>");
		return sb.toString();
	}

	public void Write() {
		try {
			// Create file
			String current = new File(".").getCanonicalPath();
			File dir = new File(current + "\\" + DIR_NAME);
			dir.mkdir();
			File file = new File(dir, CORPUS_FILE_NAME);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Write(out);
			out.close();
			System.out.println("Successfully write out to: "
					+ file.getAbsolutePath());
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void parse() throws MalformedURLException, NewsParsingException, IOException, Exception{
		String url;
		while(( url = links.next())!=null){
			Document doc=new Document();
			doc.parse(new ParseHelper(url));
			add(url,new Document());
		}
	}
	
	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		
	}

}
