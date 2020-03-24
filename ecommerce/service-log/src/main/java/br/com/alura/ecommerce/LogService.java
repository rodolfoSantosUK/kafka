package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) throws IOException {

        LogService logService = new LogService();

        HashMap<String, String> extraProperties = new HashMap<String, String> ();
        extraProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try(KafkaConsumerService service = new KafkaConsumerService( LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService :: parse,
                String.class,
                extraProperties
                )){
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, String> rec) {
            System.out.println("Processing logging  ");
            System.out.println("LOG >> " + rec.topic());
            System.out.println("Key >> " + rec.key());
            System.out.println("Value >> " + rec.value());
            System.out.println("Partição >> " + rec.partition());
            System.out.println("Offset >> " + rec.offset());
  }

}
