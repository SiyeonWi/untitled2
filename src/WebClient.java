import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebClient extends JFrame {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int DEFAULT_TIMEOUT = 5000;
    private JTextField urlField;
    private JTextField postField;
    private JEditorPane htmlview;

    public WebClient() {
        setTitle("Web Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        urlField = new JTextField();
        urlField.setColumns(40);
        JButton sendButton = new JButton("GET");
        htmlview = new JEditorPane();
        htmlview.setEditable(false);


        postField = new JTextField();
        postField.setColumns(40);
        JButton postButton = new JButton("POST");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                if (!url.isEmpty()) {
                    String response = null;
                    try {
                        response = getWebContentByGet(url, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
                    } catch (IOException ex) {
                        response = "Error Detected";
                    }
                    htmlview.setContentType("text/html");
                    htmlview.setText(response);
                }
            }
        });

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                if (!url.isEmpty()) {
                    String response = null;
                    try {
                        response = getWebContentByGet(url, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
                    } catch (IOException ex) {
                        response = "Error Detected";
                    }
                    htmlview.setContentType("text/html");
                    htmlview.setText(response);
                }
            }
        });

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("URL:"));
        topPanel.add(urlField);
        topPanel.add(sendButton);

        bottomPanel.add(new JLabel("POST:"));
        bottomPanel.add(postField);
        bottomPanel.add(postButton);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(new JScrollPane(htmlview), BorderLayout.CENTER);
    }


    public String getWebContentByGet(String urlString, final String charset, int timeout) throws IOException {
        if(urlString == null || urlString.length() == 0) {
            return null;
        }
        urlString =(urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4332; .NET CLR 2.0.50727)");

        connection.setRequestProperty("Accept", "text/html");
        connection.setConnectTimeout(timeout);

        try {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");

        }
        if(reader!=null) reader.close();
        if(connection!=null) connection.disconnect();
        return sb.toString();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WebClient webClient = new WebClient();
                webClient.setVisible(true);
            }
        });
    }
}
