package md.adrian.crop.petshop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "pet_type")
@Entity
public class PetType {

    @Id
    private Long id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "pet_category_id")
    private PetCategory petCategory;

}
