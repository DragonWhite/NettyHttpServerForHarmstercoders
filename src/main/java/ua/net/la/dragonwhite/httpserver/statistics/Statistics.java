package ua.net.la.dragonwhite.httpserver.statistics;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
	private static List<Query> queries = Collections
			.synchronizedList(new ArrayList<Query>());

	private static int count = 0;

	private static Map<String, Integer> redirects = Collections
			.synchronizedMap(new HashMap<String, Integer>());

	private static Map<String, IpQueryCounter> ipQueryCount = Collections
			.synchronizedMap(new HashMap<String, IpQueryCounter>());

	private static int activeCount = 0;
	
	private static Map<String,String>unique = Collections
			.synchronizedMap(new HashMap<String, String>());

	public static synchronized void addQuery(Query con) {
		if (queries.size() >= 16)
			queries.remove(0);
		queries.add(con);
	}

	/**
	 * 6. task
	 * 
	 * @return last 16 queries
	 */
	public static List<Query> getQueries() {
		return new ArrayList<Query>(queries);
	}

	/**
	 * 1. overall queries count
	 * 
	 * @return count of quesries
	 */
	public static synchronized int getCount() {
		return count;
	}

	public static synchronized void addCount() {
		count++;
	}

	/**
	 * 5. overall active clients 
	 * 
	 * @return count of active clients
	 */ 
	public static synchronized int getActiveCount() {
		return activeCount;
	}

	public static synchronized void addActiveCount() {
		activeCount++;
	}

	public static synchronized void removeActiveCount() {
		activeCount--;
	}

	public static synchronized void addRedirect(String url) {
		url = url.toLowerCase();
		if (redirects.containsKey(url))
			redirects.put(url, redirects.get(url) + 1);
		else
			redirects.put(url, 1);
	}


	/**
	 * 4. Count of overall URL redirections
	 * 
	 * @return Map<URL,count>
	 *  URL - redirection to address
	 *  count - redirected count times to URL  
	 */ 
	public static synchronized Map<String, Integer> getRedirects() {
		return redirects;
	}

	public static synchronized void addIpCounter(InetSocketAddress ip) {
		String ad = ip.getHostString();
		if (ipQueryCount.containsKey(ad))
			ipQueryCount.get(ad).addCount();
		else
			ipQueryCount.put(ad, new IpQueryCounter());
	}

	/**
	 * 3. Count of queries from corresponding IP
	 * 
	 * @return Map <IP,IpQueryCounter>
	 * where IpQueryCounter contains queries count and date of last query
	 */ 
	public static synchronized Map<String, IpQueryCounter> getIpCounter() {
		return ipQueryCount;
	}

	/**
	 * 2. Unique query (one by IP)
	 * 
	 * @return Map <IP,IpQueryCounter>
	 * where IpQueryCounter contains queries count and date of last query
	 */ 
	public static Map<String,String> getUnique() {
		return unique;
	}

	public static void addUnique(String uri, InetSocketAddress remoteAddress) {
		if(uri == null || uri=="")
			return;
		uri = uri.toLowerCase();
		String remote = remoteAddress.getHostString();
		if(!unique.containsKey(uri) && !unique.values().contains(remote))
			unique.put(uri, remote);
	}

}
