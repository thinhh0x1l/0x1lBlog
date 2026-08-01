package top.blogapi.gamification.shop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "item_catalog")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 500)
    String description;

    @Column(nullable = false, length = 50)
    String category;

    @Column(nullable = false, length = 20)
    String rarity;

    @Column(length = 20)
    String durationType;

    Integer durationDays;

    Integer priceCoins;

    Integer priceGems;

    Integer priceUsd;

    Integer maxSupply;

    Integer currentSupply;

    @Column(columnDefinition = "TEXT")
    String effectConfig;

    @Column(nullable = false)
    Boolean isActive;

    Instant createdAt;

    Instant updatedAt;
}
