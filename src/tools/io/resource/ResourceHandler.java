/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modified) versions of this file    *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools.io.resource;


// Java imports
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


// Tools imports
import tools.Var;


/**TODO
 * Class for handling resources which are in the source folder. <br>
 * After adding them to the jar, they cannot be accessed easily.
 * This tool class is made for easy access of these files during development and deployment.
 * 
 * TODO:
 * Implement walk file tree.
 * 
 * @author Kaj Wortel
 */
public final class ResourceHandler {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The resource type of the {@code tools} project. */
    public static final ResourceType PROJECT_RESOURCE_TYPE
            = getResourceType(ResourceHandler.class);
    
    /** The map containing all file systems created via this handler. */
    private static final Map<URI, FileSystem> FILE_SYSTEMS = new HashMap<>();
    
    /** The file system to use for this project. */
    public static final FileSystem PROJECT_FILE_SYSTEM = initProjectFileSystem();
    
    /** The default file system to use. */
    private static FileSystem defaultFileSystem = PROJECT_FILE_SYSTEM;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    
    
    /* -------------------------------------------------------------------------
     * Inner classes.
     * -------------------------------------------------------------------------
     */
    /**
     * The types of origin of resources.
     * <table border="1">
     *   <tr>
     *     <td> {@link ResourceType#LOCAL_CLASS} </td>
     *     <td> If the class was loaded from a class file. </td>
     *   </tr>
     *   <tr>
     *     <td> {@link ResourceType#JAR} </td>
     *     <td> If the class was loaded from a jar file. </td>
     *   </tr>
     * </table>
     */
    public static enum ResourceType {
        /** The project is invoked by calling a class. */
        LOCAL_CLASS,
        /** The project is invoked by calling a jar. */
        JAR
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a static singleton class. No instances should be made.
     * 
     * @deprecated No instances should be made.
     */
    @Deprecated
    private ResourceHandler() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Determines the resource type of the given class. <br>
     * Return values:
     * 
     * @param c The class to check.
     * 
     * @return The resource type of the corresponding class.
     * 
     * @see ResourceType
     */
    public static ResourceType getResourceType(Class<?> c) {
        boolean isJar;
        try {
            isJar = new File(c.getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath()).isFile();
            
        } catch (URISyntaxException e) {
            System.err.println(e);
            System.exit(-1);
            isJar = false;
        }
        
        return (isJar ? ResourceType.JAR : ResourceType.LOCAL_CLASS);
        
        //FileTools.class.getClassLoader().
        /*
        String fStr = FileTools.class.getClassLoader().getResource("img/black_square.png").getFile();
        File file = new File(fStr);
        System.out.println("fStr: " + fStr);
        System.out.println("file: " + file);
        try (InputStream is = new FileInputStream(file)) {
            is.transferTo(System.out);
        }*//*
        try (InputStream is = FileTools.class.getClassLoader().getResourceAsStream("img/black_square.png")) {
            is.transferTo(System.out);
        }
        System.out.println();
        JarFile jf = new JarFile(FileTools.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        try (InputStream is = jf.getInputStream(jf.getJarEntry("img/black_square.png"))) {
            is.transferTo(System.out);
        }
        
        *//*
        try {
            CodeSource src = FileTools.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry e;
                while ((e = zip.getNextEntry()) != null) {
                    System.out.println(e.getName());
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }*/
    }
    
    private static FileSystem initProjectFileSystem() {
        try {
            return getFileSystem(PROJECT_RESOURCE_TYPE, ResourceHandler.class);
            
        } catch (URISyntaxException | IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
        return null;
    }
    
    private static FileSystem getFileSystem(final ResourceType type, final Class<?> c)
            throws URISyntaxException, IOException {
        /*
        try {
            Map<String, ?> xMap = new HashMap();
            URI uri = URI.create("jar:file:" + c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            System.out.println(uri);
            System.out.println(FileSystems.newFileSystem(uri, xMap));
            System.out.println(FileSystems.getFileSystem(uri));
            
        } catch (Exception e) {
            System.err.println(e);
        }
        System.exit(0);
        /**/
        if (type == ResourceType.LOCAL_CLASS) {
            return FileSystems.getDefault();
            
        } else if (type == ResourceType.JAR) {
            Map<String, ?> env = new HashMap<>();
            URI uri = URI.create("jar:file:" + c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            FileSystem fs = FILE_SYSTEMS.get(uri);
            if (fs != null) return fs;
            fs = FileSystems.newFileSystem(uri, env);
            FILE_SYSTEMS.put(uri, fs);
            return fs;
        }
        return null;
    }
    
    
    public static void main(String[] args)
            throws Exception {
        //System.out.println(PROJECT_RESOURCE_TYPE);
        //System.out.println(PROJECT_FILE_SYSTEM);
        //System.out.println(FileTools.getFileList(new File(Var.WORKING_DIR + "font" + Var.FS), true).toString().replaceAll(", ", "\n"));
        String file = "font/";
        Path p;
        if (PROJECT_RESOURCE_TYPE == ResourceType.JAR) {
            p = defaultFileSystem.getPath(file);
        } else {
            p = FileSystems.getDefault().getPath(Var.WORKING_DIR + file);
        }
        Iterator<Path> it = Files.walk(p).iterator();
        while (it.hasNext()) {
            Path path = it.next();
        }
    }
    
    
    
}
