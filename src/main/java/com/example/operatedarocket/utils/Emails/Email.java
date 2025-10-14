package com.example.operatedarocket.utils.Emails;

import java.util.Objects;

public class Email {
    public String id, body, title;
    public boolean send;

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Email))
            return false;
        Email email = (Email) o;
        return Objects.equals(title, email.title) &&
                Objects.equals(body, email.body);
    }
}
