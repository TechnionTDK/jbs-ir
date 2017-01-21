package Evaluation;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

class JbsIrSolrClient {

    private String urlString;
    private SolrClient solrClient;

    private static JbsIrSolrClient Instance = null;

    static JbsIrSolrClient getInstance()
    {
        if (Instance == null)
        {
            Instance = new JbsIrSolrClient();
        }
        return Instance;
    }

    private JbsIrSolrClient()
    {

    }


    SolrClient getSolrClient()
    {
        solrClient = new HttpSolrClient.Builder(urlString).build();
        return solrClient;
    }

    void setConnectionString(String connectionString)
    {
        urlString = connectionString;
    }
}
