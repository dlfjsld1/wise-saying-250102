package app.domain.wiseSaying;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {


    @Test
    void t1() {
        int rst = 1;
        assertThat(rst).isEqualTo(1);
    }

    @Test
    void t2() {
//        app.TestApp app = new app.TestApp();
//        app.run();

        // aaa가 출력되는가?
        // assertThat(result).isEqualTo("aaa");
    }

    @Test
    void t3() {
//테스트봇 선입력
        String out = TestBot.run("");
        assertThat(out)
                .contains("명령 )")
                .contains("명언앱을 종료합니다.");
    }

    @Test
    @DisplayName("명령을 여러번 입력할 수 있다.")
    void t4() {
        String out = TestBot.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        // "명령 )"의 출현 횟수를 계산
        long count = out.split("명령 \\)").length - 1;
        // 검증
        assertThat(count).isEqualTo(3); // 기대하는 횟수에 따라 값 수정
    }

    @Test
    //테스트가 문서 역할을 할 수 있는 이유로 이런 설명이 있다.
    @DisplayName("앱 시작 시 '== 명언 앱 ==' 출력")
    void t5() {
       String out = TestBot.run("");

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
    @DisplayName("등록 - 명언 1개 입력")
    void t6() {
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

    @Test
    @DisplayName("등록 - 명언 1개 입력, 명언 번호 출력")
    void t7() {
        String out = TestBot.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);
        assertThat(out)
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 - 명언 2개 입력, 명언 번호가 증가")
    void t8() {
        String out = TestBot.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                현재를 사랑하라.
                작자미상
                등록
                현재를 사랑하라.
                작자미상
                """);
        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("3번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 - 명언 2개를 입력하면 입력된 명언들이 출력된다")
    void t9() {
        String out = TestBot.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                """);
        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .containsSubsequence("2 / 작자미상 / 과거에 집착하지 마라.", "1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제 - id를 이용해서 해당 id의 명언을 삭제할 수 있다. 입력: 삭제?id=1")
    void t10() {
        String out = TestBot.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                """);
        assertThat(out)
                //포함해야 한다
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                //포함하지 말아야 한다
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");

    }
}