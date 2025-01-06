package app.global;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommandTest {
    @Test
    @DisplayName("command Test 최초 테스트")
    void t1() {
        Command cmd = new Command("");
    }

    @Test
    @DisplayName("action 네임을 얻어올 수 있다. 삭제?id=1이면 삭제가 나와야 한다.")
    void t2() {
        Command cmd = new Command("삭제?id=1");
        String actionName = cmd.getActionName();
        assertThat(actionName).isEqualTo("삭제");
    }

    @Test
    @DisplayName("action 네임을 얻어올 수 있다. 입력값이 삭제. ?가 없으면 잘 나오는지 확인")
    void t3() {
        Command cmd = new Command("삭제");
        String actionName = cmd.getActionName();
        assertThat(actionName).isEqualTo("삭제");
    }

    @Test
    @DisplayName("불완전한 입력이 들어왔을 때, 삭제?1, 삭제?id?1")
    void t4() {
        Command cmd = new Command("삭제");
        String actionName = cmd.getActionName();
        assertThat(actionName).isEqualTo("삭제");
    }

    @Test
    @DisplayName("입력값 - 삭제?id=1일 때, 파라미터를 달라고 하면 1이 나와야 한다.")
    void t5() {
        Command cmd = new Command("삭제?id=1");
        int id = cmd.getParam();
        assertThat(id).isEqualTo(1);
    }
}
