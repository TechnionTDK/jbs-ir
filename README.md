# jbs-ir

##This is a solr base search engine for the Jewish Bookshelf project 

the repository contains the following:

1. the configured solr folder including the cliend UI
2. JSON parser to pull and extract data from jbs-text repository
3. a java test tool for the ir engine evaluation
4. an explanation how to configure a solr engine locally on new machine

for more information go to the wiki page of the repository 

##installing and configuring solr jbs - search (linux)
1. use the following guide to install solr on your machine: [solr installation guide](https://cwiki.apache.org/confluence/display/solr/Installing+Solr)
2. from the solr directory start solr with the `bin/solr start` command
3. check solr running using `bin/solr status` command
4. create a new core with the `bin/solr create -c <core name> ` command, for example: `bin/solr create -c jbs-ir`

