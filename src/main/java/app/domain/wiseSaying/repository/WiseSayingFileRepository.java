package app.domain.wiseSaying.repository;

import app.domain.wiseSaying.WiseSaying;
import app.global.AppConfig;
import app.standard.Util;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WiseSayingFileRepository implements WiseSayingRepository {

    private static final String DB_PATH = AppConfig.getDbPath() + "/wiseSaying";
    private static final String ID_File_Path = DB_PATH + "/lastId.txt";

    public WiseSayingFileRepository() {
        System.out.println("파일 DB 사용");
        init();
    }

    public void init() {
        if(!Util.File.exists(ID_File_Path)) {
            Util.File.createFile(ID_File_Path);
        }
        if(!Util.File.exists(DB_PATH)) {
            Util.File.createFile(DB_PATH);
        }
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        // 파일 저장

        boolean isNew = wiseSaying.isNew();
        //수정과 등록을 이 한 메서드로 처리하기에 아래 조건문이 없으면 문제가 생긴다
        if(isNew) {
            wiseSaying.setId(getLastId() + 1);
        }

        Util.Json.writeAsMap(getFilePath(wiseSaying.getId()), wiseSaying.toMap());

        //최신 아이디를 갱신
        if(isNew) {
            setLastId(wiseSaying.getId());
        }

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        //명언들은 파일로 파편화 되어 있다.
        //파일들을 모두 가져와야 한다.
        //하나씩 읽어서 list로 반환한다.

        //Path -> String -> Map -> WiseSaying
        return Util.File.getPaths(DB_PATH).stream()
                .map(Path::toString)
                .filter(path -> path.endsWith(".json"))
                .map(Util.Json::readAsMap)
                .map(WiseSaying::fromMap)
                .toList();
    }

    public boolean deleteById(int id) {
        return Util.File.delete(getFilePath(id));
    }

    public Optional<WiseSaying> findById(int id) {
        String path = getFilePath(id);
        Map<String, Object> map = Util.Json.readAsMap(path);

        if(map.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(WiseSaying.fromMap(map));

    }

    static String getFilePath(int id) {
        return DB_PATH + "/" + id + ".json";
    }

    public int getLastId() {
        String idStr = Util.File.readAsString(ID_File_Path);
        if (idStr.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setLastId(int id) {
        Util.File.write(ID_File_Path, id);
    }
}