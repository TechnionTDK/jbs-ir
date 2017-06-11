# Information Retrieval System For The Jewish Bookshelf Project 

## Introduction
This project is a part of the Jewish Bookshelf ecosystem in the Technion Data and Knowledge Lab.

It integrates several COTS projects in order to create a search engine for JBS data.
The data is pulled from [jbs-text](https://github.com/TechnionTDK/jbs-text) repository, which contains JSON objects that describe the JBS data.

### The COTSs which are integrated as part of the search engine
* [Apache Solr](http://lucene.apache.org/solr/)
* [HebMorph](https://github.com/synhershko/HebMorph) Hebrew analyzer
* [Apache Velocity](http://velocity.apache.org/) for the web application

### We also deliver two tools for administrators
* [JSON parser](https://github.com/TechnionTDK/jbs-ir/blob/master/README.md#indexing-documents-from-jbs-text-using-jbs-ir)
* [Evaluation tool](https://github.com/TechnionTDK/jbs-ir/blob/master/README.md#evaluation-tool)

### In this README you will learn
* How to install the system from scratch on a new machine
* How to update the serach engine's index when jbs-text is updated
* How to use the administration tools


## Repository content

* Solr configuration files
  * Velocity UI for searching
  * browse-resources for Velocity
  * solrconfig.xml
  * managed-schema
* JSON parser to convert data from `jbs-text` repository to documents for Solr
* A java test tool for the IR engine evaluation
* Instructions and documentation for bringing up the search engine on a new machine

## Installation

### In order to bring up the search engine on a new machine, we need to have the following components integrated
* Solr 
* jbs-text repository
* jbs-ir repository
* HebMorph
* Velocity UI for searching

## Solr
### Installing Solr
1. Follow this guide to install Solr on your machine: [Solr installation guide](https://cwiki.apache.org/confluence/display/solr/Installing+Solr)
2. From the Solr directory start Solr using `bin/solr start` command. This will start the Solr server in the background listening for requestes on port 8983
3. Make sure Solr is running using `bin/solr status` command, to check Solr started correctly
4. Create a new [core](https://cwiki.apache.org/confluence/display/solr/Solr+Cores+and+solr.xml) using `bin/solr create -c <core-name>` command (for example: `bin/solr create -c jbs-ir`)

Solr supports multiple cores under one Solr instance. 
Each core can be addressed by adding the the core name to Solr URL: `http://<machine-name>:<Solr-port>/solr/<core-name>`.
For example: http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir.

### Configuring Solr
#### Before indexing documents with Solr, we need to tell Solr what kind of data we would like to search, store and analyze in our documents and how.
Before modifying Solr core files, we also advise you to read [Documents, Fields, and Schema Design](https://cwiki.apache.org/confluence/display/solr/Documents%2C+Fields%2C+and+Schema+Design).

In this section you will replace two default core files with ones that we modified. 
To understand what changes have been applied to the files, or to learn how to index additional fields in your documents - visit [this Wiki page](https://github.com/TechnionTDK/jbs-ir/wiki/Changes-in-managed-schema-and-solrconfig.xml).

#### Instructions
* Clone this repository to your machine: `git clone https://github.com/TechnionTDK/jbs-ir.git`
* In the `server/solr/<core-name>/conf` directory replace the `managed-schema` file with the one in the repository under `Solr configuration files` directory
* In the `server/solr/<core-name>/conf` directory replace the `solrconfig.xml` file with the one in the repository under `Solr configuration files` directory
* Restart Solr by running the `bin/solr restart` command from the solr directory

## HebMorph
#### The text in the index will be in Hebrew, so we need to use an appropriate hebrew analyzer.
We encourage you to read this wiki to have better understanding of the changes you are going to perform: [Understanding Analyzers, Tokenizers, and Filters](https://cwiki.apache.org/confluence/display/solr/Understanding+Analyzers%2C+Tokenizers%2C+and+Filters).

We chose to use the HebMorph hewbrew analyzer: [Hebmorph github repository](https://github.com/synhershko/HebMorph).

To integrate HebMorph into your Solr core follow [SOLR-README.md](https://github.com/synhershko/HebMorph/blob/master/SOLR-README.md) in Hebmorph github repository. We provide here practical integration guidelines that should suffice:

* Download the latest HebMorph.jar file from [Hebmorph Lucene in Maven repository](https://mvnrepository.com/artifact/com.code972.hebmorph/hebmorph-lucene/6.0.0)
* If you are using Solr 6, HebMorph-6.x.x is required.
* There is no need to change `managed-schema` and `solrconfig.xml` as explained in `SOLR-README.md`, because we prepared them in advance and they were copied in the last section
* If you used the `solrconfig.xml` from this repository, please place the HebMorph .jar file under `server` directory (in `SOLR-README.md`, note that `instanceDir` refers to `server` directory in our case).
* Clone the HebMorph repository and copy the folder hspell-data-files under server.
* Finally, restart Solr by running the `bin/solr restart` command from the solr directory.

## Indexing documents from jbs-text using jbs-ir
* If you wish to update an existing index, follow the instructions in [Updating Solr Index](https://github.com/TechnionTDK/jbs-ir/wiki/Updating-Solr-Index)

Indexing is done according to `managed-schema` file we discussed before.
In order to index the relevant documents with Solr, please follow the next steps.
* Go to your Solr home directory and run these commands:
  * `git clone https://github.com/TechnionTDK/jbs-ir.git` (use git pull if you cloned the repository before)
  * `git clone https://github.com/TechnionTDK/jbs-text.git` (use git pull if you cloned the repository before)
* Go to `jbs-ir` directory and create .jar for the JsonParser in jbs-ir using `mvn package` command
  * `JsonParser-1.0-jar-with-dependencies.jar` will be located in `jbs-ir/JsonParser/target/` 
* Go back to Solr home directory and run: `cp jbs-ir/JsonParser/target/JsonParser-1.0-jar-with-dependencies.jar .` 
* In order to parse the data from jbs-text into documents run:
  * `java -jar JsonParser-1.0-jar-with-dependencies.jar <path-to-desired-data-directory> <path-to-output-documents-directory>`
  * (For example: java -jar JsonParser-1.0-jar-with-dependencies.jar jbs-text/ documentsForIndexing)
  * JsonParser converts the .json files in `<path-to-desired-data-directory>` (recursively) and proccesses them into multiple .json files, where each .json file contains one JSON object. These new .json files are placed into `<path-to-output-documents-directory>`.
* Restart Solr by running the `bin/solr restart` command from the solr directory
* To index the documents run: `bin/post -c <core-name> <path-to-output-documents-directory>`

 
## Admin UI
You can use the Solr Admin UI for running queries, analysis and viewing core details. Please visit [Overview of the Solr Admin UI](https://cwiki.apache.org/confluence/display/solr/Overview+of+the+Solr+Admin+UI) for more information.

You can access the Admin UI at: `http://<machine-name>:<Solr-port>`. 

To access a specific core: `http://<machine-name>:<Solr-port>/#/<core-name>`.

You can read about the most useful features we found in the Admin UI in [Useful Admin UI Features](https://github.com/TechnionTDK/jbs-ir/wiki/Useful-Admin-UI-Features).

## Velocity UI for searching
There is a basic UI for searching which you can access at: `http://<machine-name>:<Solr-port>/solr/<core-name>/browse`.

We wanted to make some adjusments to that UI so it will present the data in a more friendly way.

Solr uses Velocity for their web UIs, so we worked on top of the example files under `<solr-home-dir>/example/files/conf/velocity`.

For more information about the files we changed to configure the UI read the following wiki page: (Useful Velocity files)[https://github.com/TechnionTDK/jbs-ir/wiki/Useful-Velocity-files]

To read more about Velocity, go to [The Apache Velocity Project](http://velocity.apache.org/).

### Get the Velocity web UI we configured
* Copy `velocity` under `Solr configuration files` in this repository to `server/solr/<core-name>/conf` directory on your machine
* Copy `browse-resources` under `Solr configuration files` to `server` directory
* Run `bin/solr restart` from Solr home directory for the changes to apply
* Access the UI at: `http://<machine-name>:<Solr-port>/solr/<core-name>/browse`
  * For example: http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir/browse

## Evaluation tool
We included an evaluation tool for the Solr search engine.
The tool allows the user to automate the evaluation of the engine and extract any required data by:
* Setting desired parameters (e.g. query, number of documents to retrieve)
* Asking for debug information for the retrieved documents (more about debug information: [Debugging Solr](https://wiki.apache.org/solr/CommonQueryParameters#Debugging))
* Enabling the "explain other" options - allows the user to get debug information for a specific document, even if this document is not in the retrieved results

In order to use the tool (after cloning this repository) take a look at `execute()` method in `JbsIrTestTool.java` class, this method demonstrates how the tool can be used.

* **Note:** that the tool expects to receive the URL to the core of your Solr engine as an argument

* **Note:** because the terminal (Linux) and Command Prompt (Windows) don't support Hebrew text, it's better to run the application from a work envoirment such as IntelliJ IDEA 

### When running from IntelliJ IDEA (or other IDE)
Before running the Main method in the IDE, you have to configure `Program Arguments` in **Run/Debug configurations** to contain your Solr core address in this format: `http://<machine-name>:<Solr-port>/solr/<core-name>`.

### When running from command line
In case you changed the code and want to create a .jar file after running the `mvn package` command, do the following:
1. The .jar can be found in `jbs-ir/evaluation/target/evaluation-1.0-jar-with-dependencies.jar`
2. Run: `java -jar evaluation-1.0-jar-with-dependencies.jar http://<machine-name>:<Solr-port>/solr/<core-name>`
   * For example: java -jar evaluation-1.0-jar-with-dependencies.jar http://tdk2.cs.technion.ac.il:8983/solr/jbs-ir
