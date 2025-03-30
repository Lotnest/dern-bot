package dev.lotnest.dernbot.wynncraft.api.guild;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Members {
    private int total;
    private Map<String, Member.Owner> owner;
    private Map<String, Member.Chief> chief;
    private Map<String, Member.Strategist> strategist;
    private Map<String, Member.Captain> captain;
    private Map<String, Member.Recruiter> recruiter;
    private Map<String, Member.Recruit> recruit;

    public Member getByUsername(String username) {
        if (owner != null && owner.containsKey(username)) {
            return owner.get(username);
        } else if (chief != null && chief.containsKey(username)) {
            return chief.get(username);
        } else if (strategist != null && strategist.containsKey(username)) {
            return strategist.get(username);
        } else if (captain != null && captain.containsKey(username)) {
            return captain.get(username);
        } else if (recruiter != null && recruiter.containsKey(username)) {
            return recruiter.get(username);
        } else if (recruit != null && recruit.containsKey(username)) {
            return recruit.get(username);
        } else {
            return null;
        }
    }
}
