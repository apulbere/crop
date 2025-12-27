package md.adrian.crop.petshop;

import md.adrian.crop.petshop.search.PetRecord;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PetSearchTest extends BaseSearchTest {

    @Test
    void shouldFindByEq() {
        var actual = makeRequest(Map.of("id.eq", "2"));

        assertThat(actual)
            .hasSize(1)
            .allMatch(pet -> pet.id().equals(2L));
    }

    @Test
    void shouldCountByEq() {
        var actual = makeCountRequest(Map.of("id.eq", "2"));

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void shouldFindByNeq() {
        var actual = makeRequest(Map.of("id.neq", "2"));

        assertThat(actual).noneMatch(pet -> pet.id().equals(2L));
    }

    @Test
    void shouldFindByIdIn() {
        var actual = makeRequest(Map.of("id.in", "1,2,3"));

        assertThat(actual).hasSize(3)
            .map(PetRecord::id)
            .asList()
            .hasSameElementsAs(Set.of(1L, 2L, 3L));
    }

    @Test
    void shouldFindByLocalDateBetween() {
        class Util {

            static boolean isBetween(LocalDate before, LocalDate date, LocalDate after) {
                return before.equals(date) || after.equals(date) || date.isAfter(before) && date.isBefore(after);
            }
        }

        var startDate = LocalDate.parse("2002-12-01");
        var endDate = LocalDate.parse("2010-01-11");
        var actual = makeRequest(Map.of("birthdate.btw", startDate + "," + endDate));

        assertThat(actual)
            .isNotEmpty()
            .map(PetRecord::birthdate)
            .allMatch(date -> Util.isBetween(startDate, date, endDate));
    }

    @Test
    void shouldFindByBigDecimalGte() {
        var gtePrice = new BigDecimal("8.2");
        var actual = makeRequest(Map.of("price.gte", gtePrice));

        assertThat(actual)
            .isNotEmpty()
            .map(PetRecord::price)
            .allMatch(price -> price.compareTo(gtePrice) >= 0);
    }

    @Test
    void shouldFindByStringLike() {
        var startsWith = "Br";
        var actual = makeRequest(Map.of("nickname.like", startsWith));

        assertThat(actual)
            .isNotEmpty()
            .map(PetRecord::name)
            .allMatch(name -> name.startsWith(startsWith));
    }

    @Test
    void shouldFindByBoolean() {
        var actual = makeRequest(Map.of("active.eq", true));

        assertThat(actual)
                .isNotEmpty()
                .map(PetRecord::active)
                .allMatch(active -> active);
    }

    @Test
    void shouldFindByJoin() {
        var eqValue = "cat";
        var actual = makeRequest(Map.of("type.eq", eqValue));

        assertThat(actual)
            .isNotEmpty()
            .map(PetRecord::id)
            .allMatch(id -> id.equals(3L));
    }

    @Test
    void shouldFindByJoinMultiple() {
        var actual = makeRequest(Map.of("features.in", "fast,cute"));

        assertThat(actual).hasSize(2);
    }

    @Test
    void shouldFindByJoinMultipleCount() {
        var actual = makeCountRequest(Map.of("features.in", "fast,cute"));

        assertThat(actual).isEqualTo(2);
    }
}