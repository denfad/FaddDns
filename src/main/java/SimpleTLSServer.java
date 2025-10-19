import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.Date;

public class SimpleTLSServer {
    private static final int PORT = 853;
    private static final String KEYSTORE_PATH = "/Users/denisfadeev/IdeaProjects/FaddDns/keys/fadeev.com.jks";
    private static final String KEYSTORE_PASSWORD = "test123";
    private static final String KEY_PASSWORD = "test123";

    public static void main(String[] args) {
        try {
            // Загружаем keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream keyStoreStream = new FileInputStream(KEYSTORE_PATH);
            keyStore.load(keyStoreStream, KEYSTORE_PASSWORD.toCharArray());

            // Инициализируем KeyManagerFactory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEY_PASSWORD.toCharArray());

            // Инициализируем SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Создаем SSLServerSocketFactory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Создаем SSLServerSocket
            SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(false); // Не требовать клиентский сертификат

            System.out.println("TLS Server started on port " + PORT);
            System.out.println("Keystore: " + KEYSTORE_PATH);

            while (true) {
                try {
                    // Принимаем входящее соединение
                    SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

                    // Обрабатываем соединение в отдельном потоке
                    new ClientHandler(clientSocket).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private SSLSocket clientSocket;

        public ClientHandler(SSLSocket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                // Начинаем TLS handshake
                clientSocket.startHandshake();

                // Получаем информацию о сессии
                SSLSession session = clientSocket.getSession();
                System.out.println("SSL Session established:");
                System.out.println("  Protocol: " + session.getProtocol());
                System.out.println("  Cipher Suite: " + session.getCipherSuite());
                System.out.println("  Peer Host: " + session.getPeerHost());

                // Создаем потоки для чтения/записи
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream()), true);

                // Читаем запрос от клиента
                String request;
                StringBuilder requestBuilder = new StringBuilder();
                while ((request = in.readLine()) != null && !request.isEmpty()) {
                    requestBuilder.append(request).append("\n");
                    if (request.isEmpty()) break; // Конец заголовков
                }

                String requestText = requestBuilder.toString();
                System.out.println("Received request:\n" + requestText);

                // Отправляем HTTP ответ
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        "Hello from TLS Server! Secure connection established.";

                out.println(response);
                out.flush();

                // Закрываем соединение
                clientSocket.close();
                System.out.println("Connection closed\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}