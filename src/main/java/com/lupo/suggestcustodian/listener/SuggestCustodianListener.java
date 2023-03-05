package com.lupo.suggestcustodian.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lupo.akkapubsubconnector.listener.Listener;
import com.lupo.akkapubsubconnector.listener.PubSubManager;
import com.lupo.akkapubsubconnector.listener.PubSubSink;
import com.lupo.akkapubsubconnector.listener.PubSubSource;
import com.lupo.akkapubsubconnector.model.Message;
import com.lupo.suggestcustodian.model.Suggestion;
import com.lupo.suggestcustodian.stages.InitialStage;

import akka.NotUsed;
import akka.stream.alpakka.googlecloud.pubsub.ReceivedMessage;
import akka.stream.javadsl.Flow;

@Component
public class SuggestCustodianListener extends Listener {
    private static final Logger LOG = LoggerFactory.getLogger(SuggestCustodianListener.class);

    @Autowired
    private InitialStage initialStage;

    public SuggestCustodianListener(final PubSubSink pubSubSink,
                                    final PubSubSource pubSubSource,
                                    final PubSubManager pubSubPubSubManager) {
        super(pubSubSink,
              pubSubSource,
              pubSubPubSubManager);
    }

    @Override
    public Flow<ReceivedMessage, Message<Suggestion>, NotUsed> processFlow() {
        return Flow.<ReceivedMessage>create()
                   .via(initialStage.convertToMessage())
                   .via(initialStage.validateMessage());
    }

    @PostConstruct
    public void startThis() {
        start();
    }
}
