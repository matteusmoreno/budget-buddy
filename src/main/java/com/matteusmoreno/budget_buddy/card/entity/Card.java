package com.matteusmoreno.budget_buddy.card.entity;

import com.matteusmoreno.budget_buddy.card.constant.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private String cvv;
    private String expirationDate;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean active;
}
