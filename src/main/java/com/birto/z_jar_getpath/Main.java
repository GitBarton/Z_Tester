package com.birto.z_jar_getpath;

import static com.birto.z_jar_getpath.Main.fileSeparator;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Main {
    
  static  String  fileSeparator = System.getProperty("file.separator");
 final static String  BASEDOCSINIT = "baseDocsInit";
 final static String  ACTIVEDOCS = "activeDocs";
 final static String  JOURNAL = "journal";
 final static String  INDEX  = "index"; 
 static String jarPath;
    
    
    
    public static void main(String[] args) throws URISyntaxException, IOException {
 
  //Map<String, String> foldertoAdd = new HashMap<String, String>();
  //foldertoAdd.put(ACTIVEDOCS, jarPath + fileSeparator + ACTIVEDOCS + fileSeparator);
  //foldertoAdd.put(INDEX, jarPath + fileSeparator + INDEX + fileSeparator);
  //foldertoAdd.put(JOURNAL, jarPath + fileSeparator + JOURNAL + fileSeparator);
  //Debug  //     JOptionPane.showMessageDialog(null, foldertoAdd.get(ACTIVEDOCS) + foldertoAdd.get(INDEX) + foldertoAdd.get(JOURNAL), "activeDocs +  index + journal", 1);       
        

//JarPath
    	jarPath = getJarLaunchPath();   /*file:/C:/Java/Z_Jar_getPath/target/Z_Jar_getPath-1.0-jar-with-dependencies.jar */    
 
 
 	//copyDirectory(Main.class.getClassLoader().getResource("baseDocsInit/").getPath(), "/");
    	File fileDestination = new File("./activeDocs/");
    	URL url = new URL("jar:file:" + jarPath.toString() + "/Z_Jar_getPath-1.0-jar-with-dependencies.jar!/baseDocsInit/");
    	JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
    	
    	FileUtils.copyJarResourcesRecursively(fileDestination, jarConnection);
    //copyDirectory(jarPath.toString() + "/Z_Jar_getPath-1.0-jar-with-dependencies.jar/baseDocsInit", "./activeDocs/");
 
 System.out.println(jarPath.toString()  );
 System.out.println("our separator is : " + fileSeparator   );  //     \
 

        
//Check the folder outside the JAR exists and need to created: 
        //ActiveDocs
        if (!checkfolderExist(ACTIVEDOCS)) // If not existant create it and copy else break. 
        {   createDir(ACTIVEDOCS);
            copyBaseDocstoActive("baseDocsInit");  //copy the docs from BaseDocsInit while at it....
        }

        //Index   
        if (!checkfolderExist(INDEX)) // If not existant create it and copy else break. 
        {  createDir(INDEX);
        }

        //journal        
        if (!checkfolderExist(JOURNAL)) // If not existant create it and copy else break. 
        {   createDir(JOURNAL);
        }
     

    }      

    
 public static boolean checkfolderExist (String folderRelPath ) {
            
      File directory = new File(folderRelPath + fileSeparator );
      boolean alreadyExist = directory.exists();       System.out.println("In method checkfolderExist with folderPath to check existen = "+ directory);
      
      return alreadyExist; 
 }
 
    public static void createDir(String dirName) {

        File directory = new File(dirName + fileSeparator);
        if (!directory.exists()) {
            directory.mkdir();
            
            boolean existsDir = Files.isDirectory(directory.toPath());  
            System.out.println("--------------------------------------------------------------------------------------------- ");
            System.out.println("Result from test - if directory test dirName Path Exist = "+ dirName + existsDir);
            System.out.println("--------------------------------------------------------------------------------------------- ");
        }
    }
    

    public static void copyBaseDocstoActive(String fileName) throws URISyntaxException {
    
        CodeSource src = Main.class.getProtectionDomain().getCodeSource();

        if (src != null) {
            ZipInputStream zip = null;
            try {
                URL jar = src.getLocation();
                
         
                
                System.out.println("Printing Codesource (url jar) location" + jar);
                zip = new ZipInputStream(jar.openStream());
               
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    String name = e.getName();       //System.out.println("name: " + name + "  fileName + /: " + fileName + fileSeparator);

                    if (name.equals(fileName + "/")) { //we have found baseDocsInit !!!

                        URL baseDocsInit = Main.class.getClassLoader().getResource("baseDocsInit/");
                     
                        System.out.println("baseDocsInit to path" + baseDocsInit.getPath());
                        System.out.println("baseDocsInit to String " + baseDocsInit.toString());

             //HERE IS THE ISSUE ***********                                                   
                      copyFromJar( baseDocsInit.toString() ,  Paths.get("/activeDocs/"));                      
                              
          //            copyDirectory(fileName + fileSeparator, ACTIVEDOCS + fileSeparator);

                    }
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    zip.close();
                }
                catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else {
            System.out.println("ERROR in COPYBASEDOCSTOACTIVEDOCS");
        }    
    }
    
    
    //copyFromJar("/path/to/the/template/in/jar", Paths.get("/tmp/from-jar"))
    public static void copyFromJar(String source, final Path target) throws URISyntaxException, IOException {
    	System.out.println(source);
    	System.out.println(target);
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	
    	System.out.println(Main.class.getClassLoader().getResource("/activeDocs/").toURI());
    	
    	
    	System.out.println(Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath());
    	String jjarpath = Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
    	
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    URI resource = Main.class.getClassLoader().getResource(jjarpath + '!' + target).toURI();
    
    
    
    FileSystem fileSystem = FileSystems.newFileSystem(
            resource,
            Collections.<String, String>emptyMap()
    );

    final Path jarPath = fileSystem.getPath(source);

    Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {

        private Path currentTarget;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        	System.out.println(jarPath.relativize(dir).toString());
        	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            currentTarget = target.resolve(jarPath.relativize(dir).toString());
            Files.createDirectories(currentTarget);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }

    });
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     public static String getJarLaunchPath() throws UnsupportedEncodingException {
      URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
      String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
      String parentPath = new File(jarPath).getParentFile().getPath();
      return parentPath;
   }
    
    
    
    public static Path stringToPath(String str) throws URISyntaxException {

//Util to convert from String to Path for the FSDIrectory.Open
        URI uri = null;
        try {
            uri = Main.class.getClassLoader().getResource(str).toURI();
        }
        catch (URISyntaxException ex) {
            System.out.println("uri:" + uri);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Paths.get(uri);    //Convert the string to a Path
    }

    
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) 
    		  throws IOException {
	    Files.walk(Paths.get(sourceDirectoryLocation))
	      .forEach(source -> {
	          Path destination = Paths.get(destinationDirectoryLocation, source.toString()
	            .substring(sourceDirectoryLocation.length()));
	          try {
	              Files.copy(source, destination);
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	      });
	}

    
    
    
    
    
    
    
    
    
    
    
    
    /*       
       //Make a directory:
       String directoryName = PATH.concat("fakeTest");
       
              
    File directory = new File(directoryName);
    if (! directory.exists()){
        directory.mkdir();
        // If you require it to make the entire directory path including parents,
        // use directory.mkdirs(); here instead.
         
        System.out.println("absoPath:"+directory.getAbsolutePath());
       
        String pathU_Dir = System.getProperty("user.dir");      System.out.println("User.Dir = " + pathU_Dir);
            
        
        
        
        //ActiveDocs
        String pathActiveDocs = "/activeDocs";  //String        
        Path path = Paths.get(pathActiveDocs); // Path
                      
                                
        //Creer Path avec String et tester existence...      
        boolean ExistfullStrActive = Files.isDirectory( path   );                    
        System.out.println("ExistfullStrActive (true?) "+ExistfullStrActive);
  
        
        try {
            //Get the path or String to Basedocs
            
            
            //  copierBaseDocstoActiveDocs( , sessionControlleur.session.getpathBaseDocs);
            //   Path baseDocsPath =  stringToPath("baseDocs");
            
            
            System.out.println("will print from  stringToPath (\"baseDocsInit\").toString()  " +  stringToPath (BASEDOCSINIT).toString() );
            System.out.println( "tO dest: " + pathActiveDocs);
            
            copyDirectory ( stringToPath (BASEDOCSINIT).toString()  , pathActiveDocs );
            
            
            //  FileUtils.copyDirectory( new File(baseDocsPath)  ,  new File(path) );
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            
        }
                  
        // Convert files to path and psath to file? 
        //Test PAths Nows
        System.out.println("User.Dir = " + pathU_Dir);
               
//Run from Jar what is the result?       
        File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    
         System.out.println("jarFile.topath"+ jarFile.getPath() );        
        
        if (jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(pathActiveDocs + "/")) { //filter according to the path                    System.out.println(name+ " OHOHHO fsdhksjdhfskdhskdfskjdhskdfhkdjhsk");
                    
                }
            }
            jar.close();
        }

    }
 
    */
    
    
    
    
    
 }