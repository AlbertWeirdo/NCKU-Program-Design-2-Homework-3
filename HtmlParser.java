import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class HtmlParser {
    public static void main(String[] args) {
        Task0 task0 = new Task0();
        int numOfComp = task0.returnNumOfComp();

        String[] companyName = new String[numOfComp];

        if (args[0].equals("0")) {
            String[] stockPrice = new String[numOfComp];

            task0.getCompNames(companyName);
            task0.getStockPrice(stockPrice);
            task0.getDay();
            task0.task0();

        } else {
            String[][] stockPrice = new String[30][numOfComp];
            task0.getCompNames(companyName);
            Task task = new Task(args, numOfComp, companyName);

            // task.getNumOfComp(numOfComp);
            // task.getCompName(companyName);
            task.getStockPrice(stockPrice);

            switch (args[1]) {
                case "0":
                    task.task0();
                    break;
                case "1":
                    task.task1();
                    break;
                case "2":
                    task.task2();
                    break;
                case "3":
                    task.task3();
                    break;
                case "4":
                    task.task4();
                    break;
            }
        }

    }
}

class Task0 {

    String fileName = "data.csv";
    String html = "https://pd2-hw3.netdb.csie.ncku.edu.tw/";
    private int numOfComp;
    private Integer day = 0;
    private String[] companyName = new String[numOfComp];
    private String[] stockPrice = new String[numOfComp];

