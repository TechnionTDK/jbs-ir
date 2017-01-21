package Evaluation;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.StringUtils;

import java.io.IOException;
import java.util.Scanner;


class JbsIrTestTool
{
    private JbsIrQuery jbsIrQuery;
    private SolrClient solrClient;
    private JbsIrQueryResult jbsIrQueryResult;
    private JbsIrQueryDebugInfo jbsIrQueryDebugInfo;
    private boolean debugFlag;
    private boolean explainOtherFlag;
    private String connectionString;

    JbsIrTestTool(String connectionString)
    {
        this.connectionString = connectionString;
        debugFlag = false;
        explainOtherFlag = false;
        jbsIrQuery = new JbsIrQuery();
        JbsIrSolrClient.getInstance().setConnectionString(connectionString);
        solrClient = JbsIrSolrClient.getInstance().getSolrClient();
        jbsIrQueryDebugInfo = new JbsIrQueryDebugInfo();
        jbsIrQueryResult = new JbsIrQueryResult();

    }

    void execute()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("enter query");
        String query = reader.next();
        jbsIrQuery.setNewQuery(query);
        System.out.println("set number of retrieved documents: ");
        int numOfDocuments = reader.nextInt();
        jbsIrQuery.setNumberOfRetrivedDocuments(numOfDocuments);
        System.out.println("would you like debug info? y or n ");
        String isDenugInfo = reader.next();
        if(isDenugInfo.equals("y"))
        {
            debugFlag = true;
            jbsIrQuery.askForDebugInfo();
            System.out.println("enter sefer-perek-pasuk format if you want to examine of perushim as part of debug info\n" +
                    "for a specific perush enter the name of the document otherwise enter \"n\"");
            String explainOtherString = reader.next();
            if(explainOtherString.matches("\\d+-\\d+-\\d+"))
            {
                explainOtherFlag = true;
                jbsIrQuery.askForExplainOther("*-" + explainOtherString);
            }
            else if(!explainOtherString.equals("n") && !StringUtils.isEmpty(explainOtherString))
            {
                explainOtherFlag = true;
                jbsIrQuery.askForExplainOther(explainOtherString);
            }
        }


        QueryResponse queryResponse= null;
        try
        {
            queryResponse = solrClient.query(jbsIrQuery.getSolrQuery());
        }
        catch (SolrServerException | IOException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            System.out.println("Connection to specified host failed. Please enter the Solr core URL.");
        }

        jbsIrQueryResult.setResponse(queryResponse, jbsIrQuery.getSolrQuery().getQuery());
        jbsIrQueryDebugInfo.setResponse(queryResponse);

        try {
            jbsIrQueryResult.PrintResultsToFile();
        } catch (IOException e) {
            System.out.println("printing results failed");
            e.printStackTrace();
        }


        if(debugFlag)
        {
            try {
                jbsIrQueryDebugInfo.printDebugInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(explainOtherFlag)
            {
                try {
                    jbsIrQueryDebugInfo.printExplainOther();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("do you want to check if specific documents are in the results list? y or n");
        String isCheckInResults = reader.next();
        if(isCheckInResults.equals("y"))
        {
            System.out.println("enter list of documents seperated by \",\"");
            String documentsString = reader.next();
            String[] documentsArray = documentsString.split(",");
            for(String doc : documentsArray)
            {
                boolean inResults = jbsIrQueryResult.IsDocumentInResults(doc);
                if(inResults)
                {
                    System.out.println(doc + "  is in the result list");
                }
                else
                {
                    System.out.println(doc + " is not in the result list");
                }
            }
        }

        System.out.println("Result files were created in jar's directory");

    }
}
