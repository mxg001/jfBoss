package tools;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author ：quanhz
 * @date ：Created in 2019/9/24 16:43
 */
public class MyClassPathUtil {

    @Test
    public void fun1(){
        Integer a = null;
        System.out.println(1 == a);
    }


    public static void classPathConver(ArrayList<String> paths) {
        String template = "<include name=\"XXX\"/>";
        ArrayList<String> currPaths = new ArrayList<>();
        if (paths != null && paths.size() > 0) {
            String currPath = "";
            for (String path : paths) {
                if (path.indexOf(".java") > -1) {
                    int beginIndex = path.lastIndexOf("\\java\\") + 5;
                    String tmpPath = path.substring(beginIndex + 1, path.length()).replace(".java", "*.class").replace("\\", "/");
                    currPath = template.replace("XXX", "WEB-INF/classes/" + tmpPath);
                    currPaths.add(currPath);
                } else {
                    if (path.lastIndexOf("\\webapp\\") > -1) {
                        int beginIndex = path.lastIndexOf("\\webapp\\") + 7;
                        currPath = path.substring(beginIndex + 1, path.length()).replace("\\", "/");
                        currPath = template.replace("XXX", currPath);
                        currPaths.add(currPath);
                    }

                }
                System.out.println(currPath);
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> paths = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/java/tools/convertPaths.txt"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                paths.add(line);
            }
            MyClassPathUtil.classPathConver(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
