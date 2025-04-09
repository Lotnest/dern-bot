package dev.lotnest.dernbot.wynncraft.api.player.association;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociatedPlayerRepository extends JpaRepository<AssociatedPlayer, String> {
}
