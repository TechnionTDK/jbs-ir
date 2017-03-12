package Evaluation;

public class Main
{
    public static void main (String[] args)
    {
        if(args.length == 0)
        {
            System.out.println("Please specify the Solr core URL in the following format:\nhttp://<machine-name>:<Solr-port>/solr/<core-name>");
            return;
        }
        String connectionString = args[0];
        if(connectionString != null && !connectionString.isEmpty())
        {
            JbsIrTestTool jbsIrTestTool = new JbsIrTestTool(connectionString);
            jbsIrTestTool.execute();
        }
        else
        {
            System.out.println("Please specify the Solr core URL in the following format:\nhttp://<machine-name>:<Solr-port>/solr/<core-name>");
        }
    }
}
