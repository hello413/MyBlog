package itpainter.model;

import lombok.Data;

@Data
public class PageSearch {
    String search = "";
    int currentPage = 1;
    int pageSize = 6;
    String type = "";
    String tag = "";

    int start;
}
