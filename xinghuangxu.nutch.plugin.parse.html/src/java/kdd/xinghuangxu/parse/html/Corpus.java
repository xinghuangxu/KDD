package kdd.xinghuangxu.parse.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Corpus {

	public static final String DIR_NAME = "DB";

	public static final String CORPUS_FILE_NAME = "corpus_db.xml";

	private Map<String, StringBuilder> corpus = null;

	public Corpus() {
		corpus = new HashMap<String, StringBuilder>();
	}

	// private StringBuilder sb = new StringBuilder();

	public void add(String key, StringBuilder value) {
		StringBuilder val = corpus.get(key);
		if (val == null) {
			System.out.println(key);
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

}
