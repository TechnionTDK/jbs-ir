

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by shoham on 16/12/2016.
 */

public class Parser {


    public void splitJSONArrayToFiles(JSONObject a_jsonObject, String outputDirectory) {
        JSONArray jsonArray = (JSONArray) a_jsonObject.get("subjects");
        for(int i=0; i < jsonArray.size(); i++)
        {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String jsonString = jsonObject.toJSONString();
            PrintWriter printWriter = null;
            //String outputPath = "C:\\Users\\shoham\\Java workspace\\Projects\\JSONParser\\outputJsonDocsFinal\\";
            String fileName = outputDirectory + "//" + jsonObject.get("uri") + ".json";
            try {
                printWriter = new PrintWriter(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            printWriter.println(jsonString);
            printWriter.close();
        }
    }

    public void parseListOfJSONObjects(ArrayList<File> files, String outputDirectory)
    {
        for(int i=0; i< files.size(); i++)
        {
            File file = files.get(i);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            Object obj = null;
            try {
                obj = parser.parse(fileReader);
            } catch (org.json.simple.parser.ParseException | IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = (JSONObject) obj;
            splitJSONArrayToFiles(jsonObject, outputDirectory);
        }
    }



    public void getListOfFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        // get all the files from a directory
        File[] fList = directory.listFiles();
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
        if (f.isDirectory() == false)
        {
            parser.createOutputDirectory(outputDirectory);
        }

        ArrayList<File> files = new ArrayList<>();
        parser.getListOfFiles(inputDirectory, files);
        parser.parseListOfJSONObjects(files, outputDirectory);
    }
}