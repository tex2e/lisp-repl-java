package mylisp;

public class Null extends List {
    public static Null Nil = new Null();

    public String toString() {
        return "NIL";
    }
}
