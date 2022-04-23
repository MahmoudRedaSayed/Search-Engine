//package RankerPackage.Ranker;
//import IndexerPackages.Indexer.PageParsing;
//import DataBasePackages.DataBase.DataBase;
//import QueryProcessingPackages.Query.QueryProcessing;
//
//import java.lang.reflect.Array;
//import java.util.*;
//import org.json.*;
//import java.io.*;
//import java.lang.Math.*;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//
//public class Ranker {
//    private DataBasePackages.DataBase.DataBase dataBaseObject = new DataBasePackages.DataBase.DataBase();
////    private PageParsing pageParsing = new PageParsing();
//    private QueryProcessing queryProcessingObject;
//
//    {
//        try {
//            queryProcessingObject = new QueryProcessing();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    JSONArray dividedQuery = new JSONArray();
//    String message;
//
//    //To Remove Duplicates from an array
//    public static int[] removeDuplicates(int[] arr) {
//        int end = arr.length;
//        for (int i = 0; i < end; i++) {
//            for (int j = i + 1; j < end; j++) {
//                if (arr[i] == arr[j]) {
//                    int shiftLeft = j;
//                    for (int k = j + 1; k < end; k++, shiftLeft++) {
//                        arr[shiftLeft] = arr[k];
//                    }
//                    end--;
//                    j--;
//                }
//            }
//        }
//
//        int[] whitelist = new int[end];
//        for (int i = 0; i < end; i++) {
//            whitelist[i] = arr[i];
//        }
//        return whitelist;                           //Array after remove Duplicates
//    }
//
//    //To Remove Duplicates from an array
//    public static int[] removeDuplicates(Integer[] arr) {
//        int end = arr.length;
//        for (int i = 0; i < end; i++) {
//            for (int j = i + 1; j < end; j++) {
//                if (arr[i] == arr[j]) {
//                    int shiftLeft = j;
//                    for (int k = j + 1; k < end; k++, shiftLeft++) {
//                        arr[shiftLeft] = arr[k];
//                    }
//                    end--;
//                    j--;
//                }
//            }
//        }
//
//        int[] whitelist = new int[end];
//        for (int i = 0; i < end; i++) {
//            whitelist[i] = arr[i];
//        }
//        return whitelist;                       //Array after remove Duplicates
//    }
//
//
//    //To calculate the Popularity between pages
//    public Map<Integer, Double> calculatePopularity(double totalNodes)             //Popularity
//    {
//        Map<Integer, Double> pagesRank1 = new HashMap<Integer, Double>();
//        Map<Integer, Double> TempPageRank = new HashMap<Integer, Double>();
//
//        double InitialPageRank = 1.0 / totalNodes;
//
//        // initialize the rank of each page //
//        for (int k = 1; k <= totalNodes; k++)
//            pagesRank1.put(k, InitialPageRank);
//
//
//        int ITERATION_STEP = 1;
//        while (ITERATION_STEP <= 2) {
//            // Store the PageRank for All Nodes in Temporary Array
//            for (int k = 1; k <= totalNodes; k++) {
//                TempPageRank.put(k, pagesRank1.get(k));
//                pagesRank1.put(k, 0.0);
//            }
//
//            double tempSum = 0;
//            for (int currentPage = 1; currentPage <= totalNodes; currentPage++) {
//                double OutgoingLinks = dataBaseObject.getParentLinksNum(currentPage);         //Get it from From ==> (Reda) to recieve the number of outgoing links from parent link
//                double temp = TempPageRank.get(dataBaseObject.getParentId(currentPage)) * (1.0 / OutgoingLinks);
//                pagesRank1.put(currentPage, temp);
//                tempSum += pagesRank1.get(currentPage);
//
//            }
//
//            //Special handling for the first page only as there is no outgoing links to it
//            double temp = 1 - tempSum;
//            pagesRank1.put(1, temp);
//            ITERATION_STEP++;
//        }
//
//        // Add the Damping Factor to PageRank
//        double DampingFactor = 0.75;
//        double temp = 0;
//        for (int k = 40; k <= 40 + totalNodes; k++) {
//            temp = (1 - DampingFactor) + DampingFactor * pagesRank1.get(k);
//            pagesRank1.put(k, temp);
//        }
//
//        return pagesRank1;
//    }
//
//    public static double log2(double num) {
//        return (Math.log(num) / Math.log(2));
//    }
//
//    //To calculate the Relevance ( tf-idf )
//    public Map<Integer, Double> calculateRelevance(ArrayList<String> tempLines) throws FileNotFoundException, JSONException {
//        Map<Integer, Double> pagesRank2 = new HashMap<Integer, Double>();
//        // Get it from (Mustafa) the length of the page
//        double tf = 0.0;
//        double idf = 0.0;
//        double tf_idf = 0.0;
//        double numOfOccerrencesInCurrentDocument = 0.0;
//        double numOfOccerrencesInAllDocuments = 0.0;
//        int count = 0;                                     //used for TF_IDF as it is an array of map
//
//        //getWordsResult from QueryProcessing (Waleed)  ==> Array contains all words of the search query after processing
//
//        //Not needed currently, sent by servlet
//        //ArrayList<String> tempLines = new ArrayList<String>();
//        // {"/experience|[41,t]:1;[41,h]:1;[41,p]:1;[42,h]:2;[42,h]:2;[43,h]:2;[45,h]:2;", "/encyclopedia|[40,t]:1;[42,t]:1;[42,t]:1;[43,h]:5;[44,s]:5;[44,p]:3;"};
//
//
//        //This is an Array of Map to store tf-idf of each word in each document
//        List<Map<Integer, Double>> TF_IDF = new ArrayList<Map<Integer, Double>>();
//        Map<Integer, Double> Ids_numOfOccurrences = new HashMap<Integer, Double>();
//
//        HashMap<Integer, int[]> uniqueIds = new HashMap<Integer, int[]>();
//
//        //experience|[41,t]:1;[41,h]:1;[41,p]:1;[42,h]:2;[42,h]:2;[43,h]:2;[45,h]:2;
//        //encyclopedia|[40,t]:1;[42,t]:1;[43,t]:1;[42,h]:5;[44,s]:5;[44,p]:3;
//
//        int counterForWords = 0;
//        for (int i = 0; i < tempLines.size(); i++) {
//            double coeff = 0.0;                                     //to make priority between title,header,paragraph
//            int startIndex = tempLines.get(i).indexOf('|');
//            String sub = tempLines.get(i).substring(startIndex + 1);
//            String[] stringSplits = sub.split(";");
//
//            //for word ==> experience
//            //After Splitting
//            // [41,t]:1
//            // [41,h]:1
//            // [41,p]:1
//            // [42,h]:2
//            // [42,h]:2
//            // [43,h]:2
//            // [45,h]:2
//
//            //for word ==> encyclopedia
//            // After splitting
//            // [40,t]:1
//            // [42,t]:1
//            // [43,t]:1
//            // [44,h]:5
//            // [44,s]:5
//            // [44,p]:3
//
//            int idOfPreviosPage = -1;
//            int arr[] = new int[stringSplits.length];
//
//            for (int j = 1; j <= stringSplits.length; j++) {
//                int charTempType22 = stringSplits[j - 1].indexOf('[');
//                String idOfCurrentPage = stringSplits[j - 1].substring(charTempType22 + 1, stringSplits[j - 1].indexOf(','));                //to get id of current page
//                arr[j - 1] = Integer.parseInt(idOfCurrentPage);
////                long lengthOfPage = pageParsing.getLengthOfPageContent(Integer.parseInt(idOfCurrentPage));   //try to get pageID
//                int charTempType = stringSplits[j - 1].indexOf(',');
//                char charTemp = stringSplits[j - 1].charAt(charTempType + 1);
//
//                if (charTemp == 't')                                   //title
//                    coeff = 1.0 / 2.0;
//                else if (charTemp == 'h' || charTemp == 's')         //header or strong
//                    coeff = 1.0 / 4.0;
//                else                                                    //paragraph
//                    coeff = 1.0 / 8.0;
//
//                //to get number of occurrences of each word
//                int countTempType = stringSplits[j - 1].indexOf(':');
//                String countTemp = stringSplits[j - 1].substring(countTempType + 1);
//                numOfOccerrencesInCurrentDocument += coeff * Double.parseDouble(countTemp);
//                numOfOccerrencesInAllDocuments += Integer.parseInt(countTemp);                      //total occurrences of a word in all documents
//
//                int charTempType55;
//                String idOfPreviousPage;                //to get id of current page
//                int charTempType33;
//                String idOfNextPage = "-1";                //to get id of current page
//
//                if (j == stringSplits.length) {
//                    charTempType55 = stringSplits[j - 2].indexOf('[');
//                    idOfPreviousPage = stringSplits[j - 2].substring(charTempType55 + 1, stringSplits[j - 2].indexOf(','));                //to get id of current page
//                } else {
//                    charTempType33 = stringSplits[j].indexOf('[');
//                    idOfNextPage = stringSplits[j].substring(charTempType33 + 1, stringSplits[j].indexOf(','));                //to get id of current page
//                }
//
//                int qw = Integer.parseInt(idOfNextPage);
//                int qe = Integer.parseInt(idOfCurrentPage);
//                if (qw != qe) {
////                    tf = Double.valueOf(numOfOccerrencesInCurrentDocument) / Double.valueOf(lengthOfPage);
//                    Ids_numOfOccurrences.put(Integer.parseInt(idOfCurrentPage), tf);          //put id and numOfOccerrencesInCurrentDocument into the map
//                    numOfOccerrencesInCurrentDocument = 0;
//                }
//                tf = 0;
//            }
//            int tempArr[] = removeDuplicates(arr);
//
//            uniqueIds.put(counterForWords, tempArr);
//            counterForWords++;
//
//            idf = 5100.0 / Double.valueOf(numOfOccerrencesInAllDocuments);                                      // 5100 ==> number of indexed web pages
//            Map<Integer, Double> tempMap = new HashMap<Integer, Double>();
//
//            for (int h = 0; h < tempArr.length; h++) {
//                tf_idf = idf * Ids_numOfOccurrences.get(tempArr[h]);
//                tempMap.put(tempArr[h], tf_idf);
//            }
//            TF_IDF.add(tempMap);
//        }
//
//        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
//        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
//        //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena
//
//        Map[] maps = TF_IDF.toArray(new HashMap[TF_IDF.size()]);
//
//
//        Map<Integer, Double> getpagesRank1 = new HashMap<Integer, Double>();
//        getpagesRank1 = this.calculatePopularity(dataBaseObject.getCompleteCount());
//
//        int lengthForArray = 0;
//        for (int i = 0; i < maps.length; i++)
//            lengthForArray += maps[i].size();
//
//        ArrayList<Integer> arr = new ArrayList<Integer>();
//        int counter55 = 0;
//        double temptf_idf = 0;                //Used for pageRank
//
//        for (int q = 0; q < tempLines.size(); q++) {
//
//            Map<Integer, Double> tempMap1 = new HashMap<Integer, Double>();
//            tempMap1 = maps[q];
//            int tempArr1[] = new int[uniqueIds.get(counter55).length];
//            tempArr1 = uniqueIds.get(counter55);
//
//            if (counter55 == tempLines.size() - 1) {
//                for (int y = 0; y < tempArr1.length; y++) {
//                    if (!pagesRank2.containsKey(tempArr1[y])) {
//                        pagesRank2.put(tempArr1[y], (0.7 * temptf_idf) + (0.3 * getpagesRank1.get(tempArr1[y])));              //summation of Relevance (tf_idf) and Popularity
//                        arr.add(tempArr1[y]);
//                    }
//                }
//            } else {
//                for (int k = 0; k < tempArr1.length; k++) {
//                    for (int j = 1; j < tempLines.size(); j++) {
//                        Map<Integer, Double> tempMap2 = new HashMap<Integer, Double>();
//                        tempMap2 = maps[j];
//                        int tempArr2[] = new int[uniqueIds.get(counter55 + 1).length];
//                        tempArr2 = uniqueIds.get(counter55 + 1);
//                        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
//                        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
//                        //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena
//
//                        //experience    ==>    41,42,43   ,45
//                        //encyclopedia  ==> 40,   42,43,44
//
//                        for (int u = 0; u < tempArr2.length; u++) {
//                            if (tempArr1[k] == tempArr2[u]) {
//                                temptf_idf += tempMap1.get(tempArr1[k]) + tempMap2.get(tempArr2[u]);
//                            } else if (tempArr1[k] < tempArr2[u]) {
//                                break;
//                            }
//                        }
//                    }
//                    pagesRank2.put(tempArr1[k], (0.7 * temptf_idf) + (0.3 * getpagesRank1.get(tempArr1[k])));              //summation of Relevance (tf_idf) and Popularity
//                    arr.add(tempArr1[k]);
//                }
//                counter55++;
//            }
//        }
//
//        Integer[] array = arr.toArray(new Integer[0]);
//        int tempArray[] = removeDuplicates(array);
//
//        for (int t = 0; t < tempArray.length; t++) {
//            System.out.println(tempArray[t]);
//            System.out.println(pagesRank2.get(tempArray[t]));
//        }
//        return pagesRank2;
//    }
//}