package io.microsamples.messaging.publisher.contracts;

import io.microsamples.messaging.publisher.SyncSender;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMessageVerifier
@Slf4j
public abstract class TestBase {
    @Autowired
    private SyncSender syncSender;

    protected void sendIt() {
        syncSender.sendIt();
    }
}
