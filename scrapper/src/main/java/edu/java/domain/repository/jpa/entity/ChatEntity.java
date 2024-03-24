package edu.java.domain.repository.jpa.entity;

import edu.java.domain.dto.Chat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToMany
    @JoinTable(name = "link_chat",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link"))
    private Set<LinkEntity> links = new HashSet<>();

    public static List<Chat> collectionEntityToListModel(Collection<ChatEntity> links) {
        return links.stream()
            .map(ChatEntity::toModel)
            .toList();
    }

    private Chat toModel() {
        return new Chat(chatId);
    }
}
