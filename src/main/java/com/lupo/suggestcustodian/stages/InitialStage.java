package com.lupo.suggestcustodian.stages;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.lupo.akkapubsubconnector.model.Message;
import com.lupo.suggestcustodian.model.Suggestion;

import akka.NotUsed;
import akka.stream.alpakka.googlecloud.pubsub.ReceivedMessage;
import akka.stream.javadsl.Flow;

@Component
public class InitialStage {

    private static final Logger LOG = LoggerFactory.getLogger(InitialStage.class);

    private final Gson gson;

    public InitialStage(Gson gson) {
        this.gson = gson;
    }

    public Flow<ReceivedMessage, Message<Suggestion>, NotUsed> convertToMessage() {
        return Flow.<ReceivedMessage>create()
                   .map(receivedMessage -> {
                       final byte[] messageBytes = Base64.getDecoder()
                                                         .decode(receivedMessage.message()
                                                                                .data()
                                                                                .get());
                       final String decodedMessage = new String(messageBytes);


                       final Suggestion suggestion = gson.fromJson(decodedMessage,
                                                                   Suggestion.class);

                       return createMessage(suggestion,
                                            receivedMessage.ackId());
                   });
    }

    //TODO: Add flows to validate suggestion, spellcheck ...
    public Flow<Message<Suggestion>, Message<Suggestion>, NotUsed> validateMessage() {
        return Flow.<Message<Suggestion>>create()
                   .map(message -> {
                       LOG.info("Validating message");
                       final Suggestion suggestion = message.getIn();
                       return message;
                   });
    }

    //TODO: Add flows to validate suggestion, spellcheck ...

    private Message<Suggestion> createMessage(final Suggestion suggestion,
                                              final String ackId) {
        final Message<Suggestion> message = new Message<>();
        message.setIn(suggestion);
        message.setAcknowledgmentId(ackId);
        return message;
    }
}
