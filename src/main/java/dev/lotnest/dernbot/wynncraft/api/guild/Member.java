package dev.lotnest.dernbot.wynncraft.api.guild;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
public class Member {
    private String uuid;
    private boolean online;
    private String server;
    private long contributed;
    private int contributionRank;
    private String joined;

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Owner extends Member {}

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Chief extends Member {}

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Strategist extends Member {}

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Captain extends Member {}

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Recruiter extends Member {}

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @Jacksonized
    public static class Recruit extends Member {}
}
