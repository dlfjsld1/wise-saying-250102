package app.standard;

import app.App;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TestBot {
    //static으로 복제를 방지하는 이유는 개성을 표현하기 위함이다.
    //그런데 개성이라 함은 데이터를 뜻한다.
    //만약 메서드에 데이터가 없다면 개성을 표현할 필요가 없다.
    //따라서 static을 붙여도 된다.
    public static String run(String input) {
        Scanner sc = new Scanner(input + "종료\n");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        App app = new App(sc);
        app.run();

        return out.toString();
    }

    public static void makeSample(int cnt) {
        App app = new App(null);
        app.makeSampleData(cnt);
    }
}
