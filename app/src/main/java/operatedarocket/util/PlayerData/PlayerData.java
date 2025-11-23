package operatedarocket.util.PlayerData;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;

public class PlayerData {
    @Expose
    public String name;
    @Expose
    public LocalDate date;
}
