package md.adrian.crop.petshop.search;

import md.adrian.crop.petshop.domain.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {

    PetRecord map(Pet pet);
}
