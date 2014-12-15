package ua.net.la.dragonwhite.httpserver.handlers;

import java.net.InetSocketAddress;
import java.util.Map.Entry;

import io.netty.handler.codec.http.HttpRequest;
import ua.net.la.dragonwhite.httpserver.statistics.IpQueryCounter;
import ua.net.la.dragonwhite.httpserver.statistics.Query;
import ua.net.la.dragonwhite.httpserver.statistics.Statistics;
import ua.net.la.dragonwhite.httpserver.tools.Mapped;
import ua.net.la.dragonwhite.httpserver.tools.ResponseHandler;

@Mapped(uri = "/status")
public class StatusHandler extends ResponseHandler {

	public StatusHandler(InetSocketAddress remoteAddress, HttpRequest request) {
		super(remoteAddress, request);
		fillBuffer();
	}

	private void fillBuffer() {
		buffer.setLength(0);
		// 1.
		buffer.append("<head></head><body>1. Server request count: ")
				.append(Statistics.getCount())
				// 5.
				.append("<br>5. Active clients: ")
				.append(Statistics.getActiveCount())
				// 2.
				.append("<br>2. Server Unique query(1 query in 1 ip): ")
				.append(Statistics.getUnique().size())
				// 3.
				.append("<br><table align=left border = 1 width= 60%><Caption>3. Request counter by Ip </caption>")
				.append("<thead><tr><th>IP</th><th>Count</th><th>Last Request</th> </tr></thead><tbody>");
		for (Entry<String, IpQueryCounter> pair : Statistics.getIpCounter()
				.entrySet()) {
			buffer.append("<tr><td>").append(pair.getKey()).append("</td><td>")
					.append(pair.getValue().getCount()).append("</td><td>")
					.append(pair.getValue().getTime()).append("</td></tr>");
		}
		// 4.
		buffer.append(
				"</tbody></table><table align=right border=1 width= 40%><Caption>4. Redirections</caption>")
				.append("<thead><tr><th>URL</th><th>Count</th></tr></thead><tbody>");
		for (Entry<String, Integer> pair : Statistics.getRedirects().entrySet()) {
			buffer.append("<tr><td>").append(pair.getKey()).append("</td><td>")
					.append(pair.getValue()).append("</td></tr>");
		}
		// 6.
		buffer.append(
				"</tbody></table><table border=1 width= 100%><Caption>6. last 16 queries</caption>")
				.append("<thead><tr><th>src_ip</th><th>URL</th><th>time</th><th>send_bytes</th>")
				.append("<th>received_bytes</th><th>speed(B\\s)</th></tr></thead><tbody>");
		for (Query q : Statistics.getQueries()) {
			buffer.append("<tr><td>").append(q.getSrc_ip()).append("</td><td>")
					.append(q.getUri()).append("</td><td>")
					.append(q.getTimestamp()).append("</td><td>")
					.append(q.getSent_bytes()).append("</td><td>")
					.append(q.getReceived_bytes()).append("</td><td>")
					.append(q.getSpeed()).append("</td></tr>");
		}
		buffer.append("</tbody></table>").append("</body></html>");
	}
}
