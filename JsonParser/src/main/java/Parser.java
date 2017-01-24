import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;


public class Parser {


    private void splitJSONArrayToFiles(JSONObject a_jsonObject, String outputDirectory) {
        JSONArray jsonArray = (JSONArray) a_jsonObject.get("subjects");
        for (Object aJsonArray : jsonArray) {
            JSONObject jsonObject = (JSONObject) aJsonArray;
            String jsonString = jsonObject.toJSONString();
            PrintWriter printWriter = null;
            String fileName = outputDirectory + "/" + jsonObject.get("uri") + ".json";
            try {
                printWriter = new PrintWriter(fileName);
            } catch (FileNotFoundException e) {
                System.out.println("Error creating the following file: " + fileName);
            }
            assert printWriter != null;
            printWriter.println(jsonString);
            printWriter.close();
        }
    }

    private void parseListOfJSONObjects(ArrayList<File> files, String outputDirectory)
    {
        for (File file : files) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                System.out.println("Error creating fileReader for: " + file.getAbsolutePath());
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            Object obj = null;
            try {
                obj = parser.parse(fileReader);
            } catch (ParseException | IOException e) {
                System.out.println("Error parsing " + file.getAbsolutePath());
                e.printStackTrace();
            }
            JSONObject jsonObject = (JSONObject) obj;
            splitJSONArrayToFiles(jsonObject, outputDirectory);
        }
    }



    private void getListOfFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList == null) return;
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                getListOfFiles(file.getAbsolutePath(), files);
            }
        }

    }

    private void createOutputDirectory(String outputDirectory)
    {
        final File file = new File(outputDirectory);
        file.mkdir();
    }

    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            System.out.println("Please specify path to input as 1st argument and output directory as 2nd argument");
            return;
        }
        String inputDirectory = args[0];
        String outputDirectory = args[1];
        Parser parser = new Parser();
        File f = new File(outputDirectory);
        if (!f.isDirectory())
        {
            parser.createOutputDirectory(outputDirectory);
        }

        ArrayList<File> files = new ArrayList<>();
        parser.getListOfFiles(inputDirectory, files);
        parser.parseListOfJSONObjects(files, outputDirectory);
    }
}