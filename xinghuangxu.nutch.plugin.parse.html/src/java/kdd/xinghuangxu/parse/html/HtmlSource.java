package kdd.xinghuangxu.parse.html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.hadoop.io.IOUtils;




/**
 * Input URL address
 * Output HTML Source in String
 * @author xinghuang xu
 */

public class HtmlSource {

	public static void Write(String url) {

		//String url = "http://www.bbc.co.uk/news/world-us-canada-20257840";
		try {
			// Create file
			FileWriter fstream = new FileWriter("out.html");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(getUrlSourceString(url));
			out.close();
			System.out.println("Successfully ended!");
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static String getUrlSourceString(String url) throws IOException {
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
	
	public static byte[] getUrlSourceBytes(String url) throws IOException {
		URL page = new URL(url);
		URLConnection yc = page.openConnection();
		DataInputStream in=new DataInputStream(yc.getInputStream());
		int length=in.available();
		byte[] bytes=new byte[length];
		in.readFully(bytes);
		return bytes;
	}

}
