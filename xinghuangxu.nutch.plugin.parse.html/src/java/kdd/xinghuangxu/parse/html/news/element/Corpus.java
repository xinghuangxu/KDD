package kdd.xinghuangxu.parse.html.news.element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kdd.xinghuangxu.parse.html.NewsHtmlParser;
import kdd.xinghuangxu.parse.html.news.LinkDB;
import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 * 
 */
public class Corpus {

	// public static final String DIR_NAME = "DB";
	private String corpusDir;

	public static final int MAX_SIZE = 10000;
	public static final String CORPUS_FILE_NAME = "corpus-";

	// private static final String LINKS_FILE_NAME = "links-";

	private Map<String, Document> corpus = null;

	private LinkDB linkDB;

	public int size() {
		return corpus.size();
	}

	public Corpus(String corpusDir, LinkDB links) {
		this.corpusDir = corpusDir;
		this.linkDB = links;
		corpus = new HashMap<String, Document>();
		try {
			parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public Corpus() {
	// corpus = new HashMap<String, StringBuilder>();
	// }

	// private StringBuilder sb = new StringBuilder();

	public void add(String key, Document value) {
		Document val = corpus.get(key);
		if (val == null) {
			// System.out.println(key);
			corpus.put(key, value);
		}
	}

	public final void Write(BufferedWriter out) throws IOException {
		out.write(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<corpus size=\"" + size() + "\"" + ">");
		Document[] sbs = corpus.values().toArray(new Document[corpus.size()]);
		for (int i = 0; i < sbs.length; i++) {
			// Delete all the related links which is not in the corpus
			sbs[i].cleanUpRelatedLinks(this);
			sb.append(sbs[i]);
		}
		sb.append("</corpus>");
		return sb.toString();
	}

	private static String getDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System
				.currentTimeMillis()));
	}

	public void Write() {
		try {
			// Create file
			// String current = new File(".").getCanonicalPath();
			// File dir = new File(current + "\\" + DIR_NAME);
			// dir.mkdir();
			String date = getDate();
			File file = new File(corpusDir, CORPUS_FILE_NAME + date + "-"
					+ size() + ".xml");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Write(out);
			out.close();

			// File file2 = new File(corpusDir, CORPUS_FILE_NAME + date
			// + ".txt");
			// BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
			// Document[] sbs = corpus.values().toArray(new
			// Document[corpus.size()]);
			// for (int i = 0; i < sbs.length; i++) {
			// sb.append(sbs[i].);
			// }

			System.out.println("Successfully write out to: "
					+ file.getAbsolutePath());
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void parse() throws MalformedURLException, NewsParsingException,
			IOException, Exception {
		String url;
		int count = 0;
		while ((url = linkDB.next()) != null && count < MAX_SIZE) {
			if (!corpus.containsKey(url)) {
				try {
					Document doc = new Document();
					doc.parse(new ParseHelper(url, linkDB));
					add(url, doc);
					count++;
					//Thread.sleep(3000);
				} catch (Exception e) {
				}
				linkDB.addLinkDB(url);
				if (size() == 50 || size() == 100 || size() == 200
						|| size() == 300 || size() == 400 || size() == 500
						|| size() == 600 || size() == 700 || size() == 800
						|| size() == 900||size()==1000) {
					Write();
					linkDB.write();
					if(size()==1000){
						corpus=new HashMap<String, Document>();
						//linkDB.clearQueue();
					}
					
				}
			}

		}

	}

	public boolean containDocument(String value) {
		if (corpus.containsKey(value))
			return true;
		return false;
	}

}
