package hr.tvz.volontiraj.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityTest {

    @Test
    public void testIdConstructorSetsIdCorrectly() {
        Long expectedId = 42L;

        UserEntity user = new UserEntity(expectedId);

        assertEquals(expectedId, user.getId());
    }
}
