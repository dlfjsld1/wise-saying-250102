package app.domain.wiseSaying;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
public class Page {

    @Getter
    public List<WiseSaying> wiseSayings;
    @Getter
    public int totalItems;
    public int itemPerPage;

    public int getTotalPages() {
        return (int) Math.ceil((double) wiseSayings.size() / itemPerPage);
    }
}