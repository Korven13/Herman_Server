import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args) {

        ServerSocket server = null;
        Socket client;

        int portNumber = 55555;

        if (args.length >= 1) {
            portNumber = Integer.parseInt(args[0]);
        }

        try {
            server = new ServerSocket(portNumber);
        } catch (IOException ie) {
            System.out.println("Cannot open socket." + ie);
            System.exit(1);
        }
        System.out.println("ServerSocket is created " + server);


        while (true) {
            try {
                System.out.println("Waiting for connect request...");
                client = server.accept();
                System.out.println("Connect request is accepted...");
                String clientHost = client.getInetAddress().getHostAddress();
                int clientPort = client.getPort();
                System.out.println("Client host = " + clientHost + ", Client port = " + clientPort);

                InputStream clientIn = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientIn));
                String msgFromClient = br.readLine();
                System.out.println("Message recieved from client = " + msgFromClient);


                if (msgFromClient != null && !msgFromClient.equalsIgnoreCase("bye")) {
                    OutputStream clientOut = client.getOutputStream();
                    PrintWriter pw = new PrintWriter(clientOut, true);
                    String ansMsg = "Hello, " + msgFromClient;
                    pw.println(calculate(msgFromClient));
                }

                if (msgFromClient != null && msgFromClient.equalsIgnoreCase("bye")) {
                    server.close();
                    client.close();
                    break;
                }

            } catch (IOException ie) {
                System.out.println("Error: "+ ie.getMessage());

            }

        }

    }


    private static double calculate(String msgFromClient) {
        double number1;
        double number2;
        char operator=' ';
        
        if (msgFromClient.contains("+")) {
            operator = '+';
        } else if (msgFromClient.contains("-")) {
            operator = '-';
        } else if (msgFromClient.contains("*")) {
            operator = '*';
        }
        else if (msgFromClient.contains("/")) {
            operator = '/';
        }
        else {
            System.out.println("Ogiltigt uttryck");
        }

        String[] numbers = msgFromClient.split("\\" + operator);

        try {
            number1 = Double.parseDouble(numbers[0]);
            number2 = Double.parseDouble(numbers[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ogiltigt uttryck", e);
        }

        switch (operator) {
            case '+':
                return number1 + number2;
            case '-':
                return number1 - number2;
            case '*':
                return number1 * number2;
            case '/':
                return number1 / number2;
        }

        return 0;

    }



}