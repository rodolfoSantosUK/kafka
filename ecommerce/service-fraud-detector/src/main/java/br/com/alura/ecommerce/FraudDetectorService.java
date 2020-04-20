package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

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

    private void parse(ConsumerRecord<String, Order> rec) throws ExecutionException, InterruptedException {
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

        Order order  =  rec.value();
//        if(isFraud(order)) {
//           // Quando é maior de 4500
//            orderKafkaDispatcher.send("ECOMMERCE_ORDER_REJECTED",
//                                       order.getEmail(),
//                                       order );
//
//
//            System.out.println(" Order is fraud !! ");
//        } else {
//
//            orderKafkaDispatcher.send("ECOMMERCE_ORDER_APROVED",
//                    order.getEmail(),
//                    order );
//
//            System.out.println( "Aproved: " + order);
//        }
    }



    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("10")) >=0;
    }

}
