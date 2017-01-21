package Evaluation;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

class JbsIrSolrClient {

    private final String urlString = "http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir/";
    private SolrClient solrClient;

    private static JbsIrSolrClient Instance = new JbsIrSolrClient();

    static JbsIrSolrClient getInstance()
    {
        return Instance;
    }

    private JbsIrSolrClient()
    {
        solrClient = new HttpSolrClient.Builder(urlString).build();
    }

    SolrClient getSolrClient()
    {
        return solrClient;
    }
}
