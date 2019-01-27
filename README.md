# java11-member-inner-class-compilation
We will discuss how member inner classes are compiled.

_Reference_: https://www.amazon.com/Java-Language-Features-Modules-Expressions/dp/1484233476

# preview
A **member inner class** is defined inside a class the same 
way a member field or a member method. It could be private,
package-level, protected, public.

Naming convention: <outer-class-name>$<member-inner-class-name>

# decompiler configuration
1. Inner classes are hidden by default in Intellij explorer.
1. Configuration of javap decompiler is child's play:
    1. ctrl + alt + s (Settings)
    1. external tools
    1. + (plus)
        * Program: $JDKPath$\bin\javap.exe
        * Arguments: -c $FileClass$
        * Working directory: $OutputPath$
1. Using javap from context menu:
    * right click on the class -> External Tools -> javap
1. Using javap from terminal:
    "java_home\bin\javap.exe" -c path_to_compiled\Outer$Inner.class

# project description
1. Only classes (the easiest example)
    ```
    class Outer {
        class Inner {
        }
    }
    ```
    is compiled to two classes:
    * `Outer$Inner.class`
        ```
        class Outer$Inner {
            final Outer this$0;
            public Outer$Inner(Outer outer) {
                this$0 = outer;
                super();
            }
        }
        ```
        * note that `super()` is **after** setting the field
        * the default constructor inserted by compiler is
        a one-param constructor that accepts `Outer` param
    * `Outer.class`
        ```
        class Outer {
            public Outer() {
            }
        }
        ```
        * the coupling between `Outer` and `Inner` is removed 
        in the compilation