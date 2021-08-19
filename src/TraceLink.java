import java.lang.reflect.Array;

public class TraceLink<E> {
    private final int length;
    private final TraceLink<E> prev;
    private final E elem;
    
    public TraceLink(E elem) {
        this.elem = elem;
        prev = null;
        length = 1;
    }
    
    public TraceLink(TraceLink<E> prev, E elem) {
        this.elem = elem;
        this.prev = prev;
        length = prev != null ? prev.length + 1 : 1;
    }
    
    public E[] toArray() {
        @SuppressWarnings("unchecked")
        E[] ret = (E[]) Array.newInstance(elem.getClass(), length);
        int i = length - 1;
        TraceLink<E> tl = this;
        do {
            ret[i--] = tl.elem;
        } while ((tl = tl.prev) != null && i >= 0);
        return ret;
    }
}
