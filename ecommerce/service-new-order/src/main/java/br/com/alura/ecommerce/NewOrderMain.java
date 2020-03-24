package br.com.alura.ecommerce;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

//        Independente de dar erro ou não ele fai fechar a conexão atraves do metodo close
//        da classe KafkaDispatcher
        try (KafkaDispatcher dispatcher = new KafkaDispatcher<Order>()) {
                String userId = UUID.randomUUID().toString();
                String orderId = UUID.randomUUID().toString();
                BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);
                Order order = new Order(userId, orderId, amount);
                dispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

            try (KafkaDispatcher emailDispatcher = new KafkaDispatcher<Email>()) {
                //        ENVIO DE EMAIL
                String key = UUID.randomUUID().toString();
                String value = "Mensagem : ";
                String keyValue = value + key;
                String email = "Thank you for your order! We are processing your order!";
                emailDispatcher.send("ECOMMERCE_SEND_EMAIL", keyValue, email);
            }
        }
    }
}
