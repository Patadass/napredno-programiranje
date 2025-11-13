import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;

class Archive{
    public int id;
    public LocalDate dateArchived;

    public Archive(){}
    public Archive(int id){
        this.id = id;
    }

    public Archive(int id, LocalDate date){
        this.id = id;
        this.dateArchived = date;
    }

    public void setDateArchived(LocalDate dateArchived){
        this.dateArchived = dateArchived;
    }

    public boolean canOpen(LocalDate date){
        return false;
    }
    public boolean canOpen(){
        return false;
    }
    public void lower(){
        return;
    }
}

class LockedArchive extends Archive{
    public LocalDate dateToOpen;
    
    public LockedArchive(int id, LocalDate dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public boolean canOpen(LocalDate date){
        if(dateToOpen.compareTo(date) == 1){
            return false;
        }
        return true;
    }
}

class SpecialArchive extends Archive{
    public int maxOpen;
    public int amntOpen;

    public SpecialArchive(int id, int maxOpen){
        super(id);
        this.maxOpen = maxOpen;
        this.amntOpen = 0;
    }

    @Override
    public boolean canOpen(){
        return amntOpen < maxOpen;
    }

    @Override
    public void lower(){
        amntOpen++;
    }
}

class ArchiveStore{
    ArrayList<Archive> archives;
    StringBuilder sb;

    public ArchiveStore(){
        archives = new ArrayList<Archive>();
        sb = new StringBuilder();
    }
    
    public void archiveItem(Archive item, LocalDate date){
        item.setDateArchived(date);
        archives.add(item);
        sb.append(String.format("Item %d archived at %s\n", item.id, item.dateArchived.toString()));
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException{
        Archive toOpen = null;
        for(Archive a : archives){
            if(a.id == id){
                toOpen = a;
                break;
            }
        }
        if(toOpen == null){ throw new NonExistingItemException(id); }

        if(toOpen.getClass() == SpecialArchive.class){
            SpecialArchive s = (SpecialArchive) toOpen;
            if(!s.canOpen()){
                sb.append(String.format("Item %d cannot be opened more than %d times\n", s.id, s.maxOpen));
                return;
            }else{
                s.lower();
            }
        }
        if(toOpen.getClass() == LockedArchive.class){
            LockedArchive s = (LockedArchive) toOpen;
            if(!s.canOpen(date)){
                sb.append(String.format("Item %d cannot be opened before %s\n", s.id, s.dateToOpen.toString()));
                return;
            }
        }
        
        sb.append(String.format("Item %d opened at %s\n", toOpen.id, date));
    }

    public String getLog(){
        return sb.toString();
    }
}

class NonExistingItemException extends Exception{
    public NonExistingItemException(int id){
        super(String.format("Item with id %d doesn't exist", id));
    }
}

public class ArchiveStoreTest {
	public static void main(String[] args) {
		ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
		Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
		int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
		int i;
		for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
			long days = scanner.nextLong();
            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
			LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
			store.archiveItem(lockedArchive, date);
		}
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
		for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
			int maxOpen = scanner.nextInt();
			SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
		}
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
			int open = scanner.nextInt();
            try {
            	store.openItem(open, date);
            } catch(NonExistingItemException e) {
            	System.out.println(e.getMessage());
            }
        }
		System.out.println(store.getLog());
	}
}
