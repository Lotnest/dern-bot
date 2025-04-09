package dev.lotnest.dernbot.wynncraft.api.player.association;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerApiService;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerListResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerPollingService {
    private final PlayerApiService playerApiService;
    private final AssociatedPlayerRepository associatedPlayerRepository;
    private final PlayerAssociationRepository playerAssociationRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void pollOnlinePlayers() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            log.debug("Polling online players from API");

            PlayerListResponse onlinePlayers = playerApiService.getOnlinePlayers();
            Map<String, List<String>> serverPlayersMap = Maps.newHashMap();

            log.debug(onlinePlayers.toString());

            for (Map.Entry<String, String> entry : onlinePlayers.getPlayers().entrySet()) {
                String playerName = entry.getKey();
                String server = entry.getValue();

                serverPlayersMap.computeIfAbsent(server, _ -> Lists.newArrayList()).add(playerName);
                if (!associatedPlayerRepository.existsById(playerName)) {
                    Optional<AssociatedPlayer> associatedPlayer = associatedPlayerRepository.findById(playerName);
                    if (associatedPlayer.isEmpty()) {
                        AssociatedPlayer newPlayer = new AssociatedPlayer(playerName);
                        associatedPlayerRepository.save(newPlayer);
                    } else {
                        AssociatedPlayer player = associatedPlayer.get();
                        player.incrementSeenCount();
                        associatedPlayerRepository.save(player);
                    }
                }
            }

            for (Map.Entry<String, List<String>> serverPlayersEntry : serverPlayersMap.entrySet()) {
                String server = serverPlayersEntry.getKey();
                List<String> players = serverPlayersEntry.getValue();

                for (int i = 0; i < players.size(); i++) {
                    for (int j = i + 1; j < players.size(); j++) {
                        String player1 = players.get(i);
                        String player2 = players.get(j);

                        PlayerAssociationId playerAssociationId = new PlayerAssociationId(player1, player2, server);
                        Optional<PlayerAssociation> existingAssociation = playerAssociationRepository.findById(playerAssociationId);

                        if (existingAssociation.isPresent()) {
                            PlayerAssociation playerAssociation = existingAssociation.get();
                            playerAssociation.incrementCount();
                            playerAssociationRepository.save(playerAssociation);
                        } else {
                            PlayerAssociation newPlayerAssociation = new PlayerAssociation(player1, player2, server, 1);
                            playerAssociationRepository.save(newPlayerAssociation);
                        }
                    }
                }
            }

            log.debug("Polling online players took {} ms", stopwatch.stop().elapsed().toMillis());
        } catch (Exception exception) {
            log.error("Error while polling online players: {}", exception.getMessage(), exception);
        }
    }
}
