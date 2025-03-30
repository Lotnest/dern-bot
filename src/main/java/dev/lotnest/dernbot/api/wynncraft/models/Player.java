package dev.lotnest.dernbot.api.wynncraft.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lotnest.dernbot.core.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Player {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final ObjectMapper mapper = new ObjectMapper();

    private String username;
    private String uuid;
    private String rank;
    private Meta meta;
    private Class[] classes;
    private Guild guild;
    private Global global;

    public Player(String jsonBody) throws JsonProcessingException {
        JsonNode json = mapper.readTree(jsonBody);
        JsonNode data = json.get("data").get(0);

        for (Iterator<Map.Entry<String, JsonNode>> jsonNodeEntryIterator = data.fields(); jsonNodeEntryIterator.hasNext(); ) {
            Map.Entry<String, JsonNode> jsonNodeEntry = jsonNodeEntryIterator.next();

            switch (jsonNodeEntry.getKey()) {
                case "username" -> username = jsonNodeEntry.getValue().asText();
                case "uuid" -> uuid = jsonNodeEntry.getValue().asText();
                case "rank" -> rank = jsonNodeEntry.getValue().asText();
                case "meta" -> meta = new Meta(jsonNodeEntry.getValue());
                case "classes" -> {
                    classes = new Class[jsonNodeEntry.getValue().size()];
                    for (int j = 0; j < jsonNodeEntry.getValue().size(); j++) {
                        classes[j] = new Class(jsonNodeEntry.getValue().get(j));
                    }
                }
                case "guild" -> guild = new Guild(jsonNodeEntry.getValue());
                case "global" -> global = new Global(this, jsonNodeEntry.getValue());
                default -> log.warn("Unknown field in player data: {}", jsonNodeEntry.getKey());
            }
        }
    }

    @Data
    public static class Meta {
        private long firstJoin;
        private long lastJoin;
        private final String server;
        private final boolean isVeteran;
        private final int playtime;
        private final boolean tagDisplay;
        private final String tag;

        private Meta(JsonNode meta) {
            try {
                firstJoin = dateFormat.parse(meta.get("firstJoin").asText()).getTime();
                lastJoin = dateFormat.parse(meta.get("lastJoin").asText()).getTime();
            } catch (ParseException exception) {
                log.error("Failed to parse date", exception);
                firstJoin = -1;
                lastJoin = -1;
            }
            server = meta.get("location").get("online").asBoolean() ? meta.get("location").get("server").asText() : null;
            isVeteran = meta.get("veteran").asBoolean();
            playtime = meta.get("playtime").asInt();
            tagDisplay = meta.get("tag").get("display").asBoolean();
            tag = JsonUtils.getNullableString(meta.get("tag"), "value");
        }
    }

    @Data
    public static class Class {
        private final String name;
        private final int level;
        private final int quests;
        private final List<String> questNames;
        private final int itemsIdentified;
        private final int mobsKilled;
        private final int pvpKills;
        private final int pvpDeaths;
        private final long blocksWalked;
        private final int logins;
        private final int deaths;
        private final int playtime;
        private final Skills skills;
        private final Professions professions;
        private final Dungeons dungeons;
        private final int discoveries;
        private final int eventsWon;
        private final boolean preEconomyUpdate;

        private Class(JsonNode data) {
            name = data.get("name").asText();
            level = data.get("level").asInt();
            quests = data.get("quests").get("completed").asInt();
            questNames = Lists.newArrayList();
            JsonNode questList = data.get("quests").get("list");
            for (int i = 0; i < questList.size(); i++) questNames.add(questList.get(i).asText());
            itemsIdentified = data.get("itemsIdentified").asInt();
            mobsKilled = data.get("mobsKilled").asInt();
            pvpKills = data.get("pvp").get("kills").asInt();
            pvpDeaths = data.get("pvp").get("deaths").asInt();
            blocksWalked = data.get("blocksWalked").asLong();
            logins = data.get("logins").asInt();
            deaths = data.get("deaths").asInt();
            playtime = data.get("playtime").asInt();
            skills = new Skills(data.get("skills"));
            professions = new Professions(data.get("professions"));
            dungeons = new Dungeons(data.get("dungeons"));
            discoveries = data.get("discoveries").asInt();
            eventsWon = data.get("eventsWon").asInt();
            preEconomyUpdate = data.get("preEconomyUpdate").asBoolean();
        }

        public int getCombatLevel() {
            return professions.profLevels.get("combat").level;
        }

        public int getProfessionsLevel() {
            return level - getCombatLevel();
        }

        @Data
        public static class Skills {
            private final Map<String, Integer> data;

            private Skills(JsonNode node) {
                data = Maps.newHashMap();
                Iterator<String> skillsIterator = node.fieldNames();
                while (skillsIterator.hasNext()) {
                    String skillName = skillsIterator.next();
                    data.put(skillName, node.get(skillName).asInt());
                }
            }
        }
    }

    @Data
    public static class Professions {
        private final Map<String, ProfessionLevel> profLevels;

        private Professions(JsonNode professions) {
            profLevels = Maps.newHashMap();
            Iterator<String> professionsIterator = professions.fieldNames();
            while (professionsIterator.hasNext()) {
                String profName = professionsIterator.next();
                profLevels.put(profName,
                        new ProfessionLevel(
                                professions.get(profName).get("level").asInt(),
                                professions.get(profName).get("xp").asText()
                        )
                );
            }
        }

    }

    public record ProfessionLevel(int level, String xp) {
    }

    @Data
    public static class Dungeons {
        private final int totalCompleted;
        private final Map<String, Integer> list;

        private Dungeons(JsonNode data) {
            list = Maps.newHashMap();
            totalCompleted = data.get("completed").asInt();

            JsonNode listNode = data.get("list");
            if (listNode == null) return;
            for (int i = 0; i < listNode.size(); i++) {
                JsonNode child = listNode.get(i);
                list.put(child.get("name").asText(), child.get("completed").asInt());
            }
        }
    }

    @Data
    public static class Guild {
        private final String name;
        private final String rank;

        private Guild(JsonNode guild) {
            name = JsonUtils.getNullableString(guild, "name");
            rank = JsonUtils.getNullableString(guild, "rank");
        }
    }

    @Data
    public static class Global {
        private final Player parent;

        private final long blocksWalked;
        private final int itemsIdentified;
        private final int mobsKilled;

        private final int totalLevelCombat;
        private final int totalLevelProfession;

        private final int pvpKills;
        private final int pvpDeaths;

        private final int logins;
        private final int deaths;
        private final int discoveries;
        private final int eventsWon;

        private Global(Player parent, JsonNode data) {
            this.parent = parent;
            itemsIdentified = data.get("itemsIdentified").asInt();
            mobsKilled = data.get("mobsKilled").asInt();

            totalLevelCombat = data.get("totalLevel").get("combat").asInt();
            totalLevelProfession = data.get("totalLevel").get("profession").asInt();

            pvpKills = data.get("pvp").get("kills").asInt();
            pvpDeaths = data.get("pvp").get("deaths").asInt();
            blocksWalked = data.get("blocksWalked").asLong();
            logins = data.get("logins").asInt();
            deaths = data.get("deaths").asInt();

            discoveries = data.get("discoveries").asInt();
            eventsWon = data.get("eventsWon").asInt();
        }

        public int getPlayTime() {
            int playtime = 0;
            for (Class wynnClass : parent.classes) playtime += wynnClass.playtime;
            return playtime;
        }

        public int getTotalLevelCombined() {
            return totalLevelCombat + totalLevelProfession;
        }
    }
}
