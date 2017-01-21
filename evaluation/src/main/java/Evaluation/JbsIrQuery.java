package Evaluation;

import org.apache.solr.client.solrj.SolrQuery;


public class JbsIrQuery
{
    private SolrQuery solrQuery;
    private String queryString;

    JbsIrQuery()
    {
        queryString = "*.*";
        solrQuery = new SolrQuery();
        solrQuery.setQuery(queryString);
    }

    public JbsIrQuery(String query)
    {
        this.queryString = query;
        solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
    }

    SolrQuery getSolrQuery()
    {
        return solrQuery;
    }

    JbsIrQuery setNewQuery(String newQuery)
    {
        queryString = newQuery;
        solrQuery.setQuery(queryString);
        return this;
    }

    JbsIrQuery askForDebugInfo()
    {
        solrQuery.setShowDebugInfo(true);
        return this;
    }

    JbsIrQuery askForExplainOther(String uri)
    {
        solrQuery.setParam("explainOther", "uri:" + uri);
        return this;
    }

    JbsIrQuery setNumberOfRetrivedDocuments(int size)
    {
        solrQuery.setRows(size);
        return this;
    }
}
