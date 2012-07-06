import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

class Defy {

    public static void main(String[] args) throws MalformedURLException, IOException {

        URL url = new URL("http://www.defy.wdscript.fr/defy-cm9/");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null, current = null, previous = null, content = null;
        Pattern pattern = Pattern.compile("<a href=\"(.*.zip)\">");
        Matcher matcher;

        while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                while(matcher.find()) current = matcher.group(1);
        }
        br.close();

        /* read the file name of the most recently downloaded file */
        br = new BufferedReader(new FileReader("defy.txt"));
        previous = br.readLine();
        br.close();

        /* if the current and the most recent file names are same, nothing to do */
        if(current.equals(previous)) 
            System.out.println("\nCongratulations!!! You already have the latest version of CM9-ICS.\n");

        else{
            /* read from remote source and write to local file */
            url = new URL("http://www.defy.wdscript.fr/defy-cm9/" + current);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            FileWriter fw = new FileWriter(current);
            long sizeSoFar = 0;
            double sizeSoFarMB = 0;
            long totalSize = (long)url.openConnection().getContentLength();
            double totalSizeMB = (double)totalSize/1048576;
            double percentComplete = 0;
            System.out.println("");
            while((content = br.readLine()) != null){
                sizeSoFar += (long)content.length();
                sizeSoFarMB = ((double)sizeSoFar * 2)/1048576;
                percentComplete = ((double)sizeSoFar/totalSize) * 100;
                System.out.format("\rDownloading the latest version "+current+" [%.1f/%.1f MB] [%.2f%% complete]",
                    sizeSoFarMB, totalSizeMB, percentComplete);
                fw.write(content);
            }
            System.out.println("\nDownload Complete !!!\n");
            br.close();
            fw.close();

            /* update the most recent downloaded version */
            fw = new FileWriter("defy.txt");
            fw.write(current);
            fw.close();
        }
    }
}
