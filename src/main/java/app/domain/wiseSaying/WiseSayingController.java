package app.domain.wiseSaying;

import app.global.Command;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner sc) {
        this.sc = sc;
        wiseSayingService = new WiseSayingService();
    }

    public void actionWrite() {
        System.out.print("명언: ");
        String content = sc.nextLine();
        System.out.print("작가: ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = wiseSayingService.write(content, author);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    public void actionPrint() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayingList = wiseSayingService.getAllItems();

        wiseSayingList.reversed().forEach(w -> {
            System.out.printf("%d / %s / %s\n", w.getId(), w.getAuthor(), w.getContent());
        });
    }

    public void actionDelete(Command cmd) {
        //id=1
        int id = cmd.getParamAsInt("id");
        boolean result = wiseSayingService.delete(id);
        if(!result) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
        }
    }

    public void actionModify(Command cmd) {
        int id = cmd.getParamAsInt("id");
        Optional<WiseSaying> opWiseSaying = wiseSayingService.getItem(id);
        WiseSaying wiseSaying = opWiseSaying.orElse(null);
        if (wiseSaying == null) {
            System.out.println("%번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.printf("명언(기존) : %s\n", wiseSaying.getContent());
        System.out.println("명언 : ");
        String newContent = sc.nextLine();

        System.out.printf("작가(기존) : %s\n", wiseSaying.getAuthor());
        System.out.println("작가 : ");
        String newAuthor = sc.nextLine();

        wiseSayingService.modify(wiseSaying, newContent, newAuthor);
        System.out.println("%d번 명언이 수정되었습니다.".formatted(id));
    }
}
