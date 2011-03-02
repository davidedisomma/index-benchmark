/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ucieffe.benchmark;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.infinispan.Cache;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.manager.CacheContainer;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ucieffe.util.EntityManagerUtils;

/**
 * PerformanceCompareStressTest is useful to get an idea on relative performance between Infinispan
 * in local or clustered mode against a RAMDirectory or FSDirectory. To be reliable set a long
 * DURATION_MS and a number of threads similar to the use case you're interested in: results might
 * vary on the number of threads because of the lock differences. This is not meant as a benchmark
 * but used to detect regressions.
 * 
 * This requires Lucene > 2.9.1 or Lucene > 3.0.0 because of
 * https://issues.apache.org/jira/browse/LUCENE-2095
 * 
 * @author Sanne Grinovero
 * @author Davide Di Somma
 * @since 4.0
 */
@SuppressWarnings("unchecked")
@Test(groups = "profiling", testName = "lucene.profiling.PerformanceCompareStressTest", sequential = true)
public class PerformanceCompareStressTest {

   /**
    * The number of terms in the dictionary used as source of terms by the IndexWriter to produce
    * new documents
    */
   private static final int DICTIONARY_SIZE = 1 * 20;

   /** Concurrent Threads in tests */
   private static final int READER_THREADS = 5;
   private static final int WRITER_THREADS = 1;
   
   private static final int CHUNK_SIZE = 512 * 1024;

   private static final String indexName = "indexes/index-en/index";

   private static final long DURATION_MS = 2 * 60 * 100;

   private Cache cache;

//   private EmbeddedCacheManager cacheFactory;


   @Test(enabled=true)
   public void profileTestFSDirectory() throws InterruptedException, IOException {
      File indexDir = new File(new File("."), indexName);
      FSDirectory dir = FSDirectory.open(indexDir);
      stressTestDirectory(dir, "FSDirectory");
   }
   

   @Test(enabled=false)
   public void profileInfinispanLocalDirectory() throws InterruptedException, IOException {
      CacheContainer cacheContainer = CacheTestSupport.createLocalCacheManager();
      try {
         cache = cacheContainer.getCache();
         InfinispanDirectory dir = new InfinispanDirectory(cache, cache, cache, indexName, CHUNK_SIZE);
         stressTestDirectory(dir, "InfinispanLocal");
//         verifyDirectoryState();
      } finally {
         cacheContainer.stop();
      }
   }

   @Test(enabled=false)//to prevent invocations from some versions of TestNG
   public static void stressTestDirectory(Directory dir, String testLabel) throws InterruptedException, IOException {
      SharedState state = new SharedState(DICTIONARY_SIZE);
//      CacheTestSupport.initializeDirectory(dir);
      ExecutorService e = Executors.newFixedThreadPool(READER_THREADS + WRITER_THREADS);
      for (int i = 0; i < READER_THREADS; i++) {
         e.execute(new LuceneReaderThread(dir, state));
      }

      for (int i = 0; i < WRITER_THREADS; i++) {
         e.execute(new LuceneWriterThread(dir, state, EntityManagerUtils
        			.getEntityManagerInstance()));
      }
      e.shutdown();
      state.startWaitingThreads();
      Thread.sleep(DURATION_MS);
      long searchesCount = state.incrementIndexSearchesCount(0);
      long writerTaskCount = state.incrementIndexWriterTaskCount(0);
      state.quit();
      boolean terminatedCorrectly = e.awaitTermination(20, TimeUnit.SECONDS);
      Assert.assertTrue(terminatedCorrectly);
      System.out.println("Test " + testLabel + " run in " + DURATION_MS + "ms:\n\tSearches: " + searchesCount + "\n\t" + "Writes: "
               + writerTaskCount);
   }
   

//   @BeforeMethod
//   public void beforeTest() {
//      cacheFactory = TestCacheManagerFactory.createClusteredCacheManager(CacheTestSupport.createTestConfiguration());
//      cacheFactory.start();
//      cache = cacheFactory.getCache();
//      cache.clear();
//   }
//
   @AfterMethod
   public void afterTest() {
//      TestingUtil.killCaches(cache);
//	   cache.stop();
//	   cacheFactory.stop();
//      TestingUtil.killCacheManagers(cacheFactory);
//      TestingUtil.recursiveFileRemove(indexName);
	   System.out.println("Optimizing index in progress...");
	   FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(EntityManagerUtils.getEntityManagerInstance());
	   SearchFactory searchFactory = fullTextSession.getSearchFactory();

	   searchFactory.optimize();
   }
   
//   private void verifyDirectoryState() {
//      DirectoryIntegrityCheck.verifyDirectoryStructure(cache, indexName, true);
//   }

}
