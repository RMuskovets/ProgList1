package org.roman.proglist;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.roman.proglist.getInstalledAppList.*;

public class Main extends JFrame {

    private Main() {
        super("All programs");
        setLayout(new BorderLayout());
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Name");
        dtm.addColumn("Version");
        dtm.addColumn("Install date");
        dtm.addColumn("Publisher");
        List<List<String>> data = getDisplayNameDword(Objects.requireNonNull(getCurrentUserPersonalFolderPath()));
        for (List<String> row : data) {
            dtm.addRow(row.toArray());
        }
        add(new JScrollPane(new JTable(dtm)), "Center");
        JButton send = new JButton("Send to server");
        send.addActionListener(e -> {
            String col_delimiter = "^!^";
            String row_delimiter = "^$$^";
            String all;
            List<String> lines = new ArrayList<>();
            for (List<String> row : data) {
                lines.add(String.join(col_delimiter, row.toArray(new String[0])));
            }
            try {
                all = "data=" + URLEncoder.encode(String.join(row_delimiter, lines.toArray(new String[0])), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                return;
            }
            String ip = JOptionPane.showInputDialog("Enter site URL: ");
            String ret = "Success!";
            String myIp;
            try {
                myIp = "ip=" + URLEncoder.encode(InetAddress.getLocalHost().getHostAddress(), "utf-8");
            } catch (Exception e1) {
                return;
            }
            String myCompName;
            try {
                myCompName = "compName=" + URLEncoder.encode(InetAddress.getLocalHost().getHostName(), "UTF-8");
            } catch (Exception e1) {
                return;
            }
            all += "&" + myIp + "&" + myCompName;
            String type = "application/x-www-form-urlencoded";
            System.out.println(all);
            try {
                URL url = new URL(ip);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty( "Content-Type", type );
                conn.setRequestProperty( "Content-Length", String.valueOf(all.length()));
                OutputStream os = conn.getOutputStream();
                os.write(all.getBytes());
            } catch (IOException e1) {
                ret = "An error has been occurred.";
            }
            JOptionPane.showMessageDialog(null, ret);
        });
        add(send, "South");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        new Main();
    }
}
