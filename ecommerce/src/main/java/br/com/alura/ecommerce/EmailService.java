package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EmailService {

    public static void main(String[] args) throws IOException {
        EmailService emailService = new EmailService();
        try(KafkaService service = new KafkaService( EmailService.class.getSimpleName(),
                                          "ECOMMERCE_SEND_EMAIL",
                                                 emailService :: parse)){
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
