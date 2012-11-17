package kdd.xinghuangxu.parse.html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import kdd.xinghuangxu.parse.html.news.element.Corpus;

/**
 * Input URL address Output HTML Source
 * 
 * @author xinghuang xu
 */

public class HtmlSource {

	private URL url;

	private byte[] content;

	public HtmlSource(String url) throws IOException {
		this.url = new URL(url);
		content = getUrlSourceBytes();
	}

	public static void Write(String data, String fileName) {

		// String url = "http://www.bbc.co.uk/news/world-us-canada-20257840";
		try {
			// Create file
			String current = new File(".").getCanonicalPath();
			System.out.println(current);
			File dir = new File(current + "\\NEWS_DB");
			dir.mkdir();
			File file = new File(dir, fileName);
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(data);
			out.close();
			System.out.println("Successfully Write out to: " + fileName);
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public String getUrlSourceString(String url) throws IOException {
		URL page = new URL(url);
		URLConnection yc = page.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			a.append(inputLine);
		in.close();
		return a.toString();
	}

	public void WriteOutSource(String fileName) throws IOException {
		File f = new File(fileName);
		OutputStream o = new DataOutputStream(new FileOutputStream(f));
		o.write(content);
		o.close();
	}

	public byte[] getUrlSourceBytes() throws IOException {
		URL page = url;
		URLConnection yc = page.openConnection();
		InputStream i = yc.getInputStream();
		// File f=new File("outStream.html");
		// OutputStream o=new DataOutputStream(new FileOutputStream(f));
		int read = 0;
		List<Byte> bytes = new ArrayList<Byte>();
		while ((read = i.read()) != -1) {
			// o.write(read);
			bytes.add((byte) read);
		}
		byte[] byteArray = new byte[bytes.size()];
		for (int index = 0; index < byteArray.length; index++) {
			byteArray[index] = bytes.get(index);
		}
		i.close();
		return byteArray;
		// DataInputStream in=new DataInputStream(yc.getInputStream());
		// int length=in.available();
		// byte[] bytes=new byte[length];
		// in.readFully(bytes);
		// return bytes;
	}

	public byte[] getContent() {
		return content;
	}

	public static void Write(Corpus corpus, String dirName, String fileName) {
		try {
			// Create file
			String current = new File(".").getCanonicalPath();
			File dir = new File(current + "\\" + dirName);
			dir.mkdir();
			File file = new File(dir, fileName);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			corpus.Write(out);
			out.close();
			System.out.println("Successfully Write out to: " + fileName);
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
