package capstone.models;

import java.util.ArrayList;
import java.util.List;

public class Campaign {

    private List <Session> sessionList = new ArrayList<>();
    private List <CampaignUser> userList = new ArrayList<>();
    private int CampaignId;
    private int userId;
    private String name;
    private String description;
    private String type;
    private String city;
    private String state;
    private int sessionCount
    private int maxPlayers;
    int currentPlayers;

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public List<CampaignUser> getUserList() {
        return userList;
    }

    public void setUserList(List<CampaignUser> userList) {
        this.userList = userList;
    }

    public int getCampaignId() {
        return CampaignId;
    }

    public void setCampaignId(int campaignId) {
        CampaignId = campaignId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }
}
