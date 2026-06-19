package top.blogapi.common.response;

import java.util.List;

public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        PagedResponse<T> r = new PagedResponse<>();
        r.content = content;
        r.page = page;
        r.size = size;
        r.totalElements = totalElements;
        r.totalPages = (int) Math.ceil((double) totalElements / size);
        r.first = page == 0;
        r.last = page >= r.totalPages - 1;
        return r;
    }

    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isFirst() { return first; }
    public boolean isLast() { return last; }
}
