package kdd.xinghuangxu.parse.html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
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

import org.apache.hadoop.io.IOUtils;
import org.xml.sax.InputSource;




/**
 * Input URL address
 * Output HTML Source in String
 * @author xinghuang xu
 */

public class HtmlSource {
	
	private String url;
	
	private byte[] content;
	
	
	
	public HtmlSource(String url) throws IOException{
		this.url=url;
		content=getUrlSourceBytes();
	}
	

	public  void Write() {

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

	public  String getUrlSourceString(String url) throws IOException {
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
	
	public  void WriteOutSource(String fileName) throws IOException{
		File f=new File("fileName");
		OutputStream o=new DataOutputStream(new FileOutputStream(f));
		o.write(content);
		o.close();
	}
	
	
	public  byte[] getUrlSourceBytes() throws IOException {
		URL page = new URL(url);
		URLConnection yc = page.openConnection();
		InputStream i = yc.getInputStream();
//		File f=new File("outStream.html");
//		OutputStream o=new DataOutputStream(new FileOutputStream(f));
		int read=0;
		List<Byte> bytes=new ArrayList<Byte>();
		while((read=i.read())!=-1){
			//o.write(read);
			bytes.add((byte)read);
		}
		byte[] byteArray=new byte[bytes.size()];
		for(int index=0;index<byteArray.length;index++){
			byteArray[index]=bytes.get(index);
		}
		i.close();
		return byteArray;
//		DataInputStream in=new DataInputStream(yc.getInputStream());
//		int length=in.available();
//		byte[] bytes=new byte[length];
//		in.readFully(bytes);
//		return bytes;
	}


	public byte[] getContent() {
		return content;
	}
	
	
	
	

}
