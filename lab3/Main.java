import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.function.Predicate;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class Ad implements Comparable<Ad>{
    private String id, category, content;
    private double bidValue, ctr;
    private double totalScore;

    public Ad(String id, String category, String content, double bidValue, double ctr){
        this.id = id;
        this.category = category;
        this.content = content;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.totalScore = 0;
    }

    public String getId(){
        return id;
    }

    public String getCategory(){
        return category;
    }

    public String getContent(){
        return content;
    }

    public double getBid(){
        return bidValue;
    }
    public double getCtr(){
        return ctr;
    }

    public void setTotalScore(double d){
        this.totalScore = d;
    }

    public double getTotalScore(){
        return totalScore;
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

class SortByTotalScore implements Comparator<Ad>{
    public int compare(Ad a, Ad b){
        return Double.compare(b.getTotalScore(), a.getTotalScore());
    }
}

class SortByBid implements Comparator<Ad>{
    public int compare(Ad a, Ad b){
        return Double.compare(b.getBid(), a.getBid());
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

    public double getFloor(){
        return floorBid;
    }

    public String getId(){
        return id;
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
        try{
            while(br.ready()){
                String line = br.readLine();
                String[] a = line.split(" ");
                if(a.length <= 4){
                    break;
                }
                StringBuilder cnt = new StringBuilder();
                for(int i = 4;i < a.length;i++){
                    cnt.append(a[i]);
                    if(i + 1 != a.length){
                        cnt.append(" ");
                    }
                }
                ads.add(new Ad(a[0], a[1], cnt.toString(), Double.valueOf(a[2]), Double.valueOf(a[3])));
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Ad> placeAds(BufferedReader is, int k, PrintWriter os){
        String[] in = null;
        try{
            in = is.readLine().split(" ");
        }catch(IOException e){
            System.out.println(e.getMessage());
            return null;
        }
        String id = in[0];
        String cat = in[1];
        double floor_bid = Double.valueOf(in[2]);
        StringBuilder key_words = new StringBuilder("");
        for(int i = 3;i < in.length;i++){
            key_words.append(in[i]);
            if(i + 1 < in.length){
                key_words.append(" ");
            }
        }
        AdRequest adRequest = new AdRequest(id, cat, key_words.toString(), floor_bid);
        Predicate<Ad> hasLowerBid = ad -> (adRequest.getFloor() > ad.getBid());
        Consumer<Ad> calcTotalSocre = ad -> ad.setTotalScore(relevanceScore(ad, adRequest) + (5.0 * ad.getBid()) + (100.0 * ad.getCtr()));
        ads.removeIf(hasLowerBid);
        ads.forEach(calcTotalSocre);
        ads.sort(new SortByTotalScore());
        List<Ad> top = new ArrayList<Ad>();
        for(int i = 0;i < k;i++){
            top.add(this.ads.get(i));
        }
        top.sort(new SortByBid());
        os.print("Top ads for request " + adRequest.getId() + ":\n");
        for(Ad ad : top){
            os.print(ad.getId() + " " + ad.getCategory() + " (bid=" + String.format("%.2f", ad.getBid()) + ", ctr=" + String.format("%.2f", ad.getCtr() * 100) + "%) " + ad.getContent() + "\n");
        }

        return top;
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
