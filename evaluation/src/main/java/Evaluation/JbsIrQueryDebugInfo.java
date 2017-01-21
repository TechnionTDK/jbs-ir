package Evaluation;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.SimpleOrderedMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class JbsIrQueryDebugInfo
{
    private QueryResponse queryResponse;

    JbsIrQueryDebugInfo()
    {

    }

    void setResponse(QueryResponse response)
    {
        this.queryResponse = response;
    }

    public Map<String,String> getExplanation()
    {
        return queryResponse.getExplainMap();
    }

    private String[] getExplainOtherInfo()
    {
        Map<String,Object> debugMap = queryResponse.getDebugMap();
        SimpleOrderedMap<String> explainOtherMap = (SimpleOrderedMap<String>) debugMap.get("explainOther");
        String[] result = new String[explainOtherMap.size() * 2];
        for(int i = 0, j = 0  ; i < explainOtherMap.size() ; i++)
        {
            result[j++] = explainOtherMap.getName(i);
            result[j++] = explainOtherMap.getVal(i);

        }
        return result;
    }


    void printDebugInfo() throws IOException
    {
        Map<String,String> explainMap =  queryResponse.getExplainMap();
        FileWriter fw = new FileWriter("ExplanationInfo.txt");
        String[] keys = explainMap.keySet().toArray(new String[explainMap.size()]);
        ArrayList<String> values = new ArrayList<>(explainMap.values());
        for(int i = 0; i < explainMap.size(); i++)
        {
            fw.write(keys[i] + ":" + "\n");
            fw.write(values.get(i) + "\n\n");
        }

        fw.close();
    }

    void printExplainOther() throws IOException
    {
        String[] explainOther = getExplainOtherInfo();
        FileWriter fw = new FileWriter("ExplainOther.txt");
        for(int i = 0; i<explainOther.length ;)
        {
            fw.write("file name: " + explainOther[i++] + "\n");
            fw.write(explainOther[i++] + "\n");
            fw.write("------------------------------------\n");
        }

        fw.close();
    }
}
