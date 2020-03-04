package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());
        String value = "Mensagem 1";

        ProducerRecord<String, String> record =
                new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", value, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso ao enviar mensagem >> " + data.topic() + "::: partition " + data.partition()
                    + " /offset " + data.offset() + " / timestamp " + data.timestamp());
        };

        String email = "Thank you for your order! We are processing your order!";
        ProducerRecord<String,String> emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email,email);
        producer.send(record, callback).get(); // record para ficar registrado no kafka
        // Enviando e-mail
        producer.send(emailRecord, callback).get();

    }

    private static Properties properties() {
       Properties properties = new Properties();
       //Onde vai se conectar
       properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
       // Serializa de String para bytes
       properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
       properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

       return properties;
    }


}
