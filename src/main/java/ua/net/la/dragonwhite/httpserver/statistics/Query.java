package ua.net.la.dragonwhite.httpserver.statistics;

import java.net.InetSocketAddress;
import java.util.Date;

public class Query {
	private long received_bytes;
	private long sent_bytes;
	// (bytes/second)
	private long speed;
	private InetSocketAddress src_ip;
	private Date timestamp;
	private String uri;

	public Query(InetSocketAddress src_ip, String uri, Date timestamp,
			long sent_bytes, long received_bytes, long speed) {
		this.src_ip = src_ip;
		this.uri = uri;
		this.timestamp = timestamp;
		this.sent_bytes = sent_bytes;
		this.received_bytes=received_bytes;
		this.speed = speed;
	}

	public long getReceived_bytes() {
		return received_bytes;
	}

	public long getSent_bytes() {
		return sent_bytes;
	}

	public long getSpeed() {
		return speed;
	}

	public InetSocketAddress getSrc_ip() {
		return src_ip;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getUri() {
		return uri;
	}

}
