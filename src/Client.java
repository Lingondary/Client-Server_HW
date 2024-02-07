import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.ArrayList;

public class Client extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2,3));
    private final JTextField tfIPAdress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("4");
    private final JTextField tfLogin = new JTextField("Lingondary");
    private final JPasswordField tfPassword = new JPasswordField("34356");
    private final JButton bthLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private boolean isConnectedToServer = false;

    Client(Server server){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH,HEIGHT);
        setTitle("Client");

        panelTop.add(tfIPAdress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(bthLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        tfMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(server);
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(server);
            }
        });

        setVisible(true);

        new Thread(new ServerChecker(server)).start();
        startScrollLogUpdater(server.history);
    }

    private void startScrollLogUpdater(ArrayList<String> history) {
        new Thread(() -> {
            while (true) {
                try {
                    updateScrollLog(history);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateScrollLog(ArrayList<String> history) {
        String currentText = log.getText();
        String[] currentLines = currentText.split("\n");

        for (String line : history) {
            if (!currentText.contains(line)) {
                SwingUtilities.invokeLater(() -> log.append(line + "\n"));
            }
        }
    }



    private void sendMessage(Server server) {
        String message = tfMessage.getText();
        server.gotMessage(tfLogin.getText()+ ':' + message);
        tfMessage.setText("");
    }

    private class ServerChecker implements Runnable {
        private final Server server;

        ServerChecker(Server server) {
            this.server = server;
        }

        @Override
        public void run() {
            while (!server.isServerWorking) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isConnectedToServer = true;
        }
    }
}