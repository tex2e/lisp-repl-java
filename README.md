
# lisp-repl-java

Javaで実装した簡易版LispのREPL。

## Usage

REPLで実行

```
java -jar mylisp.jar
```

ファイルから読み込む

```
java -jar mylisp.jar < sample/test1.lisp
```

## Example

```
$ java -jar mylisp.jar
Welcome to MyLisp!
Type quit and hit Enter for leaving MyLisp.
> (+ 1 2)
3
> (setq hoge '(lion tiger leopard))
(LION TIGER LEOPARD)
> (cons (car hoge) (cdr hoge))
(LION TIGER LEOPARD)
> (defun fact (n) (if (<= n 1) 1 (* n (fact (- n 1)))))
FACT
> (= (fact 4) (* 2 (* 3 4)))
T
> (type-of '+)
SYMBOL
> (type-of '(foo bar))
CONS
> (symbol-function 'fact)
(LAMBDA (N) (IF (<= N 1) 1 (* N (FACT (- N 1)))))
>
> quit
bye!
```
