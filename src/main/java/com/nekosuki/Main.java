package com.nekosuki;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException{
        String filePath = "";
        if (args.length > 0) filePath = args[0];
        else throw new RuntimeException("Not DataFiles....");
        File file = new File(filePath);
        File editFile = new File(file.getName().split("\\.")[0] + "Edit.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(editFile));
        bw.write("#time \n");
        String line;
        int cnt = 0;
        int lineNum = 0;
        double delta = 0;
        while ((line = br.readLine()) != null){
            if (cnt == 2){
                delta = Double.parseDouble(line.split("\t")[1]);
            }
            if (cnt < 5) {
                cnt++;
                continue;
            }
            if ((line.split("\t").length == 4)) {
                String[] test = line.split("\t");
                System.out.println(Arrays.toString(test));
                double d1 = Double.parseDouble(test[1]);
                double d2 = Double.parseDouble(test[3]);
                String format = "%.10f";
                String time = format.formatted(lineNum * delta);
                String data1 = format.formatted(d1);
                String data2 = format.formatted(d2);
                String text = time + "\t" + data1 + "\t" + data2 + "\n";
                lineNum++;
                bw.write(text);
            }
        }
        br.close();
        bw.close();
        if (args.length >= 3){
            createPLT(file, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }else {
            createPLT(file, 0, 0);
        }
    }

    public static void createPLT(File f, int a, int b){
        var resource = Objects.requireNonNull(Main.class.getResource("example.txt")).getFile();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(resource, StandardCharsets.UTF_8))){
            while ((line = br.readLine()) != null){
                if (line.equals("plot \"%s\" u 1 : 2")){
                    line = line.formatted(f.getName().split("\\.")[0] + "Edit.txt");
                }else if (line.equals("set output \"%s\"")){
                    line = line.formatted(f.getName().split("\\.")[0] + "Out.emf");
                }
                line = line + "\n";
                stringBuilder.append(line);
            }
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        String fName = f.getName().split("\\.")[0];
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fName + "Out.plt", StandardCharsets.UTF_8))){
            bw.write(stringBuilder.toString());
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
