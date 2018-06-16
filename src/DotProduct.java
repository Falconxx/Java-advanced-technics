class DotProduct {
    static {
        System.loadLibrary("libcalc"); // Load native library at runtime
        // hello.dll (Windows) or libhello.so (Unixes)
    }
    private Double[] a;
    private double[] b = {3.0, 7.0};
    private Double c;

    public Double[] getA() { return a; }
    public double[] getB() { return b; }
    public double getC() { return c; }

    public void setA(Double[] a){}
    public void setB(Double[] b){}
    public void setC(double c){}

    public native void hello();
    public native Double average(Integer a, Integer b);

    /*
    zakładamy, że po stronie kodu natywnego wyliczony zostanie iloczyn skalarny dwóch wektorów
     */
    public native double multi01(double[] a, double[] b);

    /*
    zakładamy, że drugi atrybut będzie pobrany z obiektu przekazanego do metody natywnej
     */
    public native double multi02(double[] a);

    /*
    zakładamy, że po stronie natywnej utworzone zostanie okienko na atrybuty,
    a po ich wczytaniu i przepisaniu do a,b obliczony zostanie wynik.
    Wynik powinna wyliczać metoda Javy multi04
    (korzystająca z parametrów a,b i wpisująca wynik do c).
     */
    public native void multi03();

}