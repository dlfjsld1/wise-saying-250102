import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstTest {


    @Test
    void t1() {
        int rst = 1;
        assertThat(rst).isEqualTo(1);
    }

    @Test
    void t2() {
        TestApp app = new TestApp();
        app.run();

        // aaa가 출력되는가?
        // assertThat(result).isEqualTo("aaa");
    }

    @Test
    void t3() {
//테스트봇 선입력
        TestBot testBot = new TestBot();
        String out = testBot.run("종료");

        assertThat(out).contains("명언앱을 종료합니다.");
    }

    @Test
    //테스트가 문서 역할을 할 수 있는 이유로 이런 설명이 있다.
    @DisplayName("앱 시작 시 '== 명언 앱 ==' 출력")
    void t4() {
       String out = TestBot.run("종료");

//        assertThat(out.toString()).isEqualTo("명언앱을 종료합니다.");
        //문자열 비교에서는 contains로 하는게 낫다.
        //왜냐하면 isEqualTo는 완전 똑같아야 하는데,
        //공백이나 줄바꿈 같은거까지 고려하면 피곤해짐
//        assertThat(out)
//                .contains("== 명언 앱 ==")
//                .contains("명언앱을 종료합니다.");
        //containsSubsequence라는 메서드도 있음. 이건 순서도 봐준다.
        assertThat(out)
                .containsSubsequence("== 명언 앱 ==", "명언앱을 종료합니다.");
    }

    @Test
    @DisplayName("등록 화면에서 명언과 작가를 입력 받는다.")
    void t5() {
        String out =
//        가독성이 안 좋은 방법
//                TestBot.run("등록\n현재를 사랑하라.\n작자미상\n종료");
//                쌍다옴표 세 개로 엔터를 먹는다.
                TestBot.run("""
                        등록
                        현재를 사랑하라.
                        종료
                        """);
        assertThat(out)
                .containsSubsequence("명언: ", "작가: ");
    }
}