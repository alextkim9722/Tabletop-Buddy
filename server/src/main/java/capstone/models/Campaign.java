package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class Campaign {

    private List <Session> sessionList = new ArrayList<>();
    private List <CampaignUser> userList = new ArrayList<>();


    @NonNull
    private int campaignId;
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
    private int sessionCount;
    @NonNull
    private int maxPlayers;
    private int currentPlayers;
    private User user;
}
