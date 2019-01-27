package accessing.npriv;

/**
 * Created by mtumilowicz on 2019-01-27.
 */
class Outer {
    String s = "outer";

    class Inner {
        String getS() {
            return s;
        }
    }
}
