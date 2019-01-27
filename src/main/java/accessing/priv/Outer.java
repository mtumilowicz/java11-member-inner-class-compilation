package accessing.priv;

/**
 * Created by mtumilowicz on 2019-01-27.
 */
class Outer {
    private String s = "outer";

    class Inner {
        String getS() {
            return s;
        }
    }
}
