package app.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileTest {

    @Test
    @DisplayName("최초의 파일 테스트")
    void t1() {
        Util.File.test();
    }

    @Test
    @DisplayName("파일 생성. 내용이 없는 빈 파일 생성")
    void t2() {
        //파일 이름
        String file = "test/test.txt";

        Util.File.createFile(file);

        assertThat(Files.exists(Paths.get(file)))
                .isTrue();
    }

    @Test
    @DisplayName("파일 내용 읽어오기")
    void t3() {

        //파일을 Hello, World로 생성
        String testContent = "Hello, world!";
        String file = "test/test.txt";
        Util.File.write(file, testContent);

        String content = Util.File.readAsString(file);

        assertThat(content)
                .isEqualTo(testContent);
    }

    @Test
    @DisplayName("파일 내용 수정")
    void t4() {
        String file = "test/test.txt";
        String writeContent = "modified content";

        Util.File.write(file, "modified content");

        String readContent = Util.File.readAsString(file);

        assertThat(readContent)
                .isEqualTo(writeContent);
    }

    @Test
    @DisplayName("파일 삭제")
    void t5() {
       String file = "test/test3.txt";

       Util.File.createFile(file);
       assertThat(Files.exists((Paths.get(file))))
               .isTrue();
       Util.File.delete(file);
       assertThat(Files.exists((Paths.get(file))))
              .isFalse();
    }
}


