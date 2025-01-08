package app.domain.wiseSaying.repository;

import app.domain.wiseSaying.WiseSaying;
import app.standard.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WiseSayingFileRepository implements WiseSayingRepository {

    private static final String DB_PATH = "db/test/wiseSaying/";
    private int lastId;
    private final List<WiseSaying> wiseSayingList;

    public WiseSayingFileRepository() {
        wiseSayingList = new ArrayList<>();
        System.out.println("파일 DB 사용");
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        // 파일 저장
        wiseSaying.setId(++lastId);
        Util.Json.writeAsMap(getFilePath(wiseSaying.getId()), wiseSaying.toMap());
        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        //명언들은 파일로 파편화 되어 있다.
        //파일들을 모두 가져와야 한다.
        //하나씩 읽어서 list로 반환한다.

        //Path -> String -> Map -> WiseSaying
        return Util.File.getPaths(DB_PATH).stream()
                .map(Path::toString)
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

    private String getFilePath(int id) {
        return DB_PATH + id + ".json";
    }
}