    public int returnNumOfComp() {
        try {
            Document doc = Jsoup.connect(html).get();
            Elements companyNames = doc.select("tbody").select("tr").select("th");
            numOfComp = companyNames.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numOfComp;
    }

    public String[] returnCompNames() {
        return companyName;
    }

    public void getCompNames(String[] compName) {

        try {
            Document doc = Jsoup.connect(html).get();
            Elements companyNames = doc.select("tbody").select("tr").select("th");

            int i = 0;
            for (Element companyName : companyNames) {
                if (i < numOfComp) {
                    compName[i] = companyName.text();
                    i++;
                }
            }
            this.companyName = compName;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStockPrice(String[] stock) {
        try {
            Document doc = Jsoup.connect(html).get();
            Elements stockPrices = doc.select("tbody").select("tr").select("td");
            int i = 0;
            for (Element stockPrice : stockPrices) {
                if (i < numOfComp) {
                    stock[i] = stockPrice.text();
                    i++;
                }
            }
            this.stockPrice = stock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDay() {
        try {
            Document doc = Jsoup.connect(html).get();
            day = Integer.parseInt(doc.title().substring(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(day);
    }

    public void task0() {
        File file = new File(fileName);

        // create a file with 31 lines and also write in all the companies
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                file.createNewFile();
                for (int i = 0; i < 31; i++) {
                    if (i == 0) {
                        for (int j = 0; j < numOfComp; j++) {
                            if (j != numOfComp - 1) {
                                bw.write(companyName[j] + ",");
                            } else {
                                bw.write(companyName[j] + "\n");
                            }
                        }
                    } else if (i == 31) {
                        bw.write("no data yet.");
                    } else {
                        bw.write("no data yet.\n");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((file.exists()) && (file.length() == 0)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                for (int i = 0; i < 31; i++) {
                    if (i == 0) {
                        for (int j = 0; j < numOfComp; j++) {
                            if (j != numOfComp - 1) {
                                bw.write(companyName[j] + ",");
                            } else {
                                bw.write(companyName[j] + "\n");
                            }
                        }
                    } else if (i == 31) {
                        bw.write("no data yet.");
                    } else {
                        bw.write("no data yet.\n");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // store current day's stock prices into a string
        String dailyPrice = "";
        for (int i = 0; i < numOfComp; i++) {
            if (i != numOfComp - 1) {
                dailyPrice += stockPrice[i];
                dailyPrice += ",";
            } else {
                dailyPrice += stockPrice[i];
            }
        }
        // System.out.println(dailyPrice);

        // overwrite the data to specific line with the above data just stored
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                int currentLine = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    if (this.day == currentLine) {
                        lines.add(dailyPrice);
                    } else {
                        lines.add(line);
                    }
                    currentLine++;
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                int lineCount = 0;
                for (String line : lines) {
                    if (lineCount == 31) {
                        bw.write(line);
                    } else {
                        bw.write(line);
                        bw.newLine();
                    }
                    lineCount++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void task0() {
    // File file = new File(fileName);
    // long lineCount = 0;
    // if (file.exists()) {
    // try {
    // Path fileP = Paths.get(fileName);
    // lineCount = Files.lines(fileP).count();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    // try (
    // BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
    // if (file.length() == 0) {
    // for (int i = 0; i < numOfComp; i++) {
    // if (i != numOfComp - 1) {
    // bw.write(companyName[i] + ",");
    // } else {
    // bw.write(companyName[i]);
    // // bw.newLine();
    // }
    // }
    // }

    // // stop appending datas after day30
    // if (lineCount < 31) {
    // for (int i = 0; i < numOfComp; i++) {
    // if (i == 0) {
    // bw.newLine();
    // bw.write(stockPrice[i] + ",");
    // } else if (i != numOfComp - 1) {
    // bw.write(stockPrice[i] + ",");
    // } else {
    // bw.write(stockPrice[i]);
    // // bw.newLine();
    // }
    // }
    // }

    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // }
}

class Task {
    String fileName = "output.csv";
    private int task;
    private String stock = new String();
    private int startDay;
    private int endDay;
    private int numOfComp;
    private String[] companyName = new String[numOfComp];
    private String[][] stockPrice = new String[30][numOfComp];

    public Task(String[] args, int numOfComp, String[] companyName) {
        task = Integer.parseInt(args[1]);
        if (task != 0) {
            stock = args[2];
            startDay = Integer.parseInt(args[3]);
            endDay = Integer.parseInt(args[4]);
        }
        this.numOfComp = numOfComp;
        this.companyName = companyName;
    }

    public void getStockPrice(String[][] stockPrice) {
        int dayCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;

            while ((line = br.readLine()) != null) {
                Scanner scan = new Scanner(line);
                scan.useDelimiter(",");

                if (dayCount == 0) {
                    dayCount++;
                    continue;
                } else if (dayCount == 31) {
                    scan.close();
                    break;
                } else {
                    for (int i = 0; i < numOfComp; i++) {
                        stockPrice[dayCount - 1][i] = scan.next();
                    }
                    dayCount++;
                }

            }
            this.stockPrice = stockPrice;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task0() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {

            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {

                Scanner scan = new Scanner(line);
                bw.write(line + "\n");
                scan.useDelimiter(",");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task1() {
        int targetLoc = 0;
        while (!companyName[targetLoc].equals(stock)) {
            targetLoc++;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(stock + "," + startDay + "," + endDay + "\n");
            for (int i = 0; i < (endDay - startDay - 5 + 2); i++) {
                BigDecimal sum = new BigDecimal("0");
                BigDecimal average = new BigDecimal("5");
                for (int j = 0; j < 5; j++) {
                    BigDecimal add = new BigDecimal(stockPrice[startDay - 1 + j + i][targetLoc]);
                    sum = sum.add(add);
                }

                // avoid Arithmetic Exception
                sum = sum.divide(average, 2, RoundingMode.HALF_UP);
                // sum = sum.setScale(2, RoundingMode.HALF_UP);

                String checkEndWithZero = sum.toString();

                // if (checkEndWithZero.substring(checkEndWithZero.length() - 1,
                // checkEndWithZero.length()).equals("0")) {
                // sum = sum.setScale(1, RoundingMode.DOWN);
                // }

                for (int j = 0; j <= 2; j++) {
                    int length = checkEndWithZero.length();
                    if (checkEndWithZero.substring(length - 1, length).equals("0")) {
                        checkEndWithZero = checkEndWithZero.substring(0, length - 1);
                    } else if (checkEndWithZero.substring(length - 1, length).equals(".")) {
                        checkEndWithZero = checkEndWithZero.substring(0, length - 1);
                    }

                }
                if (i != (endDay - startDay - 5 + 2 - 1)) {
                    bw.write(checkEndWithZero + ",");
                } else {
                    bw.write(checkEndWithZero + "\n");
                }

                // if (i != (endDay - startDay - 5 + 2 - 1)) {
                // bw.write(sum + ",");
                // } else {
                // bw.write(sum + "\n");
                // }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void task2() {
        int targetLoc = 0;

        while (!companyName[targetLoc].equals(stock)) {
            targetLoc++;
        }
        // calculate the average stock price

        BigDecimal averagePrice = new BigDecimal("0");
        String baseStr = Integer.toString((endDay - startDay + 1));
        BigDecimal base = new BigDecimal(baseStr);
        for (int i = startDay - 1; i < endDay; i++) {
            BigDecimal add = new BigDecimal(stockPrice[i][targetLoc]);
            averagePrice = averagePrice.add(add);
        }
        averagePrice = averagePrice.divide(base, 8, RoundingMode.HALF_UP);
        // System.out.println(averagePrice);

        BigDecimal sumDiffSqr = new BigDecimal("0");
        for (int i = startDay - 1; i < endDay; i++) {
            BigDecimal price = new BigDecimal(stockPrice[i][targetLoc]);
            price = averagePrice.subtract(price);
            price = price.pow(2);
            sumDiffSqr = sumDiffSqr.add(price);
        }
        BigDecimal divider = new BigDecimal(Integer.toString(endDay - startDay));
        sumDiffSqr = sumDiffSqr.divide(divider, 8, RoundingMode.HALF_UP);

        MathContext mc = new MathContext(8);
        sumDiffSqr = sumDiffSqr.sqrt(mc);
        System.out.println("to the eighth precision: " + sumDiffSqr);

        sumDiffSqr = sumDiffSqr.setScale(2, RoundingMode.HALF_UP);
        String checkEndWithZero = sumDiffSqr.toString();

        for (int i = 0; i <= 2; i++) {
            int length = checkEndWithZero.length();
            if (checkEndWithZero.substring(length - 1, length).equals("0")) {
                checkEndWithZero = checkEndWithZero.substring(0, length - 1);
            } else if (checkEndWithZero.substring(length - 1, length).equals(".")) {
                checkEndWithZero = checkEndWithZero.substring(0, length - 1);
            }

        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(stock + "," + startDay + "," + endDay + "\n");
            bw.write(checkEndWithZero + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void task3() {
        BigDecimal[] standDev = new BigDecimal[numOfComp];
        Arrays.fill(standDev, BigDecimal.ZERO);
        BigDecimal divider = new BigDecimal(Integer.toString(endDay - startDay));
        BigDecimal base = new BigDecimal(Integer.toString(endDay - startDay + 1));

        // calculate each stock's standard deviaton in the given interval
        for (int i = 0; i < numOfComp; i++) {
            BigDecimal avg = new BigDecimal("0");

            for (int j = startDay - 1; j < endDay; j++) {
                BigDecimal add = new BigDecimal(stockPrice[j][i]);
                avg = avg.add(add);
            }
            avg = avg.divide(base, 8, RoundingMode.HALF_UP);

            for (int j = startDay - 1; j < endDay; j++) {
                BigDecimal price = new BigDecimal(stockPrice[j][i]);
                price = price.subtract(avg);
                price = price.pow(2);
                standDev[i] = standDev[i].add(price);
            }

            standDev[i] = standDev[i].divide(divider, 8, RoundingMode.HALF_UP);
            MathContext mc = new MathContext(8);
            standDev[i] = standDev[i].sqrt(mc);
        }

        // compare all the deviation
        int fisrtLoc = 0;
        int secondLoc = 0;
        int thirdLoc = 0;
        BigDecimal fBiggest = new BigDecimal("0");
        BigDecimal sBiggest = new BigDecimal("0");
        BigDecimal tBiggest = new BigDecimal("0");

        fBiggest = standDev[0];
        for (int i = 0; i < numOfComp; i++) {
            if (fBiggest != fBiggest.max(standDev[i])) {
                fisrtLoc = i;
            }
            fBiggest = fBiggest.max(standDev[i]);
        }

        for (int i = 0; i < numOfComp; i++) {
            if (i != fisrtLoc) {
                sBiggest = standDev[i];
                break;
            }
        }
        for (int i = 0; i < numOfComp; i++) {
            if (i != fisrtLoc) {
                if (sBiggest != sBiggest.max(standDev[i])) {
                    sBiggest = standDev[i];
                    secondLoc = i;
                }
            }
        }

        for (int i = 0; i < numOfComp; i++) {
            if ((i != fisrtLoc) && (i != secondLoc)) {
                tBiggest = standDev[i];
                break;
            }
        }
        for (int i = 0; i < numOfComp; i++) {
            if ((i != fisrtLoc) && (i != secondLoc)) {
                if (tBiggest != tBiggest.max(standDev[i])) {
                    tBiggest = standDev[i];
                    thirdLoc = i;
                }
            }
        }
        String[] checkEndWithZero = { fBiggest.setScale(2, RoundingMode.HALF_UP).toString(),
                sBiggest.setScale(2, RoundingMode.HALF_UP).toString(),
                tBiggest.setScale(2, RoundingMode.HALF_UP).toString() };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 2; j++) {
                int length = checkEndWithZero[i].length();
                if (checkEndWithZero[i].substring(length - 1, length).equals("0")) {
                    checkEndWithZero[i] = checkEndWithZero[i].substring(0, length - 1);
                } else if (checkEndWithZero[i].substring(length - 1, length).equals(".")) {
                    checkEndWithZero[i] = checkEndWithZero[i].substring(0, length - 1);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(companyName[fisrtLoc] + "," + companyName[secondLoc] + "," + companyName[thirdLoc] + "," + startDay
                    + "," + endDay + "\n");
            bw.write(checkEndWithZero[0] + "," + checkEndWithZero[1] + "," + checkEndWithZero[2] + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void task4() {
        int targetLoc = 0;
        while (!companyName[targetLoc].equals(stock)) {
            targetLoc++;
        }
        BigDecimal divider = new BigDecimal(Integer.toString(endDay - startDay + 1));
        BigDecimal avgTime = new BigDecimal("0");
        BigDecimal avgPrice = new BigDecimal("0");
        for (int i = startDay - 1; i < endDay; i++) {
            BigDecimal addTime = new BigDecimal(i + 1);
            BigDecimal addPrice = new BigDecimal(stockPrice[i][targetLoc]);
            avgTime = avgTime.add(addTime);
            avgPrice = avgPrice.add(addPrice);
        }
        avgTime = avgTime.divide(divider, 8, RoundingMode.HALF_UP);
        avgPrice = avgPrice.divide(divider, 8, RoundingMode.HALF_UP);

        BigDecimal b1 = new BigDecimal("0");
        BigDecimal b0 = new BigDecimal("0");
        BigDecimal b1Num = new BigDecimal("0");
        BigDecimal b1Denom = new BigDecimal("0");
        // MathContext mc = new MathContext(8);

        for (int i = startDay - 1; i < endDay; i++) {
            BigDecimal time = new BigDecimal(i + 1);
            BigDecimal price = new BigDecimal(stockPrice[i][targetLoc]);
            time = time.subtract(avgTime);
            price = price.subtract(avgPrice);
            b1Num = b1Num.add(time.multiply(price));
            b1Denom = b1Denom.add(time.pow(2));
        }
        b1 = b1Num.divide(b1Denom, 8, RoundingMode.HALF_UP);
        b0 = avgPrice.subtract(b1.multiply(avgTime));

        String[] checkEndWithZero = { b0.setScale(2, RoundingMode.HALF_UP).toString(),
                b1.setScale(2, RoundingMode.HALF_UP).toString() };

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= 2; j++) {
                int length = checkEndWithZero[i].length();
                if (checkEndWithZero[i].substring(length - 1, length).equals("0")) {
                    checkEndWithZero[i] = checkEndWithZero[i].substring(0, length - 1);
                } else if (checkEndWithZero[i].substring(length - 1, length).equals(".")) {
                    checkEndWithZero[i] = checkEndWithZero[i].substring(0, length - 1);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(stock + "," + startDay + "," + endDay + "\n");
            bw.write(checkEndWithZero[1] + "," + checkEndWithZero[0] + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}