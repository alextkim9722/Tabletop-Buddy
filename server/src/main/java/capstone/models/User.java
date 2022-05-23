package capstone.models;

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
    @NonNull
    int userid;

    @Getter
    @Setter
    @NonNull
    String username;

    @Getter
    @Setter
    @NonNull
    String city;

    @Getter
    @Setter
    @NonNull
    String state;

    @Getter
    @Setter
    List< UserSession > sessionList;

    @Getter
    @Setter
    List< UserCampaign > campaignList;

    @Getter
    @Setter
    List< Campaign > hostedCampaignList;

    @Getter
    @Setter
    List< UserSchedule > userScheduleList;

    public User(int appUserId, String username, String password,
                   boolean disabled, List<String> roles) {
        super(username, password, !disabled,
                true, true, true,
                convertRolesToAuthorities(roles));
        this.userid = appUserId;
    }

    private List<String> roles = new ArrayList<>();

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

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}
