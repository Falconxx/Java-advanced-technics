import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import org.mariadb.jdbc.*;

import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.List;

public class DataProcessing {

    public static String clasify(int genotype, int range)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bacteria?user=buczaq&password=123456");
            System.out.println("connected");

            Statement statement = connection.createStatement();
            ResultSet result_set1 = statement.executeQuery("SELECT * FROM flagella");
            ResultSet result_set2 = statement.executeQuery("SELECT * FROM toughness");

            int TA = 0;
            int TB = 0;
            int TC = 0;
            int TD = 0;
            int F1 = 0;
            int F2 = 0;
            int F3 = 0;
            int F4 = 0;

            int genes[] = new int[6];

            int genotypeBackup = genotype;

            for (int i = 5; i >= 0; i--) {
                genes[i] = genotype%10;
                genotype /= 10;
            }

            int alpha = genes[0] * 10 + genes[5];
            int beta = genes[1] * 10 + genes[4];
            int gamma = genes[2] * 10 + genes[3];

            System.out.println("examined gene is: ALPHA=" + alpha + " BETA=" + beta + " GAMMA=" + gamma);
            while (result_set1.next()) {
                int a = result_set1.getInt("alpha");
                int b = result_set1.getInt("beta");
                int n = result_set1.getInt("number");
                if (Math.abs(a - alpha) <= range && Math.abs(b - beta) <= range) {
                    if (n == 1) F1++;
                    else if (n == 2) F2++;
                    else if (n == 3) F3++;
                    else if (n == 4) F4++;
                }
            }

            while (result_set2.next()) {
                int b = result_set2.getInt("beta");
                int g = result_set2.getInt("gamma");
                String t = result_set2.getString("rank");
                if (Math.abs(b - beta) <= range && Math.abs(g - gamma) <= range) {
                    if (t.equals("a")) TA++;
                    else if (t.equals("b")) TB++;
                    else if (t.equals("c")) TC++;
                    else if (t.equals("d")) TD++;
                }
            }

            System.out.println("TA:" + TA +" TB:" + TB +" TC:" + TC +" TD:" + TD +" F1:" + F1 +" F2:" + F2 + " F3:" + F3 + " F4:" + F4);

            char[] cl = new char[2];

            if (F1 >= F2 && F1 >= F3 && F1 >= F4) cl[0] = '1';
            else if (F2 >= F1 && F2 >= F3 && F2 >= F4) cl[0] = '2';
            else if (F3 >= F1 && F3 >= F2 && F3 >= F4) cl[0] = '3';
            else cl[0] = '4';

            if (TA >= TB && TA >= TC && TA >= TD) cl[1] = 'a';
            else if (TB >= TA && TB > TC && TB >= TD) cl[1] = 'b';
            else if (TC >= TA && TC > TB && TC >= TD) cl[1] = 'c';
            else cl[1] = 'd';

            String cltemp = "" + cl[0] + cl[1];

            ResultSet exists = statement.executeQuery("SELECT * FROM examined WHERE genotype = " + genotypeBackup);

            if (exists.next()) {
                statement.executeQuery("UPDATE examined SET class='" + cltemp + "' WHERE genotype = " + genotypeBackup);
            }
            else {
                String sql = "INSERT INTO examined VALUES(" + genotypeBackup + ", '" + cl[0] + cl[1] + "')";
                System.out.println(sql);
                statement.executeQuery(sql);
                sql = "INSERT INTO flagella VALUES(" + alpha + ", " + beta + ", '" + cl[0] + "')";
                statement.executeQuery(sql);
                sql = "INSERT INTO toughness VALUES(" + beta + ", " + gamma + ", '" + cl[1] + "')";
                statement.executeQuery(sql);
            }

