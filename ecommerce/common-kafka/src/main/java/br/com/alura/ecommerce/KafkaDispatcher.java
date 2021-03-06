package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, Message<T>> producer;

   public KafkaDispatcher() {
      this.producer = new KafkaProducer<String, Message<T>>(properties());
    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        // Significa que vai esperar que todas as outras replicas recebam as mensagens pelo lider
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    public void send(String topic, String keyValue, T payload) throws ExecutionException, InterruptedException {
        Message<T> value = new Message<>(new CorrelationId(), payload );
        ProducerRecord<String, Message<T>> record = new ProducerRecord<>(topic, keyValue, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando >> " + data.topic() + "::: partition >> " + data.partition()
                    + " /offset >> " + data.offset() + " / timestamp >>" + data.timestamp());
        };
        producer.send(record, callback).get(); // get espera o sucesso ou fracasso
    }

    @Override
    public void close() throws IOException {
        producer.close();
    }
}
