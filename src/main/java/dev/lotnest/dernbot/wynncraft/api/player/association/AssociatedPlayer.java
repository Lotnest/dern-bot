package dev.lotnest.dernbot.wynncraft.api.player.association;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "associated_players")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssociatedPlayer {
    @Id
    private String name;
    private long seenCount;

    public AssociatedPlayer(String name) {
        this.name = name;
        this.seenCount = 1;
    }

    public void incrementSeenCount() {
        this.seenCount++;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssociatedPlayer)) return false;
        return new EqualsBuilder()
                .append(name, ((AssociatedPlayer) o).getName())
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .toHashCode();
    }
}