            return "Genotyp sklasyfikowano jako " + cltemp;
        }
        catch (Exception e) {
            System.out.println("error :( " + e);
        }
        return "";
    }

    public static void clasifyMany(String genotype, int range)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bacteria?user=buczaq&password=123456");
            System.out.println("connected");

            Statement statement = connection.createStatement();
            String genotypes = genotype.replaceAll("[^0-9]", "");
            long genes = Long.parseLong(genotypes);
            int numberOfGenotypes = genotypes.length();

            long genesArray[] = new long[numberOfGenotypes * 6];

            for (int i = (numberOfGenotypes - 1); i >= 0; i--) {
                genesArray[i] = genes%10;
                genes /= 10;
            }

            String templateQueryF = "INSERT INTO flagella VALUES(?, ?, ?)";
            String templateQueryT = "INSERT INTO toughness VALUES(?, ?, ?)";
            String templateQueryE = "INSERT INTO examined VALUES(?, ?)";

            PreparedStatement psF = connection.prepareStatement(templateQueryF);
            PreparedStatement psT = connection.prepareStatement(templateQueryT);
            PreparedStatement psE = connection.prepareStatement(templateQueryE);

            for (int i = 0; i < (numberOfGenotypes - 1);) {
                ResultSet result_set1 = statement.executeQuery("SELECT * FROM flagella");
                ResultSet result_set2 = statement.executeQuery("SELECT * FROM toughness");

                int TA = 0;
                int TB = 0;
                int TC = 0;
                int TD = 0;
                int F1 = 0;
                int F2 = 0;
                int F3 = 0;
                int F4 = 0;

                long alpha = genesArray[i] * 10 + genesArray[i + 5];
                long beta = genesArray[i + 1] * 10 + genesArray[i + 4];
                long gamma = genesArray[i + 2] * 10 + genesArray[i + 3];

                System.out.println("examined gene is: ALPHA=" + alpha + " BETA=" + beta + " GAMMA=" + gamma);
                while (result_set1.next()) {
                    int a = result_set1.getInt("alpha");
                    int b = result_set1.getInt("beta");
                    int n = result_set1.getInt("number");
                    if (Math.abs(a - alpha) <= range && Math.abs(b - beta) <= range) {
                        if (n == 1) F1++;
                        else if (n == 2) F2++;
                        else if (n == 3) F3++;
                        else if (n == 4) F4++;
                    }
                }

                while (result_set2.next()) {
                    int b = result_set2.getInt("beta");
                    int g = result_set2.getInt("gamma");
                    String t = result_set2.getString("rank");
                    if (Math.abs(b - beta) <= range && Math.abs(g - gamma) <= range) {
                        if (t.equals("a")) TA++;
                        else if (t.equals("b")) TB++;
                        else if (t.equals("c")) TC++;
                        else if (t.equals("d")) TD++;
                    }
                }

                System.out.println("TA:" + TA + " TB:" + TB + " TC:" + TC + " TD:" + TD + " F1:" + F1 + " F2:" + F2 + " F3:" + F3 + " F4:" + F4);

                char[] cl = new char[2];

                if (F1 >= F2 && F1 >= F3 && F1 >= F4) cl[0] = '1';
                else if (F2 >= F1 && F2 >= F3 && F2 >= F4) cl[0] = '2';
                else if (F3 >= F1 && F3 >= F2 && F3 >= F4) cl[0] = '3';
                else cl[0] = '4';

                if (TA >= TB && TA >= TC && TA >= TD) cl[1] = 'a';
                else if (TB >= TA && TB > TC && TB >= TD) cl[1] = 'b';
                else if (TC >= TA && TC > TB && TC >= TD) cl[1] = 'c';
                else cl[1] = 'd';

                String cltemp = "" + cl[0] + cl[1];

                int gn = (int)(genesArray[i]*100000 + genesArray[i+1]*10000 +genesArray[i+2]*1000 +genesArray[i+3]*100 +genesArray[i+4]*10 +genesArray[i]);

                psF.setInt(1, (int)alpha);
                psF.setInt(2, (int)beta);
                psF.setInt(3, cl[0] - 48);
                psF.addBatch();

                psT.setInt(1, (int)beta);
                psT.setInt(2, (int)gamma);
                psT.setString(3, new String("" + cl[1]));
                psT.addBatch();

                psE.setInt(1, gn);
                psE.setString(2, cltemp);
                psE.addBatch();

                i += 6;
            }

            psF.executeBatch();
            psT.executeBatch();
            psE.executeBatch();

        }
        catch (Exception e) {
            System.out.println("error :( " + e);
            e.printStackTrace();
        }
    }



    public String strongest()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bacteria?user=buczaq&password=123456");
            System.out.println("connected");

            Statement statement = connection.createStatement();
            ResultSet result_set1 = statement.executeQuery("SELECT * FROM examined WHERE class LIKE '%d' ");

            String r = "";

            while (result_set1.next()) {
                r += result_set1.getInt("genotype");
                r += " | ";
            }

            return r;
        }
        catch (Exception e) {
            System.out.println("error :( " + e);
            e.printStackTrace();
        }
        return "";
    }

    public String weakest()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bacteria?user=buczaq&password=123456");
            System.out.println("connected");

            Statement statement = connection.createStatement();
            ResultSet result_set1 = statement.executeQuery("SELECT * FROM examined WHERE class LIKE '%a' ");

            String r = "";

            while (result_set1.next()) {
                r += result_set1.getInt("genotype");
                r += " | ";
            }

            return r;
        }
        catch (Exception e) {
            System.out.println("error :( " + e);
        }
        return "";
    }

    public static void exportToXml()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bacteria?user=buczaq&password=123456");
            System.out.println("connected");

            Statement statement = connection.createStatement();
            ResultSet result_set1 = statement.executeQuery("SELECT * FROM examined");

            while (result_set1.next()) {
                int g = result_set1.getInt("genotype");
                String c = result_set1.getString("class");
                GenotypeItem gi = new GenotypeItem();
                gi.setCl(c);
                gi.setGenotype(g);

                try {
                    File f = new File("/home/buczak/out.xml");

                    JAXBContext jaxbContext = JAXBContext.newInstance(GenotypeItems.class);
                    // Read XML
                    BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
                    if (br.readLine() != null) {
                        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                        GenotypeItems genotypeItems = (GenotypeItems) unmarshaller.unmarshal(f);

                        List<GenotypeItem> genotypeItemsList = genotypeItems.getGenotypeItems();
                        genotypeItemsList.add(gi);

                        genotypeItems.setGenotypeItems(genotypeItemsList);

                        Marshaller marshaller = jaxbContext.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        marshaller.marshal(genotypeItems, f);
                    }
                    else {
                        Marshaller marshaller = jaxbContext.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        marshaller.marshal(gi, f);
                    }

                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ABGGBA
    public static void main(String [ ] args)
    {
        clasify(213788, 25);
        clasify(123456, 25);
        clasify(848976, 25);
        clasify(945687, 25);
        clasify(389679, 25);
        clasify(999999, 30);

        exportToXml();
    }

}
