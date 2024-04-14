package edu.java.domain.repository.jpa.entity;

import edu.java.domain.dto.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @Column(name = "link")
    String link;
    @Column(name = "type_id")
    int type;
    @Column(name = "checked_date")
    OffsetDateTime checkedDate;

    @ManyToMany(mappedBy = "links", fetch = FetchType.EAGER)
    private Set<ChatEntity> chats = new HashSet<>();

    public static List<Link> collectionEntityToListModel(Collection<LinkEntity> links) {
        return links.stream()
            .map(LinkEntity::toModel)
            .toList();
    }

    private Link toModel() {
        return new Link(link, type, checkedDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (o instanceof LinkEntity link) {
            return this.link.equals(link.link) && checkedDate.equals(link.checkedDate);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, checkedDate);
    }
}
