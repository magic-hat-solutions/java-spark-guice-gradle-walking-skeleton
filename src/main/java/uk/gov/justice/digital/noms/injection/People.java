package uk.gov.justice.digital.noms.injection;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uk.gov.justice.digital.noms.interfaces.Service;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class People implements Service {

//TODO: Factor setcookie etc

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private String cookie;

    @Override
    public Map<String, Object> findPerson(final String forename, final String surname) {

        try {

            final HttpResponse<String> homePage = Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/homepage.jsp").basicAuth("nick.talbot", "Password1!").asString();

            String setCookie = homePage.getHeaders().getFirst("Set-Cookie");

            if (setCookie != null) {
                cookie = setCookie.split(";")[0];
            }

            HttpResponse<String> searchPage = Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/findoffenders.jsp").basicAuth("nick.talbot", "Password1!").asString();

            String searchJToken = "name=\"j_token\" value=\"";
            Integer indexJToken = searchPage.getBody().indexOf(searchJToken) + searchJToken.length();

            final String jToken = searchPage.getBody().substring(indexJToken, searchPage.getBody().indexOf("\"", indexJToken));

            Map<String, Object> fields = new HashMap<String, Object>() {
                {
                    put("AJAXREQUEST", "j_id_id0");
                    put("SearchForm", "SearchForm");
                    put("SearchForm:FirstName", forename);
                    put("SearchForm:MiddleName", "");
                    put("SearchForm:LastName", surname);
                    put("SearchForm:IncludeAlias", "false");
                    put("SearchForm:Gender", "0");
                    put("SearchForm:ACRN", "");
                    put("SearchForm:PNCNumber", "");
                    put("SearchForm:DateOfBirth", "");
                    put("otherIdentifier", "");
                    put("SearchForm:NotSelected", "");
                    put("SearchForm:CRONumber", "");
                    put("SearchForm:NINumber", "");
                    put("SearchForm:NOMSNumber", "");
                    put("SearchForm:PrisonerNumber", "");
                    put("SearchForm:otherIdentifierItem", "");
                    put("SearchForm:NationalSearch", "false");
                    put("SearchForm:ATrust", "");
                    put("offenderID", "");
                    put("aliasID", "");
                    put("j_token", jToken);
                    put("javax.faces.ViewState", "j_id2");
                    put("SearchForm:searchButton", "SearchForm:searchButton");
                }
            };

            final HttpResponse<String> peoplePage = Unirest.post("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/findoffenders.jsp")
                                                .basicAuth("nick.talbot", "Password1!")
                                                .header("Cookie", cookie)
                                                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").fields(fields).asString();

            fields.remove("AJAXREQUEST");
            fields.remove("SearchForm:NotSelected");
            fields.remove("SearchForm:searchButton");

            // Extract embedded XML inside XML

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(new ByteArrayInputStream(peoplePage.getBody().getBytes(StandardCharsets.UTF_8)));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//span[@id='xml']/text()");

            final String innerXml = expr.evaluate(xmlDoc, XPathConstants.STRING).toString();

            xmlDoc = builder.parse(new ByteArrayInputStream(innerXml.getBytes(StandardCharsets.UTF_8)));
            expr = xpath.compile("//SearchResult");

            NodeList nodes = (NodeList) expr.evaluate(xmlDoc, XPathConstants.NODESET);

            final ArrayList<Map<String, Object>> results = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {

                final Element node = (Element) nodes.item(i);

                fields.put("offenderID", node.getAttribute("offenderId"));
//              fields.put("javax.faces.ViewState", "j_id16");
                fields.put("SearchForm:_idcl", "viewLink");

                final HttpResponse<String> postRes = Unirest.post("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/findoffenders.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie)
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").fields(fields).asString();

                final HttpResponse<String> loadSummaryPage = Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/offendersummary.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie).asString();


                Map<String, Object> auxFields = new HashMap<String, Object>() {
                    {
                        put("SearchForm", "SearchForm");
                        put("SearchForm:Notes", "");
                        put("SearchForm:j_id_id167", "Address History");
                        put("j_token", jToken);
//                        put("javax.faces.ViewState", "j_id58");
                    }
                };

                final HttpResponse<String> postRes2 = Unirest.post("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/offendersummary.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie)
                        .header("Content-Type", "application/x-www-form-urlencoded").fields(auxFields).asString();

                final HttpResponse<String> addressPage = Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/offenderindex/addresslist.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie).asString();
////

                auxFields = new HashMap<String, Object>() {
                    {
                        put("addressListForm", "addressListForm");
                        put("addressListForm:j_id_id97", "Close");
                        put("j_token", jToken);
//                        put("javax.faces.ViewState", "j_id10");
                    }
                };

                Unirest.post("http://spg-dls-310:7001/NDelius-war/delius/JSP/offenderindex/addresslist.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie)
                        .header("Content-Type", "application/x-www-form-urlencoded").fields(auxFields).asString();
/*
                Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/offendersummary.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie).asString();
*/
                auxFields = new HashMap<String, Object>() {
                    {
                        put("SearchForm", "SearchForm");
                        put("SearchForm:Notes", "");
                        put("SearchForm:j_id_id171", "Close");
                        put("j_token", jToken);
//                        put("javax.faces.ViewState", "j_id11");
                    }
                };

                Unirest.post("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/offendersummary.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie)
                        .header("Content-Type", "application/x-www-form-urlencoded").fields(auxFields).asString();
/*
                Unirest.get("http://spg-dls-310:7001/NDelius-war/delius/JSP/search/findoffenders.jsp")
                        .basicAuth("nick.talbot", "Password1!")
                        .header("Cookie", cookie).asString();
*/
                final ArrayList<String> addresses = new ArrayList<>();
                String[] rows = addressPage.getBody().split("<tr><td>");

                for (int j = 1; j < rows.length; j++) {

                    String number = rows[i].split("<span")[0];
                    String remainder = rows[i].split("</span></td><td>")[1].split("</td><td> &nbsp; ")[0].replace("</td><td>", ", ");

                    addresses.add(number + ", " + remainder);
                }

                results.add(new HashMap<String, Object>() {
                    {
                        put("offenderId", node.getAttribute("offenderId"));
                        put("crn", node.getAttribute("crn"));
                        put("firstName", node.getAttribute("firstName"));
                        put("secondName", node.getAttribute("secondName"));
                        put("thirdName", node.getAttribute("thirdName"));
                        put("surname", node.getAttribute("surname"));
                        put("offenderDisplayName", node.getAttribute("offenderDisplayName"));
                        put("dateOfBirth", node.getAttribute("dateOfBirth"));
                        put("gender", node.getAttribute("gender"));
                        put("trust", node.getAttribute("trust"));
                        put("officerForename", node.getAttribute("officerForename"));
                        put("officerSurname", node.getAttribute("officerSurname"));
                        put("officerDisplayName", node.getAttribute("officerDisplayName"));
                        put("current", node.getAttribute("current"));
                        put("addresses", addresses);
                    }
                });

            }

            return new HashMap<String, Object>() {
                {
                    put("results", results);
                }
            };

        } catch (final Exception ex) {

            logger.error("Find Failed", ex);

            return new HashMap<String, Object>() {
                {
                    put("results", new String[0]);
                    put("error", ex.getMessage());
                }
            };
        }
    }
}
