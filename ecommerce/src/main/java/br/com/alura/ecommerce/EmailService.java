package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class EmailService {

    public static void main(String[] args) throws IOException {
        EmailService emailService = new EmailService();
        try(KafkaConsumerService service = new KafkaConsumerService( EmailService.class.getSimpleName(),
                                          "ECOMMERCE_SEND_EMAIL",
                                                 emailService :: parse,
                                                 String.class)){
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("Processing new order, checking for fraud ");
        System.out.println("Key >>" +       record.key());
        System.out.println("Value >>" +     record.value());
        System.out.println("Partição >>" +  record.partition());
        System.out.println("Offset >>" +    record.offset());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Order processed ");
    }



}
