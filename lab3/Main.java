import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.function.Consumer;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class Ad implements Comparable<Ad>{
    private String id, category, content;
    private double bidValue, ctr;

    public Ad(String id, String category, String content, double bidValue, double ctr){
        this.id = id;
        this.category = category;
        this.content = content;
        this.bidValue = bidValue;
        this.ctr = ctr;
    }

    public String getCategory(){
        return category;
    }

    public String getContent(){
        return content;
    }

    @Override
    public String toString(){
        return id + " " + category + " (bid=" + bidValue + ",ctr=" + ctr + "% " + content;
    }

    public int compareTo(Ad b){
        if(Double.compare(this.bidValue, b.bidValue) == 0){
            return b.id.compareTo(this.id);
        }
        return Double.compare(this.bidValue, b.bidValue);
    }
}

class AdRequest{
    private String id, category, keywords;
    private double floorBid;

    public AdRequest(String id, String category, String keywords, double floorBid){
        this.id = id;
        this.category = category;
        this.keywords = keywords;
        this.floorBid = floorBid;
    }

    public String getCategory(){
        return category;
    }

    public String getKeywords(){
        return keywords;
    }

    @Override
    public String toString(){
        return id + " [" + category + "] (floor=" + floorBid + "): " + keywords;
    }

}

class AdNetwork {
    private ArrayList<Ad> ads;

    public AdNetwork(){
        ads = new ArrayList<Ad>();
    }

    public void readAds(BufferedReader br){
        Stream<String> stream = br.lines();
        Consumer<String> add = line -> {
            String[] a = line.split(" ");
            StringBuilder cnt = new StringBuilder();
            for(int i = 4;i < a.length;i++){
                cnt.append(a[i]);
                if(i + 1 != a.length){
                    cnt.append(" ");
                }
            }
            ads.add(new Ad(a[0], a[1], cnt.toString(), Double.valueOf(a[2]), Double.valueOf(a[3])));
        };
        stream.forEach(add);
    }

    public List<Ad> placeAds(BufferedReader is, int k, PrintWriter os){

    }

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
            String[] adWords = ad.getContent().toLowerCase().split("\\s+");
            String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
            for (String kw : keywords) {
                for (String aw : adWords) {
                    if (kw.equals(aw)) score++;
                }
        }
        return score;
    }
}

public class Main {
  public static void main(String[] args) throws IOException {
    AdNetwork network = new AdNetwork();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

    int k = Integer.parseInt(br.readLine().trim());

    if (k == 0) {
      network.readAds(br);
      network.placeAds(br, 1, pw);
    } else if (k == 1) {
      network.readAds(br);
      network.placeAds(br, 3, pw);
    } else {
      network.readAds(br);
      network.placeAds(br, 8, pw);
    }

    pw.flush();
  }
}
