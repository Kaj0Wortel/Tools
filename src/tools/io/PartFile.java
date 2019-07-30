
package tools.io;


// Java imports
import java.io.File;


// Tools imports.
import tools.Var;


/**
 * Extends the {@link File} class by supporting partial files. <br>
 * This includes initializing and getting a basis directory and
 * a file path relative to that directory.
 * 
 * @author Kaj Wortel
 */
public class PartFile
        extends File {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The starting directory file. */
    private final File dir;
    /** The file path relative to the directory file. */
    private final String filePath;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new {@code PartFile} with the given starting directory
     * and relative part file.
     * 
     * @param dir The name of the starting directory.
     * @param filePath The file path relative to the directory.
     */
    public PartFile(String dirName, String filePath) {
        super(dirName, filePath);
        this.dir = new File(dirName);
        this.filePath = filePath;
    }
    
    /**
     * Creates a new {@code PartFile} with the given starting directory
     * file and relative part file.
     * 
     * @param dir The starting directory.
     * @param filePath The file path relative to the directory.
     */
    public PartFile(File dir, String filePath) {
        super((dir == null ? "" : dir.getPath() + Var.FS) + filePath);
        this.dir = dir;
        this.filePath = filePath;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The directory of the file part.
     */
    public File getDir() {
        return dir;
    }
    
    /**
     * @return The string representation of the directory part of the file.
     */
    public String getDirName() {
        return dir.toString();
    }
    
    /**
     * @return The relative file path of the file part.
     */
    public File getRelativeFile() {
        return new File(filePath);
    }
    
    /**
     * @return The string representation of the relative file path of the file part.
     */
    public String getRelativeFilePath() {
        return filePath;
    }
    
    
}
