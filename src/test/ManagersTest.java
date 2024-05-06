package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tasks.Managers.getDefault;
import static tasks.Managers.getDefaultHistory;

class ManagersTest {

    @Test
    void createManagers() {
        assertNotNull(getDefault());
        assertNotNull(getDefaultHistory());
    }

}