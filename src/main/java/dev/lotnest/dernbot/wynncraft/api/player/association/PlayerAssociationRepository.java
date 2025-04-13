package dev.lotnest.dernbot.wynncraft.api.player.association;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerAssociationRepository extends JpaRepository<PlayerAssociation, PlayerAssociationId> {
    List<PlayerAssociation> findByPlayerAOrPlayerB(String playerA, String playerB);
}