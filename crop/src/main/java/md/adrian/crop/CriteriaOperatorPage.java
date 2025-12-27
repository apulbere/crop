package md.adrian.crop;

import md.adrian.crop.annotation.Experimental;

/**
 * Contains field that limits the query fetched result.
 */
@Experimental
public class CriteriaOperatorPage {
    private Integer size;
    private Integer offset;

    public Integer getSize() {
        return size;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Page{" +
                "size=" + size +
                ", offset=" + offset +
                '}';
    }
}
