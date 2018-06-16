public class app {

    public static void main(String [] args) {
        DotProduct dp = new DotProduct();

        double[] arra = { 2.0, 1.0 };
        double[] arrb = { 2.0, 1.0 };

        double i = dp.multi01(arra, arrb);
        double f = dp.multi02(arra);

        //double j = (double)i;

        System.out.println(i);
        System.out.println(f);
        //System.out.println(dp.average(3,2));
        dp.hello();

        Double j = 1.0;
        System.out.println(j);

        dp.multi03();
    }

}
