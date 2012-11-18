package kdd.xinghuangxu.parse.html.news;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Used to keep the LinkDB 
 * Queue which save the relatives links when they are read from pages
 * Seeds are used to save the potential pages that can be crawled
 * @author xinghuang
 *
 */
public class LinkDB {

	// private String[] links;
	private String linkDir;

	private List<String> linkDB;

	private Queue<String> seeds;

	private Queue<String> queue;

	public LinkDB(String linkDir) throws IOException {

		linkDB = new ArrayList<String>();
		queue = new LinkedList<String>();
		seeds = new LinkedList<String>();
		// for(int i=0;i<links.length;i++)
		// queue.add(links[i]);
		// this.links=links;
		this.linkDir = linkDir;
		read();
	}

	public String next() {
		if (queue.size() == 0 && seeds.size() == 0)
			return null;
		while (queue.size() == 0&&seeds.size()>0)
		{
			addQueue(seeds.poll());
		}
		if(seeds.size()==0) return null;
		return queue.poll();
	}

	private static String getDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System
				.currentTimeMillis()));
	}

	public void read() throws IOException {
		// Read from Link_DB

		FileInputStream in = new FileInputStream(linkDir + "\\Link_DB.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			linkDB.add(strLine);
		}
		in.close();
		br.close();

		// Read from Seeds
		in = new FileInputStream(linkDir + "\\Seeds.txt");
		br = new BufferedReader(new InputStreamReader(in));
		while ((strLine = br.readLine()) != null) {
			seeds.add(strLine);
		}
		in.close();
		br.close();

	}

	public void write() throws IOException {
		// Write out the Link_DB
		File linkFile = new File(linkDir + "\\Link_DB.txt");
		BufferedWriter linkbr = new BufferedWriter(new FileWriter(linkFile));
		File linkBackupFile = new File(linkDir + "\\Link_DB" + getDate() + ".txt");
		BufferedWriter linkbrBackup = new BufferedWriter(new FileWriter(linkBackupFile));
		
		for (int i = 0; i < linkDB.size(); i++) {
			linkbr.write(linkDB.get(i));
			linkbr.newLine();
			linkbrBackup.write(linkDB.get(i));
			linkbrBackup.newLine();
		}
		linkbr.close();
		linkbrBackup.close();
		
		
		// Write out the Seeds
		File seedfileBackUp = new File(linkDir + "\\Seeds" + getDate() + ".txt");
		BufferedWriter seedbrBackUp = new BufferedWriter(new FileWriter(seedfileBackUp));
		File seedfile = new File(linkDir + "\\Seeds.txt");
		BufferedWriter seedbr = new BufferedWriter(new FileWriter(seedfile));
		String url;
		
		Iterator<String> iterator= queue.iterator();
		while(iterator.hasNext()){
			url=iterator.next();
			seedbr.write(url);
			seedbr.newLine();
			seedbrBackUp.write(url);
			seedbrBackUp.newLine();
		}
		seedbr.close();
		seedbrBackUp.close();
	
	}

	public void addQueue(String url) {
		if (!linkDB.contains(url)) {
			queue.add(url);
		}
	}

	public void addLinkDB(String url) {
		linkDB.add(url);
		
	}

//	public void clearQueue() {
//		// TODO Auto-generated method stub
//		
//	}

}
