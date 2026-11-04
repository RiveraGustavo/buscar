package co.spring_boot.buscar;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BuscarApplication {
    private static final String QUEUE_NAME = "busca-persona";
    private static final String EXCHANGE_NAME = "servicios";
	public static void main(String[] args) {
		SpringApplication.run(BuscarApplication.class, args);
	}
 @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean 
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange)
            .with("meetings.buscar.persona");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(
        ConnectionFactory connectionFactory,
        Queue queue,
        TopicExchange exchange,
        Binding binding) {

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(binding);
        return rabbitAdmin;
    }

}
