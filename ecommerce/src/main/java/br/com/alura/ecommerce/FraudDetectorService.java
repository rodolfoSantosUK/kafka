package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class FraudDetectorService {

    public static void main(String[] args) {

        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        try(KafkaService service = new KafkaService(  FraudDetectorService.class.getSimpleName(),
                                           "ECOMMERCE_NEW_ORDER",
                                                  fraudDetectorService :: parse)){
            service.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse(ConsumerRecord<String, String> rec) {
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
