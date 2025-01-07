package app.standard;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.stream.Collectors;

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

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n");

            //스트림 이용해서 선언형 처리

            String str = map.keySet().stream()
                    .map(k -> map.get(k) instanceof String
                            ? "    \"%s\" : \"%s\"".formatted(k, map.get(k))
                            : "    \"%s\" : %s".formatted(k, map.get(k))
                    ).collect(Collectors.joining(",\n"));
            jsonBuilder.append(str);

//            int i = 0;
//            for(String key : map.keySet()) {
//
//                //숫자 타입, 문자 타입
//
//
//
//                Object obj = map.get(key);
//
//                //오브젝트의 실제 타입 밝힌다
//                if(obj instanceof String) {
//                    String value = (String)map.get(key);
//                    String tmp = "    \"%s\" : " + "\"%s\"";
//                    jsonBuilder.append(tmp.formatted(key, value));
//                } else if(obj instanceof Integer) {
//                    int value = (int)map.get(key);
//                    String tmp = "    \"%s\" : " + "%d";
//                    jsonBuilder.append(tmp.formatted(key, value));
//                }
//
//                if(i == map.size() - 1) {
//                    break;
//                }
//
//                jsonBuilder.append(",\n");
//                i++;
//            }
            jsonBuilder.append("\n}");
            return jsonBuilder.toString();
        }

        public static void writeAsMap(String filePath, Map<String, Object> map) {
            String jsonStr = mapToJson(map);
            File.write(filePath, jsonStr);
        }
    }

}
