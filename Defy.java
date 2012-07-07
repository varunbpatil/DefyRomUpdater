import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;   // only specifying java.util.* won't work

class Defy {

    public static void main(String[] args) throws MalformedURLException, IOException {

        URL url = new URL("http://www.defy.wdscript.fr/defy-cm9/");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null, current = null, previous = null;
        int content;
        Pattern pattern = Pattern.compile("<a href=\"(.*.zip)\">");
        Matcher matcher;

        /* at the end of this loop, "current" will have the filename of the latest version of rom */
        while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                while(matcher.find()) current = matcher.group(1);
        }
        br.close();

        /* read the file name of the most recently downloaded rom from "Defy.txt" */
        br = new BufferedReader(new FileReader("defy.txt"));
        previous = br.readLine();
        br.close();

        /* if the current and the most recent names of rom's are same, nothing to do */
        if(current.equals(previous)) 
            System.out.println("\nCongratulations!!! You already have the latest version of CM9-ICS.\n");

        else{
            /* read from remote source and write to local file */
            url = new URL("http://www.defy.wdscript.fr/defy-cm9/" + current);

            /* note the use of Stream classes rather that Character classes to handle binary files */
            /* note that InputStream and OutputStream are abstract classes and cannot be instantiated */
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(current);
            byte[] buffer = new byte[8192]; // This acts as a sort of buffer to speed up download over the network
            /* BufferedInputStream and BufferedOutputStream also won't be as fast as the "buffer" created above */

            long sizeSoFar = 0;
            double sizeSoFarMB = 0;
            long totalSize = (long)url.openConnection().getContentLength();
            double totalSizeMB = (double)totalSize/1048576;
            double percentComplete = 0;
            System.out.println("");

            while((content = is.read(buffer)) != -1){
                /* "content" stores the number of bytes read into "buffer" */
                if (content > 0) os.write(buffer, 0, content);
                sizeSoFar += content;
                sizeSoFarMB = ((double)sizeSoFar)/1048576;
                percentComplete = ((double)sizeSoFar/totalSize) * 100;
                System.out.format("\rDownloading the latest version "+current+" [%.2f/%.2f MB] [%.2f%% complete]",
                    sizeSoFarMB, totalSizeMB, percentComplete);
            }
            System.out.println("\nDownload Complete !!!\n");
            is.close();
            os.close();

            /* update the most recent downloaded version in "Defy.txt" */
            FileWriter fw = new FileWriter("defy.txt");
            fw.write(current);
            fw.close();
        }
    }
}
