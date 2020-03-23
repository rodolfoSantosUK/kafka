package br.com.alura.ecommerce;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

//        Independente de dar erro ou não ele fai fechar a conexão atraves do metodo close
//        da classe KafkaDispatcher
        try(KafkaDispatcher dispatcher = new KafkaDispatcher()){

        String key = UUID.randomUUID().toString();
        String value = "Mensagem : ";
        String keyValue = value + key;

        dispatcher.send("ECOMMERCE_NEW_ORDER", keyValue, value);

        String email = "Thank you for your order! We are processing your order!";

        dispatcher.send("ECOMMERCE_SEND_EMAIL", keyValue, email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
