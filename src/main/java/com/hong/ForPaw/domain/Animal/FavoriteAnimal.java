package com.hong.ForPaw.domain.Animal;

import com.hong.ForPaw.domain.Animal.Animal;
import com.hong.ForPaw.domain.User.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @Builder
    public FavoriteAnimal(User user, Animal animal) {
        this.user = user;
        this.animal = animal;
    }
}