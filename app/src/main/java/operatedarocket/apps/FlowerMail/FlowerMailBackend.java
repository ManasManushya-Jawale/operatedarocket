package operatedarocket.apps.FlowerMail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import operatedarocket.ResourceLoader;
import operatedarocket.util.Mail.Mail;

public class FlowerMailBackend {
    public static ArrayList<Mail> mails = new ArrayList<>();

    public static void init() throws Exception {
        File dir = ResourceLoader.file("/mails");
        File[] files = dir.listFiles();

        for (File file : files) {
            String filename = file.getName();
            String[] info = filename.substring(0, filename.length() - 4).split("_");
            int yr = Integer.parseInt(info[0]);
            int mt = Integer.parseInt(info[1]);
            int dy = Integer.parseInt(info[2]);
            String sender = info[3];
            String title = info[4];
            String body = "";

            FileReader reader1 = new FileReader(file);
            BufferedReader reader2 = new BufferedReader(reader1);

            String line;

            while ((line = reader2.readLine()) != null) {
                body = body == "" ? line : body + '\n' + line;
            }

            reader1.close();
            reader2.close();

            LocalDate date = LocalDate.of(yr, mt, dy);

            mails.add(new Mail(sender, title, date, body){{send=true;}});
        }

        System.out.println(mails.size());
    }

    public static void main(String[] args) {
        try {
            init();
        } catch (Exception e) {
        }

        System.out.println("Emails:\n");

        for (Mail mail : mails) {
            System.out.println(mail.getText());
        }
    }
}
