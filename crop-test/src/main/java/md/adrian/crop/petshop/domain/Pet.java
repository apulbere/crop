package md.adrian.crop.petshop.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "pet")
@Entity
public class Pet {

    @Id
    private Long id;

    @Column
    private LocalDate birthdate;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_type_id")
    private PetType petType;

    @OneToMany(mappedBy = "pet")
    private List<PetFeature> features;

    @Column
    private boolean active;
}
