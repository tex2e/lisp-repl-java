package mylisp;

public class Eval {
    // Lisp stack
    private static final int maxStackSize = 65536;
    private static T[] stack = new T[maxStackSize];
    private static int stackP = 0;

    public static T eval(T form) throws Exception {
        // Symbol
        if (form instanceof Symbol) {
            T symbolValue = ((Symbol)form).value;
            if (symbolValue == null) {
                throw new Exception("Unbound Variable Error: " + (Symbol)form);
            }
            return symbolValue;
        }
        // Atom except Symbol
        if (form instanceof Null) return form;
        if (form instanceof Atom) return form;

        // List evaluation (Function evaluation)
        T car = ((Cons)form).car;
        if (car instanceof Symbol) {
            T fun = ((Symbol)car).function;
            if (fun == null) {
                throw new Exception("Undefined Function Error: " + (Symbol)car);
            }
            // Buildin function evaluation
            if (fun instanceof Function) {
                T argumentList = ((Cons)form).cdr;
                return ((Function)fun).funcall((List)argumentList);
            }
            // S-expression function evaluation
            if (fun instanceof Cons) {
                Cons cdr = (Cons)((Cons)fun).cdr;
                T lambdaList = cdr.car;
                Cons body = (Cons)cdr.cdr;
                // if no args given, evaluate body without bind.
                if (lambdaList == Null.Nil) return evalBody(body);
                // if args given, bind with evaluated args and evaluate body.
                return bindEvalBody((Cons)lambdaList, body, (Cons)((Cons)form).cdr);
            }
            throw new Exception("Not a Function: " + fun);
        } else {
            throw new Exception("Not a Symbol: " + car);
        }
    }

    private static T bindEvalBody(Cons lambda, Cons body, Cons form) throws Exception {
        // (1) Evaluate arguments
        int oldStackP = stackP;
        while (true) {
            T ret = eval(form.car);
            stack[stackP++] = ret;
            if (form.cdr == Null.Nil) break;
            form = (Cons)form.cdr;
        }
        // (2) Binding
        // Stash previous symbol value and store evaluated value to symbol.
        Cons argList = lambda;
        int sp = oldStackP;
        while (true) {
            Symbol sym = (Symbol)argList.car;
            T swap = sym.value;
            sym.value = stack[sp];
            stack[sp++] = swap;
            if (argList.cdr == Null.Nil) break;
            argList = (Cons)argList.cdr;
        }

        // Evaluate body.
        T ret = evalBody(body);

        // Retrieve to previous value from stack.
        argList = lambda;
        stackP = oldStackP;
        while (true) {
            Symbol sym = (Symbol)argList.car;
            sym.value = stack[oldStackP++];
            if (argList.cdr == Null.Nil) break;
            argList = (Cons)argList.cdr;
        }

        return ret;
    }


    private static T evalBody(Cons body) throws Exception {
        T ret;
        while (true) {
            ret = eval(body.car);
            if (body.cdr == Null.Nil) break;
            body = (Cons)body.cdr;
        }
        return ret;
    }
}
