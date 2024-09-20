import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static service.Managers.getDefault;
import static service.Managers.getDefaultHistory;

class ManagersTest {

    @Test
    void createManagers() {
        assertNotNull(getDefault());
        assertNotNull(getDefaultHistory());
    }

}