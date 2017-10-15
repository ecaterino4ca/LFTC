package lftc.edu.util;

import lombok.Data;

@Data
public class Tuple<V1, V2> {

    private V1 val1;
    private V2 val2;

    public Tuple(V1 val1, V2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "\n(" + val1 + ", " + val2 + ')';
    }

}
