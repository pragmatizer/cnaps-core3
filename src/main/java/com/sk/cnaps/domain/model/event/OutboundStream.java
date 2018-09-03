package com.sk.cnaps.domain.model.event;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

public interface OutboundStream {
	MessageChannel outboundChannel();
}
