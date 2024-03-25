package com.hong.ForPaw.service;

import com.hong.ForPaw.controller.DTO.ChatRequest;
import com.hong.ForPaw.domain.Alarm.Alarm;
import com.hong.ForPaw.domain.Chat.Message;
import com.hong.ForPaw.repository.Alarm.AlarmRepository;
import com.hong.ForPaw.repository.Chat.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrokerService {

    private final MessageRepository messageRepository;
    private final AlarmRepository alarmRepository;
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    private final RabbitListenerContainerFactory<?> rabbitListenerContainerFactory;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final AlarmService alarmService;
    private final MessageConverter converter;

    public void registerDirectExchange(String exchangeName){
        DirectExchange fanoutExchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(fanoutExchange);
    }

    public void registerDirectExQueue(String exchangeName, String queueName){
        DirectExchange directExchange = new DirectExchange(exchangeName);

        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);

        // routingKey는 큐 이름과 동일
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
        amqpAdmin.declareBinding(binding);
    }

    public void registerChatListener(String listenerId, String queueName) {
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setId(listenerId);
        endpoint.setQueueNames(queueName);
        endpoint.setMessageListener(m -> {
            Message message = (Message) converter.fromMessage(m);

            // 메시지 저장
            messageRepository.save(message);
        });

        rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, rabbitListenerContainerFactory, true);
    }

    public void registerAlarmListener(String listenerId, String queueName){
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setId(listenerId);
        endpoint.setQueueNames(queueName);
        endpoint.setMessageListener(m -> {
            Alarm alarm = (Alarm) converter.fromMessage(m);

            // 알람 실시간 전송 후 저장
            alarmService.send(alarm);
            alarmRepository.save(alarm);
        });

        rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, rabbitListenerContainerFactory, true);
    }

    public void produceChat(Long chatRoomId, Message message){
        String exchangeName = "chat.exchange";
        String routingKey = "room." + chatRoomId;

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    public void produceAlarm(Long userId, Alarm alarm) {
        String exchangeName = "alarm.exchange";
        String routingKey = "user." + userId;

        rabbitTemplate.convertAndSend(exchangeName, routingKey, alarm);
    }
}
