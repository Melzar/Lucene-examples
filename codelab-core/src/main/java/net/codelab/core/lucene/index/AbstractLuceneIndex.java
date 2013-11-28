package net.codelab.core.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DocumentStoredFieldVisitor;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.TermFreqIterator;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.search.vectorhighlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.automaton.LevenshteinAutomata;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Melzarek on 21/11/13.
 */
public abstract class AbstractLuceneIndex<T> {

    private final Version LUCENE_VERSION = Version.LUCENE_46;

    private final Analyzer analyzer;

    private final Directory indexDirectory;

   // private final SpellChecker spellChecker;

    private IndexWriterConfig indexWriterConfig;

    private IndexWriter indexWriter;

    private IndexReader indexReader;

    private IndexSearcher indexSearcher;


    public AbstractLuceneIndex(String indexDirectoryPath)
    {
        analyzer = getAnalyzer();

        Directory dir = null;
        try {
            File file = new File( indexDirectoryPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            dir = FSDirectory.open(file);
        } catch (IOException e) {
            dir = null;
            e.printStackTrace();
        }
        indexDirectory = dir;
        try {
            indexReader = DirectoryReader.open(indexDirectory);
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexWriter= null;
    }

    public abstract Document convertToDocument(T item);

    public abstract List<Document> convertToDocumentBulk(List<T> item);

    public abstract T convertFromDocument(Document doc);

    public abstract List<T> convertFromDocumentBulk(List<Document> doc);

    public Analyzer  getAnalyzer()
    {
        return new StandardAnalyzer(Version.LUCENE_46);
    }

    public void startCreateNewIndex()
    {
        indexWriterConfig = new IndexWriterConfig(LUCENE_VERSION, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriterConfig.setRAMBufferSizeMB(256);
        try {

            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAppendDataToIndex()
    {
        indexWriterConfig = new IndexWriterConfig(LUCENE_VERSION, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
        indexWriterConfig.setRAMBufferSizeMB(256);
        try
        {
            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void startUpdateIndex()
    {
        startCreateNewIndex();
        try {
            indexWriter.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endIndexOperations()
    {
        try {
            indexWriter.forceMerge(256);
            indexWriter.commit();
            indexWriter.close();
            indexWriterConfig = null;
            indexWriter = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemToIndex(T item)
    {
        try {
            indexWriter.addDocument(convertToDocument(item));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemToIndexBulk(List<T> items)
    {
        try {
            indexWriter.addDocuments(convertToDocumentBulk(items));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty()
    {
        return indexReader.numDocs() == 0;
    }

    public ResultsDTO<T> searchIndex(String query)
    {
//        QueryParser queryParser = new QueryParser(Version.LUCENE_45, "title", analyzer);
//        queryParser.setLowercaseExpandedTerms(false);
//        int num = indexReader.numDocs()
          ResultsDTO results = new ResultsDTO();
        try {
//queryParser.parse(query);
//
 //           QueryParser queryParser = new QueryParser(Version.LUCENE_46, "title", analyzer);
//                        queryParser.setFuzzyMinSim(2);
 //           queryParser.setLowercaseExpandedTerms(true);
//            booleanQuery.add(queryParser.parse(query+"~2"), BooleanClause.Occur.SHOULD);
//            booleanQuery.add(queryParser.parse(query+"*"), BooleanClause.Occur.SHOULD);
            BooleanQuery booleanQuery = new BooleanQuery();
  //          FastVectorHighlighter highlighter = new FastVectorHighlighter(true, true, new SimpleFragListBuilder(), new ScoreOrderFragmentsBuilder(BaseFragmentsBuilder.COLORED_PRE_TAGS,BaseFragmentsBuilder.COLORED_POST_TAGS));
            TokenStream stream = analyzer.tokenStream("title", new StringReader(query));
            CharTermAttribute charterm = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while(stream.incrementToken())
            {
                Term term = new Term("title", charterm.toString());
                booleanQuery.add(new FuzzyQuery(term, 1), BooleanClause.Occur.SHOULD);
                booleanQuery.add(new PrefixQuery(term), BooleanClause.Occur.SHOULD);
     //           booleanQuery.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
            }
            stream.end();
            stream.close();
            DuplicateFilter duplicateFilter = new DuplicateFilter("title");
            duplicateFilter.setKeepMode(DuplicateFilter.KeepMode.KM_USE_LAST_OCCURRENCE);
            long start = System.currentTimeMillis();
            TopDocs docs = indexSearcher.search(booleanQuery,duplicateFilter,100);
            long end = System.currentTimeMillis();
            QueryScorer queryScorer = new QueryScorer(booleanQuery);
//            Highlighter highlighterr = new Highlighter(queryScorer);
//            highlighterr.setTextFragmenter(new SimpleSpanFragmenter(queryScorer));
//            System.out.println(booleanQuery.toString());
            ScoreDoc[] score = docs.scoreDocs;
            results.setTotalhits(docs.totalHits);
            results.setSearchtime(end-start);
//            Set<String> fieldtoload = new HashSet<>();
//            fieldtoload.add("title");
//            Query q = queryParser.parse(query);
            for(ScoreDoc document : score)
            {
//TokenStream tokenStream = TokenSources.getTokenStream(indexSearcher.getIndexReader(),document.doc,"title",analyzer);
//                String string = highlighter.getBestFragment(highlighter.getFieldQuery(booleanQuery),indexSearcher.getIndexReader(),document.doc,"title",100);
//                System.out.println(string);
//                String fragment = highlighterr.getBestFragment(tokenStream,indexSearcher.doc(document.doc).get("title"));
//                System.out.println(fragment);
                results.getResults().add(convertFromDocument(indexSearcher.doc(document.doc)));
            }
//        } catch (ParseException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        } catch (InvalidTokenOffsetsException e) {
//            e.printStackTrace();
        }
        return results;
    }

//
//    50  private IndexFiles() {}
//    051
//            052  /** Index all text files under a directory. */
//            053  public static void main(String[] args) {
//        054    String usage = "java org.apache.lucene.demo.IndexFiles"
//        055                 + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
//        056                 + "This indexes the documents in DOCS_PATH, creating a Lucene index"
//        057                 + "in INDEX_PATH that can be searched with SearchFiles";
//        058    String indexPath = "index";
//        059    String docsPath = null;
//        060    boolean create = true;
//        061    for(int i=0;i<args.length;i++) {
//            062      if ("-index".equals(args[i])) {
//                063        indexPath = args[i+1];
//                064        i++;
//                065      } else if ("-docs".equals(args[i])) {
//                066        docsPath = args[i+1];
//                067        i++;
//                068      } else if ("-update".equals(args[i])) {
//                069        create = false;
//                070      }
//            071    }
//        072
//        073    if (docsPath == null) {
//            074      System.err.println("Usage: " + usage);
//            075      System.exit(1);
//            076    }
//        077
//        078    final File docDir = new File(docsPath);
//        079    if (!docDir.exists() || !docDir.canRead()) {
//            080      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
//            081      System.exit(1);
//            082    }
//        083
//        084    Date start = new Date();
//        085    try {
//            086      System.out.println("Indexing to directory '" + indexPath + "'...");
//            087
//            088      Directory dir = FSDirectory.open(new File(indexPath));
//            089      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//            090      IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
//            091
//            092      if (create) {
//                093        // Create a new index in the directory, removing any
//                094        // previously indexed documents:
//                095        iwc.setOpenMode(OpenMode.CREATE);
//                096      } else {
//                097        // Add new documents to an existing index:
//                098        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
//                099      }
//            100
//            101      // Optional: for better indexing performance, if you
//            102      // are indexing many documents, increase the RAM
//            103      // buffer.  But if you do this, increase the max heap
//            104      // size to the JVM (eg add -Xmx512m or -Xmx1g):
//            105      //
//            106      // iwc.setRAMBufferSizeMB(256.0);
//            107
//            108      IndexWriter writer = new IndexWriter(dir, iwc);
//            109      indexDocs(writer, docDir);
//            110
//            111      // NOTE: if you want to maximize search performance,
//            112      // you can optionally call forceMerge here.  This can be
//            113      // a terribly costly operation, so generally it's only
//            114      // worth it when your index is relatively static (ie
//            115      // you're done adding documents to it):
//            116      //
//            117      // writer.forceMerge(1);
//            118
//            119      writer.close();
//            120
//            121      Date end = new Date();
//            122      System.out.println(end.getTime() - start.getTime() + " total milliseconds");
//            123
//            124    } catch (IOException e) {
//            125      System.out.println(" caught a " + e.getClass() +
//                    126       "\n with message: " + e.getMessage());
//            127    }
//        128  }
//    129
    /**
     131   * Indexes the given file using the given writer, or if a directory is given,
     132   * recurses over files and directories found under the given directory.
     133   *
     134   * NOTE: This method indexes one document per input file.  This is slow.  For good
     135   * throughput, put multiple documents into your input file(s).  An example of this is
     136   * in the benchmark module, which can create "line doc" files, one document per line,
     137   * using the
     138   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     139   * >WriteLineDocTask</a>.
     140   *
     141   * @param writer Writer to the index where the given file/dir info will be stored
     142   * @param file The file to index, or the directory to recurse into to find files to index
     143   * @throws IOException If there is a low-level I/O error
     144   */
//    145  static void indexDocs(IndexWriter writer, File file)
//    146    throws IOException {
//        147    // do not try to index files that cannot be read
//        148    if (file.canRead()) {
//            149      if (file.isDirectory()) {
//                150        String[] files = file.list();
//                151        // an IO error could occur
//                152        if (files != null) {
//                    153          for (int i = 0; i < files.length; i++) {
//                        154            indexDocs(writer, new File(file, files[i]));
//                        155          }
//                    156        }
//                157      } else {
//                158
//                159        FileInputStream fis;
//                160        try {
//                    161          fis = new FileInputStream(file);
//                    162        } catch (FileNotFoundException fnfe) {
//                    163          // at least on windows, some temporary files raise this exception with an "access denied" message
//                    164          // checking if the file can be read doesn't help
//                    165          return;
//                    166        }
//                167
//                168        try {
//                    169
//                    170          // make a new, empty document
//                    171          Document doc = new Document();
//                    172
//                    173          // Add the path of the file as a field named "path".  Use a
//                    174          // field that is indexed (i.e. searchable), but don't tokenize
//                    175          // the field into separate words and don't index term frequency
//                    176          // or positional information:
//                    177          Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
//                    178          doc.add(pathField);
//                    179
//                    180          // Add the last modified date of the file a field named "modified".
//                    181          // Use a LongField that is indexed (i.e. efficiently filterable with
//                    182          // NumericRangeFilter).  This indexes to milli-second resolution, which
//                    183          // is often too fine.  You could instead create a number based on
//                    184          // year/month/day/hour/minutes/seconds, down the resolution you require.
//                    185          // For example the long value 2011021714 would mean
//                    186          // February 17, 2011, 2-3 PM.
//                    187          doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));
//                    188
//                    189          // Add the contents of the file to a field named "contents".  Specify a Reader,
//                    190          // so that the text of the file is tokenized and indexed, but not stored.
//                    191          // Note that FileReader expects the file to be in UTF-8 encoding.
//                    192          // If that's not the case searching for special characters will fail.
//                    193          doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
//                    194
//                    195          if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
//                        196            // New index, so we just add the document (no old document can be there):
//                        197            System.out.println("adding " + file);
//                        198            writer.addDocument(doc);
//                        199          } else {
//                        200            // Existing index (an old copy of this document may have been indexed) so
//                        201            // we use updateDocument instead to replace the old one matching the exact
//                        202            // path, if present:
//                        203            System.out.println("updating " + file);
//                        204            writer.updateDocument(new Term("path", file.getPath()), doc);
//                        205          }
//                    206
//                    207        } finally {
//                    208          fis.close();
//                    209        }
//                210      }
//            211    }
//        212  }
//    213}
//
///** Simple command-line based search demo. */
//    041public class SearchFiles {
//        042
//                043  private SearchFiles() {}
//        044
//                045  /** Simple command-line based search demo. */
//                046  public static void main(String[] args) throws Exception {
//            047    String usage =
//                    048      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
//            049    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
//                050      System.out.println(usage);
//                051      System.exit(0);
//                052    }
//            053
//            054    String index = "index";
//            055    String field = "contents";
//            056    String queries = null;
//            057    int repeat = 0;
//            058    boolean raw = false;
//            059    String queryString = null;
//            060    int hitsPerPage = 10;
//            061
//            062    for(int i = 0;i < args.length;i++) {
//                063      if ("-index".equals(args[i])) {
//                    064        index = args[i+1];
//                    065        i++;
//                    066      } else if ("-field".equals(args[i])) {
//                    067        field = args[i+1];
//                    068        i++;
//                    069      } else if ("-queries".equals(args[i])) {
//                    070        queries = args[i+1];
//                    071        i++;
//                    072      } else if ("-query".equals(args[i])) {
//                    073        queryString = args[i+1];
//                    074        i++;
//                    075      } else if ("-repeat".equals(args[i])) {
//                    076        repeat = Integer.parseInt(args[i+1]);
//                    077        i++;
//                    078      } else if ("-raw".equals(args[i])) {
//                    079        raw = true;
//                    080      } else if ("-paging".equals(args[i])) {
//                    081        hitsPerPage = Integer.parseInt(args[i+1]);
//                    082        if (hitsPerPage <= 0) {
//                        083          System.err.println("There must be at least 1 hit per page.");
//                        084          System.exit(1);
//                        085        }
//                    086        i++;
//                    087      }
//                088    }
//            089
//            090    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
//            091    IndexSearcher searcher = new IndexSearcher(reader);
//            092    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//            093
//            094    BufferedReader in = null;
//            095    if (queries != null) {
//                096      in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
//                097    } else {
//                098      in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
//                099    }
//            100    QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
//            101    while (true) {
//                102      if (queries == null && queryString == null) {                        // prompt the user
//                    103        System.out.println("Enter query: ");
//                    104      }
//                105
//                106      String line = queryString != null ? queryString : in.readLine();
//                107
//                108      if (line == null || line.length() == -1) {
//                    109        break;
//                    110      }
//                111
//                112      line = line.trim();
//                113      if (line.length() == 0) {
//                    114        break;
//                    115      }
//                116
//                117      Query query = parser.parse(line);
//                118      System.out.println("Searching for: " + query.toString(field));
//                119
//                120      if (repeat > 0) {                           // repeat & time as benchmark
//                    121        Date start = new Date();
//                    122        for (int i = 0; i < repeat; i++) {
//                        123          searcher.search(query, null, 100);
//                        124        }
//                    125        Date end = new Date();
//                    126        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
//                    127      }
//                128
//                129      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
//                130
//                131      if (queryString != null) {
//                    132        break;
//                    133      }
//                134    }
//            135    reader.close();
//            136  }
//        137
//                138  /**
//         139   * This demonstrates a typical paging search scenario, where the search engine presents
//         140   * pages of size n to the user. The user can then go to the next page if interested in
//         141   * the next hits.
//         142   *
//         143   * When the query is executed for the first time, then only enough results are collected
//         144   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
//         145   * is executed another time and all hits are collected.
//         146   *
//         147   */
//                148  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
//                                                       149                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
//            150
//            151    // Collect enough docs to show 5 pages
//            152    TopDocs results = searcher.search(query, 5 * hitsPerPage);
//            153    ScoreDoc[] hits = results.scoreDocs;
//            154
//            155    int numTotalHits = results.totalHits;
//            156    System.out.println(numTotalHits + " total matching documents");
//            157
//            158    int start = 0;
//            159    int end = Math.min(numTotalHits, hitsPerPage);
//            160
//            161    while (true) {
//                162      if (end > hits.length) {
//                    163        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
//                    164        System.out.println("Collect more (y/n) ?");
//                    165        String line = in.readLine();
//                    166        if (line.length() == 0 || line.charAt(0) == 'n') {
//                        167          break;
//                        168        }
//                    169
//                    170        hits = searcher.search(query, numTotalHits).scoreDocs;
//                    171      }
//                172
//                173      end = Math.min(hits.length, start + hitsPerPage);
//                174
//                175      for (int i = start; i < end; i++) {
//                    176        if (raw) {                              // output raw format
//                        177          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
//                        178          continue;
//                        179        }
//                    180
//                    181        Document doc = searcher.doc(hits[i].doc);
//                    182        String path = doc.get("path");
//                    183        if (path != null) {
//                        184          System.out.println((i+1) + ". " + path);
//                        185          String title = doc.get("title");
//                        186          if (title != null) {
//                            187            System.out.println("   Title: " + doc.get("title"));
//                            188          }
//                        189        } else {
//                        190          System.out.println((i+1) + ". " + "No path for this document");
//                        191        }
//                    192
//                    193      }
//                194
//                195      if (!interactive || end == 0) {
//                    196        break;
//                    197      }
//                198
//                199      if (numTotalHits >= end) {
//                    200        boolean quit = false;
//                    201        while (true) {
//                        202          System.out.print("Press ");
//                        203          if (start - hitsPerPage >= 0) {
//                            204            System.out.print("(p)revious page, ");
//                            205          }
//                        206          if (start + hitsPerPage < numTotalHits) {
//                            207            System.out.print("(n)ext page, ");
//                            208          }
//                        209          System.out.println("(q)uit or enter number to jump to a page.");
//                        210
//                        211          String line = in.readLine();
//                        212          if (line.length() == 0 || line.charAt(0)=='q') {
//                            213            quit = true;
//                            214            break;
//                            215          }
//                        216          if (line.charAt(0) == 'p') {
//                            217            start = Math.max(0, start - hitsPerPage);
//                            218            break;
//                            219          } else if (line.charAt(0) == 'n') {
//                            220            if (start + hitsPerPage < numTotalHits) {
//                                221              start+=hitsPerPage;
//                                222            }
//                            223            break;
//                            224          } else {
//                            225            int page = Integer.parseInt(line);
//                            226            if ((page - 1) * hitsPerPage < numTotalHits) {
//                                227              start = (page - 1) * hitsPerPage;
//                                228              break;
//                                229            } else {
//                                230              System.out.println("No such page");
//                                231            }
//                            232          }
//                        233        }
//                    234        if (quit) break;
//                    235        end = Math.min(numTotalHits, start + hitsPerPage);
//                    236      }
//                237    }
//            238  }
//        239}
//
//




}
