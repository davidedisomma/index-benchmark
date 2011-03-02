package org.ucieffe.benchmark;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.ucieffe.model.Text;

/**
 * LuceneReaderThread is going to perform searches on the Directory until it's
 * interrupted. Good for performance comparisons and stress tests. It needs a
 * SharedState object to be shared with other readers and writers on the same
 * directory to be able to throw exceptions in case it's able to detect an
 * illegal state.
 * 
 * @author Sanne Grinovero
 * @author Davide Di Somma
 * @since 4.0
 */
public class LuceneReaderThread extends LuceneUserThread {

	protected IndexSearcher searcher;
	protected IndexReader indexReader;

	LuceneReaderThread(Directory dir, SharedState state) {
		super(dir, state);
	}

	@Override
	public void testLoop() throws IOException {
		// take ownership of some strings, so that no other thread will change
		// status for these:
		List<Text> texts = new ArrayList<Text>();
		int numElements = state.textsInIndex.drainTo(texts, 1);
		
		if(numElements == 0) {
//			System.out.println("No elements found.");
			return;
		}

		refreshIndexReader();


		for (Text text : texts) {
			System.out.println("Text-id:" + text.getOldId());
			System.out.println("Text-text:" + text.getOldText());
			
	        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_30);
            TokenStream stream = analyzer.tokenStream("contents", new StringReader(text.getOldText()));
            OffsetAttribute offsetAttribute = stream.getAttribute(OffsetAttribute.class);
            TermAttribute termAttribute = stream.getAttribute(TermAttribute.class);

            while (stream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                String term = termAttribute.term();

                System.out.print("[" + term + "] ");

				Query query = new TermQuery(new Term("oldText", term
						.toLowerCase().trim()));
				TopDocs docs = searcher.search(query, null, 1);

				System.out.println("Number of result:" + docs.totalHits);

				System.err.println(state.incrementIndexSearchesCount(1));;

            }            

		}
		// put the strings back at their place:
		state.textsInIndex.addAll(texts);
	}

	protected void refreshIndexReader() throws CorruptIndexException,
			IOException {
		if (indexReader == null) {
			indexReader = IndexReader.open(directory, true);
		} else {
			IndexReader before = indexReader;
			indexReader = indexReader.reopen();
			if (before != indexReader) {
				before.close();
			}
		}
		if (searcher != null) {
			searcher.close();
		}
		searcher = new IndexSearcher(indexReader);
	}

	@Override
	protected void cleanup() throws IOException {
		if (indexReader != null)
			indexReader.close();
	}

}
