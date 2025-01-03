import domain.wiseSaying.SystemController;
import domain.wiseSaying.WiseSayingController;
import java.util.Scanner;

public class TestApp {
    private final Scanner sc;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;

    public TestApp(Scanner sc) {
        this.sc = sc;
        wiseSayingController = new WiseSayingController(sc);
        systemController = new SystemController();
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while(true) {
            System.out.print("명령: ");
            String cmd = sc.nextLine(); //여기서 한 라인씩 가져오게 됨

            switch(cmd) {
                case "종료":
                    systemController.exit();
                case "등록":
                    wiseSayingController.actionWrite();
                case "목록":
                    wiseSayingController.actionPrint();
            }
            if(cmd.equals("종료")) {
                System.out.println("명언앱을 종료합니다.");
                break;
            } else if(cmd.equals("등록")) {
                wiseSayingController.actionWrite();
            } else if(cmd.equals("목록")) {
                wiseSayingController.actionPrint();
            }
        }


        // 테스트에 써먹을려고
    }
}