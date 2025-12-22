# CriteriaOperator (CrOp)

CriteriaOperator is a lightweight, zero dependency, and type-safe wrapper around Java Persistence Criteria API, which simplifies building queries, particularly useful within REST SQL (RSQL) context.

Example of REST query that can be interpreted by the library:

```shell
http://localhost:64503/pets?birthdate.gte=2010-01-11&nickname.like=Ba
```

Which results in the following select:
```sql
select
    p1_0.id,
    p1_0.birthdate,
    p1_0.name,
    p1_0.pet_type_id 
from
    pet p1_0 
where
    p1_0.name like ? escape '' 
    and p1_0.birthdate>=?
```

There are a couple of easy steps to achieve the above:
1. Add the dependency
```xml
<dependency>
    <groupId>md.adrian</groupId>
    <artifactId>crop</artifactId>
    <version>0.1.3</version>
</dependency>
```
2. Define a DTO that contains all fields necessary for filtering. For example, if you want to do filtering on `String` type then you choose `StringCriteriaOperator` from `md.adrian.crop.operator` package.
```java
@Getter
@Setter
public class PetSearchCriteria {
    private StringCriteriaOperator nickname;
    private LocalDateCriteriaOperator birthdate;
}
```
3. Create an instance of the service that will parse the query. It requires `EntityManager` in the constructor.
```java
@Bean
CriteriaOperatorService cropService(EntityManager entityManager) {
    return new CriteriaOperatorService(entityManager);
}
```
4. Finally, invoke the service's `create` method with the root entity and filter object. The result is a builder that lets you match the entity's meta-model with each field from the DTO. When done, execute the query.
```java
@GetMapping("/pets")
List<PetRecord> search(PetSearchCriteria petSearchCriteria) {
    return cropService.create(Pet.class, petSearchCriteria)
            .match(PetSearchCriteria::getNickname, Pet_.name)
            .match(PetSearchCriteria::getBirthdate, Pet_.birthdate)
            .getResultList()
            .stream()
            .map(petMapper::map)
            .toList();
}
```

For full example and to see all the capabilities of the library checkout [pet-shop](https://github.com/apulbere/pet-shop-crop) demo repository.
