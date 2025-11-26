import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Scanner;

class TermFrequency{
    Map<String, Integer> map;

    public TermFrequency(InputStream in, String[] stopWords){
        String[] text;
        Set<String> noWrods;
        map = new TreeMap<String, Integer>();
        Scanner sc = new Scanner(in);
        StringBuilder sb = new StringBuilder();
        while(sc.hasNext()){
            String line = sc.nextLine().toLowerCase().replaceAll("[,\\.\\-()„“]", " ");
            sb.append(line).append(" ");
        }
        sc.close();
        noWrods = new TreeSet<String>();
        for(String word: stopWords){
            noWrods.add(word);
        }
        text = sb.toString().split(" ");
        noWrods.add("");
        noWrods.add(" ");
        for(String word: text){
            if(noWrods.contains(word)){
                continue;
            }
            if(!map.containsKey(word)){
                map.put(word, 1);
                continue;
            }
            map.put(word, map.get(word) + 1);
        }
    }

    public Integer countTotal(){
        Integer sum = 0;
        for(Integer a : map.values()){
            sum += a;
        }
        return sum;
    }

    public Integer countDistinct(){
        return map.size();
    }

    public List<String> mostOften(int k){
        return valueSort(map).keySet().stream().collect(Collectors.toList()).subList(0, k);
    }

    public Map<String, Integer> valueSort(Map<String, Integer> map)
        {
            Comparator<String> valueComparator = new Comparator<String>() {
              
                      public int compare(String k1, String k2)
                      {
                          int comp = map.get(k2).compareTo(
                              map.get(k1));
                          if (comp == 0)
                              return 1;
                          else
                              return comp;
                      }
              
                  };
            Map<String, Integer> sorted = new TreeMap<String, Integer>(valueComparator);
          
            sorted.putAll(map);
          
            return sorted;
        }
}

public class TermFrequencyTest {
	public static void main(String[] args) throws FileNotFoundException {
		String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
				"ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
				"што", "на", "а", "но", "кој", "ја" };
		TermFrequency tf = new TermFrequency(System.in,
				stop);
		System.out.println(tf.countTotal());
		System.out.println(tf.countDistinct());
		System.out.println(tf.mostOften(10));
	}
}
// vasiot kod ovde
