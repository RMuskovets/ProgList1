package org.roman.proglist;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

class getInstalledAppList {

    private static final String REGQUERY_UTIL = "reg query ";
    private static final String REGSTR_TOKEN = "REG_";


    static String getCurrentUserPersonalFolderPath() {
        try {
            String s = REGQUERY_UTIL + "HKEY_LOCAL_MACHINE\\Software"
                    + "\\Microsoft\\Windows\\CurrentVersion\\Uninstall";
            Process process = Runtime.getRuntime().exec(s);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult();

            return result.trim();
        }
        catch (IOException | InterruptedException e) {
            return null;
        }
    }

    static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw;

        StreamReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        String getResult() {
            return sw.toString();
        }
    }

    static List<List<String>> getDisplayNameDword(String str){

        Set<List<String>> set = new HashSet<>();
        String [] array;
        array = str.split("\n");
        for(String i : array){
            set.add( getName(i));
        }
        Iterator<List<String>> i = set.iterator();
        List<List<String>> y = new ArrayList<>();
        while(i.hasNext()){
            List<String> x = i.next();
            for (int j = 0; x != null && j < x.size(); j++) {
                String res = x.get(j);
                if (res == null) continue;
                int base = (res.startsWith("DWORD")) ? 16 : 10; res = res.split(" {4}")[1];
                if (base == 16) res = Integer.toString(Integer.parseInt(res.substring(2), base));
                if (res.length() > 8 && base == 16) res = new SimpleDateFormat("yyyymmdd").format(new Date(Integer.parseInt(res)));
                x.set(j, res);
            }
            if (x == null) continue;
            y.add(x);
        }
        return y;
    }

    private static String getParam(String s, String param) {
        Process process;
        try {
            process = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v " + param);
            StreamReader sr = new StreamReader(process.getInputStream());
            sr.start();
            process.waitFor();
            sr.join();
            String[] parsed = sr.getResult().split(REGSTR_TOKEN);
            if (parsed.length > 1) return (parsed[parsed.length - 1]).trim();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } return null;
    }

    private static List<String> getName(String s){
        List<String> list = new ArrayList<>();
        String[] params = {"DisplayName", "DisplayVersion", "InstallDate", "Publisher"};
        Arrays.stream(params).forEach(e -> list.add(getParam(s, e)));
        return list;
    }
}