import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client {
    private String username;
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private ImageIcon logo;

    private JFrame loginFrame;
    private JPanel backgroundPanel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JButton loginButton;
    private JPanel labelFrame;
    private JPanel enterNameFrame;
    private JPanel btnLoginFrame;

    private JFrame chatFrame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JPanel TitleFrame;
    private JLabel textTitle;
    private JPanel areaChatFrame;
    private JScrollPane scrollPane;
    private JButton sendButton;
    private JPanel inputPanel;

    

    public void Login() {
        // Login frame
        loginFrame = new JFrame("TEAM TALKIES");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 300);
        loginFrame.setResizable(false);
        logo = new ImageIcon("logo2.png"); //create an ImageIcon
        loginFrame.setIconImage(logo.getImage());
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setLocationRelativeTo(null);

        // Set the background
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); //call method paintComponent in JPanel, clear area
                ImageIcon bgImage = new ImageIcon("src/bg.png");
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), null); //draw bg
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        usernameLabel = new JLabel("Enter Username: ");
        usernameLabel.setVerticalAlignment(JLabel.TOP);
        usernameLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 18));
        usernameLabel.setForeground(new Color(142, 179, 237));

        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(196, 231, 231));
        usernameField.setFont(new Font("Tahoma",Font.PLAIN, 17));
        usernameField.setCaretColor(Color.blue);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(142, 179, 237));
        loginButton.setForeground(Color.white);
        loginButton.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 16));
        
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                loginFrame.setVisible(false);
                ChatPage();
            }
        });

        labelFrame = new JPanel();
        labelFrame.setBackground(Color.black);
        labelFrame.setBounds(100, 130, 300, 20);
        labelFrame.setLayout(new BorderLayout());

        enterNameFrame = new JPanel();
        enterNameFrame.setBackground(Color.black);
        enterNameFrame.setBounds(100, 153, 300, 30);
        enterNameFrame.setLayout(new BorderLayout());

        btnLoginFrame = new JPanel();
        btnLoginFrame.setBounds(210, 190, 80, 25);
        btnLoginFrame.setLayout(new BorderLayout());
        
        loginFrame.add(labelFrame);
        loginFrame.add(enterNameFrame);

        labelFrame.add(usernameLabel);
        enterNameFrame.add(usernameField);

        loginFrame.add(btnLoginFrame);
        btnLoginFrame.add(loginButton);

        loginFrame.add(backgroundPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }

    public void ChatPage() {
        try {
            String[] word_Welcome = { "Hi", "Hello","Hey", "Good day", "Howdy", "Hey there" };
            // clientSocket = new Socket("192.168.43.9", 20000);
            clientSocket = new Socket("192.168.188.63", 20000);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"));

            // Chat frame
            chatFrame = new JFrame("TEAM TALKIES");
            chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chatFrame.setSize(500, 300);
            chatFrame.setResizable(false);
            chatFrame.setIconImage(logo.getImage());
            chatFrame.setLayout(new BorderLayout());
            chatFrame.setLocationRelativeTo(null);

            chatArea = new JTextArea();
            chatArea.setEditable(false);
            chatArea.setBackground(Color.BLACK); // กำหนดสีพื้นหลังของ JTextArea เป็นสีดำ
            chatArea.setForeground(Color.WHITE); // กำหนดสีข้อความใน JTextArea เป็นสีขาว
            chatArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
            
            TitleFrame = new JPanel();
            TitleFrame.setBackground(new Color(52, 29, 106));
            TitleFrame.setPreferredSize(new Dimension(500, 30));
            TitleFrame.setLayout(new BorderLayout());

            String word = word_Welcome[(int) (Math.random() * word_Welcome.length)]; //สุ่มคำทักทาย
            textTitle = new JLabel( " " + word + " " +username);
            textTitle.setVerticalAlignment(JLabel.TOP);
            textTitle.setFont(new Font("Tahoma",Font.BOLD, 22));
            textTitle.setForeground(new Color(102, 252, 241));
            TitleFrame.add(textTitle);

            areaChatFrame = new JPanel();
            areaChatFrame.setBackground(Color.BLACK);

            scrollPane = new JScrollPane(chatArea);

            messageField = new JTextField();
            messageField.setBackground(new Color(196, 231, 231));
            messageField.setFont(new Font("Tahoma",Font.PLAIN, 17));
            messageField.setCaretColor(Color.blue);
            messageField.addActionListener(new ActionListener() { //waitting for event press enter
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    sendMessage(message);
                    messageField.setText("");
                }
            });

            sendButton = new JButton("Send");
            sendButton.setBackground(new Color(142, 179, 237));
            sendButton.setForeground(Color.white);
            sendButton.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 15));
            sendButton.addActionListener(new ActionListener() { //waitting for press button
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    sendMessage(message);
                    messageField.setText("");
                }
            });

            inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(messageField, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            chatFrame.add(TitleFrame, BorderLayout.NORTH);
            chatFrame.add(scrollPane, BorderLayout.CENTER);
            chatFrame.add(inputPanel, BorderLayout.SOUTH);

            chatFrame.setVisible(true);

            // Start receiving messages
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String message = reader.readLine();
                            chatArea.append(" " + message + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();

            // Send username to server
            sendMessage(username);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { //create thread for run GUI
            @Override
            public void run() {
                Client client = new Client();
                client.Login();
            }
        });
    }
}
