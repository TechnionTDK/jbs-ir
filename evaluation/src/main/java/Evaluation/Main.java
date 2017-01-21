package Evaluation;

public class Main
{
    public static void main (String[] args)
    {
        String connectionString = args[0];
        if(connectionString != null && !connectionString.isEmpty())
        {
            JbsIrTestTool jbsIrTestTool = new JbsIrTestTool(connectionString);
            jbsIrTestTool.execute();
        }
        else
        {
            System.out.println("Please specify the Solr core URL");
        }
    }
}
