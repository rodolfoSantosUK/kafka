package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class KafkaConsumerService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    public KafkaConsumerService(ConsumerFunction parse, String groupId, Class<T> type, HashMap<String, String> extraProperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>(properties(type, groupId, extraProperties));
    }

     KafkaConsumerService(String groupId, String topic, ConsumerFunction parse, Class<T> type, HashMap<String, String> extraProperties ) {
        this(parse, groupId, type, extraProperties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaConsumerService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, HashMap<String, String> extraProperties) {
        this(parse, groupId, type, extraProperties);
        consumer.subscribe(topic);
    }

    void run() {
        while(true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            final List<ConsumerRecord<String, T>> allRecords = new ArrayList<>();
            records.forEach(allRecords::add);

            if (!allRecords.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros" );
                for( ConsumerRecord record : records ) {
                    parse.consume(record);
                }
            }
        }
    }

    private Properties properties(Class<T> type, String groupId, HashMap<String, String> extraProperties) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,  "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        // O auto-commit acontence no consumo de 1 em 1 mensagens
        // Isso aumenta as chances de evitar que consuma a mesma mensagem, porque nao deu tempo de enviar o commit
        // para o kafka em um caso de rebanceamento por exemplo
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1");
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.putAll(extraProperties);
        return properties ;
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
