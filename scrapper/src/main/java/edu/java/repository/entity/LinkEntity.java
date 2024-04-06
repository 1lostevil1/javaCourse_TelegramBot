package edu.java.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "link")
public class LinkEntity {

    public LinkEntity(
        String url,
        OffsetDateTime updatedAt,
        OffsetDateTime checkAt,
        String type,
        String data,
        Set<ChatEntity> chats
    ) {
        this.url = url;
        this.updateAt = updatedAt;
        this.checkAt = checkAt;
        this.linkType = type;
        this.data = data;
        this.chats = chats;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long linkId;
    private String url;
    private OffsetDateTime updateAt;
    private OffsetDateTime checkAt;
    private String linkType;
    private String data;
    @ManyToMany(mappedBy = "links")
    private Set<ChatEntity> chats = new HashSet<>();
}
