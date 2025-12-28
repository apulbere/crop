package md.adrian.crop.petshop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "pet_feature")
@Entity
public class PetFeature {

    @Id
    private Long id;

    @Column
    private String feature;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
