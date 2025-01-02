import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstTest {
    @Test
    void t1() {
        //assert의 의미는
        assertThat(1).isEqualTo(1);
    }

    @Test
    void t2() {
        TestApp app = new TestApp();
        app.run();

        // aaa가 출력되는가?
//        assertThat().isEqualTo("aaa");
    }
}
