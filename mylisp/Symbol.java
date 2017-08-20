package mylisp;

import java.util.HashMap;

public class Symbol extends Atom {
    public String name;
    public T value;
    public T function;
    private static HashMap<String, Symbol> symbolTable = new HashMap<>();
    public static Symbol symbolT = Symbol.symbol("T");
    public static Symbol symbolQuit = Symbol.symbol("QUIT");
    static {
        symbolT.value = symbolT;
    }

    private Symbol(String name) {
        this.name = name;
    }

    public static Symbol symbol(String name) {
        if (symbolTable.get(name) == null) {
            Symbol symbol = new Symbol(name);
            symbolTable.put(name, symbol);
        }
        return symbolTable.get(name);
    }

    public String toString() {
        return name;
    }
}
