package md.adrian.crop.petshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "pet_category")
@Entity
public class PetCategory {

    @Id
    private Long id;

    @Column
    private String code;

}
