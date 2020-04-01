package br.com.ecommerce;

import br.com.alura.ecommerce.KafkaConsumerService;
import br.com.ecommerce.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        connection.createStatement().execute("create table Users ( " +
                " uuid varchar(200) primary key, email varchar(200)  " +
                ")")  ;
    }

    public static void main(String[] args) throws SQLException {

        CreateUserService createUserService = new CreateUserService();
        // Passando o tipo que eu gostaria de desserializar
        try(KafkaConsumerService service = new KafkaConsumerService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUserService :: parse,
                Order.class,
                new HashMap<>())){
            service.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse(ConsumerRecord<String, Order> rec) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("Processing new order, checking for new user. ");
        System.out.println("Key >> " +       rec.key());
        System.out.println("Value >> " +     rec.value());
        System.out.println("Partição >> " +  rec.partition());
        System.out.println("Offset >> " +    rec.offset());

        Order order  =  rec.value();

        if(isNewUser(order.getEmail())) {
           insertNewUser(order.getEmail());
        }
    }

    private void insertNewUser(String email) throws SQLException {
        PreparedStatement insert = connection.prepareStatement("insert into Users (uuid,email) values (?,?) ");
        insert.setString(1, "uuid");
        insert.setString(2, "email");
        insert.execute();
        System.out.println("Usuário foi criado");
    }

    private boolean isNewUser(String email) {
        return true;
    }


}
