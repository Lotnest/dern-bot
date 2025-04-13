package dev.lotnest.dernbot.wynncraft.api.player.association;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssociationLookupService {
    private final PlayerAssociationRepository playerAssociationRepository;
    private final AssociatedPlayerRepository associatedPlayerRepository;

    /**
     * Looks up the associations for a given player and returns a list of associated players with their aggregated counts
     * and computed probability.
     *
     * @param playerName The player to look up.
     * @return A list of associations (each with an associated player's name, aggregated count, and probability score).
     */
    public List<PlayerAssociationDTO> lookupAssociations(String playerName) {
        AssociatedPlayer player = associatedPlayerRepository.findById(playerName)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerName));

        long seenCount = player.getSeenCount();
        if (seenCount <= 0L) {
            return Collections.emptyList();
        }

        List<PlayerAssociation> associations = playerAssociationRepository.findByPlayerAOrPlayerB(playerName, playerName);
        Map<String, Long> aggregatedCounts = associations.stream()
                .collect(Collectors.groupingBy(association -> association.getPlayerA().equals(playerName) ? association.getPlayerB() : association.getPlayerA(),
                        Collectors.summingLong(PlayerAssociation::getCount)));

        return aggregatedCounts.entrySet().stream()
                .map(entry -> {
                    String associatedPlayerName = entry.getKey();
                    long count = entry.getValue();
                    double probability = (double) count / seenCount;
                    return new PlayerAssociationDTO(associatedPlayerName, count, probability);
                })
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .toList();
    }

    public List<PlayerAssociationDTO> lookupAssociations(String playerName, double minProbability) {
        List<PlayerAssociationDTO> associations = lookupAssociations(playerName);
        return associations.stream()
                .filter(association -> association.getProbability() >= minProbability)
                .toList();
    }
}
