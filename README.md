# java11-member-inner-class-compilation
We will discuss how member inner classes are compiled.

_Reference_: https://www.amazon.com/Java-Language-Features-Modules-Expressions/dp/1484233476

# preview
A **member inner class** is defined inside a class the same 
way a member field or a member method. It could be private,
package-level, protected, public.

Naming convention: `<outer-class-name>$<member-inner-class-name>`

# decompiler configuration
1. Inner classes are hidden by default in Intellij explorer.
1. Configuration of javap decompiler is child's play:
    1. ctrl + alt + s (Settings)
    1. external tools
    1. `+ `(plus)
        * Program: `$JDKPath$\bin\javap.exe`
        * Arguments: `-c $FileClass$`
            * `-c` Prints out disassembled code
        * Working directory: `$OutputPath$`
1. Using javap from context menu:
    * right click on the class -> External Tools -> javap
1. Using javap from terminal:
    "java_home\bin\javap.exe" -c path_to_compiled\Outer$Inner.class

# project description
1. The easiest example, package: `easy`
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
            
            Outer$Inner(Outer outer) {
                this$0 = outer;
                super();
            }
        }
        ```
        * final field for `Outer` instance was added
        * note that `super()` is **after** setting the field
        * a one-param constructor that accepts `Outer` is inserted during compilation
    * `Outer.class`
        ```
        class Outer {
            Outer() {
            }
        }
        ```
        * the coupling between `Outer` and `Inner` is removed 
        in the compilation
1. Inner class has constructor, package: `constructor`
    ```
    class Outer {
        class Inner {
            Inner(String s) {
            }
        }
    }
    ```
    is compiled to two classes:
    * `Outer$Inner`
        ```
        class Outer$Inner {
        
            final Outer this$0;
            
            Outer$Inner(Outer outer, String s) {
                this$0 = outer;
                super();
            }
        }
        ```
        * additional param (`Outer`) was added to the existing constructor
    * `Outer.class` same as `(1.)`
1. Accessing `Outer` fields, package: `accessing`
    * accessing private fields, package: `accessing.priv`
        ```
        class Outer {
        
            private String s = "outer";
        
            class Inner {
                String getS() {
                    return s;
                }
            }
        }
        ```
        is compiled to two classes:
        * `Outer.class`
            ```
            class Outer {
                private String s = null;
                
                Outer() {
                    dummy = "outer";
                }
                
                static String access$000(Outer outer) {
                    return outer.s;
                }
            }
            ```
            * static method for extracting private field (flagged by
            compiler - we don't have direct access)
        * `Outer$Inner`
            ```
            class Outer$Inner {
            
                final Outer this$0;
                
                Outer$Inner(Outer outer) {
                    this$0 = outer;
                    super();
                }
                
                String getS() {
                    return Outer.access$000(this$0);
                }
            }
            ```
    * accessing non-private fields, package: `accessing.npriv`
        ```
        class Outer {
            String s = "outer";
        
            class Inner {
                String getS() {
                    return s;
                }
            }
        }
        ```
        is compiled to two classes:
        * `Outer$Inner`
            ```
            class Outer$Inner {
            
                final Outer this$0;
                
                Outer$Inner(Outer outer) {
                    this$0 = outer;
                    super();
                }
                
                String getS() {
                    return this$0.s;
                }
            }
            ```
        * `Outer.class`
            ```
            class Outer {
            
                private String s = null;
                
                Outer() {
                    dummy = "outer";
                }
                
                static String access$000(Outer outer) {
                    return outer.s;
                    }
            }
            ```