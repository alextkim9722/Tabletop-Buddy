package capstone.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Campaign {

    private List <Session> sessionList = new ArrayList<>();
    private List <CampaignUser> userList = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private int CampaignId;

    @NonNull
    private int userId;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private String type;
    @NonNull
    private String city;
    @NonNull
    private String state;
    @NonNull
    private int sessionCount;
    @NonNull
    private int maxPlayers;
    @NonNull
    private int currentPlayers;
}
