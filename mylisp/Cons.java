package mylisp;

public class Cons extends List {
    public T car;
    public T cdr;

    public Cons() {
        this(Null.Nil, Null.Nil);
    }
    public Cons(T car, T cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        Cons list = this;
        str.append("(");
        while (true) {
            str.append(list.car.toString());
            if (list.cdr == Null.Nil) {
                str.append(")");
                break;
            } else if (list.cdr instanceof Atom) {
                str.append(" . ");
                str.append(list.cdr.toString());
            } else {
                str.append(" ");
                list = (Cons)(list.cdr);
            }
        }
        return str.toString();
    }
}
