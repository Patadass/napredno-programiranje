import java.util.*;

interface IFile{
    public String getFileName();
    public long getFileSize();
    public String getFileInfo(int k);
    public void sortBySize();
    public long findLargestFile();
}

class File implements IFile{
    private String name;
    private long size;

    public File(String name, long size){
        this.name = name;
        this.size = size;
    }

    public String getFileName(){
        return name;
    }
    
    public long getFileSize(){
        return size;
    }

    public String getFileInfo(int k){
        StringBuilder sb = new StringBuilder("");
        for(int i = 0;i < k;i++){
            sb.append("    ");
        }
        sb.append("File name: ");
        for(int i = 0;i < 10 - this.getFileName().length();i++){
            sb.append(" ");
        }
        sb.append(getFileName());
        sb.append(" File size: ");
        for(int i = 0;i < 10 - String.valueOf(getFileSize()).length();i++){
            sb.append(" ");
        }
        sb.append(getFileSize() + "\n");
        return sb.toString();
    }

    public long findLargestFile(){
        return 0;
    }

    public void sortBySize(){
        return;
    }
}

class SortBySize implements Comparator<IFile>{
    public int compare(IFile a, IFile b){
        return Long.compare(a.getFileSize(), b.getFileSize());
    }
}

class Folder implements IFile{
    private String name;
    private ArrayList<IFile> files;
    
    public Folder(String name){
        this.name = name;
        this.files = new ArrayList<IFile>();
    }

    public String getFileName(){
        return name;
    }

    public long getFileSize(){
        long size = 0;
        for(IFile file : files){
            size += file.getFileSize();
        }
        return size;
    }

    public String getFileInfo(int k){
        StringBuilder sb = new StringBuilder("");
        for(int i = 0;i < k;i++){
            sb.append("    ");
        }
        sb.append("Folder name: ");
        for(int i = 0;i < 10 - this.getFileName().length();i++){
            sb.append(" ");
        }
        sb.append(getFileName());
        sb.append(" Folder size: ");
        for(int i = 0;i < 10 - String.valueOf(getFileSize()).length();i++){
            sb.append(" ");
        }
        sb.append(getFileSize() + "\n");
        for(IFile f : files){
            sb.append(f.getFileInfo(k + 1));
        }
        return sb.toString();
    }

    public void sortBySize(){
        for(IFile f : files){
            if(f.getClass() == Folder.class){
                f.sortBySize();
            }
        }
        files.sort(new SortBySize());
    }

    public long findLargestFile(){
        long max = 0;
        for(IFile file : files){
            long s = 0;
            if(file.getClass() == Folder.class){
                s = file.findLargestFile();
            }else{
                s = file.getFileSize();
            }
            if(max < s){
                max = s;
            }
        }
        return max;
    }

    public void addFile(IFile file) throws FileNameExistsException{
        for(IFile f : files){
            if(f.getFileName().equals(file.getFileName())){
                throw new FileNameExistsException("There is already a file named " + file.getFileName() + " in the folder " + this.getFileName());
            }
        }
        files.add(file);
    }
}

class FileNameExistsException extends Exception{
    public FileNameExistsException(String msg){
        super(msg);
    }
}

class FileSystem{
    private Folder root;

    public FileSystem(){
        root = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException{
        root.addFile(file);
    }

    public long findLargestFile(){
        return root.findLargestFile();
    }

    public void sortBySize(){
        root.sortBySize();
    }

    @Override
    public String toString(){
        return root.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
