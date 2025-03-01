package br.ifrn.edu.sisconf.service.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> void sendTask(String queueName, T data) {
        rabbitAdmin.declareQueue(new Queue(queueName, true));

        try {
            String json = objectMapper.writeValueAsString(data);
            
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setHeader("x-retries", 0);

            Message message = new Message(json.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(queueName, message);

            log.info(String.format("Task sent to %s queue", queueName));
        } catch (JsonProcessingException exception) {
            log.error("Failed to serialize task", exception);
        }

    }
}
