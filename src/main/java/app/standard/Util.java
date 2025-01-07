package app.standard;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class Util {

    public static class File {

        public static void test() {
            System.out.println("파일 유틸 테스트");
        }

        public static void createFile(String pathValue) {
            write(pathValue, "");
        }

        public static String readAsString(String file) {
            Path filePath = Paths.get(file);
            try {
                return Files.readString(filePath);
            } catch(IOException e) {
                System.out.println("파일 읽기 실패");
                e.printStackTrace();
            }

            return "";
        }

        public static void write(String file, String content) {
            Path filePath = Paths.get(file);

            //폴더 없으면 폴더 생성
            if(filePath.getParent() != null) {
                createDir(filePath.getParent().toString());
            }

            try {
                Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch(IOException e) {
                System.out.println("파일 쓰기 실패");
                e.printStackTrace();
            }
        }

        public static void delete(String file) {

            Path filePath = Paths.get(file);

            if(!Files.exists(filePath)) return;

            try {
                Files.delete(filePath);
            } catch (IOException e) {
                System.out.println("파일 삭제 실패");
                e.printStackTrace();
            }
        }

        public static void createDir(String dirPath) {
            try {
                Files.createDirectories(Paths.get(dirPath));
            } catch(IOException e) {
                System.out.println("디렉토리 생성 실패");
                e.printStackTrace();
            }
        }

        public static void deleteForce(String path) {
            Path folderPath = Path.of(path);

            if(!Files.exists(folderPath)) return;

            try {
                Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // 파일 삭제
                        Files.delete(file);
                        System.out.println("파일 삭제: " + file.toString());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        // 디렉토리 삭제 (디렉토리가 비어있을 때만 삭제 가능)
                        Files.delete(dir);
                        System.out.println("디렉토리 삭제: " + dir.toString());
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch(IOException e) {
                System.out.println("파일 삭제 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        }
    }

    public static class Json {

        public static String mapToJson(Map<String, Object> map) {

            String tmp = "";
            for(String key : map.keySet()) {
                String value = (String)map.get(key);
                tmp = "{\n" + "    \"%s\" : " + "\"%s\"" + "\n}";
                tmp = tmp.formatted(key, value);
            }

            return tmp;
        }
    }

}
