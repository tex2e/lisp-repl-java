package mylisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {
    private final static int CharBufferSize = 256;
    private static char[] charBuff = null;
    private static char ch;
    private static String line;
    private static int indexOfLine = 0;
    private static int lineLength = 0;
    private static BufferedReader br = null;
    private static boolean hasPipeStdin = false; // Reading from pipe or not
    static {
        br = new BufferedReader(new InputStreamReader(System.in));
        charBuff = new char[CharBufferSize];
        try {
            hasPipeStdin = br.ready();
        } catch (IOException e) {
            hasPipeStdin = false;
        }
    }

    public static T read() throws IOException {
        line = br.readLine();
        if (line == null) { // EOF
            System.out.print("\n");
            return Symbol.symbolQuit;
        }
        if (hasPipeStdin) System.out.println(line); // Display line
        if (line.trim().length() == 0) return null; // Skip Empty line
        prepare();
        return getSexp();
    }

    private static void prepare() {
        indexOfLine = 0;
        lineLength = line.length();
        line.getChars(0, lineLength, charBuff, 0);
        charBuff[lineLength] = '\0';
        getChar();
    }

    private static void getChar() {
        ch = charBuff[indexOfLine++];
    }

    private static void skipSpace() {
        while (Character.isWhitespace(ch)) getChar();
    }

    private static T getSexp() {
        while (true) {
            skipSpace();
            switch (ch) {
            case '(' : return makeList();
            case '\'': return makeQuote();
            case '-' : return makeMinusNumber();
            default:
                if (Character.isDigit(ch)) return makeNumber();
                return makeSymbol();
            }
        }
    }

    private static T makeNumber() {
        StringBuilder str = new StringBuilder();
        if (ch == '-') {
            str = str.append(ch);
            getChar();
        }
        for (; indexOfLine <= lineLength; getChar()) {
            if (ch == '(' || ch == ')') break;
            if (Character.isWhitespace(ch)) break;
            if (!Character.isDigit(ch)) {
                indexOfLine--;
                return makeSymbolInternal(str);
            }
            str.append(ch);
        }
        int value = new java.lang.Integer("" + str).intValue();
        return new Integer(value);
    }

    private static T makeMinusNumber() {
        char nch = charBuff[indexOfLine];
        if (Character.isDigit(nch) == false) {
            return makeSymbolInternal(new StringBuilder().append(ch));
        }
        return makeNumber();
    }

    private static T makeSymbol() {
        ch = Character.toUpperCase(ch);
        StringBuilder str = new StringBuilder().append(ch);
        return makeSymbolInternal(str);
    }

    private static T makeSymbolInternal(StringBuilder str) {
        while (indexOfLine < lineLength) {
            getChar();
            if (ch == '(' || ch == ')') break;
            if (Character.isWhitespace(ch)) break;
            ch = Character.toUpperCase(ch);
            str.append(ch);
        }
        String symStr = "" + str;

        if (symStr.equals("NIL")) return Null.Nil;
        return Symbol.symbol(symStr);
    }

    private static T makeList() {
        getChar(); // skip '('
        skipSpace();
        if (ch == ')') {
            getChar();
            return Null.Nil;
        }
        Cons top = new Cons();
        Cons list = top;
        while (true) {
            list.car = getSexp();
            skipSpace();
            if (indexOfLine > lineLength) return Null.Nil;
            if (ch == ')') break;
            if (ch == '.') {
                getChar();
                list.cdr = getSexp();
                skipSpace();
                getChar();
                return top;
            }
            list.cdr = new Cons();
            list = (Cons)list.cdr;
        }
        getChar(); // skip ')'
        return top;
    }

    private static T makeQuote() {
        Cons top = new Cons();
        Cons list = top;
        list.car = Symbol.symbol("QUOTE");
        list.cdr = new Cons();
        list = (Cons)list.cdr;
        getChar();
        list.car = getSexp();
        return top;
    }
}
