package org.ucieffe.benchmark;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MergeScheduler;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * Collects common LuceneSettings for all tests; especially define the backwards compatibility.
 * 
 * @author Sanne Grinovero
 * @since 4.0
 */
public class LuceneSettings {

   public static final Analyzer analyzer = new SimpleAnalyzer();
   
   private static final MergeScheduler mergeScheduler = new SerialMergeScheduler();
   
   public static IndexWriter openWriter(Directory directory, int maxMergeDocs, boolean useSerialMerger) throws CorruptIndexException, LockObtainFailedException, IOException {
      IndexWriter iwriter = new IndexWriter(directory, LuceneSettings.analyzer, false, MaxFieldLength.UNLIMITED);
      if (useSerialMerger) {
         iwriter.setMergeScheduler(mergeScheduler);
      }
      iwriter.setMaxMergeDocs(maxMergeDocs);
      iwriter.setUseCompoundFile(false);
      return iwriter;
   }
   
   public static IndexWriter openWriter(Directory directory, int maxMergeDocs) throws CorruptIndexException, LockObtainFailedException, IOException {
      return openWriter(directory, maxMergeDocs, false);
   }

}
