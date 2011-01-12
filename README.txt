This is a tool to setup a big database and Apache Lucene index to load test some index usage.

In particular, we aim to provide reproducible load tests for:
 * Hibernate Search - http://search.hibernate.org/
 * Infinispan's distributed Lucene Directory implementation - http://infinispan.org/ - http://community.jboss.org/wiki/infinispanasadirectoryforlucene

As a source of documents to index we use a specific dump of the Wikipedia database.

1 - Setup a MySQL database, create users: http://dev.mysql.com/doc/refman/5.1/en/adding-users.html

2 - Prepare database as described on http://en.wikipedia.org/wiki/Wikipedia:Database_download

3 - Use mwdumper.jar do efficiently import this large database http://downloads.dbpedia.org/mwdumper.jar,
    use as explained on
	https://bugzilla.wikimedia.org/show_bug.cgi?id=18328
	http://www.mediawiki.org/wiki/Manual:MWDumper

5 - Get and apply the schema to your new database
	http://svn.wikimedia.org/viewvc/mediawiki/trunk/phase3/maintenance/tables.sql?view=co

6 - Download the reference wikipedia dump, containing only last version of each article, in English only:
	http://download.wikimedia.org/enwiki/20101011/pages-meta-current.xml.bz2
	[WARNING! 12GB sized download]

7 - Execute data import:
	java -jar mwdumper.jar --format=sql:1.5 pages-meta-current.xml.bz2 | mysql -u<username> -p <databasename>
