package com.team6.tinder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long memberId;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String value;

    public void updateValue(String token) {
        this.value = token;
    }

}
