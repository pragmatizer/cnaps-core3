package com.sk.cnaps.domain.model.event;

import java.time.LocalDateTime;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class DomainEvent {
	@Getter @Setter
	private Long publishedDateTime;
		
	public boolean publish(OutboundStream channel) {
		publishedDateTime = System.currentTimeMillis();
		
	    return channel.outboundChannel().send(MessageBuilder.withPayload(this)
	    								.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
	    								.build());
	}		
}
