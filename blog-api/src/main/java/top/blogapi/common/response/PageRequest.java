package top.blogapi.common.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 20;

    private String sort;
    private String direction = "desc";

    public PageRequest() {}

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    public static PageRequest of(int page, int size, String sort, String direction) {
        PageRequest r = new PageRequest(page, size);
        r.sort = sort;
        r.direction = direction;
        return r;
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
