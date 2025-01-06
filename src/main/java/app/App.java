package app;

import app.domain.wiseSaying.SystemController;
import app.domain.wiseSaying.WiseSayingController;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;

    public App(Scanner sc) {
        this.sc = sc;
        wiseSayingController = new WiseSayingController(sc);
        systemController = new SystemController();
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while(true) {
            System.out.print("명령 ) ");
            String cmd = sc.nextLine(); //여기서 한 라인씩 가져오게 됨

            // 명령?부가정보 => 명령, 부가정보
            // 삭제?id=1 -> [삭제, id=1]
            String[] cmdBits = cmd.split("\\?");
            String actionName = cmdBits[0];
            switch(actionName) {
                case "종료" -> systemController.exit();
                case "등록" -> wiseSayingController.actionWrite();
                case "목록" -> wiseSayingController.actionPrint();
                case "삭제" -> wiseSayingController.actionDelete(cmdBits[1]);
//                case "수정" -> wiseSayingController.actionModify(cmdBits[1]);
                default -> System.out.println("올바른 명령이 아닙니다.");
            }
            if(cmd.equals("종료")) break;
        }
    }
}