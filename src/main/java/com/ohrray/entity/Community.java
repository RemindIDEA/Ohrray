package com.ohrray.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {
    @Id @GeneratedValue
    @Column(name="COMMUNITY_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MOUNTAIN_ID")
    private Mountain mountain;

    @OneToMany(mappedBy = "community")
    private List<CommunityBoard> list = new ArrayList<>();

}
