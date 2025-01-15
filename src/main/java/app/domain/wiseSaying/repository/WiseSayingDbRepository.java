package app.domain.wiseSaying.repository;

import app.domain.wiseSaying.WiseSaying;
import app.standard.simpleDb.SimpleDb;
import app.standard.simpleDb.Sql;

import java.util.List;
import java.util.Optional;

public class WiseSayingDbRepository {

    private final SimpleDb simpleDb;

    public WiseSayingDbRepository() {
        this.simpleDb = new SimpleDb("localhost", "root", "lldj123414", "wiseSaying__test");
    }

    public void createWiseSayingTable() {
        simpleDb.run("DROP TABLE IF EXISTS wise_saying");
        simpleDb.run("""
                CREATE TABLE wise_saying (
                    id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    content VARCHAR(100) NOT NULL,
                    author VARCHAR(100) NOT NULL
                )
                """);
    }

    public void truncateWiseSayingTable() {
        simpleDb.run("TRUNCATE wise_saying");
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        Sql sql = simpleDb.genSql();
        sql.append("INSERT INTO wise_saying")
                .append("SET content = ?,", wiseSaying.getContent())
                .append("author = ?", wiseSaying.getAuthor());

        //insert 안에서 DB를 반영해 최신 id를 줌
        long generatedId = sql.insert();
        wiseSaying.setId((int)generatedId);

        return wiseSaying;
    }

    public Optional<WiseSaying> findById(int id) {
        Sql sql = simpleDb.genSql();
        sql.append("SELECT *")
                .append("FROM wise_saying")
                .append("WHERE id = ?", id);

        //selectRow는 제네릭을 썼기 때문에 타입 정보를 넣어주면 그 클래스로 만들어 줌
        WiseSaying wiseSaying = sql.selectRow(WiseSaying.class);
        if(wiseSaying == null) {
            return Optional.empty();
        }
        return Optional.of(wiseSaying);
    }

    boolean deleteById(int id) {
        int rst = simpleDb.genSql().append("DELETE FROM wise_saying")
                .append("WHERE id = ?", id)
                .delete();
        return rst > 0;
    }


    public List<WiseSaying> findAll() {
        return simpleDb.genSql().append("SELECT *")
                .append("FROM wise_saying")
                .selectRows(WiseSaying.class);
    }
}
