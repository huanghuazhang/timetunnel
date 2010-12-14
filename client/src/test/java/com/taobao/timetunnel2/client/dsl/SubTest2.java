package com.taobao.timetunnel2.client.dsl;

import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.passport;
import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.tunnel;
import static com.taobao.timetunnel2.client.dsl.TimeTunnel2.use;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;

import com.taobao.timetunnel2.client.dsl.broker.BrokerImpl.SendContent;
import com.taobao.timetunnel2.client.dsl.impl.Config;
import com.taobao.timetunnel2.client.dsl.impl.Tunnel;

/**
 * 
 * @author <a href=mailto:jiugao@taobao.com>jiugao</a>
 * @created 2010-11-18
 * 
 */
public class SubTest2 extends BaseServers {
	@Test
	public void sub() {
		Config.getInstance().setRouterServerList("localhost:" + randomRouterPort);
		String brokerUrl = "{\"sessionId\":\"xxxxx\",\"brokerserver\":[\"localhost:" + port + "\"]}";
		routerImpl.setBrokerUrls(brokerUrl);

		brokerImpl.setContentGen(new SendContent() {
			@Override
			public List<ByteBuffer> get2Send() {
				return new ArrayList<ByteBuffer>();
			}
		});

		use(passport("hello", "1111"));
		Tunnel t = tunnel("SubTest2", false, false, 10, 2);
		final SubscribeFuture sub = TimeTunnel2.subscribe(t);
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
		timer.schedule(new Runnable() {

			@Override
			public void run() {
				sub.cancel();

			}
		}, 3, TimeUnit.SECONDS);
		List<Message> list = sub.get();
		assertThat(list.size(), equalTo(0));
	}

	@SuppressWarnings("static-access")
	@After
	public void clear() {
		localBrokerService.stop();
		localRouterService.stop();
	}

	@Override
	void postStartBroker() {
	}

	@Override
	void postStartRouter() {
	}

	@Override
	void preStartBroker() {
	}

	@Override
	void preStartRouter() {
	}
}