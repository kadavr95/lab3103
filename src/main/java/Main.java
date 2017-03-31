import com.sun.jndi.toolkit.url.Uri;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 31.03.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        HashSet ht = new HashSet(10);
        download(new URI("http://dimini.tk/en/"), ht);
    }

    private static void download(URI link, HashSet<URI> visited) throws IOException {
        CloseableHttpClient clientGlobal = HttpClients.createDefault();
        HttpGet getGlobal = new HttpGet(link);
        try (CloseableHttpResponse respGlobal = clientGlobal.execute(getGlobal)) {
            String htmlGlobal = EntityUtils.toString(respGlobal.getEntity());

            Pattern pGlobal = Pattern.compile("href\\s*=\\s*([^\\s>]+|\"[^\"]*\"|'[^']*')");
            Matcher mGlobal = pGlobal.matcher(htmlGlobal);
            while (mGlobal.find()) {
                String href = mGlobal.group(1);
                if (href.startsWith("\"")) {
                    href = href.substring(1, href.length() - 1);
                }
                URI child = link.resolve(href);
                if ((!visited.contains(child)) && (visited.size() <= 100)&&(!href.contains("javascript"))) {
                    System.out.println(child);
                    visited.add(child);
                    download(child, visited);
                }
            }
        }
        clientGlobal.close();
    }
}
