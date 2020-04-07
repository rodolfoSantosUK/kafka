package br.com.ecommerce;

import br.com.alura.ecommerce.KafkaConsumerService;
import br.com.alura.ecommerce.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;

    BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
//       try {
//           connection.createStatement().execute("create table Users ( " +
//                   " uuid varchar(200) primary key, email varchar(200)  " +
//                   ")");
//       }catch(SQLException ex) {
//           ex.printStackTrace();
//       }
    }

    public static void main(String[] args) throws SQLException {

        BatchSendMessageService batchSendMessageService = new BatchSendMessageService();
        // Passando o tipo que eu gostaria de desserializar
        try(KafkaConsumerService service = new KafkaConsumerService<>(BatchSendMessageService.class.getSimpleName(),
                "SEND_MESSAGE_TO_ALL_USERS",
                batchSendMessageService :: parse,
                String.class,
                new HashMap<>())){
            service.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final KafkaDispatcher<User>   userDispatcher  = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, String> rec) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("Processing new batch, checking for new user. ");
        System.out.println("Key >> " +       rec.key());
        System.out.println("Topico >> " +     rec.value());
        System.out.println("Partição >> " +  rec.partition());
        System.out.println("Offset >> " +    rec.offset());

        for(User user : getAllUsers()) {
            userDispatcher.send(rec.value(), user.getUuid(), user );
        }

    }

    private List<User> getAllUsers() throws SQLException {

        ResultSet results = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();

        while(results.next()) {
            users.add(new User(results.getString(1)));
        }
        return users;
    }

}
