package ua.net.la.dragonwhite.httpserver.statistics;

import java.util.Date;

public class IpQueryCounter{
	private int count;
	private Date time;
	
	public IpQueryCounter() {
		count = 1;
		time = new Date();
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		count++;
		time = new Date();
	}

	public Date getTime() {
		return time;
	}
}