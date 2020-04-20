package ecommerce;

import br.com.alura.ecommerce.KafkaConsumerService;
import br.com.alura.ecommerce.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadingReportService {

    private final KafkaDispatcher<User> orderKafkaDispatcher = new KafkaDispatcher<>();

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {

        ReadingReportService reportService = new ReadingReportService();
                                       // Passando o tipo que eu gostaria de desserializar
        try(KafkaConsumerService service = new KafkaConsumerService<>(  ReadingReportService.class.getSimpleName(),
                                            "USER_GENERATE_READING_REPORT",
                                                  reportService :: parse,
                                                  User.class,
                                                  new HashMap<>())){
                                                  service.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse(ConsumerRecord<String, User> rec) throws IOException {
        System.out.println("------------------------------------------------------------");
        System.out.println("Processing record for " + rec.value());

        User user = rec.value();
        File target =  new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }



}
