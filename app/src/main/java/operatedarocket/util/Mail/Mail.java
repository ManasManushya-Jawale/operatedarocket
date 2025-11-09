package operatedarocket.util.Mail;

import java.time.LocalDate;

public class Mail {
    public String sender, title, body;
    public LocalDate date;
    public boolean send;

    public Mail(String sender, String title, LocalDate date, String body) {
        this.title = title;
        this.sender = sender;
        this.body = body;
        this.date = date;
    }

    public String getText() {
        return 
        "From " + sender +
        "\nTitle - " + title +
        "\nDate - " +
                date.getDayOfMonth() + "-" +
                date.getMonth().toString() + "-" +
                date.getYear() + "\n" + body;
    }
}
