package br.ifrn.edu.sisconf.service.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    public <T> void sendTask(String queueName, T data) {
        rabbitAdmin.declareQueue(new Queue(queueName, true));

        rabbitTemplate.convertAndSend(queueName, data);
        log.info(String.format("Task sent to %s queue", queueName));
    }
}
