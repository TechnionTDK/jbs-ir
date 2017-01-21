package Evaluation;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.FileWriter;
import java.io.IOException;


public class JbsIrQueryResult
{
    private QueryResponse queryResponse = null;
    private SolrDocumentList solrDocumentList = null;
    private String query = null;

    JbsIrQueryResult()
    {

    }

    void setResponse(QueryResponse response, String query)
    {
        this.queryResponse = response;
        solrDocumentList = queryResponse.getResults();
        this.query = query;
    }

    public SolrDocumentList GetResults()
    {
        return solrDocumentList;
    }

    boolean IsDocumentInResults(String uri)
    {
        for(SolrDocument solrDocument: solrDocumentList)
        {
            String key =(String) solrDocument.getFieldValue("uri");
            if(key.equals(uri))
            {
                return true;
            }
        }
        return false;
    }


    boolean PrintResultsToFile() throws IOException {
        if(solrDocumentList == null || solrDocumentList.size() == 0)
        {
            return false;
        }
        FileWriter fw = new FileWriter("QueryResults.txt");
        fw.write("query : \"" + query + "\"" + "\n");

        for(SolrDocument solrDocument : solrDocumentList)
        {
            fw.write(solrDocument.get("uri") + "\n");
        }

        fw.close();

        return true;
    }
}
