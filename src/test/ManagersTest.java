package test;

import org.junit.jupiter.api.Test;
import tasks.*;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void createManagers() {
        Managers m = new Managers();
        assertNotNull(m.getDefault());
        assertNotNull(m.getDefaultHistory());
    }

}