import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

class User {
    private final static String QUEUE_NAME = "display";
    private final static String EXCHANGE_NAME ="display_exchange";
    private final static String QUEUE_NAME1 = "Section1";
    private final static String QUEUE_NAME2 = "Section2";
    public static boolean singleton = false;


    public static void sendText(String s) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.queueDeclare(QUEUE_NAME+s.indexOf(s.length()-1), false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME+s,"fanout");
            String message = ReadFile.getFileContent(s+".txt");
            channel.basicPublish(EXCHANGE_NAME+s, "", null, message.getBytes());
            System.out.println(" [x] Sent text for display");
        }
    }
    public static void requestDisplay(String s) throws Exception {
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String queueName = channel.queueDeclare().getQueue();
            channel.exchangeDeclare(EXCHANGE_NAME+s,"fanout");
            channel.queueBind( queueName,EXCHANGE_NAME+s,"");
            System.out.println(" [*] waiting to receive text");
            DeliverCallback deliverCallback = (consumerTag , delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
              if (s.equals("section1"))  TextEditor.content1 = message;
                else TextEditor.content2 = message;
                TextEditor.updateContent(s);
                System.out.println(" [x] received text ");
            };

            channel.basicConsume( queueName , true , deliverCallback, consumerTag -> {} );
        }

    }

    public static void RequestSection1() throws Exception {
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME1, false, false, false, null);
            //  System.out.println(" [*] waiting for message to exit press ctrl + c");

            DeliverCallback deliverCallback = (consumerTag , delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                ModifyFile.ModifySection("C:\\my-workspace\\TextEditor2\\src\\section1.txt",message);


                System.out.println(" [x]section1 is modified ");
            };

            channel.basicConsume(QUEUE_NAME1 , true , deliverCallback, consumerTag -> {} );
        }

    }
    public static void RequestSection2() throws Exception {
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
            //  System.out.println(" [*] waiting for message to exit press ctrl + c");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                ModifyFile.ModifySection("C:\\my-workspace\\TextEditor2\\src\\section2.txt", message);


                System.out.println(" [x] section 2 is modified");
            };

            channel.basicConsume(QUEUE_NAME2, true, deliverCallback, consumerTag -> {
            });
        }
    }

    public static void updateSection(String s,String queue) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.queueDeclare(queue, false, false, false, null);
            String message = s;
            channel.basicPublish("", queue, null, message.getBytes());
            System.out.println(queue+ " is updating");
        }
    }


}