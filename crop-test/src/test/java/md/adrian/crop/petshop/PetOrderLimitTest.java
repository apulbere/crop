package md.adrian.crop.petshop;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PetOrderLimitTest extends BaseSearchTest {

    @Test
    void shouldSortAndLimit() {
        var actual = makeRequest(Map.of(
                "order", "price,-id",
                "size", "2",
                "offset", "3"
        ));

        assertThat(actual).hasSize(2);

        var first = actual.get(0);
        assertThat(first.id()).isEqualTo(7L);

        var second = actual.get(1);
        assertThat(second.id()).isEqualTo(6L);
    }
}
