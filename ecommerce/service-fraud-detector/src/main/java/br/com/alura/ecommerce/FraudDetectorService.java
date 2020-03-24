package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.HashMap;

public class FraudDetectorService {

    public static void main(String[] args) {

        FraudDetectorService fraudDetectorService = new FraudDetectorService();
                                       // Passando o tipo que eu gostaria de desserializar
        try(KafkaConsumerService service = new KafkaConsumerService<>(  FraudDetectorService.class.getSimpleName(),
                                           "ECOMMERCE_NEW_ORDER",
                                                  fraudDetectorService :: parse,
                                                  Order.class,
                                                  new HashMap<>())){
            service.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse(ConsumerRecord<String, Order> rec) {
        System.out.println("Processing new order, checking for fraud. ");
        System.out.println("Key >> " +       rec.key());
        System.out.println("Value >> " +     rec.value());
        System.out.println("Partição >> " +  rec.partition());
        System.out.println("Offset >> " +    rec.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Order processed !! ");
    }

}
