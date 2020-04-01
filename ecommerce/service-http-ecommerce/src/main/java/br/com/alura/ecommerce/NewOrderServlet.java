package br.com.alura.ecommerce;

import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet  extends HttpServlet implements Servlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (KafkaDispatcher dispatcher = new KafkaDispatcher<Order>()) {
            try (KafkaDispatcher emailDispatcher = new KafkaDispatcher<String>()) {

                try {
                    String userId = UUID.randomUUID().toString();
                    String orderId = UUID.randomUUID().toString();

                    // URL -> http://localhost:8080/new?email=guilherme@email.com&amount=153

                    BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                    String emailCode =  req.getParameter("email");

                    Order order = new Order(userId, orderId, amount, emailCode);
                    dispatcher.send("ECOMMERCE_NEW_ORDER", emailCode, order);


                    //        ENVIO DE EMAIL
                    String key = UUID.randomUUID().toString();
                    String value = "Mensagem : ";
                    String keyValue = value + key;
                    String email = "Thank you for your order! We are processing your order!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);

                    System.out.println("Nova ordem foi enviada");

                    resp.getWriter().println("Nova ordem foi enviada");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
