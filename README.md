# jbs-ir

##This is a Solr based search engine for the Jewish Bookshelf project 

The repository contains the following items

* The configured solr folder including the cliend UI
* JSON parser to extract data from jbs-text repository
* A java test tool for the ir engine evaluation
* An explanation how to configure a solr engine locally on new machine

For more information go to the wiki page of the repository 

##Installing and configuring solr 
###Installing solr
1. Follow the following guide to install solr on your machine: [solr installation guide](https://cwiki.apache.org/confluence/display/solr/Installing+Solr)
2. From the solr directory start solr using `bin/solr start` command
3. Validate that Solr is running using `bin/solr status` command
4. Create a new core using `bin/solr create -c <core name>` command, for example: `bin/solr create -c jbs-ir`

####Before inserting documents into the index we need to declare the fields we are going to use:
* Before doing changes to the core files we advice you to read [Documents, Fields, and Schema Design](https://cwiki.apache.org/confluence/display/solr/Documents%2C+Fields%2C+and+Schema+Design)

1. In the `server/solr/<core name>/conf` directory replace the "managed-schema" file with the one in the repository under `Solr configuration files`

####The text in the index will be in Hebrew, so we need to use an appropriate hebrew analyzer
* Read the following wiki to have better understanding of the changes you are going to perform: [Understanding Analyzers, Tokenizers, and Filters](https://cwiki.apache.org/confluence/display/solr/Understanding+Analyzers%2C+Tokenizers%2C+and+Filters)
* We chose to use the Hebmorph hewbrew analyzer: [Hebmorph github repository](https://github.com/synhershko/HebMorph)
To integrate Hebmorph into your Solr core follow [SOLR-README.md](https://github.com/synhershko/HebMorph/blob/master/SOLR-README.md) in Hebmorph github repository.

###indexing documents:
1. clone the jbs-text repository
2. clone the jbs-ir repository
3. run the following commands:
  1. 




