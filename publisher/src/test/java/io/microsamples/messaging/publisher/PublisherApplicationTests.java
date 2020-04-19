package io.microsamples.messaging.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@DirtiesContext
class PublisherApplicationTests {

//	@Autowired
//	private Channels channels;
//
//	@Autowired
//	private MessageCollector collector;
//
//	@Test
//	void testMessages() {
//		BlockingQueue<Message<?>> messages = collector.forChannel(channels.outgoing());
//		assertThat(messages, receivesPayloadThat(containsString("talking to ya")));
//	}

}
