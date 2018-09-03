package com.sk.cnaps.domain.model.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

//@Component
public class Publisher<A extends OutboundStream, B> {
	@Autowired
	private A channel;
		
	public boolean publish(B domainEvent) {
	    return channel.outboundChannel().send(MessageBuilder.withPayload(domainEvent)
	    								.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
	    								.build());
	}	
}