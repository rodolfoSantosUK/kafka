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
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) throws IOException {

        LogService logService = new LogService();

        try(KafkaService service = new KafkaService( LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService :: parse)){
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, String> rec) {
            System.out.println("Processing logging  ");
            System.out.println("LOG" + rec.topic());
            System.out.println("Key >>" + rec.key());
            System.out.println("Value >>" + rec.value());
            System.out.println("Partição >>" + rec.partition());
            System.out.println("Offset >>" + rec.offset());
  }

}
