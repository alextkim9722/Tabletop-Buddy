package capstone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class User extends org.springframework.security.core.userdetails.User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    @Getter
    @Setter
    private int userId;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String state;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private List< UserSession > sessionList;

    @Getter
    @Setter
    private List< UserCampaign > campaignList;

    @Getter
    @Setter
    private List< Campaign > hostedCampaignList;

    @Getter
    @Setter
    private List< UserSchedule > userScheduleList;

    public User(int appUserId, String username, String password,
                   boolean disabled, List<String> roles) {
        super(username, password, !disabled,
                true, true, true,
                convertRolesToAuthorities(roles));
        this.username = username;
        this.userId = appUserId;
    }

    public User(@JsonProperty("userId") int userId,@JsonProperty("username") String username,@JsonProperty("city") String city,@JsonProperty("state") String state){
        super(username, "a", true, true, true, true, new ArrayList<>());
        this.city = city;
        this.state = state;
        this.userId = userId;
    }

    public User(int appUserId, String username, String password,
                boolean disabled, List<GrantedAuthority> roles,
                String city, String state, String description) {
        this(appUserId, username, password, disabled, convertAuthoritiesToRoles(roles));
        this.city = city;
        this.state = state;
        this.description = description;
    }

    private List<String> roles = new ArrayList<>();

    // Going from sql to server
    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () ->
                            String.
                                    format("%s cannot start with %s (it is automatically added)",
                                            role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }
        return authorities;
    }

    // Going from server to sql
    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}
