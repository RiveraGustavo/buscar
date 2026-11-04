package co.spring_boot.buscar;

import java.util.Scanner;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class BuscarRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private ConfigurableApplicationContext context;

    BuscarRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try(Scanner s = new Scanner(System.in)){
            System.out.println("El Id de la reunión");
            Long idReunion = s.nextLong();
            System.out.println("El Id del asistente");
            Long idAsistente = s.nextLong();
            InfoBuscar infoBuscar = new InfoBuscar();
            infoBuscar.setIdAsistente(idAsistente);
            infoBuscar.setIdReunion(idReunion);

            String mensaje = objectMapper.writeValueAsString(infoBuscar);
            rabbitTemplate.convertAndSend(
                "servicios",
                "meetings.buscar.persona",
                mensaje
            );
            System.out.println("Mensaje enviado a la cola 'busca-persona'");        
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            context.close();
        }
    }
    
}
