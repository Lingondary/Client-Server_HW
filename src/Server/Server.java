package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Server extends JFrame implements ServerInterface{
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 550;
    private static final int HEIGHT = 550;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    public boolean isServerWorking;

    ArrayList<String> history = new ArrayList<>();
    public Server(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Server");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(1, 2));
        add(btnStart);
        add(btnStop);

        setVisible(true);

        isServerWorking = true;

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                System.out.println("Server.Server stopped.");
            }
        });


        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                System.out.println("Server.Server started.");
                getHistory();
            }
        });
    }

    public void gotMessage(String message) {
        history.add(message);
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startServer() {
        isServerWorking = true;
        System.out.println("Server started.");
        getHistory();
    }

    @Override
    public void stopServer() {
        isServerWorking = false;
        System.out.println("Server stopped.");
    }

    @Override
    public void sendMessage(String message) {
        // Метод для отправки сообщения серверу
        gotMessage(message);
    }

    @Override
    public ArrayList<String> getMessageHistory() {
        // Метод для получения истории сообщений с сервера
        return new ArrayList<>(history);
    }
}