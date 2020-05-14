package org.mgnl.nicki.mq.browser;

import java.util.Map;

import lombok.Data;

@Data
public class NickiMessage {
	private String message;
	private Map<String, String> properties;
}
