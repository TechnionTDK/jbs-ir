# jbs-ir

##A Solr based search engine for the Jewish Bookshelf project 

The repository contains the following items

* Solr configuration files
  * Velocity UI for searching
  * solrconfig.xml
  * managed-schema
* JSON parser to convert data from `jbs-text` repository to documents for Solr
* A java test tool for the IR engine evaluation
* Instructions and documentation for bringing up the search engine on a new machine

For more information please visit the wiki page of the repository.

###In order to bring up the search engine on a new machine, we need to have the following components integrated
* Solr 
* jbs-text repository
* jbs-ir repository
* HebMorph
* Optional - Velocity UI for searching

##Solr
###Installing Solr
1. Follow this guide to install solr on your machine: [solr installation guide](https://cwiki.apache.org/confluence/display/solr/Installing+Solr)
2. From the solr directory start solr using `bin/solr start` command
3. Make sure Solr is running using `bin/solr status` command
4. Create a new core using `bin/solr create -c <core name>` command (for example: `bin/solr create -c jbs-ir`)

###Configurating Solr
####Before indexing documents with Solr, we need to configurate the fields we are going to use
Before modifying Solr core files we advise you to read [Documents, Fields, and Schema Design](https://cwiki.apache.org/confluence/display/solr/Documents%2C+Fields%2C+and+Schema+Design).

* In the `server/solr/<core name>/conf` directory replace the `managed-schema` file with the one in the repository under `Solr configuration files` directory
* In the `server/solr/<core name>/conf`directory replace the `solrconfig.xml` file with the one in the repository under `Solr configuration files` directory

##HebMorph
####The text in the index will be in Hebrew, so we need to use an appropriate hebrew analyzer
We encourage you to read this wiki to have better understanding of the changes you are going to perform: [Understanding Analyzers, Tokenizers, and Filters](https://cwiki.apache.org/confluence/display/solr/Understanding+Analyzers%2C+Tokenizers%2C+and+Filters).

We chose to use the HebMorph hewbrew analyzer: [Hebmorph github repository](https://github.com/synhershko/HebMorph).

To integrate HebMorph into your Solr core follow [SOLR-README.md](https://github.com/synhershko/HebMorph/blob/master/SOLR-README.md) in Hebmorph github repository. Please note there is no need to change `managed-schema` and `solrconfig.xml` as we prepared them in advance and they were copied in the last section.
* **Please note that if you are using Solr 6, HebMorph-6.x.x is required**

##Indexing documents from jbs-text using jbs-ir
In order to index the relevant documents with Solr, please follow the next steps
1. Go to your Solr home directory and run these commands:
 1. git clone https://github.com/TechnionTDK/jbs-ir.git
 2. git clone https://github.com/TechnionTDK/jbs-text.git
2. Go to `jbs-ir` directory and create .jar for the JsonParser in jbs-ir using `mvn package` command
 * JsonParser-1.0-jar-with-dependencies.jar will be located in jbs-ir/JsonParser/target/ 
3. Go back to Solr home directory and run: `cp jbs-ir/JsonParser/target/JsonParser-1.0-jar-with-dependencies.jar .` 
4. In order to parse the data from jbs-text into documents run: 
   `java -jar JsonParser-1.0-jar-with-dependencies.jar <path-to-desired-data-directory> <path-to-output-documents-directory>`
   For example: Java -jar JsonParser-1.0-jar-with-dependencies.jar jbs-text/old/tanach-json/ documentsForIndexing
5. To index the documents run: `bin/post -c <core-name> <path-to-output-documents-directory>`




