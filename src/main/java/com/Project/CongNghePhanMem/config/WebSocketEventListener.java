package com.Project.CongNghePhanMem.config;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.Project.CongNghePhanMem.chat.ChatMessage;
import com.Project.CongNghePhanMem.chat.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j


public class WebSocketEventListener {
	
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(WebSocketEventListener.class);
	
	@Autowired
    private SimpMessageSendingOperations messageTemplate;
		
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if(username != null) {
			log.info("User disconnected: {}", username);
//			var chatMessage = ChatMessage.builder()
//					.type(MessageType.LEAVER)
//					.build();
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(MessageType.LEAVER);

			messageTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}			
}