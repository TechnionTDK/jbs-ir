# jbs-ir

##A Solr based search engine for the Jewish Bookshelf project 

The repository contains the following items

* Solr configuration files
  * Velocity UI for searching
  * browse-resources for Velocity
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
* Velocity UI for searching

##Solr
###Installing Solr
1. Follow this guide to install solr on your machine: [solr installation guide](https://cwiki.apache.org/confluence/display/solr/Installing+Solr)
2. From the solr directory start solr using `bin/solr start` command, this will start the Solr server in the background listening on port 8983
3. Make sure Solr is running using `bin/solr status` command, to check Solr started correctly
4. Create a new core using `bin/solr create -c <core-name>` command (for example: `bin/solr create -c jbs-ir`), for better understanding of what is a core in Solr visit [Solr Cores and solr.xml](https://cwiki.apache.org/confluence/display/solr/Solr+Cores+and+solr.xml)

* Solr can support multiple cores, each core cand be adrresed by sending request to the solr server and adding the the core name for example: http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir, where http://tdk2.cs.technion.ac.il:8983 - Solr server and jbs-ir - Solr core)

###Configurating Solr
####Before indexing documents with Solr, we need to configurate the fields we are going to use
Before modifying Solr core files we advise you to read [Documents, Fields, and Schema Design](https://cwiki.apache.org/confluence/display/solr/Documents%2C+Fields%2C+and+Schema+Design).

* In the `server/solr/<core-name>/conf` directory replace the `managed-schema` file with the one in the repository under `Solr configuration files` directory
* In the `server/solr/<core-name>/conf` directory replace the `solrconfig.xml` file with the one in the repository under `Solr configuration files` directory
* Restart Solr by running the `bin/solr restart` command from the solr directory

##HebMorph
####The text in the index will be in Hebrew, so we need to use an appropriate hebrew analyzer
We encourage you to read this wiki to have better understanding of the changes you are going to perform: [Understanding Analyzers, Tokenizers, and Filters](https://cwiki.apache.org/confluence/display/solr/Understanding+Analyzers%2C+Tokenizers%2C+and+Filters).

We chose to use the HebMorph hewbrew analyzer: [Hebmorph github repository](https://github.com/synhershko/HebMorph).

To integrate HebMorph into your Solr core follow [SOLR-README.md](https://github.com/synhershko/HebMorph/blob/master/SOLR-README.md) in Hebmorph github repository. Please note there is no need to change `managed-schema` and `solrconfig.xml` as we prepared them in advance and they were copied in the last section ( **if you choose to use the files in the repository, you have to place the HebMorph .jar file inside the `server` directory** )

in the following link the HebMorph .jar file can be found: [Hebmorph Lucene Maven](https://mvnrepository.com/artifact/com.code972.hebmorph/hebmorph-lucene/6.0.0)
* **Please note that if you are using Solr 6, HebMorph-6.x.x is required**

##Indexing documents from jbs-text using jbs-ir
In order to index the relevant documents with Solr, please follow the next steps
* Go to your Solr home directory and run these commands:
 * git clone https://github.com/TechnionTDK/jbs-ir.git
 * git clone https://github.com/TechnionTDK/jbs-text.git
* Go to `jbs-ir` directory and create .jar for the JsonParser in jbs-ir using `mvn package` command
 * JsonParser-1.0-jar-with-dependencies.jar will be located in jbs-ir/JsonParser/target/ 
* Go back to Solr home directory and run: `cp jbs-ir/JsonParser/target/JsonParser-1.0-jar-with-dependencies.jar .` 
* In order to parse the data from jbs-text into documents run:
 * `java -jar JsonParser-1.0-jar-with-dependencies.jar <path-to-desired-data-directory> <path-to-output-documents-directory>`
 * (For example: Java -jar JsonParser-1.0-jar-with-dependencies.jar jbs-text/old/tanach-json/ documentsForIndexing)
* To index the documents run: `bin/post -c <core-name> <path-to-output-documents-directory>`

##Velocity UI for searching
You can use the Solr Admin UI for running queries, analysis and viewing core details. Please visit [Overview of the Solr Admin UI](https://cwiki.apache.org/confluence/display/solr/Overview+of+the+Solr+Admin+UI) for more information.

If you wish for a more simplified UI, dedicated for allowing users to run queries, you are welcome to use the Velocity user UI we designed.
To read more about Velocity, go to [The Apache Velocity Project](http://velocity.apache.org/).
###Using our Velocity web UI
* Copy `velocity` under `Solr configuration files` to `server/solr/<core-name>/conf` directory
* Copy `browse-resources` under `Solr configuration files` to `server/solr/<core-name>/conf` directory
* Access the UI at: `http://<machine-name>:<Solr-port>/solr/<core-name>/browse`
 * For example: http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir/browse

##Evaluation tool
We included an evaluation tool for the solr search engine.

In order to use the tool (after cloning this repository) take a look at `execute()` method in `JbsIrTestTool.java` class, this method demonstrates how the tool can be used.

* **Note that the tool expects to receive the URL to the core of your Solr engine as an argument**

If you want to use the tool in it's default configuration, after running the `mvn package` command, do the following:

1. The .jar can be found in `jbs-ir/evaluation/target/evaluation-1.0-jar-with-dependencies.jar`
2. Run: `java -jar evaluation-1.0-jar-with-dependencies.jar http://<machine-name>:<Solr-port>/solr/<core-name>`
 * For example: java -jar evaluation-1.0-jar-with-dependencies.jar http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir
