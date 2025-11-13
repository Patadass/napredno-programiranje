import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

class Time{
    public int hour, min;
    public String ampm;
    public TimeFormat format;


    public Time(int hour, int min){
        this.hour = hour;
        this.min = min;
        format = TimeFormat.FORMAT_24;
    }

    public Time(String s) throws InvalidTimeException{
        String in[] = null;
        if(s.contains(":")){
            in = s.split(":");
        }else if(s.contains(".")){
            in = s.split("\\.");
        }else{
            return;
        }
        this.hour = Integer.parseInt(in[0]);
        this.min = Integer.parseInt(in[1]);
        if(hour < 0 || hour > 23){
            throw new InvalidTimeException(s);
        }
        if(min < 0 || min > 59){
            throw new InvalidTimeException(s);
        }
        format = TimeFormat.FORMAT_24;
    }

    public void format(TimeFormat f){
        if(format == f){
            return;
        }
        if(f == TimeFormat.FORMAT_AMPM){
            if(hour <= 12){
                if(hour == 12){
                    ampm = "PM";
                }else{
                    ampm = "AM";
                }
                if(hour == 0){
                    hour = 12;
                }
            }else{
                ampm = "PM";
                hour -= 12;
            }
            format = TimeFormat.FORMAT_AMPM;
        }
        if(f == TimeFormat.FORMAT_24){
            if(ampm.equals("AM")){
                if(hour == 12){
                    hour = 0;
                }
            }else{
                if(hour != 12){
                    hour += 12;
                }
            }
            format = TimeFormat.FORMAT_24;
        }
    }

    @Override
    public String toString(){
        if(format == TimeFormat.FORMAT_24){
            return String.format("%2d:%02d", hour, min);
        }else{
            return String.format("%2d:%02d %s", hour, min, ampm);
        }
    }
}

class SortTime implements Comparator<Time>{
    public int compare(Time a, Time b){
        TimeFormat f = a.format;
        a.format(TimeFormat.FORMAT_24);
        b.format(TimeFormat.FORMAT_24);
        int res = 0;

        if(a.hour == b.hour){
            res = Integer.compare(a.min, b.min);
        }else{
            res = Integer.compare(a.hour, b.hour);
        }

        a.format(f);
        b.format(f);
        return res;

    }
}

class TimeTable{
    private ArrayList<Time> times;

    public TimeTable(){
        times = new ArrayList<Time>();
    }

    void readTimes(InputStream is) throws UnsupportedFormatException, InvalidTimeException{
        Scanner sc = new Scanner(is);
        while(sc.hasNextLine()){
            String[] in = sc.nextLine().split(" ");
            for(int i = 0;i < in.length;i++){
                if(!in[i].contains(".") && !in[i].contains(":")){
                    throw new UnsupportedFormatException(in[i]);
                }
                try{
                    Time ph = new Time(in[i]);
                    times.add(ph);
                }catch(InvalidTimeException e){
                    throw e;
                }
            }
        }
        sc.close();
    }

    void writeTimes(OutputStream os, TimeFormat f){
        times.sort(new SortTime());
        times.forEach((time) -> time.format(f));
        times.forEach((time) -> {
            try{
                os.write((time.toString() + "\n").getBytes());
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
        try{
            os.flush();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}

class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String msg){
        super(msg);
    }
}

class InvalidTimeException extends Exception{
    public InvalidTimeException(String msg){
        super(msg);
    }
}

public class TimesTest {

	public static void main(String[] args) {
		TimeTable timeTable = new TimeTable();
		try {
			timeTable.readTimes(System.in);
		} catch (UnsupportedFormatException e) {
			System.out.println("UnsupportedFormatException: " + e.getMessage());
		} catch (InvalidTimeException e) {
			System.out.println("InvalidTimeException: " + e.getMessage());
		}
		System.out.println("24 HOUR FORMAT");
		timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
		System.out.println("AM/PM FORMAT");
		timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
	}

}

enum TimeFormat {
	FORMAT_24, FORMAT_AMPM
}
