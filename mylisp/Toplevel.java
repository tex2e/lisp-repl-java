package mylisp;

public class Toplevel {
    public static void main(String[] args) {
        System.out.println("Welcome to MyLisp!");
        System.out.println("Type quit and hit Enter for leaving MyLisp.");
        new Function().registSystemFunctions();
        while (true) {
            try {
                System.out.print("> ");
                T sexp = Reader.read();
                if (sexp == null) continue; // Empty line
                if (sexp == Symbol.symbolQuit) break; // EOF or "quit"
                T ret = Eval.eval(sexp);
                System.out.println(ret);
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        System.out.println("bye!");
    }
}
