package kdd.xinghuangxu.parse.html.news;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LinkDB {
	
	//private String[]  links;
	
	private Queue<String> linkDB;
	
	public LinkDB(String[] links) {
		
		linkDB=new LinkedList<String>();
		for(int i=0;i<links.length;i++)
			linkDB.add(links[i]);
		//this.links=links;
	}
	
	public String next(){
		return linkDB.poll();
	}
	

}
