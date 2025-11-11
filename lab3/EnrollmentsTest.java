import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Faculty{
    private String shortName;
    private List<String> appropriateSubjects;
    private List<StudyProgramme> studyProgrammes;

    public Faculty(String s){
        this.shortName = s;
    }

    public List<String> getaps(){
        return appropriateSubjects;
    }

    public void addSubject(String name){
        appropriateSubjects.add(name);
    }

    public void addStudyProgramme(StudyProgramme p){
        studyProgrammes.add(p);
    }
}

class StudyProgramme{
    private String code, name;
    private int numPublicQuota, numPrivateQuota;
    private int enrolledInPublicQuota, enrolledInPrivateQuota;
    private List<Aplicant> aplicants;
    private List<String> aps;
    
    public StudyProgramme(String code, String name, Faculty f, int p, int pr){
        this.code = code;
        this.name = name;
        this.numPublicQuota = p;
        this.numPrivateQuota = pr;
        aps = f.getaps();
    }

    public void addAplicant(Aplicant a){
        aplicants.add(a);
    }

    public void calculateEnrollmentNumbers(){
        List<String> appSubs = aps;
        aplicants.sort(new SortByGpa(appSubs));

        int numOfApps = aplicants.size();
        if(numOfApps <= numPublicQuota){
            this.enrolledInPublicQuota = numOfApps;
            numOfApps = 0;
        }else{
            this.enrolledInPublicQuota = numPublicQuota;
            numOfApps = numOfApps - numPublicQuota;
        }

        if(numOfApps <= numPrivateQuota){
            this.enrolledInPrivateQuota = numOfApps;
            numOfApps = 0;
        }else{
            this.enrolledInPrivateQuota = numPublicQuota;
            numOfApps = numOfApps - numPrivateQuota;
        }

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Name:");
        sb.append(this.name + "\n");
        sb.append("Public Quota:\n");
        for(int i = 0;i < enrolledInPublicQuota;i++){
            sb.append(aplicants.get(i) + "\n");
        }
        sb.append("Private Quota:\n");
        for(int i = enrolledInPublicQuota;i < enrolledInPrivateQuota;i++){
            sb.append(aplicants.get(i) + "\n");
        }
        sb.append("Rejected:\n");
        for(int i = enrolledInPrivateQuota;i < aplicants.size();i++){
            sb.append(aplicants.get(i) + "\n");
        }
        return sb.toString();
    }
}

class SortByGpa implements Comparator<Aplicant>{
    private List<String> s;
    public SortByGpa(List<String> s){
        this.s = s;
    }
    public int compare(Aplicant a, Aplicant b){
        return Double.compare(a.calculatePoints(this.s), b.calculatePoints(this.s));
    }
}

class Aplicant{
    private int id;
    private String name;
    private double gpa;
    private double p;
    private List<SubjectWithGrade> subjectWithGrade;
    private StudyProgramme sturdyProgramme;

    public Aplicant(int id, String name, StudyProgramme studyProgramme, int gpa){
        this.id = id;
        this.name = name;
        this.sturdyProgramme = studyProgramme;
        this.sturdyProgramme.addAplicant(this);
        this.gpa = gpa;
    }

    public double getGpa(){
        return gpa;
    }

    public void addSubjectAndGrade(String subject, int grade){
        subjectWithGrade.add(new SubjectWithGrade(subject, grade));
    }

    public double calculatePoints(List<String> aps){
        double points = 0;
        for(SubjectWithGrade swg : subjectWithGrade){
            if(aps.contains(swg.getSubject())){
                points += swg.getGrade() * 2;
            }else{
                points += (double)swg.getGrade() * 1.2;
            }
        }
        this.p = points;
        return (points/aps.size()) * 12;
    }

    @Override
    public String toString(){
        return "Id: " + id + ", Name: " + name + ", GPA: " + gpa + " - " + p;
    }
}

class SubjectWithGrade
{
    private String subject;
    private int grade;
    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public int getGrade() {
        return grade;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {
    }

    public static void readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) {
    }
}

public class EnrollmentsTest {

    public static void main(String[] args) {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}
